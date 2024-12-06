package day5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collections;

import dayBase.DayBase;
import fileReader.FileReader;

public class Day5 extends DayBase {
	private static ArrayList<ArrayList<String>> formatData(ArrayList<String> data) {
		ArrayList<String> pageOrderingRule = new ArrayList<String>();
		ArrayList<String> pagePrintOrder = new ArrayList<String>();
		
		ArrayList<ArrayList<String>> formatted = new ArrayList<ArrayList<String>>();

		boolean isPageRule = true;

		for (String line : data) {
			if (line.equals("")) {
				isPageRule = false;
				continue;
			}
			if (isPageRule) {
				pageOrderingRule.add(line);
			} else {
				pagePrintOrder.add(line);
			}
		}

		formatted.add(pageOrderingRule);
		formatted.add(pagePrintOrder);
		return formatted;
	}

	private static HashMap<Integer, HashSet<Integer>> formatRule(ArrayList<String> rules, boolean reversed) {
		HashMap<Integer, HashSet<Integer>> ruleHashMap = new HashMap<>();
		int keyIdx = reversed ? 1 : 0;
		int valueIdx = reversed ? 0 : 1;
		for (String rule : rules) {
			String[] ruleComponent = rule.split("\\|");
			int key = Integer.parseInt(ruleComponent[keyIdx]);
			int value = Integer.parseInt(ruleComponent[valueIdx]);
			ruleHashMap.putIfAbsent(key, new HashSet<>());
			HashSet<Integer> valueSet = ruleHashMap.get(key);
			valueSet.add(value);
		}
		return ruleHashMap;
	}
	private static ArrayList<ArrayList<Integer>> formatPrintOrder(ArrayList<String> printOrder) {
		ArrayList<ArrayList<Integer>> printOrderList = new ArrayList<>();

		for (String order : printOrder) {
			ArrayList<Integer> orderSet = new ArrayList<>();
			String[] pages = order.split(",");
			for (String page : pages) {
				orderSet.add(Integer.parseInt(page));
			}
			printOrderList.add(orderSet);
		}

		return printOrderList;
	}

	private static boolean checkOrder(HashMap<Integer, HashSet<Integer>> rules, ArrayList<Integer> order) {
			// get closer from hashmap, put the openers in here as blacklist as it will break the rule if read
			HashSet<Integer> deathSet = new HashSet<>();
			
			for (Integer page : order) {
				if (deathSet.contains(page)) {
					return false;
				}
				if (rules.containsKey(page)) {
					HashSet<Integer> openers = rules.get(page);
					deathSet.addAll(openers);
				}
			}
			return true;
	}

	private static int getMiddle(ArrayList<Integer> arr) {
		return arr.get((int)(Math.ceil(arr.size()/2)));
	}

	public void part1(String path) {
		ArrayList<ArrayList<String>> data = formatData(FileReader.readData(path));
		// get hashmap with closer as key and openers as hashset value
		HashMap<Integer, HashSet<Integer>> printRule = formatRule(data.get(0), true);
		ArrayList<ArrayList<Integer>> printOrder = formatPrintOrder(data.get(1));

		int middleSum = 0;

		for (ArrayList<Integer> order : printOrder) {
			if (checkOrder(printRule, order)) {
				middleSum += getMiddle(order);
			}
		}

		System.out.println(String.format("Sum of middle page number of valid order: %d", middleSum));
	}

	public void part2(String path) {
		ArrayList<ArrayList<String>> data = formatData(FileReader.readData(path));
		// get hashmap with closer as key and openers as hashset value
		HashMap<Integer, HashSet<Integer>> printRule = formatRule(data.get(0), true);
		ArrayList<ArrayList<Integer>> printOrder = formatPrintOrder(data.get(1));
		ArrayList<ArrayList<Integer>> invalidOrder = new ArrayList<>();

		int middleSum = 0;


		for (ArrayList<Integer> order : printOrder) {
			if (!checkOrder(printRule, order)) {
				invalidOrder.add(order);
			}
		}

		for (int i = 0; i < invalidOrder.size();) {
			ArrayList<Integer> order = invalidOrder.get(i);
			// get closer from hashmap, put the openers in here as blacklist as it will break the rule if read
			HashSet<Integer> deathSet = new HashSet<>();
			HashMap<Integer, ArrayList<Integer>> killerMap = new HashMap<>();
			boolean cont = true;
			
			for (int j = 0; j < order.size(); j++) {
				int page = order.get(j);
				if (deathSet.contains(page)) {
					int killerIdx = killerMap.get(page).get(killerMap.get(page).size()-1);
					Collections.swap(order, killerIdx, j);
					cont = false;
					break;
				}
				if (printRule.containsKey(page)) {
					HashSet<Integer> openers = printRule.get(page);
					Iterator<Integer> openersIter = openers.iterator();
					deathSet.addAll(openers);
					while (openersIter.hasNext()) {
						int target = openersIter.next();
						killerMap.putIfAbsent(target, new ArrayList<>()); 
						killerMap.get(target).add(j);
					}
				}
			}
			if (cont) {
				middleSum += getMiddle(order);
				i++;
			};
		}
		System.out.println(String.format("Sum Of Middle Of Fixed Order: %d", middleSum));	
	}
}
