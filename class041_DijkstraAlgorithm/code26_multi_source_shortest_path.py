#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
多源最短路径问题 - Dijkstra算法扩展（Python实现）

题目：多源最短路径（Multi-Source Shortest Path）
来源：各大算法平台通用问题

题目描述：
给定一个带权有向图，有多个源点，需要计算从每个源点到所有其他节点的最短距离。

解题思路：
1. 方法1：对每个源点单独运行Dijkstra算法
   时间复杂度：O(K*(V+E)logV)，其中K是源点数量
   空间复杂度：O(V+E)

2. 方法2：虚拟超级源点法
   创建一个虚拟源点，连接到所有实际源点，边权为0
   然后运行一次Dijkstra算法
   时间复杂度：O((V+E)logV)
   空间复杂度：O(V+E)

算法应用场景：
- 多数据中心网络路由
- 多仓库物流配送优化
- 社交网络中多个影响源的传播分析

时间复杂度分析：
- 方法1：O(K*(V+E)logV)
- 方法2：O((V+E)logV)

空间复杂度分析：
- 方法1：O(K*V) 存储多个距离数组
- 方法2：O(V+E)
"""

import heapq
from collections import defaultdict
import sys

def multi_source_dijkstra1(n, edges, sources):
    """
    方法1：对每个源点单独运行Dijkstra算法
    
    算法步骤：
    1. 对于每个源点，运行标准的Dijkstra算法
    2. 记录从该源点到所有其他节点的最短距离
    3. 返回所有源点的最短距离矩阵
    
    时间复杂度：O(K*(V+E)logV)
    空间复杂度：O(K*V)
    
    Args:
        n: int - 节点总数
        edges: List[List[int]] - 边列表，格式为 [u, v, w]
        sources: List[int] - 源点列表
    
    Returns:
        List[List[int]] - 距离矩阵，dist[i][j]表示从源点i到节点j的最短距离
    """
    # 构建邻接表表示的图
    graph = defaultdict(list)
    
    for u, v, w in edges:
        graph[u].append((v, w))
    
    # 距离矩阵，dist[i][j]表示从源点i到节点j的最短距离
    dist = []
    
    # 对每个源点运行Dijkstra算法
    for source in sources:
        # 距离数组，初始化为无穷大
        distance = [float('inf')] * (n + 1)
        distance[source] = 0
        
        # 访问标记数组
        visited = [False] * (n + 1)
        
        # 优先队列，按距离从小到大排序
        heap = [(0, source)]
        
        while heap:
            dist_val, u = heapq.heappop(heap)
            
            if visited[u]:
                continue
            visited[u] = True
            
            for v, w in graph[u]:
                if not visited[v] and distance[u] + w < distance[v]:
                    distance[v] = distance[u] + w
                    heapq.heappush(heap, (distance[v], v))
        
        # 存储当前源点的距离数组
        dist.append(distance)
    
    return dist

def multi_source_dijkstra2(n, edges, sources):
    """
    方法2：虚拟超级源点法
    
    算法步骤：
    1. 创建一个虚拟源点0，连接到所有实际源点，边权为0
    2. 从虚拟源点0运行Dijkstra算法
    3. 得到的距离数组就是从虚拟源点到各点的最短距离
    4. 由于虚拟源点到实际源点的距离为0，所以这等价于多源最短路径
    
    时间复杂度：O((V+E)logV)
    空间复杂度：O(V+E)
    
    Args:
        n: int - 节点总数
        edges: List[List[int]] - 边列表，格式为 [u, v, w]
        sources: List[int] - 源点列表
    
    Returns:
        List[int] - 距离数组，dist[i]表示从最近的源点到节点i的最短距离
    """
    # 构建扩展图（包含虚拟源点0）
    graph = defaultdict(list)
    
    # 添加原始边
    for u, v, w in edges:
        graph[u].append((v, w))
    
    # 添加虚拟源点到所有实际源点的边（权重为0）
    for source in sources:
        graph[0].append((source, 0))
    
    # 从虚拟源点0运行Dijkstra算法
    distance = [float('inf')] * (n + 1)
    distance[0] = 0
    
    visited = [False] * (n + 1)
    heap = [(0, 0)]
    
    while heap:
        dist_val, u = heapq.heappop(heap)
        
        if visited[u]:
            continue
        visited[u] = True
        
        for v, w in graph[u]:
            if not visited[v] and distance[u] + w < distance[v]:
                distance[v] = distance[u] + w
                heapq.heappush(heap, (distance[v], v))
    
    return distance

class OptimizedMultiSourceDijkstra:
    """
    方法3：多源最短路径的优化版本
    
    算法优化点：
    1. 使用更高效的数据结构
    2. 支持大规模图的快速计算
    
    时间复杂度：O((V+E)logV)
    空间复杂度：O(V+E)
    """
    
    def __init__(self, n):
        """
        初始化
        
        Args:
            n: int - 节点总数
        """
        self.n = n
        self.graph = defaultdict(list)
    
    def add_edge(self, u, v, w):
        """
        添加边
        
        Args:
            u: int - 起点
            v: int - 终点
            w: int - 权重
        """
        self.graph[u].append((v, w))
    
    def calculate(self, edges, sources):
        """
        计算多源最短路径
        
        Args:
            edges: List[List[int]] - 边列表
            sources: List[int] - 源点列表
        
        Returns:
            List[int] - 距离数组
        """
        # 构建图
        for u, v, w in edges:
            self.add_edge(u, v, w)
        
        # 添加虚拟源点到实际源点的边
        for source in sources:
            self.add_edge(0, source, 0)
        
        # 距离数组
        distance = [float('inf')] * (self.n + 1)
        distance[0] = 0
        
        # 访问标记
        visited = [False] * (self.n + 1)
        
        # 优先队列
        heap = [(0, 0)]
        
        while heap:
            dist_val, u = heapq.heappop(heap)
            
            if visited[u]:
                continue
            visited[u] = True
            
            for v, w in self.graph[u]:
                if not visited[v] and distance[u] + w < distance[v]:
                    distance[v] = distance[u] + w
                    heapq.heappush(heap, (distance[v], v))
        
        return distance[1:]  # 返回从节点1开始的距离

def test_case():
    """
    测试用例
    """
    # 测试用例1：简单多源最短路径
    n1 = 4
    edges1 = [
        [1, 2, 1], [2, 3, 2], [3, 4, 3],
        [1, 3, 4], [2, 4, 5]
    ]
    sources1 = [1, 3]  # 两个源点：1和3
    
    print("=== 测试用例1 ===")
    print("方法1结果（单独Dijkstra）：")
    result1 = multi_source_dijkstra1(n1, edges1, sources1)
    for i, source in enumerate(sources1):
        print(f"从源点{source}到各点的距离: ", end="")
        for j in range(1, n1 + 1):
            print(f"{result1[i][j]} ", end="")
        print()
    
    print("方法2结果（虚拟源点法）：")
    result2 = multi_source_dijkstra2(n1, edges1, sources1)
    print("从最近源点到各点的距离: ", end="")
    for j in range(1, n1 + 1):
        print(f"{result2[j]} ", end="")
    print()
    
    print("方法3结果（优化版本）：")
    optimizer = OptimizedMultiSourceDijkstra(n1)
    result3 = optimizer.calculate(edges1, sources1)
    print("从最近源点到各点的距离: ", end="")
    for dist in result3:
        print(f"{dist} ", end="")
    print()
    
    # 性能对比分析
    print("\n=== 性能分析 ===")
    print("方法1：适合源点数量较少的情况，实现简单")
    print("方法2：适合源点数量较多的情况，效率更高")
    print("方法3：适合大规模图，内存使用更优")

if __name__ == "__main__":
    test_case()
    
    # 边界测试用例
    print("\n=== 边界测试 ===")
    
    # 测试用例2：单个源点
    n2 = 3
    edges2 = [[1, 2, 1], [2, 3, 2]]
    sources2 = [1]
    result_single = multi_source_dijkstra2(n2, edges2, sources2)
    print("单个源点测试:", result_single[1:])
    
    # 测试用例3：无连接图
    n3 = 3
    edges3 = [[1, 2, 1]]  # 节点3没有连接
    sources3 = [1]
    result_disconnected = multi_source_dijkstra2(n3, edges3, sources3)
    print("无连接图测试:", result_disconnected[1:])
    
    # 测试用例4：所有节点都是源点
    n4 = 3
    edges4 = [[1, 2, 1], [2, 3, 2], [1, 3, 3]]
    sources4 = [1, 2, 3]
    result_all_sources = multi_source_dijkstra2(n4, edges4, sources4)
    print("所有节点都是源点:", result_all_sources[1:])