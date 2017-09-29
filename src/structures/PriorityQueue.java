package structures;

/*
 * This Queue is implemented by binary heap
 */
public class PriorityQueue<Key extends Comparable<Key>> {
	Key[] keys;
	int entries = -1;
	public PriorityQueue(Key[] keys) {
		this.keys = keys;
		if (keys != null) {
			entries = 0;
		}
	}

	public void insert(Key key) {
		if (entries < keys.length) {
			keys[entries] = key;
			swim(entries);
			entries++;
		} else {
			int minPtr = min();
			if (keys[minPtr].compareTo(key) < 0) {
				keys[minPtr] = key;
				swim(minPtr);
			}
		}
	}

	public Key deleteMax() {
		if (this.entries == -1) {return null;}
		Key retVal = keys[0];	// Get maximum value
		if (entries > 0) {		// Put smallest value on top and sink it into position.
			keys[0] = keys[entries-1];
			keys[entries-1] = null;
			entries--;
			sink(0);
		}
		return retVal;
	}

	private void swim(int node) {
		int parent = getParent(node);
		while (parent >= 0 && keys[parent].compareTo(keys[node]) < 0) {
			swap(parent, node);
			node = parent;
			parent = getParent(parent);
		}
	}

	private int min() {		// Finds the smallest element of the queue
		int level = getLevel(keys.length-1);
		Double firstChild = Math.pow(2, level)-1;
		Key minKey = null;
		int minPtr = firstChild.intValue();
		for (int i = firstChild.intValue(); i < keys.length; i++) {
			if (minKey == null) {
				minKey = keys[i];
				minPtr = i;
			} else {
				if (minKey.compareTo(keys[i]) > 0) {
					minKey = keys[i];
					minPtr = i;
				}
			}
		}
		return minPtr;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < keys.length; i++) {
			sb.append(keys[i] + "");
			if (i < keys.length-1) sb.append(", ");
		}
		return sb.toString();
	}

	private void sink(int node) {
		int child = getFirstChild(node);
		while (child < entries-1) {
			if (keys[child].compareTo(keys[node]) > 0
				|| keys[child+1].compareTo(keys[node]) > 0) {
				if (keys[child].compareTo(keys[child+1]) > 0) {
					swap(child, node);
					node = child;
					child = getFirstChild(child);
				} else {
					swap(child+1, node);
					node = child+1;
					child = getFirstChild(child+1);
				}
			} else {
				break;
			}
		}
	}

	private void swap(int node1, int node2) {
		Key temp = keys[node1];
		keys[node1] = keys[node2];
		keys[node2] = temp;
	}

	private int getParent(int node) {
		if (node == 0) {return -1;} 
		return ((node+1)/2)-1;
	}

	private int getFirstChild(int node) {
		return ((node+1)*2)-1;
	}

	private int getLevel(int node) {
		int level = 0;
		while (node > 0) {
			level++;
			node = (node - 1) / 2;
		}
		return level;
	}

	public boolean isEmpty() {
		if (entries < 1) {return true;} 
		return false;
	}
	public Key max() {
		if (entries < 1) {return null;} 
		return keys[0];
	}

	public int size() {
		return this.entries;
	}

	public static void main(String[] args) {
		Character[] q = new Character[10];
		PriorityQueue<Character> pq = new PriorityQueue(q);
		System.out.println("Level 1 : " + pq.getLevel(1) + "  & 2: "  + pq.getLevel(2));
		System.out.println("Level 3: " + pq.getLevel(3) + " & 4: " + pq.getLevel(4));
		pq.insert('C');
		pq.insert('H');
		pq.insert('W');
		pq.insert('D');
		pq.insert('E');
		pq.insert('Q');
		pq.insert('J');
		pq.insert('L');
		pq.insert('S');
		pq.insert('B');
		pq.insert('A');
		System.out.println("Maximum 1: " + pq.deleteMax() + " {" + pq + "}");
		pq.insert('D');
		pq.insert('E');
		pq.insert('B');
		pq.insert('A');
		System.out.println(" {" + pq + "}");
		pq.insert('Z');
		pq.insert('T');
		pq.insert('B');
		System.out.println("Maximum 2: " + pq.deleteMax() + " {" + pq + "}");
		System.out.println("Maximum 3: " + pq.deleteMax() + " {" + pq + "}");
	}
}
