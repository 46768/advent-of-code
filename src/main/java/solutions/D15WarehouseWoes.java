package solutions;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Objects;

import dayBase.DayBase;
import input.InputData;
import logger.Logger;
import geomUtil.Coord;

class WarehouseObject {
	public enum Type {
		Walls,
		Boxes,
		Robot
	}

	private Type objType;
	private Coord objPos;
	private WarehouseObject linkedObject;
	public WarehouseObject(Type type, Coord initPos) {
		objType = type;
		objPos = initPos;
	}

	@Override
	public int hashCode() {
		return Objects.hash(objType, objPos.x(), objPos.y());
	}
	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (other == null || getClass() != other.getClass()) return false;
		WarehouseObject coordOther = (WarehouseObject) other;
		return this.getType() == coordOther.getType() && this.hashCode() == coordOther.hashCode();
	}
	@Override
	public String toString() {
		return String.format("%c, %d, %d", getType(), objPos.x(), objPos.y());
	}

	public void linkObject(WarehouseObject otherObject) {
		this.linkedObject = otherObject;
	}

	public long getGPS() {
		return objPos.x()*100 + objPos.y();
	}
	public char getType() {
		if (objType == Type.Boxes) return 'O';
		else if (objType == Type.Walls) return '#';
		return '@';
	}
	public WarehouseObject getLinked() {
		return this.linkedObject;
	}

	private void move(Coord direction, HashMap<Coord, WarehouseObject> warehouseObjects) {
		warehouseObjects.remove(objPos);
		objPos = objPos.add(direction);
		warehouseObjects.put(objPos, this);
	}

	public boolean canActOn(Coord direction, HashMap<Coord, WarehouseObject> warehouseObjects, WarehouseObject src) {
		if (objType == Type.Walls) {
			return false;
		}
		if (getLinked() != null && !getLinked().equals(src)) {
			if (!getLinked().canActOn(direction, warehouseObjects, src)) {
				return false;
			}
		}
		for (Coord coord = objPos; warehouseObjects.containsKey(coord); coord = coord.add(direction)) {
			WarehouseObject obj = warehouseObjects.get(coord);
			if (obj.getType() == '#') return false;
		}

		return true;
	}
	// Return true if object moves, false otherwise
	// If theres an linked object then also act on the object and share the same verdict
	public boolean actOn(Coord direction, HashMap<Coord, WarehouseObject> warehouseObjects, WarehouseObject src) {
		if (canActOn(direction, warehouseObjects, this)) {
			ArrayDeque<WarehouseObject> queue = new ArrayDeque<>();
			for (Coord coord = objPos; warehouseObjects.containsKey(coord); coord = coord.add(direction)) {
				WarehouseObject obj = warehouseObjects.get(coord);
				queue.add(obj);
			}
			Logger.debug(getLinked());
			if (getLinked() != null) {
				queue.add(getLinked());
			}
			while (!queue.isEmpty()) {
				WarehouseObject obj = queue.removeLast();
				obj.move(direction, warehouseObjects);
			}

			return true;
		}
		return false;
	}
}

class ObjectFactory {
	private WarehouseObject.Type type;
	private boolean isBigFactory;
	public ObjectFactory(WarehouseObject.Type type, boolean isBigFactory) {
		this.type = type;	
		this.isBigFactory = isBigFactory;
	}

	public WarehouseObject createObject(Coord coord) {
		if (isBigFactory) {
			WarehouseObject objLeft = new WarehouseObject(type, coord);
			WarehouseObject objRight = new WarehouseObject(type, coord.add(Coord.withY(1)));
			objLeft.linkObject(objRight);
			objRight.linkObject(objLeft);
			return objLeft;
		}
		return new WarehouseObject(type, coord);
	}
}

class Warehouse {
	HashMap<Coord, WarehouseObject> objects;
	int sizeX;
	int sizeY;
	boolean isBigWarehouse;
	ObjectFactory boxFactory;
	ObjectFactory wallFactory;
	WarehouseObject robot;

	public enum Type {
		Normal,
		Big,
	}

	private void putObject(WarehouseObject obj, Coord coord) {
		objects.put(coord, obj);
		if (obj.getLinked() != null) {
			objects.put(coord.add(Coord.withY(1)), obj.getLinked());
		}
	}

	public Warehouse(Type warehouseType, ArrayList<String> map) {
		objects = new HashMap<>();
		isBigWarehouse = warehouseType == Type.Big;
		boxFactory = new ObjectFactory(WarehouseObject.Type.Boxes, isBigWarehouse);
		wallFactory = new ObjectFactory(WarehouseObject.Type.Walls, isBigWarehouse);

		sizeX = map.size();
		sizeY = map.get(0).length();

		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				Coord coord = new Coord(x, y);
				if (isBigWarehouse) coord = coord.multiply(new Coord(1, 2));
				char objType = map.get(x).charAt(y);
				if (objType == '@') {
					robot = new WarehouseObject(WarehouseObject.Type.Robot, coord);
				} else if (objType == 'O') {
					WarehouseObject box = boxFactory.createObject(coord);
					putObject(box, coord);
				} else if (objType == '#') {
					WarehouseObject wall = wallFactory.createObject(coord);
					putObject(wall, coord);
				}
			}
		}
	}
}

public class D15WarehouseWoes extends DayBase<HashMap<Coord, WarehouseObject>> {
	int sizeX;
	int sizeY;
	int sizeBX;
	int sizeBY;
	HashMap<Coord, WarehouseObject> bigData;
	ArrayList<Character> commands;
	WarehouseObject robot;
	WarehouseObject robotBig;

	public D15WarehouseWoes(InputData dat) {
		super(dat);
	}

	protected HashMap<Coord, WarehouseObject> parseInput(ArrayList<String> dat) {
		bigData = new HashMap<>();
		commands = new ArrayList<>();
		ArrayList<String> map = new ArrayList<>();
		boolean isMapSection = true;
		for (String line : dat) {
			if (line.equals("")) {
				isMapSection = false;
				continue;
			}
			if (isMapSection) map.add(line);
			else {
				for (int i = 0; i < line.length(); i++) {
					commands.add(line.charAt(i));
				}
			}
		}
		sizeX = map.size();
		sizeY = map.get(0).length();
		sizeBX = sizeX;
		sizeBY = sizeY*2;
		HashMap<Coord, WarehouseObject> objects = new HashMap<>();

		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				char warehouseObj = map.get(x).charAt(y);
				Coord coord = new Coord(x, y);
				Coord coordBig = new Coord(x, y*2);
				if (warehouseObj == '@') {
					objects.put(coord, new WarehouseObject(WarehouseObject.Type.Robot, coord));
					bigData.put(coordBig, new WarehouseObject(WarehouseObject.Type.Robot, coordBig));
				} else if (warehouseObj == 'O') {
					Logger.debug("putting boxes");
					objects.put(coord, new WarehouseObject(WarehouseObject.Type.Boxes, coord));
					WarehouseObject boxLeft = new WarehouseObject(WarehouseObject.Type.Boxes, coordBig);
					WarehouseObject boxRight = new WarehouseObject(WarehouseObject.Type.Boxes, coordBig.add(Coord.withY(1)));
					Logger.debug("Linking %s with %s", boxLeft, boxRight);
					boxLeft.linkObject(boxRight);
					Logger.debug("Linked %s with %s", boxLeft, boxLeft.getLinked());
					Logger.debug("Linking %s with %s", boxRight, boxLeft);
					boxRight.linkObject(boxLeft);
					Logger.debug("Linked %s with %s", boxRight, boxRight.getLinked());
					bigData.put(coordBig, boxLeft);
					bigData.put(coordBig.add(Coord.withY(1)), boxRight);
				} else if (warehouseObj == '#') {
					objects.put(coord, new WarehouseObject(WarehouseObject.Type.Walls, coord));
					bigData.put(coordBig, new WarehouseObject(WarehouseObject.Type.Walls, coordBig));
					bigData.put(coordBig.add(Coord.withY(1)), new WarehouseObject(WarehouseObject.Type.Walls, coordBig.add(Coord.withY(1))));
				}
				if (warehouseObj == '@') {
					robot = objects.get(coord);
					robotBig = bigData.get(coordBig);
				}
			}
		}

		return objects;
	}

	private void printCurrentState(HashMap<Coord, WarehouseObject> state, int sx, int sy) {
		for (int x = 0; x < sx; x++) {
			for (int y = 0; y < sy; y++) {
				Coord coord = new Coord(x, y);
				if (state.containsKey(coord)) {
					Logger.print(state.get(coord).getType());
				} else {
					Logger.print('.');
				}
			}
			Logger.print('\n');
		}
	}

	public void part1() {
		long gpsSum = 0;
		//printCurrentState(data, sizeX, sizeY);
		for (char cmd : commands) {
			if (cmd == '^') {
				robot.actOn(new Coord(-1, 0), data, robot);
			} else if (cmd == '>') {
				robot.actOn(new Coord(0, 1), data, robot);
			} else if (cmd == 'v') {
				robot.actOn(new Coord(1, 0), data, robot);
			} else if (cmd == '<') {
				robot.actOn(new Coord(0, -1), data, robot);
			}
			//Logger.debug(cmd);
			//printCurrentState(data, sizeX, sizeY);
		}
		for (Coord key : data.keySet()) {
			WarehouseObject obj = data.get(key);
			if (obj.getType() == 'O') {
				gpsSum += obj.getGPS();
			}
		}
		//printCurrentState(data, sizeX, sizeY);
		Logger.log("GPS sum of all boxes: %d", gpsSum);
	}

	public void part2() {
		long gpsSum = 0;
		//printCurrentState(bigData, sizeBX, sizeBY);
		for (char cmd : commands) {
			if (cmd == '^') {
				robotBig.actOn(new Coord(-1, 0), bigData, robotBig);
			} else if (cmd == '>') {
				robotBig.actOn(new Coord(0, 1), bigData, robotBig);
			} else if (cmd == 'v') {
				robotBig.actOn(new Coord(1, 0), bigData, robotBig);
			} else if (cmd == '<') {
				robotBig.actOn(new Coord(0, -1), bigData, robotBig);
			}
			//Logger.debug(cmd);
			//printCurrentState(bigData, sizeBX, sizeBY);
		}
		/*for (Coord key : bigData.keySet()) {
			WarehouseObject obj = bigData.get(key);
			if (obj.getType() == 'O') {
				gpsSum += obj.getGPS();
			}
		}*/
		//printCurrentState(bigData, sizeBX, sizeBY);
		Logger.log("GPS sum of all boxes: %d", gpsSum);
	}
}
