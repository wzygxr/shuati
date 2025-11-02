# Problem: SPOJ COT - Count on a tree
# Link: https://www.spoj.com/problems/COT/
# Description: 给定一棵树，每个节点有一个权值，每次查询路径(u,v)上第k小的权值
# Solution: 使用树上主席树解决树上路径第k小问题
# Time Complexity: O(nlogn) for preprocessing, O(logn) for each query
# Space Complexity: O(nlogn)

import sys
from collections import defaultdict

class Node:
    def __init__(self):
        self.l = 0
        self.r = 0
        self.sum = 0

class PersistentSegmentTree:
    def __init__(self, maxn=100005):
        self.MAXN = maxn
        self.T = [Node() for _ in range(40 * maxn)]
        self.root = [0] * maxn
        self.cnt = 0
    
    def create_node(self, l=0, r=0, sum=0):
        self.cnt += 1
        self.T[self.cnt].l = l
        self.T[self.cnt].r = r
        self.T[self.cnt].sum = sum
        return self.cnt
    
    def insert(self, pre, l, r, val):
        now = self.create_node()
        self.T[now].sum = self.T[pre].sum + 1
        
        if l == r:
            return now
        
        mid = (l + r) >> 1
        if val <= mid:
            self.T[now].l = self.insert(self.T[pre].l, l, mid, val)
            self.T[now].r = self.T[pre].r
        else:
            self.T[now].l = self.T[pre].l
            self.T[now].r = self.insert(self.T[pre].r, mid + 1, r, val)
        return now
    
    def query(self, u, v, lca, flca, l, r, k):
        if l == r:
            return l
        
        mid = (l + r) >> 1
        x = self.T[self.T[u].l].sum + self.T[self.T[v].l].sum - self.T[self.T[lca].l].sum - self.T[self.T[flca].l].sum
        
        if k <= x:
            return self.query(self.T[u].l, self.T[v].l, self.T[lca].l, self.T[flca].l, l, mid, k)
        else:
            return self.query(self.T[u].r, self.T[v].r, self.T[lca].r, self.T[flca].r, mid + 1, r, k - x)

class TreeSolution:
    def __init__(self, n):
        self.n = n
        self.a = [0] * (n + 1)
        self.b = [0] * (n + 1)
        self.G = defaultdict(list)
        self.dep = [0] * (n + 1)
        self.fa = [0] * (n + 1)
        self.anc = [[0] * 20 for _ in range(n + 1)]
        self.pst = PersistentSegmentTree(n + 1)
    
    def add_edge(self, u, v):
        self.G[u].append(v)
        self.G[v].append(u)
    
    def getId(self, x):
        # 二分查找
        left, right = 1, self.n
        while left <= right:
            mid = (left + right) // 2
            if self.b[mid] == x:
                return mid
            elif self.b[mid] < x:
                left = mid + 1
            else:
                right = mid - 1
        return left
    
    def dfs(self, u, f, d):
        self.dep[u] = d
        self.fa[u] = f
        self.anc[u][0] = f
        
        # 倍增预处理
        i = 1
        while (1 << i) <= d:
            self.anc[u][i] = self.anc[self.anc[u][i-1]][i-1]
            i += 1
        
        # 在主席树中插入当前节点的权值
        self.pst.root[u] = self.pst.insert(self.pst.root[f], 1, self.n, self.getId(self.a[u]))
        
        # 递归处理子节点
        for v in self.G[u]:
            if v != f:
                self.dfs(v, u, d + 1)
    
    def lca(self, u, v):
        if self.dep[u] < self.dep[v]:
            u, v = v, u
        
        # 将u提升到和v同一深度
        for i in range(19, -1, -1):
            if self.dep[u] - (1 << i) >= self.dep[v]:
                u = self.anc[u][i]
        
        if u == v:
            return u
        
        # 同时提升u和v直到相遇
        for i in range(19, -1, -1):
            if self.anc[u][i] != self.anc[v][i]:
                u = self.anc[u][i]
                v = self.anc[v][i]
        
        return self.anc[u][0]

def main():
    line = sys.stdin.readline().split()
    n, m = int(line[0]), int(line[1])
    
    line = sys.stdin.readline().split()
    solution = TreeSolution(n)
    
    for i in range(1, n + 1):
        solution.a[i] = int(line[i - 1])
        solution.b[i] = solution.a[i]
    
    # 读取边
    for i in range(1, n):
        line = sys.stdin.readline().split()
        u, v = int(line[0]), int(line[1])
        solution.add_edge(u, v)
    
    # 离散化
    solution.b = solution.b[1:]  # 去掉索引0
    solution.b.sort()
    # 去重
    unique_b = []
    for x in solution.b:
        if not unique_b or unique_b[-1] != x:
            unique_b.append(x)
    solution.b = [0] + unique_b  # 加回索引0
    sz = len(solution.b) - 1
    
    # 重新定义getId方法以适应新的b数组
    def get_id(x):
        left, right = 1, len(solution.b) - 1
        while left <= right:
            mid = (left + right) // 2
            if solution.b[mid] == x:
                return mid
            elif solution.b[mid] < x:
                left = mid + 1
            else:
                right = mid - 1
        return left
    
    # 替换getId方法
    solution.getId = get_id
    
    # 构建主席树和预处理
    solution.pst.root[0] = solution.pst.create_node()
    solution.dfs(1, 0, 0)
    
    # 处理查询
    for _ in range(m):
        line = sys.stdin.readline().split()
        u, v, k = int(line[0]), int(line[1]), int(line[2])
        
        lcaNode = solution.lca(u, v)
        id = solution.pst.query(solution.pst.root[u], solution.pst.root[v], 
                               solution.pst.root[lcaNode], solution.pst.root[solution.fa[lcaNode]], 
                               1, sz, k)
        print(solution.b[id])

if __name__ == "__main__":
    main()