#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
POJ 2253 Frogger

题目链接: http://poj.org/problem?id=2253

题目描述:
一只小青蛙住在一条繁忙的河边。有一天，它想去看望它的朋友，它的朋友住在河的另一边。
这条河可以看作是一个二维平面，青蛙可以从一个石头跳到另一个石头。
每次跳跃的长度是两个石头之间的欧几里得距离。
青蛙希望找到一条路径，使得路径上最长的跳跃距离尽可能小。

解题思路:
这是一个变形的最短路径问题，称为瓶颈路径问题。
我们需要找到从起点到终点的路径，使得路径上边权的最大值最小。
可以使用修改版的Dijkstra算法来解决。

算法应用场景:
- 网络传输中的最大延迟路径
- 机器人路径规划中的最大步长限制
- 游戏中的角色移动路径优化

时间复杂度分析:
O((V + E) * log V)，其中V是节点数，E是边数

空间复杂度分析:
O(V + E)，用于存储图和距离数组
"""

import heapq
import math

def frogger(stones, start, end):
    """
    使用修改版Dijkstra算法计算青蛙跳跃的最小最大距离
    
    算法步骤:
    1. 构建图的邻接表表示，边权为两点间的欧几里得距离
    2. 初始化距离数组，起始节点距离为0，其他节点为无穷大
    3. 使用优先队列维护待处理节点，按距离从小到大排序
    4. 不断取出距离最小的节点，更新其邻居节点的最短距离
    5. 返回目标节点的最短距离
    
    时间复杂度: O((V + E) * log V)
    空间复杂度: O(V + E)
    
    Args:
        stones: List[List[int]] - 石头的坐标数组，每个元素为 [x, y]
        start: int - 起始石头索引
        end: int - 目标石头索引
    
    Returns:
        float - 青蛙跳跃的最小最大距离
    """
    n = len(stones)
    
    # 计算两点间的欧几里得距离
    def distance(p1, p2):
        return math.sqrt((p1[0] - p2[0])**2 + (p1[1] - p2[1])**2)
    
    # 构建邻接表表示的图
    graph = [[] for _ in range(n)]
    for i in range(n):
        for j in range(i + 1, n):
            dist = distance(stones[i], stones[j])
            graph[i].append((j, dist))
            graph[j].append((i, dist))
    
    # distance[i] 表示从起始石头到石头i的最小最大跳跃距离
    min_max_dist = [float('inf')] * n
    min_max_dist[start] = 0
    
    # visited[i] 表示石头i是否已经确定了最短距离
    visited = [False] * n
    
    # 优先队列，按距离从小到大排序
    heap = [(0, start)]
    
    # 修改版Dijkstra算法主循环
    while heap:
        # 取出最小最大跳跃距离的节点
        dist, u = heapq.heappop(heap)
        
        # 如果已经处理过，跳过
        if visited[u]:
            continue
        
        # 如果到达目标石头，直接返回结果
        if u == end:
            return dist
        
        # 标记为已处理
        visited[u] = True
        
        # 遍历u的所有邻居石头
        for v, w in graph[u]:
            # 新的距离是当前路径上的最大跳跃距离
            new_dist = max(dist, w)
            
            # 如果新的最大跳跃距离更小，则更新
            if not visited[v] and new_dist < min_max_dist[v]:
                min_max_dist[v] = new_dist
                heapq.heappush(heap, (min_max_dist[v], v))
    
    # 理论上不会执行到这里
    return -1

# 测试方法
if __name__ == "__main__":
    # 示例测试用例
    stones = [[0, 0], [1, 0], [2, 0], [3, 0]]
    start = 0
    end = 3
    
    result = frogger(stones, start, end)
    print(f"青蛙跳跃的最小最大距离为: {result:.3f}")