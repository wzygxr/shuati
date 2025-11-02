#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
#include <climits>

// LeetCode 322. 零钱兑换
// 题目描述：给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
// 计算并返回可以凑成总金额所需的 最少的硬币个数 。如果没有任何一种硬币组合能组成总金额，返回 -1 。
// 你可以认为每种硬币的数量是无限的。
// 链接：https://leetcode.cn/problems/coin-change/
// 
// 解题思路：
// 这是一个经典的完全背包问题。我们需要找到凑成总金额amount所需的最少硬币数量，每种硬币可以重复使用。
// 
// 状态定义：dp[j] 表示凑成总金额j所需的最少硬币数量
// 状态转移方程：dp[j] = min(dp[j], dp[j - coin] + 1)，其中coin是当前硬币的面额，且j >= coin
// 初始状态：dp[0] = 0，表示凑成总金额0所需的最少硬币数量为0；对于其他j，初始化为一个较大的值（如amount + 1）
// 
// 时间复杂度：O(amount * n)，其中amount是总金额，n是硬币的种类数
// 空间复杂度：O(amount)，使用一维DP数组

class Solution {
public:
    // 基础DP解法
    int coinChange(std::vector<int>& coins, int amount) {
        if (amount < 0 || coins.empty()) {
            return amount == 0 ? 0 : -1;
        }
        
        // 创建DP数组，dp[j]表示凑成总金额j所需的最少硬币数量
        // 初始化为一个较大的值（amount + 1），因为最多需要amount个1元硬币
        std::vector<int> dp(amount + 1, amount + 1);
        
        // 初始状态：凑成总金额0所需的最少硬币数量为0
        dp[0] = 0;
        
        // 填充DP数组
        // 遍历硬币
        for (int coin : coins) {
            // 遍历金额
            for (int j = coin; j <= amount; j++) {
                // 更新凑成总金额j所需的最少硬币数量
                dp[j] = std::min(dp[j], dp[j - coin] + 1);
            }
        }
        
        // 如果dp[amount]仍然是初始值，说明无法凑出总金额
        return dp[amount] > amount ? -1 : dp[amount];
    }
    
    // 从金额角度出发的实现
    int coinChange2(std::vector<int>& coins, int amount) {
        if (amount < 0 || coins.empty()) {
            return amount == 0 ? 0 : -1;
        }
        
        std::vector<int> dp(amount + 1, amount + 1);
        dp[0] = 0;
        
        // 遍历金额
        for (int j = 1; j <= amount; j++) {
            // 遍历硬币
            for (int coin : coins) {
                if (coin <= j) {
                    dp[j] = std::min(dp[j], dp[j - coin] + 1);
                }
            }
        }
        
        return dp[amount] > amount ? -1 : dp[amount];
    }
    
    // 广度优先搜索(BFS)实现
    int coinChangeBFS(std::vector<int>& coins, int amount) {
        if (amount == 0) {
            return 0;
        }
        if (coins.empty() || amount < 0) {
            return -1;
        }
        
        // 使用队列进行BFS
        std::queue<int> queue;
        // 使用数组记录访问过的金额，避免重复计算
        std::vector<bool> visited(amount + 1, false);
        
        // 将0加入队列，表示初始金额
        queue.push(0);
        visited[0] = true;
        
        int level = 0; // 记录层数，即硬币数量
        
        while (!queue.empty()) {
            int size = queue.size();
            level++;
            
            for (int i = 0; i < size; i++) {
                int current = queue.front();
                queue.pop();
                
                // 尝试每种硬币
                for (int coin : coins) {
                    int next = current + coin;
                    
                    // 如果达到目标金额，返回当前层数
                    if (next == amount) {
                        return level;
                    }
                    
                    // 如果没有超过目标金额且未访问过，则加入队列
                    if (next < amount && !visited[next]) {
                        visited[next] = true;
                        queue.push(next);
                    }
                }
            }
        }
        
        // 无法凑出总金额
        return -1;
    }
    
    // 贪心 + 回溯 实现（使用引用传递result）
    int coinChangeGreedy(std::vector<int>& coins, int amount) {
        if (amount == 0) {
            return 0;
        }
        if (coins.empty() || amount < 0) {
            return -1;
        }
        
        // 按面额降序排序
        std::sort(coins.begin(), coins.end(), std::greater<int>());
        int result = INT_MAX;
        
        // 回溯搜索
        backtrack(coins, amount, 0, 0, result);
        
        return result == INT_MAX ? -1 : result;
    }
    
    // 记忆化搜索实现
    int coinChangeMemo(std::vector<int>& coins, int amount) {
        if (amount == 0) {
            return 0;
        }
        if (coins.empty() || amount < 0) {
            return -1;
        }
        
        // 按面额降序排序，有助于剪枝
        std::sort(coins.begin(), coins.end(), std::greater<int>());
        
        // 创建记忆化缓存
        std::vector<int> memo(amount + 1, 0);
        return backtrackMemo(coins, amount, memo);
    }
    
    // 优化版本，提前过滤掉大于amount的硬币
    int coinChangeOptimized(std::vector<int>& coins, int amount) {
        if (amount < 0 || coins.empty()) {
            return amount == 0 ? 0 : -1;
        }
        
        // 过滤掉大于amount的硬币
        std::vector<int> filteredCoins;
        for (int coin : coins) {
            if (coin <= amount) {
                filteredCoins.push_back(coin);
            }
        }
        
        if (filteredCoins.empty()) {
            return amount == 0 ? 0 : -1;
        }
        
        std::vector<int> dp(amount + 1, amount + 1);
        dp[0] = 0;
        
        for (int coin : filteredCoins) {
            for (int j = coin; j <= amount; j++) {
                dp[j] = std::min(dp[j], dp[j - coin] + 1);
            }
        }
        
        return dp[amount] > amount ? -1 : dp[amount];
    }
    
    // 打印出一种最优的硬币组合
    void printOptimalCoins(std::vector<int>& coins, int amount) {
        if (amount == 0) {
            std::cout << "无需硬币" << std::endl;
            return;
        }
        if (coins.empty() || amount < 0) {
            std::cout << "无法凑出总金额" << std::endl;
            return;
        }
        
        // 计算最少硬币数量
        std::vector<int> dp(amount + 1, amount + 1);
        dp[0] = 0;
        
        // 额外创建一个数组，用于记录每个金额使用的最后一个硬币
        std::vector<int> lastCoin(amount + 1, -1);
        
        // 填充DP数组
        for (int coin : coins) {
            for (int j = coin; j <= amount; j++) {
                if (dp[j] > dp[j - coin] + 1) {
                    dp[j] = dp[j - coin] + 1;
                    lastCoin[j] = coin;
                }
            }
        }
        
        if (dp[amount] > amount) {
            std::cout << "无法凑出总金额" << std::endl;
            return;
        }
        
        // 回溯构建最优硬币组合
        std::vector<int> result;
        int current = amount;
        while (current > 0) {
            int coin = lastCoin[current];
            result.push_back(coin);
            current -= coin;
        }
        
        // 输出结果
        std::cout << "最优硬币组合: ";
        for (size_t i = 0; i < result.size(); i++) {
            std::cout << result[i];
            if (i < result.size() - 1) {
                std::cout << " + ";
            }
        }
        std::cout << " = " << amount << std::endl;
        std::cout << "最少硬币数量: " << dp[amount] << std::endl;
    }

private:
    // 回溯辅助方法（使用引用传递result）
    void backtrack(const std::vector<int>& coins, int amount, int index, int count, int& result) {
        // 已经找到一个解，或者无法继续使用更大的硬币
        if (amount == 0) {
            result = std::min(result, count);
            return;
        }
        if (index >= coins.size()) {
            return;
        }
        
        // 尝试使用当前硬币的最大可能数量
        for (int i = amount / coins[index]; i >= 0 && count + i < result; i--) {
            backtrack(coins, amount - i * coins[index], index + 1, count + i, result);
        }
    }
    
    // 记忆化搜索辅助方法
    int backtrackMemo(const std::vector<int>& coins, int amount, std::vector<int>& memo) {
        if (amount == 0) {
            return 0;
        }
        if (amount < 0) {
            return -1;
        }
        
        // 检查是否已经计算过
        if (memo[amount] != 0) {
            return memo[amount];
        }
        
        int minCount = INT_MAX;
        
        // 尝试每种硬币
        for (int coin : coins) {
            int subResult = backtrackMemo(coins, amount - coin, memo);
            if (subResult >= 0 && subResult < minCount) {
                minCount = subResult + 1;
            }
        }
        
        // 记录结果
        memo[amount] = (minCount == INT_MAX) ? -1 : minCount;
        
        return memo[amount];
    }
};

int main() {
    Solution solution;
    
    // 测试用例1
    std::vector<int> coins1 = {1, 2, 5};
    int amount1 = 11;
    std::cout << "测试用例1结果: " << solution.coinChange(coins1, amount1) << " (预期: 3)" << std::endl;
    solution.printOptimalCoins(coins1, amount1);
    std::cout << "---------------------------" << std::endl;
    
    // 测试用例2
    std::vector<int> coins2 = {2};
    int amount2 = 3;
    std::cout << "测试用例2结果: " << solution.coinChange(coins2, amount2) << " (预期: -1)" << std::endl;
    std::cout << "---------------------------" << std::endl;
    
    // 测试用例3
    std::vector<int> coins3 = {1};
    int amount3 = 0;
    std::cout << "测试用例3结果: " << solution.coinChange(coins3, amount3) << " (预期: 0)" << std::endl;
    std::cout << "---------------------------" << std::endl;
    
    // 测试用例4
    std::vector<int> coins4 = {186, 419, 83, 408};
    int amount4 = 6249;
    std::cout << "测试用例4结果: " << solution.coinChange(coins4, amount4) << " (预期: 20)" << std::endl;
    
    // 测试各种实现方法
    std::cout << "\n测试各种实现方法:" << std::endl;
    std::cout << "方法2: " << solution.coinChange2(coins1, amount1) << std::endl;
    std::cout << "BFS方法: " << solution.coinChangeBFS(coins1, amount1) << std::endl;
    std::cout << "贪心回溯方法: " << solution.coinChangeGreedy(coins1, amount1) << std::endl;
    std::cout << "记忆化搜索方法: " << solution.coinChangeMemo(coins1, amount1) << std::endl;
    std::cout << "优化方法: " << solution.coinChangeOptimized(coins1, amount1) << std::endl;
    
    // 测试特殊情况 - 硬币包含0
    std::cout << "\n测试特殊情况 - 硬币包含0:" << std::endl;
    std::vector<int> coins5 = {0, 1, 2, 5};
    std::cout << "结果: " << solution.coinChange(coins5, amount1) << std::endl;
    std::cout << "优化方法结果: " << solution.coinChangeOptimized(coins5, amount1) << std::endl;
    
    // 测试大额硬币
    std::cout << "\n测试大额硬币:" << std::endl;
    std::vector<int> coins6 = {500, 100, 50, 10, 5, 1};
    int amount6 = 12345;
    std::cout << "大额硬币结果: " << solution.coinChange(coins6, amount6) << std::endl;
    
    return 0;
}