package timer;

import java.time.Instant;
import java.time.Duration;

import logger.Logger;

public class Timer {
	public static void measure(String task, Runnable fn) {
		Instant start = Instant.now();
		fn.run();
		Instant end = Instant.now();
		Logger.log("%s took %s", task, Logger.formatTimeFromNanos(Duration.between(start, end).toNanos()));
	}
}
