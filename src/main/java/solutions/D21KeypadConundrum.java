package solutions;

import java.util.*;

import dayBase.DayBase;
import input.InputData;
import logger.Logger;
import geomUtil.*;

public class D21KeypadConundrum extends DayBase<ArrayList<String>> {
	HashMap<Integer, HashMap<Integer, String>> memonizedKeypad;
	HashMap<Integer, HashMap<Integer, HashMap<Integer, String>>> memonizedController;
	HashMap<Character, Integer> keypadMap;
	HashMap<Character, Integer> controllerMap;
	public D21KeypadConundrum(InputData dat) {
		super(dat);
	}

	protected ArrayList<String> parseInput(ArrayList<String> dat) {
		memonizedKeypad = generateShortestPathForKeypad();
		memonizedController = generateShortestPathForController();
		keypadMap = new HashMap<>();
		controllerMap = new HashMap<>();

		keypadMap.put('0', 0);
		keypadMap.put('1', 1);
		keypadMap.put('2', 2);
		keypadMap.put('3', 3);
		keypadMap.put('4', 4);
		keypadMap.put('5', 5);
		keypadMap.put('6', 6);
		keypadMap.put('7', 7);
		keypadMap.put('8', 8);
		keypadMap.put('9', 9);
		keypadMap.put('A', 10);

		controllerMap.put('^', 0);
		controllerMap.put('<', 1);
		controllerMap.put('v', 2);
		controllerMap.put('>', 3);
		controllerMap.put('A', 4);
		return dat;
	}

	private String parseCoordAsMoves(Coord coord, Coord startCoord, boolean isKeypad) {
		String vertical = coord.x() < 0 ? "v" : "^";
		String horizontal = coord.y() < 0 ? ">" : "<";
		String vertSeq = vertical.repeat((int)Math.abs(coord.x()));
		String horiSeq = horizontal.repeat((int)Math.abs(coord.y()));
		boolean seqSeq = isKeypad ? startCoord.x() < 3 : startCoord.x() > 0;
		String seq = seqSeq ? horiSeq + vertSeq : vertSeq + horiSeq;
		return seq + "A";
	}

	private HashMap<Integer, HashMap<Integer, String>> generateShortestPathForKeyset(Coord[] keyPos, boolean isKeypad) {
		HashMap<Integer, HashMap<Integer, String>> paths = new HashMap<>();

		for (int i = 0; i < keyPos.length; i++) {
			paths.put(i, new HashMap<>());
			HashMap<Integer, String> keyMap = paths.get(i); 
			for (int j = 0; j < keyPos.length; j++) {
				keyMap.put(j, parseCoordAsMoves(keyPos[i].subtract(keyPos[j]), keyPos[i], isKeypad));
			}
		}

		return paths;
	}

	// Keypad structure:
	// 7 8 9
	// 4 5 6
	// 1 2 3
	//   0 A
	private HashMap<Integer, HashMap<Integer, String>> generateShortestPathForKeypad() {
		Coord[] keyPos = new Coord[]{
			new Coord(3, 1), // 0
				new Coord(2, 0), // 1
				new Coord(2, 1), // 2
				new Coord(2, 2), // 3
				new Coord(1, 0), // 4
				new Coord(1, 1), // 5
				new Coord(1, 2), // 6
				new Coord(0, 0), // 7
				new Coord(0, 1), // 8
				new Coord(0, 2), // 9
				new Coord(3, 2), // A
		};

		return generateShortestPathForKeyset(keyPos, true);
	}

	// Controller structure:
	//   ^ A
	// < v >
	private HashMap<Integer, HashMap<Integer, HashMap<Integer, String>>> generateShortestPathForController() {
		Coord[] keyPos = new Coord[]{
			new Coord(0, 1), // ^
			new Coord(1, 0), // <
			new Coord(1, 1), // v
			new Coord(1, 2), // >
			new Coord(0, 2), // A
		};

		return generateShortestPathForKeyset(keyPos, false);
	}

	private String parseMovement(String moveSeq) {
		String returnSeq = "";
		int previousState = 4;
		for (int i = 0; i < moveSeq.length(); i++) {
			int targetState = controllerMap.get(moveSeq.charAt(i));
			returnSeq = returnSeq.concat(memonizedController.get(previousState).get(targetState));
			previousState = targetState;
		}

		return returnSeq;
	}

	public void part1() {
		// Keypad's A => 10
		// Controller's A => 4

		long complexityScore = 0;
		for (String code : data) {
			String finalSequence = "";
			String radBot = "";
			String depBot = "";
			int keypadRobotState = 10; // controlled by depressurized robot
			
			for (int i = 0; i < code.length(); i++) {
				int codeIdx = keypadMap.get(code.charAt(i));
				String keypadRobotSeq = memonizedKeypad.get(keypadRobotState).get(codeIdx); // required by dpRobotSeq
				String depressurizedRobotSeq = parseMovement(keypadRobotSeq); // required by radRobotSeq
				String radiationRobotSeq = parseMovement(depressurizedRobotSeq); // required by frozenBotSeq
				
				depBot = depBot.concat(keypadRobotSeq);
				radBot = radBot.concat(depressurizedRobotSeq);
				finalSequence = finalSequence.concat(radiationRobotSeq); // frozenBotSeq, required by you
				keypadRobotState = codeIdx;
			}
			Logger.debug("%s: %s", code, depBot);
			Logger.debug("%s: %s", code, radBot);
			Logger.debug("%s: %s", code, finalSequence);
			Logger.debug("Final length: %d", finalSequence.length());
			Logger.debug("code int part: %d", Integer.parseInt(code.substring(0, code.length()-1)));
			complexityScore += finalSequence.length() * Integer.parseInt(code.substring(0, code.length()-1));
		}

		Logger.log("Complexity Score: %d", complexityScore);
	}
	public void part2() {}
}
