#include <iostream>
#include <vector>
#include <unordered_map>
#include <unordered_set>
#include <algorithm>
#include <climits>
#include <chrono>

/**
 * 最长等差数列 - LeetCode 1027
 * 题目来源：https://leetcode.cn/problems/longest-arithmetic-subsequence/
 * 难度：中等
 * 题目描述：给你一个整数数组 nums，返回 nums 中最长等差子序列的长度。
 * 注意：子序列是指从数组中删除一些元素（可以不删除任何元素）而不改变其余元素的顺序得到的序列。
 * 等差子序列是指元素之间的差值都相等的序列。
 * 
 * 核心思路：
 * 1. 这道题是LIS问题的变种，我们需要找到具有相同差值的最长子序列
 * 2. 使用动态规划+哈希表的方法：dp[i][d] 表示以nums[i]结尾且公差为d的最长等差数列长度
 * 3. 对于每个元素nums[i]，遍历之前的所有元素nums[j]，计算差值d = nums[i] - nums[j]，并更新dp[i][d]
 * 
 * 复杂度分析：
 * 时间复杂度：O(n²)，其中n是数组的长度，对于每个元素，我们需要遍历之前的所有元素
 * 空间复杂度：O(n²)，最坏情况下，每个元素对应的不同公差数量接近n
 */

class Solution {
public:
    /**
     * 最优解法：动态规划+哈希表
     * @param nums 整数数组
     * @return 最长等差子序列的长度
     */
    int longestArithSeqLength(const std::vector<int>& nums) {
        // 边界情况处理
        if (nums.size() <= 1) {
            return nums.size();
        }
        
        int n = nums.size();
        int maxLength = 2; // 至少有两个元素时，长度至少为2
        
        // 使用数组的哈希表来存储每个位置的公差对应的最长长度
        // dp[i] 表示以nums[i]结尾的所有可能公差对应的最长等差子序列长度
        std::vector<std::unordered_map<int, int>> dp(n);
        
        // 填充dp数组
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // 计算公差
                int diff = nums[i] - nums[j];
                
                // 如果dp[j]中存在公差为diff的记录，则dp[i][diff] = dp[j][diff] + 1
                // 否则，dp[i][diff] = 2（至少有nums[j]和nums[i]两个元素）
                auto it = dp[j].find(diff);
                int prevLength = (it != dp[j].end()) ? it->second : 1;
                dp[i][diff] = prevLength + 1;
                
                // 更新最大长度
                maxLength = std::max(maxLength, dp[i][diff]);
            }
        }
        
        return maxLength;
    }
    
    /**
     * 另一种实现方式，使用二维数组（仅当数值范围较小时适用）
     * @param nums 整数数组
     * @return 最长等差子序列的长度
     */
    int longestArithSeqLength2(const std::vector<int>& nums) {
        if (nums.size() <= 1) {
            return nums.size();
        }
        
        int n = nums.size();
        int maxLength = 2;
        
        // 找出数组中的最小值和最大值，确定可能的公差范围
        int minVal = INT_MAX, maxVal = INT_MIN;
        for (int num : nums) {
            minVal = std::min(minVal, num);
            maxVal = std::max(maxVal, num);
        }
        
        // 计算可能的最大公差范围
        int maxDiff = maxVal - minVal;
        
        // dp[i][d+maxDiff] 表示以nums[i]结尾且公差为d的最长等差数列长度
        // 加上maxDiff是为了避免负索引
        std::vector<std::vector<int>> dp(n, std::vector<int>(2 * maxDiff + 1, 1));
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                int diff = nums[i] - nums[j];
                // 将公差映射到非负索引
                int idx = diff + maxDiff;
                
                dp[i][idx] = dp[j][idx] + 1;
                maxLength = std::max(maxLength, dp[i][idx]);
            }
        }
        
        return maxLength;
    }
    
    /**
     * 暴力解法优化版：使用哈希表记录元素出现的位置
     * @param nums 整数数组
     * @return 最长等差子序列的长度
     */
    int longestArithSeqLengthBruteForce(const std::vector<int>& nums) {
        if (nums.size() <= 2) {
            return nums.size();
        }
        
        int n = nums.size();
        int maxLength = 2;
        
        // 使用哈希表存储每个值及其出现的索引列表
        std::unordered_map<int, std::vector<int>> valueToIndices;
        for (int i = 0; i < n; i++) {
            valueToIndices[nums[i]].push_back(i);
        }
        
        // 枚举所有可能的前两个元素
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int prev = nums[i];
                int curr = nums[j];
                int diff = curr - prev;
                int next = curr + diff;
                int length = 2;
                int currentJ = j;
                
                // 查找下一个元素
                while (valueToIndices.find(next) != valueToIndices.end()) {
                    // 找到在currentJ之后出现的next
                    bool found = false;
                    for (int idx : valueToIndices[next]) {
                        if (idx > currentJ) {
                            currentJ = idx;  // 更新currentJ为下一个元素的索引
                            prev = curr;
                            curr = next;
                            next = curr + diff;
                            length++;
                            found = true;
                            break;
                        }
                    }
                    
                    if (!found) {
                        break;
                    }
                }
                
                maxLength = std::max(maxLength, length);
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
void runAllSolutionsTest(const std::vector<int>& nums, Solution& solution) {
    std::cout << "\n对比测试：";
    printVector(nums);
    std::cout << std::endl;
    
    // 测试动态规划+哈希表解法
    auto start = std::chrono::high_resolution_clock::now();
    int result1 = solution.longestArithSeqLength(nums);
    auto end = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
    std::cout << "动态规划+哈希表解法结果: " << result1 << std::endl;
    std::cout << "耗时: " << duration << " μs" << std::endl;
    
    // 测试二维数组解法
    start = std::chrono::high_resolution_clock::now();
    int result2 = solution.longestArithSeqLength2(nums);
    end = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
    std::cout << "二维数组解法结果: " << result2 << std::endl;
    std::cout << "耗时: " << duration << " μs" << std::endl;
    
    // 对于小型数组，也测试暴力优化解法
    if (nums.size() <= 20) { // 避免大数组导致超时
        start = std::chrono::high_resolution_clock::now();
        int result3 = solution.longestArithSeqLengthBruteForce(nums);
        end = std::chrono::high_resolution_clock::now();
        duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
        std::cout << "暴力优化解法结果: " << result3 << std::endl;
        std::cout << "耗时: " << duration << " μs" << std::endl;
    } else {
        std::cout << "数组长度较大，跳过暴力优化解法测试" << std::endl;
    }
    
    std::cout << "----------------------------------------" << std::endl;
}

int main() {
    Solution solution;
    
    // 测试用例1
    std::vector<int> arr1 = {3, 6, 9, 12};
    std::cout << "测试用例1：" << std::endl;
    std::cout << "输入数组: ";
    printVector(arr1);
    std::cout << std::endl;
    std::cout << "结果: " << solution.longestArithSeqLength(arr1) << "，预期: 4" << std::endl;
    std::cout << std::endl;
    
    // 测试用例2
    std::vector<int> arr2 = {9, 4, 7, 2, 10};
    std::cout << "测试用例2：" << std::endl;
    std::cout << "输入数组: ";
    printVector(arr2);
    std::cout << std::endl;
    std::cout << "结果: " << solution.longestArithSeqLength(arr2) << "，预期: 3" << std::endl;
    std::cout << std::endl;
    
    // 测试用例3
    std::vector<int> arr3 = {20, 1, 15, 3, 10, 5, 8};
    std::cout << "测试用例3：" << std::endl;
    std::cout << "输入数组: ";
    printVector(arr3);
    std::cout << std::endl;
    std::cout << "结果: " << solution.longestArithSeqLength(arr3) << "，预期: 4" << std::endl;
    std::cout << std::endl;
    
    // 测试用例4：边界情况
    std::vector<int> arr4 = {1, 3, 5};
    std::cout << "测试用例4：" << std::endl;
    std::cout << "输入数组: ";
    printVector(arr4);
    std::cout << std::endl;
    std::cout << "结果: " << solution.longestArithSeqLength(arr4) << "，预期: 3" << std::endl;
    
    // 运行所有解法的对比测试
    runAllSolutionsTest(arr1, solution);
    runAllSolutionsTest(arr2, solution);
    runAllSolutionsTest(arr3, solution);
    runAllSolutionsTest(arr4, solution);
    
    return 0;
}