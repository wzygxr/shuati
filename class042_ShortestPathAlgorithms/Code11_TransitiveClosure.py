"""
Floyd算法应用: 计算有向图的传递闭包

传递闭包定义: 如果存在从i到j的路径，则闭包矩阵[i][j]为True
算法思路: 将Floyd算法中的距离计算改为布尔运算
状态转移: reachable[i][j] = reachable[i][j] || (reachable[i][k] && reachable[k][j])

时间复杂度: O(N³)
空间复杂度: O(N²)
"""

def computeTransitiveClosure(n, edges):
    """
    计算有向图的传递闭包
    
    Args:
        n: 节点数量
        edges: 边数组，每个元素为[起点, 终点]
    
    Returns:
        传递闭包矩阵，reachable[i][j]为True表示存在从i到j的路径
    """
    # 初始化可达性矩阵
    reachable = [[False] * n for _ in range(n)]
    
    # 初始化: 节点到自身可达，直接边可达
    for i in range(n):
        reachable[i][i] = True
    for edge in edges:
        reachable[edge[0]][edge[1]] = True
    
    # Floyd-Warshall算法计算传递闭包
    for k in range(n):
        for i in range(n):
            for j in range(n):
                reachable[i][j] = reachable[i][j] or (reachable[i][k] and reachable[k][j])
    
    return reachable

# 测试函数
if __name__ == "__main__":
    # 测试用例1：简单有向图
    n1 = 4
    edges1 = [[0,1],[1,2],[2,3]]
    result1 = computeTransitiveClosure(n1, edges1)
    print("测试用例1结果:")
    for i in range(n1):
        for j in range(n1):
            print("1" if result1[i][j] else "0", end=" ")
        print()
    
    # 测试用例2：复杂有向图
    n2 = 3
    edges2 = [[0,1],[1,2],[2,0]]
    result2 = computeTransitiveClosure(n2, edges2)
    print("测试用例2结果:")
    for i in range(n2):
        for j in range(n2):
            print("1" if result2[i][j] else "0", end=" ")
        print()