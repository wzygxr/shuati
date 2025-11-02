#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
题目名称：LeetCode 1425. 带限制的子序列和
题目来源：LeetCode
题目链接：https://leetcode.cn/problems/constrained-subsequence-sum/
题目难度：困难

题目描述：
给你一个整数数组 nums 和一个整数 k ，请你返回 非空 子序列元素和的最大值，
子序列需要满足：子序列中每两个 相邻 元素在原数组中的下标距离不超过 k 。
数组的子序列是通过删除一些元素（可以删除零个元素）得到的一个数组。

解题思路：
这是一个典型的动态规划问题，同时需要用单调队列优化时间复杂度。

动态规划分析：
- 状态定义：dp[i] 表示以第i个元素结尾的满足条件的子序列的最大和
- 状态转移方程：dp[i] = nums[i] + max{ dp[j] | max(0, i-k) <= j < i } ，或者只取nums[i]自己
- 初始状态：dp[0] = nums[0]
- 最终结果：max(dp[0...n-1])

单调队列优化：
- 我们需要在O(1)时间内获取区间 [i-k, i-1] 内的最大dp值
- 使用单调递减队列维护这个区间内的最大值
- 队列中存储的是索引，并且对应的dp值保持单调递减

算法步骤：
1. 初始化dp数组，dp[0] = nums[0]
2. 初始化单调递减队列，将0加入队列
3. 对于每个位置i从1到n-1：
   a. 移除队列中超出窗口[i-k, i-1]的索引
   b. dp[i] = nums[i] + (队列不为空 ? dp[队列头] : 0)
   c. 移除队列中dp值小于等于dp[i]的索引
   d. 将i加入队列
4. 返回dp数组中的最大值

时间复杂度：O(n) - 每个元素最多入队出队一次
空间复杂度：O(n) - 使用dp数组和单调队列

是否为最优解：✅ 是，这是解决该问题的最优时间复杂度解法

Python实现注意事项：
- 使用collections.deque实现高效的双端队列操作
- 处理好边界情况，尤其是空数组的判断
- 考虑负数情况，确保算法正确性
"""

from collections import deque

def constrained_subset_sum(nums, k):
    """
    计算带限制的子序列和的最大值
    :param nums: 输入数组
    :param k: 相邻元素下标距离的最大限制
    :return: 满足条件的最大子序列和
    """
    n = len(nums)
    if n == 0:
        return 0
    
    # 状态数组：dp[i]表示以第i个元素结尾的满足条件的子序列的最大和
    dp = [0] * n
    # 单调递减队列，存储索引，对应dp值保持单调递减
    dq = deque()
    
    dp[0] = nums[0]
    dq.append(0)  # 初始化队列，将第一个元素索引加入队列
    
    max_sum = dp[0]  # 记录全局最大值
    
    for i in range(1, n):
        # 移除队列中超出窗口范围的元素（窗口左边界为i-k）
        while dq and dq[0] < i - k:
            dq.popleft()
        
        # 计算dp[i]，可以选择队列头部的最优解加上当前元素，或者只取当前元素自己
        # 如果队列为空，说明之前的所有元素都不在窗口范围内，此时只能取当前元素自己
        dp[i] = nums[i] + (dp[dq[0]] if dq else 0)
        
        # 维护队列的单调递减性质，移除队列尾部所有dp值小于等于当前dp[i]的索引
        # 因为这些索引对应的dp值无法成为后续位置的最优选择
        while dq and dp[dq[-1]] <= dp[i]:
            dq.pop()
        
        # 将当前索引加入队列尾部
        dq.append(i)
        
        # 更新全局最大值
        max_sum = max(max_sum, dp[i])
    
    return max_sum

def test():
    """
    测试函数
    """
    # 测试用例1
    nums1 = [10, 2, -10, 5, 20]
    k1 = 2
    print(f"测试用例1结果: {constrained_subset_sum(nums1, k1)}")  # 预期输出: 37
    
    # 测试用例2
    nums2 = [-1, -2, -3]
    k2 = 1
    print(f"测试用例2结果: {constrained_subset_sum(nums2, k2)}")  # 预期输出: -1
    
    # 测试用例3
    nums3 = [10, -2, -10, -5, 20]
    k3 = 2
    print(f"测试用例3结果: {constrained_subset_sum(nums3, k3)}")  # 预期输出: 23

if __name__ == "__main__":
    test()