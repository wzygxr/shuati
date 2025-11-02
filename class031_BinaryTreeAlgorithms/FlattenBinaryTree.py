# LeetCode 114. Flatten Binary Tree to Linked List
# 题目链接: https://leetcode.cn/problems/flatten-binary-tree-to-linked-list/
# 题目描述: 给你二叉树的根结点 root ，请你将它展开为一个单链表：
# - 展开后的单链表应该同样使用 TreeNode ，其中 right 子指针指向链表中下一个结点，而左子指针始终为 null 。
# - 展开后的单链表应该与二叉树先序遍历顺序相同。
#
# 解题思路:
# 1. 递归方法：后序遍历，先处理左右子树，再将左子树插入到根节点和右子树之间
# 2. 迭代方法：使用前序遍历，将节点按顺序连接
# 3. Morris遍历：O(1)空间复杂度的解法
#
# 时间复杂度: O(n) - n为树中节点的数量
# 空间复杂度: 
#   - 递归: O(h) - h为树的高度
#   - 迭代: O(n) - 需要存储前序遍历结果
#   - Morris: O(1) - 只使用常数空间
# 是否为最优解: Morris遍历是最优解，空间复杂度O(1)

from typing import Optional

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def flattenRecursive(self, root: Optional[TreeNode]) -> None:
        """
        递归方法展开二叉树为链表
        
        Args:
            root: 二叉树的根节点
        """
        if root is None:
            return
        
        # 递归处理左右子树
        self.flattenRecursive(root.left)
        self.flattenRecursive(root.right)
        
        # 保存右子树
        right = root.right
        
        # 将左子树移到右子树位置
        root.right = root.left
        root.left = None
        
        # 找到当前右子树（原左子树）的最后一个节点
        current = root
        while current.right is not None:
            current = current.right
        
        # 将原右子树接到末尾
        current.right = right

    def flattenIterative(self, root: Optional[TreeNode]) -> None:
        """
        迭代方法展开二叉树为链表
        
        Args:
            root: 二叉树的根节点
        """
        if root is None:
            return
        
        stack = [root]
        prev = None
        
        while stack:
            current = stack.pop()
            
            if prev is not None:
                prev.right = current
                prev.left = None
            
            # 先右后左入栈，保证出栈顺序是前序遍历
            if current.right is not None:
                stack.append(current.right)
            if current.left is not None:
                stack.append(current.left)
            
            prev = current

    def flattenMorris(self, root: Optional[TreeNode]) -> None:
        """
        Morris遍历方法展开二叉树为链表（最优解）
        
        Args:
            root: 二叉树的根节点
        """
        current = root
        
        while current is not None:
            if current.left is not None:
                # 找到当前节点左子树的最右节点（前驱节点）
                predecessor = current.left
                while predecessor.right is not None:
                    predecessor = predecessor.right
                
                # 将当前节点的右子树接到前驱节点的右边
                predecessor.right = current.right
                # 将左子树移到右子树位置
                current.right = current.left
                current.left = None
            
            # 移动到下一个节点
            current = current.right

    def flatten(self, root: Optional[TreeNode]) -> None:
        """
        展开二叉树为链表（使用Morris遍历，最优解）
        
        Args:
            root: 二叉树的根节点
        """
        self.flattenMorris(root)

# 测试用例
def main():
    solution = Solution()

    # 测试用例1:
    #       1
    #      / \
    #     2   5
    #    / \   \
    #   3   4   6
    # 展开后: 1->2->3->4->5->6
    root1 = TreeNode(1)
    root1.left = TreeNode(2)
    root1.right = TreeNode(5)
    root1.left.left = TreeNode(3)
    root1.left.right = TreeNode(4)
    root1.right.right = TreeNode(6)
    
    print("原始树结构:")
    print_tree(root1)
    
    solution.flatten(root1)
    print("展开后链表:")
    print_flattened(root1)  # 应该输出: 1 2 3 4 5 6

    # 测试用例2: 空树
    root2 = None
    solution.flatten(root2)
    print("空树展开结果:")
    print_flattened(root2)  # 应该输出空行

    # 测试用例3: 只有左子树的树
    #       1
    #      /
    #     2
    #    /
    #   3
    root3 = TreeNode(1)
    root3.left = TreeNode(2)
    root3.left.left = TreeNode(3)
    
    solution.flatten(root3)
    print("只有左子树展开结果:")
    print_flattened(root3)  # 应该输出: 1 2 3

def print_tree(root: Optional[TreeNode]) -> None:
    """打印二叉树（前序遍历）"""
    if root is None:
        return
    print(root.val, end=" ")
    print_tree(root.left)
    print_tree(root.right)

def print_flattened(root: Optional[TreeNode]) -> None:
    """打印展开后的链表"""
    current = root
    while current is not None:
        print(current.val, end=" ")
        current = current.right
    print()

if __name__ == "__main__":
    main()