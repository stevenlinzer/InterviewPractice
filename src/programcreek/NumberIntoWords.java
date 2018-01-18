package programcreek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NumberIntoWords {
	static Map<Integer, String> onesMap = new HashMap<>();
	static Map<Integer, String> teensMap = new HashMap<>();
	static Map<Integer, String> tensMap = new HashMap<>();

	static {
		onesMap.put(1, "One");
		onesMap.put(2, "Two");
		onesMap.put(3, "Three");
		onesMap.put(4, "Four");
		onesMap.put(5, "Five");
		onesMap.put(6, "Six");
		onesMap.put(7, "Seven");
		onesMap.put(8, "Eight");
		onesMap.put(9, "Nine");
		teensMap.put(11, "Eleven");
		teensMap.put(12, "Twelve");
		teensMap.put(13, "Thirteen");
		teensMap.put(14, "Fourteen");
		teensMap.put(15, "Fifteen");
		teensMap.put(16, "Sixteen");
		teensMap.put(17, "Seventeen");
		teensMap.put(18, "Eighteen");
		teensMap.put(19, "Ninteen");
		tensMap.put(1, "Ten");
		tensMap.put(2, "Twenty");
		tensMap.put(3, "Thirty");
		tensMap.put(4, "Fourty");
		tensMap.put(5, "Fifty");
		tensMap.put(6, "Sixty");
		tensMap.put(7, "Seventy");
		tensMap.put(8, "Eighty");
		tensMap.put(9, "Ninty");
	}

	static String getWords(int number) {
		String retVal = "";
		if (number / 1000 > 0) {
			retVal = getTeenWords(number / 1000) + " Thousand ";
			number %= 1000;
		}
		if (number / 100 > 0) {
			retVal += onesMap.get(number / 100) + " Hundred ";
			number %= 100;
		}
		retVal += getTeenWords(number);
		return retVal;
	}

	static String getTeenWords(int number) {
		String retVal = "";
		if (number / 10 > 0) {
			if (number / 10 == 1 && number % 10 > 0) {
				retVal += teensMap.get(number);
			} else {
				retVal += tensMap.get(number / 10);
				retVal += " " + onesMap.get(number % 10);
			}
		} else {
			retVal += onesMap.get(number % 10);
		}
		return retVal;
	}

	static List<List<Integer>> getZeroValue(int[] numbers) {
		Arrays.sort(numbers);
		int zeroPtr = Arrays.binarySearch(numbers, 0);
		int negPtr = 0;		//	points to the greatest negative number in the list
		int posPtr;
		if (zeroPtr > 0) {
			negPtr = zeroPtr - 1;
			posPtr = zeroPtr + 1;
		} else if (zeroPtr == 0) {		// if this can happens, bad input
			return null;
		} else {
			for (int i = 0; i < numbers.length; i++) {
				if (numbers[i] > 0) {
					negPtr = i-1;
					break;
				}
			}
			posPtr = negPtr + 1;
		}

		Map<Integer, List<Object>> negMap = new HashMap<>();
		for (int i = 0; i <= negPtr-1; i++) {
			for (int j = i+1; j <= negPtr; j++) {
				int thisNum = numbers[i]+numbers[j];
				List<Integer> pair = new ArrayList<>();
				pair.add(numbers[i]);
				pair.add(numbers[j]);
				if (!negMap.containsKey(thisNum)) {
					negMap.put(thisNum, new ArrayList<Object>());
				}
				negMap.get(thisNum).add(pair);
			}
		}

		Map<Integer, List<Object>> posMap = new HashMap<>();
		for (int i = posPtr; i < numbers.length-1; i++) {
			for (int j = i+1; j < numbers.length; j++) {
				int thisNum = numbers[i]+numbers[j];
				List<Integer> pair = new ArrayList<>();
				pair.add(numbers[i]);
				pair.add(numbers[j]);
				if (!posMap.containsKey(thisNum)) {
					posMap.put(thisNum, new ArrayList<Object>());
				}
				posMap.get(thisNum).add(pair);
			}
		}

		List<List<Integer>> retVal = new ArrayList<>();
		for (int i = zeroPtr+1; i < numbers.length; i++) {
			if (negMap.containsKey((-1)*numbers[i])) {
				for(Object obj : negMap.get((-1)*numbers[i])) {
					List<Integer> list = new ArrayList<Integer>((List<Integer>)obj);
					list.add(numbers[i]);
					retVal.add(list);
				}
			}
		}

		if (zeroPtr > 0) {
			int start = 0;
			int end = numbers.length-1;
			while (start < end) {
				if ((-1)*numbers[start] > numbers[end]) {
					start++;
				} else if ((-1)*numbers[start] < numbers[end]) {
					end--;
				} else {
					retVal.add(new ArrayList<Integer>(Arrays.asList(numbers[start], 0, numbers[end])));
					start++; end--;
				}
			}
		}
		return retVal;
	}

	static List<List<Integer>> getZeroThree(int[] numbers) {	// only three values in answer
		Arrays.sort(numbers);
		List<List<Integer>> retVal = new ArrayList<>();
		for (int i = 0; i < numbers.length-1; i++) {
			int j = i+1;
			int k = numbers.length-1;
			while (j < k) {
				if (numbers[i]+numbers[j]+numbers[k] == 0) {
					retVal.add(new ArrayList<Integer>(Arrays.asList(numbers[i], numbers[j], numbers[k])));
					j++; k--;
					while (j < k && numbers[k] == numbers[k-1])		k--;
					while (j < k && numbers[j] == numbers[j+1])		j++;
				} else if (numbers[i]+numbers[j]+numbers[k] > 0) {
					k--;
				} else {
					j++;
				}
			}
		}
		return retVal;
	}

	static List<Integer> getClosestThree(int[] numbers, int target) {
		Arrays.sort(numbers);
		List<Integer> retVal = null;
		Integer closest = null;
		for (int i = 0; i < numbers.length-1; i++) {
			int j = i+1;
			int k = numbers.length-1;
			while (j < k) {
				int thisNum = numbers[i]+numbers[j]+numbers[k];
				if (thisNum == target) {
					return new ArrayList<Integer>(Arrays.asList(numbers[i], numbers[j], numbers[k]));
				} else {
					if (closest == null) {
						closest = thisNum;
						retVal = new ArrayList<Integer>(Arrays.asList(numbers[i], numbers[j], numbers[k]));
					} else if (Math.abs(closest) > Math.abs(thisNum-target)) {
						closest = thisNum;
						retVal = new ArrayList<Integer>(Arrays.asList(numbers[i], numbers[j], numbers[k]));
					}
					if (thisNum > target) {
						k--;
					} else {
						j++;
					}
				}
			}
		}
		return retVal;
	}

	static List<List<Integer>> getZeroFour(int[] numbers) {	// only four values in answer
		Arrays.sort(numbers);
		List<List<Integer>> retVal = new ArrayList<>();
		for (int i = 0; i < numbers.length-1; i++) {
			int j = i+1;
			for (int l = numbers.length-1; l > j; l--) {
				int k = l-1;
				while (j < k) {
					if (numbers[i]+numbers[j]+numbers[k]+numbers[l] == 0) {
						retVal.add(new ArrayList<Integer>(Arrays.asList(numbers[i], numbers[j], numbers[k], numbers[l])));
						j++; k--;
						while (j < k && numbers[k] == numbers[k-1])		k--;
						while (j < k && numbers[j] == numbers[j+1])		j++;
					} else if (numbers[i]+numbers[j]+numbers[k] > 0) {
						k--;
					} else {
						j++;
					}
				}
			}
		}
		return retVal;
	}

	public static void main(String[] args) {
		System.out.println("    9 " + getWords(9));
		System.out.println("   11 " + getWords(11));
		System.out.println("   23 " + getWords(23));
		System.out.println("   57 " + getWords(57));
		System.out.println("  257 " + getWords(257));
		System.out.println("1,357 " + getWords(1357));
		System.out.println("16,857 " + getWords(16857));
		System.out.println("46,857 " + getWords(46857));
		System.out.println("1) Zero Value " + getZeroValue(new int[] {-1, -1, 0, 2, 1, -4}));
		System.out.println("3) Zero Value " + getZeroThree(new int[] {-1, -1, 0, 2, -2, 3, 1, -4}));
		System.out.println("4) Zero Value " + getZeroFour(new int[] {-1, -1, 0, 2, -2, 3, 1, -4}));
		System.out.println("Closest Three Value " + getClosestThree(new int[] {-1, -11, 10, 22, -2, 3, 1, -41}, 4));
	}

}
