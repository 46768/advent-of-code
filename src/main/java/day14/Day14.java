package day14;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logger.Logger;
import input.InputData;
import dayBase.DayBase;
import geomUtil.Coord;

public class Day14 extends DayBase<ArrayList<ArrayList<Coord>>> {
	private static final int MAP_WIDTH = 101;
	private static final int MAP_HEIGHT = 103;

	private enum Quadrant {
		TOP_LEFT,
		TOP_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT,
		NONE
	}

	public Day14(InputData dat) {
		super(dat);
	}

	protected ArrayList<ArrayList<Coord>> parseInput(ArrayList<String> dat) {
		String numberRegexString = "-?\\d+";
		Pattern numberRegex = Pattern.compile(numberRegexString, Pattern.CASE_INSENSITIVE);
		ArrayList<ArrayList<Coord>> robots = new ArrayList<>();
		for (String line : dat) {
			ArrayList<Coord> robot = new ArrayList<>();
			for (String robotData : line.split(" ")) {
				int x = Integer.MAX_VALUE, y = Integer.MAX_VALUE;
				Matcher numberMatch = numberRegex.matcher(robotData);
				while (numberMatch.find()) {
					if (x == Integer.MAX_VALUE) {
						x = Integer.parseInt(numberMatch.group());
					} else {
						y = Integer.parseInt(numberMatch.group());
					}
				}
				robot.add(new Coord(x, y));
			}
			robots.add(robot);
		}

		return robots;
	}

	private long unNegativeOrdinate(long ordinate, int size) {
		if (ordinate < 0) {
			return size+ordinate;
		}
		return ordinate;
	}

	private Coord resolveCircularPosition(Coord pos) {
		long moduloedX = pos.x() % MAP_WIDTH;
		long moduloedY = pos.y() % MAP_HEIGHT;
		Coord newCoordinate = new Coord(unNegativeOrdinate(moduloedX, MAP_WIDTH), unNegativeOrdinate(moduloedY, MAP_HEIGHT));
		

		return newCoordinate;
	}

	private Quadrant getQuadrant(Coord pos) {
		int midX = MAP_WIDTH/2;
		int midY = MAP_HEIGHT/2;
		boolean isTop = pos.y() < midY;
		boolean isBottom = pos.y() > midY;
		boolean isLeft = pos.x() < midX;
		boolean isRight = pos.x() > midX;

		if (isTop && isLeft) return Quadrant.TOP_LEFT;
		else if (isTop && isRight) return Quadrant.TOP_RIGHT;
		else if (isBottom && isLeft) return Quadrant.BOTTOM_LEFT;
		else if (isBottom && isRight) return Quadrant.BOTTOM_RIGHT;
		return Quadrant.NONE;
	}

	public void part1() {
		long quadTL = 0; // Top left
		long quadTR = 0; // Top right
		long quadBL = 0; // Bottom left
		long quadBR = 0; // Bottom right

		for (ArrayList<Coord> robot : data) {
			Coord initPos = robot.get(0);
			Coord velocity = robot.get(1);
			Coord futurePos = resolveCircularPosition(Coord.Add(initPos, Coord.Multiply(velocity, Coord.expand(100))));
			switch (getQuadrant(futurePos)) {
				case TOP_LEFT:
					quadTL++;
					break;
				case TOP_RIGHT:
					quadTR++;
					break;
				case BOTTOM_LEFT:
					quadBL++;
					break;
				case BOTTOM_RIGHT:
					quadBR++;
					break;
				case NONE:
					break;
			}
		}

		Logger.log("Safety factor: %d", quadTL*quadBL*quadTR*quadBR);
	}

	public void part2() {
		double averageDensity = 0d;
		int i = 1;
main:
		for (;;i++) {
			HashSet<Coord> robotPos = new HashSet<>();
			double densityMap = 0d;
			for (ArrayList<Coord> robot : data) {
				Coord initPos = robot.get(0);
				Coord velocity = robot.get(1);
				Coord futurePos = resolveCircularPosition(Coord.Add(initPos, Coord.Multiply(velocity, Coord.expand(i))));
				robotPos.add(futurePos);
			}
			for (Coord pos : robotPos) {
				int density = 1;
				for (Coord surroundingPos : pos.getSurroundingCoord(true)) {
					if (robotPos.contains(surroundingPos)) density++;
				}
				if (density == 9) densityMap++;
			}
			averageDensity = averageDensity+densityMap;
			if (densityMap - (averageDensity/i) >= 100) {
				break main;
			}
			if (i > MAP_WIDTH*MAP_HEIGHT) {
				break main;
			}
		}

		Logger.log("Easter egg at %d", i);
	}
}
