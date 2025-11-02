#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
次短路径问题（Python实现）

题目：严格次短路径（Strictly Second Shortest Path）
来源：洛谷 P2865 [USACO06NOV] Roadblocks G
链接：https://www.luogu.com.cn/problem/P2865

题目描述：
贝茜把家搬到了一个小农场，但她常常回到FJ的农场去拜访她的朋友。
贝茜很喜欢路边的风景，不想那么快地结束她的旅途，于是她每次回农场，都会选择第二短的路径。
贝茜的乡村有R条双向道路，每条路都连接了所有的N个农场中的某两个。
贝茜在1号农场，她的朋友们在N号农场。
假设次短路径长度严格大于最短路径长度，求次短路径的长度。

解题思路：
1. 方法1：删除最短路径上的边，重新计算最短路径
2. 方法2：维护两个距离数组：最短距离和次短距离
3. 方法3：使用A*算法寻找第K短路

算法应用场景：
- 交通导航中的备选路线规划
- 网络路由中的路径多样性
- 机器人路径规划中的备选路径

时间复杂度分析：
- 方法1：O(E * (V+E)logV)，效率较低
- 方法2：O((V+E)logV)，效率较高
- 方法3：O(E*K*log(E*K))，适合第K短路
"""

import heapq
from collections import defaultdict
import sys

def second_shortest_path1(n, edges, source, target):
    """
    方法1：删除最短路径上的边，重新计算最短路径
    
    算法步骤：
    1. 首先计算最短路径和路径上的边
    2. 对于最短路径上的每条边，删除后重新计算最短路径
    3. 取所有重新计算的最短路径中的最小值作为次短路径
    
    时间复杂度：O(E * (V+E)logV)
    空间复杂度：O(V+E)
    
    Args:
        n: int - 节点总数
        edges: List[Tuple[int, int, int]] - 边列表
        source: int - 源点
        target: int - 目标点
    
    Returns:
        int - 次短路径长度，如果不存在返回-1
    """
    # 首先计算最短路径和路径上的边
    shortest_path = find_shortest_path(n, edges, source, target)
    if not shortest_path:
        return -1  # 无法到达目标点
    
    # 提取最短路径上的边
    path_edges = set()
    for i in range(len(shortest_path) - 1):
        u, v = shortest_path[i], shortest_path[i + 1]
        path_edges.add((u, v))
        path_edges.add((v, u))  # 无向图
    
    second_shortest = float('inf')
    
    # 对于最短路径上的每条边，删除后重新计算最短路径
    for edge in path_edges:
        u, v = edge
        
        # 创建删除该边后的新边列表
        new_edges = []
        for e in edges:
            if (e[0] == u and e[1] == v) or (e[0] == v and e[1] == u):
                continue  # 跳过被删除的边
            new_edges.append(e)
        
        # 重新计算最短路径
        new_dist = dijkstra(n, new_edges, source, target)
        shortest_dist = get_shortest_distance(n, edges, source, target)
        
        if new_dist != float('inf') and new_dist > shortest_dist:
            second_shortest = min(second_shortest, new_dist)
    
    return -1 if second_shortest == float('inf') else second_shortest

def second_shortest_path2(n, edges, source, target):
    """
    方法2：维护两个距离数组（最优解法）
    
    算法步骤：
    1. 维护两个距离数组：dist1（最短距离）和dist2（次短距离）
    2. 使用优先队列，每个节点可能被访问两次（最短和次短）
    3. 对于每个邻居节点，更新最短和次短距离
    
    时间复杂度：O((V+E)logV)
    空间复杂度：O(V+E)
    """
    # 构建邻接表（无向图）
    graph = defaultdict(list)
    for u, v, w in edges:
        graph[u].append((v, w))
        graph[v].append((u, w))
    
    # 最短距离数组
    dist1 = [float('inf')] * (n + 1)
    dist1[source] = 0
    
    # 次短距离数组
    dist2 = [float('inf')] * (n + 1)
    
    # 优先队列，存储(距离, 节点)
    heap = [(0, source)]
    
    while heap:
        d, u = heapq.heappop(heap)
        
        # 如果当前距离大于次短距离，跳过
        if d > dist2[u]:
            continue
        
        for v, w in graph[u]:
            new_dist = d + w
            
            if new_dist < dist1[v]:
                # 发现更短路径，更新最短距离
                dist2[v] = dist1[v]  # 原来的最短距离变为次短
                dist1[v] = new_dist
                heapq.heappush(heap, (new_dist, v))
            elif new_dist > dist1[v] and new_dist < dist2[v]:
                # 发现次短路径
                dist2[v] = new_dist
                heapq.heappush(heap, (new_dist, v))
    
    return -1 if dist2[target] == float('inf') else dist2[target]

def second_shortest_path3(n, edges, source, target):
    """
    方法3：A*算法寻找第K短路（通用解法）
    
    算法步骤：
    1. 使用A*算法寻找第2短路
    2. 需要预先计算终点到所有节点的最短距离作为启发式函数
    
    时间复杂度：O(E*K*log(E*K))
    空间复杂度：O(V+E)
    """
    return find_kth_shortest_path(n, edges, source, target, 2)

# 辅助方法：计算最短路径
def find_shortest_path(n, edges, source, target):
    graph = defaultdict(list)
    for u, v, w in edges:
        graph[u].append((v, w))
        graph[v].append((u, w))
    
    dist = [float('inf')] * (n + 1)
    dist[source] = 0
    
    parent = [-1] * (n + 1)
    visited = [False] * (n + 1)
    heap = [(0, source)]
    
    while heap:
        d, u = heapq.heappop(heap)
        
        if visited[u]:
            continue
        visited[u] = True
        
        for v, w in graph[u]:
            if not visited[v] and d + w < dist[v]:
                dist[v] = d + w
                parent[v] = u
                heapq.heappush(heap, (dist[v], v))
    
    # 重构路径
    if dist[target] == float('inf'):
        return []
    
    path = []
    current = target
    while current != -1:
        path.append(current)
        current = parent[current]
    
    path.reverse()
    return path

# 辅助方法：Dijkstra算法计算最短距离
def dijkstra(n, edges, source, target):
    graph = defaultdict(list)
    for u, v, w in edges:
        graph[u].append((v, w))
        graph[v].append((u, w))
    
    dist = [float('inf')] * (n + 1)
    dist[source] = 0
    
    visited = [False] * (n + 1)
    heap = [(0, source)]
    
    while heap:
        d, u = heapq.heappop(heap)
        
        if visited[u]:
            continue
        visited[u] = True
        
        for v, w in graph[u]:
            if not visited[v] and d + w < dist[v]:
                dist[v] = d + w
                heapq.heappush(heap, (dist[v], v))
    
    return dist[target]

# 辅助方法：获取最短距离
def get_shortest_distance(n, edges, source, target):
    return dijkstra(n, edges, source, target)

# 辅助方法：A*算法寻找第K短路
def find_kth_shortest_path(n, edges, source, target, K):
    # 构建原图和反向图
    graph = defaultdict(list)
    reverse_graph = defaultdict(list)
    
    for u, v, w in edges:
        graph[u].append((v, w))
        reverse_graph[v].append((u, w))
    
    # 计算启发式函数（终点到各点的最短距离）
    heuristic = dijkstra_heuristic(n, reverse_graph, target)
    
    # A*算法寻找第K短路
    heap = [(heuristic[source], 0, source)]
    count = [0] * (n + 1)
    
    while heap:
        estimated, current_dist, u = heapq.heappop(heap)
        
        if u == target:
            count[u] += 1
            if count[u] == K:
                return current_dist
        
        if count[u] > K:
            continue
        count[u] += 1
        
        for v, w in graph[u]:
            new_dist = current_dist + w
            new_estimated = new_dist + heuristic[v]
            heapq.heappush(heap, (new_estimated, new_dist, v))
    
    return -1

# 辅助方法：计算启发式函数
def dijkstra_heuristic(n, graph, target):
    dist = [float('inf')] * (n + 1)
    dist[target] = 0
    
    visited = [False] * (n + 1)
    heap = [(0, target)]
    
    while heap:
        d, u = heapq.heappop(heap)
        
        if visited[u]:
            continue
        visited[u] = True
        
        for v, w in graph[u]:
            if not visited[v] and d + w < dist[v]:
                dist[v] = d + w
                heapq.heappush(heap, (dist[v], v))
    
    return dist

def test_case_1():
    """
    测试用例1：严格次短路径
    """
    print("=== 测试用例1：严格次短路径 ===")
    n = 4
    edges = [
        (1, 2, 100), (2, 4, 200), (2, 3, 250), (3, 4, 100)
    ]
    source, target = 1, 4
    
    print(f"方法1结果（删除边法）: {second_shortest_path1(n, edges, source, target)}")
    print(f"方法2结果（双距离数组）: {second_shortest_path2(n, edges, source, target)}")
    print(f"方法3结果（A*算法）: {second_shortest_path3(n, edges, source, target)}")

def algorithm_analysis():
    """
    算法分析
    """
    print("\n=== 算法分析 ===")
    print("方法1：删除边法")
    print("  - 优点：思路简单直观")
    print("  - 缺点：效率较低，O(E * (V+E)logV)")
    print("  - 适用：小规模图")
    
    print("方法2：双距离数组法")
    print("  - 优点：效率高，O((V+E)logV)")
    print("  - 缺点：实现稍复杂")
    print("  - 适用：大规模图，推荐使用")
    
    print("方法3：A*算法")
    print("  - 优点：通用性强，可求第K短路")
    print("  - 缺点：需要启发式函数，实现复杂")
    print("  - 适用：需要第K短路的场景")

if __name__ == "__main__":
    test_case_1()
    algorithm_analysis()