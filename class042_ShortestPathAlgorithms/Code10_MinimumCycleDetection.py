import sys

"""
最小环检测 - Floyd算法应用

算法思路:
在Floyd算法的执行过程中，当考虑中间节点k时，
检查是否存在i->k和k->i的路径，从而形成环
最小环长度 = dist[i][k] + dist[k][i]

时间复杂度: O(N³)，与标准Floyd相同
空间复杂度: O(N²)
"""

def findMinimumCycle(n, edges):
    """
    查找图中的最小环
    
    Args:
        n: 节点数量
        edges: 边数组，每个元素为[起点, 终点, 权重]
    
    Returns:
        最小环的长度，如果不存在环则返回-1
    """
    # 初始化距离矩阵
    dist = [[float('inf')] * n for _ in range(n)]
    
    # 初始化距离矩阵
    for i in range(n):
        dist[i][i] = 0
    
    # 添加边信息
    for edge in edges:
        u, v, w = edge[0], edge[1], edge[2]
        # 注意：这里假设是无向图，所以添加双向边
        # 如果是有向图，只需要添加 dist[u][v] = w
        dist[u][v] = min(dist[u][v], w)
        dist[v][u] = min(dist[v][u], w)
    
    minCycle = float('inf')
    
    # Floyd算法检测最小环
    for k in range(n):
        # 在更新之前，检查经过k的环
        for i in range(k):
            for j in range(k):
                if (dist[i][k] != float('inf') and 
                    dist[k][j] != float('inf') and
                    dist[j][i] != float('inf')):
                    
                    minCycle = min(minCycle, 
                        dist[i][k] + dist[k][j] + dist[j][i])
        
        # 标准Floyd更新
        for i in range(n):
            if dist[i][k] == float('inf'):
                continue
            for j in range(n):
                if (dist[i][k] != float('inf') and 
                    dist[k][j] != float('inf') and
                    dist[i][j] > dist[i][k] + dist[k][j]):
                    
                    dist[i][j] = dist[i][k] + dist[k][j]
    
    return -1 if minCycle == float('inf') else int(minCycle)

# 测试函数
if __name__ == "__main__":
    # 测试用例1：存在环的图
    n1 = 4
    edges1 = [[0,1,1],[1,2,2],[2,3,3],[3,0,4],[0,2,5]]
    result1 = findMinimumCycle(n1, edges1)
    print(f"测试用例1结果: {result1}")  # 期望输出: 7 (环0->1->2->0)
    
    # 测试用例2：不存在环的图
    n2 = 3
    edges2 = [[0,1,1],[1,2,2]]
    result2 = findMinimumCycle(n2, edges2)
    print(f"测试用例2结果: {result2}")  # 期望输出: -1