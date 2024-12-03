import java.util.ArrayList;
import fileReader.FileReader;

// Datatype for input data
class Data extends ArrayList<String> {}

public class main {
	static Data formatData(ArrayList<String> dat) {
		Data res = new Data();
		return res;
	}

	static Data getData(String path) {
			return formatData(FileReader.readData(path));
	}

	static void part1(String path) {
		Data data = getData(path);
		System.out.println(String.format("Part 1:"));
	}

	static void part2(String path) {
		Data data = getData(path);
		System.out.println(String.format("Part 2:"));
	}

	public static void main(String[] arg) {
		if (arg.length < 1) {
			System.out.println("No Input data given");
			return;
		}
		part1(arg[0]);
		part2(arg[0]);
	}
}
