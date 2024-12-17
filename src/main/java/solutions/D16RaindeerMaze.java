package solutions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Comparator;

import logger.Logger;
import dayBase.DayBase;
import input.InputData;
import grid.Grid;
import geomUtil.Coord;

interface HeuristicInterface {
	public double run(Coord c1, Coord c2);
}

class AStar {
	private HeuristicInterface h;
	private int penalty;
	private Grid<Character> map;
	public AStar(HeuristicInterface heuristic, int turnPenalty, Grid<Character> map) {
		h = heuristic;
		penalty = turnPenalty;
		this.map = map;
	}

	public ArrayList<Coord> pathfind(Coord start, Coord end) {
		// AStar variables
		HashMap<Coord, Coord> coordSrc = new HashMap<>();
		HashMap<Coord, Integer> gCost = new HashMap<>();

		PriorityQueue<Double[]> queue = new PriorityQueue<>(new Comparator<Double[]>() {
			@Override
			public int compare(Double[] o1, Double[] o2) {
				// Min Heap for fCost (first index)
				return Double.compare(o1[0], o2[0]);
			}
		});

		// Initialization
		coordSrc.put(start, start);
		gCost.put(start, 0);
		queue.add(new Double[]{(double)map.compressCoord(start), h.run(start, end), 0d});

		while (queue.size() > 0) {
			Double[] queueElement = queue.remove();
			Coord elementCoord = map.decompressCoord((long)(double)queueElement[1]);
			if (elementCoord.equals(end)) break;

			for (Coord neighbor : elementCoord.getSurroundingCoord(false)) {
				if (map.getVal(neighbor) == '.') {
				}
			}
		}


		// Build the array of coord used to take from start to end
		ArrayList<Coord> returnArray = new ArrayList<>();

		return returnArray;
	}
}

public class D16RaindeerMaze extends DayBase<Grid<Character>> {
	private Coord startPos;
	private Coord endPos;
>>>>>>> bbeaa98 (Day 16 AStar)
	public D16RaindeerMaze(InputData dat) {
		super(dat);
	}

	protected Grid<Character> parseInput(ArrayList<String> dat) {
<<<<<<< HEAD
		return new Grid<Character>(1, 1, '.', '!');		
=======
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
		map.printCurrentState();
		Logger.debug(startPos);
		Logger.debug(endPos);
		return map;
>>>>>>> bbeaa98 (Day 16 AStar)
	}

	public void part1() {}
	public void part2() {}
}
