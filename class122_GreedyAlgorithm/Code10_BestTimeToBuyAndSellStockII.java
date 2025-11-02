package class091;

// 买卖股票的最佳时机 II
// 给你一个整数数组 prices ，其中 prices[i] 表示某支股票第 i 天的价格。
// 在每一天，你可以决定是否购买和/或出售股票。你在任何时候 最多 只能持有 一股 股票。
// 你也可以先购买，然后在 同一天 出售。
// 返回 你能获得的 最大 利润 。
// 测试链接 : https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/
public class Code10_BestTimeToBuyAndSellStockII {

	/**
	 * 买卖股票的最佳时机 II
	 * 
	 * 算法思路：
	 * 使用贪心策略：
	 * 1. 只要明天的价格比今天高，就在今天买入，明天卖出
	 * 2. 累加所有正收益的交易
	 * 3. 这等价于收集所有上涨区间的利润
	 * 
	 * 正确性分析：
	 * 1. 贪心选择：每次只考虑相邻两天的利润
	 * 2. 如果价格上涨就交易，否则不交易
	 * 3. 这样可以收集到所有可能的利润
	 * 
	 * 时间复杂度：O(n) - 只需要遍历数组一次
	 * 空间复杂度：O(1) - 只使用常数额外空间
	 * 
	 * @param prices 股票价格数组
	 * @return 最大利润
	 */
	public static int maxProfit(int[] prices) {
		int maxProfit = 0;  // 最大利润
		
		// 从第一天遍历到倒数第二天
		for (int i = 0; i < prices.length - 1; i++) {
			// 如果明天的价格比今天高，就在今天买入，明天卖出
			if (prices[i + 1] > prices[i]) {
				maxProfit += prices[i + 1] - prices[i];
			}
		}
		
		// 返回最大利润
		return maxProfit;
	}

	// 测试用例
	public static void main(String[] args) {
		// 测试用例1: prices = [7,1,5,3,6,4] -> 输出: 7
		int[] prices1 = {7, 1, 5, 3, 6, 4};
		System.out.println("测试用例1:");
		System.out.println("股票价格: " + java.util.Arrays.toString(prices1));
		System.out.println("最大利润: " + maxProfit(prices1)); // 期望输出: 7 (1->5: 4, 3->6: 3)
		
		// 测试用例2: prices = [1,2,3,4,5] -> 输出: 4
		int[] prices2 = {1, 2, 3, 4, 5};
		System.out.println("\n测试用例2:");
		System.out.println("股票价格: " + java.util.Arrays.toString(prices2));
		System.out.println("最大利润: " + maxProfit(prices2)); // 期望输出: 4 (1->5: 4)
		
		// 测试用例3: prices = [7,6,4,3,1] -> 输出: 0
		int[] prices3 = {7, 6, 4, 3, 1};
		System.out.println("\n测试用例3:");
		System.out.println("股票价格: " + java.util.Arrays.toString(prices3));
		System.out.println("最大利润: " + maxProfit(prices3)); // 期望输出: 0 (价格持续下跌)
		
		// 测试用例4: prices = [1,2,1,3] -> 输出: 3
		int[] prices4 = {1, 2, 1, 3};
		System.out.println("\n测试用例4:");
		System.out.println("股票价格: " + java.util.Arrays.toString(prices4));
		System.out.println("最大利润: " + maxProfit(prices4)); // 期望输出: 3 (1->2: 1, 1->3: 2)
	}
}