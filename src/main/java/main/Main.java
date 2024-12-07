package main;

import dayBase.DayBase;
import day1.Day1;
import day2.Day2;
import day3.Day3;
import day4.Day4;
import day5.Day5;
import day6.Day6;
import day7.Day7;

public class Main {
	public static void main(String[] arg) {
		DayBase.runDay(Day1.class, "data/day1.txt");
		DayBase.runDay(Day2.class, "data/day2.txt");
		DayBase.runDay(Day3.class, "data/day3.txt");
		DayBase.runDay(Day4.class, "data/day4.txt");
		DayBase.runDay(Day5.class, "data/day5.txt");
		//DayBase.runDay(Day6.class, "data/day6.txt");
		DayBase.runDay(Day7.class, "data/day7.txt");
	}
}
