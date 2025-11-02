#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
高级树形DP综合应用 - Python版本
包含多个竞赛级别树形DP问题的解决方案

时间复杂度分析：
- 基础树形DP: O(n)
- 换根DP: O(n) 
- 树形背包: O(n*m)
- 虚树DP: O(k log k)

空间复杂度分析：
- 递归栈: O(h)
- DP数组: O(n)
- 图存储: O(n)
"""

from typing import List, Tuple, Dict, Optional
import sys
from collections import deque, defaultdict

sys.setrecursionlimit(1000000)  # 增加递归深度限制

class TreeNode:
    """二叉树节点定义"""
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class AdvancedTreeDP:
    """
    高级树形DP算法实现类
    """
    
    def tree_max_independent_set(self, graph: List[List[int]]) -> int:
        """
        1. 树的最大独立集（Maximum Independent Set）
        问题描述：在树中选择最多的节点，使得任意两个被选节点都不相邻
        算法要点：树形DP，状态设计为选择/不选择当前节点
        时间复杂度: O(n), 空间复杂度: O(n)
        """
        n = len(graph)
        dp = [[0, 0] for _ in range(n)]  # dp[u][0]: 不选u, dp[u][1]: 选u
        visited = [False] * n
        
        self._dfs_mis(0, -1, graph, dp, visited)
        return max(dp[0][0], dp[0][1])
    
    def _dfs_mis(self, u: int, parent: int, graph: List[List[int]], 
                 dp: List[List[int]], visited: List[bool]) -> None:
        """最大独立集的DFS辅助函数"""
        visited[u] = True
        dp[u][1] = 1  # 选择当前节点
        
        for v in graph[u]:
            if v != parent and not visited[v]:
                self._dfs_mis(v, u, graph, dp, visited)
                # 不选u时，v可选可不选
                dp[u][0] += max(dp[v][0], dp[v][1])
                # 选u时，v不能选
                dp[u][1] += dp[v][0]
    
    def tree_min_vertex_cover(self, graph: List[List[int]]) -> int:
        """
        2. 树的最小顶点覆盖（Minimum Vertex Cover）
        问题描述：选择最少的节点，使得每条边至少有一个端点被选择
        算法要点：树形DP，状态转移与最大独立集相关
        时间复杂度: O(n), 空间复杂度: O(n)
        """
        n = len(graph)
        dp = [[0, 0] for _ in range(n)]  # dp[u][0]: u不被覆盖, dp[u][1]: u被覆盖
        visited = [False] * n
        
        self._dfs_mvc(0, -1, graph, dp, visited)
        return min(dp[0][0], dp[0][1])
    
    def _dfs_mvc(self, u: int, parent: int, graph: List[List[int]], 
                dp: List[List[int]], visited: List[bool]) -> None:
        """最小顶点覆盖的DFS辅助函数"""
        visited[u] = True
        dp[u][1] = 1  # 选择当前节点
        
        for v in graph[u]:
            if v != parent and not visited[v]:
                self._dfs_mvc(v, u, graph, dp, visited)
                # u不被覆盖时，v必须被覆盖
                dp[u][0] += dp[v][1]
                # u被覆盖时，v可选可不选
                dp[u][1] += min(dp[v][0], dp[v][1])
    
    def tree_min_dominating_set(self, graph: List[List[int]]) -> int:
        """
        3. 树的最小支配集（Minimum Dominating Set）
        问题描述：选择最少的节点，使得每个节点要么被选择，要么与某个被选节点相邻
        算法要点：复杂状态设计的树形DP
        时间复杂度: O(n), 空间复杂度: O(n)
        """
        n = len(graph)
        # dp[u][0]: u被选择, dp[u][1]: u未被选择但被父节点覆盖, dp[u][2]: u未被覆盖
        dp = [[0, 0, 0] for _ in range(n)]
        visited = [False] * n
        
        self._dfs_mds(0, -1, graph, dp, visited)
        return min(dp[0][0], dp[0][2])  # 根节点不能要求父节点覆盖
    
    def _dfs_mds(self, u: int, parent: int, graph: List[List[int]], 
                dp: List[List[int]], visited: List[bool]) -> None:
        """最小支配集的DFS辅助函数"""
        visited[u] = True
        dp[u][0] = 1  # 选择当前节点
        dp[u][1] = 0   # 被父节点覆盖
        dp[u][2] = 0   # 未被覆盖
        
        min_diff = float('inf')
        has_child = False
        
        for v in graph[u]:
            if v != parent and not visited[v]:
                has_child = True
                self._dfs_mds(v, u, graph, dp, visited)
                
                # 状态转移
                dp[u][0] += min(dp[v][0], dp[v][1], dp[v][2])
                dp[u][1] += min(dp[v][0], dp[v][2])
                dp[u][2] += min(dp[v][0], dp[v][2])
                
                # 记录最小差值，用于处理dp[u][2]的特殊情况
                min_diff = min(min_diff, dp[v][0] - min(dp[v][0], dp[v][2]))
        
        # 如果没有子节点，调整状态值
        if not has_child:
            dp[u][1] = 0
            dp[u][2] = float('inf')  # 设置为无穷大
        else:
            # 处理dp[u][2]：至少有一个子节点被选择
            if min_diff != float('inf'):
                dp[u][2] += min_diff
            else:
                dp[u][2] = float('inf')
    
    def tree_weighted_max_independent_set(self, graph: List[List[int]], 
                                        weights: List[int]) -> int:
        """
        4. 树的带权最大独立集
        问题描述：每个节点有权重，选择权重和最大的独立集
        算法要点：带权树形DP
        时间复杂度: O(n), 空间复杂度: O(n)
        """
        n = len(graph)
        dp = [[0, 0] for _ in range(n)]  # dp[u][0]: 不选u, dp[u][1]: 选u
        visited = [False] * n
        
        self._dfs_wmis(0, -1, graph, weights, dp, visited)
        return max(dp[0][0], dp[0][1])
    
    def _dfs_wmis(self, u: int, parent: int, graph: List[List[int]], 
                 weights: List[int], dp: List[List[int]], visited: List[bool]) -> None:
        """带权最大独立集的DFS辅助函数"""
        visited[u] = True
        dp[u][1] = weights[u]  # 选择当前节点获得权重
        
        for v in graph[u]:
            if v != parent and not visited[v]:
                self._dfs_wmis(v, u, graph, weights, dp, visited)
                # 不选u时，v可选可不选
                dp[u][0] += max(dp[v][0], dp[v][1])
                # 选u时，v不能选
                dp[u][1] += dp[v][0]
    
    def tree_k_coloring(self, n: int, k: int, edges: List[List[int]]) -> int:
        """
        5. 树的k着色问题
        问题描述：用k种颜色给树染色，相邻节点颜色不同，求方案数
        算法要点：组合数学 + 树形DP
        时间复杂度: O(n), 空间复杂度: O(n)
        """
        # 构建图
        graph = [[] for _ in range(n)]
        for u, v in edges:
            graph[u].append(v)
            graph[v].append(u)
        
        # DP计算
        dp = [[0, 0] for _ in range(n)]  # dp[u][0]: u染特定颜色时的方案数
        visited = [False] * n
        
        self._dfs_coloring(0, -1, graph, k, dp, visited)
        return dp[0][0] * k  # 根节点有k种颜色选择
    
    def _dfs_coloring(self, u: int, parent: int, graph: List[List[int]], k: int,
                     dp: List[List[int]], visited: List[bool]) -> None:
        """k着色问题的DFS辅助函数"""
        visited[u] = True
        dp[u][0] = 1  # 当前节点染特定颜色
        
        for v in graph[u]:
            if v != parent and not visited[v]:
                self._dfs_coloring(v, u, graph, k, dp, visited)
                # 子节点不能与父节点同色，所以有(k-1)种选择
                dp[u][0] *= (dp[v][0] * (k - 1))
    
    def weighted_tree_diameter(self, graph: List[List[Tuple[int, int]]]) -> int:
        """
        6. 树的直径（带权版本）
        问题描述：求带权树的最长路径（直径）
        算法要点：两次BFS
        时间复杂度: O(n), 空间复杂度: O(n)
        """
        n = len(graph)
        # 第一次BFS找到最远点
        farthest, _ = self._bfs_farthest(0, graph)
        # 第二次BFS找到直径
        _, diameter = self._bfs_farthest(farthest, graph)
        return diameter
    
    def _bfs_farthest(self, start: int, graph: List[List[Tuple[int, int]]]) -> Tuple[int, int]:
        """BFS找到最远节点和距离"""
        n = len(graph)
        dist = [-1] * n
        dist[start] = 0
        
        queue = deque([start])
        farthest = start
        max_dist = 0
        
        while queue:
            u = queue.popleft()
            for v, w in graph[u]:
                if dist[v] == -1:
                    dist[v] = dist[u] + w
                    if dist[v] > max_dist:
                        max_dist = dist[v]
                        farthest = v
                    queue.append(v)
        
        return farthest, max_dist
    
    def tree_centroids(self, graph: List[List[int]]) -> List[int]:
        """
        7. 树的重心（Centroid）
        问题描述：找到删除后使得最大连通分量最小的节点
        算法要点：DFS计算子树大小
        时间复杂度: O(n), 空间复杂度: O(n)
        """
        n = len(graph)
        size = [0] * n
        centroids = []
        
        self._dfs_centroid(0, -1, graph, size, centroids, n)
        return centroids
    
    def _dfs_centroid(self, u: int, parent: int, graph: List[List[int]], 
                     size: List[int], centroids: List[int], n: int) -> None:
        """重心的DFS辅助函数"""
        size[u] = 1
        is_centroid = True
        
        for v in graph[u]:
            if v != parent:
                self._dfs_centroid(v, u, graph, size, centroids, n)
                size[u] += size[v]
                if size[v] > n // 2:
                    is_centroid = False
        
        # 检查删除u后的最大连通分量
        if n - size[u] > n // 2:
            is_centroid = False
        
        if is_centroid:
            centroids.append(u)
    
    def count_paths_with_sum(self, root: Optional[TreeNode], target_sum: int) -> int:
        """
        8. 树的路径统计问题
        问题描述：统计树上满足特定条件的路径数量
        算法要点：DFS + 哈希表
        时间复杂度: O(n), 空间复杂度: O(n)
        """
        prefix_sum = defaultdict(int)
        prefix_sum[0] = 1  # 空路径
        return self._dfs_path_sum(root, 0, target_sum, prefix_sum)
    
    def _dfs_path_sum(self, node: Optional[TreeNode], current_sum: int, 
                     target: int, prefix_sum: Dict[int, int]) -> int:
        """路径统计的DFS辅助函数"""
        if not node:
            return 0
        
        current_sum += node.val
        count = prefix_sum.get(current_sum - target, 0)
        
        prefix_sum[current_sum] += 1
        
        count += self._dfs_path_sum(node.left, current_sum, target, prefix_sum)
        count += self._dfs_path_sum(node.right, current_sum, target, prefix_sum)
        
        prefix_sum[current_sum] -= 1
        return count

def main():
    """单元测试函数"""
    solver = AdvancedTreeDP()
    
    # 测试用例1：简单树的最大独立集
    graph1 = [[1, 2], [0, 3], [0], [1]]
    print(f"最大独立集: {solver.tree_max_independent_set(graph1)}")
    
    # 测试用例2：最小顶点覆盖
    print(f"最小顶点覆盖: {solver.tree_min_vertex_cover(graph1)}")
    
    # 测试用例3：树的k着色
    edges = [[0, 1], [0, 2], [1, 3]]
    print(f"3着色方案数: {solver.tree_k_coloring(4, 3, edges)}")
    
    # 测试用例4：带权树直径
    weighted_graph = [
        [(1, 2), (2, 3)],  # 节点0到1权重2，到2权重3
        [(0, 2), (3, 1)],  # 节点1到0权重2，到3权重1
        [(0, 3)],          # 节点2到0权重3
        [(1, 1)]           # 节点3到1权重1
    ]
    print(f"带权树直径: {solver.weighted_tree_diameter(weighted_graph)}")

if __name__ == "__main__":
    main()