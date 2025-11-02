#include <iostream>
#include <vector>
#include <algorithm>

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

class Solution {
public:
    // 基础DP解法
    int change(int amount, std::vector<int>& coins) {
        if (amount < 0) {
            return 0;
        }
        
        // 创建DP数组，dp[j]表示凑成总金额j的硬币组合数
        std::vector<int> dp(amount + 1, 0);
        
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
    
    // 错误实现示例：先遍历金额，再遍历硬币
    // 这种实现会将不同的排列视为不同的组合
    // 例如：[1,2]和[2,1]会被视为两种不同的组合
    int changeIncorrect(int amount, std::vector<int>& coins) {
        if (amount < 0) {
            return 0;
        }
        
        std::vector<int> dp(amount + 1, 0);
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
    
    // 优化实现：提前过滤掉大于amount的硬币
    int changeOptimized(int amount, std::vector<int>& coins) {
        if (amount < 0) {
            return 0;
        }
        
        // 过滤掉大于amount的硬币
        std::vector<int> filteredCoins;
        for (int coin : coins) {
            if (coin <= amount) {
                filteredCoins.push_back(coin);
            }
        }
        
        // 创建DP数组
        std::vector<int> dp(amount + 1, 0);
        dp[0] = 1;
        
        // 填充DP数组
        for (int coin : filteredCoins) {
            for (int j = coin; j <= amount; j++) {
                dp[j] += dp[j - coin];
            }
        }
        
        return dp[amount];
    }
    
    // 打印出所有可能的硬币组合
    void printAllCombinations(int amount, std::vector<int>& coins) {
        std::vector<std::vector<int>> result;
        std::vector<int> current;
        
        // 先对硬币排序，确保较小的面额在前
        std::sort(coins.begin(), coins.end());
        
        backtrack(amount, coins, 0, current, result);
        
        std::cout << "所有可能的硬币组合:" << std::endl;
        for (const auto& combination : result) {
            std::cout << "[";
            for (size_t i = 0; i < combination.size(); i++) {
                std::cout << combination[i];
                if (i < combination.size() - 1) {
                    std::cout << ", ";
                }
            }
            std::cout << "]" << std::endl;
        }
    }
    
    // 动态规划方法打印所有可能的硬币组合
    void printAllCombinationsDP(int amount, std::vector<int>& coins) {
        if (amount < 0 || coins.empty()) {
            return;
        }
        
        // dp[j]存储凑成总金额j的所有组合
        std::vector<std::vector<std::vector<int>>> dp(amount + 1);
        
        // 凑成总金额0的方式有一个空组合
        dp[0].push_back({});
        
        // 填充dp数组
        for (int coin : coins) {
            for (int j = coin; j <= amount; j++) {
                // 对于dp[j - coin]中的每个组合，添加当前硬币
                for (const auto& prev : dp[j - coin]) {
                    std::vector<int> newCombination = prev;
                    newCombination.push_back(coin);
                    dp[j].push_back(newCombination);
                }
            }
        }
        
        std::cout << "所有可能的硬币组合 (DP实现):" << std::endl;
        for (const auto& combination : dp[amount]) {
            std::cout << "[";
            for (size_t i = 0; i < combination.size(); i++) {
                std::cout << combination[i];
                if (i < combination.size() - 1) {
                    std::cout << ", ";
                }
            }
            std::cout << "]" << std::endl;
        }
    }

private:
    // 回溯辅助方法，用于找出所有可能的硬币组合
    void backtrack(int remaining, const std::vector<int>& coins, int index,
                  std::vector<int>& current, std::vector<std::vector<int>>& result) {
        if (remaining == 0) {
            // 找到一个有效组合
            result.push_back(current);
            return;
        }
        
        if (remaining < 0 || index >= coins.size()) {
            return;
        }
        
        // 不使用当前硬币
        backtrack(remaining, coins, index + 1, current, result);
        
        // 使用当前硬币（可以重复使用）
        if (remaining >= coins[index]) {
            current.push_back(coins[index]);
            // 注意：这里index没有增加，因为可以重复使用当前硬币
            backtrack(remaining - coins[index], coins, index, current, result);
            current.pop_back(); // 回溯
        }
    }
};

int main() {
    Solution solution;
    
    // 测试用例1
    int amount1 = 5;
    std::vector<int> coins1 = {1, 2, 5};
    std::cout << "测试用例1结果: " << solution.change(amount1, coins1) << " (预期: 4)" << std::endl;
    solution.printAllCombinations(amount1, coins1);
    std::cout << "---------------------------" << std::endl;
    
    // 测试用例2
    int amount2 = 3;
    std::vector<int> coins2 = {2};
    std::cout << "测试用例2结果: " << solution.change(amount2, coins2) << " (预期: 0)" << std::endl;
    std::cout << "---------------------------" << std::endl;
    
    // 测试用例3
    int amount3 = 10;
    std::vector<int> coins3 = {10};
    std::cout << "测试用例3结果: " << solution.change(amount3, coins3) << " (预期: 1)" << std::endl;
    std::cout << "---------------------------" << std::endl;
    
    // 测试用例4
    int amount4 = 0;
    std::vector<int> coins4 = {1, 2, 5};
    std::cout << "测试用例4结果: " << solution.change(amount4, coins4) << " (预期: 1)" << std::endl;
    
    // 测试优化版本
    std::cout << "\n测试优化版本:" << std::endl;
    std::cout << "测试用例1结果: " << solution.changeOptimized(amount1, coins1) << " (预期: 4)" << std::endl;
    
    // 测试DP打印组合功能
    std::cout << "\n测试DP打印组合功能:" << std::endl;
    solution.printAllCombinationsDP(amount1, coins1);
    
    return 0;
}