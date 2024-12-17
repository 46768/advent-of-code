package grid;

import java.util.ArrayList;

import geomUtil.Coord;
import logger.Logger;

public class Grid<T> {
	private long sizeX;
	private long sizeY;
	private T outOfBoundVal;
	private ArrayList<T> data = new ArrayList<>();
	public Grid(long sx, long sy, T defVal, T oobVal) {
		sizeX = sx;
		sizeY = sy;
		outOfBoundVal = oobVal;

		for (long i = 0; i < sx; i++) {
			for (long j = 0; j < sx; j++) {
				data.add(defVal);
			}
		}
	}

	public void printCurrentState() {
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				Coord coord = new Coord(x, y);
				Logger.print(getVal(coord));
			}
			Logger.print('\n');
		}
	}

	public boolean isInBound(long x, long y) {
		if (0 > x || x >= sizeX) return false;
		if (0 > y || y >= sizeY) return false;
		return true;
	}
	public boolean isInBound(Integer[] pos) {
		if (0 > pos[0] || pos[0] >= sizeX) return false;
		if (0 > pos[1] || pos[1] >= sizeY) return false;
		return true;
	}
	public boolean isInBound(Coord pos) {
		if (0 > pos.x() || pos.x() >= sizeX) return false;
		if (0 > pos.y() || pos.y() >= sizeY) return false;
		return true;
	}
	
	public long compressCoord(Coord pos) {
		return pos.x()*sizeX + pos.y();
	}
	public Coord decompressCoord(long comp) {
		return new Coord(comp/sizeX, comp%sizeX);
	}

	public void setVal(long x, long y, T val) {
		if (!isInBound(x, y)) throw new IndexOutOfBoundsException("Grid Index Out of bound");
		data.set((int)(x*sizeX + y), val);
	}
	public void setVal(Integer[] pos, T val) {
		if (!isInBound(pos[0], pos[1])) throw new IndexOutOfBoundsException("Grid Index Out of bound");
		data.set((int)(pos[0]*sizeX + pos[1]), val);
	}
	public void setVal(Coord pos, T val) {
		if (!isInBound(pos.x(), pos.y())) throw new IndexOutOfBoundsException("Grid Index Out of bound");
		data.set((int)(pos.x()*sizeX + pos.y()), val);
	}

	public T getVal(long x, long y) {
		if (!isInBound(x, y)) return outOfBoundVal;
		return data.get((int)(x*sizeX + y));
	}
	public T getVal(Integer[] pos) {
		if (!isInBound(pos[0], pos[1])) return outOfBoundVal;
		return data.get((int)(pos[0]*sizeX + pos[1]));
	}
	public T getVal(Coord pos) {
		if (!isInBound(pos.x(), pos.y())) return outOfBoundVal;
		return data.get((int)(pos.x()*sizeX + pos.y()));
	}

	public long sizeX() { return this.sizeX; };
	public long sizeY() { return this.sizeY; };
}
