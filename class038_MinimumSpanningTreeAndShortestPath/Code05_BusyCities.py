# 洛谷P2330 [SCOI2005]繁忙的都市 - Kruskal算法实现
# 题目链接: https://www.luogu.com.cn/problem/P2330
# 
# 题目描述:
# 城市之间有许多道路，政府要修建一些道路，使得任何两个城市都可以互相到达，并且总长度最小。
# 但是，市政府希望知道在这个方案中，最大的道路长度是多少。
#
# 解题思路:
# 使用Kruskal算法构建最小生成树，在构建过程中记录使用的最大边权值
# 这实际上是最小生成树的一个性质：在保证总权值最小的情况下，最大的边权值也是最小的
#
# 时间复杂度: O(m * log m)，其中m是道路数
# 空间复杂度: O(n + m)，其中n是城市数
# 是否为最优解: 是，Kruskal算法是解决此类问题的标准方法

class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n + 1))  # 城市编号从1开始
        self.rank = [0] * (n + 1)
    
    def find(self, x):
        # 路径压缩优化
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x, y):
        fx = self.find(x)
        fy = self.find(y)
        if fx != fy:
            # 按秩合并优化
            if self.rank[fx] < self.rank[fy]:
                self.parent[fx] = fy
            else:
                self.parent[fy] = fx
                if self.rank[fx] == self.rank[fy]:
                    self.rank[fx] += 1
            return True
        return False

def busy_cities():
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])  # 城市数
    ptr += 1
    m = int(input[ptr])  # 道路数
    ptr += 1
    
    # 读取所有道路
    roads = []
    for _ in range(m):
        u = int(input[ptr])
        ptr += 1
        v = int(input[ptr])
        ptr += 1
        w = int(input[ptr])
        ptr += 1
        roads.append((w, u, v))
    
    # 按道路长度从小到大排序
    roads.sort()
    
    # 使用Kruskal算法构建最小生成树
    uf = UnionFind(n)
    max_edge = 0
    edge_count = 0
    
    for w, u, v in roads:
        if uf.union(u, v):
            max_edge = max(max_edge, w)
            edge_count += 1
            # 最小生成树有n-1条边
            if edge_count == n - 1:
                break
    
    # 输出结果：需要n-1条道路，最大的道路长度是max_edge
    print(edge_count, max_edge)

if __name__ == "__main__":
    busy_cities()