import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

package fileReader;

public class FileReader {
	public static String[] readData(String path) {
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
			System.out.println(String.format());
			e.printStackTrace();
			return new ArrayList<ArrayList<Integer>>();
		}
	}
}
