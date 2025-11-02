/**
 * LeetCode 167. 两数之和 II - 输入有序数组 (Two Sum II - Input Array Is Sorted)
 * 
 * 题目描述:
 * 给你一个下标从 1 开始的整数数组 numbers ，该数组已按非递减顺序排列，
 * 请你从数组中找出满足相加之和等于目标数 target 的两个数。
 * 如果设这两个数分别是 numbers[index1] 和 numbers[index2] ，
 * 则 1 <= index1 < index2 <= numbers.length 。
 * 以长度为 2 的整数数组 [index1, index2] 的形式返回这两个整数的下标 index1 和 index2。
 * 
 * 你可以假设每个输入只对应唯一的答案，而且你不可以重复使用相同的元素。
 * 你所设计的解决方案必须只使用常量级的额外空间。
 * 
 * 示例1:
 * 输入: numbers = [2,7,11,15], target = 9
 * 输出: [1,2]
 * 解释: 2 与 7 之和等于目标数 9 。因此 index1 = 1, index2 = 2 。返回 [1, 2] 。
 * 
 * 示例2:
 * 输入: numbers = [2,3,4], target = 6
 * 输出: [1,3]
 * 解释: 2 与 4 之和等于目标数 6 。因此 index1 = 1, index2 = 3 。返回 [1, 3] 。
 * 
 * 示例3:
 * 输入: numbers = [-1,0], target = -1
 * 输出: [1,2]
 * 解释: -1 与 0 之和等于目标数 -1 。因此 index1 = 1, index2 = 2 。返回 [1, 2] 。
 * 
 * 提示:
 * 2 <= numbers.length <= 3 * 10^4
 * -1000 <= numbers[i] <= 1000
 * numbers 按 非递减顺序 排列
 * -1000 <= target <= 1000
 * 仅存在一个有效答案
 * 
 * 题目链接: https://leetcode.cn/problems/two-sum-ii-input-array-is-sorted/
 * 
 * 解题思路:
 * 这道题可以使用多种方法解决，从暴力到高效：
 * 
 * 方法一（暴力解法）：
 * 使用两层嵌套循环遍历所有可能的数对，找到和为target的数对。
 * 时间复杂度：O(n²)，空间复杂度：O(1)
 * 
 * 方法二（二分查找）：
 * 对于每个元素，使用二分查找寻找另一个元素使得它们的和为target。
 * 时间复杂度：O(n log n)，空间复杂度：O(1)
 * 
 * 方法三（双指针）：
 * 1. 初始化左指针left指向数组起始位置，右指针right指向数组末尾位置
 * 2. 计算当前两数之和：sum = numbers[left] + numbers[right]
 * 3. 如果sum == target，返回结果[left+1, right+1]（注意题目要求索引从1开始）
 * 4. 如果sum < target，说明需要增大和，移动左指针left++
 * 5. 如果sum > target，说明需要减小和，移动右指针right--
 * 6. 重复步骤2-5，直到找到答案（题目保证有唯一解）
 * 时间复杂度：O(n)，空间复杂度：O(1)
 * 
 * 方法四（优化的双指针）：
 * 当数组中有大量重复元素时，可以通过跳过相同的元素来提高效率。
 * 时间复杂度：O(n)，空间复杂度：O(1)
 * 
 * 最优解是方法三和方法四，时间复杂度 O(n)，空间复杂度 O(1)。
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>
#include <stdexcept>
#include <climits> // 用于INT_MAX和INT_MIN
using namespace std;
class Solution {
public:
    /**
     * 解法一: 暴力解法（不推荐，会超时）
     * 
     * @param numbers 输入的有序数组
     * @param target 目标和
     * @return 两个数的索引数组（索引从1开始）
     * 
     * 时间复杂度: O(n²) - 需要两层循环遍历所有可能的数对
     * 空间复杂度: O(1) - 只使用了常量级的额外空间
     */
    vector<int> twoSumBruteForce(const vector<int>& numbers, int target) {
        // 参数校验
        if (numbers.size() < 2) {
            throw invalid_argument("输入数组长度必须至少为2");
        }
        
        int n = numbers.size();
        
        // 两层循环遍历所有可能的数对
        for (int i = 0; i < n - 1; ++i) {
            for (int j = i + 1; j < n; ++j) {
                if (numbers[i] + numbers[j] == target) {
                    return {i + 1, j + 1}; // 注意索引从1开始
                }
            }
        }
        
        // 根据题目描述，一定存在解，所以不会执行到这里
        throw invalid_argument("未找到符合条件的数对");
    }
    
    /**
     * 解法二: 二分查找
     * 
     * @param numbers 输入的有序数组
     * @param target 目标和
     * @return 两个数的索引数组（索引从1开始）
     * 
     * 时间复杂度: O(n log n) - 对每个元素执行二分查找，每个二分查找是O(log n)
     * 空间复杂度: O(1) - 只使用了常量级的额外空间
     */
    vector<int> twoSumBinarySearch(const vector<int>& numbers, int target) {
        // 参数校验
        if (numbers.size() < 2) {
            throw invalid_argument("输入数组长度必须至少为2");
        }
        
        int n = numbers.size();
        
        // 遍历数组，对每个元素使用二分查找寻找另一个元素
        for (int i = 0; i < n; ++i) {
            int complement = target - numbers[i];
            int left = i + 1;
            int right = n - 1;
            
            while (left <= right) {
                int mid = left + (right - left) / 2; // 避免整数溢出
                if (numbers[mid] == complement) {
                    return {i + 1, mid + 1}; // 注意索引从1开始
                } else if (numbers[mid] < complement) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        
        // 根据题目描述，一定存在解，所以不会执行到这里
        throw invalid_argument("未找到符合条件的数对");
    }
    
    /**
     * 解法三: 双指针（最优解）
     * 
     * @param numbers 输入的有序数组
     * @param target 目标和
     * @return 两个数的索引数组（索引从1开始）
     * 
     * 时间复杂度: O(n) - 只需要一次遍历数组
     * 空间复杂度: O(1) - 只使用了常量级的额外空间
     */
    vector<int> twoSum(const vector<int>& numbers, int target) {
        // 参数校验
        if (numbers.size() < 2) {
            throw invalid_argument("输入数组长度必须至少为2");
        }
        
        // 初始化左右指针
        int left = 0;
        int right = numbers.size() - 1;
        
        while (left < right) {
            int sum = numbers[left] + numbers[right];
            
            if (sum == target) {
                return {left + 1, right + 1}; // 注意索引从1开始
            } else if (sum < target) {
                // 和小于目标值，需要增大和，移动左指针
                ++left;
            } else {
                // 和大于目标值，需要减小和，移动右指针
                --right;
            }
        }
        
        // 根据题目描述，一定存在解，所以不会执行到这里
        throw invalid_argument("未找到符合条件的数对");
    }
    
    /**
     * 解法四: 优化的双指针实现
     * 当数组中有大量重复元素时，可以通过跳过相同的元素来提高效率
     * 
     * @param numbers 输入的有序数组
     * @param target 目标和
     * @return 两个数的索引数组（索引从1开始）
     * 
     * 时间复杂度: O(n) - 最坏情况下需要一次遍历数组
     * 空间复杂度: O(1) - 只使用了常量级的额外空间
     */
    vector<int> twoSumOptimized(const vector<int>& numbers, int target) {
        // 参数校验
        if (numbers.size() < 2) {
            throw invalid_argument("输入数组长度必须至少为2");
        }
        
        // 初始化左右指针
        int left = 0;
        int right = numbers.size() - 1;
        
        while (left < right) {
            int sum = numbers[left] + numbers[right];
            
            if (sum == target) {
                return {left + 1, right + 1}; // 注意索引从1开始
            } else if (sum < target) {
                // 和小于目标值，需要增大和，移动左指针
                ++left;
                // 跳过重复的元素
                while (left < right && numbers[left] == numbers[left - 1]) {
                    ++left;
                }
            } else {
                // 和大于目标值，需要减小和，移动右指针
                --right;
                // 跳过重复的元素
                while (left < right && numbers[right] == numbers[right + 1]) {
                    --right;
                }
            }
        }
        
        // 根据题目描述，一定存在解，所以不会执行到这里
        throw invalid_argument("未找到符合条件的数对");
    }
};

/**
 * 打印向量辅助函数
 */
void printVector(const vector<int>& vec, const string& prefix = "") {
    if (!prefix.empty()) {
        cout << prefix;
    }
    cout << "[";
    for (size_t i = 0; i < vec.size(); ++i) {
        cout << vec[i];
        if (i < vec.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
}

/**
 * 测试函数
 */
void test() {
    Solution solution;
    
    // 测试用例1
    vector<int> numbers1 = {2, 7, 11, 15};
    int target1 = 9;
    vector<int> expected1 = {1, 2};
    vector<int> result1 = solution.twoSum(numbers1, target1);
    cout << "测试用例1:" << endl;
    printVector(numbers1, "输入数组: ");
    cout << "目标值: " << target1 << endl;
    printVector(result1, "结果: ");
    printVector(expected1, "期望: ");
    cout << "测试通过: " << (result1 == expected1) << endl << endl;
    
    // 测试用例2
    vector<int> numbers2 = {2, 3, 4};
    int target2 = 6;
    vector<int> expected2 = {1, 3};
    vector<int> result2 = solution.twoSum(numbers2, target2);
    cout << "测试用例2:" << endl;
    printVector(numbers2, "输入数组: ");
    cout << "目标值: " << target2 << endl;
    printVector(result2, "结果: ");
    printVector(expected2, "期望: ");
    cout << "测试通过: " << (result2 == expected2) << endl << endl;
    
    // 测试用例3
    vector<int> numbers3 = {-1, 0};
    int target3 = -1;
    vector<int> expected3 = {1, 2};
    vector<int> result3 = solution.twoSum(numbers3, target3);
    cout << "测试用例3:" << endl;
    printVector(numbers3, "输入数组: ");
    cout << "目标值: " << target3 << endl;
    printVector(result3, "结果: ");
    printVector(expected3, "期望: ");
    cout << "测试通过: " << (result3 == expected3) << endl << endl;
    
    // 测试用例4 - 边界情况：有重复元素
    vector<int> numbers4 = {1, 1, 2, 4, 5};
    int target4 = 2;
    vector<int> expected4 = {1, 2};
    vector<int> result4 = solution.twoSum(numbers4, target4);
    cout << "测试用例4（有重复元素）:" << endl;
    printVector(numbers4, "输入数组: ");
    cout << "目标值: " << target4 << endl;
    printVector(result4, "结果: ");
    printVector(expected4, "期望: ");
    cout << "测试通过: " << (result4 == expected4) << endl << endl;
    
    // 测试用例5 - 边界情况：数组长度为2
    vector<int> numbers5 = {1, 3};
    int target5 = 4;
    vector<int> expected5 = {1, 2};
    vector<int> result5 = solution.twoSum(numbers5, target5);
    cout << "测试用例5（数组长度为2）:" << endl;
    printVector(numbers5, "输入数组: ");
    cout << "目标值: " << target5 << endl;
    printVector(result5, "结果: ");
    printVector(expected5, "期望: ");
    cout << "测试通过: " << (result5 == expected5) << endl << endl;
}

/**
 * 性能测试
 */
void performanceTest() {
    Solution solution;
    
    // 创建一个较大的测试数组
    const int size = 10000;
    vector<int> largeArray(size);
    for (int i = 0; i < size; ++i) {
        largeArray[i] = i * 2; // 生成偶数序列
    }
    int target = largeArray[size/2] + largeArray[size/2 + 1]; // 确保有解
    
    // 测试解法二的性能
    auto start = chrono::high_resolution_clock::now();
    vector<int> result2 = solution.twoSumBinarySearch(largeArray, target);
    auto end = chrono::high_resolution_clock::now();
    auto duration2 = chrono::duration_cast<chrono::milliseconds>(end - start).count();
    cout << "解法二（二分查找）耗时: " << duration2 << "ms" << endl;
    
    // 测试解法三的性能
    start = chrono::high_resolution_clock::now();
    vector<int> result3 = solution.twoSum(largeArray, target);
    end = chrono::high_resolution_clock::now();
    auto duration3 = chrono::duration_cast<chrono::milliseconds>(end - start).count();
    cout << "解法三（双指针）耗时: " << duration3 << "ms" << endl;
    
    // 测试解法四的性能
    start = chrono::high_resolution_clock::now();
    vector<int> result4 = solution.twoSumOptimized(largeArray, target);
    end = chrono::high_resolution_clock::now();
    auto duration4 = chrono::duration_cast<chrono::milliseconds>(end - start).count();
    cout << "解法四（优化双指针）耗时: " << duration4 << "ms" << endl;
    
    // 验证结果是否一致
    cout << "解法二和解法三结果一致: " << (result2 == result3) << endl;
    cout << "解法三和解法四结果一致: " << (result3 == result4) << endl;
    
    // 测试有重复元素的性能
    vector<int> duplicateArray(size);
    for (int i = 0; i < size; ++i) {
        duplicateArray[i] = i % 100; // 生成大量重复元素
    }
    target = 100; // 确保有解
    
    start = chrono::high_resolution_clock::now();
    result3 = solution.twoSum(duplicateArray, target);
    end = chrono::high_resolution_clock::now();
    duration3 = chrono::duration_cast<chrono::milliseconds>(end - start).count();
    cout << "\n重复元素测试 - 双指针耗时: " << duration3 << "ms" << endl;
    
    start = chrono::high_resolution_clock::now();
    result4 = solution.twoSumOptimized(duplicateArray, target);
    end = chrono::high_resolution_clock::now();
    duration4 = chrono::duration_cast<chrono::milliseconds>(end - start).count();
    cout << "重复元素测试 - 优化双指针耗时: " << duration4 << "ms" << endl;
    cout << "在重复元素情况下，优化效果: " << (duration3 > duration4 ? "有效" : "不明显") << endl;
}

/**
 * 边界条件测试
 */
void boundaryTest() {
    Solution solution;
    
    try {
        // 测试空输入
        vector<int> empty;
        solution.twoSum(empty, 5);
        cout << "边界测试失败：空输入没有抛出异常" << endl;
    } catch (const invalid_argument& e) {
        cout << "边界测试通过：空输入正确抛出异常: " << e.what() << endl;
    }
    
    try {
        // 测试长度为1的输入
        vector<int> single = {1};
        solution.twoSum(single, 1);
        cout << "边界测试失败：长度为1的输入没有抛出异常" << endl;
    } catch (const invalid_argument& e) {
        cout << "边界测试通过：长度为1的输入正确抛出异常: " << e.what() << endl;
    }
    
    // 测试最大可能值
    vector<int> maxArray = {INT_MAX - 1, INT_MAX};
    int maxTarget = (INT_MAX - 1) + (INT_MAX - 1); // 避免溢出
    try {
        vector<int> result = solution.twoSum(maxArray, maxTarget);
        printVector(result, "测试最大值：");
    } catch (const exception& e) {
        cout << "测试最大值时发生错误: " << e.what() << endl;
    }
    
    // 测试最小可能值
    vector<int> minArray = {INT_MIN, INT_MIN + 1};
    int minTarget = INT_MIN + (INT_MIN + 1);
    try {
        vector<int> result = solution.twoSum(minArray, minTarget);
        printVector(result, "测试最小值：");
    } catch (const exception& e) {
        cout << "测试最小值时发生错误: " << e.what() << endl;
    }
}

/**
 * 调试辅助函数 - 打印中间状态
 */
void debugTwoSum(const vector<int>& numbers, int target) {
    cout << "\n调试模式：" << endl;
    printVector(numbers, "输入数组: ");
    cout << "目标值: " << target << endl;
    
    int left = 0;
    int right = numbers.size() - 1;
    int step = 1;
    
    while (left < right) {
        int sum = numbers[left] + numbers[right];
        cout << "步骤 " << step << ": left=" << left << " (" << numbers[left] << "), " 
                  << "right=" << right << " (" << numbers[right] << "), " 
                  << "sum=" << sum << endl;
        
        if (sum == target) {
            cout << "找到解：[" << (left + 1) << ", " << (right + 1) << "]" << endl;
            break;
        } else if (sum < target) {
            cout << "sum < target，移动左指针" << endl;
            ++left;
        } else {
            cout << "sum > target，移动右指针" << endl;
            --right;
        }
        ++step;
    }
}

int main() {
    cout << "=== 测试用例 ===" << endl;
    test();
    
    cout << "=== 性能测试 ===" << endl;
    performanceTest();
    
    cout << "=== 边界条件测试 ===" << endl;
    boundaryTest();
    
    cout << "\n=== 调试演示 ===" << endl;
    debugTwoSum({2, 7, 11, 15}, 9);
    
    return 0;
}