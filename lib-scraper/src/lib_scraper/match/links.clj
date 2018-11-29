(ns lib-scraper.match.links
  (:require [clojure.string :as s])
  (:import (edu.uci.ics.crawler4j.crawler Page)
           (edu.uci.ics.crawler4j.url WebURL)))

(defn match-url
  [pattern]
  (if (instance? String pattern)
    (fn [_, ^Page page, ^WebURL url]
      (= pattern (.getURL url)))
    (fn [_, ^Page page, ^WebURL url]
      (some? (re-matches pattern (.getURL url))))))

(defn require-classes
  [required-classes]
  (fn [_, ^Page page, ^WebURL url]
    (let [{:strs [class] :or {class ""}} (into {} (.getAttributes url))
          classes (set (map s/lower-case (s/split class #"\s+")))]
      (every? (comp classes s/lower-case) required-classes))))