# Prim算法模版（洛谷）- 静态空间优化实现
# 题目链接: https://www.luogu.com.cn/problem/P3366
# 
# 题目描述:
# 给定一个无向图，求最小生成树的总边权值。如果图不连通，输出orz。
#
# 解题思路:
# 使用邻接矩阵表示图，从一个起始顶点开始，维护每个未选顶点到已选顶点集的最小距离
# 每次选择距离最小的顶点加入已选集合，并更新其他顶点的最小距离
#
# 时间复杂度: O(V^2)，其中V是顶点数，适用于稠密图
# 空间复杂度: O(V^2)，需要存储邻接矩阵
# 是否为最优解: 对于稠密图，O(V^2)的Prim算法比堆优化版本更高效
# 工程化考量:
# 1. 异常处理: 检查图是否连通
# 2. 边界条件: 处理空图、单节点图等特殊情况
# 3. 内存管理: 使用二维列表存储邻接矩阵
# 4. 性能优化: 静态空间实现避免动态内存分配

def prim_static():
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    
    # 构建邻接矩阵，初始化为无穷大
    INF = float('inf')
    graph = [[INF] * (n + 1) for _ in range(n + 1)]
    for i in range(n + 1):
        graph[i][i] = 0  # 自身到自身的距离为0
    
    for _ in range(m):
        u = int(input[ptr])
        ptr += 1
        v = int(input[ptr])
        ptr += 1
        w = int(input[ptr])
        ptr += 1
        # 取最小的边权（可能有重边）
        if w < graph[u][v]:
            graph[u][v] = w
            graph[v][u] = w
    
    # 初始化
    dist = [INF] * (n + 1)  # 存储每个顶点到已选集合的最小距离
    visited = [False] * (n + 1)  # 标记顶点是否已访问
    
    # 从顶点1开始
    dist[1] = 0
    ans = 0
    
    for _ in range(n):
        # 找到未访问顶点中距离最小的
        min_dist = INF
        u = -1
        for i in range(1, n + 1):
            if not visited[i] and dist[i] < min_dist:
                min_dist = dist[i]
                u = i
        
        # 如果没有找到这样的顶点，说明图不连通
        if u == -1:
            print("orz")
            return
        
        # 标记顶点u为已访问，并累加权值
        visited[u] = True
        ans += min_dist
        
        # 更新其他未访问顶点的最小距离
        for v in range(1, n + 1):
            if not visited[v] and graph[u][v] < dist[v]:
                dist[v] = graph[u][v]
    
    # 验证所有顶点都被访问
    for i in range(1, n + 1):
        if not visited[i]:
            print("orz")
            return
    
    print(ans)

if __name__ == "__main__":
    prim_static()