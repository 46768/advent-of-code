package solutions;

import java.util.*;

import dayBase.DayBase;
import input.InputData;
import logger.Logger;

public class D23LANParty extends DayBase<HashMap<String, HashSet<String>>> {
	public D23LANParty(InputData dat) {
		super(dat);
	}

	protected HashMap<String, HashSet<String>> parseInput(ArrayList<String> dat) {
		HashMap<String, HashSet<String>> connections = new HashMap<>();

		for (String line : dat) {
			String[] computers = line.split("\\-");
			connections.putIfAbsent(computers[0], new HashSet<>());
			connections.get(computers[0]).add(computers[1]);

			connections.putIfAbsent(computers[1], new HashSet<>());
			connections.get(computers[1]).add(computers[0]);
		}

		return connections;
	}

	private HashSet<HashSet<String>> cycleBFS(String node, String src, String superSrc, int depth, 
			String path, HashSet<HashSet<String>> cycles) {
		HashSet<String> nodeNeighbor = data.get(node);
		if (depth > 2) return cycles;
		for (String neighbor : nodeNeighbor) {
			if (neighbor != src) {
				// if path's length is 6 then there should be 2 nodes within the path string
				if (neighbor.equals(superSrc) && path.length() == 6) {
					String[] cycleNodes = path.substring(1).split(",");
					HashSet<String> cycle = new HashSet<>();
					cycle.add(cycleNodes[0]);
					cycle.add(cycleNodes[1]);
					cycle.add(node);
					cycles.add(cycle);
				} else {
					cycleBFS(neighbor, node, superSrc, depth+1, path+","+node, cycles);
				}
			}
		}
		return cycles;

	}

	public void part1() {
		// do bfs for cycle detection
		HashSet<HashSet<String>> hisotrianCycles = new HashSet<>();
		for (String src : data.keySet()) {
			if (src.startsWith("t")) {
				cycleBFS(src, src, src, 0, "", hisotrianCycles);
			}
		}

		Logger.log("inter-connected computers that have t prefix: %d", hisotrianCycles.size());
	}

	private HashSet<String> getFullyConnectedSubgraphAtNode(String node) {
		HashSet<String> graph = new HashSet<>(data.get(node));
		HashSet<String> neighbors = data.get(node);
		graph.add(node);
		Logger.log(node);

		for (String neighbor : neighbors) {
			HashSet<String> neighborNeighbors = new HashSet<>(data.get(neighbor));
			neighborNeighbors.add(neighbor);
			Logger.debug(graph);
			Logger.debug(neighborNeighbors);
			Logger.debug(neighbor);
			Logger.newline();

			graph.retainAll(neighborNeighbors);
			if (graph.size() == 0) {
				break;
			}
		}

		Logger.warn(graph);
		return graph;
	}

	private void bronKerbosh(HashSet<String> R, HashSet<String> P, HashSet<String> X, List<HashSet<String>> maximalClique) {
		HashSet<String> removedNodes = new HashSet<>();
		if (P.size() == 0 && X.size() == 0) {
			maximalClique.add(R);
		}
		for (String node : P) {
			HashSet<String> nextR = new HashSet<>(R);
			nextR.add(node);

			HashSet<String> nextP = new HashSet<>(P);
			nextP.retainAll(removedNodes);
			nextP.retainAll(data.get(node));

			HashSet<String> nextX = new HashSet<>(X);
			nextX.retainAll(data.get(node));
			bronKerbosh(nextR, nextP, nextX, maximalClique);
			removedNodes.add(node);
			X.add(node);
		}
	}

	public void part2() {
		HashSet<String> largestSubgraph = new HashSet<>();

		List<HashSet<String>> maxinalCliques = new ArrayList<>();
		bronKerbosh(new HashSet<>(), new HashSet<>(data.keySet()), new HashSet<>(), maxinalCliques);

		for (HashSet<String> clique : maxinalCliques) {
			if (clique.size() > largestSubgraph.size()) {
				largestSubgraph = clique;
			}
		}

		ArrayList<String> password = new ArrayList<>(largestSubgraph);
		Collections.sort(password);

		Logger.log("Largest subgraph password: %s", String.join(",", password));
	}
}
