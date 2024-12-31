package solutions;

import java.util.*;

import dayBase.DayBase;
import logger.Logger;
import input.InputData;

public class D25CodeChronicle extends DayBase<ArrayList<ArrayList<Integer>>> {
	ArrayList<ArrayList<Integer>> keys;
	public D25CodeChronicle(InputData dat) {
		super(dat);
	}

	protected ArrayList<ArrayList<Integer>> parseInput(ArrayList<String> dat) {
		keys = new ArrayList<>();
		ArrayList<ArrayList<Integer>> locks = new ArrayList<>();

		int[] columnHeight = {0, 0, 0, 0, 0};
		String firstLine = "";
		boolean haveFirstLine = false;
		for (String line : dat) {
			if (!haveFirstLine) {
				haveFirstLine = true;
				firstLine = line;
				continue;
			}
			if (line.equals("")) {
				boolean isKey = firstLine.equals(".....");
				ArrayList<Integer> heights = new ArrayList<>(List.of(
							columnHeight[0] - (isKey ? 1 : 0),
							columnHeight[1] - (isKey ? 1 : 0),
							columnHeight[2] - (isKey ? 1 : 0),
							columnHeight[3] - (isKey ? 1 : 0),
							columnHeight[4] - (isKey ? 1 : 0)
							));
				if (isKey) {
					keys.add(heights);
				} else {
					locks.add(heights);
				}
				columnHeight = new int[]{0, 0, 0, 0, 0};
				haveFirstLine = false;
				continue;
			}
			
			columnHeight[0] += line.charAt(0) == '#' ? 1 : 0;
			columnHeight[1] += line.charAt(1) == '#' ? 1 : 0;
			columnHeight[2] += line.charAt(2) == '#' ? 1 : 0;
			columnHeight[3] += line.charAt(3) == '#' ? 1 : 0;
			columnHeight[4] += line.charAt(4) == '#' ? 1 : 0;
		}
		boolean isKey = firstLine.equals(".....");
		ArrayList<Integer> heights = new ArrayList<>(List.of(
					columnHeight[0] - (isKey ? 1 : 0),
					columnHeight[1] - (isKey ? 1 : 0),
					columnHeight[2] - (isKey ? 1 : 0),
					columnHeight[3] - (isKey ? 1 : 0),
					columnHeight[4] - (isKey ? 1 : 0)
					));
		if (isKey) {
			keys.add(heights);
		} else {
			locks.add(heights);
		}

		return locks;
	}

	protected boolean doesKeyFit(ArrayList<Integer> key, ArrayList<Integer> lock) {
		for (int i = 0; i < 5; i++) {
			if (key.get(i)+lock.get(i) > 5) return false;
		}
		return true;
	}

	public void part1() {
		HashMap<ArrayList<Integer>, HashSet<ArrayList<Integer>>> usedPair = new HashMap<>();

		long pairFit = 0;
		for (ArrayList<Integer> lock : data) {
			for (ArrayList<Integer> key : keys) {
				if (doesKeyFit(key, lock) && !usedPair.getOrDefault(key, new HashSet<>()).contains(lock)) {
					usedPair.putIfAbsent(key, new HashSet<>());
					usedPair.get(key).add(lock);
					pairFit++;
				}
			}
		}

		Logger.log("Pairs that fit: %d", pairFit);
	}
	public void part2() {
		Logger.log("Merry christmas yall");
	}
}
