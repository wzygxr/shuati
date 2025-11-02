# LeetCode 226. Invert Binary Tree
# 题目链接: https://leetcode.cn/problems/invert-binary-tree/
# 题目描述: 给你一棵二叉树的根节点 root ，翻转这棵二叉树，并返回其根节点。
# 翻转二叉树就是将每个节点的左右子树进行交换。
#
# 解题思路:
# 1. 使用递归深度优先搜索(DFS)
# 2. 对于每个节点，先递归翻转其左右子树
# 3. 然后交换当前节点的左右子树
# 4. 返回翻转后的根节点
#
# 时间复杂度: O(n) - n为树中节点的数量，需要访问每个节点
# 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
# 是否为最优解: 是，这是翻转二叉树的标准方法

from typing import Optional

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def invertTree(self, root: Optional[TreeNode]) -> Optional[TreeNode]:
        """
        翻转二叉树
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            翻转后的根节点
        """
        # 基本情况：空节点直接返回
        if root is None:
            return None
        
        # 递归翻转左右子树
        left = self.invertTree(root.left)
        right = self.invertTree(root.right)
        
        # 交换左右子树
        root.left = right
        root.right = left
        
        return root

# 测试用例
def main():
    solution = Solution()

    # 测试用例1:
    # 原始树:
    #       4
    #      / \
    #     2   7
    #    / \ / \
    #   1  3 6  9
    #
    # 翻转后:
    #       4
    #      / \
    #     7   2
    #    / \ / \
    #   9  6 3  1
    root1 = TreeNode(4)
    root1.left = TreeNode(2)
    root1.right = TreeNode(7)
    root1.left.left = TreeNode(1)
    root1.left.right = TreeNode(3)
    root1.right.left = TreeNode(6)
    root1.right.right = TreeNode(9)
    
    print("翻转前:")
    print_tree(root1)
    
    inverted_root1 = solution.invertTree(root1)
    print("\n翻转后:")
    print_tree(inverted_root1)

# 辅助方法：打印二叉树（前序遍历）
def print_tree(root: Optional[TreeNode]) -> None:
    if root is None:
        print("null", end=" ")
        return
    print(root.val, end=" ")
    print_tree(root.left)
    print_tree(root.right)

if __name__ == "__main__":
    main()