#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
HDU 1166 敌兵布阵

题目描述：
A国在海岸线沿直线布置了N个工兵营地，每个营地初始有一定数量的士兵。
有两种操作：
1. Add i j: 第i个营地增加j个士兵
2. Query i j: 查询第i到第j个营地之间士兵总数

解题思路：
这是一个典型的线段树单点更新、区间查询问题。
1. 构建线段树存储每个区间的士兵总数
2. Add操作对应线段树的单点更新
3. Query操作对应线段树的区间查询

时间复杂度分析：
- 初始化：O(n)
- 单点更新：O(log n)
- 区间查询：O(log n)

空间复杂度分析：
- O(n)，线段树需要4*n的空间来存储节点信息

链接：http://acm.hdu.edu.cn/showproblem.php?pid=1166
"""


class HDU1166_SegmentTree:
    """
    HDU 1166 敌兵布阵的线段树实现
    """
    
    def __init__(self, nums):
        """
        构造函数，初始化线段树
        :param nums: 输入数组
        """
        self.n = len(nums)
        self.nums = nums[:]
        # 线段树数组大小通常为4*n，确保足够容纳所有节点
        self.tree = [0] * (self.n << 2)
        # 构建线段树
        self._build_tree(0, self.n - 1, 1)
    
    def _build_tree(self, start, end, node):
        """
        构建线段树
        :param start: 区间起始位置
        :param end: 区间结束位置
        :param node: 当前节点在tree数组中的索引
        """
        # 如果是叶子节点，直接赋值
        if start == end:
            self.tree[node] = self.nums[start]
            return
        
        # 计算中点
        mid = (start + end) // 2
        # 递归构建左右子树
        self._build_tree(start, mid, node * 2)
        self._build_tree(mid + 1, end, node * 2 + 1)
        # 合并左右子树信息
        self.tree[node] = self.tree[node * 2] + self.tree[node * 2 + 1]
    
    def _update_point(self, start, end, node, index, val):
        """
        单点更新
        :param start: 当前节点区间起始位置
        :param end: 当前节点区间结束位置
        :param node: 当前节点在tree数组中的索引
        :param index: 要更新的位置
        :param val: 增加的值
        """
        # 如果index不在当前区间范围内，直接返回
        if start > index or end < index:
            return
        
        # 如果是叶子节点，直接增加计数
        if start == end:
            self.tree[node] += val
            return
        
        # 递归更新子节点
        mid = (start + end) // 2
        if index <= mid:
            self._update_point(start, mid, node * 2, index, val)
        else:
            self._update_point(mid + 1, end, node * 2 + 1, index, val)
        
        # 合并左右子树信息
        self.tree[node] = self.tree[node * 2] + self.tree[node * 2 + 1]
    
    def add(self, index, val):
        """
        单点更新接口
        :param index: 要更新的位置
        :param val: 增加的值
        """
        self._update_point(0, self.n - 1, 1, index, val)
    
    def _query_range(self, start, end, node, left, right):
        """
        区间查询
        :param start: 当前节点区间起始位置
        :param end: 当前节点区间结束位置
        :param node: 当前节点在tree数组中的索引
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间和
        """
        # 如果当前区间与查询区间无重叠，返回0
        if start > right or end < left:
            return 0
        
        # 如果当前区间完全包含在查询区间内，返回当前节点的值
        if start >= left and end <= right:
            return self.tree[node]
        
        # 递归查询左右子树
        mid = (start + end) // 2
        left_sum = self._query_range(start, mid, node * 2, left, right)
        right_sum = self._query_range(mid + 1, end, node * 2 + 1, left, right)
        
        return left_sum + right_sum
    
    def query(self, left, right):
        """
        区间查询接口
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间和
        """
        return self._query_range(0, self.n - 1, 1, left, right)


# 测试函数
def test_solution():
    """测试HDU 1166实现"""
    print("测试HDU 1166实现...")
    
    # 测试用例
    nums = [1, 2, 3, 4, 5]
    seg_tree = HDU1166_SegmentTree(nums)
    
    print(f"初始数组: {nums}")
    print(f"查询区间[1,3]的和: {seg_tree.query(1, 3)}")  # 应该输出9
    
    # 单点更新：第2个营地增加3个士兵
    seg_tree.add(2, 3)
    print("第2个营地增加3个士兵后:")
    print(f"查询区间[1,3]的和: {seg_tree.query(1, 3)}")  # 应该输出12
    
    print("HDU 1166测试完成！")


if __name__ == "__main__":
    test_solution()