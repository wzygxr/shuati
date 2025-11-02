package class092;

/**
 * LeetCode 121. 买卖股票的最佳时机
 * 题目链接：https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/
 * 难度：简单
 * 
 * 问题描述：
 * 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
 * 你只能选择某一天买入这只股票，并选择在未来的某一个不同的日子卖出该股票。设计一个算法来计算你所能获取的最大利润。
 * 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。
 * 
 * 示例：
 * 输入：[7,1,5,3,6,4]
 * 输出：5
 * 解释：在第 2 天（股票价格 = 1）的时候买入，在第 5 天（股票价格 = 6）的时候卖出，最大利润 = 6-1 = 5 。
 * 
 * 解题思路：
 * 贪心算法（一次遍历）
 * 1. 维护一个最小价格变量，记录遍历过程中的最低价格
 * 2. 维护一个最大利润变量，记录当前价格减去最小价格的最大值
 * 3. 遍历价格数组，更新最小价格和最大利润
 * 
 * 时间复杂度：O(n) - 只需要遍历数组一次
 * 空间复杂度：O(1) - 只使用了常数级别的额外空间
 * 
 * 最优性证明：
 * 贪心策略的正确性：在遍历过程中，我们始终记录当前遇到的最低价格，并用当前价格减去最低价格来更新最大利润。
 * 这样可以确保我们找到的是最低买入点和最高卖出点的组合（在时间顺序正确的前提下）。
 * 
 * 工程化考量：
 * 1. 边界条件处理：空数组、单元素数组
 * 2. 异常处理：负数价格（题目保证非负）
 * 3. 性能优化：避免重复计算，使用简洁的变量命名
 */
public class Code23_BestTimeToBuyAndSellStock {
    
    /**
     * 买卖股票的最佳时机 - 贪心算法解法
     * @param prices 价格数组
     * @return 最大利润
     */
    public static int maxProfit(int[] prices) {
        // 边界条件处理
        if (prices == null || prices.length < 2) {
            return 0; // 无法完成交易
        }
        
        int minPrice = Integer.MAX_VALUE; // 最小价格
        int maxProfit = 0; // 最大利润
        
        for (int price : prices) {
            // 更新最小价格
            if (price < minPrice) {
                minPrice = price;
            }
            // 更新最大利润
            else if (price - minPrice > maxProfit) {
                maxProfit = price - minPrice;
            }
        }
        
        return maxProfit;
    }
    
    /**
     * 优化版本：添加详细注释和调试信息
     */
    public static int maxProfitOptimized(int[] prices) {
        if (prices == null || prices.length < 2) {
            System.out.println("价格数组长度不足，无法完成交易");
            return 0;
        }
        
        int minPrice = Integer.MAX_VALUE;
        int maxProfit = 0;
        
        System.out.println("开始计算股票最大利润...");
        System.out.println("价格序列: " + java.util.Arrays.toString(prices));
        
        for (int i = 0; i < prices.length; i++) {
            int price = prices[i];
            
            if (price < minPrice) {
                minPrice = price;
                System.out.printf("第%d天: 价格%d，更新最小价格为%d%n", i + 1, price, minPrice);
            } else {
                int currentProfit = price - minPrice;
                if (currentProfit > maxProfit) {
                    maxProfit = currentProfit;
                    System.out.printf("第%d天: 价格%d，当前利润%d，更新最大利润为%d%n", 
                            i + 1, price, currentProfit, maxProfit);
                } else {
                    System.out.printf("第%d天: 价格%d，当前利润%d，最大利润保持不变%n", 
                            i + 1, price, currentProfit);
                }
            }
        }
        
        System.out.println("计算完成，最大利润: " + maxProfit);
        return maxProfit;
    }
    
    /**
     * 动态规划解法（对比用）
     * 使用状态机思想，但空间复杂度为O(1)
     */
    public static int maxProfitDP(int[] prices) {
        if (prices == null || prices.length < 2) {
            return 0;
        }
        
        // dp0: 不持有股票的最大利润
        // dp1: 持有股票的最大利润（负数，表示成本）
        int dp0 = 0;
        int dp1 = -prices[0];
        
        for (int i = 1; i < prices.length; i++) {
            // 今天不持有股票：昨天就不持有 或 昨天持有今天卖出
            dp0 = Math.max(dp0, dp1 + prices[i]);
            // 今天持有股票：昨天就持有 或 今天买入（只能买入一次）
            dp1 = Math.max(dp1, -prices[i]);
        }
        
        return dp0;
    }
    
    /**
     * 暴力解法（对比用，时间复杂度O(n^2)）
     */
    public static int maxProfitBruteForce(int[] prices) {
        if (prices == null || prices.length < 2) {
            return 0;
        }
        
        int maxProfit = 0;
        for (int i = 0; i < prices.length - 1; i++) {
            for (int j = i + 1; j < prices.length; j++) {
                int profit = prices[j] - prices[i];
                if (profit > maxProfit) {
                    maxProfit = profit;
                }
            }
        }
        
        return maxProfit;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：标准示例
        int[] prices1 = {7, 1, 5, 3, 6, 4};
        System.out.println("=== 测试用例1 ===");
        System.out.println("价格: " + java.util.Arrays.toString(prices1));
        int result1 = maxProfit(prices1);
        int result1DP = maxProfitDP(prices1);
        int result1Brute = maxProfitBruteForce(prices1);
        System.out.println("贪心算法结果: " + result1 + "，预期: 5");
        System.out.println("动态规划结果: " + result1DP + "，预期: 5");
        System.out.println("暴力解法结果: " + result1Brute + "，预期: 5");
        System.out.println("结果一致性: " + (result1 == result1DP && result1 == result1Brute));
        System.out.println();
        
        // 测试用例2：价格递减，无法获利
        int[] prices2 = {7, 6, 4, 3, 1};
        System.out.println("=== 测试用例2 ===");
        System.out.println("价格: " + java.util.Arrays.toString(prices2));
        int result2 = maxProfit(prices2);
        System.out.println("结果: " + result2 + "，预期: 0");
        System.out.println();
        
        // 测试用例3：边界情况 - 只有两个元素
        int[] prices3 = {1, 2};
        System.out.println("=== 测试用例3 ===");
        System.out.println("价格: " + java.util.Arrays.toString(prices3));
        int result3 = maxProfit(prices3);
        System.out.println("结果: " + result3 + "，预期: 1");
        System.out.println();
        
        // 测试用例4：价格波动较大
        int[] prices4 = {2, 4, 1, 7, 3, 9, 1};
        System.out.println("=== 测试用例4 ===");
        System.out.println("价格: " + java.util.Arrays.toString(prices4));
        int result4 = maxProfit(prices4);
        System.out.println("结果: " + result4 + "，预期: 8");
        System.out.println();
        
        // 测试用例5：空数组
        int[] prices5 = {};
        System.out.println("=== 测试用例5 ===");
        System.out.println("价格: " + java.util.Arrays.toString(prices5));
        int result5 = maxProfit(prices5);
        System.out.println("结果: " + result5 + "，预期: 0");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        int[] largePrices = new int[10000];
        for (int i = 0; i < largePrices.length; i++) {
            largePrices[i] = (int) (Math.random() * 1000);
        }
        
        long startTime = System.currentTimeMillis();
        int largeResult = maxProfit(largePrices);
        long endTime = System.currentTimeMillis();
        System.out.println("贪心算法 - 结果: " + largeResult + "，耗时: " + (endTime - startTime) + "ms");
        
        startTime = System.currentTimeMillis();
        int largeResultDP = maxProfitDP(largePrices);
        endTime = System.currentTimeMillis();
        System.out.println("动态规划 - 结果: " + largeResultDP + "，耗时: " + (endTime - startTime) + "ms");
        
        // 注意：暴力解法在大规模数据下会很慢，这里只测试小规模
        if (largePrices.length <= 1000) {
            startTime = System.currentTimeMillis();
            int largeResultBrute = maxProfitBruteForce(largePrices);
            endTime = System.currentTimeMillis();
            System.out.println("暴力解法 - 结果: " + largeResultBrute + "，耗时: " + (endTime - startTime) + "ms");
        } else {
            System.out.println("暴力解法跳过（数据规模太大）");
        }
        
        // 调试测试
        System.out.println("\n=== 调试测试 ===");
        int[] debugPrices = {7, 1, 5, 3, 6, 4};
        System.out.println("使用优化版本进行调试:");
        maxProfitOptimized(debugPrices);
    }
}

/*
算法深度分析：

1. 贪心算法正确性证明：
   - 核心思想：在遍历过程中维护最小价格和最大利润
   - 正确性：对于每个价格，我们都能计算出如果在该价格卖出能获得的最大利润（使用之前的最小价格）
   - 时间复杂度：O(n)，空间复杂度：O(1)

2. 动态规划解法：
   - 状态定义：dp0表示不持有股票的最大利润，dp1表示持有股票的最大利润
   - 状态转移：
        dp0 = max(昨天不持有, 昨天持有今天卖出)
        dp1 = max(昨天持有, 今天买入)
   - 同样达到O(n)时间复杂度和O(1)空间复杂度

3. 暴力解法：
   - 双重循环检查所有可能的买入卖出组合
   - 时间复杂度：O(n^2)，空间复杂度：O(1)
   - 只适用于小规模数据

工程化深度考量：

1. 算法选择依据：
   - 生产环境：优先使用贪心算法（代码简洁，效率高）
   - 面试场景：可以展示多种解法，体现算法功底
   - 教学场景：通过对比理解不同解法的优劣

2. 边界条件处理：
   - 空数组和单元素数组：直接返回0
   - 价格递减情况：返回0（无法获利）
   - 极端数据：算法能够正确处理

3. 性能优化策略：
   - 避免重复计算：缓存中间结果
   - 减少函数调用：内联简单操作
   - 使用基本类型：避免自动装箱

4. 异常场景考虑：
   - 负数价格：题目保证非负，但工程中需要验证
   - 超大数组：算法时间复杂度为O(n)，可以处理
   - 价格波动剧烈：算法能够正确计算最大利润

5. 调试与测试：
   - 单元测试：覆盖各种边界情况
   - 性能测试：验证大规模数据表现
   - 对比测试：验证不同解法结果一致性

6. 跨语言实现差异：
   - Java：使用Integer.MAX_VALUE初始化最小价格
   - Python：使用float('inf')初始化最小价格
   - C++：使用INT_MAX初始化最小价格

7. 工程实践建议：
   - 生产环境添加输入验证
   - 添加监控和日志记录
   - 考虑数值溢出问题（虽然题目保证价格合理）

8. 算法扩展性：
   - 可以扩展支持多次交易（股票买卖II）
   - 可以扩展支持交易费用（股票买卖含手续费）
   - 可以扩展支持冷却期（股票买卖含冷冻期）

9. 与机器学习联系：
   - 该问题可以看作时间序列预测问题
   - 贪心策略在在线学习中有应用
   - 可以用于训练交易策略模型

10. 面试技巧：
    - 能够解释贪心策略的正确性
    - 能够分析时间/空间复杂度
    - 能够处理各种边界情况
    - 能够进行代码优化和调试

11. 实际应用价值：
    - 金融领域的交易策略分析
    - 投资组合优化
    - 风险管理
*/