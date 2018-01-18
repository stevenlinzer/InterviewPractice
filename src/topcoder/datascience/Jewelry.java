package topcoder.datascience;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
/*
5
1,2,5,3,4,5
1,2,3,4,5
7,7,8,9,10,11,1,2,2,3,4,5,6
123,217,661,678,796,964,54,111,417,526,917,923
1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000
607,0,18000000000000
----------------------------------------
22: [6, 5, 4, 3, 2, 2] <> [7, 7, 8]
23: [6, 5, 4, 3, 2, 2, 1] <> [7, 7, 9]
24: [7, 6, 5, 4, 3] <> [7, 8, 9]
25: [7, 6, 5, 4, 3, 1], [7, 6, 5, 4, 2, 2] <> [7, 8, 10]
26: [7, 6, 5, 4, 3, 2], [7, 6, 5, 4, 3, 2] <> [7, 8, 11], [7, 9, 10]
27: [7, 6, 5, 4, 3, 2, 1], [7, 6, 5, 4, 3, 2, 1] <> [7, 9, 11]
28: [7, 6, 5, 4, 3, 2, 2], [7, 6, 5, 4, 3, 2, 2] <> [8, 9, 11]
29: [7, 6, 5, 4, 3, 2, 2, 1], [7, 6, 5, 4, 3, 2, 2, 1] <> [8, 10, 11]
*/
public class Jewelry {
	static int maxForLIS;

	private static long binomialCoeff(int n, int k)	{
	  long[][] c = new long[n+1][n+1];
	  int i,j;
	  for (i = 0; i <= n; i++) {
		  for (j = 0; j <= Math.min(i, k); j++) {
			  if (i==0 || j==i || j == 0) {		  // Base Cases
				  c[i][j] = 1;
			  } else {
				  c[i][j] = c[i-1][j-1] + c[i-1][j];
			  }
		  }
	  }
	  return c[n][k];
//	  if (k==0 || k==n)		  // Base Cases
//	    return 1;
//	  return  binomialCoeff(n-1, k-1) + binomialCoeff(n-1, k);
	}

	static long comboBinCoeff(int n) {
		long retVal = 0;
		if (n < 4) return n;
		for (int i = 2; i <= n/2; i++) {
			retVal += binomialCoeff(n, i)*binomialCoeff(n-i, i);
//			System.out.println(n +"-"+ i + "> " + retVal);
		}
		return retVal;
	}

	static int firstCombo(int equal, Integer[] values) {
		List<Integer> valueList = new ArrayList<>();
		for (int i : values) valueList.add(i);
		return firstCombo(equal, valueList);
	}

	static Integer[] knapsack01(int equal, List<Integer> values) {
		if (values == null || values.size() < 2) return null;
//		This is code for the 0/1 knapsack solution that returns the index of the first match that equals equal.
		int[][] score = new int[values.size()][equal+1];			// Need a column for initial (not picked) score to be initialized to zero. 
		for (int i = 0; i < values.size(); i++) {
			for (int j = 1; j <= equal; j++) {
				int thisVal = values.get(i);
				int minPrevScore = i == 0 ? score[i][j] : score[i-1][j];
				if (thisVal <= j && thisVal > score[i][j]) {
					int left = j - thisVal;
					if (i > 0) {
						if (left > 0) {
							thisVal += score[i-1][left];
						}
					} else {
						if (left > 0) {
							thisVal += 0;
						}
					}
				} else {
					thisVal = minPrevScore;
				}
				score[i][j] = Math.max(thisVal, minPrevScore);
			}
			if (score[i][equal] == equal) {
				int x = i;
				List<Integer> returnList = new ArrayList<>();
				int start = equal;				// find the elements that first match
				for (; start > 0 && x > -1; x--) {
					if (values.get(x) <= start) {
						returnList.add(values.get(x));
						start -= values.get(x);
					}
				}
				return returnList.toArray(new Integer[0]);
			}
		}
		return new Integer[0];
	}

	static int firstCombo(int equal, List<Integer> values) {
		if (values == null || values.size() < 2) return -1;
//		This is code for the 0/1 knapsack solution that returns the index of the first match that equals equal.
		int[][] score = new int[values.size()][equal+1];			// Need a column for initial (not picked) score to be initialized to zero. 
		for (int i = 0; i < values.size(); i++) {
			for (int j = 1; j <= equal; j++) {
				int thisVal = values.get(i);
				int minPrevScore = i == 0 ? score[i][j] : score[i-1][j];
				if (thisVal <= j && thisVal > score[i][j]) {
					int left = j - thisVal;
					if (i > 0) {
						if (left > 0) {thisVal += score[i-1][left];}
					} else {
						if (left > 0) {thisVal += 0;}
					}
				} else {
					thisVal = minPrevScore;
				}
				score[i][j] = Math.max(thisVal, minPrevScore);
			}
			if (score[i][equal] == equal) {
				return i;
			}
		}
		return -1;
	}
/*
	static int tableCombo(List<Integer> values) {
		if (values == null || values.size() < 4) return 0;
		int max = 0;	// get maximum split by calculating the half of the sum.
		for (Integer i : values) {
			max += i;
		}
		max /= 2;
		int[][] score = new int[values.size()][max+1];			// Need a column for initial (not picked) score to be initialized to zero. 
		for (int i = 0; i < values.size(); i++) {
			for (int j = 1; j <= max; j++) {
				int thisVal = values.get(i);
				int minPrevScore = i == 0 ? score[i][j] : score[i-1][j];
				if (thisVal <= j && thisVal > score[i][j]) {
					int left = j - thisVal;
					if (i > 0) {
						if (left > 0) {
							thisVal += score[i-1][left];
						}
					} else {
						if (left > 0) {
							thisVal += 0;
						}
					}
				} else {
					thisVal = minPrevScore;
				}
				score[i][j] = Math.max(thisVal, minPrevScore);
			}
		}
		return -1;
	}

	static private int combos(int equal, List<Integer> values) {
		if (values.size() < 2) return 0;
		int retVal = 0;
		List<Integer> tempValues = new ArrayList(values);
		int comboIndex = -1;
		do {
			comboIndex = firstCombo(equal, tempValues);
			if (comboIndex > -1) {
				retVal += comboIndex;
				if (comboIndex == values.size()-1) {
					return retVal;
				} else {
					tempValues.remove(comboIndex);
				}
			}
		} while(comboIndex > -1);
		comboIndex += tableCombo(values);
		return retVal;
	}

	static int combos(int equal, Integer[] values) {
		return firstCombo(equal, values);
	}

	static private List<Integer> reduce(Integer[] values, int amount) {
		List<Integer> returnVal = new ArrayList<>();
		for (int s : values) returnVal.add(s);
		return Arrays.asList(knapsack01(amount, returnVal));
	}
*/
	private static long[][] makeDp(int sum, int[] values) {
		long[][] dp = new long[sum+1][values.length];
		for (int j = 0; j < values.length; j++) {
			if (j == 0) {
				dp[values[j]][j] = 1;
			} else {
				int f = 0;
				for (; f < values[j]; f++) {
					dp[f][j] = dp[f][j-1];
				}
				dp[f++][j] = dp[values[j]][j-1] + 1;
				for (; f < sum; f++) {
					dp[f][j] = dp[f][j-1] + dp[f-values[j]][j-1];
				}
			}
		}
		return dp;
	}

	static long howMany(int[] values) {
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		Map<Integer, Integer> valueMap = new TreeMap<>();
		Arrays.sort(values);
		int sum = 0;
		for (int v : values){
			if (v > max) max = v;
			if (v < min) min = v;
			sum += v;
			int oldValue = 0;
			if (valueMap.containsKey(v)) {
				oldValue = valueMap.remove(v);
			}
			valueMap.put(v, oldValue+1);
		}
		if (valueMap.size() == 1 && valueMap.get(max) > 1) {
			return comboBinCoeff(valueMap.get(max));
		}

		//	DP Partition problem solution: make table of possible sums up to half of the maximum.
		long[][] dp = makeDp(sum, values);
		long retVal = 0;
		for (int f = 1; f <= sum/2; f++) {
			if (dp[f][values.length - 1]>1) {
				if (valueMap.containsKey(f)) {
					retVal += (dp[f][values.length - 1]-1) * valueMap.get(f);
				} else {	// Analyze amount row.
					int smallestIndex = 0; // Find the smallest sum amount
					for (;smallestIndex < values.length; smallestIndex++) {
						if (dp[f][smallestIndex] > 0) break;
					}
					List<Integer> biggerValues = new ArrayList<>();
					biggerValues.add(values[smallestIndex]);
					int potTotal = values[smallestIndex];
					System.out.println(f + ") Smallest value is " + values[smallestIndex] + " at index " + smallestIndex + ".");
					for (int restIndex = smallestIndex+1; restIndex < values.length; restIndex++) {
						biggerValues.add(values[restIndex]);
						potTotal += values[restIndex];
						if (potTotal >= f) {
							Integer[] k = knapsack01(f, biggerValues);
							if (k != null && k.length > 0) {
								Arrays.sort(k);
								System.out.println("Match found " + Arrays.asList(k) + " (" + restIndex + ") " + values[restIndex] + " -> add " + dp[f][k[0]] + " to " + retVal);
								retVal += dp[f][k[0]];
								biggerValues.remove(k[k.length-1]);
								potTotal += k[k.length-1];
							}
						}
					}
//					List<Integer> vals = new ArrayList<>();
//					Integer[] firstMatch = null;
//					int removeValue =0;
//					for (int i = 0; i < values.length; i++) {
//						vals.add(values[i]);
//						if (dp[f][i] == 1 && firstMatch == null) {
//							firstMatch = knapsack01(f, vals);
//							Arrays.sort(firstMatch);
//						} else if (dp[f][i] > 0 && firstMatch != null) {
//							List<Integer> secondReduce = reduce(firstMatch, values[i] + removeValue);
//							List<Integer> valTemp = new ArrayList<>(vals);
//							valTemp.removeAll(secondReduce);
//							Integer[] tempArr = valTemp.toArray(new Integer[0]);
//							Arrays.sort(tempArr);
//							if (firstMatch[firstMatch.length-1] <= tempArr[0]) {
//								System.out.println("Match found " + firstMatch[firstMatch.length-1] + " " + tempArr[0] + " for " + f);
//								retVal++;
//							} else {
//								if (values[i] < f) removeValue += values[i];
//							}
//						}
//					}
				}
			}
		}
//		for (int sol = min; sol <= max; sol++) {	// iterate over the solutions.
//			dp[sol-min][0] = combos(sol, valueMap.keySet().toArray(new Integer[0]));
//			if (valueMap.containsKey(sol)) {
//				if (valueMap.get(sol) > 1) {
//					comboCount += comboBinCoeff(valueMap.get(sol));
//				}
//				comboCount += combos(sol, unused) * comboBinCoeff(valueMap.get(sol));
//				if (valueMap.get(sol) > 1) {
//					comboCount += valueMap.get(sol); 
//				}
//				for (int s = 0; s < valueMap.get(sol); s++) {
//					unused.add(sol);
//				}
//			}
//		}
		return retVal;
	}

	static int calculateLIS(int[] values, int size) {
		if (size == 1) return 1;	// Base case.
		int res, ending = 1;
		for (int i = 1; i < size; i++) {
			res = calculateLIS(values, i);
			if (values[i-1] < values[i] && res + 1 > ending) {
				ending = res + 1;
			}
		}
		if (maxForLIS < ending) maxForLIS = ending;
		return ending;
	}

	static int longestIncreasingSequenceRecc(int[] values) {
		maxForLIS = 1;
		calculateLIS(values, values.length);
		return maxForLIS;
	}

	static int longestIncreasingSequenceDp(int[] values) {
		int[] lis = new int[values.length];
		for (int i = 0; i < values.length; i++) lis[i] = 1;
		for (int i = 1; i < values.length; i++) {
			for (int j = 0; j < i; j++) {
				if (values[j] < values[i]) {
					lis[i] = Math.max(lis[j]+1, lis[i]);
				}
			}
		}
		return lis[values.length-1];
	}

	public static void main(String[] args) throws IOException {
	    java.io.BufferedReader reader = new java.io.BufferedReader (new java.io.InputStreamReader (System.in));
		String str = reader.readLine();
		int cases = Integer.parseInt(str);
		System.out.println("2>" + comboBinCoeff(2) + ", 3> " + comboBinCoeff(3) + ", 4> " + comboBinCoeff(4));
		System.out.println("5> " + comboBinCoeff(5) + ", 6> " + comboBinCoeff(6) + ", comboBinCoff(30) " + String.format("%,d", comboBinCoeff(30)));
		System.out.println("8> " + comboBinCoeff(8) + ", 30> " + comboBinCoeff(30) + ".");
		for (int cnt = 0; cnt < cases; cnt++) {
			str = reader.readLine();
			String[] valueStrs = str.split(",");
			int[] values = null;
			if (valueStrs.length > 1) {
				values = new int[valueStrs.length];
				for (int i = 0; i < valueStrs.length; i++) {
					values[i] = Integer.parseInt(valueStrs[i]);
				}
			}
			System.out.println("Jewery Presents> " + howMany(values));
//			System.out.println("LIS Recc> " + longestIncreasingSequenceRecc(values));
			System.out.println("LIS> " + longestIncreasingSequenceDp(values));
		}
	}
}
