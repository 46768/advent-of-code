package dayBase;

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
			part1();
			part2();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
