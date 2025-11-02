#include <iostream>
#include <vector>
#include <chrono>

/**
 * LeetCode 795. 区间子数组个数 (Number of Subarrays with Bounded Maximum)
 * 
 * 题目描述:
 * 给定一个整数数组 nums 和两个整数 left 和 right，请返回数组中满足以下条件的非空子数组的个数：
 * - 子数组中的最大值在区间 [left, right] 内
 * 
 * 示例1:
 * 输入: nums = [2, 1, 4, 3], left = 2, right = 3
 * 输出: 3
 * 解释:
 * 满足条件的子数组是 [2], [2, 1], [3]
 * 
 * 示例2:
 * 输入: nums = [2, 9, 2, 5, 6], left = 2, right = 8
 * 输出: 7
 * 解释:
 * 满足条件的子数组有 [2], [2, 9], [2], [2, 5], [5], [5, 6], [6]
 * 
 * 提示:
 * 1. 1 <= nums.length <= 10^5
 * 2. 0 <= nums[i] <= 10^9
 * 3. 0 <= left <= right <= 10^9
 * 
 * 题目链接: https://leetcode.com/problems/number-of-subarrays-with-bounded-maximum/
 * 
 * 解题思路:
 * 这个问题可以通过以下两种方法来解决：
 * 
 * 方法一：暴力枚举所有子数组并检查最大值（不推荐，时间复杂度太高）
 * 
 * 方法二：使用计数法，考虑每个位置作为子数组最大值时的贡献
 * 但这种方法实现起来比较复杂。
 * 
 * 方法三：使用前缀和的思想，计算「最大值小于等于 right」的子数组个数，
 * 减去「最大值小于 left」的子数组个数，得到「最大值在 [left, right] 之间」的子数组个数。
 * 
 * 这里我们选择方法三，具体实现思路如下：
 * 1. 定义一个辅助函数 countSubarrays，用于计算数组中最大值小于等于给定阈值的子数组个数
 * 2. 最终结果就是 countSubarrays(nums, right) - countSubarrays(nums, left - 1)
 * 
 * 时间复杂度: O(n)，其中 n 是数组的长度。我们只需要遍历数组两次。
 * 空间复杂度: O(1)，只使用了常数级别的额外空间。
 * 
 * 这是最优解，因为我们需要至少遍历数组一次，时间复杂度无法更低。
 */

/**
 * 计算数组中最大值小于等于给定阈值的子数组个数
 * 
 * @param nums 整数数组
 * @param threshold 阈值
 * @return 最大值小于等于阈值的子数组个数
 */
int countSubarrays(const std::vector<int>& nums, int threshold) {
    int count = 0;
    int currentLength = 0;
    
    for (int num : nums) {
        // 如果当前元素小于等于阈值，则可以将它加入到当前连续子数组中
        if (num <= threshold) {
            currentLength++;
            // 增加的子数组个数就是当前连续子数组的长度
            // 例如，[1,2,3]增加一个4，则新增的子数组有[4], [3,4], [2,3,4], [1,2,3,4]，共4个
            count += currentLength;
        } else {
            // 遇到大于阈值的元素，重置连续子数组长度
            currentLength = 0;
        }
    }
    
    return count;
}

/**
 * 计算数组中满足条件的非空子数组的个数
 * 
 * @param nums 整数数组
 * @param left 左边界
 * @param right 右边界
 * @return 满足条件的非空子数组个数
 */
int numSubarrayBoundedMax(const std::vector<int>& nums, int left, int right) {
    // 参数校验
    if (nums.empty() || left > right) {
        return 0;
    }
    
    // 计算最大值小于等于right的子数组个数，减去最大值小于left的子数组个数
    return countSubarrays(nums, right) - countSubarrays(nums, left - 1);
}

/**
 * 另一种实现方式，直接计算满足条件的子数组个数
 * 
 * @param nums 整数数组
 * @param left 左边界
 * @param right 右边界
 * @return 满足条件的非空子数组个数
 */
int numSubarrayBoundedMaxAlternative(const std::vector<int>& nums, int left, int right) {
    // 参数校验
    if (nums.empty() || left > right) {
        return 0;
    }
    
    int count = 0;
    // 记录上一个大于right的位置
    int lastInvalid = -1;
    // 记录上一个在[left, right]范围内的位置
    int lastValid = -1;
    
    for (int i = 0; i < nums.size(); i++) {
        if (nums[i] > right) {
            // 遇到大于right的元素，更新lastInvalid
            lastInvalid = i;
        } else if (nums[i] >= left && nums[i] <= right) {
            // 遇到在[left, right]范围内的元素
            lastValid = i;
            // 以当前元素为最大值的子数组个数为i - lastInvalid
            count += i - lastInvalid;
        } else { // nums[i] < left
            // 遇到小于left的元素
            // 如果之前有在[left, right]范围内的元素，则可以与当前元素组成有效子数组
            if (lastValid > lastInvalid) {
                count += lastValid - lastInvalid;
            }
        }
    }
    
    return count;
}

/**
 * 打印数组
 * 
 * @param nums 要打印的数组
 */
void printArray(const std::vector<int>& nums) {
    std::cout << "[";
    for (size_t i = 0; i < nums.size(); i++) {
        std::cout << nums[i];
        if (i < nums.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
}

// 主函数，用于测试
int main() {
    // 测试用例1
    std::vector<int> nums1 = {2, 1, 4, 3};
    int left1 = 2;
    int right1 = 3;
    
    std::cout << "测试用例1 - nums = ";
    printArray(nums1);
    std::cout << "left = " << left1 << ", right = " << right1 << std::endl;
    std::cout << "numSubarrayBoundedMax 结果: " << numSubarrayBoundedMax(nums1, left1, right1) << std::endl; // 预期输出: 3
    std::cout << "numSubarrayBoundedMaxAlternative 结果: " << numSubarrayBoundedMaxAlternative(nums1, left1, right1) << std::endl; // 预期输出: 3
    std::cout << std::endl;
    
    // 测试用例2
    std::vector<int> nums2 = {2, 9, 2, 5, 6};
    int left2 = 2;
    int right2 = 8;
    
    std::cout << "测试用例2 - nums = ";
    printArray(nums2);
    std::cout << "left = " << left2 << ", right = " << right2 << std::endl;
    std::cout << "numSubarrayBoundedMax 结果: " << numSubarrayBoundedMax(nums2, left2, right2) << std::endl; // 预期输出: 7
    std::cout << "numSubarrayBoundedMaxAlternative 结果: " << numSubarrayBoundedMaxAlternative(nums2, left2, right2) << std::endl; // 预期输出: 7
    std::cout << std::endl;
    
    // 测试用例3 - 边界情况：数组长度为1
    std::vector<int> nums3 = {1};
    int left3 = 1;
    int right3 = 1;
    
    std::cout << "测试用例3 - nums = ";
    printArray(nums3);
    std::cout << "left = " << left3 << ", right = " << right3 << std::endl;
    std::cout << "numSubarrayBoundedMax 结果: " << numSubarrayBoundedMax(nums3, left3, right3) << std::endl; // 预期输出: 1
    std::cout << "numSubarrayBoundedMaxAlternative 结果: " << numSubarrayBoundedMaxAlternative(nums3, left3, right3) << std::endl; // 预期输出: 1
    std::cout << std::endl;
    
    // 测试用例4 - 边界情况：所有元素都在范围内
    std::vector<int> nums4 = {2, 3, 4};
    int left4 = 1;
    int right4 = 5;
    
    std::cout << "测试用例4 - nums = ";
    printArray(nums4);
    std::cout << "left = " << left4 << ", right = " << right4 << std::endl;
    std::cout << "numSubarrayBoundedMax 结果: " << numSubarrayBoundedMax(nums4, left4, right4) << std::endl; // 预期输出: 6
    std::cout << "numSubarrayBoundedMaxAlternative 结果: " << numSubarrayBoundedMaxAlternative(nums4, left4, right4) << std::endl; // 预期输出: 6
    std::cout << std::endl;
    
    // 测试用例5 - 边界情况：没有元素在范围内
    std::vector<int> nums5 = {1, 1, 1};
    int left5 = 2;
    int right5 = 3;
    
    std::cout << "测试用例5 - nums = ";
    printArray(nums5);
    std::cout << "left = " << left5 << ", right = " << right5 << std::endl;
    std::cout << "numSubarrayBoundedMax 结果: " << numSubarrayBoundedMax(nums5, left5, right5) << std::endl; // 预期输出: 0
    std::cout << "numSubarrayBoundedMaxAlternative 结果: " << numSubarrayBoundedMaxAlternative(nums5, left5, right5) << std::endl; // 预期输出: 0
    std::cout << std::endl;
    
    // 性能测试
    std::cout << "性能测试:" << std::endl;
    std::vector<int> largeNums(100000);
    // 生成一个混合数组，一部分元素在范围内，一部分不在
    for (int i = 0; i < largeNums.size(); i++) {
        if (i % 5 == 0) {
            largeNums[i] = 100; // 大于right的元素
        } else if (i % 3 == 0) {
            largeNums[i] = 50; // 在范围内的元素
        } else {
            largeNums[i] = 10; // 小于left的元素
        }
    }
    int left6 = 40;
    int right6 = 60;
    
    auto startTime = std::chrono::high_resolution_clock::now();
    int result1 = numSubarrayBoundedMax(largeNums, left6, right6);
    auto endTime = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
    std::cout << "大数组 - numSubarrayBoundedMax 结果: " << result1 << std::endl;
    std::cout << "大数组 - numSubarrayBoundedMax 耗时: " << duration.count() << "ms" << std::endl;
    
    startTime = std::chrono::high_resolution_clock::now();
    int result2 = numSubarrayBoundedMaxAlternative(largeNums, left6, right6);
    endTime = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
    std::cout << "大数组 - numSubarrayBoundedMaxAlternative 结果: " << result2 << std::endl;
    std::cout << "大数组 - numSubarrayBoundedMaxAlternative 耗时: " << duration.count() << "ms" << std::endl;
    
    return 0;
}