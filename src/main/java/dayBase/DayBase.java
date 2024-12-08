package dayBase;

import java.time.Duration;
import java.time.Instant;

import logger.Logger;

public abstract class DayBase<T> {
	protected String inputPath;
	protected T data;
	protected abstract T parseInput(String path);

	public DayBase(String path) {
		inputPath = path;
		data = parseInput(path);
	}

	public abstract void part1();
	public abstract void part2();

	public void runDay() {
		try {
			System.out.println("----------------------");
			Instant startTime = Instant.now();
			part1();
			Instant part1Time = Instant.now();
			part2();
			Instant endTime = Instant.now();

			Logger.log("Part 1 took: " + Logger.formatTimeFromNanos(Duration.between(startTime, part1Time).toNanos()));
			Logger.log("Part 2 took: " + Logger.formatTimeFromNanos(Duration.between(part1Time, endTime).toNanos()));
			Logger.log("Total took: " + Logger.formatTimeFromNanos(Duration.between(startTime, endTime).toNanos()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
