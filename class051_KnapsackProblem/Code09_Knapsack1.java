package class073;

// AtCoder Educational DP Contest D - Knapsack 1
// 题目描述：有N个物品，每个物品有重量w_i和价值v_i。
// 背包容量为W，求能装入背包的物品的最大价值总和。
// 链接：https://atcoder.jp/contests/dp/tasks/dp_d
// 
// 解题思路：
// 这是经典的01背包问题。
// 1. dp[i][j] 表示前i个物品，背包容量为j时能获得的最大价值
// 2. 状态转移方程：
//    dp[i][j] = max(dp[i-1][j], dp[i-1][j-w[i]] + v[i])  (当j >= w[i]时)
//    dp[i][j] = dp[i-1][j]  (当j < w[i]时)
// 3. 可以使用滚动数组优化空间复杂度
//
// 时间复杂度：O(N * W)
// 空间复杂度：O(W)

public class Code09_Knapsack1 {
    
    /**
     * 计算01背包问题的最大价值
     * 
     * 解题思路：
     * 这是经典的01背包问题。
     * 1. dp[i][j] 表示前i个物品，背包容量为j时能获得的最大价值
     * 2. 状态转移方程：
     *    dp[i][j] = max(dp[i-1][j], dp[i-1][j-w[i]] + v[i])  (当j >= w[i]时)
     *    dp[i][j] = dp[i-1][j]  (当j < w[i]时)
     * 3. 可以使用滚动数组优化空间复杂度
     * 
     * @param N 物品数量
     * @param W 背包容量
     * @param weights 物品重量数组
     * @param values 物品价值数组
     * @return 能装入背包的物品的最大价值总和
     */
    public static long knapsack(int N, int W, int[] weights, int[] values) {
        // dp[j] 表示背包容量为j时能获得的最大价值
        // 这里使用了空间优化的一维DP数组，相当于dp[i][j]压缩为dp[j]
        long[] dp = new long[W + 1];
        
        // 遍历每个物品（01背包的物品遍历）
        for (int i = 0; i < N; i++) {
            // 获取当前物品的重量和价值
            int weight = weights[i];
            int value = values[i];
            
            // 01背包需要倒序遍历，确保每个物品只使用一次
            // j表示当前背包的容量
            for (int j = W; j >= weight; j--) {
                // 状态转移方程：
                // dp[j] = max(不选择当前物品, 选择当前物品)
                // 不选择当前物品：dp[j]（保持原值）
                // 选择当前物品：dp[j - weight] + value（前一个状态+当前物品价值）
                dp[j] = Math.max(dp[j], dp[j - weight] + value);
            }
        }
        
        // 返回背包容量为W时能获得的最大价值
        return dp[W];
    }
    
    /*
     * 示例:
     * 输入: N = 3, W = 8
     * weights = [3, 4, 5]
     * values = [30, 50, 60]
     * 输出: 90
     * 解释: 选择物品1和物品3，总重量3+5=8，总价值30+60=90
     *
     * 输入: N = 5, W = 5
     * weights = [1, 1, 1, 1, 1]
     * values = [1000000000, 1000000000, 1000000000, 1000000000, 1000000000]
     * 输出: 5000000000
     * 解释: 选择所有物品，总重量5，总价值5000000000
     *
     * 时间复杂度: O(N * W)
     *   - 外层循环遍历所有物品：O(N)
     *   - 内层循环遍历背包容量：O(W)
     * 空间复杂度: O(W)
     *   - 一维DP数组的空间消耗
     */
}