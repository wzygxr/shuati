#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>
#include <stdexcept>

/**
 * LeetCode 75. 颜色分类 (Sort Colors)
 * 
 * 题目描述:
 * 给定一个包含红色、白色和蓝色、共 n 个元素的数组 nums，原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。
 * 我们使用整数 0、1 和 2 分别表示红色、白色和蓝色。
 * 必须在不使用库内置的 sort 函数的情况下解决这个问题。
 * 
 * 示例1:
 * 输入: nums = [2,0,2,1,1,0]
 * 输出: [0,0,1,1,2,2]
 * 
 * 示例2:
 * 输入: nums = [2,0,1]
 * 输出: [0,1,2]
 * 
 * 提示:
 * n == nums.length
 * 1 <= n <= 300
 * nums[i] 为 0、1 或 2
 * 
 * 题目链接: https://leetcode.com/problems/sort-colors/
 * 
 * 解题思路:
 * 这道题是一个经典的「荷兰国旗问题」，可以使用双指针或者三指针技术来解决。
 * 
 * 方法一（三指针法）：
 * 1. 使用三个指针：left（指向0的右边界）、mid（当前处理的元素）、right（指向2的左边界）
 * 2. 初始化left=0，mid=0，right=nums.length-1
 * 3. 当mid<=right时，根据nums[mid]的值进行不同的处理：
 *    - 如果nums[mid]==0，交换nums[left]和nums[mid]，然后left++，mid++
 *    - 如果nums[mid]==1，mid++
 *    - 如果nums[mid]==2，交换nums[mid]和nums[right]，然后right--（注意此时mid不增加，因为交换后的元素还未处理）
 * 
 * 方法二（两次遍历）：
 * 1. 第一次遍历统计0、1、2的个数
 * 2. 第二次遍历根据统计结果填充数组
 * 
 * 最优解是三指针法，时间复杂度O(n)，空间复杂度O(1)，且只需要一次遍历。
 */

class Solution {
public:
    /**
     * 解法一: 三指针法（最优解）
     * 
     * @param nums 输入数组，只包含0、1、2三种元素
     */
    void sortColors(std::vector<int>& nums) {
        int left = 0;      // 0的右边界
        int mid = 0;       // 当前处理的元素
        int right = nums.size() - 1;  // 2的左边界
        
        while (mid <= right) {
            switch (nums[mid]) {
                case 0:
                    // 当前元素是0，放到left指针位置
                    std::swap(nums[left], nums[mid]);
                    left++;
                    mid++;
                    break;
                case 1:
                    // 当前元素是1，已经在正确位置
                    mid++;
                    break;
                case 2:
                    // 当前元素是2，放到right指针位置
                    std::swap(nums[mid], nums[right]);
                    right--;
                    // 注意：此时mid不增加，因为交换后的元素还未处理
                    break;
                default:
                    // 非法输入检查
                    throw std::invalid_argument("输入数组包含非法元素，只能包含0、1、2");
            }
        }
    }
    
    /**
     * 解法二: 两次遍历（计数排序思想）
     * 
     * @param nums 输入数组，只包含0、1、2三种元素
     */
    void sortColorsTwoPass(std::vector<int>& nums) {
        int count0 = 0, count1 = 0, count2 = 0;
        
        // 第一次遍历：统计0、1、2的个数
        for (int num : nums) {
            switch (num) {
                case 0:
                    count0++;
                    break;
                case 1:
                    count1++;
                    break;
                case 2:
                    count2++;
                    break;
                default:
                    throw std::invalid_argument("输入数组包含非法元素，只能包含0、1、2");
            }
        }
        
        // 第二次遍历：根据统计结果填充数组
        int index = 0;
        while (count0 > 0) {
            nums[index++] = 0;
            count0--;
        }
        while (count1 > 0) {
            nums[index++] = 1;
            count1--;
        }
        while (count2 > 0) {
            nums[index++] = 2;
            count2--;
        }
    }
    
    /**
     * 解法三: 双指针优化版
     * 
     * @param nums 输入数组，只包含0、1、2三种元素
     */
    void sortColorsTwoPointers(std::vector<int>& nums) {
        // 先将所有的0移到数组前面
        int p0 = 0;
        for (int i = 0; i < nums.size(); i++) {
            if (nums[i] == 0) {
                std::swap(nums[i], nums[p0]);
                p0++;
            }
        }
        
        // 再将所有的1移到0之后
        int p1 = p0;
        for (int i = p0; i < nums.size(); i++) {
            if (nums[i] == 1) {
                std::swap(nums[i], nums[p1]);
                p1++;
            }
        }
    }
};

/**
 * 打印数组
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

/**
 * 验证排序结果是否正确
 */
bool isSorted(const std::vector<int>& nums) {
    for (size_t i = 1; i < nums.size(); i++) {
        if (nums[i] < nums[i - 1]) {
            return false;
        }
    }
    return true;
}

/**
 * 测试函数
 */
void test() {
    Solution solution;
    
    // 测试用例1
    std::vector<int> nums1 = {2, 0, 2, 1, 1, 0};
    std::cout << "测试用例1:" << std::endl;
    std::cout << "排序前: ";
    printArray(nums1);
    solution.sortColors(nums1);
    std::cout << "排序后: ";
    printArray(nums1);
    std::cout << std::endl;
    
    // 测试用例2
    std::vector<int> nums2 = {2, 0, 1};
    std::cout << "测试用例2:" << std::endl;
    std::cout << "排序前: ";
    printArray(nums2);
    solution.sortColors(nums2);
    std::cout << "排序后: ";
    printArray(nums2);
    std::cout << std::endl;
    
    // 测试用例3 - 边界情况：只有一个元素
    std::vector<int> nums3 = {0};
    std::cout << "测试用例3（单元素数组）:" << std::endl;
    std::cout << "排序前: ";
    printArray(nums3);
    solution.sortColors(nums3);
    std::cout << "排序后: ";
    printArray(nums3);
    std::cout << std::endl;
    
    // 测试用例4 - 边界情况：已经排序的数组
    std::vector<int> nums4 = {0, 0, 1, 1, 2, 2};
    std::cout << "测试用例4（已排序数组）:" << std::endl;
    std::cout << "排序前: ";
    printArray(nums4);
    solution.sortColors(nums4);
    std::cout << "排序后: ";
    printArray(nums4);
    std::cout << std::endl;
    
    // 测试用例5 - 边界情况：逆序排列的数组
    std::vector<int> nums5 = {2, 2, 1, 1, 0, 0};
    std::cout << "测试用例5（逆序数组）:" << std::endl;
    std::cout << "排序前: ";
    printArray(nums5);
    solution.sortColors(nums5);
    std::cout << "排序后: ";
    printArray(nums5);
    std::cout << std::endl;
    
    // 测试用例6 - 边界情况：所有元素都相同
    std::vector<int> nums6 = {1, 1, 1, 1};
    std::cout << "测试用例6（全相同元素）:" << std::endl;
    std::cout << "排序前: ";
    printArray(nums6);
    solution.sortColors(nums6);
    std::cout << "排序后: ";
    printArray(nums6);
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
    
    // 随机填充0、1、2
    for (int i = 0; i < size; i++) {
        largeArray.push_back(i % 3);
    }
    
    // 测试解法一的性能
    std::vector<int> array1(largeArray.begin(), largeArray.end());
    auto startTime = std::chrono::high_resolution_clock::now();
    solution.sortColors(array1);
    auto endTime = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
    std::cout << "解法一（三指针）耗时: " << duration.count() << "ms" << std::endl;
    
    // 测试解法二的性能
    std::vector<int> array2(largeArray.begin(), largeArray.end());
    startTime = std::chrono::high_resolution_clock::now();
    solution.sortColorsTwoPass(array2);
    endTime = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
    std::cout << "解法二（两次遍历）耗时: " << duration.count() << "ms" << std::endl;
    
    // 测试解法三的性能
    std::vector<int> array3(largeArray.begin(), largeArray.end());
    startTime = std::chrono::high_resolution_clock::now();
    solution.sortColorsTwoPointers(array3);
    endTime = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
    std::cout << "解法三（双指针优化）耗时: " << duration.count() << "ms" << std::endl;
    
    // 验证所有解法结果一致且已排序
    bool resultsConsistent = true;
    for (int i = 0; i < size; i++) {
        if (array1[i] != array2[i] || array1[i] != array3[i]) {
            resultsConsistent = false;
            break;
        }
    }
    std::cout << "所有解法结果一致: " << (resultsConsistent ? "是" : "否") << std::endl;
    std::cout << "排序正确: " << (isSorted(array1) ? "是" : "否") << std::endl;
}

int main() {
    std::cout << "=== 测试用例 ===" << std::endl;
    test();
    
    std::cout << "=== 性能测试 ===" << std::endl;
    performanceTest();
    
    return 0;
}