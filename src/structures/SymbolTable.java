package structures;

public class SymbolTable<Key extends Comparable<Key>, Value> {
	static final int SPACING = 5;
	MapNode<Key, Value> root;
	int entries = -1;

	public SymbolTable() {
		entries = 0;
		root = null;
	}
	public Value get(MapNode<Key, Value> n, Key key) {
		while (n != null) {
			int cmp = key.compareTo(n.key);
			if (cmp > 0) return get(n.right, key);
			else if (cmp < 0) return get(n.left, key);
			else return n.value;
		}
		return null;
	}

	public Key max(MapNode<Key, Value> n) {
		if (n == null) return null;
		while  (n.right != null) {
			n = n.right;
		}
		return n.key;
	}

	public Key min(MapNode<Key, Value> n) {
		if (n == null) return null;
		while  (n.left != null) {
			n = n.left;
		}
		return n.key;
	}

	public int size() {
		return this.entries;
	}

	public int rank(Key key) {
		return -1;
	}

	private boolean isRed(MapNode<Key, Value> n) {
		if (n == null) {return false;}
		return n.color == Node.RED;
	}

	private MapNode rotateLeft(MapNode<Key, Value> n) {
		MapNode x = n.right;
		n.right = x.left;
		x.left = n;
		x.color = n.color;
		n.color = Node.RED;
		return x;
	}

	private MapNode rotateRight(MapNode<Key, Value> n) {
		MapNode x = n.left;
		n.left = x.right;
		x.right = n;
		x.color = n.color;
		n.color = Node.RED;
		return x;
	}

	private void flipColors(MapNode<Key, Value> n) {
		n.color = Node.RED;
		n.left.color = Node.BLACK;
		n.right.color = Node.BLACK;
	}

	/*
	 *  Floor: gives the greatest integer that is less than or equal to x.
	 */
	public Key floor(Key key) {
		MapNode n = floor(this.root, key);
		return (Key)n.key;
	}
	private MapNode floor(MapNode<Key, Value> n, Key key) {
		if (n == null) { return null; }
		int cmp = key.compareTo(n.key);
		if (cmp == 0) return n;
		if (cmp < 0) return floor(n.left, key);
		MapNode x = floor(n.right, key);
		if (x != null) return x;
		else return n;
	}

	/*
	 *  Ceiling: gives the least integer that is greater than or equal to x.
	 */
	public Key ceiling(Key key) {
		MapNode n = ceiling(this.root, key);
		return (Key)n.key;
	}
	private MapNode ceiling(MapNode<Key, Value> n, Key key) {
		if (n == null) { return null; }
		int cmp = key.compareTo(n.key);
		if (cmp == 0) return n;
		if (cmp > 0) return ceiling(n.right, key);
		MapNode x = ceiling(n.left, key);
		if (x != null) return x;
		else return n;
	}
/*
	public int rank(Key key) {
		return rank(this.root, key);
	}
	private int rank(Node<Key, Value> n, Key key) {
		if (n == null) { return 0; }
		int cmp = key.compareTo(n.key);
		if (cmp < 0) return rank(n.left, key);
		else if (cmp > 0) return rank(n.right, key);
		else return 0;
	}
 */

	public MapNode put(MapNode<Key, Value> n, Key key, Value value) {
		if (n == null) {
			entries++;
			return new MapNode(key, value, Node.RED);
		}
		int cmp = key.compareTo(n.key);
		if (cmp < 0) n.left = put(n.left, key, value);
		else if (cmp > 0) n.right = put(n.right, key, value);
		else n.value = value;

		if (isRed(n.right) && !isRed(n.left)) n = rotateLeft(n);
		if (isRed(n.left) && isRed(n.left.left)) n = rotateRight(n);
		if (isRed(n.left) && isRed(n.right)) flipColors(n);
		return n;
	}

	public static void main(String[] args) {
		SymbolTable<String, String> st = new SymbolTable<>();
		st.root = st.put(null, "S", "SS");
		st.root = st.put(st.root, "E", "EE");
		st.root = st.put(st.root, "A", "AA");
		st.root = st.put(st.root, "C", "CC");
		st.root = st.put(st.root, "R", "RR");
		st.root = st.put(st.root, "H", "HH");
		st.root = st.put(st.root, "X", "XX");
		st.root = st.put(st.root, "M", "MM");
		st.root = st.put(st.root, "P", "PP");
		st.root = st.put(st.root, "L", "LL");
		printNode(st.root, 0);
		System.out.println("");
		System.out.println("get: " + st.get(st.root, "A") + ", size " + st.size());
		System.out.println("Min: " + st.min(st.root) + ", Max: " + st.max(st.root));
		System.out.println("Floor O: " + st.floor("O") + ", Ceiling O: " + st.ceiling("O"));
		// Stack testing
		MyStack<String> ms = new MyStack<>();
		ms.push("A");ms.push("c");ms.push("G");
		System.out.println("Size " + ms.size() + ", c: " + ms.search("c") + ", A: " + ms.search("A") + ", G: " + ms.search("G"));
		System.out.println("pop " + ms.pop());
		System.out.println("poll " + ms.poll());
		System.out.println("pop 2 " + ms.pop());
		System.out.println("pop 3 " + ms.pop());
		System.out.println("pop 4 " + ms.pop());
		ms.push("C");
		System.out.println("pop 5 " + ms.pop());
	}

	static void printNode(MapNode n, int level) {
		if (n.left != null) {
			printNode(n.left, level + 1);
		} 
		int tabs = level * (SPACING);
		String format = "%" + (tabs == 0 ? "" : ""+tabs) + "s (%s) %n";
		System.out.printf(format, n.key, n.color);
		if (n.right != null) {
			printNode(n.right, level + 1);
		}
	}
}
