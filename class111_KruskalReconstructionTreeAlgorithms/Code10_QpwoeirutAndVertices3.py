# Codeforces 1706E Qpwoeirut and Vertices - Python实现
# 题目描述：
# 给定一个包含n个节点和m条边的无向图，以及q个查询。
# 每个查询给出一个区间[l,r]，要求找出使得区间[l,r]内所有节点都连通的最少边数。
# 注意：这些边必须是原图中编号从1到某个值的连续边。
#
# 输入格式：
# 第一行包含一个整数t，表示测试用例数量。
# 每个测试用例的第一行包含三个整数n, m, q (2≤n≤10^5, 1≤m,q≤2⋅10^5)。
# 接下来m行，每行包含两个整数u, v (1≤u,v≤n, u≠v)，表示一条边。
# 接下来q行，每行包含两个整数l, r (1≤l≤r≤n)，表示一个查询。
#
# 输出格式：
# 对于每个查询，输出一个整数表示答案。
#
# 解题思路：
# 这是一道典型的Kruskal重构树应用题。
# 由于要求的是使得区间[l,r]内所有节点都连通的最少边数，我们可以将边按照编号排序，
# 然后构建Kruskal重构树。对于每个查询，我们需要找到包含[l,r]区间内所有节点的最小连通子树。
# 在Kruskal重构树中，这个子树的根节点对应的边编号就是答案。
#
# 算法步骤：
# 1. 构建Kruskal重构树，将边按照编号排序
# 2. 对于每个节点，记录它在重构树中的叶子节点
# 3. 对于每个查询[l,r]，找到包含这些节点的最小连通子树
# 4. 这可以通过找到这些节点在重构树中的LCA来实现
#
# 时间复杂度分析：
# 1. 构建Kruskal重构树：O(m log m)
# 2. DFS预处理：O(n)
# 3. 每次查询：O(log n)
# 总复杂度：O(m log m + q log n)
#
# 空间复杂度分析：
# 1. 存储边：O(m)
# 2. 存储图和重构树：O(n)
# 3. 倍增表：O(n log n)
# 总空间复杂度：O(n log n + m)

import sys
import threading

def main():
    # 增加递归深度限制
    sys.setrecursionlimit(1 << 25)
    
    class KruskalRebuildTree:
        def __init__(self, n, m):
            self.n = n
            self.m = m
            self.MAXN = 200001
            self.MAXH = 20
            
            # 每条边有三个信息，节点u、节点v、边编号i
            self.edge = [[0, 0, 0] for _ in range(self.MAXN)]
            
            # 并查集
            self.father = [0] * (self.MAXN * 2)
            
            # Kruskal重构树的建图
            self.head = [0] * (self.MAXN * 2)
            self.next = [0] * (self.MAXN * 2)
            self.to = [0] * (self.MAXN * 2)
            self.cntg = 0
            
            # Kruskal重构树上，节点的权值（边编号）
            self.nodeKey = [0] * (self.MAXN * 2)
            # Kruskal重构树上，点的数量
            self.cntu = 0
            
            # 每个原始节点在重构树中对应的叶子节点
            self.leaf = [0] * self.MAXN
            
            # Kruskal重构树上，dfs过程建立的信息
            self.dep = [0] * (self.MAXN * 2)
            self.stjump = [[0] * self.MAXH for _ in range(self.MAXN * 2)]
            self.dfn = [0] * (self.MAXN * 2)
            self.size = [0] * (self.MAXN * 2)
            self.dfntime = 0
        
        def find(self, i):
            if i != self.father[i]:
                self.father[i] = self.find(self.father[i])
            return self.father[i]
        
        def addEdge(self, u, v):
            self.cntg += 1
            self.next[self.cntg] = self.head[u]
            self.to[self.cntg] = v
            self.head[u] = self.cntg
        
        # 构建Kruskal重构树
        # 按边编号从小到大排序
        def kruskalRebuild(self):
            for i in range(1, self.n + 1):
                self.father[i] = i
            
            # 按边编号从小到大排序
            self.edge[1:self.m+1] = sorted(self.edge[1:self.m+1], key=lambda x: x[2])
            
            self.cntu = self.n
            for i in range(1, self.m + 1):
                fx = self.find(self.edge[i][0])
                fy = self.find(self.edge[i][1])
                if fx != fy:
                    # 合并两个连通分量
                    self.father[fx] = self.father[fy] = self.cntu + 1
                    self.cntu += 1
                    self.father[self.cntu] = self.cntu
                    # 新节点的权值为边编号
                    self.nodeKey[self.cntu] = self.edge[i][2]
                    # 建立父子关系
                    self.addEdge(self.cntu, fx)
                    self.addEdge(self.cntu, fy)
        
        # DFS预处理，构建倍增表
        def dfs(self, u, fa):
            self.dep[u] = self.dep[fa] + 1
            self.stjump[u][0] = fa
            self.dfn[u] = self.dfntime + 1
            self.dfntime += 1
            
            # 构建倍增表
            for p in range(1, self.MAXH):
                self.stjump[u][p] = self.stjump[self.stjump[u][p - 1]][p - 1]
            
            self.size[u] = 1 if u <= self.n else 0  # 叶子节点size为1
            # 递归处理子节点
            e = self.head[u]
            while e > 0:
                self.dfs(self.to[e], u)
                self.size[u] += self.size[self.to[e]]
                e = self.next[e]
        
        # 计算两点的最近公共祖先(LCA)
        def lca(self, a, b):
            # 保证a在更深的位置
            if self.dep[a] < self.dep[b]:
                a, b = b, a
            
            # 将a提升到和b同一深度
            for p in range(self.MAXH - 1, -1, -1):
                if self.dep[self.stjump[a][p]] >= self.dep[b]:
                    a = self.stjump[a][p]
            
            # 如果已经相遇，直接返回
            if a == b:
                return a
            
            # 同时向上提升，直到相遇
            for p in range(self.MAXH - 1, -1, -1):
                if self.stjump[a][p] != self.stjump[b][p]:
                    a = self.stjump[a][p]
                    b = self.stjump[b][p]
            
            # 返回LCA
            return self.stjump[a][0]
        
        # 找到包含[l,r]区间内所有节点的最小连通子树的根节点
        def findSubtreeRoot(self, l, r):
            # 特殊情况：只有一个节点
            if l == r:
                return self.leaf[l]
            
            # 找到[l,r]区间内节点的LCA
            root = self.leaf[l]
            for i in range(l + 1, r + 1):
                root = self.lca(root, self.leaf[i])
            
            return root
    
    def solve():
        t = int(input())
        
        for _ in range(t):
            n, m, q = map(int, input().split())
            
            # 创建Kruskal重构树实例
            krt = KruskalRebuildTree(n, m)
            
            # 初始化
            krt.cntg = 0
            krt.dfntime = 0
            
            for i in range(1, m + 1):
                u, v = map(int, input().split())
                krt.edge[i][0] = u
                krt.edge[i][1] = v
                krt.edge[i][2] = i  # 边编号就是i
            
            # 构建Kruskal重构树
            krt.kruskalRebuild()
            
            # 对每个连通分量进行DFS预处理
            for i in range(1, krt.cntu + 1):
                if i == krt.father[i]:
                    krt.dfs(i, 0)
            
            # 记录每个原始节点在重构树中对应的叶子节点
            for i in range(1, n + 1):
                krt.leaf[i] = i
            
            for _ in range(q):
                l, r = map(int, input().split())
                
                # 找到包含[l,r]区间内所有节点的最小连通子树的根节点
                root = krt.findSubtreeRoot(l, r)
                
                # 输出该根节点对应的边编号
                print(krt.nodeKey[root])
    
    solve()

# 使用多线程处理输入输出，避免Python的输入输出瓶颈
threading.Thread(target=main).start()