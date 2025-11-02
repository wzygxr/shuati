# LeetCode 958. Check Completeness of a Binary Tree
# 题目链接: https://leetcode.cn/problems/check-completeness-of-a-binary-tree/
# 给定一个二叉树的 root ，确定它是否是一个完全二叉树
# 在一棵完全二叉树中，除了最后一层，其他层都被完全填满，
# 并且最后一层中的所有节点都尽可能靠左
#
# 解题思路:
# 1. 使用层序遍历(BFS)的方式遍历二叉树
# 2. 在遍历过程中，一旦遇到空节点，之后就不应该再有非空节点
# 3. 如果在空节点之后又遇到了非空节点，则不是完全二叉树
#
# 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点
# 空间复杂度: O(w) - w为树的最大宽度，队列中最多存储一层的节点
# 是否为最优解: 是，这是检查完全二叉树的标准方法

from typing import Optional
from collections import deque

# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

def isCompleteTree(root: Optional[TreeNode]) -> bool:
    """
    判断给定的二叉树是否是完全二叉树
    
    Args:
        root: 二叉树的根节点
        
    Returns:
        如果是完全二叉树返回True，否则返回False
    """
    if not root:
        return True
    
    # 使用队列进行层序遍历
    queue: deque[Optional[TreeNode]] = deque([root])
    found_null = False  # 标记是否遇到了空节点

    while queue:
        node = queue.popleft()

        if node is None:
            # 遇到空节点，标记为True
            found_null = True
        else:
            # 遇到非空节点
            if found_null:
                # 如果之前已经遇到过空节点，说明不是完全二叉树
                return False
            # 将左右子节点加入队列（即使是None也要加入）
            queue.append(node.left)
            queue.append(node.right)

    # 遍历完成，没有发现问题，是完全二叉树
    return True

# 测试用例
if __name__ == "__main__":
    # 测试用例1: 完全二叉树
    #       1
    #      / \
    #     2   3
    #    / \
    #   4   5
    root1 = TreeNode(1)
    root1.left = TreeNode(2)
    root1.right = TreeNode(3)
    root1.left.left = TreeNode(4)
    root1.left.right = TreeNode(5)
    print(f"完全二叉树测试: {isCompleteTree(root1)}")  # 应该输出True

    # 测试用例2: 非完全二叉树
    #       1
    #      / \
    #     2   3
    #    /     \
    #   4       5
    root2 = TreeNode(1)
    root2.left = TreeNode(2)
    root2.right = TreeNode(3)
    root2.left.left = TreeNode(4)
    root2.right.right = TreeNode(5)
    print(f"非完全二叉树测试: {isCompleteTree(root2)}")  # 应该输出False

    # 测试用例3: 完全二叉树
    #       1
    #      / \
    #     2   3
    #    / \
    #   4   5
    #  /
    # 6
    root3 = TreeNode(1)
    root3.left = TreeNode(2)
    root3.right = TreeNode(3)
    root3.left.left = TreeNode(4)
    root3.left.right = TreeNode(5)
    root3.left.left.left = TreeNode(6)
    print(f"完全二叉树测试2: {isCompleteTree(root3)}")  # 应该输出True