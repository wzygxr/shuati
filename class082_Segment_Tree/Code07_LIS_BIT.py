#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
最长递增子序列（LIS）问题的树状数组解法

问题描述：
给定一个无序的整数数组，找到其中最长上升子序列的长度。

示例：
输入: [10,9,2,5,3,7,101,18]
输出: 4 
解释: 最长的上升子序列是 [2,3,7,101]，长度是 4

解题思路：
使用树状数组优化动态规划解法，将时间复杂度从O(n²)优化到O(n log n)

1. 离散化处理：将原数组元素映射到较小的范围内（压缩值域）
2. 利用树状数组维护以每个值结尾的最长递增子序列长度
3. 对于每个元素num[i]，查询比它小的元素中最大的LIS值，然后更新当前元素的LIS值

时间复杂度分析：
- 离散化处理：O(n log n)
- 每个元素查询和更新操作：O(log n)
- 总时间复杂度：O(n log n)

空间复杂度分析：
- 树状数组空间：O(n)
- 离散化数组空间：O(n)
- 总空间复杂度：O(n)

LeetCode 300. 最长递增子序列
链接：https://leetcode.cn/problems/longest-increasing-subsequence/
"""

class Code07_LIS_BIT:
    """
    使用树状数组（Binary Indexed Tree）解决最长递增子序列问题
    提供了基本的树状数组操作和LIS解法
    """
    
    def lengthOfLIS(self, nums):
        """
        计算最长递增子序列的长度
        
        Args:
            nums: 输入数组
            
        Returns:
            最长递增子序列的长度
        """
        if not nums:
            return 0
        
        n = len(nums)
        
        # 离散化处理
        sorted_nums = sorted(list(set(nums)))
        rank_dict = {num: i + 1 for i, num in enumerate(sorted_nums)}
        
        # 树状数组实现
        class FenwickTree:
            def __init__(self, size):
                self.tree = [0] * (size + 1)
            
            def lowbit(self, x):
                """计算lowbit值，即x的二进制表示中最低位1所对应的值"""
                return x & (-x)
            
            def query(self, index):
                """查询[1, index]区间内的最大值"""
                max_val = 0
                while index > 0:
                    max_val = max(max_val, self.tree[index])
                    index -= self.lowbit(index)
                return max_val
            
            def update(self, index, value):
                """将索引index的值更新为value（保留最大值）"""
                while index < len(self.tree):
                    self.tree[index] = max(self.tree[index], value)
                    index += self.lowbit(index)
        
        bit = FenwickTree(len(sorted_nums))
        max_lis = 0
        
        # 从左到右遍历数组
        for num in nums:
            # 获取当前元素的排名
            rank = rank_dict[num]
            # 查询比当前元素小的最大LIS长度
            current_lis = bit.query(rank - 1) + 1
            # 更新以当前元素结尾的LIS长度
            bit.update(rank, current_lis)
            # 更新全局最大LIS长度
            max_lis = max(max_lis, current_lis)
        
        return max_lis
    
    # ==================== 补充题目：LeetCode 307. 区域和检索 - 数组可修改 ====================
    """
    LeetCode 307. 区域和检索 - 数组可修改
    链接：https://leetcode.cn/problems/range-sum-query-mutable/
    题目：给你一个数组 nums ，请你完成两类查询。
    1. 更新数组 nums 下标 i 处的值
    2. 计算数组 nums 中从下标 left 到下标 right 的元素和
    
    解题思路：
    使用树状数组实现单点更新和区间查询
    """
    class NumArray:
        def __init__(self, nums):
            """
            初始化NumArray对象
            
            Args:
                nums: 输入数组
            """
            self.n = len(nums)
            self.nums = nums.copy()  # 保存原始数组
            self.tree = [0] * (self.n + 1)  # 树状数组，索引从1开始
            
            # 初始化树状数组
            for i in range(self.n):
                self._update_tree(i + 1, nums[i])
        
        def update(self, index, val):
            """
            更新数组中index位置的值为val
            
            Args:
                index: 要更新的元素索引
                val: 新的值
            """
            delta = val - self.nums[index]
            self.nums[index] = val
            self._update_tree(index + 1, delta)  # 树状数组索引从1开始
        
        def sumRange(self, left, right):
            """
            计算区间[left, right]的元素和
            
            Args:
                left: 左边界索引
                right: 右边界索引
                
            Returns:
                区间和
            """
            return self._query_tree(right + 1) - self._query_tree(left)
        
        def _lowbit(self, x):
            """计算lowbit值"""
            return x & (-x)
        
        def _update_tree(self, index, delta):
            """
            更新树状数组中index位置的值，加上delta
            
            Args:
                index: 树状数组索引（从1开始）
                delta: 增量值
            """
            while index <= self.n:
                self.tree[index] += delta
                index += self._lowbit(index)
        
        def _query_tree(self, index):
            """
            查询树状数组中[1, index]的前缀和
            
            Args:
                index: 右边界索引（从1开始）
                
            Returns:
                前缀和
            """
            result = 0
            while index > 0:
                result += self.tree[index]
                index -= self._lowbit(index)
            return result
    
    # ==================== 补充题目：LeetCode 673. 最长递增子序列的个数 ====================
    """
    LeetCode 673. 最长递增子序列的个数
    链接：https://leetcode.cn/problems/number-of-longest-increasing-subsequence/
    题目：给定一个未排序的整数数组，找到最长递增子序列的个数。
    
    解题思路：
    使用两个树状数组，一个维护最长递增子序列的长度，另一个维护对应的路径数
    """
    def findNumberOfLIS(self, nums):
        """
        计算最长递增子序列的个数
        
        Args:
            nums: 输入数组
            
        Returns:
            最长递增子序列的个数
        """
        if not nums:
            return 0
        
        n = len(nums)
        
        # 离散化处理
        sorted_nums = sorted(list(set(nums)))
        rank_dict = {num: i + 1 for i, num in enumerate(sorted_nums)}
        
        # 树状数组类，用于维护LIS长度和对应路径数
        class FenwickTreeForCount:
            def __init__(self, size):
                self.size = size
                # tree_len[i]表示以rank=i的元素结尾的最长递增子序列长度
                self.tree_len = [0] * (size + 1)
                # tree_count[i]表示以rank=i的元素结尾的最长递增子序列的路径数
                self.tree_count = [0] * (size + 1)
            
            def lowbit(self, x):
                """计算lowbit值"""
                return x & (-x)
            
            def update(self, index, length, count):
                """
                更新树状数组
                
                Args:
                    index: 要更新的位置
                    length: 最长递增子序列长度
                    count: 路径数
                """
                while index <= self.size:
                    if self.tree_len[index] < length:
                        self.tree_len[index] = length
                        self.tree_count[index] = count
                    elif self.tree_len[index] == length:
                        self.tree_count[index] += count
                    index += self.lowbit(index)
            
            def query(self, index):
                """
                查询[1, index]区间内的最长递增子序列长度和对应路径数
                
                Args:
                    index: 查询的右边界
                    
                Returns:
                    (max_length, total_count) - 最长长度和对应路径数
                """
                max_length = 0
                total_count = 0
                
                while index > 0:
                    if self.tree_len[index] > max_length:
                        max_length = self.tree_len[index]
                        total_count = self.tree_count[index]
                    elif self.tree_len[index] == max_length:
                        total_count += self.tree_count[index]
                    index -= self.lowbit(index)
                
                return (max_length, total_count)
        
        bit = FenwickTreeForCount(len(sorted_nums))
        
        for num in nums:
            rank = rank_dict[num]
            # 查询比当前元素小的最大LIS长度和路径数
            max_len, path_count = bit.query(rank - 1)
            
            # 如果没有找到比当前元素小的元素，则当前元素自身构成一个长度为1的子序列
            current_len = max_len + 1
            current_count = path_count if path_count > 0 else 1
            
            # 更新树状数组
            bit.update(rank, current_len, current_count)
        
        # 查询整个数组的最长递增子序列长度和路径数
        max_len, total_count = bit.query(len(sorted_nums))
        return total_count
    
    def binarySearch(self, sorted_arr, target):
        """
        二分查找辅助方法，查找target在排序数组中的位置
        用于离散化处理
        
        Args:
            sorted_arr: 已排序的数组
            target: 目标值
            
        Returns:
            目标值应该插入的位置
        """
        left, right = 0, len(sorted_arr)
        while left < right:
            mid = left + (right - left) // 2
            if sorted_arr[mid] < target:
                left = mid + 1
            else:
                right = mid
        return left

# 测试函数
def test_solution():
    solution = Code07_LIS_BIT()
    
    # 测试最长递增子序列
    print("最长递增子序列测试用例1:")
    nums1 = [10, 9, 2, 5, 3, 7, 101, 18]
    print(f"输入: {nums1}")
    print(f"输出: {solution.lengthOfLIS(nums1)}")
    print(f"期望: 4\n")
    
    print("最长递增子序列测试用例2:")
    nums2 = [0, 1, 0, 3, 2, 3]
    print(f"输入: {nums2}")
    print(f"输出: {solution.lengthOfLIS(nums2)}")
    print(f"期望: 4\n")
    
    print("最长递增子序列测试用例3:")
    nums3 = [7, 7, 7, 7, 7, 7, 7]
    print(f"输入: {nums3}")
    print(f"输出: {solution.lengthOfLIS(nums3)}")
    print(f"期望: 1\n")
    
    # 测试最长递增子序列的个数
    print("最长递增子序列的个数测试用例:")
    nums4 = [1, 3, 5, 4, 7]
    print(f"输入: {nums4}")
    print(f"输出: {solution.findNumberOfLIS(nums4)}")
    print(f"期望: 2")

if __name__ == "__main__":
    test_solution()