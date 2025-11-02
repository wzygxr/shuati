package class082;

// 买卖股票的最佳时机含冷冻期
// 给定一个整数数组prices，其中第  prices[i] 表示第 i 天的股票价格
// 设计一个算法计算出最大利润
// 在满足以下约束条件下，你可以尽可能地完成更多的交易（多次买卖一支股票）:
// 卖出股票后，你无法在第二天买入股票 (即冷冻期为 1 天)
// 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）
// 测试链接 : https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-with-cooldown/
public class Code06_Stack6 {

	/*
	 * 解题思路:
	 * 这是股票系列问题中加入了冷冻期的变种，卖出股票后有一天冷冻期不能买入。
	 * 核心思想是状态机动态规划，维护两个状态：
	 * 1. prepare[i] : 第i天持有股票时的最大利润
	 * 2. done[i] : 第i天不持有股票时的最大利润
	 * 
	 * 状态转移:
	 * prepare[i] = max(prepare[i-1], done[i-2] - prices[i])  // 继续持有或买入股票(需考虑冷冻期)
	 * done[i] = max(done[i-1], prepare[i-1] + prices[i])     // 继续不持有或卖出股票
	 * 
	 * 时间复杂度分析:
	 * O(n) - 只需要遍历一次数组
	 * 
	 * 空间复杂度分析:
	 * O(n) - 未优化空间版本
	 * O(1) - 空间优化后的版本
	 * 
	 * 是否为最优解:
	 * 是，这是解决该问题的最优解
	 * 
	 * 工程化考量:
	 * 1. 边界条件处理: 空数组或元素较少的情况
	 * 2. 异常处理: 输入参数校验
	 * 3. 可读性: 变量命名清晰，注释详细
	 * 4. 性能优化: 空间压缩优化
	 * 
	 * 相关题目扩展:
	 * 1. LeetCode 121. 买卖股票的最佳时机 - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/
	 * 2. LeetCode 122. 买卖股票的最佳时机 II - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/
	 * 3. LeetCode 123. 买卖股票的最佳时机 III - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iii/
	 * 4. LeetCode 188. 买卖股票的最佳时机 IV - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iv/
	 * 5. LeetCode 714. 买卖股票的最佳时机含手续费 - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/
	 */
	public static int maxProfit1(int[] prices) {
		// 边界条件处理
		if (prices == null || prices.length < 2) {
			return 0;
		}
		
		int n = prices.length;
		if (n < 2) {
			return 0;
		}
		// prepare[i] : 0...i范围上，可以做无限次交易，获得收益的同时一定要扣掉一次购买，所有情况中的最好情况
		int[] prepare = new int[n];
		// done[i] : 0...i范围上，可以做无限次交易，能获得的最大收益
		int[] done = new int[n];
		prepare[1] = Math.max(-prices[0], -prices[1]);
		done[1] = Math.max(0, prices[1] - prices[0]);
		for (int i = 2; i < n; i++) {
			done[i] = Math.max(done[i - 1], prepare[i - 1] + prices[i]);
			prepare[i] = Math.max(prepare[i - 1], done[i - 2] - prices[i]);
		}
		return done[n - 1];
	}

	// 只是把方法1做了变量滚动更新(空间压缩)
	// 并没有新的东西
	public static int maxProfit2(int[] prices) {
		// 边界条件处理
		if (prices == null || prices.length < 2) {
			return 0;
		}
		
		int n = prices.length;
		if (n < 2) {
			return 0;
		}
		// prepare : prepare[i-1]
		int prepare = Math.max(-prices[0], -prices[1]);
		// done2 : done[i-2]
		int done2 = 0;
		// done1 : done[i-1]
		int done1 = Math.max(0, prices[1] - prices[0]);
		for (int i = 2, curDone; i < n; i++) {
			// curDone : done[i]
			curDone = Math.max(done1, prepare + prices[i]);
			// prepare : prepare[i-1] -> prepare[i]
			prepare = Math.max(prepare, done2 - prices[i]);
			done2 = done1;
			done1 = curDone;
		}
		return done1;
	}
	
	// 主方法，使用最优解
	public static int maxProfit(int[] prices) {
		return maxProfit2(prices);
	}

	// 测试方法
	public static void main(String[] args) {
		// 测试用例1: prices=[1,2,3,0,2] -> 3
		int[] prices1 = {1, 2, 3, 0, 2};
		System.out.println("测试用例1结果: " + maxProfit(prices1)); // 期望输出: 3
		// 解释: [买入, 卖出, 冷冻期, 买入, 卖出]
		
		// 测试用例2: prices=[1] -> 0
		int[] prices2 = {1};
		System.out.println("测试用例2结果: " + maxProfit(prices2)); // 期望输出: 0
	}
}