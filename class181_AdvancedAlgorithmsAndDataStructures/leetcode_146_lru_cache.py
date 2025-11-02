#!/usr/bin/env python3
"""
LeetCode 146. LRU缓存机制 (LRU Cache) - Python版本

题目来源：https://leetcode.cn/problems/lru-cache/

解题思路：
使用双向循环链表 + 哈希表实现LRU缓存

时间复杂度：O(1)
空间复杂度：O(capacity)
"""

import time
import random
from typing import Optional

class DLinkedNode:
    """双向链表节点"""
    def __init__(self, key: int = 0, value: int = 0):
        self.key = key
        self.value = value
        self.prev: Optional[DLinkedNode] = None
        self.next: Optional[DLinkedNode] = None

class LRUCache:
    """LRU缓存实现"""
    
    def __init__(self, capacity: int):
        self.cache = {}
        self.size = 0
        self.capacity = capacity
        
        # 使用伪头部和伪尾部节点
        self.head = DLinkedNode()
        self.tail = DLinkedNode()
        self.head.next = self.tail
        self.tail.prev = self.head
    
    def _add_to_head(self, node: DLinkedNode) -> None:
        """将节点添加到头部"""
        node.prev = self.head
        node.next = self.head.next
        self.head.next.prev = node
        self.head.next = node
    
    def _remove_node(self, node: DLinkedNode) -> None:
        """移除节点"""
        node.prev.next = node.next
        node.next.prev = node.prev
    
    def _move_to_head(self, node: DLinkedNode) -> None:
        """将节点移到头部"""
        self._remove_node(node)
        self._add_to_head(node)
    
    def _remove_tail(self) -> DLinkedNode:
        """移除尾部节点"""
        node = self.tail.prev
        self._remove_node(node)
        return node
    
    def get(self, key: int) -> int:
        """获取缓存值"""
        if key not in self.cache:
            return -1
        
        # 如果key存在，先通过哈希表定位，再移到头部
        node = self.cache[key]
        self._move_to_head(node)
        return node.value
    
    def put(self, key: int, value: int) -> None:
        """添加缓存值"""
        if key not in self.cache:
            # 如果key不存在，创建一个新的节点
            node = DLinkedNode(key, value)
            
            # 添加进哈希表
            self.cache[key] = node
            
            # 添加至双向链表的头部
            self._add_to_head(node)
            self.size += 1
            
            if self.size > self.capacity:
                # 如果超出容量，删除双向链表的尾部节点
                removed = self._remove_tail()
                
                # 删除哈希表中对应的项
                del self.cache[removed.key]
                self.size -= 1
        else:
            # 如果key存在，先通过哈希表定位，再修改value，并移到头部
            node = self.cache[key]
            node.value = value
            self._move_to_head(node)

def test_lru_cache():
    """测试LRU缓存实现"""
    print("=== LeetCode 146. LRU缓存机制 (Python版本) ===")
    
    # 测试用例1
    print("测试用例1:")
    lru_cache = LRUCache(2)
    
    lru_cache.put(1, 1)
    lru_cache.put(2, 2)
    print("put(1,1), put(2,2)")
    
    result1 = lru_cache.get(1)
    print(f"get(1) = {result1} (期望: 1)")
    
    lru_cache.put(3, 3)
    print("put(3,3)")
    
    result2 = lru_cache.get(2)
    print(f"get(2) = {result2} (期望: -1)")
    
    lru_cache.put(4, 4)
    print("put(4,4)")
    
    result3 = lru_cache.get(1)
    print(f"get(1) = {result3} (期望: -1)")
    
    result4 = lru_cache.get(3)
    print(f"get(3) = {result4} (期望: 3)")
    
    result5 = lru_cache.get(4)
    print(f"get(4) = {result5} (期望: 4)")
    print()
    
    # 性能测试
    print("=== 性能测试 ===")
    performance_cache = LRUCache(1000)
    random.seed(42)
    
    start_time = time.time()
    
    # 执行10000次操作
    for i in range(10000):
        operation = random.randint(0, 2)
        key = random.randint(0, 2000)
        
        if operation == 0:
            # get操作
            performance_cache.get(key)
        else:
            # put操作
            value = random.randint(0, 10000)
            performance_cache.put(key, value)
    
    end_time = time.time()
    
    print("10000次操作完成")
    print(f"运行时间: {(end_time - start_time) * 1000:.2f} ms")
    
    # Python语言特性考量
    print("\n=== Python语言特性考量 ===")
    print("1. 类型注解：使用类型注解提高代码可读性")
    print("2. 私有方法：使用下划线前缀表示私有方法")
    print("3. 内存管理：Python自动管理内存，无需手动释放")
    print("4. 异常处理：Python有完善的异常处理机制")
    
    # 算法复杂度分析
    print("\n=== 算法复杂度分析 ===")
    print("时间复杂度: O(1)")
    print("  - get操作: O(1)")
    print("  - put操作: O(1)")
    print("空间复杂度: O(capacity)")
    print("  - 哈希表: O(capacity)")
    print("  - 双向链表: O(capacity)")

if __name__ == "__main__":
    test_lru_cache()