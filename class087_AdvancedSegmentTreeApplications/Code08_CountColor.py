#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
POJ 2777 - Count Color

题目描述：
给定一个长度为L的板条(1 <= L <= 100000)，初始时所有位置都是颜色1
执行O次操作(1 <= O <= 100000)，操作类型：
1. "C A B C": 将区间[A,B]染成颜色C
2. "P A B": 查询区间[A,B]中有多少种不同的颜色

解题思路：
使用线段树维护区间信息，每个节点存储以下信息：
1. 区间颜色集合(用位运算表示，第i位为1表示有颜色i)
2. 懒惰标记(表示区间被染成的颜色)

关键技术：
1. 位运算优化：用一个整数的二进制位表示颜色集合，第i位为1表示有颜色i
2. 懒惰标记：延迟更新子区间
3. 区间染色：将整个区间染成同一种颜色

时间复杂度分析：
1. 建树：O(L)
2. 更新：O(log L)
3. 查询：O(log L)
4. 空间复杂度：O(L)

是否最优解：是
这是解决区间染色和颜色计数问题的最优解法，时间复杂度为O(log L)

工程化考量：
1. 位运算优化：使用位运算高效表示颜色集合
2. 懒惰标记：延迟更新子区间，提高效率
3. 内存管理：预分配列表避免频繁内存分配
4. 边界处理：处理区间完全包含和部分重叠的情况

题目链接：http://poj.org/problem?id=2777

@author Algorithm Journey
@version 1.0
"""

import sys

class SegmentTree:
    """
    线段树类
    用于维护区间染色和颜色计数信息
    """
    
    def __init__(self, n):
        """
        初始化线段树
        
        @param n: 板条长度
        """
        self.n = n
        self.color = [0] * (4 * n)  # 区间颜色集合(位运算表示)
        self.lazy = [0] * (4 * n)   # 懒惰标记
        self.build(1, 1, n)
    
    def count_bits(self, n):
        """
        计算一个整数二进制表示中1的个数
        用于计算颜色种类数
        
        @param n: 输入整数
        @return: 二进制表示中1的个数
        """
        count = 0
        while n > 0:
            count += n & 1
            n >>= 1
        return count
    
    def push_up(self, rt):
        """
        向上更新节点信息
        将左右子节点的颜色集合信息合并到父节点
        
        @param rt: 节点索引
        """
        self.color[rt] = self.color[2 * rt] | self.color[2 * rt + 1]
    
    def push_down(self, rt):
        """
        向下传递懒惰标记
        在访问子节点前，将当前节点的懒惰标记传递给子节点
        
        @param rt: 节点索引
        """
        if self.lazy[rt] != 0:
            self.lazy[2 * rt] = self.lazy[rt]
            self.lazy[2 * rt + 1] = self.lazy[rt]
            self.color[2 * rt] = self.lazy[rt]
            self.color[2 * rt + 1] = self.lazy[rt]
            self.lazy[rt] = 0
    
    def build(self, rt, l, r):
        """
        建立线段树
        初始化线段树，所有位置初始颜色为1
        
        @param rt: 节点索引
        @param l: 区间左端点
        @param r: 区间右端点
        """
        self.lazy[rt] = 0
        if l == r:
            self.color[rt] = 1  # 初始颜色为1，用二进制表示第0位为1
            return
        mid = (l + r) // 2
        self.build(2 * rt, l, mid)
        self.build(2 * rt + 1, mid + 1, r)
        self.push_up(rt)
    
    def update(self, L, R, c, l, r, rt):
        """
        区间染色操作
        将区间[L, R]染成颜色c
        
        @param L: 操作区间左端点
        @param R: 操作区间右端点
        @param c: 染色颜色
        @param l: 当前节点表示的区间左端点
        @param r: 当前节点表示的区间右端点
        @param rt: 当前节点索引
        """
        # 如果当前区间完全被操作区间包含，直接染色
        if L <= l and r <= R:
            self.color[rt] = 1 << (c - 1)  # 将第c位设为1
            self.lazy[rt] = 1 << (c - 1)
            return
        
        # 下推懒惰标记
        self.push_down(rt)
        
        mid = (l + r) // 2
        if L <= mid:
            self.update(L, R, c, l, mid, 2 * rt)
        if R > mid:
            self.update(L, R, c, mid + 1, r, 2 * rt + 1)
        
        # 向上更新节点信息
        self.push_up(rt)
    
    def query(self, L, R, l, r, rt):
        """
        查询区间颜色数
        查询区间[L, R]中有多少种不同的颜色
        
        @param L: 查询区间左端点
        @param R: 查询区间右端点
        @param l: 当前节点表示的区间左端点
        @param r: 当前节点表示的区间右端点
        @param rt: 当前节点索引
        @return: 区间颜色数
        """
        # 如果当前区间完全被查询区间包含，直接返回颜色数
        if L <= l and r <= R:
            return self.count_bits(self.color[rt])
        
        # 下推懒惰标记
        self.push_down(rt)
        
        mid = (l + r) // 2
        res = 0
        if L <= mid:
            res |= self.query(L, R, l, mid, 2 * rt)
        if R > mid:
            res |= self.query(L, R, mid + 1, r, 2 * rt + 1)
        
        return self.count_bits(res)

def main():
    """
    主函数
    处理输入输出，执行操作
    """
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    L = int(data[idx])      # 板条长度
    T = int(data[idx + 1])  # 颜色数
    O = int(data[idx + 2])  # 操作数
    idx += 3
    
    seg_tree = SegmentTree(L)
    
    results = []
    for _ in range(O):
        op = data[idx]
        idx += 1
        if op == 'C':
            A = int(data[idx])
            B = int(data[idx + 1])
            C = int(data[idx + 2])
            idx += 3
            # 确保A <= B
            if A > B:
                A, B = B, A
            seg_tree.update(A, B, C, 1, L, 1)
        else:  # op == 'P'
            A = int(data[idx])
            B = int(data[idx + 1])
            idx += 2
            # 确保A <= B
            if A > B:
                A, B = B, A
            results.append(str(seg_tree.query(A, B, 1, L, 1)))
    
    print('\n'.join(results))

if __name__ == "__main__":
    main()