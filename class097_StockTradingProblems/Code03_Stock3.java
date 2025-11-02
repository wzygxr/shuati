package class082;

// 买卖股票的最佳时机 III
// 给定一个数组，它的第 i 个元素是一支给定的股票在第 i 天的价格。
// 设计一个算法来计算你所能获取的最大利润。你最多可以完成 两笔 交易
// 注意：你不能同时参与多笔交易，你必须在再次购买前出售掉之前的股票
// 测试链接 : https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iii
public class Code03_Stock3 {

	/*
	 * 解题思路:
	 * 这是股票系列问题中的经典难题，最多允许两笔交易。
	 * 核心思想是动态规划，将问题分解为两个子问题：
	 * 1. 第一次交易在第i天结束时的最大利润
	 * 2. 第二次交易在第i天结束时的最大利润
	 * 
	 * 状态定义:
	 * dp1[i] : 在0...i天内完成一次交易的最大利润
	 * dp2[i] : 在0...i天内完成两次交易的最大利润，且第二次交易在第i天卖出
	 * 
	 * 算法步骤:
	 * 1. 计算dp1数组，记录到第i天为止一次交易的最大利润
	 * 2. 计算dp2数组，记录到第i天为止两次交易的最大利润
	 * 3. 返回dp2数组中的最大值
	 * 
	 * 时间复杂度分析:
	 * O(n) - 优化后的版本只需要遍历数组常数次
	 * 
	 * 空间复杂度分析:
	 * O(1) - 空间优化后的版本只使用常数额外空间
	 * 
	 * 是否为最优解:
	 * 是，这是解决该问题的最优解
	 * 
	 * 工程化考量:
	 * 1. 边界条件处理: 空数组或元素较少的情况
	 * 2. 异常处理: 输入参数校验
	 * 3. 可读性: 变量命名清晰，注释详细
	 * 4. 性能优化: 逐步优化从O(n^2)到O(n)时间复杂度
	 * 
	 * 相关题目扩展:
	 * 1. LeetCode 121. 买卖股票的最佳时机 - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/
	 * 2. LeetCode 122. 买卖股票的最佳时机 II - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/
	 * 3. LeetCode 188. 买卖股票的最佳时机 IV - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iv/
	 * 4. LeetCode 309. 最佳买卖股票时机含冷冻期 - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-with-cooldown/
	 * 5. LeetCode 714. 买卖股票的最佳时机含手续费 - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/
	 */

	// 完全不优化枚举的方法
	// 通过不了，会超时
	public static int maxProfit1(int[] prices) {
		// 边界条件处理
		if (prices == null || prices.length <= 1) {
			return 0;
		}
		
		int n = prices.length;
		// dp1[i] : 0...i范围上发生一次交易，不要求在i的时刻卖出，最大利润是多少
		int[] dp1 = new int[n];
		for (int i = 1, min = prices[0]; i < n; i++) {
			min = Math.min(min, prices[i]);
			dp1[i] = Math.max(dp1[i - 1], prices[i] - min);
		}
		// dp2[i] : 0...i范围上发生两次交易，并且第二次交易在i时刻卖出，最大利润是多少
		int[] dp2 = new int[n];
		int ans = 0;
		for (int i = 1; i < n; i++) {
			// 第二次交易一定要在i时刻卖出
			for (int j = 0; j <= i; j++) {
				// 枚举第二次交易的买入时机j <= i
				dp2[i] = Math.max(dp2[i], dp1[j] + prices[i] - prices[j]);
			}
			ans = Math.max(ans, dp2[i]);
		}
		return ans;
	}

	// 观察出优化枚举的方法
	// 引入best数组，需要分析能力
	public static int maxProfit2(int[] prices) {
		// 边界条件处理
		if (prices == null || prices.length <= 1) {
			return 0;
		}
		
		int n = prices.length;
		// dp1[i] : 0...i范围上发生一次交易，不要求在i的时刻卖出，最大利润是多少
		int[] dp1 = new int[n];
		for (int i = 1, min = prices[0]; i < n; i++) {
			min = Math.min(min, prices[i]);
			dp1[i] = Math.max(dp1[i - 1], prices[i] - min);
		}
		// best[i] : 0...i范围上，所有的dp1[i]-prices[i]，最大值是多少
		// 这是数组的设置完全是为了替代最后for循环的枚举行为
		int[] best = new int[n];
		best[0] = dp1[0] - prices[0];
		for (int i = 1; i < n; i++) {
			best[i] = Math.max(best[i - 1], dp1[i] - prices[i]);
		}
		// dp2[i] : 0...i范围上发生两次交易，并且第二次交易在i时刻卖出，最大利润是多少
		int[] dp2 = new int[n];
		int ans = 0;
		for (int i = 1; i < n; i++) {
			// 不需要枚举了
			// 因为，best[i]已经揭示了，0...i范围上，所有的dp1[i]-prices[i]，最大值是多少
			dp2[i] = best[i] + prices[i];
			ans = Math.max(ans, dp2[i]);
		}
		return ans;
	}

	// 发现所有更新行为都可以放在一起
	// 并不需要写多个并列的for循环
	// 就是等义改写，不需要分析能力
	public static int maxProfit3(int[] prices) {
		// 边界条件处理
		if (prices == null || prices.length <= 1) {
			return 0;
		}
		
		int n = prices.length;
		int[] dp1 = new int[n];
		int[] best = new int[n];
		best[0] = -prices[0];
		int[] dp2 = new int[n];
		int ans = 0;
		for (int i = 1, min = prices[0]; i < n; i++) {
			min = Math.min(min, prices[i]);
			dp1[i] = Math.max(dp1[i - 1], prices[i] - min);
			best[i] = Math.max(best[i - 1], dp1[i] - prices[i]);
			dp2[i] = best[i] + prices[i];
			ans = Math.max(ans, dp2[i]);
		}
		return ans;
	}

	// 发现只需要有限几个变量滚动更新下去就可以了
	// 空间压缩的版本
	// 就是等义改写，不需要分析能力
	public static int maxProfit4(int[] prices) {
		// 边界条件处理
		if (prices == null || prices.length <= 1) {
			return 0;
		}
		
		int dp1 = 0;
		int best = -prices[0];
		int ans = 0;
		for (int i = 1, min = prices[0]; i < prices.length; i++) {
			min = Math.min(min, prices[i]);
			dp1 = Math.max(dp1, prices[i] - min);
			best = Math.max(best, dp1 - prices[i]);
			ans = Math.max(ans, best + prices[i]); // ans = Math.max(ans, dp2);
		}
		return ans;
	}

	// 主方法，使用最优解
	public static int maxProfit(int[] prices) {
		return maxProfit4(prices);
	}

	// 测试方法
	public static void main(String[] args) {
		// 测试用例1: [3,3,5,0,0,3,1,4] -> 6
		int[] prices1 = {3, 3, 5, 0, 0, 3, 1, 4};
		System.out.println("测试用例1结果: " + maxProfit(prices1)); // 期望输出: 6
		// 解释: 第4天买入(0)，第6天卖出(3)获利3；第7天买入(1)，第8天卖出(4)获利3；总利润6
		
		// 测试用例2: [1,2,3,4,5] -> 4
		int[] prices2 = {1, 2, 3, 4, 5};
		System.out.println("测试用例2结果: " + maxProfit(prices2)); // 期望输出: 4
		// 解释: 只能进行一次交易，第1天买入(1)，第5天卖出(5)获利4
		
		// 测试用例3: [7,6,4,3,1] -> 0
		int[] prices3 = {7, 6, 4, 3, 1};
		System.out.println("测试用例3结果: " + maxProfit(prices3)); // 期望输出: 0
		// 解释: 价格持续下跌，不交易利润最大
	}
}