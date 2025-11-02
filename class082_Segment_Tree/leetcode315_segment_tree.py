#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 315. 计算右侧小于当前元素的个数

题目描述：
给你一个整数数组 nums ，按要求返回一个新数组 counts 。数组 counts 有该性质：
counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。

示例：
输入：nums = [5,2,6,1]
输出：[2,1,1,0]
解释：
5 的右侧有 2 个更小的元素 (2 和 1)
2 的右侧仅有 1 个更小的元素 (1)
6 的右侧有 1 个更小的元素 (1)
1 的右侧有 0 个更小的元素

解题思路：
使用权值线段树来解决这个问题。
1. 离散化：由于数组元素可能很大，需要先进行离散化处理，将元素映射到连续的小范围
2. 权值线段树：线段树的每个节点存储某个值域范围内元素出现的次数
3. 从右向左遍历数组，在权值线段树中查询比当前元素小的元素个数，然后将当前元素插入线段树

时间复杂度分析：
- 离散化：O(n log n)
- 遍历数组并查询/更新：O(n log n)
- 总时间复杂度：O(n log n)

空间复杂度分析：
- O(n)，线段树需要4*n的空间来存储节点信息

链接：https://leetcode.cn/problems/count-of-smaller-numbers-after-self
"""


class LeetCode315_SegmentTree:
    """
    LeetCode 315. 计算右侧小于当前元素的个数的线段树实现
    """
    
    def __init__(self):
        """构造函数"""
        self.tree = []  # 线段树数组，存储区间元素个数
        self.n = 0      # 离散化后的数组大小
    
    def countSmaller(self, nums):
        """
        计算右侧小于当前元素的个数
        :param nums: 输入数组
        :return: 结果数组
        """
        result = []
        if not nums:
            return result
        
        # 离散化处理
        sorted_nums = sorted(nums)
        # 去重
        unique = list(dict.fromkeys(sorted_nums))  # 保持顺序的去重
        self.n = len(unique)
        
        # 线段树数组大小通常为4*n，确保足够容纳所有节点
        self.tree = [0] * (self.n << 2)
        
        # 从右向左遍历数组
        for i in range(len(nums) - 1, -1, -1):
            # 找到当前元素在离散化数组中的位置
            index = self._binary_search(unique, nums[i])
            # 查询比当前元素小的元素个数（即查询[0, index-1]区间内的元素个数）
            count = self._query(0, self.n - 1, 1, 0, index - 1)
            result.append(count)
            # 将当前元素插入线段树
            self._update(0, self.n - 1, 1, index)
        
        # 由于是从右向左遍历的，需要反转结果
        result.reverse()
        return result
    
    def _binary_search(self, arr, target):
        """
        二分查找元素在数组中的位置
        :param arr: 已排序数组
        :param target: 目标值
        :return: 目标值在数组中的索引
        """
        left, right = 0, len(arr) - 1
        while left <= right:
            mid = left + (right - left) // 2
            if arr[mid] == target:
                return mid
            elif arr[mid] < target:
                left = mid + 1
            else:
                right = mid - 1
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
    """测试LeetCode 315实现"""
    print("测试LeetCode 315实现...")
    
    # 测试用例
    solution = LeetCode315_SegmentTree()
    nums = [5, 2, 6, 1]
    
    print(f"输入数组: {nums}")
    result = solution.countSmaller(nums)
    print(f"输出结果: {result}")  # 应该输出[2, 1, 1, 0]
    
    print("LeetCode 315测试完成！")


if __name__ == "__main__":
    test_solution()