package dayBase;

public abstract class DayBase {
	public static void runDay(Class<? extends DayBase> dayClass, String path) {
		try {
			DayBase dayInstance = dayClass.getDeclaredConstructor().newInstance();
			System.out.println("----------------------");
			System.out.println(dayClass.getSimpleName());
			dayInstance.part1(path);
			dayInstance.part2(path);
			
		} catch (Exception e) {

		}
	}

	public abstract void part1(String path);
	public abstract void part2(String path);
}
