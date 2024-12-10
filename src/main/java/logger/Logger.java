package logger;

import java.util.Arrays;

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

	public static String formatColor(String str, String col) {
		return String.format("%s%s%s", col, str, ANSI_RESET);
	}

	private static void logBase(String level, Object... varargs) {
		if (varargs[0] instanceof String) {
			System.out.print(level);
			System.out.println(String.format((String)varargs[0], Arrays.copyOfRange(varargs, 1, varargs.length)));
		} else {
			System.out.print(level);
			System.out.println(varargs[0]);
		}
	}

	public static void print(Object obj) {
		System.out.print(obj);
	}

	public static void log(Object... obj) {
		logBase(INFO, obj);
	}

	public static void warn(Object... obj) {
		logBase(WARN, obj);
	}

	public static void error(Object... obj) {
		logBase(ERROR, obj);
	}

	public static void debug(Object... obj) {
		logBase(DEBUG, obj);
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
