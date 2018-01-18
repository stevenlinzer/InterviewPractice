package stringProcessing;

public class SuffixWork {

	private static int[] suffixArray(String str) {
		String[] strs = new String[str.length()];
		int[] retval = new int[str.length()];
		for (int i = 0; i < str.length(); i++) {
			strs[i] = str.substring(i);
			int j = 0;
			for (; j < i; j++) {
				if (strs[retval[j]].compareTo(strs[i]) > 0) {
					for (int k = i; k > j; k--) {
						retval[k] = retval[k-1];	// move all higher entries up one.
					}
					break;
				}
			}
			retval[j] = i;
		}
		return retval;
	}

	public static void main(String[] args) {
		int[] ret = suffixArray("banana");
		for (int i = 0; i < ret.length; i++) {
			System.out.print(ret[i] + ", ");
		}
		System.out.println();
	}

}
