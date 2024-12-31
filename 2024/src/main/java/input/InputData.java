package input;

import java.util.ArrayList;

import fileHandler.FileHandler;

/**
 * Input data class
 */
public class InputData {
	/** Array of lines of data file */
	private ArrayList<String> fileData;

	/** Input data ID used by {@link dayBase.DayBase} */
	private String dayID;

	/** Does the data exists */
	private boolean dataExist;

	/**
	 * Construct an input data from file and ID
	 * @param filePath Path to data file, usually managed by {@link Input} manager
	 * @param ID ID of input, usually used by {@link dayBase.DayBase} to refer datafile not exists, managed by {@link Input} manager
	 */
	public InputData(String filePath, String ID) {
		fileData = FileHandler.readFile(filePath);
		dayID = ID;
		dataExist = true;
		if (fileData.equals(new ArrayList<>())) {
			dataExist = false;
		}
	}

	/**
	 * Data array getter method
	 * @return The data in fileData
	 */
	public ArrayList<String> data() {
		return fileData;
	}

	/**
	 * InputData ID getter method
	 * @return InputData's ID
	 */
	public String id() {
		return dayID;
	}

	/**
	 * InputData's data availbility
	 * @return The data's availbility
	 */
	public boolean exists() {
		return dataExist;
	}
}
