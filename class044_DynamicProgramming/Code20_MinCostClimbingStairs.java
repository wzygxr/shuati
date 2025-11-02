// 使用最小花费爬楼梯 (Min Cost Climbing Stairs)
// 给你一个整数数组 cost ，其中 cost[i] 是从楼梯第 i 个台阶向上爬需要支付的费用。
// 一旦你支付此费用，即可选择向上爬一个或者两个台阶。
// 你可以选择从下标为 0 或下标为 1 的台阶开始爬楼梯。
// 请你计算并返回达到楼梯顶部的最低花费。
// 测试链接 : https://leetcode.cn/problems/min-cost-climbing-stairs/

package class066;

/**
 * 使用最小花费爬楼梯 - 动态规划经典问题
 * 时间复杂度分析：
 * - 暴力递归：O(2^n) 指数级，存在大量重复计算
 * - 记忆化搜索：O(n) 每个状态只计算一次
 * - 动态规划：O(n) 线性时间复杂度
 * - 空间优化：O(1) 只保存必要的前两个状态
 * 
 * 空间复杂度分析：
 * - 暴力递归：O(n) 递归调用栈深度
 * - 记忆化搜索：O(n) 递归栈 + 记忆化数组
 * - 动态规划：O(n) dp数组存储所有状态
 * - 空间优化：O(1) 工程首选
 * 
 * 工程化考量：
 * 1. 异常处理：空数组、单元素数组等边界情况
 * 2. 边界测试：cost长度为0,1,2的情况
 * 3. 性能优化：空间优化版本应对大规模数据
 * 4. 可读性：清晰的变量命名和状态转移逻辑
 */
public class Code20_MinCostClimbingStairs {

    // 方法1：暴力递归解法
    // 时间复杂度：O(2^n) - 指数级时间复杂度，效率极低
    // 空间复杂度：O(n) - 递归调用栈的深度
    // 问题：存在大量重复计算，n较大时栈溢出
    public static int minCostClimbingStairs1(int[] cost) {
        if (cost == null || cost.length == 0) return 0;
        // 可以从第0阶或第1阶开始，取最小值
        return Math.min(dfs1(cost, cost.length - 1), dfs1(cost, cost.length - 2));
    }
    
    private static int dfs1(int[] cost, int i) {
        if (i < 0) return 0;
        if (i == 0 || i == 1) return cost[i];
        
        return cost[i] + Math.min(dfs1(cost, i - 1), dfs1(cost, i - 2));
    }

    // 方法2：记忆化搜索（自顶向下动态规划）
    // 时间复杂度：O(n) - 每个状态只计算一次
    // 空间复杂度：O(n) - memo数组和递归调用栈
    // 优化：通过缓存避免重复计算
    public static int minCostClimbingStairs2(int[] cost) {
        if (cost == null || cost.length == 0) return 0;
        if (cost.length == 1) return 0;
        
        int n = cost.length;
        int[] memo = new int[n];
        java.util.Arrays.fill(memo, -1);
        
        return Math.min(dfs2(cost, n - 1, memo), dfs2(cost, n - 2, memo));
    }
    
    private static int dfs2(int[] cost, int i, int[] memo) {
        if (i < 0) return 0;
        if (i == 0 || i == 1) return cost[i];
        if (memo[i] != -1) return memo[i];
        
        memo[i] = cost[i] + Math.min(dfs2(cost, i - 1, memo), dfs2(cost, i - 2, memo));
        return memo[i];
    }

    // 方法3：动态规划（自底向上）
    // 时间复杂度：O(n) - 从底向上计算每个状态
    // 空间复杂度：O(n) - dp数组存储所有状态
    // 优化：避免了递归调用的开销
    public static int minCostClimbingStairs3(int[] cost) {
        if (cost == null || cost.length == 0) return 0;
        if (cost.length == 1) return 0;
        
        int n = cost.length;
        int[] dp = new int[n];
        
        // 初始化基础情况
        dp[0] = cost[0];
        dp[1] = cost[1];
        
        // 状态转移：到达第i阶的最小花费 = cost[i] + min(到达i-1阶的最小花费, 到达i-2阶的最小花费)
        for (int i = 2; i < n; i++) {
            dp[i] = cost[i] + Math.min(dp[i - 1], dp[i - 2]);
        }
        
        // 可以从最后两阶直接到达楼顶，取最小值
        return Math.min(dp[n - 1], dp[n - 2]);
    }

    // 方法4：空间优化的动态规划
    // 时间复杂度：O(n) - 仍然需要计算所有状态
    // 空间复杂度：O(1) - 只保存必要的前两个状态值
    // 优化：大幅减少空间使用，工程首选
    public static int minCostClimbingStairs4(int[] cost) {
        if (cost == null || cost.length == 0) return 0;
        if (cost.length == 1) return 0;
        
        int n = cost.length;
        int prev2 = cost[0];  // 到达第i-2阶的最小花费
        int prev1 = cost[1];  // 到达第i-1阶的最小花费
        
        for (int i = 2; i < n; i++) {
            int current = cost[i] + Math.min(prev1, prev2);
            prev2 = prev1;
            prev1 = current;
        }
        
        return Math.min(prev1, prev2);
    }

    // 方法5：更直观的动态规划（从楼顶向下看）
    // 时间复杂度：O(n) - 遍历数组一次
    // 空间复杂度：O(n) - dp数组
    // 核心思路：dp[i]表示到达第i阶（包括楼顶）的最小花费
    public static int minCostClimbingStairs5(int[] cost) {
        if (cost == null || cost.length == 0) return 0;
        
        int n = cost.length;
        int[] dp = new int[n + 1];  // dp[n]表示到达楼顶的最小花费
        
        // 初始化：从第0阶或第1阶开始不需要花费（但需要支付该阶的费用）
        dp[0] = 0;
        dp[1] = 0;
        
        for (int i = 2; i <= n; i++) {
            // 到达第i阶的最小花费 = min(从i-1阶上来, 从i-2阶上来) + 相应的费用
            dp[i] = Math.min(dp[i - 1] + cost[i - 1], dp[i - 2] + cost[i - 2]);
        }
        
        return dp[n];
    }

    // 全面的测试用例
    public static void main(String[] args) {
        System.out.println("=== 使用最小花费爬楼梯测试 ===");
        
        // 边界测试
        testCase(new int[]{}, 0, "空数组");
        testCase(new int[]{10}, 0, "单元素数组");
        testCase(new int[]{10, 15}, 10, "双元素数组");
        
        // LeetCode示例测试
        testCase(new int[]{10, 15, 20}, 15, "示例1");
        testCase(new int[]{1, 100, 1, 1, 1, 100, 1, 1, 100, 1}, 6, "示例2");
        
        // 常规测试
        testCase(new int[]{0, 0, 0, 0}, 0, "全零费用");
        testCase(new int[]{1, 2, 3, 4, 5}, 6, "递增费用");
        testCase(new int[]{5, 4, 3, 2, 1}, 6, "递减费用");
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        int[] largeCost = new int[1000];
        java.util.Arrays.fill(largeCost, 1);
        
        long start = System.currentTimeMillis();
        int result3 = minCostClimbingStairs3(largeCost);
        long end = System.currentTimeMillis();
        System.out.println("动态规划方法: " + result3 + ", 耗时: " + (end - start) + "ms");
        
        start = System.currentTimeMillis();
        int result4 = minCostClimbingStairs4(largeCost);
        end = System.currentTimeMillis();
        System.out.println("空间优化方法: " + result4 + ", 耗时: " + (end - start) + "ms");
        
        start = System.currentTimeMillis();
        int result5 = minCostClimbingStairs5(largeCost);
        end = System.currentTimeMillis();
        System.out.println("楼顶视角方法: " + result5 + ", 耗时: " + (end - start) + "ms");
    }
    
    private static void testCase(int[] cost, int expected, String description) {
        int result1 = minCostClimbingStairs1(cost);
        int result2 = minCostClimbingStairs2(cost);
        int result3 = minCostClimbingStairs3(cost);
        int result4 = minCostClimbingStairs4(cost);
        int result5 = minCostClimbingStairs5(cost);
        
        boolean allCorrect = (result1 == expected && result2 == expected && 
                            result3 == expected && result4 == expected && result5 == expected);
        
        System.out.println(description + ": " + (allCorrect ? "✓" : "✗"));
        if (!allCorrect) {
            System.out.println("  方法1: " + result1 + " | 方法2: " + result2 + 
                             " | 方法3: " + result3 + " | 方法4: " + result4 + 
                             " | 方法5: " + result5 + " | 预期: " + expected);
        }
    }
    
    /**
     * 算法总结与工程化思考：
     * 
     * 1. 问题本质：斐波那契数列的变种，但每个台阶有相应的费用
     *    f(i) = cost[i] + min(f(i-1), f(i-2))
     * 
     * 2. 关键洞察：
     *    - 可以从第0阶或第1阶开始爬楼梯
     *    - 楼顶在数组长度之外（索引n）
     *    - 最终结果是最后两阶的最小值
     * 
     * 3. 时间复杂度对比：
     *    - 暴力递归：O(2^n) - 不可接受
     *    - 记忆化搜索：O(n) - 可接受
     *    - 动态规划：O(n) - 推荐
     *    - 空间优化：O(n) - 工程首选
     * 
     * 4. 空间复杂度对比：
     *    - 暴力递归：O(n) - 栈深度
     *    - 记忆化搜索：O(n) - 递归栈+缓存
     *    - 动态规划：O(n) - 数组存储
     *    - 空间优化：O(1) - 最优
     * 
     * 5. 工程选择依据：
     *    - 面试笔试：方法4（空间优化）
     *    - 大规模数据：方法4或方法5
     *    - 代码清晰：方法5（楼顶视角）
     * 
     * 6. 调试技巧：
     *    - 打印dp数组验证状态转移
     *    - 边界测试确保正确性
     *    - 性能测试选择最优算法
     * 
     * 7. 关联题目：
     *    - 爬楼梯问题（无费用版本）
     *    - 打家劫舍问题（相邻元素不能同时选择）
     *    - 斐波那契数列
     */
}