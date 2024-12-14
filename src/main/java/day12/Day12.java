package day12;

import java.util.ArrayList;
import java.util.List;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.HashMap;

import logger.Logger;
import dayBase.DayBase;
import input.InputData;
import grid.Grid;
import geomUtil.Coord;

public class Day12 extends DayBase<Grid<Character>> {
	public Day12(InputData dat) {
		super(dat);	
	}

	protected Grid<Character> parseInput(ArrayList<String> dat) {
		Grid<Character> field = new Grid<>(dat.size(), dat.get(0).length(), '.', '!');

		for (int i = 0; i < field.sizeX(); i++) {
			for (int j = 0; j < field.sizeY(); j++) {
				field.setVal(i, j, dat.get(i).charAt(j));
			}
		}

		return field;
	}

	public void part1() {
		int result = 0;
		HashSet<Coord> visited = new HashSet<>();
		for (int x = 0; x < data.sizeX(); x++) {
			for (int y = 0; y < data.sizeY(); y++) {
				Coord position = new Coord(x, y);
				if (!visited.contains(position)) {
					int area = 0;
					int perimeter = 0;
					char regionChar = data.getVal(position.x(), position.y());

					// BFS Search
					ArrayDeque<Coord> queue = new ArrayDeque<>();
					HashSet<Coord> localVisited = new HashSet<>();
					queue.add(position);

					while (queue.size() > 0) {
						Coord pos = queue.pop();
						if (!localVisited.add(pos)) continue;
						area++;
						// Get surrounding cell and add if its valid
						for (Coord nCoord : pos.getSurroundingCoord(false)) {
							char selectedCell = data.getVal(nCoord);
							if (selectedCell == regionChar && !localVisited.contains(nCoord)) {
								queue.add(nCoord);
							} else if (selectedCell != regionChar) {
								perimeter++;
							}
						}
					}
					visited.addAll(localVisited);
					result += area*perimeter;
				}
			}
		}
		Logger.log("Total Region Cost: %d", result);
	}

	public void part2() {
		int result = 0;
		HashSet<Coord> visited = new HashSet<>();

		for (int x = 0; x < data.sizeX(); x++) {
			for (int y = 0; y < data.sizeY(); y++) {
				Coord position = new Coord(x, y);
				if (!visited.contains(position)) {
					int area = 0;
					char regionChar = data.getVal(position.x(), position.y());
					ArrayList<Coord> verticalEdges = new ArrayList<>();
					ArrayList<Coord> horizontalEdges = new ArrayList<>();

					// BFS Search
					ArrayDeque<Coord> queue = new ArrayDeque<>();
					HashSet<Coord> localVisited = new HashSet<>();
					queue.add(position);

					while (queue.size() > 0) {
						// <Border pos, offset from src>
						Coord pos = queue.pop();
						if (!localVisited.add(pos)) continue;
						area++;
						// Get surrounding cell and add if its valid
						for (Coord nCoord : pos.getSurroundingCoord(false)) {
							char selectedCell = data.getVal(nCoord);
							if (selectedCell == regionChar && !localVisited.contains(nCoord)) {
								queue.add(nCoord);
							} else if (selectedCell != regionChar) {
								Coord offset = Coord.Subtract(nCoord, pos);
								if (offset.y() == 0) {
									verticalEdges.add(new Coord(pos.y(), pos.x()*4+offset.x()));
								} else {
									horizontalEdges.add(new Coord(pos.x(), pos.y()*4+offset.y()));
								}
							}
						}
					}

					// Edge Pruning
					int sides = 0;
					ArrayList<Coord> debugVertical = new ArrayList<>();
					ArrayList<Coord> debugHorizontal = new ArrayList<>();
					for (ArrayList<Coord> edge : List.of(horizontalEdges, verticalEdges)) {
						while (!edge.isEmpty()) {
							Coord edgeBase = edge.removeFirst();
							if (edge.equals(verticalEdges)) {
								debugVertical.add(edgeBase);
							} else {
								debugHorizontal.add(edgeBase);
							}
							long targetOrdinal = edgeBase.x()+1;
							int idx = 0;
							while (idx < edge.size()) {
								Coord edgePos = edge.get(idx);
								if (edgePos.x() == targetOrdinal && edgePos.y() == edgeBase.y()) {
									edge.remove(idx);
									targetOrdinal++;
								} else {
									idx++;
								}
							}
							targetOrdinal = edgeBase.x()-1;
							idx = 0;
							while (idx < edge.size()) {
								Coord edgePos = edge.get(idx);
								if (edgePos.x() == targetOrdinal && edgePos.y() == edgeBase.y()) {
									edge.remove(idx);
									targetOrdinal--;
								} else {
									idx++;
								}
							}
							sides++;
						}

					}
					visited.addAll(localVisited);
					//int edges = verticalEdges.size() + horizontalEdges.size();
					Logger.log("verticalEdges: %s, horizontalEdges: %s", debugVertical, debugHorizontal);
					Logger.debug("Region for %c, Area: %d, Edges: %d", data.getVal(position), area, sides);
					result += area*sides;
				}
			}
		}
		Logger.log("Total Region Cost with bulk discount: %d", result);
	}
}
