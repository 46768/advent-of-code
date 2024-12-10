package input;

import java.util.ArrayList;

import fileHandler.FileHandler;

// Input Data Class
public class InputData {
	private ArrayList<String> fileData;
	private String dayID;
	private boolean dataExist;

	public InputData(String filePath, String ID) {
		fileData = FileHandler.readFile(filePath);
		dayID = ID;
		dataExist = true;
		if (fileData.equals(new ArrayList<>())) {
			dataExist = false;
		}
	}

	public ArrayList<String> data() {
		return fileData;
	}

	public String id() {
		return dayID;
	}

	public boolean exists() {
		return dataExist;
	}
}
