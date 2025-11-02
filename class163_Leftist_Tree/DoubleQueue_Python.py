#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
POJ 3481 Double Queue（双端队列）
题目链接: http://poj.org/problem?id=3481

题目描述：实现一个双端队列，支持以下操作：
1. 插入一个客户，包含id和优先级
2. 删除并返回优先级最高的客户
3. 删除并返回优先级最低的客户

解题思路：使用两个左偏树，一个维护最大值，一个维护最小值

时间复杂度：所有操作均为O(log n)
空间复杂度：O(n)
"""

class Customer:
    def __init__(self, id, priority):
        self.id = id
        self.priority = priority
        self.deleted = False

class LeftistTreeNode:
    def __init__(self, customer):
        self.customer = customer
        self.dist = 0
        self.left = None
        self.right = None

class DoubleQueue_Python:
    def __init__(self):
        self.max_heap_root = None
        self.min_heap_root = None
        self.customers = {}
    
    def _merge_max(self, a, b):
        """合并两个左偏树（用于最大堆）"""
        if a is None:
            return b
        if b is None:
            return a
        
        # 维护大根堆性质
        if a.customer.priority < b.customer.priority:
            a, b = b, a
        
        # 递归合并右子树
        a.right = self._merge_max(a.right, b)
        
        # 维护左偏性质
        if a.left is None or (a.right is not None and a.left.dist < a.right.dist):
            a.left, a.right = a.right, a.left
        
        # 更新距离
        a.dist = 0 if a.right is None else a.right.dist + 1
        return a
    
    def _merge_min(self, a, b):
        """合并两个左偏树（用于最小堆）"""
        if a is None:
            return b
        if b is None:
            return a
        
        # 维护小根堆性质
        if a.customer.priority > b.customer.priority:
            a, b = b, a
        
        # 递归合并右子树
        a.right = self._merge_min(a.right, b)
        
        # 维护左偏性质
        if a.left is None or (a.right is not None and a.left.dist < a.right.dist):
            a.left, a.right = a.right, a.left
        
        # 更新距离
        a.dist = 0 if a.right is None else a.right.dist + 1
        return a
    
    def insert(self, id, priority):
        """插入一个客户"""
        # 如果客户已存在，先删除旧记录
        if id in self.customers:
            self._delete(id)
        
        # 创建新客户
        customer = Customer(id, priority)
        self.customers[id] = customer
        
        # 同时插入到最大堆和最小堆
        node = LeftistTreeNode(customer)
        self.max_heap_root = self._merge_max(self.max_heap_root, node)
        self.min_heap_root = self._merge_min(self.min_heap_root, node)
    
    def _delete(self, id):
        """删除特定ID的客户（内部方法）"""
        if id in self.customers:
            self.customers[id].deleted = True
            del self.customers[id]
    
    def delete_max(self):
        """删除并返回优先级最高的客户"""
        # 清理堆中已删除的节点
        while self.max_heap_root is not None and self.max_heap_root.customer.deleted:
            self.max_heap_root = self._merge_max(self.max_heap_root.left, self.max_heap_root.right)
        
        if self.max_heap_root is None:
            return None  # 堆为空
        
        # 获取最大值节点
        max_node = self.max_heap_root
        max_customer = max_node.customer
        
        # 从最大值堆中删除
        self.max_heap_root = self._merge_max(self.max_heap_root.left, self.max_heap_root.right)
        
        # 标记客户为已删除
        max_customer.deleted = True
        del self.customers[max_customer.id]
        
        return max_customer
    
    def delete_min(self):
        """删除并返回优先级最低的客户"""
        # 清理堆中已删除的节点
        while self.min_heap_root is not None and self.min_heap_root.customer.deleted:
            self.min_heap_root = self._merge_min(self.min_heap_root.left, self.min_heap_root.right)
        
        if self.min_heap_root is None:
            return None  # 堆为空
        
        # 获取最小值节点
        min_node = self.min_heap_root
        min_customer = min_node.customer
        
        # 从最小值堆中删除
        self.min_heap_root = self._merge_min(self.min_heap_root.left, self.min_heap_root.right)
        
        # 标记客户为已删除
        min_customer.deleted = True
        del self.customers[min_customer.id]
        
        return min_customer

# 主函数，处理输入输出
def main():
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    queue = DoubleQueue_Python()
    
    while True:
        command = int(input[ptr])
        ptr += 1
        if command == 0:
            break  # 结束程序
        elif command == 1:
            # 插入操作
            id = int(input[ptr])
            priority = int(input[ptr + 1])
            ptr += 2
            queue.insert(id, priority)
        elif command == 2:
            # 删除最大值
            max_cust = queue.delete_max()
            if max_cust is not None:
                print(max_cust.id)
        elif command == 3:
            # 删除最小值
            min_cust = queue.delete_min()
            if min_cust is not None:
                print(min_cust.id)

if __name__ == "__main__":
    main()