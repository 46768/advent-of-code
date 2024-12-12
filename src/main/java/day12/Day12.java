package day12;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.HashSet;

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

	private ArrayList<Integer> processRegion(Coord position, HashSet<String> visited) {
		ArrayList<Integer> res = new ArrayList<>();
		int area = 0;
		int perimeter = 0;
		char regionChar = data.getVal(position.x(), position.y());

		// BFS Search
		ArrayDeque<Coord> queue = new ArrayDeque<>();
		HashSet<String> localVisited = new HashSet<String>();
		HashSet<String> perimeterVisited = new HashSet<String>();
		queue.add(position);

main:
		while (queue.size() > 0) {
			Logger.log(localVisited);
			Coord pos = queue.pop();
			int x = pos.x();
			int y = pos.y();
			if (!localVisited.contains(pos.toString())) {
				area++;
			}
			localVisited.add(pos.toString());
			// Get surrounding cell and add if its valid
			for (boolean isY : new boolean[]{false, true}) {
				for (int offset : new int[]{-1, 1}) {
					int nx = !isY ? x+offset : x;
					int ny = isY ? y+offset : y;
					Coord nCoord = new Coord(nx, ny);
					char selectedCell = data.getVal(nx, ny);
					if (!localVisited.contains(nCoord.toString())) {
						if (selectedCell == regionChar) {
							queue.add(nCoord);
						} else if (!perimeterVisited.contains(nCoord.toString()+pos.toString())) {
							perimeter++;
							perimeterVisited.add(nCoord.toString()+pos.toString());
						}
					}
				}
			}
		}
		visited.addAll(localVisited);
		res.add(area);
		res.add(perimeter);
		return res;
	}

	public void part1() {
		int result = 0;
		HashSet<String> visitedCell = new HashSet<>();
		for (int x = 0; x < data.sizeX(); x++) {
			for (int y = 0; y < data.sizeY(); y++) {
				Coord searchCoord = new Coord(x, y);
				if (!visitedCell.contains(searchCoord.toString())) {
					ArrayList<Integer> resultRegion = processRegion(searchCoord, visitedCell);
					result += resultRegion.get(0) * resultRegion.get(1);
				}
			}
		}
		Logger.log("Total Region Cost: %d", result);
	}

	public void part2() {

	}
}
