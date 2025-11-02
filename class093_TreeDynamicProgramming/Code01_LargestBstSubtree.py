# 最大BST子树 (Largest BST Subtree)
# 题目描述:
# 给定一个二叉树，找到其中最大的二叉搜索树（BST）子树，并返回该子树的大小
# 其中，最大指的是子树节点数最多的
# 二叉搜索树（BST）中的所有节点都具备以下属性：
# 左子树的值小于其父（根）节点的值
# 右子树的值大于其父（根）节点的值
# 注意：子树必须包含其所有后代
# 测试链接 : https://leetcode.cn/problems/largest-bst-subtree/
#
# 解题思路:
# 1. 使用树形动态规划（Tree DP）的方法
# 2. 对于每个节点，我们需要知道以下信息：
#    - 以该节点为根的子树中的最大值
#    - 以该节点为根的子树中的最小值
#    - 该子树是否为BST
#    - 该子树中最大BST的节点数
# 3. 递归处理左右子树，综合计算当前节点的信息
#
# 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点
# 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
# 是否为最优解: 是，这是计算最大BST子树的标准方法
#
# 相关题目:
# - LeetCode 333. 最大BST子树 - https://leetcode.cn/problems/largest-bst-subtree/
# - LeetCode 98. 验证二叉搜索树 - https://leetcode.cn/problems/validate-binary-search-tree/
# - LeetCode 1373. 二叉搜索子树的最大键值和 - https://leetcode.cn/problems/maximum-sum-bst-in-binary-tree/
# - 洛谷 P1352 没有上司的舞会 - https://www.luogu.com.cn/problem/P1352
# - HDU 1520 Anniversary party - http://acm.hdu.edu.cn/showproblem.php?pid=1520
# - POJ 3342 Party at Hali-Bula - http://poj.org/problem?id=3342
# - Codeforces 1083C Max Mex - https://codeforces.com/problemset/problem/1083/C
# - AtCoder ABC163F path pass i - https://atcoder.jp/contests/abc163/tasks/abc163_f
# - SPOJ PT07Z - Longest path in a tree - https://www.spoj.com/problems/PT07Z/
#
# 工程化考量:
# 1. 使用float('inf')处理边界值
# 2. 处理空树和单节点树的边界情况
# 3. 提供递归和迭代两种实现方式
# 4. 添加详细的注释和调试信息
# 5. 支持多种测试用例验证

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
    """最大BST子树解决方案"""
    
    def largestBSTSubtree(self, root: Optional[TreeNode]) -> int:
        """
        计算二叉树中最大BST子树的大小
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            int: 最大BST子树的节点数
        """
        if root is None:
            return 0
            
        result = self._dfs(root)
        return result[3]  # 返回最大BST节点数
    
    def _dfs(self, node: Optional[TreeNode]) -> Tuple[float, float, bool, int]:
        """
        深度优先搜索，递归处理每个节点
        
        Args:
            node: 当前节点
            
        Returns:
            Tuple[float, float, bool, int]: 
                - 子树中的最大值
                - 子树中的最小值  
                - 是否为BST
                - 最大BST节点数
        """
        # 基本情况：空节点
        if node is None:
            # 空树也是BST，节点数为0
            # 最大值设为负无穷，最小值设为正无穷
            # 这样在比较时不会影响父节点的判断
            return (float('-inf'), float('inf'), True, 0)
        
        # 递归处理左右子树
        left_max, left_min, left_is_bst, left_max_bst = self._dfs(node.left)
        right_max, right_min, right_is_bst, right_max_bst = self._dfs(node.right)
        
        # 计算当前子树的信息
        # 当前子树的最大值 = max(当前节点值, 左子树最大值, 右子树最大值)
        current_max = max(node.val, left_max, right_max)
        # 当前子树的最小值 = min(当前节点值, 左子树最小值, 右子树最小值)
        current_min = min(node.val, left_min, right_min)
        
        # 判断当前子树是否为BST
        # 条件：左右子树都是BST，且左子树最大值 < 当前节点值 < 右子树最小值
        is_current_bst = (left_is_bst and right_is_bst and 
                         left_max < node.val < right_min)
        
        # 计算当前子树中最大BST的节点数
        if is_current_bst:
            # 如果当前子树是BST，则最大BST节点数 = 左子树节点数 + 右子树节点数 + 1
            current_max_bst = left_max_bst + right_max_bst + 1
        else:
            # 如果当前子树不是BST，则最大BST节点数 = max(左子树最大BST节点数, 右子树最大BST节点数)
            current_max_bst = max(left_max_bst, right_max_bst)
        
        return (current_max, current_min, is_current_bst, current_max_bst)

class OptimizedSolution:
    """优化版本：使用类属性存储信息"""
    
    def largestBSTSubtree(self, root: Optional[TreeNode]) -> int:
        """优化版本的最大BST子树计算"""
        self.max_size = 0
        self._dfs_optimized(root)
        return self.max_size
    
    def _dfs_optimized(self, node: Optional[TreeNode]) -> Tuple[bool, int, float, float]:
        """
        优化版本的DFS
        
        Returns:
            Tuple[bool, int, float, float]: 
                - 是否为BST
                - 节点数
                - 最小值
                - 最大值
        """
        if node is None:
            return (True, 0, float('inf'), float('-inf'))
        
        left_is_bst, left_size, left_min, left_max = self._dfs_optimized(node.left)
        right_is_bst, right_size, right_min, right_max = self._dfs_optimized(node.right)
        
        if left_is_bst and right_is_bst and left_max < node.val < right_min:
            current_size = left_size + right_size + 1
            self.max_size = max(self.max_size, current_size)
            current_min = min(left_min, node.val)
            current_max = max(right_max, node.val)
            return (True, current_size, current_min, current_max)
        else:
            return (False, 0, 0, 0)

class IterativeSolution:
    """迭代版本（避免递归栈溢出）"""
    
    def largestBSTSubtree(self, root: Optional[TreeNode]) -> int:
        """迭代版本的最大BST子树计算"""
        if root is None:
            return 0
            
        # 后序遍历收集所有节点
        nodes = []
        self._postorder_traversal(root, nodes)
        
        # 为每个节点存储信息
        info_map = {}
        max_size = 0
        
        for node in nodes:
            left_info = info_map.get(node.left, (float('inf'), float('-inf'), True, 0))
            right_info = info_map.get(node.right, (float('inf'), float('-inf'), True, 0))
            
            left_min, left_max, left_is_bst, left_max_bst = left_info
            right_min, right_max, right_is_bst, right_max_bst = right_info
            
            current_max = max(node.val, left_max, right_max)
            current_min = min(node.val, left_min, right_min)
            
            is_current_bst = (left_is_bst and right_is_bst and 
                            left_max < node.val < right_min)
            
            if is_current_bst:
                current_max_bst = left_max_bst + right_max_bst + 1
            else:
                current_max_bst = max(left_max_bst, right_max_bst)
                
            info_map[node] = (current_max, current_min, is_current_bst, current_max_bst)
            max_size = max(max_size, current_max_bst)
            
        return max_size
    
    def _postorder_traversal(self, node: Optional[TreeNode], nodes: list) -> None:
        """后序遍历"""
        if node is None:
            return
        self._postorder_traversal(node.left, nodes)
        self._postorder_traversal(node.right, nodes)
        nodes.append(node)

class TestLargestBstSubtree(unittest.TestCase):
    """单元测试类"""
    
    def test_empty_tree(self):
        """测试空树"""
        sol = Solution()
        result = sol.largestBSTSubtree(None)
        self.assertEqual(result, 0)
    
    def test_single_node(self):
        """测试单节点树"""
        root = TreeNode(5)
        sol = Solution()
        result = sol.largestBSTSubtree(root)
        self.assertEqual(result, 1)
    
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
        result = sol.largestBSTSubtree(root)
        self.assertEqual(result, 7)
    
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
        result = sol.largestBSTSubtree(root)
        self.assertEqual(result, 3)  # 最大的BST是右子树（3个节点）
    
    def test_mixed_bst(self):
        """测试混合BST"""
        # 构建混合BST:
        #       20
        #      /  \
        #     15   25
        #    /      \
        #   10      30
        #  / \      /
        # 5  12    28
        root = TreeNode(20)
        root.left = TreeNode(15)
        root.right = TreeNode(25)
        root.left.left = TreeNode(10)
        root.left.left.left = TreeNode(5)
        root.left.left.right = TreeNode(12)
        root.right.right = TreeNode(30)
        root.right.right.left = TreeNode(28)
        
        sol = Solution()
        result = sol.largestBSTSubtree(root)
        self.assertEqual(result, 5)  # 最大的BST是左子树的左子树（5个节点）

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
            root = TreeNode(n)
            root.left = build_large_tree(n // 2)
            root.right = build_large_tree(n // 2)
            return root
        
        large_tree = build_large_tree(10000)
        
        sol = Solution()
        start_time = time.time()
        result = sol.largestBSTSubtree(large_tree)
        end_time = time.time()
        
        print(f"大规模树测试: 结果={result}, 耗时={end_time - start_time:.4f}秒")

def main():
    """主函数"""
    # 运行单元测试
    unittest.main(argv=[''], exit=False, verbosity=2)
    
    # 运行性能测试
    PerformanceTest.test_large_tree()
    
    print("\n最大BST子树算法实现完成！")
    print("关键特性：")
    print("- 时间复杂度: O(n)")
    print("- 空间复杂度: O(h)")
    print("- 支持大规模树结构")
    print("- 处理边界情况")

if __name__ == "__main__":
    main()