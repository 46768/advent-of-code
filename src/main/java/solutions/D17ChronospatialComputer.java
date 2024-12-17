package solutions;

import java.util.ArrayList;

import input.InputData;
import logger.Logger;
import dayBase.DayBase;

public class D17ChronospatialComputer extends DayBase<ArrayList<Integer>> {
	private int regA;
	private int regB;
	private int regC;
	private int instPtr;
	private ArrayList<Integer> stdout;
	public D17ChronospatialComputer(InputData dat) {
		super(dat);
	}

	protected ArrayList<Integer> parseInput(ArrayList<String> dat) {
		instPtr = 0;
		ArrayList<Integer> instMem = new ArrayList<Integer>();
		stdout = new ArrayList<Integer>();
		boolean isInst = false;

		for (String line : dat) {
			if (isInst) {
				Logger.log(line.substring(9));
				for (String inst : line.substring(9).split(",")) {
					instMem.add(Integer.parseInt(inst));
				}
			} else {
				if (line.equals("")) {
					isInst = true;
					continue;
				}
				String regData = line.substring(9);
				if (regData.charAt(0) == 'A') {
					regA = Integer.parseInt(regData.substring(3));
				} else if (regData.charAt(0) == 'B') {
					regB = Integer.parseInt(regData.substring(3));
				} else {
					regC = Integer.parseInt(regData.substring(3));
				}
			}
		}

		Logger.log(regA);
		Logger.log(regB);
		Logger.log(regC);
		Logger.log(instMem);
		return instMem;
	}

	private int resolveComboOperand(int operand) {
		if (operand <= 3) return operand;
		if (operand == 4) return regA;
		if (operand == 5) return regB;
		if (operand == 6) return regC;
		return -1;
	}

	private void runOpcode(int opcode, int operand) {
		if (0 > operand && operand > 7) {
			Logger.error("Invalid operand range, range 0-7, got %d", operand);
			return;
		}

		// 0 - write A with A div by combo operand
		// 1 - write B with bitXOR B by literal operand
		// 2 - write B with combo operand % 8
		// 3 - jump to literal operand if A == 0
		// 4 - write B with bitXOR B by C
		// 5 - print combo operand % 8
		// 6 - same as '0' but write to B 
		// 7 - same as '0' but write to C
		Logger.debug("running opcode %d with %d", opcode, operand);
		switch (opcode) {
			case 0:
				regA /= 1 << resolveComboOperand(operand);
				break;
			case 1:
				regB ^= operand;
				break;
			case 2:
				regB = resolveComboOperand(operand) % 8;
				break;
			case 3:
				Logger.debug(regA);
				if (regA != 0) instPtr = operand;
				break;
			case 4:
				regB ^= regC;
				break;
			case 5:
				stdout.add(resolveComboOperand(operand) % 8);
				break;
			case 6:
				regB = regA / 1 << resolveComboOperand(operand);
				break;
			case 7:
				regC = regA / 1 << resolveComboOperand(operand);
				break;
			default:
				Logger.error("Invalid Opcode %d", opcode);
				break;
		}
	}

	public void part1() {
		for (;instPtr < data.size(); instPtr += 2) {
			int opcode = data.get(instPtr);
			int operand = data.get(instPtr+1);
			runOpcode(opcode, operand);
		}
		Logger.log("Stdout: %s", 
				stdout
				.toString()
				.replaceAll("\\ \\[\\]", "")
		);
	}
	public void part2() {}
}
