# ACWing 849. Dijkstra求最短路I
# 给定一个 n 个点 m 条边的有向图，图中可能存在重边和自环，所有边权均为正值
# 请你求出 1 号点到 n 号点的最短距离，如果无法从 1 号点走到 n 号点，则输出-1
# 测试链接：https://www.acwing.com/problem/content/851/

def dijkstra(n, edges):
    """
    使用Dijkstra算法求解单源最短路径（朴素版本）
    时间复杂度: O(V^2) 其中V是节点数
    空间复杂度: O(V^2) 存储邻接矩阵
    """
    # 构建邻接矩阵表示的图
    INF = float('inf')
    graph = [[INF] * (n + 1) for _ in range(n + 1)]
    
    # 添加边到图中
    for u, v, w in edges:
        # 有向图，可能存在重边，取最小值
        graph[u][v] = min(graph[u][v], w)
    
    # distance[i] 表示从源节点1到节点i的最短距离
    distance = [INF] * (n + 1)
    # 源节点到自己的距离为0
    distance[1] = 0
    
    # visited[i] 表示节点i是否已经确定了最短距离
    visited = [False] * (n + 1)
    
    # Dijkstra算法主循环
    for i in range(1, n + 1):
        # 找到未访问节点中距离最小的节点
        u = -1
        for j in range(1, n + 1):
            if not visited[j] and (u == -1 or distance[j] < distance[u]):
                u = j
        
        # 如果找不到有效节点，说明无法到达
        if u == -1:
            break
        
        # 标记为已访问
        visited[u] = True
        
        # 更新u的所有邻居节点的距离
        for v in range(1, n + 1):
            if not visited[v] and graph[u][v] != INF:
                distance[v] = min(distance[v], distance[u] + graph[u][v])
    
    # 如果无法到达终点，返回-1
    if distance[n] == INF:
        return -1
    
    # 返回最短距离
    return distance[n]

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    n1 = 3
    edges1 = [[1,2,2],[2,3,1],[1,3,4]]
    print("测试用例1结果:", dijkstra(n1, edges1))  # 期望输出: 3