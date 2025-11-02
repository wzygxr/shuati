# 网络延迟时间
# 有 n 个网络节点，标记为 1 到 n
# 给你一个列表 times ，表示信号经过有向边的传递时间
# times[i] = (ui, vi, wi)，其中 ui 是源节点，vi 是目标节点，wi 是一个信号从源节点传递到目标节点的时间
# 现在，从某个节点 K 发出一个信号
# 需要多久才能使所有节点都收到信号？如果不能使所有节点收到信号，返回 -1
# 测试链接 : https://leetcode.com/problems/network-delay-time/
# 
# 算法思路：
# 使用优先队列BFS（Dijkstra算法）解决单源最短路径问题
# 从节点K开始，计算到所有其他节点的最短传输时间
# 最终结果是所有节点中最长的传输时间
# 
# 时间复杂度：O(E log V)，其中E是边数，V是节点数
# 空间复杂度：O(V + E)，用于存储图和优先队列
# 
# 工程化考量：
# 1. 图的表示：使用邻接表存储有向图
# 2. 优先队列：使用最小堆维护当前距离最小的节点
# 3. 结果验证：检查是否所有节点都能到达

import heapq
from collections import defaultdict
import sys

def networkDelayTime(times, n, k):
    """
    计算网络延迟时间
    
    Args:
        times: List[List[int]] - 信号传输时间列表，每个元素为[源节点, 目标节点, 传输时间]
        n: int - 节点总数
        k: int - 起始节点
        
    Returns:
        int - 网络延迟时间，如果不能使所有节点收到信号则返回-1
    """
    # 图的邻接表表示
    graph = defaultdict(list)
    
    # 构建邻接表
    for u, v, w in times:
        graph[u].append((v, w))
    
    # 距离数组，distance[i]表示从节点K到节点i的最短时间
    distance = [sys.maxsize] * (n + 1)
    
    # 访问状态数组
    visited = [False] * (n + 1)
    
    # 优先队列，存储(距离, 节点)
    heap = []
    
    # 起点距离为0
    distance[k] = 0
    heapq.heappush(heap, (0, k))
    
    # Dijkstra算法
    while heap:
        # 取出距离最小的节点
        dist, u = heapq.heappop(heap)
        
        # 如果已经访问过，跳过
        if visited[u]:
            continue
        
        visited[u] = True
        
        # 更新相邻节点的距离
        for v, w in graph[u]:
            # 如果通过节点u到达节点v的距离更短，则更新
            if not visited[v] and dist + w < distance[v]:
                distance[v] = dist + w
                heapq.heappush(heap, (distance[v], v))
    
    # 计算最大延迟时间
    maxDelay = 0
    for i in range(1, n + 1):
        if distance[i] == sys.maxsize:
            # 存在无法到达的节点
            return -1
        maxDelay = max(maxDelay, distance[i])
    
    return maxDelay