package day1;

import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;

import fileReader.FileReader;
import dayBase.DayBase;

public class Day1 extends DayBase<ArrayList<ArrayList<Integer>>> {

	public Day1(String path) {
		super(path);
	}

	protected ArrayList<ArrayList<Integer>> parseInput(String path) {
		ArrayList<String> data = FileReader.readData(path);
		ArrayList<ArrayList<Integer>> res = new ArrayList<>();
		res.add(new ArrayList<>());
		res.add(new ArrayList<>());
		for (int idx = 0; idx < data.size(); idx++) {
			String line = data.get(idx);
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
		Collections.sort(data.get(0));
		Collections.sort(data.get(1));
		for (int i = 0; i < data.get(0).size(); i++) {
			int leftVal = data.get(0).get(i);
			int rightVal = data.get(1).get(i);
			sum += Math.abs(leftVal - rightVal);
		}
		System.out.println(String.format("Distance Sum: %d", sum));
	}

	public void part2() {
		ArrayList<Integer> dataLeft = data.get(0);
		ArrayList<HashMap<Integer, Integer>> maps = new ArrayList<>();
		maps.add(new HashMap<>());
		maps.add(new HashMap<>());
		int sum = 0;

		for (int i = 0; i < dataLeft.size(); i++) {
			for (int j = 0; j < 2; j++) {
				ArrayList<Integer> line = data.get(j);
				HashMap<Integer, Integer> map = maps.get(j);
				int val = line.get(i);
				if (!map.containsKey(val)) {
					map.put(val, 1);
				} else {
					int stored = map.get(val);
					map.replace(val, stored+1);
				}
			}
		}

		HashMap<Integer, Integer> leftMap = maps.get(0);
		HashMap<Integer, Integer> rightMap = maps.get(1);
		for (int key : leftMap.keySet()) {
			int rightMatch = rightMap.containsKey(key) ? rightMap.get(key) : 0;
			sum += key * leftMap.get(key) * rightMatch;
		}

		System.out.println(String.format("Similarity Score: %d", sum));
	}
}
