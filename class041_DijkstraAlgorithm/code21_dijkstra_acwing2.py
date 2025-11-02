import heapq
from collections import defaultdict

# ACWing 850. Dijkstra求最短路II
# 给定一个 n 个点 m 条边的有向图，图中可能存在重边和自环，所有边权均为非负值
# 请你求出 1 号点到 n 号点的最短距离，如果无法从 1 号点走到 n 号点，则输出-1
# 测试链接：https://www.acwing.com/problem/content/852/

def dijkstra(n, edges):
    """
    使用Dijkstra算法求解单源最短路径（堆优化版本）
    时间复杂度: O((V+E)logV) 其中V是节点数，E是边数
    空间复杂度: O(V+E) 存储图和距离数组
    """
    # 构建邻接表表示的图
    graph = defaultdict(list)
    
    # 添加边到图中
    for u, v, w in edges:
        # 有向图
        graph[u].append((v, w))
    
    # distance[i] 表示从源节点1到节点i的最短距离
    distance = [float('inf')] * (n + 1)
    # 源节点到自己的距离为0
    distance[1] = 0
    
    # visited[i] 表示节点i是否已经确定了最短距离
    visited = [False] * (n + 1)
    
    # 优先队列，按距离从小到大排序
    # 存储 (距离, 节点)
    heap = [(0, 1)]
    
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
    
    # 如果无法到达终点，返回-1
    if distance[n] == float('inf'):
        return -1
    
    # 返回最短距离
    return distance[n]

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    n1 = 3
    edges1 = [[1,2,2],[2,3,1],[1,3,4]]
    print("测试用例1结果:", dijkstra(n1, edges1))  # 期望输出: 3