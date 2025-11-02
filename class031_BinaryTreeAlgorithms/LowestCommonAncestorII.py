# LintCode 474. Lowest Common Ancestor II
# 题目链接: https://www.lintcode.com/problem/474/
# 给定一个二叉树中的节点 a 和 b，找出他们的最近公共祖先
# 每个节点包含一个指向其父节点的指针
# 
# 解题思路:
# 1. 由于每个节点都有父节点指针，可以将问题转化为链表相交问题
# 2. 从节点a开始，沿着父节点指针向上遍历，记录所有访问过的节点
# 3. 从节点b开始，沿着父节点指针向上遍历，第一个已经在步骤2中访问过的节点就是LCA
#
# 时间复杂度: O(h) - h为树的高度
# 空间复杂度: O(h) - 需要存储从a到根节点的路径
# 是否为最优解: 是，利用父节点指针可以避免遍历整棵树

from typing import Optional

class ParentTreeNode:
    def __init__(self, val):
        self.val = val
        self.parent: Optional['ParentTreeNode'] = None
        self.left: Optional['ParentTreeNode'] = None
        self.right: Optional['ParentTreeNode'] = None

def lowestCommonAncestorII(root: Optional[ParentTreeNode], 
                          A: Optional[ParentTreeNode], 
                          B: Optional[ParentTreeNode]) -> Optional[ParentTreeNode]:
    """
    @param root: The root of the tree
    @param A: node in the tree
    @param B: node in the tree
    @return: The lowest common ancestor of A and B
    """
    # 使用集合记录从节点A到根节点的路径
    visited = set()
    
    # 从节点A开始向上遍历到根节点，记录路径上的所有节点
    current = A
    while current is not None:
        visited.add(current)
        current = current.parent
    
    # 从节点B开始向上遍历，第一个在visited集合中的节点就是LCA
    current = B
    while current is not None:
        if current in visited:
            return current  # 找到LCA
        current = current.parent
    
    # 理论上不会执行到这里，因为A和B都在树中，肯定有公共祖先
    return None

# 测试用例
if __name__ == "__main__":
    # 构造测试用例:
    #       4
    #      / \
    #     3   7
    #        / \
    #       5   6
    node4 = ParentTreeNode(4)
    node3 = ParentTreeNode(3)
    node7 = ParentTreeNode(7)
    node5 = ParentTreeNode(5)
    node6 = ParentTreeNode(6)

    # 设置父子关系
    node3.parent = node4
    node7.parent = node4
    node5.parent = node7
    node6.parent = node7

    node4.left = node3
    node4.right = node7
    node7.left = node5
    node7.right = node6

    # 测试LCA(3, 5) = 4
    result1 = lowestCommonAncestorII(node4, node3, node5)
    if result1:
        print(f"LCA(3, 5) = {result1.val}")  # 应该输出4

    # 测试LCA(5, 6) = 7
    result2 = lowestCommonAncestorII(node4, node5, node6)
    if result2:
        print(f"LCA(5, 6) = {result2.val}")  # 应该输出7

    # 测试LCA(6, 7) = 7
    result3 = lowestCommonAncestorII(node4, node6, node7)
    if result3:
        print(f"LCA(6, 7) = {result3.val}")  # 应该输出7