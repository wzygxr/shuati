# LeetCode 199. Binary Tree Right Side View
# 题目链接: https://leetcode.cn/problems/binary-tree-right-side-view/
# 题目描述: 给定一个二叉树的根节点 root，想象自己站在它的右侧，
# 按照从顶部到底部的顺序，返回从右侧所能看到的节点值。
#
# 解题思路:
# 1. BFS层序遍历：记录每层的最后一个节点
# 2. DFS递归遍历：先访问右子树，记录每层第一个访问到的节点
# 3. 使用队列进行BFS是更直观的解法
#
# 时间复杂度: O(n) - n为树中节点的数量，每个节点访问一次
# 空间复杂度: O(w) - w为树的最大宽度，队列中最多存储一层的节点
# 是否为最优解: 是，这是解决右视图问题的标准方法

from typing import List, Optional
from collections import deque

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def rightSideViewBFS(self, root: Optional[TreeNode]) -> List[int]:
        """
        BFS层序遍历方法获取二叉树的右视图
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            从右侧看到的节点值列表
        """
        result = []
        if root is None:
            return result
        
        queue = deque([root])
        
        while queue:
            level_size = len(queue)
            
            for i in range(level_size):
                node = queue.popleft()
                
                # 如果是当前层的最后一个节点，加入结果
                if i == level_size - 1:
                    result.append(node.val)
                
                # 将子节点加入队列
                if node.left is not None:
                    queue.append(node.left)
                if node.right is not None:
                    queue.append(node.right)
        
        return result

    def rightSideViewDFS(self, root: Optional[TreeNode]) -> List[int]:
        """
        DFS递归遍历方法获取二叉树的右视图
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            从右侧看到的节点值列表
        """
        result = []
        self._dfs(root, result, 0)
        return result
    
    def _dfs(self, node: Optional[TreeNode], result: List[int], depth: int) -> None:
        """
        DFS递归辅助函数
        
        Args:
            node: 当前节点
            result: 结果列表
            depth: 当前深度
        """
        if node is None:
            return
        
        # 如果当前深度还没有记录节点，说明这是该层第一个访问到的节点
        if depth == len(result):
            result.append(node.val)
        
        # 先递归右子树，再递归左子树
        self._dfs(node.right, result, depth + 1)
        self._dfs(node.left, result, depth + 1)

    def rightSideView(self, root: Optional[TreeNode]) -> List[int]:
        """
        获取二叉树的右视图（使用BFS版本，更直观）
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            从右侧看到的节点值列表
        """
        return self.rightSideViewBFS(root)

# 测试用例
def main():
    solution = Solution()

    # 测试用例1:
    #       1
    #      / \
    #     2   3
    #      \   \
    #       5   4
    # 右视图: [1, 3, 4]
    root1 = TreeNode(1)
    root1.left = TreeNode(2)
    root1.right = TreeNode(3)
    root1.left.right = TreeNode(5)
    root1.right.right = TreeNode(4)
    
    result1 = solution.rightSideView(root1)
    print(f"测试用例1结果: {result1}")  # 应该输出[1, 3, 4]

    # 测试用例2:
    #       1
    #      / 
    #     2
    #    /
    #   3
    # 右视图: [1, 2, 3]
    root2 = TreeNode(1)
    root2.left = TreeNode(2)
    root2.left.left = TreeNode(3)
    
    result2 = solution.rightSideView(root2)
    print(f"测试用例2结果: {result2}")  # 应该输出[1, 2, 3]

    # 测试用例3: 空树
    root3 = None
    result3 = solution.rightSideView(root3)
    print(f"测试用例3结果: {result3}")  # 应该输出[]

    # 测试用例4: 完全二叉树
    #       1
    #      / \
    #     2   3
    #    / \ / \
    #   4  5 6  7
    # 右视图: [1, 3, 7]
    root4 = TreeNode(1)
    root4.left = TreeNode(2)
    root4.right = TreeNode(3)
    root4.left.left = TreeNode(4)
    root4.left.right = TreeNode(5)
    root4.right.left = TreeNode(6)
    root4.right.right = TreeNode(7)
    
    result4 = solution.rightSideView(root4)
    print(f"测试用例4结果: {result4}")  # 应该输出[1, 3, 7]

    # 补充题目测试: 二叉树的左视图
    print("\n=== 补充题目测试: 二叉树的左视图 ===")
    
    def leftSideView(root: Optional[TreeNode]) -> List[int]:
        """
        获取二叉树的左视图
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            从左侧看到的节点值列表
        """
        result = []
        if root is None:
            return result
        
        queue = deque([root])
        
        while queue:
            level_size = len(queue)
            
            for i in range(level_size):
                node = queue.popleft()
                
                # 如果是当前层的第一个节点，加入结果
                if i == 0:
                    result.append(node.val)
                
                # 将子节点加入队列（先左后右）
                if node.left is not None:
                    queue.append(node.left)
                if node.right is not None:
                    queue.append(node.right)
        
        return result
    
    left_view_result = leftSideView(root4)
    print(f"左视图结果: {left_view_result}")  # 应该输出[1, 2, 4]

if __name__ == "__main__":
    main()