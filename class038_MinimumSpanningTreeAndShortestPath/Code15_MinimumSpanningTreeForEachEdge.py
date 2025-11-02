# Codeforces 609E. Minimum spanning tree for each edge
# 题目链接: https://codeforces.com/problemset/problem/609/E
# 
# 题目描述:
# 给定一个带权无向连通图，对于图中的每条边，计算包含该边的最小生成树的权值。
# 如果包含该边后图不连通，输出-1。
#
# 解题思路:
# 1. 首先计算原图的最小生成树MST
# 2. 对于每条边e：
#    - 如果e在MST中，那么包含e的最小生成树权值就是MST权值
#    - 如果e不在MST中，那么需要找到MST中连接e两端点的路径上的最大边权
#    - 用e替换这条最大边，得到的新生成树权值为MST权值 - 最大边权 + e的权值
# 3. 使用LCA和树上倍增算法快速查询任意两点间路径的最大边权
#
# 时间复杂度: O((n + m) log n)，其中n是顶点数，m是边数
# 空间复杂度: O(n log n)
# 是否为最优解: 是，这是解决该问题的标准方法

import sys
sys.setrecursionlimit(10**6)

class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n + 1))
        self.rank = [0] * (n + 1)
    
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

LOG = 20
tree = []
depth = []
up = []
max_edge = []

def dfs(u, p, w):
    depth[u] = depth[p] + 1
    up[u][0] = p
    max_edge[u][0] = w
    
    for i in range(1, LOG):
        up[u][i] = up[up[u][i-1]][i-1]
        max_edge[u][i] = max(max_edge[u][i-1], max_edge[up[u][i-1]][i-1])
    
    for v, weight in tree[u]:
        if v != p:
            dfs(v, u, weight)

def query_max_edge(u, v):
    if depth[u] < depth[v]:
        u, v = v, u
    
    max_w = 0
    
    # 将u提升到与v同一深度
    for i in range(LOG-1, -1, -1):
        if depth[u] - (1 << i) >= depth[v]:
            max_w = max(max_w, max_edge[u][i])
            u = up[u][i]
    
    if u == v:
        return max_w
    
    # 同时提升u和v直到它们的父节点相同
    for i in range(LOG-1, -1, -1):
        if up[u][i] != up[v][i]:
            max_w = max(max_w, max_edge[u][i])
            max_w = max(max_w, max_edge[v][i])
            u = up[u][i]
            v = up[v][i]
    
    max_w = max(max_w, max_edge[u][0])
    max_w = max(max_w, max_edge[v][0])
    
    return max_w

def main():
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    
    n = int(input[ptr]); ptr += 1
    m = int(input[ptr]); ptr += 1
    
    edges = []
    for i in range(m):
        u = int(input[ptr]); ptr += 1
        v = int(input[ptr]); ptr += 1
        w = int(input[ptr]); ptr += 1
        edges.append((u, v, w, i))
    
    # 按权重排序用于Kruskal
    sorted_edges = sorted(edges, key=lambda x: x[2])
    
    uf = UnionFind(n)
    mst_weight = 0
    in_mst = [False] * m
    
    global tree, depth, up, max_edge
    tree = [[] for _ in range(n + 1)]
    
    # 构建最小生成树
    for u, v, w, idx in sorted_edges:
        if uf.union(u, v):
            mst_weight += w
            in_mst[idx] = True
            tree[u].append((v, w))
            tree[v].append((u, w))
    
    # 初始化LCA数组
    depth = [0] * (n + 1)
    up = [[0] * LOG for _ in range(n + 1)]
    max_edge = [[0] * LOG for _ in range(n + 1)]
    
    depth[0] = -1
    dfs(1, 0, 0)
    
    # 处理每条边
    result = [0] * m
    for i in range(m):
        u, v, w, idx = edges[i]
        if in_mst[idx]:
            result[i] = mst_weight
        else:
            max_w = query_max_edge(u, v)
            result[i] = mst_weight - max_w + w
    
    for res in result:
        print(res)

if __name__ == "__main__":
    main()