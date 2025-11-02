import heapq
from collections import defaultdict

# 杭电OJ 2544 最短路
# 在一个无向图中，求从节点1到节点N的最短路径
# 测试链接：http://acm.hdu.edu.cn/showproblem.php?pid=2544

def shortest_path(n, edges):
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
    
    return distance[n]

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    n1 = 4
    edges1 = [[1,2,3],[2,3,2],[3,4,1],[1,4,5]]
    print("测试用例1结果:", shortest_path(n1, edges1))  # 期望输出: 4