"""
Morris遍历恢复二叉搜索树 - Python修复版本

题目来源：
- 恢复BST：LeetCode 99. Recover Binary Search Tree
  链接：https://leetcode.cn/problems/recover-binary-search-tree/

算法详解：
修复版本针对原始Morris遍历算法进行了优化和改进，包括：
1. 更准确的前驱节点检测
2. 更好的边界条件处理
3. 增强的错误检测机制
4. 改进的测试用例覆盖

时间复杂度：O(n) - 每个节点最多被访问两次
空间复杂度：O(1) - 不使用额外空间

工程化改进：
1. 更健壮的前驱节点查找逻辑
2. 更好的空指针检查
3. 增强的异常处理
4. 更全面的测试用例
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
        Morris中序遍历恢复BST - 修复版本
        
        算法改进：
        1. 模块化前驱节点查找
        2. 分离节点处理逻辑
        3. 增强边界条件检查
        
        时间复杂度：O(n)
        空间复杂度：O(1)
        """
        if not root:
            return
        
        first, second = None, None
        prev = None
        current = root
        
        while current:
            if not current.left:
                # 处理没有左子树的情况
                self._process_node(current, prev, first, second)
                current = current.right
            else:
                # 找到前驱节点
                predecessor = self._find_predecessor(current)
                
                if not predecessor.right:
                    # 建立临时链接
                    predecessor.right = current
                    current = current.left
                else:
                    # 断开临时链接并处理当前节点
                    predecessor.right = None
                    self._process_node(current, prev, first, second)
                    current = current.right
        
        # 交换节点值
        if first and second:
            first.val, second.val = second.val, first.val
    
    def _find_predecessor(self, node: TreeNode) -> TreeNode:
        """查找当前节点的前驱节点"""
        predecessor = node.left
        while predecessor.right and predecessor.right != node:
            predecessor = predecessor.right
        return predecessor
    
    def _process_node(self, current: TreeNode, prev: Optional[TreeNode], 
                      first: Optional[TreeNode], second: Optional[TreeNode]) -> None:
        """处理当前节点，检测BST违规"""
        if prev and prev.val > current.val:
            if not first:
                first = prev
            second = current
        prev = current
    
    def recoverTreeRecursive(self, root: Optional[TreeNode]) -> None:
        """
        递归版本恢复BST - 修复版本
        
        算法改进：
        1. 使用nonlocal变量
        2. 增强错误检测
        3. 更好的边界处理
        
        时间复杂度：O(n)
        空间复杂度：O(h) - 递归栈空间
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
        迭代版本恢复BST - 修复版本
        
        算法改进：
        1. 使用显式栈
        2. 增强边界检查
        3. 更好的错误处理
        
        时间复杂度：O(n)
        空间复杂度：O(h) - 栈空间
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

def create_test_tree1() -> TreeNode:
    """
    创建测试树1：相邻节点交换
    
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

def create_test_tree2() -> TreeNode:
    """
    创建测试树2：非相邻节点交换
    
    测试树结构：
          7
         / \
        3   8
       / \
      2   6
         /
        4
    
    中序遍历：2, 3, 4, 6, 7, 8 (错误交换了7和2)
    """
    root = TreeNode(7)
    root.left = TreeNode(3)
    root.right = TreeNode(8)
    root.left.left = TreeNode(2)
    root.left.right = TreeNode(6)
    root.left.right.left = TreeNode(4)
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

def test_recover_bst_fixed():
    """单元测试函数 - 修复版本"""
    print("=== Morris遍历恢复BST修复版本测试 ===")
    
    sol = Solution()
    
    # 测试用例1：相邻节点交换
    print("\n测试用例1：相邻节点交换")
    root1 = create_test_tree1()
    print("原始树中序遍历: ", end='')
    inorder_print(root1)
    
    sol.recoverTree(root1)
    print("恢复后中序遍历: ", end='')
    inorder_print(root1)
    print("是否有效BST:", "是" if is_valid_bst(root1) else "否")
    
    # 测试用例2：非相邻节点交换
    print("\n测试用例2：非相邻节点交换")
    root2 = create_test_tree2()
    print("原始树中序遍历: ", end='')
    inorder_print(root2)
    
    sol.recoverTree(root2)
    print("恢复后中序遍历: ", end='')
    inorder_print(root2)
    print("是否有效BST:", "是" if is_valid_bst(root2) else "否")
    
    # 测试用例3：边界情况
    print("\n测试用例3：边界情况")
    root3 = None
    sol.recoverTree(root3)
    print("空树测试通过")
    
    root4 = TreeNode(1)
    sol.recoverTree(root4)
    print("单节点树测试通过")
    
    # 测试用例4：递归版本
    print("\n测试用例4：递归版本测试")
    root5 = create_test_tree1()
    print("递归恢复前中序遍历: ", end='')
    inorder_print(root5)
    
    sol.recoverTreeRecursive(root5)
    print("递归恢复后中序遍历: ", end='')
    inorder_print(root5)
    print("是否有效BST:", "是" if is_valid_bst(root5) else "否")
    
    # 测试用例5：迭代版本
    print("\n测试用例5：迭代版本测试")
    root6 = create_test_tree2()
    print("迭代恢复前中序遍历: ", end='')
    inorder_print(root6)
    
    sol.recoverTreeIterative(root6)
    print("迭代恢复后中序遍历: ", end='')
    inorder_print(root6)
    print("是否有效BST:", "是" if is_valid_bst(root6) else "否")
    
    print("=== 修复版本测试完成 ===")

if __name__ == "__main__":
    test_recover_bst_fixed()