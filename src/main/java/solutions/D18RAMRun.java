package solutions;

import java.util.*;

import dayBase.DayBase;
import logger.Logger;
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
		queue.add(new Double[]{
			h.run(start, end),
				(double)map.compressCoord(start),
				(double)map.compressCoord(Coord.withY(1))
		});

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
					Long neighborGCost = ((directionProduct.x() + directionProduct.y()) == 0 ?
							1+penalty : 1
							)+currentGCost;

					if (neighborGCost < gCost.getOrDefault(neighbor, Long.MAX_VALUE) &&
							!visited.contains(neighbor)) {
						coordSrc.put(neighbor, elementCoord);
						gCost.put(neighbor, neighborGCost);
						queue.add(new Double[]{
							(double)neighborGCost+h.run(neighbor, end),
								(double)map.compressCoord(neighbor),
								(double)map.compressCoord(facingDirection)
						});
							}
				} else if (neighbor.equals(end)) {
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

public class D18RAMRun extends DayBase<ArrayList<Coord>> {
	private int sizeX;
	private int sizeY;
	public D18RAMRun(InputData dat) {
		super(dat);
	}

	protected ArrayList<Coord> parseInput(ArrayList<String> dat) {
		sizeX = 71;
		sizeY = 71;
		ArrayList<Coord> byteList = new ArrayList<>();
		for (String line : dat) {
			String[] coordSet = line.split(",");
			byteList.add(new Coord(Long.parseLong(coordSet[0]), Long.parseLong(coordSet[1])));
		}

		return byteList;
	}

	private Grid<Character> getGridWithBytes(int byteCount) {
		Grid<Character> map = new Grid<>(sizeX, sizeY, '.', '!');
		for (int i = 0; i < byteCount; i++) {
			Coord coord = data.get(i);
			map.setVal(coord, '#');
		}
		return map;
	}

	private double heuristic(Coord start, Coord end) {
		long diffX = start.x()-end.x();
		long diffY = start.y()-end.y();
		return Math.sqrt(diffX*diffX + diffY*diffY);
	}

	public void part1() {
		AStar pathfinder = new AStar((s, e) -> heuristic(s, e), 0, getGridWithBytes(1024));
		ArrayList<Coord> path = pathfinder.pathfind(
				new Coord(0, 0),
				new Coord(sizeX-1, sizeY-1)
				);

		Logger.log("Minimum steps: %d", path.size()-1);
	}

	private void part2Sorter(Coord coord, HashSet<Coord> bl, HashSet<Coord> tr, HashSet<Coord> us) {
		long sByteX = coord.x();
		long sByteY = coord.y();
		boolean isSorted = false;
		if (sByteX == 0 || sByteY == sizeY-1) {
			tr.add(coord);
			isSorted = true;
		}
		if (sByteX == sizeX-1 || sByteY == 0) {
			bl.add(coord);
			isSorted = true;
		}
		if (!isSorted) {
			us.add(coord);
		}
	}

	private void part2Connector(Coord coord, HashSet<Coord> set, HashSet<Coord> falled, HashSet<Coord> us) {
		ArrayList<Coord> queue = new ArrayList<>();
		HashSet<Coord> visited = new HashSet<>();
		int loopLimit = 0;
		int limit = sizeX*sizeY;
		queue.add(coord);

		while (queue.size() > 0 && loopLimit < limit) {
			Coord extracted = queue.removeFirst();
			visited.add(extracted);
			us.remove(extracted);
			set.add(extracted);
			for (Coord surrounding : extracted.getSurroundingCoord(true)) {
				if (falled.contains(surrounding) && !visited.contains(surrounding)) {
					queue.add(surrounding);
				}
			}
			loopLimit++;
		}
	}

	public void part2() {
		HashSet<Coord> falled = new HashSet<>();
		HashSet<Coord> bottomLeft = new HashSet<>();
		HashSet<Coord> topRight = new HashSet<>();
		HashSet<Coord> unsorted = new HashSet<>();

		int byteIndex = 0;
		for (;byteIndex < data.size(); byteIndex++) {
			Coord byteCoord = data.get(byteIndex);

			part2Sorter(byteCoord, bottomLeft, topRight, unsorted);
			if (topRight.contains(byteCoord)) {
				part2Connector(byteCoord, topRight, falled, unsorted);
			}
			if (bottomLeft.contains(byteCoord)) {
				part2Connector(byteCoord, bottomLeft, falled, unsorted);
			}
			falled.add(byteCoord);

			boolean isTRed = false;
			boolean isBLed = false;
			for (Coord surrounding : byteCoord.getSurroundingCoord(true)) {
				if (bottomLeft.contains(surrounding) && !isBLed) {
					part2Connector(byteCoord, bottomLeft, falled, unsorted);
					isBLed = true;
				}
				if (topRight.contains(surrounding) && !isTRed) {
					part2Connector(byteCoord, topRight, falled, unsorted);
					isTRed = true;
				}
			}

			if (bottomLeft.contains(byteCoord) && topRight.contains(byteCoord)) {
				break;
			}
			Logger.debug("At : %d", byteIndex);
		}
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				Coord coord = new Coord(x, y);
				if (bottomLeft.contains(coord) && topRight.contains(coord)) {
					Logger.print('!');
				} else if (bottomLeft.contains(coord)) {
					Logger.print('B');
				} else if (topRight.contains(coord)) {
					Logger.print('T');
				} else if (unsorted.contains(coord)) {
					Logger.print('U');
				} else {
					Logger.print('.');
				}
			}
			Logger.print('\n');
		}
		Logger.print('\n');

		Logger.log("Coord that cause path to be unreachable: %s", data.get(byteIndex));
	}
}
