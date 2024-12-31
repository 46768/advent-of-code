package input;

/**
 * Puzzle input manager
 */
public class Input {
	/** Main data directory for real inputs */
	private String main;

	/** Test data directory for test inputs */
	private String test;

	/** Data directory currently in use */
	private String inUse;

	/** Data files name format, used in String.format */
	private String nameFormat;

	/**
	 * Input manager constructor
	 * <p> Relative path are relative to where the program is ran
	 *
	 * @param mainPath Main data directory path
	 * @param testPath Test data directory path
	 * @param filenameFormat data file's name format
	 */
	public Input(String mainPath, String testPath, String filenameFormat) {
		main = mainPath;
		test = testPath;
		inUse = mainPath;
		nameFormat = filenameFormat;
	}

	/**
	 * Switch to use test data directory
	 */
	public void useTest() {
		inUse = test;
	}

	/**
	 * Switch to use main data directory
	 */
	public void useMain() {
		inUse = main;
	}

	/**
	 * Get day data from currently in use data directory
	 * @param day Specified day to get data from
	 * @return The {@link InputData} that is used by {@link dayBase.DayBase}'s constructor
	 */
	public InputData getDay(int day) {
		String filePath = "data" + inUse + "/" + String.format(nameFormat, day);
		return new InputData(filePath, String.format("Day %d", day));
	}
}
