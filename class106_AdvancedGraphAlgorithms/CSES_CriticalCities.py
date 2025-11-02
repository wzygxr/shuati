#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
CSES - Critical Cities 解决方案

题目链接: https://cses.fi/problemset/task/1703
题目描述: 给定一个有向图，找出从节点1到节点n的所有路径上都必须经过的城市（关键城市）
解题思路: 构建支配树，从节点n向上追溯到根节点1的所有节点即为关键城市

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
    
    def get_critical_cities(self, target):
        """
        获取关键城市
        
        Args:
            target (int): 目标节点
            
        Returns:
            list: 关键城市列表（按升序排列）
        """
        result = []
        current = target
        
        while current != -1:
            result.append(current + 1)  # 转换为1-based索引
            current = self.idom[current]
        
        result.sort()
        return result

def main():
    """
    主函数
    """
    # 读取输入
    line = input().strip()
    # 处理可能的BOM字符
    if line.startswith('\ufeff'):
        line = line[1:]
    line = line.split()
    n = int(line[0])
    m = int(line[1])
    
    # 构建图
    dt = DominatorTree(n, 0)  # 节点0作为根节点(1-based转0-based)
    
    for _ in range(m):
        line = input().strip()
        # 处理可能的BOM字符
        if line.startswith('\ufeff'):
            line = line[1:]
        line = line.split()
        u = int(line[0]) - 1  # 转换为0-based索引
        v = int(line[1]) - 1
        dt.add_edge(u, v)
    
    # 构建支配树
    dt.build()
    
    # 获取关键城市
    critical_cities = dt.get_critical_cities(n - 1)  # n-1是目标节点(0-based)
    
    # 输出结果
    print(len(critical_cities))
    print(' '.join(map(str, critical_cities)))

if __name__ == "__main__":
    main()