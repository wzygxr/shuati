#include <iostream>
#include <vector>
#include <algorithm>

// LeetCode 518. 零钱兑换 II
// 题目描述：给你一个整数数组 coins 表示不同面额的硬币，另给一个整数 amount 表示总金额。
// 请你计算并返回可以凑成总金额的硬币组合数。如果没有任何一种硬币组合能组成总金额，返回 0 。
// 假设每一种面额的硬币有无限个。
// 链接：https://leetcode.cn/problems/coin-change-ii/
// 
// 解题思路：
// 这是一个典型的完全背包问题的计数变种，因为每种硬币可以使用无限次，且我们需要计算组合数而非最大值。
// 状态定义：dp[i] 表示凑成金额 i 的硬币组合数
// 状态转移方程：dp[i] += dp[i - coins[j]]，其中 j 遍历所有硬币，且 i >= coins[j]
// 初始状态：dp[0] = 1（表示凑成金额0有一种方式，即不选任何硬币）
// 
// 注意事项：
// 1. 为了确保计算的是组合数而非排列数，我们需要先遍历硬币，再遍历金额
// 2. 这样可以保证每个硬币只使用一次（在组合中的顺序无关）
// 
// 时间复杂度：O(amount * n)，其中n是硬币种类数
// 空间复杂度：O(amount)，使用一维DP数组

using namespace std;

/**
 * 计算凑成总金额的硬币组合数
 * @param amount 总金额
 * @param coins 不同面额的硬币数组
 * @return 硬币组合数
 */
int change(int amount, vector<int>& coins) {
    // 参数验证
    if (amount == 0) {
        return 1; // 凑成金额0有一种方式，即不选任何硬币
    }
    if (coins.empty()) {
        return 0;
    }
    
    // 创建DP数组，dp[i]表示凑成金额i的硬币组合数
    vector<int> dp(amount + 1, 0);
    
    // 初始状态：凑成金额0有一种方式
    dp[0] = 1;
    
    // 遍历每种硬币
    for (int coin : coins) {
        // 正序遍历金额，因为完全背包允许重复使用物品
        // 先遍历硬币再遍历金额，确保计算的是组合数而非排列数
        for (int i = coin; i <= amount; i++) {
            // 状态转移：加上使用当前硬币的组合数
            dp[i] += dp[i - coin];
        }
    }
    
    return dp[amount];
}

/**
 * 优化版本：添加一些剪枝和提前终止的条件
 */
int changeOptimized(int amount, vector<int>& coins) {
    // 参数验证
    if (amount == 0) {
        return 1;
    }
    if (coins.empty()) {
        return 0;
    }
    
    // 排序有助于提前剪枝
    sort(coins.begin(), coins.end());
    
    // 创建DP数组
    vector<int> dp(amount + 1, 0);
    dp[0] = 1;
    
    // 遍历每种硬币
    for (int coin : coins) {
        // 如果当前硬币面值已经大于目标金额，可以跳过
        if (coin > amount) {
            continue;
        }
        
        // 正序遍历金额
        for (int i = coin; i <= amount; i++) {
            dp[i] += dp[i - coin];
        }
    }
    
    return dp[amount];
}

/**
 * 二维DP实现，虽然空间复杂度更高，但更容易理解
 */
int change2D(int amount, vector<int>& coins) {
    // 参数验证
    if (amount == 0) {
        return 1;
    }
    if (coins.empty()) {
        return 0;
    }
    
    int n = coins.size();
    // dp[i][j]表示使用前i种硬币凑成金额j的组合数
    vector<vector<int>> dp(n + 1, vector<int>(amount + 1, 0));
    
    // 初始化：不使用任何硬币，只能凑成金额0
    dp[0][0] = 1;
    
    // 遍历每种硬币
    for (int i = 1; i <= n; i++) {
        int coin = coins[i - 1];
        // 遍历每种金额
        for (int j = 0; j <= amount; j++) {
            // 不使用当前硬币的情况
            dp[i][j] = dp[i - 1][j];
            
            // 使用当前硬币的情况（可以使用多次）
            if (j >= coin) {
                dp[i][j] += dp[i][j - coin];
            }
        }
    }
    
    return dp[n][amount];
}

/**
 * 计算排列数的版本（与本题要求不同，仅作对比）
 * 注意：先遍历金额再遍历硬币，这样会计算排列数
 */
int permutationChange(int amount, vector<int>& coins) {
    // 参数验证
    if (amount == 0) {
        return 1;
    }
    if (coins.empty()) {
        return 0;
    }
    
    // 创建DP数组
    vector<int> dp(amount + 1, 0);
    dp[0] = 1;
    
    // 先遍历金额再遍历硬币，计算的是排列数
    for (int i = 1; i <= amount; i++) {
        for (int coin : coins) {
            if (i >= coin) {
                dp[i] += dp[i - coin];
            }
        }
    }
    
    return dp[amount];
}

/**
 * 递归+记忆化搜索实现
 */
int changeDFS(int amount, vector<int>& coins) {
    // 参数验证
    if (amount == 0) {
        return 1;
    }
    if (coins.empty()) {
        return 0;
    }
    
    // 排序有助于剪枝
    sort(coins.begin(), coins.end());
    
    // 使用记忆化搜索
    vector<vector<int>> memo(coins.size(), vector<int>(amount + 1, -1));
    
    // 定义DFS函数
    function<int(int, int)> dfs = [&](int index, int remaining) -> int {
        // 基础情况：凑成金额0，找到一种方式
        if (remaining == 0) {
            return 1;
        }
        
        // 基础情况：无法凑成
        if (index == coins.size() || remaining < 0) {
            return 0;
        }
        
        // 如果已经计算过，直接返回结果
        if (memo[index][remaining] != -1) {
            return memo[index][remaining];
        }
        
        // 不使用当前硬币的情况
        int notUse = dfs(index + 1, remaining);
        
        // 使用当前硬币的情况（如果可以使用）
        int use = 0;
        if (remaining >= coins[index]) {
            // 注意这里index不变，表示可以重复使用当前硬币
            use = dfs(index, remaining - coins[index]);
        }
        
        // 计算结果并记忆化
        memo[index][remaining] = notUse + use;
        return memo[index][remaining];
    };
    
    return dfs(0, amount);
}

int main() {
    // 测试用例1
    vector<int> coins1 = {1, 2, 5};
    int amount1 = 5;
    cout << "测试用例1结果: " << change(amount1, coins1) << endl; // 预期输出: 4
    
    // 测试用例2
    vector<int> coins2 = {2};
    int amount2 = 3;
    cout << "测试用例2结果: " << change(amount2, coins2) << endl; // 预期输出: 0
    
    // 测试用例3
    vector<int> coins3 = {10};
    int amount3 = 10;
    cout << "测试用例3结果: " << change(amount3, coins3) << endl; // 预期输出: 1
    
    // 测试用例4
    vector<int> coins4 = {1, 2, 5, 10};
    int amount4 = 10;
    cout << "测试用例4结果: " << change(amount4, coins4) << endl; // 预期输出: 11
    
    return 0;
}