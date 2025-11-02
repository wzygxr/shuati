#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
最短路计数

题目链接: https://www.luogu.com.cn/problem/P1144

题目描述：
给出一个 N 个顶点 M 条边的无向无权图，顶点编号为 1∼N。
问从顶点 1 开始，到其他每个点的最短路有几条。

解题思路：
这是一个在Dijkstra算法基础上扩展的问题，不仅要计算最短距离，还要统计最短路径的条数。
在传统的Dijkstra算法中，我们只维护每个节点的最短距离。
在这个问题中，我们还需要维护到达每个节点的最短路径条数。
当我们找到更短的路径时，更新最短距离和路径条数；
当我们找到相同长度的路径时，累加路径条数。

算法应用场景：
- 网络路由中的路径选择
- 社交网络中的最短连接路径统计
- 图论中的路径计数问题

时间复杂度分析：
O((V+E)logV) 其中V是顶点数，E是边数

空间复杂度分析：
O(V+E) 存储图、距离数组和路径计数数组
"""

import heapq
from collections import defaultdict

def countPaths(n, edges):
    """
    使用Dijkstra算法求解最短路计数
    
    算法核心思想：
    1. 在传统Dijkstra算法基础上，增加路径计数功能
    2. 维护两个数组：distance记录最短距离，count记录最短路径条数
    3. 当发现更短路径时，更新距离和路径数
    4. 当发现相同长度路径时，累加路径数
    
    算法步骤：
    1. 初始化距离数组和路径计数数组
    2. 使用优先队列维护待处理节点，按距离从小到大排序
    3. 不断取出距离最小的节点，更新其邻居节点的最短距离和路径数
    4. 返回所有节点的最短路径条数
    
    时间复杂度: O((V+E)logV) 其中V是顶点数，E是边数
    空间复杂度: O(V+E)
    
    Args:
        n: int - 顶点数量
        edges: List[List[int]] - 边的信息，edges[i] = [顶点1, 顶点2]
    
    Returns:
        List[int] - 从顶点1到其他每个点的最短路径条数数组
    """
    MOD = 100003
    
    # 构建邻接表表示的图
    # graph[i] 存储顶点i的所有邻居节点
    graph = defaultdict(list)
    
    # 添加边到图中（无向图）
    # 对于每条边，将其添加到两个顶点的邻居列表中
    for edge in edges:
        graph[edge[0]].append(edge[1])  # 顶点1到顶点2
        graph[edge[1]].append(edge[0])  # 顶点2到顶点1
    
    # distance[i]表示从顶点1到顶点i的最短距离
    # 初始化为最大值，表示尚未访问
    distance = [float('inf')] * (n + 1)
    
    # count[i]表示从顶点1到顶点i的最短路径条数
    # 初始化为0，表示尚未找到路径
    count = [0] * (n + 1)
    
    # visited[i]表示顶点i是否已经确定了最短距离
    # 用于避免重复处理已经确定最短距离的节点
    visited = [False] * (n + 1)
    
    # 优先队列，按距离从小到大排序
    # 元组含义：(距离, 顶点)
    heap = [(0, 1)]
    
    # 初始状态：在顶点1，距离为0，路径数为1
    distance[1] = 0   # 从顶点1到自身的距离为0
    count[1] = 1      # 从顶点1到自身的路径数为1
    
    # Dijkstra算法主循环
    while heap:
        # 取出距离最小的节点
        dist, u = heapq.heappop(heap)
        
        # 如果已经处理过，跳过
        # 这是为了避免同一节点多次处理导致的重复计算
        if visited[u]:
            continue
        
        # 标记为已处理，表示已确定从顶点1到该顶点的最短距离
        visited[u] = True
        
        # 遍历所有邻居节点
        for v in graph[u]:
            # 在无权图中，每条边的权重为1
            # 如果通过u到达v的距离更短，则更新最短距离和路径数
            if distance[u] + 1 < distance[v]:
                distance[v] = distance[u] + 1  # 更新最短距离
                count[v] = count[u]            # 路径数等于到达u的路径数
                # 将更新后的节点加入优先队列
                heapq.heappush(heap, (distance[v], v))
            # 如果通过u到达v的距离等于当前最短距离，则累加路径数
            # 这表示我们找到了另一条同样长度的最短路径
            elif distance[u] + 1 == distance[v]:
                # 累加路径数，注意取模防止溢出
                count[v] = (count[v] + count[u]) % MOD
    
    return count


# 测试用例
if __name__ == "__main__":
    # 示例
    # 输入: n = 5, edges = [[1, 2], [1, 3], [2, 4], [3, 4], [2, 3], [4, 5], [4, 5]]
    # 输出: 从顶点1到其他每个点的最短路径条数
    n = 5
    edges = [[1, 2], [1, 3], [2, 4], [3, 4], [2, 3], [4, 5], [4, 5]]
    result = countPaths(n, edges)
    
    # 输出结果
    for i in range(1, n + 1):
        if result[i] == float('inf'):
            print(0)  # 无法到达的节点路径数为0
        else:
            print(result[i])  # 输出路径数