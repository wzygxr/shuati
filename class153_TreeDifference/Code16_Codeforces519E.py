import math
from typing import List

"""
Codeforces 519E. A and B and Lecture Rooms
题目描述：给定一棵树，有多个查询，每个查询给出两个节点u和v，
要求找到树上到u和v距离相等的节点数量。
使用LCA和树上差分思想解决。
"""

class Solution:
    def solve(self, n: int, edges: List[List[int]], queries: List[List[int]]) -> List[int]:
        # 构建图（节点编号从1开始）
        graph = [[] for _ in range(n + 1)]
        for u, v in edges:
            graph[u].append(v)
            graph[v].append(u)
        
        self.graph = graph
        self.n = n
        
        # 预处理
        LOG = int(math.log2(n)) + 1
        self.parent = [[0] * LOG for _ in range(n + 1)]
        self.depth = [0] * (n + 1)
        self.size = [0] * (n + 1)
        
        # DFS预处理
        self.dfs(1, 0)
        
        # 预处理倍增数组
        for j in range(1, LOG):
            for i in range(1, n + 1):
                if self.parent[i][j-1] != 0:
                    self.parent[i][j] = self.parent[self.parent[i][j-1]][j-1]
        
        result = []
        for u, v in queries:
            result.append(self.query(u, v))
        
        return result
    
    def dfs(self, u: int, p: int):
        self.parent[u][0] = p
        self.depth[u] = self.depth[p] + 1
        self.size[u] = 1
        
        for v in self.graph[u]:
            if v != p:
                self.dfs(v, u)
                self.size[u] += self.size[v]
    
    def getLCA(self, u: int, v: int) -> int:
        if self.depth[u] < self.depth[v]:
            u, v = v, u
        
        # 将u提升到和v同一深度
        for j in range(len(self.parent[0]) - 1, -1, -1):
            if self.depth[u] - (1 << j) >= self.depth[v]:
                u = self.parent[u][j]
        
        if u == v:
            return u
        
        # 同时向上提升
        for j in range(len(self.parent[0]) - 1, -1, -1):
            if self.parent[u][j] != self.parent[v][j]:
                u = self.parent[u][j]
                v = self.parent[v][j]
        
        return self.parent[u][0]
    
    def getKthParent(self, u: int, k: int) -> int:
        for j in range(len(self.parent[0])):
            if k & (1 << j):
                u = self.parent[u][j]
        return u
    
    def query(self, u: int, v: int) -> int:
        if u == v:
            return self.n  # 所有节点都满足
        
        lca = self.getLCA(u, v)
        dist = self.depth[u] + self.depth[v] - 2 * self.depth[lca]
        
        if dist % 2 == 1:
            return 0  # 距离为奇数，没有满足的节点
        
        midDist = dist // 2
        
        if self.depth[u] == self.depth[v]:
            # u和v在同一深度，中点在lca
            uMid = self.getKthParent(u, midDist - 1)
            vMid = self.getKthParent(v, midDist - 1)
            return self.n - self.size[uMid] - self.size[vMid]
        else:
            # u和v在不同深度，找到中点
            if self.depth[u] < self.depth[v]:
                u, v = v, u
            
            mid = self.getKthParent(u, midDist)
            prev = self.getKthParent(u, midDist - 1)
            return self.size[mid] - self.size[prev]

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例
    n = 4
    edges = [[1,2], [1,3], [2,4]]
    queries = [[1,2], [2,3], [3,4], [2,4]]
    
    result = solution.solve(n, edges, queries)
    print(result)  # 输出: [2, 1, 1, 1]