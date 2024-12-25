package main;

import solutions.*;

import logger.Logger;
import input.Input;

/**
 * <pre>
 * Main entry point for all solutions, Current day: 23
 * example use case:
 *     
 *     java /path/to/jar.jar [Options]
 *
 *  Options:
 *      fetch - fetch input from advent of code's server (Not yet implemented)
 *      test - use test data directory
 *  </pre>
 */
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

		new D1HistorianHysteria(inputManager.getDay(1)).runDay();
		new D2RedNosedReports(inputManager.getDay(2)).runDay();
		new D3MullItOver(inputManager.getDay(3)).runDay();
		new D4CeresSearch(inputManager.getDay(4)).runDay();
		new D5PrintQueue(inputManager.getDay(5)).runDay();
		new D6GuardGallivant(inputManager.getDay(6)).runDay(); // Long running day (~5s)
		new D7BridgeRepair(inputManager.getDay(7)).runDay();
		new D8ResonantCollinearity(inputManager.getDay(8)).runDay();
		new D9DiskFragmenter(inputManager.getDay(9)).runDay();
		new D10HoofIt(inputManager.getDay(10)).runDay();
		new D11PlutonianPebbles(inputManager.getDay(11)).runDay();
		// new D12GardenGroups(inputManager.getDay(12)).runDay(); // Non-working solution
		new D13ClawContraption(inputManager.getDay(13)).runDay();
		new D14RestroomRedoubt(inputManager.getDay(14)).runDay();
		new D15WarehouseWoes(inputManager.getDay(15)).runDay();
		// new D16RaindeerMaze(inputManager.getDay(16)).runDay();
		new D17ChronospatialComputer(inputManager.getDay(17)).runDay();
		new D18RAMRun(inputManager.getDay(18)).runDay();
		new D19LinenLayout(inputManager.getDay(19)).runDay();
		new D20RaceCondition(inputManager.getDay(20)).runDay();
		//new D21KeypadConundrum(inputManager.getDay(21)).runDay();
		new D22MonkeyMarket(inputManager.getDay(22)).runDay();
		new D23LANParty(inputManager.getDay(23)).runDay();
		new D24CrossedWires(inputManager.getDay(24)).runDay();
	}
}
