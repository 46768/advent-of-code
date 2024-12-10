package day3;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dayBase.DayBase;
import logger.Logger;
import input.InputData;

public class Day3 extends DayBase<ArrayList<String>> {
	public Day3(InputData dat) {
		super(dat);
	}

	protected ArrayList<String> parseInput(ArrayList<String> dat) {
			return dat;
	}

	private int parseMul(String code) {
		// Get rid of the operator and parentesis mul(x,y) -> x,y
		String args = code.substring(4, code.length()-1);

		String[] argsSplit = args.split(",");
		return Integer.parseInt(argsSplit[0]) * Integer.parseInt(argsSplit[1]);
	}

	public void part1() {
		// Regex: mul\(\d*,\d*\)
		// mul\(	- find "mul(" literally - 1
		// \d*		- find digits more than 0 character right after [1] - 2
		// ,		- find "," literally after [2] - 3
		// \d*		- find digits more than 0 character right after [3] - 4
		// \)		- find ")" literally after [4] - 5
		String regex = "mul\\(\\d*,\\d*\\)";

		int sum = 0;
		Pattern functionMatch = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

		for (String line : data) {
			Matcher lineMatch = functionMatch.matcher(line);
			while (lineMatch.find()) {
				// Get matching substring that passed the regex
				String match = lineMatch.group();
				sum += parseMul(match);
			}
		}
		Logger.log("Sum Of Mul() FN: %d", sum);
	}

	public void part2() {
		// Regex: (mul\(\d*,\d*\))|(do\(\))|(don't\(\))
		// Group 1:
		//		mul\(		- find "mul(" literally - 1
		//		\d*			- find digits more than 0 character right after [1] - 2
		//		,			- find "," literally after [2] - 3
		//		\d*			- find digits more than 0 character right after [3] - 4
		//		\)			- find ")" literally after [4] - 5
		//	OR
		//	Group 2:
		//		do\(\)		- find "do()" literally - 1
		//	OR
		//	Group 3:
		//		don't\(\)	- find "don't()" literally - 1
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
					sum += parseMul(match);
				}
			}
		}
		Logger.log("Sum Of Mul() With Enable/Disable: %d", sum);
	}
}

