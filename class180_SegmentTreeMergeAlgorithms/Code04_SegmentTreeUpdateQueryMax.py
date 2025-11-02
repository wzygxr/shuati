#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
线段树实现 - 支持范围重置、范围查询
维护最大值

时间复杂度分析:
- 建树: O(n)
- 单点更新: O(log n)
- 区间更新: O(log n)
- 区间查询: O(log n)

空间复杂度: O(4n)
"""

class SegmentTreeUpdateQueryMax:
    def __init__(self, size):
        """
        初始化线段树
        
        Args:
            size: 数组大小
        """
        self.n = size
        # 线段树数组通常开4倍空间，确保有足够空间存储所有节点
        self.max_val = [0] * (size * 4)
        self.change = [0] * (size * 4)
        self.update = [False] * (size * 4)

    def push_up(self, i):
        """
        向上更新节点信息 - 最大值信息的汇总
        
        Args:
            i: 当前节点编号
        """
        self.max_val[i] = max(self.max_val[i << 1], self.max_val[i << 1 | 1])

    def push_down(self, i):
        """
        向下传递懒标记
        
        Args:
            i: 当前节点编号
        """
        if self.update[i]:
            self.lazy(i << 1, self.change[i])
            self.lazy(i << 1 | 1, self.change[i])
            self.update[i] = False

    def lazy(self, i, v):
        """
        懒标记操作
        
        Args:
            i: 节点编号
            v: 重置的值
        """
        self.max_val[i] = v
        self.change[i] = v
        self.update[i] = True

    def build(self, arr, l, r, i):
        """
        建树
        
        Args:
            arr: 原始数组
            l: 当前区间左端点
            r: 当前区间右端点
            i: 当前节点编号
        """
        if l == r:
            self.max_val[i] = arr[l]
        else:
            mid = (l + r) >> 1
            self.build(arr, l, mid, i << 1)
            self.build(arr, mid + 1, r, i << 1 | 1)
            self.push_up(i)
        self.change[i] = 0
        self.update[i] = False

    def update_range(self, jobl, jobr, jobv, l, r, i):
        """
        范围重置 - jobl ~ jobr范围上每个数字重置为jobv
        
        Args:
            jobl: 任务区间左端点
            jobr: 任务区间右端点
            jobv: 重置的值
            l: 当前区间左端点
            r: 当前区间右端点
            i: 当前节点编号
        """
        if jobl <= l and r <= jobr:
            self.lazy(i, jobv)
        else:
            self.push_down(i)
            mid = (l + r) >> 1
            if jobl <= mid:
                self.update_range(jobl, jobr, jobv, l, mid, i << 1)
            if jobr > mid:
                self.update_range(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)
            self.push_up(i)

    def query(self, jobl, jobr, l, r, i):
        """
        查询最大值
        
        Args:
            jobl: 查询区间左端点
            jobr: 查询区间右端点
            l: 当前区间左端点
            r: 当前区间右端点
            i: 当前节点编号
            
        Returns:
            区间最大值
        """
        if jobl <= l and r <= jobr:
            return self.max_val[i]
        self.push_down(i)
        mid = (l + r) >> 1
        ans = float('-inf')
        if jobl <= mid:
            ans = max(ans, self.query(jobl, jobr, l, mid, i << 1))
        if jobr > mid:
            ans = max(ans, self.query(jobl, jobr, mid + 1, r, i << 1 | 1))
        return ans


# 测试代码
if __name__ == "__main__":
    # 示例测试
    print("线段树测试 - 支持范围重置和范围查询最大值")
    seg_tree = SegmentTreeUpdateQueryMax(10)
    print("初始化完成")