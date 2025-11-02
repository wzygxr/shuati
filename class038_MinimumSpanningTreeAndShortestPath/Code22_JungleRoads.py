# POJ 1251 Jungle Roads
# 题目链接: http://poj.org/problem?id=1251
# 
# 题目描述:
# 在遥远的热带雨林中，有n个村庄，编号从A到Z（最多26个村庄）。
# 一些村庄之间有道路连接，但这些道路可能需要重建。
# 你的任务是重建一些道路，使得所有村庄都连通，并且重建成本最小。
#
# 输入格式:
# 每个测试用例以整数n（1<n<27）开始，表示村庄数量。
# 接下来n-1行描述每个村庄可以连接的道路：
# 第一行描述村庄A可以连接的道路，第二行描述村庄B可以连接的道路，以此类推。
# 每行的格式为：村庄名 道路数 目标村庄1 成本1 目标村庄2 成本2 ...
# 
# 解题思路:
# 这是一个标准的最小生成树问题。我们需要：
# 1. 将输入的村庄和道路信息转换为图的表示
# 2. 使用Kruskal或Prim算法计算最小生成树
# 3. 返回MST的总权重
#
# 时间复杂度: O(E * log E)，其中E是边数
# 空间复杂度: O(V + E)，其中V是顶点数，E是边数
# 是否为最优解: 是，这是解决该问题的标准方法
# 工程化考量:
# 1. 异常处理: 检查输入参数的有效性
# 2. 边界条件: 处理少于2个村庄的情况
# 3. 内存管理: 使用列表存储边信息
# 4. 性能优化: 并查集的路径压缩和按秩合并优化

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

def jungle_roads(n, edges):
    # 特殊情况：只有一个村庄
    if n == 1:
        return 0
    
    # 按权重排序
    edges.sort(key=lambda x: x[2])
    
    # 使用并查集构建MST
    uf = UnionFind(n)
    total_cost = 0
    edges_used = 0
    
    for u, v, weight in edges:
        if uf.union(u, v):
            total_cost += weight
            edges_used += 1
            # MST完成
            if edges_used == n - 1:
                break
    
    return total_cost

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    # 输入：
    # 9
    # A 2 B 12 I 25
    # B 3 C 10 H 40 I 8
    # C 2 D 20 G 55
    # D 1 E 44
    # E 2 F 60 G 38
    # F 0
    # G 1 H 35
    # H 1 I 35
    #
    # 构建边列表
    edges1 = []
    # A-B:12, A-I:25
    edges1.append((0, 1, 12))
    edges1.append((0, 8, 25))
    # B-C:10, B-H:40, B-I:8
    edges1.append((1, 2, 10))
    edges1.append((1, 7, 40))
    edges1.append((1, 8, 8))
    # C-D:20, C-G:55
    edges1.append((2, 3, 20))
    edges1.append((2, 6, 55))
    # D-E:44
    edges1.append((3, 4, 44))
    # E-F:60, E-G:38
    edges1.append((4, 5, 60))
    edges1.append((4, 6, 38))
    # G-H:35
    edges1.append((6, 7, 35))
    # H-I:35
    edges1.append((7, 8, 35))
    
    result1 = jungle_roads(9, edges1)
    print("测试用例1结果:", result1)  # 预期输出: 216