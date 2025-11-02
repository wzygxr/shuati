#include <iostream>
#include <vector>
#include <unordered_map>
#include <unordered_set>
#include <algorithm>
#include <chrono>

/**
 * 最长的斐波那契子序列的长度 - LeetCode 873
 * 题目来源：https://leetcode.cn/problems/length-of-longest-fibonacci-subsequence/
 * 难度：中等
 * 题目描述：如果序列 X_1, X_2, ..., X_n 满足下列条件，就说它是斐波那契式的：
 * n >= 3
 * 对于所有 i + 2 <= n，都有 X_i + X_{i+1} = X_{i+2}
 * 给定一个严格递增的正整数数组形成序列 arr ，找到 arr 中最长的斐波那契式的子序列的长度。
 * 如果没有这样的子序列，返回 0。
 * 子序列是指从原数组中删除一些元素（可以删除任何元素，包括零个元素），剩下的元素保持原来的顺序而不改变。
 * 
 * 核心思路：
 * 1. 这道题是LIS问题的变种，我们需要找到满足斐波那契关系的最长子序列
 * 2. 使用动态规划+哈希表的方法：dp[i][j] 表示以arr[i]和arr[j]结尾的最长斐波那契子序列的长度
 * 3. 对于每对(i,j)，我们查找arr[j]-arr[i]是否存在于数组中且索引小于i，如果存在则可以形成更长的子序列
 * 
 * 复杂度分析：
 * 时间复杂度：O(n²)，其中n是数组的长度，我们需要填充大小为n×n的dp数组
 * 空间复杂度：O(n²)，用于存储dp数组，以及O(n)用于哈希表存储值到索引的映射
 */

class Solution {
public:
    /**
     * 最优解法：动态规划+哈希表
     * @param arr 严格递增的正整数数组
     * @return 最长斐波那契子序列的长度，如果不存在返回0
     */
    int lenLongestFibSubseq(const std::vector<int>& arr) {
        // 边界情况处理
        if (arr.size() < 3) {
            return 0;
        }
        
        int n = arr.size();
        // 创建哈希表，存储值到索引的映射，用于快速查找
        std::unordered_map<int, int> valueToIndex;
        for (int i = 0; i < n; i++) {
            valueToIndex[arr[i]] = i;
        }
        
        // dp[i][j] 表示以arr[i]和arr[j]结尾的最长斐波那契子序列的长度
        // 初始化为2，表示至少有两个元素
        std::vector<std::vector<int>> dp(n, std::vector<int>(n, 2));
        int maxLength = 0;
        
        // 填充dp数组
        for (int j = 0; j < n; j++) {
            for (int k = j + 1; k < n; k++) {
                // 查找潜在的第一个元素 arr[i] = arr[k] - arr[j]
                int target = arr[k] - arr[j];
                // 确保target存在且严格小于arr[j]（因为数组严格递增）
                auto it = valueToIndex.find(target);
                if (target < arr[j] && it != valueToIndex.end()) {
                    int i = it->second;
                    // 更新dp[j][k]
                    dp[j][k] = dp[i][j] + 1;
                    // 更新最大长度
                    maxLength = std::max(maxLength, dp[j][k]);
                }
            }
        }
        
        // 如果maxLength至少为3，则返回，否则返回0
        return maxLength >= 3 ? maxLength : 0;
    }
    
    /**
     * 另一种动态规划解法，使用不同的遍历顺序
     * @param arr 严格递增的正整数数组
     * @return 最长斐波那契子序列的长度，如果不存在返回0
     */
    int lenLongestFibSubseqAlternative(const std::vector<int>& arr) {
        if (arr.size() < 3) {
            return 0;
        }
        
        int n = arr.size();
        std::unordered_map<int, int> map;
        for (int i = 0; i < n; i++) {
            map[arr[i]] = i;
        }
        
        std::vector<std::vector<int>> dp(n, std::vector<int>(n, 0));
        int maxLength = 0;
        
        // 遍历所有可能的三元组 (i,j,k) 其中 i < j < k
        for (int k = 2; k < n; k++) {
            for (int j = 1; j < k; j++) {
                int target = arr[k] - arr[j];
                auto it = map.find(target);
                if (it != map.end() && it->second < j) {
                    int i = it->second;
                    dp[j][k] = dp[i][j] + 1;
                    maxLength = std::max(maxLength, dp[j][k]);
                }
            }
        }
        
        // 如果存在斐波那契子序列，长度至少为3
        return maxLength > 0 ? maxLength + 2 : 0;
    }
    
    /**
     * 暴力解法（仅供对比，时间复杂度高）
     * @param arr 严格递增的正整数数组
     * @return 最长斐波那契子序列的长度，如果不存在返回0
     */
    int lenLongestFibSubseqBruteForce(const std::vector<int>& arr) {
        if (arr.size() < 3) {
            return 0;
        }
        
        int n = arr.size();
        std::unordered_set<int> set(arr.begin(), arr.end());
        
        int maxLength = 0;
        
        // 枚举所有可能的前两个元素
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int a = arr[i];
                int b = arr[j];
                int length = 2;
                int next = a + b;
                
                // 检查是否可以形成更长的斐波那契序列
                while (set.count(next)) {
                    a = b;
                    b = next;
                    next = a + b;
                    length++;
                }
                
                if (length >= 3) {
                    maxLength = std::max(maxLength, length);
                }
            }
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
void runAllSolutionsTest(const std::vector<int>& arr, Solution& solution) {
    std::cout << "\n对比测试：";
    printVector(arr);
    std::cout << std::endl;
    
    // 测试动态规划+哈希表解法
    auto start = std::chrono::high_resolution_clock::now();
    int result1 = solution.lenLongestFibSubseq(arr);
    auto end = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
    std::cout << "动态规划+哈希表解法结果: " << result1 << std::endl;
    std::cout << "耗时: " << duration << " μs" << std::endl;
    
    // 测试另一种动态规划解法
    start = std::chrono::high_resolution_clock::now();
    int result2 = solution.lenLongestFibSubseqAlternative(arr);
    end = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
    std::cout << "另一种动态规划解法结果: " << result2 << std::endl;
    std::cout << "耗时: " << duration << " μs" << std::endl;
    
    // 对于小型数组，也测试暴力解法
    if (arr.size() <= 20) { // 避免大数组导致超时
        start = std::chrono::high_resolution_clock::now();
        int result3 = solution.lenLongestFibSubseqBruteForce(arr);
        end = std::chrono::high_resolution_clock::now();
        duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
        std::cout << "暴力解法结果: " << result3 << std::endl;
        std::cout << "耗时: " << duration << " μs" << std::endl;
    } else {
        std::cout << "数组长度较大，跳过暴力解法测试" << std::endl;
    }
    
    std::cout << "----------------------------------------" << std::endl;
}

int main() {
    Solution solution;
    
    // 测试用例1
    std::vector<int> arr1 = {1, 2, 3, 4, 5, 6, 7, 8};
    std::cout << "测试用例1：" << std::endl;
    std::cout << "输入数组: ";
    printVector(arr1);
    std::cout << std::endl;
    std::cout << "结果: " << solution.lenLongestFibSubseq(arr1) << "，预期: 5" << std::endl;
    std::cout << std::endl;
    
    // 测试用例2
    std::vector<int> arr2 = {1, 3, 7, 11, 12, 14, 18};
    std::cout << "测试用例2：" << std::endl;
    std::cout << "输入数组: ";
    printVector(arr2);
    std::cout << std::endl;
    std::cout << "结果: " << solution.lenLongestFibSubseq(arr2) << "，预期: 3" << std::endl;
    std::cout << std::endl;
    
    // 测试用例3：边界情况
    std::vector<int> arr3 = {1, 2, 3};
    std::cout << "测试用例3：" << std::endl;
    std::cout << "输入数组: ";
    printVector(arr3);
    std::cout << std::endl;
    std::cout << "结果: " << solution.lenLongestFibSubseq(arr3) << "，预期: 3" << std::endl;
    std::cout << std::endl;
    
    // 测试用例4：没有斐波那契子序列
    std::vector<int> arr4 = {1, 4, 5};
    std::cout << "测试用例4：" << std::endl;
    std::cout << "输入数组: ";
    printVector(arr4);
    std::cout << std::endl;
    std::cout << "结果: " << solution.lenLongestFibSubseq(arr4) << "，预期: 0" << std::endl;
    
    // 运行所有解法的对比测试
    runAllSolutionsTest(arr1, solution);
    runAllSolutionsTest(arr2, solution);
    runAllSolutionsTest(arr3, solution);
    runAllSolutionsTest(arr4, solution);
    
    return 0;
}