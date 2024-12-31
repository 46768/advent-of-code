package solutions;

import java.util.ArrayList;

import dayBase.DayBase;
import logger.Logger;
import input.InputData;

class Calibration {
	long target;
	ArrayList<Long> operand = new ArrayList<>();
	boolean isExtended = false;

	public Calibration(String data, boolean extended) {
		isExtended = extended;
		String[] targetOperandSplit = data.split("\\:");
		target = Long.parseUnsignedLong(targetOperandSplit[0]);
		String[] operandSplit = targetOperandSplit[1].substring(1).split(" ");
		for (String op : operandSplit) {
			operand.add(Long.parseUnsignedLong(op));
		}
	}

	private class OperatorFactory {
		private long res;
		private int nextI;
		public OperatorFactory(long result, int nextIdx) {
			res = result;
			nextI = nextIdx;
		}
		public boolean apply(int operator) {
			return operatorsExists(res, nextI, operator);
		}
	}

	private long concatLong(long l1, long l2) {
		long l2Len = (long)Math.log10(l2)+1;
		return l1*(long)Math.pow(10, l2Len) + l2;
	}

	private long performOperator(long currentVal, long nextVal, int operator) {
		long result = currentVal;
		if (operator == 0) {
			result += nextVal;
		} else if (operator == 1) {
			result *= nextVal;
		} else if (isExtended && operator == 2) {
			result = concatLong(result, nextVal);
		}
		return result;
	}

	public boolean operatorsExists(long passedResult, int idx, int operator) {
		long nextOperand = operand.get(idx);
		long result = performOperator(passedResult, nextOperand, operator);
		if (result == target && idx+1 >= operand.size()) {
			return true;
		} else if (idx+1 >= operand.size()) {
			return false;
		}
		OperatorFactory operatorFactory = new OperatorFactory(result, idx+1);
		return operatorFactory.apply(0) || operatorFactory.apply(1) || operatorFactory.apply(2);
	}
}

public class D7BridgeRepair extends DayBase<ArrayList<String>> {
	public D7BridgeRepair(InputData dat) {
		super(dat);
	}

	protected ArrayList<String> parseInput(ArrayList<String> dat) {
		return dat;
	}

	public void part1() {
		long targetSum = 0;
		for (String line : data) {
			Calibration calibrationTest = new Calibration(line, false);
			for (int i = 0; i < 3; i++) {
				if (calibrationTest.operatorsExists(calibrationTest.operand.get(0), 1, i)) {
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
			Calibration calibrationTest = new Calibration(line, true);
			for (int i = 0; i < 3; i++) {
				if (calibrationTest.operatorsExists(calibrationTest.operand.get(0), 1, i)) {
					targetSum += calibrationTest.target;
					// Exit early since we dont care if theres more solutions available
					break;
				}
			}
		}

		Logger.log("Sum of possible extended calibration: %d", targetSum);
	}
}
