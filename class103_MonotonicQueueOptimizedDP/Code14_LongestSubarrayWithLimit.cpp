#include <iostream>
#include <vector>
#include <deque>
#include <string>
#include <sstream>
using namespace std;

/**
 * LeetCode 1438 绝对差不超过限制的最长连续子数组
 * 题目来源：LeetCode
 * 网址：https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
 * 
 * 题目描述：
 * 给你一个整数数组 nums ，和一个表示限制的整数 limit，请你返回最长连续子数组的长度，
 * 该子数组中的任意两个元素之间的绝对差必须小于或者等于 limit 。
 * 
 * 解题思路：
 * 这是一个使用双端单调队列的滑动窗口问题。
 * - 使用一个滑动窗口，维护窗口内的最大值和最小值
 * - 使用两个双端队列，一个维护窗口内的最大值（单调递减队列），一个维护窗口内的最小值（单调递增队列）
 * - 当窗口内的最大值和最小值之差超过limit时，移动左指针缩小窗口
 * - 记录窗口的最大长度
 * 
 * 时间复杂度：O(n)，每个元素最多被加入和弹出队列各一次
 * 空间复杂度：O(n)，最坏情况下队列中存储所有元素
 */

/**
 * 找出最长子数组，其中任意两个元素的绝对差不超过limit
 * @param nums 整数数组
 * @param limit 绝对差限制
 * @return 最长子数组的长度
 */
int longestSubarray(vector<int>& nums, int limit) {
    int n = nums.size();
    int left = 0; // 滑动窗口的左边界
    int maxLength = 0; // 记录最长子数组的长度
    
    // 单调递减队列，存储的是索引，队列头部是当前窗口的最大值
    deque<int> maxDeque;
    // 单调递增队列，存储的是索引，队列头部是当前窗口的最小值
    deque<int> minDeque;
    
    // 遍历右边界
    for (int right = 0; right < n; right++) {
        // 维护单调递减队列，确保队列头部是最大值
        while (!maxDeque.empty() && nums[right] >= nums[maxDeque.back()]) {
            maxDeque.pop_back();
        }
        maxDeque.push_back(right);
        
        // 维护单调递增队列，确保队列头部是最小值
        while (!minDeque.empty() && nums[right] <= nums[minDeque.back()]) {
            minDeque.pop_back();
        }
        minDeque.push_back(right);
        
        // 检查当前窗口是否满足条件
        while (!maxDeque.empty() && !minDeque.empty() && 
               nums[maxDeque.front()] - nums[minDeque.front()] > limit) {
            // 如果最大值的索引是左边界，则移除
            if (maxDeque.front() == left) {
                maxDeque.pop_front();
            }
            // 如果最小值的索引是左边界，则移除
            if (minDeque.front() == left) {
                minDeque.pop_front();
            }
            // 移动左边界
            left++;
        }
        
        // 更新最大长度
        maxLength = max(maxLength, right - left + 1);
    }
    
    return maxLength;
}

int main() {
    string line;
    getline(cin, line);
    istringstream iss(line);
    vector<int> nums;
    int num;
    while (iss >> num) {
        nums.push_back(num);
    }
    
    int limit;
    cin >> limit;
    
    cout << longestSubarray(nums, limit) << endl;
    
    return 0;
}