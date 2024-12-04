import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;

import fileReader.FileReader;

class Data extends ArrayList<ArrayList<Integer>> {};
public class main {
	// Takes in array of lines of data file
	// Output an array containing 2 arrays for left and right data
	static Data formatData(ArrayList<String> dat) {
		Data res = new Data();
		res.add(new ArrayList<Integer>());
		res.add(new ArrayList<Integer>());
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

	static Data getData(String path) {
		Data dataFormatted = formatData(FileReader.readData(path));
		return dataFormatted;
	}

	static void part1(String path) {
		ArrayList<ArrayList<Integer>> data = getData(path);
		int sum = 0;
		Collections.sort(data.get(0));
		Collections.sort(data.get(1));
		for (int i = 0; i < data.get(0).size(); i++) {
			int leftVal = data.get(0).get(i);
			int rightVal = data.get(1).get(i);
			sum += Math.abs(leftVal - rightVal);
		}
		System.out.println(String.format("Part 1: %d", sum));
	}

	static void part2(String path) {
		ArrayList<ArrayList<Integer>> data = getData(path);
		ArrayList<Integer> dataLeft = data.get(0);
		ArrayList<Integer> dataRight = data.get(1);
		HashMap<Integer, Integer> leftMap = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> rightMap = new HashMap<Integer, Integer>();
		int sum = 0;

		for (int i = 0; i < dataLeft.size(); i++) {
			int valLeft = dataLeft.get(i);
			int valRight = dataRight.get(i);
			if (!leftMap.containsKey(valLeft)) {
				leftMap.put(valLeft, 1);
			} else {
				int stored = leftMap.get(valLeft);
				leftMap.replace(valLeft, stored+1);
			}
			if (!rightMap.containsKey(valRight)) {
				rightMap.put(valRight, 1);
			} else {
				int stored = rightMap.get(valRight);
				rightMap.replace(valRight, stored+1);
			}
		}

		for (int key : leftMap.keySet()) {
			int rightMatch = rightMap.containsKey(key) ? rightMap.get(key) : 0;
			sum += key * leftMap.get(key) * rightMatch;
		}

		System.out.println(String.format("Part 2: %d", sum));
	}

	public static void main(String[] arg) {
		if (arg.length < 1) {
			System.out.println("No Input data given");
			return;
		}
		part1(arg[0]);
		part2(arg[0]);
	}
}
