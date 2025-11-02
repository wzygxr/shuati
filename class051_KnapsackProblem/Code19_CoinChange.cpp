#include <iostream>
#include <vector>
#include <algorithm>
#include <queue>
#include <climits>

// LeetCode 322. 零钱兑换
// 题目描述：给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
// 计算并返回可以凑成总金额所需的 最少的硬币个数 。如果没有任何一种硬币组合能组成总金额，返回 -1 。
// 你可以认为每种硬币的数量是无限的。
// 链接：https://leetcode.cn/problems/coin-change/
// 
// 解题思路：
// 这是一个典型的完全背包问题，因为每种硬币可以使用无限次。
// 状态定义：dp[i] 表示凑成金额 i 所需的最少硬币个数
// 状态转移方程：dp[i] = min(dp[i], dp[i - coins[j]] + 1)，其中 j 遍历所有硬币
// 初始状态：dp[0] = 0（凑成金额0需要0个硬币），其他初始化为一个较大值（如amount+1）
// 
// 时间复杂度：O(amount * n)，其中n是硬币种类数
// 空间复杂度：O(amount)，使用一维DP数组

using namespace std;

/**
 * 计算凑成总金额所需的最少硬币个数
 * @param coins 不同面额的硬币数组
 * @param amount 总金额
 * @return 最少硬币个数，如果无法凑成则返回-1
 */
int coinChange(vector<int>& coins, int amount) {
    // 参数验证
    if (amount == 0) {
        return 0;
    }
    if (coins.empty()) {
        return -1;
    }
    
    // 创建DP数组，dp[i]表示凑成金额i所需的最少硬币个数
    vector<int> dp(amount + 1, amount + 1);
    
    // 基础情况：凑成金额0需要0个硬币
    dp[0] = 0;
    
    // 遍历每种硬币（物品）
    for (int coin : coins) {
        // 正序遍历金额（容量），因为完全背包允许重复使用物品
        for (int i = coin; i <= amount; i++) {
            // 状态转移：选择当前硬币或不选择当前硬币
            dp[i] = min(dp[i], dp[i - coin] + 1);
        }
    }
    
    // 如果dp[amount]仍为初始值，说明无法凑成
    return dp[amount] > amount ? -1 : dp[amount];
}

/**
 * 优化版本：提前剪枝和优化循环范围
 */
int coinChangeOptimized(vector<int>& coins, int amount) {
    // 参数验证和快速返回
    if (amount == 0) {
        return 0;
    }
    if (coins.empty()) {
        return -1;
    }
    
    // 对硬币进行排序，从小到大
    sort(coins.begin(), coins.end());
    
    // 创建DP数组
    vector<int> dp(amount + 1, amount + 1);
    dp[0] = 0;
    
    // 遍历金额
    for (int i = 1; i <= amount; i++) {
        // 遍历每种硬币
        for (int coin : coins) {
            // 剪枝：如果当前硬币大于金额i，直接跳过
            if (coin > i) {
                break;
            }
            // 状态转移
            if (dp[i - coin] != amount + 1) {
                dp[i] = min(dp[i], dp[i - coin] + 1);
            }
        }
    }
    
    return dp[amount] > amount ? -1 : dp[amount];
}

/**
 * BFS优化版本：对于找最少硬币个数的问题，BFS可能更快找到解
 */
int coinChangeBFS(vector<int>& coins, int amount) {
    // 参数验证
    if (amount == 0) {
        return 0;
    }
    if (coins.empty()) {
        return -1;
    }
    
    // 对硬币进行排序，有助于提前剪枝
    sort(coins.begin(), coins.end());
    
    // 使用BFS，每个节点表示当前的金额和已使用的硬币个数
    // 使用一个布尔数组记录已经访问过的金额，避免重复计算
    vector<bool> visited(amount + 1, false);
    queue<int> q;
    q.push(0);
    visited[0] = true;
    int level = 0; // 当前层数，表示已使用的硬币个数
    
    while (!q.empty()) {
        int size = q.size();
        level++;
        
        for (int i = 0; i < size; i++) {
            int current = q.front();
            q.pop();
            
            // 尝试每种硬币
            for (int coin : coins) {
                int next = current + coin;
                
                // 如果找到目标金额，返回当前层数
                if (next == amount) {
                    return level;
                }
                
                // 剪枝：如果超过目标金额或已经访问过，跳过
                if (next > amount || visited[next]) {
                    continue;
                }
                
                visited[next] = true;
                q.push(next);
            }
        }
    }
    
    // 无法凑成目标金额
    return -1;
}

/**
 * 贪心+DFS优化版本：对于某些情况（如硬币是倍数关系时）效率更高
 */
int coinChangeGreedyDFS(vector<int>& coins, int amount) {
    // 参数验证
    if (amount == 0) {
        return 0;
    }
    if (coins.empty()) {
        return -1;
    }
    
    // 对硬币进行排序，从大到小
    sort(coins.begin(), coins.end(), greater<int>());
    
    // 记录最小硬币个数
    int minCount = INT_MAX;
    
    // DFS搜索
    function<void(int, int, int)> dfs = [&](int index, int remaining, int count) {
        // 已经找到一个解，或者当前硬币个数已经超过已知的最小硬币个数，直接返回
        if (remaining == 0) {
            minCount = min(minCount, count);
            return;
        }
        
        if (index == coins.size() || count >= minCount - 1) {
            return;
        }
        
        // 贪心策略：尽可能多地使用当前面值的硬币
        int maxUse = remaining / coins[index];
        for (int i = maxUse; i >= 0; i--) {
            int newRemaining = remaining - i * coins[index];
            int newCount = count + i;
            
            // 剪枝：如果剩余金额为0或者当前硬币个数加上剩余金额的最小可能个数
            // 仍然小于已知的最小硬币个数，才继续搜索
            if (newRemaining == 0 || newCount + 1 < minCount) {
                dfs(index + 1, newRemaining, newCount);
            }
        }
    };
    
    dfs(0, amount, 0);
    
    return minCount == INT_MAX ? -1 : minCount;
}

int main() {
    // 测试用例1
    vector<int> coins1 = {1, 2, 5};
    int amount1 = 11;
    cout << "测试用例1结果: " << coinChange(coins1, amount1) << endl; // 预期输出: 3 (5+5+1)
    
    // 测试用例2
    vector<int> coins2 = {2};
    int amount2 = 3;
    cout << "测试用例2结果: " << coinChange(coins2, amount2) << endl; // 预期输出: -1
    
    // 测试用例3
    vector<int> coins3 = {1};
    int amount3 = 0;
    cout << "测试用例3结果: " << coinChange(coins3, amount3) << endl; // 预期输出: 0
    
    // 测试用例4
    vector<int> coins4 = {1, 2, 5, 10, 20, 50, 100};
    int amount4 = 489;
    cout << "测试用例4结果: " << coinChange(coins4, amount4) << endl; // 预期输出: 9
    
    return 0;
}