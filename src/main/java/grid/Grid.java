package grid;

import java.util.ArrayList;

import geomUtil.Coord;
import logger.Logger;

/**
 * 2D Grid class
 */
public class Grid<T> {
	/**
	 * Check if the given position is inbound to the given sizes
	 *
	 * @param sizeX Size of X bound
	 * @param sizeY Size of Y bound
	 * @param pos Position to check
	 * @return Whether the position is inbound
	 */
	public static boolean withinBound(long sizeX, long sizeY, Coord pos) {
		if (0 > pos.x() || pos.x() >= sizeX) return false;
		if (0 > pos.y() || pos.y() >= sizeY) return false;
		return true;
	}

	/** Size of X of the grid */
	private long sizeX;

	/** Size of Y of the grid */
	private long sizeY;

	/** Value to return for getting out of bound position */
	private T outOfBoundVal;

	/** 1D Array containing the data */
	private ArrayList<T> data = new ArrayList<>();

	/**
	 * Generate a grid with the given size, inital value,
	 * and out of bound value
	 *
	 * @param sx Size of X
	 * @param sy Size of Y
	 * @param defVal Initial value of the grid
	 * @param oobVal Out of bound value
	 */
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

	/**
	 * Prints the current state of the grid
	 * <p> Used for debugging most of the time
	 */
	public void printCurrentState() {
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				Coord coord = new Coord(x, y);
				Logger.print(getVal(coord));
			}
			Logger.print('\n');
		}
	}

	/**
	 * Check whether the position provided as 2 longs are in bound
	 * @param x x position
	 * @param y y position
	 * @return whether the positon is inbount or not
	 */
	public boolean isInBound(long x, long y) {
		if (0 > x || x >= sizeX) return false;
		if (0 > y || y >= sizeY) return false;
		return true;
	}
	/**
	 * Check whether the position provided as long array are in bound
	 * @param pos position as array as [x, y]
	 * @return whether the positon is inbount or not
	 */
	public boolean isInBound(Integer[] pos) {
		if (0 > pos[0] || pos[0] >= sizeX) return false;
		if (0 > pos[1] || pos[1] >= sizeY) return false;
		return true;
	}
	/**
	 * Check whether the position provided as {@link Coord} are in bound
	 * @param pos Coordinate
	 * @return whether the positon is inbount or not
	 */
	public boolean isInBound(Coord pos) {
		if (0 > pos.x() || pos.x() >= sizeX) return false;
		if (0 > pos.y() || pos.y() >= sizeY) return false;
		return true;
	}
	
	/**
	 * Compress coordinate into a long pointing to the
	 * internal array index for the positon
	 *
	 * @param pos Coordinate to compress
	 * @return The array index of the data array of the position
	 */
	public long compressCoord(Coord pos) {
		return pos.x()*sizeX + pos.y();
	}
	/**
	 * Decompress array index to a {@link Coord}
	 *
	 * @param comp The compacted array index
	 * @return The Coordinate of the grid
	 */
	public Coord decompressCoord(long comp) {
		return new Coord(comp/sizeX, comp%sizeX);
	}

	/**
	 * Set the value of a positon in the grid
	 * 
	 * @param x x position
	 * @param y y position
	 * @param val Value to set
	 */
	public void setVal(long x, long y, T val) {
		if (!isInBound(x, y)) throw new IndexOutOfBoundsException("Grid Index Out of bound");
		data.set((int)(x*sizeX + y), val);
	}
	/**
	 * Set the value of a positon in the grid
	 * 
	 * @param pos Position array of [x, y]
	 * @param val Value to set
	 */
	public void setVal(Integer[] pos, T val) {
		if (!isInBound(pos[0], pos[1])) throw new IndexOutOfBoundsException("Grid Index Out of bound");
		data.set((int)(pos[0]*sizeX + pos[1]), val);
	}
	/**
	 * Set the value of a positon in the grid
	 * 
	 * @param pos Coordinate position of the grid
	 * @param val Value to set
	 */
	public void setVal(Coord pos, T val) {
		if (!isInBound(pos.x(), pos.y())) throw new IndexOutOfBoundsException("Grid Index Out of bound");
		data.set((int)(pos.x()*sizeX + pos.y()), val);
	}

	/**
	 * Get the value of a position in the grid
	 * @param x x position
	 * @param y y position
	 * @return Value at the position, if out of bound then return out of bould value
	 */
	public T getVal(long x, long y) {
		if (!isInBound(x, y)) return outOfBoundVal;
		return data.get((int)(x*sizeX + y));
	}
	/**
	 * Get the value of a position in the grid
	 * @param pos position array of the grid as [x, y]
	 * @return Value at the position, if out of bound then return out of bould value
	 */
	public T getVal(Integer[] pos) {
		if (!isInBound(pos[0], pos[1])) return outOfBoundVal;
		return data.get((int)(pos[0]*sizeX + pos[1]));
	}
	/**
	 * Get the value of a position in the grid
	 * @param pos Coordiante position of the grid
	 * @return Value at the position, if out of bound then return out of bould value
	 */
	public T getVal(Coord pos) {
		if (!isInBound(pos.x(), pos.y())) return outOfBoundVal;
		return data.get((int)(pos.x()*sizeX + pos.y()));
	}

	/**
	 * Size of X bound getter method
	 * @return Size of X bound
	 */
	public long sizeX() { return this.sizeX; };
	/**
	 * Size of Y bound getter method
	 * @return Size of Y bound
	 */
	public long sizeY() { return this.sizeY; };
}
