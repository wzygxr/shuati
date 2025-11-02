# -*- coding: utf-8 -*-

# 最小密度路径
# 给定一个有向图，每条边有两个权值a[i]和b[i]
# 定义路径密度为路径上所有a[i]的和除以所有b[i]的和
# 求所有简单路径中密度最小的值
# 1 <= n <= 50
# 1 <= m <= 300
# 0 <= a[i], b[i] <= 100000
# b[i] > 0
# 测试链接 : https://www.luogu.com.cn/problem/P1730

import sys

# 常量定义
MAXN = 51
MAXM = 301
INF = 1e20
sml = 1e-9

# 全局变量
head = [0] * MAXN
next_edge = [0] * MAXM
to = [0] * MAXM
a = [0] * MAXM
b = [0] * MAXM
cnt = 0

# floyd数组，f[i][j][k]表示从i到j，只经过编号不超过k的点的最小密度
# sumA[i][j][k]表示从i到j，只经过编号不超过k的点的最小密度路径的a值和
# sumB[i][j][k]表示从i到j，只经过编号不超过k的点的最小密度路径的b值和
f = [[[INF for k in range(MAXN)] for j in range(MAXN)] for i in range(MAXN)]
sumA = [[[0.0 for k in range(MAXN)] for j in range(MAXN)] for i in range(MAXN)]
sumB = [[[0.0 for k in range(MAXN)] for j in range(MAXN)] for i in range(MAXN)]

n = 0
m = 0

def prepare():
    """初始化链式前向星结构和Floyd数组"""
    global cnt
    
    cnt = 1
    for i in range(n + 1):
        head[i] = 0
    
    # 初始化floyd数组
    for i in range(1, n + 1):
        for j in range(1, n + 1):
            for k in range(n + 1):
                f[i][j][k] = INF
                sumA[i][j][k] = 0
                sumB[i][j][k] = 0
    
    # 初始化直接相连的边
    for e in range(1, cnt):
        u = to[e ^ 1]  # 反向边
        v = to[e]
        if b[e] > 0:  # 避免除零错误
            f[u][v][0] = float(a[e]) / b[e]
            sumA[u][v][0] = a[e]
            sumB[u][v][0] = b[e]

def addEdge(u, v, aa, bb):
    """添加边到链式前向星结构"""
    global cnt
    next_edge[cnt] = head[u]
    to[cnt] = v
    a[cnt] = aa
    b[cnt] = bb
    head[u] = cnt
    cnt += 1
    
    # 添加反向边，便于查找
    next_edge[cnt] = head[v]
    to[cnt] = u
    a[cnt] = aa
    b[cnt] = bb
    head[v] = cnt
    cnt += 1

# Floyd变形，计算所有点对之间的最小密度路径
def floyd():
    """Floyd变形算法计算最小密度路径"""
    for k in range(1, n + 1):
        for i in range(1, n + 1):
            for j in range(1, n + 1):
                # 通过点k转移
                newSumA = sumA[i][k][k-1] + sumA[k][j][k-1]
                newSumB = sumB[i][k][k-1] + sumB[k][j][k-1]
                
                if newSumB > 0:  # 避免除零错误
                    newDensity = newSumA / newSumB
                    
                    if newDensity < f[i][j][k-1]:
                        f[i][j][k] = newDensity
                        sumA[i][j][k] = newSumA
                        sumB[i][j][k] = newSumB
                    else:
                        f[i][j][k] = f[i][j][k-1]
                        sumA[i][j][k] = sumA[i][j][k-1]
                        sumB[i][j][k] = sumB[i][j][k-1]

def main():
    """主函数"""
    global n, m
    
    # 读取输入
    line = sys.stdin.readline().split()
    n = int(line[0])
    m = int(line[1])
    
    for i in range(1, m + 1):
        line = sys.stdin.readline().split()
        u = int(line[0])
        v = int(line[1])
        aa = int(line[2])
        bb = int(line[3])
        addEdge(u, v, aa, bb)
    
    prepare()
    floyd()
    
    # 寻找最小密度
    ans = INF
    for i in range(1, n + 1):
        for j in range(1, n + 1):
            if f[i][j][n] < ans:
                ans = f[i][j][n]
    
    print("%.10f" % ans)

if __name__ == "__main__":
    main()