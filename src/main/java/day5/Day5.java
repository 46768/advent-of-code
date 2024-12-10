package day5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collections;

import dayBase.DayBase;
import logger.Logger;
import input.InputData;

public class Day5 extends DayBase<ArrayList<ArrayList<String>>> {
	private HashMap<Integer, HashSet<Integer>> printRule;
	private ArrayList<ArrayList<Integer>> printOrder;

	public Day5(InputData dat) {
		super(dat);
	}

	private static HashMap<Integer, HashSet<Integer>> formatRule(ArrayList<String> rules) {
		HashMap<Integer, HashSet<Integer>> ruleHashMap = new HashMap<>();
		int keyIdx = 1;
		int valueIdx = 0;
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

	protected ArrayList<ArrayList<String>> parseInput(ArrayList<String> dat) {
		ArrayList<String> pageOrderingRule = new ArrayList<String>();
		ArrayList<String> pagePrintOrder = new ArrayList<String>();
		
		ArrayList<ArrayList<String>> formatted = new ArrayList<ArrayList<String>>();

		boolean isPageRule = true;

		for (String line : dat) {
			// Input separator between page ordering rule and
			// page print order
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
		this.printRule = formatRule(pageOrderingRule);
		this.printOrder = formatPrintOrder(pagePrintOrder);
		return formatted;
	}


	private boolean checkOrder(ArrayList<Integer> order) {
			// get closer from hashmap, put the openers in here as blacklist as it will break the rule if read
			HashSet<Integer> invalidPage = new HashSet<>();
			
			for (Integer page : order) {
				if (invalidPage.contains(page)) {
					return false;
				}
				if (printRule.containsKey(page)) {
					HashSet<Integer> openers = printRule.get(page);
					invalidPage.addAll(openers);
				}
			}
			return true;
	}

	private static int getMiddle(ArrayList<Integer> arr) {
		return arr.get(Math.ceilDiv(arr.size(), 2));
	}

	public void part1() {
		int middleSum = 0;

		for (ArrayList<Integer> order : printOrder) {
			if (checkOrder(order)) {
				middleSum += getMiddle(order);
			}
		}

		Logger.log("Sum of middle page number of valid order: %d", middleSum);
	}

	public void part2() {
		ArrayList<ArrayList<Integer>> invalidOrder = new ArrayList<>();

		int middleSum = 0;


		for (ArrayList<Integer> order : printOrder) {
			if (!checkOrder(order)) {
				invalidOrder.add(order);
			}
		}

		for (int i = 0; i < invalidOrder.size();) {
			ArrayList<Integer> order = invalidOrder.get(i);

			// Get closer from hashmap, put the openers in here as blacklist as 
			// it will break the rule if read
			HashSet<Integer> invalidPages = new HashSet<>();

			// Stores what page is invalidated by what page's index
			HashMap<Integer, ArrayList<Integer>> invalidatorMap = new HashMap<>();
			boolean count = true;
			
			for (int j = 0; j < order.size(); j++) {
				int page = order.get(j);

				if (invalidPages.contains(page)) {
					// If invalid order is found, swap it with the first
					// invalidator found due to
					// If A -> C, A -> B Topology: [C, B, A]
					//
					// during print rule check invalidators order would be
					// [C, B], swapping with B (last found invalidator) cause
					// [C, A, B] but still cause invalid topology due to A -> C
					//
					// swapping with C cause
					// [A, B, C] which turns the topology valid within 1 swap instead of 2
					int invalidatorIdx = invalidatorMap.get(page).getFirst();
					Collections.swap(order, invalidatorIdx, j);
					count = false;
					break;
				}
				if (printRule.containsKey(page)) {
					HashSet<Integer> openers = printRule.get(page);
					Iterator<Integer> openersIter = openers.iterator();
					invalidPages.addAll(openers);
					while (openersIter.hasNext()) {
						int target = openersIter.next();
						invalidatorMap.putIfAbsent(target, new ArrayList<>()); 
						invalidatorMap.get(target).add(j);
					}
				}
			}

			// Loops until a valid topology is found
			if (count) {
				middleSum += getMiddle(order);
				i++;
			};
		}
		Logger.log("Sum Of Middle Of Fixed Order: %d", middleSum);	
	}
}
