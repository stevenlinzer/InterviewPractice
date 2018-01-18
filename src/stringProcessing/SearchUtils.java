package stringProcessing;

import java.util.ArrayList;
import java.util.List;

public class SearchUtils {
	int[] knuthMorrisPrattCompiledPattern;
	int[][] finiteAutomataCompiledPattern;
	String pattern;

	private static String printIntArray(int[] array) {
		StringBuffer sb = new StringBuffer();
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				if (i > 0) sb.append(", ");
				sb.append(array[i]);
			}
		}
		return sb.toString();
	}
	private static String printIntArray(Integer[] array) {
		StringBuffer sb = new StringBuffer();
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				if (i > 0) sb.append(", ");
				sb.append(array[i]);
			}
		}
		return sb.toString();
	}

	public String toString() {
		return pattern + ": " + printIntArray(knuthMorrisPrattCompiledPattern);
	}

	private int getNextStep(String pattern, int state, char aChar) {
		if (state < pattern.length() && aChar == pattern.charAt(state)) {
			return state + 1;
		}
		int i;
		for (int ns = state; ns > 0; ns--) {
			if (aChar == pattern.charAt(ns-1)) {
				for (i = 0; i < ns-1; i++) {
					if (pattern.charAt(i) != pattern.charAt(state-ns+1+i)) {
						break;
					}					
				}
				if (i == ns-1) {
					return ns;
				}
			}
		}
		return 0;
	}

	SearchUtils(String pattern) {
		this.pattern = pattern;
		knuthMorrisPrattCompiledPattern = new int[pattern.length()];
		int i = 1, len = 0;		// Compile the pattern into the KMP array
		while (i < pattern.length()) {
			if (pattern.charAt(i) == pattern.charAt(len)) {
				len++;
				knuthMorrisPrattCompiledPattern[i] = len;
				i++;
			} else {
				if (len != 0) {
					len = knuthMorrisPrattCompiledPattern[len-1];
				} else {
					knuthMorrisPrattCompiledPattern[i] = knuthMorrisPrattCompiledPattern[len];
					i++;
				}
			}
		}

		final int NO_OF_CHARS = 256;
		finiteAutomataCompiledPattern = new int[pattern.length()+1][NO_OF_CHARS];
		for (int state = 0; state <= pattern.length(); ++state) {
			for (char ch = 0; ch < NO_OF_CHARS; ch++) {
				finiteAutomataCompiledPattern[state][ch] = getNextStep(pattern, state, ch);
			}
		}
	}

	public Integer[] matchArrayFiniteAuto(String string) {
		List<Integer> matches = new ArrayList<>();
		int state = 0;
		for (int i = 0; i < string.length(); i++) {
			state = finiteAutomataCompiledPattern[state][string.charAt(i)];
			if (state == pattern.length()) {
				matches.add(i - pattern.length() + 1);
			}
		}
		return matches.toArray(new Integer[0]);
	}

	public Integer[] matchArrayKmp(String string) {
		List<Integer> matches = new ArrayList<>();
		int strPtr = 0;
		int patternPtr = 0;
		while (strPtr < string.length()) {
			if (this.pattern.charAt(patternPtr) == string.charAt(strPtr)) {
				strPtr++; patternPtr++;
			}
			if (patternPtr == this.pattern.length()) {
				matches.add(strPtr - patternPtr);
				patternPtr = knuthMorrisPrattCompiledPattern[patternPtr-1];
			} else if (strPtr < string.length() && this.pattern.charAt(patternPtr) != string.charAt(strPtr)) {
				if (patternPtr != 0) patternPtr = knuthMorrisPrattCompiledPattern[patternPtr-1];
				else strPtr++;
			}
			
		}
		return matches.toArray(new Integer[0]);
	}

	public static void main(String[] args) {
		SearchUtils search1 = new SearchUtils("AAAA");
		System.out.println("KMP1: " + search1 + ".");
		String str = "AAAAABAAA";
		System.out.println("Match '" + str + "': ");
		System.out.println("> KMP " + printIntArray(search1.matchArrayKmp(str)) + " and FA " + printIntArray(search1.matchArrayFiniteAuto(str)));
		str = "AAAAABAAAA";
		System.out.println("Match '" + str + "': ");
		System.out.println("> KMP " + printIntArray(search1.matchArrayKmp(str)) + " and FA " + printIntArray(search1.matchArrayFiniteAuto(str)));
		SearchUtils search2 = new SearchUtils("AABAACAA");
		System.out.println("KMP2 " + search2 + ".");
		str = "AAAAABAACAABAACAA";
		System.out.println("Match '" + str + "': ");
		System.out.println("> KMP " + printIntArray(search2.matchArrayKmp(str)) + " and FA " + printIntArray(search2.matchArrayFiniteAuto(str)));
		int segSize = 9;
		System.out.println("Height of Segment trees of " + segSize + ": " + ((int) (Math.ceil(Math.log(segSize) / Math.log(2)))));
	}

}
