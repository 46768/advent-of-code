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

public class Main {
	public static void main(String[] arg) {
		if (arg.length == 1 && arg[0].equals("fetch")) {
			System.out.println("TODO: implement input fetcher");
			return;
		}
		
		new Day1("data/day1.txt").runDay();
		new Day2("data/day2.txt").runDay();
		new Day3("data/day3.txt").runDay();
		new Day4("data/day4.txt").runDay();
		new Day5("data/day5.txt").runDay();
		//new Day6("data/day6.txt").runDay(); // Long running Day (~4 s)
		//new Day7("data/day7.txt").runDay(); // Long running Day (~2 s)
		new Day8("data/day8.txt").runDay();
		new Day9("data/day9.txt").runDay();
		new Day10("data/day10.txt").runDay();
	}
}
