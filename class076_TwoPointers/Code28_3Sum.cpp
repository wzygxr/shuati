#include <iostream>
#include <vector>
#include <algorithm>
#include <set>
#include <chrono>

/**
 * LeetCode 15. 三数之和 (3Sum)
 * 
 * 题目描述:
 * 给你一个整数数组 nums ，判断是否存在三元组 [nums[i], nums[j], nums[k]] 满足 i != j、i != k 且 j != k ，
 * 同时还满足 nums[i] + nums[j] + nums[k] == 0 。请你找出所有和为 0 且不重复的三元组。
 * 注意：答案中不可以包含重复的三元组。
 * 
 * 示例1:
 * 输入: nums = [-1,0,1,2,-1,-4]
 * 输出: [[-1,-1,2],[-1,0,1]]
 * 
 * 示例2:
 * 输入: nums = [0,1,1]
 * 输出: []
 * 
 * 示例3:
 * 输入: nums = [0,0,0]
 * 输出: [[0,0,0]]
 * 
 * 提示:
 * 3 <= nums.length <= 3000
 * -10^5 <= nums[i] <= 10^5
 * 
 * 题目链接: https://leetcode.com/problems/3sum/
 * 
 * 解题思路:
 * 这道题可以使用排序 + 双指针的方法来解决：
 * 
 * 方法一（暴力解法）：
 * 使用三层嵌套循环遍历所有可能的三元组，找出和为0的三元组。使用set去重。
 * 时间复杂度：O(n^3)，空间复杂度：O(n)
 * 
 * 方法二（排序 + 双指针）：
 * 1. 先对数组进行排序
 * 2. 遍历数组，对于每个元素nums[i]，使用双指针left和right分别指向i+1和数组末尾
 * 3. 计算当前三个数的和：sum = nums[i] + nums[left] + nums[right]
 * 4. 如果sum == 0，将这三个数加入结果集，并移动left和right指针
 *    - 为了避免重复，需要跳过相同的元素
 * 5. 如果sum < 0，说明需要增大和，移动left指针
 * 6. 如果sum > 0，说明需要减小和，移动right指针
 * 7. 重复步骤3-6，直到left >= right
 * 时间复杂度：O(n^2)，空间复杂度：O(1)或O(log n)（排序的空间复杂度）
 * 
 * 最优解是方法二，时间复杂度 O(n^2)，空间复杂度 O(1)或O(log n)。
 */

/**
 * 解法一: 暴力解法（不推荐，会超时）
 * 
 * @param nums 输入数组
 * @return 所有和为0且不重复的三元组
 * @throws std::invalid_argument 如果输入数组长度小于3
 */
std::vector<std::vector<int>> threeSumBruteForce(const std::vector<int>& nums) {
    // 参数校验
    if (nums.size() < 3) {
        throw std::invalid_argument("输入数组长度必须至少为3");
    }
    
    std::set<std::vector<int>> resultSet;
    int n = nums.size();
    
    // 三层循环遍历所有可能的三元组
    for (size_t i = 0; i < n; i++) {
        for (size_t j = i + 1; j < n; j++) {
            for (size_t k = j + 1; k < n; k++) {
                if (nums[i] + nums[j] + nums[k] == 0) {
                    // 将三元组排序后加入结果集，利用set去重
                    std::vector<int> triplet = {nums[i], nums[j], nums[k]};
                    std::sort(triplet.begin(), triplet.end());
                    resultSet.insert(triplet);
                }
            }
        }
    }
    
    return std::vector<std::vector<int>>(resultSet.begin(), resultSet.end());
}

/**
 * 解法二: 排序 + 双指针（最优解）
 * 
 * @param nums 输入数组
 * @return 所有和为0且不重复的三元组
 */
std::vector<std::vector<int>> threeSum(const std::vector<int>& nums) {
    // 参数校验
    if (nums.size() < 3) {
        throw std::invalid_argument("输入数组长度必须至少为3");
    }
    
    std::vector<std::vector<int>> result;
    std::vector<int> sortedNums = nums; // 复制数组，避免修改原数组
    int n = sortedNums.size();
    
    // 对数组进行排序
    std::sort(sortedNums.begin(), sortedNums.end());
    
    // 遍历数组，固定第一个元素
    for (int i = 0; i < n; i++) {
        // 跳过重复的第一个元素，避免产生重复的三元组
        if (i > 0 && sortedNums[i] == sortedNums[i - 1]) {
            continue;
        }
        
        // 如果当前元素已经大于0，由于数组已排序，后面的元素都大于0，三数之和不可能为0
        if (sortedNums[i] > 0) {
            break;
        }
        
        // 初始化双指针
        int left = i + 1;
        int right = n - 1;
        
        while (left < right) {
            int sum = sortedNums[i] + sortedNums[left] + sortedNums[right];
            
            if (sum == 0) {
                // 找到一个三元组
                result.push_back({sortedNums[i], sortedNums[left], sortedNums[right]});
                
                // 跳过重复的左指针元素
                while (left < right && sortedNums[left] == sortedNums[left + 1]) {
                    left++;
                }
                // 跳过重复的右指针元素
                while (left < right && sortedNums[right] == sortedNums[right - 1]) {
                    right--;
                }
                
                // 移动两个指针
                left++;
                right--;
            } else if (sum < 0) {
                // 和小于0，需要增大和，移动左指针
                left++;
            } else {
                // 和大于0，需要减小和，移动右指针
                right--;
            }
        }
    }
    
    return result;
}

/**
 * 解法三: 优化的双指针实现
 * 
 * @param nums 输入数组
 * @return 所有和为0且不重复的三元组
 */
std::vector<std::vector<int>> threeSumOptimized(const std::vector<int>& nums) {
    // 参数校验
    if (nums.size() < 3) {
        throw std::invalid_argument("输入数组长度必须至少为3");
    }
    
    std::vector<std::vector<int>> result;
    std::vector<int> sortedNums = nums; // 复制数组，避免修改原数组
    int n = sortedNums.size();
    
    // 对数组进行排序
    std::sort(sortedNums.begin(), sortedNums.end());
    
    // 遍历数组，固定第一个元素
    for (int i = 0; i < n - 2; i++) {
        // 跳过重复的第一个元素，避免产生重复的三元组
        if (i > 0 && sortedNums[i] == sortedNums[i - 1]) {
            continue;
        }
        
        // 剪枝：如果当前元素已经大于0，三数之和不可能为0
        if (sortedNums[i] > 0) {
            break;
        }
        
        // 剪枝：如果当前元素和最大的两个元素之和仍小于0，跳过
        if (sortedNums[i] + sortedNums[n - 1] + sortedNums[n - 2] < 0) {
            continue;
        }
        
        // 初始化双指针
        int left = i + 1;
        int right = n - 1;
        
        while (left < right) {
            int sum = sortedNums[i] + sortedNums[left] + sortedNums[right];
            
            if (sum == 0) {
                // 找到一个三元组
                result.push_back({sortedNums[i], sortedNums[left], sortedNums[right]});
                
                // 跳过重复的左指针元素
                while (left < right && sortedNums[left] == sortedNums[left + 1]) {
                    left++;
                }
                // 跳过重复的右指针元素
                while (left < right && sortedNums[right] == sortedNums[right - 1]) {
                    right--;
                }
                
                // 移动两个指针
                left++;
                right--;
            } else if (sum < 0) {
                // 和小于0，需要增大和，移动左指针
                left++;
            } else {
                // 和大于0，需要减小和，移动右指针
                right--;
            }
        }
    }
    
    return result;
}

/**
 * 打印向量内容
 */
void printVector(const std::vector<int>& vec) {
    std::cout << "[";
    for (size_t i = 0; i < vec.size(); i++) {
        std::cout << vec[i];
        if (i < vec.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]";
}

/**
 * 打印三元组列表
 */
void printTriplets(const std::vector<std::vector<int>>& triplets) {
    std::cout << "[";
    for (size_t i = 0; i < triplets.size(); i++) {
        printVector(triplets[i]);
        if (i < triplets.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]";
}

/**
 * 测试函数
 */
void test() {
    // 测试用例1
    std::vector<int> nums1 = {-1, 0, 1, 2, -1, -4};
    std::vector<std::vector<int>> expected1 = {{-1, -1, 2}, {-1, 0, 1}};
    std::cout << "测试用例1:\n";
    std::cout << "输入数组: ";
    printVector(nums1);
    std::cout << std::endl;
    std::vector<std::vector<int>> result1 = threeSum(nums1);
    std::cout << "结果: ";
    printTriplets(result1);
    std::cout << std::endl;
    std::cout << "期望: ";
    printTriplets(expected1);
    std::cout << std::endl << std::endl;
    
    // 测试用例2
    std::vector<int> nums2 = {0, 1, 1};
    std::vector<std::vector<int>> expected2 = {};
    std::cout << "测试用例2:\n";
    std::cout << "输入数组: ";
    printVector(nums2);
    std::cout << std::endl;
    std::vector<std::vector<int>> result2 = threeSum(nums2);
    std::cout << "结果: ";
    printTriplets(result2);
    std::cout << std::endl;
    std::cout << "期望: ";
    printTriplets(expected2);
    std::cout << std::endl << std::endl;
    
    // 测试用例3
    std::vector<int> nums3 = {0, 0, 0};
    std::vector<std::vector<int>> expected3 = {{0, 0, 0}};
    std::cout << "测试用例3:\n";
    std::cout << "输入数组: ";
    printVector(nums3);
    std::cout << std::endl;
    std::vector<std::vector<int>> result3 = threeSum(nums3);
    std::cout << "结果: ";
    printTriplets(result3);
    std::cout << std::endl;
    std::cout << "期望: ";
    printTriplets(expected3);
    std::cout << std::endl << std::endl;
    
    // 测试用例4 - 边界情况：多个重复元素
    std::vector<int> nums4 = {-2, 0, 0, 2, 2};
    std::vector<std::vector<int>> expected4 = {{-2, 0, 2}};
    std::cout << "测试用例4（多个重复元素）:\n";
    std::cout << "输入数组: ";
    printVector(nums4);
    std::cout << std::endl;
    std::vector<std::vector<int>> result4 = threeSum(nums4);
    std::cout << "结果: ";
    printTriplets(result4);
    std::cout << std::endl;
    std::cout << "期望: ";
    printTriplets(expected4);
    std::cout << std::endl << std::endl;
    
    // 测试用例5 - 边界情况：所有元素都为负数
    std::vector<int> nums5 = {-1, -2, -3, -4, -5};
    std::vector<std::vector<int>> expected5 = {};
    std::cout << "测试用例5（全负数）:\n";
    std::cout << "输入数组: ";
    printVector(nums5);
    std::cout << std::endl;
    std::vector<std::vector<int>> result5 = threeSum(nums5);
    std::cout << "结果: ";
    printTriplets(result5);
    std::cout << std::endl;
    std::cout << "期望: ";
    printTriplets(expected5);
    std::cout << std::endl << std::endl;
}

/**
 * 性能测试
 */
void performanceTest() {
    // 创建一个中等大小的数组进行性能测试
    const int size = 1000;
    std::vector<int> mediumArray(size);
    
    // 生成测试数据：包含正负数和零
    for (int i = 0; i < size; i++) {
        mediumArray[i] = (i % 100) - 50;  // -50 到 49
    }
    
    // 测试解法二的性能
    auto start = std::chrono::high_resolution_clock::now();
    std::vector<std::vector<int>> result2 = threeSum(mediumArray);
    auto end = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
    std::cout << "解法二（双指针）耗时: " << duration << "ms, 找到的三元组数量: " << result2.size() << std::endl;
    
    // 测试解法三的性能
    start = std::chrono::high_resolution_clock::now();
    std::vector<std::vector<int>> result3 = threeSumOptimized(mediumArray);
    end = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
    std::cout << "解法三（优化双指针）耗时: " << duration << "ms, 找到的三元组数量: " << result3.size() << std::endl;
    
    // 验证两种解法结果一致（这里只比较数量，不比较具体内容）
    bool resultsConsistent = (result2.size() == result3.size());
    std::cout << "所有解法结果数量一致: " << (resultsConsistent ? "true" : "false") << std::endl;
}

/**
 * 边界条件测试
 */
void boundaryTest() {
    try {
        // 测试长度为2的输入
        threeSum(std::vector<int>{1, 2});
        std::cout << "边界测试失败：长度为2的输入没有抛出异常" << std::endl;
    } catch (const std::invalid_argument& e) {
        std::cout << "边界测试通过：长度为2的输入正确抛出异常" << std::endl;
    }
    
    try {
        // 测试空输入
        threeSum(std::vector<int>{});
        std::cout << "边界测试失败：空输入没有抛出异常" << std::endl;
    } catch (const std::invalid_argument& e) {
        std::cout << "边界测试通过：空输入正确抛出异常" << std::endl;
    }
}

int main() {
    std::cout << "=== 测试用例 ===" << std::endl;
    test();
    
    std::cout << "=== 性能测试 ===" << std::endl;
    performanceTest();
    
    std::cout << "=== 边界条件测试 ===" << std::endl;
    boundaryTest();
    
    return 0;
}