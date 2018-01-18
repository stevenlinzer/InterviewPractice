package tree;

/*
 * This is a basic binary Search tree meaning that the left nodes are smaller than right nodes.
 * Nodes are represented by an index and value pair.
 */
public class BinarySearchTree {

	static String[] TREE_DATA = {
			"40 -1 50",			"50 -1 60",			"60 -1 65",
			"65 -1 70",			"70 -1 80",			"80 -1 100",
			"100 -1 150",		"150 -1 170",		"170 -1 190",
			"190 -1 -1"
/*
			"100 50 150",	//" 50 190",
			"50 40 60",
			"60 -1 80",
			"40 -1 -1",
			"80 70 -1",			// " -1 -1"
			"70 65 -1",
			"65 -1 -1",
			"150 -1 170",		// " -1 -1",
			"170 -1 190",		// " 150 -1",
			"190 -1 -1"			// " 170 -1"
*/
	};
	static int treeCount;
	static final int SPACING = 5;

	public static void main(String[] args) {
		// Tree array: [0] value,  [1] leftValue,  [2] rightValue
		int[][] tree = new int[TREE_DATA.length][3];
		for (int i = 0; i < TREE_DATA.length; i++) {
			String[] splitLine =  TREE_DATA[i].split(" ");
			for (int j = 0; j < splitLine.length; j++) {
				tree[i][j] = Integer.parseInt(splitLine[j]);
			}
		}
		for (int i = 0; i < tree.length; i++) {
			tree[i][1] = findNode(tree, tree[i][1]);
			tree[i][2] = findNode(tree, tree[i][2]);
		}
		weightNode(tree, 0, true);
		int[][] treeInfo = sortTree(tree, false);
		printTree(tree, treeInfo);
	}

	private static int findNode(int[][] tree, int nodeValue) {
		for (int i = 0; i < tree.length; i++) {
			if (tree[i][0] == nodeValue) {
				return i;
			}
		}
		return -1;
	}

	private static int[] weightNode(int[][] tree, int nodeNumber, boolean balance) {
		int[] retval = new int[4];
		if (tree[nodeNumber][1] == -1) {
			retval[0] = 0;
		} else {
			int[] temp = weightNode(tree, tree[nodeNumber][1], balance);
			if (balance) {
				if (Math.abs(temp[0]-temp[1]) > 1) {
					int child = tree[nodeNumber][1];
					if (temp[0]-temp[1] > 0) {
						System.out.println("LL)Temp Parent Node " + nodeNumber + " for " + tree[nodeNumber][2] + " has left height (" + temp[0] + " , " + temp[1] + ")");
						int grandchild = tree[child][1];
						tree[nodeNumber][2] = grandchild;
						tree[grandchild][2] = child;
						tree[child][1] = -1;
						temp[0]--;	temp[1]++;
					} else {
						System.out.println("RL)Temp Parent Node " + nodeNumber + " for " + tree[nodeNumber][2] + " has left height (" + temp[0] + " , " + temp[1] + ")");
						// Parent node gets Right grandchild, grandchild gets node
						int grandchild = tree[child][2];
						tree[nodeNumber][2] = grandchild;
						tree[grandchild][1] = child;
						tree[child][2] = -1;
						temp[1]--;	temp[0]++;
					}
				}
			}
			retval[0] = Math.abs(temp[0] - temp[1]) + 1;
			retval[2] = temp[0] + temp[1];
		}
		if (tree[nodeNumber][2] == -1) {
			retval[1] = 0;
		} else {
			int[] temp = weightNode(tree, tree[nodeNumber][2], balance);
			if (balance) {
				if (Math.abs(temp[0]-temp[1]) > 1) {
					int child = tree[nodeNumber][2];
					if (temp[0]-temp[1] > 0) {
						System.out.println("LR)Temp Parent Node " + nodeNumber + " for " + child + " has left height (" + temp[0] + " , " + temp[1] + ")");
						// Parent node gets Left grandchild, grandchild gets node
						int grandchild = tree[child][1];
						tree[nodeNumber][2] = grandchild;
						tree[grandchild][2] = child;
						tree[child][1] = -1;
						temp[0]--;	temp[1]++;
					} else {
						System.out.println("RR)Temp Parent Node " + nodeNumber + " for " + child + " has left height (" + temp[0] + " , " + temp[1] + ")");
						// Parent node gets Right grandchild, grandchild gets node
						int grandchild = tree[child][2];
						tree[nodeNumber][2] = grandchild;
						tree[grandchild][1] = child;
						tree[child][2] = -1;
						temp[1]--;	temp[0]++;
					}
				}
			}
			retval[1] = Math.abs(temp[0] - temp[1]) + 1;
			retval[3] = temp[0] + temp[1];
		}
		System.out.println("Node " + nodeNumber + "(" + tree[nodeNumber][0] + ") has left height (" + retval[0] + " , " + retval[1] + ") (" + retval[2] + ","+ retval[3] + ")");
		return retval;
	}

	private static int[][] sortTree(int[][] tree, boolean assending) {
		int[][] retval = new int[tree.length][2];
		if (assending) {
			treeCount = 0;
		} else {
			treeCount = tree.length-1;
		}
		getSortNode(tree, 0, 0, retval, assending);
		return retval;
	}

	private static void getSortNode(int[][] tree, int nodeNumber, int level, int[][] retval, boolean assending) {
		if (tree[nodeNumber][1] == -1 && tree[nodeNumber][2] == -1) {
			retval[nodeNumber][0] = assending ? treeCount++ : treeCount--;
			retval[nodeNumber][1] = level;
		} else {
			if (tree[nodeNumber][1] != -1) {
				getSortNode(tree, tree[nodeNumber][1], level+1, retval, assending);
			}
			retval[nodeNumber][0] = assending ? treeCount++ : treeCount--;
			retval[nodeNumber][1] = level;
			if (tree[nodeNumber][2] != -1) {
				getSortNode(tree, tree[nodeNumber][2], level+1, retval, assending);
			}
		}
	}

	private static void printTree(int[][] tree, int[][] treeSort) {
		for (int recordCount = 0; recordCount < tree.length; recordCount++) {
			for (int i = 0; i < tree.length; i++) {
				if (treeSort[i][0] == recordCount) {
					int tabs = treeSort[i][1] * (SPACING);
					String format = "%" + (tabs == 0 ? "" : ""+tabs) + "s%n";
					System.out.printf(format, tree[i][0]);
					break;
				}
			}
		}
	}
}
