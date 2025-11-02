#include <iostream>
#include <vector>
#include <unordered_map>
#include <algorithm>
#include <chrono>

/**
 * 等差数列划分 II - 子序列 - LeetCode 446
 * 题目来源：https://leetcode.cn/problems/arithmetic-slices-ii-subsequence/
 * 难度：困难
 * 题目描述：给你一个整数数组 nums，请你返回所有长度至少为 3 的等差子序列的数目。
 * 注意：子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。
 * 另外，子序列中的元素在原数组中可能不连续，但等差子序列需要满足元素之间的差值相等。
 * 
 * 核心思路：
 * 1. 这道题是LIS问题的变种，我们需要计算所有可能的等差数列子序列数目
 * 2. 使用动态规划+哈希表的方法：dp[i][d] 表示以nums[i]结尾且公差为d的等差数列子序列的数目（至少有两个元素）
 * 3. 对于每个元素nums[i]，遍历之前的所有元素nums[j]，计算差值d = nums[i] - nums[j]，并更新dp[i][d] += dp[j][d] + 1
 * 4. 其中+1表示nums[j]和nums[i]形成的新的二元组，dp[j][d]表示可以接在已有等差序列后面的数目
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
     * @return 所有长度至少为3的等差子序列的数目
     */
    int numberOfArithmeticSlices(std::vector<int>& nums) {
        // 边界情况处理
        if (nums.size() < 3) {
            return 0;
        }
        
        int n = nums.size();
        int total = 0; // 记录所有长度至少为3的等差子序列数目
        
        // dp[i] 是一个哈希表，键为公差，值为以nums[i]结尾且具有该公差的等差子序列数目（至少有两个元素）
        std::vector<std::unordered_map<long long, int>> dp(n);
        
        // 填充dp数组
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // 计算公差，注意可能会溢出，所以使用long long
                long long diff = static_cast<long long>(nums[i]) - nums[j];
                
                // 获取以nums[j]结尾且公差为diff的等差子序列数目
                auto it = dp[j].find(diff);
                int countJ = (it != dp[j].end()) ? it->second : 0;
                
                // 以nums[i]结尾且公差为diff的等差子序列数目 = 
                // 以nums[j]结尾且公差为diff的等差子序列数目（将nums[i]添加到这些序列后面） + 1（nums[j]和nums[i]形成的新二元组）
                dp[i][diff] += countJ + 1;
                
                // 只有当countJ >= 1时，才能形成长度至少为3的等差子序列
                // 因为countJ表示以nums[j]结尾且公差为diff的等差子序列数目（至少有两个元素）
                // 所以将nums[i]添加到这些序列后面，就形成了长度至少为3的等差子序列
                total += countJ;
            }
        }
        
        return total;
    }
    
    /**
     * 另一种实现方式，逻辑相同但写法略有不同
     * @param nums 整数数组
     * @return 所有长度至少为3的等差子序列的数目
     */
    int numberOfArithmeticSlicesAlternative(std::vector<int>& nums) {
        if (nums.size() < 3) {
            return 0;
        }
        
        int n = nums.size();
        int total = 0;
        
        // 使用vector的unordered_map存储状态
        std::vector<std::unordered_map<long long, int>> dp(n);
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // 计算公差
                long long diff = static_cast<long long>(nums[i]) - nums[j];
                
                // 从dp[j]中获取公差为diff的序列数目
                int prevCount = 0;
                auto it = dp[j].find(diff);
                if (it != dp[j].end()) {
                    prevCount = it->second;
                }
                
                // 更新dp[i]中的状态
                dp[i][diff] += prevCount + 1;
                
                // 累加可以形成长度>=3的子序列数目
                total += prevCount;
            }
        }
        
        return total;
    }
    
    /**
     * 解释性更强的版本，添加了详细的中间变量说明
     * @param nums 整数数组
     * @return 所有长度至少为3的等差子序列的数目
     */
    int numberOfArithmeticSlicesExplained(std::vector<int>& nums) {
        if (nums.size() < 3) {
            return 0;
        }
        
        int n = nums.size();
        int result = 0;
        
        // dp[i][d]表示以nums[i]结尾，公差为d的等差子序列的数量（至少包含两个元素）
        std::vector<std::unordered_map<long long, int>> dp(n);
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // 计算公差，注意数据范围
                long long diff = static_cast<long long>(nums[i]) - nums[j];
                
                // 获取以nums[j]结尾且公差为diff的等差子序列数目
                int sequencesEndingAtJ = 0;
                auto it = dp[j].find(diff);
                if (it != dp[j].end()) {
                    sequencesEndingAtJ = it->second;
                }
                
                // 新的序列数目：已有的序列数目 + 1（nums[j], nums[i]这个新的二元组）
                int newSequencesCount = sequencesEndingAtJ + 1;
                
                // 更新dp[i][diff]
                dp[i][diff] += newSequencesCount;
                
                // 对于每个以nums[j]结尾且公差为diff的序列，加上nums[i]后就形成了一个长度至少为3的序列
                // 因此，将sequencesEndingAtJ加到结果中
                result += sequencesEndingAtJ;
            }
        }
        
        return result;
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
void runAllSolutionsTest(const std::vector<int>& nums) {
    Solution solution;
    std::cout << "\n对比测试：";
    printVector(nums);
    std::cout << std::endl;
    
    // 测试动态规划+哈希表解法
    auto start = std::chrono::high_resolution_clock::now();
    int result1 = solution.numberOfArithmeticSlices(nums);
    auto end = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
    std::cout << "动态规划+哈希表解法结果: " << result1 << std::endl;
    std::cout << "耗时: " << duration << " μs" << std::endl;
    
    // 测试另一种实现方式
    start = std::chrono::high_resolution_clock::now();
    int result2 = solution.numberOfArithmeticSlicesAlternative(nums);
    end = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
    std::cout << "另一种实现方式结果: " << result2 << std::endl;
    std::cout << "耗时: " << duration << " μs" << std::endl;
    
    // 测试解释性更强的版本
    start = std::chrono::high_resolution_clock::now();
    int result3 = solution.numberOfArithmeticSlicesExplained(nums);
    end = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
    std::cout << "解释性版本结果: " << result3 << std::endl;
    std::cout << "耗时: " << duration << " μs" << std::endl;
    
    std::cout << "----------------------------------------" << std::endl;
}

// 性能测试函数
void performanceTest(int n) {
    Solution solution;
    // 生成随机测试数据
    std::vector<int> nums(n);
    for (int i = 0; i < n; i++) {
        nums[i] = rand() % 1000;
    }
    
    std::cout << "\n性能测试：数组长度 = " << n << std::endl;
    
    // 测试动态规划+哈希表解法
    auto start = std::chrono::high_resolution_clock::now();
    int result1 = solution.numberOfArithmeticSlices(nums);
    auto end = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
    std::cout << "动态规划+哈希表解法耗时: " << duration << " ms, 结果: " << result1 << std::endl;
}

int main() {
    Solution solution;
    
    // 测试用例1
    std::vector<int> arr1 = {2, 4, 6, 8, 10};
    std::cout << "测试用例1：" << std::endl;
    std::cout << "输入数组: ";
    printVector(arr1);
    std::cout << std::endl;
    std::cout << "结果: " << solution.numberOfArithmeticSlices(arr1) << "，预期: 7" << std::endl;
    std::cout << std::endl;
    
    // 测试用例2
    std::vector<int> arr2 = {7, 7, 7, 7, 7};
    std::cout << "测试用例2：" << std::endl;
    std::cout << "输入数组: ";
    printVector(arr2);
    std::cout << std::endl;
    std::cout << "结果: " << solution.numberOfArithmeticSlices(arr2) << "，预期: 16" << std::endl;
    std::cout << std::endl;
    
    // 测试用例3：边界情况
    std::vector<int> arr3 = {1, 2, 3};
    std::cout << "测试用例3：" << std::endl;
    std::cout << "输入数组: ";
    printVector(arr3);
    std::cout << std::endl;
    std::cout << "结果: " << solution.numberOfArithmeticSlices(arr3) << "，预期: 1" << std::endl;
    
    // 运行所有解法的对比测试
    runAllSolutionsTest(arr1);
    runAllSolutionsTest(arr2);
    runAllSolutionsTest(arr3);
    
    // 性能测试
    std::cout << "性能测试:" << std::endl;
    std::cout << "----------------------------------------" << std::endl;
    performanceTest(100);
    performanceTest(200);
    
    // 特殊测试用例：完全相同的元素
    std::cout << "\n特殊测试用例：完全相同的元素" << std::endl;
    std::vector<int> arrSame = {5, 5, 5, 5, 5};
    std::cout << "输入数组: ";
    printVector(arrSame);
    std::cout << std::endl;
    std::cout << "结果: " << solution.numberOfArithmeticSlices(arrSame) << std::endl;
    
    // 特殊测试用例：降序数组
    std::cout << "\n特殊测试用例：降序数组" << std::endl;
    std::vector<int> arrDesc = {10, 8, 6, 4, 2};
    std::cout << "输入数组: ";
    printVector(arrDesc);
    std::cout << std::endl;
    std::cout << "结果: " << solution.numberOfArithmeticSlices(arrDesc) << std::endl;
    
    return 0;
}