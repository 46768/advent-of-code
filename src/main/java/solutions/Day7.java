package solutions;

import java.util.ArrayList;

import dayBase.DayBase;
import logger.Logger;
import input.InputData;

class Calibration {
	long target;
	ArrayList<Long> operand = new ArrayList<>();

	public Calibration(String data) {
		String[] targetOperandSplit = data.split("\\:");
		target = Long.parseUnsignedLong(targetOperandSplit[0]);
		String[] operandSplit = targetOperandSplit[1].substring(1).split(" ");
		for (String op : operandSplit) {
			operand.add(Long.parseUnsignedLong(op));
		}
	}

	public boolean testOperators(int operators) {
		int operatorsVar = operators;
		long result = operand.get(0);
		for (int i = 1; i < operand.size(); i++) {
			int operator = operatorsVar & 1;
			long currentOperand = operand.get(i);
			// Operators are stored as bits, 0 as addition, 1 as multiplication
			// this allows for compact operator storage
			if (operator == 0) {
				result += currentOperand;
			} else {
				result *= currentOperand;
			}
			operatorsVar >>= 1;
		}
		return result == target;
	}
}

class CalibrationExtended {
	long target;
	ArrayList<Long> operand = new ArrayList<>();

	public CalibrationExtended(String data) {
		String[] targetOperandSplit = data.split("\\:");
		target = Long.parseUnsignedLong(targetOperandSplit[0]);
		String[] operandSplit = targetOperandSplit[1].substring(1).split(" ");
		for (String op : operandSplit) {
			operand.add(Long.parseUnsignedLong(op));
		}
	}

	public boolean testOperators(long operators) {
		long result = operand.get(0);
		for (int i = 1; i < operand.size(); i++) {
			long operator = operators % 10;
			long currentOperand = operand.get(i);
			// Operates on tenary integer, 0 as addition, 1 as multiplication, 2 as concatenation
			if (operator == 0) {
				result += currentOperand;
			} else if (operator == 1) {
				result *= currentOperand;
			} else {
				String resString = Long.toString(result);
				String opString = Long.toString(currentOperand);
				result = Long.parseUnsignedLong(resString.concat(opString));
			}
			operators = Math.floorDiv(operators, 10);
		}
		return result == target;
	}
}

public class Day7 extends DayBase<ArrayList<String>> {
	public Day7(InputData dat) {
		super(dat);
	}

	protected ArrayList<String> parseInput(ArrayList<String> dat) {
		return dat;
	}

	private long getBase3(long base10) {
		long result = 0, factor = 1;
		while (base10 > 0) {
			result += base10 % 3 * factor;
			base10 /= 3;
			factor *= 10;
		}

		return result;
	}

	public void part1() {
		long targetSum = 0;
		for (String line : data) {
			Calibration calibrationTest = new Calibration(line);
			// Iterate over all possible combination of binary birs for additon and multiplication
			for (int i = 0; i < Math.pow(2, calibrationTest.operand.size()-1); i++) {
				if (calibrationTest.testOperators(i)) {
					targetSum += calibrationTest.target;
					// Exit early since we dont care if theres more solutions available
					break;
				}
			}
		}
		Logger.log("Sum of possible calibration: %d", targetSum);
	}

	public void part2() {
		long targetSum = 0;
		for (String line : data) {
			CalibrationExtended calibrationTest = new CalibrationExtended(line);
			// Iterate over all possible tenary bits for addtion, multiplication, concatenation
			for (int i = 0; i < Math.pow(3, calibrationTest.operand.size()-1); i++) {
				long operators = getBase3(i);
				if (calibrationTest.testOperators(operators)) {
					targetSum += calibrationTest.target;
					// Exit early since we dont care if theres more solutions available
					break;
				}
			}
		}

		Logger.log("Sum of possible extended calibration: %d", targetSum);
	}
}
