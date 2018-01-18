package programcreek;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class ArrayRotate {
	int[] array; 
	ArrayRotate(int size) {
		array = new int[size];
		for (int i=0; i < size; i++) {
			array[i] =  i+1;
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i < array.length; i++) {
			sb.append(array[i] + " ");
		}
		return sb.toString();
	}

	void rotate1() {
		Integer temp = null;
		for (int i=0; i < array.length; i++) {
			if (temp == null) {
				temp = array[i+1];
				array[i+1] = array[i];
			} else {
				int temp1 = array[(i+1) % array.length];
				array[(i+1) % array.length] = temp;
				temp = temp1;
			}
		}
	}

	void rotate(int times) {
		Queue<Integer> temp = new LinkedList<>();
		for (int i=0; i < array.length; i++) {
			if (temp.size() < times) {
				temp.add(array[i+times]);
				array[i+times] = array[i];
			} else {
				temp.add(array[(i+times) % array.length]);
				array[(i+times) % array.length] = temp.poll();
			}
		}
	}

	void sortStrings(String[] strings) {
		for (int i = 0; i < strings.length; i++) {
			for (int j = i+1; j < strings.length; j++) {
				if (strings[i].length() > strings[j].length()) {
					String temp = strings[i];
					strings[i] = strings[j];
					strings[j] = temp;
				}
			}
		}
	}

	int hIndex(int[] citations) {
		Arrays.sort(citations);
		int hIndex = citations.length-1;
		int rest = 0;
		for (int i = citations.length-2; i >= 0; i--) {
			if (rest + citations[i] >= citations[hIndex]) {
				hIndex = i+1;
				rest -= citations[i+1];
			}
			rest += citations[i];
		}
		return citations[hIndex];
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Must specify number of rotations.");
			System.exit(-1);
		}
		ArrayRotate ar = new ArrayRotate(10);
		int intRotate = Integer.parseInt(args[0]);
		if (intRotate > ar.array.length) {
			System.err.println("Number of rotations must be less than the array size.");
			System.exit(-1);
		}

		ar.rotate(intRotate);
		System.out.println(ar.toString());

		String[] strings = new String[] {"abd","a","safadaf", "szdfggfsZDgsagh"};
		ar.sortStrings(strings);
		for (String str : strings) {
			System.out.println(str);
		}
		System.out.println("H Index " + ar.hIndex(new int[]{3, 0, 6, 1, 5}));
	}

}
