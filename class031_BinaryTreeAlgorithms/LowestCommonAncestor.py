# LeetCode 236. Lowest Common Ancestor of a Binary Tree
# 题目链接: https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/
# 题目描述: 给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。
# 最近公共祖先的定义为："对于有根树 T 的两个节点 p、q，最近公共祖先表示为一个节点 x，
# 满足 x 是 p、q 的祖先且 x 的深度尽可能大。"
#
# 解题思路:
# 1. 使用递归深度优先搜索(DFS)
# 2. 对于每个节点，递归检查其左右子树
# 3. 如果当前节点是p或q，则直接返回当前节点
# 4. 如果左右子树分别找到了p和q，则当前节点就是LCA
# 5. 如果只在一侧子树找到了p或q，则返回找到的节点
#
# 时间复杂度: O(n) - n为树中节点的数量，最坏情况下需要遍历所有节点
# 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
# 是否为最优解: 是，这是寻找LCA的标准方法

from typing import Optional

# 二叉树节点定义
class TreeNode:
    def __init__(self, x):
        self.val = x
        self.left: Optional['TreeNode'] = None
        self.right: Optional['TreeNode'] = None

class Solution:
    def lowestCommonAncestor(self, root: Optional[TreeNode], p: TreeNode, q: TreeNode) -> Optional[TreeNode]:
        """
        找到二叉树中两个指定节点的最近公共祖先
        
        Args:
            root: 二叉树的根节点
            p: 目标节点p
            q: 目标节点q
            
        Returns:
            最近公共祖先节点
        """
        # 基本情况：空节点或找到p或q
        if root is None or root == p or root == q:
            return root
        
        # 递归在左右子树中查找p和q
        left = self.lowestCommonAncestor(root.left, p, q)
        right = self.lowestCommonAncestor(root.right, p, q)
        
        # 如果左右子树都找到了，说明当前节点是LCA
        if left is not None and right is not None:
            return root
        
        # 如果只在一侧找到了，返回找到的节点
        return left if left is not None else right

# 测试用例
def main():
    solution = Solution()

    # 构造测试用例:
    #       3
    #      / \
    #     5   1
    #    / \ / \
    #   6  2 0  8
    #     / \
    #    7   4
    root = TreeNode(3)
    root.left = TreeNode(5)
    root.right = TreeNode(1)
    root.left.left = TreeNode(6)
    root.left.right = TreeNode(2)
    root.right.left = TreeNode(0)
    root.right.right = TreeNode(8)
    root.left.right.left = TreeNode(7)
    root.left.right.right = TreeNode(4)
    
    p1 = root.left  # 节点5
    q1 = root.right # 节点1
    result1 = solution.lowestCommonAncestor(root, p1, q1)
    if result1 is not None:
        print("LCA(5, 1) =", result1.val)  # 应该输出3

    p2 = root.left        # 节点5
    q2 = root.left.right.right  # 节点4
    result2 = solution.lowestCommonAncestor(root, p2, q2)
    if result2 is not None:
        print("LCA(5, 4) =", result2.val)  # 应该输出5

if __name__ == "__main__":
    main()