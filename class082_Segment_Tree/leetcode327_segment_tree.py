#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 327. 区间和的个数

题目描述：
给你一个整数数组 nums 以及两个整数 lower 和 upper 。
求数组中，值位于范围 [lower, upper] （包含 lower 和 upper）之内的区间和的个数。
区间和 S(i, j) 表示在 nums 中，位置从 i 到 j 的元素之和，包含 i 和 j (i ≤ j)。

示例：
输入：nums = [-2,5,-1], lower = -2, upper = 2
输出：3
解释：存在三个区间：[0,0]、[2,2] 和 [0,2] ，对应的区间和分别是：-2 、-1 、2 。

解题思路：
使用权值线段树来解决这个问题。
1. 计算前缀和数组，将区间和问题转化为前缀和差值问题
2. 离散化：由于前缀和可能很大，需要先进行离散化处理
3. 权值线段树：线段树的每个节点存储某个值域范围内前缀和出现的次数
4. 从左向右遍历前缀和数组，在权值线段树中查询满足条件的前缀和个数，然后将当前前缀和插入线段树

时间复杂度分析：
- 计算前缀和：O(n)
- 离散化：O(n log n)
- 遍历数组并查询/更新：O(n log n)
- 总时间复杂度：O(n log n)

空间复杂度分析：
- O(n)，线段树需要4*n的空间来存储节点信息

链接：https://leetcode.cn/problems/count-of-range-sum
"""


class LeetCode327_SegmentTree:
    """
    LeetCode 327. 区间和的个数的线段树实现
    """
    
    def __init__(self):
        """构造函数"""
        self.tree = []    # 线段树数组，存储区间元素个数
        self.sorted = []  # 离散化后的数组
        self.n = 0        # 离散化后的数组大小
    
    def countRangeSum(self, nums, lower, upper):
        """
        计算区间和的个数
        :param nums: 输入数组
        :param lower: 下界
        :param upper: 上界
        :return: 区间和的个数
        """
        if not nums:
            return 0
        
        # 计算前缀和数组
        prefix_sum = [0]
        for num in nums:
            prefix_sum.append(prefix_sum[-1] + num)
        
        # 离散化处理
        num_set = set()
        for s in prefix_sum:
            num_set.add(s)
            num_set.add(s - lower)
            num_set.add(s - upper)
        
        self.sorted = sorted(list(num_set))
        self.n = len(self.sorted)
        
        # 线段树数组大小通常为4*n，确保足够容纳所有节点
        self.tree = [0] * (self.n << 2)
        
        result = 0
        # 从左向右遍历前缀和数组
        for s in prefix_sum:
            # 查询满足条件 prefix_sum[j] - prefix_sum[i] 在 [lower, upper] 范围内的j个数
            # 即查询 prefix_sum[j] 在 [prefix_sum[i] + lower, prefix_sum[i] + upper] 范围内的个数
            left_bound = self._lower_bound(s + lower)
            right_bound = self._upper_bound(s + upper)
            result += self._query(0, self.n - 1, 1, left_bound, right_bound)
            # 将当前前缀和插入线段树
            self._update(0, self.n - 1, 1, self._lower_bound(s))
        
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
    
    def _upper_bound(self, target):
        """
        找到目标值在排序数组中的上界（第一个大于目标值的位置）
        :param target: 目标值
        :return: 上界位置
        """
        left, right = 0, self.n
        while left < right:
            mid = left + (right - left) // 2
            if self.sorted[mid] <= target:
                left = mid + 1
            else:
                right = mid
        return left - 1
    
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
    """测试LeetCode 327实现"""
    print("测试LeetCode 327实现...")
    
    # 测试用例
    solution = LeetCode327_SegmentTree()
    
    nums = [-2, 5, -1]
    lower, upper = -2, 2
    print(f"输入数组: {nums}, lower = {lower}, upper = {upper}")
    print(f"输出结果: {solution.countRangeSum(nums, lower, upper)}")  # 应该输出3
    
    print("LeetCode 327测试完成！")


if __name__ == "__main__":
    test_solution()