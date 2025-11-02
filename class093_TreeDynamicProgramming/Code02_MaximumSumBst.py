# 二叉搜索子树的最大键值和 (Maximum Sum BST)
# 题目描述:
# 给你一棵以 root 为根的二叉树
# 请你返回 任意 二叉搜索子树的最大键值和
# 二叉搜索树的定义如下：
# 任意节点的左子树中的键值都 小于 此节点的键值
# 任意节点的右子树中的键值都 大于 此节点的键值
# 任意节点的左子树和右子树都是二叉搜索树
# 测试链接 : https://leetcode.cn/problems/maximum-sum-bst-in-binary-tree/
#
# 解题思路:
# 1. 使用树形动态规划（Tree DP）的方法
# 2. 对于每个节点，我们需要知道以下信息：
#    - 以该节点为根的子树中的最大值
#    - 以该节点为根的子树中的最小值
#    - 该子树中所有节点值的和
#    - 该子树是否为BST
#    - 以该节点为根的子树中BST的最大键值和
# 3. 递归处理左右子树，综合计算当前节点的信息
#
# 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点
# 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
# 是否为最优解: 是，这是计算BST最大键值和的标准方法
#
# 相关题目:
# - LeetCode 1373. 二叉搜索子树的最大键值和
# - LeetCode 333. 最大BST子树
# - LeetCode 98. 验证二叉搜索树
#
# 工程化考量:
# 1. 使用int类型，因为题目数据范围在[-4*10^4, 4*10^4]
# 2. 处理空树和单节点树的边界情况
# 3. 支持负数值的处理
# 4. 提供递归和迭代两种实现方式
# 5. 添加详细的注释和调试信息

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
    """二叉搜索子树的最大键值和解决方案"""
    
    def maxSumBST(self, root: Optional[TreeNode]) -> int:
        """
        计算二叉搜索子树的最大键值和
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            int: 最大键值和（非负数）
        """
        if root is None:
            return 0
            
        result = self._dfs(root)
        return max(0, result[4])  # 确保返回非负数
    
    def _dfs(self, node: Optional[TreeNode]) -> Tuple[int, int, int, bool, int]:
        """
        深度优先搜索，递归处理每个节点
        
        Args:
            node: 当前节点
            
        Returns:
            Tuple[int, int, int, bool, int]: 
                - 子树中的最大值
                - 子树中的最小值
                - 子树节点值的和
                - 是否为BST
                - 最大BST键值和
        """
        # 基本情况：空节点
        if node is None:
            # 空树也是BST，节点数为0，和为0
            # 最大值设为负无穷，最小值设为正无穷
            return (float('-inf'), float('inf'), 0, True, 0)
        
        # 递归处理左右子树
        left_max, left_min, left_sum, left_is_bst, left_max_bst = self._dfs(node.left)
        right_max, right_min, right_sum, right_is_bst, right_max_bst = self._dfs(node.right)
        
        # 计算当前子树的信息
        # 当前子树的最大值 = max(当前节点值, 左子树最大值, 右子树最大值)
        current_max = max(node.val, left_max, right_max)
        # 当前子树的最小值 = min(当前节点值, 左子树最小值, 右子树最小值)
        current_min = min(node.val, left_min, right_min)
        # 当前子树所有节点值的和 = 左子树节点值和 + 右子树节点值和 + 当前节点值
        current_sum = left_sum + right_sum + node.val
        
        # 判断当前子树是否为BST
        # 条件：左右子树都是BST，且左子树最大值 < 当前节点值 < 右子树最小值
        is_current_bst = (left_is_bst and right_is_bst and 
                         left_max < node.val < right_min)
        
        # 计算当前子树中BST的最大键值和
        current_max_bst = max(left_max_bst, right_max_bst)
        if is_current_bst:
            # 如果当前子树是BST，则更新最大键值和
            current_max_bst = max(current_max_bst, current_sum)
        
        return (current_max, current_min, current_sum, is_current_bst, current_max_bst)

class OptimizedSolution:
    """优化版本：使用类属性存储最大键值和"""
    
    def maxSumBST(self, root: Optional[TreeNode]) -> int:
        """优化版本的最大键值和计算"""
        self.max_sum = 0
        self._dfs_optimized(root)
        return max(0, self.max_sum)
    
    def _dfs_optimized(self, node: Optional[TreeNode]) -> Tuple[bool, int, int, int]:
        """
        优化版本的DFS
        
        Returns:
            Tuple[bool, int, int, int]: 
                - 是否为BST
                - 最小值
                - 最大值
                - 节点值的和
        """
        if node is None:
            return (True, float('inf'), float('-inf'), 0)
        
        left_is_bst, left_min, left_max, left_sum = self._dfs_optimized(node.left)
        right_is_bst, right_min, right_max, right_sum = self._dfs_optimized(node.right)
        
        if (left_is_bst and right_is_bst and 
            left_max < node.val < right_min):
            
            current_sum = left_sum + right_sum + node.val
            self.max_sum = max(self.max_sum, current_sum)
            
            current_min = min(left_min, node.val)
            current_max = max(right_max, node.val)
            
            return (True, current_min, current_max, current_sum)
        else:
            return (False, 0, 0, 0)

class IterativeSolution:
    """迭代版本（避免递归栈溢出）"""
    
    def maxSumBST(self, root: Optional[TreeNode]) -> int:
        """迭代版本的最大键值和计算"""
        if root is None:
            return 0
            
        # 后序遍历收集所有节点
        nodes = []
        self._postorder_traversal(root, nodes)
        
        # 为每个节点存储信息
        info_map = {}
        max_sum = 0
        
        for node in nodes:
            left_info = info_map.get(node.left, (float('-inf'), float('inf'), 0, True, 0))
            right_info = info_map.get(node.right, (float('-inf'), float('inf'), 0, True, 0))
            
            left_max, left_min, left_sum, left_is_bst, left_max_bst = left_info
            right_max, right_min, right_sum, right_is_bst, right_max_bst = right_info
            
            current_max = max(node.val, left_max, right_max)
            current_min = min(node.val, left_min, right_min)
            current_sum = left_sum + right_sum + node.val
            
            is_current_bst = (left_is_bst and right_is_bst and 
                            left_max < node.val < right_min)
            
            current_max_bst = max(left_max_bst, right_max_bst)
            if is_current_bst:
                current_max_bst = max(current_max_bst, current_sum)
                
            info_map[node] = (current_max, current_min, current_sum, is_current_bst, current_max_bst)
            max_sum = max(max_sum, current_max_bst)
            
        return max(0, max_sum)
    
    def _postorder_traversal(self, node: Optional[TreeNode], nodes: list) -> None:
        """后序遍历"""
        if node is None:
            return
        self._postorder_traversal(node.left, nodes)
        self._postorder_traversal(node.right, nodes)
        nodes.append(node)

class TestMaximumSumBst(unittest.TestCase):
    """单元测试类"""
    
    def test_empty_tree(self):
        """测试空树"""
        sol = Solution()
        result = sol.maxSumBST(None)
        self.assertEqual(result, 0)
    
    def test_single_node(self):
        """测试单节点树"""
        root = TreeNode(5)
        sol = Solution()
        result = sol.maxSumBST(root)
        self.assertEqual(result, 5)
    
    def test_complete_bst(self):
        """测试完全BST"""
        # 构建完全BST: 
        #       10
        #      /  \
        #     5    15
        #    / \   / \
        #   1   8 12  20
        root = TreeNode(10)
        root.left = TreeNode(5, TreeNode(1), TreeNode(8))
        root.right = TreeNode(15, TreeNode(12), TreeNode(20))
        
        sol = Solution()
        result = sol.maxSumBST(root)
        self.assertEqual(result, 71)  # 1+5+8+10+12+15+20 = 71
    
    def test_non_bst(self):
        """测试非BST"""
        # 构建非BST:
        #       10
        #      /  \
        #     5    15
        #    / \   / \
        #   1  20 12  20  (20 > 5，违反BST规则)
        root = TreeNode(10)
        root.left = TreeNode(5, TreeNode(1), TreeNode(20))  # 违反BST
        root.right = TreeNode(15, TreeNode(12), TreeNode(20))
        
        sol = Solution()
        result = sol.maxSumBST(root)
        self.assertEqual(result, 47)  # 最大的BST是右子树（12+15+20=47）
    
    def test_negative_values(self):
        """测试负数值"""
        # 构建包含负数的BST:
        #       -10
        #      /   \
        #    -20    5
        #    /  \   / \
        #  -30 -15 3   8
        root = TreeNode(-10)
        root.left = TreeNode(-20, TreeNode(-30), TreeNode(-15))
        root.right = TreeNode(5, TreeNode(3), TreeNode(8))
        
        sol = Solution()
        result = sol.maxSumBST(root)
        self.assertEqual(result, 8)  # 最大的BST是右子树的右子树（8）
    
    def test_mixed_bst(self):
        """测试混合BST"""
        # 构建混合BST:
        #       20
        #      /  \
        #     15   25  (15 > 10，但15在20的左边，违反BST)
        #    /      \
        #   10      30
        root = TreeNode(20)
        root.left = TreeNode(15)
        root.right = TreeNode(25)
        root.left.left = TreeNode(10)
        root.right.right = TreeNode(30)
        
        sol = Solution()
        result = sol.maxSumBST(root)
        self.assertEqual(result, 55)  # 最大的BST是右子树（25+30=55）

class PerformanceTest:
    """性能测试类"""
    
    @staticmethod
    def test_large_tree():
        """测试大规模树"""
        import time
        
        # 构建大规模平衡BST
        def build_large_bst(n):
            if n <= 0:
                return None
            root = TreeNode(n)
            root.left = build_large_bst(n // 2)
            root.right = build_large_bst(n // 2)
            return root
        
        large_tree = build_large_bst(10000)
        
        sol = Solution()
        start_time = time.time()
        result = sol.maxSumBST(large_tree)
        end_time = time.time()
        
        print(f"大规模树测试: 结果={result}, 耗时={end_time - start_time:.4f}秒")

def main():
    """主函数"""
    # 运行单元测试
    unittest.main(argv=[''], exit=False, verbosity=2)
    
    # 运行性能测试
    PerformanceTest.test_large_tree()
    
    print("\n二叉搜索子树的最大键值和算法实现完成！")
    print("关键特性：")
    print("- 时间复杂度: O(n)")
    print("- 空间复杂度: O(h)")
    print("- 支持负数值处理")
    print("- 处理边界情况")
    print("- 返回非负结果")

if __name__ == "__main__":
    main()