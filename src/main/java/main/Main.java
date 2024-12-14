package main;

import day1.Day1;
import day2.Day2;
import day3.Day3;
import day4.Day4;
import day5.Day5;
import day6.Day6;
import day7.Day7;
import day8.Day8;
import day9.Day9;
import day10.Day10;
import day11.Day11;
import day12.Day12;
import day13.Day13;
import day14.Day14;

import logger.Logger;
import input.Input;

public class Main {
	public static void main(String[] arg) {
		Input inputManager = new Input("/main", "/test", "day%d.txt");
		if (arg.length == 1 && arg[0].equals("fetch")) {
			Logger.warn("TODO: implement input fetcher");
			return;
		} else if (arg.length == 1 && arg[0].equals("test")) {
			inputManager.useTest();
			Logger.log("Using test data");
		}

		new Day1(inputManager.getDay(1)).runDay();
		new Day2(inputManager.getDay(2)).runDay();
		new Day3(inputManager.getDay(3)).runDay();
		new Day4(inputManager.getDay(4)).runDay();
		new Day5(inputManager.getDay(5)).runDay();
		//new Day6(inputManager.getDay(6)).runDay(); // Long running day (~5s)
		//new Day7(inputManager.getDay(7)).runDay(); // Long running day (~2s)
		new Day8(inputManager.getDay(8)).runDay();
		new Day9(inputManager.getDay(9)).runDay();
		new Day10(inputManager.getDay(10)).runDay();
		new Day11(inputManager.getDay(11)).runDay();
		//new Day12(inputManager.getDay(12)).runDay();
		new Day13(inputManager.getDay(13)).runDay();
		new Day14(inputManager.getDay(14)).runDay();
	}
}
