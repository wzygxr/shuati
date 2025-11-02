#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 146. LRU Cache 解决方案

题目链接: https://leetcode.com/problems/lru-cache/
题目描述: 实现最近最少使用缓存
解题思路: 使用双向循环链表和哈希表

时间复杂度: 
- get操作: O(1)
- put操作: O(1)
空间复杂度: O(capacity)
"""

from typing import Optional

class Node:
    """双向链表节点类"""
    
    def __init__(self, key: int, value: int):
        self.key = key
        self.value = value
        self.prev = self
        self.next = self

class DoublyCircularLinkedList:
    """双向循环链表类"""
    
    def __init__(self):
        self.head = None  # 头节点（最久未使用的节点）
        self.size = 0     # 链表大小
    
    def insert_at_tail(self, node: Node) -> None:
        """在链表尾部插入节点（表示最近使用）"""
        if self.head is None:
            # 空链表
            self.head = node
        else:
            # 非空链表，插入到尾部
            tail = self.head.prev
            
            node.next = self.head
            node.prev = tail
            
            tail.next = node
            self.head.prev = node
        
        self.size += 1
    
    def delete_node(self, node: Node) -> None:
        """删除指定节点"""
        if node is None or self.head is None:
            return
        
        if self.size == 1:
            # 链表只有一个节点
            self.head = None
        else:
            # 链表有多个节点
            node.prev.next = node.next
            node.next.prev = node.prev
            
            # 如果删除的是头节点，更新头节点
            if node == self.head:
                self.head = node.next
        
        self.size -= 1
    
    def delete_head(self) -> Optional[Node]:
        """删除头节点（最久未使用的节点）
        
        Returns:
            被删除的节点，如果链表为空返回None
        """
        if self.head is None:
            return None
        
        old_head = self.head
        
        if self.size == 1:
            # 链表只有一个节点
            self.head = None
        else:
            # 链表有多个节点
            tail = self.head.prev
            new_head = self.head.next
            
            tail.next = new_head
            new_head.prev = tail
            
            self.head = new_head
        
        self.size -= 1
        return old_head
    
    def move_to_tail(self, node: Node) -> None:
        """将节点移动到尾部（表示最近使用）"""
        # 先删除节点
        self.delete_node(node)
        # 再插入到尾部
        self.insert_at_tail(node)
    
    def is_empty(self) -> bool:
        """检查链表是否为空"""
        return self.size == 0

class LRUCache:
    """LRU缓存实现"""
    
    def __init__(self, capacity: int):
        """
        构造函数
        
        Args:
            capacity: 缓存容量
        """
        self.capacity = capacity
        self.list = DoublyCircularLinkedList()
        self.map = {}  # 哈希表，用于O(1)查找
    
    def get(self, key: int) -> int:
        """
        获取键对应的值
        
        Args:
            key: 键
            
        Returns:
            值，如果键不存在返回-1
        """
        node = self.map.get(key)
        if node is None:
            return -1  # 键不存在
        
        # 将节点移动到链表尾部（表示最近使用）
        self.list.move_to_tail(node)
        
        return node.value
    
    def put(self, key: int, value: int) -> None:
        """
        插入或更新键值对
        
        Args:
            key: 键
            value: 值
        """
        node = self.map.get(key)
        
        if node is None:
            # 键不存在，需要插入新节点
            new_node = Node(key, value)
            
            # 检查是否需要淘汰最久未使用的节点
            if self.list.size >= self.capacity:
                old_node = self.list.delete_head()
                if old_node is not None:
                    del self.map[old_node.key]
            
            # 插入新节点
            self.list.insert_at_tail(new_node)
            self.map[key] = new_node
        else:
            # 键已存在，更新值并移动到尾部
            node.value = value
            self.list.move_to_tail(node)
    
    @staticmethod
    def test_lru_cache():
        """测试方法"""
        # 测试用例1
        print("=== 测试用例1 ===")
        lru_cache = LRUCache(2)
        
        lru_cache.put(1, 1)  # 缓存是 {1=1}
        lru_cache.put(2, 2)  # 缓存是 {1=1, 2=2}
        print(f"get(1) = {lru_cache.get(1)}")  # 返回 1
        lru_cache.put(3, 3)  # 该操作会使得关键字 2 作废，缓存是 {1=1, 3=3}
        print(f"get(2) = {lru_cache.get(2)}")  # 返回 -1 (未找到)
        lru_cache.put(4, 4)  # 该操作会使得关键字 1 作废，缓存是 {4=4, 3=3}
        print(f"get(1) = {lru_cache.get(1)}")  # 返回 -1 (未找到)
        print(f"get(3) = {lru_cache.get(3)}")  # 返回 3
        print(f"get(4) = {lru_cache.get(4)}")  # 返回 4
        
        # 测试用例2
        print("\n=== 测试用例2 ===")
        lru_cache2 = LRUCache(1)
        
        lru_cache2.put(2, 1)  # 缓存是 {2=1}
        print(f"get(2) = {lru_cache2.get(2)}")  # 返回 1
        lru_cache2.put(3, 2)  # 该操作会使得关键字 2 作废，缓存是 {3=2}
        print(f"get(2) = {lru_cache2.get(2)}")  # 返回 -1 (未找到)
        print(f"get(3) = {lru_cache2.get(3)}")  # 返回 2
        
        # 测试用例3：更新已存在的键
        print("\n=== 测试用例3：更新已存在的键 ===")
        lru_cache3 = LRUCache(2)
        
        lru_cache3.put(2, 1)  # 缓存是 {2=1}
        lru_cache3.put(1, 1)  # 缓存是 {2=1, 1=1}
        lru_cache3.put(2, 3)  # 更新键2的值，缓存是 {1=1, 2=3}
        lru_cache3.put(4, 1)  # 该操作会使得关键字 1 作废，缓存是 {2=3, 4=1}
        print(f"get(1) = {lru_cache3.get(1)}")  # 返回 -1 (未找到)
        print(f"get(2) = {lru_cache3.get(2)}")  # 返回 3

if __name__ == "__main__":
    LRUCache.test_lru_cache()