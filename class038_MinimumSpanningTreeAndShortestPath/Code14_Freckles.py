# UVa 10034 Freckles
# 题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=12&page=show_problem&problem=975
# 
# 题目描述:
# 在你的脸上有一些雀斑，你可以把它们看作是二维平面上的一些点。
# 你的任务是用一些直线将这些点连接起来，使得：
# 1. 每个点都至少被一条直线连接
# 2. 任意两个点之间都存在一条路径（直接或间接）
# 3. 使用的直线总长度最小
#
# 解题思路:
# 这是一个标准的最小生成树问题：
# 1. 将每个雀斑看作图中的一个节点
# 2. 任意两个雀斑之间都有一条边，权重为它们之间的欧几里得距离
# 3. 求这个完全图的最小生成树
# 4. 返回MST中所有边的权重之和
#
# 我们使用Kruskal算法：
# 1. 计算所有点对之间的距离，构造成边
# 2. 将所有边按权重升序排序
# 3. 使用并查集判断添加边是否会形成环
# 4. 依次选择不形成环的最小边，直到选择了n-1条边
#
# 时间复杂度: O(N^2 * log(N))，其中N是点的数量
# 空间复杂度: O(N^2)，用于存储所有边
# 是否为最优解: 是，这是解决该问题的标准方法

import math

class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n))
        self.rank = [0] * n
    
    def find(self, x):
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])  # 路径压缩
        return self.parent[x]
    
    def union(self, x, y):
        root_x = self.find(x)
        root_y = self.find(y)
        
        if root_x == root_y:
            return False
        
        # 按秩合并
        if self.rank[root_x] < self.rank[root_y]:
            root_x, root_y = root_y, root_x
        self.parent[root_y] = root_x
        if self.rank[root_x] == self.rank[root_y]:
            self.rank[root_x] += 1
        return True

def freckles(points):
    n = len(points)
    
    # 如果只有一个点，不需要连接
    if n <= 1:
        return 0.0
    
    # 计算两点间距离
    def distance(p1, p2):
        dx = p1[0] - p2[0]
        dy = p1[1] - p2[1]
        return math.sqrt(dx * dx + dy * dy)
    
    # 构造所有边
    edges = []
    for i in range(n):
        for j in range(i + 1, n):
            dist = distance(points[i], points[j])
            edges.append((i, j, dist))
    
    # 按权重升序排序所有边
    edges.sort(key=lambda x: x[2])
    
    # 初始化并查集
    uf = UnionFind(n)
    
    total_cost = 0.0
    edges_used = 0
    
    # 遍历所有边
    for u, v, weight in edges:
        # 如果两个点不在同一集合中，说明连接它们不会形成环
        if uf.union(u, v):
            total_cost += weight
            edges_used += 1
            
            # 如果已经选择了n-1条边，则已形成最小生成树
            if edges_used == n - 1:
                break
    
    return total_cost

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    points1 = [
        [1.0, 1.0],
        [2.0, 2.0],
        [2.0, 4.0]
    ]
    print("测试用例1结果: {:.2f}".format(freckles(points1)))  # 预期输出: 3.41
    
    # 测试用例2
    points2 = [
        [1.0, 1.0],
        [2.0, 2.0],
        [3.0, 3.0],
        [4.0, 4.0]
    ]
    print("测试用例2结果: {:.2f}".format(freckles(points2)))  # 预期输出: 4.24