package day4;

import java.util.ArrayList;

import dayBase.DayBase;
import fileReader.FileReader;

public class Day4 extends DayBase {
	private static int[][] searchIterator = {
	   //x, y
		{0, 1},
		{1, 1},
		{1, 0},
		{1, -1},

		{0, -1},
		{-1, -1},
		{-1, 0},
		{-1, 1},
	};
	private static String xMas = "XMAS";

	private static char getChar(ArrayList<String> matrix, int x, int y) {
		return matrix.get(x).charAt(y);
	}

	public void part1(String path) {
		ArrayList<String> data = FileReader.readData(path);
		int lineCnt = data.size();
		int strCnt = data.get(0).length();

		int count = 0;

		for (int i = 0; i < lineCnt; i++) {
			for (int j = 0; j < strCnt; j++) {
				if (data.get(i).charAt(j) != 'X') continue;
				for (int[] searchDirection : searchIterator) {
					boolean isXmas = true;
					for (int k = 0; k < 4; k++) {
						int row = i + (k*searchDirection[0]);
						int col = j + (k*searchDirection[1]);
						if (row >= lineCnt || col >= strCnt || row < 0 || col < 0) {
							isXmas = false;
							break;
						}
						if (getChar(data, row, col) != xMas.charAt(k)) {
							isXmas = false;
							break;
						};
					}
					if (isXmas) count++;
				}
			}
		}

		System.out.println(String.format("XMAS Count: %d", count));
	}

	public void part2(String path) {
		ArrayList<String> data = FileReader.readData(path);
		int lineCnt = data.size();
		int strCnt = data.get(0).length();

		int count = 0;

		for (int i = 1; i < lineCnt-1; i++) {
			for (int j = 1; j < strCnt-1; j++) {
				if (data.get(i).charAt(j) != 'A') continue;
				char[] pair1 = {getChar(data, i+1, j+1), getChar(data, i-1, j-1)};
				char[] pair2 = {getChar(data, i+1, j-1), getChar(data, i-1, j+1)};


				if (pair1[0] == 'X') {
					continue;
				}
				if (pair2[0] == 'X') {
					continue;
				}
				if (pair1[1] == 'X') {
					continue;
				}
				if (pair2[1] == 'X') {
					continue;
				}
				if (pair1[0] == 'A') {
					continue;
				}
				if (pair2[0] == 'A') {
					continue;
				}
				if (pair1[1] == 'A') {
					continue;
				}
				if (pair2[1] == 'A') {
					continue;
				}

				if (pair1[0] == pair1[1]) {
					continue;
				}
				if (pair2[0] == pair2[1]) {
					continue;
				}
				count++;
			}
		}

		System.out.println(String.format("X-MAS Count: %d", count));
	}
}
