#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>

/**
 * LeetCode 27. 移除元素 (Remove Element)
 * 
 * 题目描述:
 * 给你一个数组 nums 和一个值 val，你需要原地移除所有数值等于 val 的元素，并返回移除后数组的新长度。
 * 不要使用额外的数组空间，你必须仅使用 O(1) 额外空间并原地修改输入数组。
 * 元素的顺序可以改变。你不需要考虑数组中超出新长度后面的元素。
 * 
 * 示例1:
 * 输入: nums = [3,2,2,3], val = 3
 * 输出: 2, nums = [2,2]
 * 解释: 函数应该返回新的长度 2, 并且 nums 中的前两个元素均为 2。
 * 不需要考虑数组中超出新长度后面的元素。例如，函数返回的新长度为 2 ，
 * 而 nums = [2,2,3,3] 或 nums = [2,2,0,0]，也会被视作正确答案。
 * 
 * 示例2:
 * 输入: nums = [0,1,2,2,3,0,4,2], val = 2
 * 输出: 5, nums = [0,1,4,0,3]
 * 解释: 函数应该返回新的长度 5, 并且 nums 中的前五个元素为 0, 1, 3, 0, 4。
 * 注意这五个元素可为任意顺序。你不需要考虑数组中超出新长度后面的元素。
 * 
 * 提示:
 * 0 <= nums.length <= 100
 * 0 <= nums[i] <= 50
 * 0 <= val <= 100
 * 
 * 题目链接: https://leetcode.com/problems/remove-element/
 * 
 * 解题思路:
 * 这道题与删除有序数组中的重复项类似，都可以使用双指针技术。
 * 我们可以使用一个慢指针来跟踪应该放置下一个非val元素的位置，
 * 然后用一个快指针来遍历整个数组。
 * 当快指针找到一个不等于val的元素时，我们将该元素放到慢指针指向的位置，然后将慢指针向前移动一位。
 * 
 * 时间复杂度: O(n)，其中n是数组的长度。快指针最多遍历数组一次。
 * 空间复杂度: O(1)，只使用了常数级别的额外空间。
 */

class Solution {
public:
    /**
     * 解法一: 双指针（最优解）
     * 
     * @param nums 输入数组
     * @param val 要移除的元素值
     * @return 移除后数组的新长度
     */
    int removeElement(std::vector<int>& nums, int val) {
        int slow = 0; // 慢指针，指向下一个非val元素应该放置的位置
        
        // 快指针遍历整个数组
        for (int fast = 0; fast < nums.size(); fast++) {
            // 如果当前元素不等于val，则将其移到慢指针位置，并将慢指针向前移动
            if (nums[fast] != val) {
                nums[slow] = nums[fast];
                slow++;
            }
        }
        
        // 慢指针的值就是新数组的长度
        return slow;
    }
    
    /**
     * 解法二: 优化的双指针（当需要删除的元素很少时）
     * 
     * 当要删除的元素很少时，我们可以将不等于val的元素保留，而将等于val的元素与数组末尾的元素交换，
     * 然后减少数组的长度。这样可以减少不必要的赋值操作。
     * 
     * @param nums 输入数组
     * @param val 要移除的元素值
     * @return 移除后数组的新长度
     */
    int removeElementOptimized(std::vector<int>& nums, int val) {
        int left = 0;
        int right = nums.size();
        
        while (left < right) {
            if (nums[left] == val) {
                // 将当前元素与数组末尾元素交换
                nums[left] = nums[right - 1];
                // 减少数组长度
                right--;
            } else {
                // 当前元素不等于val，保留它
                left++;
            }
        }
        
        return right;
    }
    
    /**
     * 解法三: 简洁版双指针
     * 
     * @param nums 输入数组
     * @param val 要移除的元素值
     * @return 移除后数组的新长度
     */
    int removeElementConcise(std::vector<int>& nums, int val) {
        int index = 0;
        
        for (int i = 0; i < nums.size(); i++) {
            if (nums[i] != val) {
                nums[index++] = nums[i];
            }
        }
        
        return index;
    }
};

/**
 * 打印数组的前k个元素
 */
void printArray(const std::vector<int>& nums, int k) {
    std::cout << "[";
    for (int i = 0; i < k; i++) {
        std::cout << nums[i];
        if (i < k - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
}

/**
 * 打印完整的vector数组
 */
void printFullArray(const std::vector<int>& nums) {
    std::cout << "[";
    for (size_t i = 0; i < nums.size(); i++) {
        std::cout << nums[i];
        if (i < nums.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
}

/**
 * 测试函数
 */
void test() {
    Solution solution;
    
    // 测试用例1
    std::vector<int> nums1 = {3, 2, 2, 3};
    int val1 = 3;
    std::cout << "测试用例1:" << std::endl;
    std::cout << "原始数组: ";
    printFullArray(nums1);
    std::cout << "要移除的值: " << val1 << std::endl;
    int length1 = solution.removeElement(nums1, val1);
    std::cout << "新长度: " << length1 << std::endl;
    std::cout << "新数组前" << length1 << "个元素: ";
    printArray(nums1, length1);
    std::cout << std::endl;
    
    // 测试用例2
    std::vector<int> nums2 = {0, 1, 2, 2, 3, 0, 4, 2};
    int val2 = 2;
    std::cout << "测试用例2:" << std::endl;
    std::cout << "原始数组: ";
    printFullArray(nums2);
    std::cout << "要移除的值: " << val2 << std::endl;
    int length2 = solution.removeElement(nums2, val2);
    std::cout << "新长度: " << length2 << std::endl;
    std::cout << "新数组前" << length2 << "个元素: ";
    printArray(nums2, length2);
    std::cout << std::endl;
    
    // 测试用例3 - 边界情况：空数组
    std::vector<int> nums3;
    int val3 = 0;
    std::cout << "测试用例3（空数组）:" << std::endl;
    std::cout << "原始数组: []" << std::endl;
    std::cout << "要移除的值: " << val3 << std::endl;
    int length3 = solution.removeElement(nums3, val3);
    std::cout << "新长度: " << length3 << std::endl;
    std::cout << "新数组前" << length3 << "个元素: []" << std::endl;
    std::cout << std::endl;
    
    // 测试用例4 - 边界情况：所有元素都等于val
    std::vector<int> nums4 = {5, 5, 5, 5};
    int val4 = 5;
    std::cout << "测试用例4（全等于val的数组）:" << std::endl;
    std::cout << "原始数组: ";
    printFullArray(nums4);
    std::cout << "要移除的值: " << val4 << std::endl;
    int length4 = solution.removeElement(nums4, val4);
    std::cout << "新长度: " << length4 << std::endl;
    std::cout << "新数组前" << length4 << "个元素: []" << std::endl;
    std::cout << std::endl;
    
    // 测试用例5 - 边界情况：没有元素等于val
    std::vector<int> nums5 = {1, 2, 3, 4, 5};
    int val5 = 6;
    std::cout << "测试用例5（无等于val的元素）:" << std::endl;
    std::cout << "原始数组: ";
    printFullArray(nums5);
    std::cout << "要移除的值: " << val5 << std::endl;
    int length5 = solution.removeElement(nums5, val5);
    std::cout << "新长度: " << length5 << std::endl;
    std::cout << "新数组前" << length5 << "个元素: ";
    printArray(nums5, length5);
    std::cout << std::endl;
}

/**
 * 性能测试
 */
void performanceTest() {
    Solution solution;
    
    // 创建一个大数组进行性能测试
    int size = 1000000;
    std::vector<int> largeArray;
    largeArray.reserve(size);
    
    // 填充数组，其中约20%的元素等于val
    int val = 5;
    for (int i = 0; i < size; i++) {
        largeArray.push_back(i % 10 == 0 ? val : i % 10);
    }
    
    // 测试解法一的性能
    std::vector<int> array1(largeArray.begin(), largeArray.end());
    auto startTime = std::chrono::high_resolution_clock::now();
    int result1 = solution.removeElement(array1, val);
    auto endTime = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
    std::cout << "解法一耗时: " << duration.count() << "ms, 结果长度: " << result1 << std::endl;
    
    // 测试解法二的性能
    std::vector<int> array2(largeArray.begin(), largeArray.end());
    startTime = std::chrono::high_resolution_clock::now();
    int result2 = solution.removeElementOptimized(array2, val);
    endTime = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
    std::cout << "解法二耗时: " << duration.count() << "ms, 结果长度: " << result2 << std::endl;
    
    // 测试解法三的性能
    std::vector<int> array3(largeArray.begin(), largeArray.end());
    startTime = std::chrono::high_resolution_clock::now();
    int result3 = solution.removeElementConcise(array3, val);
    endTime = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
    std::cout << "解法三耗时: " << duration.count() << "ms, 结果长度: " << result3 << std::endl;
    
    // 验证所有解法结果一致
    std::cout << "所有解法结果一致: " << (result1 == result2 && result2 == result3 ? "是" : "否") << std::endl;
}

int main() {
    std::cout << "=== 测试用例 ===" << std::endl;
    test();
    
    std::cout << "=== 性能测试 ===" << std::endl;
    performanceTest();
    
    return 0;
}