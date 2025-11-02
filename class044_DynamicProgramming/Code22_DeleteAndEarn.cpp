// 删除并获得点数 (Delete and Earn)
// 给你一个整数数组 nums ，你可以对它进行一些操作。
// 每次操作中，选择任意一个 nums[i] ，删除它并获得 nums[i] 的点数。
// 之后，你必须删除所有等于 nums[i] - 1 和 nums[i] + 1 的元素。
// 开始你拥有 0 个点数。返回你能通过这些操作获得的最大点数。
// 测试链接 : https://leetcode.cn/problems/delete-and-earn/

#include <iostream>
#include <vector>
#include <algorithm>
#include <map>
#include <chrono>
using namespace std;

class Solution {
public:
    // 方法1：动态规划（转化为打家劫舍问题）
    // 时间复杂度：O(n + k) - n为数组长度，k为最大值
    // 空间复杂度：O(k) - 计数数组和dp数组
    // 核心思路：将问题转化为不能选择相邻数字的打家劫舍问题
    int deleteAndEarn1(vector<int>& nums) {
        if (nums.empty()) return 0;
        
        // 找到数组中的最大值
        int maxVal = 0;
        for (int num : nums) {
            maxVal = max(maxVal, num);
        }
        
        // 创建计数数组，统计每个数字出现的总点数
        vector<int> sum(maxVal + 1, 0);
        for (int num : nums) {
            sum[num] += num;
        }
        
        // 转化为打家劫舍问题：不能选择相邻的数字
        return robHouse(sum);
    }
    
    // 打家劫舍问题的解决方案
    int robHouse(vector<int>& sum) {
        int n = sum.size();
        if (n == 1) return sum[0];
        
        vector<int> dp(n);
        dp[0] = sum[0];
        dp[1] = max(sum[0], sum[1]);
        
        for (int i = 2; i < n; i++) {
            dp[i] = max(dp[i - 1], dp[i - 2] + sum[i]);
        }
        
        return dp[n - 1];
    }

    // 方法2：空间优化的动态规划
    // 时间复杂度：O(n + k) - 与方法1相同
    // 空间复杂度：O(k) - 只使用计数数组，dp使用常数空间
    // 优化：使用滚动数组减少空间使用
    int deleteAndEarn2(vector<int>& nums) {
        if (nums.empty()) return 0;
        
        int maxVal = 0;
        for (int num : nums) {
            maxVal = max(maxVal, num);
        }
        
        vector<int> sum(maxVal + 1, 0);
        for (int num : nums) {
            sum[num] += num;
        }
        
        return robHouseOptimized(sum);
    }
    
    int robHouseOptimized(vector<int>& sum) {
        int n = sum.size();
        if (n == 1) return sum[0];
        
        int prev2 = sum[0];  // dp[i-2]
        int prev1 = max(sum[0], sum[1]);  // dp[i-1]
        
        for (int i = 2; i < n; i++) {
            int current = max(prev1, prev2 + sum[i]);
            prev2 = prev1;
            prev1 = current;
        }
        
        return prev1;
    }

    // 方法3：使用map优化空间（当数字范围很大但实际数字很少时）
    // 时间复杂度：O(n log n) - 排序和遍历
    // 空间复杂度：O(n) - map存储
    // 核心思路：当数字范围很大但实际出现的数字很少时，避免创建大数组
    int deleteAndEarn3(vector<int>& nums) {
        if (nums.empty()) return 0;
        
        // 统计每个数字的总点数
        map<int, int> pointMap;
        for (int num : nums) {
            pointMap[num] += num;
        }
        
        // 如果没有数字，返回0
        if (pointMap.empty()) return 0;
        
        // 将数字按顺序排列
        vector<int> keys;
        for (auto& pair : pointMap) {
            keys.push_back(pair.first);
        }
        
        // 动态规划处理
        int n = keys.size();
        vector<int> dp(n);
        dp[0] = pointMap[keys[0]];
        
        for (int i = 1; i < n; i++) {
            int currentKey = keys[i];
            int currentValue = pointMap[currentKey];
            
            if (currentKey == keys[i - 1] + 1) {
                // 当前数字与前一个数字相邻
                if (i >= 2) {
                    dp[i] = max(dp[i - 1], dp[i - 2] + currentValue);
                } else {
                    dp[i] = max(dp[i - 1], currentValue);
                }
            } else {
                // 当前数字与前一个数字不相邻
                dp[i] = dp[i - 1] + currentValue;
            }
        }
        
        return dp[n - 1];
    }

    // 方法4：记忆化搜索（自顶向下）
    // 时间复杂度：O(n + k) - 与方法1相同
    // 空间复杂度：O(k) - 递归栈和记忆化数组
    // 核心思路：递归解决，使用记忆化避免重复计算
    int deleteAndEarn4(vector<int>& nums) {
        if (nums.empty()) return 0;
        
        int maxVal = 0;
        for (int num : nums) {
            maxVal = max(maxVal, num);
        }
        
        vector<int> sum(maxVal + 1, 0);
        for (int num : nums) {
            sum[num] += num;
        }
        
        vector<int> memo(maxVal + 1, -1);
        return dfs(sum, 1, memo);
    }
    
private:
    int dfs(vector<int>& sum, int i, vector<int>& memo) {
        if (i >= sum.size()) return 0;
        if (memo[i] != -1) return memo[i];
        
        // 选择1：不取当前数字，考虑下一个
        int skip = dfs(sum, i + 1, memo);
        // 选择2：取当前数字，跳过下一个（相邻数字）
        int take = sum[i] + dfs(sum, i + 2, memo);
        
        memo[i] = max(skip, take);
        return memo[i];
    }
};

// 测试函数
void testCase(Solution& solution, vector<int>& nums, int expected, const string& description) {
    int result1 = solution.deleteAndEarn1(nums);
    int result2 = solution.deleteAndEarn2(nums);
    int result3 = solution.deleteAndEarn3(nums);
    int result4 = solution.deleteAndEarn4(nums);
    
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
    int result1 = solution.deleteAndEarn1(nums);
    auto end = chrono::high_resolution_clock::now();
    auto duration1 = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "方法1: " << result1 << ", 耗时: " << duration1.count() << "μs" << endl;
    
    start = chrono::high_resolution_clock::now();
    int result2 = solution.deleteAndEarn2(nums);
    end = chrono::high_resolution_clock::now();
    auto duration2 = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "方法2: " << result2 << ", 耗时: " << duration2.count() << "μs" << endl;
}

int main() {
    Solution solution;
    
    cout << "=== 删除并获得点数测试 ===" << endl;
    
    // 边界测试
    vector<int> nums1 = {};
    testCase(solution, nums1, 0, "空数组");
    
    vector<int> nums2 = {5};
    testCase(solution, nums2, 5, "单元素数组");
    
    vector<int> nums3 = {3, 3};
    testCase(solution, nums3, 6, "重复元素");
    
    // LeetCode示例测试
    vector<int> nums4 = {3, 4, 2};
    testCase(solution, nums4, 6, "示例1");
    
    vector<int> nums5 = {2, 2, 3, 3, 3, 4};
    testCase(solution, nums5, 9, "示例2");
    
    vector<int> nums6 = {1, 1, 1, 2};
    testCase(solution, nums6, 3, "示例3");
    
    // 常规测试
    vector<int> nums7 = {1, 2, 3, 4, 5};
    testCase(solution, nums7, 9, "连续数字");
    
    vector<int> nums8 = {5, 5, 5, 5, 5};
    testCase(solution, nums8, 25, "全部相同");
    
    vector<int> nums9 = {1, 3, 5, 7, 9};
    testCase(solution, nums9, 25, "间隔数字");
    
    // 性能测试
    cout << "\n=== 性能测试 ===" << endl;
    vector<int> largeNums(1000);
    for (int i = 0; i < largeNums.size(); i++) {
        largeNums[i] = (i % 50) + 1;  // 1-50的循环数字
    }
    performanceTest(solution, largeNums);
    
    return 0;
}