package dayBase;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import logger.Logger;
import input.InputData;

public abstract class DayBase<T> {
	protected T data;
	protected InputData rawInput;
	protected abstract T parseInput(ArrayList<String> path);

	public DayBase(InputData dat) {
		rawInput = dat;
		if (dat.exists()) {
			data = parseInput(dat.data());
		}
	}

	public abstract void part1();
	public abstract void part2();

	public void runDay() {
		try {
			Logger.print("----------------------\n");
			if (!rawInput.exists()) {
				Logger.error("%s data file does not exist, skipping", rawInput.id());
				return;
			}
			Instant startTime = Instant.now();
			part1();
			Instant part1Time = Instant.now();
			part2();
			Instant endTime = Instant.now();

			Logger.print("\n");
			Logger.log("Part 1 took: " + Logger.formatTimeFromNanos(Duration.between(startTime, part1Time).toNanos()));
			Logger.log("Part 2 took: " + Logger.formatTimeFromNanos(Duration.between(part1Time, endTime).toNanos()));
			Logger.log("Total took: " + Logger.formatTimeFromNanos(Duration.between(startTime, endTime).toNanos()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
