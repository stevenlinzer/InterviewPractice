package topcoder.datascience;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class CaptureThemAll {
	static final int BOARD_SIZE = 8;

	public int fastKnight(String knight, String rook, String queen) {
		Map<String, Boolean> finish = new HashMap<>();
		finish.put(rook.toUpperCase(), false);
		finish.put(queen.toUpperCase(), false);
		Set<String> visited = new HashSet<>();		// NEEDS TO BE A Set !!!
		Integer minCount = new Integer(100); 
		return moveSearch(finish, visited, knight.toUpperCase(), 0, minCount);
	}

	private int moveSearch(Map<String, Boolean> finish,Set<String> visited, String candidate, int moves, int minSearch) {
		List<String> newCandidates = new ArrayList<>();
//		System.out.println(moves + ", consider " + candidate);
		int[] knightPos = findPosition(candidate);
		String knightStr = positionToString(knightPos);
		if (finish.containsKey(knightStr) && !finish.get(knightStr)) {
//			System.out.println("Check " + knightStr);
			finish.put(knightStr, true);
		}
		if (finished(finish)) {
//			System.out.println("Finished " + candidate + " - " + moves);
			return moves;
		}
		visited.add(candidate);
		List<int[]> retVal = new ArrayList<>();
		knightMoves(knightPos, new int[]{1,2}, retVal);
		knightMoves(knightPos, new int[]{2,1}, retVal);
		for (int[] pos : retVal) {
			if (!visited.contains(positionToString(pos))) {
				newCandidates.add(positionToString(pos));
			}
		}
		if (!newCandidates.isEmpty()) {
			for (String cand : newCandidates) {
//				System.out.println(moves + ": " + cand);
				if (moves < minSearch) {
					Set<String> tempVisited = new HashSet<>(visited);
					int search = moveSearch(finish, tempVisited, cand, moves+1, minSearch);
					if (search > -1) {
						if (search < minSearch) {
							minSearch = search;
						}
//						System.out.println("Found " + search + " -> " + cand + " " + minSearch);
						if (finish.containsKey(cand) && finish.get(cand)) {
//							System.out.println("UnFound " + cand);
							finish.put(cand, false);
							visited.remove(cand);
						}
					}
				}
			}
			if (minSearch != 100) return minSearch;
		}
		return -1;
	}

	private boolean finished(Map<String, Boolean> finish) {
		for (Entry<String, Boolean> entry : finish.entrySet()) {
			if (!entry.getValue()) {
				return false;
			}
		}
		return true;
	}

	/*
	 *	position[0] is x coordinate and [1] is the y.
	 *	options[0] is number of boxes piece can move on the x axis and [1] is the y axis.
	 */
	private void knightMoves(int[] position, int[] options, List<int[]> retVal) {
		if (position[0] - options[0] > 0) {
			if (position[1] - options[1] > 0) {
				retVal.add(new int[]{position[0] - options[0], position[1] - options[1]});
			}
			if (position[1] + options[1] <= BOARD_SIZE) {
				retVal.add(new int[]{position[0] - options[0], position[1] + options[1]});
			}
		}
		if (position[0] + options[0] <= BOARD_SIZE) {
			if (position[1] - options[1] > 0) {
				retVal.add(new int[]{position[0] + options[0], position[1] - options[1]});
			}
			if (position[1] + options[1] <= BOARD_SIZE) {
				retVal.add(new int[]{position[0] + options[0], position[1] + options[1]});
			}
		}
	}

	static private int[] findPosition(String piece) {
		int first = Character.getNumericValue(piece.charAt(0))-9;
		int second = Character.getNumericValue(piece.charAt(1));
		return new int[] {first, second};
	}
	static private String positionToString(int[] position) {
		char retval = Character.valueOf((char)(64 + position[0]));
		return retval + "" +position[1];
	}
}
