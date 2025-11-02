#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Binary Lifting算法实现k-th祖先查询
时间复杂度:
  - 预处理: O(n log n)
  - 查询: O(log k)
空间复杂度: O(n log n)
"""

import math

class BinaryLiftingKthAncestor:
    def __init__(self, n, edges, root):
        """
        初始化Binary Lifting结构
        
        Args:
            n (int): 节点数量
            edges (list): 边集合，每个元素是[u, v]
            root (int): 根节点
        """
        self.n = n
        self.root = root
        
        # 计算需要的最大层数
        self.log = math.ceil(math.log2(n)) + 1 if n > 0 else 1
        
        # 初始化邻接表
        self.graph = [[] for _ in range(n)]
        for u, v in edges:
            self.graph[u].append(v)
            self.graph[v].append(u)
        
        # 初始化up表和深度数组
        self.up = [[-1] * n for _ in range(self.log)]
        self.depth = [0] * n
        
        # 预处理
        self._dfs(root, -1, 0)
        
        # 填充up表
        for k in range(1, self.log):
            for u in range(n):
                if self.up[k-1][u] != -1:
                    self.up[k][u] = self.up[k-1][self.up[k-1][u]]
    
    def _dfs(self, u, parent, current_depth):
        """
        DFS遍历，填充up[0]和depth数组
        
        Args:
            u (int): 当前节点
            parent (int): 父节点
            current_depth (int): 当前深度
        """
        self.up[0][u] = parent
        self.depth[u] = current_depth
        
        for v in self.graph[u]:
            if v != parent:
                self._dfs(v, u, current_depth + 1)
    
    def get_kth_ancestor(self, u, k):
        """
        获取节点u的k级祖先
        
        Args:
            u (int): 节点
            k (int): 祖先的级数
            
        Returns:
            int: u的k级祖先，如果不存在则返回-1
        """
        # 如果k大于节点深度，返回-1
        if k > self.depth[u]:
            return -1
        
        # 二进制分解k
        for i in range(self.log):
            if k & (1 << i):
                u = self.up[i][u]
                if u == -1:
                    return -1
        
        return u
    
    def lca(self, u, v):
        """
        计算两个节点的最近公共祖先(LCA)
        
        Args:
            u (int): 第一个节点
            v (int): 第二个节点
            
        Returns:
            int: u和v的LCA
        """
        # 确保u的深度大于等于v的深度
        if self.depth[u] < self.depth[v]:
            u, v = v, u
        
        # 将u提升到与v同一深度
        u = self.get_kth_ancestor(u, self.depth[u] - self.depth[v])
        
        # 如果u和v相同，说明已经找到LCA
        if u == v:
            return u
        
        # 同时提升u和v
        for k in range(self.log - 1, -1, -1):
            if self.up[k][u] != -1 and self.up[k][u] != self.up[k][v]:
                u = self.up[k][u]
                v = self.up[k][v]
        
        # LCA是u和v的父节点
        return self.up[0][u]
    
    def get_depth(self, u):
        """
        获取节点深度
        
        Args:
            u (int): 节点
            
        Returns:
            int: 节点的深度
        """
        return self.depth[u]

# 测试代码
if __name__ == "__main__":
    # 测试用例
    n = 6
    root = 0
    edges = [
        [0, 1],
        [0, 2],
        [1, 3],
        [1, 4],
        [2, 5]
    ]
    
    bl = BinaryLiftingKthAncestor(n, edges, root)
    
    # 测试k-th祖先查询
    print("k-th祖先查询结果：")
    print(f"节点3的1级祖先: {bl.get_kth_ancestor(3, 1)}")  # 应该是1
    print(f"节点3的2级祖先: {bl.get_kth_ancestor(3, 2)}")  # 应该是0
    print(f"节点3的3级祖先: {bl.get_kth_ancestor(3, 3)}")  # 应该是-1
    
    # 测试LCA查询
    print("\nLCA查询结果：")
    print(f"LCA(3, 4) = {bl.lca(3, 4)}")  # 应该是1
    print(f"LCA(3, 5) = {bl.lca(3, 5)}")  # 应该是0
    print(f"LCA(4, 5) = {bl.lca(4, 5)}")  # 应该是0