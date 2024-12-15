package solutions;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
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

	public Coord getPos() {
		return objPos;
	}
	public void setPos(Coord newCoord) {
		objPos = newCoord;
	}

	@Override
	public int hashCode() {
		return Objects.hash(objType, objPos.x(), objPos.y());
	}
	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (other == null || getClass() != other.getClass()) return false;
		WarehouseObject objOther = (WarehouseObject) other;
		Coord otherPos = objOther.getPos();
		return this.getType() == objOther.getType() && objPos.equals(otherPos);
	}
	@Override
	public String toString() {
		return String.format("%c,%d,%d", getType(), objPos.x(), objPos.y());
	}

	public void linkObject(WarehouseObject otherObject) {
		this.linkedObject = otherObject;
	}

	public long getGPS() {
		return objPos.x()*100 + objPos.y();
	}
	public char getType() {
		if (objType == Type.Boxes) {
			if (getLinked() != null) {
				if (objPos.subtract(getLinked().getPos()).equals(Coord.withY(-1))) {
					return '[';
				} else {
					return ']';
				}
			}
			return 'O';
		} else if (objType == Type.Walls) return '#';
		return '@';
	}
	public WarehouseObject getLinked() {
		return this.linkedObject;
	}

	public boolean canMove(Coord direction, HashMap<Coord, WarehouseObject> warehouseObjects, HashSet<WarehouseObject> visited) {
		// Get all boxes in a row, if hit a wall then cant move, starting from current object
		for (Coord coord = objPos; warehouseObjects.containsKey(coord); coord = coord.add(direction)) {
			WarehouseObject obj = warehouseObjects.get(coord);
			WarehouseObject linked = obj.getLinked();
			visited.add(obj);
			// if linked isnt from what called this method then recursively search
			if (linked != null && !visited.contains(linked)) {
				if (!linked.canMove(direction, warehouseObjects, visited)) {
					return false;
				}
			}
			if (obj.getType() == '#') return false;
		}

		return true;
	}
	// Return true if object moves, false otherwise
	// If theres an linked object then also act on the object and share the same verdict
	public HashSet<WarehouseObject> getMoving(Coord direction, HashMap<Coord, WarehouseObject> warehouseObjects) {
		HashSet<WarehouseObject> visited = new HashSet<>();
		if (canMove(direction, warehouseObjects, visited)) {
			return visited;
		}
		return new HashSet<WarehouseObject>();
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
	private HashMap<Coord, WarehouseObject> objects;
	private HashMap<WarehouseObject, WarehouseObject> linkedObjects;
	private int sizeX;
	private int sizeY;
	private boolean isBigWarehouse;
	private ObjectFactory boxFactory;
	private ObjectFactory wallFactory;
	private WarehouseObject robot;

	public enum Type {
		Normal,
		Big,
	}

	private void putObject(WarehouseObject obj, Coord coord) {
		WarehouseObject linked = obj.getLinked();
		objects.put(coord, obj);
		if (linked != null) {
			objects.put(coord.add(Coord.withY(1)), linked);
			linkedObjects.put(obj, linked);
			linkedObjects.put(linked, obj);
		}
	}

	private void moveObjects(HashSet<WarehouseObject> objs, Coord dir) {
		@SuppressWarnings("unchecked")
		HashMap<Coord, WarehouseObject> objectsClone = 
		(HashMap<Coord, WarehouseObject>)objects.clone();
		for (WarehouseObject obj : objs) {
			objectsClone.remove(obj.getPos());
		}
		for (WarehouseObject obj : objs) {
			Coord newCoord = obj.getPos().add(dir);
			objectsClone.put(newCoord, obj);
			obj.setPos(newCoord);
		}

		objects = objectsClone;
	}

	public Warehouse(Type warehouseType, ArrayList<String> map) {
		objects = new HashMap<>();
		linkedObjects = new HashMap<>();
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
					putObject(robot, coord);
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

	public void inputCommand(char cmd) {
		Coord direction;
		if (cmd == '^') {
			direction = new Coord(-1, 0);
		} else if (cmd == '>') {
			direction = new Coord(0, 1);
		} else if (cmd == 'v') {
			direction = new Coord(1, 0);
		} else {
			direction = new Coord(0, -1);
		}
		moveObjects(robot.getMoving(direction, objects), direction);
		//printCurrentState();
	}

	public void printCurrentState() {
		int yScale = isBigWarehouse ? 2 : 1;
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY*yScale; y++) {
				Coord coord = new Coord(x, y);
				if (objects.containsKey(coord)) {
					Logger.print(objects.get(coord).getType());
				} else {
					Logger.print('.');
				}
			}
			Logger.print('\n');
		}
	}

	public long getBoxesGPSSum() {
		long sum = 0;
		char boxChar = isBigWarehouse ? '[' : 'O';
		for (Coord key : objects.keySet()) {
			WarehouseObject obj = objects.get(key);
			if (obj.getType() == boxChar) {
				sum += obj.getGPS();
			}
		}
		return sum;
	}
}

public class D15WarehouseWoes extends DayBase<ArrayList<Warehouse>> {
	ArrayList<Character> commands;

	public D15WarehouseWoes(InputData dat) {
		super(dat);
	}

	protected ArrayList<Warehouse> parseInput(ArrayList<String> dat) {
		commands = new ArrayList<>();
		ArrayList<String> map = new ArrayList<>();
		ArrayList<Warehouse> warehouses = new ArrayList<>();
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

		warehouses.add(new Warehouse(Warehouse.Type.Normal, map));
		warehouses.add(new Warehouse(Warehouse.Type.Big, map));

		return warehouses;
	}

	public void part1() {
		Warehouse warehouse = data.get(0);
		//warehouse.printCurrentState();
		for (char cmd : commands) {
			warehouse.inputCommand(cmd);
		}
		//warehouse.printCurrentState();
		Logger.log("GPS sum of all boxes: %d", warehouse.getBoxesGPSSum());
	}

	public void part2() {
		Warehouse warehouse = data.get(1);
		//warehouse.printCurrentState();
		for (char cmd : commands) {
			warehouse.inputCommand(cmd);
		}
		//warehouse.printCurrentState();
		Logger.log("GPS sum of all big boxes: %d", warehouse.getBoxesGPSSum());
	}
}
