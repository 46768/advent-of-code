package day3;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dayBase.DayBase;
import fileReader.FileReader;

public class Day3 extends DayBase {
	private static ArrayList<String> getData(String path) {
			return FileReader.readData(path);
	}

	public void part1(String path) {
		ArrayList<String> data = getData(path);
		String regex = "mul\\(\\d*,\\d*\\)";

		int sum = 0;
		Pattern functionMatch = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

		for (String line : data) {
			Matcher lineMatch = functionMatch.matcher(line);
			while (lineMatch.find()) {
				String match = lineMatch.group();
				String args = match.substring(4, match.length()-1);
				String[] argsSplit = args.split(",");
				int product = Integer.parseInt(argsSplit[0]) * Integer.parseInt(argsSplit[1]);
				sum += product;
			}
		}
		System.out.println(String.format("Sum Of Mul() FN: %d", sum));
	}

	public void part2(String path) {
		ArrayList<String> data = getData(path);
		String regex = "(mul\\(\\d*,\\d*\\))|(do\\(\\))|(don't\\(\\))";

		int sum = 0;
		boolean enabled = true;

		Pattern functionMatch = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		for (String line : data) {
			Matcher lineMatch = functionMatch.matcher(line);
			while (lineMatch.find()) {
				String match = lineMatch.group();
				if (match.equals("do()")) {
					enabled = true;
				} else if (match.equals("don't()")) {
					enabled = false;
				} else if (enabled) {
					String args = match.substring(4, match.length()-1);
					String[] argsSplit = args.split(",");
					int product = Integer.parseInt(argsSplit[0]) * Integer.parseInt(argsSplit[1]);
					sum += product;
				}
			}
		}
		System.out.println(String.format("Sum Of Mul() With Enable/Disable: %d", sum));
	}
}

