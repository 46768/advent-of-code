package solutions;

import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;

import dayBase.DayBase;
import logger.Logger;
import input.InputData;

public class D1HistorianHysteria extends DayBase<ArrayList<ArrayList<Integer>>> {

	public D1HistorianHysteria(InputData dat) {
		super(dat);
	}

	protected ArrayList<ArrayList<Integer>> parseInput(ArrayList<String> dat) {
		ArrayList<ArrayList<Integer>> res = new ArrayList<>();

		for (int i : new int[]{0, 1}) {
			res.add(new ArrayList<>());
			for (int idx = 0; idx < dat.size(); idx++) {
				String line = dat.get(idx);
				String[] pairValue = line.split("   ");
				int value = Integer.parseInt(pairValue[i]);
				res.get(i).add(value);
			}
		}
		return res;
	}

	public void part1() {
		int sum = 0;
		ArrayList<Integer> leftList = data.get(0);
		ArrayList<Integer> rightList = data.get(1);
		
		// Sort the arrays so we get the lowest of the 2 arrays to have the same index
		// and the second lowest and so on
		Collections.sort(leftList);
		Collections.sort(rightList);

		// Sum up the absolute distance between pairs
		for (int i = 0; i < leftList.size(); i++) {
			int leftVal = leftList.get(i);
			int rightVal = rightList.get(i);
			sum += Math.abs(leftVal - rightVal);
		}
		Logger.log("Distance Sum: %d", sum);
	}

	// Put data on index dataIdx of data (0 for left, 1 for right) into a hashmap
	private HashMap<Integer, Integer> hashMapData(int dataIdx) {
		HashMap<Integer, Integer> hashmap = new HashMap<>();
		ArrayList<Integer> line = data.get(dataIdx);
		for (int i = 0; i < line.size(); i++) {
			int val = line.get(i);
			hashmap.put(val, hashmap.getOrDefault(val, 0)+1);
		}
		return hashmap;
	}

	public void part2() {
		// Hashmap containing values from both side of the data
		// array value : count
		HashMap<Integer, Integer> leftMap = hashMapData(0);
		HashMap<Integer, Integer> rightMap = hashMapData(1);

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
