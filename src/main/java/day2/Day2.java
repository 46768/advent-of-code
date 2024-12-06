package day2;

import java.util.ArrayList;

import dayBase.DayBase;
import fileReader.FileReader;

public class Day2 extends DayBase {
	private static ArrayList<ArrayList<Integer>> formatData(ArrayList<String> dat) {
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

	private static ArrayList<ArrayList<Integer>> getData(String path) {
			return formatData(FileReader.readData(path));
	}

	private static int checkReport(ArrayList<Integer> report) {
		int dif1 = 0;
		for (int i = 1; i < report.size(); i++) {
			//all decreasing / increasing check
			//check if initial 2 and the rest are either increasing or decreasing with xor (!=)
			int difN = report.get(i) - report.get(i-1);
			if (i == 1) dif1 = difN;
			if (difN * dif1 <= 0) return i;
			if (Math.abs(difN) > 3) return i;
		}
		return -1;
	}

	public void part1(String path) {
		ArrayList<ArrayList<Integer>> data = getData(path);

		int safeCount = 0;

		for (ArrayList<Integer> report : data) {
			if (checkReport(report) == -1) {
				safeCount++;
			}
		}

		System.out.println(String.format("Report Safe Count: %d", safeCount));
	}

	public void part2(String path) {
		ArrayList<ArrayList<Integer>> data = getData(path);

		int safeCount = 0;
		ArrayList<ArrayList<Integer>> saveableReport = new ArrayList<ArrayList<Integer>>();

		for (ArrayList<Integer> report : data) {
			if (checkReport(report) == -1) {
				safeCount++;
			} else {
				saveableReport.add(report);
			}
		}

		for (ArrayList<Integer> report : saveableReport) {
			if (checkReport(report) >= 0) {
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
		}

		System.out.println(String.format("Report Safe Count With Problem Dampener: %d", safeCount));
	}
}
