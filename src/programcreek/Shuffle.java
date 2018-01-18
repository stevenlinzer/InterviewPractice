package programcreek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Shuffle {
	static String[] STR_BOARD = new String[] {"ABCE", "SFCS", "ADEE"};
	static String[] ANAGRAM_BOARD = new String[] {"torchwood", "doctorwho", "cat", "tac"};

	public static void main(String[] args) {
		if (args.length < 3) {
			System.err.println("Must specify the first and second strings with the destination string.");
			System.exit(-1);
		}
		System.out.println("Answer " + isShuffle(args[0], args[1], args[2]));
		int[] pal = longestPalindrome("abaccdefed");
		System.out.println("Palindrome " + pal[0] + " " + pal[1]);

		System.out.println("Board (true) ABCCED " + isBoardWord("ABCCED"));
		System.out.println("Board (true) SEE " + isBoardWord("SEE"));
		System.out.println("Board (true) ABCB " + isBoardWord("ABCB"));
		System.out.println("Group Anagrams " + groupAnagrams(ANAGRAM_BOARD));
		System.out.println("Missing " + missingNumber(new int[] {0, 1, 2, 4, 5}));
	}

	static int[] longestPalindrome(String target) {
		int[] retval = new int[] {0, 0};
		for (int currStart = 0; currStart < target.length(); currStart++) {
			StringBuilder sb = new StringBuilder();
			for (int currEnd = currStart+1; currEnd <= target.length(); currEnd++) {
//				String reversed = new StringBuilder(target.substring(currStart, currEnd)).reverse().toString();
//				if (target.substring(currStart, currEnd).equals(reversed)) {
				sb.append(target.charAt(currEnd-1));		// Remove substring calculations
				if (isPalindrome(sb.toString())) {
					if (retval[1]-retval[0] < currEnd - currStart) {
						retval[1] = currEnd;
						retval[0] = currStart;
					}
				}
			}
			if (currStart < retval[1]) currStart = retval[1];
		}
		return retval;
	}

	static boolean isPalindrome(String target) {
		char[] chars = target.toCharArray();
		for (int i = 0; i < chars.length/2; i++) {
			if (chars[i] != chars[chars.length-i-1]) {
				return false;
			}
		}
		return true;
	}

	static boolean isBoardWord(String target) {
		char[][] charBoard = new char[STR_BOARD.length][STR_BOARD[0].length()];
		List<String> starts = new ArrayList<String>();
		for (int i = 0; i < STR_BOARD.length; i++) {	// load board
			for (int j = 0; j < STR_BOARD[0].length(); j++) {
				charBoard[i][j] = STR_BOARD[i].charAt(j);
				if (target.charAt(0) == STR_BOARD[i].charAt(j)) {
					starts.add(""+i+","+j);		// Find first character
				}
			}
		}

		for (String start : starts) {
			String[] startSplit = start.split(",");
			int[] startCoord = new int[] {Integer.parseInt(startSplit[0]), Integer.parseInt(startSplit[1])};
			if (isWordOnBoard(target.substring(1), charBoard, startCoord)) return true;
		}
		return false;
	}

	static boolean isWordOnBoard(String target, char[][] charBoard, int[] start) {
		if (target.length() == 0) {		// length check
			return true;
		}
		if (start[0]+1 < charBoard.length && charBoard[start[0]+1][start[1]] == target.charAt(0)) {
			return isWordOnBoard(target.substring(1), charBoard, new int[] {start[0]+1, start[1]});
		}
		if (start[1]+1 < charBoard.length && charBoard[start[0]][start[1]+1] == target.charAt(0)) {
			return isWordOnBoard(target.substring(1), charBoard, new int[] {start[0], start[1]+1});
		}
		if (start[0]-1 >= 0 && charBoard[start[0]-1][start[1]] == target.charAt(0)) {
			return isWordOnBoard(target.substring(1), charBoard, new int[] {start[0]-1, start[1]});
		}
		if (start[1]-1 >= 0 && charBoard[start[0]][start[1]-1] == target.charAt(0)) {
			return isWordOnBoard(target.substring(1), charBoard, new int[] {start[0], start[1]-1});
		}
		return false;
	}

	static boolean isShuffle(String first, String second, String target) {
		int firstPointer = 0;
		int secondPointer = 0;
		int matchCount = 0;
		for (Character ch : target.toCharArray()) {
			if (firstPointer+matchCount < first.length() && ch.equals(first.charAt(firstPointer+matchCount))
					&& secondPointer+matchCount < second.length() && ch.equals(second.charAt(secondPointer+matchCount))) {
				matchCount++;
			} else if (ch.equals(first.charAt(firstPointer+matchCount))) {
				firstPointer += matchCount + 1;
				matchCount = 0;
			} else if (ch.equals(second.charAt(secondPointer+matchCount))) {
				secondPointer += matchCount + 1;
				matchCount = 0;
			} else {
				return false;
			}
		}
		if (first.length() + second.length() != target.length()) {
			return false;
		}
		return true;
	}

	static Map<String, ArrayList<String>> groupAnagrams(String[] strings) {
	    Map<String, ArrayList<String>> map = new HashMap<>();
		for (String str : strings) {
			char[] pot = new char[26];
			for (char ch : str.toCharArray()) {
				pot[ch-'a']++;
			}
			String ns = new String(pot);
			if(map.containsKey(ns)){
	            map.get(ns).add(str);
	        } else {
	            ArrayList<String> al = new ArrayList<String>();
	            al.add(str);
	            map.put(ns, al);
	        }
		}
		return map;
	}

	static private int missingNumber(int[] nums) {
	    int miss=0;
	    for(int i=0; i<nums.length; i++){
	        int temp = (i+1) ^nums[i];
	        miss ^= temp;
	    }
	    return miss;
	}

	static public String getHint(String secret, String guess) {
		Map<Character, Integer> secretMap = new HashMap<>();
		int bulls = 0;
		int cows = 0;
		for (int i = 0; i < guess.length(); i++) {
			if (secret.charAt(i) == guess.charAt(i)) {
				bulls++;
			} else {
				if (secretMap.containsKey(guess.charAt(i))) {
					secretMap.put(guess.charAt(i), secretMap.get(guess.charAt(i))+1);
				} else {
					secretMap.put(guess.charAt(i), 1);
				}
			}
		}
		for (int i = 0; i < guess.length(); i++) {
			if (secret.charAt(i) != guess.charAt(i)) {
				if (!secretMap.containsKey(guess.charAt(i))) {	// ??
				} else if (secretMap.get(guess.charAt(i)) == 1) {
					secretMap.remove(guess.charAt(i));
				} else {
					secretMap.put(guess.charAt(i), secretMap.get(guess.charAt(i))-1);
				}
				cows++;
			}
		}
		return "A"  + bulls + "B" + cows;
	}
}
