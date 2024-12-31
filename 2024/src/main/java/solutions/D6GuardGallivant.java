package solutions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

import dayBase.DayBase;
import logger.Logger;
import input.InputData;
import grid.Grid;
import geomUtil.Coord;

class Guard {
	private Grid<Long> guardMap;
	private Grid<Long> guardMapRotated;
	// Guard's position
	public Coord position;
	// Direction
	public Coord direction;
	public HashMap<Coord, HashSet<Coord>> visited = new HashMap<>();

	public Guard(ArrayList<String> map, Coord tempObstacle, Coord startPos, Coord starDir) {
		guardMap = new Grid<Long>(map.size(), map.get(0).length(), 0l, -1l);
		guardMapRotated = new Grid<Long>(map.get(0).length(), map.size(), 0l, -1l);
		for (int i = 0; i < guardMap.sizeX(); i++) {
			for (int j = 0; j < guardMap.sizeY(); j++) {
				// '^' is the guard's initial position
				if (guardMap.getVal(i, j) == '^') {
					position = new Coord(i, j);
				}
			}
		}
		direction = new Coord(-1, 0);
		if (!tempObstacle.equals(Coord.NEG_IDENTITY_COORD)) {
		}

		if (!startPos.equals(Coord.NEG_IDENTITY_COORD)) {
			position = startPos;
		}

		if (!starDir.equals(Coord.NEG_IDENTITY_COORD)) {
			direction = starDir;
		}
	}

	public boolean walk() {
		visited.putIfAbsent(position, new HashSet<>());
		if (visited.get(position).contains(direction)) {
			throw new RuntimeException("Infinite Loop");
		}
		visited.get(position).add(direction);
		return true;
	}
}

public class D6GuardGallivant extends DayBase<ArrayList<String>> {
	public D6GuardGallivant(InputData dat) {
		super(dat);
	}

	protected ArrayList<String> parseInput(ArrayList<String> dat) {
		return dat;
	}

	public void part1() {
		Guard guard = new Guard(data, Coord.NEG_IDENTITY_COORD, Coord.NEG_IDENTITY_COORD, Coord.NEG_IDENTITY_COORD);
		while (guard.walk()) {}
		Logger.log("Total cell guard visited: %d", guard.visited.size());
	}

	public void part2() {
		HashSet<Coord> obstaclePlacementLocation = new HashSet<>();
		int mapX = data.size();
		int mapY = data.get(0).length();
		Guard mainGuard = new Guard(data, Coord.NEG_IDENTITY_COORD, Coord.NEG_IDENTITY_COORD, Coord.NEG_IDENTITY_COORD);
		while (mainGuard.walk()) {
			Coord next = mainGuard.position.add(mainGuard.direction);
			if (!obstaclePlacementLocation.contains(next)) {
				if (next.x() >= mapX || next.y() >= mapY) continue;
				Guard childGuard = new Guard(data, next, Coord.NEG_IDENTITY_COORD, Coord.NEG_IDENTITY_COORD);
				try {
					while (childGuard.walk()) {} 
				} catch (RuntimeException e) {
					obstaclePlacementLocation.add(next);
				}
			}
		}
		Logger.log("Total possible obstacle placement: %d", obstaclePlacementLocation.size());
	}
}
