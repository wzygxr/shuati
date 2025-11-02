package class073;

// LeetCode 1155. 掷骰子的N种方法
// 题目描述：这里有 n 个一样的骰子，每个骰子上都有 k 个面，分别标有 1 到 k 的数字。
// 给定三个整数 n, k 和 target，请你计算并返回投掷骰子的所有可能得到的结果等于 target 的方案数。
// 答案可能很大，所以需要返回模 10^9 + 7 的结果。
// 链接：https://leetcode.cn/problems/number-of-dice-rolls-with-target-sum/
// 
// 解题思路：
// 这是一个典型的分组背包问题，每个骰子可以看作一组，每组有k个选项（1到k的点数）
// 我们需要从每组中选择一个选项，使得它们的总和等于target，求总共有多少种选法。
// 
// 状态定义：dp[i][j] 表示使用i个骰子能得到点数和为j的方案数
// 状态转移方程：dp[i][j] = sum(dp[i-1][j-m])，其中m从1到k且j-m >= i-1（因为每个骰子至少为1，i-1个骰子至少为i-1）
// 初始状态：dp[0][0] = 1（使用0个骰子得到点数和为0只有一种方式）
// 
// 时间复杂度：O(n * k * target)
// 空间复杂度：O(n * target)，可以优化为O(target)

public class Code34_NumberOfDiceRollsWithTargetSum {

    // 主方法，用于测试
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 1, k1 = 6, target1 = 3;
        System.out.println("测试用例1结果: " + numRollsToTarget(n1, k1, target1)); // 预期输出: 1
        
        // 测试用例2
        int n2 = 2, k2 = 6, target2 = 7;
        System.out.println("测试用例2结果: " + numRollsToTarget(n2, k2, target2)); // 预期输出: 6
        
        // 测试用例3
        int n3 = 30, k3 = 30, target3 = 500;
        System.out.println("测试用例3结果: " + numRollsToTarget(n3, k3, target3)); // 预期输出: 222616187
    }
    
    // 模数
    private static final int MOD = 1000000007;
    
    /**
     * 计算投掷骰子得到目标点数和的方案数
     * @param n 骰子数量
     * @param k 每个骰子的面数（1到k）
     * @param target 目标点数和
     * @return 方案数模10^9+7的结果
     */
    public static int numRollsToTarget(int n, int k, int target) {
        // 参数验证
        if (n < 1 || k < 1 || target < n || target > n * k) {
            return 0; // 不可能的情况：target小于骰子数或大于骰子数*最大面数
        }
        
        // 创建二维DP数组，dp[i][j]表示使用i个骰子能得到点数和为j的方案数
        int[][] dp = new int[n + 1][target + 1];
        
        // 初始状态：使用0个骰子得到点数和为0只有一种方式
        dp[0][0] = 1;
        
        // 填充DP数组
        for (int i = 1; i <= n; i++) { // 遍历骰子数量
            for (int j = i; j <= Math.min(target, i * k); j++) { // 遍历可能的点数和（至少i，最多i*k）
                for (int m = 1; m <= k && m <= j; m++) { // 遍历当前骰子的可能点数
                    dp[i][j] = (dp[i][j] + dp[i-1][j-m]) % MOD;
                }
            }
        }
        
        return dp[n][target];
    }
    
    /**
     * 优化空间复杂度的版本，使用一维DP数组
     * @param n 骰子数量
     * @param k 每个骰子的面数（1到k）
     * @param target 目标点数和
     * @return 方案数模10^9+7的结果
     */
    public static int numRollsToTargetOptimized(int n, int k, int target) {
        // 参数验证
        if (n < 1 || k < 1 || target < n || target > n * k) {
            return 0;
        }
        
        // 创建一维DP数组，dp[j]表示使用当前骰子数能得到点数和为j的方案数
        int[] dp = new int[target + 1];
        
        // 初始状态：使用0个骰子得到点数和为0只有一种方式
        dp[0] = 1;
        
        // 遍历骰子数量
        for (int i = 1; i <= n; i++) {
            // 创建一个新数组来保存当前轮次的结果
            int[] newDp = new int[target + 1];
            
            // 遍历可能的点数和
            for (int j = i; j <= Math.min(target, i * k); j++) {
                // 遍历当前骰子的可能点数
                for (int m = 1; m <= k && m <= j; m++) {
                    newDp[j] = (newDp[j] + dp[j - m]) % MOD;
                }
            }
            
            // 更新dp数组为当前轮次的结果
            dp = newDp;
        }
        
        return dp[target];
    }
    
    /**
     * 另一种空间优化的方式，只使用一个一维数组，并倒序更新
     * 这种方式不适用，因为我们需要严格区分不同骰子数的状态
     * 所以这里只是作为对比展示，不推荐使用
     */
    public static int numRollsToTargetAlternative(int n, int k, int target) {
        // 参数验证
        if (n < 1 || k < 1 || target < n || target > n * k) {
            return 0;
        }
        
        // 创建DP数组
        int[] dp = new int[target + 1];
        dp[0] = 1;
        
        // 遍历骰子数量
        for (int i = 1; i <= n; i++) {
            // 创建临时数组来保存前一轮的结果
            int[] prevDp = new int[target + 1];
            for (int j = 0; j <= target; j++) {
                prevDp[j] = dp[j];
            }
            
            // 重置当前轮次的结果数组（除了dp[0]）
            for (int j = 1; j <= target; j++) {
                dp[j] = 0;
            }
            
            // 更新当前轮次的结果
            for (int j = 1; j <= target; j++) {
                for (int m = 1; m <= k && m <= j; m++) {
                    dp[j] = (dp[j] + prevDp[j - m]) % MOD;
                }
            }
        }
        
        return dp[target];
    }
    
    /**
     * 递归+记忆化搜索实现
     * @param n 骰子数量
     * @param k 每个骰子的面数（1到k）
     * @param target 目标点数和
     * @return 方案数模10^9+7的结果
     */
    public static int numRollsToTargetDFS(int n, int k, int target) {
        // 参数验证
        if (n < 1 || k < 1 || target < n || target > n * k) {
            return 0;
        }
        
        // 创建记忆化缓存，dp[i][j]表示使用i个骰子能得到点数和为j的方案数
        // 使用二维数组作为缓存
        Integer[][] memo = new Integer[n + 1][target + 1];
        
        return dfs(n, k, target, memo);
    }
    
    /**
     * 递归辅助函数
     * @param remainingDice 剩余骰子数量
     * @param k 每个骰子的面数（1到k）
     * @param remainingTarget 剩余目标点数
     * @param memo 记忆化缓存
     * @return 方案数模10^9+7的结果
     */
    private static int dfs(int remainingDice, int k, int remainingTarget, Integer[][] memo) {
        // 基础情况：如果没有骰子了，检查是否达成目标
        if (remainingDice == 0) {
            return remainingTarget == 0 ? 1 : 0;
        }
        
        // 检查缓存
        if (memo[remainingDice][remainingTarget] != null) {
            return memo[remainingDice][remainingTarget];
        }
        
        int ways = 0;
        
        // 尝试当前骰子的所有可能点数
        for (int i = 1; i <= k; i++) {
            // 只有当前点数不超过剩余目标，并且剩余的骰子可以凑成剩余的点数时，才继续递归
            if (i <= remainingTarget && (remainingDice - 1) <= (remainingTarget - i) && (remainingTarget - i) <= (remainingDice - 1) * k) {
                ways = (ways + dfs(remainingDice - 1, k, remainingTarget - i, memo)) % MOD;
            }
        }
        
        // 缓存结果
        memo[remainingDice][remainingTarget] = ways;
        return ways;
    }
}