#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>

// 零钱兑换 (Coin Change)
// 题目链接: https://leetcode.cn/problems/coin-change/
// 难度: 中等
// 这是一个经典的完全背包问题变种
class Solution {
public:
    // 方法1: 暴力递归（超时解法，仅作为思路展示）
    // 时间复杂度: O(S^n) - S是硬币面额数量，n是金额大小
    // 空间复杂度: O(n) - 递归调用栈深度
    int coinChange1(std::vector<int>& coins, int amount) {
        // 特殊情况处理
        if (amount < 0) {
            return -1;
        }
        if (amount == 0) {
            return 0;
        }
        // 调用递归函数
        int minCoins = process1(coins, amount);
        return minCoins == INT_MAX ? -1 : minCoins;
    }

private:
    // 递归函数: 计算凑成金额amount所需的最少硬币数
    int process1(const std::vector<int>& coins, int amount) {
        // 基本情况: 已经凑够了金额
        if (amount == 0) {
            return 0;
        }
        // 基本情况: 金额为负数，无法凑成
        if (amount < 0) {
            return INT_MAX;
        }
        
        int minCoins = INT_MAX;
        // 尝试每一种硬币
        for (int coin : coins) {
            // 递归计算使用当前硬币后的最少硬币数
            int subResult = process1(coins, amount - coin);
            // 如果子问题有解，更新最小值
            if (subResult != INT_MAX) {
                minCoins = std::min(minCoins, subResult + 1);
            }
        }
        
        return minCoins;
    }

public:
    // 方法2: 记忆化搜索
    // 时间复杂度: O(S * n) - S是硬币面额数量，n是金额大小
    // 空间复杂度: O(n) - 备忘录和递归调用栈
    int coinChange2(std::vector<int>& coins, int amount) {
        if (amount < 0) {
            return -1;
        }
        if (amount == 0) {
            return 0;
        }
        // 备忘录，存储已经计算过的结果
        std::vector<int> memo(amount + 1, -2); // 使用-2表示未计算过
        int minCoins = process2(coins, amount, memo);
        return minCoins == INT_MAX ? -1 : minCoins;
    }

private:
    int process2(const std::vector<int>& coins, int amount, std::vector<int>& memo) {
        if (amount == 0) {
            return 0;
        }
        if (amount < 0) {
            return INT_MAX;
        }
        
        // 检查是否已经计算过
        if (memo[amount] != -2) {
            return memo[amount];
        }
        
        int minCoins = INT_MAX;
        for (int coin : coins) {
            int subResult = process2(coins, amount - coin, memo);
            if (subResult != INT_MAX) {
                minCoins = std::min(minCoins, subResult + 1);
            }
        }
        
        // 记录结果到备忘录
        memo[amount] = minCoins;
        return minCoins;
    }

public:
    // 方法3: 动态规划（自底向上）
    // 时间复杂度: O(S * n) - S是硬币面额数量，n是金额大小
    // 空间复杂度: O(n) - dp数组大小
    int coinChange3(std::vector<int>& coins, int amount) {
        // 特殊情况处理
        if (amount < 0) {
            return -1;
        }
        if (amount == 0) {
            return 0;
        }
        
        // dp[i]表示凑成金额i所需的最少硬币数
        std::vector<int> dp(amount + 1, amount + 1);
        // 基础情况：凑成金额0需要0个硬币
        dp[0] = 0;
        
        // 遍历每个金额从1到amount
        for (int i = 1; i <= amount; i++) {
            // 遍历每种硬币
            for (int coin : coins) {
                // 如果当前硬币面额不大于当前金额，并且使用当前硬币后可以得到一个更优解
                if (coin <= i && dp[i - coin] != amount + 1) {
                    dp[i] = std::min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        
        // 如果dp[amount]仍然是初始值，说明无法凑成
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // 方法4: 动态规划优化版（减少不必要的计算）
    // 时间复杂度: O(S * n) - 与方法3相同，但常数项可能更小
    // 空间复杂度: O(n) - dp数组大小
    int coinChange4(std::vector<int>& coins, int amount) {
        if (amount < 0) {
            return -1;
        }
        if (amount == 0) {
            return 0;
        }
        
        // 优化：先排序硬币，这样在某些情况下可以提前终止循环
        std::sort(coins.begin(), coins.end());
        
        std::vector<int> dp(amount + 1, amount + 1);
        dp[0] = 0;
        
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin > i) {
                    // 由于硬币已排序，后续硬币更大，无需继续检查
                    break;
                }
                dp[i] = std::min(dp[i], dp[i - coin] + 1);
            }
        }
        
        return dp[amount] > amount ? -1 : dp[amount];
    }
};

// 测试代码
int main() {
    Solution solution;
    
    // 测试用例1: 标准测试
    std::vector<int> coins1 = {1, 2, 5};
    int amount1 = 11;
    std::cout << "测试用例1结果:" << std::endl;
    std::cout << "记忆化搜索: " << solution.coinChange2(coins1, amount1) << std::endl; // 预期输出: 3 (11 = 5 + 5 + 1)
    std::cout << "动态规划: " << solution.coinChange3(coins1, amount1) << std::endl; // 预期输出: 3
    std::cout << "动态规划优化版: " << solution.coinChange4(coins1, amount1) << std::endl; // 预期输出: 3
    
    // 测试用例2: 无法凑成的情况
    std::vector<int> coins2 = {2};
    int amount2 = 3;
    std::cout << "\n测试用例2结果:" << std::endl;
    std::cout << "动态规划: " << solution.coinChange3(coins2, amount2) << std::endl; // 预期输出: -1
    
    // 测试用例3: 边界情况
    std::vector<int> coins3 = {1};
    int amount3 = 0;
    std::cout << "\n测试用例3结果:" << std::endl;
    std::cout << "动态规划: " << solution.coinChange3(coins3, amount3) << std::endl; // 预期输出: 0
    
    // 测试用例4: 大金额测试
    std::vector<int> coins4 = {1, 3, 4, 5};
    int amount4 = 7;
    std::cout << "\n测试用例4结果:" << std::endl;
    std::cout << "动态规划: " << solution.coinChange3(coins4, amount4) << std::endl; // 预期输出: 2 (7 = 3 + 4)
    
    // 测试用例5: 大面额优先
    std::vector<int> coins5 = {186, 419, 83, 408};
    int amount5 = 6249;
    std::cout << "\n测试用例5结果:" << std::endl;
    std::cout << "动态规划优化版: " << solution.coinChange4(coins5, amount5) << std::endl; // 预期输出: 20
    
    return 0;
}