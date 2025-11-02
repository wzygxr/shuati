import heapq
from collections import defaultdict

# HackerRank Dijkstra: Shortest Reach 2
# 给定一个无向图和一个起始节点，确定从起始节点到所有其他节点的最短路径长度
# 如果无法到达某个节点，则返回-1
# 测试链接：https://www.hackerrank.com/challenges/dijkstrashortreach/problem

def shortest_reach(n, edges, s):
    """
    使用Dijkstra算法求解单源最短路径
    时间复杂度: O((V+E)logV) 其中V是节点数，E是边数
    空间复杂度: O(V+E) 存储图和距离数组
    """
    # 构建邻接表表示的图
    graph = defaultdict(list)
    
    # 添加边到图中
    for u, v, w in edges:
        # 无向图，需要添加两条边
        graph[u].append((v, w))
        graph[v].append((u, w))
    
    # distance[i] 表示从源节点s到节点i的最短距离
    distance = [-1] * (n + 1)
    # 源节点到自己的距离为0
    distance[s] = 0
    
    # visited[i] 表示节点i是否已经确定了最短距离
    visited = [False] * (n + 1)
    
    # 优先队列，按距离从小到大排序
    # 存储 (距离, 节点)
    heap = [(0, s)]
    
    while heap:
        # 取出距离源点最近的节点
        dist, u = heapq.heappop(heap)
        
        # 如果已经处理过，跳过
        if visited[u]:
            continue
        # 标记为已处理
        visited[u] = True
        
        # 遍历u的所有邻居节点
        for v, w in graph[u]:
            # 如果邻居节点未访问且通过u到达v的距离更短，则更新
            if not visited[v] and (distance[v] == -1 or distance[u] + w < distance[v]):
                distance[v] = distance[u] + w
                heapq.heappush(heap, (distance[v], v))
    
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
    edges1 = [[1,2,24],[1,4,20],[3,1,3],[4,3,12]]
    s1 = 1
    result1 = shortest_reach(n1, edges1, s1)
    print(" ".join(map(str, result1)))