package solutions;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dayBase.DayBase;
import input.InputData;
import geomUtil.Coord;
import logger.Logger;

public class Day13 extends DayBase<ArrayList<ArrayList<Coord>>> {
	public Day13(InputData dat) {
		super(dat);
	}

	protected ArrayList<ArrayList<Coord>> parseInput(ArrayList<String> dat) {
		ArrayList<ArrayList<Coord>> machines = new ArrayList<>();
		String vectorRegexString = "\\d+";
		Pattern vectorRegex = Pattern.compile(vectorRegexString, Pattern.CASE_INSENSITIVE);


		int machineIdx = 0;
		for (String line : dat) {
			if (machines.size() == machineIdx) {
				machines.add(new ArrayList<>());
			}
			ArrayList<Coord> machine = machines.get(machineIdx);
			if (line.equals("")) {
				machineIdx++;
				continue;
			}
			Matcher vectorComponentMatch = vectorRegex.matcher(line);
			int x = -1, y = -1;
			while (vectorComponentMatch.find()) {
				String component = vectorComponentMatch.group();
				//Logger.debug("vector component: %s", component);
				if (x == -1) {
					x = Integer.parseInt(component);
				} else {
					y = Integer.parseInt(component);
				}
			}
			machine.add(new Coord(x, y));
		}
		//Logger.debug(machines);

		return machines;
	}

	private long[] getCramers(Coord iHat, Coord jHat, Coord target) {
			long tjDet = target.x()*jHat.y() - target.y()*jHat.x();
			long itDet = iHat.x()*target.y() - iHat.y()*target.x();
			long ijDet = iHat.x()*jHat.y() - iHat.y()*jHat.x();
			long aPress = tjDet/ijDet;
			long bPress = itDet/ijDet;
			return new long[]{aPress, bPress};
	}

	private Coord getVector(Coord iHat, Coord jHat, long iCof, long jCof) {
		Coord iVector = Coord.Multiply(iHat, Coord.expand(iCof));
		Coord jVector = Coord.Multiply(jHat, Coord.expand(jCof));
		return Coord.Add(iVector, jVector);
	}

	public void part1() {
		int tokenUsed = 0;
		for (ArrayList<Coord> machine : data) {
			Coord iHat = machine.get(0);
			Coord jHat = machine.get(1);
			Coord target = machine.get(2);

			long[] cramerResult = getCramers(iHat, jHat, target);
			long aPress = cramerResult[0];
			long bPress = cramerResult[1];
			if ((0 <= aPress && aPress <= 100) && (0 <= bPress && bPress <= 100) && getVector(iHat, jHat, aPress, bPress).equals(target)) {
				tokenUsed += (3*aPress)+bPress;
			}
		}
		Logger.log("Token used: %d", tokenUsed);
	}

	public void part2() {
		long tokenUsed = 0;
		for (ArrayList<Coord> machine : data) {
			Coord iHat = machine.get(0);
			Coord jHat = machine.get(1);
			Coord target = machine.get(2);
			target.add(Coord.expand(10000000000000L));

			long[] cramerResult = getCramers(iHat, jHat, target);
			long aPress = cramerResult[0];
			long bPress = cramerResult[1];
			if (getVector(iHat, jHat, aPress, bPress).equals(target)) {
				long tokenTook = (3*aPress)+bPress;
				tokenUsed += tokenTook;
			}
		}
		Logger.log("Token used with Fixed unit: %d", tokenUsed);
	}
}
