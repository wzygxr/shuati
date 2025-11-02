package class082;

// 买卖股票的最佳时机 IV
// 给你一个整数数组 prices 和一个整数 k ，其中 prices[i] 是某支给定的股票在第 i 天的价格
// 设计一个算法来计算你所能获取的最大利润。你最多可以完成 k 笔交易
// 也就是说，你最多可以买 k 次，卖 k 次
// 注意：你不能同时参与多笔交易，你必须在再次购买前出售掉之前的股票
// 测试链接 : https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iv/
public class Code04_Stock4 {

	/*
	 * 解题思路:
	 * 这是股票系列问题中最通用的一个变种，最多允许k笔交易。
	 * 核心思想是动态规划，使用二维数组dp[i][j]表示最多进行i次交易在前j天能获得的最大利润。
	 * 
	 * 状态定义:
	 * dp[i][j] : 最多进行i次交易，在前j天能获得的最大利润
	 * 
	 * 状态转移方程:
	 * dp[i][j] = max(dp[i][j-1], max(dp[i-1][p] + prices[j] - prices[p])) for p in 0..j-1
	 * 
	 * 优化思路:
	 * 1. 当k >= n/2时，相当于可以无限次交易，退化为股票问题II
	 * 2. 使用best变量优化内层循环，避免重复计算
	 * 
	 * 时间复杂度分析:
	 * O(n*k) - 优化后的版本
	 * 
	 * 空间复杂度分析:
	 * O(n*k) - 未优化空间版本
	 * O(n) - 空间优化后的版本
	 * 
	 * 是否为最优解:
	 * 是，这是解决该问题的最优解
	 * 
	 * 工程化考量:
	 * 1. 边界条件处理: 空数组、k为0或1等特殊情况
	 * 2. 异常处理: 输入参数校验
	 * 3. 可读性: 变量命名清晰，注释详细
	 * 4. 性能优化: 剪枝和空间压缩
	 * 
	 * 相关题目扩展:
	 * 1. LeetCode 121. 买卖股票的最佳时机 - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/
	 * 2. LeetCode 122. 买卖股票的最佳时机 II - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/
	 * 3. LeetCode 123. 买卖股票的最佳时机 III - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iii/
	 * 4. LeetCode 309. 最佳买卖股票时机含冷冻期 - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-with-cooldown/
	 * 5. LeetCode 714. 买卖股票的最佳时机含手续费 - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/
	 */

	// 就是股票问题2
	public static int free(int[] prices) {
		// 边界条件处理
		if (prices == null || prices.length <= 1) {
			return 0;
		}
		
		int ans = 0;
		for (int i = 1; i < prices.length; i++) {
			ans += Math.max(prices[i] - prices[i - 1], 0);
		}
		return ans;
	}

	public static int maxProfit1(int k, int[] prices) {
		// 边界条件处理
		if (prices == null || prices.length <= 1 || k == 0) {
			return 0;
		}
		
		int n = prices.length;
		if (k >= n / 2) {
			// 这是一个剪枝
			// 如果k >= n / 2，那么代表所有上坡都可以抓到
			// 所有上坡都可以抓到 == 交易次数无限，所以回到股票问题2
			return free(prices);
		}
		int[][] dp = new int[k + 1][n];
		for (int i = 1; i <= k; i++) {
			for (int j = 1; j < n; j++) {
				dp[i][j] = dp[i][j - 1];
				for (int p = 0; p < j; p++) {
					dp[i][j] = Math.max(dp[i][j], dp[i - 1][p] + prices[j] - prices[p]);
				}
			}
		}
		return dp[k][n - 1];
	}

	public static int maxProfit2(int k, int[] prices) {
		// 边界条件处理
		if (prices == null || prices.length <= 1 || k == 0) {
			return 0;
		}
		
		int n = prices.length;
		if (k >= n / 2) {
			// 这是一个剪枝
			// 如果k >= n / 2，那么代表所有上坡都可以抓到
			// 所有上坡都可以抓到 == 交易次数无限，所以回到股票问题2
			return free(prices);
		}
		int[][] dp = new int[k + 1][n];
		for (int i = 1, best; i <= k; i++) {
			best = dp[i - 1][0] - prices[0];
			for (int j = 1; j < n; j++) {
				// 用best变量替代了枚举行为
				dp[i][j] = Math.max(dp[i][j - 1], best + prices[j]);
				best = Math.max(best, dp[i - 1][j] - prices[j]);
			}
		}
		return dp[k][n - 1];
	}

	// 对方法2进行空间压缩的版本
	public static int maxProfit3(int k, int[] prices) {
		// 边界条件处理
		if (prices == null || prices.length <= 1 || k == 0) {
			return 0;
		}
		
		int n = prices.length;
		if (k >= n / 2) {
			// 这是一个剪枝
			// 如果k >= n / 2，那么代表所有上坡都可以抓到
			// 所有上坡都可以抓到 == 交易次数无限，所以回到股票问题2
			return free(prices);
		}
		int[] dp = new int[n];
		for (int i = 1, best, tmp; i <= k; i++) {
			best = dp[0] - prices[0];
			for (int j = 1; j < n; j++) {
				tmp = dp[j];
				dp[j] = Math.max(dp[j - 1], best + prices[j]);
				best = Math.max(best, tmp - prices[j]);
			}
		}
		return dp[n - 1];
	}
	
	// 主方法，使用最优解
	public static int maxProfit(int k, int[] prices) {
		return maxProfit3(k, prices);
	}

	// 测试方法
	public static void main(String[] args) {
		// 测试用例1: k=2, prices=[2,4,1] -> 2
		int[] prices1 = {2, 4, 1};
		System.out.println("测试用例1结果: " + maxProfit(2, prices1)); // 期望输出: 2
		// 解释: 第1天买入(2)，第2天卖出(4)获利2
		
		// 测试用例2: k=2, prices=[3,2,6,5,0,3] -> 7
		int[] prices2 = {3, 2, 6, 5, 0, 3};
		System.out.println("测试用例2结果: " + maxProfit(2, prices2)); // 期望输出: 7
		// 解释: 第2天买入(2)，第3天卖出(6)获利4；第5天买入(0)，第6天卖出(3)获利3；总利润7
		
		// 测试用例3: k=2, prices=[1,2,4,2,5,7,2,4,9,0] -> 13
		int[] prices3 = {1, 2, 4, 2, 5, 7, 2, 4, 9, 0};
		System.out.println("测试用例3结果: " + maxProfit(2, prices3)); // 期望输出: 13
	}
}