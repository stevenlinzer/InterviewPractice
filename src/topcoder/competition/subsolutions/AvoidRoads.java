package topcoder.competition.subsolutions;

import java.io.IOException;
/*	This code shows how to solve a problem using a 2-dimensional dynamic programming algorithm.
1
35
31

1
6
6
"0 0 0 1","4 5 5 5"
"0 0 0 1","5 6 6 6"
Blocks are sx sy ex ey
*/
public class AvoidRoads {

	private static boolean isBlocked(int[][] blocks, int startX, int startY, int endX, int endY) {
		for (int i = 0; i < blocks.length; i++) {
			if (blocks[i][0] == startX && blocks[i][1] == startY && blocks[i][2] == endX && blocks[i][3] == endY)
				return true;
		}
		return false;
	}
	public static void main(String[] args) throws IOException {
	    java.io.BufferedReader reader = new java.io.BufferedReader (new java.io.InputStreamReader (System.in));
		String str = reader.readLine();
		int cases = Integer.parseInt(str);
		for (int cnt = 0; cnt < cases; cnt++) {
			str = reader.readLine();		// Doing the Topcoder AvoidRoads problem
			int width = Integer.parseInt(str);
			str = reader.readLine();
			int height = Integer.parseInt(str);
			str = reader.readLine();		// string for bad roads
			String[] blockStrs = str.split(",");
			int[][] blocks;
			if (blockStrs.length > 1) {
				blocks = new int[blockStrs.length][];
				for (int i = 0; i < blockStrs.length; i++) {
					String trimmed = blockStrs[i].substring(1, blockStrs[i].length()-1); 
					String[] trimArray = trimmed.split(" "); 
					blocks[i] = new int[trimArray.length];
					for (int j = 0; j < trimArray.length; j++) {
						blocks[i][j] = Integer.parseInt(trimArray[j]);
					}
				}
			} else {
				blocks = new int[0][];
			}
			long[][] paths = new long[height+1][width+1];
			for (int x = 1; x < Math.max(width, height)+1; x++) {
				if (x == 1) {
					if (!isBlocked(blocks, 0, 0, x, 0))
						paths[0][x] = 1;	// First road upward
					if (!isBlocked(blocks, 0, 0, 0, x))
						paths[x][0] = 1;	// First road right
					paths[x][x] = paths[0][x] + paths[x][0];
					continue;
				}
				for (int y = 0; y < x; y++) {
					if (x <= width && y <= height){
						if (!isBlocked(blocks, x-1, y, x, y)) {
							paths[y][x] = paths[y][x-1];	// Next road upward
						}
						if (y > 0 && !isBlocked(blocks, x-1, y, x, y)) paths[y][x] += paths[y-1][x]; 
					}
					if (x <= height && y <= width){
						if (!isBlocked(blocks, x, y-1, x, y)) {
							paths[x][y] = paths[x-1][y];	// Next road right
						}
						if (y > 0 && !isBlocked(blocks, x-1, y, x, y)) paths[x][y] += paths[x][y-1]; 
					}
				}
				if (x <= height && x <= width) {
					if (!isBlocked(blocks, x-1, x, x, x)) {
//						System.out.println("X not blocked (" + (x-1) + " to " + x + ")");
						paths[x][x] += paths[x][x-1];
					}
					if (!isBlocked(blocks, x, x-1, x, x)) {
//						System.out.println("Y not blocked (" + (x-1) + " to " + x + ")");
						paths[x][x] += paths[x-1][x];
					}
				}
//				paths[x][x] = paths[x-1][x] + paths[x][x-1];
			}
			System.out.println(paths[height][width]);
		}
	}
}
