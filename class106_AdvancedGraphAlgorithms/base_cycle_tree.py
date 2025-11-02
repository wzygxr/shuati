#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
基环树（内向基环树）算法实现

基环树是一种特殊的图结构，它由一个环和连接在环上的若干棵树组成。
每个基环树都有且仅有一个环，删除环中的任意一条边后，图变为一棵树。

时间复杂度：O(n)，其中n是节点数
空间复杂度：O(n)，用于存储图和辅助数组
"""

from collections import defaultdict, deque
import sys
sys.setrecursionlimit(1 << 25)


class BaseCycleTree:
    def __init__(self, n):
        """
        构造函数
        :param n: 节点数
        """
        self.n = n
        self.graph = defaultdict(list)  # 使用defaultdict避免键不存在的问题
        self.visited = [False] * (n + 1)
        self.in_cycle = [False] * (n + 1)
        self.cycle = []
        self.parent = [0] * (n + 1)
        self.loop_start = -1
        self.loop_end = -1
    
    def add_edge(self, u, v):
        """
        添加边
        :param u: 起点
        :param v: 终点
        """
        self.graph[u].append(v)
    
    def dfs(self, u):
        """
        使用DFS寻找环
        :param u: 当前节点
        :return: 是否找到环
        """
        self.visited[u] = True
        for v in self.graph[u]:
            if not self.visited[v]:
                self.parent[v] = u
                if self.dfs(v):
                    return True
            elif v != self.parent[u]:  # 发现回边，说明找到了环
                self.loop_start = v
                self.loop_end = u
                return True
        return False
    
    def find_cycle(self):
        """
        寻找基环树中的环
        :return: 环中的节点列表
        """
        for i in range(1, self.n + 1):
            if not self.visited[i]:
                if self.dfs(i):
                    # 从loop_end回溯到loop_start，构建环
                    u = self.loop_end
                    while u != self.loop_start:
                        self.cycle.append(u)
                        self.in_cycle[u] = True
                        u = self.parent[u]
                    self.cycle.append(self.loop_start)
                    self.in_cycle[self.loop_start] = True
                    self.cycle.reverse()  # 按照环的顺序排列
                    return self.cycle
        return []
    
    def process_subtrees(self):
        """
        处理环上的子树，计算每个子树的信息
        :return: 每个节点子树的大小
        """
        subtree_info = [0] * (self.n + 1)
        
        def dfs_subtree(u, parent_node):
            """
            计算子树大小的DFS函数
            :param u: 当前节点
            :param parent_node: 父节点
            :return: 子树大小
            """
            res = 1  # 节点自身
            for v in self.graph[u]:
                if v != parent_node and not self.in_cycle[v]:
                    res += dfs_subtree(v, u)
            subtree_info[u] = res
            return res
        
        # 对环上的每个节点处理其子树
        for node in self.cycle:
            dfs_subtree(node, -1)
        
        return subtree_info


# 测试代码
if __name__ == "__main__":
    # 示例：创建一个基环树
    bct = BaseCycleTree(5)
    bct.add_edge(1, 2)
    bct.add_edge(2, 3)
    bct.add_edge(3, 4)
    bct.add_edge(4, 5)
    bct.add_edge(5, 2)  # 形成环：2->3->4->5->2
    
    cycle = bct.find_cycle()
    print("找到的环:", cycle)
    
    subtree_info = bct.process_subtrees()
    print("子树信息:", subtree_info)