# POJ 2421. Constructing Roads
# 题目链接: http://poj.org/problem?id=2421
# 
# 题目描述:
# 有N个村庄，已知所有村庄之间的距离。
# 有些村庄之间已经存在道路。
# 求连接所有村庄的最小成本。
#
# 解题思路:
# 标准的最小生成树问题，但部分边已经存在：
# 1. 将已存在的边的权重设为0
# 2. 使用Kruskal算法计算最小生成树
# 3. 已存在的边会被优先选择（权重为0）
#
# 时间复杂度: O(E log E)，其中E是边数
# 空间复杂度: O(V + E)
# 是否为最优解: 是，这是解决该问题的标准方法

class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n))
        self.rank = [0] * n
    
    def find(self, x):
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x, y):
        root_x = self.find(x)
        root_y = self.find(y)
        if root_x == root_y:
            return False
        if self.rank[root_x] < self.rank[root_y]:
            root_x, root_y = root_y, root_x
        self.parent[root_y] = root_x
        if self.rank[root_x] == self.rank[root_y]:
            self.rank[root_x] += 1
        return True

def constructing_roads(n, dist, existing_roads):
    edges = []
    
    # 构建所有可能的边
    for i in range(n):
        for j in range(i + 1, n):
            edges.append((i, j, dist[i][j]))
    
    # 将已存在道路的权重设为0
    for road in existing_roads:
        u = road[0] - 1  # 转换为0-based索引
        v = road[1] - 1
        # 找到对应的边并设置权重为0
        for i in range(len(edges)):
            if (edges[i][0] == u and edges[i][1] == v) or (edges[i][0] == v and edges[i][1] == u):
                edges[i] = (u, v, 0)
                break
    
    edges.sort(key=lambda x: x[2])
    
    uf = UnionFind(n)
    total_cost = 0
    edges_used = 0
    
    for u, v, w in edges:
        if uf.union(u, v):
            total_cost += w
            edges_used += 1
            
            if edges_used == n - 1:
                break
    
    return total_cost

def main():
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    
    n = int(input[ptr]); ptr += 1
    dist = []
    
    for i in range(n):
        row = []
        for j in range(n):
            row.append(int(input[ptr])); ptr += 1
        dist.append(row)
    
    q = int(input[ptr]); ptr += 1
    existing_roads = []
    for _ in range(q):
        u = int(input[ptr]); ptr += 1
        v = int(input[ptr]); ptr += 1
        existing_roads.append((u, v))
    
    result = constructing_roads(n, dist, existing_roads)
    print(result)

if __name__ == "__main__":
    main()