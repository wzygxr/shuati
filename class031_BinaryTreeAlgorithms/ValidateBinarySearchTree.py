# LeetCode 98. Validate Binary Search Tree
# 题目链接: https://leetcode.cn/problems/validate-binary-search-tree/
# 题目描述: 给你一个二叉树的根节点 root ，判断其是否是一个有效的二叉搜索树。
# 有效二叉搜索树定义如下：
# 节点的左子树只包含小于当前节点的数。
# 节点的右子树只包含大于当前节点的数。
# 所有左子树和右子树自身必须也是二叉搜索树。
#
# 解题思路:
# 1. 使用递归方法，为每个节点设置上下界
# 2. 对于根节点，上下界为无穷大和无穷小
# 3. 对于左子树，上界更新为当前节点值
# 4. 对于右子树，下界更新为当前节点值
# 5. 递归检查每个节点是否满足上下界约束
#
# 时间复杂度: O(n) - n为树中节点的数量，需要访问每个节点
# 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
# 是否为最优解: 是，这是验证BST的标准方法

from typing import Optional
import sys

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def isValidBST(self, root: Optional[TreeNode]) -> bool:
        """
        验证二叉搜索树
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            是否为有效的BST
        """
        return self._isValidBST(root, -sys.maxsize - 1, sys.maxsize)
    
    def _isValidBST(self, node: Optional[TreeNode], min_val: int, max_val: int) -> bool:
        """
        递归验证BST
        
        Args:
            node: 当前节点
            min_val: 下界（不包含）
            max_val: 上界（不包含）
            
        Returns:
            是否为有效的BST
        """
        # 空节点是有效的BST
        if node is None:
            return True
        
        # 检查当前节点是否满足上下界约束
        if node.val <= min_val or node.val >= max_val:
            return False
        
        # 递归检查左右子树
        # 左子树的上界更新为当前节点值
        # 右子树的下界更新为当前节点值
        return (self._isValidBST(node.left, min_val, node.val) and 
                self._isValidBST(node.right, node.val, max_val))

# 测试用例
def main():
    solution = Solution()

    # 测试用例1: 有效的BST
    #       2
    #      / \
    #     1   3
    root1 = TreeNode(2)
    root1.left = TreeNode(1)
    root1.right = TreeNode(3)
    result1 = solution.isValidBST(root1)
    print("测试用例1结果:", result1)  # 应该输出True

    # 测试用例2: 无效的BST
    #       5
    #      / \
    #     1   4
    #        / \
    #       3   6
    root2 = TreeNode(5)
    root2.left = TreeNode(1)
    root2.right = TreeNode(4)
    root2.right.left = TreeNode(3)
    root2.right.right = TreeNode(6)
    result2 = solution.isValidBST(root2)
    print("测试用例2结果:", result2)  # 应该输出False

    # 测试用例3: 无效的BST（相同值）
    #       1
    #      / \
    #     1   1
    root3 = TreeNode(1)
    root3.left = TreeNode(1)
    root3.right = TreeNode(1)
    result3 = solution.isValidBST(root3)
    print("测试用例3结果:", result3)  # 应该输出False

if __name__ == "__main__":
    main()