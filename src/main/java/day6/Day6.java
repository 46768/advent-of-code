package day6;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

import dayBase.DayBase;
import fileReader.FileReader;

class Common {
	public static char getChar(ArrayList<String> matrix, int x, int y) {
		return matrix.get(x).charAt(y);
	}
}

class Guard {
	private ArrayList<String> guardMap;
	public int sx, sy;
	public int x, y;
	public int dx = -1, dy = 0;
	private int tox, toy;
	public HashMap<String, HashSet<String>> visited = new HashMap<>();

	public Guard(ArrayList<String> map, int tempObstacleX, int tempObstacleY) {
		guardMap = map;
		sx = map.size();
		sy = map.get(0).length();
		tox = tempObstacleX;
		toy = tempObstacleY;
		boolean br = false;
		for (int i = 0; i < sx; i++) {
			for (int j = 0; j < sy; j++) {
				x = i;
				y = j;
				if (Common.getChar(map, i, j) == '^') {
					br = true;
					break;
				}
			}
			if (br) break;
		}
	}

	private boolean positionInbound(int ax, int ay) {
		if (0 > ax || ax >= sx) return false;
		if (0 > ay || ay >= sy) return false;
		return true;
	}

	private char getValAtPos(int ax, int ay) {
		if (!positionInbound(ax, ay)) return '!';	
		if (ax == tox && ay == toy) return '#';
		return guardMap.get(ax).charAt(ay);
	}

	public boolean walk() {
		String mapIdx = String.format("%d,%d", x, y);		
		visited.putIfAbsent(mapIdx, new HashSet<>());
		if (visited.get(mapIdx).contains(String.format("%d,%d", dx, dy))) {
			throw new RuntimeException("Infinite Loop");
		}
		visited.get(mapIdx).add(String.format("%d,%d", dx, dy));
		char posVal = getValAtPos(x+dx, y+dy);
		if (posVal == '#') {
			dx = dx ^ dy;
			dy = dx ^ dy;
			dx = dx ^ dy;
			dy *= -1;
		} else if (posVal == '.' || posVal == '^') {
			x += dx;
			y += dy;
		} else return false;
		//if (!positionInbound(x, y)) return false;

		return true;
	}
}

public class Day6 extends DayBase<ArrayList<String>> {
	public Day6(String path) {
		super(path);
	}
	
	protected ArrayList<String> parseInput(String path) {
		return FileReader.readData(path);
	}

	public void part1() {
		Guard guard = new Guard(data, -1, -1);
		while (guard.walk()) {}
		System.out.println(String.format("Total cell guard visited: %d", guard.visited.size()));
	}

	public void part2() {
		HashSet<String> obstaclePlacementLocation = new HashSet<>();
		Guard mainGuard = new Guard(data, -1, -1);
		while (mainGuard.walk()) {
			int nextX = mainGuard.x+mainGuard.dx;
			int nextY = mainGuard.y+mainGuard.dy;
			String obstaclePositionString = String.format("%d,%d", nextX, nextY);
			if (!obstaclePlacementLocation.contains(obstaclePositionString)) {
				Guard childGuard = new Guard(data, nextX, nextY);
				try {
					while (childGuard.walk()) {} 
				} catch (RuntimeException e) {
					obstaclePlacementLocation.add(obstaclePositionString);
				}
			}
		}
		System.out.println(String.format("Total possible obstacle placement: %d", obstaclePlacementLocation.size()));
	}
}
