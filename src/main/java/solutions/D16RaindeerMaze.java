package solutions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Collections;

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
		HashMap<Coord, Long> gCost = new HashMap<>();

		HashSet<Coord> visited = new HashSet<>();
		PriorityQueue<Double[]> queue = new PriorityQueue<>(new Comparator<Double[]>() {
			@Override
			public int compare(Double[] o1, Double[] o2) {
				// Min Heap for fCost (first index)
				return Double.compare(o1[0], o2[0]);
			}
		});

		// Initialization
		coordSrc.put(start, start);
		gCost.put(start, 0l);
		queue.add(new Double[]{h.run(start, end), (double)map.compressCoord(start), (double)map.compressCoord(Coord.withY(1))});

		while (queue.size() > 0) {
			Double[] queueElement = queue.remove();
			Coord elementCoord = map.decompressCoord((long)(double)queueElement[1]);
			Coord direction = map.decompressCoord((long)(double)queueElement[2]);
			Long currentGCost = gCost.get(elementCoord);
			visited.add(elementCoord);
			if (elementCoord.equals(end)) break;

			for (Coord neighbor : elementCoord.getSurroundingCoord(false)) {
				if (map.getVal(neighbor) == '.') {
					Coord facingDirection = neighbor.subtract(elementCoord);
					Coord directionProduct = direction.multiply(facingDirection);
					Long neighborGCost = ((directionProduct.x() + directionProduct.y()) == 0 ? 1+penalty : 1)+currentGCost;

					if (neighborGCost < gCost.getOrDefault(neighbor, Long.MAX_VALUE) && !visited.contains(neighbor)) {
						coordSrc.put(neighbor, elementCoord);
						gCost.put(neighbor, neighborGCost);
						queue.add(new Double[]{
							(double)neighborGCost+h.run(neighbor, end),
							(double)map.compressCoord(neighbor),
							(double)map.compressCoord(facingDirection)
						});
					}
				} else if (map.getVal(neighbor) == 'E') {
					coordSrc.put(neighbor, elementCoord);
					queue.clear();
					break;
				}
			}
		}

		// Build the array of coord used to take from start to end
		ArrayList<Coord> returnArray = new ArrayList<>();
		Coord currentCoord = end;
		while (!currentCoord.equals(start)) {
			returnArray.add(currentCoord);
			currentCoord = coordSrc.get(currentCoord);
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
		ArrayList<Coord> path = searcher.pathfind(startPos, endPos);
		
		// debug
		for (int x = 0; x < data.sizeX(); x++) {
			for (int y = 0; y < data.sizeY(); y++) {
				Coord coord = new Coord(x, y);
				if (path.contains(coord)) {
					Logger.print('O');
				} else {
					Logger.print(data.getVal(coord));
				}
			}
			Logger.print('\n');
		}

		// Score calculation
		Logger.log("Score for map: %d", calculateScore(path));
	}
	public void part2() {}
}
