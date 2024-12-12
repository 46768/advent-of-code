package geomUtil;

import java.util.Arrays;
import java.util.function.BiFunction;

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
}
