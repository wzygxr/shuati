#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
支配树 (Dominator Tree) Python 实现

支配树是图论中的一个重要概念，主要用于程序优化和静态分析。
在控制流图中，如果从入口节点到节点 v 的每条路径都经过节点 u，
则称节点 u 支配节点 v。支配树是一种表示支配关系的树结构。

应用场景：
1. 编译器优化：死代码消除、循环优化
2. 程序分析：数据流分析、控制流分析
3. 网络分析：关键路径分析

算法思路：
使用 Lengauer-Tarjan 算法构建支配树：
1. 对图进行深度优先搜索，构建 DFS 树
2. 计算半支配点 (semi-dominator)
3. 计算支配点 (immediate dominator)

时间复杂度：O((V+E) log V)
空间复杂度：O(V+E)
"""

class DominatorTree:
    def __init__(self, n):
        """
        初始化支配树
        
        Args:
            n: 节点数
        """
        self.n = n
        self.graph = [[] for _ in range(n)]  # 原图的邻接表
        self.reverse_graph = [[] for _ in range(n)]  # 原图的反向图
        self.parent = [-1] * n  # DFS树中的父节点
        self.semi = [-1] * n  # 半支配点
        self.idom = [-1] * n  # 立即支配点
        self.dfn = [-1] * n  # DFS序
        self.id = [0] * n  # dfn的反向映射
        self.dfs_time = 0  # DFS时间戳
        
        # 用于Lengauer-Tarjan算法的数据结构
        self.bucket = [[] for _ in range(n)]  # bucket[v]存储semi[v] = w的所有节点v
        self.ancestor = [-1] * n  # 并查集的父节点
        self.label = list(range(n))  # 并查集中用于路径压缩的标签
    
    def add_edge(self, u, v):
        """
        添加边
        
        Args:
            u: 起点
            v: 终点
        """
        self.graph[u].append(v)
        self.reverse_graph[v].append(u)
    
    def build_dominator_tree(self, root):
        """
        构建支配树
        
        Args:
            root: 根节点
        """
        self.dfs_time = 0
        self.dfn = [-1] * self.n
        
        # 第一步：DFS遍历，构建DFS树
        self.dfs(root)
        
        # 初始化semi和label数组
        for i in range(self.n):
            self.semi[i] = self.dfn[i]
            self.label[i] = i
        
        # 第二步：从后向前计算半支配点
        for i in range(self.n - 1, 0, -1):
            w = self.id[i]
            
            # 计算semi[w]
            for v in self.reverse_graph[w]:
                if self.dfn[v] == -1:
                    continue  # 跳过不在DFS树中的节点
                u = self.eval(v)
                if self.semi[u] < self.semi[w]:
                    self.semi[w] = self.semi[u]
            
            self.bucket[self.id[self.semi[w]]].append(w)
            self.link(self.parent[w], w)
            
            # 处理bucket[parent[w]]
            for v in self.bucket[self.parent[w]]:
                u = self.eval(v)
                if self.semi[u] < self.semi[v]:
                    self.idom[v] = u
                else:
                    self.idom[v] = self.parent[w]
            
            self.bucket[self.parent[w]].clear()
        
        # 第三步：计算立即支配点
        for i in range(1, self.n):
            w = self.id[i]
            if self.idom[w] != self.id[self.semi[w]]:
                self.idom[w] = self.idom[self.idom[w]]
        
        self.idom[root] = root
    
    def dfs(self, u):
        """
        深度优先搜索
        
        Args:
            u: 当前节点
        """
        self.dfn[u] = self.dfs_time
        self.id[self.dfs_time] = u
        self.dfs_time += 1
        
        for v in self.graph[u]:
            if self.dfn[v] == -1:
                self.parent[v] = u
                self.dfs(v)
    
    def link(self, v, w):
        """
        并查集的link操作
        
        Args:
            v: 节点v
            w: 节点w
        """
        self.ancestor[w] = v
    
    def eval(self, v):
        """
        并查集的eval操作（带路径压缩）
        
        Args:
            v: 节点v
            
        Returns:
            节点v的根节点
        """
        if self.ancestor[v] == -1:
            return v
        
        self.compress(v)
        return self.label[v]
    
    def compress(self, v):
        """
        路径压缩
        
        Args:
            v: 节点v
        """
        if self.ancestor[self.ancestor[v]] == -1:
            return
        
        self.compress(self.ancestor[v])
        
        if self.semi[self.label[self.ancestor[v]]] < self.semi[self.label[v]]:
            self.label[v] = self.label[self.ancestor[v]]
        
        self.ancestor[v] = self.ancestor[self.ancestor[v]]
    
    def get_dominator(self, v):
        """
        获取节点v的支配点
        
        Args:
            v: 节点v
            
        Returns:
            节点v的支配点
        """
        return self.idom[v]
    
    def dominates(self, u, v):
        """
        检查节点u是否支配节点v
        
        Args:
            u: 节点u
            v: 节点v
            
        Returns:
            节点u是否支配节点v
        """
        # 从v沿着支配树向上查找，看是否能到达u
        current = v
        while current != u and current != self.idom[current]:
            current = self.idom[current]
        return current == u
    
    def get_dominator_tree(self):
        """
        获取支配树的邻接表表示
        
        Returns:
            支配树的邻接表
        """
        dom_tree = [[] for _ in range(self.n)]
        
        for i in range(self.n):
            if self.idom[i] != i:  # 不是根节点
                dom_tree[self.idom[i]].append(i)
        
        return dom_tree
    
    def print_dominator_tree(self):
        """打印支配树"""
        print("Dominator Tree:")
        dom_tree = self.get_dominator_tree()
        for i in range(self.n):
            print(f"Node {i} is dominated by {self.idom[i]}, dominates: {dom_tree[i]}")


# 测试方法
def main():
    # 测试用例1：简单的控制流图
    print("测试用例1: 简单控制流图")
    dt1 = DominatorTree(6)
    
    # 构建控制流图
    # 0 -> 1, 2
    # 1 -> 3
    # 2 -> 3
    # 3 -> 4, 5
    # 4 -> 3
    # 5 -> 3
    dt1.add_edge(0, 1)
    dt1.add_edge(0, 2)
    dt1.add_edge(1, 3)
    dt1.add_edge(2, 3)
    dt1.add_edge(3, 4)
    dt1.add_edge(3, 5)
    dt1.add_edge(4, 3)
    dt1.add_edge(5, 3)
    
    # 构建支配树
    dt1.build_dominator_tree(0)
    dt1.print_dominator_tree()
    
    print("节点1是否支配节点3:", dt1.dominates(1, 3))
    print("节点0是否支配节点3:", dt1.dominates(0, 3))
    print()
    
    # 测试用例2：线性结构
    print("测试用例2: 线性结构")
    dt2 = DominatorTree(5)
    
    # 构建线性控制流图
    # 0 -> 1 -> 2 -> 3 -> 4
    dt2.add_edge(0, 1)
    dt2.add_edge(1, 2)
    dt2.add_edge(2, 3)
    dt2.add_edge(3, 4)
    
    # 构建支配树
    dt2.build_dominator_tree(0)
    dt2.print_dominator_tree()
    print()
    
    # 测试用例3：循环结构
    print("测试用例3: 循环结构")
    dt3 = DominatorTree(4)
    
    # 构建带循环的控制流图
    # 0 -> 1
    # 1 -> 2
    # 2 -> 1, 3
    dt3.add_edge(0, 1)
    dt3.add_edge(1, 2)
    dt3.add_edge(2, 1)
    dt3.add_edge(2, 3)
    
    # 构建支配树
    dt3.build_dominator_tree(0)
    dt3.print_dominator_tree()


if __name__ == "__main__":
    main()