package programcreek;
import java.util.Arrays;
import java.util.Stack;

public class Coins {

	public static void main(String[] args) {
		// Use Dynamic programming to figure out all combinations of answser.
		if (args.length < 1) {
			System.err.println("Must specify the amount of change needed.");
			System.exit(-1);
		}
		if (args.length < 2) {
			System.err.println("Must specify the value of coins to be used.");
			System.exit(-1);
		}
		int total = Integer.parseInt(args[0]);	// Load arguments
		System.out.println("Total change " + total);
		Integer[] coinArr = new Integer[args.length-1];
		for (int i = 0; i < args.length-1; i++) {
			coinArr[i] = Integer.parseInt(args[i+1]);
		}
		Arrays.sort(coinArr);

		for (int i = coinArr.length-1; i >= 0; i--) {
			Stack<Integer> stack = new Stack<>();
			Stack<Integer> coinStack = new Stack<>();
			int nextIValue = coinArr[i]; 
			int currentTotal = nextIValue;
			stack.push(nextIValue);
			for (int j = i; j >= 0;) {
				int nextValue = coinArr[j]; 
				currentTotal += nextValue;
				stack.push(nextValue);
				coinStack.push(j);
				int poppedIndex = j;
				while (coinStack.size() >= 1) {
					while (currentTotal <= total) {
						if (currentTotal == total) {
							System.out.println("Solution " + stack);
							break;
						} else {
							stack.add(nextValue);
							coinStack.push(poppedIndex);
							currentTotal += nextValue; 
						}
					}
					int popped = stack.pop();
					currentTotal -= popped;
					poppedIndex = coinStack.pop();
					j--;
					if (j >= 0) {
						nextValue = coinArr[j]; 
					} else {
						do {
							currentTotal -= stack.pop();
							poppedIndex = coinStack.pop() - 1;
						} while (!coinStack.isEmpty() && poppedIndex == -1);
						if (poppedIndex == -1) {
							break;
						}
						nextValue = coinArr[poppedIndex];
					}
				}
			}
		}
	}

}
