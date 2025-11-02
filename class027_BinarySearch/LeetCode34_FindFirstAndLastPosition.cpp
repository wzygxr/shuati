/**
 * LeetCode 34. 在排序数组中查找元素的第一个和最后一个位置 (C++版本)
 * 
 * 题目描述：
 * 给你一个按照非递减顺序排列的整数数组 nums，和一个目标值 target。
 * 请你找出给定目标值在数组中的开始位置和结束位置。
 * 如果数组中不存在目标值 target，返回 [-1, -1]。
 * 必须设计并实现时间复杂度为 O(log n) 的算法解决此问题。
 * 
 * 示例：
 * 输入：nums = [5,7,7,8,8,10], target = 8
 * 输出：[3,4]
 * 
 * 输入：nums = [5,7,7,8,8,10], target = 6
 * 输出：[-1,-1]
 * 
 * 输入：nums = [], target = 0
 * 输出：[-1,-1]
 * 
 * 约束条件：
 * - 0 <= nums.length <= 10^5
 * - -10^9 <= nums[i] <= 10^9
 * - nums 是一个非递减数组
 * - -10^9 <= target <= 10^9
 * 
 * 解题思路：
 * 这是二分查找的高级应用。我们需要分别找到目标值的第一个位置和最后一个位置。
 * 1. 查找第一个位置：找到第一个等于目标值的元素
 * 2. 查找最后一个位置：找到最后一个等于目标值的元素
 * 两种查找都可以通过修改二分查找的逻辑来实现。
 * 
 * 时间复杂度：O(log n)，其中 n 是数组的长度。需要执行两次二分查找。
 * 空间复杂度：O(1)，只使用了常数级别的额外空间。
 * 
 * 工程化考量：
 * 1. 边界条件处理：空数组、单元素数组、目标值不存在
 * 2. 整数溢出防护：使用 left + (right - left) / 2 而不是 (left + right) / 2
 * 3. 异常输入处理：检查数组是否为 NULL
 */

#include <iostream>
#include <vector>

// 查找第一个等于目标值的元素
int findFirst(std::vector<int>& nums, int target) {
    // 异常处理：检查数组是否为空
    if (nums.empty()) {
        return -1;
    }
    
    int left = 0;
    int right = nums.size() - 1;
    int result = -1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        
        if (nums[mid] == target) {
            result = mid;    // 记录找到的位置
            right = mid - 1; // 继续在左半部分查找
        } else if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return result;
}

// 查找最后一个等于目标值的元素
int findLast(std::vector<int>& nums, int target) {
    // 异常处理：检查数组是否为空
    if (nums.empty()) {
        return -1;
    }
    
    int left = 0;
    int right = nums.size() - 1;
    int result = -1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        
        if (nums[mid] == target) {
            result = mid;   // 记录找到的位置
            left = mid + 1; // 继续在右半部分查找
        } else if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return result;
}

// 查找目标值的第一个和最后一个位置
std::vector<int> searchRange(std::vector<int>& nums, int target) {
    // 异常处理：检查数组是否为空
    if (nums.empty()) {
        return std::vector<int>{-1, -1};
    }
    
    // 查找第一个位置
    int first = findFirst(nums, target);
    
    // 如果第一个位置不存在，说明目标值不存在
    if (first == -1) {
        return std::vector<int>{-1, -1};
    }
    
    // 查找最后一个位置
    int last = findLast(nums, target);
    
    return std::vector<int>{first, last};
}

// 标准二分查找实现
int binarySearch(std::vector<int>& nums, int target) {
    // 异常处理：检查数组是否为空
    if (nums.empty()) {
        return -1;
    }
    
    int left = 0;
    int right = nums.size() - 1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        
        if (nums[mid] == target) {
            return mid;
        } else if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return -1;
}

// 另一种实现方式：使用一次二分查找找到任意一个目标值，然后向两边扩展
// 注意：这种方法的时间复杂度在最坏情况下是O(n)，不满足题目要求
std::vector<int> searchRangeAlternative(std::vector<int>& nums, int target) {
    // 异常处理：检查数组是否为空
    if (nums.empty()) {
        return std::vector<int>{-1, -1};
    }
    
    // 使用标准二分查找找到任意一个目标值
    int index = binarySearch(nums, target);
    
    // 如果没有找到目标值
    if (index == -1) {
        return std::vector<int>{-1, -1};
    }
    
    // 向左扩展找到第一个位置
    int left = index;
    while (left > 0 && nums[left - 1] == target) {
        left--;
    }
    
    // 向右扩展找到最后一个位置
    int right = index;
    while (right < (int)nums.size() - 1 && nums[right + 1] == target) {
        right++;
    }
    
    return std::vector<int>{left, right};
}

// 测试函数
void runTests() {
    // 测试用例1
    std::vector<int> nums1 = {5, 7, 7, 8, 8, 10};
    int target1 = 8;
    std::vector<int> result1 = searchRange(nums1, target1);
    std::cout << "测试用例1:" << std::endl;
    std::cout << "数组: [5, 7, 7, 8, 8, 10]" << std::endl;
    std::cout << "目标值: " << target1 << std::endl;
    std::cout << "结果: [" << result1[0] << ", " << result1[1] << "]" << std::endl;
    std::cout << std::endl;
    
    // 测试用例2
    std::vector<int> nums2 = {5, 7, 7, 8, 8, 10};
    int target2 = 6;
    std::vector<int> result2 = searchRange(nums2, target2);
    std::cout << "测试用例2:" << std::endl;
    std::cout << "数组: [5, 7, 7, 8, 8, 10]" << std::endl;
    std::cout << "目标值: " << target2 << std::endl;
    std::cout << "结果: [" << result2[0] << ", " << result2[1] << "]" << std::endl;
    std::cout << std::endl;
    
    // 测试用例3
    std::vector<int> nums3 = {};
    int target3 = 0;
    std::vector<int> result3 = searchRange(nums3, target3);
    std::cout << "测试用例3:" << std::endl;
    std::cout << "数组: []" << std::endl;
    std::cout << "目标值: " << target3 << std::endl;
    std::cout << "结果: [" << result3[0] << ", " << result3[1] << "]" << std::endl;
    std::cout << std::endl;
    
    // 测试用例4：单元素数组
    std::vector<int> nums4 = {1};
    int target4 = 1;
    std::vector<int> result4 = searchRange(nums4, target4);
    std::cout << "测试用例4:" << std::endl;
    std::cout << "数组: [1]" << std::endl;
    std::cout << "目标值: " << target4 << std::endl;
    std::cout << "结果: [" << result4[0] << ", " << result4[1] << "]" << std::endl;
    std::cout << std::endl;
    
    // 测试替代方法
    std::cout << "替代方法测试:" << std::endl;
    std::vector<int> result5 = searchRangeAlternative(nums1, target1);
    std::cout << "替代方法结果: [" << result5[0] << ", " << result5[1] << "]" << std::endl;
}

// 主函数
int main() {
    runTests();
    return 0;
}