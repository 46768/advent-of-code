import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class main {
	static ArrayList<ArrayList<Integer>> formatData(ArrayList<String> dat) {
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

	static ArrayList<ArrayList<Integer>> getData(String path) {
		try {
			File data = new File(path);
			Scanner dataScan = new Scanner(data);
			ArrayList<String> dataArray = new ArrayList<String>();
			while (dataScan.hasNextLine()) {
				String line = dataScan.nextLine();
				dataArray.add(line);
			}
			dataScan.close();
			return formatData(dataArray);
		} catch (FileNotFoundException e) {
			System.out.println("data.txt not found");
			e.printStackTrace();
			return new ArrayList<ArrayList<Integer>>();
		}
	}

	static boolean checkSafety(int level1, int level2, boolean isIncrease) {
		if ((level1 > level2) != isIncrease) {
			return false;
		}
		//distance check
		int distance = Math.abs(level1-level2);
		if (distance > 3) {
			return false;
		}
		return true;
	}

	static int checkReport(ArrayList<Integer> report) {
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

	static void part1(String path) {
		ArrayList<ArrayList<Integer>> data = getData(path);

		int safeCount = 0;

		for (ArrayList<Integer> report : data) {
			if (checkReport(report) == -1) {
				safeCount++;
			}
		}

		System.out.println(String.format("Report Safe Count: %d", safeCount));
	}

	static void part2(String path) {
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

	public static void main(String[] arg) {
		if (arg.length < 1) {
			System.out.println("No Input data given");
			return;
		}
		part1(arg[0]);
		part2(arg[0]);
	}
}
