package day8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import dayBase.DayBase;
import fileReader.FileReader;
import logger.Logger;

public class Day8 extends DayBase<HashMap<Character, ArrayList<Integer>>> {
	private int sx;
	private int sy;
	public Day8(String path) {
		super(path);
	}

	protected HashMap<Character, ArrayList<Integer>> parseInput(String path) {
		ArrayList<String> dat = FileReader.readData(path);
		HashMap<Character, ArrayList<Integer>> charMatrix = new HashMap<>();
		sx = dat.size();
		sy = dat.get(0).length();
		for (int i = 0; i < sx; i++) {
			String line = dat.get(i);
			for (int j = 0; j < sy; j++) {
				char chatAtPos = line.charAt(j);

				// Ignore empty space and antinodes
				if (chatAtPos == '.') continue;
				if (chatAtPos == '#') continue;
				charMatrix.putIfAbsent(chatAtPos, new ArrayList<>());

				// Stored position as compacted 1d array index
				charMatrix.get(chatAtPos).add((i*sx) + j);
			}
		}
		return charMatrix;
	}

	private boolean positionInbound(int[] a) {
		if (0 > a[0] || a[0] >= sx) return false;
		if (0 > a[1] || a[1] >= sy) return false;
		return true;
	}

	public void part1() {
		HashSet<Integer> antinodes = new HashSet<>();

		for (char key : data.keySet()) {
			ArrayList<Integer> antennas = data.get(key);
			for (int i = 0; i < antennas.size()-1; i++) {
				for (int j = i+1; j < antennas.size(); j++) {
					int ant1 = antennas.get(i);
					int ant2 = antennas.get(j);

					int[] ant1Pos = {Math.floorDiv(ant1, sx), ant1 % sx};
					int[] ant2Pos = {Math.floorDiv(ant2, sx), ant2 % sx};

					// Assume antPos and oAntPos is a vector, NOT a compacted index
					// antiPos = antPos + (antPos - oAntPos) -> 2*antPos - oAntPos
					int[] anti1Pos = {2*ant1Pos[0] - ant2Pos[0], 2*ant1Pos[1] - ant2Pos[1]};
					int[] anti2Pos = {2*ant2Pos[0] - ant1Pos[0], 2*ant2Pos[1] - ant1Pos[1]};

					int anti1 = anti1Pos[0]*sx + anti1Pos[1];
					int anti2 = anti2Pos[0]*sx + anti2Pos[1];

					if (positionInbound(anti1Pos)) antinodes.add(anti1);
					if (positionInbound(anti2Pos)) antinodes.add(anti2);
				}
			}
		}
		Logger.log("Part 1: %d", antinodes.size());
	}

	private void repeatAntinode(HashSet<Integer> antinodes, int[] antPos, int[] offset) {
		while (positionInbound(antPos)) {
			int[] antiPos = {antPos[0] + offset[0], antPos[1] + offset[1]};
			int anti = antiPos[0]*sx + antiPos[1];
			if (positionInbound(antiPos)) {
				antinodes.add(anti);
				antPos[0] = antiPos[0];
				antPos[1] = antiPos[1];
			} else break;
		}
	}

	public void part2() {
		HashSet<Integer> antinodes = new HashSet<>();

		for (char key : data.keySet()) {
			ArrayList<Integer> antennas = data.get(key);
			for (int i = 0; i < antennas.size()-1; i++) {
				for (int j = i+1; j < antennas.size(); j++) {
					int ant1 = antennas.get(i);
					int ant2 = antennas.get(j);
					antinodes.add(ant1);
					antinodes.add(ant2);

					int[] ant1Pos = {Math.floorDiv(ant1, sx), ant1 % sx};
					int[] ant2Pos = {Math.floorDiv(ant2, sx), ant2 % sx};
					int[] anti1Offset = {ant1Pos[0] - ant2Pos[0], ant1Pos[1] - ant2Pos[1]};
					int[] anti2Offset = {ant2Pos[0] - ant1Pos[0], ant2Pos[1] - ant1Pos[1]};

					repeatAntinode(antinodes, ant1Pos, anti1Offset);
					repeatAntinode(antinodes, ant2Pos, anti2Offset);
				}
			}
		}
		Logger.log("Part 2: %d", antinodes.size());
	}
}
