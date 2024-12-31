package solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import dayBase.DayBase;
import input.InputData;
import logger.Logger;

public class D11PlutonianPebbles extends DayBase<ArrayList<Long>> {
	public D11PlutonianPebbles(InputData dat) {
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

	private List<Long> transformStone(long stone) {
		if (stone == 0) {
			return List.of((long)1);
		} else if (Long.toString(stone).length() % 2 == 0) {
			long stoneLen = (long)Math.log10(stone)+1;
			long stonePow = (long)Math.pow(10, stoneLen/2);
			long firstHalf = Math.floorDiv(stone, stonePow);
			long secondHalf = stone % stonePow;
			return List.of(firstHalf, secondHalf);
		}
		return List.of(stone*2024);
	}

	private long blinkStone(long stone, int count, HashMap<Integer, HashMap<Long, Long>> memo) {
		if (count == 0) {
			return (long)1;
		}
		if (memo.getOrDefault(count, HashMap.newHashMap(0)).containsKey(stone)) return memo.get(count).get(stone);

		long cnt = 0;
		List<Long> blinks = transformStone(stone);
		for (long blinkStone : blinks) {
			cnt += blinkStone(blinkStone, count-1, memo);
		}

		memo.putIfAbsent(count, new HashMap<>());
		memo.get(count).putIfAbsent(stone, cnt);
		return cnt;
	}

	public void part1() {
		HashMap<Integer, HashMap<Long, Long>> memos = new HashMap<>();
		long totalStones = 0;
		for (long stone : data) {
			totalStones += blinkStone(stone, 25, memos);
		}
		Logger.log("Stone counts w 25 blinks: %d", totalStones);
	}

	public void part2() {
		HashMap<Integer, HashMap<Long, Long>> memos = new HashMap<>();
		long totalStones = 0;
		for (long stone : data) {
			totalStones += blinkStone(stone, 75, memos);
		}
		Logger.log("Stone counts w 75 blinks: %d", totalStones);
	}
}
