#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Codeforces 52C Circular RMQ

题目描述：
给定一个长度为n的环形数组（即a[0]的前一个元素是a[n-1]，a[n-1]的后一个元素是a[0]），
需要处理以下两种操作：
1. 将区间[l, r]中的每个元素都加上v（如果l > r，则表示环形区间[l, n-1]和[0, r]）
2. 查询区间[l, r]中所有元素的最小值（如果l > r，则表示环形区间[l, n-1]和[0, r]）

解题思路：
这是一个环形线段树问题，需要处理环形区间操作。
1. 对于环形区间操作，如果l > r，可以将其拆分为两个普通区间[l, n-1]和[0, r]
2. 使用线段树配合懒标记来处理区间更新和区间最值查询

时间复杂度分析：
- 初始化：O(n)
- 区间更新：O(log n)
- 区间查询：O(log n)

空间复杂度分析：
- O(n)，线段树需要4*n的空间来存储节点信息

链接：https://codeforces.com/contest/52/problem/C
"""


class Codeforces52C_SegmentTree:
    """
    Codeforces 52C Circular RMQ的线段树实现
    """
    
    def __init__(self, nums):
        """
        构造函数，初始化线段树
        :param nums: 输入数组
        """
        self.n = len(nums)
        self.nums = nums[:]
        # 线段树数组大小通常为4*n，确保足够容纳所有节点
        self.tree = [float('inf')] * (self.n << 2)
        self.lazy = [0] * (self.n << 2)
        # 构建线段树
        self._build_tree(0, self.n - 1, 1)
    
    def _build_tree(self, start, end, node):
        """
        构建线段树
        :param start: 区间起始位置
        :param end: 区间结束位置
        :param node: 当前节点在tree数组中的索引
        """
        # 清空懒标记
        self.lazy[node] = 0
        
        # 如果是叶子节点，直接赋值
        if start == end:
            self.tree[node] = self.nums[start]
            return
        
        # 计算中点
        mid = (start + end) // 2
        # 递归构建左右子树
        self._build_tree(start, mid, node * 2)
        self._build_tree(mid + 1, end, node * 2 + 1)
        # 合并左右子树信息，取最小值
        self.tree[node] = min(self.tree[node * 2], self.tree[node * 2 + 1])
    
    def _push_down(self, node, start, end):
        """
        下推懒标记
        :param node: 当前节点
        :param start: 当前节点区间起始位置
        :param end: 当前节点区间结束位置
        """
        if self.lazy[node] != 0:
            # 将懒标记下推到左右子节点
            self.lazy[node * 2] += self.lazy[node]
            self.lazy[node * 2 + 1] += self.lazy[node]
            
            # 如果不是叶子节点，更新子节点的值
            if start != end:
                self.tree[node * 2] += self.lazy[node]
                self.tree[node * 2 + 1] += self.lazy[node]
            
            # 清空当前节点的懒标记
            self.lazy[node] = 0
    
    def _update_range(self, start, end, node, left, right, val):
        """
        区间更新
        :param start: 当前节点区间起始位置
        :param end: 当前节点区间结束位置
        :param node: 当前节点在tree数组中的索引
        :param left: 更新区间左边界
        :param right: 更新区间右边界
        :param val: 更新值
        """
        # 如果当前区间与更新区间无重叠，直接返回
        if start > right or end < left:
            return
        
        # 如果当前区间完全包含在更新区间内
        if start >= left and end <= right:
            # 更新当前节点的值
            self.tree[node] += val
            # 设置懒标记
            if start != end:
                self.lazy[node] += val
            return
        
        # 下推懒标记
        self._push_down(node, start, end)
        
        # 递归更新左右子树
        mid = (start + end) // 2
        self._update_range(start, mid, node * 2, left, right, val)
        self._update_range(mid + 1, end, node * 2 + 1, left, right, val)
        
        # 合并左右子树信息，取最小值
        self.tree[node] = min(self.tree[node * 2], self.tree[node * 2 + 1])
    
    def update(self, left, right, val):
        """
        区间更新接口（处理环形区间）
        :param left: 更新区间左边界
        :param right: 更新区间右边界
        :param val: 更新值
        """
        # 处理环形区间
        if left <= right:
            self._update_range(0, self.n - 1, 1, left, right, val)
        else:
            # 环形区间拆分为两个普通区间
            self._update_range(0, self.n - 1, 1, left, self.n - 1, val)
            self._update_range(0, self.n - 1, 1, 0, right, val)
    
    def _query_range(self, start, end, node, left, right):
        """
        区间查询
        :param start: 当前节点区间起始位置
        :param end: 当前节点区间结束位置
        :param node: 当前节点在tree数组中的索引
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间最小值
        """
        # 如果当前区间与查询区间无重叠，返回最大值
        if start > right or end < left:
            return float('inf')
        
        # 如果当前区间完全包含在查询区间内
        if start >= left and end <= right:
            return self.tree[node]
        
        # 下推懒标记
        self._push_down(node, start, end)
        
        # 递归查询左右子树
        mid = (start + end) // 2
        left_min = self._query_range(start, mid, node * 2, left, right)
        right_min = self._query_range(mid + 1, end, node * 2 + 1, left, right)
        
        return min(left_min, right_min)
    
    def query(self, left, right):
        """
        区间查询接口（处理环形区间）
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间最小值
        """
        # 处理环形区间
        if left <= right:
            return self._query_range(0, self.n - 1, 1, left, right)
        else:
            # 环形区间拆分为两个普通区间
            min1 = self._query_range(0, self.n - 1, 1, left, self.n - 1)
            min2 = self._query_range(0, self.n - 1, 1, 0, right)
            return min(min1, min2)


# 测试函数
def test_solution():
    """测试Codeforces 52C实现"""
    print("测试Codeforces 52C实现...")
    
    # 测试用例
    nums = [1, 2, 3, 4, 5]
    seg_tree = Codeforces52C_SegmentTree(nums)
    
    print(f"初始数组: {nums}")
    print(f"查询区间[1,3]的最小值: {seg_tree.query(1, 3)}")  # 应该输出2
    
    # 区间更新：将区间[1,3]中的每个元素都加上2
    seg_tree.update(1, 3, 2)
    print("将区间[1,3]中的每个元素都加上2后:")
    print(f"查询区间[1,3]的最小值: {seg_tree.query(1, 3)}")  # 应该输出4
    
    # 环形区间查询：查询区间[3,1]（环形）
    # 环形区间[3,1]包含元素索引3,4,0,1，对应值为4,5,1,2，加上之前的更新后为4,5,1,4，最小值是1
    print(f"环形区间[3,1]的最小值: {seg_tree.query(3, 1)}")  # 应该输出1
    
    print("Codeforces 52C测试完成！")


if __name__ == "__main__":
    test_solution()