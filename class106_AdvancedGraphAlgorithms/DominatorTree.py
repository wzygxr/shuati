#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
支配树(Dominator Tree)实现 - Python版本

支配树是图论中的一个重要概念，主要用于程序分析和编译器优化等领域。
在有向图中，对于指定的源点s，如果从s到达节点w的所有路径都必须经过节点u，
则称节点u支配节点w。

本实现基于Lengauer-Tarjan算法，时间复杂度为O((V+E)log(V+E))

应用场景：
1. 编译器优化：控制流图分析，死代码消除，循环优化
2. 程序分析：数据流分析，可达性分析
3. 图论问题：关键节点识别，路径分析
"""

import sys
from collections import deque, defaultdict

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
        self.graph = defaultdict(list)        # 原图邻接表
        self.reverse_graph = defaultdict(list) # 反向图邻接表
        self.dfn = [-1] * n                   # DFS序
        self.id = [0] * n                     # DFS序到节点的映射
        self.fa = [0] * n                     # DFS树中的父节点
        self.semi = [-1] * n                  # 半支配点
        self.idom = [-1] * n                  # 立即支配点
        self.best = list(range(n))            # 并查集优化用
        self.dfs_clock = 0                    # DFS时钟
        self.bucket = defaultdict(list)       # bucket[v]存储semi[v]相同的节点
        self.tree = defaultdict(list)         # 支配树
    
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
    
    def get_dominated_nodes(self, u):
        """
        获取节点u的支配节点
        
        Args:
            u (int): 节点
            
        Returns:
            list: 支配节点列表
        """
        if self.dfn[u] == -1:
            return []  # 节点不存在
        
        result = []
        queue = deque([u])
        
        while queue:
            v = queue.popleft()
            result.append(v)
            
            for w in self.tree[v]:
                queue.append(w)
        
        return result
    
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
        
        # 从v向上追溯到根节点，检查是否经过u
        current = v
        while current != self.root and current != -1:
            if current == u:
                return True
            current = self.idom[current]
        
        return current == u
    
    def get_immediate_dominator(self, u):
        """
        获取立即支配点
        
        Args:
            u (int): 节点
            
        Returns:
            int: 立即支配点，如果不存在返回-1
        """
        if self.dfn[u] == -1 or u == self.root:
            return -1
        return self.idom[u]
    
    def get_dominator_tree(self):
        """
        获取支配树
        
        Returns:
            dict: 支配树的邻接表表示
        """
        return self.tree
    
    def print_dominator_tree(self):
        """
        打印支配树结构
        """
        print("支配树结构:")
        for i in range(self.n):
            if self.tree[i]:
                print(f"节点 {i} 支配:", end=" ")
                for child in self.tree[i]:
                    print(child, end=" ")
                print()

def main():
    """
    测试函数
    """
    print("=== 支配树测试 ===")
    
    # 创建测试图
    # 0 -> 1 -> 2 -> 4
    #  \-> 3 --^
    dt = DominatorTree(5, 0)
    dt.add_edge(0, 1)
    dt.add_edge(0, 3)
    dt.add_edge(1, 2)
    dt.add_edge(3, 2)
    dt.add_edge(2, 4)
    
    # 构建支配树
    dt.build()
    
    # 打印结果
    dt.print_dominator_tree()
    
    # 测试支配关系
    print("\n支配关系测试:")
    print(f"节点0是否支配节点4: {'是' if dt.dominates(0, 4) else '否'}")
    print(f"节点1是否支配节点4: {'是' if dt.dominates(1, 4) else '否'}")
    print(f"节点2是否支配节点4: {'是' if dt.dominates(2, 4) else '否'}")
    
    # 测试立即支配点
    print("\n立即支配点:")
    for i in range(1, 5):
        idom = dt.get_immediate_dominator(i)
        print(f"节点{i}的立即支配点: {idom if idom != -1 else '无'}")
    
    # 测试被支配节点
    print("\n被支配节点:")
    for i in range(5):
        dominated = dt.get_dominated_nodes(i)
        print(f"节点{i}支配的节点: {dominated}")

if __name__ == "__main__":
    main()