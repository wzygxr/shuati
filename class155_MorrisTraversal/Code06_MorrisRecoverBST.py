"""
Morris遍历恢复二叉搜索树 - Python实现

题目来源：
- 恢复BST：LeetCode 99. Recover Binary Search Tree
  链接：https://leetcode.cn/problems/recover-binary-search-tree/

算法详解：
利用BST的中序遍历结果应该是严格递增的特性，通过Morris中序遍历找到被错误交换的两个节点并恢复
1. 使用Morris中序遍历访问BST
2. 在遍历过程中找到违反BST性质的节点对
3. 记录第一对和最后一对违反BST性质的节点
4. 交换这两个节点的值，恢复BST

时间复杂度：O(n) - 每个节点最多被访问两次
空间复杂度：O(1) - 不使用额外空间
适用场景：内存受限环境中恢复大规模BST、在线算法恢复BST

工程化考量：
1. 异常处理：检查空树、单节点树等边界情况
2. 线程安全：非线程安全，需要外部同步
3. 性能优化：使用Morris遍历避免递归栈空间
4. 可测试性：提供完整的测试用例
"""

from typing import Optional, List
import sys

class TreeNode:
    """二叉树节点定义"""
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def recoverTree(self, root: Optional[TreeNode]) -> None:
        """
        Morris中序遍历恢复BST
        
        算法步骤：
        1. 使用Morris中序遍历BST
        2. 在遍历过程中检测违反BST性质的节点对
        3. 记录需要交换的两个节点
        4. 交换节点值恢复BST
        
        时间复杂度：O(n)
        空间复杂度：O(1)
        """
        if not root:
            return
        
        first, second = None, None  # 记录需要交换的两个节点
        prev = None  # 记录前一个访问的节点
        current = root  # 当前节点
        
        while current:
            if not current.left:
                # 如果没有左子树，访问当前节点
                if prev and prev.val > current.val:
                    if not first:
                        first = prev
                    second = current
                prev = current
                current = current.right
            else:
                # 找到当前节点的前驱节点
                predecessor = current.left
                while predecessor.right and predecessor.right != current:
                    predecessor = predecessor.right
                
                if not predecessor.right:
                    # 建立临时链接
                    predecessor.right = current
                    current = current.left
                else:
                    # 断开临时链接并访问当前节点
                    predecessor.right = None
                    if prev and prev.val > current.val:
                        if not first:
                            first = prev
                        second = current
                    prev = current
                    current = current.right
        
        # 交换两个节点的值
        if first and second:
            first.val, second.val = second.val, first.val
    
    def recoverTreeRecursive(self, root: Optional[TreeNode]) -> None:
        """
        递归版本恢复BST
        
        算法步骤：
        1. 使用递归中序遍历BST
        2. 在遍历过程中检测违反BST性质的节点对
        3. 记录需要交换的两个节点
        4. 交换节点值恢复BST
        
        时间复杂度：O(n)
        空间复杂度：O(h) - 递归栈空间，h为树的高度
        """
        def inorder_recursive(node: Optional[TreeNode]) -> None:
            nonlocal prev, first, second
            if not node:
                return
            
            inorder_recursive(node.left)
            
            if prev and prev.val > node.val:
                if not first:
                    first = prev
                second = node
            prev = node
            
            inorder_recursive(node.right)
        
        prev, first, second = None, None, None
        inorder_recursive(root)
        
        if first and second:
            first.val, second.val = second.val, first.val
    
    def recoverTreeIterative(self, root: Optional[TreeNode]) -> None:
        """
        迭代版本恢复BST
        
        算法步骤：
        1. 使用栈进行迭代中序遍历
        2. 在遍历过程中检测违反BST性质的节点对
        3. 记录需要交换的两个节点
        4. 交换节点值恢复BST
        
        时间复杂度：O(n)
        空间复杂度：O(h) - 栈空间，h为树的高度
        """
        if not root:
            return
        
        stack = []
        current = root
        prev, first, second = None, None, None
        
        while current or stack:
            while current:
                stack.append(current)
                current = current.left
            
            current = stack.pop()
            
            if prev and prev.val > current.val:
                if not first:
                    first = prev
                second = current
            prev = current
            current = current.right
        
        if first and second:
            first.val, second.val = second.val, first.val

def create_test_tree() -> TreeNode:
    """
    创建测试树
    
    测试树结构：
          3
         / \
        1   4
           /
          2
    
    中序遍历：1, 3, 2, 4 (错误交换了3和2)
    """
    root = TreeNode(3)
    root.left = TreeNode(1)
    root.right = TreeNode(4)
    root.right.left = TreeNode(2)
    return root

def is_valid_bst(root: Optional[TreeNode]) -> bool:
    """验证BST是否有效"""
    def is_valid_helper(node: Optional[TreeNode], min_val: float, max_val: float) -> bool:
        if not node:
            return True
        if node.val <= min_val or node.val >= max_val:
            return False
        return (is_valid_helper(node.left, min_val, node.val) and 
                is_valid_helper(node.right, node.val, max_val))
    
    return is_valid_helper(root, float('-inf'), float('inf'))

def inorder_print(root: Optional[TreeNode]) -> None:
    """中序遍历打印"""
    def inorder_helper(node: Optional[TreeNode]) -> None:
        if not node:
            return
        inorder_helper(node.left)
        print(node.val, end=' ')
        inorder_helper(node.right)
    
    inorder_helper(root)
    print()

def test_recover_bst():
    """单元测试函数"""
    print("=== Morris遍历恢复BST测试 ===")
    
    # 测试用例1：正常情况
    root1 = create_test_tree()
    print("原始树中序遍历: ", end='')
    inorder_print(root1)
    
    sol = Solution()
    sol.recoverTree(root1)
    
    print("恢复后中序遍历: ", end='')
    inorder_print(root1)
    
    print("是否有效BST:", "是" if is_valid_bst(root1) else "否")
    
    # 测试用例2：边界情况 - 空树
    root2 = None
    sol.recoverTree(root2)
    print("空树测试通过")
    
    # 测试用例3：单节点树
    root3 = TreeNode(1)
    sol.recoverTree(root3)
    print("单节点树测试通过")
    
    # 测试用例4：两个节点交换
    root4 = TreeNode(2)
    root4.left = TreeNode(3)
    root4.right = TreeNode(1)
    print("交换前中序遍历: ", end='')
    inorder_print(root4)
    sol.recoverTree(root4)
    print("交换后中序遍历: ", end='')
    inorder_print(root4)
    print("是否有效BST:", "是" if is_valid_bst(root4) else "否")
    
    print("=== 测试完成 ===")

if __name__ == "__main__":
    test_recover_bst()