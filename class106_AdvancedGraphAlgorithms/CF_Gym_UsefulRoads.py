#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Codeforces Gym - Useful Roads 解决方案

题目链接: https://codeforces.com/gym/100513/problem/L
题目描述: 给定一个有向图和一些指定路径，找出在所有指定路径中都使用的边（有用的边）
解题思路: 构建支配树和后支配树，判断边是否在所有路径中都被使用

时间复杂度: O((V+E)log(V+E))
空间复杂度: O(V+E)
"""

import sys
from collections import defaultdict

class DominatorTree:
    """
    支配树实现类
    """
    
    def __init__(self, n, root):
        """
        初始化支配树
        
        Args:
            n (int): 节点数量
            root (int): 根节点
        """
        self.n = n
        self.root = root
        self.graph = defaultdict(list)
        self.reverse_graph = defaultdict(list)
        self.dfn = [-1] * n
        self.id = [0] * n
        self.fa = [0] * n
        self.semi = [-1] * n
        self.idom = [-1] * n
        self.best = list(range(n))
        self.dfs_clock = 0
        self.bucket = defaultdict(list)
        self.tree = defaultdict(list)
    
    def add_edge(self, u, v):
        """
        添加有向边
        
        Args:
            u (int): 起点
            v (int): 终点
        """
        self.graph[u].append(v)
        self.reverse_graph[v].append(u)
    
    def dfs(self, u):
        """
        DFS遍历，构建DFS树
        
        Args:
            u (int): 当前节点
        """
        self.dfn[u] = self.dfs_clock
        self.id[self.dfs_clock] = u
        self.dfs_clock += 1
        
        for v in self.graph[u]:
            if self.dfn[v] == -1:
                self.fa[v] = u
                self.dfs(v)
    
    def find(self, x):
        """
        并查集查找操作（带路径压缩）
        
        Args:
            x (int): 节点
            
        Returns:
            int: 根节点
        """
        if x == self.fa[x]:
            return x
        
        root = self.find(self.fa[x])
        
        # 路径压缩优化
        if self.semi[self.best[self.fa[x]]] < self.semi[self.best[x]]:
            self.best[x] = self.best[self.fa[x]]
        
        self.fa[x] = root
        return root
    
    def build(self):
        """
        构建支配树
        """
        # 1. DFS遍历，构建DFS树
        self.dfs(self.root)
        
        # 2. 从后向前处理每个节点
        for i in range(self.dfs_clock - 1, -1, -1):
            u = self.id[i]
            
            # 计算半支配点
            for v in self.reverse_graph[u]:
                if self.dfn[v] == -1:
                    continue  # 节点v不在DFS树中
                
                if self.dfn[v] < self.dfn[u]:
                    # v是u的祖先
                    if self.semi[u] == -1 or self.dfn[v] < self.semi[u]:
                        self.semi[u] = self.dfn[v]
                else:
                    # v是u的后代，通过并查集找到v的祖先
                    self.find(v)
                    if self.semi[u] == -1 or self.semi[self.best[v]] < self.semi[u]:
                        self.semi[u] = self.semi[self.best[v]]
            
            if i > 0:
                self.bucket[self.id[self.semi[u]]].append(u)
                
                # 处理bucket中的节点
                w = self.fa[u]
                for v in self.bucket[w]:
                    self.find(v)
                    if self.semi[self.best[v]] == self.semi[v]:
                        self.idom[v] = w
                    else:
                        self.idom[v] = self.best[v]
                
                self.bucket[w].clear()
        
        # 3. 确定立即支配点
        for i in range(1, self.dfs_clock):
            u = self.id[i]
            if self.idom[u] != self.id[self.semi[u]]:
                self.idom[u] = self.idom[self.idom[u]]
        
        # 4. 构建支配树
        for i in range(1, self.dfs_clock):
            u = self.id[i]
            self.tree[self.idom[u]].append(u)
    
    def dominates(self, u, v):
        """
        检查节点u是否支配节点v
        
        Args:
            u (int): 可能的支配节点
            v (int): 被支配节点
            
        Returns:
            bool: 是否支配
        """
        if self.dfn[u] == -1 or self.dfn[v] == -1:
            return False
        
        current = v
        while current != self.root and current != -1:
            if current == u:
                return True
            current = self.idom[current]
        
        return current == u

class PostDominatorTree:
    """
    后支配树实现类
    """
    
    def __init__(self, n, root):
        """
        初始化后支配树
        
        Args:
            n (int): 节点数量
            root (int): 根节点
        """
        self.n = n
        self.root = root
        self.graph = defaultdict(list)
        self.reverse_graph = defaultdict(list)
        self.dfn = [-1] * n
        self.id = [0] * n
        self.fa = [0] * n
        self.semi = [-1] * n
        self.idom = [-1] * n
        self.best = list(range(n))
        self.dfs_clock = 0
        self.bucket = defaultdict(list)
        self.tree = defaultdict(list)
    
    def add_edge(self, u, v):
        """
        添加有向边（注意方向是反的）
        
        Args:
            u (int): 起点
            v (int): 终点
        """
        self.reverse_graph[u].append(v)  # 反转边的方向
        self.graph[v].append(u)
    
    def dfs(self, u):
        """
        DFS遍历，构建DFS树
        
        Args:
            u (int): 当前节点
        """
        self.dfn[u] = self.dfs_clock
        self.id[self.dfs_clock] = u
        self.dfs_clock += 1
        
        for v in self.graph[u]:
            if self.dfn[v] == -1:
                self.fa[v] = u
                self.dfs(v)
    
    def find(self, x):
        """
        并查集查找操作（带路径压缩）
        
        Args:
            x (int): 节点
            
        Returns:
            int: 根节点
        """
        if x == self.fa[x]:
            return x
        
        root = self.find(self.fa[x])
        
        # 路径压缩优化
        if self.semi[self.best[self.fa[x]]] < self.semi[self.best[x]]:
            self.best[x] = self.best[self.fa[x]]
        
        self.fa[x] = root
        return root
    
    def build(self):
        """
        构建后支配树
        """
        # 1. DFS遍历，构建DFS树
        self.dfs(self.root)
        
        # 2. 从后向前处理每个节点
        for i in range(self.dfs_clock - 1, -1, -1):
            u = self.id[i]
            
            # 计算半支配点
            for v in self.reverse_graph[u]:
                if self.dfn[v] == -1:
                    continue  # 节点v不在DFS树中
                
                if self.dfn[v] < self.dfn[u]:
                    # v是u的祖先
                    if self.semi[u] == -1 or self.dfn[v] < self.semi[u]:
                        self.semi[u] = self.dfn[v]
                else:
                    # v是u的后代，通过并查集找到v的祖先
                    self.find(v)
                    if self.semi[u] == -1 or self.semi[self.best[v]] < self.semi[u]:
                        self.semi[u] = self.semi[self.best[v]]
            
            if i > 0:
                self.bucket[self.id[self.semi[u]]].append(u)
                
                # 处理bucket中的节点
                w = self.fa[u]
                for v in self.bucket[w]:
                    self.find(v)
                    if self.semi[self.best[v]] == self.semi[v]:
                        self.idom[v] = w
                    else:
                        self.idom[v] = self.best[v]
                
                self.bucket[w].clear()
        
        # 3. 确定立即支配点
        for i in range(1, self.dfs_clock):
            u = self.id[i]
            if self.idom[u] != self.id[self.semi[u]]:
                self.idom[u] = self.idom[self.idom[u]]
        
        # 4. 构建后支配树
        for i in range(1, self.dfs_clock):
            u = self.id[i]
            self.tree[self.idom[u]].append(u)
    
    def post_dominates(self, u, v):
        """
        检查节点u是否后支配节点v
        
        Args:
            u (int): 可能的后支配节点
            v (int): 被后支配节点
            
        Returns:
            bool: 是否后支配
        """
        if self.dfn[u] == -1 or self.dfn[v] == -1:
            return False
        
        current = v
        while current != self.root and current != -1:
            if current == u:
                return True
            current = self.idom[current]
        
        return current == u

def main():
    """
    主函数
    """
    # 读取输入
    line = input().split()
    n = int(line[0])
    m = int(line[1])
    
    # 存储边的信息
    edges = []
    graph = defaultdict(list)
    
    for i in range(m):
        line = input().split()
        u = int(line[0]) - 1  # 转换为0-based索引
        v = int(line[1]) - 1
        edges.append((u, v))
        graph[u].append(v)
    
    line = input().split()
    k = int(line[0])
    
    paths = []
    for i in range(k):
        line = input().split()
        s = int(line[0]) - 1  # 转换为0-based索引
        t = int(line[1]) - 1
        paths.append((s, t))
    
    # 构建支配树和后支配树
    dt = DominatorTree(n, 0)
    pdt = PostDominatorTree(n, n - 1)
    
    for u, v in edges:
        dt.add_edge(u, v)
        pdt.add_edge(u, v)
    
    dt.build()
    pdt.build()
    
    # 判断每条边是否为有用的边
    useful_edges = []
    for i, (u, v) in enumerate(edges):
        is_useful = True
        for s, t in paths:
            # 检查边(u,v)是否在从s到t的所有路径上
            # 这等价于s支配u且v后支配t
            if not dt.dominates(s, u) or not pdt.post_dominates(v, t):
                is_useful = False
                break
        
        if is_useful:
            useful_edges.append(i + 1)  # 转换为1-based索引
    
    # 输出结果
    print(len(useful_edges))
    if useful_edges:
        print(' '.join(map(str, useful_edges)))

if __name__ == "__main__":
    main()