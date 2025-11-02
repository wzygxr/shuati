#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
圆方树 (Circle Square Tree) Python 实现

圆方树是一种将无向图转化为树结构的方法，主要用于处理仙人掌图（每条边最多属于一个环的图）。
在圆方树中：
- 圆点：原图中的节点
- 方点：原图中的环

应用场景：
1. 仙人掌图算法：最短路径、环相关问题
2. 图论问题：点双连通分量、割点
3. 竞赛算法：处理特殊图结构

算法思路：
1. 使用DFS找出图中的点双连通分量
2. 对于每个点双连通分量：
   - 如果是单个边，创建圆点-圆点的连接
   - 如果包含多个节点（形成环），创建一个方点代表这个环
3. 将圆点和方点连接形成树结构

时间复杂度：O(V + E)
空间复杂度：O(V + E)
"""

class CircleSquareTree:
    def __init__(self, n):
        """
        初始化圆方树
        
        Args:
            n: 原图节点数
        """
        self.n = n
        self.graph = [[] for _ in range(n)]  # 原图的邻接表
        self.tree = []  # 圆方树的邻接表
        self.tree_node_count = 0  # 圆方树节点数（包括圆点和方点）
        self.visited = [False] * n  # DFS访问标记
        self.dfn = [-1] * n  # DFS时间戳
        self.low = [-1] * n  # 最小时间戳
        self.stack = []  # DFS栈
        self.dfs_time = 0  # DFS时间
        self.biconnected_components = {}  # 点双连通分量
        self.bcc_count = 0  # 点双连通分量数量
    
    def add_edge(self, u, v):
        """
        添加边
        
        Args:
            u: 节点u
            v: 节点v
        """
        self.graph[u].append(v)
        self.graph[v].append(u)
    
    def build_circle_square_tree(self):
        """构建圆方树"""
        # 初始化
        self.visited = [False] * self.n
        self.dfn = [-1] * self.n
        self.low = [-1] * self.n
        self.stack = []
        self.biconnected_components = {}
        self.bcc_count = 0
        self.dfs_time = 0
        
        # 找出所有点双连通分量
        for i in range(self.n):
            if self.dfn[i] == -1:
                self.tarjan(i, -1)
        
        # 构建圆方树
        self.build_tree()
    
    def tarjan(self, u, parent):
        """
        Tarjan算法找点双连通分量
        
        Args:
            u: 当前节点
            parent: 父节点
        """
        self.dfn[u] = self.low[u] = self.dfs_time
        self.dfs_time += 1
        self.visited[u] = True
        self.stack.append(u)
        children = 0
        
        for v in self.graph[u]:
            if v == parent:
                continue
            
            if self.dfn[v] == -1:
                children += 1
                self.tarjan(v, u)
                self.low[u] = min(self.low[u], self.low[v])
                
                # 发现点双连通分量
                if self.low[v] >= self.dfn[u]:
                    bcc = []
                    node = None
                    while node != v:
                        node = self.stack.pop()
                        bcc.append(node)
                    bcc.append(u)  # 添加根节点
                    
                    self.biconnected_components[self.bcc_count] = bcc
                    self.bcc_count += 1
            else:
                self.low[u] = min(self.low[u], self.dfn[v])
        
        # 根节点特殊情况
        if parent == -1 and children == 0:
            bcc = [u]
            self.biconnected_components[self.bcc_count] = bcc
            self.bcc_count += 1
    
    def build_tree(self):
        """构建圆方树"""
        # 圆方树节点数 = 原图节点数 + 点双连通分量数
        self.tree_node_count = self.n + self.bcc_count
        self.tree = [[] for _ in range(self.tree_node_count)]
        
        # 为每个点双连通分量创建方点，并连接圆点
        for i in range(self.bcc_count):
            square_node = self.n + i  # 方点编号从n开始
            bcc = self.biconnected_components[i]
            
            # 连接方点和该分量中的所有圆点
            for circle_node in bcc:
                self.tree[square_node].append(circle_node)
                self.tree[circle_node].append(square_node)
    
    def get_circle_square_tree(self):
        """
        获取圆方树
        
        Returns:
            圆方树的邻接表
        """
        return [neighbors[:] for neighbors in self.tree]
    
    def get_tree_node_count(self):
        """
        获取圆方树节点数
        
        Returns:
            圆方树节点数
        """
        return self.tree_node_count
    
    def get_biconnected_components(self):
        """
        获取点双连通分量
        
        Returns:
            点双连通分量字典
        """
        return self.biconnected_components.copy()
    
    def distance(self, u, v):
        """
        计算两点间在圆方树上的距离
        
        Args:
            u: 起点
            v: 终点
            
        Returns:
            两点间距离
        """
        if u == v:
            return 0
        
        # BFS计算最短距离
        visited = [False] * self.tree_node_count
        queue = [(u, 0)]
        visited[u] = True
        
        while queue:
            node, dist = queue.pop(0)
            
            for neighbor in self.tree[node]:
                if not visited[neighbor]:
                    if neighbor == v:
                        return dist + 1
                    visited[neighbor] = True
                    queue.append((neighbor, dist + 1))
        
        return -1  # 不连通
    
    def print_structure(self):
        """打印圆方树结构"""
        print("Circle-Square Tree Structure:")
        print("Original nodes:", self.n)
        print("Biconnected components:", self.bcc_count)
        print("Tree nodes:", self.tree_node_count)
        
        print("Biconnected Components:")
        for i, bcc in self.biconnected_components.items():
            print(f"  BCC {i}: {bcc}")
        
        print("Tree Edges:")
        for i in range(self.tree_node_count):
            if self.tree[i]:
                print(f"  Node {i} -> {self.tree[i]}")


# 测试方法
def main():
    # 测试用例1：简单的仙人掌图
    print("测试用例1: 简单仙人掌图")
    cst1 = CircleSquareTree(5)
    
    # 构建图结构:
    # 0-1-2 (链)
    # 1-3-4 (链)
    # 1-2-3 (环)
    cst1.add_edge(0, 1)
    cst1.add_edge(1, 2)
    cst1.add_edge(1, 3)
    cst1.add_edge(2, 3)
    cst1.add_edge(3, 4)
    
    cst1.build_circle_square_tree()
    cst1.print_structure()
    
    print("节点0和节点4在圆方树上的距离:", cst1.distance(0, 4))
    print()
    
    # 测试用例2：树结构
    print("测试用例2: 树结构")
    cst2 = CircleSquareTree(5)
    
    # 构建树结构:
    # 0-1-2
    # |   |
    # 3   4
    cst2.add_edge(0, 1)
    cst2.add_edge(0, 3)
    cst2.add_edge(1, 2)
    cst2.add_edge(2, 4)
    
    cst2.build_circle_square_tree()
    cst2.print_structure()
    
    print("节点3和节点4在圆方树上的距离:", cst2.distance(3, 4))
    print()
    
    # 测试用例3：复杂仙人掌图
    print("测试用例3: 复杂仙人掌图")
    cst3 = CircleSquareTree(6)
    
    # 构建图结构:
    # 0-1-2 (链)
    # 0-2 (形成环)
    # 2-3-4 (链)
    # 2-4 (形成环)
    # 4-5 (链)
    cst3.add_edge(0, 1)
    cst3.add_edge(1, 2)
    cst3.add_edge(0, 2)
    cst3.add_edge(2, 3)
    cst3.add_edge(3, 4)
    cst3.add_edge(2, 4)
    cst3.add_edge(4, 5)
    
    cst3.build_circle_square_tree()
    cst3.print_structure()
    
    print("节点0和节点5在圆方树上的距离:", cst3.distance(0, 5))


if __name__ == "__main__":
    main()