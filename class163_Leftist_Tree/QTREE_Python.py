#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
SPOJ QTREE - Query on a tree
题目链接：https://www.spoj.com/problems/QTREE/

问题描述：
给定一棵N个节点的树，每条边都有一个权值。
有两种操作：
1. CHANGE i ti: 将第i条边的权值改为ti
2. QUERY a b: 询问从节点a到节点b路径上边权的最大值

解题思路：
使用树链剖分（Tree Chain Decomposition）+ 线段树（Segment Tree）

树链剖分核心思想：
1. 第一次DFS：计算每个节点的深度、子树大小、重儿子
2. 第二次DFS：进行链剖分，给节点重新编号，确定每条链的顶端
3. 使用线段树维护重链上的边权信息

树链剖分关键概念：
- 重儿子：子树大小最大的子节点
- 轻儿子：除重儿子外的其他子节点
- 重边：父节点与重儿子之间的边
- 轻边：父节点与轻儿子之间的边
- 重链：由重边连接形成的链

时间复杂度分析：
- 预处理: O(N)
- 修改操作: O(log N)
- 查询操作: O(log² N)
- 总体: O(N + M * log² N)

空间复杂度分析:
- 存储树结构: O(N)
- 线段树: O(N)
- 其他辅助数组: O(N)
- 总体: O(N)

相关题目：
- Java实现：QTREE_Java.java
- Python实现：QTREE_Python.py
"""


class SegmentTree:
    """线段树类"""
    
    def __init__(self, n):
        """
        构造函数
        :param n: 节点数量
        """
        self.n = n
        self.tree = [0] * (4 * n)
        self.lazy = [0] * (4 * n)
    
    def push_up(self, rt):
        """
        向上更新
        :param rt: 当前节点在线段树中的位置
        """
        self.tree[rt] = max(self.tree[rt << 1], self.tree[rt << 1 | 1])
    
    def build(self, l, r, rt):
        """
        构建线段树
        :param l: 区间左端点
        :param r: 区间右端点
        :param rt: 当前节点在线段树中的位置
        """
        self.lazy[rt] = 0
        if l == r:
            self.tree[rt] = 0
            return
        mid = (l + r) >> 1
        self.build(l, mid, rt << 1)
        self.build(mid + 1, r, rt << 1 | 1)
        self.push_up(rt)
    
    def update(self, p, val, l, r, rt):
        """
        单点更新
        :param p: 要更新的位置
        :param val: 新的值
        :param l: 区间左端点
        :param r: 区间右端点
        :param rt: 当前节点在线段树中的位置
        """
        if l == r:
            self.tree[rt] = val
            return
        mid = (l + r) >> 1
        if p <= mid:
            self.update(p, val, l, mid, rt << 1)
        else:
            self.update(p, val, mid + 1, r, rt << 1 | 1)
        self.push_up(rt)
    
    def query(self, L, R, l, r, rt):
        """
        区间查询最大值
        :param L: 查询区间左端点
        :param R: 查询区间右端点
        :param l: 当前区间左端点
        :param r: 当前区间右端点
        :param rt: 当前节点在线段树中的位置
        :return: 区间最大值
        """
        if L <= l and r <= R:
            return self.tree[rt]
        mid = (l + r) >> 1
        ans = float('-inf')
        if L <= mid:
            ans = max(ans, self.query(L, R, l, mid, rt << 1))
        if R > mid:
            ans = max(ans, self.query(L, R, mid + 1, r, rt << 1 | 1))
        return ans


class TreeChainDecomposition:
    """树链剖分类"""
    
    def __init__(self, n):
        """
        构造函数
        :param n: 节点数量
        """
        self.n = n
        # 邻接表存图
        self.graph = [[] for _ in range(n + 1)]
        
        # 树链剖分相关数组
        self.size = [0] * (n + 1)      # 子树大小
        self.depth = [0] * (n + 1)     # 节点深度
        self.father = [0] * (n + 1)    # 父节点
        self.son = [0] * (n + 1)       # 重儿子
        self.top = [0] * (n + 1)       # 所在链的顶端节点
        self.pos = [0] * (n + 1)       # 线段树中位置
        self.edge_id = [0] * (n + 1)   # 边的映射
        self.pos_count = 0
        
        # 边信息
        self.edges = {}
        
        # 线段树
        self.seg_tree = SegmentTree(n)
    
    def add_edge(self, u, v, w, idx):
        """
        添加边
        :param u: 起始节点
        :param v: 终止节点
        :param w: 边的权重
        :param idx: 边的编号
        """
        self.graph[u].append((v, w, idx))
        self.graph[v].append((u, w, idx))
        self.edges[idx] = (u, v, w)
    
    def dfs1(self, u, pre, dep):
        """
        第一次DFS：计算深度、子树大小、重儿子
        :param u: 当前节点
        :param pre: 父节点
        :param dep: 当前深度
        """
        self.father[u] = pre
        self.depth[u] = dep
        self.size[u] = 1
        
        for v, w, idx in self.graph[u]:
            if v == pre:
                continue
            
            self.edge_id[v] = idx
            self.dfs1(v, u, dep + 1)
            self.size[u] += self.size[v]
            
            # 更新重儿子
            if self.size[v] > self.size[self.son[u]]:
                self.son[u] = v
    
    def dfs2(self, u, tp):
        """
        第二次DFS：链剖分，重新编号
        :param u: 当前节点
        :param tp: 当前链的顶端节点
        """
        self.top[u] = tp
        self.pos[u] = self.pos_count = self.pos_count + 1
        
        if self.son[u] != 0:
            self.dfs2(self.son[u], tp)  # 优先遍历重儿子
        
        for v, w, idx in self.graph[u]:
            if v == self.father[u] or v == self.son[u]:
                continue
            self.dfs2(v, v)  # 轻儿子作为新链的顶端
    
    def init(self):
        """初始化树链剖分"""
        self.dfs1(1, 0, 0)
        self.dfs2(1, 1)
        self.seg_tree.build(1, self.n, 1)
        
        # 初始化线段树
        for idx, (u, v, w) in self.edges.items():
            # 将边权赋给深度更深的节点
            if self.depth[u] > self.depth[v]:
                self.seg_tree.update(self.pos[u], w, 1, self.n, 1)
            else:
                self.seg_tree.update(self.pos[v], w, 1, self.n, 1)
    
    def query_path(self, u, v):
        """
        查询树上两点间路径的最大边权
        :param u: 起始节点
        :param v: 终止节点
        :return: 路径上的最大边权
        """
        ans = float('-inf')
        
        # 两个点向上跳，直到在一个链上
        while self.top[u] != self.top[v]:
            if self.depth[self.top[u]] < self.depth[self.top[v]]:
                u, v = v, u
            
            # 查询u到top[u]的路径最大值
            ans = max(ans, self.seg_tree.query(self.pos[self.top[u]], self.pos[u], 1, self.n, 1))
            u = self.father[self.top[u]]
        
        # 在同一条链上
        if u == v:
            return ans
        
        # 保证u是深度更深的节点
        if self.depth[u] > self.depth[v]:
            u, v = v, u
        
        # 查询u的子节点到v的路径最大值
        ans = max(ans, self.seg_tree.query(self.pos[self.son[u]], self.pos[v], 1, self.n, 1))
        return ans
    
    def change_edge(self, edge_idx, new_weight):
        """
        修改边权
        :param edge_idx: 边的编号
        :param new_weight: 新的权重
        """
        u, v, _ = self.edges[edge_idx]
        self.edges[edge_idx] = (u, v, new_weight)
        
        # 更新线段树
        if self.depth[u] > self.depth[v]:
            self.seg_tree.update(self.pos[u], new_weight, 1, self.n, 1)
        else:
            self.seg_tree.update(self.pos[v], new_weight, 1, self.n, 1)


def main():
    """
    主函数
    输入格式：
    第一行包含一个整数T，表示测试用例数量
    对于每个测试用例：
      第一行包含一个整数N，表示节点数量
      接下来N-1行，每行包含三个整数a、b、c，表示节点a和b之间有一条权值为c的边
      接下来若干行，每行包含一个操作：
        - CHANGE i ti: 将第i条边的权值改为ti
        - QUERY a b: 询问从节点a到节点b路径上边权的最大值
        - DONE: 结束当前测试用例
    输出格式：
    对于每个QUERY操作，输出路径上的最大边权
    """
    import sys
    
    # 读取所有输入
    lines = []
    for line in sys.stdin:
        line = line.strip()
        if line:
            lines.append(line)
    
    i = 0
    test_cases = int(lines[i])
    i += 1
    
    for _ in range(test_cases):
        n = int(lines[i])
        i += 1
        
        # 创建树链剖分对象
        tree_chain = TreeChainDecomposition(n)
        
        # 存储边信息
        edges_info = []
        
        # 读取边信息
        for j in range(1, n):
            u, v, w = map(int, lines[i].split())
            i += 1
            tree_chain.add_edge(u, v, w, j)
            edges_info.append((u, v, w))
        
        # 初始化树链剖分
        tree_chain.init()
        
        # 处理操作
        while i < len(lines) and lines[i] != "DONE":
            operation = lines[i].split()
            i += 1
            
            if operation[0] == "QUERY":
                u = int(operation[1])
                v = int(operation[2])
                print(tree_chain.query_path(u, v))
            elif operation[0] == "CHANGE":
                edge_idx = int(operation[1])
                new_weight = int(operation[2])
                tree_chain.change_edge(edge_idx, new_weight)
        
        i += 1  # 跳过"DONE"


if __name__ == "__main__":
    main()