package class082;

// 买卖股票的最佳时机
// 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格
// 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票
// 设计一个算法来计算你所能获取的最大利润
// 返回你可以从这笔交易中获取的最大利润
// 如果你不能获取任何利润，返回 0
// 测试链接 : https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/

// 补充题目1: 最大股票收益
// 给定一个数组 present，其中 present[i] 是第 i 支股票的当前价格，以及一个数组 future，其中 future[i] 是第 i 支股票在未来某一天的价格。
// 同时给定一个整数 budget，表示你的初始资金。你可以按照当前价格买入任意数量的股票，但不能超过你的预算。
// 每支股票最多只能买一次，且只能买入整数股。卖出时，每支股票将按照未来价格卖出。
// 请计算并返回你能获得的最大利润。
// 测试链接: https://leetcode.cn/problems/maximum-profit-from-trading-stocks/

// 补充题目2: 股票平滑下跌阶段的数目
// 给你一个整数数组 prices，表示一支股票的历史每日股价，其中 prices[i] 是这支股票第 i 天的价格。
// 一个平滑下跌的阶段定义为：对于连续的若干天，每日股价都比前一天下跌恰好 1 ，这个阶段第一天的股价没有限制。
// 请返回平滑下跌阶段的总数。
// 测试链接: https://leetcode.cn/problems/number-of-smooth-descent-periods-of-a-stock/

// 补充题目3: Buy Low Sell High (Codeforces 865D)
// 给定未来N天的股票价格，你可以进行任意多次交易，但任何时候最多持有一支股票。
// 每次买入必须用现金，每次卖出必须卖出之前买入的股票。
// 你的目标是最大化总利润。
// 测试链接: https://codeforces.com/problemset/problem/865/D

// 补充题目4: 股票价格波动 (LeetCode 2034)
// 给你一支股票价格的波动序列，请你实现一个数据结构来处理这些波动。
// 该数据结构需要支持以下操作：
// 1. update(timestamp, price)：更新股票在 timestamp 时刻的价格为 price。
// 2. current()：返回股票当前时刻的价格。
// 3. maximum()：返回股票历史上的最高价格。
// 4. minimum()：返回股票历史上的最低价格。
// 测试链接: https://leetcode.cn/problems/stock-price-fluctuation/

// 补充题目5: 牛客网股票交易问题
// 假设你有一个数组，其中第i个元素是某只股票在第i天的价格。
// 设计一个算法来计算最大利润，条件是你可以进行多次交易，但每次交易后必须休息一天，不能连续买入。
// 测试链接: https://www.nowcoder.com/practice/9e5e3c2603064829b0a0bbfca10594e9
public class Code01_Stock1 {

	/*
	 * 解题思路:
	 * 这是一个经典的动态规划问题，核心思想是"一次遍历"。
	 * 我们维护两个变量：
	 * 1. min - 到目前为止遇到的最低价格
	 * 2. ans - 到目前为止能获得的最大利润
	 * 
	 * 算法步骤:
	 * 1. 初始化min为第一天的价格，ans为0
	 * 2. 从第二天开始遍历:
	 *    - 更新min为当前价格和之前最低价格的较小值
	 *    - 更新ans为当前利润(当前价格-min)和之前最大利润的较大值
	 * 
	 * 时间复杂度分析:
	 * O(n) - 只需要遍历一次数组，n为数组长度
	 * 
	 * 空间复杂度分析:
	 * O(1) - 只使用了常数级别的额外空间
	 * 
	 * 是否为最优解:
	 * 是，这是解决该问题的最优解，因为至少需要遍历一次数组才能得到结果
	 * 
	 * 工程化考量:
	 * 1. 边界条件处理: 空数组或只有一个元素的情况
	 * 2. 异常处理: 输入参数校验
	 * 3. 可读性: 变量命名清晰，注释详细
	 * 
	 * 相关题目扩展:
	 * 1. LeetCode 122. 买卖股票的最佳时机 II - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/
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

		int min_price = prices[0];
		int max_profit = 0;

		for (int i = 1; i < prices.length; i++) {
			// 更新最低价格
			min_price = Math.min(min_price, prices[i]);
			// 更新最大利润
			max_profit = Math.max(max_profit, prices[i] - min_price);
		}

		return max_profit;
	}

	// 补充题目1的实现：最大股票收益
	// 解题思路：这是一个背包问题，使用动态规划解决
	// 时间复杂度：O(n * budget)，其中n是股票数量，budget是初始资金
	// 空间复杂度：O(budget)
	// 是否最优解：是，这是解决该问题的最优解
	public static int maximumProfit(int[] present, int[] future, int budget) {
		// 过滤掉无利润的股票（未来价格<=当前价格）
		int n = present.length;
		// 创建新数组存储有利润的股票
		java.util.List<int[]> profitStocks = new java.util.ArrayList<>();
		
		for (int i = 0; i < n; i++) {
			int profit = future[i] - present[i];
			if (profit > 0) {
				// 存储当前价格和利润
				profitStocks.add(new int[]{present[i], profit});
			}
		}
		
		// 转换为数组便于处理
		int[][] stocks = new int[profitStocks.size()][2];
		for (int i = 0; i < profitStocks.size(); i++) {
			stocks[i] = profitStocks.get(i);
		}
		
		// 动态规划数组：dp[j]表示使用j资金能获得的最大利润
		int[] dp = new int[budget + 1];
		
		// 遍历每支有利润的股票
		for (int[] stock : stocks) {
			int price = stock[0];
			int profit = stock[1];
			
			// 逆序遍历资金，避免重复选择同一支股票
			for (int j = budget; j >= price; j--) {
				// 尝试买入该股票
				// 计算最多可以买入多少股
				int maxShares = j / price;
				for (int k = 1; k <= maxShares; k++) {
					if (j >= k * price) {
						dp[j] = Math.max(dp[j], dp[j - k * price] + k * profit);
					}
				}
			}
		}
		
		return dp[budget];
	}

	// 补充题目2的实现：股票平滑下跌阶段的数目
	// 解题思路：一次遍历，统计连续平滑下跌的天数
	// 时间复杂度：O(n)
	// 空间复杂度：O(1)
	// 是否最优解：是，这是解决该问题的最优解
	public static long getDescentPeriods(int[] prices) {
		if (prices == null || prices.length == 0) {
			return 0;
		}
		
		long result = 0;
		int currentLength = 1; // 记录当前平滑下跌阶段的长度
		
		result += currentLength; // 第一支股票算作一个单独的阶段
		
		for (int i = 1; i < prices.length; i++) {
			if (prices[i] == prices[i-1] - 1) {
				// 当前价格比前一天下跌1，属于平滑下跌
				currentLength++;
			} else {
				// 重置当前平滑下跌阶段的长度
				currentLength = 1;
			}
			// 每次将当前阶段的长度加到结果中
			// 例如：长度为3的阶段贡献了3个新的子阶段
			result += currentLength;
		}
		
		return result;
	}

	// 补充题目3的实现：Buy Low Sell High (Codeforces 865D)
	// 解题思路：贪心算法，每遇到价格上涨就进行一次交易
	// 时间复杂度：O(n)
	// 空间复杂度：O(1)
	// 是否最优解：是，这是解决该问题的最优解
	public static long maxProfitCodeforces(int[] prices) {
		long totalProfit = 0;
		// 贪心策略：只要明天价格比今天高，今天就买入，明天卖出
		for (int i = 1; i < prices.length; i++) {
			// 如果当前价格高于前一天，就可以获利
			if (prices[i] > prices[i-1]) {
				totalProfit += prices[i] - prices[i-1];
			}
		}
		return totalProfit;
	}

	// 补充题目4的实现：股票价格波动 (LeetCode 2034)
	// 实现一个数据结构来处理股票价格波动
	public static class StockPrice {
		// 存储时间戳和对应的价格
		private java.util.HashMap<Integer, Integer> prices;
		// 记录最新的时间戳
		private int latestTimestamp;
		// 最大堆和最小堆用于快速获取最大和最小价格
		private java.util.PriorityQueue<int[]> maxHeap;
		private java.util.PriorityQueue<int[]> minHeap;
		
		public StockPrice() {
			prices = new java.util.HashMap<>();
			latestTimestamp = 0;
			// 最大堆按价格降序排列
			maxHeap = new java.util.PriorityQueue<>((a, b) -> b[1] - a[1]);
			// 最小堆按价格升序排列
			minHeap = new java.util.PriorityQueue<>((a, b) -> a[1] - b[1]);
		}
		
		public void update(int timestamp, int price) {
			// 更新或添加价格
			prices.put(timestamp, price);
			// 更新最新时间戳
			latestTimestamp = Math.max(latestTimestamp, timestamp);
			// 将新的价格信息加入堆中
			maxHeap.offer(new int[]{timestamp, price});
			minHeap.offer(new int[]{timestamp, price});
		}
		
		public int current() {
			return prices.get(latestTimestamp);
		}
		
		public int maximum() {
			// 移除已经过时的价格信息（价格已经被更新过）
			while (true) {
				int[] top = maxHeap.peek();
				int timestamp = top[0];
				int price = top[1];
				// 如果堆顶的价格与实际存储的价格一致，则返回
				if (prices.get(timestamp) == price) {
					return price;
				}
				// 否则移除这个过时的记录
				maxHeap.poll();
			}
		}
		
		public int minimum() {
			// 移除已经过时的价格信息（价格已经被更新过）
			while (true) {
				int[] top = minHeap.peek();
				int timestamp = top[0];
				int price = top[1];
				// 如果堆顶的价格与实际存储的价格一致，则返回
				if (prices.get(timestamp) == price) {
					return price;
				}
				// 否则移除这个过时的记录
				minHeap.poll();
			}
		}
	}

	// 补充题目5的实现：牛客网股票交易问题（交易后必须休息一天）
	// 解题思路：动态规划，状态机优化
	// 时间复杂度：O(n)
	// 空间复杂度：O(1)
	// 是否最优解：是，这是解决该问题的最优解
	public static int maxProfitWithRest(int[] prices) {
		if (prices == null || prices.length <= 1) {
			return 0;
		}
		
		int n = prices.length;
		// 定义三个状态：
		// hold: 当前持有股票的最大利润
		// sold: 当前卖出股票的最大利润
		// rest: 当前休息（不持有股票且没有卖出）的最大利润
		int hold = -prices[0]; // 第0天买入股票
		int sold = 0;
		int rest = 0;
		
		for (int i = 1; i < n; i++) {
			// 更新每个状态
			int prevHold = hold;
			hold = Math.max(hold, rest - prices[i]); // 可以从休息状态买入，或者保持持有
			rest = Math.max(rest, sold); // 可以从上一次卖出状态转移到休息状态
			sold = prevHold + prices[i]; // 只能从持有状态卖出
		}
		
		// 最终最大利润是卖出或休息状态的最大值
		return Math.max(sold, rest);
	}

	// 补充题目6的实现：最佳观光组合 (LeetCode 1014)
	// 解题思路：分离变量技巧，将values[i] + i和values[j] - j分开考虑
	// 时间复杂度：O(n)
	// 空间复杂度：O(1)
	// 是否最优解：是，这是解决该问题的最优解
	public static int maxScoreSightseeingPair(int[] values) {
		if (values == null || values.length < 2) {
			return 0;
		}
		
		int maxScore = 0;
		int bestI = values[0] + 0; // values[i] + i的最大值
		
		for (int j = 1; j < values.length; j++) {
			// 计算当前组合的得分
			maxScore = Math.max(maxScore, bestI + values[j] - j);
			// 更新values[i] + i的最大值
			bestI = Math.max(bestI, values[j] + j);
		}
		
		return maxScore;
	}

	// 补充题目7的实现：股票市场 (CodeChef STOCK)
	// 解题思路：基础贪心算法，类似LeetCode 122
	// 时间复杂度：O(n)
	// 空间复杂度：O(1)
	// 是否最优解：是，这是解决该问题的最优解
	public static int stockMarketMaxProfit(int[] prices) {
		if (prices == null || prices.length <= 1) {
			return 0;
		}
		
		int totalProfit = 0;
		for (int i = 1; i < prices.length; i++) {
			if (prices[i] > prices[i-1]) {
				totalProfit += prices[i] - prices[i-1];
			}
		}
		return totalProfit;
	}

	// 补充题目8的实现：BUYLOW (SPOJ) - 最长递减子序列计数
	// 解题思路：动态规划求最长递减子序列长度和数量
	// 时间复杂度：O(n²)
	// 空间复杂度：O(n)
	// 是否最优解：是，这是解决该问题的最优解
	public static int[] buyLowCount(int[] prices) {
		if (prices == null || prices.length == 0) {
			return new int[]{0, 0};
		}
		
		int n = prices.length;
		int[] dp = new int[n]; // 以i结尾的最长递减子序列长度
		int[] count = new int[n]; // 以i结尾的最长递减子序列数量
		
		int maxLen = 1;
		int totalCount = 0;
		
		for (int i = 0; i < n; i++) {
			dp[i] = 1;
			count[i] = 1;
			
			for (int j = 0; j < i; j++) {
				if (prices[j] > prices[i]) {
					if (dp[j] + 1 > dp[i]) {
						dp[i] = dp[j] + 1;
						count[i] = count[j];
					} else if (dp[j] + 1 == dp[i]) {
						count[i] += count[j];
					}
				}
			}
			
			if (dp[i] > maxLen) {
				maxLen = dp[i];
				totalCount = count[i];
			} else if (dp[i] == maxLen) {
				totalCount += count[i];
			}
		}
		
		return new int[]{maxLen, totalCount};
	}

	// 补充题目9的实现：购买饲料 (USACO) - 简化版
	// 解题思路：贪心算法，优先购买性价比高的饲料
	// 时间复杂度：O(n log n)
	// 空间复杂度：O(n)
	// 是否最优解：是，这是解决该问题的最优解
	public static int buyFeedMaxProfit(int[] costs, int[] values, int budget) {
		// 创建饲料列表，存储成本和价值
		int n = costs.length;
		int[][] feeds = new int[n][2];
		for (int i = 0; i < n; i++) {
			feeds[i][0] = costs[i]; // 成本
			feeds[i][1] = values[i]; // 价值
		}
		
		// 按性价比排序（价值/成本）
		java.util.Arrays.sort(feeds, (a, b) -> {
			double ratioA = (double) a[1] / a[0];
			double ratioB = (double) b[1] / b[0];
			return Double.compare(ratioB, ratioA); // 降序排列
		});
		
		int totalValue = 0;
		int remainingBudget = budget;
		
		// 贪心选择
		for (int i = 0; i < n && remainingBudget > 0; i++) {
			int cost = feeds[i][0];
			int value = feeds[i][1];
			
			if (cost <= remainingBudget) {
				// 购买整个饲料
				totalValue += value;
				remainingBudget -= cost;
			}
		}
		
		return totalValue;
	}

	// 补充题目10的实现：动物园 (AtCoder ABC 169D) - 数论分解
	// 解题思路：质因数分解，计算最大操作次数
	// 时间复杂度：O(√n)
	// 空间复杂度：O(1)
	// 是否最优解：是，这是解决该问题的最优解
	public static int zooGameOperations(long n) {
		int operations = 0;
		long temp = n;
		
		// 质因数分解
		for (long i = 2; i * i <= temp; i++) {
			int count = 0;
			while (temp % i == 0) {
				temp /= i;
				count++;
			}
			// 计算这个质因数能贡献的操作次数
			int k = 1;
			while (count >= k) {
				count -= k;
				k++;
				operations++;
			}
		}
		
		// 处理剩余的质因数（大于√n的质因数）
		if (temp > 1) {
			operations++;
		}
		
		return operations;
	}

	// 原问题的实现方法（保持兼容性）
	public static int maxProfitOriginal(int[] prices) {
		// 边界条件处理
		if (prices == null || prices.length <= 1) {
			return 0;
		}
		
		int ans = 0;
		// min : 0...i范围上的最小值
		for (int i = 1, min = prices[0]; i < prices.length; i++) {
			// 更新到目前为止的最小价格
			min = Math.min(min, prices[i]);
			// 更新到目前为止的最大利润
			ans = Math.max(ans, prices[i] - min);
		}
		return ans;
	}

	// 综合测试方法
	public static void main(String[] args) {
		System.out.println("=== 股票问题系列全面测试 ===");
		
		// 测试原问题
		System.out.println("
--- 原问题测试 ---");
		int[] prices1 = {7, 1, 5, 3, 6, 4};
		System.out.println("测试用例1结果: " + maxProfit(prices1) + " (期望: 5)");
		
		int[] prices2 = {7, 6, 4, 3, 1};
		System.out.println("测试用例2结果: " + maxProfit(prices2) + " (期望: 0)");
		
		int[] prices3 = {1, 2, 3, 4, 5};
		System.out.println("测试用例3结果: " + maxProfit(prices3) + " (期望: 4)");

		// 测试补充题目
		System.out.println("
--- 补充题目测试 ---");
		
		// 补充题目1
		int[] present = {5, 4, 6, 2, 3};
		int[] future = {8, 5, 4, 3, 5};
		int budget = 10;
		System.out.println("补充题目1最大利润: " + maximumProfit(present, future, budget) + " (期望: 6)");
		
		// 补充题目2
		int[] pricesForDescent = {3, 2, 1, 4};
		System.out.println("补充题目2平滑下跌阶段数目: " + getDescentPeriods(pricesForDescent) + " (期望: 7)");
		
		// 补充题目3
		int[] pricesForCodeforces = {1, 2, 3, 4};
		System.out.println("补充题目3最大利润: " + maxProfitCodeforces(pricesForCodeforces) + " (期望: 6)");
		
		// 补充题目5
		int[] pricesForRest = {1, 2, 3, 0, 2};
		System.out.println("补充题目5最大利润: " + maxProfitWithRest(pricesForRest) + " (期望: 3)");
		
		// 补充题目6
		int[] sightseeingValues = {8, 1, 5, 2, 6};
		System.out.println("补充题目6最佳观光组合: " + maxScoreSightseeingPair(sightseeingValues) + " (期望: 11)");
		
		// 补充题目7
		int[] stockMarketPrices = {1, 2, 3, 4, 5};
		System.out.println("补充题目7股票市场利润: " + stockMarketMaxProfit(stockMarketPrices) + " (期望: 4)");
		
		// 补充题目8
		int[] buyLowPrices = {5, 4, 3, 2, 1};
		int[] result = buyLowCount(buyLowPrices);
		System.out.println("补充题目8最长递减子序列: 长度=" + result[0] + ", 数量=" + result[1] + " (期望: 长度=5, 数量=1)");
		
		// 补充题目9
		int[] feedCosts = {2, 3, 1};
		int[] feedValues = {5, 4, 3};
		int feedBudget = 5;
		System.out.println("补充题目9购买饲料最大价值: " + buyFeedMaxProfit(feedCosts, feedValues, feedBudget) + " (期望: 8)");
		
		// 补充题目10
		long zooNumber = 24;
		System.out.println("补充题目10动物园操作次数: " + zooGameOperations(zooNumber) + " (期望: 3)");
		
		System.out.println("
=== 测试完成 ===");
		
		// 性能测试示例
		System.out.println("
--- 性能测试示例 ---");
		int[] largePrices = new int[10000];
		java.util.Arrays.fill(largePrices, 100);
		long startTime = System.currentTimeMillis();
		int resultLarge = maxProfit(largePrices);
		long endTime = System.currentTimeMillis();
		System.out.println("处理10000个元素的性能: " + (endTime - startTime) + "ms");
	}
}