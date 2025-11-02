#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
树形DP综合应用 - Python版本
包含虚树构建、复杂状态设计、多约束条件等高级技术

题目来源：Codeforces, AtCoder, 洛谷高级题目等
算法类型：虚树DP、多状态DP、组合优化等

相关题目:
1. https://codeforces.com/contest/1187/problem/E (Tree Painting)
2. https://codeforces.com/contest/1324/problem/F (Maximum White Subtree)
3. https://atcoder.jp/contests/abc160/tasks/abc160_f (Distributing Integers)
4. https://www.luogu.com.cn/problem/P2495 (最小消耗)
5. https://www.luogu.com.cn/problem/P3246 (序列)
"""

import sys
import heapq
from typing import List, Tuple, Set, Dict, Optional
from collections import deque, defaultdict

sys.setrecursionlimit(1000000)

class TreeDPComprehensive:
    """
    树形DP综合应用类
    """
    
    class VirtualTree:
        """
        虚树构建类
        时间复杂度: O(k log k), 空间复杂度: O(k)
        """
        def __init__(self, graph: List[List[int]]):
            self.graph = graph
            self.n = len(graph)
            self.depth = [0] * self.n
            self.dfn = [0] * self.n
            self.timer = 0
            self.log = 1
            self.parent = []
            self._preprocess()
        
        def _preprocess(self):
            """预处理：计算深度、DFS序和LCA信息"""
            # 计算对数深度
            while (1 << self.log) < self.n:
                self.log += 1
            
            self.parent = [[-1] * self.log for _ in range(self.n)]
            self._dfs_lca(0, -1)
        
        def _dfs_lca(self, u: int, p: int):
            """DFS计算LCA相关信息"""
            self.dfn[u] = self.timer
            self.timer += 1
            self.parent[u][0] = p
            
            for i in range(1, self.log):
                if self.parent[u][i-1] != -1:
                    self.parent[u][i] = self.parent[self.parent[u][i-1]][i-1]
            
            for v in self.graph[u]:
                if v != p:
                    self.depth[v] = self.depth[u] + 1
                    self._dfs_lca(v, u)
        
        def lca(self, u: int, v: int) -> int:
            """计算两个节点的最近公共祖先"""
            if self.depth[u] < self.depth[v]:
                u, v = v, u
            
            # 将u提升到与v同一深度
            for i in range(self.log-1, -1, -1):
                if self.depth[u] - (1 << i) >= self.depth[v]:
                    u = self.parent[u][i]
            
            if u == v:
                return u
            
            # 同时提升u和v
            for i in range(self.log-1, -1, -1):
                if self.parent[u][i] != self.parent[v][i]:
                    u = self.parent[u][i]
                    v = self.parent[v][i]
            
            return self.parent[u][0]
        
        def build_virtual_tree(self, key_points: List[int]) -> List[List[int]]:
            """构建虚树"""
            # 按DFS序排序关键点
            sorted_key_points = sorted(key_points, key=lambda x: self.dfn[x])
            
            # 添加LCA节点
            virtual_nodes = set(sorted_key_points)
            for i in range(1, len(sorted_key_points)):
                lca_node = self.lca(sorted_key_points[i-1], sorted_key_points[i])
                virtual_nodes.add(lca_node)
            
            sorted_nodes = sorted(virtual_nodes, key=lambda x: self.dfn[x])
            
            # 构建虚树
            virtual_tree = [[] for _ in range(self.n)]
            stack = [sorted_nodes[0]]
            
            for i in range(1, len(sorted_nodes)):
                u = sorted_nodes[i]
                while len(stack) > 1 and self.depth[stack[-1]] > self.depth[self.lca(stack[-1], u)]:
                    v = stack.pop()
                    virtual_tree[stack[-1]].append(v)
                
                lca_node = self.lca(stack[-1], u)
                if stack[-1] != lca_node:
                    virtual_tree[lca_node].append(stack[-1])
                    stack.pop()
                    stack.append(lca_node)
                stack.append(u)
            
            while len(stack) > 1:
                v = stack.pop()
                virtual_tree[stack[-1]].append(v)
            
            return virtual_tree
    
    def tree_maximum_matching(self, graph: List[List[int]]) -> int:
        """
        2. 树上最大匹配（Maximum Matching）
        问题描述：选择最多的不相交边
        算法要点：树形DP + 匹配理论
        时间复杂度: O(n), 空间复杂度: O(n)
        """
        n = len(graph)
        # dp[u][0]: u不参与匹配时的最大匹配数
        # dp[u][1]: u参与匹配时的最大匹配数
        dp = [[0, 0] for _ in range(n)]
        visited = [False] * n
        
        self._dfs_matching(0, -1, graph, dp, visited)
        return max(dp[0][0], dp[0][1])
    
    def _dfs_matching(self, u: int, parent: int, graph: List[List[int]],
                     dp: List[List[int]], visited: List[bool]) -> None:
        """最大匹配的DFS辅助函数"""
        visited[u] = True
        
        sum_not_matched = 0
        children = []
        
        for v in graph[u]:
            if v != parent and not visited[v]:
                children.append(v)
                self._dfs_matching(v, u, graph, dp, visited)
                sum_not_matched += max(dp[v][0], dp[v][1])
        
        dp[u][0] = sum_not_matched
        
        # 计算u参与匹配的情况
        max_with_matching = 0
        for v in children:
            # u与v匹配，其他子节点可以自由选择
            current = 1 + dp[v][0]  # u与v匹配
            for w in children:
                if w != v:
                    current += max(dp[w][0], dp[w][1])
            max_with_matching = max(max_with_matching, current)
        
        dp[u][1] = max_with_matching
    
    def tree_minimum_edge_cover(self, graph: List[List[int]]) -> int:
        """
        3. 树上最小边覆盖
        问题描述：选择最少的边覆盖所有节点
        算法要点：树形DP，与最大匹配相关
        时间复杂度: O(n), 空间复杂度: O(n)
        """
        n = len(graph)
        # 最小边覆盖 = 节点数 - 最大匹配
        max_matching = self.tree_maximum_matching(graph)
        return n - 1 - max_matching
    
    def tree_maximum_weighted_matching(self, 
                                      weighted_graph: List[List[Tuple[int, int]]]) -> int:
        """
        4. 树上带权最大匹配
        问题描述：每条边有权重，选择权重和最大的不相交边集合
        算法要点：带权树形DP
        时间复杂度: O(n), 空间复杂度: O(n)
        """
        n = len(weighted_graph)
        # dp[u][0]: u不参与匹配的最大权重和
        # dp[u][1]: u参与匹配的最大权重和
        dp = [[0, 0] for _ in range(n)]
        visited = [False] * n
        
        self._dfs_weighted_matching(0, -1, weighted_graph, dp, visited)
        return max(dp[0][0], dp[0][1])
    
    def _dfs_weighted_matching(self, u: int, parent: int,
                              weighted_graph: List[List[Tuple[int, int]]],
                              dp: List[List[int]], visited: List[bool]) -> None:
        """带权最大匹配的DFS辅助函数"""
        visited[u] = True
        
        sum_not_matched = 0
        children = []  # 存储(v, weight)对
        
        for v, weight in weighted_graph[u]:
            if v != parent and not visited[v]:
                children.append((v, weight))
                self._dfs_weighted_matching(v, u, weighted_graph, dp, visited)
                sum_not_matched += max(dp[v][0], dp[v][1])
        
        dp[u][0] = sum_not_matched
        
        # 计算u参与匹配的情况
        max_with_matching = 0
        for v, weight in children:
            # u与v匹配
            current = weight + dp[v][0]  # u与v匹配的权重
            for w, w_weight in children:
                if w != v:
                    current += max(dp[w][0], dp[w][1])
            max_with_matching = max(max_with_matching, current)
        
        dp[u][1] = max_with_matching
    
    def tree_steiner_tree(self, graph: List[List[Tuple[int, int]]],
                         terminals: List[int]) -> int:
        """
        5. 树上最小斯坦纳树（Steiner Tree）
        问题描述：连接关键点的最小权重子树
        算法要点：状态压缩DP + 树形DP
        时间复杂度: O(3^k * n + 2^k * n²), 空间复杂度: O(2^k * n)
        """
        n = len(graph)
        k = len(terminals)
        
        # 状态压缩：每个终端节点对应一个bit
        INF = 10**9
        dp = [[INF] * n for _ in range(1 << k)]
        
        # 初始化：单个终端节点
        for i, terminal in enumerate(terminals):
            dp[1 << i][terminal] = 0
        
        # 状态转移
        for mask in range(1, 1 << k):
            # 子树合并
            for u in range(n):
                submask = (mask - 1) & mask
                while submask > 0:
                    dp[mask][u] = min(dp[mask][u], 
                                    dp[submask][u] + dp[mask ^ submask][u])
                    submask = (submask - 1) & mask
            
            # Dijkstra-like relaxation
            pq = []
            for u in range(n):
                if dp[mask][u] < INF:
                    heapq.heappush(pq, (dp[mask][u], u))
            
            while pq:
                dist, u = heapq.heappop(pq)
                if dist > dp[mask][u]:
                    continue
                
                for v, weight in graph[u]:
                    new_dist = dist + weight
                    if new_dist < dp[mask][v]:
                        dp[mask][v] = new_dist
                        heapq.heappush(pq, (new_dist, v))
        
        # 找到最小权重和
        min_cost = min(dp[(1 << k) - 1])
        return min_cost
    
    def tree_path_cover(self, graph: List[List[int]]) -> int:
        """
        6. 树上路径覆盖问题
        问题描述：用最少的路径覆盖树的所有边，路径可以重叠
        算法要点：贪心 + 树形DP
        时间复杂度: O(n), 空间复杂度: O(n)
        """
        n = len(graph)
        dp = [0] * n  # 以u为根的子树需要的最少路径数
        visited = [False] * n
        
        self._dfs_path_cover(0, -1, graph, dp, visited)
        return dp[0]
    
    def _dfs_path_cover(self, u: int, parent: int, graph: List[List[int]],
                       dp: List[int], visited: List[bool]) -> None:
        """路径覆盖的DFS辅助函数"""
        visited[u] = True
        
        leaf_count = 0
        sum_dp = 0
        
        for v in graph[u]:
            if v != parent and not visited[v]:
                self._dfs_path_cover(v, u, graph, dp, visited)
                sum_dp += dp[v]
                
                if len(graph[v]) == 1:  # 叶子节点
                    leaf_count += 1
        
        if len(graph[u]) == 1 and parent != -1:
            # 叶子节点（非根）
            dp[u] = 1
        else:
            # 内部节点
            dp[u] = sum_dp - max(0, leaf_count - 1)

def main():
    """单元测试函数"""
    solver = TreeDPComprehensive()
    
    # 测试最大匹配
    graph = [
        [1, 2],
        [0, 3, 4],
        [0, 5],
        [1], [1], [2]
    ]
    
    print(f"树上最大匹配数: {solver.tree_maximum_matching(graph)}")
    
    # 测试最小边覆盖
    print(f"树上最小边覆盖: {solver.tree_minimum_edge_cover(graph)}")
    
    # 测试虚树构建
    vt = solver.VirtualTree(graph)
    key_points = [3, 4, 5]
    virtual_tree = vt.build_virtual_tree(key_points)
    
    print(f"虚树构建完成，节点数: {len(virtual_tree)}")
    
    # 测试斯坦纳树
    weighted_graph = [
        [(1, 2), (2, 3)],
        [(0, 2), (3, 1), (4, 2)],
        [(0, 3), (5, 1)],
        [(1, 1)],
        [(1, 2)],
        [(2, 1)]
    ]
    terminals = [3, 4, 5]
    min_cost = solver.tree_steiner_tree(weighted_graph, terminals)
    print(f"最小斯坦纳树成本: {min_cost}")

if __name__ == "__main__":
    main()