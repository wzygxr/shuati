# -*- coding: utf-8 -*-

# 观光奶牛 (Sightseeing Cows)
# 给定一个有向图，每个点有一个点权value[i]，每条边有一个边权weight[i]
# 找到一个环，使得环上点权和除以边权和最大
# 输出最大比率值，保留两位小数
# 1 <= n <= 1000
# 1 <= m <= 5000
# 1 <= value[i], weight[i] <= 1000
# 测试链接 : https://www.luogu.com.cn/problem/P2868
# 测试链接 : http://poj.org/problem?id=3621

import sys
from collections import deque

# 常量定义
MAXN = 1001
MAXM = 5001
NA = -1e9
sml = 1e-9

# 全局变量
head = [0] * MAXN
next_edge = [0] * MAXM
to = [0] * MAXM
weight = [0.0] * MAXM
cnt = 0

# 点权
value = [0] * MAXN

# dfs判断正环，每个点的累积点权
dist = [0.0] * MAXN

# dfs判断正环，每个点是否是递归路径上的点
path = [False] * MAXN

n = 0
m = 0

def prepare():
    """初始化链式前向星结构"""
    global cnt
    cnt = 1
    for i in range(n + 1):
        head[i] = 0

def addEdge(u, v, w):
    """添加边到链式前向星结构"""
    global cnt
    next_edge[cnt] = head[u]
    to[cnt] = v
    weight[cnt] = w
    head[u] = cnt
    cnt += 1

def dfs(u, x):
    """DFS判断是否存在正环"""
    if u == 0:
        # 认为0号点是超级源点，具有通往所有点的有向边
        for i in range(1, n + 1):
            if dfs(i, x):
                return True
    else:
        path[u] = True
        e = head[u]
        while e != 0:
            v = to[e]
            w = weight[e] - x  # 边权减去比率值
            # 只有v的累积点权变大，才会递归，非常强的剪枝
            # 如果递归路径回到v，并且此时是v的累积点权更大的情况，说明遇到了正环
            # 或者后续递归找到了正环，直接返回True
            if dist[v] < dist[u] + w:
                dist[v] = dist[u] + w
                if path[v] or dfs(v, x):
                    return True
            e = next_edge[e]
        path[u] = False
    return False

def check(x):
    """检查是否存在比率值为x的正环"""
    # 初始化距离和路径数组
    for i in range(1, n + 1):
        dist[i] = 0
        path[i] = False
    return dfs(0, x)

def main():
    """主函数"""
    global n, m
    
    # 读取输入
    line = sys.stdin.readline().split()
    n = int(line[0])
    m = int(line[1])
    
    prepare()
    
    # 读取点权
    line = sys.stdin.readline().split()
    for i in range(1, n + 1):
        value[i] = int(line[i - 1])
    
    # 读取边信息
    for i in range(1, m + 1):
        line = sys.stdin.readline().split()
        u = int(line[0])
        v = int(line[1])
        w = float(line[2])
        addEdge(u, v, w)
    
    # 二分答案
    l = 0.0
    r = 1000.0
    ans = 0.0
    
    while l < r and r - l >= sml:
        x = (l + r) / 2
        # 如果存在正环，说明可以找到更大的比率值，向右二分
        # 如果不存在正环，说明当前比率值过大，向左二分
        if check(x):
            ans = x
            l = x + sml
        else:
            r = x - sml
    
    print("%.2f" % ans)

if __name__ == "__main__":
    main()