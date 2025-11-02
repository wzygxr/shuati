#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
最短路径计数问题（Python实现）

题目：最短路径条数统计
来源：洛谷 P1144 最短路计数
链接：https://www.luogu.com.cn/problem/P1144

题目描述：
给出一个N个顶点M条边的无向无权图，顶点编号为1∼N。
问从顶点1出发，到其他每个点的最短路有几条。

解题思路：
1. 使用Dijkstra算法计算最短距离
2. 同时维护一个计数数组，记录到达每个节点的最短路径条数
3. 当发现更短的路径时，更新距离和计数
4. 当发现相同长度的路径时，累加计数

算法应用场景：
- 网络路由中的路径多样性分析
- 社交网络中的最短关系链统计
- 交通网络中的最短路径选择

时间复杂度分析：
- O((V+E)logV)，其中V是节点数，E是边数

空间复杂度分析：
- O(V+E)，存储图和距离计数数组
"""

import heapq
from collections import defaultdict, deque

def shortest_path_count(n, edges, source):
    """
    计算最短路径条数（无权图版本）
    
    算法步骤：
    1. 初始化距离数组和计数数组
    2. 源点距离为0，计数为1
    3. 使用优先队列进行Dijkstra算法
    4. 对于每个邻居节点：
        - 如果发现更短路径：更新距离，重置计数
        - 如果发现相同长度路径：累加计数
    
    时间复杂度：O((V+E)logV)
    空间复杂度：O(V+E)
    
    Args:
        n: int - 节点总数
        edges: List[Tuple[int, int]] - 边列表（无向图）
        source: int - 源点
    
    Returns:
        List[int] - 计数数组，count[i]表示从源点到节点i的最短路径条数
    """
    # 构建邻接表（无向图）
    graph = defaultdict(list)
    for u, v in edges:
        graph[u].append(v)
        graph[v].append(u)
    
    # 距离数组
    dist = [float('inf')] * (n + 1)
    dist[source] = 0
    
    # 计数数组
    count = [0] * (n + 1)
    count[source] = 1
    
    # 访问标记数组
    visited = [False] * (n + 1)
    
    # 优先队列，按距离排序
    heap = [(0, source)]
    
    while heap:
        d, u = heapq.heappop(heap)
        
        if visited[u]:
            continue
        visited[u] = True
        
        for v in graph[u]:
            if not visited[v]:
                new_dist = d + 1  # 无权图，边权为1
                
                if new_dist < dist[v]:
                    # 发现更短路径
                    dist[v] = new_dist
                    count[v] = count[u]  # 重置计数
                    heapq.heappush(heap, (new_dist, v))
                elif new_dist == dist[v]:
                    # 发现相同长度路径
                    count[v] = (count[v] + count[u]) % 100003  # 题目要求取模
    
    return count

def weighted_shortest_path_count(n, edges, source):
    """
    带权图的最短路径计数（扩展版本）
    
    算法步骤：
    1. 支持带权图的最短路径计数
    2. 使用更通用的Dijkstra算法实现
    
    时间复杂度：O((V+E)logV)
    空间复杂度：O(V+E)
    
    Args:
        n: int - 节点总数
        edges: List[Tuple[int, int, int]] - 边列表，格式为 (u, v, w)
        source: int - 源点
    
    Returns:
        List[int] - 计数数组
    """
    # 构建邻接表（带权图）
    graph = defaultdict(list)
    for u, v, w in edges:
        graph[u].append((v, w))
        graph[v].append((u, w))  # 无向图
    
    dist = [float('inf')] * (n + 1)
    dist[source] = 0
    
    count = [0] * (n + 1)
    count[source] = 1
    
    visited = [False] * (n + 1)
    heap = [(0, source)]
    
    while heap:
        d, u = heapq.heappop(heap)
        
        if visited[u]:
            continue
        visited[u] = True
        
        for v, w in graph[u]:
            if not visited[v]:
                new_dist = d + w
                
                if new_dist < dist[v]:
                    dist[v] = new_dist
                    count[v] = count[u]
                    heapq.heappush(heap, (new_dist, v))
                elif new_dist == dist[v]:
                    count[v] = (count[v] + count[u]) % 100003
    
    return count

def multi_source_shortest_path_count(n, edges, sources):
    """
    多源最短路径计数（扩展功能）
    
    算法步骤：
    1. 计算从多个源点到所有节点的最短路径计数
    2. 使用虚拟超级源点法
    
    时间复杂度：O((V+E)logV)
    空间复杂度：O(V+E)
    
    Args:
        n: int - 节点总数
        edges: List[Tuple[int, int]] - 边列表
        sources: List[int] - 源点列表
    
    Returns:
        List[int] - 计数数组
    """
    # 构建扩展图（包含虚拟源点0）
    graph = defaultdict(list)
    for u, v in edges:
        graph[u].append(v)
        graph[v].append(u)
    
    # 添加虚拟源点到所有实际源点的边
    for source in sources:
        graph[0].append(source)
        graph[source].append(0)
    
    dist = [float('inf')] * (n + 1)
    dist[0] = 0
    
    count = [0] * (n + 1)
    count[0] = 1
    
    visited = [False] * (n + 1)
    heap = [(0, 0)]
    
    while heap:
        d, u = heapq.heappop(heap)
        
        if visited[u]:
            continue
        visited[u] = True
        
        for v in graph[u]:
            if not visited[v]:
                new_dist = d + 1
                
                if new_dist < dist[v]:
                    dist[v] = new_dist
                    count[v] = count[u]
                    heapq.heappush(heap, (new_dist, v))
                elif new_dist == dist[v]:
                    count[v] = (count[v] + count[u]) % 100003
    
    # 返回从节点1开始的结果（排除虚拟源点0）
    return count[1:]

def test_case_1():
    """
    测试用例1：无权图最短路径计数
    """
    print("=== 测试用例1：无权图最短路径计数 ===")
    n = 4
    edges = [
        (1, 2), (1, 3), (2, 3), (2, 4), (3, 4)
    ]
    source = 1
    
    result = shortest_path_count(n, edges, source)
    for i in range(1, n + 1):
        print(f"节点{i}的最短路径条数: {result[i]}")

def test_case_2():
    """
    测试用例2：带权图最短路径计数
    """
    print("\n=== 测试用例2：带权图最短路径计数 ===")
    n = 4
    edges = [
        (1, 2, 1), (1, 3, 2), (2, 3, 1), (2, 4, 3), (3, 4, 1)
    ]
    source = 1
    
    result = weighted_shortest_path_count(n, edges, source)
    for i in range(1, n + 1):
        print(f"节点{i}的最短路径条数: {result[i]}")

def test_case_3():
    """
    测试用例3：多源最短路径计数
    """
    print("\n=== 测试用例3：多源最短路径计数 ===")
    n = 5
    edges = [
        (1, 2), (2, 3), (3, 4), (4, 5), (1, 3), (2, 4)
    ]
    sources = [1, 3]
    
    result = multi_source_shortest_path_count(n, edges, sources)
    for i in range(len(result)):
        print(f"节点{i+1}的最短路径条数: {result[i]}")

def algorithm_analysis():
    """
    算法分析
    """
    print("\n=== 算法分析 ===")
    print("1. 核心思想：在Dijkstra算法基础上维护计数数组")
    print("2. 关键点：正确处理相同距离的路径计数累加")
    print("3. 时间复杂度：O((V+E)logV)")
    print("4. 空间复杂度：O(V+E)")
    print("5. 应用场景：")
    print("   - 网络路由路径多样性分析")
    print("   - 社交网络关系链统计")
    print("   - 交通网络最短路径选择")

if __name__ == "__main__":
    test_case_1()
    test_case_2()
    test_case_3()
    algorithm_analysis()