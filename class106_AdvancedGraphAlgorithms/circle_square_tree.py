#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
圆方树算法实现

圆方树是一种用于处理仙人掌图（Cactus Graph）的数据结构。
仙人掌图是一种特殊的无向图，其中任意两条简单环最多只有一个公共顶点。
圆方树将仙人掌图转化为一棵树结构，使得可以使用树型DP等树算法来解决仙人掌图上的问题。

时间复杂度：O(n + m)，其中n是节点数，m是边数
空间复杂度：O(n + m)，用于存储图和圆方树
"""

from collections import defaultdict, deque
import sys
sys.setrecursionlimit(1 << 25)


class CircleSquareTree:
    def __init__(self, n):
        """
        构造函数
        :param n: 原图顶点数
        """
        self.n = n  # 原图顶点数
        self.m = 0  # 圆方树顶点数（初始为原图顶点数）
        self.graph = defaultdict(list)  # 原图
        self.square_graph = []  # 圆方树
        self.dfn = [0] * (n + 1)  # 深度优先搜索的时间戳
        self.low = [0] * (n + 1)  # 能够回溯到的最早的时间戳
        self.stk = []  # 栈，用于保存双连通分量
        self.cnt = 0  # 时间戳计数器
        self.id = 0  # 圆方树顶点编号
    
    def add_edge(self, u, v):
        """
        添加边
        :param u: 起点
        :param v: 终点
        """
        self.graph[u].append(v)
        self.graph[v].append(u)
    
    def tarjan(self, u, parent):
        """
        Tarjan算法寻找双连通分量并构建圆方树
        :param u: 当前节点
        :param parent: 父节点
        """
        self.cnt += 1
        self.dfn[u] = self.low[u] = self.cnt
        self.stk.append(u)
        
        for v in self.graph[u]:
            if v == parent:
                continue
            
            if not self.dfn[v]:
                self.tarjan(v, u)
                self.low[u] = min(self.low[u], self.low[v])
                
                # 发现一个双连通分量
                if self.low[v] >= self.dfn[u]:
                    self.id += 1  # 创建新的方点
                    self.square_graph.append([])
                    
                    # 将双连通分量中的顶点与方点相连
                    w = -1
                    while w != v:
                        w = self.stk.pop()
                        self.square_graph[w].append(self.id)
                        self.square_graph[self.id].append(w)
                    
                    # 将当前顶点u与方点相连
                    self.square_graph[u].append(self.id)
                    self.square_graph[self.id].append(u)
            else:
                # 回边，更新low值
                self.low[u] = min(self.low[u], self.dfn[v])
    
    def build(self):
        """
        构建圆方树
        :return: 圆方树的邻接表表示
        """
        self.id = self.n  # 方点编号从n+1开始
        self.square_graph = [[] for _ in range(self.n * 2 + 1)]  # 预估大小
        
        for i in range(1, self.n + 1):
            if not self.dfn[i]:
                self.tarjan(i, 0)
        
        self.m = self.id  # 更新圆方树顶点数
        return self.square_graph
    
    def is_square(self, u):
        """
        判断是否为方点
        :param u: 节点编号
        :return: 是否为方点
        """
        return u > self.n


# 测试代码
if __name__ == "__main__":
    # 示例：创建一个简单的仙人掌图
    cst = CircleSquareTree(4)
    cst.add_edge(1, 2)
    cst.add_edge(2, 3)
    cst.add_edge(3, 1)  # 形成一个三角形环
    cst.add_edge(3, 4)
    
    square_graph = cst.build()
    print("圆方树构建完成，顶点数:", cst.m)
    
    # 输出圆方树的邻接表
    for i in range(1, cst.m + 1):
        print(f"节点 {i} 的邻居: {square_graph[i]}")