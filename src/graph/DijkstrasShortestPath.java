package graph;

import java.util.List;
import java.util.Stack;

/*
 * Dijkstra's Shortest Path algorithm is a BFS that adds edge values to nodes along the
 * shortest path.
 */
public class DijkstrasShortestPath implements Graphs {

	private static void weightVertex(int[][] graph, boolean[] visited, int[] lengths, int currentVertex, int[] comeFrom) {
		if (!visited[currentVertex]) {
			visited[currentVertex] = true;
		}
		int minPathValue = Integer.MAX_VALUE;
		int minPathPtr = -1;
		for (int i = 0; i < graph.length; i++) {
			if (graph[currentVertex][i] > 0 && !visited[i]) {
				int thisWeight = lengths[currentVertex] + graph[currentVertex][i];
				if (lengths[i] > thisWeight) {
					lengths[i] = thisWeight;
					comeFrom[i] = currentVertex;
				}
				if (minPathValue > thisWeight) {
					minPathValue = thisWeight;
					minPathPtr = i;
				}
			}
		}
		if (minPathValue < Integer.MAX_VALUE) {
			weightVertex(graph, visited, lengths, minPathPtr, comeFrom);
		}
	}
	private static List<Integer> shortestPath(int[] comeFrom, int currentVertex, int endVertex) {
		Stack<Integer> stack = new Stack<>();
		stack.push(currentVertex);
		while (stack.size() > 0) {
			for (int i = 0; i < comeFrom.length; i++) {
				if (comeFrom[i] == currentVertex && !stack.contains(i)) {
					stack.push(i);
					currentVertex = i;
				}
			}
			if (stack.peek() == endVertex) {
				break;
			}
			currentVertex = stack.peek();
		}
		return stack;
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Must specify graph.");
			return;
		}
		int[][] selectedGraph = args[0].equals("0") ? UndirectedGRAPH : DirectedGRAPH;
		boolean[] visited = new boolean[selectedGraph.length];
		int[] lengths = new int[selectedGraph.length];
		int[] comeFrom = new int[selectedGraph.length];
		for (int i = 1; i < selectedGraph.length; i++) {
			lengths[i] = Integer.MAX_VALUE;		// Initial length is infinity.
			comeFrom[i] = -1;
		}
		weightVertex(selectedGraph, visited, lengths, 0, comeFrom);
		for(int i = 0; i < lengths.length; i++) {
			System.out.println(VertexName[i] + " (" + (comeFrom[i] == -1 ? "^,^" : lengths[i] + "," + VertexName[comeFrom[i]]) + "," + comeFrom[i] + ") ");
		}
		System.out.println("Shortest Path " + shortestPath(comeFrom, 0, 5));
	}
}
