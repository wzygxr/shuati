// 买卖股票的最佳时机II (Best Time to Buy and Sell Stock II)
// 给你一个整数数组 prices ，其中 prices[i] 表示某支股票第 i 天的价格。
// 在每一天，你可以决定是否购买和/或出售股票。你在任何时候最多只能持有一股股票。你也可以先购买，然后在同一天出售。
// 返回你能获得的最大利润。
// 测试链接 : https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/

package class066;

/**
 * 买卖股票的最佳时机II - 无限次交易的最大利润问题
 * 时间复杂度分析：
 * - 贪心算法：O(n) - 遍历数组一次
 * - 动态规划：O(n) - 遍历数组一次
 * - 空间优化：O(1) - 只保存必要的前一个状态
 * 
 * 空间复杂度分析：
 * - 贪心算法：O(1) - 只使用常数空间
 * - 动态规划：O(n) - dp数组存储所有状态
 * - 空间优化：O(1) - 工程首选
 * 
 * 工程化考量：
 * 1. 多种解法对比：贪心 vs 动态规划
 * 2. 边界处理：空数组、单元素数组、价格不变情况
 * 3. 性能优化：选择最优算法
 * 4. 代码清晰：明确的算法思路和注释
 */
public class Code25_BestTimeToBuyAndSellStockII {

    // 方法1：贪心算法（收集所有上涨）
    // 时间复杂度：O(n) - 遍历数组一次
    // 空间复杂度：O(1) - 只使用常数空间
    // 核心思路：只要后一天比前一天价格高，就进行交易
    public static int maxProfit1(int[] prices) {
        if (prices == null || prices.length <= 1) return 0;
        
        int n = prices.length;
        int maxProfit = 0;
        
        for (int i = 1; i < n; i++) {
            if (prices[i] > prices[i - 1]) {
                maxProfit += prices[i] - prices[i - 1];
            }
        }
        
        return maxProfit;
    }

    // 方法2：动态规划（状态机）
    // 时间复杂度：O(n) - 遍历数组一次
    // 空间复杂度：O(n) - 使用dp数组
    // 核心思路：明确两个状态：持有股票和不持有股票
    public static int maxProfit2(int[] prices) {
        if (prices == null || prices.length <= 1) return 0;
        
        int n = prices.length;
        int[][] dp = new int[n][2];
        // dp[i][0]: 第i天不持有股票的最大利润
        // dp[i][1]: 第i天持有股票的最大利润
        
        // 初始化
        dp[0][0] = 0;           // 第0天不持有股票，利润为0
        dp[0][1] = -prices[0]; // 第0天持有股票，利润为负的买入价格
        
        for (int i = 1; i < n; i++) {
            // 第i天不持有股票：昨天就不持有 或 昨天持有今天卖出
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1] + prices[i]);
            // 第i天持有股票：昨天就持有 或 昨天不持有今天买入（可以多次交易）
            dp[i][1] = Math.max(dp[i - 1][1], dp[i - 1][0] - prices[i]);
        }
        
        return dp[n - 1][0];  // 最后一天不持有股票才能获得最大利润
    }

    // 方法3：空间优化的动态规划（状态机）
    // 时间复杂度：O(n) - 与方法2相同
    // 空间复杂度：O(1) - 只使用常数空间
    // 优化：使用变量代替数组，减少空间使用
    public static int maxProfit3(int[] prices) {
        if (prices == null || prices.length <= 1) return 0;
        
        int n = prices.length;
        int dp0 = 0;           // 不持有股票的最大利润
        int dp1 = -prices[0];  // 持有股票的最大利润
        
        for (int i = 1; i < n; i++) {
            // 保存前一天的状态，避免覆盖
            int prevDp0 = dp0;
            int prevDp1 = dp1;
            
            // 更新状态
            dp0 = Math.max(prevDp0, prevDp1 + prices[i]);
            dp1 = Math.max(prevDp1, prevDp0 - prices[i]);
        }
        
        return dp0;
    }

    // 方法4：峰谷法（直观理解）
    // 时间复杂度：O(n) - 遍历数组一次
    // 空间复杂度：O(1) - 只使用常数空间
    // 核心思路：找到所有的波谷和波峰，计算差值之和
    public static int maxProfit4(int[] prices) {
        if (prices == null || prices.length <= 1) return 0;
        
        int n = prices.length;
        int maxProfit = 0;
        int valley = prices[0];  // 波谷
        int peak = prices[0];    // 波峰
        int i = 0;
        
        while (i < n - 1) {
            // 找到波谷
            while (i < n - 1 && prices[i] >= prices[i + 1]) {
                i++;
            }
            valley = prices[i];
            
            // 找到波峰
            while (i < n - 1 && prices[i] <= prices[i + 1]) {
                i++;
            }
            peak = prices[i];
            
            maxProfit += peak - valley;
        }
        
        return maxProfit;
    }

    // 方法5：暴力递归（用于对比）
    // 时间复杂度：O(2^n) - 指数级，效率极低
    // 空间复杂度：O(n) - 递归调用栈深度
    // 问题：存在大量重复计算，仅用于教学目的
    public static int maxProfit5(int[] prices) {
        if (prices == null || prices.length <= 1) return 0;
        return dfs(prices, 0, false);
    }
    
    private static int dfs(int[] prices, int index, boolean hasStock) {
        if (index >= prices.length) return 0;
        
        // 如果今天不操作，直接考虑下一天
        int skip = dfs(prices, index + 1, hasStock);
        
        if (hasStock) {
            // 如果持有股票，可以选择卖出
            int sell = prices[index] + dfs(prices, index + 1, false);
            return Math.max(skip, sell);
        } else {
            // 如果没有持有股票，可以选择买入
            int buy = -prices[index] + dfs(prices, index + 1, true);
            return Math.max(skip, buy);
        }
    }

    // 全面的测试用例
    public static void main(String[] args) {
        System.out.println("=== 买卖股票的最佳时机II测试 ===");
        
        // 边界测试
        testCase(new int[]{}, 0, "空数组");
        testCase(new int[]{5}, 0, "单元素数组");
        testCase(new int[]{1, 2, 3, 4, 5}, 4, "连续上涨");
        testCase(new int[]{5, 4, 3, 2, 1}, 0, "连续下跌");
        
        // LeetCode示例测试
        testCase(new int[]{7, 1, 5, 3, 6, 4}, 7, "示例1");
        testCase(new int[]{1, 2, 3, 4, 5}, 4, "示例2");
        testCase(new int[]{7, 6, 4, 3, 1}, 0, "示例3");
        
        // 常规测试
        testCase(new int[]{1, 3, 2, 4}, 4, "波动上涨");
        testCase(new int[]{2, 1, 4}, 3, "先跌后涨");
        testCase(new int[]{3, 3, 5, 0, 0, 3, 1, 4}, 8, "复杂波动");
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        int[] largePrices = new int[10000];
        for (int i = 0; i < largePrices.length; i++) {
            largePrices[i] = (int)(Math.random() * 1000) + 1;  // 1-1000的随机价格
        }
        
        long start = System.currentTimeMillis();
        int result1 = maxProfit1(largePrices);
        long end = System.currentTimeMillis();
        System.out.println("贪心算法: " + result1 + ", 耗时: " + (end - start) + "ms");
        
        start = System.currentTimeMillis();
        int result3 = maxProfit3(largePrices);
        end = System.currentTimeMillis();
        System.out.println("空间优化DP: " + result3 + ", 耗时: " + (end - start) + "ms");
        
        // 暴力方法太慢，不测试
        System.out.println("暴力方法在n=10000时太慢，跳过测试");
    }
    
    private static void testCase(int[] prices, int expected, String description) {
        int result1 = maxProfit1(prices);
        int result2 = maxProfit2(prices);
        int result3 = maxProfit3(prices);
        int result4 = maxProfit4(prices);
        
        boolean allCorrect = (result1 == expected && result2 == expected && 
                            result3 == expected && result4 == expected);
        
        System.out.println(description + ": " + (allCorrect ? "✓" : "✗"));
        if (!allCorrect) {
            System.out.println("  方法1: " + result1 + " | 方法2: " + result2 + 
                             " | 方法3: " + result3 + " | 方法4: " + result4 + 
                             " | 预期: " + expected);
        }
    }
    
    /**
     * 算法总结与工程化思考：
     * 
     * 1. 问题本质：无限次交易的最大利润问题
     *    - 关键洞察：可以多次买卖，只要后一天比前一天价格高就交易
     *    - 核心思路：收集所有的上涨区间
     * 
     * 2. 时间复杂度对比：
     *    - 暴力递归：O(2^n) - 不可接受
     *    - 贪心算法：O(n) - 推荐
     *    - 动态规划：O(n) - 可接受
     *    - 空间优化：O(n) - 工程首选
     * 
     * 3. 空间复杂度对比：
     *    - 暴力递归：O(n) - 栈深度
     *    - 贪心算法：O(1) - 最优
     *    - 动态规划：O(n) - 数组存储
     *    - 空间优化：O(1) - 最优
     * 
     * 4. 特殊情况处理：
     *    - 价格不变：利润为0
     *    - 连续上涨：利润为最后-最初
     *    - 连续下跌：利润为0
     *    - 波动市场：收集所有上涨
     * 
     * 5. 工程选择依据：
     *    - 一般情况：方法1（贪心算法）
     *    - 需要状态机思路：方法3（空间优化DP）
     *    - 直观理解：方法4（峰谷法）
     * 
     * 6. 调试技巧：
     *    - 验证上涨区间的识别
     *    - 检查状态转移的正确性
     *    - 边界测试确保正确性
     * 
     * 7. 关联题目：
     *    - 买卖股票的最佳时机（一次交易）
     *    - 买卖股票的最佳时机III（最多两次交易）
     *    - 买卖股票的最佳时机IV（最多k次交易）
     *    - 含冷冻期的买卖股票
     *    - 含手续费的买卖股票
     * 
     * 8. 优化思路：
     *    - 贪心算法是最优解
     *    - 动态规划提供状态机思路
     *    - 峰谷法帮助直观理解
     */
}