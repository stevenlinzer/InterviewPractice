package graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

/*
 *	Implements a DiJkstra's Shortest Path algorithm for each path of of 
 *	the mower.
 */
public class LawnPractice {
	static String[] yard = new String[] {
			"00000000000000000000",	// 0-20
			"00000100000000000000",	// 1-40
			"000.0100000000000000",
			"0000.100000000000000",	// 3-80
			"01223000000000000000",	// 4-100
			"01232340000000000000",
			"000..353100000000000",	// 6-140
			"000.0256640000000000",
			"00000077000000000000",	// 8-180
			"00000077777777000000",
			"00000077770000000000",
			"00000777550000000000",	// 11-240
			"00777777540000000000",
			"00007777043330000000",
			"00000077.43300000000",
			"00000077.44440000000",
			"00000077004400000000",
			"00000077003301100000",
			"00000077002211100000",
			"00000000000000000000"
	};
	static int[][] moves = new int[][] {{1,0},{0,1},{-1,0},{0,-1}}; // Down, right, up, left
	static int[] staticLengths = new int[yard.length * yard[0].length()];
	static int[] staticComeFrom = new int[yard.length * yard[0].length()];
	static Set<Integer> mowed = new HashSet<>();
	static int slopeCost = 9;
	static int turnCost = 5;
	static int greatestScore = 5;
	static int grassCells = 0;
	static int[][] scores;
	static float[] costs = new float[greatestScore];
	static {
		for (int i = 0; i < yard.length * yard[0].length(); i++) {
			staticLengths[i] = Integer.MAX_VALUE;		// Initial length is infinity.
			staticComeFrom[i] = -1;
			int y =  i / yard[0].length();
			int x =  i % yard[0].length();
			if (yard[y].charAt(x) != '.') {
				grassCells++;
			}
		}
	}

	private static void weightVertex(String[] graph, boolean[] visited, int[] lengths, int currentVertex, int currentMove, int[] comeFrom) {
//		System.out.println("Visiting " + currentVertex + ", move " +currentMove);
		visited[currentVertex] = true;
		List<String> theRest = new ArrayList<>();
		int minPathValue = Integer.MAX_VALUE;
		int minPathPtr = -1;
		int minMove = -1;
		int currentY =  currentVertex / graph[0].length();
		int currentX =  currentVertex % graph[0].length();
		int[][] nextMoves = new int[4][];
		int currentHeight = Integer.parseInt(Character.toString(yard[currentY].charAt(currentX)));
		for (int m = 0; m < moves.length; m++) {
			nextMoves[m] = new int[]{(currentY+moves[m][0])%yard[0].length(),(currentX+moves[m][1])%yard[0].length()};
			if (nextMoves[m][0] < 0) nextMoves[m][0] = yard.length-1;
			if (nextMoves[m][1] < 0) nextMoves[m][1] = yard[0].length()-1;
			char nextChar = yard[nextMoves[m][0]].charAt(nextMoves[m][1]);
			if (nextChar == '.') continue;
			int nextHeight = Integer.parseInt(Character.toString(nextChar));
			float change = (nextHeight > currentHeight ? nextHeight - currentHeight : 0) + 1;		// Assume that forward cost is 1.
			if (mowed.contains((nextMoves[m][0]*yard[0].length())+nextMoves[m][1])) {
				change *= 0.2;
			}
			int i = (nextMoves[m][0]*yard[0].length())+nextMoves[m][1];
//			if (!visited[i]) {
//			}
			int thisWeight = lengths[currentVertex] + (int)change;
			if (currentMove != m) {
				if (currentMove%2 == m%2) {
					thisWeight = thisWeight + turnCost * 2;
				} else {
					thisWeight = thisWeight + turnCost;
				}
			}
			if (lengths[i] > thisWeight) {
				lengths[i] = thisWeight;
				comeFrom[i] = currentVertex;
			}
			theRest.add(i + "," + m);
			if (minPathValue > thisWeight) {
				minPathValue = thisWeight;
				minPathPtr = i;
				minMove = m;
			}
		}
		if (minPathValue < Integer.MAX_VALUE && !visited[minPathPtr]) {
			theRest.remove(minPathPtr+","+minMove);
			weightVertex(graph, visited, lengths, minPathPtr, minMove, comeFrom);
		}
		for (String a : theRest) {
			String[] as = a.split(",");
			if (!visited[Integer.parseInt(as[0])]) {
				weightVertex(graph, visited, lengths, Integer.parseInt(as[0]), Integer.parseInt(as[1]), comeFrom);
			}
		}
	}

	private static List<Integer> getPath(int start, int end, int[] lengths, int[] comeFrom, int moves) {
		List<Integer> retVal = new ArrayList<>();
		for (int i = 0; i < comeFrom.length; i++) {
			if (comeFrom[i] == start) {
				List<Integer> tempRetVal = getPath(i, end, lengths, comeFrom, moves + 1);
			}
		}
		return retVal;
	}

	private int[] move(int current, int[] move) {
		int verticalMove = ((current/yard[0].length()) + move[0]) % current/yard[0].length();
		int horizontalMove = ((current%current/yard[0].length()) + move[1]) % current/yard[0].length();
		if (horizontalMove < 0) {horizontalMove = current/yard[0].length() + horizontalMove;}
		if (verticalMove < 0) {verticalMove = current/yard[0].length() + verticalMove;}
		return new int[]{verticalMove, horizontalMove};
	}

	static void printPath(int start, int[] lengths, int[] comeFrom, int moves, int hits, boolean print) {
		float cost = Float.MAX_VALUE;
		if (moves > 0) {
			cost = ((float)lengths[start])/((float)moves);
		}
		String format = "%" + (moves == 0 ? "" : ""+moves) + "s %s cost (%4.2f) hits %s.%n";
		int s = scores.length-1;
		for (; s > 0; s--) {
			if (scores[s][1] > hits) {
				break;
			}
		}
		if (s < scores.length-1) {
			if ((scores[s][1] < hits || (scores[s][1] == hits && costs[s] < cost))
					&& !mowed.contains(start)) {
				for (int a = scores.length-2; a >= s; a--) {
					scores[a+1][0] = scores[a][0];
					scores[a+1][1] = scores[a][1];
					costs[a+1] = costs[a];
				}
				scores[s][0] = start;
				scores[s][1] = hits;
				costs[s] = cost;
			}
		}
		if (print) System.out.printf(format, start, lengths[start], cost, hits);
		for (int i = 0; i < comeFrom.length; i++) {
			if (comeFrom[i] == start) {
				printPath(i, lengths, comeFrom, moves + 1, hits + (mowed.contains(i) ? 0 : 1), print);
			}
		}
	}

	private static void createTreePath(int currentVertex, Map<Integer, int[]> tree, Map<Integer, Set<Integer>> neighbors) {
		if (tree.get(currentVertex) == null) {
			return;
		}
		int[] current = tree.get(currentVertex);
		int currentY =  currentVertex / yard[0].length();
		int currentX =  currentVertex % yard[0].length();
		for (int i = 0; i < moves.length; i++) {
			int y =  (currentY+moves[i][0]);
			int x =  (currentX+moves[i][1]);
			if (y < 0) y = yard[0].length()-1;
			else if (y == yard[0].length()) y = 0;
			if (x < 0) x = yard[0].length()-1;
			else if (x == yard[0].length()) x = 0;
			if (yard[currentY].charAt(currentX) == yard[y].charAt(x) && !tree.containsKey((y*yard[0].length())+x)) {
				current[i+1] = (y*yard[0].length())+x;
				tree.put((y*yard[0].length())+x, new int[]{currentVertex, -1, -1, -1, -1, -1});
				createTreePath((y*yard[0].length())+x, tree, neighbors);
			} else if (yard[currentY].charAt(currentX) != yard[y].charAt(x)
					&& yard[y].charAt(x) != '.' && !mowed.contains((y*yard[0].length())+x)) {
				if (!neighbors.containsKey(Character.getNumericValue(yard[y].charAt(x)))) {
					neighbors.put(Character.getNumericValue(yard[y].charAt(x)), new TreeSet<Integer>());
				}
				neighbors.get(Character.getNumericValue(yard[y].charAt(x))).add((y*yard[0].length())+x);
			}
		}
//		System.out.println("Current " + currentVertex + "(" + currentY + "," + currentX + ") (" + current[0] + "," + current[1] + "," + current[2] + "," + current[3] + "," + current[4] + ")");
		tree.put(currentVertex, current);
	}

	private static void mowPatch(Map<Integer, int[]> tree, Integer curr, List<Integer> path, List<Integer> must) {	//List<Integer> save, 
		if (path.size() == 0) {
			path.add(curr);
		} else {
			if (canAddToPatch(path.get(path.size()-1), curr)) path.add(curr);
//			if (save.size() == 0) path.add(curr);
			else must.add(curr);
		}
//		if (save.contains(curr)) save.remove(curr);
		for (int i = 1; i <= moves.length; i++) {
			if (tree.get(curr)[i] != -1) {
				mowPatch(tree, tree.get(curr)[i], path, must);	// save,
//				path.add(curr);
				boolean noSaveCurr = false;
				if (Math.abs(curr - tree.get(curr)[i]) == 1) {	// left-right move
//					static int[][] moves = new int[][] {{1,0},{0,1},{-1,0},{0,-1}}; // Down, right, up, left
					if (tree.get(curr)[1] == -1 && tree.get(curr)[3] == -1) {
						noSaveCurr = true;
					}
				} else {
					if (tree.get(curr)[2] == -1 && tree.get(curr)[4] == -1) {
						noSaveCurr = true;
					}
				}
				if (noSaveCurr) {
//					if (!save.contains(curr)) save.add(curr);
					if (must.size() == 0) path.add(curr);
//				} else {
//					save.add(curr);
				}
			}
		}
	}

	private static boolean canAddToPatch(Integer inPath, Integer first) {
		int currPlus1 = (first + 1) % yard[0].length() == 0 ? first - yard[0].length() + 1 : first + 1;
		int currMinus1 = (first == 0) ? (yard.length*yard[0].length())-1: (first%yard[0].length() == 0) ? first+yard[0].length()-1 : first-1;
		int currYPlus = (first + yard[0].length()) % (yard.length*yard[0].length());
		int currYMinus = first < yard[0].length() ? first + (yard.length*yard[0].length())-yard[0].length() : first - yard[0].length();
		if (inPath == currPlus1 || inPath == currMinus1 || inPath == currYPlus || inPath == currYMinus)
			return true;
		else return false;
	}

	private static List<Integer> findPatch(List<Integer> path, Integer first, List<Integer> must, boolean matchEndPath) {
		List<Integer> retVal = new ArrayList<>();
		if (canAddToPatch(path.get(path.size()-1), first))
			retVal.add(first);
		else if (matchEndPath) return retVal;
		else retVal.add(first);
		List<Integer> removeList = new ArrayList<>();
		for (int m : must) {
			if (canAddToPatch(m, retVal.get(retVal.size()-1))) {
//			if (m == currPlus1 || m == currMinus1 || m == currYPlus || m == currYMinus) {
				retVal.add(m);
				removeList.add(m);
			}
		}
		for (int m = must.size()-1; m > 0; m--) {
			if (canAddToPatch(must.get(m), retVal.get(retVal.size()-1))) {
				if (!retVal.contains(must.get(m))) {
					retVal.add(must.get(m));
					removeList.add(must.get(m));
				}
			}
		}
		must.removeAll(removeList);
		if (removeList.size() == 0) return new ArrayList<>();;
		return retVal;
	}

	private static Map<String, List<Integer>> findSubPatchs(List<Integer> must) {
		Map<String, List<Integer>> retVal = new HashMap<>();
		if (must.size()==0) return retVal;
		List<Integer> start = new ArrayList<Integer>();
		start.add(must.get(0));
		retVal.put(must.get(0)+","+must.get(0), start);
		for (int m=1; m < must.size(); m++) {
			String foundKey = null;
			String newKey = null;
			List<Integer> foundValue = null;
			for (Entry<String, List<Integer>> e : retVal.entrySet()) {
				String[] range = e.getKey().split(",");
				for (int r = 0; r < range.length; r++) {
//					int currPlus1 = (Integer.parseInt(range[r]) + 1) % yard[0].length() == 0 ? Integer.parseInt(range[r]) - yard[0].length() + 1 : Integer.parseInt(range[r]) + 1;
//					int currMinus1 = (Integer.parseInt(range[r]) == 0) ? (yard.length*yard[0].length())-1: (Integer.parseInt(range[r])%yard[0].length() == 0) ? Integer.parseInt(range[r])+yard[0].length()-1 : Integer.parseInt(range[r])-1;
//					int currYPlus = (Integer.parseInt(range[r]) + yard[0].length()) % yard.length*yard[0].length();
//					int currYMinus = Integer.parseInt(range[r]) < yard[0].length() ? Integer.parseInt(range[r]) + (yard.length*yard[0].length())-yard[0].length() : Integer.parseInt(range[r]) - yard[0].length();
//					if (must.get(m) == currPlus1 || must.get(m) == currMinus1 || must.get(m) == currYPlus || must.get(m) == currYMinus) {
					if (canAddToPatch(must.get(m), Integer.parseInt(range[r]))) {
						foundKey = e.getKey();
						foundValue = e.getValue();
						if (r==0) {
							foundValue.add(0, must.get(m));	// Add at beginning.
							newKey = must.get(m) + "," + range[1];
						} else {
							foundValue.add(must.get(m));
							newKey = range[0] + "," + must.get(m);
						}
						break;
					}
				}
				if (foundKey != null) break;
			}
			if (foundKey != null) {
				retVal.remove(foundKey);
				retVal.put(newKey, foundValue);
			} else {
				List<Integer> another = new ArrayList<Integer>();
				another.add(must.get(m));
				retVal.put(must.get(m)+","+must.get(m), another);
			}
		}
		return retVal;
	}

	private static void mowFixPatch(List<Integer> path, List<Integer> must) {	//
//		for (int s : save) {
//			List<Integer> tempPath = findPatch(path, s, must, true);
//			if (tempPath.size() > 0) {
//				path.addAll(tempPath);
//			}
//			if (must.size() == 0) break;
//		}

		//	if anything is left in the must list, recurse the path backwards.
		while (must.size() > 0) {
			int backPtr = path.size() - 1;
			while (must.size() > 0 && backPtr >= 0) {
				List<Integer> tempPath = findPatch(path, path.get(backPtr), must, false);
				if (tempPath.size() > 0) {
					for (int i = path.size() - 2; i > backPtr; i--) {
						int thisOne = path.get(i);
						for (int j = i-1; j > backPtr; j--) {
							if (path.get(j) == thisOne) i = j;
						}
						path.add(path.get(i));
					}
					path.addAll(tempPath);
				}
				backPtr--;
			}
		}
	}

	private static void mowFixPatch(List<Integer> path, Map<String, List<Integer>> mustMap) {
		Set<Integer> cells = new HashSet<Integer>();
		for (Entry<String, List<Integer>> e : mustMap.entrySet()) {
			String[] cellStr = e.getKey().split(",");
			cells.add(Integer.parseInt(cellStr[0]));
			cells.add(Integer.parseInt(cellStr[1]));
		}

		int start = path.get(path.size()-1);
		while (cells.size() > 0) {
			String key = null;
			int[] comeFrom = Arrays.copyOf(staticComeFrom,yard.length * yard[0].length());
			boolean[] visited = new boolean[yard.length*yard[0].length()];
			int[] lengths = Arrays.copyOf(staticLengths,yard.length * yard[0].length());
			comeFrom[start] = -1;
			lengths[start] = 0;
			weightVertex(yard, visited, lengths, start, 1, comeFrom);	// Start going right.
			writeLengths(lengths, comeFrom, visited);
			int closest = getClosest(cells, lengths);
			String[] cellStr = null;
			for (String thisKey : mustMap.keySet()) {
				cellStr = thisKey.split(",");
				if (Integer.parseInt(cellStr[0]) == closest || Integer.parseInt(cellStr[1]) == closest) {
					key = thisKey;
					break;
				}
			}
			if (key != null) {
				List<Integer> tempMow = mowShortestPath(comeFrom, start, closest);
				if (closest == Integer.parseInt(cellStr[0])) {
					tempMow.addAll(mustMap.get(key));
				} else {
					for (int m = mustMap.get(key).size()-2; m >= 0; m--) {
						tempMow.add(mustMap.get(key).get(m));
					}
				}
				start = tempMow.get(tempMow.size()-1);
				cells.remove(Integer.parseInt(cellStr[0]));
				cells.remove(Integer.parseInt(cellStr[1]));
				System.out.println("Add for (" + tempMow + ") to path " + mustMap.get(key) + " new start " + start);
				path.addAll(tempMow.subList(1, tempMow.size()));
				mustMap.remove(key);
			}
		}
	}

	private static List<Integer> mowShortestPath(int[] comeFrom, int currentVertex, int endVertex) {
		Stack<Integer> stack = new Stack<>();
		Set<Integer> set = new HashSet<>();
		stack.push(currentVertex);
		while (stack.size() > 0) {
			for (int i = 0; i < comeFrom.length; i++) {
				if (comeFrom[i] == currentVertex && !set.contains(i)) {
					if (!stack.contains(i)) {
						stack.push(i);
						currentVertex = i;
					}
					set.add(i);
				}
			}
//			System.out.println(stack + ", current " + currentVertex + ", end " + endVertex);
			if (stack.peek() == endVertex) {
				break;
			}
			if (currentVertex == stack.peek()) {
				int xPlus1 = (currentVertex + 1) % yard[0].length();
				int y = (currentVertex / yard[0].length()) * yard[0].length();
				if (currentVertex > 0 && comeFrom[currentVertex-1] == currentVertex && !set.contains(currentVertex-1)) {
					stack.push(currentVertex-1);
				} else if (comeFrom[y+xPlus1] == currentVertex && !set.contains(y+xPlus1)) {
					stack.push(y+xPlus1);
				} else if (currentVertex > yard[0].length() && comeFrom[currentVertex-yard[0].length()] == currentVertex && !set.contains(currentVertex-yard[0].length())) {
					stack.push(currentVertex-yard[0].length());
				} else if (currentVertex+yard[0].length() > yard.length+yard[0].length()
						&& comeFrom[currentVertex%yard[0].length()] == currentVertex && !set.contains(currentVertex%yard[0].length())) {
					stack.push(currentVertex-yard[0].length());
				} else {
					stack.pop();
				}
			}
			currentVertex = stack.peek();
		}
		return stack;
	}

	static private void writeLengths(int[] lengths, int[] comeFrom, boolean[] visited) {
        System.out.print("   0) ");
		for (int i = 0; i < lengths.length; i++) {
			String lengthTemp = (lengths[i] == Integer.MAX_VALUE ? "   ~" : String.format("%4s", lengths[i])) + ", ";
			String comeFromTemp = String.format("%4s, %5s) ", comeFrom[i], visited[i]);
			if (i > 0 && i % yard.length == 0) {
				System.out.println(String.format("  -> %d", i));
				System.out.print(String.format("%4s) ", (i/yard.length)));
			}
			System.out.print("("+ lengthTemp + comeFromTemp);
		}
		System.out.println();
	}

	// This method returns the closest cell to your present location. 
	private static int getClosest(Set<Integer> cells, int[] lengths) {
		int retVal = -1;
		int shortest = Integer.MAX_VALUE;
		for (Integer cell : cells) {
			if (lengths[cell] < shortest) {
				shortest = lengths[cell];
				retVal = cell;
			}
		}
		return retVal;
	}

	public static void main(String[] args) {
		int start = 206;
		while (mowed.size() < grassCells) {
			Map<Integer, int[]> tree = new HashMap<>();
			Map<Integer, Set<Integer>> neighbors = new HashMap<>();
			tree.put(start, new int[]{-1, -1, -1, -1, -1, -1});
			createTreePath(start, tree, neighbors);
			List<Integer> tempMowP = new ArrayList<>();
			List<Integer> must = new ArrayList<>();
			mowPatch(tree, start, tempMowP, must);
			if (must.size() > 0) System.out.println("Temp Mow Path  " + tempMowP + ", Must " + must + ".");		//  + ", save " + save
			Map<String, List<Integer>> mustMap = findSubPatchs(must); 
			mowFixPatch(tempMowP, mustMap);
//			mowFixPatch(tempMowP, must);
			System.out.println("After Mow Path " + tempMowP + ", Must " + must + ".");
			mowed.addAll(tempMowP);
			int y = start / yard[0].length();
			int x = start % yard[0].length();
			StringBuffer sb = new StringBuffer();
			for (int m : tempMowP) {
				sb.append("(" + (m/yard[0].length()) + "," + (m%yard[0].length())+") ");
			}
			System.out.println("Patch of " + yard[y].charAt(x) + "'s ("+ mowed.size() + "," + tempMowP.size() + "), Mow " + sb);
			if (neighbors.size() > 0) {
				int[] comeFrom = Arrays.copyOf(staticComeFrom,yard.length * yard[0].length());
				comeFrom[start] = -1;
				int[] lengths = Arrays.copyOf(staticLengths,yard.length * yard[0].length());
				lengths[start] = 0;
				boolean[] visited = new boolean[yard.length*yard[0].length()];
				weightVertex(yard, visited, lengths, start, 1, comeFrom);	// Start going right.
				int heighestKey = Character.getNumericValue(yard[y].charAt(x));
				int lowerKey = -1;
				for (Entry<Integer, Set<Integer>> neighbor : neighbors.entrySet()) {
					if (neighbor.getKey() > heighestKey) heighestKey = neighbor.getKey();
					if (neighbor.getKey() < Character.getNumericValue(yard[y].charAt(x))
							&& neighbor.getKey() > lowerKey) lowerKey = neighbor.getKey();
				}
				if (lowerKey > -1 && lowerKey < Character.getNumericValue(yard[y].charAt(x))) {
					start = getClosest(neighbors.get(lowerKey), lengths);
				} else if (heighestKey > Character.getNumericValue(yard[y].charAt(x))) {
					start = getClosest(neighbors.get(heighestKey), lengths);
				} else {
					break;
				}
			} else break;
		}
		for (int t = 0; t < 1000; t++) {
			int[] comeFrom = Arrays.copyOf(staticComeFrom,yard.length * yard[0].length());
			boolean[] visited = new boolean[yard.length*yard[0].length()];
			int[] lengths = Arrays.copyOf(staticLengths,yard.length * yard[0].length());
			comeFrom[start] = -1;
			lengths[start] = 0;
			scores = new int[greatestScore][2];
			weightVertex(yard, visited, lengths, start, 1, comeFrom);	// Start going right.
//			writeLengths(lengths, comeFrom, visited);
			printPath(start, lengths, comeFrom, 0, 0, false);
//			for (int i = 0; i < scores.length; i++) {
//				System.out.println(i + "-> (" + scores[i][0] + "," + scores[i][1] + ") " + costs[i]);
//			}
			if (scores[0][1] == 0) break;
			List<Integer> tempMow = mowShortestPath(comeFrom, start, scores[0][0]);
			System.out.println(mowed.size() + ", Mow " + tempMow);
			mowed.addAll(tempMow);
			start = scores[0][0];
		}
		for (int i = 0; i < yard.length*yard[0].length(); i++) {
			int y =  i / yard[0].length();
			int x =  i % yard[0].length();
			if (yard[y].charAt(x) != '.') {
				if (!mowed.contains(i)) System.out.print(i + " ");
			}
		}
		System.out.println((grassCells-mowed.size()) + ") Mowed " + mowed);
//		System.out.println("Mow " + mowShortestPath(comeFrom, scores[0][0], start));
//		for(int i = 0; i < yard.length*yard[0].length(); i++) {
//			System.out.println(i + "-> L " + lengths[i] + ", From " + comeFrom[i] + ", V " + visited[i]);
//		}
	}

}
