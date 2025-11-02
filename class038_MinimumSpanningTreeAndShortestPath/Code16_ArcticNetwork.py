# UVa 10369. Arctic Network
# 题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=15&page=show_problem&problem=1310
# 
# 题目描述:
# 北极的哨所之间需要建立通信网络。有两种通信方式：
# 1. 无线电通信：距离不超过D
# 2. 卫星通信：不受距离限制，但只有S个卫星频道
# 求最小的D，使得所有哨所都能通信。
#
# 解题思路:
# 与无线通讯网类似的问题：
# 1. 构建完全图，边权为哨所之间的距离
# 2. 使用Kruskal算法计算最小生成树
# 3. 由于有S个卫星频道，可以省去S-1条最长的边
# 4. 最小生成树中第P-S大的边权就是答案
#
# 时间复杂度: O(P^2 * log P)，其中P是哨所数量
# 空间复杂度: O(P^2)
# 是否为最优解: 是，这是解决该问题的标准方法

import math

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

def distance(x1, y1, x2, y2):
    return math.sqrt((x1 - x2)**2 + (y1 - y2)**2)

def arctic_network(s, p, outposts):
    edges = []
    
    for i in range(p):
        for j in range(i + 1, p):
            dist = distance(outposts[i][0], outposts[i][1], outposts[j][0], outposts[j][1])
            edges.append((dist, i, j))
    
    edges.sort(key=lambda x: x[0])
    
    uf = UnionFind(p)
    mst_edges = []
    
    for dist, u, v in edges:
        if uf.union(u, v):
            mst_edges.append(dist)
    
    # 有S个卫星频道，可以省去S-1条最长的边
    return mst_edges[p - s - 1]

def main():
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    
    n = int(input[ptr]); ptr += 1
    
    for _ in range(n):
        s = int(input[ptr]); ptr += 1
        p = int(input[ptr]); ptr += 1
        
        outposts = []
        for _ in range(p):
            x = int(input[ptr]); ptr += 1
            y = int(input[ptr]); ptr += 1
            outposts.append((x, y))
        
        result = arctic_network(s, p, outposts)
        print(f"{result:.2f}")

if __name__ == "__main__":
    main()