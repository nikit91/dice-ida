(ns librarian.scraper.postprocessor
  "Implementation of the validation and postprocessing of scraped information."
  (:require [datascript.core :as d]
            [clojure.spec.alpha :as s]
            [librarian.helpers.spec :as hs]
            [librarian.helpers.transaction :as tx]
            [librarian.helpers.transients :refer [into!]]
            [clojure.tools.logging :as log]))

(defn retract-tempids
  "Takes a database and a collection of ids and returns a transaction retracting the tempid attributes for the given id."
  [db ids]
  (mapv #(vector :db.fn/retractAttribute % :tempid) ids))

(defn complete-ids
  "Takes a collection of ids and returns a transaction removing the :allow-incomplete flag from the corresponding entities."
  [ids]
  (mapv #(vector :db.fn/retractAttribute % :allow-incomplete) ids))

(defn validate-transaction
  "Takes a database, an ecosystem and a collection of ids.
   Checks whether the given ids correspond to valid entities in the database.
   The validity of an entity is checked by checking whether it satisfies the spec of the ecosystem's concepts it is an instance of.#
   Returns a transaction that removes all entities that were found to be invalid."
  [db {:keys [specs] :as ecosystem} ids]
  (let [{:keys [valid invalid completed]}
        (group-by (fn [id]
                    (let [{:keys [type allow-incomplete] :as e} (d/entity db id)
                          specs (keep specs type)]
                      (if (and (seq specs) (s/valid? (apply hs/and specs) e))
                        (if allow-incomplete :completed :valid)
                        (do
                          (when (seq specs)
                            (log/debug (str "Retract entity " id "!")
                                       type
                                       (s/explain-str (apply hs/and specs) e)))
                          (if allow-incomplete :incomplete :invalid)))))
                  ids)
        tx (complete-ids completed)]
    (if (empty? invalid)
      tx
      (-> (transient tx)
          (into! (map #(vector :db.fn/retractEntity %)) invalid)
          ; revalidate remaining concepts after concept removal
          ; to check whether they are still valid:
          (conj! [:db.fn/call validate-transaction ecosystem (concat valid completed)])
          (persistent!)))))

(defn ecosystem-postprocessing
  "Returns a transaction that applies the postprocessor functions that are applicable to the entities associated to `ids`."
  [db {:keys [attributes postprocessors]} ids id->tid]
  (keep (fn [id]
          (let [types (:type (d/pull db [:type] id))
                procs (->> types
                           (keep postprocessors)
                           (map #(fn [_] (% db id))))]
            (when (seq procs)
              ; Use calls instead of mapcatting all results for lazy evaluation:
              [:db.fn/call (tx/replace-ids attributes id->tid (apply tx/merge procs))])))
        ids))

(defn postprocess-transactions
  "Takes a database, an ecosystem and a collection on newly added unprocessed temporary ids.
   Returns a transaction that applies concept postprocessors to the added entities, performs entity cleanups and checks the validity of the added entities."
  [db ecosystem tids]
  (let [ids (map #(d/entid db [:tempid %]) tids)
        id->tid (zipmap ids tids) ; if tids were unified, the last tid wins
        ids (distinct ids)]
    [[:db.fn/call ecosystem-postprocessing ecosystem ids id->tid]
     [:db.fn/call retract-tempids ids]
     [:db.fn/call validate-transaction ecosystem ids]]))
