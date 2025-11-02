package class053;

import java.util.*;

// 股票价格跨度
// 设计一个算法收集某些股票的每日报价，并返回该股票当日价格的跨度。
// 当日股票价格的跨度被定义为股票价格小于或等于今天价格的连续日数（从今天开始往回数，包括今天）。
// 例如，如果未来 7 天股票的价格 = [100,80,60,70,60,75,85]，那么股票跨度将是 [1,1,1,2,1,4,6]
// 实现 StockSpanner 类：
// StockSpanner() 初始化类对象。
// int next(int price) 给出今天的股价 price，返回该股票当日价格的跨度。
// 测试链接 : https://leetcode.cn/problems/online-stock-span/
public class Code10_OnlineStockSpan {

	/*
	 * 解题思路：
	 * 这是一个在线算法问题，需要设计一个数据结构来高效计算股票价格跨度。
	 * 使用单调递减栈来解决，栈中存储价格和跨度的二元组。
	 * 
	 * 具体思路：
	 * 1. 对于每个新价格，我们需要找到左边第一个比它大的价格位置
	 * 2. 使用单调递减栈，存储 (价格, 跨度) 的二元组
	 * 3. 当新价格到来时：
	 *    - 如果栈顶价格小于等于当前价格，则可以将其跨度合并到当前价格的跨度中
	 *    - 弹出栈顶元素，累加其跨度到当前跨度中
	 *    - 重复此过程直到栈为空或栈顶价格大于当前价格
	 *    - 将 (当前价格, 当前跨度) 入栈
	 *    - 返回当前跨度
	 * 
	 * 时间复杂度分析：
	 * 均摊 O(1) - 每个元素最多入栈和出栈各一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈的空间最多为n
	 * 
	 * 是否为最优解：
	 * 是，这是解决该问题的最优解
	 */

	static class StockSpanner {
		// 使用单调递减栈，存储 (价格, 跨度) 的二元组
		private Stack<int[]> stack;

		public StockSpanner() {
			stack = new Stack<>();
		}

		public int next(int price) {
			// 当前跨度至少为1（包含今天）
			int span = 1;

			// 如果栈顶价格小于等于当前价格，则可以将其跨度合并到当前跨度中
			while (!stack.isEmpty() && stack.peek()[0] <= price) {
				// 弹出栈顶元素，累加其跨度到当前跨度中
				span += stack.pop()[1];
			}

			// 将 (当前价格, 当前跨度) 入栈
			stack.push(new int[]{price, span});

			// 返回当前跨度
			return span;
		}
	}

	// 测试用例
	public static void main(String[] args) {
		StockSpanner stockSpanner = new StockSpanner();
		
		// 测试用例：价格序列 [100, 80, 60, 70, 60, 75, 85]
		int[] prices = {100, 80, 60, 70, 60, 75, 85};
		int[] expected = {1, 1, 1, 2, 1, 4, 6};
		
		System.out.print("测试用例输出: [");
		for (int i = 0; i < prices.length; i++) {
			int result = stockSpanner.next(prices[i]);
			System.out.print(result);
			if (i < prices.length - 1) {
				System.out.print(", ");
			}
		}
		System.out.println("]"); 
		// 期望输出: [1, 1, 1, 2, 1, 4, 6]
	}
}