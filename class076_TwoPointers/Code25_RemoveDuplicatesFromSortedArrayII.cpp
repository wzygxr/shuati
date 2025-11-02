#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>
#include <stdexcept>

/**
 * LeetCode 80. 删除有序数组中的重复项 II (Remove Duplicates from Sorted Array II)
 * 
 * 题目描述:
 * 给你一个有序数组 nums，请你原地删除重复出现的元素，使得出现次数超过两次的元素只出现两次，返回删除后数组的新长度。
 * 不要使用额外的数组空间，你必须在原地修改输入数组并在使用 O(1) 额外空间的条件下完成。
 * 
 * 示例1:
 * 输入: nums = [1,1,1,2,2,3]
 * 输出: 5, nums = [1,1,2,2,3]
 * 解释: 函数应返回新长度 length = 5，并且原数组的前五个元素被修改为 [1,1,2,2,3]。 不需要考虑数组中超出新长度后面的元素。
 * 
 * 示例2:
 * 输入: nums = [0,0,1,1,1,1,2,3,3]
 * 输出: 7, nums = [0,0,1,1,2,3,3]
 * 解释: 函数应返回新长度 length = 7，并且原数组的前七个元素被修改为 [0,0,1,1,2,3,3]。 不需要考虑数组中超出新长度后面的元素。
 * 
 * 提示:
 * 1 <= nums.length <= 3 * 10^4
 * -10^4 <= nums[i] <= 10^4
 * nums 已按升序排列
 * 
 * 题目链接: https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/
 * 
 * 解题思路:
 * 这道题是 LeetCode 26 的升级版，允许元素最多出现两次。我们可以使用快慢指针的方法来解决：
 * 
 * 方法一（快慢指针）：
 * 1. 使用 slow 指针指向当前已处理好的有效部分的下一个位置
 * 2. 使用 count 变量记录当前元素的重复次数
 * 3. 遍历数组，当发现当前元素与前一个元素相同时，增加计数；否则重置计数为1
 * 4. 如果当前元素的计数不超过2，将其移动到 slow 指向的位置，然后 slow++
 * 
 * 方法二（优化的快慢指针）：
 * 由于数组是有序的，我们可以简化为直接比较 nums[fast] 和 nums[slow-2] 的值
 * - 如果 nums[fast] != nums[slow-2]，说明当前元素可以保留，放到 slow 位置，然后 slow++
 * - 否则，说明这个元素已经出现了两次以上，跳过
 * 
 * 最优解是方法二，时间复杂度 O(n)，空间复杂度 O(1)。
 */

class Solution {
public:
    /**
     * 解法一: 快慢指针 + 计数法
     * 
     * @param nums 输入的有序数组
     * @return 删除重复元素后的新长度
     */
    int removeDuplicates(std::vector<int>& nums) {
        // 边界情况：数组长度小于等于2，所有元素都可以保留
        int n = nums.size();
        if (n <= 2) {
            return n;
        }
        
        int slow = 1; // 慢指针，指向当前已处理好的不重复元素的最后一个位置
        int count = 1; // 记录当前元素重复的次数
        
        // 从第二个元素开始遍历
        for (int fast = 1; fast < n; fast++) {
            // 如果当前元素与前一个元素相同，增加计数
            if (nums[fast] == nums[fast - 1]) {
                count++;
            } else {
                // 否则重置计数
                count = 1;
            }
            
            // 如果当前元素的计数不超过2，将其移到慢指针位置
            if (count <= 2) {
                slow++;
                // 只有当fast和slow不同时才需要复制，避免不必要的操作
                if (fast != slow - 1) {
                    nums[slow - 1] = nums[fast];
                }
            }
        }
        
        return slow;
    }
    
    /**
     * 解法二: 优化的快慢指针（最优解）
     * 
     * @param nums 输入的有序数组
     * @return 删除重复元素后的新长度
     */
    int removeDuplicatesOptimized(std::vector<int>& nums) {
        // 边界情况：数组长度小于等于2，所有元素都可以保留
        int n = nums.size();
        if (n <= 2) {
            return n;
        }
        
        // 慢指针初始化为2，因为前两个元素肯定可以保留
        int slow = 2;
        
        // 快指针从第三个元素开始遍历
        for (int fast = 2; fast < n; fast++) {
            // 如果当前元素与slow-2位置的元素不同，说明这个元素可以保留
            if (nums[fast] != nums[slow - 2]) {
                nums[slow] = nums[fast];
                slow++;
            }
        }
        
        return slow;
    }
    
    /**
     * 解法三: 通用解法，支持最多k个重复元素
     * 
     * @param nums 输入的有序数组
     * @param k 允许的最大重复次数
     * @return 删除重复元素后的新长度
     */
    int removeDuplicatesGeneric(std::vector<int>& nums, int k) {
        if (k <= 0) {
            throw std::invalid_argument("k必须为正整数");
        }
        
        // 边界情况：数组长度小于等于k，所有元素都可以保留
        int n = nums.size();
        if (n <= k) {
            return n;
        }
        
        // 慢指针初始化为k，因为前k个元素肯定可以保留
        int slow = k;
        
        // 快指针从第k+1个元素开始遍历
        for (int fast = k; fast < n; fast++) {
            // 如果当前元素与slow-k位置的元素不同，说明这个元素可以保留
            if (nums[fast] != nums[slow - k]) {
                nums[slow] = nums[fast];
                slow++;
            }
        }
        
        return slow;
    }
};

/**
 * 打印数组的前len个元素
 */
void printArray(const std::vector<int>& nums, int len) {
    std::cout << "[";
    for (int i = 0; i < len; i++) {
        std::cout << nums[i];
        if (i < len - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
}

/**
 * 验证结果是否正确
 */
bool validateResult(const std::vector<int>& original, const std::vector<int>& expected, int expectedLen) {
    // 检查长度
    if (expectedLen != expected.size()) {
        return false;
    }
    
    // 检查前expectedLen个元素
    for (int i = 0; i < expectedLen; i++) {
        if (original[i] != expected[i]) {
            return false;
        }
    }
    
    // 检查数组是否仍然有序
    for (int i = 1; i < expectedLen; i++) {
        if (original[i] < original[i - 1]) {
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
    std::vector<int> nums1 = {1, 1, 1, 2, 2, 3};
    std::vector<int> expected1 = {1, 1, 2, 2, 3};
    std::cout << "测试用例1:" << std::endl;
    std::cout << "原数组: [";
    for (size_t i = 0; i < nums1.size(); i++) {
        std::cout << nums1[i];
        if (i < nums1.size() - 1) std::cout << ", ";
    }
    std::cout << "]" << std::endl;
    int len1 = solution.removeDuplicatesOptimized(nums1);
    std::cout << "处理后: ";
    printArray(nums1, len1);
    std::cout << "长度: " << len1 << std::endl;
    std::cout << "验证结果: " << (validateResult(nums1, expected1, len1) ? "通过" : "失败") << std::endl;
    std::cout << std::endl;
    
    // 测试用例2
    std::vector<int> nums2 = {0, 0, 1, 1, 1, 1, 2, 3, 3};
    std::vector<int> expected2 = {0, 0, 1, 1, 2, 3, 3};
    std::cout << "测试用例2:" << std::endl;
    std::cout << "原数组: [";
    for (size_t i = 0; i < nums2.size(); i++) {
        std::cout << nums2[i];
        if (i < nums2.size() - 1) std::cout << ", ";
    }
    std::cout << "]" << std::endl;
    int len2 = solution.removeDuplicatesOptimized(nums2);
    std::cout << "处理后: ";
    printArray(nums2, len2);
    std::cout << "长度: " << len2 << std::endl;
    std::cout << "验证结果: " << (validateResult(nums2, expected2, len2) ? "通过" : "失败") << std::endl;
    std::cout << std::endl;
    
    // 测试用例3 - 边界情况：数组长度小于等于2
    std::vector<int> nums3 = {1, 1};
    std::cout << "测试用例3（数组长度为2）:" << std::endl;
    std::cout << "原数组: [";
    for (size_t i = 0; i < nums3.size(); i++) {
        std::cout << nums3[i];
        if (i < nums3.size() - 1) std::cout << ", ";
    }
    std::cout << "]" << std::endl;
    int len3 = solution.removeDuplicatesOptimized(nums3);
    std::cout << "处理后: ";
    printArray(nums3, len3);
    std::cout << "长度: " << len3 << std::endl;
    std::cout << std::endl;
    
    // 测试用例4 - 边界情况：所有元素都相同
    std::vector<int> nums4 = {2, 2, 2, 2, 2};
    std::vector<int> expected4 = {2, 2};
    std::cout << "测试用例4（所有元素相同）:" << std::endl;
    std::cout << "原数组: [";
    for (size_t i = 0; i < nums4.size(); i++) {
        std::cout << nums4[i];
        if (i < nums4.size() - 1) std::cout << ", ";
    }
    std::cout << "]" << std::endl;
    int len4 = solution.removeDuplicatesOptimized(nums4);
    std::cout << "处理后: ";
    printArray(nums4, len4);
    std::cout << "长度: " << len4 << std::endl;
    std::cout << "验证结果: " << (validateResult(nums4, expected4, len4) ? "通过" : "失败") << std::endl;
    std::cout << std::endl;
    
    // 测试用例5 - 边界情况：没有重复元素
    std::vector<int> nums5 = {1, 2, 3, 4, 5};
    std::cout << "测试用例5（无重复元素）:" << std::endl;
    std::cout << "原数组: [";
    for (size_t i = 0; i < nums5.size(); i++) {
        std::cout << nums5[i];
        if (i < nums5.size() - 1) std::cout << ", ";
    }
    std::cout << "]" << std::endl;
    int len5 = solution.removeDuplicatesOptimized(nums5);
    std::cout << "处理后: ";
    printArray(nums5, len5);
    std::cout << "长度: " << len5 << std::endl;
    std::cout << "验证结果: " << (validateResult(nums5, std::vector<int>(nums5.begin(), nums5.begin() + len5), len5) ? "通过" : "失败") << std::endl;
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
    
    // 生成测试数据：交替重复元素
    for (int i = 0; i < size; i++) {
        largeArray.push_back(i / 10);  // 每个数字重复10次
    }
    
    // 测试解法一的性能
    std::vector<int> array1(largeArray.begin(), largeArray.end());
    auto startTime = std::chrono::high_resolution_clock::now();
    int len1 = solution.removeDuplicates(array1);
    auto endTime = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
    std::cout << "解法一（快慢指针+计数）耗时: " << duration.count() << "ms, 处理后长度: " << len1 << std::endl;
    
    // 测试解法二的性能
    std::vector<int> array2(largeArray.begin(), largeArray.end());
    startTime = std::chrono::high_resolution_clock::now();
    int len2 = solution.removeDuplicatesOptimized(array2);
    endTime = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
    std::cout << "解法二（优化快慢指针）耗时: " << duration.count() << "ms, 处理后长度: " << len2 << std::endl;
    
    // 测试解法三的性能
    std::vector<int> array3(largeArray.begin(), largeArray.end());
    startTime = std::chrono::high_resolution_clock::now();
    int len3 = solution.removeDuplicatesGeneric(array3, 2);
    endTime = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
    std::cout << "解法三（通用解法）耗时: " << duration.count() << "ms, 处理后长度: " << len3 << std::endl;
    
    // 验证所有解法结果一致
    bool resultsConsistent = (len1 == len2 && len2 == len3);
    if (resultsConsistent) {
        for (int i = 0; i < len1; i++) {
            if (array1[i] != array2[i] || array1[i] != array3[i]) {
                resultsConsistent = false;
                break;
            }
        }
    }
    std::cout << "所有解法结果一致: " << (resultsConsistent ? "是" : "否") << std::endl;
}

int main() {
    std::cout << "=== 测试用例 ===" << std::endl;
    test();
    
    std::cout << "=== 性能测试 ===" << std::endl;
    performanceTest();
    
    return 0;
}