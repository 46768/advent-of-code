import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import fileReader.FileReader;

public class main {
	static ArrayList<String> getData(String path) {
			return FileReader.readData(path);
	}

	static void part1(String path) {
		ArrayList<String> data = getData(path);
		String regex = "mul\\(\\d*,\\d*\\)";

		int sum = 0;
		Pattern functionMatch = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

		for (String line : data) {
			Matcher lineMatch = functionMatch.matcher(line);
			while (lineMatch.find()) {
				String match = lineMatch.group();
				String args = match.substring(4, match.length()-1);
				String[] argsSplit = args.split(",");
				int product = Integer.parseInt(argsSplit[0]) * Integer.parseInt(argsSplit[1]);
				sum += product;
				System.out.println(product);
			}
		}
		System.out.println(String.format("Sum Of Mul() FN: %d", sum));
	}

	static void part2(String path) {
		ArrayList<String> data = getData(path);
		String regex = "(mul\\(\\d*,\\d*\\))|(do\\(\\))|(don't\\(\\))";

		int sum = 0;
		boolean enabled = true;

		Pattern functionMatch = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		for (String line : data) {
			Matcher lineMatch = functionMatch.matcher(line);
			while (lineMatch.find()) {
				String match = lineMatch.group();
				//System.out.println(match);
				if (match.equals("do()")) {
					enabled = true;
				} else if (match.equals("don't()")) {
					enabled = false;
				} else if (enabled) {
					String args = match.substring(4, match.length()-1);
					String[] argsSplit = args.split(",");
					int product = Integer.parseInt(argsSplit[0]) * Integer.parseInt(argsSplit[1]);
					sum += product;
				}
			}
		}
		System.out.println(String.format("Sum Of Mul() With Enable/Disable: %d", sum));
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

