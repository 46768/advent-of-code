package day11;

import java.util.ArrayList;

import dayBase.DayBase;
import input.InputData;
import logger.Logger;

public class Day11 extends DayBase<ArrayList<Long>> {
	public Day11(InputData dat) {
		super(dat);
	}

	protected ArrayList<Long> parseInput(ArrayList<String> dat) {
		String wholeData = dat.get(0);
		String[] stonesString = wholeData.split(" ");
		ArrayList<Long> stones = new ArrayList<>();

		for (String stone : stonesString) {
			stones.add(Long.parseLong(stone));
		}

		return stones;
	}

	private ArrayList<Long> transformStone(long stone) {
		ArrayList<Long> transformedStone = new ArrayList<>();
		if (stone == 0) {
			transformedStone.add((long)1);
		} else if (Long.toString(stone).length() % 2 == 0) {
			String stringgedStone = Long.toString(stone);
			String firstHalf = stringgedStone.substring(0, (stringgedStone.length()/2));
			String secondHalf = stringgedStone.substring(stringgedStone.length()/2);
			transformedStone.add(Long.parseLong(firstHalf));
			transformedStone.add(Long.parseLong(secondHalf));
		} else {
			transformedStone.add(stone*2024);
		}

		return transformedStone;
	}

	public void part1() {
		@SuppressWarnings("unchecked")
		ArrayList<Long> dataClone = (ArrayList<Long>)data.clone();

		for (int i = 0; i < 25; i++) {
			ArrayList<Long> newData = new ArrayList<>();
			for (Long stone : dataClone) {
				newData.addAll(transformStone(stone));	
			}
			dataClone = newData;
		}

		Logger.log("Stone counts: %d", dataClone.size());
	}

	public void part2() {
		@SuppressWarnings("unchecked")
		ArrayList<Long> dataClone = (ArrayList<Long>)data.clone();

		for (int i = 0; i < 75; i++) {
			ArrayList<Long> newData = new ArrayList<>();
			for (Long stone : dataClone) {
				newData.addAll(transformStone(stone));	
			}
			dataClone = newData;
		}

		Logger.log("Stone counts: %d", dataClone.size());
	}
}
