#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 239 Sliding Window Maximum

题目描述：
给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
返回滑动窗口中的最大值。

解题思路：
我们可以使用稀疏表来预处理数组，然后对每个窗口查询最大值。
1. 使用稀疏表预处理数组，构建RMQ数据结构
2. 对每个窗口使用稀疏表查询最大值

时间复杂度：O(n log n + m)
空间复杂度：O(n log n)
"""

import math

class Solution:
    def max_sliding_window(self, nums, k):
        """
        计算滑动窗口中的最大值
        
        Args:
            nums: 输入数组
            k: 窗口大小
            
        Returns:
            每个窗口中的最大值列表
        """
        if not nums or k == 0:
            return []
        
        n = len(nums)
        
        # 计算稀疏表的大小
        log_size = int(math.log2(n)) + 1
        
        # 创建稀疏表
        sparse_table = [[0] * log_size for _ in range(n)]
        
        # 初始化稀疏表的第一列
        for i in range(n):
            sparse_table[i][0] = nums[i]
        
        # 构建稀疏表
        for j in range(1, log_size):
            for i in range(n - (1 << j) + 1):
                sparse_table[i][j] = max(
                    sparse_table[i][j-1],
                    sparse_table[i + (1 << (j-1))][j-1]
                )
        
        # 查询每个窗口的最大值
        result = []
        for i in range(n - k + 1):
            l, r = i, i + k - 1
            length = r - l + 1
            log_length = int(math.log2(length))
            max1 = sparse_table[l][log_length]
            max2 = sparse_table[r - (1 << log_length) + 1][log_length]
            result.append(max(max1, max2))
        
        return result


# 测试方法
def main():
    solution = Solution()
    
    # 测试用例1
    nums1 = [1, 3, -1, -3, 5, 3, 6, 7]
    k1 = 3
    print("测试用例1:")
    print("数组:", nums1)
    print("窗口大小:", k1)
    print("结果:", solution.max_sliding_window(nums1, k1))
    print()
    
    # 测试用例2
    nums2 = [1]
    k2 = 1
    print("测试用例2:")
    print("数组:", nums2)
    print("窗口大小:", k2)
    print("结果:", solution.max_sliding_window(nums2, k2))
    print()
    
    # 测试用例3
    nums3 = [1, -1]
    k3 = 1
    print("测试用例3:")
    print("数组:", nums3)
    print("窗口大小:", k3)
    print("结果:", solution.max_sliding_window(nums3, k3))


if __name__ == "__main__":
    main()