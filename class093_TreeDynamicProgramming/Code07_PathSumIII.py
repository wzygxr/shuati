# 路径总和 III (Path Sum III)
# 题目描述:
# 给定一个二叉树的根节点 root，和一个整数 targetSum ，求该二叉树里节点值之和等于 targetSum 的路径的数目。
# 路径不需要从根节点开始，也不需要在叶子节点结束，但是路径方向必须是向下的（只能从父节点到子节点）。
# 测试链接 : https://leetcode.cn/problems/path-sum-iii/
#
# 解题思路:
# 1. 使用前缀和 + 深度优先搜索的方法
# 2. 维护从根节点到当前节点的路径前缀和
# 3. 使用哈希表记录各个前缀和出现的次数
# 4. 对于当前节点，查找是否存在前缀和等于 currentSum - targetSum
# 5. 路径数目等于该前缀和出现的次数
#
# 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点
# 空间复杂度: O(n) - 哈希表存储前缀和，递归调用栈深度为O(h)
# 是否为最优解: 是，这是解决路径总和III问题的标准方法
#
# 相关题目:
# - LeetCode 437. 路径总和 III
# - 类似问题：子数组和等于k的数目
#
# 工程化考量:
# 1. 处理空树和单节点树的边界情况
# 2. 支持负数值的处理
# 3. 提供递归和迭代两种实现方式
# 4. 添加详细的注释和调试信息

import sys
from typing import Optional, Dict
import unittest
from collections import defaultdict

class TreeNode:
    """二叉树节点定义"""
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    """路径总和III解决方案"""
    
    def pathSum(self, root: Optional[TreeNode], targetSum: int) -> int:
        """
        计算路径和等于目标值的路径数目
        
        Args:
            root: 二叉树的根节点
            targetSum: 目标路径和
            
        Returns:
            int: 路径数目
        """
        if root is None:
            return 0
        
        # 使用哈希表记录前缀和出现的次数
        prefix_sum_count = defaultdict(int)
        prefix_sum_count[0] = 1  # 前缀和为0的路径有1条（空路径）
        
        return self._dfs(root, 0, targetSum, prefix_sum_count)
    
    def _dfs(self, node: Optional[TreeNode], current_sum: int, target_sum: int, 
             prefix_sum_count: Dict[int, int]) -> int:
        """
        深度优先搜索
        
        Args:
            node: 当前节点
            current_sum: 当前路径和
            target_sum: 目标路径和
            prefix_sum_count: 前缀和计数字典
            
        Returns:
            int: 路径数目
        """
        if node is None:
            return 0
        
        # 更新当前路径和
        current_sum += node.val
        
        # 查找是否存在前缀和等于 current_sum - target_sum
        path_count = prefix_sum_count[current_sum - target_sum]
        
        # 更新前缀和计数
        prefix_sum_count[current_sum] += 1
        
        # 递归处理左右子树
        path_count += self._dfs(node.left, current_sum, target_sum, prefix_sum_count)
        path_count += self._dfs(node.right, current_sum, target_sum, prefix_sum_count)
        
        # 回溯：恢复前缀和计数
        prefix_sum_count[current_sum] -= 1
        if prefix_sum_count[current_sum] == 0:
            del prefix_sum_count[current_sum]
        
        return path_count

class DoubleDFSSolution:
    """双重DFS版本（更直观但效率较低）"""
    
    def pathSum(self, root: Optional[TreeNode], targetSum: int) -> int:
        """
        双重DFS版本
        
        Args:
            root: 二叉树的根节点
            targetSum: 目标路径和
            
        Returns:
            int: 路径数目
        """
        if root is None:
            return 0
        
        # 以当前节点为起点的路径数目
        count_from_root = self._count_paths(root, targetSum)
        
        # 递归处理左右子树
        count_from_left = self.pathSum(root.left, targetSum)
        count_from_right = self.pathSum(root.right, targetSum)
        
        return count_from_root + count_from_left + count_from_right
    
    def _count_paths(self, node: Optional[TreeNode], remaining_sum: int) -> int:
        """
        计算以当前节点为起点的路径数目
        
        Args:
            node: 当前节点
            remaining_sum: 剩余路径和
            
        Returns:
            int: 路径数目
        """
        if node is None:
            return 0
        
        count = 0
        if node.val == remaining_sum:
            count += 1
        
        # 继续向下搜索
        count += self._count_paths(node.left, remaining_sum - node.val)
        count += self._count_paths(node.right, remaining_sum - node.val)
        
        return count

class IterativeSolution:
    """迭代版本（避免递归栈溢出）"""
    
    def pathSum(self, root: Optional[TreeNode], targetSum: int) -> int:
        """
        迭代版本的路径总和计算
        
        Args:
            root: 二叉树的根节点
            targetSum: 目标路径和
            
        Returns:
            int: 路径数目
        """
        if root is None:
            return 0
        
        total_count = 0
        stack = [(root, [0])]  # 节点和路径和数组
        
        while stack:
            node, path_sums = stack.pop()
            
            # 更新路径和
            new_path_sums = []
            for path_sum in path_sums:
                new_sum = path_sum + node.val
                new_path_sums.append(new_sum)
                if new_sum == targetSum:
                    total_count += 1
            
            new_path_sums.append(node.val)  # 以当前节点为起点的新路径
            if node.val == targetSum:
                total_count += 1
            
            # 处理子节点
            if node.right is not None:
                stack.append((node.right, new_path_sums.copy()))
            if node.left is not None:
                stack.append((node.left, new_path_sums.copy()))
        
        return total_count

class TestPathSumIII(unittest.TestCase):
    """单元测试类"""
    
    def test_empty_tree(self):
        """测试空树"""
        sol = Solution()
        result = sol.pathSum(None, 5)
        self.assertEqual(result, 0)
    
    def test_single_node(self):
        """测试单节点树"""
        root = TreeNode(5)
        sol = Solution()
        result = sol.pathSum(root, 5)
        self.assertEqual(result, 1)
    
    def test_simple_tree(self):
        """测试简单树"""
        # 简单树测试：
        #       10
        #      /  \
        #     5   -3
        #    / \    \
        #   3   2    11
        #  / \   \
        # 3  -2   1
        # targetSum = 8, 期望结果: 3
        root = TreeNode(10)
        root.left = TreeNode(5)
        root.right = TreeNode(-3)
        root.left.left = TreeNode(3)
        root.left.right = TreeNode(2)
        root.right.right = TreeNode(11)
        root.left.left.left = TreeNode(3)
        root.left.left.right = TreeNode(-2)
        root.left.right.right = TreeNode(1)
        
        sol = Solution()
        result = sol.pathSum(root, 8)
        self.assertEqual(result, 3)
    
    def test_negative_values(self):
        """测试负数值"""
        # 负数值测试：
        #       1
        #      / \
        #    -2   -3
        # targetSum = -1, 期望结果: 2
        root = TreeNode(1)
        root.left = TreeNode(-2)
        root.right = TreeNode(-3)
        
        sol = Solution()
        result = sol.pathSum(root, -1)
        self.assertEqual(result, 2)
    
    def test_complex_tree(self):
        """测试复杂树"""
        # 复杂树测试：
        #       5
        #      / \
        #     4   8
        #    /   / \
        #   11  13  4
        #  /  \    / \
        # 7    2  5   1
        # targetSum = 22, 期望结果: 3
        root = TreeNode(5)
        root.left = TreeNode(4)
        root.right = TreeNode(8)
        root.left.left = TreeNode(11)
        root.right.left = TreeNode(13)
        root.right.right = TreeNode(4)
        root.left.left.left = TreeNode(7)
        root.left.left.right = TreeNode(2)
        root.right.right.left = TreeNode(5)
        root.right.right.right = TreeNode(1)
        
        sol = Solution()
        result = sol.pathSum(root, 22)
        self.assertEqual(result, 3)
    
    def test_double_dfs_solution(self):
        """测试双重DFS版本"""
        root = TreeNode(10)
        root.left = TreeNode(5)
        root.right = TreeNode(-3)
        root.left.left = TreeNode(3)
        root.left.right = TreeNode(2)
        root.right.right = TreeNode(11)
        
        sol = DoubleDFSSolution()
        result = sol.pathSum(root, 8)
        self.assertEqual(result, 1)

class PerformanceTest:
    """性能测试类"""
    
    @staticmethod
    def test_large_tree():
        """测试大规模树"""
        import time
        
        # 构建大规模平衡树
        def build_large_tree(n):
            if n <= 0:
                return None
            root = TreeNode(1)
            root.left = build_large_tree(n // 2)
            root.right = build_large_tree(n // 2)
            return root
        
        large_tree = build_large_tree(10000)
        
        sol = Solution()
        start_time = time.time()
        result = sol.pathSum(large_tree, 100000)
        end_time = time.time()
        
        print(f"大规模树测试: 结果={result}, 耗时={end_time - start_time:.4f}秒")

class DebugTool:
    """调试工具类"""
    
    @staticmethod
    def print_tree_with_path(root: Optional[TreeNode], target_sum: int):
        """打印二叉树路径信息"""
        if root is None:
            print("空树")
            return
        
        print(f"二叉树结构 (targetSum = {target_sum}):")
        DebugTool._print_tree_helper(root, 0)
    
    @staticmethod
    def _print_tree_helper(node: Optional[TreeNode], depth: int):
        """辅助函数打印树结构"""
        if node is None:
            return
        
        # 先打印右子树
        DebugTool._print_tree_helper(node.right, depth + 1)
        
        # 打印当前节点
        indent = "    " * depth
        print(f"{indent}{node.val}")
        
        # 打印左子树
        DebugTool._print_tree_helper(node.left, depth + 1)

def main():
    """主函数"""
    # 运行单元测试
    unittest.main(argv=[''], exit=False, verbosity=2)
    
    # 运行性能测试
    PerformanceTest.test_large_tree()
    
    print("\n路径总和III算法实现完成！")
    print("关键特性：")
    print("- 时间复杂度: O(n)")
    print("- 空间复杂度: O(n)")
    print("- 支持大规模树结构")
    print("- 处理负数值")
    print("- 前缀和+哈希表的优化方法")

if __name__ == "__main__":
    main()