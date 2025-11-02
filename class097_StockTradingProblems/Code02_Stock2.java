package class082;

// 买卖股票的最佳时机 II
// 给你一个整数数组 prices ，其中 prices[i] 表示某支股票第 i 天的价格
// 在每一天，你可以决定是否购买和/或出售股票
// 你在任何时候 最多 只能持有 一股 股票
// 你也可以先购买，然后在 同一天 出售
// 返回 你能获得的 最大 利润
// 测试链接 : https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/
public class Code02_Stock2 {

	/*
	 * 解题思路:
	 * 这是股票系列问题中最简单的一个变种，允许无限次交易。
	 * 核心思想是"贪心算法"，只要明天价格比今天高，就在今天买入明天卖出。
	 * 或者可以理解为收集所有的上升波段。
	 * 
	 * 算法步骤:
	 * 1. 遍历价格数组，从第二天开始
	 * 2. 如果今天价格比昨天高，就将差值加入总利润
	 * 3. 返回总利润
	 * 
	 * 时间复杂度分析:
	 * O(n) - 只需要遍历一次数组，n为数组长度
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
	 * 2. LeetCode 123. 买卖股票的最佳时机 III - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iii/
	 * 3. LeetCode 188. 买卖股票的最佳时机 IV - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iv/
	 * 4. LeetCode 309. 最佳买卖股票时机含冷冻期 - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-with-cooldown/
	 * 5. LeetCode 714. 买卖股票的最佳时机含手续费 - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/
	 * 6. 剑指Offer 63. 股票的最大利润 - https://leetcode.cn/problems/gu-piao-de-zui-da-li-run-lcof/
	 */
	public static int maxProfit(int[] prices) {
		// 边界条件处理
		if (prices == null || prices.length <= 1) {
			return 0;
		}
		
		int ans = 0;
		for (int i = 1; i < prices.length; i++) {
			// 只要今天价格比昨天高，就将差值加入总利润
			// 这等价于在所有上涨日进行交易
			ans += Math.max(prices[i] - prices[i - 1], 0);
		}
		return ans;
	}

	// 补充方法：验证贪心算法的正确性（数学证明）
	public static void validateGreedyAlgorithm() {
		System.out.println("=== 贪心算法正确性验证 ===");
		
		// 数学证明：贪心策略等价于收集所有上升波段
		// 对于任意价格序列，总利润 = Σ(max(0, prices[i] - prices[i-1]))
		// 这等价于在所有的局部最低点买入，局部最高点卖出
		
		int[] testPrices = {1, 3, 2, 5, 4, 6};
		int greedyResult = maxProfit(testPrices);
		
		// 手动计算验证
		int manualResult = 0;
		for (int i = 1; i < testPrices.length; i++) {
			if (testPrices[i] > testPrices[i-1]) {
				manualResult += testPrices[i] - testPrices[i-1];
			}
		}
		
		System.out.println("贪心算法结果: " + greedyResult);
		System.out.println("手动计算结果: " + manualResult);
		System.out.println("验证结果: " + (greedyResult == manualResult ? "通过" : "失败"));
	}
	
	// 补充方法：性能测试
	public static void performanceTest() {
		System.out.println("
=== 性能测试 ===");
		
		// 生成大规模测试数据
		int[] largePrices = new int[100000];
		java.util.Random random = new java.util.Random();
		for (int i = 0; i < largePrices.length; i++) {
			largePrices[i] = random.nextInt(1000) + 1; // 1-1000的随机价格
		}
		
		long startTime = System.currentTimeMillis();
		int result = maxProfit(largePrices);
		long endTime = System.currentTimeMillis();
		
		System.out.println("处理100,000个元素的耗时: " + (endTime - startTime) + "ms");
		System.out.println("计算结果: " + result);
	}
	
	// 补充方法：边界测试
	public static void boundaryTest() {
		System.out.println("
=== 边界测试 ===");
		
		// 测试空数组
		int[] emptyArray = {};
		System.out.println("空数组测试: " + maxProfit(emptyArray) + " (期望: 0)");
		
		// 测试单元素数组
		int[] singleElement = {100};
		System.out.println("单元素数组测试: " + maxProfit(singleElement) + " (期望: 0)");
		
		// 测试全相同价格
		int[] samePrices = {50, 50, 50, 50, 50};
		System.out.println("全相同价格测试: " + maxProfit(samePrices) + " (期望: 0)");
		
		// 测试极端波动
		int[] extremePrices = {1, 1000, 1, 1000, 1};
		System.out.println("极端波动测试: " + maxProfit(extremePrices) + " (期望: 1998)");
	}
	
	// 补充方法：与其他算法对比
	public static void compareWithOtherAlgorithms() {
		System.out.println("
=== 算法对比 ===");
		
		int[] prices = {7, 1, 5, 3, 6, 4};
		
		// 贪心算法
		long startTime = System.nanoTime();
		int greedyResult = maxProfit(prices);
		long greedyTime = System.nanoTime() - startTime;
		
		// 动态规划解法（用于对比）
		startTime = System.nanoTime();
		int dpResult = maxProfitDP(prices);
		long dpTime = System.nanoTime() - startTime;
		
		System.out.println("贪心算法结果: " + greedyResult + ", 耗时: " + greedyTime + "ns");
		System.out.println("动态规划结果: " + dpResult + ", 耗时: " + dpTime + "ns");
		System.out.println("结果一致性: " + (greedyResult == dpResult ? "一致" : "不一致"));
	}
	
	// 动态规划解法（用于对比）
	private static int maxProfitDP(int[] prices) {
		if (prices == null || prices.length <= 1) {
			return 0;
		}
		
		int n = prices.length;
		int[][] dp = new int[n][2]; // dp[i][0]表示第i天不持有股票的最大利润，dp[i][1]表示持有股票的最大利润
		
		dp[0][0] = 0; // 第0天不持有股票
		dp[0][1] = -prices[0]; // 第0天持有股票（买入）
		
		for (int i = 1; i < n; i++) {
			// 第i天不持有股票：要么前一天也不持有，要么前一天持有今天卖出
			dp[i][0] = Math.max(dp[i-1][0], dp[i-1][1] + prices[i]);
			// 第i天持有股票：要么前一天也持有，要么前一天不持有今天买入
			dp[i][1] = Math.max(dp[i-1][1], dp[i-1][0] - prices[i]);
		}
		
		return dp[n-1][0]; // 最后一天不持有股票获得最大利润
	}

	// 综合测试方法
	public static void main(String[] args) {
		System.out.println("=== 买卖股票的最佳时机 II 全面测试 ===");
		
		// 基础功能测试
		System.out.println("
--- 基础功能测试 ---");
		// 测试用例1: [7,1,5,3,6,4] -> 7
		int[] prices1 = {7, 1, 5, 3, 6, 4};
		System.out.println("测试用例1结果: " + maxProfit(prices1) + " (期望: 7)");
		// 解释: 第2天买入(1)，第3天卖出(5)获利4；第4天买入(3)，第5天卖出(6)获利3；总利润7
		
		// 测试用例2: [1,2,3,4,5] -> 4
		int[] prices2 = {1, 2, 3, 4, 5};
		System.out.println("测试用例2结果: " + maxProfit(prices2) + " (期望: 4)");
		// 解释: 第1天买入(1)，第5天卖出(5)获利4
		
		// 测试用例3: [7,6,4,3,1] -> 0
		int[] prices3 = {7, 6, 4, 3, 1};
		System.out.println("测试用例3结果: " + maxProfit(prices3) + " (期望: 0)");
		// 解释: 价格持续下跌，不交易利润最大
		
		// 运行补充测试
		validateGreedyAlgorithm();
		boundaryTest();
		performanceTest();
		compareWithOtherAlgorithms();
		
		System.out.println("
=== 所有测试完成 ===");
	}
}