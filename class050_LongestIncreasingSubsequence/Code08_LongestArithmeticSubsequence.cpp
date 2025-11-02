#include <iostream>
#include <vector>
#include <unordered_map>
#include <algorithm>
#include <string>
#include <chrono>

/**
 * 最长定差子序列 - LeetCode 1218
 * 题目来源：https://leetcode.cn/problems/longest-arithmetic-subsequence-of-given-difference/
 * 难度：中等
 * 题目描述：给你一个整数数组 arr 和一个整数 difference，请你找出并返回 arr 中最长等差子序列的长度，
 * 该子序列中相邻元素之间的差等于 difference。
 * 子序列 是指在不改变其余元素顺序的情况下，通过删除一些元素或不删除任何元素而从 arr 派生出来的序列。
 * 
 * 核心思路：
 * 1. 这道题是LIS问题的变种，但更特殊，因为我们需要的是固定差值的等差数列子序列
 * 2. 可以使用哈希表来优化动态规划的过程
 * 3. 对于每个元素arr[i]，我们需要查找是否存在arr[i] - difference这个元素，如果存在，则当前元素可以接在它后面形成更长的等差数列
 * 
 * 复杂度分析：
 * 时间复杂度：O(n)，其中n是数组的长度，我们只需要遍历一次数组，每次查询和更新哈希表的操作都是O(1)
 * 空间复杂度：O(n)，哈希表最多存储n个元素
 */

class Solution {
public:
    /**
     * 最优解法：使用哈希表优化动态规划
     * @param arr 输入数组
     * @param difference 固定差值
     * @return 最长等差子序列的长度
     */
    int longestSubsequence(const std::vector<int>& arr, int difference) {
        // 边界情况处理
        if (arr.empty()) {
            return 0;
        }
        
        // 使用哈希表存储每个数字最后出现时的最长子序列长度
        // key: 数字值, value: 以该数字结尾的最长等差子序列长度
        std::unordered_map<int, int> dp;
        
        int maxLength = 1; // 至少有一个元素
        
        // 遍历数组中的每个元素
        for (int num : arr) {
            // 查找前驱元素：num - difference
            int prev = num - difference;
            // 如果前驱元素存在，则当前元素可以接在它后面形成更长的子序列
            // 否则，当前元素自身形成一个长度为1的子序列
            auto it = dp.find(prev);
            int currentLength = (it != dp.end()) ? it->second + 1 : 1;
            
            // 更新当前元素的最长子序列长度
            auto currentIt = dp.find(num);
            if (currentIt == dp.end() || currentLength > currentIt->second) {
                dp[num] = currentLength;
            }
            
            // 更新全局最大长度
            maxLength = std::max(maxLength, currentLength);
        }
        
        return maxLength;
    }
    
    /**
     * 动态规划解法（未优化，用于对比）
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n)
     * @param arr 输入数组
     * @param difference 固定差值
     * @return 最长等差子序列的长度
     */
    int longestSubsequenceDP(const std::vector<int>& arr, int difference) {
        if (arr.empty()) {
            return 0;
        }
        
        int n = arr.size();
        std::vector<int> dp(n, 1); // 初始化每个元素长度为1
        
        int maxLength = 1;
        
        // 填充dp数组
        for (int i = 1; i < n; ++i) {
            for (int j = 0; j < i; ++j) {
                if (arr[i] - arr[j] == difference) {
                    dp[i] = std::max(dp[i], dp[j] + 1);
                }
            }
            maxLength = std::max(maxLength, dp[i]);
        }
        
        return maxLength;
    }
    
    /**
     * 使用哈希表优化的进阶解法 - 考虑到可能有重复元素
     * @param arr 输入数组
     * @param difference 固定差值
     * @return 最长等差子序列的长度
     */
    int longestSubsequenceOptimized(const std::vector<int>& arr, int difference) {
        if (arr.empty()) {
            return 0;
        }
        
        // 使用哈希表记录每个数字最后出现时的最长子序列长度
        std::unordered_map<int, int> dp;
        int maxLength = 1;
        
        for (int num : arr) {
            // 当前数字可以接在num - difference后面
            int prevLength = 0;
            auto it = dp.find(num - difference);
            if (it != dp.end()) {
                prevLength = it->second;
            }
            
            int currentLength = prevLength + 1;
            
            // 如果当前数字已经在哈希表中，且之前记录的长度更大，则不更新
            auto currIt = dp.find(num);
            if (currIt == dp.end() || currentLength > currIt->second) {
                dp[num] = currentLength;
            }
            
            maxLength = std::max(maxLength, currentLength);
        }
        
        return maxLength;
    }
};

// 辅助函数：打印数组
void printVector(const std::vector<int>& vec) {
    std::cout << "[";
    for (size_t i = 0; i < vec.size(); ++i) {
        std::cout << vec[i];
        if (i < vec.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]";
}

// 运行所有解法的对比测试
void runAllSolutionsTest(const std::vector<int>& arr, int difference, Solution& solution) {
    std::cout << "\n对比测试：";
    printVector(arr);
    std::cout << "，difference = " << difference << std::endl;
    
    // 测试哈希表优化解法
    auto start = std::chrono::high_resolution_clock::now();
    int result1 = solution.longestSubsequence(arr, difference);
    auto end = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
    std::cout << "哈希表优化解法结果: " << result1 << std::endl;
    std::cout << "耗时: " << duration << " μs" << std::endl;
    
    // 测试优化进阶解法
    start = std::chrono::high_resolution_clock::now();
    int result3 = solution.longestSubsequenceOptimized(arr, difference);
    end = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
    std::cout << "进阶优化解法结果: " << result3 << std::endl;
    std::cout << "耗时: " << duration << " μs" << std::endl;
    
    // 对于小型数组，也测试O(n²)的DP解法
    if (arr.size() <= 1000) { // 避免大数组导致超时
        start = std::chrono::high_resolution_clock::now();
        int result2 = solution.longestSubsequenceDP(arr, difference);
        end = std::chrono::high_resolution_clock::now();
        duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
        std::cout << "传统DP解法结果: " << result2 << std::endl;
        std::cout << "耗时: " << duration << " μs" << std::endl;
    } else {
        std::cout << "数组长度较大，跳过传统DP解法测试" << std::endl;
    }
    
    std::cout << "----------------------------------------" << std::endl;
}

int main() {
    Solution solution;
    
    // 测试用例1
    std::vector<int> arr1 = {1, 2, 3, 4};
    int difference1 = 1;
    std::cout << "测试用例1：" << std::endl;
    std::cout << "输入数组: ";
    printVector(arr1);
    std::cout << ", difference: " << difference1 << std::endl;
    std::cout << "结果: " << solution.longestSubsequence(arr1, difference1) << "，预期: 4" << std::endl;
    std::cout << std::endl;
    
    // 测试用例2
    std::vector<int> arr2 = {1, 3, 5, 7};
    int difference2 = 1;
    std::cout << "测试用例2：" << std::endl;
    std::cout << "输入数组: ";
    printVector(arr2);
    std::cout << ", difference: " << difference2 << std::endl;
    std::cout << "结果: " << solution.longestSubsequence(arr2, difference2) << "，预期: 1" << std::endl;
    std::cout << std::endl;
    
    // 测试用例3
    std::vector<int> arr3 = {1, 5, 7, 8, 5, 3, 4, 2, 1};
    int difference3 = -2;
    std::cout << "测试用例3：" << std::endl;
    std::cout << "输入数组: ";
    printVector(arr3);
    std::cout << ", difference: " << difference3 << std::endl;
    std::cout << "结果: " << solution.longestSubsequence(arr3, difference3) << "，预期: 4" << std::endl;
    std::cout << std::endl;
    
    // 测试用例4：边界情况
    std::vector<int> arr4 = {1};
    int difference4 = 0;
    std::cout << "测试用例4：" << std::endl;
    std::cout << "输入数组: ";
    printVector(arr4);
    std::cout << ", difference: " << difference4 << std::endl;
    std::cout << "结果: " << solution.longestSubsequence(arr4, difference4) << "，预期: 1" << std::endl;
    std::cout << std::endl;
    
    // 测试用例5：负数差值
    std::vector<int> arr5 = {3, 0, -3, 4, -4, 7, 6};
    int difference5 = 3;
    std::cout << "测试用例5：" << std::endl;
    std::cout << "输入数组: ";
    printVector(arr5);
    std::cout << ", difference: " << difference5 << std::endl;
    std::cout << "结果: " << solution.longestSubsequence(arr5, difference5) << "，预期: 2" << std::endl;
    
    // 运行所有解法的对比测试
    runAllSolutionsTest(arr1, difference1, solution);
    runAllSolutionsTest(arr2, difference2, solution);
    runAllSolutionsTest(arr3, difference3, solution);
    runAllSolutionsTest(arr4, difference4, solution);
    runAllSolutionsTest(arr5, difference5, solution);
    
    return 0;
}