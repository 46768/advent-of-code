package solutions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import dayBase.DayBase;
import logger.Logger;
import grid.Grid;
import geomUtil.Coord;
import input.InputData;

class AStar {

}

public class D16RaindeerMaze extends DayBase<Grid<Character>> {
	public D16RaindeerMaze(InputData dat) {
		super(dat);
	}

	protected Grid<Character> parseInput(ArrayList<String> dat) {
		return new Grid<Character>(1, 1, '.', '!');		
	}

	public void part1() {}
	public void part2() {}
}
