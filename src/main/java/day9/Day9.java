package day9;

import java.util.ArrayList;
import java.util.Collections;

import fileReader.FileReader;
import dayBase.DayBase;
import logger.Logger;

public class Day9 extends DayBase<ArrayList<Integer>> {
	public Day9(String path) {
		super(path);
	}

	protected ArrayList<Integer> parseInput(String path) {
		ArrayList<Integer> result = new ArrayList<>();

		String dat = FileReader.readData(path).get(0);
		boolean isEmpty = false;
		int blockData = 0;
		Logger.log(dat);
		for (int i = 0; i < dat.length(); i++) {
			int blockLength = Integer.parseInt(Character.toString(dat.charAt(i)));
			for (int j = 0; j < blockLength; j++) {
				if (!isEmpty) {
					result.add(blockData);
				} else {
					result.add(-1);
				}
			}
			if (!isEmpty) {
				blockData++;
			}
			isEmpty = !isEmpty;
		}
		return result;
	}

	public void part1() {
		@SuppressWarnings("unchecked")
		ArrayList<Integer> dataClone = (ArrayList<Integer>)(data.clone());
		int ptr1Idx = 0;
		int ptr2Idx = data.size()-1;
		int checksum = 0;

		while (ptr1Idx  < dataClone.size()) {
			if (dataClone.get(ptr1Idx) == -1) {
				Collections.swap(dataClone, ptr1Idx, ptr2Idx);
				Logger.log(ptr1Idx);
				Logger.log(ptr2Idx);
				ptr2Idx--;
				while (dataClone.get(ptr2Idx) != -1) {
					ptr2Idx--;
				}
				Logger.log(dataClone);
				Logger.log("\n");
			}
			ptr1Idx++;
			//checksum += dataClone.get(ptr1Idx);
		}

		Logger.log(dataClone);
		Logger.log("Part 1: %d", checksum);
	}

	public void part2() {

	}
}
