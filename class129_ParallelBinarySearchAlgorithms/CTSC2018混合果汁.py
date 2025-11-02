#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
洛谷P4602 [CTSC2018]混合果汁 - Python实现
题目来源：https://www.luogu.com.cn/problem/P4602
题目描述：线段树与整体二分结合的优化问题

问题描述：
有n种果汁，每种果汁有美味度d，单价p，数量l。现在有m个询问，每个询问给出需要的总数量g和最高预算v，
要求选一些果汁，使得总数量至少g，总费用不超过v，并且所选果汁的最低美味度尽可能大。

解题思路：
使用整体二分法来二分可能的最低美味度d，对于每个d，使用线段树维护满足d' >= d的果汁，
并支持查询在预算v下最多能买多少果汁。

时间复杂度：O((n+m) * log(n) * log(max_p))
空间复杂度：O(n + m)

注意：在Python中处理大规模数据时，需要注意递归深度和输入效率的问题。
"""

import sys
import bisect
from sys import stdin

MAXN = 100005
MAXM = 100005
INF = 1 << 30

# 线段树结构
class SegmentTree:
    def __init__(self, size):
        self.size = size
        self.tree = [(0, 0)] * (4 * size)  # (sumL, sumCost)
    
    def update(self, o, l, r, pos, addL, addCost):
        sumL, sumCost = self.tree[o]
        new_sumL = sumL + addL
        new_sumCost = sumCost + addCost
        self.tree[o] = (new_sumL, new_sumCost)
        
        if l == r:
            return
        
        mid = (l + r) >> 1
        if pos <= mid:
            self.update(o << 1, l, mid, pos, addL, addCost)
        else:
            self.update(o << 1 | 1, mid + 1, r, pos, addL, addCost)
    
    def query(self, o, l, r, need, maxCost):
        if need <= 0:
            return 0
        sumL, sumCost = self.tree[o]
        if sumL == 0 or sumCost > maxCost:
            return 0
        if l == r:
            # 注意这里要转换回原始价格
            costPerUnit = disP[l - 1]  # 假设disP是全局的离散化价格数组
            maxAffordable = maxCost // costPerUnit
            return min(sumL, maxAffordable, need)
        
        mid = (l + r) >> 1
        leftSon = o << 1
        rightSon = o << 1 | 1
        left_sumL, left_sumCost = self.tree[leftSon]
        
        # 优先选择价格低的（左子树）
        if left_sumCost <= maxCost:
            # 左子树全部购买，再买右子树的
            return left_sumL + self.query(rightSon, mid + 1, r, need - left_sumL, maxCost - left_sumCost)
        else:
            # 只买左子树的一部分
            return self.query(leftSon, l, mid, need, maxCost)

# 全局变量
disD = []  # 离散化后的美味度数组
disP = []  # 离散化后的价格数组

# 离散化处理
def discrete(d_list, p_list):
    global disD, disP
    # 离散化美味度
    disD = sorted(list(set(d_list)))
    # 离散化价格
    disP = sorted(list(set(p_list)))
    # 转换为离散化后的值（从1开始）
    d_values = []
    p_values = []
    for d in d_list:
        d_values.append(bisect.bisect_left(disD, d) + 1)
    for p in p_list:
        p_values.append(bisect.bisect_left(disP, p) + 1)
    return d_values, p_values

# 整体二分核心函数
def solve(ql, qr, l, r, d_list, p_list, l_list, g_list, v_list, qid, ans):
    if ql > qr or l > r:
        return
    
    if l == r:
        # 所有查询的答案都是disD[l-1]
        for i in range(ql, qr + 1):
            ans[qid[i]] = disD[l - 1]
        return
    
    mid = (l + r + 1) >> 1
    
    # 创建线段树
    st = SegmentTree(len(disP))
    
    # 将所有美味度>=mid的果汁加入线段树
    for i in range(len(d_list)):
        if d_list[i] >= mid:
            pos = p_list[i]
            cost = disP[pos - 1] * l_list[i]
            st.update(1, 1, len(disP), pos, l_list[i], cost)
    
    # 记录哪些查询可以满足
    left = []
    right = []
    
    for i in range(ql, qr + 1):
        idx = qid[i]
        maxBuy = st.query(1, 1, len(disP), g_list[idx], v_list[idx])
        if maxBuy >= g_list[idx]:
            # 可以满足，答案可能更大
            left.append(idx)
        else:
            # 无法满足，答案必须更小
            right.append(idx)
    
    # 合并查询顺序
    new_qid = qid.copy()
    ptr = ql
    for x in left:
        new_qid[ptr] = x
        ptr += 1
    for x in right:
        new_qid[ptr] = x
        ptr += 1
    
    # 递归处理左右两部分
    solve(ql, ql + len(left) - 1, mid, r, d_list, p_list, l_list, g_list, v_list, new_qid, ans)
    solve(ql + len(left), qr, l, mid - 1, d_list, p_list, l_list, g_list, v_list, new_qid, ans)

def main():
    # 使用快速输入方法
    input = sys.stdin.read().split()
    ptr = 0
    
    n = int(input[ptr])
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    
    # 读取果汁信息
    d_list = []
    p_list = []
    l_list = []
    for _ in range(n):
        d = int(input[ptr])
        ptr += 1
        p = int(input[ptr])
        ptr += 1
        l = int(input[ptr])
        ptr += 1
        d_list.append(d)
        p_list.append(p)
        l_list.append(l)
    
    # 离散化
    d_values, p_values = discrete(d_list, p_list)
    
    # 读取查询
    g_list = [0] * (m + 1)  # 1-based
    v_list = [0] * (m + 1)
    qid = [0] * (m + 1)
    ans = [0] * (m + 1)
    
    for i in range(1, m + 1):
        g = int(input[ptr])
        ptr += 1
        v = int(input[ptr])
        ptr += 1
        g_list[i] = g
        v_list[i] = v
        qid[i] = i
    
    # 整体二分求解
    solve(1, m, 1, len(disD), d_values, p_values, l_list, g_list, v_list, qid, ans)
    
    # 输出结果
    output = []
    for i in range(1, m + 1):
        output.append(str(ans[i]))
    print('\n'.join(output))

if __name__ == "__main__":
    # 设置递归深度，防止Python默认递归深度限制导致错误
    sys.setrecursionlimit(1 << 25)
    main()