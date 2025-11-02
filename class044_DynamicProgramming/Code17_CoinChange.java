package class066;

import java.util.Arrays;

// 零钱兑换 (Coin Change)
// 题目链接: https://leetcode.cn/problems/coin-change/
// 难度: 中等
// 这是一个经典的完全背包问题变种
public class Code17_CoinChange {

    // 方法1: 暴力递归（超时解法，仅作为思路展示）
    // 时间复杂度: O(S^n) - S是硬币面额数量，n是金额大小
    // 空间复杂度: O(n) - 递归调用栈深度
    // 问题: 存在大量重复计算，无法通过大测试用例
    public static int coinChange1(int[] coins, int amount) {
        // 特殊情况处理
        if (amount < 0) {
            return -1;
        }
        if (amount == 0) {
            return 0;
        }
        // 调用递归函数
        int minCoins = process1(coins, amount);
        return minCoins == Integer.MAX_VALUE ? -1 : minCoins;
    }

    // 递归函数: 计算凑成金额amount所需的最少硬币数
    private static int process1(int[] coins, int amount) {
        // 基本情况: 已经凑够了金额
        if (amount == 0) {
            return 0;
        }
        // 基本情况: 金额为负数，无法凑成
        if (amount < 0) {
            return Integer.MAX_VALUE;
        }
        
        int minCoins = Integer.MAX_VALUE;
        // 尝试每一种硬币
        for (int coin : coins) {
            // 递归计算使用当前硬币后的最少硬币数
            int subResult = process1(coins, amount - coin);
            // 如果子问题有解，更新最小值
            if (subResult != Integer.MAX_VALUE) {
                minCoins = Math.min(minCoins, subResult + 1);
            }
        }
        
        return minCoins;
    }

    // 方法2: 记忆化搜索
    // 时间复杂度: O(S * n) - S是硬币面额数量，n是金额大小
    // 空间复杂度: O(n) - 备忘录和递归调用栈
    public static int coinChange2(int[] coins, int amount) {
        if (amount < 0) {
            return -1;
        }
        if (amount == 0) {
            return 0;
        }
        // 备忘录，存储已经计算过的结果
        int[] memo = new int[amount + 1];
        Arrays.fill(memo, -2); // 使用-2表示未计算过
        int minCoins = process2(coins, amount, memo);
        return minCoins == Integer.MAX_VALUE ? -1 : minCoins;
    }

    private static int process2(int[] coins, int amount, int[] memo) {
        if (amount == 0) {
            return 0;
        }
        if (amount < 0) {
            return Integer.MAX_VALUE;
        }
        
        // 检查是否已经计算过
        if (memo[amount] != -2) {
            return memo[amount];
        }
        
        int minCoins = Integer.MAX_VALUE;
        for (int coin : coins) {
            int subResult = process2(coins, amount - coin, memo);
            if (subResult != Integer.MAX_VALUE) {
                minCoins = Math.min(minCoins, subResult + 1);
            }
        }
        
        // 记录结果到备忘录
        memo[amount] = minCoins;
        return minCoins;
    }

    // 方法3: 动态规划（自底向上）
    // 时间复杂度: O(S * n) - S是硬币面额数量，n是金额大小
    // 空间复杂度: O(n) - dp数组大小
    public static int coinChange3(int[] coins, int amount) {
        // 特殊情况处理
        if (amount < 0) {
            return -1;
        }
        if (amount == 0) {
            return 0;
        }
        
        // dp[i]表示凑成金额i所需的最少硬币数
        int[] dp = new int[amount + 1];
        // 初始化为amount + 1（一个不可能的大值）
        Arrays.fill(dp, amount + 1);
        // 基础情况：凑成金额0需要0个硬币
        dp[0] = 0;
        
        // 遍历每个金额从1到amount
        for (int i = 1; i <= amount; i++) {
            // 遍历每种硬币
            for (int coin : coins) {
                // 如果当前硬币面额不大于当前金额，并且使用当前硬币后可以得到一个更优解
                if (coin <= i && dp[i - coin] != amount + 1) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        
        // 如果dp[amount]仍然是初始值，说明无法凑成
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // 方法4: 动态规划优化版（减少不必要的计算）
    // 时间复杂度: O(S * n) - 与方法3相同，但常数项可能更小
    // 空间复杂度: O(n) - dp数组大小
    public static int coinChange4(int[] coins, int amount) {
        if (amount < 0) {
            return -1;
        }
        if (amount == 0) {
            return 0;
        }
        
        // 优化：先排序硬币，这样在某些情况下可以提前终止循环
        Arrays.sort(coins);
        
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin > i) {
                    // 由于硬币已排序，后续硬币更大，无需继续检查
                    break;
                }
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }
        
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // 测试代码
    public static void main(String[] args) {
        // 测试用例1: 标准测试
        int[] coins1 = {1, 2, 5};
        int amount1 = 11;
        System.out.println("测试用例1结果:");
        System.out.println("记忆化搜索: " + coinChange2(coins1, amount1)); // 预期输出: 3 (11 = 5 + 5 + 1)
        System.out.println("动态规划: " + coinChange3(coins1, amount1)); // 预期输出: 3
        System.out.println("动态规划优化版: " + coinChange4(coins1, amount1)); // 预期输出: 3
        
        // 测试用例2: 无法凑成的情况
        int[] coins2 = {2};
        int amount2 = 3;
        System.out.println("\n测试用例2结果:");
        System.out.println("动态规划: " + coinChange3(coins2, amount2)); // 预期输出: -1
        
        // 测试用例3: 边界情况
        int[] coins3 = {1};
        int amount3 = 0;
        System.out.println("\n测试用例3结果:");
        System.out.println("动态规划: " + coinChange3(coins3, amount3)); // 预期输出: 0
        
        // 测试用例4: 大金额测试
        int[] coins4 = {1, 3, 4, 5};
        int amount4 = 7;
        System.out.println("\n测试用例4结果:");
        System.out.println("动态规划: " + coinChange3(coins4, amount4)); // 预期输出: 2 (7 = 3 + 4)
        
        // 测试用例5: 大面额优先
        int[] coins5 = {186, 419, 83, 408};
        int amount5 = 6249;
        System.out.println("\n测试用例5结果:");
        System.out.println("动态规划优化版: " + coinChange4(coins5, amount5)); // 预期输出: 20
    }
}