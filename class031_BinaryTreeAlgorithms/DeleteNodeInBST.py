# LeetCode 450. Delete Node in a BST
# 题目链接: https://leetcode.cn/problems/delete-node-in-a-bst/
# 给定一个二叉搜索树的根节点 root 和一个值 key，
# 删除二叉搜索树中值等于 key 的节点，保持二叉搜索树的性质不变
# 返回二叉搜索树（有可能被更新）的根节点的引用
#
# 解题思路:
# 1. 首先查找要删除的节点
# 2. 找到节点后，根据节点的子节点情况分三种情况处理：
#    - 情况1：节点没有子节点（叶子节点），直接删除
#    - 情况2：节点只有一个子节点，用子节点替换该节点
#    - 情况3：节点有两个子节点，找到右子树中的最小节点（后继节点）替换该节点
#
# 时间复杂度: O(h) - h为树的高度，查找和删除操作都需要沿着树的高度进行
# 空间复杂度: O(h) - 递归调用栈的深度
# 是否为最优解: 是，这是删除BST节点的标准方法

from typing import Optional

# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

def deleteNode(root: Optional[TreeNode], key: int) -> Optional[TreeNode]:
    """
    删除二叉搜索树中值等于key的节点
    
    Args:
        root: 二叉搜索树的根节点
        key: 要删除的节点值
        
    Returns:
        删除节点后的二叉搜索树根节点
    """
    if root is None:
        return None

    # 递归查找要删除的节点
    if key < root.val:
        # key小于当前节点值，在左子树中查找
        root.left = deleteNode(root.left, key)
    elif key > root.val:
        # key大于当前节点值，在右子树中查找
        root.right = deleteNode(root.right, key)
    else:
        # 找到要删除的节点
        if root.left is None:
            # 情况1和情况2：节点没有左子树，直接返回右子树
            return root.right
        elif root.right is None:
            # 情况1和情况2：节点没有右子树，直接返回左子树
            return root.left
        else:
            # 情况3：节点有两个子节点
            # 找到右子树中的最小节点（后继节点）
            successor = findMin(root.right)
            # 用后继节点的值替换当前节点的值
            root.val = successor.val
            # 删除右子树中的后继节点
            root.right = deleteNode(root.right, successor.val)

    return root

def findMin(node: TreeNode) -> TreeNode:
    """
    查找以node为根的子树中的最小节点
    
    Args:
        node: 子树的根节点
        
    Returns:
        最小节点
    """
    while node.left is not None:
        node = node.left
    return node

# 测试用例
if __name__ == "__main__":
    # 构造测试用例:
    #       5
    #      / \
    #     3   6
    #    / \   \
    #   2   4   7
    root = TreeNode(5)
    root.left = TreeNode(3)
    root.right = TreeNode(6)
    root.left.left = TreeNode(2)
    root.left.right = TreeNode(4)
    root.right.right = TreeNode(7)

    # 删除节点3（有两个子节点）
    result = deleteNode(root, 3)
    if result:
        print(f"删除节点3后的树根节点值: {result.val}")  # 应该输出5

    # 删除节点0（不存在的节点）
    result2 = deleteNode(root, 0)
    if result2:
        print(f"删除不存在的节点0后的树根节点值: {result2.val}")  # 应该输出5

    # 删除节点2（叶子节点）
    result3 = deleteNode(root, 2)
    if result3:
        print(f"删除节点2后的树根节点值: {result3.val}")  # 应该输出5