package structures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import topcoder.datascience.GrafixMask;

public class UnionFind {
	int array[];
	int size[];
	int maxTree;
	Set<Integer> emptySet = new HashSet<>();
	Map<Integer, List<Integer>> globalMap = new HashMap<>();
	public UnionFind(int N) {
		array = new int[N];
		size = new int[N];
		for (int i = 0; i < array.length; i++) {
			array[i] = i;	// Each node is it's own root
			size[i] = 1;	// with a tree size of one.
			emptySet.add(i);
		}
		maxTree = -1;
	}
	public void union(int a, int b) {
		int rootA = root(a);
		int rootB = root(b);
		if (rootA != rootB) {
			if (size[rootB] > size[rootA]) {
				array[rootA] = rootB;	// Change the root of A to be child of B.
				size[rootB] += size[rootA];	// Add the size of the old tree to the root of the new parent.
				updateMaxTree(rootB);
				if (emptySet.contains(a)) {
					emptySet.remove(a);
				}
				if (globalMap.containsKey(rootA)) {
					globalMap.get(rootB).addAll(globalMap.get(rootA));
					globalMap.remove(rootA);
				} else {
					globalMap.get(rootB).add(a);
				}
			} else {
				array[rootB] = rootA;	// Change the root of A to be child of B.
				size[rootA] += size[rootB];	// Add the size of the old tree to the root of the new parent.
				updateMaxTree(rootA);
				if (!globalMap.containsKey(rootA)) {
					List<Integer> tempList = new ArrayList<Integer>();
					tempList.add(a);
					tempList.add(b);
					globalMap.put(rootA, tempList);
				} else {
					if (globalMap.containsKey(rootB)) {
						globalMap.get(rootA).addAll(globalMap.get(rootB));
						globalMap.remove(rootB);
					} else {
						globalMap.get(rootA).add(b);
					}
				}
				if (emptySet.contains(b)) {	emptySet.remove(b);	}
				if (emptySet.contains(a)) {	emptySet.remove(a);	}
			}
		}
	}
	void updateMaxTree(int root) {
		if (maxTree == -1) {
			maxTree = root;
		} else if (size[root] > size[maxTree]) {
			maxTree = root;
		}
	}
	public int getMaxTree() {
		if (maxTree == -1) return -1;
		return size[maxTree];
	}

	public int root(int a) {
		if (a < 0 || a >= array.length) return -1;
		int retVal = a;
		while (array[retVal] != retVal) {
			retVal = array[retVal];
		}
		return retVal;
	}

	public boolean find(int a, int b) {
		return root(a) == root(b);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			sb.append(i+"("+array[i]+","+size[i]+")");
			if (i < array.length-1) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

	public List<List<Integer>> getConnectedLists() {
		List<List<Integer>> retVal = new ArrayList<>();
		SortedSet<Integer> keys = new TreeSet<Integer>(globalMap.keySet());
		for (Integer key : keys) { 
			Integer[] arr = globalMap.get(key).toArray(new Integer[0]);
			Arrays.sort(arr);
			retVal.add(Arrays.asList(arr));
		}
		for (Integer empty : emptySet) {
			List<Integer> list = new ArrayList<>();
			list.add(empty);
			retVal.add(list);
		}
		return retVal;
	}


	static void printArray(String header, int[] array) {
		System.out.println(header + " " + array.length);
		for (int m : array) System.out.print(m + " "); 
		System.out.println();
	}

	public static void main(String[] args) {
//		UnionFind uf = new UnionFind(100);
//		System.out.println("Is 3 connected to 4: " + uf.find(3,4));
//		uf.union(3, 4);
//		System.out.println("Is 3 connected to 4: " + uf.find(3,4));
//		for (int i = 5; i < uf.array.length-1; i+=2) {
//			uf.union(i, i+1);
//		}
//		for (int i = 0; i < uf.array.length/2-1; i++) {
//			uf.union(i, uf.array.length-i-1);
//		}

		if (args.length < 1) {
			System.err.println("Must specify the problem");
			return;
		}
		if (args[0].equals("OneProblem")) {
			char[][] oneGraph = new char[][] {
					{'x','o','o','x'},
					{'o','x','o','x'},
					{'x','x','o','o'},
					{'x','o','o','x'}};
			char[][] tttGraph = new char[][] {		// tic tac toe X's 12*3=36 + 13*4=52 = 88
					{'o','o','o','o','o','o','o','o','o','o','o','o','o','o','o','o','o'},	// 1
					{'o','o','o','o','o','o','o','o','o','o','o','o','o','o','o','o','o'},	// 2	34
					{'o','o','o','o','o','x','x','o','o','o','x','x','o','o','o','o','o'},	// 3	51
					{'o','o','o','o','o','x','x','o','o','o','x','x','o','o','o','o','o'},	// 4	68
					{'o','o','o','o','o','x','x','o','o','o','x','x','o','o','o','o','o'},	// 5	85
					{'o','o','x','x','x','x','x','x','x','x','x','x','x','x','x','o','o'},	// 6	102
					{'o','o','x','x','x','x','x','x','x','x','x','x','x','x','x','o','o'},	// 7	119
					{'o','o','o','o','o','x','x','o','o','o','x','x','o','o','o','o','o'},	// 8	136
					{'o','o','o','o','o','x','x','o','o','o','x','x','o','o','o','o','o'},	// 9	151
					{'o','o','o','o','o','x','x','o','o','o','x','x','o','o','o','o','o'},	// 10
					{'o','o','x','x','x','x','x','x','x','x','x','x','x','x','x','o','o'},	// 11
					{'o','o','x','x','x','x','x','x','x','x','x','x','x','x','x','o','o'},	// 12
					{'o','o','o','o','o','x','x','o','o','o','x','x','o','o','o','o','o'},	// 13
					{'o','o','o','o','o','x','x','o','o','o','x','x','o','o','o','o','o'},	// 14
					{'o','o','o','o','o','x','x','o','o','o','x','x','o','o','o','o','o'},	// 15
					{'o','o','o','o','o','o','o','o','o','o','o','o','o','o','o','o','o'},	// 16
					{'o','o','o','o','o','o','o','o','o','o','o','o','o','o','o','o','o'}};	// 17
			UnionFind uf = new UnionFind(oneGraph.length * oneGraph.length);
			UnionFindUtils.makeFriends(oneGraph, uf);
			System.out.println("One");
			UnionFindUtils.printDetails(uf);
			uf = new UnionFind(tttGraph.length * tttGraph.length);
			UnionFindUtils.makeFriends(tttGraph, uf);
			System.out.println("Two");
			UnionFindUtils.printDetails(uf);
		} else if (args[0].equals("GrafixMask")) {
			// Topcoder grafixMask
			GrafixMask grafixMask = new GrafixMask();
			printArray("First" , grafixMask.sortedAreas(new String[]{"0 292 399 307"}));
			String[] second = new String[]{"48 192 351 207", "48 392 351 407", "120 52 135 547", "260 52 275 547"};
			printArray("Second" , grafixMask.sortedAreas(second));
			String[] third = new String[]{"0 192 399 207", "0 392 399 407", "120 0 135 599", "260 0 275 599"};
			printArray("Third" , grafixMask.sortedAreas(third));
			String[] four = new String[]{"50 300 199 300", "201 300 350 300", "200 50 200 299", "200 301 200 550"};
			printArray("Four" , grafixMask.sortedAreas(four));
		}
	}
}
