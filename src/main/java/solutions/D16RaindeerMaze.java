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
	public Dijkstra(int turnPenalty, Grid<Character> map) {
		penalty = turnPenalty;
		this.map = map;
	}

	public HashMap<Coord, ArrayList<Coord>> pathfind(Coord start, Coord end) {
		// Dijkstra variables
		HashMap<Coord, ArrayList<Coord>> coordSrc = new HashMap<>();
		HashMap<List<Double>, Long> stateLowestScore = new HashMap<>();
		HashSet<Coord> visited = new HashSet<>();
		PriorityQueue<List<Double>> queue = new PriorityQueue<>(new Comparator<List<Double>>() {
			@Override
			public int compare(List<Double> o1, List<Double> o2) {
				// Min Heap for fCost (first index)
				return Double.compare(o1.get(0), o2.get(0));
			}
		});

		// Initialization
		queue.add(List.of(
			0d,
			(double)map.compressCoord(start),
			(double)map.compressCoord(Coord.withY(1))
		));

		while (queue.size() > 0) {
			// Variable destructuring
			List<Double> queueElement = queue.remove();
			Coord elementCoord = map.decompressCoord((long)(double)queueElement.get(1));
			Coord direction = map.decompressCoord((long)(double)queueElement.get(2));

			visited.add(elementCoord);
			//if (elementCoord.equals(end)) break;

			for (Coord neighbor : elementCoord.getSurroundingCoord(false)) {
				if (map.getVal(neighbor) == '.') {
					Coord facingDirection = neighbor.subtract(elementCoord);
					Coord directionProduct = direction.multiply(facingDirection);
				} else if (map.getVal(neighbor) == 'E') {
					coordSrc.putIfAbsent(neighbor, new ArrayList<>());
					coordSrc.get(neighbor).add(elementCoord);
					queue.clear();
					break;
				}
			}
		}

		Logger.debug(coordSrc);
		return coordSrc;
	}

	public ArrayList<Coord> buildPath(HashMap<Coord, ArrayList<Coord>> coordSrc, Coord start, Coord end) {
		ArrayList<Coord> returnArray = new ArrayList<>();
		Coord currentCoord = end;
		while (!currentCoord.equals(start)) {
			returnArray.add(currentCoord);
			currentCoord = coordSrc.get(currentCoord).getLast();
		}
		returnArray.add(start);
		Collections.reverse(returnArray);

		return returnArray;
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

	private double heuristic(Coord start, Coord end, int cost) {
		long diffX = start.x()-end.x();
		long diffY = start.y()-end.y();
		double dist = Math.sqrt(diffX*diffX + diffY*diffY);
		return cost*(diffY/dist) + dist - (cost/100)*(diffX/dist);
	}

	private long calculateScore(ArrayList<Coord> path) {
		long score = 0;
		Coord direction = new Coord(0, 1);
		for (int i = 0; i < path.size()-1; i++) {
			Coord current = path.get(i);
			Coord next = path.get(i+1);
			Coord dir = next.subtract(current);
			if (!direction.equals(dir)) {
				direction = dir;
				score += 1001;
			} else {
				score += 1;
			}
		}
		return score;
	}

	public void part1() {
		int turnCost = 1000;
		AStar searcher = new AStar((s, e) -> heuristic(s, e, turnCost), turnCost, data);
		ArrayList<Coord> path = searcher.buildPath(searcher.pathfind(startPos, endPos), startPos, endPos);
		
		// Score calculation
		Logger.log("Score for map: %d", calculateScore(path));
	}
	

	public void part2() {
	}
}
