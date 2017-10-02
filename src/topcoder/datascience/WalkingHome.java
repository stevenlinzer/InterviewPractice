package topcoder.datascience;

import java.util.ArrayList;
import java.util.List;

/**
 *	Uses DiJkstra's Shortest Path Algorithm for a two dimensioned graph
 */
public class WalkingHome {
	int Y, X;
	char[][] charMap = null;
	int[] homeLoc = null; 
	int[][] moves = new int[][] {{0,1},  {-1,0},  {0,1},  {1,0}}; 
	char[] roads = new char[]{'|','-','|','-'}; 

	boolean isIn(int y, int x) {
		if (y >= 0 && y < Y && x >= 0 && x < X) return true;
		return false;
	}
	boolean isWalkable(int y, int x) {
		if (charMap[y][x] == '.' || charMap[y][x] == 'S' || charMap[y][x] == 'H')
			return true;
		return false;
	}

	public int fewestCrossings(String[] map) {
		Y = map.length;
		X = map[0].length();
		int[][] distMap = new int[Y][X];
		charMap = new char[Y][X];
		boolean[][] considered = new boolean[Y][X];
		int lastX=0, lastY = 0;
		for (int i = 0; i < Y; i++) {
			for (int j = 0; j < X; j++) {
				charMap[i][j] = map[i].charAt(j);
				distMap[i][j] = Integer.MAX_VALUE;
				considered[i][j] = true;
				switch (charMap[i][j]) {
				case 'S':
					distMap[i][j] = 0;
					lastX = j;
					lastY = i;
					considered[i][j] = false;
					break;
				case 'H':
					homeLoc = new int[]{i,j}; 
				}
			}
		}
		// do this through graph theory
		List<String> intList = new ArrayList<>();
		while (true) {
			for (int move=0; move < roads.length; move++) {	// All possible moves
				int x = lastX+moves[move][1];
				int y = lastY+moves[move][0];
				int cost = 0;
				while (isIn(y,x) && charMap[y][x] == roads[move]) {
					x += moves[move][1];	// Skip multiple lane crossings
					y += moves[move][0];
					cost = 1;
				}
				if (!isIn(y,x)) continue;
				// If after crossing land on a walkable cell, check distance 
				if (isWalkable(y,x) && considered[y][x] && distMap[y][x] > distMap[lastY][lastX] + cost) {
					distMap[y][x] = distMap[lastY][lastX] + cost;
					intList.add(y+","+x);
//					System.out.println("in List (" + y + "," + x + ")");
				}
			}

			int min = Integer.MAX_VALUE;
			for (String intObj : intList) {
				String[] intSplit = intObj.split(",");
				if (distMap[Integer.parseInt(intSplit[0])][Integer.parseInt(intSplit[1])] < min) {
					min = distMap[Integer.parseInt(intSplit[0])][Integer.parseInt(intSplit[1])];
					lastX = Integer.parseInt(intSplit[1]);
					lastY = Integer.parseInt(intSplit[0]);
				}
			}
			if (min >= Integer.MAX_VALUE) return -1;
			if (lastY == homeLoc[0] && lastX == homeLoc[1]) return min;
//			System.out.println("Not considered (" + lastY + "," + lastX + ") min " + min);
			if (intList.contains(lastY + "," + lastX)) {
				intList.remove(lastY + "," + lastX);
//				System.out.println("remove - " + lastY + "," + lastX);
			}
			considered[lastY][lastX] = false;
		}
	}
}
