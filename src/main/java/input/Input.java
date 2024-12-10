package input;

import input.InputData;

// Puzzle input manager
public class Input {
	private String main;
	private String test;
	private String inUse;

	private String nameFormat;
	public Input(String mainPath, String testPath, String filenameFormat) {
		main = mainPath;
		test = testPath;
		inUse = mainPath;
		nameFormat = filenameFormat;
	}

	public void useTest() {
		inUse = test;
	}

	public void useMain() {
		inUse = main;
	}

	public InputData getDay(int day) {
		String filePath = "data" + inUse + "/" + String.format(nameFormat, day);
		return new InputData(filePath, String.format("Day %d", day));
	}
}
