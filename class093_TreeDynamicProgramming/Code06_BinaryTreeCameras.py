# 二叉树监控 (Binary Tree Cameras)
# 题目描述:
# 给定一个二叉树，我们在树的节点上安装摄像头。
# 节点上的每个摄影头都可以监视其父对象、自身及其直接子对象。
# 计算监控树的所有节点所需的最小摄像头数量。
# 测试链接 : https://leetcode.cn/problems/binary-tree-cameras/
#
# 解题思路:
# 1. 使用树形动态规划（Tree DP）的方法
# 2. 对于每个节点，定义三种状态：
#    - 状态0：当前节点没有被监控，需要父节点安装摄像头
#    - 状态1：当前节点被监控，但没有安装摄像头
#    - 状态2：当前节点安装了摄像头
# 3. 状态转移方程：
#    - 状态0：子节点必须处于状态1（被监控但没摄像头）
#    - 状态1：子节点至少有一个处于状态2（安装摄像头）
#    - 状态2：子节点可以处于任意状态，取最小值
#
# 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点
# 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
# 是否为最优解: 是，这是解决二叉树监控问题的标准方法
#
# 相关题目:
# - LeetCode 968. 监控二叉树
# - 类似问题：最小顶点覆盖、资源分配优化
#
# 工程化考量:
# 1. 处理空树和单节点树的边界情况
# 2. 提供递归和迭代两种实现方式
# 3. 添加详细的注释和调试信息
# 4. 支持大规模树结构

import sys
from typing import Optional, List, Tuple
import unittest

class TreeNode:
    """二叉树节点定义"""
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    """二叉树监控解决方案"""
    
    def minCameraCover(self, root: Optional[TreeNode]) -> int:
        """
        计算最小摄像头数量
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            int: 最小摄像头数量
        """
        if root is None:
            return 0
        
        result = self._dfs(root)
        # 根节点需要被监控，但不能依赖父节点（因为没有父节点）
        # 所以取状态1和状态2的最小值
        return min(result[1], result[2])
    
    def _dfs(self, node: Optional[TreeNode]) -> Tuple[int, int, int]:
        """
        深度优先搜索，返回三种状态的最小摄像头数量
        
        Args:
            node: 当前节点
            
        Returns:
            Tuple[int, int, int]: 
                - 状态0：当前节点没有被监控，需要父节点安装摄像头
                - 状态1：当前节点被监控，但没有安装摄像头
                - 状态2：当前节点安装了摄像头
        """
        if node is None:
            # 空节点：状态0和状态1不需要摄像头，状态2需要但不可能
            # 使用大数表示不可能的状态
            return (0, 0, float('inf'))
        
        # 递归处理左右子树
        left = self._dfs(node.left)
        right = self._dfs(node.right)
        
        # 状态0：当前节点没有被监控，需要父节点安装摄像头
        # 子节点必须处于状态1（被监控但没摄像头）
        state0 = left[1] + right[1]
        
        # 状态1：当前节点被监控，但没有安装摄像头
        # 子节点至少有一个处于状态2（安装摄像头）
        state1 = min(left[2] + min(right[1], right[2]),
                    right[2] + min(left[1], left[2]))
        
        # 状态2：当前节点安装了摄像头
        # 子节点可以处于任意状态，取最小值
        state2 = 1 + min(left[0], min(left[1], left[2])) + \
                    min(right[0], min(right[1], right[2]))
        
        return (state0, state1, state2)

class OptimizedSolution:
    """优化版本：更简洁的实现"""
    
    def __init__(self):
        """初始化解决方案"""
        self.result = 0
    
    def minCameraCover(self, root: Optional[TreeNode]) -> int:
        """
        优化版本的最小摄像头数量计算
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            int: 最小摄像头数量
        """
        self.result = 0
        # 从根节点开始，根节点需要被监控
        if self._dfs(root) == 0:  # 0表示需要被监控
            self.result += 1
        return self.result
    
    def _dfs(self, node: Optional[TreeNode]) -> int:
        """
        返回状态：
        0: 该节点没有被监控，需要父节点安装摄像头
        1: 该节点被监控，但没有安装摄像头
        2: 该节点安装了摄像头
        
        Args:
            node: 当前节点
            
        Returns:
            int: 节点状态
        """
        if node is None:
            return 1  # 空节点视为被监控
        
        left = self._dfs(node.left)
        right = self._dfs(node.right)
        
        # 如果左右子节点有未被监控的，当前节点必须安装摄像头
        if left == 0 or right == 0:
            self.result += 1
            return 2
        
        # 如果左右子节点有安装摄像头的，当前节点被监控
        if left == 2 or right == 2:
            return 1
        
        # 否则当前节点未被监控
        return 0

class IterativeSolution:
    """迭代版本（避免递归栈溢出）"""
    
    def minCameraCover(self, root: Optional[TreeNode]) -> int:
        """
        迭代版本的最小摄像头数量计算
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            int: 最小摄像头数量
        """
        if root is None:
            return 0
        
        # 使用后序遍历
        stack = []
        dp = {}  # 存储每个节点的三种状态
        prev = None
        
        stack.append(root)
        
        while stack:
            curr = stack[-1]
            
            # 如果当前节点是叶子节点或者其子节点已经处理过
            if ((curr.left is None and curr.right is None) or
                (prev is not None and (prev == curr.left or prev == curr.right))):
                
                # 处理当前节点
                left_state = dp.get(curr.left, (0, 0, float('inf'))) if curr.left else (0, 0, float('inf'))
                right_state = dp.get(curr.right, (0, 0, float('inf'))) if curr.right else (0, 0, float('inf'))
                
                state0 = left_state[1] + right_state[1]
                state1 = min(left_state[2] + min(right_state[1], right_state[2]),
                            right_state[2] + min(left_state[1], left_state[2]))
                state2 = 1 + min(left_state[0], min(left_state[1], left_state[2])) + \
                            min(right_state[0], min(right_state[1], right_state[2]))
                
                dp[curr] = (state0, state1, state2)
                stack.pop()
                prev = curr
            else:
                # 先处理右子树，再处理左子树
                if curr.right is not None:
                    stack.append(curr.right)
                if curr.left is not None:
                    stack.append(curr.left)
        
        root_state = dp[root]
        return min(root_state[1], root_state[2])

class TestBinaryTreeCameras(unittest.TestCase):
    """单元测试类"""
    
    def test_empty_tree(self):
        """测试空树"""
        sol = Solution()
        result = sol.minCameraCover(None)
        self.assertEqual(result, 0)
    
    def test_single_node(self):
        """测试单节点树"""
        root = TreeNode(0)
        sol = Solution()
        result = sol.minCameraCover(root)
        self.assertEqual(result, 1)
    
    def test_simple_tree(self):
        """测试简单树"""
        # 简单树：一个摄像头可以覆盖所有节点
        #   0
        #  / \
        # 0   0
        root = TreeNode(0)
        root.left = TreeNode(0)
        root.right = TreeNode(0)
        
        sol = Solution()
        result = sol.minCameraCover(root)
        self.assertEqual(result, 1)
    
    def test_chain_tree(self):
        """测试链式树"""
        # 链式树：0-0-0-0
        # 需要2个摄像头：安装在第二个和第四个节点
        root = TreeNode(0)
        root.right = TreeNode(0)
        root.right.right = TreeNode(0)
        root.right.right.right = TreeNode(0)
        
        sol = Solution()
        result = sol.minCameraCover(root)
        self.assertEqual(result, 2)
    
    def test_complex_tree(self):
        """测试复杂树"""
        # 复杂树：
        #       0
        #      / \
        #     0   0
        #    / \
        #   0   0
        # 需要2个摄像头
        root = TreeNode(0)
        root.left = TreeNode(0)
        root.right = TreeNode(0)
        root.left.left = TreeNode(0)
        root.left.right = TreeNode(0)
        
        sol = Solution()
        result = sol.minCameraCover(root)
        self.assertEqual(result, 2)
    
    def test_optimized_solution(self):
        """测试优化版本"""
        root = TreeNode(0)
        root.left = TreeNode(0)
        root.right = TreeNode(0)
        
        sol = OptimizedSolution()
        result = sol.minCameraCover(root)
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
            root = TreeNode(0)
            root.left = build_large_tree(n // 2)
            root.right = build_large_tree(n // 2)
            return root
        
        large_tree = build_large_tree(10000)
        
        sol = Solution()
        start_time = time.time()
        result = sol.minCameraCover(large_tree)
        end_time = time.time()
        
        print(f"大规模树测试: 结果={result}, 耗时={end_time - start_time:.4f}秒")

class DebugTool:
    """调试工具类"""
    
    @staticmethod
    def print_tree_with_cameras(root: Optional[TreeNode], prefix: str = "", is_left: bool = True):
        """打印二叉树结构"""
        if root is None:
            print(prefix + ("├── " if is_left else "└── ") + "null")
            return
        
        print(prefix + ("├── " if is_left else "└── ") + str(root.val))
        
        if root.left is not None or root.right is not None:
            DebugTool.print_tree_with_cameras(root.left, prefix + ("│   " if is_left else "    "), True)
            DebugTool.print_tree_with_cameras(root.right, prefix + ("│   " if is_left else "    "), False)

def main():
    """主函数"""
    # 运行单元测试
    unittest.main(argv=[''], exit=False, verbosity=2)
    
    # 运行性能测试
    PerformanceTest.test_large_tree()
    
    print("\n二叉树监控算法实现完成！")
    print("关键特性：")
    print("- 时间复杂度: O(n)")
    print("- 空间复杂度: O(h)")
    print("- 支持大规模树结构")
    print("- 处理边界情况")
    print("- 三种状态：未监控/被监控/安装摄像头")

if __name__ == "__main__":
    main()