#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
HackerRank - Dijkstra: Shortest Reach 2

题目链接: https://www.hackerrank.com/challenges/dijkstrashortreach/problem

题目描述:
给定一个无向图和起始节点，确定从起始节点到图中所有其他节点的最短路径长度。
如果某个节点无法从起始节点到达，则返回-1。

解题思路:
这是一个标准的单源最短路径问题，使用Dijkstra算法解决。

算法应用场景:
- 网络路由
- 社交网络中的影响力传播
- 交通导航系统

时间复杂度分析:
O((V + E) * log V)，其中V是节点数，E是边数

空间复杂度分析:
O(V + E)，用于存储图和距离数组
"""

import heapq
from collections import defaultdict

def shortestReach(n, edges, s):
    """
    使用Dijkstra算法计算从起始节点到所有其他节点的最短距离
    
    算法步骤:
    1. 构建图的邻接表表示
    2. 初始化距离数组，起始节点距离为0，其他节点为无穷大
    3. 使用优先队列维护待处理节点，按距离从小到大排序
    4. 不断取出距离最小的节点，更新其邻居节点的最短距离
    5. 返回所有节点的最短距离数组
    
    时间复杂度: O((V + E) * log V)
    空间复杂度: O(V + E)
    
    Args:
        n: int - 节点数
        edges: List[List[int]] - 边的列表，每个元素为 [from, to, weight]
        s: int - 起始节点
    
    Returns:
        List[int] - 从起始节点到所有节点的最短距离数组，无法到达的节点距离为-1
    """
    # 构建邻接表表示的图
    graph = defaultdict(list)
    for u, v, w in edges:
        graph[u].append((v, w))
        graph[v].append((u, w))
    
    # distance[i] 表示从源节点s到节点i的最短距离
    distance = [float('inf')] * (n + 1)
    distance[s] = 0
    
    # visited[i] 表示节点i是否已经确定了最短距离
    visited = [False] * (n + 1)
    
    # 优先队列，按距离从小到大排序
    heap = [(0, s)]
    
    # Dijkstra算法主循环
    while heap:
        # 取出距离源点最近的节点
        dist, u = heapq.heappop(heap)
        
        # 如果已经处理过，跳过
        if visited[u]:
            continue
        
        # 标记为已处理
        visited[u] = True
        
        # 遍历u的所有邻居节点
        for v, w in graph[u]:
            # 如果邻居节点未访问且通过u到达v的距离更短，则更新
            if not visited[v] and distance[u] + w < distance[v]:
                distance[v] = distance[u] + w
                heapq.heappush(heap, (distance[v], v))
    
    # 构造结果数组，无法到达的节点距离为-1，起始节点不包含在结果中
    result = []
    for i in range(1, n + 1):
        if i != s:
            result.append(-1 if distance[i] == float('inf') else distance[i])
    
    return result

# 测试方法
if __name__ == "__main__":
    # 示例测试用例
    n = 4
    edges = [[1, 2, 1], [1, 3, 3], [2, 3, 1], [3, 4, 2]]
    s = 1
    
    result = shortestReach(n, edges, s)
    print(f"从节点 {s} 到其他节点的最短距离为: {' '.join(map(str, result))}")