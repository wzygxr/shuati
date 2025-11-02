/**
 * LeetCode 153. 寻找旋转排序数组中的最小值 (C++版本)
 * 
 * 题目描述：
 * 已知一个长度为 n 的数组，预先按照升序排列，经由 1 到 n 次旋转后，得到输入数组。
 * 例如，原数组 nums = [0,1,2,4,5,6,7] 在变化后可能得到：
 * 若旋转 4 次，则可以得到 [4,5,6,7,0,1,2]
 * 若旋转 7 次，则可以得到 [0,1,2,4,5,6,7]
 * 注意，数组 [a[0], a[1], a[2], ..., a[n-1]] 旋转一次的结果为数组 [a[n-1], a[1], a[2], ..., a[n-2]] 。
 * 给你一个元素值互不相同的数组 nums，它原来是一个升序排列的数组，并按上述情形进行了多次旋转。
 * 请你找出并返回数组中的最小元素。
 * 必须设计一个时间复杂度为 O(log n) 的算法解决此问题。
 * 
 * 示例：
 * 输入：nums = [3,4,5,1,2]
 * 输出：1
 * 解释：原数组为 [1,2,3,4,5] ，旋转 3 次得到输入数组。
 * 
 * 输入：nums = [4,5,6,7,0,1,2]
 * 输出：0
 * 解释：原数组为 [0,1,2,4,5,6,7] ，旋转 3 次得到输入数组。
 * 
 * 输入：nums = [11,13,15,17]
 * 输出：11
 * 解释：原数组为 [11,13,15,17] ，旋转 4 次得到输入数组。
 * 
 * 约束条件：
 * - n == nums.length
 * - 1 <= n <= 5000
 * - -5000 <= nums[i] <= 5000
 * - nums 中的所有整数互不相同
 * - nums 原来是一个升序排序的数组，并进行了 1 至 n 次旋转
 * 
 * 解题思路：
 * 这是二分查找在旋转数组中的应用。旋转后的数组可以分为两个有序部分，
 * 最小值位于第二个有序部分的开头。
 * 我们可以通过比较中间元素与右边界元素的大小关系来判断最小值在哪一部分。
 * 
 * 时间复杂度：O(log n)，其中 n 是数组的长度。
 * 空间复杂度：O(1)，只使用了常数级别的额外空间。
 * 
 * 工程化考量：
 * 1. 边界条件处理：单元素数组、未旋转数组
 * 2. 整数溢出防护：使用 left + (right - left) / 2 而不是 (left + right) / 2
 * 3. 异常输入处理：检查数组是否为 NULL
 */

#include <iostream>
#include <vector>
#include <stdexcept>

// 寻找旋转排序数组中的最小值
int findMin(std::vector<int>& nums) {
    // 异常处理：检查数组是否为空
    if (nums.empty()) {
        throw std::invalid_argument("数组不能为空");
    }
    
    // 初始化左右边界
    int left = 0;
    int right = nums.size() - 1;
    
    // 如果数组没有旋转，直接返回第一个元素
    if (nums[left] < nums[right]) {
        return nums[left];
    }
    
    // 二分查找最小值
    while (left < right) {
        // 防止整数溢出的中点计算方式
        int mid = left + (right - left) / 2;
        
        // 如果中间元素大于右边界元素，说明最小值在右半部分
        if (nums[mid] > nums[right]) {
            left = mid + 1;
        } 
        // 如果中间元素小于右边界元素，说明最小值在左半部分（包括mid）
        else if (nums[mid] < nums[right]) {
            right = mid;
        }
        // 这种情况不会出现，因为题目说明所有元素互不相同
        else {
            // 为了代码完整性，处理相等的情况
            right = mid;
        }
    }
    
    // 循环结束时，left == right，指向最小值
    return nums[left];
}

// 另一种实现方式：比较中间元素与左边界元素
int findMinAlternative(std::vector<int>& nums) {
    // 异常处理：检查数组是否为空
    if (nums.empty()) {
        throw std::invalid_argument("数组不能为空");
    }
    
    // 初始化左右边界
    int left = 0;
    int right = nums.size() - 1;
    
    // 如果数组没有旋转，直接返回第一个元素
    if (nums[left] < nums[right]) {
        return nums[left];
    }
    
    // 二分查找最小值
    while (left < right) {
        // 防止整数溢出的中点计算方式
        int mid = left + (right - left) / 2;
        
        // 如果中间元素大于左边界元素，说明最小值在右半部分
        if (nums[mid] > nums[left]) {
            // 特殊情况：如果左边界元素小于右边界元素，说明最小值是左边界元素
            if (nums[left] < nums[right]) {
                return nums[left];
            }
            left = mid + 1;
        } 
        // 如果中间元素小于左边界元素，说明最小值在左半部分（包括mid）
        else if (nums[mid] < nums[left]) {
            right = mid;
        }
        // 这种情况不会出现，因为题目说明所有元素互不相同
        else {
            // 为了代码完整性，处理相等的情况
            left = mid + 1;
        }
    }
    
    // 循环结束时，left == right，指向最小值
    return nums[left];
}

// 线性查找方法（用于对比和验证）
// 时间复杂度：O(n)
int findMinLinear(std::vector<int>& nums) {
    // 异常处理：检查数组是否为空
    if (nums.empty()) {
        throw std::invalid_argument("数组不能为空");
    }
    
    int min_val = nums[0];
    for (size_t i = 1; i < nums.size(); i++) {
        if (nums[i] < min_val) {
            min_val = nums[i];
        }
    }
    
    return min_val;
}

// 测试函数
void runTests() {
    // 测试用例1
    std::vector<int> nums1 = {3, 4, 5, 1, 2};
    int result1 = findMin(nums1);
    std::cout << "测试用例1:" << std::endl;
    std::cout << "数组: [3, 4, 5, 1, 2]" << std::endl;
    std::cout << "结果: " << result1 << std::endl;
    std::cout << std::endl;
    
    // 测试用例2
    std::vector<int> nums2 = {4, 5, 6, 7, 0, 1, 2};
    int result2 = findMin(nums2);
    std::cout << "测试用例2:" << std::endl;
    std::cout << "数组: [4, 5, 6, 7, 0, 1, 2]" << std::endl;
    std::cout << "结果: " << result2 << std::endl;
    std::cout << std::endl;
    
    // 测试用例3
    std::vector<int> nums3 = {11, 13, 15, 17};
    int result3 = findMin(nums3);
    std::cout << "测试用例3:" << std::endl;
    std::cout << "数组: [11, 13, 15, 17]" << std::endl;
    std::cout << "结果: " << result3 << std::endl;
    std::cout << std::endl;
    
    // 测试用例4：单元素数组
    std::vector<int> nums4 = {5};
    int result4 = findMin(nums4);
    std::cout << "测试用例4:" << std::endl;
    std::cout << "数组: [5]" << std::endl;
    std::cout << "结果: " << result4 << std::endl;
    std::cout << std::endl;
    
    // 测试用例5：两元素数组
    std::vector<int> nums5 = {2, 1};
    int result5 = findMin(nums5);
    std::cout << "测试用例5:" << std::endl;
    std::cout << "数组: [2, 1]" << std::endl;
    std::cout << "结果: " << result5 << std::endl;
    std::cout << std::endl;
    
    // 测试替代方法
    std::cout << "替代方法测试:" << std::endl;
    int result6 = findMinAlternative(nums2);
    std::cout << "替代方法结果: " << result6 << std::endl;
    
    // 测试线性方法
    std::cout << "线性方法测试:" << std::endl;
    int result7 = findMinLinear(nums2);
    std::cout << "线性方法结果: " << result7 << std::endl;
}

// 主函数
int main() {
    runTests();
    return 0;
}