# POJ 3522. Slim Span
# 题目链接: http://poj.org/problem?id=3522
# 
# 题目描述:
# 给定一个无向图，定义生成树的"苗条度"为最大边权与最小边权的差值。
# 求所有生成树中苗条度的最小值。
#
# 解题思路:
# 1. 枚举最小边，然后使用Kruskal算法构建包含该边的最小生成树
# 2. 记录每次生成树的最大边权，计算苗条度
# 3. 取所有可能苗条度中的最小值
#
# 时间复杂度: O(E^2 * α(V))，其中E是边数，V是顶点数
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

def slim_span(n, edges):
    edges.sort(key=lambda x: x[2])
    min_slim = float('inf')
    m = len(edges)
    
    # 枚举最小边
    for i in range(m):
        uf = UnionFind(n)
        edge_count = 0
        max_weight = edges[i][2]
        min_weight = edges[i][2]
        
        # 从第i条边开始构建生成树
        for j in range(i, m):
            u, v, w = edges[j]
            if uf.union(u, v):
                edge_count += 1
                max_weight = max(max_weight, w)
                
                if edge_count == n - 1:
                    min_slim = min(min_slim, max_weight - min_weight)
                    break
    
    return -1 if min_slim == float('inf') else min_slim

def main():
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    
    while True:
        n = int(input[ptr]); ptr += 1
        m = int(input[ptr]); ptr += 1
        
        if n == 0 and m == 0:
            break
        
        edges = []
        for _ in range(m):
            u = int(input[ptr]) - 1  # 转换为0-based索引
            ptr += 1
            v = int(input[ptr]) - 1
            ptr += 1
            w = int(input[ptr])
            ptr += 1
            edges.append((u, v, w))
        
        result = slim_span(n, edges)
        print(result)

if __name__ == "__main__":
    main()