package upb.ida.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class ListMapsForFilteringCheck {

	public List<Map<String, String>> generateMapsforTopAsc() {

		List<Map<String, String>> forTopHeightAsc = new ArrayList<>();

		HashMap<String, String> mapE1 = new HashMap<String, String>();
		mapE1.put("name", "andrew");
		mapE1.put("height", "144");
		mapE1.put("weight", "56");
		mapE1.put("sibling", "yes");

		HashMap<String, String> mapE2 = new HashMap<String, String>();
		mapE2.put("name", "jon");
		mapE2.put("height", "145");
		mapE2.put("weight", "12");
		mapE2.put("sibling", "yes");

		HashMap<String, String> mapE3 = new HashMap<String, String>();
		mapE3.put("name", "dave");
		mapE3.put("height", "154");
		mapE3.put("weight", "23");
		mapE3.put("sibling", "no");

		forTopHeightAsc.add(mapE1);
		forTopHeightAsc.add(mapE2);
		forTopHeightAsc.add(mapE3);

		return forTopHeightAsc;
	}

	public List<Map<String, String>> generateMapsforTopDes() {

		List<Map<String, String>> forTopHeightDes = new ArrayList<>();

		HashMap<String, String> mapE3 = new HashMap<String, String>();
		mapE3.put("name", "carsan");
		mapE3.put("height", "167");
		mapE3.put("weight", "78");
		mapE3.put("sibling", "yes");

		HashMap<String, String> mapE2 = new HashMap<String, String>();
		mapE2.put("name", "jordan");
		mapE2.put("height", "166");
		mapE2.put("weight", "10");
		mapE2.put("sibling", "no");

		HashMap<String, String> mapE1 = new HashMap<String, String>();
		mapE1.put("name", "lisa");
		mapE1.put("height", "165");
		mapE1.put("weight", "46");
		mapE1.put("sibling", "yes");

		forTopHeightDes.add(mapE1);
		forTopHeightDes.add(mapE2);
		forTopHeightDes.add(mapE3);

		return forTopHeightDes;

	}

	public List<Map<String, String>> generateMapsforfirstN() {

		List<Map<String, String>> forFirstN = new ArrayList<>();

		HashMap<String, String> mapE3 = new HashMap<String, String>();
		mapE3.put("name", "dave");
		mapE3.put("height", "154");
		mapE3.put("weight", "23");
		mapE3.put("sibling", "no");

		HashMap<String, String> mapE1 = new HashMap<String, String>();
		mapE1.put("name", "lisa");
		mapE1.put("height", "165");
		mapE1.put("weight", "46");
		mapE1.put("sibling", "yes");

		HashMap<String, String> mapE2 = new HashMap<String, String>();
		mapE2.put("name", "andrew");
		mapE2.put("height", "144");
		mapE2.put("weight", "56");
		mapE2.put("sibling", "yes");

		forFirstN.add(mapE1);
		forFirstN.add(mapE2);
		forFirstN.add(mapE3);

		return forFirstN;

	}

	public List<Map<String, String>> generateMapsforLasttN() {

		List<Map<String, String>> forLastN = new ArrayList<>();

		HashMap<String, String> mapE1 = new HashMap<String, String>();
		mapE1.put("name", "jordan");
		mapE1.put("height", "166");
		mapE1.put("weight", "10");
		mapE1.put("sibling", "no");

		HashMap<String, String> mapE2 = new HashMap<String, String>();
		mapE2.put("name", "carsan");
		mapE2.put("height", "167");
		mapE2.put("weight", "78");
		mapE2.put("sibling", "yes");
		
		HashMap<String, String> mapE3 = new HashMap<String, String>();
		mapE3.put("name", "jon");
		mapE3.put("height", "145");
		mapE3.put("weight", "12");
		mapE3.put("sibling", "yes");


		forLastN.add(mapE1);
		forLastN.add(mapE2);
		forLastN.add(mapE3);

		return forLastN;

	}
	 public List<Map<String,String>> generateMapsforFromNTo(){
	
	 List<Map<String, String>> forFromNTo = new ArrayList<>();

	
	 HashMap<String, String> mapE1 = new HashMap<String, String>();
	 mapE1.put("name", "andrew");
	 mapE1.put("height", "144");
	 mapE1.put("weight", "56");
	 mapE1.put("sibling","yes");
	
	 HashMap<String, String> mapE2 = new HashMap<String, String>();
	 mapE2.put("name", "dave");
	 mapE2.put("height","154");
	 mapE2.put("weight", "23");
	 mapE2.put("sibling","no");
	
	 forFromNTo.add(mapE1);
	 forFromNTo.add(mapE2);
	
	 return forFromNTo;
	
	 }
	
	public static boolean compareMaps(Map<String, String> aMap, Map<String, String> bMap) {
		boolean res = true;
		
		int aMapSize = aMap.size();
		int bMapSize = bMap.size();
		res = aMapSize == bMapSize;
		System.out.println(res);


		if (res) {

			Set<String> aKeys = aMap.keySet();
			Set<String> bKeys = bMap.keySet();

			boolean leftCheck = aKeys.containsAll(bKeys);
			boolean rightCheck = bKeys.containsAll(aKeys);
			res = leftCheck && rightCheck;
			System.out.println(res);
		}

		return res;

	}

}
