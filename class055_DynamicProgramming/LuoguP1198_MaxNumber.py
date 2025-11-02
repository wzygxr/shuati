#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import sys

"""
Luogu P1198 [JSOI2008] 最大数
题目链接: https://www.luogu.com.cn/problem/P1198

题目描述:
维护一个数列，支持两种操作：
1. 查询操作 Q L: 查询当前数列中末尾L个数中的最大数
2. 插入操作 A n: 将n加上最近一次查询操作的答案t（初始为0），对D取模后插入数列末尾

解题思路:
使用线段树来维护数列，支持区间最大值查询和单点更新操作。
由于数列是动态增长的，我们可以预先开一个足够大的线段树数组，
用一个指针记录当前数列的实际长度。

时间复杂度分析:
- 建树: O(n)
- 单点更新: O(log n)
- 区间查询: O(log n)

空间复杂度: O(4n)
"""


class SegmentTree:
    def __init__(self, size):
        """
        初始化线段树
        
        Args:
            size: 线段树大小
        """
        self.n = size
        self.max_val = [float('-inf')] * (4 * size)  # 线段树数组，存储区间最大值
        self.arr = [0] * size  # 原始数组
        self.size = 0  # 当前数列的实际长度

    def push_up(self, i):
        """
        向上更新节点信息 - 最大值信息的汇总
        
        Args:
            i: 当前节点编号
        """
        self.max_val[i] = max(self.max_val[i << 1], self.max_val[i << 1 | 1])

    def update(self, idx, val, l, r, i):
        """
        单点更新 - 在位置idx处插入值val
        
        Args:
            idx: 要更新的位置
            val: 新的值
            l: 当前区间左端点
            r: 当前区间右端点
            i: 当前节点编号
        """
        if l == r:
            self.max_val[i] = val
            self.arr[idx] = val
        else:
            mid = (l + r) >> 1
            if idx <= mid:
                self.update(idx, val, l, mid, i << 1)
            else:
                self.update(idx, val, mid + 1, r, i << 1 | 1)
            self.push_up(i)

    def query_max(self, jobl, jobr, l, r, i):
        """
        区间最大值查询
        
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
        mid = (l + r) >> 1
        ans = float('-inf')
        if jobl <= mid:
            ans = max(ans, self.query_max(jobl, jobr, l, mid, i << 1))
        if jobr > mid:
            ans = max(ans, self.query_max(jobl, jobr, mid + 1, r, i << 1 | 1))
        return ans

    def add(self, val):
        """
        在数列末尾添加一个数
        
        Args:
            val: 要添加的值
        """
        self.update(self.size, val, 0, self.n - 1, 1)
        self.size += 1

    def query_last_l(self, l):
        """
        查询末尾L个数中的最大值
        
        Args:
            l: 查询的个数
            
        Returns:
            最大值
        """
        # 查询区间为 [size-L, size-1]
        return self.query_max(self.size - l, self.size - 1, 0, self.n - 1, 1)

    def get_size(self):
        """
        获取当前数列长度
        
        Returns:
            数列长度
        """
        return self.size


def main():
    """主函数"""
    # 为了简化处理，我们使用示例输入
    # 实际使用时应该用: import sys; input = sys.stdin.read
    input_lines = [
        "10 7",
        "A 1",
        "A 2",
        "A 3",
        "Q 2",
        "A 4",
        "Q 3",
        "A 5",
        "Q 4",
        "A 6",
        "Q 5"
    ]
    
    try:
        # 解析第一行输入
        m, d = map(int, input_lines[0].split())  # 操作个数和取模常数
        
        # 初始化线段树，大小为M足够使用
        seg_tree = SegmentTree(m)
        
        last_query_result = 0  # 最近一次查询操作的答案，初始为0
        
        # 处理每个操作
        for i in range(1, m + 1):
            operation = input_lines[i].split()
            op_type = operation[0]
            
            if op_type == 'A':
                # 插入操作
                n = int(operation[1])
                val = (n + last_query_result) % d
                seg_tree.add(val)
            elif op_type == 'Q':
                # 查询操作
                l = int(operation[1])
                last_query_result = seg_tree.query_last_l(l)
                print(last_query_result)
    except Exception as e:
        print(f"处理输入时发生错误: {e}", file=sys.stderr)
        raise


# 测试代码
if __name__ == "__main__":
    main()