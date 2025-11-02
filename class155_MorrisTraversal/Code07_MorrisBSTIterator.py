"""
Morris遍历实现BST迭代器 - Python实现

题目来源：
- BST迭代器：LeetCode 173. Binary Search Tree Iterator
  链接：https://leetcode.cn/problems/binary-search-tree-iterator/

算法详解：
利用Morris中序遍历实现BST迭代器，在O(1)空间复杂度下实现next()和hasNext()方法
1. 使用Morris中序遍历的思想，在每次调用next()时找到下一个节点
2. 通过维护当前节点和前驱节点的关系来实现迭代器的状态保持
3. 在hasNext()方法中检查是否还有未访问的节点

时间复杂度：
- next(): 均摊O(1) - 虽然单次调用可能需要O(n)时间，但n次调用的总时间复杂度为O(n)
- hasNext(): O(1)
空间复杂度：O(1) - 不使用额外空间

工程化考量：
1. 异常处理：处理空树、迭代器结束等情况
2. 线程安全：非线程安全，需要外部同步
3. 性能优化：使用Morris遍历避免栈空间
4. 可测试性：提供完整的测试用例
"""

from typing import Optional, List

class TreeNode:
    """二叉树节点定义"""
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class BSTIteratorMorris:
    """Morris遍历BST迭代器类"""
    
    def __init__(self, root: Optional[TreeNode]):
        self.current = root
        self.prev = None
    
    def hasNext(self) -> bool:
        """检查是否还有下一个节点"""
        return self.current is not None
    
    def next(self) -> int:
        """获取下一个节点的值"""
        if not self.hasNext():
            raise StopIteration("No more elements")
        
        result = 0
        
        while self.current:
            if not self.current.left:
                # 如果没有左子树，访问当前节点
                result = self.current.val
                self.current = self.current.right
                break
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
                    result = self.current.val
                    self.current = self.current.right
                    break
        
        return result

class BSTIteratorStack:
    """基于栈的BST迭代器类"""
    
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

class BSTIteratorPreprocess:
    """预处理的BST迭代器类"""
    
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

def create_test_tree() -> TreeNode:
    """
    创建测试树
    
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

def test_bst_iterator():
    """单元测试函数"""
    print("=== Morris遍历BST迭代器测试 ===")
    
    # 创建测试树
    root = create_test_tree()
    
    # 测试Morris迭代器
    print("\n1. Morris迭代器测试:")
    morris_it = BSTIteratorMorris(root)
    print("中序遍历结果: ", end='')
    while morris_it.hasNext():
        print(morris_it.next(), end=' ')
    print()
    
    # 测试栈迭代器
    print("\n2. 栈迭代器测试:")
    stack_it = BSTIteratorStack(root)
    print("中序遍历结果: ", end='')
    while stack_it.hasNext():
        print(stack_it.next(), end=' ')
    print()
    
    # 测试预处理迭代器
    print("\n3. 预处理迭代器测试:")
    preprocess_it = BSTIteratorPreprocess(root)
    print("中序遍历结果: ", end='')
    while preprocess_it.hasNext():
        print(preprocess_it.next(), end=' ')
    print()
    
    # 测试边界情况
    print("\n4. 边界情况测试:")
    
    # 空树测试
    empty_root = None
    empty_it = BSTIteratorMorris(empty_root)
    print("空树hasNext:", empty_it.hasNext())
    
    # 单节点树测试
    single_node = TreeNode(1)
    single_it = BSTIteratorMorris(single_node)
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
    
    print("=== 测试完成 ===")

def performance_comparison():
    """性能对比测试"""
    print("\n=== 性能对比测试 ===")
    
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
        morris_it = BSTIteratorMorris(large_tree)
        while morris_it.hasNext():
            morris_it.next()
        morris_time = time.time() - start_time
        
        # 栈迭代器测试
        start_time = time.time()
        stack_it = BSTIteratorStack(large_tree)
        while stack_it.hasNext():
            stack_it.next()
        stack_time = time.time() - start_time
        
        # 预处理迭代器测试
        start_time = time.time()
        preprocess_it = BSTIteratorPreprocess(large_tree)
        while preprocess_it.hasNext():
            preprocess_it.next()
        preprocess_time = time.time() - start_time
        
        print(f"Morris迭代器时间: {morris_time:.4f}s")
        print(f"栈迭代器时间: {stack_time:.4f}s")
        print(f"预处理迭代器时间: {preprocess_time:.4f}s")

if __name__ == "__main__":
    test_bst_iterator()
    performance_comparison()