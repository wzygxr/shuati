"""
差分约束系统求解 - Bellman-Ford算法应用

问题描述: 求解一组形如 xj - xi ≤ ck 的不等式组
算法思路: 将不等式转化为图论问题，使用Bellman-Ford求解

转换方法:
对于每个不等式 xj - xi ≤ ck，添加一条边 i->j，权重为ck
添加超级源点0，到所有点的边权重为0
运行Bellman-Ford算法，如果存在负环则无解，否则距离数组即为解

时间复杂度: O(N×E)
空间复杂度: O(N+E)
"""

def solveDifferenceConstraints(n, constraints):
    """
    求解差分约束系统
    
    Args:
        n: 变量数量
        constraints: 约束条件数组，每个元素为[xi, xj, ck]表示xj - xi ≤ ck
    
    Returns:
        解数组，如果无解返回None
    """
    # 构建图（包含超级源点0）
    edges = []
    
    # 添加约束边
    for constraint in constraints:
        # 注意：变量编号从1开始，转换为从0开始
        edges.append([constraint[0]-1, constraint[1]-1, constraint[2]])
    
    # 添加超级源点边
    for i in range(n):
        edges.append([n, i, 0]) # 超级源点n到所有点的边权重为0
    
    # 运行Bellman-Ford算法
    distance = [float('inf')] * (n + 1)
    distance[n] = 0 # 超级源点距离为0
    
    # n+1轮松弛
    for i in range(n + 1):
        updated = False
        for edge in edges:
            u, v, w = edge[0], edge[1], edge[2]
            if distance[u] != float('inf') and distance[u] + w < distance[v]:
                distance[v] = distance[u] + w
                updated = True
        # 如果某轮没有更新，提前结束
        if not updated:
            break
    
    # 检测负环
    for edge in edges:
        u, v, w = edge[0], edge[1], edge[2]
        if distance[u] != float('inf') and distance[u] + w < distance[v]:
            return None # 存在负环，无解
    
    # 返回解（去掉超级源点）
    return distance[:n]

# 测试函数
if __name__ == "__main__":
    # 测试用例1：有解的差分约束系统
    # 约束条件: x1 - x0 <= 2, x2 - x1 <= 3, x0 - x2 <= -4
    n1 = 3
    constraints1 = [[0,1,2],[1,2,3],[2,0,-4]]
    result1 = solveDifferenceConstraints(n1, constraints1)
    print(f"测试用例1结果: {result1 if result1 is not None else '无解'}")
    
    # 测试用例2：无解的差分约束系统
    # 约束条件: x1 - x0 <= 1, x0 - x1 <= -2
    n2 = 2
    constraints2 = [[0,1,1],[1,0,-2]]
    result2 = solveDifferenceConstraints(n2, constraints2)
    print(f"测试用例2结果: {result2 if result2 is not None else '无解'}")