package geomUtil;

import java.util.function.BiFunction;
import java.util.ArrayList;
import java.util.Objects;

import logger.Logger;

public class Coord {
	public static Coord Add(Coord coord1, Coord coord2) {
		return new Coord(coord1.x()+coord2.x(), coord1.y()+coord2.y());
	}
	public static Coord Subtract(Coord coord1, Coord coord2) {
		return new Coord(coord1.x()-coord2.x(), coord1.y()-coord2.y());
	}
	public static Coord Multiply(Coord coord1, Coord coord2) {
		return new Coord(coord1.x()*coord2.x(), coord1.y()*coord2.y());
	}
	public static Coord Divide(Coord coord1, Coord coord2) {
		return new Coord(coord1.x()/coord2.x(), coord1.y()/coord2.y());
	}

	private int[] pos;
	public Coord(int x, int y) {
		pos = new int[]{x, y};
	}
	public static Coord withX(int x) {
		return new Coord(x, 0);
	}
	public static Coord withY(int y) {
		return new Coord(0, y);
	}
	public static Coord withX(Coord pos) {
		return new Coord(pos.x(), 0);
	}
	public static Coord withY(Coord pos) {
		return new Coord(0, pos.y());
	}

	public int x() {
		return pos[0];
	}
	public int y() {
		return pos[1];
	}

	private void applyMethod(BiFunction<Coord, Coord, Coord> fn, Coord coord) {
		Coord newCoord = fn.apply(this, coord);
		pos = new int[]{newCoord.x(), newCoord.y()};
	}

	public Coord add(Coord coord) {
		applyMethod(Coord::Add, coord);
		return this;
	}
	public Coord subtract(Coord coord) {
		applyMethod(Coord::Subtract, coord);
		return this;
	}
	public Coord multiply(Coord coord) {
		applyMethod(Coord::Multiply, coord);
		return this;
	}
	public Coord divide(Coord coord) {
		applyMethod(Coord::Divide, coord);
		return this;
	}

	@Override
	public String toString() {
		return String.format("%d,%d", x(), y());
	}
	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (other == null || getClass() != other.getClass()) return false;
		Coord coordOther = (Coord) other;
		return this.x() == coordOther.x() && this.y() == coordOther.y();
	}
	@Override
	public int hashCode() {
		return Objects.hash(x(), y());
	}

	public ArrayList<Coord> getSurroundingCoord(boolean extendedDirection) {
		ArrayList<Coord> surroundings = new ArrayList<>();
		
		for (int xOff : new int[]{-1, 0, 1}) {
			for (int yOff : new int[]{-1, 0, 1}) {
				if ((xOff*yOff == 0 || extendedDirection) && (xOff != 0 || yOff != 0)) {
					surroundings.add(Add(this, new Coord(xOff, yOff)));
				}
			}
		}

		return surroundings;
	}
}
