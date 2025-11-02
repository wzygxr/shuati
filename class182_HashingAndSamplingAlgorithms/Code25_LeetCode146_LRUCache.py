"""
LeetCode 146. LRU缓存机制 (LRU Cache)
题目链接：https://leetcode.com/problems/lru-cache/

题目描述：
运用你所掌握的数据结构，设计和实现一个 LRU (最近最少使用) 缓存机制。
实现 LRUCache 类：
- LRUCache(int capacity) 以正整数作为容量 capacity 初始化 LRU 缓存
- int get(int key) 如果关键字 key 存在于缓存中，则返回关键字的值，否则返回 -1
- void put(int key, int value) 如果关键字已经存在，则变更其数据值；
  如果关键字不存在，则插入该组「关键字-值」。
  当缓存容量达到上限时，它应该在写入新数据之前删除最久未使用的数据值。

算法思路：
1. 使用哈希表存储键到链表节点的映射，实现O(1)查找
2. 使用双向链表维护访问顺序，实现O(1)插入和删除
3. 当访问或更新节点时，将其移动到链表头部（表示最近使用）
4. 当缓存满时，删除链表尾部节点（表示最久未使用）

时间复杂度：
- get: O(1)
- put: O(1)

空间复杂度：O(capacity)

工程化考量：
1. 线程安全：在多线程环境下需要加锁保护
2. 内存优化：使用虚拟头尾节点简化链表操作
3. 异常处理：处理非法容量和键值
4. 边界情况：处理空缓存、满缓存等场景
"""

import threading
from collections import defaultdict
from typing import Dict, Any, Optional
import time
import random


class Node:
    """链表节点类"""
    
    def __init__(self, key: int, value: int):
        self.key = key
        self.value = value
        self.prev: Optional['Node'] = None
        self.next: Optional['Node'] = None


class Code25_LeetCode146_LRUCache:
    """LRU缓存实现"""
    
    def __init__(self, capacity: int):
        """
        构造函数
        :param capacity: 缓存容量
        :raises ValueError: 如果容量小于等于0
        """
        if capacity <= 0:
            raise ValueError("Capacity must be positive")
        
        self.capacity = capacity
        self.cache: Dict[int, Node] = {}
        
        # 初始化虚拟头尾节点
        self.head = Node(0, 0)
        self.tail = Node(0, 0)
        self.head.next = self.tail
        self.tail.prev = self.head
        
        # 线程锁
        self.lock = threading.RLock()
    
    def get(self, key: int) -> int:
        """
        获取键对应的值
        :param key: 键
        :return: 键对应的值，如果不存在则返回-1
        """
        with self.lock:
            node = self.cache.get(key)
            if node is None:
                return -1
            
            # 将节点移动到头部（表示最近使用）
            self._move_to_head(node)
            return node.value
    
    def put(self, key: int, value: int) -> None:
        """
        插入或更新键值对
        :param key: 键
        :param value: 值
        """
        with self.lock:
            node = self.cache.get(key)
            
            if node is None:
                # 创建新节点
                new_node = Node(key, value)
                
                # 检查缓存是否已满
                if len(self.cache) >= self.capacity:
                    # 删除最久未使用的节点（尾部节点）
                    tail_node = self._remove_tail()
                    del self.cache[tail_node.key]
                
                # 添加新节点到头部
                self._add_to_head(new_node)
                self.cache[key] = new_node
            else:
                # 更新现有节点的值
                node.value = value
                # 将节点移动到头部（表示最近使用）
                self._move_to_head(node)
    
    def _add_to_head(self, node: Node) -> None:
        """
        将节点添加到头部
        :param node: 节点
        """
        node.prev = self.head
        node.next = self.head.next
        if self.head.next is not None:
            self.head.next.prev = node
        self.head.next = node
    
    def _remove_node(self, node: Node) -> None:
        """
        删除节点
        :param node: 节点
        """
        if node.prev is not None:
            node.prev.next = node.next
        if node.next is not None:
            node.next.prev = node.prev
    
    def _move_to_head(self, node: Node) -> None:
        """
        将节点移动到头部
        :param node: 节点
        """
        self._remove_node(node)
        self._add_to_head(node)
    
    def _remove_tail(self) -> Node:
        """
        删除尾部节点
        :return: 被删除的节点
        """
        last_node = self.tail.prev
        if last_node is not None:
            self._remove_node(last_node)
            return last_node
        else:
            # 这种情况理论上不会发生，因为有虚拟头尾节点
            return Node(0, 0)
    
    def get_statistics(self) -> Dict[str, Any]:
        """
        获取缓存统计信息
        :return: 统计信息映射
        """
        with self.lock:
            stats: Dict[str, Any] = {
                "capacity": self.capacity,
                "size": len(self.cache),
                "usage": float(len(self.cache) / self.capacity) if self.capacity > 0 else 0.0
            }
            return stats


class PerformanceTest:
    """性能测试类"""
    
    @staticmethod
    def testLRUPerformance(lru_cache: Code25_LeetCode146_LRUCache, 
                          operation_count: int) -> None:
        """
        测试LRU缓存的性能
        :param lru_cache: LRU缓存实例
        :param operation_count: 操作数量
        """
        print("=== LRU缓存性能测试 ===")
        print(f"操作数量: {operation_count}")
        
        random.seed(42)
        
        # 测试插入性能
        start_time = time.perf_counter()
        
        for i in range(operation_count // 2):
            key = random.randint(0, operation_count)
            lru_cache.put(key, key * 2)
        
        put_time = time.perf_counter() - start_time
        
        # 测试查询性能
        start_time = time.perf_counter()
        
        hit_count = 0
        miss_count = 0
        
        for i in range(operation_count // 2):
            key = random.randint(0, operation_count)
            value = lru_cache.get(key)
            if value != -1:
                hit_count += 1
            else:
                miss_count += 1
        
        get_time = time.perf_counter() - start_time
        
        print(f"插入平均时间: {put_time / (operation_count // 2) * 1e9:.2f} ns")
        print(f"查询平均时间: {get_time / (operation_count // 2) * 1e9:.2f} ns")
        print(f"缓存命中率: {hit_count / (hit_count + miss_count) * 100:.2f}%")
        
        # 显示统计信息
        print("缓存统计信息:", lru_cache.get_statistics())


def main():
    """主函数"""
    print("=== LRU缓存机制测试 ===\n")
    
    # 基本功能测试
    print("1. 基本功能测试:")
    
    lru_cache = Code25_LeetCode146_LRUCache(2)
    
    # 插入键值对
    lru_cache.put(1, 1)
    lru_cache.put(2, 2)
    print("插入 (1,1) 和 (2,2)")
    
    # 查询键1
    value = lru_cache.get(1)
    print("查询键1:", value)
    
    # 插入键3，缓存满，应删除键2
    lru_cache.put(3, 3)
    print("插入 (3,3)，缓存满，删除最久未使用的键2")
    
    # 查询键2，应返回-1
    value = lru_cache.get(2)
    print("查询键2:", value)
    
    # 插入键4，缓存满，应删除键1
    lru_cache.put(4, 4)
    print("插入 (4,4)，缓存满，删除最久未使用的键1")
    
    # 查询键1，应返回-1
    value = lru_cache.get(1)
    print("查询键1:", value)
    
    # 查询键3和键4
    value = lru_cache.get(3)
    print("查询键3:", value)
    value = lru_cache.get(4)
    print("查询键4:", value)
    
    # 更新键4的值
    lru_cache.put(4, 40)
    print("更新键4的值为40")
    value = lru_cache.get(4)
    print("查询键4:", value)
    
    # 复杂场景测试
    print("\n2. 复杂场景测试:")
    
    lru_cache2 = Code25_LeetCode146_LRUCache(3)
    
    # 插入多个键值对
    for i in range(1, 6):
        lru_cache2.put(i, i * 10)
    
    # 查询所有键
    for i in range(1, 6):
        val = lru_cache2.get(i)
        print(f"查询键{i}: {val}")
    
    # 性能测试
    print("\n3. 性能测试:")
    lru_cache3 = Code25_LeetCode146_LRUCache(100)
    PerformanceTest.testLRUPerformance(lru_cache3, 10000)
    
    print("\n=== 算法复杂度分析 ===")
    print("时间复杂度:")
    print("- get: O(1)")
    print("- put: O(1)")
    print("空间复杂度: O(capacity)")
    
    print("\n=== 工程化应用场景 ===")
    print("1. Web浏览器缓存: 存储最近访问的网页内容")
    print("2. 数据库查询缓存: 缓存热点查询结果")
    print("3. 操作系统页面置换: 管理内存页面")
    print("4. CDN缓存策略: 管理边缘节点内容")
    
    print("\n=== 设计要点 ===")
    print("1. 数据结构选择: 哈希表 + 双向链表实现O(1)操作")
    print("2. 虚拟节点: 使用虚拟头尾节点简化链表操作")
    print("3. 访问顺序维护: 每次访问都将节点移到链表头部")
    print("4. 容量控制: 缓存满时删除链表尾部节点")


if __name__ == "__main__":
    main()