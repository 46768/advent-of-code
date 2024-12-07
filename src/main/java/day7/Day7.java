package day7;

import java.util.ArrayList;
import java.util.HashSet;
import java.math.BigInteger;

import dayBase.DayBase;
import fileReader.FileReader;

class Calibration {
	long target;
	ArrayList<Long> operand = new ArrayList<>();

	public Calibration(String data) {
		//System.out.println("start construction");
		String[] targetOperandSplit = data.split("\\:");
		//System.out.println("target operand split");
		target = Long.parseUnsignedLong(targetOperandSplit[0]);
		//System.out.println("parsing target to int");
		//System.out.println(targetOperandSplit[1].substring(1));
		String[] operandSplit = targetOperandSplit[1].substring(1).split(" ");
		//System.out.println("splitted operand");
		for (String op : operandSplit) {
			//System.out.println(op);
			operand.add(Long.parseUnsignedLong(op));
		}
		//System.out.println("constructed");
	}

	// takes in operand.size()-1 bits, 0 is +, 1 is *
	public boolean testOperators(int operators) {
		int operatorReversed = (operators);
		long result = operand.get(0);
		for (int i = 1; i < operand.size(); i++) {
			int operator = operatorReversed & 1;
			long currentOperand = operand.get(i);
			if (operator == 0) {
				result += currentOperand;
			} else {
				result *= currentOperand;
			}
			//System.out.println(result);
			operatorReversed >>= 1;
		}
		return result == target;
	}
}

class CalibrationExtended {
	long target;
	ArrayList<Long> operand = new ArrayList<>();

	public CalibrationExtended(String data) {
		//System.out.println("start construction");
		String[] targetOperandSplit = data.split("\\:");
		//System.out.println("target operand split");
		target = Long.parseUnsignedLong(targetOperandSplit[0]);
		//System.out.println("parsing target to int");
		//System.out.println(targetOperandSplit[1].substring(1));
		String[] operandSplit = targetOperandSplit[1].substring(1).split(" ");
		//System.out.println("splitted operand");
		for (String op : operandSplit) {
			//System.out.println(op);
			operand.add(Long.parseUnsignedLong(op));
		}
		//System.out.println("constructed");
	}

	private String padOperators(String operators) {
		String formatString = "%" + (operand.size()-1) + "s";
		String spacePadded = String.format(formatString, operators);
		String result = spacePadded.replace(' ', '0');
		return result;
	}
	// takes in operand.size()-1 bits, 0 is +, 1 is *
	public boolean testOperators(String operators) {
		String paddedOperators = padOperators(operators);
		//System.out.println(paddedOperators);
		long result = operand.get(0);
		for (int i = 1; i < operand.size(); i++) {
			char operator = paddedOperators.charAt(i-1);
			long currentOperand = operand.get(i);
			if (operator == '0') {
				result += currentOperand;
			} else if (operator == '1') {
				result *= currentOperand;
			} else {
				String resString = String.format("%d", result);
				String opString = String.format("%d", currentOperand);
				result = Long.parseUnsignedLong(resString.concat(opString));
			}
		}
		return result == target;
	}
}

public class Day7 extends DayBase {
	private String getBase3(long base10) {
		long result = 0, factor = 1;
		while (base10 > 0) {
			result += base10 % 3 * factor;
			base10 /= 3;
			factor *= 10;
		}

		return String.format("%d", result);
	}

	public void part1(String path) {
		ArrayList<String> data = FileReader.readData(path);

		HashSet<Integer> testedCalibration = new HashSet<>();
		long targetSum = 0;
		for (int j = 0; j < data.size(); j++) {
			String line = data.get(j);
			Calibration calibrationTest = new Calibration(line);
			for (int i = 0; i < Math.pow(2, calibrationTest.operand.size()-1); i++) {
				if (calibrationTest.testOperators(i) && !testedCalibration.contains(j)) {
					testedCalibration.add(j);
					targetSum += calibrationTest.target;
				}
			}
		}
		System.out.println(String.format("Part 1: %d", targetSum));
	}

	public void part2(String path) {
		ArrayList<String> data = FileReader.readData(path);

		HashSet<Integer> testedCalibration = new HashSet<>();
		long targetSum = 0;
		for (int j = 0; j < data.size(); j++) {
			String line = data.get(j);
			CalibrationExtended calibrationTest = new CalibrationExtended(line);
			for (int i = 0; i < Math.pow(3, calibrationTest.operand.size()-1); i++) {
				String operators = getBase3(i);
				if (calibrationTest.testOperators(operators) && !testedCalibration.contains(j)) {
					testedCalibration.add(j);
					targetSum += calibrationTest.target;
				}
			}
		}

		System.out.println(String.format("Part 2: %d", targetSum));
	}
}
