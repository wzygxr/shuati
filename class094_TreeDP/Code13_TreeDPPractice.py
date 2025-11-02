#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
树形DP实战练习题目 - Python版本
包含各大OJ平台的经典树形DP问题实现

题目来源：LeetCode, LintCode, Codeforces, 洛谷, POJ等
算法类型：基础树形DP、换根DP、树形背包、虚树DP
"""

from typing import List, Optional, Tuple
import sys
from collections import deque, defaultdict

sys.setrecursionlimit(1000000)  # 增加递归深度限制

class TreeNode:
    """二叉树节点定义"""
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class TreeDPPractice:
    """
    树形DP实战练习类
    """
    
    def rob(self, root: Optional[TreeNode]) -> int:
        """
        1. LeetCode 337 - 打家劫舍 III（经典树形DP）
        题目链接：https://leetcode.cn/problems/house-robber-iii/
        时间复杂度: O(n), 空间复杂度: O(h)
        """
        not_rob, do_rob = self._rob_helper(root)
        return max(not_rob, do_rob)
    
    def _rob_helper(self, node: Optional[TreeNode]) -> Tuple[int, int]:
        """打家劫舍辅助函数"""
        if not node:
            return 0, 0
        
        left_not_rob, left_do_rob = self._rob_helper(node.left)
        right_not_rob, right_do_rob = self._rob_helper(node.right)
        
        # 不偷当前节点：左右子树可以偷或不偷
        not_rob = max(left_not_rob, left_do_rob) + max(right_not_rob, right_do_rob)
        # 偷当前节点：左右子树都不能偷
        do_rob = node.val + left_not_rob + right_not_rob
        
        return not_rob, do_rob
    
    def max_path_sum(self, root: Optional[TreeNode]) -> int:
        """
        2. LeetCode 124 - 二叉树中的最大路径和
        题目链接：https://leetcode.cn/problems/binary-tree-maximum-path-sum/
        时间复杂度: O(n), 空间复杂度: O(h)
        """
        self.max_sum = float('-inf')
        self._max_gain(root)
        return self.max_sum
    
    def _max_gain(self, node: Optional[TreeNode]) -> int:
        """最大路径和辅助函数"""
        if not node:
            return 0
        
        left_gain = max(self._max_gain(node.left), 0)
        right_gain = max(self._max_gain(node.right), 0)
        
        self.max_sum = max(self.max_sum, node.val + left_gain + right_gain)
        return node.val + max(left_gain, right_gain)
    
    def diameter_of_binary_tree(self, root: Optional[TreeNode]) -> int:
        """
        3. LeetCode 543 - 二叉树的直径
        题目链接：https://leetcode.cn/problems/diameter-of-binary-tree/
        时间复杂度: O(n), 空间复杂度: O(h)
        """
        self.max_diameter = 0
        self._depth(root)
        return self.max_diameter
    
    def _depth(self, node: Optional[TreeNode]) -> int:
        """深度计算辅助函数"""
        if not node:
            return 0
        
        left_depth = self._depth(node.left)
        right_depth = self._depth(node.right)
        
        self.max_diameter = max(self.max_diameter, left_depth + right_depth)
        return max(left_depth, right_depth) + 1
    
    def min_camera_cover(self, root: Optional[TreeNode]) -> int:
        """
        4. LeetCode 968 - 二叉树摄像头
        题目链接：https://leetcode.cn/problems/binary-tree-cameras/
        时间复杂度: O(n), 空间复杂度: O(h)
        """
        install, monitored, _ = self._min_camera_cover_helper(root)
        return min(install, monitored)
    
    def _min_camera_cover_helper(self, node: Optional[TreeNode]) -> Tuple[int, int, int]:
        """摄像头覆盖辅助函数"""
        if not node:
            return float('inf'), 0, 0
        
        left_install, left_monitored, left_unmonitored = self._min_camera_cover_helper(node.left)
        right_install, right_monitored, right_unmonitored = self._min_camera_cover_helper(node.right)
        
        # 状态0：当前节点安装摄像头
        install = 1 + min(left_install, left_monitored, left_unmonitored) + \
                    min(right_install, right_monitored, right_unmonitored)
        
        # 状态1：当前节点被子节点监控
        monitored = min(
            left_install + right_install,
            left_install + right_monitored,
            left_monitored + right_install
        )
        
        # 状态2：当前节点未被监控（需要父节点安装摄像头）
        unmonitored = left_monitored + right_monitored
        
        return install, monitored, unmonitored
    
    def sum_of_distances_in_tree(self, n: int, edges: List[List[int]]) -> List[int]:
        """
        5. LeetCode 834 - 树中距离之和（换根DP经典题）
        题目链接：https://leetcode.cn/problems/sum-of-distances-in-tree/
        时间复杂度: O(n), 空间复杂度: O(n)
        """
        # 构建图
        graph = [[] for _ in range(n)]
        for u, v in edges:
            graph[u].append(v)
            graph[v].append(u)
        
        dp = [0] * n  # 以节点i为根的子树的距离和
        size = [0] * n  # 子树大小
        result = [0] * n
        
        self._dfs1(0, -1, graph, dp, size)
        self._dfs2(0, -1, graph, dp, size, result, n)
        
        return result
    
    def _dfs1(self, u: int, parent: int, graph: List[List[int]], 
             dp: List[int], size: List[int]) -> None:
        """第一次DFS：计算基础信息"""
        size[u] = 1
        for v in graph[u]:
            if v != parent:
                self._dfs1(v, u, graph, dp, size)
                size[u] += size[v]
                dp[u] += dp[v] + size[v]
    
    def _dfs2(self, u: int, parent: int, graph: List[List[int]], 
             dp: List[int], size: List[int], result: List[int], n: int) -> None:
        """第二次DFS：换根计算"""
        result[u] = dp[u]
        for v in graph[u]:
            if v != parent:
                # 保存原始值
                dp_u, dp_v = dp[u], dp[v]
                sz_u, sz_v = size[u], size[v]
                
                # 换根操作
                dp[u] = dp[u] - dp[v] - size[v]
                size[u] = size[u] - size[v]
                dp[v] = dp[v] + dp[u] + size[u]
                size[v] = size[v] + size[u]
                
                self._dfs2(v, u, graph, dp, size, result, n)
                
                # 恢复原始值
                dp[u], dp[v] = dp_u, dp_v
                size[u], size[v] = sz_u, sz_v
    
    def course_selection(self, n: int, m: int, prerequisites: List[int], 
                        credits: List[int]) -> int:
        """
        6. 洛谷 P2014 - 选课（树形背包DP）
        题目链接：https://www.luogu.com.cn/problem/P2014
        时间复杂度: O(n*m²), 空间复杂度: O(n*m)
        """
        # 构建树（0为虚拟根节点）
        graph = [[] for _ in range(n + 1)]
        for i in range(1, n + 1):
            pre = prerequisites[i - 1]
            graph[pre].append(i)
        
        dp = [[0] * (m + 1) for _ in range(n + 1)]
        self._dfs_course(0, graph, credits, dp, m)
        return dp[0][m]
    
    def _dfs_course(self, u: int, graph: List[List[int]], credits: List[int],
                   dp: List[List[int]], m: int) -> None:
        """选课问题的DFS辅助函数"""
        # 初始化：选择当前节点（如果u>0）
        if u > 0:
            for j in range(1, m + 1):
                dp[u][j] = credits[u - 1]
        
        for v in graph[u]:
            self._dfs_course(v, graph, credits, dp, m)
            
            # 背包DP：从大到小遍历
            for j in range(m, -1, -1):
                for k in range(1, j + 1):
                    if u == 0:
                        # 虚拟根节点，只能选择子节点
                        dp[u][j] = max(dp[u][j], dp[u][j - k] + dp[v][k])
                    else:
                        # 普通节点，可以选择当前节点和子节点
                        dp[u][j] = max(dp[u][j], dp[u][j - k] + dp[v][k] - credits[u - 1])
    
    def tree_painting(self, n: int, edges: List[List[int]]) -> int:
        """
        7. Codeforces 1187E - Tree Painting（换根DP）
        题目链接：https://codeforces.com/contest/1187/problem/E
        时间复杂度: O(n), 空间复杂度: O(n)
        """
        graph = [[] for _ in range(n)]
        for u, v in edges:
            graph[u].append(v)
            graph[v].append(u)
        
        size = [0] * n
        dp = [0] * n
        
        # 第一次DFS：计算以0为根时的结果
        self._dfs_painting1(0, -1, graph, size, dp)
        
        max_score = dp[0]
        # 第二次DFS：换根计算最大值
        self._dfs_painting2(0, -1, graph, size, dp, max_score, n)
        
        return max_score
    
    def _dfs_painting1(self, u: int, parent: int, graph: List[List[int]],
                      size: List[int], dp: List[int]) -> None:
        """第一次DFS计算"""
        size[u] = 1
        for v in graph[u]:
            if v != parent:
                self._dfs_painting1(v, u, graph, size, dp)
                size[u] += size[v]
                dp[u] += dp[v]
        dp[u] += size[u]
    
    def _dfs_painting2(self, u: int, parent: int, graph: List[List[int]],
                      size: List[int], dp: List[int], max_score: int, n: int) -> None:
        """第二次DFS换根"""
        max_score = max(max_score, dp[u])
        
        for v in graph[u]:
            if v != parent:
                # 保存原始值
                dp_u, dp_v = dp[u], dp[v]
                sz_u, sz_v = size[u], size[v]
                
                # 换根：u->v
                dp[u] = dp[u] - dp[v] - size[v]
                size[u] = size[u] - size[v]
                dp[v] = dp[v] + dp[u] + size[u]
                size[v] = size[v] + size[u]
                
                self._dfs_painting2(v, u, graph, size, dp, max_score, n)
                
                # 恢复
                dp[u], dp[v] = dp_u, dp_v
                size[u], size[v] = sz_u, sz_v
    
    def find_centroids(self, n: int, edges: List[List[int]]) -> List[int]:
        """
        8. POJ 3107 - Godfather（树的重心）
        题目链接：http://poj.org/problem?id=3107
        时间复杂度: O(n), 空间复杂度: O(n)
        """
        graph = [[] for _ in range(n)]
        for u, v in edges:
            graph[u].append(v)
            graph[v].append(u)
        
        size = [0] * n
        centroids = []
        max_component = [float('inf')] * n
        
        self._dfs_centroid(0, -1, graph, size, centroids, max_component, n)
        return centroids
    
    def _dfs_centroid(self, u: int, parent: int, graph: List[List[int]],
                     size: List[int], centroids: List[int],
                     max_component: List[int], n: int) -> None:
        """重心计算的DFS辅助函数"""
        size[u] = 1
        max_size = 0
        
        for v in graph[u]:
            if v != parent:
                self._dfs_centroid(v, u, graph, size, centroids, max_component, n)
                size[u] += size[v]
                max_size = max(max_size, size[v])
        
        max_size = max(max_size, n - size[u])
        max_component[u] = max_size
        
        # 如果是重心，加入结果
        if max_size <= n // 2:
            centroids.append(u)
    
    def longest_univalue_path(self, root: Optional[TreeNode]) -> int:
        """
        9. LeetCode 687 - 最长同值路径
        题目链接：https://leetcode.cn/problems/longest-univalue-path/
        时间复杂度: O(n), 空间复杂度: O(h)
        """
        self.longest_path = 0
        self._dfs_univalue(root)
        return self.longest_path
    
    def _dfs_univalue(self, node: Optional[TreeNode]) -> int:
        """同值路径DFS辅助函数"""
        if not node:
            return 0
        
        left = self._dfs_univalue(node.left)
        right = self._dfs_univalue(node.right)
        
        left_path = 0
        right_path = 0
        
        if node.left and node.left.val == node.val:
            left_path = left + 1
        if node.right and node.right.val == node.val:
            right_path = right + 1
        
        self.longest_path = max(self.longest_path, left_path + right_path)
        return max(left_path, right_path)
    
    def distribute_coins(self, root: Optional[TreeNode]) -> int:
        """
        10. LeetCode 979 - 在二叉树中分配硬币
        题目链接：https://leetcode.cn/problems/distribute-coins-in-binary-tree/
        时间复杂度: O(n), 空间复杂度: O(h)
        """
        self.moves = 0
        self._dfs_distribute(root)
        return self.moves
    
    def _dfs_distribute(self, node: Optional[TreeNode]) -> int:
        """硬币分配DFS辅助函数"""
        if not node:
            return 0
        
        left = self._dfs_distribute(node.left)
        right = self._dfs_distribute(node.right)
        
        self.moves += abs(left) + abs(right)
        return node.val - 1 + left + right

def main():
    """单元测试函数"""
    solver = TreeDPPractice()
    
    # 测试打家劫舍III
    root = TreeNode(3)
    root.left = TreeNode(2)
    root.right = TreeNode(3)
    root.left.right = TreeNode(3)
    root.right.right = TreeNode(1)
    
    print(f"打家劫舍III结果: {solver.rob(root)}")
    
    # 测试二叉树直径
    diameter_root = TreeNode(1)
    diameter_root.left = TreeNode(2)
    diameter_root.right = TreeNode(3)
    diameter_root.left.left = TreeNode(4)
    diameter_root.left.right = TreeNode(5)
    
    print(f"二叉树直径: {solver.diameter_of_binary_tree(diameter_root)}")
    
    # 测试最长同值路径
    univalue_root = TreeNode(5)
    univalue_root.left = TreeNode(4)
    univalue_root.right = TreeNode(5)
    univalue_root.left.left = TreeNode(1)
    univalue_root.left.right = TreeNode(1)
    univalue_root.right.right = TreeNode(5)
    
    print(f"最长同值路径: {solver.longest_univalue_path(univalue_root)}")

if __name__ == "__main__":
    main()