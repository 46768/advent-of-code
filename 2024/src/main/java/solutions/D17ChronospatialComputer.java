package solutions;

import java.util.ArrayList;
import java.util.Collections;

import input.InputData;
import logger.Logger;
import dayBase.DayBase;

class Computer {
	private long regA;
	private long regB;
	private long regC;
	private long regAInit;
	private long regBInit;
	private long regCInit;
	private long instPtr;
	private ArrayList<Long> stdout;
	private ArrayList<Long> program;
	public Computer(ArrayList<Long> program, long initA, long initB, long initC) {
		this.program = program;
		stdout = new ArrayList<>();
		regAInit = initA;
		regBInit = initB;
		regCInit = initC;
		regA = initA;
		regB = initB;
		regC = initC;
		instPtr = 0;
	}

	private long resolveComboOperand(long operand) {
		if (operand <= 3) return operand;
		if (operand == 4) return regA;
		if (operand == 5) return regB;
		if (operand == 6) return regC;
		return -1;
	}

	private void runOpcode(long opcode, long operand) {
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
		//Logger.debug("running opcode %d with %d", opcode, operand);
		//Logger.debug("State: A %d B %d C %d", regA, regB, regC);
		//Logger.debug("stdout: %s", stdout);
		switch ((int)opcode) {
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
				//Logger.debug(regA);
				if (regA != 0) instPtr = operand-2;
				break;
			case 4:
				regB ^= regC;
				break;
			case 5:
				stdout.add(resolveComboOperand(operand) % 8);
				break;
			case 6:
				regB = regA / (1 << resolveComboOperand(operand));
				break;
			case 7:
				regC = regA / (1 << resolveComboOperand(operand));
				break;
			default:
				Logger.error("Invalid Opcode %d", opcode);
				break;
		}
		//Logger.debug("State: A %d B %d C %d", regA, regB, regC);
		//Logger.debug("stdout: %s", stdout);
	}

	public String runProgram() {
		for (;instPtr < program.size(); instPtr += 2) {
			long opcode = program.get((int)instPtr);
			long operand = program.get((int)instPtr+1);
			runOpcode(opcode, operand);
		}
		return stdout
			.toString()
			.replace(" ", "")
			.replace("[", "")
			.replace("]", "");
	}

	public long reverseEnginnerStdout(ArrayList<Long> stdOut) {
		ArrayList<Long> possibleInput = new ArrayList<>();
		ArrayList<Long> reversedStdOut = new ArrayList<>(stdOut);
		ArrayList<Long> possibleInputCurrent = new ArrayList<>();
		Collections.reverse(reversedStdOut);

		possibleInput.add(0l);
		for (long out : reversedStdOut) {
			while (possibleInput.size() > 0) {
				possibleInputCurrent.add(possibleInput.getFirst());
				possibleInput.removeFirst();
			}
			for (long inpt : possibleInputCurrent) {
				for (int test = 0; test < 8; test++) {
					long testRegA = (inpt<<3)|test;
					Computer testComputer = new Computer(program, testRegA, regBInit, regCInit);
					String stdOutTest = testComputer.runProgram();
					long firstOut = Long.parseLong(stdOutTest.split(",")[0]);
					if (firstOut == out) possibleInput.add(testRegA);
				}
			}
			possibleInputCurrent.clear();
		}


		Collections.sort(possibleInput);

		return possibleInput.get(0);
	}
}

public class D17ChronospatialComputer extends DayBase<ArrayList<Long>> {
	private long regA;
	private long regB;
	private long regC;
	public D17ChronospatialComputer(InputData dat) {
		super(dat);
	}

	protected ArrayList<Long> parseInput(ArrayList<String> dat) {
		ArrayList<Long> instMem = new ArrayList<Long>();
		boolean isInst = false;

		for (String line : dat) {
			if (isInst) {
				Logger.log(line.substring(9));
				for (String inst : line.substring(9).split(",")) {
					instMem.add(Long.parseLong(inst));
				}
			} else {
				if (line.equals("")) {
					isInst = true;
					continue;
				}
				String regData = line.substring(9);
				if (regData.charAt(0) == 'A') {
					regA = Long.parseLong(regData.substring(3));
				} else if (regData.charAt(0) == 'B') {
					regB = Long.parseLong(regData.substring(3));
				} else {
					regC = Long.parseLong(regData.substring(3));
				}
			}
		}

		return instMem;
	}

	public void part1() {
		Computer normalComputer = new Computer(data, regA, regB, regC);
		Logger.log("Stdout: %s", normalComputer.runProgram());
	}
	public void part2() {
		Computer normalComputer = new Computer(data, regA, regB, regC);
		Logger.log("Reversed engineered regA: %d", normalComputer.reverseEnginnerStdout(data));
	}
}
