# LeetCode 426. Convert Binary Search Tree to Sorted Doubly Linked List
# 题目链接: https://leetcode.com/problems/convert-binary-search-tree-to-sorted-doubly-linked-list/
# 题目描述: 将一个二叉搜索树就地转换为已排序的循环双向链表。
# 对于双向循环链表，左右子指针分别作为前驱和后继指针。
#
# 解题思路:
# 1. 利用BST的中序遍历特性，可以按升序访问所有节点
# 2. 在中序遍历过程中，维护前一个访问的节点(prev)
# 3. 将当前节点与前一个节点连接起来
# 4. 遍历完成后，将首尾节点连接形成循环链表
#
# 时间复杂度: O(n) - n为树中节点的数量，每个节点访问一次
# 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
# 是否为最优解: 是，这是将BST转换为排序双向链表的标准解法

from typing import Optional

# 二叉树节点定义
class Node:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

def treeToDoublyList(root: Optional['Node']) -> Optional['Node']:
    """
    将二叉搜索树转换为排序的循环双向链表
    
    Args:
        root: 二叉搜索树的根节点
        
    Returns:
        排序的循环双向链表的头节点
    """
    # 边界条件：空树
    if not root:
        return None
    
    # 用于跟踪前一个节点和头节点
    prev: list[Optional[Node]] = [None]  # 使用列表来模拟引用传递
    head: list[Optional[Node]] = [None]
    
    def inorder_traversal(node: Optional['Node']) -> None:
        """
        中序遍历并构建双向链表
        
        Args:
            node: 当前节点
        """
        # 基础情况：空节点
        if not node:
            return
        
        # 递归处理左子树
        inorder_traversal(node.left)
        
        # 处理当前节点
        if prev[0] is None:
            # 第一个节点，设置为头节点
            head[0] = node
        else:
            # 将当前节点与前一个节点连接
            assert prev[0] is not None
            prev[0].right = node
            node.left = prev[0]
        
        # 更新前一个节点
        prev[0] = node
        
        # 递归处理右子树
        inorder_traversal(node.right)
    
    # 中序遍历构建双向链表
    inorder_traversal(root)
    
    # 连接首尾节点形成循环链表
    if head[0] is not None and prev[0] is not None:
        prev[0].right = head[0]
        head[0].left = prev[0]
    
    return head[0]

# 测试用例
if __name__ == "__main__":
    # 测试用例1:
    # 构建BST:
    #       4
    #      / \
    #     2   5
    #    / \
    #   1   3
    root1 = Node(4)
    root1.left = Node(2)
    root1.right = Node(5)
    root1.left.left = Node(1)
    root1.left.right = Node(3)
    
    result1 = treeToDoublyList(root1)
    
    # 遍历循环双向链表验证结果
    print("测试用例1结果: ", end="")
    if result1:
        current = result1
        while True:
            print(current.val, end=" ")
            current = current.right
            if current is not None and current == result1:
                break
    print()  # 应该输出: 1 2 3 4 5
    
    # 测试用例2: 空树
    root2 = None
    result2 = treeToDoublyList(root2)
    print("测试用例2结果:", result2)  # 应该输出: None
    
    # 测试用例3: 只有根节点
    root3 = Node(1)
    result3 = treeToDoublyList(root3)
    print("测试用例3结果: ", end="")
    if result3:
        current = result3
        while True:
            print(current.val, end=" ")
            current = current.right
            if current is not None and current == result3:
                break
    print()  # 应该输出: 1