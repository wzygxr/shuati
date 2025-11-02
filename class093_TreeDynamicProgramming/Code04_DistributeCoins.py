# 分发硬币 (Distribute Coins in Binary Tree)
# 题目描述:
# 给定一个有 N 个结点的二叉树的根结点 root，树中的每个结点上都对应有 node.val 枚硬币，并且总共有 N 枚硬币。
# 在一次移动中，我们可以选择两个相邻的结点，然后将一枚硬币从其中一个结点移动到另一个结点。
# (移动可以是从父结点到子结点，或者从子结点到父结点。)
# 返回使每个结点上只有一枚硬币所需的最少移动次数。
# 测试链接 : https://leetcode.cn/problems/distribute-coins-in-binary-tree/
#
# 解题思路:
# 1. 使用树形动态规划（Tree DP）的方法
# 2. 对于每个节点，计算其硬币的盈余或赤字
# 3. 移动次数 = 所有节点的绝对盈余/赤字之和
# 4. 关键观察：每个硬币的移动都会经过一条边，移动次数等于所有边的硬币流动量
#
# 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点
# 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
# 是否为最优解: 是，这是计算最少移动次数的标准方法
#
# 相关题目:
# - LeetCode 979. 在二叉树中分配硬币
# - 类似问题：资源分配优化、负载均衡
#
# 工程化考量:
# 1. 处理空树和单节点树的边界情况
# 2. 支持负数值（赤字）的处理
# 3. 提供递归和迭代两种实现方式
# 4. 添加详细的注释和调试信息

import sys
from typing import Optional
import unittest

class TreeNode:
    """二叉树节点定义"""
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    """分发硬币解决方案"""
    
    def __init__(self):
        """初始化解决方案"""
        self.moves = 0
    
    def distributeCoins(self, root: Optional[TreeNode]) -> int:
        """
        计算最少移动次数
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            int: 最少移动次数
        """
        if root is None:
            return 0
            
        self.moves = 0  # 重置计数器
        self._dfs(root)
        return self.moves
    
    def _dfs(self, node: Optional[TreeNode]) -> int:
        """
        深度优先搜索，返回当前节点的硬币盈余（正数）或赤字（负数）
        
        Args:
            node: 当前节点
            
        Returns:
            int: 硬币盈余（正数）或赤字（负数）
        """
        if node is None:
            return 0
        
        # 递归处理左右子树
        left_balance = self._dfs(node.left)
        right_balance = self._dfs(node.right)
        
        # 当前节点的硬币流动量 = 左子树流动量 + 右子树流动量 + 当前节点硬币数 - 1
        # 因为每个节点最终需要恰好1枚硬币
        current_balance = left_balance + right_balance + node.val - 1
        
        # 移动次数增加当前节点的绝对流动量
        # 因为每个硬币的移动都会经过当前节点
        self.moves += abs(left_balance) + abs(right_balance)
        
        return current_balance

class OptimizedSolution:
    """优化版本：更简洁的实现"""
    
    def distributeCoins(self, root: Optional[TreeNode]) -> int:
        """优化版本的分发硬币计算"""
        moves = 0
        
        def dfs(node):
            nonlocal moves
            if node is None:
                return 0
                
            left = dfs(node.left)
            right = dfs(node.right)
            
            # 移动次数增加左右子树的绝对流动量
            moves += abs(left) + abs(right)
            
            # 返回当前节点的净流动量
            return left + right + node.val - 1
        
        dfs(root)
        return moves

class IterativeSolution:
    """迭代版本（避免递归栈溢出）"""
    
    def distributeCoins(self, root: Optional[TreeNode]) -> int:
        """迭代版本的分发硬币计算"""
        if root is None:
            return 0
            
        # 使用后序遍历
        stack = []
        balance_map = {}  # 存储每个节点的净流动量
        moves = 0
        prev = None
        
        stack.append(root)
        
        while stack:
            curr = stack[-1]
            
            # 如果当前节点是叶子节点或者其子节点已经处理过
            if ((curr.left is None and curr.right is None) or
                (prev is not None and (prev == curr.left or prev == curr.right))):
                
                # 处理当前节点
                left_balance = balance_map.get(curr.left, 0) if curr.left else 0
                right_balance = balance_map.get(curr.right, 0) if curr.right else 0
                current_balance = left_balance + right_balance + curr.val - 1
                
                # 更新移动次数
                moves += abs(left_balance) + abs(right_balance)
                
                # 存储当前节点的净流动量
                balance_map[curr] = current_balance
                stack.pop()
                prev = curr
            else:
                # 先处理右子树，再处理左子树
                if curr.right is not None:
                    stack.append(curr.right)
                if curr.left is not None:
                    stack.append(curr.left)
        
        return moves

class TestDistributeCoins(unittest.TestCase):
    """单元测试类"""
    
    def test_empty_tree(self):
        """测试空树"""
        sol = Solution()
        result = sol.distributeCoins(None)
        self.assertEqual(result, 0)
    
    def test_single_node_balanced(self):
        """测试单节点平衡"""
        root = TreeNode(1)  # 单节点，硬币数为1（已经平衡）
        sol = Solution()
        result = sol.distributeCoins(root)
        self.assertEqual(result, 0)
    
    def test_simple_imbalance(self):
        """测试简单不平衡"""
        # 简单不平衡树:
        # 根节点有3个硬币，左子节点有0个硬币
        # 需要移动2次：根节点移动2个硬币到左子节点
        #       3
        #      /
        #     0
        root = TreeNode(3)
        root.left = TreeNode(0)
        
        sol = Solution()
        result = sol.distributeCoins(root)
        self.assertEqual(result, 2)
    
    def test_complex_imbalance(self):
        """测试复杂不平衡"""
        # 复杂不平衡树:
        #       0
        #      / \
        #     3   0
        # 需要移动3次：左子节点移动2个硬币到根节点，根节点移动1个硬币到右子节点
        root = TreeNode(0)
        root.left = TreeNode(3)
        root.right = TreeNode(0)
        
        sol = Solution()
        result = sol.distributeCoins(root)
        self.assertEqual(result, 3)
    
    def test_all_deficit(self):
        """测试所有节点都需要硬币"""
        # 所有节点都需要硬币:
        #       0
        #      / \
        #     0   0
        # 需要从外部引入3个硬币，但题目保证总硬币数等于节点数
        # 实际上这种情况不会发生，因为总硬币数=节点数=3
        # 但初始分布为0,0,0，需要内部调整
        root = TreeNode(0)
        root.left = TreeNode(0)
        root.right = TreeNode(0)
        
        sol = Solution()
        result = sol.distributeCoins(root)
        self.assertEqual(result, 2)
    
    def test_all_surplus(self):
        """测试所有节点都有多余硬币"""
        # 所有节点都有多余硬币:
        #       3
        #      / \
        #     1   1
        # 需要移动4次：根节点移动2个硬币出去，每个子节点移动1个硬币出去
        root = TreeNode(3)
        root.left = TreeNode(1)
        root.right = TreeNode(1)
        
        sol = Solution()
        result = sol.distributeCoins(root)
        self.assertEqual(result, 4)

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
            root = TreeNode(1)  # 所有节点初始硬币数为1（平衡状态）
            root.left = build_large_tree(n // 2)
            root.right = build_large_tree(n // 2)
            return root
        
        large_tree = build_large_tree(10000)
        
        sol = Solution()
        start_time = time.time()
        result = sol.distributeCoins(large_tree)
        end_time = time.time()
        
        print(f"大规模树测试: 结果={result}, 耗时={end_time - start_time:.4f}秒")

class DebugTool:
    """调试工具类"""
    
    @staticmethod
    def print_tree_with_coins(root: Optional[TreeNode], prefix: str = "", is_left: bool = True):
        """打印二叉树硬币分布"""
        if root is None:
            print(prefix + ("├── " if is_left else "└── ") + "null")
            return
        
        print(prefix + ("├── " if is_left else "└── ") + f"[{root.val}]")
        
        if root.left is not None or root.right is not None:
            DebugTool.print_tree_with_coins(root.left, prefix + ("│   " if is_left else "    "), True)
            DebugTool.print_tree_with_coins(root.right, prefix + ("│   " if is_left else "    "), False)

def main():
    """主函数"""
    # 运行单元测试
    unittest.main(argv=[''], exit=False, verbosity=2)
    
    # 运行性能测试
    PerformanceTest.test_large_tree()
    
    print("\n分发硬币算法实现完成！")
    print("关键特性：")
    print("- 时间复杂度: O(n)")
    print("- 空间复杂度: O(h)")
    print("- 支持大规模树结构")
    print("- 处理边界情况")
    print("- 数学原理：移动次数 = Σ|节点硬币数 - 1|")

if __name__ == "__main__":
    main()