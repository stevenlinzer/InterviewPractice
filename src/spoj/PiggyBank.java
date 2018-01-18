package spoj;

import java.io.IOException;
/*
3
10 110
2
1 1
30 50
10 110
2
1 1
50 30
1 6
2
10 3
20 4
*/
public class PiggyBank {
/*	private static long knapsack01(int totalWeight, int[] weights, int[] values, int size) {
		if (totalWeight == 0 || size == 0) return 0;
		if (weights[size-1] > totalWeight)
			return knapsack01(totalWeight, weights, values, size-1);
		long addCoin = values[size-1] + knapsack01(totalWeight - weights[size-1], weights, values, size-1);
		long nextCoin = knapsack01(totalWeight, weights, values, size-1);
		System.out.println(totalWeight + " - " + size + ", same " + addCoin + ", next " + nextCoin);
		return Math.max(addCoin, nextCoin);
	} */
	private static long unboundKnapsack(int totalWeight, int[] weights, int[] values) {
		long[] dp = new long[totalWeight+1];
		for (int w = 1; w <= totalWeight; w++) {
			long minValue = Integer.MAX_VALUE;
			int maxIndex = -1;
			for (int i = 0; i < weights.length; i++) {
				if (weights[i] <= w) {
					long thisValue = values[i] + dp[w-weights[i]];
					if (dp[w-weights[i]] == -1) thisValue = Integer.MAX_VALUE;
					if (thisValue < minValue) {
						minValue = thisValue;
						maxIndex = i;
					}
				}
			}
			if (maxIndex > -1) {
				dp[w] = minValue;
			} else dp[w] = -1;
		}
		return dp[totalWeight];
	}

	public static void main(String[] args) throws IOException {
	    java.io.BufferedReader reader = new java.io.BufferedReader (new java.io.InputStreamReader (System.in));
		String str = reader.readLine();
		int cases = Integer.parseInt(str);
		for (int cnt = 0; cnt < cases; cnt++) {
			str = reader.readLine();	// The size of the needle
			String[] pigWeights = str.split(" ");
			int totalWeight = Integer.parseInt(pigWeights[1]) - Integer.parseInt(pigWeights[0]);
			str = reader.readLine();
			int coins = Integer.parseInt(str);
			int[] weights = new int[coins];
			int[] values = new int[coins];
			for (int coin = 0; coin < coins; coin++) {
				String text = reader.readLine();
				String[] coinSplit = text.split(" ");
				values[coin] = Integer.parseInt(coinSplit[0]);
				weights[coin] = Integer.parseInt(coinSplit[1]);
			}
			long answer = unboundKnapsack(totalWeight, weights, values); 
			if (answer > -1)
				System.out.println("The minimum amount of money in the piggy-bank is " + answer + ".");
			else
				System.out.println("This is impossible.");
		}
	}
}
