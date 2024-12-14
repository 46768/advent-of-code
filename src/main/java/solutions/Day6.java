package solutions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

import dayBase.DayBase;
import logger.Logger;
import input.InputData;

class Common {
	public static char getChar(ArrayList<String> matrix, int x, int y) {
		return matrix.get(x).charAt(y);
	}
}

class Guard {
	private ArrayList<String> guardMap;
	// Guard's map's size
	public int sx, sy;
	// Guard's position
	public int x, y;
	// Direction
	public int dx = -1, dy = 0;
	// Temporary Obstacle
	private int tox, toy;
	public HashMap<String, HashSet<String>> visited = new HashMap<>();

	public Guard(ArrayList<String> map, int tempObstacleX, int tempObstacleY) {
		guardMap = map;
		sx = map.size();
		sy = map.get(0).length();
		tox = tempObstacleX;
		toy = tempObstacleY;
main:
		for (int i = 0; i < sx; i++) {
			for (int j = 0; j < sy; j++) {
				x = i;
				y = j;
				// '^' is the guard's initial position
				if (Common.getChar(map, i, j) == '^') {
					break main;
				}
			}
		}
	}

	private boolean positionInbound(int ix, int iy) {
		if (0 > ix || ix >= sx) return false;
		if (0 > iy || iy >= sy) return false;
		return true;
	}

	private char getValAtPos(int ix, int iy) {
		if (!positionInbound(ix, iy)) return '!';	
		if (ix == tox && iy == toy) return '#';
		return guardMap.get(ix).charAt(iy);
	}

	private String formatCoord(int ix, int iy) {
		return String.format("%d,%d", ix, iy);
	}

	public boolean walk() {
		String mapIdx = formatCoord(x, y);		
		String direction = formatCoord(dx, dy);
		visited.putIfAbsent(mapIdx, new HashSet<>());
		if (visited.get(mapIdx).contains(direction)) {
			throw new RuntimeException("Infinite Loop");
		}
		visited.get(mapIdx).add(direction);
		char posVal = getValAtPos(x+dx, y+dy);
		if (posVal == '#') {
			// Swap variable XOR style
			dx = dx ^ dy;
			dy = dx ^ dy;
			dx = dx ^ dy;
			dy *= -1;
		} else if (posVal == '.' || posVal == '^') {
			x += dx;
			y += dy;
		} else return false;
		return true;
	}
}

public class Day6 extends DayBase<ArrayList<String>> {
	public Day6(InputData dat) {
		super(dat);
	}

	protected ArrayList<String> parseInput(ArrayList<String> dat) {
		return dat;
	}

	public void part1() {
		Guard guard = new Guard(data, -1, -1);
		while (guard.walk()) {}
		Logger.log("Total cell guard visited: %d", guard.visited.size());
	}

	public void part2() {
		HashSet<String> obstaclePlacementLocation = new HashSet<>();
		Guard mainGuard = new Guard(data, -1, -1);
		while (mainGuard.walk()) {
			int nextX = mainGuard.x+mainGuard.dx;
			int nextY = mainGuard.y+mainGuard.dy;
			String obstaclePositionString = String.format("%d,%d", nextX, nextY);
			if (!obstaclePlacementLocation.contains(obstaclePositionString)) {
				// Run from start
				Guard childGuard = new Guard(data, nextX, nextY);
				try {
					while (childGuard.walk()) {} 
				} catch (RuntimeException e) {
					obstaclePlacementLocation.add(obstaclePositionString);
				}
			}
		}
		Logger.log("Total possible obstacle placement: %d", obstaclePlacementLocation.size());
	}
}
