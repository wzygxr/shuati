package class093;

/**
 * 买卖股票的最佳时机 II（Best Time to Buy and Sell Stock II）
 * 题目来源：LeetCode 122
 * 题目链接：https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/
 * 
 * 问题描述：
 * 给你一个整数数组 prices ，其中 prices[i] 表示某支股票第 i 天的价格。
 * 在每一天，你可以决定是否购买和/或出售股票。你在任何时候最多只能持有一股股票。
 * 你也可以先购买，然后在同一天出售。
 * 返回你能获得的最大利润。
 * 
 * 算法思路：
 * 使用贪心策略，只要明天的价格比今天高，就在今天买入，明天卖出。
 * 具体步骤：
 * 1. 遍历价格数组，从第二天开始
 * 2. 如果当天价格高于前一天，就累加差价作为利润
 * 3. 最终得到的就是最大利润
 * 
 * 时间复杂度：O(n)，其中n是价格数组的长度，只需遍历数组一次
 * 空间复杂度：O(1)，只使用了常数额外空间
 * 
 * 是否最优解：是。贪心策略在此问题中能得到最优解。
 * 
 * 适用场景：
 * 1. 股票交易策略问题
 * 2. 连续收益最大化问题
 * 
 * 异常处理：
 * 1. 处理空数组情况
 * 2. 处理数组长度为1的边界情况（无法交易）
 * 
 * 工程化考量：
 * 1. 输入验证：检查数组是否为空或长度不足
 * 2. 边界条件：处理单元素数组
 * 3. 性能优化：一次遍历完成计算
 */
public class Code09_BestTimeToBuyAndSellStockII {
    
    /**
     * 计算能获得的最大利润
     * 
     * @param prices 表示股票每天价格的数组
     * @return 最大利润
     */
    public static int maxProfit(int[] prices) {
        // 边界条件检查
        if (prices == null || prices.length <= 1) {
            return 0; // 数组为空或只有一天，无法交易
        }
        
        int totalProfit = 0; // 总利润
        
        // 遍历价格数组，从第二天开始
        for (int i = 1; i < prices.length; i++) {
            // 如果当天价格高于前一天，就累加差价作为利润
            if (prices[i] > prices[i - 1]) {
                totalProfit += prices[i] - prices[i - 1];
            }
        }
        
        return totalProfit;
    }
    
    /**
     * 测试函数，验证算法正确性
     */
    public static void main(String[] args) {
        // 测试用例1: 基本情况 - 有上涨趋势
        int[] prices1 = {7, 1, 5, 3, 6, 4};
        int result1 = maxProfit(prices1);
        System.out.println("测试用例1:");
        System.out.print("价格数组: [");
        for (int i = 0; i < prices1.length; i++) {
            System.out.print(prices1[i]);
            if (i < prices1.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        System.out.println("最大利润: " + result1);
        System.out.println("期望输出: 7");
        System.out.println();
        
        // 测试用例2: 基本情况 - 持续上涨
        int[] prices2 = {1, 2, 3, 4, 5};
        int result2 = maxProfit(prices2);
        System.out.println("测试用例2:");
        System.out.print("价格数组: [");
        for (int i = 0; i < prices2.length; i++) {
            System.out.print(prices2[i]);
            if (i < prices2.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        System.out.println("最大利润: " + result2);
        System.out.println("期望输出: 4");
        System.out.println();
        
        // 测试用例3: 基本情况 - 持续下跌
        int[] prices3 = {7, 6, 4, 3, 1};
        int result3 = maxProfit(prices3);
        System.out.println("测试用例3:");
        System.out.print("价格数组: [");
        for (int i = 0; i < prices3.length; i++) {
            System.out.print(prices3[i]);
            if (i < prices3.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        System.out.println("最大利润: " + result3);
        System.out.println("期望输出: 0");
        System.out.println();
        
        // 测试用例4: 边界情况 - 单元素数组
        int[] prices4 = {5};
        int result4 = maxProfit(prices4);
        System.out.println("测试用例4:");
        System.out.println("价格数组: [5]");
        System.out.println("最大利润: " + result4);
        System.out.println("期望输出: 0");
        System.out.println();
        
        // 测试用例5: 复杂情况 - 波动较大
        int[] prices5 = {3, 3, 5, 0, 0, 3, 1, 4};
        int result5 = maxProfit(prices5);
        System.out.println("测试用例5:");
        System.out.print("价格数组: [");
        for (int i = 0; i < prices5.length; i++) {
            System.out.print(prices5[i]);
            if (i < prices5.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        System.out.println("最大利润: " + result5);
        System.out.println("期望输出: 8");
    }
}