import heapq
from typing import List, Dict

"""
SPOJ SHPATH - The Shortest Path
题目链接: https://www.spoj.com/problems/SHPATH/

题目描述:
给定一个城市的地图，城市之间有道路连接，每条道路都有一个成本。目标是找到城市对之间的最小成本路径。

算法思路:
这是一个标准的单源最短路径问题，可以使用Dijkstra算法解决。
由于需要处理多个查询，我们可以对每个查询都运行一次Dijkstra算法。

时间复杂度: O(Q * (V + E) * logV)，其中Q是查询数，V是城市数，E是道路数
空间复杂度: O(V + E)
"""

def the_shortest_path(n: int, city_names: List[str], roads: List[List[int]], queries: List[List[str]]) -> List[int]:
    """
    求解城市间的最短路径
    
    Args:
        n: 城市数量
        city_names: 城市名称数组
        roads: 道路数组，每个元素为[city1, city2, cost]表示两个城市之间的道路及其成本
        queries: 查询数组，每个元素为[start_city, end_city]表示查询的起点和终点城市
    
    Returns:
        每个查询的最短路径成本数组
    """
    # 创建城市名称到索引的映射
    city_index_map = {name: i for i, name in enumerate(city_names)}
    
    # 构建邻接表表示的图
    graph = [[] for _ in range(n)]
    
    # 添加道路到图中
    for road in roads:
        city1 = road[0] - 1  # 转换为0-based索引
        city2 = road[1] - 1  # 转换为0-based索引
        cost = road[2]
        
        # 无向图，需要添加两条边
        graph[city1].append([city2, cost])
        graph[city2].append([city1, cost])
    
    # 处理每个查询
    results = []
    for query in queries:
        start_city = query[0]
        end_city = query[1]
        
        # 获取起点和终点的索引
        start = city_index_map[start_city]
        end = city_index_map[end_city]
        
        # 使用Dijkstra算法求最短路径
        result = dijkstra(graph, start, end, n)
        results.append(result)
    
    return results

def dijkstra(graph: List[List[List[int]]], start: int, end: int, n: int) -> int:
    """
    Dijkstra算法实现
    
    Args:
        graph: 图的邻接表表示
        start: 起点
        end: 终点
        n: 顶点数
    
    Returns:
        从起点到终点的最短距离，如果无法到达则返回-1
    """
    # distance[i] 表示从源节点到节点i的最短距离
    distance = [float('inf')] * n
    # 源节点到自己的距离为0
    distance[start] = 0.0
    
    # visited[i] 表示节点i是否已经确定了最短距离
    visited = [False] * n
    
    # 优先队列，按距离从小到大排序
    # 0 : 源点到当前点距离
    # 1 : 当前节点
    pq: List[tuple] = [(0.0, start)]
    
    while pq:
        # 取出距离源点最近的节点
        dist, u = heapq.heappop(pq)
        
        # 如果已经处理过，跳过
        if visited[u]:
            continue
        # 标记为已处理
        visited[u] = True
        
        # 如果到达终点，直接返回距离
        if u == end:
            return int(dist)
        
        # 遍历u的所有邻居节点
        for edge in graph[u]:
            v = edge[0]  # 邻居节点
            w = edge[1]  # 边的权重
            
            # 如果邻居节点未访问且通过u到达v的距离更短，则更新
            if not visited[v] and distance[u] + w < distance[v]:
                distance[v] = distance[u] + w
                heapq.heappush(pq, (distance[v], v))
    
    # 如果无法到达终点，返回-1
    return -1

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    n1 = 4
    city_names1 = ["a", "b", "c", "d"]
    roads1 = [[1, 2, 1], [2, 3, 2], [3, 4, 3], [1, 4, 10]]
    queries1 = [["a", "d"], ["b", "c"]]
    result1 = the_shortest_path(n1, city_names1, roads1, queries1)
    print(f"测试用例1结果: {result1}")  # 期望输出: [6, 2]