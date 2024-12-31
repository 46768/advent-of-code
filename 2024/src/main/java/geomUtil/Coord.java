package geomUtil;

import java.util.function.BiFunction;
import java.util.ArrayList;
import java.util.Objects;

public class Coord {
	public static final Coord ZERO_COORD = new Coord(0, 0);
	public static final Coord NEG_IDENTITY_COORD = new Coord(-1, -1);

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
	public static Coord Swap(Coord coord, Coord empty) {
		return new Coord(coord.y(), coord.x());
	}

	private long[] pos;
	private long[] lastOperation;
	public Coord(long x, long y) {
		pos = new long[]{x, y};
		lastOperation = new long[]{x, y};
	}
	public static Coord withX(long x) {
		return new Coord(x, 0);
	}
	public static Coord withY(long y) {
		return new Coord(0, y);
	}
	public static Coord withX(Coord pos) {
		return new Coord(pos.x(), 0);
	}
	public static Coord withY(Coord pos) {
		return new Coord(0, pos.y());
	}
	public static Coord expand(long ord) {
		return new Coord(ord, ord);
	}

	public long x() {
		return pos[0];
	}
	public long y() {
		return pos[1];
	}

	private Coord applyMethod(BiFunction<Coord, Coord, Coord> fn, Coord coord) {
		Coord newCoord = fn.apply(this, coord);
		lastOperation = new long[]{newCoord.x(), newCoord.y()};
		return newCoord;
	}

	public Coord add(Coord coord) {
		return applyMethod(Coord::Add, coord);
	}
	public Coord subtract(Coord coord) {
		return applyMethod(Coord::Subtract, coord);
	}
	public Coord multiply(Coord coord) {
		return applyMethod(Coord::Multiply, coord);
	}
	public Coord divide(Coord coord) {
		return applyMethod(Coord::Divide, coord);
	}
	public Coord swap() {
		return applyMethod(Coord::Swap, ZERO_COORD);
	}
	// Set positon to lastOperation's result
	public Coord set() {
		pos[0] = lastOperation[0];
		pos[1] = lastOperation[1];
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
	@Override
	public Coord clone() {
		return new Coord(x(), y());
	}

	public ArrayList<Coord> getSurroundingCoord(boolean extendedDirection) {
		ArrayList<Coord> surroundings = new ArrayList<>();
		
		for (long xOff : new long[]{-1, 0, 1}) {
			for (long yOff : new long[]{-1, 0, 1}) {
				if ((xOff*yOff == 0 || extendedDirection) && (xOff != 0 || yOff != 0)) {
					surroundings.add(Add(this, new Coord(xOff, yOff)));
				}
			}
		}

		return surroundings;
	}
}
