#include <iostream>
#include <vector>
#include <climits>
#include <algorithm>
#include <queue>

// LeetCode 322. 零钱兑换
// 题目描述：给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
// 计算并返回可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回 -1 。
// 你可以认为每种硬币的数量是无限的。
// 链接：https://leetcode.cn/problems/coin-change/
// 
// 解题思路：
// 这是一个典型的完全背包问题：
// - 硬币可以多次使用（完全背包的特点）
// - 目标是凑成总金额，并且硬币个数最少（最优解问题）
// 
// 状态定义：dp[i] 表示凑成金额i所需的最少硬币个数
// 状态转移方程：dp[i] = min(dp[i], dp[i - coin] + 1)，其中coin是每种硬币的面额，且i >= coin
// 初始状态：dp[0] = 0（凑成金额0不需要任何硬币），其他位置初始化为一个较大的值（表示无法凑成）
// 
// 时间复杂度：O(amount * n)，其中n是硬币的种类数
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
    if (amount < 0) {
        return -1;
    }
    if (amount == 0) {
        return 0;
    }
    if (coins.empty()) {
        return -1;
    }
    
    // 创建DP数组，dp[i]表示凑成金额i所需的最少硬币个数
    // 初始化为amount + 1，因为最多使用amount个面值为1的硬币，所以amount + 1是一个不可能达到的值
    vector<int> dp(amount + 1, amount + 1);
    dp[0] = 0; // 凑成金额0不需要任何硬币
    
    // 遍历每种硬币
    for (int coin : coins) {
        // 完全背包问题，正序遍历金额（允许重复使用硬币）
        for (int i = coin; i <= amount; ++i) {
            // 状态转移：取不使用当前硬币和使用当前硬币的最小值
            dp[i] = min(dp[i], dp[i - coin] + 1);
        }
    }
    
    // 如果dp[amount]仍然是初始值，说明无法凑成总金额
    return dp[amount] > amount ? -1 : dp[amount];
}

/**
 * 优化版本：提前排序硬币，允许提前终止某些循环
 */
int coinChangeOptimized(vector<int>& coins, int amount) {
    if (amount < 0) {
        return -1;
    }
    if (amount == 0) {
        return 0;
    }
    if (coins.empty()) {
        return -1;
    }
    
    // 排序硬币，从小到大
    sort(coins.begin(), coins.end());
    
    vector<int> dp(amount + 1, amount + 1);
    dp[0] = 0;
    
    for (int coin : coins) {
        // 如果当前硬币面额大于amount，可以直接跳过
        if (coin > amount) {
            break;
        }
        
        for (int i = coin; i <= amount; ++i) {
            dp[i] = min(dp[i], dp[i - coin] + 1);
        }
    }
    
    return dp[amount] > amount ? -1 : dp[amount];
}

/**
 * 二维DP数组实现（更直观但空间复杂度更高）
 * dp[i][j]表示使用前i种硬币，凑成金额j所需的最少硬币个数
 */
int coinChange2D(vector<int>& coins, int amount) {
    if (amount < 0) {
        return -1;
    }
    if (amount == 0) {
        return 0;
    }
    if (coins.empty()) {
        return -1;
    }
    
    int n = coins.size();
    // 创建二维DP数组
    vector<vector<int>> dp(n + 1, vector<int>(amount + 1, amount + 1));
    dp[0][0] = 0; // 凑成金额0不需要任何硬币
    
    // 填充DP数组
    for (int i = 1; i <= n; ++i) {
        int coin = coins[i - 1];
        
        for (int j = 0; j <= amount; ++j) {
            // 不使用第i种硬币
            dp[i][j] = dp[i - 1][j];
            
            // 使用第i种硬币（如果可以的话）
            if (j >= coin) {
                // 完全背包问题：可以重复使用同一种硬币，所以是dp[i][j-coin]而不是dp[i-1][j-coin]
                dp[i][j] = min(dp[i][j], dp[i][j - coin] + 1);
            }
        }
    }
    
    return dp[n][amount] > amount ? -1 : dp[n][amount];
}

/**
 * 递归+记忆化搜索实现
 */
int coinChangeDFS(vector<int>& coins, int amount) {
    if (amount < 0) {
        return -1;
    }
    if (amount == 0) {
        return 0;
    }
    if (coins.empty()) {
        return -1;
    }
    
    // 创建记忆化数组，memo[i]表示凑成金额i所需的最少硬币个数
    vector<int> memo(amount + 1, -2); // -2表示未计算过
    memo[0] = 0;
    
    function<int(int)> dfs = [&](int currentAmount) -> int {
        // 如果金额小于0，无法凑成
        if (currentAmount < 0) {
            return -1;
        }
        
        // 如果已经计算过，直接返回
        if (memo[currentAmount] != -2) {
            return memo[currentAmount];
        }
        
        int minCoins = INT_MAX;
        
        // 尝试使用每种硬币
        for (int coin : coins) {
            int subResult = dfs(currentAmount - coin);
            // 如果子问题有解，更新最小硬币数
            if (subResult != -1) {
                minCoins = min(minCoins, subResult + 1);
            }
        }
        
        // 记录结果
        memo[currentAmount] = (minCoins == INT_MAX) ? -1 : minCoins;
        return memo[currentAmount];
    };
    
    return dfs(amount);
}

/**
 * BFS实现：找到最少硬币个数，相当于找到从0到amount的最短路径
 */
int coinChangeBFS(vector<int>& coins, int amount) {
    if (amount < 0) {
        return -1;
    }
    if (amount == 0) {
        return 0;
    }
    if (coins.empty()) {
        return -1;
    }
    
    // 使用BFS，队列中存储当前金额
    // 为了避免重复访问，使用visited数组记录已经访问过的金额
    vector<bool> visited(amount + 1, false);
    queue<int> q;
    int step = 0;
    
    // 初始状态：金额为0
    q.push(0);
    visited[0] = true;
    
    while (!q.empty()) {
        int size = q.size();
        step++;
        
        // 遍历当前层的所有节点
        for (int i = 0; i < size; ++i) {
            int current = q.front();
            q.pop();
            
            // 尝试使用每种硬币
            for (int coin : coins) {
                int next = current + coin;
                
                // 如果达到目标金额，返回当前步数
                if (next == amount) {
                    return step;
                }
                
                // 如果next在有效范围内且未被访问过，加入队列
                if (next < amount && !visited[next]) {
                    visited[next] = true;
                    q.push(next);
                }
            }
        }
    }
    
    // 无法凑成总金额
    return -1;
}

/**
 * 贪心算法 + DFS剪枝（在某些情况下可能比动态规划更快）
 * 注意：贪心算法并不总是能得到最优解，因为可能存在较大面额的硬币虽然看起来更优，但会导致后续无法凑成总金额
 */
int coinChangeGreedy(vector<int>& coins, int amount) {
    if (amount < 0) {
        return -1;
    }
    if (amount == 0) {
        return 0;
    }
    if (coins.empty()) {
        return -1;
    }
    
    // 按面值降序排序
    sort(coins.begin(), coins.end(), greater<int>());
    
    int minCoins = INT_MAX;
    
    function<void(int, int, int)> greedyDFS = [&](int index, int currentAmount, int count) {
        // 如果剩余金额为0，更新最小硬币个数
        if (currentAmount == 0) {
            minCoins = min(minCoins, count);
            return;
        }
        
        // 如果已经考虑完所有硬币，或者当前硬币个数已经超过已知的最小硬币个数，返回
        if (index == coins.size() || count >= minCoins) {
            return;
        }
        
        int coin = coins[index];
        // 贪心策略：尽可能多地使用当前面额的硬币
        int maxCount = currentAmount / coin;
        
        // 从最多可以使用的个数开始尝试，逐步减少
        for (int i = maxCount; i >= 0; --i) {
            // 剪枝：如果当前硬币个数加上剩余金额用面值为1的硬币（最坏情况）都超过已知的最小硬币个数，直接跳过
            if (count + i >= minCoins) {
                break;
            }
            
            // 递归尝试使用剩余的硬币凑成剩余的金额
            greedyDFS(index + 1, currentAmount - i * coin, count + i);
        }
    };
    
    // 调用贪心DFS
    greedyDFS(0, amount, 0);
    
    return minCoins == INT_MAX ? -1 : minCoins;
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
    
    return 0;
}