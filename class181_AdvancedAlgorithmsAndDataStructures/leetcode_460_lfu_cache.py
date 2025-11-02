#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 460 LFU Cache

题目描述：
请你为最不经常使用（LFU）缓存算法设计并实现数据结构。
实现 LFUCache 类：
- LFUCache(int capacity) - 用数据结构的容量 capacity 初始化对象
- int get(int key) - 如果键存在于缓存中，则获取键的值，否则返回 -1。
- void put(int key, int value) - 如果键已存在，则变更其值；如果键不存在，请插入键值对。

解题思路：
我们可以使用双向循环链表来实现LFU缓存。
1. 使用哈希表存储键到节点的映射
2. 使用另一个哈希表存储频率到双向链表的映射
3. 每个节点包含键、值、频率和指向链表节点的指针
4. 维护最小频率以快速找到要删除的节点

时间复杂度：O(1)
空间复杂度：O(capacity)
"""

class ListNode:
    def __init__(self, key=0, value=0, freq=0):
        self.key = key
        self.value = value
        self.freq = freq
        self.prev: 'ListNode | None' = None
        self.next: 'ListNode | None' = None

class LFUCache:
    def __init__(self, capacity):
        """
        初始化LFU缓存
        
        Args:
            capacity: 缓存容量
        """
        self.capacity = capacity
        self.size = 0
        self.min_freq = 0
        # 哈希表：键到节点的映射
        self.key_to_node = {}
        # 哈希表：频率到双向链表的映射
        self.freq_to_list = {}
    
    def get(self, key):
        """
        获取键对应的值
        
        Args:
            key: 键
            
        Returns:
            键对应的值，如果不存在则返回-1
        """
        if key not in self.key_to_node:
            return -1
        
        # 获取节点并更新频率
        node = self.key_to_node[key]
        self._update_freq(node)
        return node.value
    
    def put(self, key, value):
        """
        插入或更新键值对
        
        Args:
            key: 键
            value: 值
        """
        if self.capacity == 0:
            return
        
        if key in self.key_to_node:
            # 更新现有键值对
            node = self.key_to_node[key]
            node.value = value
            self._update_freq(node)
        else:
            # 插入新键值对
            if self.size == self.capacity:
                # 删除最不经常使用的节点
                self._remove_lfu()
            
            # 创建新节点
            node = ListNode(key, value, 1)
            self.key_to_node[key] = node
            self._add_to_freq_list(node)
            self.min_freq = 1
            self.size += 1
    
    def _update_freq(self, node):
        """
        更新节点频率
        
        Args:
            node: 节点
        """
        # 从当前频率链表中移除节点
        self._remove_from_freq_list(node)
        
        # 如果当前频率是最小频率且链表为空，更新最小频率
        if node.freq == self.min_freq and node.freq not in self.freq_to_list:
            self.min_freq += 1
        
        # 增加节点频率
        node.freq += 1
        
        # 将节点添加到新频率的链表中
        self._add_to_freq_list(node)
    
    def _remove_lfu(self):
        """删除最不经常使用的节点"""
        # 获取最小频率的链表
        freq_list = self.freq_to_list[self.min_freq]
        
        # 删除链表尾部节点（最久未使用的节点）
        node = freq_list.prev
        self._remove_from_freq_list(node)
        del self.key_to_node[node.key]
        self.size -= 1
    
    def _add_to_freq_list(self, node):
        """
        将节点添加到指定频率的链表中
        
        Args:
            node: 节点
        """
        freq = node.freq
        if freq not in self.freq_to_list:
            # 创建新的双向循环链表
            head = ListNode()
            head.prev = head  # type: ignore
            head.next = head  # type: ignore
            self.freq_to_list[freq] = head
        
        # 将节点添加到链表头部
        head = self.freq_to_list[freq]
        node.next = head.next
        node.prev = head
        head.next.prev = node
        head.next = node
    
    def _remove_from_freq_list(self, node):
        """
        从频率链表中移除节点
        
        Args:
            node: 节点
        """
        if node.freq in self.freq_to_list:
            node.prev.next = node.next
            node.next.prev = node.prev
            
            # 如果链表为空，删除链表
            head = self.freq_to_list[node.freq]
            if head.next == head:
                del self.freq_to_list[node.freq]


# 测试方法
def main():
    # 测试用例1
    lfu = LFUCache(2)
    lfu.put(1, 1)
    lfu.put(2, 2)
    print("测试用例1:")
    print("get(1) =", lfu.get(1))
    lfu.put(3, 3)
    print("get(2) =", lfu.get(2))
    print("get(3) =", lfu.get(3))
    lfu.put(4, 4)
    print("get(1) =", lfu.get(1))
    print("get(3) =", lfu.get(3))
    print("get(4) =", lfu.get(4))
    print()
    
    # 测试用例2
    lfu2 = LFUCache(0)
    lfu2.put(0, 0)
    print("测试用例2:")
    print("get(0) =", lfu2.get(0))
    print()
    
    # 测试用例3
    lfu3 = LFUCache(2)
    lfu3.put(2, 1)
    lfu3.put(3, 2)
    print("测试用例3:")
    print("get(3) =", lfu3.get(3))
    print("get(2) =", lfu3.get(2))
    lfu3.put(4, 3)
    print("get(2) =", lfu3.get(2))
    print("get(3) =", lfu3.get(3))
    print("get(4) =", lfu3.get(4))


if __name__ == "__main__":
    main()