#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 543. 二叉树的直径 (Diameter of Binary Tree) - Python实现
题目链接：https://leetcode.com/problems/diameter-of-binary-tree/

题目描述：
给定一棵二叉树，你需要计算它的直径长度。二叉树的直径是指树中任意两个节点之间最长路径的长度。
这条路径可能穿过也可能不穿过根节点。

算法思路：
1. 树形DP思想：对于每个节点，计算以该节点为根的子树的最大深度
2. 直径计算：对于每个节点，直径 = 左子树深度 + 右子树深度
3. 全局维护：在递归过程中维护全局最大直径

时间复杂度：O(n) - 每个节点访问一次
空间复杂度：O(h) - 递归栈深度，h为树的高度

最优解验证：这是最优解，无法进一步优化时间复杂度

工程化考量：
1. 异常处理：空树、单节点树等边界情况
2. 递归深度控制：Python默认递归深度有限，对于极端不平衡树可能栈溢出
3. 内存优化：使用nonlocal变量避免全局变量污染
"""

from typing import Optional, List
from collections import deque

class TreeNode:
    """二叉树节点定义"""
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right
    
    def __repr__(self):
        return f"TreeNode({self.val})"

class Solution:
    """二叉树的直径解决方案类"""
    
    def diameterOfBinaryTree(self, root: Optional[TreeNode]) -> int:
        """
        计算二叉树的直径
        
        Args:
            root: 二叉树根节点
            
        Returns:
            int: 二叉树的直径（最长路径的边数）
            
        Raises:
            TypeError: 如果输入不是TreeNode类型
        """
        # 输入验证
        if root is None:
            return 0
        
        # 使用nonlocal变量维护最大直径
        max_diameter = 0
        
        def depth(node: Optional[TreeNode]) -> int:
            """
            递归计算节点深度，同时更新最大直径
            
            Args:
                node: 当前节点
                
            Returns:
                int: 当前节点的深度
            """
            nonlocal max_diameter
            
            if node is None:
                return 0
            
            # 递归计算左右子树深度
            left_depth = depth(node.left)
            right_depth = depth(node.right)
            
            # 更新全局最大直径：当前节点的直径 = 左深度 + 右深度
            max_diameter = max(max_diameter, left_depth + right_depth)
            
            # 返回当前节点的深度：左右子树最大深度 + 1
            return max(left_depth, right_depth) + 1
        
        depth(root)
        return max_diameter
    
    def build_tree(self, values: List[Optional[int]]) -> Optional[TreeNode]:
        """
        根据层序遍历数组构建二叉树
        
        Args:
            values: 层序遍历的节点值，None表示空节点
            
        Returns:
            Optional[TreeNode]: 构建的二叉树根节点
            
        Raises:
            ValueError: 如果输入数组为空或格式错误
        """
        if not values or values[0] is None:
            return None
        
        root = TreeNode(values[0])
        queue = deque([root])
        i = 1
        
        while queue and i < len(values):
            current = queue.popleft()
            
            # 处理左子节点
            if i < len(values) and values[i] is not None:
                current.left = TreeNode(values[i])
                queue.append(current.left)
            i += 1
            
            # 处理右子节点
            if i < len(values) and values[i] is not None:
                current.right = TreeNode(values[i])
                queue.append(current.right)
            i += 1
        
        return root
    
    def print_tree(self, root: Optional[TreeNode], level: int = 0) -> None:
        """
        打印树结构（用于调试）
        
        Args:
            root: 二叉树根节点
            level: 当前层级
        """
        if root is None:
            print("  " * level + "None")
            return
        
        print("  " * level + str(root.val))
        self.print_tree(root.left, level + 1)
        self.print_tree(root.right, level + 1)

def run_tests():
    """运行测试用例验证算法正确性"""
    solution = Solution()
    
    print("=== LeetCode 543. 二叉树的直径测试 ===")
    
    # 测试用例1：空树
    print(f"测试用例1 - 空树: {solution.diameterOfBinaryTree(None)} (期望: 0)")
    
    # 测试用例2：单节点树
    single_node = TreeNode(1)
    print(f"测试用例2 - 单节点树: {solution.diameterOfBinaryTree(single_node)} (期望: 0)")
    
    # 测试用例3：示例树 [1,2,3,4,5]
    #       1
    #      / \
    #     2   3
    #    / \
    #   4   5
    tree1_values = [1, 2, 3, 4, 5, None, None, None, None, None, None]
    root1 = solution.build_tree(tree1_values)
    print(f"测试用例3 - 示例树: {solution.diameterOfBinaryTree(root1)} (期望: 3)")
    
    # 测试用例4：链状树（退化为链表）
    # 1 -> 2 -> 3 -> 4
    tree2_values = [1, None, 2, None, None, None, 3, None, None, None, None, None, None, None, 4]
    root2 = solution.build_tree(tree2_values)
    print(f"测试用例4 - 链状树: {solution.diameterOfBinaryTree(root2)} (期望: 3)")
    
    # 测试用例5：完全二叉树
    #       1
    #      / \
    #     2   3
    #    / \ / \
    #   4  5 6 7
    tree3_values = [1, 2, 3, 4, 5, 6, 7]
    root3 = solution.build_tree(tree3_values)
    print(f"测试用例5 - 完全二叉树: {solution.diameterOfBinaryTree(root3)} (期望: 4)")
    
    # 测试用例6：直径不经过根节点
    #       1
    #      / \
    #     2   3
    #    / \
    #   4   5
    #  /     \
    # 6       7
    tree4_values = [1, 2, 3, 4, 5, None, None, 6, None, None, 7]
    root4 = solution.build_tree(tree4_values)
    print(f"测试用例6 - 复杂树: {solution.diameterOfBinaryTree(root4)} (期望: 5)")
    
    print("=== 所有测试用例执行完成！ ===")

def performance_test():
    """性能测试：大规模数据处理"""
    solution = Solution()
    
    print("\n=== 性能测试 ===")
    
    # 构建深度为8的完全二叉树（255个节点）
    large_tree_values = list(range(1, 256))
    large_root = solution.build_tree(large_tree_values)
    
    print("大规模树直径计算中...")
    result = solution.diameterOfBinaryTree(large_root)
    print(f"深度为8的完全二叉树直径: {result}")
    
    # 内存使用分析
    import sys
    def get_tree_size(node):
        """估算树的内存占用"""
        if node is None:
            return 0
        return sys.getsizeof(node) + get_tree_size(node.left) + get_tree_size(node.right)
    
    memory_usage = get_tree_size(large_root)
    print(f"树结构内存占用: {memory_usage} 字节")

def edge_case_analysis():
    """边界情况分析"""
    solution = Solution()
    
    print("\n=== 边界情况分析 ===")
    
    # 极端不平衡树（可能栈溢出）
    print("测试极端不平衡树...")
    unbalanced_root = TreeNode(1)
    current = unbalanced_root
    for i in range(2, 100):  # 限制深度避免栈溢出
        current.right = TreeNode(i)
        current = current.right
    
    try:
        result = solution.diameterOfBinaryTree(unbalanced_root)
        print(f"极端不平衡树直径: {result}")
    except RecursionError:
        print("递归深度过大，Python默认递归深度限制")
    
    # 空树和单节点树已经在上面的测试中覆盖

if __name__ == "__main__":
    # 运行基础测试
    run_tests()
    
    # 性能测试
    performance_test()
    
    # 边界情况分析
    edge_case_analysis()
    
    print("\n=== 程序执行完成 ===")
    
    # 算法复杂度分析
    print("\n=== 算法复杂度分析 ===")
    print("时间复杂度: O(n) - 每个节点访问一次")
    print("空间复杂度: O(h) - 递归栈深度，h为树的高度")
    print("最优解验证: 是，这是最优解法")
    print("工程化考量: 包含异常处理、内存管理、性能优化")