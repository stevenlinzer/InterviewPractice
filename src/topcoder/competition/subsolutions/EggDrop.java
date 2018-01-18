package topcoder.competition.subsolutions;

public class EggDrop {

	static int solveDP(int floors, int eggs) {
		int[][] dp = new int[eggs+1][floors+1];
		for (int j = 0; j <= eggs; j++) {
			dp[j][0] = 0;	// No trials needed for a building of no floors.
			dp[j][1] = 1;	// Need one trial for a building of one floor.
		}
		// With only one egg, the number of trials will be the number of floors.
		for (int i = 0; i <= floors; i++) dp[1][i] = i;
		for (int egg = 2; egg <= eggs; egg++) {
			for (int floor = 2; floor <= floors; floor++) {
				dp[egg][floor] = Integer.MAX_VALUE;
				for (int x = 1; x <= floor; x++) {
					int res = 1 + Math.max(dp[egg-1][x-1], dp[egg][floor-x]); 
					if (res < dp[egg][floor]) dp[egg][floor] = res;
				}
			}
		}
		return dp[eggs][floors];
	}

	static int solveRecursive(int floors, int eggs) {
		if (floors == 0 || floors == 1) return floors;
		if (eggs == 0) return Integer.MAX_VALUE;
		System.out.println("Floors " + floors + " eggs " + eggs + ".");
		if (eggs == 1) return floors;
		int min = Integer.MAX_VALUE;
		for (int i = 1; i <= floors; i++) {
			int brokenEggResult = solveRecursive(i-1, eggs-1);
			int wholeEggResult = solveRecursive(floors-i, eggs);
			int localMin = Math.max(brokenEggResult, wholeEggResult) + 1;
			System.out.println(i + " <F-" + floors + ", e-" + eggs + "> brokenEggResult " + brokenEggResult + " wholeEggResult "+wholeEggResult+ ".");
			if (localMin < min)	min = localMin;
		}
		System.out.println("Return for " + floors + " eggs " + eggs + " - " + min);
		return min;
	}

	public static void main(String[] args) {
		int floors = Integer.parseInt(args[0]);
		int eggs = Integer.parseInt(args[1]);
		System.out.println("For " + args[0] + " floors and "+args[1]+" eggs, tries: " + solveRecursive(floors, eggs));
		System.out.println("For " + args[0] + " floors and "+args[1]+" eggs, tries: " + solveDP(floors, eggs));
	}

}
