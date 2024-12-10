package grid;

import java.util.ArrayList;

public class Grid<T> {
	private int sizeX;
	private int sizeY;
	private T outOfBoundVal;
	private ArrayList<T> data = new ArrayList<>();
	public Grid(int sx, int sy, T defVal, T oobVal) {
		sizeX = sx;
		sizeY = sy;
		outOfBoundVal = oobVal;

		for (int i = 0; i < sx; i++) {
			for (int j = 0; j < sx; j++) {
				data.add(defVal);
			}
		}
	}

	public boolean isInBound(int x, int y) {
		if (0 > x || x >= sizeX) return false;
		if (0 > y || y >= sizeY) return false;
		return true;
	}
	public boolean isInBound(Integer[] pos) {
		if (0 > pos[0] || pos[0] >= sizeX) return false;
		if (0 > pos[1] || pos[1] >= sizeY) return false;
		return true;
	}

	public void setVal(int x, int y, T val) {
		if (!isInBound(x, y)) throw new IndexOutOfBoundsException("Grid Index Out of bound");
		data.set(x*sizeX + y, val);
	}
	public void setVal(Integer[] pos, T val) {
		if (!isInBound(pos[0], pos[1])) throw new IndexOutOfBoundsException("Grid Index Out of bound");
		data.set(pos[0]*sizeX + pos[1], val);
	}

	public T getVal(int x, int y) {
		if (!isInBound(x, y)) return outOfBoundVal;
		return data.get(x*sizeX + y);
	}
	public T getVal(Integer[] pos) {
		if (!isInBound(pos[0], pos[1])) return outOfBoundVal;
		return data.get(pos[0]*sizeX + pos[1]);
	}

	public int sizeX() { return this.sizeX; };
	public int sizeY() { return this.sizeY; };
}
