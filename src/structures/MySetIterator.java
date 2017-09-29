package structures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MySetIterator implements Iterator<Object> {
	MySet mySet;
	MyStack myStack;
	int position;
	Object[] data;

	public MySetIterator(MySet mySet) {
		this.mySet = mySet;
		myStack = new MyStack();
		this.position = 0;	// Find first element.
		List<Object> list = new ArrayList<>();
		for (int i = 0; i < this.mySet.roots.length; i++) {
			if (this.mySet.roots[i] != null) {
				extractNode(this.mySet.roots[i], list);
			}
		}
		data = list.toArray(new Object[0]);
		Arrays.sort(data);
//		getLeftStacked();
	}
	private void extractNode(SetNode node, List<Object> list) {
		if (node.left != null) {
			extractNode(node.left, list);
		}
		list.add(node.key);
		if (node.right != null) {
			extractNode(node.right, list);
		}
	}
/*
	private void getLeftStacked() {
		if (this.position != null) {
			while (this.position.left != null) {
				myStack.push(this.position);
				this.position = this.position.left;
			}
		}
	}

	private void whatNext() {
		if (this.position == null) return;
		SetNode tempPosition = this.position;
		if (tempPosition.right != null) {
			this.position = tempPosition.right; 
			getLeftStacked();
		} else {
			this.position = (SetNode)this.myStack.pop();
		}
	}
 */

	@Override
	public boolean hasNext() {
		return this.position < data.length;
	}

	@Override
	public Object next() {	// Have to increment position first.
		if (this.position < data.length) {
			return data[this.position++];
		}
		return null;
	}
}
