# -*- coding: utf-8 -*-
"""
SPOJ COT2 - Count on a tree II

题目来源: SPOJ COT2
题目链接: https://www.spoj.com/problems/COT2/

题目描述:
给你一棵有N个节点的树。树的节点编号从1到N。每个节点都有一个整数权重。
我们将要求你执行以下操作:
u v : 询问从u到v的路径上有多少个不同的整数表示节点的权重。

解题思路:
使用树上莫队算法解决树上路径不同元素个数问题。
1. 使用欧拉序将树上路径问题转化为序列问题
2. 对欧拉序上的区间进行莫队算法处理
3. 对于路径u到v的查询，根据u和v在欧拉序中的位置关系确定对应的区间

时间复杂度: O((n + m) * sqrt(n))
空间复杂度: O(n)

约束条件:
N, M <= 40000

示例:
输入:
8 4
1 2 3 4 5 6 7 8
1 2
2 3
2 4
3 5
3 6
4 7
4 8
1 8
3 5
2 7
5 8

输出:
6
3
4
6
"""

import sys
import math
input = sys.stdin.read

# 全局变量
MAXN = 40010
MAXM = 100010

# 树的存储
head = [-1] * MAXN
edge = [0] * (MAXN * 2)
next_edge = [0] * (MAXN * 2)
edge_cnt = 0

# 节点权重
weight = [0] * MAXN
sorted_weights = [0] * MAXN

# DFS相关
dfn = [0] * MAXN  # 欧拉序
dep = [0] * MAXN  # 深度
fa = [0] * MAXN   # 父亲节点
first = [0] * MAXN # 第一次出现位置
second = [0] * MAXN # 第二次出现位置
timestamp = 0

# LCA相关
dp = [[0] * 20 for _ in range(MAXN)]

# 莫队相关
block_size = 0
cnt = [0] * MAXN  # 权值计数
now_ans = 0  # 当前答案

# 离散化相关
values = [0] * MAXN
value_cnt = 0

class Query:
    def __init__(self, l, r, lca, id):
        self.l = l
        self.r = r
        self.lca = lca
        self.id = id

queries = []
ans = [0] * MAXM

def add_edge(u, v):
    """添加边"""
    global edge_cnt
    edge[edge_cnt] = v
    next_edge[edge_cnt] = head[u]
    head[u] = edge_cnt
    edge_cnt += 1

def dfs(u, father, depth):
    """DFS生成欧拉序"""
    global timestamp
    fa[u] = father
    dep[u] = depth
    first[u] = timestamp + 1
    timestamp += 1
    dfn[timestamp] = u
    
    # 倍增计算LCA
    dp[u][0] = father
    i = 1
    while (1 << i) <= dep[u]:
        dp[u][i] = dp[dp[u][i-1]][i-1]
        i += 1
    
    # 遍历子节点
    i = head[u]
    while i != -1:
        v = edge[i]
        if v != father:
            dfs(v, u, depth + 1)
        i = next_edge[i]
    
    second[u] = timestamp + 1
    timestamp += 1
    dfn[timestamp] = u

def lca(u, v):
    """计算LCA"""
    if dep[u] < dep[v]:
        u, v = v, u
    
    # 让u和v在同一深度
    for i in range(19, -1, -1):
        if dep[u] - (1 << i) >= dep[v]:
            u = dp[u][i]
    
    if u == v:
        return u
    
    # 同时向上跳
    for i in range(19, -1, -1):
        if dp[u][i] != dp[v][i]:
            u = dp[u][i]
            v = dp[v][i]
    
    return dp[u][0]

def discretize(n):
    """离散化权重值"""
    global value_cnt
    for i in range(1, n + 1):
        sorted_weights[i] = weight[i]
    
    sorted_weights[1:n+1] = sorted(sorted_weights[1:n+1])
    
    value_cnt = 1
    values[1] = sorted_weights[1]
    for i in range(2, n + 1):
        if sorted_weights[i] != sorted_weights[i-1]:
            value_cnt += 1
            values[value_cnt] = sorted_weights[i]

def binary_search(target):
    """二分查找离散化后的索引"""
    left, right = 1, value_cnt
    while left <= right:
        mid = (left + right) // 2
        if values[mid] == target:
            return mid
        elif values[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    return -1

def add(pos):
    """莫队添加元素"""
    global now_ans
    u = dfn[pos]
    val = binary_search(weight[u])
    cnt[val] += 1
    if cnt[val] == 1:
        now_ans += 1

def delete(pos):
    """莫队删除元素"""
    global now_ans
    u = dfn[pos]
    val = binary_search(weight[u])
    cnt[val] -= 1
    if cnt[val] == 0:
        now_ans -= 1

def main():
    global block_size, now_ans, timestamp, edge_cnt
    
    data = input().split()
    idx = 0
    
    n = int(data[idx])
    idx += 1
    m = int(data[idx])
    idx += 1
    
    # 读取节点权重
    for i in range(1, n + 1):
        weight[i] = int(data[idx])
        idx += 1
    
    # 离散化
    discretize(n)
    
    # 读取边
    for i in range(1, n):
        u = int(data[idx])
        idx += 1
        v = int(data[idx])
        idx += 1
        add_edge(u, v)
        add_edge(v, u)
    
    # DFS生成欧拉序
    dfs(1, 0, 1)
    
    # 设置块大小
    block_size = int(math.sqrt(timestamp))
    
    # 处理查询
    for i in range(1, m + 1):
        u = int(data[idx])
        idx += 1
        v = int(data[idx])
        idx += 1
        
        lca_node = lca(u, v)
        
        # 根据u和v在欧拉序中的位置确定查询区间
        if first[u] > first[v]:
            u, v = v, u
        
        if u == lca_node:
            queries.append(Query(first[u], first[v], 0, i))
        else:
            # 路径u->v经过lcaNode
            if first[u] > second[v]:
                queries.append(Query(second[v], first[u], lca_node, i))
            else:
                queries.append(Query(first[u], first[v], lca_node, i))
    
    # 莫队排序
    queries.sort(key=lambda q: (q.l // block_size, q.r))
    
    # 莫队处理
    l, r = 1, 0
    for q in queries:
        # 扩展右端点
        while r < q.r:
            r += 1
            add(r)
        # 收缩右端点
        while r > q.r:
            delete(r)
            r -= 1
        # 收缩左端点
        while l < q.l:
            delete(l)
            l += 1
        # 扩展左端点
        while l > q.l:
            l -= 1
            add(l)
        
        # 处理LCA
        if q.lca != 0:
            val = binary_search(weight[q.lca])
            cnt[val] += 1
            if cnt[val] == 1:
                now_ans += 1
        
        ans[q.id] = now_ans
        
        # 恢复LCA
        if q.lca != 0:
            val = binary_search(weight[q.lca])
            cnt[val] -= 1
            if cnt[val] == 0:
                now_ans -= 1
    
    # 输出答案
    for i in range(1, m + 1):
        print(ans[i])

if __name__ == "__main__":
    main()