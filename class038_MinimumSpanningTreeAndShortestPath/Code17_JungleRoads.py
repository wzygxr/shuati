# POJ 1251. Jungle Roads
# 题目链接: http://poj.org/problem?id=1251
# 
# 题目描述:
# 热带岛屿上的村庄之间需要修建道路。每个村庄用大写字母表示。
# 输入给出每个村庄到其他村庄的道路修建成本。
# 求连接所有村庄的最小成本。
#
# 解题思路:
# 标准的最小生成树问题：
# 1. 将村庄看作图中的节点
# 2. 将道路修建成本看作边的权重
# 3. 使用Kruskal或Prim算法计算最小生成树
#
# 时间复杂度: O(E log E)，其中E是边数
# 空间复杂度: O(V + E)，其中V是顶点数
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

def jungle_roads(n, edges):
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
    while True:
        try:
            n = int(input().strip())
            if n == 0:
                break
            
            edges = []
            
            for _ in range(n - 1):
                data = input().split()
                village = data[0]
                k = int(data[1])
                
                u = ord(village) - ord('A')
                
                ptr = 2
                for _ in range(k):
                    neighbor = data[ptr]
                    cost = int(data[ptr + 1])
                    ptr += 2
                    
                    v = ord(neighbor) - ord('A')
                    edges.append((u, v, cost))
            
            result = jungle_roads(n, edges)
            print(result)
            
        except EOFError:
            break

if __name__ == "__main__":
    main()