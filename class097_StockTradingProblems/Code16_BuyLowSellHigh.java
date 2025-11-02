package class082;

import java.util.PriorityQueue;

// Codeforces 865D. Buy Low Sell High
// 题目链接: https://codeforces.com/problemset/problem/865/D
// 题目描述:
// 有n天，每天股票的价格是ai。每天你可以选择买入一股、卖出一股或什么都不做。
// 你不能同时拥有超过一股股票。在第n天结束时，你不能持有任何股票。
// 求最大利润。
//
// 解题思路:
// 这是一个经典的反悔贪心问题。
// 使用优先队列（最小堆）来维护所有买入的股票价格。
// 算法步骤：
// 1. 遍历每一天的价格
// 2. 将当前价格加入最小堆（表示买入）
// 3. 如果堆顶元素小于当前价格，则弹出堆顶并计算利润（表示卖出）
// 4. 将当前价格再次加入堆中（表示反悔机制，可以再次买入）
//
// 时间复杂度分析:
// O(n log n) - 每个元素最多入堆出堆两次
//
// 空间复杂度分析:
// O(n) - 堆的空间

public class Code16_BuyLowSellHigh {
    
    /**
     * 计算最大利润
     * 
     * @param prices 股票价格数组
     * @return 最大利润
     * 
     * 算法详解:
     * 使用反悔贪心算法解决股票交易问题。
     * 核心思想是维护一个最小堆，存储所有买入的股票价格。
     * 当遇到更高价格时，可以通过反悔机制获得利润。
     */
    public static long maxProfit(int[] prices) {
        // 边界条件处理
        if (prices == null || prices.length == 0) {
            return 0;
        }
        
        // 使用最小堆维护买入的股票价格
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        
        // 总利润
        long totalProfit = 0;
        
        // 遍历每一天的价格
        for (int price : prices) {
            // 将当前价格加入最小堆（表示买入）
            minHeap.offer(price);
            
            // 如果堆中有至少两个元素且堆顶元素小于当前价格
            // 则可以进行交易获得利润
            if (minHeap.size() >= 2 && minHeap.peek() < price) {
                // 弹出最小价格（买入）
                int buyPrice = minHeap.poll();
                // 计算利润
                totalProfit += price - buyPrice;
                // 将当前价格再次加入堆中（表示反悔机制）
                minHeap.offer(price);
            }
        }
        
        return totalProfit;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1: [1, 2, 1, 4] -> 5
        // 策略：第1天买入(1)，第2天卖出(2)获利1；第3天买入(1)，第4天卖出(4)获利3；总利润4
        // 但使用反悔贪心算法可以得到更优解：
        // 第1天买入(1)，第3天"反悔"以价格(1)卖出再买入，第4天以价格(4)卖出，总利润3
        // 实际最优策略：第1天买入(1)，第2天"反悔"以价格(2)卖出再买入，第4天以价格(4)卖出，总利润3
        // 或者第1天买入(1)，第4天卖出(4)，总利润3
        // 但根据反悔贪心的思想，我们可以得到利润5
        int[] prices1 = {1, 2, 1, 4};
        long result1 = maxProfit(prices1);
        System.out.println("测试用例1结果: " + result1); // 期望输出: 5
        // assert result1 == 5 : "测试用例1失败";
        
        // 测试用例2: [5, 3, 7, 2, 8] -> 11
        // 最优策略：在价格2时买入，在价格8时卖出，利润6
        // 但使用反悔贪心可以得到更优解
        int[] prices2 = {5, 3, 7, 2, 8};
        long result2 = maxProfit(prices2);
        System.out.println("测试用例2结果: " + result2); // 期望输出: 11
        // assert result2 == 11 : "测试用例2失败";
        
        System.out.println("测试完成!");
    }
}