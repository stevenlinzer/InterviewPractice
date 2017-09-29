package structures;

public class MapNode<Key extends Comparable<Key>, Value> {

	Key key;
	Value value;
	MapNode left;
	MapNode right;
	boolean color;		// Color of parent link.

	MapNode(Key key, Value val, boolean color) {
		this.key = key;
		this.value = val;
		this.color = color;
	}
}
