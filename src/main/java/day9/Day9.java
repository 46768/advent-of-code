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

	private long getChecksum(ArrayList<Integer> dat) {
		long checksum = 0;
		int checksumPtr = 0;
		for (int val : dat) {
			checksum += Math.max(val, 0)*checksumPtr;
			checksumPtr++;
		}
		return checksum;
	}

	public void part1() {
		@SuppressWarnings("unchecked")
		ArrayList<Integer> dataClone = (ArrayList<Integer>)(data.clone());
		int ptr1Idx = 0;
		int ptr2Idx = data.size()-1;

		while (ptr1Idx < ptr2Idx && ptr1Idx < data.size()) {
			if (dataClone.get(ptr1Idx) == -1) {
				Collections.swap(dataClone, ptr1Idx, ptr2Idx);
				while (dataClone.get(ptr2Idx) == -1) {
					ptr2Idx--;
				}

			}
			ptr1Idx++;
		}
		Logger.log("Part 1: %d", getChecksum(dataClone));
	}

	public void part2() {
		@SuppressWarnings("unchecked")
		ArrayList<Integer> dataClone = (ArrayList<Integer>)(data.clone());
		int ptr1Idx = 0;
		int ptr2Idx = data.size()-1;

		int[] movingFileRange = {-1, -1};

		main: while (ptr1Idx < ptr2Idx && ptr1Idx < data.size()) {
			int blockData = -1;
			while (true) {
				if (ptr2Idx < 0) break main;
				int ptrData = dataClone.get(ptr2Idx);
				if (blockData != ptrData && blockData == -1) {
					blockData = ptrData;
					movingFileRange[1] = ptr2Idx+1;
				} else if (blockData != ptrData && blockData != -1) {
					movingFileRange[0] = ptr2Idx+1;
					break;
				}
				ptr2Idx--;
			}
			int fileSize = movingFileRange[1] - movingFileRange[0];
			int emptySize = 0;

			ptr1Idx = 0;
			while (ptr1Idx < data.size() && dataClone.get(ptr1Idx) != -1) {
				ptr1Idx++;
			}

			// Get Empty block size, if fit then move file there
			while (ptr1Idx < data.size()) {
				int ptrData = dataClone.get(ptr1Idx);
				if (emptySize >= fileSize) {
					for (int i = 0; i < fileSize; i++) {
						Collections.swap(dataClone, ptr1Idx-(fileSize-i), movingFileRange[0]+i);
					}
					break;
				}
				if (ptrData == -1) {
					emptySize++;
				} else emptySize = 0;
				if (ptr1Idx > ptr2Idx) break;
				ptr1Idx++;
			}
			ptr1Idx = 0;

		}
		Logger.log(data.size());
		Logger.log("Part 2: %d", getChecksum(dataClone));
	}
}
