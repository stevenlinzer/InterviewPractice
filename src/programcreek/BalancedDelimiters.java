package programcreek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class BalancedDelimiters {
	static String[] leftDelimiters = new String[] {"(", "{", "[", "<"};
	static String[] rightDelimiters = new String[] {")", "}", "]", ">"};
	String input = "when I was in ( that means { well [so to speak]} and [that really means <me>])";

	static List<Integer> findDelimIndexes(String str, String delim){
		int startStr = -1;
//		System.out.println("for " + delim);
		List<Integer> delims = new ArrayList<>();
		do {
			startStr++;
			startStr = str.indexOf(delim, startStr);
			if (startStr > -1) {
				delims.add(startStr);
			}
		} while (startStr > -1);
		return delims;
	}

	private static void isDelimitersBalanced(String arg, Stack<Character> stack) {
		System.out.println("Analizing " + arg);
		List<String> leftList = Arrays.asList(leftDelimiters);
		List<String> rightList = Arrays.asList(rightDelimiters);
		for (Character c : arg.toCharArray()) {
//			System.out.print(c);
			if (leftList.contains(c.toString())) {
				stack.push(c);
//				System.out.println("push " + c);
			} else if (rightList.contains(c.toString())) {
				if (stack.size() < 1) {
					System.err.println("Encountered right delimitor on empty stack.");
				} else {
					int a = rightList.indexOf(c.toString());
					int b = leftList.indexOf(stack.pop().toString());
//					System.out.println("A " + a + ", b " + b);
					if (a != b) {
						System.err.println("Encountered incorrect right delimitor " + rightDelimiters[a] + " for last left delimitor " + leftDelimiters[b] + ".");
					}
				}
			}
		}
	}

	private static String[] fixDelimiters(String arg) {	// Only uses leftDelimiters[0] and rightDelimiters[0]
		List<Integer> leftDelims = findDelimIndexes(arg, "(");
		List<Integer> rightDelims = findDelimIndexes(arg, ")");
		Stack<Character> stack = new Stack<>();
		isDelimitersBalanced(arg, stack);

		System.out.println("( " + leftDelims + ", ) " + rightDelims);
		if (leftDelims.size() == rightDelims.size() && stack.size() == 0) {
			return new String[] {arg};
		}

		List<String> fixes = new ArrayList<String>();
		// The distance to fix the string would be one. That is, either remove the extra parend.
		if (leftDelims.size() < rightDelims.size()) {			// Remove which a right?!.
			int r = 0;		// Right pointer
			for (int l = 0; l < leftDelims.size()-1; l++) {		// Find rightmost parend
				if (leftDelims.get(l+1) > rightDelims.get(r)) {
					r++;
					continue;
				} 
			}
			System.out.println("Remove " + rightDelims.get(r) + ".");
			if (r > 1 && rightDelims.get(r-1) == rightDelims.get(r) - 1) {
				fixes.add(arg.substring(0,rightDelims.get(r)) + arg.substring(rightDelims.get(r)+1));
				fixes.add(arg.substring(0,rightDelims.get(0)) + arg.substring(rightDelims.get(0)+1));
			}
		} else if (leftDelims.size() > rightDelims.size()) {	// Remove which a left?!.
			int l = leftDelims.size()-1;						// Left pointer
			for (int r = rightDelims.size()-1; r >= 0; r--) {		// Find leftmost parend
				if (leftDelims.get(l-1) < rightDelims.get(r)) {
					l--;
					continue;
				} 
			}
			System.out.println("Remove " + leftDelims.get(l) + ".");
			if (l < leftDelims.size() && leftDelims.get(1) == leftDelims.get(0) + 1) {
				fixes.add(arg.substring(0,leftDelims.get(l)) + arg.substring(leftDelims.get(l)+1));
				fixes.add(arg.substring(0,leftDelims.get(leftDelims.size()-1)) + arg.substring(leftDelims.get(leftDelims.size()-1)+1));
			}
		} else {		// Same number of parends, but the right parent is first
			fixes.add("");
		}
		return fixes.toArray(new String[0]);
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Must specify text to be seached.");
			System.exit(-1);
		}

		Stack<Character> stack = new Stack<>();
		isDelimitersBalanced(args[0], stack);
		if (stack.size() == 0) {
			System.out.println("Success!");
		} else {
			List<String> leftList = Arrays.asList(leftDelimiters);
			while (stack.size() > 0) {
				int b = leftList.indexOf(stack.pop().toString());
				System.err.println("Encountered incorrect right delimitor for last left delimitor " + leftDelimiters[b] + ".");
			}
		}
		String[] fixes = fixDelimiters("(()()");		// ")(", "(a)())()"
		System.out.println("Fixs " + fixes.length);
		for (String fix : fixes) {
			System.out.println("Fix: '" + fix + "'");
		}
	}
}
