#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
高级哈希算法题目集合 - Python版本

本文件包含各大算法平台的高级哈希相关题目，包括：
- 滚动哈希 (Rabin-Karp算法)
- 布隆过滤器 (Bloom Filter)
- 一致性哈希 (Consistent Hashing)
- 完美哈希 (Perfect Hashing)
- 分布式哈希表 (DHT)

这些算法在分布式系统、大数据处理、网络安全等领域有重要应用
"""

import math
import random
import hashlib
from typing import List, Dict, Set, Tuple, Optional, Any
from collections import defaultdict, OrderedDict


class RollingHashSolution:
    """
    滚动哈希算法实现 (Rabin-Karp算法)
    应用场景：字符串匹配、子串查找、重复检测等
    
    算法原理：
    1. 使用多项式哈希函数计算字符串的哈希值
    2. 通过滑动窗口实现O(1)时间复杂度的哈希值更新
    3. 使用大质数取模防止整数溢出
    
    时间复杂度：O(n + m)，其中n是文本长度，m是模式长度
    空间复杂度：O(1)
    """
    
    def __init__(self):
        self.BASE = 256  # 字符集大小
        self.MOD = 1000000007  # 大质数
    
    def rabin_karp(self, text: str, pattern: str) -> List[int]:
        """
        Rabin-Karp字符串匹配算法
        
        Args:
            text: 文本字符串
            pattern: 模式字符串
            
        Returns:
            模式在文本中出现的起始位置列表
        """
        if not text or not pattern or len(pattern) > len(text):
            return []
        
        n, m = len(text), len(pattern)
        result = []
        
        # 计算BASE^(m-1) mod MOD
        highest_power = 1
        for _ in range(m - 1):
            highest_power = (highest_power * self.BASE) % self.MOD
        
        # 计算模式和文本前m个字符的哈希值
        pattern_hash = 0
        text_hash = 0
        
        for i in range(m):
            pattern_hash = (pattern_hash * self.BASE + ord(pattern[i])) % self.MOD
            text_hash = (text_hash * self.BASE + ord(text[i])) % self.MOD
        
        # 滑动窗口匹配
        for i in range(n - m + 1):
            # 哈希值匹配时，进行精确比较（防止哈希冲突）
            if pattern_hash == text_hash:
                if text[i:i+m] == pattern:
                    result.append(i)
            
            # 更新下一个窗口的哈希值
            if i < n - m:
                text_hash = (text_hash - ord(text[i]) * highest_power) % self.MOD
                text_hash = (text_hash * self.BASE + ord(text[i + m])) % self.MOD
                
                # 处理负数
                if text_hash < 0:
                    text_hash += self.MOD
        
        return result
    
    def count_distinct_substrings(self, s: str) -> int:
        """
        计算字符串的所有不同子串数量（使用滚动哈希）
        
        Args:
            s: 输入字符串
            
        Returns:
            不同子串的数量
        """
        if not s:
            return 0
        
        hash_set = set()
        n = len(s)
        
        for i in range(n):
            current_hash = 0
            for j in range(i, n):
                current_hash = (current_hash * self.BASE + ord(s[j])) % self.MOD
                hash_set.add(current_hash)
        
        return len(hash_set)
    
    def longest_repeating_substring(self, s: str) -> str:
        """
        查找最长重复子串（使用滚动哈希和二分搜索）
        
        Args:
            s: 输入字符串
            
        Returns:
            最长重复子串
        """
        if not s:
            return ""
        
        n = len(s)
        left, right = 1, n - 1
        result = ""
        
        while left <= right:
            mid = left + (right - left) // 2
            found = self._find_repeating_substring(s, mid)
            
            if found:
                result = found
                left = mid + 1
            else:
                right = mid - 1
        
        return result
    
    def _find_repeating_substring(self, s: str, length: int) -> str:
        """查找指定长度的重复子串"""
        hash_map = defaultdict(list)
        current_hash = 0
        power = 1
        
        # 计算BASE^(length-1) mod MOD
        for _ in range(length - 1):
            power = (power * self.BASE) % self.MOD
        
        # 计算第一个窗口的哈希值
        for i in range(length):
            current_hash = (current_hash * self.BASE + ord(s[i])) % self.MOD
        
        hash_map[current_hash].append(0)
        
        # 滑动窗口
        for i in range(1, len(s) - length + 1):
            current_hash = (current_hash - ord(s[i - 1]) * power) % self.MOD
            current_hash = (current_hash * self.BASE + ord(s[i + length - 1])) % self.MOD
            
            if current_hash < 0:
                current_hash += self.MOD
            
            if current_hash in hash_map:
                current_substring = s[i:i+length]
                for start in hash_map[current_hash]:
                    if s[start:start+length] == current_substring:
                        return current_substring
            
            hash_map[current_hash].append(i)
        
        return ""


class BloomFilter:
    """
    布隆过滤器实现
    应用场景：大规模数据去重、缓存穿透防护、垃圾邮件过滤等
    
    算法原理：
    1. 使用k个哈希函数将元素映射到位数组的k个位置
    2. 查询时检查所有k个位置是否都为1
    3. 存在假阳性（false positive），但不存在假阴性（false negative）
    
    时间复杂度：O(k)
    空间复杂度：O(m)，其中m是位数组大小
    """
    
    def __init__(self, expected_elements: int, false_positive_rate: float):
        """
        构造函数
        
        Args:
            expected_elements: 预期元素数量
            false_positive_rate: 期望的假阳性率
        """
        self.size = self._calculate_size(expected_elements, false_positive_rate)
        self.hash_count = self._calculate_hash_count(expected_elements, self.size)
        self.bit_array = [False] * self.size
    
    def _calculate_size(self, n: int, p: float) -> int:
        """计算位数组大小"""
        if p == 0:
            p = float('inf')
        return int(-n * math.log(p) / (math.log(2) * math.log(2)))
    
    def _calculate_hash_count(self, n: int, m: int) -> int:
        """计算哈希函数数量"""
        return max(1, int(round(m / n * math.log(2))))
    
    def _hash(self, element: str, seed: int) -> int:
        """哈希函数（使用不同的种子生成不同的哈希值）"""
        hash_val = 0
        for char in element:
            hash_val = seed * hash_val + ord(char)
        return hash_val
    
    def add(self, element: str) -> None:
        """添加元素"""
        for i in range(self.hash_count):
            hash_val = self._hash(element, i)
            index = abs(hash_val % self.size)
            self.bit_array[index] = True
    
    def contains(self, element: str) -> bool:
        """检查元素是否存在"""
        for i in range(self.hash_count):
            hash_val = self._hash(element, i)
            index = abs(hash_val % self.size)
            if not self.bit_array[index]:
                return False
        return True
    
    def estimate_false_positive_rate(self, inserted_elements: int) -> float:
        """获取当前假阳性率的估计值"""
        return math.pow(1 - math.exp(-self.hash_count * inserted_elements / self.size), self.hash_count)
    
    def get_usage_rate(self) -> float:
        """获取位数组的使用率"""
        used = sum(1 for bit in self.bit_array if bit)
        return used / self.size


class ConsistentHashing:
    """
    一致性哈希算法实现
    应用场景：分布式缓存、负载均衡、分布式存储等
    
    算法原理：
    1. 将节点和键都映射到哈希环上
    2. 每个键顺时针找到的第一个节点就是其归属节点
    3. 节点增减时，只有少量数据需要重新分配
    
    时间复杂度：O(log n) 查找，其中n是虚拟节点数量
    空间复杂度：O(n)
    """
    
    def __init__(self, virtual_node_count: int):
        """
        构造函数
        
        Args:
            virtual_node_count: 每个物理节点的虚拟节点数量
        """
        self.circle = OrderedDict()  # 哈希环
        self.virtual_node_count = virtual_node_count
    
    def _hash(self, key: str) -> int:
        """哈希函数"""
        return abs(hash(key))
    
    def add_node(self, node: str) -> None:
        """添加节点"""
        for i in range(self.virtual_node_count):
            virtual_node = f"{node}#{i}"
            hash_val = self._hash(virtual_node)
            self.circle[hash_val] = node
        
        # 重新排序哈希环
        self.circle = OrderedDict(sorted(self.circle.items()))
    
    def remove_node(self, node: str) -> None:
        """移除节点"""
        for i in range(self.virtual_node_count):
            virtual_node = f"{node}#{i}"
            hash_val = self._hash(virtual_node)
            if hash_val in self.circle:
                del self.circle[hash_val]
    
    def get_node(self, key: str) -> Optional[str]:
        """获取键对应的节点"""
        if not self.circle:
            return None
        
        hash_val = self._hash(key)
        
        # 找到第一个大于等于hash_val的节点
        for node_hash in sorted(self.circle.keys()):
            if node_hash >= hash_val:
                return self.circle[node_hash]
        
        # 环回，返回第一个节点
        return self.circle[next(iter(self.circle))]
    
    def get_all_nodes(self) -> Set[str]:
        """获取所有节点"""
        return set(self.circle.values())
    
    def calculate_imbalance(self, key_distribution: Dict[str, int]) -> float:
        """计算数据分布的不平衡度"""
        if not key_distribution:
            return 0.0
        
        values = list(key_distribution.values())
        average = sum(values) / len(values)
        
        variance = sum((x - average) ** 2 for x in values) / len(values)
        return math.sqrt(variance) / average  # 变异系数


class PerfectHashTable:
    """
    完美哈希实现（两级哈希表）
    应用场景：静态数据集、编译器符号表、字典等
    
    算法原理：
    1. 第一级哈希将元素分组
    2. 第二级为每个组创建无冲突的哈希表
    3. 保证O(1)查找时间，无哈希冲突
    
    时间复杂度：O(1) 查找
    空间复杂度：O(n)
    """
    
    class HashFunction:
        """哈希函数类"""
        def __init__(self, a: int, b: int, table_size: int):
            self.a = a
            self.b = b
            self.table_size = table_size
        
        def hash(self, key: str) -> int:
            """计算哈希值"""
            return abs(self.a * hash(key) + self.b) % self.table_size
    
    def __init__(self, keys: Set[str]):
        """
        构造函数
        
        Args:
            keys: 键集合
        """
        self.size = len(keys)
        self.second_level = [None] * self.size  # 第二级哈希函数
        self.tables = [None] * self.size  # 存储数据的表
        
        self._build_perfect_hash(keys)
    
    def _first_level_hash(self, key: str) -> int:
        """第一级哈希函数"""
        return abs(hash(key)) % self.size
    
    def _find_perfect_hash_function(self, keys: List[str]) -> 'PerfectHashTable.HashFunction':
        """为给定的键集合找到无冲突的哈希函数"""
        if not keys:
            return self.HashFunction(0, 0, 0)
        
        table_size = len(keys) * len(keys)  # 平方空间保证无冲突
        
        # 尝试不同的哈希参数直到找到无冲突的
        while True:
            a = random.randint(1, 1000)
            b = random.randint(1, 1000)
            
            positions = set()
            collision = False
            
            for key in keys:
                pos = abs(a * hash(key) + b) % table_size
                if pos in positions:
                    collision = True
                    break
                positions.add(pos)
            
            if not collision:
                return self.HashFunction(a, b, table_size)
    
    def _build_perfect_hash(self, keys: Set[str]) -> None:
        """构建完美哈希表"""
        # 第一级：将键分组
        groups = defaultdict(list)
        
        for key in keys:
            hash_val = self._first_level_hash(key)
            groups[hash_val].append(key)
        
        # 第二级：为每个组构建无冲突哈希表
        for group_index, group_keys in groups.items():
            # 为这个组找到无冲突的哈希函数
            hash_func = self._find_perfect_hash_function(group_keys)
            self.second_level[group_index] = hash_func
            
            # 创建第二级哈希表
            self.tables[group_index] = [None] * hash_func.table_size
            for key in group_keys:
                pos = hash_func.hash(key)
                self.tables[group_index][pos] = key
    
    def contains(self, key: str) -> bool:
        """查找键"""
        first_level = self._first_level_hash(key)
        if self.second_level[first_level] is None:
            return False
        
        second_level_pos = self.second_level[first_level].hash(key)
        return key == self.tables[first_level][second_level_pos]


def test_advanced_hash_problems():
    """单元测试函数"""
    print("=== 高级哈希算法测试 ===\n")
    
    # 测试滚动哈希
    print("1. 滚动哈希算法测试:")
    rolling_hash = RollingHashSolution()
    
    text = "abracadabra"
    pattern = "cad"
    positions = rolling_hash.rabin_karp(text, pattern)
    print(f"文本: '{text}', 模式: '{pattern}'")
    print(f"匹配位置: {positions}")
    
    distinct_count = rolling_hash.count_distinct_substrings("abc")
    print(f"字符串'abc'的不同子串数量: {distinct_count}")
    
    repeating = rolling_hash.longest_repeating_substring("banana")
    print(f"字符串'banana'的最长重复子串: '{repeating}'")
    
    # 测试布隆过滤器
    print("\n2. 布隆过滤器测试:")
    bloom_filter = BloomFilter(1000, 0.01)
    
    bloom_filter.add("hello")
    bloom_filter.add("world")
    bloom_filter.add("test")
    
    print(f"包含'hello': {bloom_filter.contains('hello')}")
    print(f"包含'unknown': {bloom_filter.contains('unknown')}")
    print(f"使用率: {bloom_filter.get_usage_rate():.2%}")
    
    # 测试一致性哈希
    print("\n3. 一致性哈希测试:")
    consistent_hashing = ConsistentHashing(100)
    
    consistent_hashing.add_node("node1")
    consistent_hashing.add_node("node2")
    consistent_hashing.add_node("node3")
    
    distribution = {}
    for i in range(1000):
        key = f"key{i}"
        node = consistent_hashing.get_node(key)
        distribution[node] = distribution.get(node, 0) + 1
    
    print(f"数据分布: {distribution}")
    print(f"不平衡度: {consistent_hashing.calculate_imbalance(distribution):.4f}")
    
    # 测试完美哈希
    print("\n4. 完美哈希测试:")
    keys = {"apple", "banana", "cherry", "date", "elderberry"}
    perfect_hash = PerfectHashTable(keys)
    
    for key in keys:
        print(f"包含'{key}': {perfect_hash.contains(key)}")
    print(f"包含'unknown': {perfect_hash.contains('unknown')}")
    
    print("\n=== 算法复杂度分析 ===")
    print("1. 滚动哈希: O(n+m) 时间, O(1) 空间")
    print("2. 布隆过滤器: O(k) 时间, O(m) 空间")
    print("3. 一致性哈希: O(log n) 时间, O(n) 空间")
    print("4. 完美哈希: O(1) 时间, O(n) 空间")
    
    print("\n=== Python工程化应用场景 ===")
    print("1. 滚动哈希: 字符串匹配、重复检测、版本控制")
    print("2. 布隆过滤器: 缓存系统、垃圾邮件过滤、爬虫去重")
    print("3. 一致性哈希: 分布式缓存、负载均衡、分布式存储")
    print("4. 完美哈希: 编译器符号表、静态字典、配置文件")


if __name__ == "__main__":
    test_advanced_hash_problems()