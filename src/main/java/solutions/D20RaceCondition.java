package solutions;

import java.util.*;

import dayBase.DayBase;
import input.InputData;
import logger.Logger;
import geomUtil.Coord;
import grid.Grid;

public class D20RaceCondition extends DayBase<HashMap<Coord, Long>> {
	private long trackLenght;
	private Grid<Character> debugMap;

	public D20RaceCondition(InputData dat) {
		super(dat);
	}

	protected HashMap<Coord, Long> parseInput(ArrayList<String> dat) {
		// Track length scan
		Coord startpos = null;
		Grid<Character> map = new Grid<Character>(dat.size(), dat.get(0).length(), '#', '!');
		trackLenght = 0;
		for (int x = 0; x < dat.size(); x++) {
			for (int y = 0; y < dat.get(0).length(); y++) {
				char charAtPos = dat.get(x).charAt(y);
				map.setVal(x, y, charAtPos);
				if (charAtPos == '.' || charAtPos == 'E') trackLenght++;
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

		debugMap = map;
		return trackData;
	}

	private long getTrackDistWithOffset(Coord current, Coord offset) {
		return data.getOrDefault(current.add(offset), Long.MAX_VALUE);
	}

	private long getValidCheatWithOffsetArray(Coord[] offsetArray) {
		long validCheatCount = 0;
		for (Coord key : data.keySet()) {
			long keyDist = data.get(key);
			for (Coord offset : offsetArray) {
				Coord cheatStart = offset.divide(new Coord(2, 2));
				long offsetDist = getTrackDistWithOffset(key, offset);
				long isWall = getTrackDistWithOffset(key, cheatStart);
				long cheatDist = Math.abs(offset.x()+offset.y());
				if (offsetDist != Long.MAX_VALUE && isWall == Long.MAX_VALUE) {
					long timeSaved = offsetDist-(keyDist+cheatDist);
					// change 100 to change threshold
					if (timeSaved >= 100) validCheatCount++;
				}
			}
		}
		return validCheatCount;
	}

	public void part1() {
		// Iterate over all track position and get cheat time save
		final Coord[] offsetArray = {
			new Coord(-2, 0),
			new Coord(0, 2),
			new Coord(2, 0),
			new Coord(0, -2),
		};

		long validCheatCount = getValidCheatWithOffsetArray(offsetArray);
		Logger.log("Cheats that will save at least 100 picoseconds: %d", validCheatCount);
	}

	public void part2() {

	}
}
