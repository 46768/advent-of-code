package logger;

import java.util.Arrays;

/**
 * Logger utility class //TODO: Implement log files
 */
public class Logger {
	public static final String[] TIME_UNIT = {
		"ns",
		"us",
		"ms",
		"s",
	};

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	public static final String ANSI_BLACK = "\u001B[30m";

	private static final String INFO = "["+formatColor("INFO", ANSI_BLUE)+"] ";
	private static final String WARN = "["+formatColor("WARNING", ANSI_YELLOW)+"] ";
	private static final String ERROR = "["+formatColor("ERROR", ANSI_RED)+"] ";
	private static final String DEBUG = "["+formatColor("DEBUG", ANSI_PURPLE)+"] ";
	private static final String SUCCESS = "["+formatColor("SUCCESS", ANSI_GREEN)+"] ";

	/**
	 * Format the text to a ANSI color
	 *
	 * @param str Text to format
	 * @param col Color to format to, Must be ANSI color
	 */
	public static String formatColor(String str, String col) {
		return String.format("%s%s%s", col, str, ANSI_RESET);
	}

	/**
	 * The base for logging with levels of log
	 *
	 * @param level The prefix of a log
	 * @param varargs Arguments for logging
	 */
	private static void logBase(String level, Object... varargs) {
		if (varargs[0] instanceof String) {
			System.out.print(level);
			System.out.println(String.format((String)varargs[0], Arrays.copyOfRange(varargs, 1, varargs.length)));
		} else {
			System.out.print(level);
			System.out.println(varargs[0]);
		}
	}

	/**
	 * Raw printing to System.out
	 *
	 * @param obj Object to print to System.out
	 */
	public static void print(Object obj) {
		System.out.print(obj);
	}

	/**
	 * Print new line to System.out
	 */
	public static void newline() {
		System.out.println();
	}

	/**
	 * Log information to System.out
	 *
	 * @param obj Objects to log
	 */
	public static void log(Object... obj) {
		logBase(INFO, obj);
	}

	/**
	 * Warns information to System.out
	 *
	 * @param obj Objects to log
	 */
	public static void warn(Object... obj) {
		logBase(WARN, obj);
	}

	/**
	 * Errors to System.out, not fatal
	 *
	 * @param obj Objects to log
	 */
	public static void error(Object... obj) {
		logBase(ERROR, obj);
	}

	/**
	 * Log debug information to System.out
	 *
	 * @param obj Objects to log
	 */
	public static void debug(Object... obj) {
		logBase(DEBUG, obj);
	}

	/**
	 * Format time from nanoseconds to be more readable
	 *
	 * @param nano Time in nanoseconds
	 * @return The time formatted to time unit that is most readable
	 */
	public static void success(Object... obj) {
		logBase(SUCCESS, obj);
	}

	public static String formatTimeFromNanos(long nano) {
		double time = nano;
		int unitIdx = 0;
		while (unitIdx < 4) {
			if (time >= 1000) {
				time = time / 1000;
				unitIdx++;
			} else {
				break;
			}
		}
		String unit = TIME_UNIT[unitIdx];
		String ret = String.format("%f %s", time, unit);

		return ret;
	}
}
