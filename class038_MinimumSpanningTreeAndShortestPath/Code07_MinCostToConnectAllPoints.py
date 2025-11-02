# LeetCode 1584. Min Cost to Connect All Points
# 题目链接: https://leetcode.cn/problems/min-cost-to-connect-all-points/
# 
# 题目描述:
# 给你一个points数组，表示 2D 平面上的一些点，其中 points[i] = [xi, yi]。
# 连接点 [xi, yi] 和点 [xj, yj] 的费用为它们之间的曼哈顿距离：|xi - xj| + |yi - yj|，
# 其中 |val| 表示 val 的绝对值。请你返回将所有点连接的最小总费用。
# 只有任意两点之间有且仅有一条简单路径时，才认为所有点都已连接。
#
# 解题思路:
# 这是一个典型的最小生成树问题，但与传统MST问题不同的是，这里没有直接给出边，
# 而是给出了点的坐标，需要我们计算任意两点之间的曼哈顿距离作为边的权重。
# 使用Kruskal算法：
# 1. 计算所有点对之间的曼哈顿距离，构造成边
# 2. 将所有边按权重升序排序
# 3. 使用并查集判断添加边是否会形成环
# 4. 依次选择不形成环的最小边，直到选择了n-1条边
#
# 时间复杂度: O(N^2 * log(N))，其中N是点的数量
# 空间复杂度: O(N^2)，用于存储所有边
# 是否为最优解: 是，这是解决该问题的标准方法


class UnionFind:
    """并查集数据结构实现"""
    
    def __init__(self, n):
        """初始化并查集"""
        # parent[i] 表示节点i的父节点
        self.parent = list(range(n))
        # rank[i] 表示以i为根的树的秩（近似高度）
        self.rank = [0] * n
    
    def find(self, x):
        """查找x的根节点（带路径压缩优化）"""
        if self.parent[x] != x:
            # 路径压缩：将路径上的所有节点直接连接到根节点
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x, y):
        """合并x和y所在的集合（按秩合并优化）"""
        root_x = self.find(x)
        root_y = self.find(y)
        
        # 如果已经在同一集合中，返回False
        if root_x == root_y:
            return False
        
        # 按秩合并：将秩小的树合并到秩大的树下
        if self.rank[root_x] < self.rank[root_y]:
            self.parent[root_x] = root_y
        elif self.rank[root_x] > self.rank[root_y]:
            self.parent[root_y] = root_x
        else:
            self.parent[root_y] = root_x
            self.rank[root_x] += 1
        
        return True


def minCostConnectPoints(points):
    """
    计算连接所有点的最小费用
    
    Args:
        points: 点坐标列表，每个元素为 [x, y]
    
    Returns:
        连接所有点的最小总费用
    """
    n = len(points)
    
    # 如果只有一个点，不需要连接
    if n <= 1:
        return 0
    
    # 构造所有边，每个元素为 [点1索引, 点2索引, 曼哈顿距离]
    edges = []
    for i in range(n):
        for j in range(i + 1, n):
            dist = abs(points[i][0] - points[j][0]) + abs(points[i][1] - points[j][1])
            edges.append([i, j, dist])
    
    # 按权重升序排序所有边
    edges.sort(key=lambda x: x[2])
    
    # 初始化并查集
    uf = UnionFind(n)
    
    total_cost = 0
    edges_used = 0
    
    # 遍历所有边
    for point1, point2, cost in edges:
        # 如果两个点不在同一集合中，说明连接它们不会形成环
        if uf.union(point1, point2):
            total_cost += cost
            edges_used += 1
            
            # 如果已经选择了n-1条边，则已形成最小生成树
            if edges_used == n - 1:
                return total_cost
    
    return total_cost


# 测试用例
if __name__ == "__main__":
    # 测试用例1
    points1 = [[0, 0], [2, 2], [3, 10], [5, 2], [7, 0]]
    print("测试用例1结果:", minCostConnectPoints(points1))  # 预期输出: 20
    
    # 测试用例2
    points2 = [[3, 12], [-2, 5], [-4, 1]]
    print("测试用例2结果:", minCostConnectPoints(points2))  # 预期输出: 18
    
    # 测试用例3
    points3 = [[0, 0], [1, 1], [1, 0], [-1, 1]]
    print("测试用例3结果:", minCostConnectPoints(points3))  # 预期输出: 4