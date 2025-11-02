package class073;

// LeetCode 518. 零钱兑换 II
// 题目描述：给你一个整数数组 coins 表示不同面额的硬币，另给一个整数 amount 表示总金额。
// 请你计算并返回可以凑成总金额的硬币组合数。如果任何硬币组合都无法凑出总金额，返回 0 。
// 假设每一种面额的硬币有无限个。
// 链接：https://leetcode.cn/problems/coin-change-ii/
// 
// 解题思路：
// 这是一个完全背包问题的变种。我们可以将问题转化为：使用不同面额的硬币（可以重复使用），恰好凑出总金额amount的方式有多少种。
// 
// 状态定义：dp[j] 表示凑成总金额j的硬币组合数
// 状态转移方程：dp[j] += dp[j - coin]，其中coin是当前硬币的面额，且j >= coin
// 初始状态：dp[0] = 1，表示凑成总金额0的方式有一种（不使用任何硬币）
// 
// 时间复杂度：O(amount * n)，其中n是硬币的种类数
// 空间复杂度：O(amount)，使用一维DP数组

public class Code41_CoinChangeII {

    // 主方法，用于测试
    public static void main(String[] args) {
        // 测试用例1
        int amount1 = 5;
        int[] coins1 = {1, 2, 5};
        System.out.println("测试用例1结果: " + change(amount1, coins1)); // 预期输出: 4
        
        // 测试用例2
        int amount2 = 3;
        int[] coins2 = {2};
        System.out.println("测试用例2结果: " + change(amount2, coins2)); // 预期输出: 0
        
        // 测试用例3
        int amount3 = 10;
        int[] coins3 = {10};
        System.out.println("测试用例3结果: " + change(amount3, coins3)); // 预期输出: 1
        
        // 测试用例4
        int amount4 = 0;
        int[] coins4 = {1, 2, 5};
        System.out.println("测试用例4结果: " + change(amount4, coins4)); // 预期输出: 1
    }
    
    /**
     * 计算可以凑成总金额的硬币组合数
     * @param amount 总金额
     * @param coins 硬币面额数组
     * @return 可以凑成总金额的硬币组合数
     */
    public static int change(int amount, int[] coins) {
        if (amount < 0 || coins == null) {
            return 0;
        }
        
        // 创建DP数组，dp[j]表示凑成总金额j的硬币组合数
        int[] dp = new int[amount + 1];
        
        // 初始状态：凑成总金额0的方式有一种（不使用任何硬币）
        dp[0] = 1;
        
        // 填充DP数组
        // 注意：这里我们先遍历硬币，再遍历金额，这样可以确保每个硬币只被考虑一次，避免重复计算不同的排列
        for (int coin : coins) {
            for (int j = coin; j <= amount; j++) {
                dp[j] += dp[j - coin];
            }
        }
        
        return dp[amount];
    }
    
    /**
     * 错误实现示例：先遍历金额，再遍历硬币
     * 这种实现会将不同的排列视为不同的组合
     * 例如：[1,2]和[2,1]会被视为两种不同的组合
     */
    public static int changeIncorrect(int amount, int[] coins) {
        if (amount < 0 || coins == null) {
            return 0;
        }
        
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        
        // 错误：先遍历金额，再遍历硬币
        for (int j = 1; j <= amount; j++) {
            for (int coin : coins) {
                if (j >= coin) {
                    dp[j] += dp[j - coin];
                }
            }
        }
        
        return dp[amount];
    }
    
    /**
     * 优化实现：提前过滤掉大于amount的硬币
     */
    public static int changeOptimized(int amount, int[] coins) {
        if (amount < 0 || coins == null) {
            return 0;
        }
        
        // 过滤掉大于amount的硬币
        int[] filteredCoins = new int[coins.length];
        int filteredCount = 0;
        for (int coin : coins) {
            if (coin <= amount) {
                filteredCoins[filteredCount++] = coin;
            }
        }
        
        // 创建DP数组
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        
        // 填充DP数组
        for (int i = 0; i < filteredCount; i++) {
            int coin = filteredCoins[i];
            for (int j = coin; j <= amount; j++) {
                dp[j] += dp[j - coin];
            }
        }
        
        return dp[amount];
    }
    
    /**
     * 打印出所有可能的硬币组合
     * 注意：这个方法仅用于教学目的，对于大额amount可能效率不高
     */
    public static void printAllCombinations(int amount, int[] coins) {
        java.util.List<List<Integer>> result = new java.util.ArrayList<>();
        java.util.List<Integer> current = new java.util.ArrayList<>();
        
        // 先对硬币排序，确保较小的面额在前
        java.util.Arrays.sort(coins);
        
        backtrack(amount, coins, 0, current, result);
        
        System.out.println("所有可能的硬币组合:");
        for (List<Integer> combination : result) {
            System.out.println(combination);
        }
    }
    
    /**
     * 回溯辅助方法，用于找出所有可能的硬币组合
     */
    private static void backtrack(int remaining, int[] coins, int index, List<Integer> current, List<List<Integer>> result) {
        if (remaining == 0) {
            // 找到一个有效组合
            result.add(new java.util.ArrayList<>(current));
            return;
        }
        
        if (remaining < 0 || index >= coins.length) {
            return;
        }
        
        // 不使用当前硬币
        backtrack(remaining, coins, index + 1, current, result);
        
        // 使用当前硬币（可以重复使用）
        if (remaining >= coins[index]) {
            current.add(coins[index]);
            // 注意：这里index没有增加，因为可以重复使用当前硬币
            backtrack(remaining - coins[index], coins, index, current, result);
            current.remove(current.size() - 1); // 回溯
        }
    }
    
    /**
     * 动态规划方法打印所有可能的硬币组合
     * 注意：这个方法仅用于教学目的，对于大额amount可能效率不高
     */
    public static void printAllCombinationsDP(int amount, int[] coins) {
        if (amount < 0 || coins == null || coins.length == 0) {
            return;
        }
        
        // dp[j]存储凑成总金额j的所有组合
        java.util.List<List<List<Integer>>> dp = new java.util.ArrayList<>();
        
        // 初始化dp数组
        for (int j = 0; j <= amount; j++) {
            dp.add(new java.util.ArrayList<>());
        }
        
        // 凑成总金额0的方式有一个空组合
        dp.get(0).add(new java.util.ArrayList<>());
        
        // 填充dp数组
        for (int coin : coins) {
            for (int j = coin; j <= amount; j++) {
                // 对于dp[j - coin]中的每个组合，添加当前硬币
                for (List<Integer> prev : dp.get(j - coin)) {
                    List<Integer> newCombination = new java.util.ArrayList<>(prev);
                    newCombination.add(coin);
                    dp.get(j).add(newCombination);
                }
            }
        }
        
        System.out.println("所有可能的硬币组合 (DP实现):");
        for (List<Integer> combination : dp.get(amount)) {
            System.out.println(combination);
        }
    }
}