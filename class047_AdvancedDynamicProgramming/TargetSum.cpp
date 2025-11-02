/**
 * 目标和 (Target Sum) - 多维费用背包问题 - C++实现
 * 
 * 题目描述：
 * 给你一个非负整数数组 nums 和一个整数 target。
 * 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个表达式。
 * 返回可以通过上述方法构造的、运算结果等于 target 的不同表达式的数目。
 * 
 * 题目来源：LeetCode 494. 目标和
 * 测试链接：https://leetcode.cn/problems/target-sum/
 * 
 * 解题思路：
 * 这是一个典型的多维费用背包问题，可以转化为子集和问题。
 * 假设我们选择一部分数字加上正号，另一部分数字加上负号。
 * 设正数集合的和为 P，负数集合的和为 N，数组总和为 S。
 * 则有：P - N = target，且 P + N = S
 * 联立可得：P = (S + target) / 2
 * 所以问题转化为：在数组中选择一些数字，使其和等于 (S + target) / 2 的方案数。
 * 
 * 算法实现：
 * 1. 动态规划：转化为子集和问题，使用背包DP
 * 2. 记忆化搜索：直接枚举所有可能的符号组合
 * 
 * 时间复杂度分析：
 * - 动态规划：O(n * sum)，其中n是数组长度，sum是数组元素和
 * - 记忆化搜索：O(n * sum)，使用偏移量处理负数
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
#include <cmath>
using namespace std;

class Solution {
public:
    /**
     * 动态规划解法（转化为子集和问题）
     * 
     * @param nums 非负整数数组
     * @param target 目标值
     * @return 表达式数目
     */
    int findTargetSumWays(vector<int>& nums, int target) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        // 如果target的绝对值大于sum，不可能达到目标
        if (abs(target) > sum) {
            return 0;
        }
        
        // 如果(S + target)是奇数，无法整除，无解
        if ((sum + target) % 2 != 0) {
            return 0;
        }
        
        // 转化为子集和问题，目标和为(S + target) / 2
        int s = (sum + target) / 2;
        if (s < 0) {
            return 0;
        }
        
        // dp[i]表示和为i的方案数
        vector<int> dp(s + 1, 0);
        dp[0] = 1; // 和为0的方案数为1（什么都不选）
        
        // 遍历每个数字
        for (int num : nums) {
            // 从后往前更新，避免重复使用同一个数字
            for (int j = s; j >= num; j--) {
                dp[j] += dp[j - num];
            }
        }
        
        return dp[s];
    }
    
    /**
     * 记忆化搜索解法
     * 
     * @param nums 非负整数数组
     * @param target 目标值
     * @return 表达式数目
     */
    int findTargetSumWaysMemo(vector<int>& nums, int target) {
        int n = nums.size();
        // 使用哈希表进行记忆化（偏移量处理负数）
        unordered_map<string, int> memo;
        
        function<int(int, int)> dfs = [&](int i, int currentSum) -> int {
            string key = to_string(i) + "," + to_string(currentSum);
            if (memo.find(key) != memo.end()) {
                return memo[key];
            }
            
            // 边界条件：处理完所有数字
            if (i == n) {
                return currentSum == target ? 1 : 0;
            }
            
            // 两种选择：加法或减法
            int ways = dfs(i + 1, currentSum + nums[i]) + 
                       dfs(i + 1, currentSum - nums[i]);
            
            memo[key] = ways;
            return ways;
        };
        
        return dfs(0, 0);
    }
};

// 测试函数
int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {1, 1, 1, 1, 1};
    int target1 = 3;
    cout << "测试用例1:" << endl;
    cout << "数组: [1,1,1,1,1], 目标值: " << target1 << endl;
    cout << "动态规划结果: " << solution.findTargetSumWays(nums1, target1) << endl;
    cout << "记忆化搜索结果: " << solution.findTargetSumWaysMemo(nums1, target1) << endl;
    cout << endl;
    
    // 测试用例2
    vector<int> nums2 = {1};
    int target2 = 1;
    cout << "测试用例2:" << endl;
    cout << "数组: [1], 目标值: " << target2 << endl;
    cout << "动态规划结果: " << solution.findTargetSumWays(nums2, target2) << endl;
    cout << "记忆化搜索结果: " << solution.findTargetSumWaysMemo(nums2, target2) << endl;
    cout << endl;
    
    // 测试用例3
    vector<int> nums3 = {1, 0};
    int target3 = 1;
    cout << "测试用例3:" << endl;
    cout << "数组: [1,0], 目标值: " << target3 << endl;
    cout << "动态规划结果: " << solution.findTargetSumWays(nums3, target3) << endl;
    cout << "记忆化搜索结果: " << solution.findTargetSumWaysMemo(nums3, target3) << endl;
    
    return 0;
}