#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
线段树实现 - 同时支持范围重置、范围增加、范围查询
维护累加和

时间复杂度分析:
- 建树: O(n)
- 单点更新: O(log n)
- 区间更新: O(log n)
- 区间查询: O(log n)

空间复杂度: O(4n)
"""

class SegmentTreeUpdateAddQuerySum:
    def __init__(self, size):
        """
        初始化线段树
        
        Args:
            size: 数组大小
        """
        self.n = size
        # 线段树数组通常开4倍空间，确保有足够空间存储所有节点
        self.sum = [0] * (size * 4)
        self.add = [0] * (size * 4)
        self.change = [0] * (size * 4)
        self.update = [False] * (size * 4)

    def push_up(self, i):
        """
        向上更新节点信息 - 累加和信息的汇总
        
        Args:
            i: 当前节点编号
        """
        self.sum[i] = self.sum[i << 1] + self.sum[i << 1 | 1]

    def push_down(self, i, ln, rn):
        """
        向下传递懒标记
        
        Args:
            i: 当前节点编号
            ln: 左子树节点数量
            rn: 右子树节点数量
        """
        if self.update[i]:
            self.update_lazy(i << 1, self.change[i], ln)
            self.update_lazy(i << 1 | 1, self.change[i], rn)
            self.update[i] = False
        if self.add[i] != 0:
            self.add_lazy(i << 1, self.add[i], ln)
            self.add_lazy(i << 1 | 1, self.add[i], rn)
            self.add[i] = 0

    def update_lazy(self, i, v, n):
        """
        重置操作的懒标记
        
        Args:
            i: 节点编号
            v: 重置的值
            n: 节点对应的区间长度
        """
        self.sum[i] = v * n
        self.add[i] = 0
        self.change[i] = v
        self.update[i] = True

    def add_lazy(self, i, v, n):
        """
        增加操作的懒标记
        
        Args:
            i: 节点编号
            v: 增加的值
            n: 节点对应的区间长度
        """
        self.sum[i] += v * n
        self.add[i] += v

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
            self.sum[i] = arr[l]
        else:
            mid = (l + r) >> 1
            self.build(arr, l, mid, i << 1)
            self.build(arr, mid + 1, r, i << 1 | 1)
            self.push_up(i)
        self.add[i] = 0
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
            self.update_lazy(i, jobv, r - l + 1)
        else:
            mid = (l + r) >> 1
            self.push_down(i, mid - l + 1, r - mid)
            if jobl <= mid:
                self.update_range(jobl, jobr, jobv, l, mid, i << 1)
            if jobr > mid:
                self.update_range(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)
            self.push_up(i)

    def add_range(self, jobl, jobr, jobv, l, r, i):
        """
        范围修改 - jobl ~ jobr范围上每个数字增加jobv
        
        Args:
            jobl: 任务区间左端点
            jobr: 任务区间右端点
            jobv: 增加的值
            l: 当前区间左端点
            r: 当前区间右端点
            i: 当前节点编号
        """
        if jobl <= l and r <= jobr:
            self.add_lazy(i, jobv, r - l + 1)
        else:
            mid = (l + r) >> 1
            self.push_down(i, mid - l + 1, r - mid)
            if jobl <= mid:
                self.add_range(jobl, jobr, jobv, l, mid, i << 1)
            if jobr > mid:
                self.add_range(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)
            self.push_up(i)

    def query(self, jobl, jobr, l, r, i):
        """
        查询累加和
        
        Args:
            jobl: 查询区间左端点
            jobr: 查询区间右端点
            l: 当前区间左端点
            r: 当前区间右端点
            i: 当前节点编号
            
        Returns:
            区间和
        """
        if jobl <= l and r <= jobr:
            return self.sum[i]
        mid = (l + r) >> 1
        self.push_down(i, mid - l + 1, r - mid)
        ans = 0
        if jobl <= mid:
            ans += self.query(jobl, jobr, l, mid, i << 1)
        if jobr > mid:
            ans += self.query(jobl, jobr, mid + 1, r, i << 1 | 1)
        return ans


# 测试代码
if __name__ == "__main__":
    # 示例测试
    print("线段树测试 - 支持范围重置、范围增加和范围查询")
    seg_tree = SegmentTreeUpdateAddQuerySum(10)
    print("初始化完成")