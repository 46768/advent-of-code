package input;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

import java.time.Instant;
import java.time.Duration;

import java.util.ArrayList;

import java.io.FileNotFoundException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import fileHandler.FileHandler;
import logger.Logger;

// Puzzle input fetcher
public class Web {
	private static final String FETCH_DATA_PATH = "data/http/fetchData.txt";
	private static final String COOKIE_PATH = "data/http/cookies.txt";

	private static String agentHeaderFormat = "https://github.com/46768/AOC2024 from thermalserver7@gmail.com - day %d";
	private static String inputURLFormat = "https://adventofcode.com/2024/day/%d/input";
	private static int requestPerHour = 25; // Limits requests to this amount per hour
	private static int requestSecDelay = 5; // Delay each requests by this amount of seconds as to not hammer AOC's server

	private String agentHeader;
	private Instant lastFetched;
	private int requestMadeThisHour;

	// Looks for file data/http/fetchData.txt to determine when was last request made;
	public Web(int year) throws FileNotFoundException {
		agentHeader = String.format(agentHeaderFormat, year);
		ArrayList<String> fetchData = FileHandler.readFile(FETCH_DATA_PATH);
		if (fetchData.size() == 0) {
			Logger.error("Fetch Data cant be found at %s", FETCH_DATA_PATH);
			throw new FileNotFoundException("fetch data cant be found");
		}
		lastFetched = Instant.parse(fetchData.get(0));
		requestMadeThisHour = Integer.parseInt(fetchData.get(1));
	}

	// SHA256 Checksum
	private String genSHA256Checksum(ArrayList<String> data) {
		try {
			MessageDigest hasher = MessageDigest.getInstance("SHA-256");
			StringBuilder dataBuilder = new StringBuilder();
			for (String line : data) {
				dataBuilder.append(line);
			}
			StringBuilder hashBuilder = new StringBuilder();
			byte[] hashBytes = hasher.digest(dataBuilder.toString().getBytes());
			for (byte b : hashBytes) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) hashBuilder.append('0');
				hashBuilder.append(hex);
			}
			return hashBuilder.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}

	// Fetch cookie from file data/http/cookies.txt
	private String fetchCookie() throws FileNotFoundException {
		ArrayList<String> fetchedCookie = FileHandler.readFile(COOKIE_PATH);
		if (fetchedCookie.size() == 0) {
			Logger.error("Fetch Data cant be found at %s", COOKIE_PATH);
			throw new FileNotFoundException("fetch data cant be found");
		}
		return fetchedCookie.get(0);
	}

	private void cacheInput(int day) {
		
	}

	public void setCookie(String cookie) {
		
	}

	public String fetchDay(int day) throws FileNotFoundException {
		String inputURL = String.format(inputURLFormat, day);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest req = HttpRequest
			.newBuilder(URI.create(inputURL))
			.GET()
			.header("Cookie", fetchCookie())
			.build();
		return "";
	}
}
