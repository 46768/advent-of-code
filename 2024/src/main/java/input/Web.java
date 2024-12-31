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

/**
 * Puzzle data fetcher //TODO: Implement this
 */
public class Web {
	/** Fetching data path, contains last fetched, fetched count */
	private static final String FETCH_DATA_PATH = "data/http/fetchData.txt";

	/** Cookie file path //TODO: Change to use env vars instead for security */
	private static final String COOKIE_PATH = "data/http/cookies.txt";

	/** Agent header format for HTTP request */
	private static String agentHeaderFormat = "https://github.com/46768/AOC2024 from thermalserver7@gmail.com - year %d";

	/** Input data url format for HTTP request target */
	private static String inputURLFormat = "https://adventofcode.com/2024/day/%d/input";

	/** Max request per hour */
	private static int requestPerHour = 25; // Limits requests to this amount per hour

	/** Delay between each request */
	private static int requestSecDelay = 5; // Delay each requests by this amount of seconds as to not hammer AOC's server

	/** Agent header */
	private String agentHeader;

	/** Last fetched's time */
	private Instant lastFetched;

	/** Total request made in the hour */
	private int requestMadeThisHour;

	/** 
	 * Construct an input fetcher
	 * <p>Looks for file data/http/fetchData.txt to determine when was last request made
	 *
	 * @param year The year for input
	 */
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

	/**
	 * Generate SHA256 Checksum
	 *
	 * <p> Usually to handle file corruption like modifying files
	 *
	 * @param data The data array, usually provided by {@link Input} manager
	 * @return The SHA256 checksum of the data
	 */
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

	/**
	 * Fetch cookie from file data/http/cookies.txt
	 *
	 * @deprecated Pending changes to use env vars for more security
	 *
	 * @return Advent of code session cookie
	 */
	private String fetchCookie() throws FileNotFoundException {
		ArrayList<String> fetchedCookie = FileHandler.readFile(COOKIE_PATH);
		if (fetchedCookie.size() == 0) {
			Logger.error("Fetch Data cant be found at %s", COOKIE_PATH);
			throw new FileNotFoundException("fetch data cant be found");
		}
		return fetchedCookie.get(0);
	}

	/**
	 * Fetch and cache input data of provided day
	 * <p>
	 * If the file for the day's data dont exists or the checksum mismatched
	 * then fetch the data from advent of code's server and create/override the
	 * previous file
	 *
	 * @param day The day to fetch and cache input data
	 */
	private void cacheInput(int day) {
		
	}

	/**
	 * Set the session cookie
	 * 
	 * @param cookie The cookie to set
	 */
	public void setCookie(String cookie) {
		
	}

	/**
	 * Fetch the data from advent of code's server
	 *
	 * @param day The day to fetch the data
	 */
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
