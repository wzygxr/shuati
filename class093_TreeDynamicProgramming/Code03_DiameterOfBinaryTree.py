# 二叉树的直径 (Diameter of Binary Tree)
# 题目描述:
# 给定一棵二叉树，你需要计算它的直径长度。
# 一棵二叉树的直径长度是任意两个结点路径长度中的最大值。
# 这条路径可能穿过也可能不穿过根结点。
# 测试链接 : https://leetcode.cn/problems/diameter-of-binary-tree/
#
# 解题思路:
# 1. 使用树形动态规划（Tree DP）的方法
# 2. 对于每个节点，计算其左右子树的最大深度
# 3. 经过当前节点的最长路径 = 左子树深度 + 右子树深度
# 4. 全局最大直径 = max(左子树直径, 右子树直径, 经过当前节点的最长路径)
#
# 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点
# 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
# 是否为最优解: 是，这是计算二叉树直径的标准方法
#
# 相关题目:
# - LeetCode 543. 二叉树的直径
# - LeetCode 1245. 树的直径（一般树）
# - LeetCode 1522. N叉树的直径
#
# 工程化考量:
# 1. 处理空树和单节点树的边界情况
# 2. 提供递归和迭代两种实现方式
# 3. 添加详细的注释和调试信息
# 4. 支持大规模树结构

import sys
from typing import Optional, Tuple
import unittest

class TreeNode:
    """二叉树节点定义"""
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    """二叉树直径解决方案"""
    
    def diameterOfBinaryTree(self, root: Optional[TreeNode]) -> int:
        """
        计算二叉树的直径长度
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            int: 二叉树的直径长度
        """
        if root is None:
            return 0
            
        self.max_diameter = 0
        self._max_depth(root)
        return self.max_diameter
    
    def _max_depth(self, node: Optional[TreeNode]) -> int:
        """
        计算树的最大深度，同时更新最大直径
        
        Args:
            node: 当前节点
            
        Returns:
            int: 以node为根的子树的最大深度
        """
        if node is None:
            return 0
        
        # 计算左右子树的最大深度
        left_depth = self._max_depth(node.left)
        right_depth = self._max_depth(node.right)
        
        # 更新最大直径：经过当前节点的最长路径 = 左子树深度 + 右子树深度
        self.max_diameter = max(self.max_diameter, left_depth + right_depth)
        
        # 返回当前节点的最大深度
        return max(left_depth, right_depth) + 1

class OptimizedSolution:
    """优化版本：使用元组返回深度和直径"""
    
    def diameterOfBinaryTree(self, root: Optional[TreeNode]) -> int:
        """优化版本的直径计算"""
        if root is None:
            return 0
            
        result = self._dfs(root)
        return result[1]  # 返回直径
    
    def _dfs(self, node: Optional[TreeNode]) -> Tuple[int, int]:
        """
        返回元组(深度, 直径)
        
        Returns:
            Tuple[int, int]: (深度, 直径)
        """
        if node is None:
            return (0, 0)
        
        left_depth, left_diameter = self._dfs(node.left)
        right_depth, right_diameter = self._dfs(node.right)
        
        # 当前节点的深度 = max(左子树深度, 右子树深度) + 1
        current_depth = max(left_depth, right_depth) + 1
        # 当前节点的直径 = max(左子树直径, 右子树直径, 左子树深度+右子树深度)
        current_diameter = max(left_diameter, right_diameter, left_depth + right_depth)
        
        return (current_depth, current_diameter)

class IterativeSolution:
    """迭代版本（避免递归栈溢出）"""
    
    def diameterOfBinaryTree(self, root: Optional[TreeNode]) -> int:
        """迭代版本的直径计算"""
        if root is None:
            return 0
            
        # 使用后序遍历计算每个节点的深度
        depth_map = {}
        stack = []
        prev = None
        max_diameter = 0
        
        stack.append(root)
        
        while stack:
            curr = stack[-1]
            
            # 如果当前节点是叶子节点或者其子节点已经处理过
            if ((curr.left is None and curr.right is None) or 
                (prev is not None and (prev == curr.left or prev == curr.right))):
                
                # 处理当前节点
                left_depth = depth_map.get(curr.left, 0) if curr.left else 0
                right_depth = depth_map.get(curr.right, 0) if curr.right else 0
                current_depth = max(left_depth, right_depth) + 1
                
                # 更新最大直径
                max_diameter = max(max_diameter, left_depth + right_depth)
                
                # 存储当前节点的深度
                depth_map[curr] = current_depth
                stack.pop()
                prev = curr
            else:
                # 先处理右子树，再处理左子树（这样出栈时是左-右-根的顺序）
                if curr.right is not None:
                    stack.append(curr.right)
                if curr.left is not None:
                    stack.append(curr.left)
        
        return max_diameter

class TestDiameterOfBinaryTree(unittest.TestCase):
    """单元测试类"""
    
    def test_empty_tree(self):
        """测试空树"""
        sol = Solution()
        result = sol.diameterOfBinaryTree(None)
        self.assertEqual(result, 0)
    
    def test_single_node(self):
        """测试单节点树"""
        root = TreeNode(1)
        sol = Solution()
        result = sol.diameterOfBinaryTree(root)
        self.assertEqual(result, 0)
    
    def test_chain_tree(self):
        """测试链式树"""
        # 构建链式树: 1-2-3-4-5
        root = TreeNode(1)
        root.right = TreeNode(2)
        root.right.right = TreeNode(3)
        root.right.right.right = TreeNode(4)
        root.right.right.right.right = TreeNode(5)
        
        sol = Solution()
        result = sol.diameterOfBinaryTree(root)
        self.assertEqual(result, 4)  # 1到5的路径长度为4
    
    def test_balanced_tree(self):
        """测试平衡树"""
        # 构建平衡树: 
        #       1
        #      / \
        #     2   3
        #    / \   \
        #   4   5   6
        root = TreeNode(1)
        root.left = TreeNode(2, TreeNode(4), TreeNode(5))
        root.right = TreeNode(3, None, TreeNode(6))
        
        sol = Solution()
        result = sol.diameterOfBinaryTree(root)
        self.assertEqual(result, 4)  # 4到6的路径长度为4
    
    def test_complex_tree(self):
        """测试复杂树"""
        # 构建复杂树:
        #         1
        #        / \
        #       2   3
        #      / \   \
        #     4   5   6
        #    /       / \
        #   7       8   9
        root = TreeNode(1)
        root.left = TreeNode(2)
        root.right = TreeNode(3)
        root.left.left = TreeNode(4)
        root.left.right = TreeNode(5)
        root.right.right = TreeNode(6)
        root.left.left.left = TreeNode(7)
        root.right.right.left = TreeNode(8)
        root.right.right.right = TreeNode(9)
        
        sol = Solution()
        result = sol.diameterOfBinaryTree(root)
        self.assertEqual(result, 6)  # 7到9的路径长度为6

class PerformanceTest:
    """性能测试类"""
    
    @staticmethod
    def test_large_tree():
        """测试大规模树"""
        import time
        
        # 构建大规模链式树（最坏情况）
        def build_large_chain_tree(n):
            if n <= 0:
                return None
            root = TreeNode(1)
            current = root
            for i in range(2, n + 1):
                current.right = TreeNode(i)
                current = current.right
            return root
        
        # 构建大规模平衡树
        def build_large_balanced_tree(start, end):
            if start > end:
                return None
            mid = start + (end - start) // 2
            root = TreeNode(mid)
            root.left = build_large_balanced_tree(start, mid - 1)
            root.right = build_large_balanced_tree(mid + 1, end)
            return root
        
        # 测试链式树
        chain_tree = build_large_chain_tree(10000)
        sol = Solution()
        
        start_time = time.time()
        result1 = sol.diameterOfBinaryTree(chain_tree)
        end_time = time.time()
        print(f"大规模链式树测试: 结果={result1}, 耗时={end_time - start_time:.4f}秒")
        
        # 测试平衡树
        balanced_tree = build_large_balanced_tree(1, 10000)
        
        start_time = time.time()
        result2 = sol.diameterOfBinaryTree(balanced_tree)
        end_time = time.time()
        print(f"大规模平衡树测试: 结果={result2}, 耗时={end_time - start_time:.4f}秒")

class DebugTool:
    """调试工具类"""
    
    @staticmethod
    def print_tree(root: Optional[TreeNode], prefix: str = "", is_left: bool = True):
        """打印二叉树结构"""
        if root is None:
            print(prefix + ("├── " if is_left else "└── ") + "null")
            return
        
        print(prefix + ("├── " if is_left else "└── ") + str(root.val))
        
        if root.left is not None or root.right is not None:
            DebugTool.print_tree(root.left, prefix + ("│   " if is_left else "    "), True)
            DebugTool.print_tree(root.right, prefix + ("│   " if is_left else "    "), False)

def main():
    """主函数"""
    # 运行单元测试
    unittest.main(argv=[''], exit=False, verbosity=2)
    
    # 运行性能测试
    PerformanceTest.test_large_tree()
    
    print("\n二叉树直径算法实现完成！")
    print("关键特性：")
    print("- 时间复杂度: O(n)")
    print("- 空间复杂度: O(h)")
    print("- 支持大规模树结构")
    print("- 处理边界情况")
    print("- 提供递归和迭代两种实现")

if __name__ == "__main__":
    main()