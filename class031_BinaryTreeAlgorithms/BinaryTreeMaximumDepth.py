# LeetCode 104. Maximum Depth of Binary Tree
# 题目链接: https://leetcode.cn/problems/maximum-depth-of-binary-tree/
# 题目描述: 给定一个二叉树，找出其最大深度。
# 二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。
#
# 解题思路:
# 1. 递归方法：最大深度 = max(左子树深度, 右子树深度) + 1
# 2. BFS层序遍历：记录层数
# 3. DFS迭代遍历：使用栈模拟递归
#
# 时间复杂度: O(n) - n为树中节点的数量，每个节点访问一次
# 空间复杂度: 
#   - 递归: O(h) - h为树的高度，递归调用栈的深度
#   - BFS: O(w) - w为树的最大宽度
# 是否为最优解: 是，这是计算二叉树最大深度的标准方法

from typing import Optional
from collections import deque

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def maxDepthRecursive(self, root: Optional[TreeNode]) -> int:
        """
        递归方法计算二叉树的最大深度
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            二叉树的最大深度
        """
        if root is None:
            return 0
        
        left_depth = self.maxDepthRecursive(root.left)
        right_depth = self.maxDepthRecursive(root.right)
        
        return max(left_depth, right_depth) + 1

    def maxDepthBFS(self, root: Optional[TreeNode]) -> int:
        """
        BFS层序遍历方法计算二叉树的最大深度
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            二叉树的最大深度
        """
        if root is None:
            return 0
        
        queue = deque([root])
        depth = 0
        
        while queue:
            level_size = len(queue)
            depth += 1
            
            for _ in range(level_size):
                node = queue.popleft()
                
                if node.left is not None:
                    queue.append(node.left)
                if node.right is not None:
                    queue.append(node.right)
        
        return depth

    def maxDepthDFS(self, root: Optional[TreeNode]) -> int:
        """
        DFS迭代遍历方法计算二叉树的最大深度
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            二叉树的最大深度
        """
        if root is None:
            return 0
        
        node_stack = [root]
        depth_stack = [1]
        max_depth = 0
        
        while node_stack:
            node = node_stack.pop()
            current_depth = depth_stack.pop()
            max_depth = max(max_depth, current_depth)
            
            if node.right is not None:
                node_stack.append(node.right)
                depth_stack.append(current_depth + 1)
            if node.left is not None:
                node_stack.append(node.left)
                depth_stack.append(current_depth + 1)
        
        return max_depth

    def maxDepth(self, root: Optional[TreeNode]) -> int:
        """
        计算二叉树的最大深度（使用递归版本，最简洁）
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            二叉树的最大深度
        """
        return self.maxDepthRecursive(root)

# 测试用例
def main():
    solution = Solution()

    # 测试用例1:
    #       3
    #      / \
    #     9  20
    #       /  \
    #      15   7
    # 最大深度: 3
    root1 = TreeNode(3)
    root1.left = TreeNode(9)
    root1.right = TreeNode(20)
    root1.right.left = TreeNode(15)
    root1.right.right = TreeNode(7)
    
    result1 = solution.maxDepth(root1)
    print(f"测试用例1结果: {result1}")  # 应该输出3

    # 测试用例2: 单节点树
    root2 = TreeNode(1)
    result2 = solution.maxDepth(root2)
    print(f"测试用例2结果: {result2}")  # 应该输出1

    # 测试用例3: 空树
    root3 = None
    result3 = solution.maxDepth(root3)
    print(f"测试用例3结果: {result3}")  # 应该输出0

    # 测试用例4: 退化为链表的树
    #       1
    #        \
    #         2
    #          \
    #           3
    # 最大深度: 3
    root4 = TreeNode(1)
    root4.right = TreeNode(2)
    root4.right.right = TreeNode(3)
    
    result4 = solution.maxDepth(root4)
    print(f"测试用例4结果: {result4}")  # 应该输出3

    # 补充题目测试: 二叉树的最小深度
    print("\n=== 补充题目测试: 二叉树的最小深度 ===")
    
    def minDepth(root: Optional[TreeNode]) -> int:
        """
        计算二叉树的最小深度
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            二叉树的最小深度
        """
        if root is None:
            return 0
        
        # 如果是叶节点，深度为1
        if root.left is None and root.right is None:
            return 1
        
        min_depth = float('inf')
        
        # 只有左子树或只有右子树的情况需要特殊处理
        if root.left is not None:
            min_depth = min(min_depth, minDepth(root.left))
        if root.right is not None:
            min_depth = min(min_depth, minDepth(root.right))
        
        return min_depth + 1
    
    min_depth_result = minDepth(root1)
    print(f"最小深度结果: {min_depth_result}")  # 应该输出2

if __name__ == "__main__":
    main()