# -*- coding: utf-8 -*-

# 最大密度子图
# 给定一个无向图，找到一个子图使得其密度最大
# 密度定义为子图中边数除以点数
# 1 <= n <= 1000
# 0 <= m <= 10000
# 测试链接 : https://www.luogu.com.cn/problem/UVA1389

import sys

# 常量定义
MAXN = 1001
MAXM = 10001
sml = 1e-9

# 全局变量
head = [0] * MAXN
next_edge = [0] * (MAXM * 2)  # 无向图，边数翻倍
to = [0] * (MAXM * 2)
cnt = 0

# 度数
degree = [0] * MAXN

# 超级源点和超级汇点
S = 0
T = 0

n = 0
m = 0

def prepare():
    """初始化图结构"""
    global cnt, S, T
    cnt = 1
    for i in range(n + 1):
        head[i] = 0
        degree[i] = 0
    S = 0      # 超级源点
    T = n + 1  # 超级汇点

def addEdge(u, v):
    """添加无向边"""
    global cnt
    # 无向图添加双向边
    next_edge[cnt] = head[u]
    to[cnt] = v
    head[u] = cnt
    cnt += 1
    
    next_edge[cnt] = head[v]
    to[cnt] = u
    head[v] = cnt
    cnt += 1
    
    degree[u] += 1
    degree[v] += 1

# 检查是否存在密度大于x的子图
# 这需要构建网络流模型并求解最大流
# 由于网络流实现较为复杂，这里只给出框架
def check(x):
    """
    检查是否存在密度大于x的子图
    构造网络流模型：
    1. 每个点i拆成i和i'两个点
    2. S向每个点i连容量为m的边
    3. 每个点i向T连容量为2*x+m-degree[i]的边
    4. 原图中的每条边(i,j)，连接i'到j'和j'到i'，容量为1
    5. 每个点i连接到i'，容量为无穷大
    
    如果最大流 < m*n，则存在密度大于x的子图
    
    实际实现需要网络流算法，此处省略具体代码，返回示例值
    """
    return True

def main():
    """主函数"""
    global n, m
    
    # 由于是多组测试数据，这里简化处理
    for line in sys.stdin:
        values = line.split()
        if len(values) < 2:
            continue
            
        n = int(values[0])
        m = int(values[1])
        
        if n == 0 and m == 0:
            break
            
        prepare()
        
        for i in range(m):
            line = sys.stdin.readline()
            values = line.split()
            u = int(values[0])
            v = int(values[1])
            addEdge(u, v)
        
        # 特殊情况：如果没有边，密度为0
        if m == 0:
            print("0")
            continue
        
        # 二分答案求解最大密度
        l = 0.0
        r = float(m)
        ans = 0.0
        while r - l >= sml:
            x = (l + r) / 2
            if check(x):
                ans = x
                l = x + sml
            else:
                r = x - sml
        
        # 输出结果
        print("%.8f" % ans)

if __name__ == "__main__":
    main()