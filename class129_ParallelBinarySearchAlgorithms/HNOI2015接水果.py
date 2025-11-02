#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
洛谷P3242 [HNOI2015]接水果 - Python实现
题目来源：https://www.luogu.com.cn/problem/P3242
题目描述：树上路径包含关系与扫描线结合的整体二分问题

问题描述：
给定一棵树，每个节点有一个权值。有两种操作：
1. 类型A：在节点u和v之间连接一条边，边权为w
2. 类型B：查询所有满足路径u-v被路径a-b包含的边的边权的第k小

解题思路：
1. 首先使用DFS序将树上路径转换为平面矩形区域
2. 将边和查询转换为矩形区域的覆盖和查询问题
3. 使用扫描线和树状数组结合整体二分求解

时间复杂度：O((P+Q) * log(P) * log(max_weight))
空间复杂度：O(P+Q)

注意：在Python中实现时需要注意递归深度限制，对于大规模数据可能需要调整递归深度或转换为迭代实现。
"""

import sys
from sys import stdin
from bisect import bisect_left

MAXN = 40005
MAXM = 80005
MAXQ = 100005
LOG = 20

# 树的结构
head = [0] * MAXN
next_ = [0] * MAXM
to_ = [0] * MAXM
cnt = 0

# DFS序
in_ = [0] * MAXN
out_ = [0] * MAXN
timeStamp = 0

# 父节点和深度（用于LCA）
dep = [0] * MAXN
f = [[0] * LOG for _ in range(MAXN)]

# 边的信息
u = [0] * MAXM
v = [0] * MAXM
w = [0] * MAXM
m = 0
q = 0

# 扫描线事件
class Event:
    def __init__(self, x=0, y1=0, y2=0, type_=0, id_=0):
        self.x = x
        self.y1 = y1
        self.y2 = y2
        self.type = type_
        self.id = id_
    
    def __lt__(self, other):
        return self.x < other.x

# 查询信息
class Query:
    def __init__(self, x1=0, x2=0, y1=0, y2=0, k=0, id_=0):
        self.x1 = x1
        self.x2 = x2
        self.y1 = y1
        self.y2 = y2
        self.k = k
        self.id = id_

queries = [Query() for _ in range(MAXQ)]
ans = [0] * MAXQ

# 树状数组
class FenwickTree:
    def __init__(self):
        self.tree = [0] * MAXN
    
    def update(self, x, val):
        while x < MAXN:
            self.tree[x] += val
            x += x & -x
    
    def query(self, x):
        res = 0
        while x > 0:
            res += self.tree[x]
            x -= x & -x
        return res
    
    def query_range(self, l, r):
        return self.query(r) - self.query(l - 1)

ft = FenwickTree()

# 初始化树的邻接表
def add_edge(u, v):
    global cnt
    cnt += 1
    next_[cnt] = head[u]
    head[u] = cnt
    to_[cnt] = v

# DFS计算入时间戳和出时间戳
def dfs(u, fa):
    global timeStamp
    timeStamp += 1
    in_[u] = timeStamp
    dep[u] = dep[fa] + 1
    f[u][0] = fa
    for i in range(1, LOG):
        f[u][i] = f[f[u][i-1]][i-1]
    i = head[u]
    while i > 0:
        v = to_[i]
        if v != fa:
            dfs(v, u)
        i = next_[i]
    out_[u] = timeStamp

# 求LCA
def lca(u, v):
    if dep[u] < dep[v]:
        u, v = v, u
    
    # 将u提升到v的深度
    for i in range(LOG-1, -1, -1):
        if dep[f[u][i]] >= dep[v]:
            u = f[u][i]
    
    if u == v:
        return u
    
    # 同时提升u和v直到LCA
    for i in range(LOG-1, -1, -1):
        if f[u][i] != f[v][i]:
            u = f[u][i]
            v = f[v][i]
    
    return f[u][0]

# 整体二分核心函数
def solve(ql, qr, l, r):
    if ql > qr or l > r:
        return
    
    if l == r:
        # 所有查询的答案都是l
        for i in range(ql, qr + 1):
            ans[queries[i].id] = l
        return
    
    mid = (l + r) // 2
    
    # 收集扫描线事件
    tmp_events = []
    for i in range(1, m + 1):
        if w[i] <= mid:
            # 处理边，将其转换为扫描线事件
            u_node, v_node = u[i], v[i]
            if dep[u_node] < dep[v_node]:
                u_node, v_node = v_node, u_node
            # 将边转换为矩形区域
            tmp_events.append(Event(1, in_[u_node], out_[u_node], 1, i))
            tmp_events.append(Event(in_[v_node], in_[u_node], out_[u_node], -1, i))
            tmp_events.append(Event(out_[v_node] + 1, in_[u_node], out_[u_node], 1, i))
    
    # 将查询也加入事件列表
    for i in range(ql, qr + 1):
        tmp_events.append(Event(queries[i].x1, queries[i].y1, queries[i].y2, -2, i))
        tmp_events.append(Event(queries[i].x2 + 1, queries[i].y1, queries[i].y2, -3, i))
    
    # 按x坐标排序事件
    tmp_events.sort()
    
    # 初始化答案计数
    cnt_array = [0] * (qr - ql + 1)
    
    # 处理扫描线
    event_ptr = 0
    for x in range(1, timeStamp + 1):
        # 处理所有x坐标等于当前x的事件
        while event_ptr < len(tmp_events) and tmp_events[event_ptr].x == x:
            e = tmp_events[event_ptr]
            event_ptr += 1
            if e.type == 1 or e.type == -1:
                # 矩形覆盖事件
                ft.update(e.y1, e.type)
                ft.update(e.y2 + 1, -e.type)
            elif e.type == -2:
                # 查询开始事件
                idx = e.id - ql
                cnt_array[idx] -= ft.query_range(e.y1, e.y2)
            elif e.type == -3:
                # 查询结束事件
                idx = e.id - ql
                cnt_array[idx] += ft.query_range(e.y1, e.y2)
    
    # 清理树状数组
    for e in tmp_events:
        if e.type == 1 or e.type == -1:
            ft.update(e.y1, -e.type)
            ft.update(e.y2 + 1, e.type)
    
    # 分类查询
    left = ql
    right = qr
    left_queries = [0] * (qr - ql + 1)
    right_queries = [0] * (qr - ql + 1)
    
    # 保存当前查询状态
    tmp_queries = [Query() for _ in range(qr - ql + 1)]
    for i in range(ql, qr + 1):
        tmp_queries[i - ql] = Query(queries[i].x1, queries[i].x2, queries[i].y1, 
                                   queries[i].y2, queries[i].k, queries[i].id)
    
    for i in range(ql, qr + 1):
        idx = i - ql
        if cnt_array[idx] >= tmp_queries[idx].k:
            # 答案在左半部分
            left_queries[left - ql] = i
            left += 1
        else:
            # 答案在右半部分，调整k值
            tmp_queries[idx].k -= cnt_array[idx]
            right_queries[right - qr] = i
            right -= 1
    
    # 合并查询顺序
    for i in range(ql, left):
        pos = left_queries[i - ql]
        queries[i] = Query(tmp_queries[pos - ql].x1, tmp_queries[pos - ql].x2, 
                          tmp_queries[pos - ql].y1, tmp_queries[pos - ql].y2, 
                          tmp_queries[pos - ql].k, tmp_queries[pos - ql].id)
    
    for i in range(qr, right, -1):
        pos = right_queries[i - qr]
        queries[i] = Query(tmp_queries[pos - ql].x1, tmp_queries[pos - ql].x2, 
                          tmp_queries[pos - ql].y1, tmp_queries[pos - ql].y2, 
                          tmp_queries[pos - ql].k, tmp_queries[pos - ql].id)
    
    # 递归处理左右两部分
    solve(ql, left - 1, l, mid)
    solve(right + 1, qr, mid + 1, r)

def main():
    # 使用快速输入方法，优化处理大数据量
    input = sys.stdin.read().split()
    ptr = 0
    
    # 读取输入
    n = int(input[ptr])
    ptr += 1
    global m, q
    m = int(input[ptr])
    ptr += 1
    q = int(input[ptr])
    ptr += 1
    
    # 初始化邻接表
    global cnt
    cnt = 0
    for i in range(MAXN):
        head[i] = 0
    for i in range(MAXM):
        next_[i] = 0
        to_[i] = 0
    
    # 读取树的边
    for i in range(1, n):
        u_node = int(input[ptr])
        ptr += 1
        v_node = int(input[ptr])
        ptr += 1
        add_edge(u_node, v_node)
        add_edge(v_node, u_node)
    
    # 计算DFS序和LCA所需信息
    global timeStamp
    timeStamp = 0
    dep[0] = 0
    for i in range(MAXN):
        for j in range(LOG):
            f[i][j] = 0
    dfs(1, 0)
    
    # 读取水果（边）的信息
    weights = [0] * (m + 1)
    for i in range(1, m + 1):
        u[i] = int(input[ptr])
        ptr += 1
        v[i] = int(input[ptr])
        ptr += 1
        w[i] = int(input[ptr])
        ptr += 1
        weights[i] = w[i]
    
    # 离散化边权
    sorted_weights = sorted(weights[1:m+1])
    unique_weights = [sorted_weights[0]]
    for i in range(1, len(sorted_weights)):
        if sorted_weights[i] != unique_weights[-1]:
            unique_weights.append(sorted_weights[i])
    unique_cnt = len(unique_weights)
    
    for i in range(1, m + 1):
        w[i] = bisect_left(unique_weights, w[i]) + 1  # 从1开始编号
    
    # 读取查询
    for i in range(1, q + 1):
        a = int(input[ptr])
        ptr += 1
        b = int(input[ptr])
        ptr += 1
        k = int(input[ptr])
        ptr += 1
        
        l_node = lca(a, b)
        if l_node == a:
            # 路径a-b是链状的，且a是LCA
            if dep[a] > dep[b]:
                a, b = b, a
            queries[i] = Query(in_[b], out_[b], in_[l_node] + 1, in_[a], k, i)
        elif l_node == b:
            # 路径a-b是链状的，且b是LCA
            if dep[a] < dep[b]:
                a, b = b, a
            queries[i] = Query(in_[a], out_[a], in_[l_node] + 1, in_[b], k, i)
        else:
            # 路径a-b经过LCA，分成两段
            if in_[a] > in_[b]:
                a, b = b, a
            queries[i] = Query(in_[a], out_[a], in_[b], out_[b], k, i)
    
    # 整体二分求解
    solve(1, q, 1, unique_cnt)
    
    # 输出结果
    output = []
    for i in range(1, q + 1):
        output.append(str(unique_weights[ans[i] - 1]))
    print('\n'.join(output))

if __name__ == "__main__":
    # 设置递归深度，防止大规模数据导致栈溢出
    sys.setrecursionlimit(1 << 25)
    main()