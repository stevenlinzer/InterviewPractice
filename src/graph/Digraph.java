package graph;

import java.util.ArrayList;
import java.util.List;

public class Digraph {
	List<Integer>[] edges;
	public int[] indegree;
	public int E = 0;
	public Digraph(int v) {
		edges = new ArrayList[v];
		indegree = new int[v];
	}

	public void addEdge(int v, int w) {
		if (v < 0 || v >= edges.length) return;
		if (w < 0 || w >= edges.length) return;
		if (edges[v] == null) edges[v] = new ArrayList<Integer>();
		edges[v].add(w);
		E++;
		indegree[w]++;
	}

	Iterable<Integer> adj(int v) {
		return edges[v];
	}

	public int V() {return edges.length;}
	public int outdegree(int v) {return edges[v].size();}

	public Digraph reverse(Digraph D) {
		Digraph retVal = new Digraph(D.V());
		for (int i = 0; i < D.V(); i++) {
			for (int a : D.adj(i)) {
				retVal.addEdge(a, i);
			}
		}
		return retVal;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < edges.length; i++) {
			if (edges[i] != null) {
				sb.append(i+" - "+edges[i]);
			}
		}
		return sb.toString();
	}
}
