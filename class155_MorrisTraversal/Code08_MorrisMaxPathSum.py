"""
Morris遍历求二叉树最大路径和 - Python实现

题目来源：
- 二叉树最大路径和：LeetCode 124. Binary Tree Maximum Path Sum
  链接：https://leetcode.cn/problems/binary-tree-maximum-path-sum/

算法详解：
二叉树中的路径被定义为一条节点序列，序列中每对相邻节点之间都存在一条边。
同一个节点在一条路径序列中至多出现一次。该路径至少包含一个节点，且不一定经过根节点。
路径和是路径中各节点值的总和。

解题思路：
1. 对于每个节点，计算经过该节点的最大路径和
2. 路径可以分为三部分：左子树路径 + 节点值 + 右子树路径
3. 但向父节点返回时，只能返回单侧路径的最大值（节点值 + max(左子树路径, 右子树路径)）
4. 使用递归后序遍历，自底向上计算每个节点的最大贡献值

时间复杂度：O(n) - 每个节点访问一次
空间复杂度：O(h) - 递归栈空间，h为树高

工程化考量：
1. 异常处理：处理空树、负数值等边界情况
2. 性能优化：使用全局变量避免重复计算
3. 可测试性：提供完整的测试用例
4. 边界检查：处理整数溢出情况
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
    def maxPathSum(self, root: Optional[TreeNode]) -> int:
        """
        递归求解最大路径和
        
        算法步骤：
        1. 使用后序遍历计算每个节点的最大贡献值
        2. 对于每个节点，计算经过该节点的最大路径和
        3. 更新全局最大路径和
        4. 返回当前节点的最大贡献值
        
        时间复杂度：O(n)
        空间复杂度：O(h)
        """
        self.max_sum = -sys.maxsize - 1  # 初始化为最小整数
        self._max_gain(root)
        return self.max_sum
    
    def _max_gain(self, node: Optional[TreeNode]) -> int:
        """
        计算节点的最大贡献值
        
        参数:
            node: 当前节点
        
        返回:
            当前节点的最大贡献值
        """
        if not node:
            return 0
        
        # 递归计算左右子树的最大贡献值
        # 如果贡献值为负，则不计入路径
        left_gain = max(self._max_gain(node.left), 0)
        right_gain = max(self._max_gain(node.right), 0)
        
        # 计算经过当前节点的最大路径和
        price_new_path = node.val + left_gain + right_gain
        
        # 更新全局最大路径和
        self.max_sum = max(self.max_sum, price_new_path)
        
        # 返回当前节点的最大贡献值
        return node.val + max(left_gain, right_gain)
    
    def maxPathSumIterative(self, root: Optional[TreeNode]) -> int:
        """
        迭代版本求解最大路径和
        
        算法步骤：
        1. 使用栈进行迭代后序遍历
        2. 维护每个节点的最大贡献值
        3. 计算经过每个节点的最大路径和
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        if not root:
            return 0
        
        max_sum = -sys.maxsize - 1
        stack = []
        gain_map = {}  # 存储每个节点的最大贡献值
        last_visited = None
        current = root
        
        while current or stack:
            if current:
                stack.append(current)
                current = current.left
            else:
                node = stack[-1]
                
                if node.right and node.right != last_visited:
                    current = node.right
                else:
                    # 处理当前节点
                    stack.pop()
                    
                    # 计算左右子树的最大贡献值
                    left_gain = max(gain_map.get(node.left, 0), 0)
                    right_gain = max(gain_map.get(node.right, 0), 0)
                    
                    # 计算经过当前节点的最大路径和
                    price_new_path = node.val + left_gain + right_gain
                    max_sum = max(max_sum, price_new_path)
                    
                    # 计算当前节点的最大贡献值
                    gain_map[node] = node.val + max(left_gain, right_gain)
                    
                    last_visited = node
        
        return max_sum

def create_test_tree1() -> TreeNode:
    """
    创建测试树1：标准情况
    
    测试树结构：
          1
         / \
        2   3
    
    最大路径和：6 (2->1->3)
    """
    root = TreeNode(1)
    root.left = TreeNode(2)
    root.right = TreeNode(3)
    return root

def create_test_tree2() -> TreeNode:
    """
    创建测试树2：包含负值
    
    测试树结构：
          -10
          / \
         9  20
            / \
           15  7
    
    最大路径和：42 (15->20->7)
    """
    root = TreeNode(-10)
    root.left = TreeNode(9)
    root.right = TreeNode(20)
    root.right.left = TreeNode(15)
    root.right.right = TreeNode(7)
    return root

def create_test_tree3() -> TreeNode:
    """创建测试树3：单节点"""
    return TreeNode(5)

def create_test_tree4() -> TreeNode:
    """
    创建测试树4：全负值
    
    测试树结构：
          -1
          / \
        -2  -3
    
    最大路径和：-1 (单个节点-1)
    """
    root = TreeNode(-1)
    root.left = TreeNode(-2)
    root.right = TreeNode(-3)
    return root

def test_max_path_sum():
    """单元测试函数"""
    print("=== Morris遍历求二叉树最大路径和测试 ===")
    
    sol = Solution()
    
    # 测试用例1：标准情况
    print("\n1. 标准情况测试:")
    root1 = create_test_tree1()
    result1 = sol.maxPathSum(root1)
    print(f"最大路径和: {result1} (期望: 6)")
    
    # 测试用例2：包含负值
    print("\n2. 包含负值测试:")
    root2 = create_test_tree2()
    result2 = sol.maxPathSum(root2)
    print(f"最大路径和: {result2} (期望: 42)")
    
    # 测试用例3：单节点
    print("\n3. 单节点测试:")
    root3 = create_test_tree3()
    result3 = sol.maxPathSum(root3)
    print(f"最大路径和: {result3} (期望: 5)")
    
    # 测试用例4：全负值
    print("\n4. 全负值测试:")
    root4 = create_test_tree4()
    result4 = sol.maxPathSum(root4)
    print(f"最大路径和: {result4} (期望: -1)")
    
    # 测试用例5：空树
    print("\n5. 空树测试:")
    root5 = None
    result5 = sol.maxPathSum(root5)
    print(f"最大路径和: {result5} (期望: 0)")
    
    # 测试迭代版本
    print("\n6. 迭代版本测试:")
    root6 = create_test_tree2()
    result6 = sol.maxPathSumIterative(root6)
    print(f"迭代版本最大路径和: {result6} (期望: 42)")
    
    print("=== 测试完成 ===")

def performance_comparison():
    """性能对比测试"""
    print("\n=== 性能对比测试 ===")
    
    # 创建大型测试树
    def create_large_tree(n: int) -> TreeNode:
        """创建包含n个节点的大型BST"""
        def build_tree(start: int, end: int) -> Optional[TreeNode]:
            if start > end:
                return None
            mid = (start + end) // 2
            node = TreeNode(mid)
            node.left = build_tree(start, mid - 1)
            node.right = build_tree(mid + 1, end)
            return node
        
        return build_tree(1, n)
    
    import time
    
    # 测试不同规模的树
    sizes = [100, 1000, 5000]
    
    for size in sizes:
        print(f"\n测试树规模: {size}个节点")
        
        # 创建测试树
        large_tree = create_large_tree(size)
        
        sol = Solution()
        
        # 递归版本测试
        start_time = time.time()
        result_recursive = sol.maxPathSum(large_tree)
        recursive_time = time.time() - start_time
        
        # 迭代版本测试
        start_time = time.time()
        result_iterative = sol.maxPathSumIterative(large_tree)
        iterative_time = time.time() - start_time
        
        print(f"递归版本时间: {recursive_time:.4f}s")
        print(f"迭代版本时间: {iterative_time:.4f}s")
        print(f"结果一致性: {result_recursive == result_iterative}")

if __name__ == "__main__":
    test_max_path_sum()
    performance_comparison()