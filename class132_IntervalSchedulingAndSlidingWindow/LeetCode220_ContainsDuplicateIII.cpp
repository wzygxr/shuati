#include <iostream>
#include <vector>
#include <set>
#include <cmath>
#include <climits>

/**
 * LeetCode 220. 存在重复元素 III
 * 
 * 题目描述：
 * 给你一个整数数组 nums 和两个整数 indexDiff 和 valueDiff 。
 * 找出满足下述条件的下标对 (i, j)：
 * 1. i != j
 * 2. abs(i - j) <= indexDiff
 * 3. abs(nums[i] - nums[j]) <= valueDiff
 * 如果存在，返回 true ；否则，返回 false 。
 * 
 * 解题思路：
 * 这是一个滑动窗口结合平衡二叉搜索树（set）的问题。
 * 
 * 核心思想：
 * 1. 使用滑动窗口维护最多k+1个元素（k=indexDiff）
 * 2. 使用set维护窗口内元素的有序性
 * 3. 对于每个新元素，检查是否存在满足条件的旧元素
 * 
 * 具体步骤：
 * 1. 遍历数组，维护一个大小为k+1的滑动窗口
 * 2. 对于窗口中的每个元素，使用set的lower_bound和upper_bound方法查找值域范围内最近的元素
 * 3. 如果找到满足条件的元素，返回true
 * 4. 当窗口大小超过k+1时，移除最早加入的元素
 * 
 * 时间复杂度：O(n log k)
 * 空间复杂度：O(k)
 * 
 * 相关题目：
 * - LeetCode 219. 存在重复元素 II
 * - LeetCode 287. 寻找重复数
 * - LeetCode 448. 找到所有数组中消失的数字
 */
class Solution {
public:
    /**
     * 判断是否存在满足条件的下标对
     * 
     * @param nums 整数数组
     * @param indexDiff 下标差的最大值
     * @param valueDiff 值差的最大值
     * @return 是否存在满足条件的下标对
     */
    bool containsNearbyAlmostDuplicate(std::vector<int>& nums, int indexDiff, int valueDiff) {
        // 使用set维护滑动窗口内元素的有序性
        std::set<long long> window;
        
        for (int i = 0; i < nums.size(); i++) {
            long long current = static_cast<long long>(nums[i]);
            
            // 在set中查找是否存在满足条件的元素
            // lower_bound返回大于等于x的最小元素
            auto lower = window.lower_bound(current - valueDiff);
            // 如果找到满足条件的元素，返回true
            if (lower != window.end() && *lower <= current + valueDiff) {
                return true;
            }
            
            // 将当前元素加入窗口
            window.insert(current);
            
            // 如果窗口大小超过indexDiff+1，移除最早加入的元素
            if (window.size() > indexDiff + 1) {
                window.erase(static_cast<long long>(nums[i - indexDiff - 1]));
            }
        }
        
        return false;
    }
};

/**
 * 测试函数
 */
void testContainsNearbyAlmostDuplicate() {
    Solution solution;
    
    // 测试用例1
    std::vector<int> nums1 = {1, 2, 3, 1};
    int indexDiff1 = 3;
    int valueDiff1 = 0;
    std::cout << "测试用例1:" << std::endl;
    std::cout << "输入: nums = [1,2,3,1], indexDiff = " << indexDiff1 << ", valueDiff = " << valueDiff1 << std::endl;
    std::cout << "输出: " << (solution.containsNearbyAlmostDuplicate(nums1, indexDiff1, valueDiff1) ? "true" : "false") << std::endl; // 期望输出: true
    
    // 测试用例2
    std::vector<int> nums2 = {1, 5, 9, 1, 5, 9};
    int indexDiff2 = 2;
    int valueDiff2 = 3;
    std::cout << "\n测试用例2:" << std::endl;
    std::cout << "输入: nums = [1,5,9,1,5,9], indexDiff = " << indexDiff2 << ", valueDiff = " << valueDiff2 << std::endl;
    std::cout << "输出: " << (solution.containsNearbyAlmostDuplicate(nums2, indexDiff2, valueDiff2) ? "true" : "false") << std::endl; // 期望输出: false
    
    // 测试用例3
    std::vector<int> nums3 = {1, 0, 1, 1};
    int indexDiff3 = 1;
    int valueDiff3 = 2;
    std::cout << "\n测试用例3:" << std::endl;
    std::cout << "输入: nums = [1,0,1,1], indexDiff = " << indexDiff3 << ", valueDiff = " << valueDiff3 << std::endl;
    std::cout << "输出: " << (solution.containsNearbyAlmostDuplicate(nums3, indexDiff3, valueDiff3) ? "true" : "false") << std::endl; // 期望输出: true
    
    // 边界测试用例4：空数组
    std::vector<int> nums4 = {};
    int indexDiff4 = 1;
    int valueDiff4 = 1;
    std::cout << "\n测试用例4:" << std::endl;
    std::cout << "输入: nums = [], indexDiff = " << indexDiff4 << ", valueDiff = " << valueDiff4 << std::endl;
    std::cout << "输出: " << (solution.containsNearbyAlmostDuplicate(nums4, indexDiff4, valueDiff4) ? "true" : "false") << std::endl; // 期望输出: false
    
    // 边界测试用例5：单个元素
    std::vector<int> nums5 = {1};
    int indexDiff5 = 0;
    int valueDiff5 = 0;
    std::cout << "\n测试用例5:" << std::endl;
    std::cout << "输入: nums = [1], indexDiff = " << indexDiff5 << ", valueDiff = " << valueDiff5 << std::endl;
    std::cout << "输出: " << (solution.containsNearbyAlmostDuplicate(nums5, indexDiff5, valueDiff5) ? "true" : "false") << std::endl; // 期望输出: false
}

/**
 * 主函数
 */
int main() {
    std::cout << "=== LeetCode 220. 存在重复元素 III C++实现测试 ===" << std::endl;
    testContainsNearbyAlmostDuplicate();
    std::cout << "\n=== 测试完成 ===" << std::endl;
    return 0;
}