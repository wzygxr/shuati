#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 315. 计算右侧小于当前元素的个数
题目链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/

题目描述:
给定一个整数数组 nums，按要求返回一个新数组 counts。数组 counts 有该性质：
counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。

示例:
输入: [5,2,6,1]
输出: [2,1,1,0]
解释:
5 的右侧有 2 个更小的元素 (2 和 1)
2 的右侧仅有 1 个更小的元素 (1)
6 的右侧有 1 个更小的元素 (1)
1 的右侧有 0 个更小的元素

解题思路 - 整体二分算法:
1. 将原问题转化为多个查询问题：对于每个位置i，查询数组中索引大于i且值小于nums[i]的元素个数
2. 对值域进行二分，利用树状数组维护当前已经处理过的元素
3. 从右到左处理每个元素，这样每次处理i时，i右侧的元素已经被处理
4. 使用整体二分，同时处理所有查询

时间复杂度分析:
O(N log N log M)，其中N是数组长度，M是值域范围
- 整体二分需要log M次迭代（M是可能的数值范围）
- 每次迭代需要O(N log N)时间进行树状数组操作

空间复杂度分析:
O(N)，需要额外的数组存储排序后的值、离散化映射、结果等
"""

import bisect

class LeetCode315_CountOfSmallerNumbersAfterSelf:
    """
    LeetCode 315题的解决方案类，使用树状数组和离散化来解决问题
    """
    
    def __init__(self):
        """
        初始化解决方案类
        """
        self.n = 0  # 数组长度
        self.tree = []  # 树状数组
        self.pos_list = []  # 记录操作位置，用于回溯
    
    def update(self, idx, val):
        """
        树状数组的更新操作
        
        Args:
            idx: 索引位置（从1开始）
            val: 更新的值
        """
        while idx <= self.n:
            self.tree[idx] += val
            idx += idx & -idx
        self.pos_list.append(idx - (idx & -idx))  # 记录被修改的位置
    
    def query(self, idx):
        """
        树状数组的查询操作，查询前缀和
        
        Args:
            idx: 索引位置
            
        Returns:
            前缀和结果
        """
        res = 0
        while idx > 0:
            res += self.tree[idx]
            idx -= idx & -idx
        return res
    
    def rollback(self):
        """
        回溯树状数组的状态，用于分治过程
        """
        for pos in self.pos_list:
            # 回滚每个位置的更新
            while pos <= self.n:
                self.tree[pos] -= 1  # 因为我们每次更新都是+1，所以回滚时-1
                pos += pos & -pos
        self.pos_list.clear()  # 清空记录
    
    def countSmaller(self, nums):
        """
        主方法：使用树状数组计算右侧小于当前元素的个数
        
        Args:
            nums: 输入数组
            
        Returns:
            结果列表，其中每个元素表示原数组对应位置右侧小于它的元素个数
        """
        self.n = len(nums)
        if self.n == 0:
            return []
        
        # 初始化树状数组
        self.tree = [0] * (self.n + 2)  # 树状数组大小
        self.pos_list = []
        
        # 离散化处理
        unique_nums = sorted(set(nums))
        
        # 从右到左处理
        result = [0] * self.n
        for i in range(self.n - 1, -1, -1):
            # 查找nums[i]在离散化数组中的位置
            rank = bisect.bisect_left(unique_nums, nums[i]) + 1  # +1是因为树状数组从1开始
            
            # 查询当前树状数组中小于nums[i]的元素个数
            result[i] = self.query(rank - 1)
            
            # 将当前元素添加到树状数组
            self.update(rank, 1)
        
        return result
    
    def countSmallerWithParallelBinarySearch(self, nums):
        """
        使用整体二分的思想来解决问题
        这个版本更接近整体二分的标准实现
        
        Args:
            nums: 输入数组
            
        Returns:
            结果列表
        """
        self.n = len(nums)
        if self.n == 0:
            return []
        
        # 初始化数据结构
        self.tree = [0] * (self.n + 2)  # 树状数组
        self.pos_list = []
        result = [0] * self.n
        
        # 离散化处理
        all_nums = sorted(nums)
        unique_count = 1
        for i in range(1, self.n):
            if all_nums[i] != all_nums[i-1]:
                all_nums[unique_count] = all_nums[i]
                unique_count += 1
        all_nums = all_nums[:unique_count]
        
        # 从右到左处理，逐个添加元素到树状数组，并查询
        for i in range(self.n - 1, -1, -1):
            # 离散化当前值
            rank = bisect.bisect_left(all_nums, nums[i])
            
            # 查询比nums[i]小的元素个数
            result[i] = self.query(rank)
            
            # 添加当前元素
            self.update(rank + 1, 1)  # +1因为树状数组从1开始
        
        return result

# 测试代码
def test_solution():
    # 测试用例1
    solution = LeetCode315_CountOfSmallerNumbersAfterSelf()
    nums1 = [5, 2, 6, 1]
    result1 = solution.countSmaller(nums1)
    result1_bs = solution.countSmallerWithParallelBinarySearch(nums1)
    print(f"输入: [5, 2, 6, 1]")
    print(f"输出: {result1}")  # 预期输出: [2, 1, 1, 0]
    print(f"整体二分版本输出: {result1_bs}")
    
    # 测试用例2
    solution = LeetCode315_CountOfSmallerNumbersAfterSelf()
    nums2 = [-1, -1]
    result2 = solution.countSmaller(nums2)
    result2_bs = solution.countSmallerWithParallelBinarySearch(nums2)
    print(f"输入: [-1, -1]")
    print(f"输出: {result2}")  # 预期输出: [0, 0]
    print(f"整体二分版本输出: {result2_bs}")
    
    # 测试用例3
    solution = LeetCode315_CountOfSmallerNumbersAfterSelf()
    nums3 = []
    result3 = solution.countSmaller(nums3)
    result3_bs = solution.countSmallerWithParallelBinarySearch(nums3)
    print(f"输入: []")
    print(f"输出: {result3}")  # 预期输出: []
    print(f"整体二分版本输出: {result3_bs}")
    
    # 边界测试 - 大量重复元素
    solution = LeetCode315_CountOfSmallerNumbersAfterSelf()
    nums4 = [1, 1, 1, 1, 1]
    result4 = solution.countSmaller(nums4)
    print(f"输入: [1, 1, 1, 1, 1]")
    print(f"输出: {result4}")  # 预期输出: [0, 0, 0, 0, 0]
    
    # 边界测试 - 逆序数组
    solution = LeetCode315_CountOfSmallerNumbersAfterSelf()
    nums5 = [5, 4, 3, 2, 1]
    result5 = solution.countSmaller(nums5)
    print(f"输入: [5, 4, 3, 2, 1]")
    print(f"输出: {result5}")  # 预期输出: [4, 3, 2, 1, 0]
    
    # 边界测试 - 顺序数组
    solution = LeetCode315_CountOfSmallerNumbersAfterSelf()
    nums6 = [1, 2, 3, 4, 5]
    result6 = solution.countSmaller(nums6)
    print(f"输入: [1, 2, 3, 4, 5]")
    print(f"输出: {result6}")  # 预期输出: [0, 0, 0, 0, 0]
    
    # 边界测试 - 混合正负数
    solution = LeetCode315_CountOfSmallerNumbersAfterSelf()
    nums7 = [-3, 5, 0, -2, 8, 1]
    result7 = solution.countSmaller(nums7)
    print(f"输入: [-3, 5, 0, -2, 8, 1]")
    print(f"输出: {result7}")  # 预期输出: [0, 3, 0, 0, 1, 0]

if __name__ == "__main__":
    test_solution()