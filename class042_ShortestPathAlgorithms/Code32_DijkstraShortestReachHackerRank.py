import heapq
from typing import List

"""
HackerRank Dijkstra: Shortest Reach 2
题目链接: https://www.hackerrank.com/challenges/dijkstrashortreach/problem

题目描述:
给定一个无向图和一个起始节点，确定从起始节点到所有其他节点的最短路径长度。
如果无法到达某个节点，则返回-1。

算法思路:
使用标准的Dijkstra算法求解单源最短路径问题。

时间复杂度: O((V+E)logV)，其中V是节点数，E是边数
空间复杂度: O(V+E)
"""

def shortest_reach(n: int, edges: List[List[int]], s: int) -> List[int]:
    """
    求解单源最短路径
    
    Args:
        n: 节点数
        edges: 边数组，每个元素为[u, v, w]表示节点u和v之间的边，权重为w
        s: 起始节点
    
    Returns:
        从起始节点到所有其他节点的最短距离数组，无法到达的节点距离为-1
    """
    # 构建邻接表表示的图
    graph = [[] for _ in range(n + 1)]
    
    # 添加边到图中
    for edge in edges:
        u, v, w = edge
        # 无向图，需要添加两条边
        graph[u].append([v, w])
        graph[v].append([u, w])
    
    # distance[i] 表示从源节点s到节点i的最短距离
    distance = [-1] * (n + 1)
    # 源节点到自己的距离为0
    distance[s] = 0
    
    # visited[i] 表示节点i是否已经确定了最短距离
    visited = [False] * (n + 1)
    
    # 优先队列，按距离从小到大排序
    # 0 : 源点到当前点距离
    # 1 : 当前节点
    pq = [(0, s)]
    
    while pq:
        # 取出距离源点最近的节点
        dist, u = heapq.heappop(pq)
        
        # 如果已经处理过，跳过
        if visited[u]:
            continue
        # 标记为已处理
        visited[u] = True
        
        # 遍历u的所有邻居节点
        for edge in graph[u]:
            v, w = edge  # 邻居节点和边的权重
            
            # 如果邻居节点未访问且通过u到达v的距离更短，则更新
            if not visited[v] and (distance[v] == -1 or distance[u] + w < distance[v]):
                distance[v] = distance[u] + w
                heapq.heappush(pq, (distance[v], v))
    
    # 构造结果数组，排除起始节点
    result = []
    for i in range(1, n + 1):
        if i != s:
            result.append(distance[i])
    
    return result

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    n1 = 4
    edges1 = [[1, 2, 24], [1, 4, 20], [3, 1, 3], [4, 3, 12]]
    s1 = 1
    result1 = shortest_reach(n1, edges1, s1)
    print(f"测试用例1结果: {result1}")  # 期望输出: [24, 3, 15]