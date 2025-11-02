#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 705. 设计哈希集合 - Python版本

题目来源：https://leetcode.com/problems/design-hashset/
题目描述：不使用任何内建的哈希表库设计一个哈希集合（HashSet）。

算法思路：
1. 使用链地址法解决哈希冲突
2. 选择合适的哈希函数和桶大小
3. 实现动态扩容机制
4. 处理边界情况和异常

时间复杂度：平均O(1)，最坏O(n)
空间复杂度：O(n)

工程化考量：
- 动态扩容策略
- 哈希函数设计
- 内存管理优化
- 异常安全处理
"""

import math
import time
from typing import List, Optional
from bisect import bisect_left, insort

class MyHashSet:
    """
    哈希集合实现类
    """
    
    # 默认初始容量
    DEFAULT_CAPACITY = 16
    # 负载因子阈值
    LOAD_FACTOR = 0.75
    
    def __init__(self, initial_capacity: Optional[int] = None):
        """
        构造函数：初始化哈希集合
        
        Args:
            initial_capacity: 初始容量，如果为None则使用默认容量
        """
        if initial_capacity is None:
            initial_capacity = self.DEFAULT_CAPACITY
        
        if initial_capacity <= 0:
            raise ValueError("初始容量必须大于0")
        
        self.capacity = initial_capacity
        self.size = 0
        # 桶数组，每个桶是一个列表
        self.buckets: List[List[int]] = [[] for _ in range(self.capacity)]
    
    def hash(self, key: int) -> int:
        """
        哈希函数：计算元素的哈希值
        
        Args:
            key: 元素值
            
        Returns:
            哈希值（桶索引）
        """
        # 使用Python内置的hash函数并取模
        return hash(key) % self.capacity
    
    def add(self, key: int) -> None:
        """
        添加元素到哈希集合
        
        Args:
            key: 要添加的元素
        """
        # 检查是否需要扩容
        if self.size / self.capacity >= self.LOAD_FACTOR:
            self._resize()
        
        index = self.hash(key)
        bucket = self.buckets[index]
        
        # 检查元素是否已存在
        if key not in bucket:
            bucket.append(key)
            self.size += 1
    
    def remove(self, key: int) -> None:
        """
        从哈希集合中移除元素
        
        Args:
            key: 要移除的元素
        """
        index = self.hash(key)
        bucket = self.buckets[index]
        
        if key in bucket:
            bucket.remove(key)
            self.size -= 1
    
    def contains(self, key: int) -> bool:
        """
        检查哈希集合是否包含指定元素
        
        Args:
            key: 要检查的元素
            
        Returns:
            如果包含返回True，否则返回False
        """
        index = self.hash(key)
        bucket = self.buckets[index]
        return key in bucket
    
    def _resize(self) -> None:
        """
        动态扩容：当负载因子超过阈值时扩容
        """
        new_capacity = self.capacity * 2
        new_buckets: List[List[int]] = [[] for _ in range(new_capacity)]
        
        # 重新哈希所有元素
        for bucket in self.buckets:
            for key in bucket:
                new_index = hash(key) % new_capacity
                new_buckets[new_index].append(key)
        
        # 更新容量和桶数组
        self.capacity = new_capacity
        self.buckets = new_buckets
    
    def get_size(self) -> int:
        """
        获取哈希集合的大小
        
        Returns:
            元素数量
        """
        return self.size
    
    def is_empty(self) -> bool:
        """
        检查哈希集合是否为空
        
        Returns:
            如果为空返回True，否则返回False
        """
        return self.size == 0
    
    def clear(self) -> None:
        """
        清空哈希集合
        """
        for bucket in self.buckets:
            bucket.clear()
        self.size = 0
    
    def __str__(self) -> str:
        """
        获取哈希集合的字符串表示
        
        Returns:
            字符串表示
        """
        elements = []
        for bucket in self.buckets:
            elements.extend(bucket)
        return f"MyHashSet{{{', '.join(map(str, sorted(elements)))}}}"

class MyHashSetOptimized:
    """
    优化版本：使用更好的哈希函数和更高效的数据结构
    """
    
    DEFAULT_CAPACITY = 16
    LOAD_FACTOR = 0.75
    
    def __init__(self, initial_capacity: Optional[int] = None):
        if initial_capacity is None:
            initial_capacity = self.DEFAULT_CAPACITY
        
        if initial_capacity <= 0:
            raise ValueError("初始容量必须大于0")
        
        self.capacity = initial_capacity
        self.size = 0
        # 使用有序列表提高查找效率
        self.buckets: List[List[int]] = [[] for _ in range(self.capacity)]
    
    def hash(self, key: int) -> int:
        """
        优化的哈希函数：使用乘法哈希法
        """
        # 使用黄金分割率的倒数作为乘数
        A = (math.sqrt(5) - 1) / 2
        fractional_part = (key * A) % 1
        return int(fractional_part * self.capacity)
    
    def add(self, key: int) -> None:
        if self.size / self.capacity >= self.LOAD_FACTOR:
            self._resize()
        
        index = self.hash(key)
        bucket = self.buckets[index]
        
        # 使用二分查找检查元素是否存在（桶内元素有序）
        pos = bisect_left(bucket, key)
        if pos == len(bucket) or bucket[pos] != key:
            # 插入到正确位置保持有序
            insort(bucket, key)
            self.size += 1
    
    def remove(self, key: int) -> None:
        index = self.hash(key)
        bucket = self.buckets[index]
        
        pos = bisect_left(bucket, key)
        if pos < len(bucket) and bucket[pos] == key:
            del bucket[pos]
            self.size -= 1
    
    def contains(self, key: int) -> bool:
        index = self.hash(key)
        bucket = self.buckets[index]
        
        pos = bisect_left(bucket, key)
        return pos < len(bucket) and bucket[pos] == key
    
    def _resize(self) -> None:
        new_capacity = self.capacity * 2
        new_buckets: List[List[int]] = [[] for _ in range(new_capacity)]
        
        for bucket in self.buckets:
            for key in bucket:
                new_index = self.hash(key)
                new_bucket = new_buckets[new_index]
                # 插入时保持有序
                pos = bisect_left(new_bucket, key)
                if pos == len(new_bucket) or new_bucket[pos] != key:
                    new_bucket.insert(pos, key)
        
        self.capacity = new_capacity
        self.buckets = new_buckets
    
    def get_size(self) -> int:
        return self.size
    
    def is_empty(self) -> bool:
        return self.size == 0
    
    def clear(self) -> None:
        for bucket in self.buckets:
            bucket.clear()
        self.size = 0

def test_basic_version():
    """基础版本测试"""
    print("=== 基础版本测试 ===")
    hash_set = MyHashSet()
    
    # 基本操作测试
    hash_set.add(1)
    hash_set.add(2)
    hash_set.add(3)
    
    print(f"添加1,2,3后大小: {hash_set.get_size()}")
    print(f"包含2: {hash_set.contains(2)}")
    print(f"包含4: {hash_set.contains(4)}")
    
    hash_set.remove(2)
    print(f"删除2后包含2: {hash_set.contains(2)}")
    print(f"当前集合: {hash_set}")
    
    # 重复添加测试
    hash_set.add(1)
    print(f"重复添加1后大小: {hash_set.get_size()}")
    
    hash_set.clear()
    print(f"清空后大小: {hash_set.get_size()}")
    print(f"是否为空: {hash_set.is_empty()}")

def test_optimized_version():
    """优化版本测试"""
    print("\n=== 优化版本测试 ===")
    hash_set = MyHashSetOptimized()
    
    # 基本操作测试
    hash_set.add(10)
    hash_set.add(20)
    hash_set.add(30)
    hash_set.add(5)
    hash_set.add(15)
    
    print(f"添加多个元素后大小: {hash_set.get_size()}")
    print(f"包含15: {hash_set.contains(15)}")
    print(f"包含25: {hash_set.contains(25)}")
    
    hash_set.remove(20)
    print(f"删除20后包含20: {hash_set.contains(20)}")
    
    # 测试有序性
    hash_set.add(8)
    hash_set.add(18)
    hash_set.add(28)
    print("添加无序元素后操作正常")

def performance_test():
    """性能对比测试"""
    print("\n=== 性能对比测试 ===")
    test_size = 10000
    
    # 基础版本性能测试
    start_time = time.time()
    basic_set = MyHashSet()
    for i in range(test_size):
        basic_set.add(i)
    for i in range(test_size):
        basic_set.contains(i)
    end_time = time.time()
    print(f"基础版本耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    # 优化版本性能测试
    start_time = time.time()
    optimized_set = MyHashSetOptimized()
    for i in range(test_size):
        optimized_set.add(i)
    for i in range(test_size):
        optimized_set.contains(i)
    end_time = time.time()
    print(f"优化版本耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    # Python内置set性能测试
    start_time = time.time()
    python_set = set()
    for i in range(test_size):
        python_set.add(i)
    for i in range(test_size):
        i in python_set
    end_time = time.time()
    print(f"Python内置set耗时: {(end_time - start_time) * 1000:.2f}ms")

def edge_case_test():
    """边界情况测试"""
    print("\n=== 边界情况测试 ===")
    
    # 边界值测试
    hash_set = MyHashSet(1)  # 最小容量
    
    # 大量重复操作
    for i in range(100):
        hash_set.add(1)
    print(f"重复添加100次1后大小: {hash_set.get_size()}")
    
    # 删除不存在的元素
    hash_set.remove(999)
    print(f"删除不存在的元素后大小: {hash_set.get_size()}")
    
    # 空集合操作
    hash_set.clear()
    print(f"清空后包含1: {hash_set.contains(1)}")
    print(f"清空后是否为空: {hash_set.is_empty()}")
    
    # 负数和零测试
    hash_set.add(-1)
    hash_set.add(0)
    import sys
    hash_set.add(-sys.maxsize - 1)  # 最小整数
    hash_set.add(sys.maxsize)      # 最大整数
    print(f"添加边界值后大小: {hash_set.get_size()}")
    print(f"包含最小整数: {hash_set.contains(-sys.maxsize - 1)}")
    print(f"包含最大整数: {hash_set.contains(sys.maxsize)}")

if __name__ == "__main__":
    try:
        test_basic_version()
        test_optimized_version()
        performance_test()
        edge_case_test()
    except Exception as e:
        print(f"错误: {e}")

"""
复杂度分析：

时间复杂度：
- 添加操作(add): 平均O(1)，最坏O(n)（当所有元素哈希到同一个桶时）
- 删除操作(remove): 平均O(1)，最坏O(n)
- 查询操作(contains): 平均O(1)，最坏O(n)
- 扩容操作(_resize): O(n)

空间复杂度：
- 总空间：O(n + m)，其中n是元素数量，m是桶的数量
- 每个桶需要额外空间存储列表元素

算法优化点：
1. 链地址法：简单有效，易于实现
2. 动态扩容：避免哈希冲突过多
3. 负载因子控制：平衡空间和时间效率
4. 优化版本使用有序桶：提高查找效率

边界情况处理：
- 空集合操作
- 重复元素添加
- 删除不存在的元素
- 边界值（最小/最大整数）
- 初始容量验证

工程化考量：
- 参数可配置性（容量、负载因子）
- 异常处理
- 内存管理
- 性能监控

Python特定优化：
- 使用bisect模块进行二分查找
- 使用类型注解提高代码可读性
- 利用Python的动态特性
- 使用内置hash函数

实际应用场景：
1. 数据库索引：快速查找记录
2. 缓存系统：存储热点数据
3. 编译器：符号表管理
4. 网络路由：快速查找路由表

扩展思考：
1. 如何实现线程安全的哈希集合？
2. 如何支持自定义哈希函数？
3. 如何实现迭代器功能？
4. 如何添加统计信息（命中率、冲突率等）？
"""