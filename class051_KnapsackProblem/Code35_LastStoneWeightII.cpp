#include <iostream>
#include <vector>
#include <algorithm>

// LeetCode 1049. 最后一块石头的重量 II
// 题目描述：有一堆石头，每块石头的重量都是正整数。
// 每一回合，从中选出任意两块石头，然后将它们一起粉碎。假设石头的重量分别为 x 和 y，且 x <= y。那么粉碎的可能结果如下：
// 如果 x == y，那么两块石头都会被完全粉碎；
// 如果 x != y，那么重量为 x 的石头将会完全粉碎，而重量为 y 的石头新重量为 y-x。
// 最后，最多只会剩下一块石头。返回此石头的最小可能重量。如果没有石头剩下，就返回 0。
// 链接：https://leetcode.cn/problems/last-stone-weight-ii/
// 
// 解题思路：
// 这是一个经典的0-1背包问题的变种。我们的目标是将石头分成两组，使得两组的重量差最小。
// 设石头总重量为sum，我们希望找到一个子集，其总重量尽可能接近sum/2。
// 这样，两组的重量差就是sum - 2 * subsetSum，我们需要最小化这个值。
// 
// 状态定义：dp[j]表示是否能组成重量为j的子集
// 状态转移方程：dp[j] = dp[j] || dp[j - stones[i]]
// 初始状态：dp[0] = true（空子集的重量为0）
// 
// 时间复杂度：O(n * target)，其中n是石头数量，target是sum/2
// 空间复杂度：O(target)，使用一维DP数组

using namespace std;

/**
 * 计算最后一块石头的最小可能重量
 * @param stones 石头的重量数组
 * @return 最后一块石头的最小可能重量
 */
int lastStoneWeightII(vector<int>& stones) {
    if (stones.empty()) {
        return 0;
    }
    
    // 计算石头总重量
    int sum = 0;
    for (int stone : stones) {
        sum += stone;
    }
    
    // 目标是找到尽可能接近sum/2的子集和
    int target = sum / 2;
    
    // 创建DP数组，dp[j]表示是否能组成重量为j的子集
    vector<bool> dp(target + 1, false);
    
    // 初始状态：空子集的重量为0是可以组成的
    dp[0] = true;
    
    // 遍历每一块石头
    for (int stone : stones) {
        // 逆序遍历重量，避免重复使用同一块石头
        for (int j = target; j >= stone; j--) {
            // 如果j-stone可以组成，那么j也可以组成
            dp[j] = dp[j] || dp[j - stone];
        }
    }
    
    // 找到最大的j，使得dp[j]为true，且j <= target
    int maxSubsetSum = 0;
    for (int j = target; j >= 0; j--) {
        if (dp[j]) {
            maxSubsetSum = j;
            break;
        }
    }
    
    // 两组的重量差就是sum - 2 * maxSubsetSum
    return sum - 2 * maxSubsetSum;
}

/**
 * 使用二维DP数组的版本
 * @param stones 石头的重量数组
 * @return 最后一块石头的最小可能重量
 */
int lastStoneWeightII2D(vector<int>& stones) {
    if (stones.empty()) {
        return 0;
    }
    
    // 计算石头总重量
    int sum = 0;
    for (int stone : stones) {
        sum += stone;
    }
    
    int target = sum / 2;
    int n = stones.size();
    
    // 创建二维DP数组，dp[i][j]表示前i个石头是否能组成重量为j的子集
    vector<vector<bool>> dp(n + 1, vector<bool>(target + 1, false));
    
    // 初始状态：空子集的重量为0是可以组成的
    for (int i = 0; i <= n; i++) {
        dp[i][0] = true;
    }
    
    // 填充DP数组
    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= target; j++) {
            // 不选第i个石头
            dp[i][j] = dp[i-1][j];
            
            // 选第i个石头（如果j >= stones[i-1]）
            if (j >= stones[i-1]) {
                dp[i][j] = dp[i][j] || dp[i-1][j - stones[i-1]];
            }
        }
    }
    
    // 找到最大的j，使得dp[n][j]为true
    int maxSubsetSum = 0;
    for (int j = target; j >= 0; j--) {
        if (dp[n][j]) {
            maxSubsetSum = j;
            break;
        }
    }
    
    return sum - 2 * maxSubsetSum;
}

/**
 * 使用DP数组记录可达的重量集合
 * @param stones 石头的重量数组
 * @return 最后一块石头的最小可能重量
 */
int lastStoneWeightIIBitSet(vector<int>& stones) {
    if (stones.empty()) {
        return 0;
    }
    
    // 使用布尔数组模拟位集合，记录可达的重量
    vector<bool> dp(1501, false); // 根据约束，最大可能的总重量是30 * 100 = 3000，所以target最多是1500
    dp[0] = true;
    
    int sum = 0;
    
    for (int stone : stones) {
        sum += stone;
        // 逆序更新，避免重复使用同一块石头
        for (int j = min(sum, 1500); j >= stone; j--) {
            dp[j] = dp[j] || dp[j - stone];
        }
    }
    
    // 寻找最小可能的重量差
    int minWeight = sum;
    for (int j = 0; j <= sum / 2; j++) {
        if (dp[j]) {
            minWeight = min(minWeight, sum - 2 * j);
        }
    }
    
    return minWeight;
}

/**
 * 递归+记忆化搜索实现
 * 这个方法对于较大的输入可能会超时，但展示了递归的思路
 * @param stones 石头的重量数组
 * @return 最后一块石头的最小可能重量
 */
int lastStoneWeightIIRecursive(vector<int>& stones) {
    if (stones.empty()) {
        return 0;
    }
    
    // 计算石头总重量
    int sum = 0;
    for (int stone : stones) {
        sum += stone;
    }
    
    int target = sum / 2;
    int n = stones.size();
    
    // 创建记忆化缓存，使用二维数组保存中间结果
    // memo[i][j]表示处理到第i个石头时，当前和为j的情况下，是否已经访问过
    vector<vector<bool>> visited(n, vector<bool>(target + 1, false));
    
    // 寻找最大的可能的子集和，使得该和不超过target
    function<int(int, int)> dfs = [&](int index, int currentSum) -> int {
        // 基础情况：处理完所有石头或当前和已经超过target
        if (index == n || currentSum > target) {
            return currentSum <= target ? currentSum : 0;
        }
        
        // 检查是否已经访问过
        if (visited[index][currentSum]) {
            return currentSum;
        }
        
        // 标记为已访问
        visited[index][currentSum] = true;
        
        // 选择当前石头
        int takeSum = dfs(index + 1, currentSum + stones[index]);
        
        // 不选择当前石头
        int notTakeSum = dfs(index + 1, currentSum);
        
        return max(takeSum, notTakeSum);
    };
    
    int maxSubsetSum = dfs(0, 0);
    return sum - 2 * maxSubsetSum;
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
    
    // 测试二维DP版本
    cout << "测试用例2 (二维DP): " << lastStoneWeightII2D(stones2) << endl;
    
    // 测试位集合版本
    cout << "测试用例2 (位集合): " << lastStoneWeightIIBitSet(stones2) << endl;
    
    // 测试递归版本
    cout << "测试用例2 (递归): " << lastStoneWeightIIRecursive(stones2) << endl;
    
    return 0;
}