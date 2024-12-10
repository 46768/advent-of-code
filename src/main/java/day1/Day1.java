package day1;

import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;

import dayBase.DayBase;
import logger.Logger;
import input.InputData;

public class Day1 extends DayBase<ArrayList<ArrayList<Integer>>> {

	public Day1(InputData dat) {
		super(dat);
	}

	protected ArrayList<ArrayList<Integer>> parseInput(ArrayList<String> dat) {
		ArrayList<ArrayList<Integer>> res = new ArrayList<>();

		// Left Side
		res.add(new ArrayList<>());
		// Right Side
		res.add(new ArrayList<>());
		for (int idx = 0; idx < dat.size(); idx++) {
			String line = dat.get(idx);
			String[] pairValue = line.split("   ");
			int leftValue = Integer.parseInt(pairValue[0]);
			int rightValue = Integer.parseInt(pairValue[1]);
			res.get(0).add(leftValue);
			res.get(1).add(rightValue);
		}
		return res;
	}

	public void part1() {
		int sum = 0;
		
		// Sort the arrays so we get the lowest of the 2 arrays to have the same index
		// and the second lowest and so on
		Collections.sort(data.get(0));
		Collections.sort(data.get(1));

		// Sum up the absolute distance between pairs
		for (int i = 0; i < data.get(0).size(); i++) {
			int leftVal = data.get(0).get(i);
			int rightVal = data.get(1).get(i);
			sum += Math.abs(leftVal - rightVal);
		}
		Logger.log("Distance Sum: %d", sum);
	}

	// Put data on index dataIdx of data (0 for left, 1 for right) into a hashmap
	private void hashMapData(HashMap<Integer, Integer> hashmap, int dataIdx) {
		for (int i = 0; i < data.get(0).size(); i++) {
			ArrayList<Integer> line = data.get(dataIdx);
			int val = line.get(i);
			if (!hashmap.containsKey(val)) {
				hashmap.put(val, 1);
			} else {
				int stored = hashmap.get(val);
				hashmap.replace(val, stored+1);
			}
		}
	}

	public void part2() {
		// Hashmap containing values from both side of the data
		// array value : count
		HashMap<Integer, Integer> leftMap = new HashMap<>();
		HashMap<Integer, Integer> rightMap = new HashMap<>();

		hashMapData(leftMap, 0);
		hashMapData(rightMap, 1);

		int sum = 0;
		for (int key : leftMap.keySet()) {
			// If rightMap dont contains the key that exists in leftMap then skip since it
			// will be multiplied by 0 anyway
			if (!rightMap.containsKey(key)) continue;
			sum += key * leftMap.get(key) * rightMap.get(key);
		}

		Logger.log("Similarity Score: %d", sum);
	}
}
