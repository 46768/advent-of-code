package solutions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.ArrayDeque;

import dayBase.DayBase;
import logger.Logger;
import input.InputData;
import grid.Grid;

public class D10HoofIt extends DayBase<Grid<Integer>> {
	protected HashSet<Integer[]> trailheads;

	public D10HoofIt(InputData dat) {
		super(dat);
	}

	protected Grid<Integer> parseInput(ArrayList<String> dat) {
		trailheads = new HashSet<>();
		Grid<Integer> trailMap = new Grid<>(dat.size(), dat.get(0).length(), -1, -1);

		for (int i = 0; i < trailMap.sizeX(); i++) {
			String line = dat.get(i);
			for (int j = 0; j < trailMap.sizeY(); j++) {
				int height = Integer.parseInt(Character.toString(line.charAt(j)));
				trailMap.setVal(i, j, height);
				if (height == 0) {
					Integer[] pos = {i, j};
					trailheads.add(pos);
				}
			}
		}

		return trailMap;
	}
	
	private Integer[][] getSurrounding(Integer[] pos) {
		Integer[][] valRet = {
			{pos[0]-1, pos[1]},
			{pos[0], pos[1]+1},
			{pos[0]+1, pos[1]},
			{pos[0], pos[1]-1},
		};
		return valRet;
	}

	private long convertPos(Integer[] pos) {
		return pos[0]*data.sizeX() + pos[1];
	}

	public void part1() {
		int res = 0;

		for (Integer[] trailhead : trailheads) {
			HashSet<Long> trailEnd = new HashSet<>();

			// BFS
			ArrayDeque<Integer[]> pathSearch = new ArrayDeque<>();			
			HashSet<Long> visited = new HashSet<>();
			pathSearch.add(trailhead);

			while (!pathSearch.isEmpty()) {
				Integer[] popPos = pathSearch.pop();
				int posVal = data.getVal(popPos);
				Integer[][] surroundings = getSurrounding(popPos);
				visited.add(convertPos(popPos));

				if (data.getVal(popPos) == 9) {
					trailEnd.add(convertPos(popPos));
				} else {
					for (Integer[] pos : surroundings) {
						Long posComp = pos[0]*data.sizeX() + pos[1];
						if (data.isInBound(pos) && ((data.getVal(pos)-posVal) == 1) && !visited.contains(posComp)) {
							pathSearch.add(pos);
						}
					}
				}
			}
			res += trailEnd.size();
		}
		Logger.log("part 1: %d", res);
	}

	public void part2() {
		int totalRating = 0;

		for (Integer[] trailhead : trailheads) {
			// BFS
			int rating = 0;
			ArrayDeque<Integer[]> pathSearch = new ArrayDeque<>();			
			HashSet<Long> visited = new HashSet<>();
			pathSearch.add(trailhead);
			while (!pathSearch.isEmpty()) {
				Integer[] popPos = pathSearch.pop();
				int posVal = data.getVal(popPos);
				Integer[][] surroundings = getSurrounding(popPos);
				visited.add(convertPos(popPos));

				if (data.getVal(popPos) == 9) {
					rating++;
				} else {
					for (Integer[] pos : surroundings) {
						Long posComp = pos[0]*data.sizeX() + pos[1];
						if (data.isInBound(pos) && ((data.getVal(pos)-posVal) == 1) && !visited.contains(posComp)) {
							pathSearch.add(pos);
						}
					}
				}
			}
			totalRating += rating;
		}
		Logger.log("part 2: %d", totalRating);
	}
}
