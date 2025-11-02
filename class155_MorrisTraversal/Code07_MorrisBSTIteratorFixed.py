"""
Morris遍历实现BST迭代器 - Python修复版本

题目来源：
- BST迭代器：LeetCode 173. Binary Search Tree Iterator
  链接：https://leetcode.cn/problems/binary-search-tree-iterator/

算法详解：
修复版本针对原始Morris遍历算法进行了优化和改进，包括：
1. 更准确的迭代器状态管理
2. 更好的边界条件处理
3. 增强的错误检测机制
4. 改进的测试用例覆盖

时间复杂度：
- next(): 均摊O(1) - 虽然单次调用可能需要O(n)时间，但n次调用的总时间复杂度为O(n)
- hasNext(): O(1)
空间复杂度：O(1) - 不使用额外空间

工程化改进：
1. 更健壮的迭代器状态管理
2. 更好的空指针检查
3. 增强的异常处理
4. 更全面的测试用例
"""

from typing import Optional, List

class TreeNode:
    """二叉树节点定义"""
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class BSTIteratorMorrisFixed:
    """Morris遍历BST迭代器类 - 修复版本"""
    
    def __init__(self, root: Optional[TreeNode]):
        self.current = root
    
    def _find_next(self) -> Optional[TreeNode]:
        """查找下一个节点"""
        while self.current:
            if not self.current.left:
                # 如果没有左子树，访问当前节点
                result = self.current
                self.current = self.current.right
                return result
            else:
                # 找到当前节点的前驱节点
                predecessor = self.current.left
                while predecessor.right and predecessor.right != self.current:
                    predecessor = predecessor.right
                
                if not predecessor.right:
                    # 建立临时链接
                    predecessor.right = self.current
                    self.current = self.current.left
                else:
                    # 断开临时链接并访问当前节点
                    predecessor.right = None
                    result = self.current
                    self.current = self.current.right
                    return result
        return None
    
    def hasNext(self) -> bool:
        """检查是否还有下一个节点"""
        return self.current is not None
    
    def next(self) -> int:
        """获取下一个节点的值"""
        if not self.hasNext():
            raise StopIteration("No more elements")
        
        next_node = self._find_next()
        if not next_node:
            raise StopIteration("Iterator error: no next node found")
        
        return next_node.val

class BSTIteratorStackFixed:
    """基于栈的BST迭代器类 - 修复版本"""
    
    def __init__(self, root: Optional[TreeNode]):
        self.stack = []
        self._push_left(root)
    
    def _push_left(self, node: Optional[TreeNode]) -> None:
        """将左子树节点压入栈"""
        while node:
            self.stack.append(node)
            node = node.left
    
    def hasNext(self) -> bool:
        """检查是否还有下一个节点"""
        return len(self.stack) > 0
    
    def next(self) -> int:
        """获取下一个节点的值"""
        if not self.hasNext():
            raise StopIteration("No more elements")
        
        node = self.stack.pop()
        
        if node.right:
            self._push_left(node.right)
        
        return node.val

class BSTIteratorPreprocessFixed:
    """预处理的BST迭代器类 - 修复版本"""
    
    def __init__(self, root: Optional[TreeNode]):
        self.values = []
        self.index = 0
        self._inorder(root)
    
    def _inorder(self, node: Optional[TreeNode]) -> None:
        """中序遍历收集节点值"""
        if not node:
            return
        self._inorder(node.left)
        self.values.append(node.val)
        self._inorder(node.right)
    
    def hasNext(self) -> bool:
        """检查是否还有下一个节点"""
        return self.index < len(self.values)
    
    def next(self) -> int:
        """获取下一个节点的值"""
        if not self.hasNext():
            raise StopIteration("No more elements")
        
        result = self.values[self.index]
        self.index += 1
        return result

def create_test_tree1() -> TreeNode:
    """
    创建测试树1：标准BST
    
    测试树结构：
          7
         / \
        3   15
           /  \
          9    20
    
    中序遍历：3, 7, 9, 15, 20
    """
    root = TreeNode(7)
    root.left = TreeNode(3)
    root.right = TreeNode(15)
    root.right.left = TreeNode(9)
    root.right.right = TreeNode(20)
    return root

def create_test_tree2() -> TreeNode:
    """
    创建测试树2：左斜树
    
    测试树结构：
          5
         /
        4
       /
      3
     /
    2
    
    中序遍历：2, 3, 4, 5
    """
    root = TreeNode(5)
    root.left = TreeNode(4)
    root.left.left = TreeNode(3)
    root.left.left.left = TreeNode(2)
    return root

def create_test_tree3() -> TreeNode:
    """
    创建测试树3：右斜树
    
    测试树结构：
          2
           \
            3
             \
              4
               \
                5
    
    中序遍历：2, 3, 4, 5
    """
    root = TreeNode(2)
    root.right = TreeNode(3)
    root.right.right = TreeNode(4)
    root.right.right.right = TreeNode(5)
    return root

def test_bst_iterator_fixed():
    """单元测试函数 - 修复版本"""
    print("=== Morris遍历BST迭代器修复版本测试 ===")
    
    # 测试用例1：标准BST
    print("\n1. 标准BST测试:")
    root1 = create_test_tree1()
    
    print("Morris迭代器: ", end='')
    morris_it1 = BSTIteratorMorrisFixed(root1)
    while morris_it1.hasNext():
        print(morris_it1.next(), end=' ')
    print()
    
    print("栈迭代器: ", end='')
    stack_it1 = BSTIteratorStackFixed(root1)
    while stack_it1.hasNext():
        print(stack_it1.next(), end=' ')
    print()
    
    # 测试用例2：左斜树
    print("\n2. 左斜树测试:")
    root2 = create_test_tree2()
    
    print("Morris迭代器: ", end='')
    morris_it2 = BSTIteratorMorrisFixed(root2)
    while morris_it2.hasNext():
        print(morris_it2.next(), end=' ')
    print()
    
    # 测试用例3：右斜树
    print("\n3. 右斜树测试:")
    root3 = create_test_tree3()
    
    print("Morris迭代器: ", end='')
    morris_it3 = BSTIteratorMorrisFixed(root3)
    while morris_it3.hasNext():
        print(morris_it3.next(), end=' ')
    print()
    
    # 测试用例4：边界情况
    print("\n4. 边界情况测试:")
    
    # 空树测试
    empty_root = None
    empty_it = BSTIteratorMorrisFixed(empty_root)
    print("空树hasNext:", empty_it.hasNext())
    
    # 单节点树测试
    single_node = TreeNode(1)
    single_it = BSTIteratorMorrisFixed(single_node)
    print("单节点树遍历: ", end='')
    while single_it.hasNext():
        print(single_it.next(), end=' ')
    print()
    
    # 测试异常处理
    print("\n5. 异常处理测试:")
    try:
        empty_it.next()
    except StopIteration as e:
        print("异常处理正确:", str(e))
    
    print("=== 修复版本测试完成 ===")

def performance_comparison_fixed():
    """性能对比测试 - 修复版本"""
    print("\n=== 性能对比测试 - 修复版本 ===")
    
    # 创建大型测试树
    def create_large_tree(n: int) -> TreeNode:
        """创建包含n个节点的大型BST"""
        def build_tree(start: int, end: int) -> Optional[TreeNode]:
            if start > end:
                return None
            mid = (start + end) // 2
            node = TreeNode(mid)
            node.left = build_tree(start, mid - 1)
            node.right = build_tree(mid + 1, end)
            return node
        
        return build_tree(1, n)
    
    # 测试不同规模的树
    sizes = [100, 1000, 10000]
    
    for size in sizes:
        print(f"\n测试树规模: {size}个节点")
        
        # 创建测试树
        large_tree = create_large_tree(size)
        
        # Morris迭代器测试
        import time
        
        start_time = time.time()
        morris_it = BSTIteratorMorrisFixed(large_tree)
        while morris_it.hasNext():
            morris_it.next()
        morris_time = time.time() - start_time
        
        # 栈迭代器测试
        start_time = time.time()
        stack_it = BSTIteratorStackFixed(large_tree)
        while stack_it.hasNext():
            stack_it.next()
        stack_time = time.time() - start_time
        
        # 预处理迭代器测试
        start_time = time.time()
        preprocess_it = BSTIteratorPreprocessFixed(large_tree)
        while preprocess_it.hasNext():
            preprocess_it.next()
        preprocess_time = time.time() - start_time
        
        print(f"Morris迭代器时间: {morris_time:.4f}s")
        print(f"栈迭代器时间: {stack_time:.4f}s")
        print(f"预处理迭代器时间: {preprocess_time:.4f}s")
        
        # 内存使用对比
        import sys
        
        morris_memory = sys.getsizeof(morris_it)
        stack_memory = sys.getsizeof(stack_it)
        preprocess_memory = sys.getsizeof(preprocess_it)
        
        print(f"Morris迭代器内存: {morris_memory} bytes")
        print(f"栈迭代器内存: {stack_memory} bytes")
        print(f"预处理迭代器内存: {preprocess_memory} bytes")

if __name__ == "__main__":
    test_bst_iterator_fixed()
    performance_comparison_fixed()