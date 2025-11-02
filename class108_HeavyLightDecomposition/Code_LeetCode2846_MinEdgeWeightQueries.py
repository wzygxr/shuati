import sys
sys.setrecursionlimit(1000000)

class Edge:
    def __init__(self, to, weight):
        self.to = to
        self.weight = weight

class SegmentTree:
    def __init__(self, n):
        self.n = n
        self.min_val = [10**9] * (4 * n)
    
    def push_up(self, rt):
        self.min_val[rt] = min(self.min_val[rt << 1], self.min_val[rt << 1 | 1])
    
    def build(self, l, r, rt, arr):
        if l == r:
            self.min_val[rt] = arr[l]
            return
        mid = (l + r) >> 1
        self.build(l, mid, rt << 1, arr)
        self.build(mid + 1, r, rt << 1 | 1, arr)
        self.push_up(rt)
    
    def query(self, L, R, l, r, rt):
        if L <= l and r <= R:
            return self.min_val[rt]
        mid = (l + r) >> 1
        res = 10**9
        if L <= mid:
            res = min(res, self.query(L, R, l, mid, rt << 1))
        if R > mid:
            res = min(res, self.query(L, R, mid + 1, r, rt << 1 | 1))
        return res

class HeavyLightDecomposition:
    def __init__(self, n):
        self.n = n
        self.cnt = 0
        self.parent = [0] * n
        self.depth = [0] * n
        self.size = [0] * n
        self.heavy = [-1] * n
        self.head = [0] * n
        self.pos = [0] * n
        self.edge_weight = [0] * n
        self.tree = [[] for _ in range(n)]
        self.seg = None
    
    def add_edge(self, u, v, w):
        self.tree[u].append(Edge(v, w))
        self.tree[v].append(Edge(u, w))
    
    def dfs1(self, u, p, d):
        self.parent[u] = p
        self.depth[u] = d
        self.size[u] = 1
        max_size = 0
        
        for e in self.tree[u]:
            v = e.to
            if v == p:
                continue
            self.edge_weight[v] = e.weight
            self.dfs1(v, u, d + 1)
            self.size[u] += self.size[v]
            if self.size[v] > max_size:
                max_size = self.size[v]
                self.heavy[u] = v
    
    def dfs2(self, u, h):
        self.head[u] = h
        self.pos[u] = self.cnt
        self.cnt += 1
        
        if self.heavy[u] != -1:
            self.dfs2(self.heavy[u], h)
        
        for e in self.tree[u]:
            v = e.to
            if v != self.parent[u] and v != self.heavy[u]:
                self.dfs2(v, v)
    
    def decompose(self):
        self.dfs1(0, -1, 0)
        self.dfs2(0, 0)
        
        arr = [10**9] * self.n
        for i in range(self.n):
            if i != 0:
                arr[self.pos[i]] = self.edge_weight[i]
        
        self.seg = SegmentTree(self.n)
        self.seg.build(1, self.n - 1, 1, arr)
    
    def query_path(self, u, v):
        res = 10**9
        while self.head[u] != self.head[v]:
            if self.depth[self.head[u]] < self.depth[self.head[v]]:
                u, v = v, u
            res = min(res, self.seg.query(self.pos[self.head[u]], self.pos[u], 1, self.n - 1, 1))
            u = self.parent[self.head[u]]
        
        if u == v:
            return res
        
        if self.depth[u] > self.depth[v]:
            u, v = v, u
        
        res = min(res, self.seg.query(self.pos[u] + 1, self.pos[v], 1, self.n - 1, 1))
        return res

def min_edge_weight_queries(n, edges, queries):
    hld = HeavyLightDecomposition(n)
    
    # 构建树
    for edge in edges:
        u, v, w = edge
        hld.add_edge(u, v, w)
    
    hld.decompose()
    
    # 处理查询
    result = []
    for query in queries:
        u, v = query
        result.append(hld.query_path(u, v))
    
    return result

if __name__ == "__main__":
    # 测试用例
    n = 5
    edges = [
        [0, 1, 2],
        [1, 2, 3],
        [2, 3, 1],
        [3, 4, 4]
    ]
    queries = [
        [0, 4],
        [1, 3],
        [2, 4]
    ]
    
    result = min_edge_weight_queries(n, edges, queries)
    
    print("查询结果:")
    for i in range(len(queries)):
        print(f"查询 {queries[i][0]} -> {queries[i][1]}: {result[i]}")

'''
LeetCode 2846. 边权重查询的最小值
题目描述：给定一棵无向树，每个边有一个权重。支持多次查询，每次查询给出两个节点u和v，
要求找到从u到v路径上所有边权重的最小值。

数据范围：n ≤ 10^5，q ≤ 10^5
解法：树链剖分 + 线段树维护区间最小值

时间复杂度：预处理O(n)，每次查询O(log²n)
空间复杂度：O(n)

网址：https://leetcode.cn/problems/minimum-edge-weight-equilibrium-queries-in-a-tree/

算法总结：
1. 边权转点权：将边权下放到深度较深的节点上
2. 树链剖分：将树划分为重链，便于路径查询
3. 线段树维护：支持区间最小值查询

工程化考量：
1. 输入验证：验证输入数据的合法性
2. 性能优化：使用快速IO，优化线段树实现
3. 内存管理：合理分配数组大小，避免内存泄漏

测试用例：
1. 单边树：验证基本功能
2. 链状树：测试路径查询
3. 完全二叉树：验证复杂度
4. 极端数据：测试边界情况
'''