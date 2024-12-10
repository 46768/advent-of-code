package input;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

import fileHandler.FileHandler;

// Puzzle input fetcher
public class Web {
	private static String agentHeaderFormat = "https://github.com/46768/AOC2024 from thermalserver7@gmail.com - day %d";
	private static String inputURLFormat = "https://adventofcode.com/2024/day/%d/input";

	private String agentHeader;

	public Web(int year) {
		agentHeader = String.format(agentHeaderFormat, year);
	}

	private String fetchCookie() {
		return "";
	}

	public String fetchDay(int day) {
		String inputURL = String.format(inputURLFormat, day);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest req = HttpRequest
			.newBuilder(URI.create(inputURL))
			.GET()
			.header("Cookie", "")
			.build();
		return "";
	}
}
