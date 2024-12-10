package fileHandler;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import logger.Logger;

public class FileHandler {
	public static ArrayList<String> readFile(String path) {
		try {
			File data = new File(path);
			Scanner dataScan = new Scanner(data);
			ArrayList<String> dataArray = new ArrayList<String>();
			while (dataScan.hasNextLine()) {
				String line = dataScan.nextLine();
				dataArray.add(line);
			}
			dataScan.close();
			return dataArray;
		} catch (FileNotFoundException e) {
			//Logger.error("%s: File not found", path);
			//e.printStackTrace();
			return new ArrayList<String>();
		}
	}

	public static void writeFile(String path, String data) {
		try {
			File writingFile = new File(path);
			if (!writingFile.exists()) {
				writingFile.createNewFile();
			} else {
				Logger.log("file %s failed to be created");
			}

		} catch (IOException e) {
			Logger.log("file %s failed to be created");
		}
	}
}
