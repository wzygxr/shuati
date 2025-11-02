import heapq
from collections import defaultdict

# Aizu OJ GRL_1_A: Single Source Shortest Path
# 对于给定的加权图G(V,E)和源点r，找到从源点到每个顶点的源最短路径
# 测试链接：https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=GRL_1_A

def single_source_shortest_path(V, edges, r):
    """
    使用Dijkstra算法求解单源最短路径
    时间复杂度: O((V+E)logV) 其中V是节点数，E是边数
    空间复杂度: O(V+E) 存储图和距离数组
    """
    # 构建邻接表表示的图
    graph = defaultdict(list)
    
    # 添加边到图中
    for u, v, w in edges:
        # 有向图
        graph[u].append((v, w))
    
    # distance[i] 表示从源节点r到节点i的最短距离
    distance = [float('inf')] * V
    # 源节点到自己的距离为0
    distance[r] = 0
    
    # visited[i] 表示节点i是否已经确定了最短距离
    visited = [False] * V
    
    # 优先队列，按距离从小到大排序
    # 存储 (距离, 节点)
    heap = [(0, r)]
    
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
            if not visited[v] and distance[u] + w < distance[v]:
                distance[v] = distance[u] + w
                heapq.heappush(heap, (distance[v], v))
    
    return distance

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    V1 = 4
    edges1 = [[0,1,1],[0,2,4],[1,2,2],[2,3,1],[1,3,5]]
    r1 = 0
    result1 = single_source_shortest_path(V1, edges1, r1)
    for i in range(len(result1)):
        if result1[i] == float('inf'):
            print("INF")
        else:
            print(result1[i])