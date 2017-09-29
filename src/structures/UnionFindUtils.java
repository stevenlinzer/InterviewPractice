package structures;

import java.util.List;

public class UnionFindUtils {
	public static void makeFriends(char[][] graph, UnionFind uf) {
		for (int i = 0; i < graph.length-1; i++) {
			for (int j = 0; j < graph[i].length-1; j++) {
				if (graph[i][j] == graph[i][j+1]) {
					uf.union((i*graph[i].length)+j, (i*graph[i].length)+j+1);
				}
				if (graph[i][j] == graph[i+1][j]) {
					uf.union((i*graph[i].length)+j, ((i+1)*graph[i].length)+j);
				}
			}
			if (graph[i][graph[i].length-1] == graph[i+1][graph[i].length-1]) {
				uf.union((i*graph[i].length)+graph[i].length-1, ((i+1)*graph[i].length)+graph[i].length-1);
			}
		}
		for (int j = 0; j < graph[graph.length-1].length-1; j++) {
			if (graph[graph.length-1][j] == graph[graph.length-1][j+1]) {
				uf.union(((graph.length-1)*graph[graph.length-1].length)+j, ((graph.length-1)*graph[graph.length-1].length)+j+1);
			}
		}
	}

	public static void makeFriends(String[] graph, UnionFind uf) {
		if (graph.length < 1) return; 		
		char[][] charGraph = new char[graph.length][graph[0].length()]; 		
		for (int i = 0; i < graph.length; i++) {
			charGraph[i] = graph[i].toCharArray();
		}
		makeFriends(charGraph, uf);
	}

	public static void printDetails(UnionFind uf) {
		System.out.println(uf);
		System.out.println("Max " + uf.getMaxTree());
		for (List<Integer> l : uf.getConnectedLists()) {
			System.out.println("Size " + l.size() + ", " + l);
		}
	}
}
