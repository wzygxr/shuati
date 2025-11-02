package class073;

// LeetCode 518. 零钱兑换 II
// 题目描述：给你一个整数数组 coins 表示不同面额的硬币，另给一个整数 amount 表示总金额。
// 请你计算并返回可以凑成总金额的硬币组合数。如果任何硬币组合都无法凑出总金额，返回 0 。
// 假设每一种面额的硬币有无限个。
// 链接：https://leetcode.cn/problems/coin-change-ii/
// 
// 解题思路：
// 这是一个典型的完全背包问题，但目标是求组合数而不是最少硬币个数：
// - 硬币可以多次使用（完全背包的特点）
// - 目标是计算凑成总金额的不同组合数
// 
// 状态定义：dp[i] 表示凑成金额i的不同组合数
// 状态转移方程：dp[i] += dp[i - coin]，其中coin是每种硬币的面额，且i >= coin
// 初始状态：dp[0] = 1（凑成金额0只有一种方式：不使用任何硬币）
// 
// 时间复杂度：O(amount * n)，其中n是硬币的种类数
// 空间复杂度：O(amount)，使用一维DP数组

public class Code32_CoinChangeII {

    // 主方法，用于测试
    public static void main(String[] args) {
        // 测试用例1
        int[] coins1 = {1, 2, 5};
        int amount1 = 5;
        System.out.println("测试用例1结果: " + change(coins1, amount1)); // 预期输出: 4 ([1,1,1,1,1], [1,1,1,2], [1,2,2], [5])
        
        // 测试用例2
        int[] coins2 = {2};
        int amount2 = 3;
        System.out.println("测试用例2结果: " + change(coins2, amount2)); // 预期输出: 0
        
        // 测试用例3
        int[] coins3 = {10};
        int amount3 = 10;
        System.out.println("测试用例3结果: " + change(coins3, amount3)); // 预期输出: 1
    }
    
    /**
     * 计算凑成总金额的不同硬币组合数
     * @param coins 不同面额的硬币数组
     * @param amount 总金额
     * @return 凑成总金额的不同组合数
     */
    public static int change(int[] coins, int amount) {
        // 参数验证
        if (amount < 0) {
            return 0;
        }
        if (coins == null) {
            return 0;
        }
        
        // 创建DP数组，dp[i]表示凑成金额i的不同组合数
        int[] dp = new int[amount + 1];
        dp[0] = 1; // 凑成金额0只有一种方式：不使用任何硬币
        
        // 遍历每种硬币
        for (int coin : coins) {
            // 完全背包问题，正序遍历金额（允许重复使用硬币）
            // 注意：这里遍历硬币放在外层，金额放在内层，这样可以避免重复计算不同顺序的组合
            // 例如，对于coins=[1,2]和amount=3，如果先遍历金额再遍历硬币，会计算出[1,2]和[2,1]作为两种不同的组合
            for (int i = coin; i <= amount; i++) {
                // 状态转移：当前金额可以由(i-coin)的金额加上一个coin得到
                dp[i] += dp[i - coin];
            }
        }
        
        return dp[amount];
    }
    
    /**
     * 二维DP数组实现（更直观但空间复杂度更高）
     * dp[i][j]表示使用前i种硬币，凑成金额j的不同组合数
     */
    public static int change2D(int[] coins, int amount) {
        if (amount < 0) {
            return 0;
        }
        if (coins == null) {
            return 0;
        }
        
        int n = coins.length;
        // 创建二维DP数组
        int[][] dp = new int[n + 1][amount + 1];
        
        // 初始化：使用0种硬币，只能凑成金额0，有一种方式
        for (int j = 0; j <= amount; j++) {
            dp[0][j] = 0;
        }
        dp[0][0] = 1;
        
        // 填充DP数组
        for (int i = 1; i <= n; i++) {
            int coin = coins[i - 1];
            
            for (int j = 0; j <= amount; j++) {
                // 不使用第i种硬币
                dp[i][j] = dp[i - 1][j];
                
                // 使用第i种硬币（如果可以的话）
                // 完全背包问题：可以重复使用同一种硬币，所以是dp[i][j-coin]而不是dp[i-1][j-coin]
                if (j >= coin) {
                    dp[i][j] += dp[i][j - coin];
                }
            }
        }
        
        return dp[n][amount];
    }
    
    /**
     * 优化版本：提前处理特殊情况
     */
    public static int changeOptimized(int[] coins, int amount) {
        // 快速处理特殊情况
        if (amount < 0) {
            return 0;
        }
        if (amount == 0) {
            return 1;
        }
        if (coins == null || coins.length == 0) {
            return 0;
        }
        
        // 创建DP数组
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        
        for (int coin : coins) {
            // 如果当前硬币面额大于amount，可以跳过
            if (coin > amount) {
                continue;
            }
            
            for (int i = coin; i <= amount; i++) {
                dp[i] += dp[i - coin];
            }
        }
        
        return dp[amount];
    }
    
    /**
     * 递归+记忆化搜索实现
     * 注意：由于这个问题的参数范围较大，递归+记忆化搜索可能会超时
     * 这里仅作为一种实现方式展示
     */
    public static int changeDFS(int[] coins, int amount) {
        if (amount < 0) {
            return 0;
        }
        if (amount == 0) {
            return 1;
        }
        if (coins == null || coins.length == 0) {
            return 0;
        }
        
        // 为了避免重复计算不同顺序的组合，我们先对硬币进行排序，然后确保每次选择的硬币不小于上一次选择的硬币
        java.util.Arrays.sort(coins);
        
        // 创建记忆化数组，memo[index][remain]表示从第index种硬币开始，凑成剩余金额remain的不同组合数
        Integer[][] memo = new Integer[coins.length][amount + 1];
        
        return dfs(coins, 0, amount, memo);
    }
    
    /**
     * 递归辅助函数
     * @param coins 硬币数组
     * @param index 当前考虑的硬币索引
     * @param remain 剩余需要凑成的金额
     * @param memo 记忆化数组
     * @return 从当前索引开始，凑成剩余金额的不同组合数
     */
    private static int dfs(int[] coins, int index, int remain, Integer[][] memo) {
        // 基础情况：如果剩余金额为0，说明找到了一种组合
        if (remain == 0) {
            return 1;
        }
        
        // 基础情况：如果已经考虑完所有硬币，或者当前硬币面额大于剩余金额，无法凑成
        if (index >= coins.length || coins[index] > remain) {
            return 0;
        }
        
        // 检查缓存
        if (memo[index][remain] != null) {
            return memo[index][remain];
        }
        
        int ways = 0;
        
        // 计算使用当前硬币的不同次数的情况
        // k表示使用当前硬币的个数，从0开始
        for (int k = 0; k * coins[index] <= remain; k++) {
            // 递归计算不使用当前硬币（k=0）或使用k次当前硬币后的组合数
            ways += dfs(coins, index + 1, remain - k * coins[index], memo);
        }
        
        // 缓存结果
        memo[index][remain] = ways;
        return ways;
    }
    
    /**
     * 另一种递归实现方式，更加简洁
     */
    public static int changeDFS2(int[] coins, int amount) {
        if (amount < 0) {
            return 0;
        }
        if (amount == 0) {
            return 1;
        }
        if (coins == null || coins.length == 0) {
            return 0;
        }
        
        // 排序硬币，避免重复计算
        java.util.Arrays.sort(coins);
        
        Integer[][] memo = new Integer[coins.length][amount + 1];
        
        return dfs2(coins, 0, amount, memo);
    }
    
    /**
     * 递归辅助函数 - 更简洁的实现
     * @param coins 硬币数组
     * @param index 当前考虑的硬币索引
     * @param remain 剩余需要凑成的金额
     * @param memo 记忆化数组
     * @return 从当前索引开始，凑成剩余金额的不同组合数
     */
    private static int dfs2(int[] coins, int index, int remain, Integer[][] memo) {
        if (remain == 0) {
            return 1;
        }
        
        if (index >= coins.length || coins[index] > remain) {
            return 0;
        }
        
        if (memo[index][remain] != null) {
            return memo[index][remain];
        }
        
        // 不使用当前硬币的情况
        int skip = dfs2(coins, index + 1, remain, memo);
        
        // 使用当前硬币的情况（可以继续使用当前硬币）
        int use = dfs2(coins, index, remain - coins[index], memo);
        
        // 缓存结果
        memo[index][remain] = skip + use;
        return memo[index][remain];
    }
}