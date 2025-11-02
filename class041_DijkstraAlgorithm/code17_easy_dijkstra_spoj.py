import heapq
from collections import defaultdict

# SPOJ EZDIJKST - Easy Dijkstra Problem
# 确定图中指定顶点之间的最短路径
# 如果无法到达目标节点，则返回"NO"
# 否则返回最短距离
# 测试链接：https://www.spoj.com/problems/EZDIJKST/

def shortest_path(n, edges, start, end):
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
    
    # distance[i] 表示从源节点start到节点i的最短距离
    distance = [float('inf')] * (n + 1)
    # 源节点到自己的距离为0
    distance[start] = 0
    
    # visited[i] 表示节点i是否已经确定了最短距离
    visited = [False] * (n + 1)
    
    # 优先队列，按距离从小到大排序
    # 存储 (距离, 节点)
    heap = [(0, start)]
    
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
    
    # 如果无法到达终点，返回"NO"
    if distance[end] == float('inf'):
        return "NO"
    
    # 返回最短距离
    return str(distance[end])

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    n1 = 3
    edges1 = [[1,2,1],[2,3,1]]
    start1 = 1
    end1 = 3
    print("测试用例1结果:", shortest_path(n1, edges1, start1, end1))  # 期望输出: 2
    
    # 测试用例2
    n2 = 3
    edges2 = [[1,2,1]]
    start2 = 1
    end2 = 3
    print("测试用例2结果:", shortest_path(n2, edges2, start2, end2))  # 期望输出: NO