#include <iostream>
#include <vector>
#include <functional>

// LeetCode 1049. 最后一块石头的重量 II
// 题目描述：有一堆石头，每块石头的重量都是正整数。每一回合，从中选出任意两块石头，然后将它们一起粉碎。
// 假设石头的重量分别为x和y，且x <= y。那么粉碎的可能结果如下：
// - 如果x == y，那么两块石头都会被完全粉碎；
// - 如果x != y，那么重量为x的石头会被完全粉碎，而重量为y的石头新重量为y - x。
// 最后，最多只会剩下一块石头。返回此石头的最小可能重量。如果没有石头剩下，就返回0。
// 链接：https://leetcode.cn/problems/last-stone-weight-ii/
// 
// 解题思路：
// 这是一个0-1背包问题的变种，问题可以转化为：
// 将石头分成两组，使得两组的重量差最小。那么剩下的石头重量就是两组重量的差的最小值。
// 等价于：找到一组石头，使得它们的重量尽可能接近总重量的一半。
// 
// 状态定义：dp[i] 表示是否可以组成和为i的子集
// 状态转移方程：dp[i] = dp[i] || dp[i - stone]，其中 stone 是当前石头的重量，且 i >= stone
// 初始状态：dp[0] = true，表示和为0的子集存在（空集）
// 
// 时间复杂度：O(n * target)，其中 n 是石头的数量，target 是总重量的一半
// 空间复杂度：O(target)，使用一维DP数组

using namespace std;

/**
 * 计算最后一块石头的最小可能重量
 * @param stones 石头重量数组
 * @return 最后一块石头的最小可能重量
 */
int lastStoneWeightII(vector<int>& stones) {
    // 参数验证
    if (stones.empty()) {
        return 0;
    }
    
    // 计算石头总重量
    int sum = 0;
    for (int stone : stones) {
        sum += stone;
    }
    
    // 计算目标和：总重量的一半（向下取整）
    int target = sum / 2;
    
    // 创建一维DP数组，dp[i]表示是否可以组成和为i的子集
    vector<bool> dp(target + 1, false);
    
    // 初始状态：和为0的子集存在（空集）
    dp[0] = true;
    
    // 对于每个石头，逆序遍历目标和（0-1背包问题）
    for (int stone : stones) {
        for (int i = target; i >= stone; i--) {
            // 状态转移：如果dp[i - stone]为true，说明可以组成和为i - stone的子集，
            // 那么再加上当前石头stone，就可以组成和为i的子集
            dp[i] = dp[i] || dp[i - stone];
        }
    }
    
    // 找到最大的i，使得dp[i]为true，其中i <= target
    // 剩下的石头重量就是sum - 2 * i
    for (int i = target; i >= 0; i--) {
        if (dp[i]) {
            return sum - 2 * i;
        }
    }
    
    return 0; // 理论上不会到达这里
}

/**
 * 二维DP数组实现
 * dp[i][j]表示前i个石头中是否可以选择一些石头，使得它们的和为j
 */
int lastStoneWeightII2D(vector<int>& stones) {
    if (stones.empty()) {
        return 0;
    }
    
    int sum = 0;
    for (int stone : stones) {
        sum += stone;
    }
    
    int target = sum / 2;
    int n = stones.size();
    
    // 创建二维DP数组
    vector<vector<bool>> dp(n + 1, vector<bool>(target + 1, false));
    
    // 初始化：前0个石头可以组成和为0的子集
    for (int i = 0; i <= n; i++) {
        dp[i][0] = true;
    }
    
    // 填充DP数组
    for (int i = 1; i <= n; i++) {
        int stone = stones[i - 1];
        for (int j = 1; j <= target; j++) {
            // 不选当前石头
            dp[i][j] = dp[i - 1][j];
            // 选当前石头（如果可以的话）
            if (j >= stone) {
                dp[i][j] = dp[i][j] || dp[i - 1][j - stone];
            }
        }
    }
    
    // 找到最大的j，使得dp[n][j]为true，其中j <= target
    for (int j = target; j >= 0; j--) {
        if (dp[n][j]) {
            return sum - 2 * j;
        }
    }
    
    return 0;
}

/**
 * 优化版本：提前剪枝
 */
int lastStoneWeightIIOptimized(vector<int>& stones) {
    if (stones.empty()) {
        return 0;
    }
    
    // 计算石头总重量
    int sum = 0;
    for (int stone : stones) {
        sum += stone;
    }
    
    // 计算目标和：总重量的一半（向下取整）
    int target = sum / 2;
    
    // 创建一维DP数组，dp[i]表示是否可以组成和为i的子集
    vector<bool> dp(target + 1, false);
    dp[0] = true;
    
    // 使用一个变量记录当前可以达到的最大和
    int currentMax = 0;
    
    for (int stone : stones) {
        // 逆序遍历目标和
        for (int i = target; i >= stone; i--) {
            if (dp[i - stone] && !dp[i]) {
                dp[i] = true;
                currentMax = max(currentMax, i);
                // 提前剪枝：如果已经可以达到目标和，直接返回结果
                if (currentMax == target) {
                    return sum - 2 * target;
                }
            }
        }
    }
    
    // 返回最小可能的最后一块石头重量
    return sum - 2 * currentMax;
}

/**
 * 递归+记忆化搜索实现
 */
int lastStoneWeightIIDFS(vector<int>& stones) {
    if (stones.empty()) {
        return 0;
    }
    
    int sum = 0;
    for (int stone : stones) {
        sum += stone;
    }
    
    int target = sum / 2;
    int n = stones.size();
    
    // 使用二维数组作为缓存，memo[i][j]表示从第i个石头开始，是否可以组成和为j的子集
    vector<vector<int>> memo(n, vector<int>(target + 1, -1)); // -1表示未计算，0表示false，1表示true
    
    // 定义DFS函数
    function<bool(int, int)> dfs = [&](int index, int remaining) -> bool {
        // 基础情况：如果剩余和为0，说明找到了一个子集
        if (remaining == 0) {
            return true;
        }
        
        // 基础情况：如果已经考虑完所有石头或者剩余和小于0，返回false
        if (index == n || remaining < 0) {
            return false;
        }
        
        // 检查缓存
        if (memo[index][remaining] != -1) {
            return memo[index][remaining] == 1;
        }
        
        // 尝试两种选择：选或不选当前石头
        // 1. 选当前石头：剩余和减去当前石头的重量，继续考虑下一个石头
        bool choose = dfs(index + 1, remaining - stones[index]);
        
        // 2. 不选当前石头：剩余和不变，继续考虑下一个石头
        bool notChoose = dfs(index + 1, remaining);
        
        // 缓存结果
        memo[index][remaining] = (choose || notChoose) ? 1 : 0;
        return memo[index][remaining] == 1;
    };
    
    // 从最大的可能和开始，找到最大的可达到的和
    for (int i = target; i >= 0; i--) {
        if (dfs(0, i)) {
            return sum - 2 * i;
        }
    }
    
    return 0;
}

/**
 * 位运算优化的DP实现
 * 每个二进制位表示是否可以组成对应索引的和
 */
int lastStoneWeightIIBit(vector<int>& stones) {
    if (stones.empty()) {
        return 0;
    }
    
    int sum = 0;
    for (int stone : stones) {
        sum += stone;
    }
    
    int target = sum / 2;
    
    // 使用位集，每个位表示是否可以组成对应的和
    // bits的第i位为1表示可以组成和为i的子集
    long long bits = 1; // 0b000...0001，表示和为0是可以的
    
    for (int stone : stones) {
        // 位运算：将当前bits左移stone位，并与原bits进行或操作
        bits |= bits << stone;
        
        // 限制bits的范围，避免不必要的计算
        if (bits > (1LL << (target + 1)) - 1) {
            bits &= (1LL << (target + 1)) - 1;
        }
    }
    
    // 找到最大的i，使得bits的第i位为1，其中i <= target
    for (int i = target; i >= 0; i--) {
        if ((bits & (1LL << i)) != 0) {
            return sum - 2 * i;
        }
    }
    
    return 0;
}

/**
 * 另一种方法：直接计算可能的重量差异
 * 使用集合来记录所有可能的重量和
 */
int lastStoneWeightIISet(vector<int>& stones) {
    if (stones.empty()) {
        return 0;
    }
    
    // 使用布尔数组来记录所有可能的重量和
    int sum = 0;
    for (int stone : stones) {
        sum += stone;
    }
    
    int target = sum / 2;
    vector<bool> dp(target + 1, false);
    dp[0] = true;
    
    for (int stone : stones) {
        for (int i = target; i >= stone; i--) {
            dp[i] = dp[i] || dp[i - stone];
        }
    }
    
    // 找到最大的可能和
    for (int i = target; i >= 0; i--) {
        if (dp[i]) {
            return sum - 2 * i;
        }
    }
    
    return 0;
}

int main() {
    // 测试用例1
    vector<int> stones1 = {2, 7, 4, 1, 8, 1};
    cout << "测试用例1结果: " << lastStoneWeightII(stones1) << endl; // 预期输出: 1
    
    // 测试用例2
    vector<int> stones2 = {31, 26, 33, 21, 40};
    cout << "测试用例2结果: " << lastStoneWeightII(stones2) << endl; // 预期输出: 5
    
    // 测试用例3
    vector<int> stones3 = {1, 2};
    cout << "测试用例3结果: " << lastStoneWeightII(stones3) << endl; // 预期输出: 1
    
    return 0;
}