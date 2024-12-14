package solutions;

import java.util.ArrayList;

import dayBase.DayBase;
import logger.Logger;
import input.InputData;

public class Day2 extends DayBase<ArrayList<ArrayList<Integer>>> {
	public Day2(InputData dat) {
		super(dat);
	}

	protected ArrayList<ArrayList<Integer>> parseInput(ArrayList<String> dat) {
		ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();
		for (String line : dat) {
			ArrayList<Integer> report = new ArrayList<Integer>();
			String[] dataSplit = line.split(" ");
			for (String dataCell : dataSplit) {
				report.add(Integer.parseInt(dataCell));
			}
			res.add(report);
		}
		return res;
	}

	// Return problematic index if theres a problem, otherwise return -1
	// Note: index is compared to the index-1
	private static int checkReport(ArrayList<Integer> report) {
		// Initial difference for direction base
		int dif1 = 0;
		for (int i = 1; i < report.size(); i++) {
			int difN = report.get(i) - report.get(i-1);
			if (i == 1) dif1 = difN;

			// Difference is + if increasing, - if decreasing, check if
			// multiplying these 2 give negative which only occur if
			// they have different signs
			if (difN * dif1 <= 0) return i;

			// Problem states difference in level cant be more than 3
			if (Math.abs(difN) > 3) return i;
		}
		return -1;
	}

	public void part1() {
		int safeCount = 0;

		for (ArrayList<Integer> report : data) {
			if (checkReport(report) == -1) {
				safeCount++;
			}
		}

		Logger.log("Report Safe Count: %d", safeCount);
	}

	public void part2() {
		int safeCount = 0;
		ArrayList<ArrayList<Integer>> problematicReport = new ArrayList<ArrayList<Integer>>();

		for (ArrayList<Integer> report : data) {
			if (checkReport(report) == -1) {
				safeCount++;
			} else {
				problematicReport.add(report);
			}
		}

		for (ArrayList<Integer> report : problematicReport) {
			// Bruteforce finding the first index that can be removed to make the
			// report safe
			for (int i = 0; i < report.size(); i++) {
				@SuppressWarnings("unchecked")
				ArrayList<Integer> reportClone = (ArrayList<Integer>)report.clone();
				reportClone.remove(i);
				if (checkReport(reportClone) == -1) {
					safeCount++;
					break;
				}
			}
		}

		Logger.log("Report Safe Count With Problem Dampener: %d", safeCount);
	}
}
