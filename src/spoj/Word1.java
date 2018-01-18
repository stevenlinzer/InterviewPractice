package spoj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class UnionFind {
	int[] array;
	int[] size;
	UnionFind(int size) {
		array = new int[size];
		this.size = new int[size];
		for (int i = 0; i < size; i++) {
			array[i] = i;
			this.size[i] = 1;
		}
	}
	int root(int a) {
		if (a < 0 || a >= array.length) return -1;
		while (a != array[a]) a = array[a]; 
		return a;
	}
	void union(int a, int b) {
		int rootA = root(a);
		int rootB = root(b);
		if (rootA != rootB) {
			if (size[rootB] > size[rootA]) {
				array[rootB] = rootA;
				this.size[rootB] += this.size[rootA];
			} else {
				array[rootA] = rootB;
				this.size[rootA] += this.size[rootB];
			}
		}
	}
	boolean find(int a, int b) {
		if (root(a) == root(b)) return true;
		return false;
	}
}

public class Word1 {

	static final String SUCCESS_MESSAGE = "Ordering is possible.";
	static final String FAILURE_MESSAGE = "The door cannot be opened.";
	
	public static void main(String[] args) throws IOException {
	    java.io.BufferedReader reader = new java.io.BufferedReader (new java.io.InputStreamReader (System.in));
		String str = reader.readLine();
		int caseCount = Integer.valueOf(str);
		String[] output = new String[caseCount];
		for (int count = 0; count < caseCount; count++) {
			List<Integer>[] starts = new ArrayList['z'];
			List<Integer>[] ends = new ArrayList['z'];
			UnionFind uf = new UnionFind(Integer.valueOf('z').intValue());
			str = reader.readLine();
			int dictCount = Integer.valueOf(str);
			String[] dictionary = new String[dictCount];
			for (int dCnt = 0; dCnt < dictCount; dCnt++) {
				String thisWord = reader.readLine();
				dictionary[dCnt] = thisWord;
				if (starts[thisWord.charAt(0)-'a'] == null) {
					starts[thisWord.charAt(0)-'a'] = new ArrayList<Integer>();
				}
				starts[thisWord.charAt(0)-'a'].add(dCnt);
				if (ends[thisWord.charAt(thisWord.length()-1)-'a'] == null) {
					ends[thisWord.charAt(thisWord.length()-1)-'a'] = new ArrayList<Integer>();
				}
				ends[thisWord.charAt(thisWord.length()-1)-'a'].add(dCnt);
				uf.union(thisWord.charAt(0)-'a', thisWord.charAt(thisWord.length()-1)-'a');
			}

			boolean mayHaveHamiltonPath = true;
			int startCnt = 0;
			int endCnt = 0;
			Set<Integer> roots = new HashSet<>();
			for (int dCnt = 0; dCnt < 'z'-'a'; dCnt++) {
				if (ends[dCnt] == null && starts[dCnt] != null) {
					roots.add(uf.root(dCnt));
					startCnt += starts[dCnt].size();
				} else if (ends[dCnt] != null && starts[dCnt] == null) {
					roots.add(uf.root(dCnt));
					endCnt += ends[dCnt].size();
				} else if (ends[dCnt] == null && starts[dCnt] == null) {
				} else {
					roots.add(uf.root(dCnt));
					if (starts[dCnt].size() > ends[dCnt].size()) {
						startCnt += starts[dCnt].size() - ends[dCnt].size();
					} else if (starts[dCnt].size() < ends[dCnt].size()) {
						endCnt += ends[dCnt].size() -  starts[dCnt].size();
					}
				}
				if (startCnt > 1 || endCnt > 1) {		// Do we have a letter that is ends the path with more than one two
					mayHaveHamiltonPath = false;
					break;
				}
				if (roots.size() > 1) {
					mayHaveHamiltonPath = false;
					break;
				}
			}
			
			if (mayHaveHamiltonPath) {
				output[count] = SUCCESS_MESSAGE;
			} else {
				output[count] = FAILURE_MESSAGE;
			}
	    	System.out.println(output[count]);
		}
	}
}
