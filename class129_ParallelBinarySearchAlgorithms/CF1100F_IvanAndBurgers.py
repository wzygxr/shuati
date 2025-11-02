#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Codeforces 1100F Ivan and Burgers - Python实现
题目来源：https://codeforces.com/problemset/problem/1100/F
题目描述：区间最大异或和查询

问题描述：
给定一个长度为n的数组arr，有q条查询，每条查询格式为l r，要求在arr[l..r]中选若干个数，打印最大的异或和。

解题思路：
使用整体二分结合线性基处理区间最大异或和问题。线性基可以高效地处理最大异或和查询，
整体二分帮助我们将所有查询一起处理，优化时间复杂度。

时间复杂度：O((n+q) * log(n) * log(max_value))
空间复杂度：O(n * log(max_value))

注意：在Python中实现整体二分时，需要注意递归深度限制，对于大规模数据可能需要调整递归深度或转换为迭代实现。
"""

import sys
from sys import stdin

LOG = 20  # 2^20 > 1e6
MAXN = 100005

class LinearBasis:
    """线性基类，用于处理区间最大异或和问题"""
    def __init__(self):
        self.basis = [0] * LOG
        self.pos = [0] * LOG  # 记录每个基插入的位置
    
    def clear(self):
        """重置线性基"""
        for i in range(LOG):
            self.basis[i] = 0
            self.pos[i] = 0
    
    def insert(self, val, position):
        """插入元素到线性基"""
        for i in range(LOG-1, -1, -1):
            if (val >> i) == 0:
                continue
            if self.basis[i] == 0:
                self.basis[i] = val
                self.pos[i] = position
                break
            if position > self.pos[i]:
                # 交换当前元素和基中的元素
                self.basis[i], val = val, self.basis[i]
                self.pos[i], position = position, self.pos[i]
            val ^= self.basis[i]
    
    def query_max(self):
        """查询区间内的最大异或和"""
        res = 0
        for i in range(LOG-1, -1, -1):
            if (res ^ self.basis[i]) > res:
                res ^= self.basis[i]
        return res

# 全局变量初始化
n = 0
q = 0
arr = [0] * MAXN
queryL = [0] * MAXN
queryR = [0] * MAXN
queryId = [0] * MAXN
ans = [0] * MAXN
eid = [0] * MAXN
lset = [0] * MAXN
rset = [0] * MAXN
lb = LinearBasis()

def solve(ql, qr, l, r):
    """
    整体二分核心函数
    
    参数:
        ql: 查询范围的左端点
        qr: 查询范围的右端点
        l: 数组左边界
        r: 数组右边界
    """
    if ql > qr:
        return
    
    if l == r:
        # 所有查询的答案都是arr[l]（如果区间包含l）
        for i in range(ql, qr + 1):
            id = eid[i]
            if queryL[id] <= l and l <= queryR[id]:
                if arr[l] > ans[queryId[id]]:
                    ans[queryId[id]] = arr[l]
        return
    
    mid = (l + r) // 2
    
    # 处理左半部分元素（先插入再查询）
    lb.clear()
    lsiz = 0
    rsiz = 0
    
    # 记录当前的查询结果
    temp_ans = [0] * (qr - ql + 1)
    
    # 第一次扫描：插入左半部分元素并处理查询
    for i in range(l, mid + 1):
        lb.insert(arr[i], i)
    
    # 检查哪些查询可以在左半部分得到答案
    for i in range(ql, qr + 1):
        id = eid[i]
        if queryR[id] <= mid:
            # 整个查询区间在左半部分
            lsiz += 1
            lset[lsiz] = id
        elif queryL[id] > mid:
            # 整个查询区间在右半部分
            rsiz += 1
            rset[rsiz] = id
        else:
            # 查询区间跨越mid，需要分两次处理
            # 记录当前线性基的最大异或和（左半部分的贡献）
            temp_ans[i - ql] = lb.query_max()
            rsiz += 1
            rset[rsiz] = id
    
    # 重新排列查询顺序
    idx = ql
    for i in range(1, lsiz + 1):
        eid[idx] = lset[i]
        idx += 1
    for i in range(1, rsiz + 1):
        eid[idx] = rset[i]
        idx += 1
    
    # 递归处理左半部分
    solve(ql, ql + lsiz - 1, l, mid)
    
    # 处理右半部分
    lb.clear()
    
    # 从mid+1开始插入元素
    for i in range(mid + 1, r + 1):
        lb.insert(arr[i], i)
    
    # 第二次扫描：处理跨越mid的查询的右半部分
    pos = ql + lsiz
    for i in range(ql, qr + 1):
        id = eid[i]
        if queryL[id] <= mid and queryR[id] > mid:
            # 跨越mid的查询，需要合并左右两部分的贡献
            # 重新计算左半部分的线性基
            left_lb = LinearBasis()
            left_lb.clear()
            for j in range(l, mid + 1):
                left_lb.insert(arr[j], j)
            # 合并左右两部分的线性基
            merge_lb = LinearBasis()
            merge_lb.clear()
            for j in range(LOG):
                if left_lb.basis[j] != 0:
                    merge_lb.insert(left_lb.basis[j], left_lb.pos[j])
                if lb.basis[j] != 0:
                    merge_lb.insert(lb.basis[j], lb.pos[j])
            ans[queryId[id]] = merge_lb.query_max()
    
    # 递归处理右半部分
    solve(pos, qr, mid + 1, r)

def main():
    """主函数，处理输入输出并调用求解函数"""
    # 使用快速输入方法，优化处理大数据量
    input = sys.stdin.read().split()
    ptr = 0
    
    # 读取数组长度
    global n
    n = int(input[ptr])
    ptr += 1
    
    # 读取数组元素
    global arr
    for i in range(1, n + 1):
        arr[i] = int(input[ptr])
        ptr += 1
    
    # 读取查询数量
    global q
    q = int(input[ptr])
    ptr += 1
    
    # 读取查询
    global queryL, queryR, queryId, eid, ans
    for i in range(1, q + 1):
        queryL[i] = int(input[ptr])
        ptr += 1
        queryR[i] = int(input[ptr])
        ptr += 1
        queryId[i] = i
        eid[i] = i
    
    # 初始化答案数组
    ans = [0] * (q + 1)
    
    # 整体二分求解
    solve(1, q, 1, n)
    
    # 输出结果
    output = []
    for i in range(1, q + 1):
        output.append(str(ans[i]))
    print('\n'.join(output))

if __name__ == "__main__":
    # 设置递归深度，防止大规模数据导致栈溢出
    sys.setrecursionlimit(1 << 25)
    main()