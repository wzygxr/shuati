#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 493. 翻转对

题目描述：
给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们就将 (i, j) 称作一个重要翻转对。
你需要返回给定数组中的重要翻转对的数量。

示例：
输入: [1,3,2,3,1]
输出: 2

输入: [2,4,3,5,1]
输出: 3

解题思路：
使用权值线段树来解决这个问题。
1. 离散化：由于数组元素可能很大，需要先进行离散化处理，将元素映射到连续的小范围
2. 权值线段树：线段树的每个节点存储某个值域范围内元素出现的次数
3. 从左向右遍历数组，在权值线段树中查询满足条件的元素个数，然后将当前元素插入线段树

时间复杂度分析：
- 离散化：O(n log n)
- 遍历数组并查询/更新：O(n log n)
- 总时间复杂度：O(n log n)

空间复杂度分析：
- O(n)，线段树需要4*n的空间来存储节点信息

链接：https://leetcode.cn/problems/reverse-pairs
"""


class LeetCode493_SegmentTree:
    """
    LeetCode 493. 翻转对的线段树实现
    """
    
    def __init__(self):
        """构造函数"""
        self.tree = []    # 线段树数组，存储区间元素个数
        self.sorted = []  # 离散化后的数组
        self.n = 0        # 离散化后的数组大小
    
    def reversePairs(self, nums):
        """
        计算重要翻转对的数量
        :param nums: 输入数组
        :return: 重要翻转对的数量
        """
        if not nums or len(nums) < 2:
            return 0
        
        # 离散化处理
        num_set = set()
        for num in nums:
            num_set.add(num)
            num_set.add(2 * num)  # 同时加入2*num，用于后续查询
        
        self.sorted = sorted(list(num_set))
        self.n = len(self.sorted)
        
        # 线段树数组大小通常为4*n，确保足够容纳所有节点
        self.tree = [0] * (self.n << 2)
        
        result = 0
        # 从左向右遍历数组
        for i in range(len(nums)):
            # 查询满足条件 element > 2*nums[i] 的元素个数
            # 即查询在已经处理的元素中，有多少个元素大于 2*nums[i]
            count = self._query(0, self.n - 1, 1, self._lower_bound(2 * nums[i] + 1), self.n - 1)
            result += count
            # 将当前元素插入线段树
            self._update(0, self.n - 1, 1, self._lower_bound(nums[i]))
        
        return result
    
    def _lower_bound(self, target):
        """
        找到目标值在排序数组中的下界（第一个大于等于目标值的位置）
        :param target: 目标值
        :return: 下界位置
        """
        left, right = 0, self.n
        while left < right:
            mid = left + (right - left) // 2
            if self.sorted[mid] < target:
                left = mid + 1
            else:
                right = mid
        return left
    
    def _update(self, start, end, node, index):
        """
        更新线段树中的值（单点更新）
        :param start: 当前节点区间起始位置
        :param end: 当前节点区间结束位置
        :param node: 当前节点在tree数组中的索引
        :param index: 要更新的位置
        """
        # 如果index不在当前区间范围内，直接返回
        if start > index or end < index:
            return
        
        # 如果是叶子节点，直接增加计数
        if start == end:
            self.tree[node] += 1
            return
        
        # 递归更新子节点
        mid = start + (end - start) // 2
        if index <= mid:
            self._update(start, mid, node * 2, index)
        else:
            self._update(mid + 1, end, node * 2 + 1, index)
        
        # 合并左右子树信息
        self.tree[node] = self.tree[node * 2] + self.tree[node * 2 + 1]
    
    def _query(self, start, end, node, left, right):
        """
        查询线段树中指定区间的元素个数
        :param start: 当前节点区间起始位置
        :param end: 当前节点区间结束位置
        :param node: 当前节点在tree数组中的索引
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间内元素个数
        """
        # 如果查询区间无效或当前区间与查询区间无重叠，返回0
        if left > right or start > right or end < left:
            return 0
        
        # 如果当前区间完全包含在查询区间内，返回当前节点的值
        if start >= left and end <= right:
            return self.tree[node]
        
        # 递归查询左右子树
        mid = start + (end - start) // 2
        left_count = self._query(start, mid, node * 2, left, right)
        right_count = self._query(mid + 1, end, node * 2 + 1, left, right)
        
        return left_count + right_count


# 测试函数
def test_solution():
    """测试LeetCode 493实现"""
    print("测试LeetCode 493实现...")
    
    # 测试用例
    solution = LeetCode493_SegmentTree()
    
    nums1 = [1, 3, 2, 3, 1]
    print(f"输入数组: {nums1}")
    print(f"输出结果: {solution.reversePairs(nums1)}")  # 应该输出2
    
    nums2 = [2, 4, 3, 5, 1]
    print(f"输入数组: {nums2}")
    print(f"输出结果: {solution.reversePairs(nums2)}")  # 应该输出3
    
    print("LeetCode 493测试完成！")


if __name__ == "__main__":
    test_solution()