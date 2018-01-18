package spoj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/*3
(a+(b*c))
((a+b)*(z+x))
((a+t)*((b+(a+c))^(c+d)))
*/
import java.util.Stack;

public class TransformExpression {
	static final char[] OPERANDS = {'+', '-', '/','*', '^'};
	static final char[] BRACKETS = {'(',')'};

	public static void main(String[] args) throws IOException {
	    java.io.BufferedReader reader = new java.io.BufferedReader (new java.io.InputStreamReader (System.in));
		String str = reader.readLine();
		int cases = Integer.parseInt(str);
		List<Character> operandList = new ArrayList<>();
		for (char c : OPERANDS) operandList.add(c);
		for (int cnt = 0; cnt < cases; cnt++) {
			Stack<Character> stack = new Stack<>();
			str = reader.readLine();
			StringBuffer sb = new StringBuffer(); 
			for (char c : str.toCharArray()) {
				if (c == BRACKETS[0]) stack.push(c);
				else if (c == BRACKETS[1]) {
					if (!stack.isEmpty()) {
						if (operandList.contains(stack.peek())) {
							sb.append(stack.pop());
						}
						stack.pop();
					}
				}
				else if (c >= 'a' && c <= 'z') {
					sb.append(c);
					if (!stack.isEmpty() && operandList.contains(stack.peek())) {
						sb.append(stack.pop());
					}
				}
				else {
					int index = operandList.indexOf(c);
					if (index < 0) return;		// ERROR
					if (!stack.isEmpty()) {
						if (index < operandList.indexOf(stack.peek())) {
						}
					}
					stack.push(c);
				}
			}
	    	System.out.println(sb);
		}
	}
}
