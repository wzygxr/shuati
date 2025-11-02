/**
 * 最后一块石头的重量 II (Last Stone Weight II) - 背包问题变形 - C++实现
 * 
 * 题目描述：
 * 有一堆石头，用整数数组 stones 表示。其中 stones[i] 表示第 i 块石头的重量。
 * 每一回合，从中选出任意两块石头，然后将它们一起粉碎。
 * 假设石头的重量分别为 x 和 y，且 x <= y。粉碎的可能结果如下：
 * 如果 x == y，那么两块石头都会被完全粉碎；
 * 如果 x != y，那么重量为 x 的石头完全粉碎，重量为 y 的石头新重量为 y-x。
 * 最后，最多只会剩下一块石头。返回此石头最小的可能重量。如果没有石头剩下，就返回 0。
 * 
 * 题目来源：LeetCode 1049. 最后一块石头的重量 II
 * 测试链接：https://leetcode.cn/problems/last-stone-weight-ii/
 * 
 * 解题思路：
 * 这道题可以转化为经典的背包问题。我们希望最后剩下的石头重量最小，相当于要将石头分成两堆，使得两堆的重量差最小。
 * 设石头总重量为 sum，其中一堆的重量为 s，则另一堆的重量为 sum - s。
 * 两堆重量差为 |s - (sum - s)| = |2*s - sum|。
 * 要使差值最小，就要使 s 尽可能接近 sum/2。
 * 所以问题转化为：在石头中选择一些，使其总重量尽可能接近 sum/2，但不超过 sum/2。
 * 这就变成了一个 0-1 背包问题，背包容量为 sum/2，物品重量和价值都为 stones[i]。
 * 
 * 算法实现：
 * 1. 动态规划：使用背包DP求解最大可装重量
 * 2. 记忆化搜索：递归枚举所有选择方案
 * 
 * 时间复杂度分析：
 * - 动态规划：O(n * sum)，其中n是数组长度，sum是数组元素和
 * - 记忆化搜索：O(n * sum)，每个状态计算一次
 * 
 * 空间复杂度分析：
 * - 动态规划：O(sum)，一维DP数组
 * - 记忆化搜索：O(n * sum)，二维记忆化数组
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <unordered_map>
#include <string>
using namespace std;

class Solution {
public:
    /**
     * 动态规划解法
     * 
     * @param stones 石头重量数组
     * @return 最后剩下的石头最小重量
     */
    int lastStoneWeightII(vector<int>& stones) {
        int sum = 0;
        for (int stone : stones) {
            sum += stone;
        }
        
        // 背包容量为 sum/2
        int target = sum / 2;
        
        // dp[i] 表示容量为 i 的背包最多能装的石头重量
        vector<int> dp(target + 1, 0);
        
        // 遍历每个石头
        for (int stone : stones) {
            // 从后往前更新，避免重复使用同一个石头
            for (int j = target; j >= stone; j--) {
                dp[j] = max(dp[j], dp[j - stone] + stone);
            }
        }
        
        // 两堆石头的重量差
        return sum - 2 * dp[target];
    }
    
    /**
     * 记忆化搜索解法
     * 
     * @param stones 石头重量数组
     * @return 最后剩下的石头最小重量
     */
    int lastStoneWeightIIMemo(vector<int>& stones) {
        int n = stones.size();
        int sum = 0;
        for (int stone : stones) {
            sum += stone;
        }
        
        int target = sum / 2;
        // 使用哈希表进行记忆化
        unordered_map<string, int> memo;
        
        function<int(int, int)> dfs = [&](int i, int currentWeight) -> int {
            string key = to_string(i) + "," + to_string(currentWeight);
            if (memo.find(key) != memo.end()) {
                return memo[key];
            }
            
            // 边界条件：处理完所有石头或背包已满
            if (i == n || currentWeight == 0) {
                return 0;
            }
            
            // 不选择当前石头
            int maxWeight = dfs(i + 1, currentWeight);
            
            // 选择当前石头（如果容量足够）
            if (currentWeight >= stones[i]) {
                maxWeight = max(maxWeight, dfs(i + 1, currentWeight - stones[i]) + stones[i]);
            }
            
            memo[key] = maxWeight;
            return maxWeight;
        };
        
        int maxWeight = dfs(0, target);
        return sum - 2 * maxWeight;
    }
};

// 测试函数
int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> stones1 = {2, 7, 4, 1, 8, 1};
    cout << "测试用例1:" << endl;
    cout << "石头重量: [2,7,4,1,8,1]" << endl;
    cout << "动态规划结果: " << solution.lastStoneWeightII(stones1) << endl;
    cout << "记忆化搜索结果: " << solution.lastStoneWeightIIMemo(stones1) << endl;
    cout << endl;
    
    // 测试用例2
    vector<int> stones2 = {31, 26, 33, 21, 40};
    cout << "测试用例2:" << endl;
    cout << "石头重量: [31,26,33,21,40]" << endl;
    cout << "动态规划结果: " << solution.lastStoneWeightII(stones2) << endl;
    cout << "记忆化搜索结果: " << solution.lastStoneWeightIIMemo(stones2) << endl;
    cout << endl;
    
    // 测试用例3
    vector<int> stones3 = {1, 2};
    cout << "测试用例3:" << endl;
    cout << "石头重量: [1,2]" << endl;
    cout << "动态规划结果: " << solution.lastStoneWeightII(stones3) << endl;
    cout << "记忆化搜索结果: " << solution.lastStoneWeightIIMemo(stones3) << endl;
    
    return 0;
}