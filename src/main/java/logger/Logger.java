package logger;

public class Logger {
	public static String[] timeUnit = {
		"ns",
		"us",
		"ms",
		"s",
	};


	public static void log(String fmt, Object... objs) {
		System.out.println(String.format(fmt, objs));
	}
	public static void log(String str) {
		System.out.println(str);
	}
	public static void log(Object obj) {
		System.out.println(obj);
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
		String unit = timeUnit[unitIdx];
		String ret = String.format("%f %s", time, unit);

		return ret;
	}
}
