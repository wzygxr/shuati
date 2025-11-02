#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Bellman-Ford算法与Dijkstra算法对比（Python实现）

题目：带负权边的最短路径问题
来源：各大算法平台通用问题

题目描述：
给定一个带权有向图，图中可能包含负权边，需要计算从源点到所有其他节点的最短距离。
如果图中存在负权回路，则无法计算最短路径。

解题思路：
1. Dijkstra算法：适用于非负权图，时间复杂度O((V+E)logV)
2. Bellman-Ford算法：适用于含负权边图，时间复杂度O(V*E)
3. SPFA算法：Bellman-Ford的队列优化版本，平均时间复杂度O(E)

算法对比分析：
- Dijkstra算法：贪心策略，不能处理负权边
- Bellman-Ford算法：动态规划思想，可以检测负权回路
- SPFA算法：实际效率较高，但最坏情况下退化为O(V*E)

时间复杂度分析：
- Dijkstra: O((V+E)logV)
- Bellman-Ford: O(V*E)
- SPFA: 平均O(E)，最坏O(V*E)

空间复杂度分析：
- 均为O(V+E)
"""

import heapq
from collections import defaultdict, deque
import sys

def dijkstra(n, edges, source):
    """
    Dijkstra算法实现（仅适用于非负权图）
    
    算法步骤：
    1. 初始化距离数组，源点距离为0，其他点为无穷大
    2. 使用优先队列维护待处理节点
    3. 每次取出距离最小的节点，更新其邻居节点的距离
    
    时间复杂度：O((V+E)logV)
    空间复杂度：O(V+E)
    
    Args:
        n: int - 节点总数
        edges: List[List[int]] - 边列表
        source: int - 源点
    
    Returns:
        List[int] - 最短距离数组
    """
    # 构建邻接表
    graph = defaultdict(list)
    for u, v, w in edges:
        graph[u].append((v, w))
    
    dist = [float('inf')] * (n + 1)
    dist[source] = 0
    
    visited = [False] * (n + 1)
    heap = [(0, source)]
    
    while heap:
        current_dist, u = heapq.heappop(heap)
        
        if visited[u]:
            continue
        visited[u] = True
        
        for v, w in graph[u]:
            if not visited[v] and current_dist + w < dist[v]:
                dist[v] = current_dist + w
                heapq.heappush(heap, (dist[v], v))
    
    return dist

def bellman_ford(n, edges, source):
    """
    Bellman-Ford算法实现（可处理负权边）
    
    算法步骤：
    1. 初始化距离数组，源点距离为0
    2. 进行V-1次松弛操作
    3. 检查是否存在负权回路
    
    时间复杂度：O(V*E)
    空间复杂度：O(V+E)
    
    Args:
        n: int - 节点总数
        edges: List[List[int]] - 边列表
        source: int - 源点
    
    Returns:
        List[int] - 最短距离数组
    """
    dist = [float('inf')] * (n + 1)
    dist[source] = 0
    
    # 进行V-1次松弛操作
    for i in range(n - 1):
        updated = False
        for u, v, w in edges:
            if dist[u] != float('inf') and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                updated = True
        
        # 如果没有更新，提前结束
        if not updated:
            break
    
    # 检查负权回路
    for u, v, w in edges:
        if dist[u] != float('inf') and dist[u] + w < dist[v]:
            raise ValueError("图中存在负权回路")
    
    return dist

def spfa(n, edges, source):
    """
    SPFA算法（Bellman-Ford的队列优化）
    
    算法步骤：
    1. 初始化距离数组和队列
    2. 将源点加入队列
    3. 不断从队列取出节点进行松弛操作
    4. 检查负权回路
    
    时间复杂度：平均O(E)，最坏O(V*E)
    空间复杂度：O(V+E)
    
    Args:
        n: int - 节点总数
        edges: List[List[int]] - 边列表
        source: int - 源点
    
    Returns:
        List[int] - 最短距离数组
    """
    # 构建邻接表
    graph = defaultdict(list)
    for u, v, w in edges:
        graph[u].append((v, w))
    
    dist = [float('inf')] * (n + 1)
    dist[source] = 0
    
    in_queue = [False] * (n + 1)
    count = [0] * (n + 1)  # 记录入队次数，用于检测负权回路
    
    queue = deque()
    queue.append(source)
    in_queue[source] = True
    count[source] += 1
    
    while queue:
        u = queue.popleft()
        in_queue[u] = False
        
        for v, w in graph[u]:
            if dist[u] != float('inf') and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                
                if not in_queue[v]:
                    queue.append(v)
                    in_queue[v] = True
                    count[v] += 1
                    
                    # 如果某个节点入队次数超过V次，说明存在负权回路
                    if count[v] > n:
                        raise ValueError("图中存在负权回路")
    
    return dist

def compare_algorithms():
    """
    算法性能对比测试
    """
    print("=== 测试用例1：非负权图 ===")
    n1 = 5
    edges1 = [
        (1, 2, 2), (1, 3, 4), (2, 3, 1),
        (2, 4, 7), (3, 4, 3), (3, 5, 5), (4, 5, 2)
    ]
    source1 = 1
    
    print("Dijkstra算法结果:")
    result1 = dijkstra(n1, edges1, source1)
    print_array(result1, 1, n1)
    
    print("Bellman-Ford算法结果:")
    result2 = bellman_ford(n1, edges1, source1)
    print_array(result2, 1, n1)
    
    print("SPFA算法结果:")
    result3 = spfa(n1, edges1, source1)
    print_array(result3, 1, n1)
    
    print("\n=== 测试用例2：含负权边图（无负权回路） ===")
    n2 = 4
    edges2 = [
        (1, 2, 3), (1, 3, 5), (2, 3, -2),
        (2, 4, 4), (3, 4, 1)
    ]
    source2 = 1
    
    print("Dijkstra算法结果（可能不正确）:")
    try:
        result4 = dijkstra(n2, edges2, source2)
        print_array(result4, 1, n2)
    except:
        print("Dijkstra算法无法处理负权边")
    
    print("Bellman-Ford算法结果:")
    result5 = bellman_ford(n2, edges2, source2)
    print_array(result5, 1, n2)
    
    print("SPFA算法结果:")
    result6 = spfa(n2, edges2, source2)
    print_array(result6, 1, n2)
    
    print("\n=== 测试用例3：含负权回路图 ===")
    n3 = 3
    edges3 = [
        (1, 2, 1), (2, 3, -2), (3, 1, -1)
    ]
    source3 = 1
    
    print("Bellman-Ford算法检测负权回路:")
    try:
        result7 = bellman_ford(n3, edges3, source3)
        print_array(result7, 1, n3)
    except ValueError as e:
        print(f"检测到负权回路: {e}")
    
    print("SPFA算法检测负权回路:")
    try:
        result8 = spfa(n3, edges3, source3)
        print_array(result8, 1, n3)
    except ValueError as e:
        print(f"检测到负权回路: {e}")

def print_array(arr, start, end):
    """
    打印数组
    """
    for i in range(start, end + 1):
        if arr[i] == float('inf'):
            print("INF", end=" ")
        else:
            print(arr[i], end=" ")
    print()

def algorithm_selection_guide():
    """
    算法选择指南
    """
    print("\n=== 最短路径算法选择指南 ===")
    print("1. 如果图中所有边权重非负：")
    print("   - 优先选择Dijkstra算法（效率最高）")
    print("   - 时间复杂度：O((V+E)logV)")
    
    print("2. 如果图中包含负权边但无负权回路：")
    print("   - 选择Bellman-Ford或SPFA算法")
    print("   - Bellman-Ford：O(V*E)，实现简单")
    print("   - SPFA：平均O(E)，实际效率较高")
    
    print("3. 如果需要检测负权回路：")
    print("   - 必须使用Bellman-Ford或SPFA算法")
    print("   - Dijkstra算法无法检测负权回路")
    
    print("4. 图规模较大时：")
    print("   - 非负权图：Dijkstra算法")
    print("   - 含负权边图：SPFA算法")
    
    print("5. 特殊场景：")
    print("   - 多源最短路径：Floyd算法")
    print("   - 稀疏图：SPFA可能比Dijkstra更快")

def performance_test():
    """
    性能测试建议
    """
    print("\n=== 性能测试建议 ===")
    print("对于V=1000, E=10000的图：")
    print("Dijkstra算法：约10000*log(1000) ≈ 100000次操作")
    print("Bellman-Ford算法：约1000*10000 = 10,000,000次操作")
    print("SPFA算法：平均约10000次操作，最坏10,000,000次操作")

if __name__ == "__main__":
    compare_algorithms()
    algorithm_selection_guide()
    performance_test()