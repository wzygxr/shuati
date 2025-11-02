// 买卖股票的最佳时机 (Best Time to Buy and Sell Stock)
// 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
// 你只能选择某一天买入这只股票，并选择在未来的某一个不同的日子卖出该股票。
// 设计一个算法来计算你所能获取的最大利润。
// 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0。
// 测试链接 : https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/

package class066;

/**
 * 买卖股票的最佳时机 - 一次交易的最大利润问题
 * 时间复杂度分析：
 * - 暴力解法：O(n^2) - 枚举所有买卖组合
 * - 动态规划：O(n) - 遍历数组一次
 * - 空间优化：O(1) - 只保存必要的前一个状态
 * 
 * 空间复杂度分析：
 * - 暴力解法：O(1) - 只保存当前最大值
 * - 动态规划：O(n) - dp数组存储所有状态
 * - 空间优化：O(1) - 工程首选
 * 
 * 工程化考量：
 * 1. 边界处理：空数组、单元素数组、递减数组
 * 2. 性能优化：空间优化版本应对大规模数据
 * 3. 代码清晰：明确的变量命名和状态转移逻辑
 * 4. 异常处理：处理无效输入和边界情况
 */
public class Code24_BestTimeToBuyAndSellStock {

    // 方法1：动态规划（记录历史最低价）
    // 时间复杂度：O(n) - 遍历数组一次
    // 空间复杂度：O(1) - 只使用常数空间
    // 核心思路：记录历史最低价，计算当前价格与历史最低价的差值
    public static int maxProfit1(int[] prices) {
        if (prices == null || prices.length <= 1) return 0;
        
        int n = prices.length;
        int minPrice = prices[0];  // 历史最低价
        int maxProfit = 0;          // 最大利润
        
        for (int i = 1; i < n; i++) {
            // 更新历史最低价
            minPrice = Math.min(minPrice, prices[i]);
            // 计算当前利润并更新最大值
            maxProfit = Math.max(maxProfit, prices[i] - minPrice);
        }
        
        return maxProfit;
    }

    // 方法2：Kadane算法变种（最大子数组和）
    // 时间复杂度：O(n) - 遍历数组一次
    // 空间复杂度：O(1) - 只使用常数空间
    // 核心思路：将价格差转化为最大子数组和问题
    public static int maxProfit2(int[] prices) {
        if (prices == null || prices.length <= 1) return 0;
        
        int n = prices.length;
        int maxCur = 0;    // 当前最大利润
        int maxSoFar = 0;   // 全局最大利润
        
        for (int i = 1; i < n; i++) {
            // 计算相邻两天的价格差
            int diff = prices[i] - prices[i - 1];
            // 使用Kadane算法思想
            maxCur = Math.max(0, maxCur + diff);
            maxSoFar = Math.max(maxSoFar, maxCur);
        }
        
        return maxSoFar;
    }

    // 方法3：动态规划（状态机）
    // 时间复杂度：O(n) - 遍历数组一次
    // 空间复杂度：O(n) - 使用dp数组
    // 核心思路：明确两个状态：持有股票和不持有股票
    public static int maxProfit3(int[] prices) {
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
            // 第i天持有股票：昨天就持有 或 今天买入（只能买入一次）
            dp[i][1] = Math.max(dp[i - 1][1], -prices[i]);
        }
        
        return dp[n - 1][0];  // 最后一天不持有股票才能获得最大利润
    }

    // 方法4：空间优化的动态规划（状态机）
    // 时间复杂度：O(n) - 与方法3相同
    // 空间复杂度：O(1) - 只使用常数空间
    // 优化：使用变量代替数组，减少空间使用
    public static int maxProfit4(int[] prices) {
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
            dp1 = Math.max(prevDp1, -prices[i]);
        }
        
        return dp0;
    }

    // 方法5：暴力解法（用于对比）
    // 时间复杂度：O(n^2) - 枚举所有买卖组合
    // 空间复杂度：O(1) - 只保存当前最大值
    // 问题：效率低，仅用于教学目的
    public static int maxProfit5(int[] prices) {
        if (prices == null || prices.length <= 1) return 0;
        
        int n = prices.length;
        int maxProfit = 0;
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int profit = prices[j] - prices[i];
                maxProfit = Math.max(maxProfit, profit);
            }
        }
        
        return maxProfit;
    }

    // 方法6：分治解法（用于对比）
    // 时间复杂度：O(n log n) - 分治递归
    // 空间复杂度：O(log n) - 递归栈深度
    // 核心思路：将数组分成左右两部分，最大利润可能在左、右或跨越中间
    public static int maxProfit6(int[] prices) {
        if (prices == null || prices.length <= 1) return 0;
        return divideAndConquer(prices, 0, prices.length - 1);
    }
    
    private static int divideAndConquer(int[] prices, int left, int right) {
        if (left >= right) return 0;
        
        int mid = left + (right - left) / 2;
        
        // 左半部分的最大利润
        int leftProfit = divideAndConquer(prices, left, mid);
        // 右半部分的最大利润
        int rightProfit = divideAndConquer(prices, mid + 1, right);
        // 跨越中间的最大利润（在左半部分买入，右半部分卖出）
        int crossProfit = maxCrossingProfit(prices, left, mid, right);
        
        return Math.max(leftProfit, Math.max(rightProfit, crossProfit));
    }
    
    private static int maxCrossingProfit(int[] prices, int left, int mid, int right) {
        // 在左半部分找到最低价
        int leftMin = Integer.MAX_VALUE;
        for (int i = left; i <= mid; i++) {
            leftMin = Math.min(leftMin, prices[i]);
        }
        
        // 在右半部分找到最高价
        int rightMax = Integer.MIN_VALUE;
        for (int i = mid + 1; i <= right; i++) {
            rightMax = Math.max(rightMax, prices[i]);
        }
        
        return Math.max(0, rightMax - leftMin);
    }

    // 全面的测试用例
    public static void main(String[] args) {
        System.out.println("=== 买卖股票的最佳时机测试 ===");
        
        // 边界测试
        testCase(new int[]{}, 0, "空数组");
        testCase(new int[]{5}, 0, "单元素数组");
        testCase(new int[]{7, 1, 5, 3, 6, 4}, 5, "示例1");
        testCase(new int[]{7, 6, 4, 3, 1}, 0, "递减数组");
        
        // LeetCode示例测试
        testCase(new int[]{7, 1, 5, 3, 6, 4}, 5, "LeetCode示例1");
        testCase(new int[]{7, 6, 4, 3, 1}, 0, "LeetCode示例2");
        testCase(new int[]{1, 2}, 1, "两天递增");
        testCase(new int[]{2, 1}, 0, "两天递减");
        
        // 常规测试
        testCase(new int[]{1, 2, 3, 4, 5}, 4, "连续递增");
        testCase(new int[]{5, 4, 3, 2, 1}, 0, "连续递减");
        testCase(new int[]{2, 4, 1}, 2, "先增后减");
        testCase(new int[]{3, 2, 6, 5, 0, 3}, 4, "复杂情况");
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        int[] largePrices = new int[10000];
        for (int i = 0; i < largePrices.length; i++) {
            largePrices[i] = (int)(Math.random() * 1000) + 1;  // 1-1000的随机价格
        }
        
        long start = System.currentTimeMillis();
        int result1 = maxProfit1(largePrices);
        long end = System.currentTimeMillis();
        System.out.println("方法1: " + result1 + ", 耗时: " + (end - start) + "ms");
        
        start = System.currentTimeMillis();
        int result2 = maxProfit2(largePrices);
        end = System.currentTimeMillis();
        System.out.println("方法2: " + result2 + ", 耗时: " + (end - start) + "ms");
        
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
     * 1. 问题本质：一次交易的最大利润问题
     *    - 关键洞察：在最低点买入，在最高点卖出（但卖出必须在买入之后）
     *    - 核心思路：记录历史最低价，计算当前价格与历史最低价的差值
     * 
     * 2. 时间复杂度对比：
     *    - 暴力解法：O(n^2) - 不可接受
     *    - 分治解法：O(n log n) - 可接受但非最优
     *    - 动态规划：O(n) - 推荐
     *    - 空间优化：O(n) - 工程首选
     * 
     * 3. 空间复杂度对比：
     *    - 暴力解法：O(1) - 但效率低
     *    - 分治解法：O(log n) - 递归栈
     *    - 动态规划：O(n) - 数组存储
     *    - 空间优化：O(1) - 最优
     * 
     * 4. 特殊情况处理：
     *    - 价格递减：最大利润为0
     *    - 单元素数组：无法交易，利润为0
     *    - 空数组：利润为0
     *    - 价格全部相同：利润为0
     * 
     * 5. 工程选择依据：
     *    - 一般情况：方法1（记录历史最低价）
     *    - 需要状态机思路：方法4（空间优化状态机）
     *    - 需要Kadane算法：方法2（最大子数组和）
     * 
     * 6. 调试技巧：
     *    - 跟踪历史最低价的变化
     *    - 验证价格差的计算
     *    - 检查边界情况处理
     * 
     * 7. 关联题目：
     *    - 买卖股票的最佳时机II（无限次交易）
     *    - 买卖股票的最佳时机III（最多两次交易）
     *    - 买卖股票的最佳时机IV（最多k次交易）
     *    - 含冷冻期的买卖股票
     *    - 含手续费的买卖股票
     * 
     * 8. 优化思路：
     *    - 提前终止：当价格递减时直接返回0
     *    - 空间优化：使用变量代替数组
     *    - 边界处理：单独处理特殊情况
     */
}