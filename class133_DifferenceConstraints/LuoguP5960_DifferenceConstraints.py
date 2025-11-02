#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
洛谷 P5960 【模板】差分约束算法（Python版本）

题目链接：https://www.luogu.com.cn/problem/P5960

题目描述：
给定n个变量x1, x2, ..., xn和m个约束条件，每个约束条件形如：
x_a - x_b <= c
判断是否存在满足所有约束条件的解，如果存在则输出一组解，否则输出"NO"。

解题思路：
这是一个标准的差分约束系统模板题。差分约束系统可以通过图论中的最短路径算法来解决。
对于每个约束条件x_a - x_b <= c，我们可以将其转化为：
x_a <= x_b + c
这与最短路径中的三角不等式dist[v] <= dist[u] + w(u,v)非常相似。

因此，我们可以构建一个有向图：
1. 每个变量xi对应图中的一个节点
2. 对于每个约束条件x_a - x_b <= c，从节点b向节点a连一条权值为c的有向边
3. 添加一个超级源点0，向所有节点连权值为0的边，确保图的连通性
4. 使用SPFA算法求从超级源点到各点的最短路径
5. 如果存在负环，则无解；否则最短路径就是一组可行解

算法实现细节：
- 使用链式前向星存储图结构，提高内存访问效率
- 使用SPFA算法求最短路径，检测负环
- dist数组初始化为INF表示无穷大距离
- count数组记录每个节点入队次数，用于检测负环
- inQueue数组标记节点是否在队列中，避免重复入队

时间复杂度：O(n * m)，其中n是变量数量，m是约束条件数
空间复杂度：O(n + m)

相关题目：
1. 洛谷 P5960 【模板】差分约束算法 - 本题
2. POJ 1201 Intervals - 区间选点问题
3. POJ 2983 Is the Information Reliable? - 信息可靠性判断
4. POJ 3169 Layout - 奶牛排队布局问题
5. POJ 1364 King - 国王序列约束问题
6. 洛谷 P1993 小K的农场 - 农场约束问题
7. 洛谷 P1250 种树 - 区间种树问题
8. 洛谷 P2294 [HNOI2005]狡猾的商人 - 商人账本合理性判断
9. 洛谷 P4926 [1007]倍杀测量者 - 倍杀测量问题
10. 洛谷 P3275 [SCOI2011]糖果 - 分糖果问题
11. LibreOJ #10087 「一本通3.4 例1」Intervals
12. LibreOJ #10088 「一本通3.4 例2」出纳员问题
13. AtCoder ABC216G 01Sequence
14. Codeforces 1473E - Minimum Path

工程化考虑：
1. 异常处理：输入校验、图构建检查、算法执行检测
2. 性能优化：链式前向星存储图、静态数组、队列预分配
3. 可维护性：函数职责单一、变量命名清晰、详细注释
4. 可扩展性：支持更多约束类型、添加输出信息
5. 边界情况：空输入、极端值、重复约束
6. 测试用例：基本功能、边界值、异常情况、性能测试
"""

import sys
from collections import deque

class Graph:
    """图类，使用链式前向星存储"""
    
    def __init__(self, max_nodes, max_edges):
        self.max_nodes = max_nodes
        self.max_edges = max_edges
        self.head = [0] * (max_nodes + 1)
        self.next = [0] * (max_edges + 1)
        self.to = [0] * (max_edges + 1)
        self.weight = [0] * (max_edges + 1)
        self.cnt = 1
    
    def add_edge(self, u, v, w):
        """添加边到图中"""
        self.next[self.cnt] = self.head[u]
        self.to[self.cnt] = v
        self.weight[self.cnt] = w
        self.head[u] = self.cnt
        self.cnt += 1

class SPFA:
    """SPFA算法实现"""
    
    def __init__(self, graph, max_nodes):
        self.graph = graph
        self.max_nodes = max_nodes
        self.INF = 10**9
    
    def has_negative_cycle(self, start, n):
        """判断是否存在负环"""
        dist = [self.INF] * (n + 1)
        in_queue = [False] * (n + 1)
        count = [0] * (n + 1)
        
        queue = deque()
        dist[start] = 0
        in_queue[start] = True
        queue.append(start)
        count[start] = 1
        
        while queue:
            u = queue.popleft()
            in_queue[u] = False
            
            i = self.graph.head[u]
            while i > 0:
                v = self.graph.to[i]
                w = self.graph.weight[i]
                
                if dist[v] > dist[u] + w:
                    dist[v] = dist[u] + w
                    
                    if not in_queue[v]:
                        queue.append(v)
                        in_queue[v] = True
                        count[v] += 1
                        
                        if count[v] > n:
                            return True  # 存在负环
                
                i = self.graph.next[i]
        
        return False, dist  # 无负环，返回距离数组

def main():
    """主函数"""
    data = sys.stdin.read().strip().split()
    if not data:
        return
    
    n = int(data[0])
    m = int(data[1])
    idx = 2
    
    # 初始化图
    max_nodes = n + 1
    max_edges = m + n + 10
    graph = Graph(max_nodes, max_edges)
    
    # 读入约束条件
    for i in range(m):
        if idx + 2 >= len(data):
            break
            
        a = int(data[idx])
        b = int(data[idx + 1])
        c = int(data[idx + 2])
        idx += 3
        
        # 约束条件：x_a - x_b <= c
        # 转化为：从节点b向节点a连权值为c的边
        graph.add_edge(b, a, c)
    
    # 添加超级源点，向所有点连权值为0的边
    super_source = 0
    for i in range(1, n + 1):
        graph.add_edge(super_source, i, 0)
    
    # 判断是否存在负环
    spfa = SPFA(graph, max_nodes)
    has_cycle, dist = spfa.has_negative_cycle(super_source, n + 1)
    
    if has_cycle:
        print("NO")
    else:
        # 输出一组解
        result = []
        for i in range(1, n + 1):
            result.append(str(dist[i]))
        print(" ".join(result))

if __name__ == "__main__":
    main()