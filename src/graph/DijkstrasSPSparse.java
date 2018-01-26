package graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*2
2 2
1 2 13
2 1 33
4 6
1 2 10
2 1 60
1 3 20
3 4 10
2 4 5
4 1 50
*/
public class DijkstrasSPSparse {

	static int MAX_STOPS = 1000000;
	private static void weightVertex(Map<Integer, List<Integer>> routes, Map<Integer, List<Integer>> backRoutes, Map<String, Integer> costs, boolean[] visited, int[] lengths, int currentVertex, int[] comeFrom, int endVertex) {
		if (!visited[currentVertex]) {
			visited[currentVertex] = true;
		}
		int minPathValue = Integer.MAX_VALUE;
		int minPathPtr = -1;
		for (Map.Entry<Integer, List<Integer>> entry : routes.entrySet()) {
			if (!visited[entry.getKey()] && costs.containsKey(currentVertex + "/" + entry.getKey())) {
				int thisWeight = lengths[currentVertex] + costs.get(currentVertex + "/" + entry.getKey());
				if (lengths[entry.getKey()] > thisWeight) {
					lengths[entry.getKey()] = thisWeight;
					comeFrom[entry.getKey()] = currentVertex;
				}
				if (minPathValue > thisWeight) {
					minPathValue = thisWeight;
					minPathPtr = entry.getKey();
				}
			}
		}
		if (minPathValue < Integer.MAX_VALUE && currentVertex != endVertex) {
			weightVertex(routes, backRoutes, costs, visited, lengths, minPathPtr, comeFrom, endVertex);
		}
	}

	public static void main(String[] args) throws Exception {
	    java.io.BufferedReader reader = new java.io.BufferedReader (new java.io.InputStreamReader (System.in));
		String str = reader.readLine();
		int[] distanceMaster = new int[MAX_STOPS];
		int[] comeFromMaster = new int[MAX_STOPS];
		for (int i = 0; i < MAX_STOPS; i++) {
				distanceMaster[i] = Integer.MAX_VALUE;
				comeFromMaster[i] = -1;
			}
		int cases = Integer.parseInt(str);
		for (int cnt = 0; cnt < cases; cnt++) {
			str = reader.readLine();	// The number of stops and lines
			String[] stopsLines = str.split(" ");
			Map<Integer, List<Integer>> routes = new HashMap<>();	// The list of destinations
			Map<Integer, List<Integer>> backRoutes = new HashMap<>();	// The list of sources
			Map<String, Integer> costs = new HashMap<>();			// The list of route costs
			int stops = Integer.parseInt(stopsLines[0]);
			int lines = Integer.parseInt(stopsLines[1]);
			boolean[] visited = new boolean[stops];
			int[] distanceFromCCS = Arrays.copyOfRange(distanceMaster, 0, stops);
			int[] comeFromCCS = Arrays.copyOfRange(comeFromMaster, 0, stops);
			distanceFromCCS[0] = 0;
			comeFromCCS[0] = 0;
			for (int line=0; line < lines; line++) {
				str = reader.readLine();	// The route of this line.
				String[] strSplit = str.split(" ");
				int i = Integer.parseInt(strSplit[0]) - 1;
				int j = Integer.parseInt(strSplit[1]) - 1;
				if (!routes.containsKey(i)) routes.put(i, new ArrayList<Integer>());
				routes.get(i).add(j);
				if (!backRoutes.containsKey(j)) backRoutes.put(j, new ArrayList<Integer>());
				backRoutes.get(j).add(i);
				costs.put(i+"/"+j, Integer.parseInt(strSplit[2]));
			}
			weightVertex(routes, backRoutes, costs, visited, distanceFromCCS, 0, comeFromCCS, -1);
			int retVal = 0;
			for (int i = 1; i < stops; i++) {
				boolean[] visitedBack = new boolean[stops];
				int[] distanceFromBack = Arrays.copyOfRange(distanceMaster, 0, stops);
				int[] comeFromBack = Arrays.copyOfRange(comeFromMaster, 0, stops);
				distanceFromBack[i] = 0;
				comeFromBack[i] = i;
				weightVertex(routes, backRoutes, costs, visitedBack, distanceFromBack, i, comeFromBack, 0);
				retVal += distanceFromCCS[i] + distanceFromBack[0];
			}
			System.out.println(retVal);
		}
	}
}
