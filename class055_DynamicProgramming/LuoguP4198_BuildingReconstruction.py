#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Luogu P4198 楼房重建
题目链接: https://www.luogu.com.cn/problem/P4198

题目描述:
小A在平面上(0,0)点的位置，第i栋楼房可以用一条连接(i,0)和(i,Hi)的线段表示。
如果这栋楼房上存在一个高度大于0的点与(0,0)的连线没有与之前的线段相交，那么这栋楼房就被认为是可见的。
每天建筑队会修改一栋楼房的高度，求每天小A能看到多少栋楼房。

解题思路:
这是一个经典的线段树问题。关键在于将问题转化为斜率比较问题。
从原点(0,0)能看到第i栋楼，当且仅当第i栋楼的斜率Hi/i大于前面所有楼的斜率。
因此，我们需要维护区间最大值，并统计从左到右严格递增的斜率个数。

我们使用线段树来维护每个区间的以下信息：
1. 区间最大值
2. 区间内从左端点开始能看到的楼房数量（在给定左端点限制斜率的情况下）

时间复杂度分析:
- 单点更新: O(log n)
- 查询全局可见楼房数: O(log n)

空间复杂度: O(4n)
"""

import sys


class Node:
    def __init__(self, max_slope=0, visible_count=0):
        """
        线段树节点类
        
        Args:
            max_slope: 区间最大斜率
            visible_count: 区间内可见楼房数量
        """
        self.max_slope = max_slope      # 区间最大斜率
        self.visible_count = visible_count  # 区间内可见楼房数量


class SegmentTree:
    def __init__(self, size):
        """
        初始化线段树
        
        Args:
            size: 数组大小
        """
        self.n = size
        self.tree = [Node() for _ in range(4 * size)]  # 线段树数组
        self.slopes = [0.0] * (size + 1)              # 存储每个位置的斜率

    def push_up(self, i):
        """
        向上更新节点信息
        
        Args:
            i: 当前节点编号
        """
        self.tree[i].max_slope = max(self.tree[i << 1].max_slope, self.tree[i << 1 | 1].max_slope)

    def count_visible(self, l, r, limit, i):
        """
        计算区间[l,r]内从左端点开始，在限制斜率limit下可见的楼房数量
        
        Args:
            l: 区间左端点
            r: 区间右端点
            limit: 限制斜率
            i: 当前节点编号
            
        Returns:
            可见楼房数量
        """
        # 如果整个区间最大斜率都不超过限制，那么这个区间内没有可见楼房
        if self.tree[i].max_slope <= limit:
            return 0

        # 叶子节点
        if l == r:
            return 1 if self.slopes[l] > limit else 0

        mid = (l + r) >> 1
        # 如果左子树最大斜率不超过限制，只考虑右子树
        if self.tree[i << 1].max_slope <= limit:
            return self.count_visible(mid + 1, r, limit, i << 1 | 1)
        else:
            # 否则左子树中有可见的，加上右子树中可见的
            return self.tree[i << 1].visible_count + self.count_visible(
                mid + 1, r, max(limit, self.tree[i << 1].max_slope), i << 1 | 1)

    def update_visible_count(self, l, r, i):
        """
        更新节点可见数量
        
        Args:
            l: 区间左端点
            r: 区间右端点
            i: 当前节点编号
        """
        if l == r:
            self.tree[i].visible_count = 1 if self.slopes[l] > 0 else 0
        else:
            mid = (l + r) >> 1
            self.update_visible_count(l, mid, i << 1)
            self.update_visible_count(mid + 1, r, i << 1 | 1)
            self.tree[i].visible_count = self.count_visible(l, r, 0, i)

    def update(self, idx, val, l, r, i):
        """
        单点更新
        
        Args:
            idx: 要更新的位置
            val: 新的高度
            l: 当前区间左端点
            r: 当前区间右端点
            i: 当前节点编号
        """
        if l == r:
            self.slopes[idx] = 0 if val == 0 else val / idx
            self.tree[i].max_slope = self.slopes[idx]
            self.tree[i].visible_count = 1 if val > 0 else 0
        else:
            mid = (l + r) >> 1
            if idx <= mid:
                self.update(idx, val, l, mid, i << 1)
            else:
                self.update(idx, val, mid + 1, r, i << 1 | 1)
            self.push_up(i)
            self.tree[i].visible_count = self.count_visible(l, r, 0, i)

    def query_visible_count(self):
        """
        查询全局可见楼房数量
        
        Returns:
            可见楼房数量
        """
        return self.tree[1].visible_count


def main():
    """主函数"""
    # 为了简化处理，我们使用示例输入
    # 实际使用时应该用: import sys; input = sys.stdin.read
    input_lines = [
        "5 4",
        "1 1",
        "2 2",
        "3 1",
        "4 3"
    ]
    
    try:
        # 解析第一行输入
        n, m = map(int, input_lines[0].split())  # 楼房数量和操作天数
        
        # 初始化线段树
        seg_tree = SegmentTree(n)
        
        # 处理每天的操作
        for i in range(1, m + 1):
            operation = input_lines[i].split()
            x = int(operation[0])  # 楼房编号
            y = int(operation[1])  # 新的高度
            
            # 更新楼房高度
            seg_tree.update(x, y, 1, n, 1)
            
            # 查询并输出可见楼房数量
            visible_count = seg_tree.query_visible_count()
            print(visible_count)
    except Exception as e:
        print(f"处理输入时发生错误: {e}", file=sys.stderr)
        raise


# 测试代码
if __name__ == "__main__":
    main()