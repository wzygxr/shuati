package class073;

import java.util.Arrays;

// LeetCode 518. 零钱兑换 II
// 题目描述：给你一个整数数组 coins 表示不同面额的硬币，另给一个整数 amount 表示总金额。
// 请你计算并返回可以凑成总金额的硬币组合数。如果任何硬币组合都无法凑出总金额，返回 0。
// 链接：https://leetcode.cn/problems/coin-change-ii/
// 
// 解题思路：
// 这是一个完全背包问题的组合数变种，需要计算所有可能的组合数。
// 与组合总和IV不同，这里顺序不同的序列被视为相同的组合。
// 
// 状态定义：dp[i] 表示凑成总金额 i 的硬币组合数
// 状态转移方程：dp[i] += dp[i - coin]，其中 coin 是 coins 中的硬币且 i >= coin
// 初始状态：dp[0] = 1（空组合）
// 
// 关键点：为了计算组合数，需要将硬币循环放在外层，金额循环放在内层
// 
// 时间复杂度：O(amount * n)，其中 n 是硬币种类数
// 空间复杂度：O(amount)，使用一维DP数组
// 
// 工程化考量：
// 1. 异常处理：处理空数组、负数等情况
// 2. 边界条件：amount=0时返回1
// 3. 性能优化：排序硬币进行剪枝
// 4. 可读性：清晰的变量命名和注释

public class Code46_CoinChangeII {
    
    /**
     * 动态规划解法 - 计算组合数
     * @param coins 不同面额的硬币数组
     * @param amount 目标总金额
     * @return 凑成总金额的硬币组合数
     */
    public static int change(int amount, int[] coins) {
        // 参数验证
        if (coins == null || coins.length == 0) {
            return amount == 0 ? 1 : 0;
        }
        if (amount < 0) {
            return 0;
        }
        
        // 特殊情况处理
        if (amount == 0) {
            return 1; // 空组合
        }
        
        // 创建DP数组
        int[] dp = new int[amount + 1];
        dp[0] = 1; // 空组合
        
        // 为了计算组合数，需要将硬币循环放在外层
        // 金额循环放在内层
        for (int coin : coins) {
            for (int i = coin; i <= amount; i++) {
                dp[i] += dp[i - coin];
            }
        }
        
        return dp[amount];
    }
    
    /**
     * 优化的动态规划解法 - 排序硬币进行剪枝
     */
    public static int changeOptimized(int amount, int[] coins) {
        if (coins == null || coins.length == 0) {
            return amount == 0 ? 1 : 0;
        }
        if (amount < 0) {
            return 0;
        }
        
        // 排序硬币，便于理解和调试（对组合数结果无影响）
        Arrays.sort(coins);
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        
        for (int coin : coins) {
            for (int i = coin; i <= amount; i++) {
                dp[i] += dp[i - coin];
            }
        }
        
        return dp[amount];
    }
    
    /**
     * 带剪枝的动态规划解法 - 当硬币大于剩余金额时提前终止
     */
    public static int changeWithPruning(int amount, int[] coins) {
        if (coins == null || coins.length == 0) {
            return amount == 0 ? 1 : 0;
        }
        if (amount < 0) {
            return 0;
        }
        
        // 排序硬币，便于剪枝
        Arrays.sort(coins);
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        
        for (int coin : coins) {
            // 如果硬币面额已经大于amount，后续硬币更大，直接跳过
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
     * 递归+记忆化搜索解法
     */
    public static int changeDFS(int amount, int[] coins) {
        if (coins == null || coins.length == 0) {
            return amount == 0 ? 1 : 0;
        }
        if (amount < 0) {
            return 0;
        }
        
        // 使用记忆化数组
        Integer[][] memo = new Integer[coins.length][amount + 1];
        return dfs(coins, 0, amount, memo);
    }
    
    /**
     * 递归辅助函数
     * @param coins 硬币数组
     * @param index 当前考虑的硬币索引
     * @param amount 剩余金额
     * @param memo 记忆化数组
     * @return 组合数
     */
    private static int dfs(int[] coins, int index, int amount, Integer[][] memo) {
        // 基础情况
        if (amount == 0) {
            return 1;
        }
        if (amount < 0 || index >= coins.length) {
            return 0;
        }
        
        // 检查记忆化数组
        if (memo[index][amount] != null) {
            return memo[index][amount];
        }
        
        int count = 0;
        // 选择当前硬币0次或多次
        for (int k = 0; k * coins[index] <= amount; k++) {
            count += dfs(coins, index + 1, amount - k * coins[index], memo);
        }
        
        memo[index][amount] = count;
        return count;
    }
    
    /**
     * 空间优化的递归解法 - 一维记忆化
     */
    public static int changeDFSOptimized(int amount, int[] coins) {
        if (coins == null || coins.length == 0) {
            return amount == 0 ? 1 : 0;
        }
        if (amount < 0) {
            return 0;
        }
        
        // 使用一维记忆化数组
        Integer[] memo = new Integer[amount + 1];
        return dfsOptimized(coins, 0, amount, memo);
    }
    
    private static int dfsOptimized(int[] coins, int index, int amount, Integer[] memo) {
        if (amount == 0) {
            return 1;
        }
        if (amount < 0 || index >= coins.length) {
            return 0;
        }
        
        if (memo[amount] != null) {
            return memo[amount];
        }
        
        int count = 0;
        // 考虑当前硬币
        if (amount >= coins[index]) {
            count += dfsOptimized(coins, index, amount - coins[index], memo);
        }
        // 跳过当前硬币
        count += dfsOptimized(coins, index + 1, amount, memo);
        
        memo[amount] = count;
        return count;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例
        int[][] testCases = {
            {5}, {1, 2, 5},    // 预期：4
            {3}, {2},           // 预期：0
            {10}, {10},         // 预期：1
            {0}, {1, 2, 3},     // 预期：1
            {5}, {1, 2, 3},     // 预期：5
            {100}, {1, 2, 5}    // 大规模测试
        };
        
        System.out.println("零钱兑换II问题测试：");
        for (int i = 0; i < testCases.length; i += 2) {
            int amount = testCases[i][0];
            int[] coins = testCases[i + 1];
            
            int result1 = change(amount, coins);
            int result2 = changeOptimized(amount, coins);
            int result3 = changeWithPruning(amount, coins);
            int result4 = changeDFS(amount, coins);
            int result5 = changeDFSOptimized(amount, coins);
            
            System.out.printf("amount=%d, coins=%s: DP=%d, Optimized=%d, Pruning=%d, DFS=%d, DFS_Opt=%d%n",
                            amount, Arrays.toString(coins), result1, result2, result3, result4, result5);
            
            // 验证结果一致性
            if (result1 != result2 || result2 != result3 || result3 != result4 || result4 != result5) {
                System.out.println("警告：不同方法结果不一致！");
            }
        }
        
        // 性能测试 - 大规模数据
        int[] largeCoins = {1, 2, 5, 10, 20, 50, 100};
        int largeAmount = 1000;
        
        long startTime = System.currentTimeMillis();
        int largeResult = changeWithPruning(largeAmount, largeCoins);
        long endTime = System.currentTimeMillis();
        
        System.out.printf("大规模测试: coins长度=%d, amount=%d, 结果=%d, 耗时=%dms%n",
                        largeCoins.length, largeAmount, largeResult, endTime - startTime);
        
        // 边界情况测试
        System.out.println("边界情况测试：");
        System.out.println("空数组, amount=0: " + change(new int[]{}, 0)); // 预期：1
        System.out.println("空数组, amount=1: " + change(new int[]{}, 1)); // 预期：0
        System.out.println("负数amount: " + change(new int[]{1, 2, 3}, -1)); // 预期：0
        
        // 对比组合总和IV，验证遍历顺序的重要性
        System.out.println("组合数 vs 排列数对比：");
        int[] coins = {1, 2, 5};
        int amt = 5;
        System.out.printf("零钱兑换II（组合数）: amount=%d, coins=%s, 结果=%d%n",
                        amt, Arrays.toString(coins), change(amt, coins));
        
        // 模拟组合总和IV的排列数计算（错误用法）
        int[] dp = new int[amt + 1];
        dp[0] = 1;
        for (int i = 1; i <= amt; i++) {
            for (int coin : coins) {
                if (i >= coin) {
                    dp[i] += dp[i - coin];
                }
            }
        }
        System.out.printf("错误用法（排列数）: amount=%d, coins=%s, 结果=%d%n",
                        amt, Arrays.toString(coins), dp[amt]);
    }
}

/*
 * 复杂度分析：
 * 
 * 方法1：动态规划（组合数）
 * - 时间复杂度：O(amount * n)
 *   - 外层循环：n 次（硬币种类数）
 *   - 内层循环：amount 次（金额范围）
 * - 空间复杂度：O(amount)
 * 
 * 方法2：优化的动态规划
 * - 时间复杂度：O(amount * n)（与方法1相同）
 * - 空间复杂度：O(amount)
 * 
 * 方法3：带剪枝的动态规划
 * - 时间复杂度：O(amount * n)（平均情况下可能更快）
 * - 空间复杂度：O(amount)
 * 
 * 方法4：递归+记忆化搜索
 * - 时间复杂度：O(amount * n)（每个状态计算一次）
 * - 空间复杂度：O(amount * n)（二维记忆化数组）
 * 
 * 方法5：空间优化的递归
 * - 时间复杂度：O(amount * n)
 * - 空间复杂度：O(amount)（一维记忆化数组）
 * 
 * 关键点分析：
 * 1. 组合数 vs 排列数：本题需要计算组合数，因此遍历顺序很重要
 * 2. 外层循环硬币：确保计算的是组合数（顺序无关）
 * 3. 内层循环金额：完全背包的正序遍历
 * 
 * 与组合总和IV的区别：
 * - 零钱兑换II：计算组合数（顺序不同的序列视为相同）
 * - 组合总和IV：计算排列数（顺序不同的序列视为不同）
 * 
 * 工程化考量：
 * 1. 多解法对比：提供不同实现便于理解和选择
 * 2. 性能测试：包含大规模数据测试
 * 3. 边界测试：验证各种边界情况
 * 4. 错误演示：展示遍历顺序错误导致的差异
 * 
 * 面试要点：
 * 1. 理解组合数和排列数的本质区别
 * 2. 掌握动态规划中遍历顺序的重要性
 * 3. 能够分析不同解法的时空复杂度
 * 4. 了解记忆化搜索的实现技巧
 */