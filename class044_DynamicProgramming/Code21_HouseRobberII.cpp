// 打家劫舍II (House Robber II)
// 你是一个专业的小偷，计划偷窃沿街的房屋，每间房内都藏有一定的现金。
// 这个地方所有的房屋都围成一圈，这意味着第一个房屋和最后一个房屋是紧挨着的。
// 同时，相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
// 给定一个代表每个房屋存放金额的非负整数数组，计算你在不触动警报装置的情况下，能够偷窃到的最高金额。
// 测试链接 : https://leetcode.cn/problems/house-robber-ii/

#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>
using namespace std;

class Solution {
public:
    // 方法1：分解为两个线性问题
    // 时间复杂度：O(n) - 解决两个线性问题
    // 空间复杂度：O(n) - 使用辅助数组
    // 核心思路：环形问题分解为[0, n-2]和[1, n-1]两个线性问题
    int rob1(vector<int>& nums) {
        if (nums.empty()) return 0;
        if (nums.size() == 1) return nums[0];
        
        int n = nums.size();
        // 情况1：不偷最后一间房（偷第一间房）
        vector<int> nums1(nums.begin(), nums.end() - 1);
        int max1 = robLinear(nums1);
        // 情况2：不偷第一间房（偷最后一间房）
        vector<int> nums2(nums.begin() + 1, nums.end());
        int max2 = robLinear(nums2);
        
        return max(max1, max2);
    }
    
    // 线性打家劫舍问题的解决方案（打家劫舍I）
    int robLinear(vector<int>& nums) {
        if (nums.empty()) return 0;
        if (nums.size() == 1) return nums[0];
        
        int n = nums.size();
        vector<int> dp(n);
        dp[0] = nums[0];
        dp[1] = max(nums[0], nums[1]);
        
        for (int i = 2; i < n; i++) {
            dp[i] = max(dp[i - 1], dp[i - 2] + nums[i]);
        }
        
        return dp[n - 1];
    }

    // 方法2：空间优化的分解方案
    // 时间复杂度：O(n) - 解决两个线性问题
    // 空间复杂度：O(1) - 只使用常数空间
    // 优化：避免创建新数组，直接在原数组上操作
    int rob2(vector<int>& nums) {
        if (nums.empty()) return 0;
        if (nums.size() == 1) return nums[0];
        
        int n = nums.size();
        // 情况1：不偷最后一间房
        int max1 = robLinearOptimized(nums, 0, n - 2);
        // 情况2：不偷第一间房
        int max2 = robLinearOptimized(nums, 1, n - 1);
        
        return max(max1, max2);
    }
    
    // 空间优化的线性打家劫舍
    int robLinearOptimized(vector<int>& nums, int start, int end) {
        if (start > end) return 0;
        if (start == end) return nums[start];
        
        int prev2 = nums[start];  // dp[i-2]
        int prev1 = max(nums[start], nums[start + 1]);  // dp[i-1]
        
        for (int i = start + 2; i <= end; i++) {
            int current = max(prev1, prev2 + nums[i]);
            prev2 = prev1;
            prev1 = current;
        }
        
        return prev1;
    }

    // 方法3：动态规划（统一处理）
    // 时间复杂度：O(n) - 遍历数组两次
    // 空间复杂度：O(n) - 使用dp数组
    // 核心思路：分别计算包含第一个元素和不包含第一个元素的情况
    int rob3(vector<int>& nums) {
        if (nums.empty()) return 0;
        if (nums.size() == 1) return nums[0];
        
        int n = nums.size();
        // dp1: 不偷第一间房的情况
        vector<int> dp1(n);
        // dp2: 偷第一间房的情况（不能偷最后一间房）
        vector<int> dp2(n);
        
        // 初始化dp1（不偷第一间房）
        dp1[0] = 0;
        dp1[1] = nums[1];
        for (int i = 2; i < n; i++) {
            dp1[i] = max(dp1[i - 1], dp1[i - 2] + nums[i]);
        }
        
        // 初始化dp2（偷第一间房，不能偷最后一间房）
        dp2[0] = nums[0];
        dp2[1] = max(nums[0], nums[1]);
        for (int i = 2; i < n - 1; i++) {
            dp2[i] = max(dp2[i - 1], dp2[i - 2] + nums[i]);
        }
        
        return max(dp1[n - 1], dp2[n - 2]);
    }

    // 方法4：记忆化搜索（自顶向下）
    // 时间复杂度：O(n) - 每个状态只计算一次
    // 空间复杂度：O(n) - 递归栈和记忆化数组
    // 核心思路：递归解决两个子问题，使用记忆化避免重复计算
    int rob4(vector<int>& nums) {
        if (nums.empty()) return 0;
        if (nums.size() == 1) return nums[0];
        
        int n = nums.size();
        vector<int> memo1(n, -1);  // 记忆化数组1（不偷最后一间房）
        vector<int> memo2(n, -1);  // 记忆化数组2（不偷第一间房）
        
        // 情况1：不偷最后一间房
        int max1 = dfs(nums, 0, n - 2, memo1);
        // 情况2：不偷第一间房
        int max2 = dfs(nums, 1, n - 1, memo2);
        
        return max(max1, max2);
    }
    
private:
    int dfs(vector<int>& nums, int start, int end, vector<int>& memo) {
        if (start > end) return 0;
        if (memo[start] != -1) return memo[start];
        
        if (start == end) {
            memo[start] = nums[start];
            return nums[start];
        }
        
        if (start + 1 == end) {
            int max_val = max(nums[start], nums[end]);
            memo[start] = max_val;
            return max_val;
        }
        
        // 选择1：偷当前房屋，跳过下一个
        int robCurrent = nums[start] + dfs(nums, start + 2, end, memo);
        // 选择2：不偷当前房屋，考虑下一个
        int skipCurrent = dfs(nums, start + 1, end, memo);
        
        int max_val = max(robCurrent, skipCurrent);
        memo[start] = max_val;
        return max_val;
    }

public:
    // 方法5：暴力递归（用于对比）
    // 时间复杂度：O(2^n) - 指数级，效率极低
    // 空间复杂度：O(n) - 递归调用栈深度
    // 问题：存在大量重复计算，仅用于教学目的
    int rob5(vector<int>& nums) {
        if (nums.empty()) return 0;
        if (nums.size() == 1) return nums[0];
        
        int n = nums.size();
        // 分解为两个线性问题
        vector<int> nums1(nums.begin(), nums.end() - 1);
        vector<int> nums2(nums.begin() + 1, nums.end());
        int max1 = robLinearBruteForce(nums1);
        int max2 = robLinearBruteForce(nums2);
        
        return max(max1, max2);
    }
    
private:
    int robLinearBruteForce(vector<int>& nums) {
        return dfsBruteForce(nums, 0);
    }
    
    int dfsBruteForce(vector<int>& nums, int index) {
        if (index >= nums.size()) return 0;
        
        // 选择1：偷当前房屋，跳过下一个
        int robCurrent = nums[index] + dfsBruteForce(nums, index + 2);
        // 选择2：不偷当前房屋，考虑下一个
        int skipCurrent = dfsBruteForce(nums, index + 1);
        
        return max(robCurrent, skipCurrent);
    }
};

// 测试函数
void testCase(Solution& solution, vector<int>& nums, int expected, const string& description) {
    int result1 = solution.rob1(nums);
    int result2 = solution.rob2(nums);
    int result3 = solution.rob3(nums);
    int result4 = solution.rob4(nums);
    
    bool allCorrect = (result1 == expected && result2 == expected && 
                      result3 == expected && result4 == expected);
    
    cout << description << ": " << (allCorrect ? "✓" : "✗");
    if (!allCorrect) {
        cout << " 方法1:" << result1 << " 方法2:" << result2 
             << " 方法3:" << result3 << " 方法4:" << result4 
             << " 预期:" << expected;
    }
    cout << endl;
}

// 性能测试函数
void performanceTest(Solution& solution, vector<int>& nums) {
    cout << "性能测试 n=" << nums.size() << ":" << endl;
    
    auto start = chrono::high_resolution_clock::now();
    int result2 = solution.rob2(nums);
    auto end = chrono::high_resolution_clock::now();
    auto duration2 = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "空间优化方法: " << result2 << ", 耗时: " << duration2.count() << "μs" << endl;
    
    start = chrono::high_resolution_clock::now();
    int result3 = solution.rob3(nums);
    end = chrono::high_resolution_clock::now();
    auto duration3 = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "统一处理方法: " << result3 << ", 耗时: " << duration3.count() << "μs" << endl;
    
    // 暴力方法太慢，不测试
    cout << "暴力方法在n=100时太慢，跳过测试" << endl;
}

int main() {
    Solution solution;
    
    cout << "=== 打家劫舍II测试 ===" << endl;
    
    // 边界测试
    vector<int> nums1 = {};
    testCase(solution, nums1, 0, "空数组");
    
    vector<int> nums2 = {5};
    testCase(solution, nums2, 5, "单元素数组");
    
    vector<int> nums3 = {2, 3};
    testCase(solution, nums3, 3, "双元素数组");
    
    // LeetCode示例测试
    vector<int> nums4 = {2, 3, 2};
    testCase(solution, nums4, 3, "示例1");
    
    vector<int> nums5 = {1, 2, 3, 1};
    testCase(solution, nums5, 4, "示例2");
    
    vector<int> nums6 = {1, 2, 3};
    testCase(solution, nums6, 3, "示例3");
    
    // 常规测试
    vector<int> nums7 = {1, 2, 3, 4, 5};
    testCase(solution, nums7, 8, "递增金额");
    
    vector<int> nums8 = {5, 4, 3, 2, 1};
    testCase(solution, nums8, 8, "递减金额");
    
    vector<int> nums9 = {2, 7, 9, 3, 1};
    testCase(solution, nums9, 11, "混合金额");
    
    // 性能测试
    cout << "\n=== 性能测试 ===" << endl;
    vector<int> largeNums(100);
    for (int i = 0; i < largeNums.size(); i++) {
        largeNums[i] = i % 10 + 1;  // 1-10的循环金额
    }
    performanceTest(solution, largeNums);
    
    return 0;
}