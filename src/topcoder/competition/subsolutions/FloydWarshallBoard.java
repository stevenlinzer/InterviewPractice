package topcoder.competition.subsolutions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/*
 *	Implements a Floyd Warshall Shortest Path algorithm for each path.
 */
public class FloydWarshallBoard {
	static int[][] moves = new int[][] {{1,0},{0,1},{-1,0},{0,-1}}; // Down, right, up, left
	static int[] staticLengths = new int[10*10];
	static int[] staticComeFrom = new int[10*10];
	static int[][] comeFrom, lengths;
	static int boardSize = 50;

	private static void weightVertex(int boardSize, boolean[] visited, int[] lengths, int currentVertex, int[] comeFrom) {
//		System.out.println("Visiting "+ currentVertex + ".");
		if (!visited[currentVertex]) {
			visited[currentVertex] = true;
		}
		int minPathValue = Integer.MAX_VALUE;
		int minPathPtr = -1;
		int currentY =  currentVertex / boardSize;
		int currentX =  currentVertex % boardSize;
		int[][] nextMoves = new int[4][];
		for (int m = 0; m < moves.length; m++) {
			if (currentY+moves[m][0] >= boardSize ||currentX+moves[m][1] >= boardSize
					|| currentY+moves[m][0] < 0 ||currentX+moves[m][1] < 0) {
				continue;
			}
			nextMoves[m] = new int[]{(currentY+moves[m][0]),(currentX+moves[m][1])};
			int i = (nextMoves[m][0]*boardSize)+nextMoves[m][1];
			int iWeight = lengths[i];
			int thisWeight = Math.min(iWeight, lengths[currentVertex]) + 1;
			if (lengths[i] > thisWeight) {
				lengths[i] = thisWeight;
				comeFrom[i] = currentVertex;
//				System.out.println("  change (" + i + ") lengths " + lengths[i] + ".");
			}
			if (!visited[i]) {
				if (minPathValue > thisWeight) {
					minPathValue = thisWeight;
					minPathPtr = i;
				}
			}
		}
		if (minPathValue < Integer.MAX_VALUE) {
			weightVertex(boardSize, visited, lengths, minPathPtr, comeFrom);
		}
		for (int bigM = 0; bigM < nextMoves.length; bigM++) {
			if (nextMoves[bigM] == null) {continue;} 
			int i = (nextMoves[bigM][0]*boardSize)+nextMoves[bigM][1];
			if (i != minPathPtr) {
				if (!visited[i]) {
					weightVertex(boardSize, visited, lengths, (nextMoves[bigM][0]*boardSize)+nextMoves[bigM][1], comeFrom);
				}
			}
		}
	}

	static int getCost(Integer[] ret) {
		int thisCost = 0;
		for (int i = 1; i < ret.length; i++) {
			thisCost += lengths[ret[i-1]][ret[i]]; 
		}
		return thisCost;
	}

	static int getCost(Stack<Integer> stack) {
		return getCost(stack.toArray(new Integer[0]));
	}

	static private Integer[] getShortestPath(int[] path, Stack<Integer> stack, int minCost) {
		Integer[] retVal = null;
		if (stack.size() >= path.length) {
			int thisCost = getCost(stack);
			if (thisCost < minCost) return stack.toArray(new Integer[0]);
			else return null;
		}

		for (int p : path) {
			if (stack.contains(p)) {
				continue;
			}
			stack.push(p);
			Integer[] ret = getShortestPath(path, stack, minCost);
			if (ret != null) {
				minCost = getCost(ret);
				retVal = ret;
			}
			stack.pop();
		}
		return retVal;
	}

	static private Integer[] getShortestPath(int[] path) {
		Stack<Integer> stack = new Stack<>();
		return getShortestPath(path, stack, Integer.MAX_VALUE);
	}
	static private Integer[] getPath(int start, int endVertex) {
		int currentVertex = start;
		Stack<Integer> stack = new Stack<>();
		Set<Integer> set = new HashSet<>();
		stack.push(currentVertex);
		while (stack.size() > 0) {
			for (int i = 0; i < comeFrom.length; i++) {
				if (comeFrom[start][i] == currentVertex && !set.contains(i)) {
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
				int xPlus1 = (currentVertex + 1) % boardSize;
				int y = (currentVertex / boardSize) * boardSize;
				if (currentVertex > 0 && comeFrom[start][currentVertex-1] == currentVertex && !set.contains(currentVertex-1)) {
					stack.push(currentVertex-1);
				} else if (comeFrom[start][y+xPlus1] == currentVertex && !set.contains(y+xPlus1)) {
					stack.push(y+xPlus1);
				} else if (currentVertex > boardSize && comeFrom[start][currentVertex-boardSize] == currentVertex && !set.contains(currentVertex-boardSize)) {
					stack.push(currentVertex-boardSize);
				} else if (currentVertex+boardSize > boardSize+boardSize
						&& comeFrom[start][currentVertex%boardSize] == currentVertex && !set.contains(currentVertex%boardSize)) {
					stack.push(currentVertex-boardSize);
				} else {
					stack.pop();
				}
			}
			currentVertex = stack.peek();
		}
		return stack.toArray(new Integer[0]);
	}
	static private Integer[] getPath(Integer[] minPath) {
		List<Integer> retVal = new ArrayList<>();
		for (int i = 1; i < minPath.length; i++) {
			Integer[] onePath = getPath(minPath[i-1], minPath[i]);
			if (i == 1) retVal.addAll(Arrays.asList(onePath));
			else {
				for (int o = 1; o < onePath.length; o++) {
					retVal.add(onePath[o]);
				}
			}
		}
		return retVal.toArray(new Integer[0]);
	}

	public static void main(String[] args) {
		comeFrom = new int[boardSize*boardSize][];
		lengths = new int[boardSize*boardSize][];
		// Create the Floyd Warshall Shortest distance matrix.
		for (int t = 0; t < boardSize*boardSize; t++) {
			int[] tempComeFrom = Arrays.copyOf(staticComeFrom,boardSize*boardSize);
			boolean[] visited = new boolean[boardSize*boardSize];
			int[] tempLengths = Arrays.copyOf(staticLengths,boardSize*boardSize);
			for (int i = 0; i < tempLengths.length; i++) tempLengths[i] = Integer.MAX_VALUE;
			tempComeFrom[t] = -1;
			tempLengths[t] = 0;
			weightVertex(boardSize, visited, tempLengths, t, tempComeFrom);
			comeFrom[t] = tempComeFrom;
			lengths[t] = tempLengths;
		}

		int[] samples = new int[]{98, 57, 22, 75, 153, 0};
		int start = 37;
		for (int s : samples) {
			System.out.println("The distance between " + start + " and " + s + " is " + lengths[start][s]);
			start = s;
		}
		Integer[] minPath = getShortestPath(samples);
		for (int p : minPath) System.out.print(p + ", ");
		System.out.println(" cost = " + getCost(minPath) + ".");
		minPath = getPath(minPath);
		System.out.print("Length " + minPath.length + " > ");
		for (int p : minPath) System.out.print(p + ", ");
		System.out.println();
	}

}
