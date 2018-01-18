package topcoder.competition.subsolutions;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
/*		ChessMetric 2-dim DP
5
100
"0 0","0 99"
50
3
"0 0","0 0"
2
3
"0 0","1 0"
1
3
"0 0","1 2"
1
3
"0 0","2 2"
1
*/
public class ChessMetric {
	static final int[][] MOVES = new int[][]{{0,1},{1,0},{0,-1},{-1,0}, {1,1},{-1,-1},{1,-1},{-1,1}, {2,1},{1,2},{2,-1},{-1,2}, {-2,1},{1,-2},{-2,-1},{-1,-2}};
	static final int CHESS_PTR = 8;

	private static long howMany(int boardSize, int[] start, int[] end, int totalMoves) {
		long[][] board = new long[boardSize][boardSize];
		Set<String> moveList = new HashSet<>();
		moveList.add(start[0]+","+start[1]);
		for (int nextMove = 0; nextMove < totalMoves; nextMove++) {
			long[][] boardAdditions = new long[boardSize][boardSize];
			Set<String> removeList = new HashSet<>();
			Set<String> addList = new HashSet<>();
			for (String coordStr : moveList) {
				String[] coordsStr = coordStr.split(","); 
				int[] coord = new int[]{Integer.parseInt(coordsStr[0]),Integer.parseInt(coordsStr[1])};
				for (int mc = 0; mc < MOVES.length; mc++) {
					if (coord[0]+MOVES[mc][0] > -1 && coord[0]+MOVES[mc][0] < boardSize
							&&	coord[1]+MOVES[mc][1] > -1 && coord[1]+MOVES[mc][1] < boardSize) {
						if (board[coord[0]][coord[1]] == 0) {
							boardAdditions[coord[0]+MOVES[mc][0]][coord[1]+MOVES[mc][1]]++;
						} else {
							boardAdditions[coord[0]+MOVES[mc][0]][coord[1]+MOVES[mc][1]] += board[coord[0]][coord[1]];
						}
						if (removeList.contains((coord[0]+MOVES[mc][0])+","+(coord[1]+MOVES[mc][1]))) {
							removeList.remove((coord[0]+MOVES[mc][0])+","+(coord[1]+MOVES[mc][1]));
						} else {
							addList.add((coord[0]+MOVES[mc][0])+","+(coord[1]+MOVES[mc][1]));
						}
					}
				}
				removeList.add(coordStr);
			}
			for (int y = 0; y < boardSize; y++) {
				for (int x = 0; x < boardSize; x++) {
					board[y][x] += boardAdditions[y][x] + board[y][x];
				}
			}
			moveList.removeAll(removeList);
			moveList.addAll(addList);
		}
		return board[end[0]][end[1]];
	}

	public static void main(String[] args) throws IOException {
	    java.io.BufferedReader reader = new java.io.BufferedReader (new java.io.InputStreamReader (System.in));
		String str = reader.readLine();
		int cases = Integer.parseInt(str);
		for (int cnt = 0; cnt < cases; cnt++) {
			str = reader.readLine();		// Board size
			int boardSize = Integer.parseInt(str);
			str = reader.readLine();
			String[] positionStrs = str.split(",");
			int[][] positions = null;
			if (positionStrs.length > 1) {
				positions = new int[positionStrs.length][];
				for (int i = 0; i < positionStrs.length; i++) {
					String trimmed = positionStrs[i].substring(1, positionStrs[i].length()-1); 
					String[] trimArray = trimmed.split(" "); 
					positions[i] = new int[trimArray.length];
					for (int j = 0; j < trimArray.length; j++) {
						positions[i][j] = Integer.parseInt(trimArray[j]);
					}
				}
			}
			str = reader.readLine();		// moves
			int totalMoves = Integer.parseInt(str);
			System.out.println("> " + howMany(boardSize, positions[0], positions[1], totalMoves));
		}
	}
}
