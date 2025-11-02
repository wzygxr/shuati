#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
LeetCode 716. Max Stack（最大栈）
题目链接: https://leetcode.com/problems/max-stack/

题目描述：设计一个最大栈，支持push、pop、top、peekMax和popMax操作。
操作说明：
- push(x) -- 将元素x压入栈中
- pop() -- 移除栈顶元素并返回该元素
- top() -- 返回栈顶元素
- peekMax() -- 返回栈中最大元素
- popMax() -- 返回栈中最大元素，并将其删除

解题思路：使用左偏树实现最大栈

时间复杂度：所有操作均为O(log n)
空间复杂度：O(n)
"""

class Node:
    def __init__(self, value):
        self.value = value
        self.prev = None
        self.next = None
        self.deleted = False

class LeftistTreeNode:
    def __init__(self, value, stack_node):
        self.value = value
        self.stack_node = stack_node
        self.dist = 0
        self.left = None
        self.right = None

class MaxStack_Python:
    def __init__(self):
        # 初始化双向链表的头尾哨兵节点
        self.head = Node(float('-inf'))
        self.tail = Node(float('inf'))
        self.head.next = self.tail
        self.tail.prev = self.head
        
        self.max_heap_root = None
    
    def _merge(self, a, b):
        if a is None:
            return b
        if b is None:
            return a
        
        # 维护大根堆性质
        if a.value < b.value:
            a, b = b, a
        
        # 递归合并右子树
        a.right = self._merge(a.right, b)
        
        # 维护左偏性质
        if a.left is None or (a.right is not None and a.left.dist < a.right.dist):
            a.left, a.right = a.right, a.left
        
        # 更新距离
        a.dist = 0 if a.right is None else a.right.dist + 1
        return a
    
    def push(self, x):
        """将元素x压入栈中"""
        # 创建新的栈节点
        new_node = Node(x)
        
        # 将新节点插入到链表尾部（栈顶）
        new_node.next = self.tail
        new_node.prev = self.tail.prev
        self.tail.prev.next = new_node
        self.tail.prev = new_node
        
        # 将新节点加入大根堆
        self.max_heap_root = self._merge(self.max_heap_root, LeftistTreeNode(x, new_node))
    
    def pop(self):
        """移除栈顶元素并返回该元素"""
        if self.is_empty():
            raise IndexError("pop from an empty stack")
        
        # 获取栈顶节点
        top_node = self.tail.prev
        
        # 标记为已删除
        top_node.deleted = True
        
        # 从链表中移除
        top_node.prev.next = top_node.next
        top_node.next.prev = top_node.prev
        
        return top_node.value
    
    def top(self):
        """返回栈顶元素"""
        if self.is_empty():
            raise IndexError("top from an empty stack")
        return self.tail.prev.value
    
    def peek_max(self):
        """返回栈中最大元素"""
        if self.is_empty():
            raise IndexError("peek_max from an empty stack")
        
        # 清理堆中已删除的节点
        while self.max_heap_root is not None and self.max_heap_root.stack_node.deleted:
            self.max_heap_root = self._merge(self.max_heap_root.left, self.max_heap_root.right)
        
        return self.max_heap_root.value if self.max_heap_root else None
    
    def pop_max(self):
        """返回栈中最大元素，并将其删除"""
        if self.is_empty():
            raise IndexError("pop_max from an empty stack")
        
        # 清理堆中已删除的节点
        while self.max_heap_root is not None and self.max_heap_root.stack_node.deleted:
            self.max_heap_root = self._merge(self.max_heap_root.left, self.max_heap_root.right)
        
        # 获取最大值节点
        max_node = self.max_heap_root
        max_value = max_node.value
        
        # 从堆中删除最大值节点
        self.max_heap_root = self._merge(self.max_heap_root.left, self.max_heap_root.right)
        
        # 从栈中删除对应的节点
        stack_node = max_node.stack_node
        stack_node.deleted = True
        stack_node.prev.next = stack_node.next
        stack_node.next.prev = stack_node.prev
        
        return max_value
    
    def is_empty(self):
        """检查栈是否为空"""
        return self.head.next == self.tail

# 测试代码
def test_max_stack():
    max_stack = MaxStack_Python()
    max_stack.push(5)
    max_stack.push(1)
    max_stack.push(5)
    
    print("top:", max_stack.top())        # 应输出 5
    print("pop_max:", max_stack.pop_max())  # 应输出 5
    print("top:", max_stack.top())        # 应输出 1
    print("peek_max:", max_stack.peek_max()) # 应输出 5
    print("pop:", max_stack.pop())        # 应输出 1
    print("top:", max_stack.top())        # 应输出 5

if __name__ == "__main__":
    test_max_stack()