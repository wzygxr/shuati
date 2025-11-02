# P1967 [NOIP2013 提高组] 货车运输 - Python实现
# 题目描述：
# A 国有 n 座城市，编号从 1 到 n，城市之间有 m 条双向道路。每一条道路对车辆都有重量限制，简称限重。
# 现在有 q 辆货车在运输货物，司机们想知道每辆车在不超过车辆限重的情况下，最多能运多重的货物。
#
# 输入格式：
# 第一行有两个用一个空格隔开的整数 n,m，表示 A 国有 n 座城市和 m 条道路。
# 接下来 m 行每行三个整数 x, y, z，每两个整数之间用一个空格隔开，表示从 x 号城市到 y 号城市有一条限重为 z 的道路。
# 注意：x ≠ y，两座城市之间可能有多条道路。
# 接下来一行有一个整数 q，表示有 q 辆货车需要运货。
# 接下来 q 行，每行两个整数 x,y，之间用一个空格隔开，表示一辆货车需要从 x 城市运输货物到 y 城市，保证 x ≠ y。
#
# 输出格式：
# 共有 q 行，每行一个整数，表示对于每一辆货车，它的最大载重是多少。
# 如果货车不能到达目的地，输出 -1。
#
# 解题思路：
# 这是一个经典的Kruskal重构树应用问题。
# 要求路径上最小边权的最大值，可以转化为在最大生成树上求两点间路径上的最小边权。
# 使用Kruskal重构树的方法：
# 1. 按边权从大到小排序，构建最大生成树的Kruskal重构树
# 2. 重构树中，每个原始节点是叶子节点，内部节点代表边
# 3. 重构树满足小根堆性质（因为我们按从大到小排序构建）
# 4. 两点间路径的最小边权最大值等于它们在重构树上的LCA节点权值
#
# 时间复杂度分析：
# 1. 构建Kruskal重构树：O(m log m) - 主要是排序的复杂度
# 2. DFS预处理：O(n) - 每个节点访问一次
# 3. 每次查询：O(log n) - 倍增LCA的复杂度
# 总复杂度：O(m log m + q log n)
#
# 空间复杂度分析：
# 1. 存储边：O(m)
# 2. 存储图和重构树：O(n)
# 3. 倍增表：O(n log n)
# 总空间复杂度：O(n log n + m)

import sys
from typing import List, Tuple

# 常量定义
MAXN = 10001
MAXM = 50001
MAXH = 16

class Edge:
    def __init__(self, u: int, v: int, w: int):
        self.u = u
        self.v = v
        self.w = w

class UnionFind:
    def __init__(self, n: int):
        self.parent = list(range(n))
    
    def find(self, x: int) -> int:
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x: int, y: int) -> bool:
        px, py = self.find(x), self.find(y)
        if px != py:
            self.parent[px] = py
            return True
        return False

class Solution:
    def __init__(self):
        self.n = 0
        self.m = 0
        self.q = 0
        self.edges: List[Edge] = []
        self.father: List[int] = [0] * (MAXN * 2)
        self.head: List[int] = [0] * (MAXN * 2)
        self.next: List[int] = [0] * (MAXN * 2)
        self.to: List[int] = [0] * (MAXN * 2)
        self.cntg = 0
        self.nodeKey: List[int] = [0] * (MAXN * 2)
        self.cntu = 0
        self.dep: List[int] = [0] * (MAXN * 2)
        self.stjump: List[List[int]] = [[0] * MAXH for _ in range(MAXN * 2)]
        
    def addEdge(self, u: int, v: int) -> None:
        self.cntg += 1
        self.next[self.cntg] = self.head[u]
        self.to[self.cntg] = v
        self.head[u] = self.cntg
        
    def find(self, i: int) -> int:
        if i != self.father[i]:
            self.father[i] = self.find(self.father[i])
        return self.father[i]
        
    def kruskalRebuild(self) -> None:
        # 初始化并查集
        for i in range(1, self.n + 1):
            self.father[i] = i
            
        # 按边权从大到小排序
        self.edges.sort(key=lambda x: x.w, reverse=True)
        
        self.cntu = self.n
        for i in range(self.m):
            edge = self.edges[i]
            fx = self.find(edge.u)
            fy = self.find(edge.v)
            if fx != fy:
                # 合并两个连通分量
                self.father[fx] = self.father[fy] = self.cntu + 1
                self.cntu += 1
                self.father[self.cntu] = self.cntu
                # 新节点的权值为边权
                self.nodeKey[self.cntu] = edge.w
                # 建立父子关系
                self.addEdge(self.cntu, fx)
                self.addEdge(self.cntu, fy)
                
    def dfs(self, u: int, fa: int) -> None:
        self.dep[u] = self.dep[fa] + 1
        self.stjump[u][0] = fa
        
        # 构建倍增表
        for p in range(1, MAXH):
            self.stjump[u][p] = self.stjump[self.stjump[u][p - 1]][p - 1]
            
        # 递归处理子节点
        e = self.head[u]
        while e > 0:
            self.dfs(self.to[e], u)
            e = self.next[e]
            
    def lca(self, a: int, b: int) -> int:
        # 保证a在更深的位置
        if self.dep[a] < self.dep[b]:
            a, b = b, a
            
        # 将a提升到和b同一深度
        for p in range(MAXH - 1, -1, -1):
            if self.dep[self.stjump[a][p]] >= self.dep[b]:
                a = self.stjump[a][p]
                
        # 如果已经相遇，直接返回
        if a == b:
            return a
            
        # 同时向上提升，直到相遇
        for p in range(MAXH - 1, -1, -1):
            if self.stjump[a][p] != self.stjump[b][p]:
                a = self.stjump[a][p]
                b = self.stjump[b][p]
                
        # 返回LCA
        return self.stjump[a][0]
        
    def solve(self) -> None:
        # 读取输入
        line = sys.stdin.readline().split()
        self.n, self.m = int(line[0]), int(line[1])
        
        for _ in range(self.m):
            line = sys.stdin.readline().split()
            u, v, w = int(line[0]), int(line[1]), int(line[2])
            self.edges.append(Edge(u, v, w))
            
        # 构建Kruskal重构树
        self.kruskalRebuild()
        
        # 对每个连通分量进行DFS预处理
        for i in range(1, self.cntu + 1):
            if i == self.father[i]:
                self.dfs(i, 0)
                
        # 处理查询
        self.q = int(sys.stdin.readline())
        for _ in range(self.q):
            line = sys.stdin.readline().split()
            x, y = int(line[0]), int(line[1])
            
            # 如果两点不连通，输出-1
            if self.find(x) != self.find(y):
                print(-1)
            else:
                # 否则输出LCA节点的权值，即路径上最小边权的最大值
                print(self.nodeKey[self.lca(x, y)])

# 主函数
if __name__ == "__main__":
    solution = Solution()
    solution.solve()