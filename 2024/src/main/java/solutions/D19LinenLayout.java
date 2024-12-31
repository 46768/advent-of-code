package solutions;

import java.util.*;

import dayBase.DayBase;
import input.InputData;
import logger.Logger;

public class D19LinenLayout extends DayBase<ArrayList<String>> {
	public D19LinenLayout(InputData dat) {
		super(dat);
	}

	protected ArrayList<String> parseInput(ArrayList<String> dat) {
		return dat;
	}

	private ArrayList<String> getTowelPattern() {
		ArrayList<String> pattern = new ArrayList<>();
		for (String line : data) {
			pattern.addAll(List.of(line.split(", ")));
			if (line.equals("")) break;
		}

		return pattern;
	}

	private ArrayList<String> getDesign() {
		ArrayList<String> designs = new ArrayList<>();
		boolean isDesignSection = false;
		for (String line : data) {
			if (line.equals("")) {
				isDesignSection = true;
				continue;
			}
			if (!isDesignSection) continue;
			designs.add(line);
		}

		return designs;
	}

	private long getArrangementCount(String design, ArrayList<String> pattern) {
		int designLen = design.length();
		long[] memoTable = new long[designLen+1];
		memoTable[0] = 1;

		for (int i = 0; i <= designLen; i++) {
			for (String patt: pattern) {
				int pattLen = patt.length();
				if (i >= pattLen && !patt.isEmpty() && design.substring(i - pattLen, i).equals(patt)) {
					memoTable[i] += memoTable[i - pattLen];
				}
			}
		}

		return memoTable[designLen];
	}

	public void part1() {
		ArrayList<String> patterns = getTowelPattern();
		ArrayList<String> designs = getDesign();

		long validCount = 0;
		for (String design : designs) {
			if (getArrangementCount(design, patterns) > 0) validCount++;
		}

		Logger.log("Valid towel designs: %d", validCount);
	}

	public void part2() {
		ArrayList<String> patterns = getTowelPattern();
		ArrayList<String> designs = getDesign();

		long validCount = 0;
		for (String design : designs) {
			long arragementCount = getArrangementCount(design, patterns);
			validCount += arragementCount;
		}

		Logger.log("Valid towel arragement: %d", validCount);
	}
}
