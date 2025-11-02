#include <iostream>
#include <vector>
#include <deque>
#include <algorithm>
#include <climits>
#include <chrono>

using namespace std;

/**
 * 题目名称：LeetCode 1438. 绝对差不超过限制的最长连续子数组
 * 题目来源：LeetCode
 * 题目链接：https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
 * 题目难度：中等
 * 
 * 题目描述：
 * 给你一个整数数组 nums ，和一个表示限制的整数 limit，
 * 请你返回最长连续子数组的长度，该子数组中的任意两个元素之间的绝对差必须小于或者等于 limit。
 * 如果不存在满足条件的子数组，则返回 0。
 * 
 * 解题思路：
 * 使用滑动窗口配合双单调队列解决该问题。
 * 1. 使用单调递增队列维护窗口内的最小值
 * 2. 使用单调递减队列维护窗口内的最大值
 * 3. 滑动窗口右边界不断扩展，当窗口内最大值与最小值的差超过limit时，收缩左边界
 * 4. 记录满足条件的最长窗口长度
 *
 * 算法步骤：
 * 1. 初始化双指针和双单调队列
 * 2. 右指针不断向右扩展窗口
 * 3. 维护两个单调队列的性质
 * 4. 当窗口不满足条件时，收缩左边界
 * 5. 记录最长窗口长度
 *
 * 时间复杂度分析：
 * O(n) - 每个元素最多入队出队一次
 *
 * 空间复杂度分析：
 * O(n) - 两个单调队列最多存储n个元素
 *
 * 是否最优解：
 * ✅ 是，这是处理此类问题的最优解法
 * 
 * 工程化考量：
 * - 使用STL deque提高代码可读性
 * - 考虑边界条件处理（空数组、单个元素等）
 * - 处理极端输入情况（大数组、极限值等）
 */

class Solution {
public:
    /**
     * 计算绝对差不超过限制的最长连续子数组长度
     * @param nums 输入数组
     * @param limit 绝对差限制
     * @return 最长满足条件的子数组长度
     */
    int longestSubarray(vector<int>& nums, int limit) {
        // 边界条件处理
        if (nums.empty()) {
            return 0;
        }
        
        // 使用双端队列维护最大值和最小值
        deque<int> maxDeque; // 单调递减队列，维护最大值
        deque<int> minDeque; // 单调递增队列，维护最小值
        
        int left = 0; // 窗口左边界
        int maxLength = 0; // 记录最大长度
        int n = nums.size();
        
        // 遍历数组，扩展窗口右边界
        for (int right = 0; right < n; right++) {
            int current = nums[right];
            
            // 维护单调递减队列（最大值队列）
            // 从队尾开始，移除所有小于等于当前元素的索引
            while (!maxDeque.empty() && nums[maxDeque.back()] <= current) {
                maxDeque.pop_back();
            }
            maxDeque.push_back(right);
            
            // 维护单调递增队列（最小值队列）
            // 从队尾开始，移除所有大于等于当前元素的索引
            while (!minDeque.empty() && nums[minDeque.back()] >= current) {
                minDeque.pop_back();
            }
            minDeque.push_back(right);
            
            // 检查当前窗口是否满足条件
            // 如果最大值与最小值的差超过limit，需要收缩左边界
            while (!maxDeque.empty() && !minDeque.empty() && 
                   nums[maxDeque.front()] - nums[minDeque.front()] > limit) {
                // 如果左边界指向的是最小值队列的头部，需要移除
                if (minDeque.front() == left) {
                    minDeque.pop_front();
                }
                // 如果左边界指向的是最大值队列的头部，需要移除
                if (maxDeque.front() == left) {
                    maxDeque.pop_front();
                }
                left++; // 收缩左边界
            }
            
            // 更新最大窗口长度
            maxLength = max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
};

/**
 * 测试函数 - 包含多种边界情况和测试用例
 */
void testLongestSubarray() {
    Solution solution;
    cout << "=== LeetCode 1438 测试用例 ===" << endl;
    
    // 测试用例1：基础示例
    vector<int> nums1 = {8, 2, 4, 7};
    int limit1 = 4;
    int result1 = solution.longestSubarray(nums1, limit1);
    cout << "测试用例1 - 输入: [8,2,4,7], limit=4" << endl;
    cout << "预期输出: 2, 实际输出: " << result1 << endl;
    cout << "测试结果: " << (result1 == 2 ? "✓ 通过" : "✗ 失败") << endl;
    
    // 测试用例2：包含重复元素
    vector<int> nums2 = {10, 1, 2, 4, 7, 2};
    int limit2 = 5;
    int result2 = solution.longestSubarray(nums2, limit2);
    cout << "\n测试用例2 - 输入: [10,1,2,4,7,2], limit=5" << endl;
    cout << "预期输出: 4, 实际输出: " << result2 << endl;
    cout << "测试结果: " << (result2 == 4 ? "✓ 通过" : "✗ 失败") << endl;
    
    // 测试用例3：limit为0的特殊情况
    vector<int> nums3 = {4, 2, 2, 2, 4, 4, 2, 2};
    int limit3 = 0;
    int result3 = solution.longestSubarray(nums3, limit3);
    cout << "\n测试用例3 - 输入: [4,2,2,2,4,4,2,2], limit=0" << endl;
    cout << "预期输出: 3, 实际输出: " << result3 << endl;
    cout << "测试结果: " << (result3 == 3 ? "✓ 通过" : "✗ 失败") << endl;
    
    // 测试用例4：单个元素
    vector<int> nums4 = {5};
    int limit4 = 10;
    int result4 = solution.longestSubarray(nums4, limit4);
    cout << "\n测试用例4 - 输入: [5], limit=10" << endl;
    cout << "预期输出: 1, 实际输出: " << result4 << endl;
    cout << "测试结果: " << (result4 == 1 ? "✓ 通过" : "✗ 失败") << endl;
    
    // 测试用例5：空数组
    vector<int> nums5 = {};
    int limit5 = 5;
    int result5 = solution.longestSubarray(nums5, limit5);
    cout << "\n测试用例5 - 输入: [], limit=5" << endl;
    cout << "预期输出: 0, 实际输出: " << result5 << endl;
    cout << "测试结果: " << (result5 == 0 ? "✓ 通过" : "✗ 失败") << endl;
    
    // 测试用例6：递减序列
    vector<int> nums6 = {5, 4, 3, 2, 1};
    int limit6 = 2;
    int result6 = solution.longestSubarray(nums6, limit6);
    cout << "\n测试用例6 - 输入: [5,4,3,2,1], limit=2" << endl;
    cout << "预期输出: 3, 实际输出: " << result6 << endl;
    cout << "测试结果: " << (result6 == 3 ? "✓ 通过" : "✗ 失败") << endl;
    
    cout << "\n=== 性能测试 ===" << endl;
    
    // 性能测试：大数组测试
    vector<int> largeNums(10000, 1);
    auto startTime = chrono::high_resolution_clock::now();
    int largeResult = solution.longestSubarray(largeNums, 0);
    auto endTime = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
    cout << "大数组测试 (10000个元素): " << largeResult << endl;
    cout << "执行时间: " << duration.count() << "ms" << endl;
    
    cout << "\n=== 算法分析 ===" << endl;
    cout << "时间复杂度: O(n) - 每个元素最多入队出队一次" << endl;
    cout << "空间复杂度: O(n) - 两个单调队列最多存储n个元素" << endl;
    cout << "最优解: ✅ 是" << endl;
}

int main() {
    testLongestSubarray();
    return 0;
}