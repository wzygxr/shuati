// LeetCode 322. 零钱兑换
// 题目描述：给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
// 计算并返回可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回 -1 。
// 你可以认为每种硬币的数量是无限的。
// 链接：https://leetcode.cn/problems/coin-change/
// 
// 解题思路：
// 这是一个完全背包问题的变形。
// 1. 状态定义：dp[i] 表示凑成金额 i 所需的最少硬币个数
// 2. 状态转移方程：dp[i] = min(dp[i], dp[i-coin] + 1)，其中 coin 是每种硬币的面额
// 3. 初始状态：dp[0] = 0（凑成金额0不需要硬币），其余初始化为一个较大值
// 4. 遍历顺序：由于硬币可以重复使用，这是完全背包问题，使用正序遍历金额
// 
// 时间复杂度：O(amount * n)，其中 n 是硬币种类数
// 空间复杂度：O(amount)

#define MAX_AMOUNT 10001

// 获取两个数中的较小值
int min(int a, int b) {
    return a < b ? a : b;
}

/**
 * 计算凑成总金额所需的最少硬币个数
 * 
 * 解题思路：
 * 这是一个完全背包问题的变形。
 * 1. 状态定义：dp[i] 表示凑成金额 i 所需的最少硬币个数
 * 2. 状态转移方程：dp[i] = min(dp[i], dp[i-coin] + 1)，其中 coin 是每种硬币的面额
 * 3. 初始状态：dp[0] = 0（凑成金额0不需要硬币），其余初始化为一个较大值
 * 4. 遍历顺序：由于硬币可以重复使用，这是完全背包问题，使用正序遍历金额
 * 
 * 参数:
 *   coins: 不同面额的硬币数组
 *   coinsSize: 硬币数组的大小
 *   amount: 目标总金额
 * 返回值:
 *   最少硬币个数，如果无法凑成返回-1
 */
int coinChange(int* coins, int coinsSize, int amount) {
    // 边界条件检查
    if (amount < 0) {
        return -1; // 金额不能为负数
    }
    if (amount == 0) {
        return 0; // 金额为0不需要硬币
    }
    if (coinsSize == 0) {
        return -1; // 硬币数组为空，无法凑成任何金额
    }
    
    // 创建dp数组，dp[i]表示凑成金额i所需的最少硬币个数
    // 初始化为amount + 1（因为最多使用amount个面值为1的硬币）
    int dp[MAX_AMOUNT];
    for (int i = 0; i <= amount; i++) {
        dp[i] = amount + 1;
    }
    dp[0] = 0; // 基础情况：凑成金额0需要0个硬币
    
    // 遍历每种硬币（物品）
    for (int i = 0; i < coinsSize; i++) {
        int coin = coins[i];
        // 完全背包问题，正序遍历金额（容量）
        // 这样可以保证每个硬币可以被重复使用
        for (int j = coin; j <= amount; j++) {
            // 状态转移：选择当前硬币或不选当前硬币
            // 如果选择当前硬币，则需要dp[j-coin] + 1个硬币
            // dp[j] = min(不选择当前硬币, 选择当前硬币)
            // 不选择当前硬币：dp[j]（保持原值）
            // 选择当前硬币：dp[j - coin] + 1（前一个状态+1个当前硬币）
            if (dp[j - coin] != amount + 1) { // 确保dp[j-coin]是可达的
                dp[j] = min(dp[j], dp[j - coin] + 1);
            }
        }
    }
    
    // 如果dp[amount]仍为初始值，说明无法凑成该金额
    return dp[amount] > amount ? -1 : dp[amount];
}

/**
 * 优化版本：提前剪枝和优化
 * 
 * 参数:
 *   coins: 不同面额的硬币数组
 *   coinsSize: 硬币数组的大小
 *   amount: 目标总金额
 * 返回值:
 *   最少硬币个数，如果无法凑成返回-1
 */
int coinChangeOptimized(int* coins, int coinsSize, int amount) {
    // 边界条件检查
    if (amount < 0) {
        return -1;
    }
    if (amount == 0) {
        return 0;
    }
    if (coinsSize == 0) {
        return -1;
    }
    
    // 这里省略了排序步骤，因为在C中实现排序比较复杂
    // 在实际应用中可以使用qsort函数进行排序
    
    int dp[MAX_AMOUNT];
    for (int i = 0; i <= amount; i++) {
        dp[i] = amount + 1;
    }
    dp[0] = 0;
    
    for (int i = 0; i < coinsSize; i++) {
        int coin = coins[i];
        // 剪枝：如果当前硬币面值大于目标金额，可以跳过
        if (coin > amount) {
            continue;
        }
        
        for (int j = coin; j <= amount; j++) {
            if (dp[j - coin] != amount + 1) {
                dp[j] = min(dp[j], dp[j - coin] + 1);
            }
        }
    }
    
    return dp[amount] > amount ? -1 : dp[amount];
}

/*
 * 示例:
 * 输入: coins = [1, 2, 5], amount = 11
 * 输出: 3
 * 解释: 11 = 5 + 5 + 1
 *
 * 输入: coins = [2], amount = 3
 * 输出: -1
 *
 * 输入: coins = [1], amount = 0
 * 输出: 0
 *
 * 时间复杂度: O(amount * n)
 *   - 外层循环遍历所有硬币：O(n)
 *   - 内层循环遍历金额：O(amount)
 * 空间复杂度: O(amount)
 *   - 一维DP数组的空间消耗
 */