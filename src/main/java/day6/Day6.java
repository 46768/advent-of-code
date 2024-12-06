package day6;

import java.util.ArrayList;
import java.util.HashSet;
import fileReader.FileReader;

private class Guard {
	static int[][] dirs = {
		{-1, 0},
		{0, 1},
		{1, 0},
		{0, -1},
	};

	ArrayList<String> guardMap;
	int x, y;
	int[] dir = dirs[0];
	HashSet<int[]> visited = new HashSet<>();
	public Guard(ArrayList<String> map, int initX, int initY) {
		guardMap = map;
		main: for (int i = 0; i < map.size(); i++) {
			for (int j = 0; j < map.get(0).length(); j++) {
				if (Day6.getChar(map, i, j) == '^') {
					x = i;
					y = j;
					int[] coord = {i, j};
					visited.add(coord);
					break main;
				}
			}
		}
	}

	public int[] getPos() {
		int[] pos = {x, y};
		return pos;
	}

	public boolean walk() {
	
		return false;
	}
}

public class Day6 {
	public static char getChar(ArrayList<String> matrix, int x, int y) {
		return matrix.get(x).charAt(y);
	}
}
