package graph;

import java.util.List;
import java.util.Stack;

public class FloydWarshall implements Graphs {

	private static void printTables(int[][] distance, int[][] comeFrom) {
		System.out.print("  ");
		for (int i = 0; i < distance.length; i++) {
			System.out.print(" "+ VertexName[i] + "  ");
		}
		System.out.print(" |  ");
		for (int i = 0; i < distance.length; i++) {
			System.out.print(" "+ VertexName[i] + " ");
		}
		System.out.println();
		for (int i = 0; i < distance.length; i++) {
			System.out.print(VertexName[i] + " ");
			for (int j = 0; j < distance.length; j++) {
				if (distance[i][j] > Integer.MAX_VALUE - 1) {
					System.out.print(" ~  ");
				} else {
					System.out.format("%2d  ", distance[i][j]);
				}
			}
			System.out.print(" |   ");
			for (int j = 0; j < comeFrom.length; j++) {
				System.out.print(comeFrom[i][j] + "  ");
			}
			System.out.println();
		}

	}
	private static List<Integer> shortestPath(int[][] comeFrom, int currentVertex, int endVertex) {
		Stack<Integer> stack = new Stack<>();
		stack.push(currentVertex);
		while (stack.size() > 0) {
			for (int i = 0; i < comeFrom.length; i++) {
				if (comeFrom[currentVertex][i] == currentVertex && !stack.contains(i)) {
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
		if (Integer.parseInt(args[0]) < 2) {
			int[][] selectedGraph = args[0].equals("0") ? UndirectedGRAPH : DirectedGRAPH;
			int[][] distance = new int[selectedGraph.length][selectedGraph.length];
			int[][] comeFrom = new int[selectedGraph.length][selectedGraph.length];
			for (int i = 0; i < distance.length; i++) {
				for (int j = 0; j < distance[0].length; j++) {
					distance[i][j] = selectedGraph[i][j] > 0 ? selectedGraph[i][j] : (selectedGraph[i][j] < 0 ? Integer.MAX_VALUE : 0);
					comeFrom[i][j] = selectedGraph[i][j] > 0 ? i : (selectedGraph[i][j] < 0 ? -1 : 0);
				}
			}
			printTables(distance, comeFrom);
			System.out.println("Original");
			for (int k = 0; k < selectedGraph.length; k++) {
				for (int i = 0; i < selectedGraph.length; i++) {
					for (int j = 0; j < selectedGraph[i].length; j++) {
						if (k != i && k != j && distance[i][k] < Integer.MAX_VALUE && distance[k][j] < Integer.MAX_VALUE && distance[i][j] > distance[i][k] + distance[k][j]) {
							distance[i][j] = distance[i][k] + distance[k][j];
							comeFrom[i][j] = k;
						}
					}
				}
				printTables(distance, comeFrom);
				System.out.println("Iteration for " + VertexName[k]);
			}
			printTables(distance, comeFrom);
			System.out.println("Final");
			System.out.println("Shortest Path " + shortestPath(comeFrom, 1, 6));
		}
	}
}
