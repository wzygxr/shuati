package class089;

import java.util.Arrays;

/**
 * 买卖股票的最佳时机 II - 贪心算法解决方案
 * <p>
 * 题目描述：
 * 给你一个整数数组 prices ，其中 prices[i] 表示某支股票第 i 天的价格
 * 在每一天，你可以决定是否购买和/或出售股票。你在任何时候 最多 只能持有 一股 股票
 * 你也可以先购买，然后在 同一天 出售
 * 返回 你能获得的最大利润
 * <p>
 * 测试链接：https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/
 * <p>
 * 算法思想：
 * 使用贪心算法，将问题转化为每天的收益，只要收益为正就累加
 * 只要明天价格比今天高，就在今天买入明天卖出
 * 累加所有正的收益差值
 */
public class Code07_BestTimeBuySellStockII {

    /**
     * 计算买卖股票能获得的最大利润
     * 
     * @param prices 股票每天的价格数组
     * @return 能获得的最大利润
     * 
     * 时间复杂度：O(n) - 只需要遍历一次数组
     * 空间复杂度：O(1) - 只使用了常数级别的额外空间
     * 
     * 工程化考量：
     * 1. 边界条件处理：处理null输入、空数组和单元素数组
     * 2. 输入验证：检查输入参数的有效性
     * 3. 异常处理：对非法输入进行检查
     * 4. 可读性：使用清晰的变量命名和详细的注释
     * 
     * 算法证明：
     * 由于可以进行多次交易，且交易次数没有限制，我们可以将所有的上涨波段都捕获
     * 任何一段上涨都可以分解为连续的每日上涨，因此贪心策略是正确的
     */
    public static int maxProfit(int[] prices) {
        // 输入验证：处理边界情况
        if (prices == null || prices.length <= 1) {
            return 0;  // 没有交易日或只有一天，无法获得利润
        }
        
        int maxProfit = 0;  // 累计最大利润
        
        // 遍历数组，累加所有正的收益差值
        for (int i = 1; i < prices.length; i++) {
            // 贪心策略：只要今天价格比昨天高，就累加差值作为利润
            if (prices[i] > prices[i - 1]) {
                maxProfit += prices[i] - prices[i - 1];
            }
        }
        
        return maxProfit;
    }

    /**
     * 调试版本的maxProfit函数，打印中间过程
     * 
     * @param prices 股票每天的价格数组
     * @return 能获得的最大利润
     * 
     * 调试技巧：
     * 1. 打印中间变量值
     * 2. 显示每一步的决策过程
     * 3. 帮助理解算法执行流程
     */
    public static int debugMaxProfit(int[] prices) {
        if (prices == null || prices.length <= 1) {
            return 0;
        }
        
        int maxProfit = 0;
        
        System.out.println("执行过程详情:");
        System.out.println("天\t价格\t操作\t收益变化\t累计利润");
        System.out.printf("%d\t%d\t%s\t%d\t\t%d\n", 0, prices[0], "买入", 0, 0);
        
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1]) {
                int profit = prices[i] - prices[i - 1];
                maxProfit += profit;
                System.out.printf("%d\t%d\t%s\t+%d\t\t%d\n", 
                                 i, prices[i], "卖出再买入", profit, maxProfit);
            } else {
                System.out.printf("%d\t%d\t%s\t%d\t\t%d\n", 
                                 i, prices[i], "持有", 0, maxProfit);
            }
        }
        
        return maxProfit;
    }

    /**
     * 打印数组的辅助方法
     * 
     * @param array 要打印的数组
     * @return 格式化的数组字符串
     */
    private static String printArray(int[] array) {
        if (array == null) {
            return "null";
        }
        return Arrays.toString(array);
    }

    /**
     * 测试函数：测试各种边界条件和典型用例
     */
    public static void testMaxProfit() {
        // 测试用例1: [7,1,5,3,6,4]
        int[] prices1 = {7, 1, 5, 3, 6, 4};
        int result1 = maxProfit(prices1);
        System.out.println("输入: " + printArray(prices1));
        System.out.println("输出: " + result1);
        System.out.println("预期: 7");
        System.out.println();
        
        // 测试用例2: [1,2,3,4,5] - 单调递增数组
        int[] prices2 = {1, 2, 3, 4, 5};
        int result2 = maxProfit(prices2);
        System.out.println("输入: " + printArray(prices2));
        System.out.println("输出: " + result2);
        System.out.println("预期: 4");
        System.out.println();
        
        // 测试用例3: [7,6,4,3,1] - 单调递减数组
        int[] prices3 = {7, 6, 4, 3, 1};
        int result3 = maxProfit(prices3);
        System.out.println("输入: " + printArray(prices3));
        System.out.println("输出: " + result3);
        System.out.println("预期: 0");
        System.out.println();
        
        // 测试用例4: 空数组
        int[] prices4 = {};
        int result4 = maxProfit(prices4);
        System.out.println("输入: " + printArray(prices4));
        System.out.println("输出: " + result4);
        System.out.println("预期: 0");
        System.out.println();
        
        // 测试用例5: 单个元素
        int[] prices5 = {1};
        int result5 = maxProfit(prices5);
        System.out.println("输入: " + printArray(prices5));
        System.out.println("输出: " + result5);
        System.out.println("预期: 0");
        System.out.println();
        
        // 测试用例6: 复杂波动
        int[] prices6 = {1, 3, 2, 8, 4, 9};
        int result6 = maxProfit(prices6);
        System.out.println("输入: " + printArray(prices6));
        System.out.println("输出: " + result6);
        System.out.println("预期: 13");
        System.out.println();
        
        // 测试用例7: 多次波动
        int[] prices7 = {3, 3, 5, 0, 0, 3, 1, 4};
        int result7 = maxProfit(prices7);
        System.out.println("输入: " + printArray(prices7));
        System.out.println("输出: " + result7);
        System.out.println("预期: 8");
        System.out.println();
    }

    /**
     * 主函数：运行测试
     */
    public static void main(String[] args) {
        System.out.println("买卖股票的最佳时机 II - 贪心算法解决方案");
        System.out.println("=====================================");
        System.out.println("基础测试:");
        testMaxProfit();
        
        System.out.println("\n调试模式示例:");
        int[] debugPrices = {7, 1, 5, 3, 6, 4};
        System.out.println("\n对数组 " + printArray(debugPrices) + " 进行调试跟踪:");
        int finalProfit = debugMaxProfit(debugPrices);
        System.out.println("\n最终利润: " + finalProfit);
        
        System.out.println("\n算法分析:");
        System.out.println("- 时间复杂度: O(n) - 只需要遍历一次数组");
        System.out.println("- 空间复杂度: O(1) - 只使用常数级别额外空间");
        System.out.println("- 贪心策略: 只要今天比昨天贵，就累加差值作为利润");
        System.out.println("- 最优性: 这种贪心策略能够得到全局最优解，因为所有可能的上涨波段都被捕获");
    }
}