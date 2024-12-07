package ndArray;

import java.util.HashMap;

public class NDArray<T> {
	private HashMap<String, T> dimensionalArray = new HashMap<>();
	public NDArray() {}

	private String formatKey(int... indices) {
		String res = "";
		for (int index : indices) {
			res.concat(String.format("%d,", index));
		}
		return res;
	}

	public void set(T val, int... indices) {
		dimensionalArray.put(formatKey(indices), val);
	}

	public T get(int... indices) {
		return dimensionalArray.getOrDefault(formatKey(indices), null);
	}

	public void delete(int... indices) {
		dimensionalArray.remove(formatKey(indices));
	}
}
