# LeetCode 1135. Connecting Cities With Minimum Cost
# 题目链接: https://leetcode.cn/problems/connecting-cities-with-minimum-cost/
# 
# 题目描述:
# 有 n 个城市，从 1 到 n 进行编号。给定一个 roads 数组，其中 roads[i] = [ai, bi, costi] 表示城市 ai 和 bi 之间建有一条成本为 costi 的双向道路。
# 如果所有城市之间都能通过这些道路相互到达，则返回连接所有城市的最小成本；否则返回 -1。
#
# 解题思路:
# 这是一个典型的最小生成树问题。使用Kruskal算法：
# 1. 将所有边按权重升序排序
# 2. 使用并查集判断添加边是否会形成环
# 3. 依次选择不形成环的最小边，直到选择了n-1条边或遍历完所有边
# 4. 如果最终选择了n-1条边，则返回总成本；否则返回-1
#
# 时间复杂度: O(E * log E)，其中E是边数，主要是排序的时间复杂度
# 空间复杂度: O(V)，其中V是顶点数，用于并查集存储
# 是否为最优解: 是，这是解决最小生成树问题的经典方法


class UnionFind:
    """并查集数据结构实现"""
    
    def __init__(self, n):
        """初始化并查集"""
        # parent[i] 表示节点i的父节点
        self.parent = list(range(n + 1))  # 城市编号从1开始
        # rank[i] 表示以i为根的树的秩（近似高度）
        self.rank = [0] * (n + 1)
    
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


def minimumCost(n, connections):
    """
    计算连接所有城市的最小成本
    
    Args:
        n: 城市数量
        connections: 道路连接信息，每个元素为 [城市1, 城市2, 成本]
    
    Returns:
        连接所有城市的最小成本，如果无法连接所有城市则返回-1
    """
    # 按权重升序排序所有边
    connections.sort(key=lambda x: x[2])
    
    # 初始化并查集
    uf = UnionFind(n)
    
    total_cost = 0
    edges_used = 0
    
    # 遍历所有边
    for city1, city2, cost in connections:
        # 如果两个城市不在同一集合中，说明连接它们不会形成环
        if uf.union(city1, city2):
            total_cost += cost
            edges_used += 1
            
            # 如果已经选择了n-1条边，则已形成最小生成树
            if edges_used == n - 1:
                return total_cost
    
    # 如果无法连接所有城市，返回-1
    return -1


# 测试用例
if __name__ == "__main__":
    # 测试用例1
    connections1 = [[1, 2, 5], [1, 3, 6], [2, 3, 1]]
    print("测试用例1结果:", minimumCost(3, connections1))  # 预期输出: 6
    
    # 测试用例2
    connections2 = [[1, 2, 3], [3, 4, 4]]
    print("测试用例2结果:", minimumCost(4, connections2))  # 预期输出: -1