package solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Collections;
import java.util.Stack;

import logger.Logger;
import dayBase.DayBase;
import input.InputData;
import grid.Grid;
import geomUtil.Coord;

class Dijkstra {
	private int penalty;
	private Grid<Character> map;

	private long lowestScoreRecent;
	private HashMap<List<Long>, ArrayList<List<Long>>> recentCoordSrc;
	private HashSet<List<Long>> recentEndStates;
	public Dijkstra(int turnPenalty, Grid<Character> map) {
		penalty = turnPenalty;
		this.map = map;
	}

	public void pathfind(Coord start, Coord end) {
		// Dijkstra variables
		HashMap<List<Long>, ArrayList<List<Long>>> coordSrc = new HashMap<>();
		HashMap<List<Long>, Long> stateLowestScore = new HashMap<>();
		HashSet<List<Long>> endStates = new HashSet<>();
		long lowestScoreToEnd = Long.MAX_VALUE;
		PriorityQueue<List<Long>> queue = new PriorityQueue<>(new Comparator<List<Long>>() {
			@Override
			public int compare(List<Long> o1, List<Long> o2) {
				// Min Heap for fCost (first index)
				return Long.compare(o1.get(0), o2.get(0));
			}
		});

		// Initialization
		List<Long> initState = List.of(
			0l, // score
			map.compressCoord(start), // coordinate
			map.compressCoord(Coord.withY(1)) // direction
		);
		stateLowestScore.put(initState, 0l);
		queue.add(initState);

		while (queue.size() > 0) {
			// Variable destructuring
			List<Long> currentState = queue.remove();
			Long currentScore = currentState.get(0);
			Coord currentCoord = map.decompressCoord(currentState.get(1));
			Coord direction = map.decompressCoord(currentState.get(2));
			//Logger.debug("Current state: %s", currentState);
			for (int x = 0; x < map.sizeX(); x++) {
				for (int y = 0; y < map.sizeY(); y++) {
					Coord coord = new Coord(x, y);
					if (coord.equals(currentCoord)) {
						Logger.print('X');
					} else {
						Logger.print(map.getVal(coord));
					}
				}
				Logger.print('\n');
			}
			Logger.debug(queue);
			Logger.print('\n');

			if (currentScore > stateLowestScore.getOrDefault(currentState, Long.MAX_VALUE)) {
				continue;
			}

			if (currentCoord.equals(end)) {
				if (currentScore > lowestScoreToEnd) break;
				lowestScoreToEnd = currentScore;
				endStates.add(currentState);
			}

			for (Coord neighbor : currentCoord.getSurroundingCoord(false)) {
				if (map.getVal(neighbor) == '.') {
					Coord facingDirection = neighbor.subtract(currentCoord);
					Coord directionProduct = direction.multiply(facingDirection);
					boolean isRightAngle = directionProduct.x()+directionProduct.y() == 0;

					long neighborScore = currentScore + (isRightAngle ? 1+penalty : 1);
					List<Long> neighborState = List.of(
							neighborScore,
							map.compressCoord(neighbor),
							map.compressCoord(facingDirection)
							);
					if (neighborScore <= stateLowestScore.getOrDefault(
								neighborState,
								Long.MAX_VALUE)) {
						stateLowestScore.put(neighborState, neighborScore);
						coordSrc.putIfAbsent(neighborState, new ArrayList<>());
						coordSrc.get(neighborState).add(currentState);
						queue.add(neighborState);
					}
				}
			}
			//Logger.debug("Queue: %s", queue);
		}

		lowestScoreRecent = lowestScoreToEnd;
		recentCoordSrc = coordSrc;
		recentEndStates = endStates;
	}
	
	public long getRecentLowestScoreToEnd() {
		return lowestScoreRecent;
	}
	public HashMap<List<Long>, ArrayList<List<Long>>> getRecentCoordSrc() {
		return recentCoordSrc;
	}
	public HashSet<List<Long>> getRecentEndStates() {
		return recentEndStates;
	}
}

public class D16RaindeerMaze extends DayBase<Grid<Character>> {
	private Coord startPos;
	private Coord endPos;
	public D16RaindeerMaze(InputData dat) {
		super(dat);
	}

	protected Grid<Character> parseInput(ArrayList<String> dat) {
		int sizeX = dat.size();
		int sizeY = dat.get(0).length();
		Grid<Character> map = new Grid<>(sizeX, sizeY, '.', '!');
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				char cellChar = dat.get(x).charAt(y);
				Coord coord = new Coord(x, y);
				map.setVal(coord, cellChar);
				if (cellChar == 'S') {
					startPos = coord;
				} else if (cellChar == 'E') {
					endPos = coord;
				}
			}
		}
		return map;
	}

	public void part1() {
		Dijkstra searcher = new Dijkstra(1000, data);
		searcher.pathfind(startPos, endPos);
		
		// Score calculation
		Logger.log("Score for map: %d", searcher.getRecentLowestScoreToEnd());
	}
	

	public void part2() {
	}
}
