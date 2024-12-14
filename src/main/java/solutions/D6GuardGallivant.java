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
	private Grid<Character> guardMap;
	// Guard's position
	public Coord position;
	public Coord positionPrev;
	// Direction
	public Coord direction;
	public Coord directionPrev;
	public HashMap<Coord, HashSet<Coord>> visited = new HashMap<>();

	public Guard(ArrayList<String> map, Coord tempObstacle, Coord startPos, Coord starDir) {
		guardMap = new Grid<Character>(map.size(), map.get(0).length(), '.', '!');
		for (int i = 0; i < guardMap.sizeX(); i++) {
			for (int j = 0; j < guardMap.sizeY(); j++) {
				guardMap.setVal(i, j, map.get(i).charAt(j));
				// '^' is the guard's initial position
				if (guardMap.getVal(i, j) == '^') {
					guardMap.setVal(i, j, '.');
					position = new Coord(i, j);
					positionPrev = position.clone();
				}
			}
		}
		direction = new Coord(-1, 0);
		directionPrev = new Coord(-1, 0);
		if (!tempObstacle.equals(Coord.NEG_IDENTITY_COORD)) {
			guardMap.setVal(tempObstacle, '#');
		}

		if (!startPos.equals(Coord.NEG_IDENTITY_COORD)) {
			position = startPos;
			positionPrev = startPos.clone();
		}

		if (!starDir.equals(Coord.NEG_IDENTITY_COORD)) {
			direction = starDir;
			directionPrev = starDir.clone();
		}
	}

	public boolean walk() {
		visited.putIfAbsent(position, new HashSet<>());
		if (visited.get(position).contains(direction)) {
			throw new RuntimeException("Infinite Loop");
		}
		visited.get(position).add(direction);
		char posVal = guardMap.getVal(position.add(direction));
		if (posVal == '#') {
			directionPrev = direction.clone();
			direction = direction.swap().set();
			direction = direction.multiply(new Coord(1, -1)).set();
		} else if (posVal == '.' || posVal == '^') {
			positionPrev = position.clone();
			position = position.add(direction).set();
		} else return false;
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
