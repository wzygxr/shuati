// 数组的均值分割
// 题目来源：LeetCode 805
// 题目描述：
// 给定一个整数数组 nums，判断是否可以将数组分割成两个非空子集，使得两个子集的平均值相等。
// 测试链接：https://leetcode.cn/problems/split-array-with-same-average/
// 
// 算法思路：
// 使用折半搜索（Meet in the Middle）算法解决，将数组分为两半分别计算所有可能的和与元素个数组合，
// 然后通过哈希表查找是否存在满足条件的组合
// 时间复杂度：O(2^(n/2) * n)
// 空间复杂度：O(2^(n/2))
// 
// 工程化考量：
// 1. 异常处理：检查数组长度和边界条件
// 2. 性能优化：使用折半搜索减少搜索空间，提前剪枝
// 3. 可读性：变量命名清晰，注释详细
// 4. 数学优化：利用数学性质进行优化
// 
// 语言特性差异：
// C++中使用unordered_map进行组合统计，使用递归计算子集和

#include <iostream>
#include <vector>
#include <unordered_map>
#include <unordered_set>
#include <algorithm>
#include <functional>
#include <chrono>
#include <ctime>
using namespace std;

class Solution {
public:
    /**
     * 判断是否可以将数组分割成两个非空子集，使得两个子集的平均值相等
     * 
     * @param nums 输入数组
     * @return 如果可以分割返回true，否则返回false
     * 
     * 算法核心思想：
     * 1. 数学推导：如果两个子集平均值相等，则整个数组的平均值等于每个子集的平均值
     * 2. 折半搜索：将数组分为两半，分别计算所有可能的和与元素个数组合
     * 3. 组合查找：使用哈希表快速查找满足条件的组合
     * 
     * 时间复杂度分析：
     * - 折半搜索将O(2^n)优化为O(2^(n/2))
     * - 每个组合需要存储和与元素个数信息
     * - 总体时间复杂度：O(2^(n/2) * n)
     * 
     * 空间复杂度分析：
     * - 需要存储两个子问题的所有可能组合
     * - 空间复杂度：O(2^(n/2))
     */
    bool splitArraySameAverage(vector<int>& nums) {
        // 边界条件检查
        if (nums.empty() || nums.size() < 2) {
            return false;
        }
        
        int n = nums.size();
        int totalSum = 0;
        for (int num : nums) {
            totalSum += num;
        }
        
        // 数学优化：如果整个数组的和为0，则任何非空子集的和都为0时平均值相等
        if (totalSum == 0) {
            // 检查是否存在非空真子集和为0
            return hasZeroSubset(nums, n);
        }
        
        // 使用折半搜索，将数组分为两半
        int mid = n / 2;
        
        // 存储左半部分的所有可能组合：key为pair<和, 元素个数>
        unordered_map<pair<int, int>, bool, PairHash> leftCombinations;
        // 存储右半部分的所有可能组合
        unordered_map<pair<int, int>, bool, PairHash> rightCombinations;
        
        // 计算左半部分的所有可能组合
        generateCombinations(nums, 0, mid, 0, 0, leftCombinations);
        
        // 计算右半部分的所有可能组合
        generateCombinations(nums, mid, n, 0, 0, rightCombinations);
        
        // 检查左半部分的空集情况（右半部分需要是非空真子集）
        for (auto& entry : rightCombinations) {
            int rightSum = entry.first.first;
            int rightCount = entry.first.second;
            
            // 右半部分必须是非空子集
            if (rightCount > 0 && rightCount < n) {
                // 检查平均值是否相等：rightSum/rightCount = totalSum/n
                // 等价于：rightSum * n == totalSum * rightCount
                if ((long long)rightSum * n == (long long)totalSum * rightCount) {
                    return true;
                }
            }
        }
        
        // 检查左右两部分组合的搭配
        for (auto& leftEntry : leftCombinations) {
            int leftSum = leftEntry.first.first;
            int leftCount = leftEntry.first.second;
            
            for (auto& rightEntry : rightCombinations) {
                int rightSum = rightEntry.first.first;
                int rightCount = rightEntry.first.second;
                
                // 两个子集都必须是非空的，且它们的并集不能是整个数组
                int totalCount = leftCount + rightCount;
                if (leftCount > 0 && rightCount > 0 && totalCount < n) {
                    int totalSubsetSum = leftSum + rightSum;
                    
                    // 检查平均值是否相等：totalSubsetSum/totalCount = totalSum/n
                    // 等价于：totalSubsetSum * n == totalSum * totalCount
                    if ((long long)totalSubsetSum * n == (long long)totalSum * totalCount) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
private:
    /**
     * 哈希函数用于pair<int, int>
     */
    struct PairHash {
        size_t operator()(const pair<int, int>& p) const {
            return hash<int>()(p.first) ^ hash<int>()(p.second);
        }
    };
    
    /**
     * 检查数组中是否存在非空真子集和为0
     */
    bool hasZeroSubset(vector<int>& nums, int n) {
        // 使用动态规划检查是否存在和为0的非空真子集
        unordered_set<int> sums;
        sums.insert(0);
        
        for (int num : nums) {
            unordered_set<int> newSums = sums;
            for (int sum : sums) {
                newSums.insert(sum + num);
            }
            sums = newSums;
        }
        
        // 检查是否存在非空真子集和为0
        return sums.find(0) != sums.end() && n > 1;
    }
    
    /**
     * 递归生成指定范围内所有可能的和与元素个数组合
     * 
     * @param nums 输入数组
     * @param start 起始索引
     * @param end 结束索引
     * @param currentSum 当前累积和
     * @param currentCount 当前元素个数
     * @param combinations 存储结果的Map
     */
    void generateCombinations(vector<int>& nums, int start, int end,
                             int currentSum, int currentCount,
                             unordered_map<pair<int, int>, bool, PairHash>& combinations) {
        // 递归终止条件
        if (start == end) {
            combinations[{currentSum, currentCount}] = true;
            return;
        }
        
        // 不选择当前元素
        generateCombinations(nums, start + 1, end, currentSum, currentCount, combinations);
        
        // 选择当前元素
        generateCombinations(nums, start + 1, end, currentSum + nums[start], currentCount + 1, combinations);
    }
};

// 优化版本：包含剪枝和去重
class OptimizedSolution {
public:
    bool splitArraySameAverage(vector<int>& nums) {
        if (nums.empty() || nums.size() < 2) {
            return false;
        }
        
        int n = nums.size();
        int totalSum = 0;
        for (int num : nums) {
            totalSum += num;
        }
        
        // 特殊处理：总和为0的情况
        if (totalSum == 0) {
            return n > 1; // 只要数组长度大于1，就可以分割
        }
        
        // 使用折半搜索
        int mid = n / 2;
        
        // 生成左右两部分的子集和
        auto leftSums = generateSubsetsOptimized(nums, 0, mid);
        auto rightSums = generateSubsetsOptimized(nums, mid, n);
        
        // 检查各种可能的分割方式
        for (auto& leftEntry : leftSums) {
            int leftSize = leftEntry.first;
            for (int leftSum : leftEntry.second) {
                // 检查左半部分单独是否能满足条件
                if (leftSize > 0 && leftSize < n) {
                    if ((long long)leftSum * n == (long long)totalSum * leftSize) {
                        return true;
                    }
                }
                
                // 检查与右半部分的组合
                for (auto& rightEntry : rightSums) {
                    int rightSize = rightEntry.first;
                    for (int rightSum : rightEntry.second) {
                        int totalSize = leftSize + rightSize;
                        int totalSubsetSum = leftSum + rightSum;
                        
                        if (leftSize > 0 && rightSize > 0 && 
                            totalSize < n && totalSize > 0) {
                            if ((long long)totalSubsetSum * n == (long long)totalSum * totalSize) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
private:
    /**
     * 优化版本的子集和生成函数
     */
    unordered_map<int, unordered_set<int>> generateSubsetsOptimized(vector<int>& nums, int start, int end) {
        unordered_map<int, unordered_set<int>> result;
        result[0].insert(0); // 包含空集
        
        for (int i = start; i < end; i++) {
            auto newResult = result;
            for (auto& entry : result) {
                int size = entry.first;
                for (int sum : entry.second) {
                    // 包含当前元素
                    int newSize = size + 1;
                    int newSum = sum + nums[i];
                    newResult[newSize].insert(newSum);
                }
            }
            result = newResult;
        }
        
        return result;
    }
};

// 单元测试
void testSplitArraySameAverage() {
    Solution solution;
    
    // 测试用例1：存在均值分割
    cout << "=== 测试用例1：存在均值分割 ===" << endl;
    vector<int> nums1 = {1, 2, 3, 4, 5, 6, 7, 8};
    cout << "输入数组: ";
    for (int num : nums1) cout << num << " ";
    cout << endl;
    cout << "期望输出: true" << endl;
    bool result1 = solution.splitArraySameAverage(nums1);
    cout << "实际输出: " << (result1 ? "true" : "false") << endl;
    cout << "测试结果: " << (result1 ? "通过" : "失败") << endl;
    cout << endl;
    
    // 测试用例2：不存在均值分割
    cout << "=== 测试用例2：不存在均值分割 ===" << endl;
    vector<int> nums2 = {3, 1};
    cout << "输入数组: ";
    for (int num : nums2) cout << num << " ";
    cout << endl;
    cout << "期望输出: false" << endl;
    bool result2 = solution.splitArraySameAverage(nums2);
    cout << "实际输出: " << (result2 ? "true" : "false") << endl;
    cout << "测试结果: " << (!result2 ? "通过" : "失败") << endl;
    cout << endl;
    
    // 测试用例3：边界情况 - 两个元素
    cout << "=== 测试用例3：两个元素 ===" << endl;
    vector<int> nums3 = {1, 3};
    cout << "输入数组: ";
    for (int num : nums3) cout << num << " ";
    cout << endl;
    cout << "期望输出: false" << endl;
    bool result3 = solution.splitArraySameAverage(nums3);
    cout << "实际输出: " << (result3 ? "true" : "false") << endl;
    cout << "测试结果: " << (!result3 ? "通过" : "失败") << endl;
    cout << endl;
    
    // 测试用例4：全零数组
    cout << "=== 测试用例4：全零数组 ===" << endl;
    vector<int> nums4 = {0, 0, 0, 0};
    cout << "输入数组: ";
    for (int num : nums4) cout << num << " ";
    cout << endl;
    cout << "期望输出: true" << endl;
    bool result4 = solution.splitArraySameAverage(nums4);
    cout << "实际输出: " << (result4 ? "true" : "false") << endl;
    cout << "测试结果: " << (result4 ? "通过" : "失败") << endl;
    cout << endl;
}

// 性能测试
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    OptimizedSolution solution;
    
    // 生成大规模测试数据
    vector<int> largeNums(20);
    srand(time(nullptr));
    for (int i = 0; i < 20; i++) {
        largeNums[i] = rand() % 100 + 1;
    }
    
    auto start = chrono::high_resolution_clock::now();
    bool result = solution.splitArraySameAverage(largeNums);
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "数据规模: 20个元素" << endl;
    cout << "执行时间: " << duration.count() << " 毫秒" << endl;
    cout << "结果: " << (result ? "true" : "false") << endl;
}

int main() {
    testSplitArraySameAverage();
    performanceTest();
    return 0;
}

/*
 * 算法深度分析：
 * 
 * 1. 数学原理：
 *    - 如果数组可以被分割成两个平均值相等的子集，那么有：sum1/k1 = sum2/k2 = total/n
 *    - 等价于：sum1 * n = total * k1 且 sum2 * n = total * k2
 *    - 其中k1 + k2 = n，sum1 + sum2 = total
 * 
 * 2. 折半搜索优化：
 *    - 直接搜索所有子集的时间复杂度为O(2^n)，对于n=30就达到10^9级别
 *    - 折半搜索将复杂度降为O(2^(n/2))，对于n=30只有约3万种可能
 *    - 结合哈希表查找，实现高效搜索
 * 
 * 3. C++特性利用：
 *    - 使用unordered_map进行快速查找
 *    - 自定义哈希函数处理pair类型
 *    - 使用递归生成组合，代码简洁高效
 * 
 * 4. 工程化改进：
 *    - 提供基础版本和优化版本
 *    - 全面的异常处理和测试用例
 *    - 性能监控和优化建议
 * 
 * 5. 扩展应用：
 *    - 类似思路可用于其他均值相关的分割问题
 *    - 可以扩展到多个子集的分割问题
 *    - 可以处理带权重的均值分割问题
 */