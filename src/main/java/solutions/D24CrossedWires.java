package solutions;

import java.util.*;
import java.util.regex.*;

import dayBase.DayBase;
import logger.Logger;
import input.InputData;


public class D24CrossedWires extends DayBase<ArrayList<String>> {
	static enum CircuitType {
		Independent,
		Partial,
		Dependent
	};
	static Pattern typeRegex = Pattern.compile("[xy]\\d{2}", Pattern.CASE_INSENSITIVE);

	public D24CrossedWires(InputData dat) {
		super(dat);
	}

	protected ArrayList<String> parseInput(ArrayList<String> dat) {
		return dat;
	}

	protected HashMap<String, Boolean> parseWireInput() {
		HashMap<String, Boolean> returnMap = new HashMap<>();
		for (String line : data) {
			if (line.equals("")) return returnMap;
			String[] inputComponent = line.split("\\: ");
			returnMap.put(inputComponent[0], inputComponent[1].equals("1"));
		}

		return returnMap;
	}

	protected ArrayList<String> parseCircuitData() {
		ArrayList<String> returnArray = new ArrayList<>(data);
		ArrayList<String> filterArray = new ArrayList<>();

		for (String line : data) {
			if (line.equals("")) {
				filterArray.add("");
				break;
			}
			filterArray.add(line);
		}

		returnArray.removeAll(filterArray);
		return returnArray;
	}

	protected String getCircuitType(String circuit) {
		return circuit.substring(4, 7).trim();
	}

	protected ArrayList<String> getCircuitInputs(String circuit) {
		String[] firstSplit = circuit.split("( AND )|( OR )|( XOR )");
		String[] secondSplit = firstSplit[1].split(" -> ");
		return new ArrayList<>(List.of(firstSplit[0], secondSplit[0], secondSplit[1]));
	}

	protected boolean runCircuit(String circuit, HashMap<String, Boolean> wiringMap) {
		ArrayList<String> circuitPins = new ArrayList<>(getCircuitInputs(circuit));
		boolean in1Val = wiringMap.get(circuitPins.get(0));
		boolean in2Val = wiringMap.get(circuitPins.get(1));
		String operator = getCircuitType(circuit);

		if (operator.equals("AND")) {
			wiringMap.put(circuitPins.get(2), in1Val && in2Val);
			return in1Val && in2Val;
		}
		if (operator.equals("OR")) {
			wiringMap.put(circuitPins.get(2), in1Val || in2Val);
			return in1Val || in2Val;
		}
		// XOR is !=
		wiringMap.put(circuitPins.get(2), in1Val != in2Val);
		return in1Val != in2Val;
	}

	public void part1() {
		HashMap<String, Boolean> wireInput = parseWireInput();

		// Sort circuit data to have all logic dependencies resolved
		ArrayList<String> initCircuitData = parseCircuitData();
		ArrayList<String> circuitData = new ArrayList<>();
		HashSet<String> availableWiring = new HashSet<>(wireInput.keySet());
		
		while (circuitData.size() < initCircuitData.size()) {
			for (String circuit : initCircuitData) {
				ArrayList<String> circuitInput = getCircuitInputs(circuit);
				if (availableWiring.contains(circuitInput.get(0))
				&& availableWiring.contains(circuitInput.get(1))
				&& !circuitData.contains(circuit)) {
					circuitData.add(circuit);
					availableWiring.add(circuitInput.get(2));
				}
			}
		}

		long returnBinary = 0;
		for (String circuit : circuitData) {
			ArrayList<String> circuitPins = getCircuitInputs(circuit);
			boolean result = runCircuit(circuit, wireInput);
			if (circuitPins.get(2).charAt(0) == 'z' && result) {
				int bitPos = Integer.parseInt(circuitPins.get(2).substring(1));
				returnBinary |= 1l << bitPos;
			}
		}
		Logger.log("Return binary value in decimal: %d", returnBinary);
	}

	protected ArrayList<Boolean> getBits(char prefix) {
		ArrayList<Boolean> xBits = new ArrayList<>();
		HashMap<String, Boolean> wireInput = parseWireInput();
		for (int i = 0; i < 100; i++) {
			String key = prefix + String.format("%2d", i).replace(" ", "0");
			if (!wireInput.containsKey(key)) return xBits;
			xBits.add(wireInput.get(key));	
		}
		return xBits;
	}

	public void part2() {
		ArrayList<String> circuits = parseCircuitData();
		ArrayList<String> misroutedWires = new ArrayList<>();

		for (String circuit : circuits) {
			ArrayList<String> io = getCircuitInputs(circuit);
			String type = getCircuitType(circuit);

			char fcIn = io.get(0).charAt(0);
			char scIn = io.get(0).charAt(0);
			char fcOut = io.get(2).charAt(0);

			// Find if all output XOR is not connected to output wiring
			if (fcOut == 'z' && !type.equals("XOR") && !io.get(2).equals("z45")) {
				misroutedWires.add(io.get(2));
			}
			// Find any non output wire is connected to output XOR
			else if (fcOut != 'z' && type.equals("XOR")
					&& (fcIn != 'x' && fcIn != 'y') && (scIn != 'x' && scIn != 'y')) {
				misroutedWires.add(io.get(2));
			}

			if (misroutedWires.size() > 8) break;
		}

		Collections.sort(misroutedWires);

		Logger.log("Misrouted wires: %s", String.join(",", misroutedWires));
	}
}
