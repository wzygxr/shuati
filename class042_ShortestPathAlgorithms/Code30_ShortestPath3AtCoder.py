import heapq
from typing import List

"""
AtCoder ABC362 D - Shortest Path 3
题目链接: https://atcoder.jp/contests/abc362/tasks/abc362_d

题目描述:
给定一个无向图，每个顶点和每条边都有权重。路径的权重定义为路径上出现的顶点和边的权重之和。
找到从顶点1到顶点N的最短路径。

算法思路:
这是一个带点权和边权的最短路径问题。我们可以将点权加到边权上，然后使用Dijkstra算法求解。
对于每条边(u,v,w)，我们将边权更新为 w + vertex_weight[u] + vertex_weight[v]。
但需要注意的是，起点和终点的点权只计算一次，所以我们需要特殊处理。

时间复杂度: O((V+E)logV)，其中V是顶点数，E是边数
空间复杂度: O(V+E)
"""

def shortest_path_3(n: int, vertex_weights: List[int], edges: List[List[int]]) -> int:
    """
    求解带点权和边权的最短路径
    
    Args:
        n: 顶点数
        vertex_weights: 顶点权重数组，vertex_weights[i]表示顶点i+1的权重
        edges: 边数组，每个元素为[u, v, w]表示顶点u和v之间的边，权重为w
    
    Returns:
        从顶点1到顶点n的最短路径权重，如果无法到达则返回-1
    """
    # 构建邻接表表示的图
    graph = [[] for _ in range(n)]
    
    # 添加边到图中
    for edge in edges:
        u = edge[0] - 1  # 转换为0-based索引
        v = edge[1] - 1  # 转换为0-based索引
        w = edge[2]
        
        # 无向图，需要添加两条边
        # 边的权重 = 边权 + 起点点权 + 终点点权
        graph[u].append([v, w + vertex_weights[u] + vertex_weights[v]])
        graph[v].append([u, w + vertex_weights[u] + vertex_weights[v]])
    
    # 使用Dijkstra算法求最短路径
    # distance[i] 表示从源节点1到节点i的最短距离
    distance = [float('inf')] * n
    # 源节点到自己的距离为起点的点权
    distance[0] = vertex_weights[0]
    
    # visited[i] 表示节点i是否已经确定了最短距离
    visited = [False] * n
    
    # 优先队列，按距离从小到大排序
    # 0 : 当前节点
    # 1 : 源点到当前点距离
    pq = [(vertex_weights[0], 0)]
    
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
            v = edge[0]  # 邻居节点
            w = edge[1]  # 边的权重（已包含点权）
            
            # 如果邻居节点未访问且通过u到达v的距离更短，则更新
            # 注意：这里不需要减去终点的点权，因为在最终结果中需要加上终点的点权
            if not visited[v] and distance[u] + w - vertex_weights[u] < distance[v]:
                distance[v] = distance[u] + w - vertex_weights[u]
                heapq.heappush(pq, (distance[v], v))
    
    # 如果无法到达终点，返回-1
    if distance[n - 1] == float('inf'):
        return -1
    
    # 返回最短距离
    return int(distance[n - 1])

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    n1 = 3
    vertex_weights1 = [2, 1, 3]
    edges1 = [[1, 2, 1], [2, 3, 2]]
    print(f"测试用例1结果: {shortest_path_3(n1, vertex_weights1, edges1)}")  # 期望输出: 5
    
    # 测试用例2
    n2 = 4
    vertex_weights2 = [1, 100, 1, 100]
    edges2 = [[1, 2, 10], [2, 3, 10], [3, 4, 10]]
    print(f"测试用例2结果: {shortest_path_3(n2, vertex_weights2, edges2)}")  # 期望输出: 122