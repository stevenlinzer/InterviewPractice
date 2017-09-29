package structures;

public class SetNode<Key extends Comparable<Key>> {
	Key key;
	SetNode left;
	SetNode right;
	boolean color;		// Color of parent link.

	SetNode(Key key, boolean color) {
		this.key = key;
		this.color = color;
	}
	public String toString() {
		return key.toString();
	}
}
