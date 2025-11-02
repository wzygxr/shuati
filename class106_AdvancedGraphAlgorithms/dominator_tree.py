#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
支配树算法实现

支配树是一种用于分析有向图中必经点的数据结构。
在有向图中，如果从起点s到终点t的所有路径都必须经过某个顶点u，则称u支配t，记为u dom t。
支配树将这种支配关系组织成一棵树结构，其中每个节点的父节点是其最近的支配点（即直接支配点）。

时间复杂度：O(n log n)，其中n是节点数
空间复杂度：O(n)，用于存储图和辅助数组
"""

from collections import defaultdict, deque
import sys
sys.setrecursionlimit(1 << 25)


class DominatorTree:
    def __init__(self, n, start):
        """
        构造函数
        :param n: 节点数
        :param start: 起点
        """
        self.n = n  # 节点数
        self.start = start  # 起点
        self.graph = defaultdict(list)  # 原图
        self.rev_graph = defaultdict(list)  # 反图
        self.size = 0  # 访问的节点数
        self.dfn = [0] * (n + 1)  # 发现时间
        self.idx = [0] * (n + 1)  # 时间戳对应的节点
        self.parent = [0] * (n + 1)  # DFS树中的父节点
        self.semi = [0] * (n + 1)  # 半支配点
        self.idom = [0] * (n + 1)  # 直接支配点
        self.ancestor = [0] * (n + 1)  # 并查集中的祖先
        self.best = [0] * (n + 1)  # 维护半支配点最小的节点
        self.out = defaultdict(list)  # 用于构建支配树
    
    def add_edge(self, u, v):
        """
        添加有向边u->v
        :param u: 起点
        :param v: 终点
        """
        self.graph[u].append(v)
        self.rev_graph[v].append(u)
    
    def dfs(self, u):
        """
        深度优先搜索，初始化相关信息
        :param u: 当前节点
        """
        self.size += 1
        self.dfn[u] = self.size
        self.idx[self.size] = u
        self.semi[self.size] = self.size
        self.best[self.size] = self.size
        self.ancestor[self.size] = 0
        
        for v in self.graph[u]:
            if not self.dfn[v]:
                self.parent[self.dfn[v]] = self.dfn[u]
                self.dfs(v)
    
    def find(self, u):
        """
        并查集查询，路径压缩并维护best信息
        :param u: 当前节点
        :return: 根节点
        """
        if self.ancestor[u] == 0:
            return u
        
        root = self.find(self.ancestor[u])
        if self.semi[self.best[self.ancestor[u]]] < self.semi[self.best[u]]:
            self.best[u] = self.best[self.ancestor[u]]
        
        self.ancestor[u] = root
        return root
    
    def union(self, u, v):
        """
        并查集合并操作
        :param u: 节点u
        :param v: 节点v
        """
        self.ancestor[v] = u
    
    def build(self):
        """
        构建支配树
        :return: 支配树的邻接表表示
        """
        # 第一步：DFS初始化
        self.dfs(self.start)
        
        # 第二步：按照发现时间逆序处理节点
        for i in range(self.size, 1, -1):
            # 计算半支配点
            u = self.idx[i]
            for v in self.rev_graph[u]:
                if not self.dfn[v]:
                    continue
                
                self.find(self.dfn[v])
                if self.semi[self.best[self.dfn[v]]] < self.semi[i]:
                    self.semi[i] = self.semi[self.best[self.dfn[v]]]
            
            # 合并到父节点所在的集合
            self.union(self.parent[i], i)
            
            # 计算直接支配点
            # 这里先存储，稍后处理
        
        # 第三步：计算直接支配点
        for i in range(2, self.size + 1):
            u = self.idx[i]
            if self.semi[i] == self.semi[self.parent[i]]:
                self.idom[i] = self.semi[i]
            else:
                self.idom[i] = self.idom[self.parent[i]]
        
        # 第四步：构建支配树
        for i in range(2, self.size + 1):
            u = self.idx[i]
            self.out[self.idx[self.idom[i]]].append(u)
        
        return self.out
    
    def get_dominators(self, v):
        """
        获取所有支配v的节点
        :param v: 目标节点
        :return: 支配v的所有节点列表
        """
        dominators = []
        if not self.dfn[v]:  # v不可达
            return dominators
        
        # 检查u是否是v在支配树中的祖先
        current = v
        while current != self.start:
            dominators.append(current)
            current = self.idx[self.idom[self.dfn[current]]]
        dominators.append(self.start)
        return dominators[::-1]  # 从根到叶排序
    
    def is_dominator(self, u, v):
        """
        判断u是否支配v
        :param u: 可能的支配点
        :param v: 被支配点
        :return: u是否支配v
        """
        if not self.dfn[v]:  # v不可达
            return False
        
        # 检查u是否是v在支配树中的祖先
        current = v
        while current != self.start:
            if current == u:
                return True
            current = self.idx[self.idom[self.dfn[current]]]
        return u == self.start
    
    def get_direct_dominator(self, v):
        """
        获取v的直接支配点
        :param v: 目标节点
        :return: v的直接支配点，如果不存在则返回-1
        """
        if not self.dfn[v] or v == self.start:
            return -1  # 不可达或为起点
        return self.idx[self.idom[self.dfn[v]]]


# 测试代码
if __name__ == "__main__":
    # 示例：创建一个有向图并构建支配树
    dt = DominatorTree(6, 1)
    dt.add_edge(1, 2)
    dt.add_edge(1, 3)
    dt.add_edge(2, 4)
    dt.add_edge(3, 4)
    dt.add_edge(4, 5)
    dt.add_edge(4, 6)
    dt.add_edge(5, 4)  # 形成环
    
    dominator_tree = dt.build()
    print("支配树构建完成")
    
    # 输出支配树的邻接表
    for i in range(1, 7):
        print(f"节点 {i} 的子节点: {dominator_tree[i]}")
    
    # 测试支配关系
    print(f"节点1是否支配节点4: {dt.is_dominator(1, 4)}")
    print(f"节点2是否支配节点5: {dt.is_dominator(2, 5)}")
    print(f"节点4的直接支配点: {dt.get_direct_dominator(4)}")