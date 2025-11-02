#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 716 Max Stack

题目描述：
设计一个最大栈数据结构，支持普通栈的全部四种操作（push、top、pop、empty），
还支持查找栈中最大元素的操作（peekMax）和弹出栈中最大元素的操作（popMax）。

解题思路：
我们可以使用双向循环链表来实现最大栈。
1. 使用双向链表维护栈中元素
2. 使用另一个双向链表或平衡二叉搜索树维护元素的有序关系
3. 支持所有栈操作和最大值操作

时间复杂度：
- push: O(1)
- pop: O(1)
- top: O(1)
- peekMax: O(1)
- popMax: O(1)

空间复杂度：O(n)
"""

class ListNode:
    def __init__(self, value=0):
        self.value = value
        self.prev: 'ListNode' = self
        self.next: 'ListNode' = self

class MaxStack:
    def __init__(self):
        """初始化最大栈"""
        # 创建哨兵节点
        self.head = ListNode()
        self.head.prev = self.head
        self.head.next = self.head
        # 最大值链表头指针
        self.max_head = ListNode()
        self.max_head.prev = self.max_head
        self.max_head.next = self.max_head
    
    def push(self, x):
        """
        入栈
        
        Args:
            x: 要入栈的元素
        """
        # 创建新节点并插入栈顶
        node = ListNode(x)
        node.next = self.head.next
        node.prev = self.head
        self.head.next.prev = node
        self.head.next = node
        
        # 更新最大值链表
        # 将节点插入到最大值链表的正确位置
        max_node = ListNode(x)
        if self.max_head.next == self.max_head or x >= self.max_head.next.value:
            # 插入到最大值链表头部
            max_node.next = self.max_head.next
            max_node.prev = self.max_head
            self.max_head.next.prev = max_node
            self.max_head.next = max_node
        else:
            # 找到正确位置插入
            current = self.max_head.next
            while current != self.max_head and current.value > x:
                current = current.next
            max_node.next = current
            max_node.prev = current.prev
            current.prev.next = max_node
            current.prev = max_node
    
    def pop(self):
        """
        出栈
        
        Returns:
            栈顶元素
        """
        # 移除栈顶节点
        node = self.head.next
        value = node.value
        node.prev.next = node.next
        node.next.prev = node.prev
        
        # 从最大值链表中移除对应节点
        current = self.max_head.next
        while current != self.max_head and current.value != value:
            current = current.next
        if current != self.max_head:
            current.prev.next = current.next
            current.next.prev = current.prev
        
        return value
    
    def top(self):
        """
        获取栈顶元素
        
        Returns:
            栈顶元素
        """
        return self.head.next.value
    
    def peek_max(self):
        """
        获取最大元素
        
        Returns:
            最大元素
        """
        return self.max_head.next.value
    
    def pop_max(self):
        """
        弹出最大元素
        
        Returns:
            最大元素
        """
        # 获取最大值
        max_value = self.max_head.next.value
        
        # 从最大值链表中移除最大值节点
        max_node = self.max_head.next
        max_node.prev.next = max_node.next
        max_node.next.prev = max_node.prev
        
        # 从栈中移除对应节点
        current = self.head.next
        while current != self.head and current.value != max_value:
            current = current.next
        if current != self.head:
            current.prev.next = current.next
            current.next.prev = current.prev
        
        return max_value
    
    def empty(self):
        """
        检查栈是否为空
        
        Returns:
            栈是否为空
        """
        return self.head.next == self.head


# 测试方法
def main():
    # 测试用例1
    stk = MaxStack()
    stk.push(5)
    stk.push(1)
    stk.push(5)
    print("测试用例1:")
    print("top() =", stk.top())
    print("popMax() =", stk.pop_max())
    print("top() =", stk.top())
    print("peekMax() =", stk.peek_max())
    print("pop() =", stk.pop())
    print("top() =", stk.top())
    print()
    
    # 测试用例2
    stk2 = MaxStack()
    stk2.push(1)
    stk2.push(2)
    stk2.push(3)
    print("测试用例2:")
    print("peekMax() =", stk2.peek_max())
    print("popMax() =", stk2.pop_max())
    print("peekMax() =", stk2.peek_max())
    print("pop() =", stk2.pop())
    print("peekMax() =", stk2.peek_max())
    print()
    
    # 测试用例3
    stk3 = MaxStack()
    stk3.push(5)
    stk3.push(1)
    stk3.push(3)
    stk3.push(5)
    print("测试用例3:")
    print("popMax() =", stk3.pop_max())
    print("popMax() =", stk3.pop_max())
    print("top() =", stk3.top())
    print("peekMax() =", stk3.peek_max())
    print("pop() =", stk3.pop())
    print("top() =", stk3.top())


if __name__ == "__main__":
    main()