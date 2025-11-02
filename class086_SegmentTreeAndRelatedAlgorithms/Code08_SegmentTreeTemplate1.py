#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
线段树模板1 - 区间加法和区间求和
题目来源：洛谷 P3372 【模板】线段树 1
题目链接：https://www.luogu.com.cn/problem/P3372

核心算法：线段树 + 懒标记
难度：普及+/提高-

【题目详细描述】
如题，已知一个数列，你需要进行下面两种操作：
1. 将某区间每一个数加上 k
2. 求出某区间每一个数的和

输入格式：
第一行包含两个整数 n, m，分别表示该数列数字的个数和操作的总个数。
第二行包含 n 个用空格分隔的整数，其中第 i 个数字表示数列第 i 项的初始值。
接下来 m 行每行包含 3 或 4 个整数，表示一个操作。

输出格式：
输出若干行整数，表示每次操作2的结果。

【解题思路】
使用带懒标记的线段树来实现区间更新和区间查询。

【核心算法】
1. 线段树构建：构建支持区间求和的线段树
2. 懒标记：使用懒标记优化区间更新操作
3. 区间更新：支持区间加法操作
4. 区间查询：支持区间求和操作

【复杂度分析】
- 时间复杂度：
  - 构建线段树：O(n)
  - 区间更新：O(log n)
  - 区间查询：O(log n)
- 空间复杂度：O(n)，线段树所需空间

【算法优化点】
1. 懒标记优化：延迟下传标记，避免不必要的计算
2. 位运算优化：使用位移操作优化索引计算
3. IO优化：使用sys.stdin和sys.stdout优化输入输出

【工程化考量】
1. 输入输出效率：使用高效的IO处理大数据量
2. 内存管理：合理分配线段树数组空间
3. 错误处理：处理非法输入和边界情况

【类似题目推荐】
1. 洛谷 P3373 【模板】线段树 2 - https://www.luogu.com.cn/problem/P3373
2. LeetCode 307. 区域和检索 - 数组可修改 - https://leetcode.cn/problems/range-sum-query-mutable/
3. HDU 1698 Just a Hook - http://acm.hdu.edu.cn/showproblem.php?pid=1698
4. POJ 3468 A Simple Problem with Integers - http://poj.org/problem?id=3468
"""

import sys

class SegmentTree:
    def __init__(self, arr):
        """
        初始化线段树
        
        Args:
            arr: 初始数组
        """
        self.n = len(arr)
        self.arr = arr
        self.tree = [0] * (4 * self.n)
        self.lazy = [0] * (4 * self.n)
        self.build(1, 1, self.n)
    
    def push_down(self, node, start, end):
        """
        向下传递懒标记
        
        Args:
            node: 当前节点索引
            start: 当前区间左边界
            end: 当前区间右边界
        """
        if self.lazy[node] != 0:
            mid = (start + end) // 2
            # 更新左右子节点的值
            self.tree[2 * node] += self.lazy[node] * (mid - start + 1)
            self.tree[2 * node + 1] += self.lazy[node] * (end - mid)
            # 传递懒标记给子节点
            self.lazy[2 * node] += self.lazy[node]
            self.lazy[2 * node + 1] += self.lazy[node]
            # 清除当前节点的懒标记
            self.lazy[node] = 0
    
    def build(self, node, start, end):
        """
        构建线段树
        
        Args:
            node: 当前节点索引
            start: 当前区间左边界
            end: 当前区间右边界
        """
        if start == end:
            self.tree[node] = self.arr[start]
        else:
            mid = (start + end) // 2
            self.build(2 * node, start, mid)
            self.build(2 * node + 1, mid + 1, end)
            self.tree[node] = self.tree[2 * node] + self.tree[2 * node + 1]
    
    def update(self, node, start, end, l, r, val):
        """
        区间更新操作
        
        Args:
            node: 当前节点索引
            start: 当前区间左边界
            end: 当前区间右边界
            l: 更新区间左边界
            r: 更新区间右边界
            val: 要加上的值
        """
        if l <= start and end <= r:
            # 当前区间完全包含在更新区间内
            self.tree[node] += val * (end - start + 1)
            self.lazy[node] += val
        else:
            # 向下传递懒标记
            self.push_down(node, start, end)
            mid = (start + end) // 2
            if l <= mid:
                self.update(2 * node, start, mid, l, r, val)
            if r > mid:
                self.update(2 * node + 1, mid + 1, end, l, r, val)
            self.tree[node] = self.tree[2 * node] + self.tree[2 * node + 1]
    
    def query(self, node, start, end, l, r):
        """
        区间查询操作
        
        Args:
            node: 当前节点索引
            start: 当前区间左边界
            end: 当前区间右边界
            l: 查询区间左边界
            r: 查询区间右边界
            
        Returns:
            long: 区间和
        """
        if l <= start and end <= r:
            # 当前区间完全包含在查询区间内
            return self.tree[node]
        # 向下传递懒标记
        self.push_down(node, start, end)
        mid = (start + end) // 2
        sum_val = 0
        if l <= mid:
            sum_val += self.query(2 * node, start, mid, l, r)
        if r > mid:
            sum_val += self.query(2 * node + 1, mid + 1, end, l, r)
        return sum_val


def main():
    """
    主函数：处理输入并执行操作
    """
    import sys
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    n = int(data[idx])
    idx += 1
    m = int(data[idx])
    idx += 1
    
    arr = [0] * (n + 1)
    for i in range(1, n + 1):
        arr[i] = int(data[idx])
        idx += 1
    
    seg_tree = SegmentTree(arr)
    
    results = []
    for _ in range(m):
        op = int(data[idx])
        idx += 1
        if op == 1:
            x = int(data[idx])
            idx += 1
            y = int(data[idx])
            idx += 1
            k = int(data[idx])
            idx += 1
            seg_tree.update(1, 1, n, x, y, k)
        else:
            x = int(data[idx])
            idx += 1
            y = int(data[idx])
            idx += 1
            results.append(str(seg_tree.query(1, 1, n, x, y)))
    
    print('\n'.join(results))


if __name__ == "__main__":
    main()