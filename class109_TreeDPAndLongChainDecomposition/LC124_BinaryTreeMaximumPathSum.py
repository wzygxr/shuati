#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 124. 二叉树中的最大路径和 (Binary Tree Maximum Path Sum) - Python实现
题目链接：https://leetcode.com/problems/binary-tree-maximum-path-sum/

题目描述：
给定一个非空二叉树，返回其最大路径和。路径被定义为一条从树中任意节点出发，
达到任意节点的序列。该路径至少包含一个节点，且不一定经过根节点。

算法思路：
1. 树形DP思想：对于每个节点，计算以该节点为起点的最大路径和
2. 路径类型：路径可能出现在左子树、右子树，或穿过当前节点
3. 全局维护：在递归过程中维护全局最大路径和

时间复杂度：O(n) - 每个节点访问一次
空间复杂度：O(h) - 递归栈深度，h为树的高度

最优解验证：这是最优解，无法进一步优化时间复杂度
"""

from typing import Optional
import sys

class TreeNode:
    """二叉树节点定义"""
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    """二叉树最大路径和解决方案类"""
    
    def maxPathSum(self, root: Optional[TreeNode]) -> int:
        """
        计算二叉树的最大路径和
        
        Args:
            root: 二叉树根节点
            
        Returns:
            int: 最大路径和
        """
        if root is None:
            return 0
        
        # 使用nonlocal变量维护最大路径和
        max_sum = -sys.maxsize - 1
        
        def max_gain(node: Optional[TreeNode]) -> int:
            """
            递归计算以当前节点为起点的最大增益
            
            Args:
                node: 当前节点
                
            Returns:
                int: 以当前节点为起点的最大路径和（只能选择一条分支）
            """
            nonlocal max_sum
            
            if node is None:
                return 0
            
            # 递归计算左右子树的最大增益（如果为负则舍弃）
            left_gain = max(max_gain(node.left), 0)
            right_gain = max(max_gain(node.right), 0)
            
            # 计算穿过当前节点的路径和
            path_through_node = node.val + left_gain + right_gain
            
            # 更新全局最大路径和
            max_sum = max(max_sum, path_through_node)
            
            # 返回以当前节点为起点的最大路径和（只能选择一条分支）
            return node.val + max(left_gain, right_gain)
        
        max_gain(root)
        return max_sum

def run_tests():
    """运行测试用例验证算法正确性"""
    solution = Solution()
    
    print("=== LeetCode 124. 二叉树中的最大路径和测试 ===")
    
    # 测试用例1：单节点树
    root1 = TreeNode(1)
    print(f"测试用例1 - 单节点树: {solution.maxPathSum(root1)} (期望: 1)")
    
    # 测试用例2：示例树 [1,2,3]
    root2 = TreeNode(1)
    root2.left = TreeNode(2)
    root2.right = TreeNode(3)
    print(f"测试用例2 - 示例树: {solution.maxPathSum(root2)} (期望: 6)")
    
    # 测试用例3：包含负数的树 [-10,9,20,null,null,15,7]
    root3 = TreeNode(-10)
    root3.left = TreeNode(9)
    root3.right = TreeNode(20)
    root3.right.left = TreeNode(15)
    root3.right.right = TreeNode(7)
    print(f"测试用例3 - 包含负数树: {solution.maxPathSum(root3)} (期望: 42)")
    
    # 测试用例4：全负数树
    root4 = TreeNode(-3)
    root4.left = TreeNode(-2)
    root4.right = TreeNode(-1)
    print(f"测试用例4 - 全负数树: {solution.maxPathSum(root4)} (期望: -1)")
    
    print("=== 所有测试用例执行完成！ ===")

if __name__ == "__main__":
    run_tests()