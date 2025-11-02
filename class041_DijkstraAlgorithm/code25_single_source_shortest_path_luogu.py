import heapq
from collections import defaultdict

# 洛谷 P4779 【模板】单源最短路径（标准版）
# 给定一个 n 个点，m 条有向边的带非负权图，请你计算从 s 出发，到每个点的距离
# 数据保证你能从 s 出发到任意点
# 测试链接：https://www.luogu.com.cn/problem/P4779

def dijkstra(n, m, s, edges):
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
    
    # distance[i] 表示从源节点s到节点i的最短距离
    distance = [float('inf')] * (n + 1)
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
            if not visited[v] and distance[u] + w < distance[v]:
                distance[v] = distance[u] + w
                heapq.heappush(heap, (distance[v], v))
    
    return distance

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    n1, m1, s1 = 4, 6, 1
    edges1 = [[1,2,2],[2,3,2],[2,4,1],[1,3,5],[3,4,3],[1,4,4]]
    result1 = dijkstra(n1, m1, s1, edges1)
    print(" ".join(str(result1[i]) for i in range(1, n1 + 1)))