# LeetCode 1245. 树的直径（非二叉树版本）
# 题目：给你一棵树，树中包含 n 个节点，节点编号从 0 到 n-1。
# 树用一个边列表来表示，其中 edges[i] = [u, v] 表示节点 u 和 v 之间有一条无向边。
# 返回这棵树的直径长度。
# 树的直径是树中任意两个节点之间最长路径的长度。
# 这条路径可能不经过根节点。
# 来源：LeetCode
# 链接：https://leetcode.cn/problems/tree-diameter/

from collections import deque
from typing import List, Tuple

class LeetCode1245_TreeDiameter:
    """
    树的直径计算类
    提供两种方法：两次BFS法和树形DP法
    """
    
    def treeDiameter(self, edges: List[List[int]]) -> int:
        """
        计算树的直径（两次BFS法）
        
        Args:
            edges: 边列表，表示树结构
            
        Returns:
            int: 树的直径长度
            
        时间复杂度：O(n)，其中n是节点数
        空间复杂度：O(n)，用于存储邻接表和BFS队列
        """
        n = len(edges) + 1  # 节点数 = 边数 + 1
        
        # 特殊情况处理
        if n == 0:
            return 0
        if n == 1:
            return 0
        
        # 构建邻接表
        graph = [[] for _ in range(n)]
        
        for u, v in edges:
            graph[u].append(v)
            graph[v].append(u)
        
        # 第一次BFS：从任意节点（如0）出发，找到最远的节点A
        nodeA, _ = self._bfs(0, graph, n)
        
        # 第二次BFS：从节点A出发，找到最远的节点B，距离就是直径
        _, diameter = self._bfs(nodeA, graph, n)
        
        return diameter
    
    def _bfs(self, start: int, graph: List[List[int]], n: int) -> Tuple[int, int]:
        """
        BFS方法，返回最远节点和距离
        
        Args:
            start: 起始节点
            graph: 邻接表
            n: 节点总数
            
        Returns:
            Tuple[int, int]: (最远节点, 距离)
        """
        distance = [-1] * n
        distance[start] = 0
        
        queue = deque([start])
        
        farthest_node = start
        max_distance = 0
        
        while queue:
            current = queue.popleft()
            
            # 遍历当前节点的所有邻居
            for neighbor in graph[current]:
                if distance[neighbor] == -1:
                    distance[neighbor] = distance[current] + 1
                    queue.append(neighbor)
                    
                    # 更新最远节点和最大距离
                    if distance[neighbor] > max_distance:
                        max_distance = distance[neighbor]
                        farthest_node = neighbor
        
        return farthest_node, max_distance
    
    def treeDiameterDP(self, edges: List[List[int]]) -> int:
        """
        树形DP方法计算树的直径（可以处理负权边）
        
        Args:
            edges: 边列表
            
        Returns:
            int: 树的直径长度
            
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        n = len(edges) + 1
        
        # 特殊情况处理
        if n == 0:
            return 0
        if n == 1:
            return 0
        
        # 构建邻接表
        graph = [[] for _ in range(n)]
        
        for u, v in edges:
            graph[u].append(v)
            graph[v].append(u)
        
        # 全局变量记录最大直径
        max_diameter = [0]
        
        # DFS计算每个节点的最大深度
        self._dfs(0, -1, graph, max_diameter)
        
        return max_diameter[0]
    
    def _dfs(self, node: int, parent: int, graph: List[List[int]], max_diameter: List[int]) -> int:
        """
        DFS计算节点深度并更新最大直径
        
        Args:
            node: 当前节点
            parent: 父节点
            graph: 邻接表
            max_diameter: 全局最大直径
            
        Returns:
            int: 当前节点的最大深度
        """
        max_depth1 = 0  # 最大深度
        max_depth2 = 0  # 次大深度
        
        for neighbor in graph[node]:
            if neighbor != parent:
                depth = self._dfs(neighbor, node, graph, max_diameter)
                
                if depth > max_depth1:
                    max_depth2 = max_depth1
                    max_depth1 = depth
                elif depth > max_depth2:
                    max_depth2 = depth
        
        # 更新最大直径：经过当前节点的最长路径 = max_depth1 + max_depth2
        max_diameter[0] = max(max_diameter[0], max_depth1 + max_depth2)
        
        # 返回当前节点的最大深度
        return max_depth1 + 1
    
    def test(self):
        """测试方法"""
        solution = LeetCode1245_TreeDiameter()
        
        # 测试用例1: [[0,1],[0,2]]
        # 树结构：
        #   0
        #  / \
        # 1   2
        # 预期输出：2（路径 1-0-2）
        edges1 = [[0,1],[0,2]]
        print(f"测试用例1结果: {solution.treeDiameter(edges1)}")  # 应该输出2
        print(f"测试用例1(DP)结果: {solution.treeDiameterDP(edges1)}")  # 应该输出2
        
        # 测试用例2: [[0,1],[1,2],[2,3],[1,4],[4,5]]
        # 树结构：
        #     0
        #     |
        #     1
        #    / \
        #   2   4
        #  /     \
        # 3       5
        # 预期输出：4（路径 3-2-1-4-5）
        edges2 = [[0,1],[1,2],[2,3],[1,4],[4,5]]
        print(f"测试用例2结果: {solution.treeDiameter(edges2)}")  # 应该输出4
        print(f"测试用例2(DP)结果: {solution.treeDiameterDP(edges2)}")  # 应该输出4
        
        # 测试用例3: 单节点树
        edges3 = []
        print(f"测试用例3结果: {solution.treeDiameter(edges3)}")  # 应该输出0
        print(f"测试用例3(DP)结果: {solution.treeDiameterDP(edges3)}")  # 应该输出0

# 主函数
if __name__ == "__main__":
    solution = LeetCode1245_TreeDiameter()
    solution.test()