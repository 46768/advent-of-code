package day4;

import java.util.ArrayList;

import dayBase.DayBase;
import logger.Logger;
import input.InputData;

public class Day4 extends DayBase<ArrayList<String>> {
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

	private char getChar(int x, int y) {
		return data.get(x).charAt(y);
	}

	public Day4(InputData dat) {
		super(dat);
	}

	protected ArrayList<String> parseInput(ArrayList<String> dat) {
		return dat;
	}

	public void part1() {
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
						if (getChar(row, col) != xMas.charAt(k)) {
							isXmas = false;
							break;
						};
					}
					if (isXmas) count++;
				}
			}
		}

		Logger.log("XMAS Count: %d", count);
	}

	private boolean checkPair(char[] pair, char checkChar) {
		return pair[0] == checkChar && pair[1] == checkChar;
	}

	public void part2() {
		int lineCnt = data.size();
		int strCnt = data.get(0).length();

		int count = 0;

		for (int i = 1; i < lineCnt-1; i++) {
			for (int j = 1; j < strCnt-1; j++) {
				if (getChar(i, j) != 'A') continue;
				// 0 --------> y
				// | 1[1] . 2[1]
				// v .   A   .
				// x 2[0] . 1[0]
				char[] pair1 = {getChar(i+1, j+1), getChar(i-1, j-1)};
				char[] pair2 = {getChar(i+1, j-1), getChar(i-1, j+1)};

				// We dont want X or A in our X-Mas, Only M and S
				checkPair(pair1, 'X');
				checkPair(pair2, 'X');
				checkPair(pair1, 'A');
				checkPair(pair2, 'A');

				// Skip MAM and SAS
				if (pair1[0] == pair1[1]) {
					continue;
				}
				if (pair2[0] == pair2[1]) {
					continue;
				}
				count++;
			}
		}

		Logger.log("X-MAS Count: %d", count);
	}
}
