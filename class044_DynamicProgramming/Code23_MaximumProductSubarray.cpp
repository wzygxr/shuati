// 乘积最大子数组 (Maximum Product Subarray)
// 给你一个整数数组 nums ，请你找出数组中乘积最大的非空连续子数组（该子数组中至少包含一个数字），并返回该子数组所对应的乘积。
// 测试链接 : https://leetcode.cn/problems/maximum-product-subarray/

#include <vector>
#include <algorithm>
#include <climits>
#include <chrono>
#include <iostream>
#include <string>
using namespace std;

class Solution {
public:
    // 方法1：动态规划（同时维护最大值和最小值）
    // 时间复杂度：O(n) - 遍历数组一次
    // 空间复杂度：O(n) - 使用两个dp数组
    // 核心思路：由于存在负数，最小值可能变成最大值，需要同时维护
    int maxProduct1(vector<int>& nums) {
        if (nums.empty()) return 0;
        
        int n = nums.size();
        vector<int> maxDp(n);  // 存储以i结尾的最大乘积
        vector<int> minDp(n);  // 存储以i结尾的最小乘积
        
        maxDp[0] = nums[0];
        minDp[0] = nums[0];
        int result = nums[0];
        
        for (int i = 1; i < n; i++) {
            // 三种可能：当前数字、当前数字×最大乘积、当前数字×最小乘积
            int num = nums[i];
            int option1 = num;
            int option2 = num * maxDp[i - 1];
            int option3 = num * minDp[i - 1];
            
            maxDp[i] = max(option1, max(option2, option3));
            minDp[i] = min(option1, min(option2, option3));
            
            result = max(result, maxDp[i]);
        }
        
        return result;
    }

    // 方法2：空间优化的动态规划
    // 时间复杂度：O(n) - 与方法1相同
    // 空间复杂度：O(1) - 只使用常数空间
    // 优化：使用变量代替数组，减少空间使用
    int maxProduct2(vector<int>& nums) {
        if (nums.empty()) return 0;
        
        int n = nums.size();
        int maxSoFar = nums[0];  // 当前最大乘积
        int minSoFar = nums[0];  // 当前最小乘积
        int result = nums[0];
        
        for (int i = 1; i < n; i++) {
            int num = nums[i];
            int tempMax = maxSoFar;  // 保存之前的值，避免覆盖
            
            // 更新最大值和最小值
            maxSoFar = max(num, max(num * maxSoFar, num * minSoFar));
            minSoFar = min(num, min(num * tempMax, num * minSoFar));
            
            result = max(result, maxSoFar);
        }
        
        return result;
    }

    // 方法3：分治解法（用于对比）
    // 时间复杂度：O(n log n) - 分治递归
    // 空间复杂度：O(log n) - 递归栈深度
    // 核心思路：将数组分成左右两部分，最大乘积可能在左、右或跨越中间
    int maxProduct3(vector<int>& nums) {
        if (nums.empty()) return 0;
        return divideAndConquer(nums, 0, nums.size() - 1);
    }
    
private:
    int divideAndConquer(vector<int>& nums, int left, int right) {
        if (left == right) return nums[left];
        
        int mid = left + (right - left) / 2;
        
        // 左半部分的最大乘积
        int leftMax = divideAndConquer(nums, left, mid);
        // 右半部分的最大乘积
        int rightMax = divideAndConquer(nums, mid + 1, right);
        // 跨越中间的最大乘积
        int crossMax = maxCrossingProduct(nums, left, mid, right);
        
        return max(leftMax, max(rightMax, crossMax));
    }
    
    int maxCrossingProduct(vector<int>& nums, int left, int mid, int right) {
        // 从左到右计算包含mid的最大乘积
        int leftMax = nums[mid];
        int leftMin = nums[mid];
        int product = nums[mid];
        
        for (int i = mid - 1; i >= left; i--) {
            product *= nums[i];
            leftMax = max(leftMax, product);
            leftMin = min(leftMin, product);
        }
        
        // 从右到左计算包含mid+1的最大乘积
        int rightMax = nums[mid + 1];
        int rightMin = nums[mid + 1];
        product = nums[mid + 1];
        
        for (int i = mid + 2; i <= right; i++) {
            product *= nums[i];
            rightMax = max(rightMax, product);
            rightMin = min(rightMin, product);
        }
        
        // 跨越中间的最大乘积可能是各种组合
        return max(leftMax * rightMax, max(leftMax * rightMin, 
                max(leftMin * rightMax, leftMin * rightMin)));
    }

public:
    // 方法4：暴力解法（用于对比）
    // 时间复杂度：O(n^2) - 枚举所有子数组
    // 空间复杂度：O(1) - 只保存当前最大值
    // 问题：效率低，仅用于教学目的
    int maxProduct4(vector<int>& nums) {
        if (nums.empty()) return 0;
        
        int n = nums.size();
        int result = INT_MIN;
        
        for (int i = 0; i < n; i++) {
            int product = 1;
            for (int j = i; j < n; j++) {
                product *= nums[j];
                result = max(result, product);
            }
        }
        
        return result;
    }

    // 方法5：前缀积解法（处理0的特殊情况）
    // 时间复杂度：O(n) - 遍历数组两次
    // 空间复杂度：O(1) - 只使用常数空间
    // 核心思路：计算前缀积，遇到0时重置
    int maxProduct5(vector<int>& nums) {
        if (nums.empty()) return 0;
        
        int n = nums.size();
        int result = nums[0];
        int product = 1;
        
        // 从左到右计算
        for (int i = 0; i < n; i++) {
            product *= nums[i];
            result = max(result, product);
            if (nums[i] == 0) {
                product = 1;  // 遇到0重置
            }
        }
        
        // 从右到左计算（处理负数情况）
        product = 1;
        for (int i = n - 1; i >= 0; i--) {
            product *= nums[i];
            result = max(result, product);
            if (nums[i] == 0) {
                product = 1;  // 遇到0重置
            }
        }
        
        return result;
    }
};

// 测试函数
void testCase(Solution& solution, vector<int>& nums, int expected, const string& description) {
    int result1 = solution.maxProduct1(nums);
    int result2 = solution.maxProduct2(nums);
    int result3 = solution.maxProduct3(nums);
    int result5 = solution.maxProduct5(nums);
    
    bool allCorrect = (result1 == expected && result2 == expected && 
                      result3 == expected && result5 == expected);
    
    cout << description << ": " << (allCorrect ? "✓" : "✗");
    if (!allCorrect) {
        cout << " 方法1:" << result1 << " 方法2:" << result2 
             << " 方法3:" << result3 << " 方法5:" << result5 
             << " 预期:" << expected;
    }
    cout << endl;
}

// 性能测试函数
void performanceTest(Solution& solution, vector<int>& nums) {
    cout << "性能测试 n=" << nums.size() << ":" << endl;
    
    auto start = chrono::high_resolution_clock::now();
    int result2 = solution.maxProduct2(nums);
    auto end = chrono::high_resolution_clock::now();
    auto duration2 = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "空间优化方法: " << result2 << ", 耗时: " << duration2.count() << "μs" << endl;
    
    start = chrono::high_resolution_clock::now();
    int result5 = solution.maxProduct5(nums);
    end = chrono::high_resolution_clock::now();
    auto duration5 = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "前缀积方法: " << result5 << ", 耗时: " << duration5.count() << "μs" << endl;
}

int main() {
    Solution solution;
    
    cout << "=== 乘积最大子数组测试 ===" << endl;
    
    // 边界测试
    vector<int> nums1 = {};
    testCase(solution, nums1, 0, "空数组");
    
    vector<int> nums2 = {5};
    testCase(solution, nums2, 5, "单元素数组");
    
    vector<int> nums3 = {-5};
    testCase(solution, nums3, -5, "单负数元素");
    
    // LeetCode示例测试
    vector<int> nums4 = {2, 3, -2, 4};
    testCase(solution, nums4, 6, "示例1");
    
    vector<int> nums5 = {-2, 0, -1};
    testCase(solution, nums5, 0, "示例2");
    
    vector<int> nums6 = {-2, 3, -4};
    testCase(solution, nums6, 24, "示例3");
    
    // 常规测试
    vector<int> nums7 = {1, 2, 3, 4, 5};
    testCase(solution, nums7, 120, "全正数");
    
    vector<int> nums8 = {-1, -2, -3, -4, -5};
    testCase(solution, nums8, 120, "全负数（偶数个）");
    
    vector<int> nums9 = {-1, -2, -3, -4};
    testCase(solution, nums9, 24, "全负数（奇数个）");
    
    // 包含0的测试
    vector<int> nums10 = {2, 0, 3, 4};
    testCase(solution, nums10, 12, "包含0");
    
    vector<int> nums11 = {-2, 0, 3, 4};
    testCase(solution, nums11, 12, "负数后接0");
    
    vector<int> nums12 = {0, 0, 0, 5};
    testCase(solution, nums12, 5, "多个0");
    
    // 性能测试
    cout << "\n=== 性能测试 ===" << endl;
    vector<int> largeNums(1000);
    for (int i = 0; i < largeNums.size(); i++) {
        largeNums[i] = (i % 10) - 5;  // -5到4的循环数字
    }
    performanceTest(solution, largeNums);
    
    return 0;
}