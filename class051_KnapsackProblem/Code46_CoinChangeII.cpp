#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>

using namespace std;

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
// 4. 类型安全：使用适当的数据类型

class Solution {
public:
    /**
     * 动态规划解法 - 计算组合数
     * @param coins 不同面额的硬币数组
     * @param amount 目标总金额
     * @return 凑成总金额的硬币组合数
     */
    int change(int amount, vector<int>& coins) {
        // 参数验证
        if (coins.empty()) {
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
        vector<int> dp(amount + 1, 0);
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
    int changeOptimized(int amount, vector<int>& coins) {
        if (coins.empty()) {
            return amount == 0 ? 1 : 0;
        }
        if (amount < 0) {
            return 0;
        }
        
        // 排序硬币，便于理解和调试（对组合数结果无影响）
        sort(coins.begin(), coins.end());
        vector<int> dp(amount + 1, 0);
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
    int changeWithPruning(int amount, vector<int>& coins) {
        if (coins.empty()) {
            return amount == 0 ? 1 : 0;
        }
        if (amount < 0) {
            return 0;
        }
        
        // 排序硬币，便于剪枝
        sort(coins.begin(), coins.end());
        vector<int> dp(amount + 1, 0);
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
    int changeDFS(int amount, vector<int>& coins) {
        if (coins.empty()) {
            return amount == 0 ? 1 : 0;
        }
        if (amount < 0) {
            return 0;
        }
        
        // 使用记忆化数组
        vector<vector<int>> memo(coins.size(), vector<int>(amount + 1, -1));
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
    int dfs(vector<int>& coins, int index, int amount, vector<vector<int>>& memo) {
        // 基础情况
        if (amount == 0) {
            return 1;
        }
        if (amount < 0 || index >= coins.size()) {
            return 0;
        }
        
        // 检查记忆化数组
        if (memo[index][amount] != -1) {
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
    int changeDFSOptimized(int amount, vector<int>& coins) {
        if (coins.empty()) {
            return amount == 0 ? 1 : 0;
        }
        if (amount < 0) {
            return 0;
        }
        
        // 使用一维记忆化数组
        vector<int> memo(amount + 1, -1);
        return dfsOptimized(coins, 0, amount, memo);
    }
    
    int dfsOptimized(vector<int>& coins, int index, int amount, vector<int>& memo) {
        if (amount == 0) {
            return 1;
        }
        if (amount < 0 || index >= coins.size()) {
            return 0;
        }
        
        if (memo[amount] != -1) {
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
};

/**
 * 测试函数
 */
void testCoinChangeII() {
    Solution sol;
    
    // 测试用例
    vector<pair<int, vector<int>>> testCases = {
        {5, {1, 2, 5}},    // 预期：4
        {3, {2}},           // 预期：0
        {10, {10}},         // 预期：1
        {0, {1, 2, 3}},     // 预期：1
        {5, {1, 2, 3}},     // 预期：5
        {100, {1, 2, 5}}    // 大规模测试
    };
    
    cout << "零钱兑换II问题测试：" << endl;
    for (auto& testCase : testCases) {
        int amount = testCase.first;
        vector<int> coins = testCase.second;
        
        int result1 = sol.change(amount, coins);
        int result2 = sol.changeOptimized(amount, coins);
        int result3 = sol.changeWithPruning(amount, coins);
        int result4 = sol.changeDFS(amount, coins);
        int result5 = sol.changeDFSOptimized(amount, coins);
        
        cout << "amount=" << amount << ", coins=[";
        for (size_t i = 0; i < coins.size(); i++) {
            cout << coins[i];
            if (i < coins.size() - 1) cout << ", ";
        }
        cout << "]: DP=" << result1 
             << ", Optimized=" << result2 
             << ", Pruning=" << result3 
             << ", DFS=" << result4 
             << ", DFS_Opt=" << result5 << endl;
        
        // 验证结果一致性
        if (result1 != result2 || result2 != result3 || result3 != result4 || result4 != result5) {
            cout << "警告：不同方法结果不一致！" << endl;
        }
    }
    
    // 性能测试 - 大规模数据
    vector<int> largeCoins = {1, 2, 5, 10, 20, 50, 100};
    int largeAmount = 1000;
    
    auto startTime = chrono::high_resolution_clock::now();
    int largeResult = sol.changeWithPruning(largeAmount, largeCoins);
    auto endTime = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
    
    cout << "大规模测试: coins长度=" << largeCoins.size() 
         << ", amount=" << largeAmount 
         << ", 结果=" << largeResult 
         << ", 耗时=" << duration.count() << "ms" << endl;
    
    // 边界情况测试
    cout << "边界情况测试：" << endl;
    vector<int> emptyCoins;
    cout << "空数组, amount=0: " << sol.change(0, emptyCoins) << endl; // 预期：1
    cout << "空数组, amount=1: " << sol.change(1, emptyCoins) << endl; // 预期：0
    cout << "负数amount: " << sol.change(-1, {1, 2, 3}) << endl; // 预期：0
    
    // 对比组合总和IV，验证遍历顺序的重要性
    cout << "组合数 vs 排列数对比：" << endl;
    vector<int> coins = {1, 2, 5};
    int amt = 5;
    cout << "零钱兑换II（组合数）: amount=" << amt << ", coins=[";
    for (size_t i = 0; i < coins.size(); i++) {
        cout << coins[i];
        if (i < coins.size() - 1) cout << ", ";
    }
    cout << "], 结果=" << sol.change(amt, coins) << endl;
    
    // 模拟组合总和IV的排列数计算（错误用法）
    vector<int> dp(amt + 1, 0);
    dp[0] = 1;
    for (int i = 1; i <= amt; i++) {
        for (int coin : coins) {
            if (i >= coin) {
                dp[i] += dp[i - coin];
            }
        }
    }
    cout << "错误用法（排列数）: amount=" << amt << ", coins=[";
    for (size_t i = 0; i < coins.size(); i++) {
        cout << coins[i];
        if (i < coins.size() - 1) cout << ", ";
    }
    cout << "], 结果=" << dp[amt] << endl;
}

int main() {
    try {
        testCoinChangeII();
    } catch (const exception& e) {
        cerr << "错误: " << e.what() << endl;
        return 1;
    }
    return 0;
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
 * C++特定优化：
 * 1. 使用vector代替数组，更安全
 * 2. 使用STL算法进行排序
 * 3. 使用chrono进行精确性能测试
 * 4. 异常处理使用C++标准异常
 * 
 * 关键点分析：
 * 1. 组合数 vs 排列数：本题需要计算组合数，因此遍历顺序很重要
 * 2. 外层循环硬币：确保计算的是组合数（顺序无关）
 * 3. 内层循环金额：完全背包的正序遍历
 * 
 * 工程化考量：
 * 1. 模块化设计：将不同解法封装为类方法
 * 2. 性能优化：利用STL算法和数据结构
 * 3. 测试覆盖：包含各种边界情况和性能测试
 * 4. 错误演示：展示遍历顺序错误导致的差异
 */