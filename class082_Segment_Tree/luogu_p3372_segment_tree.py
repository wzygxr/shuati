#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
洛谷P3372 【模板】线段树1

题目描述：
如题，已知一个数列，你需要进行下面两种操作：
1. 将某区间每一个数加上x
2. 求出某区间每一个数的和

解题思路：
使用线段树配合懒标记(Lazy Propagation)来解决区间更新问题。
1. 线段树节点维护区间和
2. 懒标记用于延迟区间更新操作，避免每次都更新到叶子节点
3. 在需要访问子节点时，将懒标记下推(push down)

时间复杂度分析：
- 初始化：O(n)
- 区间更新：O(log n)
- 区间查询：O(log n)

空间复杂度分析：
- O(n)，线段树需要4*n的空间来存储节点信息

链接：https://www.luogu.com.cn/problem/P3372
"""


class LuoguP3372_SegmentTree:
    """
    洛谷P3372 【模板】线段树1的线段树实现
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
        # 合并左右子树信息
        self.tree[node] = self.tree[node * 2] + self.tree[node * 2 + 1]
    
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
                mid = (start + end) // 2
                self.tree[node * 2] += self.lazy[node] * (mid - start + 1)
                self.tree[node * 2 + 1] += self.lazy[node] * (end - mid)
            
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
            self.tree[node] += val * (end - start + 1)
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
        
        # 合并左右子树信息
        self.tree[node] = self.tree[node * 2] + self.tree[node * 2 + 1]
    
    def update(self, left, right, val):
        """
        区间更新接口
        :param left: 更新区间左边界
        :param right: 更新区间右边界
        :param val: 更新值
        """
        self._update_range(0, self.n - 1, 1, left, right, val)
    
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
        
        # 如果当前区间完全包含在查询区间内
        if start >= left and end <= right:
            return self.tree[node]
        
        # 下推懒标记
        self._push_down(node, start, end)
        
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
    """测试洛谷P3372实现"""
    print("测试洛谷P3372实现...")
    
    # 测试用例
    nums = [1, 2, 3, 4, 5]
    seg_tree = LuoguP3372_SegmentTree(nums)
    
    print(f"初始数组: {nums}")
    print(f"查询区间[1,3]的和: {seg_tree.query(1, 3)}")  # 应该输出9
    
    # 区间更新：将区间[1,3]中的每个元素都加上2
    seg_tree.update(1, 3, 2)
    print("将区间[1,3]中的每个元素都加上2后:")
    print(f"查询区间[1,3]的和: {seg_tree.query(1, 3)}")  # 应该输出15
    
    print("洛谷P3372测试完成！")


if __name__ == "__main__":
    test_solution()