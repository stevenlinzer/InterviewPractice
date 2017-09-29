package structures;

import java.util.Collection;
import java.util.Iterator;

public class MySet<Key extends Comparable<Key>> implements Iterable<Object> {
	static final int SPACING = 5;
	static final int BINS = 1000000;
	int entries;
	boolean nullVal;
	SetNode<Key> roots[];

	public MySet() {
		this(10);
	}
	public MySet(int bins) {
		roots = new SetNode[bins];
		removeAllRoots();
		entries = 0;
		nullVal = false;
	}
	public int size() {
		return entries;
	}

	public boolean isEmpty() {
		return entries == 0 ? true : false;
	}

	private boolean isRed(SetNode<Key> n) {
		if (n == null) {return false;}
		return n.color == Node.RED;
	}

	public boolean contains(Key key) {
		if (key == null) {
			return nullVal;
		}
		int hash = key.hashCode() % roots.length;
		return contains(roots[hash], key);
	}

	public boolean containsAll(Collection<Key> keys) {
		for (Key key : keys) {
			int hash = key.hashCode() % roots.length;
			if (!contains(roots[hash], key)) return false;
		}
		return true;
	}

	private boolean contains(SetNode<Key> node, Key key) {
		if (key == null) {
			return nullVal;
		}
		while (node != null) {
			int cmp = key.compareTo(node.key);
			if (cmp > 0) node = node.right;
			else if (cmp < 0) node = node.left;
			else return true;
		}
		return false;
	}

	public boolean remove(Key key) {
		int hash = key.hashCode() % roots.length;
		return remove(roots[hash], key);
	}
	private boolean remove(SetNode<Key> node, Key key) {
		SetNode<Key> parentNode = node;
		SetNode<Key> childNode = node;
		while (childNode != null) {
			int cmp = key.compareTo(node.key);
			if (cmp > 0) {
				if (!childNode.equals(parentNode)) {
					parentNode = childNode;
				}
				childNode = childNode.right;
			}
			else if (cmp < 0) {
				if (!childNode.equals(parentNode)) {
					parentNode = childNode;
				}
				childNode = childNode.left;
			}
			else break;
		}
		if (childNode == null) return false;
		if (childNode.left != null && childNode.right != null) return false;
		if (childNode.equals(parentNode)) return false;
		if (parentNode.left.equals(childNode)) {
			if (childNode.left != null) parentNode.left = childNode.left;
			else if (childNode.right != null) parentNode.left = childNode.right;
		} else if (parentNode.right.equals(childNode)) {
			if (childNode.left != null) parentNode.right = childNode.left;
			else if (childNode.right != null) parentNode.right = childNode.right;
		}
		parentNode = rules(parentNode);
		entries--;
		return true;
	}

	public SetNode add(Key key) {
		if (key == null) {
			if (!nullVal) {
				entries++;
				nullVal = true;
				return null;
			}
		}
		int hash = key.hashCode() % roots.length;
//		root = add(root, key);
		roots[hash] = add(roots[hash], key);
		return roots[hash];
	}

	private SetNode add(SetNode<Key> n, Key key) {
		if (n == null) {
			entries++;
			return new SetNode(key, Node.RED);
		}
		if (key == null) {
			if (!nullVal) {
				entries++;
				nullVal = true;
				return n;
			}
		}
		int cmp = key.compareTo(n.key);
		if (cmp < 0) n.left = add(n.left, key);
		else if (cmp > 0) n.right = add(n.right, key);
		else return n;
		n = rules(n);
		return n;
	}
	private SetNode<Key> rules(SetNode<Key> n) {
		if (isRed(n.right) && !isRed(n.left)) n = rotateLeft(n);
		if (isRed(n.left) && isRed(n.left.left)) n = rotateRight(n);
		if (isRed(n.left) && isRed(n.right)) flipColors(n);
		return n;
	}

	private SetNode rotateLeft(SetNode<Key> n) {
		SetNode x = n.right;
		n.right = x.left;
		x.left = n;
		x.color = n.color;
		n.color = Node.RED;
		return x;
	}

	private SetNode rotateRight(SetNode<Key> n) {
		SetNode x = n.left;
		n.left = x.right;
		x.right = n;
		x.color = n.color;
		n.color = Node.RED;
		return x;
	}

	private void flipColors(SetNode<Key> n) {
		n.color = Node.RED;
		n.left.color = Node.BLACK;
		n.right.color = Node.BLACK;
	}

	public void clear() {
//		root = null;
		removeAllRoots();
		entries = 0;
	}

	private void removeAllRoots() {
		for (int i = 0; i < roots.length; i++) {
			roots[i] = null;
		}
	}

	@Override
	public Iterator<Object> iterator() {
		return new MySetIterator(this);
	}

	static void printNode(SetNode n, int level) {
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

	public static void main(String[] args) {
		MySet ms = new MySet<String>(2);
		ms.add("S");
		ms.add("E");
		ms.add("A");
		ms.add("C");
		ms.add("R");
		ms.add("H");
		ms.add("X");
		ms.add("M");
		ms.add("P");
		ms.add("L");
		System.out.println("Contains X " + ms.contains("X") + ", null " + ms.contains(null));
		ms.add(null);
		for (SetNode root : ms.roots) {
			if (root != null) printNode(root, 0);
		}
		System.out.println("Contains " + ms.contains(null));
		for (Object o : ms) {
			System.out.println("> " + o);
		}
	}
}
