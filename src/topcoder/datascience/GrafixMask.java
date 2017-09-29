package topcoder.datascience;

import java.util.ArrayList;
import java.util.List;

import structures.UnionFind;
import structures.UnionFindUtils;

public class GrafixMask {
	public int[] sortedAreas(String[] rectangles) {
		char[][] graph = new char[400][600];
		UnionFind uf = new UnionFind(400 * 600);
		for (String rect : rectangles) {
			String[] rectBroken = rect.split(" ");
			for (int i = Integer.parseInt(rectBroken[0]); i <= Integer.parseInt(rectBroken[2]); i++) {
				for (int j = Integer.parseInt(rectBroken[1]); j <= Integer.parseInt(rectBroken[3]); j++) {
					graph[i][j] = '\u0001';		// Make this cell not passible.
				}
			}
		}
		UnionFindUtils.makeFriends(graph, uf);
		List<List<Integer>> listOfLists = uf.getConnectedLists();
		List<Integer> returnList = new ArrayList<>();
//		System.out.println("List of List Size " + listOfLists.size());
		for (List<Integer> l : listOfLists) {
			System.out.print("Size " + l.size() + " - " + l.get(0) + " " + l.get(l.size()-1));
			int yFirst = (int)l.get(0)/graph[0].length;
			int xFirst = (int)l.get(0) % graph[0].length;
			int yLast = (int)l.get(l.size()-1)/graph[0].length;
			int xLast = (int)l.get(l.size()-1) % graph[0].length;
			System.out.println(" (" + yFirst + "," + xFirst + ") " + " to (" + yLast + "," + xLast + ") " + (graph[yFirst][xFirst]+64));
			if (graph[yFirst][xFirst] == '\u0000') {
				returnList.add(l.size());
			}
		}
		int[] retVal = new int[returnList.size()];
		for (int m = 0; m < retVal.length; m++) {
			retVal[m] = returnList.get(m); 
		}
		return retVal;
	}
}
