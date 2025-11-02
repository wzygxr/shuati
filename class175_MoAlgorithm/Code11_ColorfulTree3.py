# -*- coding: utf-8 -*-

"""
树上莫队应用：树上路径不同颜色数
给定一棵n个节点的树，每个节点有一个颜色
有m次查询，每次查询两点间路径上不同颜色的数目
1 <= n, m <= 100000
1 <= color[i] <= 100000
测试链接 : https://vjudge.net/problem/HDU-5678

树上莫队的经典应用
核心思想：
1. 使用欧拉序将树上问题转化为序列问题
2. 利用莫队算法处理转化后的序列问题
3. 通过特定的处理方式，解决树上路径查询问题
"""

import sys
import math

# 常量定义
MAXN = 100001
MAXM = 100001
MAXP = 20

# 全局变量
n, m = 0, 0
# 颜色数组
color = [0] * MAXN
# 查询: l, r, lca, id
queries = [[0, 0, 0, 0] for _ in range(MAXM)]

# 链式前向星存树
head = [0] * MAXN
to = [0] * (MAXN << 1)
next_edge = [0] * (MAXN << 1)
edgeCount = 0

# 树上信息
depth = [0] * MAXN
euler = [0] * (MAXN << 1)  # 欧拉序
first = [0] * MAXN         # 第一次出现位置
last = [0] * MAXN          # 最后一次出现位置
jump = [[0] * MAXP for _ in range(MAXN)]  # 倍增表
eulerLen = 0               # 欧拉序长度

# 分块相关
belong = [0] * (MAXN << 1)

# 窗口信息
visited = [False] * MAXN   # 节点是否在窗口中
count = [0] * MAXN         # 每种颜色的出现次数
colorTypes = 0             # 不同颜色的种类数
answers = [0] * MAXM


# 添加边
def addEdge(u, v):
    global edgeCount
    edgeCount += 1
    next_edge[edgeCount] = head[u]
    to[edgeCount] = v
    head[u] = edgeCount


# DFS生成欧拉序和预处理LCA信息
def dfs(u, parent):
    global eulerLen
    depth[u] = depth[parent] + 1
    eulerLen += 1
    euler[eulerLen] = u
    first[u] = eulerLen
    jump[u][0] = parent

    # 填充倍增表
    for i in range(1, MAXP):
        jump[u][i] = jump[jump[u][i - 1]][i - 1]

    # 遍历子节点
    e = head[u]
    while e != 0:
        v = to[e]
        if v != parent:
            dfs(v, u)
        e = next_edge[e]

    eulerLen += 1
    euler[eulerLen] = u
    last[u] = eulerLen


# 倍增法求LCA
def lca(a, b):
    if depth[a] < depth[b]:
        a, b = b, a

    # 让a和b在同一深度
    for i in range(MAXP - 1, -1, -1):
        if depth[jump[a][i]] >= depth[b]:
            a = jump[a][i]

    if a == b:
        return a

    # 一起向上跳
    for i in range(MAXP - 1, -1, -1):
        if jump[a][i] != jump[b][i]:
            a = jump[a][i]
            b = jump[b][i]

    return jump[a][0]


# 普通莫队排序规则
def QueryComparator(a, b):
    if belong[a[0]] != belong[b[0]]:
        return belong[a[0]] - belong[b[0]]
    return a[1] - b[1]


# 翻转节点状态
def toggle(node):
    global colorTypes
    c = color[node]
    if visited[node]:
        # 节点在窗口中，移除它
        count[c] -= 1
        if count[c] == 0:
            colorTypes -= 1
    else:
        # 节点不在窗口中，添加它
        count[c] += 1
        if count[c] == 1:
            colorTypes += 1
    visited[node] = not visited[node]


# 主计算函数
def compute():
    global colorTypes
    l, r = 1, 0

    for i in range(1, m + 1):
        ql = queries[i][0]
        qr = queries[i][1]
        lcaNode = queries[i][2]
        id = queries[i][3]

        # 调整窗口边界
        while l > ql:
            l -= 1
            toggle(euler[l])
        while r < qr:
            r += 1
            toggle(euler[r])
        while l < ql:
            toggle(euler[l])
            l += 1
        while r > qr:
            toggle(euler[r])
            r -= 1

        # 特殊处理LCA
        if lcaNode != 0:
            toggle(lcaNode)

        answers[id] = colorTypes

        # 恢复LCA状态
        if lcaNode != 0:
            toggle(lcaNode)


# 预处理函数
def prepare():
    # 对欧拉序分块
    blockSize = int(math.sqrt(eulerLen))
    for i in range(1, eulerLen + 1):
        belong[i] = (i - 1) // blockSize + 1

    # 对查询进行排序
    queries[1:m+1] = sorted(queries[1:m+1], key=lambda x: (belong[x[0]], x[1]))


def main():
    global n, m, edgeCount
    # 读取输入
    try:
        line = sys.stdin.readline().split()
        n, m = int(line[0]), int(line[1])
    except:
        return

    # 初始化
    edgeCount = 0
    for i in range(MAXN):
        head[i] = 0
        visited[i] = False
        count[i] = 0
    eulerLen = 0
    colorTypes = 0

    # 读取颜色
    colors = list(map(int, sys.stdin.readline().split()))
    for i in range(1, n + 1):
        color[i] = colors[i - 1]

    # 读取边
    for i in range(1, n):
        line = sys.stdin.readline().split()
        u, v = int(line[0]), int(line[1])
        addEdge(u, v)
        addEdge(v, u)

    # 生成欧拉序
    dfs(1, 0)

    # 处理查询
    for i in range(1, m + 1):
        line = sys.stdin.readline().split()
        u, v = int(line[0]), int(line[1])

        # 确保first[u] <= first[v]
        if first[u] > first[v]:
            u, v = v, u

        lcaNode = lca(u, v)

        if u == lcaNode:
            # u是LCA
            queries[i][0] = first[u]
            queries[i][1] = first[v]
            queries[i][2] = 0  # 不需要特殊处理LCA
        else:
            # u不是LCA
            queries[i][0] = last[u]
            queries[i][1] = first[v]
            queries[i][2] = lcaNode
        queries[i][3] = i

    prepare()
    compute()

    # 输出结果
    for i in range(1, m + 1):
        print(answers[i])


if __name__ == "__main__":
    while True:
        try:
            main()
        except:
            break