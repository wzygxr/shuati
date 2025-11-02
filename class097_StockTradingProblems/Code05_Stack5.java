package class082;

// 买卖股票的最佳时机含手续费
// 给定一个整数数组 prices，其中 prices[i]表示第 i 天的股票价格
// 整数 fee 代表了交易股票的手续费用
// 你可以无限次地完成交易，但是你每笔交易都需要付手续费
// 如果你已经购买了一个股票，在卖出它之前你就不能再继续购买股票了。
// 返回获得利润的最大值
// 注意：这里的一笔交易指买入持有并卖出股票的整个过程，每笔交易你只需要为支付一次手续费
// 测试链接 : https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/
public class Code05_Stack5 {

	/*
	 * 解题思路:
	 * 这是股票系列问题中加入了手续费的变种，允许无限次交易但每笔交易需要支付手续费。
	 * 核心思想是状态机动态规划，维护两个状态：
	 * 1. prepare : 持有股票时的最大利润
	 * 2. done : 不持有股票时的最大利润
	 * 
	 * 状态转移:
	 * prepare = max(prepare, done - prices[i] - fee)  // 买入股票
	 * done = max(done, prepare + prices[i])           // 卖出股票
	 * 
	 * 注意手续费的处理：在买入时扣除手续费，这样每笔交易只扣除一次手续费
	 * 
	 * 时间复杂度分析:
	 * O(n) - 只需要遍历一次数组
	 * 
	 * 空间复杂度分析:
	 * O(1) - 只使用了常数级别的额外空间
	 * 
	 * 是否为最优解:
	 * 是，这是解决该问题的最优解
	 * 
	 * 工程化考量:
	 * 1. 边界条件处理: 空数组或只有一个元素的情况
	 * 2. 异常处理: 输入参数校验
	 * 3. 可读性: 变量命名清晰，注释详细
	 * 
	 * 相关题目扩展:
	 * 1. LeetCode 121. 买卖股票的最佳时机 - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/
	 * 2. LeetCode 122. 买卖股票的最佳时机 II - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/
	 * 3. LeetCode 123. 买卖股票的最佳时机 III - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iii/
	 * 4. LeetCode 188. 买卖股票的最佳时机 IV - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iv/
	 * 5. LeetCode 309. 最佳买卖股票时机含冷冻期 - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-with-cooldown/
	 * 6. 剑指Offer 63. 股票的最大利润 - https://leetcode.cn/problems/gu-piao-de-zui-da-li-run-lcof/
	 */
	public static int maxProfit(int[] prices, int fee) {
		// 边界条件处理
		if (prices == null || prices.length <= 1) {
			return 0;
		}
		
		// prepare : 交易次数无限制情况下，获得收益的同时扣掉了一次购买和手续费之后，最好的情况
		int prepare = -prices[0] - fee;
		// done : 交易次数无限制情况下，能获得的最大收益
		int done = 0;
		for (int i = 1; i < prices.length; i++) {
			// 卖出股票：当前持有股票的收益 + 当前价格
			done = Math.max(done, prepare + prices[i]);
			// 买入股票：当前不持有股票的收益 - 当前价格 - 手续费
			prepare = Math.max(prepare, done - prices[i] - fee);
		}
		return done;
	}

	// 测试方法
	public static void main(String[] args) {
		// 测试用例1: prices=[1, 3, 2, 8, 4, 9], fee=2 -> 8
		int[] prices1 = {1, 3, 2, 8, 4, 9};
		int fee1 = 2;
		System.out.println("测试用例1结果: " + maxProfit(prices1, fee1)); // 期望输出: 8
		// 解释: 第1天买入(1)，第4天卖出(8)获利5，手续费2，净利3；第5天买入(4)，第6天卖出(9)获利5，手续费2，净利3；总利润6
		// 实际最优策略: 第1天买入(1)，第6天卖出(9)获利8，手续费2，净利6
		
		// 测试用例2: prices=[1,3,7,5,10,3], fee=3 -> 6
		int[] prices2 = {1, 3, 7, 5, 10, 3};
		int fee2 = 3;
		System.out.println("测试用例2结果: " + maxProfit(prices2, fee2)); // 期望输出: 6
		// 解释: 第1天买入(1)，第5天卖出(10)获利9，手续费3，净利6
	}
}