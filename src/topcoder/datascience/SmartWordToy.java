package topcoder.datascience;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import structures.MySet;

public class SmartWordToy {

	public int minPresses(String start, String finish, String[] forbid) {
		Set<String> constraints = makeConstraints(forbid);
		MySet<String> visited = new MySet<>(500000);		// NEEDS TO BE A Set or 500,000 bins!!!
		List<String> candidates = new ArrayList<>();
		candidates.add(start);
		return wordSearch(finish, constraints, visited, candidates, 0);		// Start BFS
	}

	static private int distance(String start, String finish) {
		int retVal = 0;
		for (int i = 0; i < start.length(); i++) {
			int diff = Math.abs(start.charAt(i) - finish.charAt(i));
			retVal += diff > 13 ? 26 - diff : diff;
		}
		return retVal;
	}

	private int wordSearch(String finish, Set<String> constraints, MySet<String> visited, List<String> candidates, int moves) {
		System.out.println(moves + ") candidates " + candidates.size() + ", visited " + visited.size());
		List<String> newCandidates = new ArrayList<>();
		for (String candidate : candidates) {
			if (candidate.equals(finish)) {return moves;}
			if (!constraints.contains(candidate)) {
//				int candidateDist = distance(candidate, finish);
				for (int i = 0; i < candidate.length(); i++) {
					char[] startChars = candidate.toCharArray();
					char iChar = candidate.charAt(i);
					int upChar;
					int downChar;
					if (iChar == 'z') upChar = 'a';
					else upChar = iChar+1;			// one up
					if (iChar == 'a') downChar = 'z';
					else downChar = iChar - 1;	// one down
					startChars[i] = (char)upChar;
					String string = String.valueOf(startChars);
					boolean contains = visited.contains(string);
					if (!contains) {
						newCandidates.add(string);
						visited.add(string);
					}
					startChars[i] = (char)downChar;
					string = String.valueOf(startChars);
					contains = visited.contains(string);
					if (!contains) {
						newCandidates.add(string);
						visited.add(string);
					}
				}
			}
		}
		if (!newCandidates.isEmpty()) {
			int search = wordSearch(finish, constraints, visited, newCandidates, moves+1);
			if (search > -1) return search;
		} else {
			return -1;
		}
		return -1;
	}

	private Set<String> makeConstraints(String[] forbids) {
		if (forbids == null) {return null;}
		Set<String> constraints = new HashSet<>();
		for (String forbid : forbids) {
			String[] forbidSplit = forbid.split(" ");
			if (forbidSplit.length != 4) {return null;}
			for (char c0 : forbidSplit[0].toCharArray()) {
				for (char c1 : forbidSplit[1].toCharArray()) {
					for (char c2 : forbidSplit[2].toCharArray()) {
						for (char c3 : forbidSplit[3].toCharArray()) {
							char[] tempCharArry = new char[]{c0,c1,c2,c3};
							constraints.add(String.valueOf(tempCharArry));							
						}
					}
				}
			}
		}
		return constraints;
	}
}
