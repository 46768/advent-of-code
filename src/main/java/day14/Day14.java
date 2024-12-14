package day14;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logger.Logger;
import input.InputData;
import dayBase.DayBase;
import geomUtil.Coord;

public class Day14 extends DayBase<ArrayList<ArrayList<Coord>>> {
	private static final int MAP_WIDTH = 101;
	private static final int MAP_HEIGHT = 103;

	private static final double STANDARD_VARIANCE = 0.9;
	private static final double STANDARD_MEAN = 1.2;
	private static final double NORMAL_DISTRIBUTION_COEFFICIENT = 1/Math.sqrt(2*Math.PI*STANDARD_VARIANCE*STANDARD_VARIANCE);
	private static final double NORMAL_DISTRIBUTION_EXPONENT_COEFFICIENT = Math.exp(-1/2*STANDARD_VARIANCE*STANDARD_VARIANCE);

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

	private HashMap<Integer, Double> genDensityMap() {
		HashMap<Integer, Double> densityMap = new HashMap<>();
		densityMap.put(1, 0d);
		densityMap.put(2, 0d);
		densityMap.put(3, 0d);
		densityMap.put(4, 0d);
		densityMap.put(5, 0d);
		densityMap.put(6, 0d);
		densityMap.put(7, 0d);
		densityMap.put(8, 0d);
		densityMap.put(9, 0d);
		return densityMap;
	}

	private double getDensityNormalDistribution(double x) {
		return NORMAL_DISTRIBUTION_COEFFICIENT * Math.pow(
				NORMAL_DISTRIBUTION_EXPONENT_COEFFICIENT, 
				(x - STANDARD_MEAN)*(x - STANDARD_MEAN)
				);
	}

	private double getDensityAnomaly(HashMap<Integer, Double> averageDensity, HashMap<Integer, Double> densityMap) {
		double anomalyScore = 0;
		for (int key : averageDensity.keySet()) {
			double avgDensity = averageDensity.get(key);
			double density = densityMap.get(key);
			double densityDistribution = getDensityNormalDistribution(key);
			anomalyScore += densityDistribution * Math.abs(avgDensity - density);
		}
		return anomalyScore;
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
		HashMap<Integer, Double> averageDensity = genDensityMap();
		int i = 1;
main:
		for (;;i++) {
			HashSet<Coord> robotPos = new HashSet<>();
			HashMap<Integer, Double> densityMap = genDensityMap();
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
				densityMap.replace(density, densityMap.get(density)+1d);
			}
			for (int key : densityMap.keySet()) {
				double avgDensity = averageDensity.get(key);
				double density = densityMap.get(key);
				double newAvgDensity = ((avgDensity*(i-1))+density)/i;
				averageDensity.replace(key, newAvgDensity);
			}
			if (robotPos.size() == data.size() && getDensityAnomaly(averageDensity, densityMap) >= 100) {
				break main;
			}
			if (i > MAP_WIDTH*MAP_HEIGHT) {
				break main;
			}
		}

		Logger.log("Easter egg at %d", i);
	}
}
