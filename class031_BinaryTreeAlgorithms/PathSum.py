# LeetCode 112. Path Sum
# 题目链接: https://leetcode.cn/problems/path-sum/
# 给你二叉树的根节点 root 和一个表示目标和的整数 targetSum
# 判断该树中是否存在根节点到叶子节点的路径，这条路径上所有节点值相加等于目标和 targetSum
# 叶子节点是指没有子节点的节点
#
# 解题思路:
# 1. 使用递归深度优先搜索(DFS)
# 2. 从根节点开始，每访问一个节点，将目标和减去当前节点的值
# 3. 当到达叶子节点时，检查剩余的目标和是否等于当前节点的值
# 4. 递归检查左右子树是否存在满足条件的路径
#
# 时间复杂度: O(n) - n为树中节点的数量，最坏情况下需要遍历所有节点
# 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
# 是否为最优解: 是，这是解决此类路径问题的标准方法

from typing import Optional

# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

def hasPathSum(root: Optional[TreeNode], targetSum: int) -> bool:
    """
    判断二叉树中是否存在从根到叶子节点的路径，其节点值之和等于目标和
    
    Args:
        root: 二叉树的根节点
        targetSum: 目标和
        
    Returns:
        如果存在满足条件的路径返回True，否则返回False
    """
    # 边界情况：空树
    if root is None:
        return False

    # 到达叶子节点，检查路径和是否等于目标和
    if root.left is None and root.right is None:
        return root.val == targetSum

    # 递归检查左右子树，目标和减去当前节点的值
    return hasPathSum(root.left, targetSum - root.val) or \
           hasPathSum(root.right, targetSum - root.val)

# 测试用例
if __name__ == "__main__":
    # 构造测试用例:
    #       5
    #      / \
    #     4   8
    #    /   / \
    #   11  13  4
    #  / \       \
    # 7   2       1
    root = TreeNode(5)
    root.left = TreeNode(4)
    root.right = TreeNode(8)
    root.left.left = TreeNode(11)
    root.right.left = TreeNode(13)
    root.right.right = TreeNode(4)
    root.left.left.left = TreeNode(7)
    root.left.left.right = TreeNode(2)
    root.right.right.right = TreeNode(1)

    # 测试 targetSum = 22, 应该返回 true (5->4->11->2)
    result1 = hasPathSum(root, 22)
    print(f"targetSum=22: {result1}")  # 应该输出True

    # 测试 targetSum = 26, 应该返回 true (5->8->13)
    result2 = hasPathSum(root, 26)
    print(f"targetSum=26: {result2}")  # 应该输出True

    # 测试 targetSum = 19, 应该返回 true (5->8->4->1)
    result3 = hasPathSum(root, 19)
    print(f"targetSum=19: {result3}")  # 应该输出True

    # 测试 targetSum = 10, 应该返回 false
    result4 = hasPathSum(root, 10)
    print(f"targetSum=10: {result4}")  # 应该输出False