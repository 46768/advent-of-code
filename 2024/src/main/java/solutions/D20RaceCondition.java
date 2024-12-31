package solutions;

import java.util.*;

import dayBase.DayBase;
import input.InputData;
import logger.Logger;
import geomUtil.Coord;
import grid.Grid;

public class D20RaceCondition extends DayBase<HashMap<Coord, Long>> {
	public D20RaceCondition(InputData dat) {
		super(dat);
	}

	protected HashMap<Coord, Long> parseInput(ArrayList<String> dat) {
		// Track length scan
		Coord startpos = null;
		Grid<Character> map = new Grid<Character>(dat.size(), dat.get(0).length(), '#', '!');
		for (int x = 0; x < dat.size(); x++) {
			for (int y = 0; y < dat.get(0).length(); y++) {
				char charAtPos = dat.get(x).charAt(y);
				map.setVal(x, y, charAtPos);
				if (charAtPos == 'S') startpos = new Coord(x, y);
			}
		}

		// Get track data (Coord : dist from start)
		HashMap<Coord, Long> trackData = new HashMap<>();
		if (startpos == null) {
			Logger.error("No start position");
			return trackData;
		}

		ArrayDeque<Coord> queue = new ArrayDeque<>();
		queue.add(startpos);

		long distFromStart = 0;
		while (!queue.isEmpty()) {
			Coord currentPos = queue.pollFirst();
			trackData.put(currentPos, distFromStart);
			distFromStart++;
			for (Coord surrounding : currentPos.getSurroundingCoord(false)) {
				if (map.getVal(surrounding) == 'E') {
					trackData.put(surrounding, distFromStart);
					queue.clear();
					break;
				}
				if (map.getVal(surrounding) == '.' && !trackData.containsKey(surrounding)) {
					queue.add(surrounding);	
				}
			}
		}
		return trackData;
	}

	private long getTrackDistWithOffset(Coord current, Coord offset) {
		return data.getOrDefault(current.add(offset), Long.MAX_VALUE);
	}

	private long getValidCheatWithOffsetArray(ArrayList<Coord> offsetArray) {
		long validCheatCount = 0;
		for (Coord key : data.keySet()) {
			for (Coord offset : offsetArray) {
				long offsetDist = getTrackDistWithOffset(key, offset);
				if (offsetDist != Long.MAX_VALUE) {
					long keyDist = data.get(key);
					long cheatDist = Math.abs(offset.x())+Math.abs(offset.y());
					long timeSaved = offsetDist-(keyDist+cheatDist);

					// change 100 to change threshold
					if (timeSaved >= 100) validCheatCount++;
				}
			}
		}
		return validCheatCount;
	}

	private ArrayList<Coord> generateOffsetArray(int dist) {
		ArrayList<Coord> offsets = new ArrayList<>();

		for (int x = -dist; x <= dist; x++) {
			int yOffset = dist-Math.abs(x);
			for (int y = 0; y < ((dist-Math.abs(x))*2)+1; y++) {
				offsets.add(new Coord(x, y-yOffset));
			}
		}

		// Removes immediate neighbor since its most likely a wall or
		// does not help with time
		for (int x : new int[]{-1, 0, 1}) {
			for (int y : new int[]{-1, 0, 1}) {
				offsets.remove(new Coord(x, y));
			}
		}

		return offsets;
	}

	public void part1() {
		// Iterate over all track position and get cheat time save
		final ArrayList<Coord> offsetArray = generateOffsetArray(2);
		long validCheatCount = getValidCheatWithOffsetArray(offsetArray);
		Logger.log("Cheats that will save at least 100 picoseconds: %d", validCheatCount);
	}

	public void part2() {
		final ArrayList<Coord> offsetArray = generateOffsetArray(20);
		long validCheatCount = getValidCheatWithOffsetArray(offsetArray);
		Logger.log("Cheats that will save at least 100 picoseconds (20 picoseconds cheat): %d", validCheatCount);
	}
}
