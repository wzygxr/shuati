#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Tarjan离线算法实现最近公共祖先(LCA)查询
时间复杂度: O(n + q)，其中n是节点数，q是查询次数
空间复杂度: O(n + q)
"""

class TarjanOfflineLCA:
    def __init__(self, n, edges, queries):
        """
        初始化Tarjan离线LCA算法
        
        Args:
            n (int): 节点数量
            edges (list): 边集合，每个元素是[u, v]
            queries (list): 查询列表，每个元素是[u, v]
        """
        self.n = n
        self.q = len(queries)
        
        # 初始化图的邻接表
        self.graph = [[] for _ in range(n)]
        for u, v in edges:
            self.graph[u].append(v)
            self.graph[v].append(u)
        
        # 初始化查询数组
        self.queries = [[] for _ in range(n)]
        for i, (u, v) in enumerate(queries):
            self.queries[u].append((v, i))
            self.queries[v].append((u, i))
        
        # 初始化并查集和其他数组
        self.parent = list(range(n))  # 并查集父节点
        self.visited = [False] * n    # 访问标记
        self.ancestor = [i for i in range(n)]  # 祖先数组
        self.result = [0] * self.q    # 查询结果
    
    def find(self, x):
        """
        并查集的查找操作，带路径压缩
        
        Args:
            x (int): 要查找的节点
            
        Returns:
            int: x所在集合的根节点
        """
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x, y):
        """
        并查集的合并操作
        
        Args:
            x (int): 第一个节点
            y (int): 第二个节点
        """
        root_x = self.find(x)
        root_y = self.find(y)
        if root_x != root_y:
            self.parent[root_y] = root_x
    
    def tarjan(self, u, parent_u):
        """
        Tarjan算法的递归实现
        
        Args:
            u (int): 当前节点
            parent_u (int): u的父节点
        """
        self.visited[u] = True
        self.ancestor[u] = u
        
        # 遍历所有邻接点
        for v in self.graph[u]:
            if v != parent_u:  # 避免回到父节点
                self.tarjan(v, u)
                self.union(u, v)
                self.ancestor[self.find(u)] = u
        
        # 处理与u相关的查询
        for v, index in self.queries[u]:
            if self.visited[v]:
                self.result[index] = self.ancestor[self.find(v)]
    
    def solve(self):
        """
        执行查询并返回结果
        
        Returns:
            list: 查询结果数组
        """
        # 从根节点开始遍历（假设根节点是0）
        self.tarjan(0, -1)
        return self.result

# 测试代码
if __name__ == "__main__":
    # 测试用例
    n = 6
    edges = [
        [0, 1],
        [0, 2],
        [1, 3],
        [1, 4],
        [2, 5]
    ]
    
    queries = [
        [3, 4],
        [3, 5],
        [4, 5]
    ]
    
    tarjan_lca = TarjanOfflineLCA(n, edges, queries)
    results = tarjan_lca.solve()
    
    print("LCA查询结果：")
    for i in range(len(results)):
        u, v = queries[i]
        print(f"LCA({u}, {v}) = {results[i]}")