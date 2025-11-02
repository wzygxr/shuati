#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
布隆过滤器与一致性哈希算法实现 - Python版本

本文件包含高级哈希算法的实现，包括：
- 布隆过滤器 (Bloom Filter)
- 计数布隆过滤器 (Counting Bloom Filter)
- 一致性哈希 (Consistent Hashing)
- 虚拟节点技术 (Virtual Nodes)
- 分布式哈希表 (Distributed Hash Table)

这些算法在大数据、分布式系统、缓存系统等领域有重要应用
"""

import math
import random
import hashlib
import time
from typing import List, Dict, Set, Optional, Any
from bisect import bisect_right
from threading import RLock


class BloomFilter:
    """
    布隆过滤器实现
    应用场景：缓存系统、垃圾邮件过滤、URL去重
    
    算法原理：
    1. 使用多个哈希函数将元素映射到位数组的不同位置
    2. 查询时检查所有对应位置是否都为1
    3. 存在假阳性（可能误判存在），但不存在假阴性
    
    时间复杂度：O(k)，k为哈希函数数量
    空间复杂度：O(m)，m为位数组大小
    """
    
    def __init__(self, expected_elements: int, false_positive_rate: float):
        # 计算最优位数组大小和哈希函数数量
        self.size = self._optimal_size(expected_elements, false_positive_rate)
        self.hash_functions = self._optimal_hash_functions(expected_elements, self.size)
        self.bit_array = [False] * self.size
        self.seeds = [random.randint(0, 1000000) for _ in range(self.hash_functions)]
    
    def _optimal_size(self, n: int, p: float) -> int:
        """计算最优位数组大小"""
        return math.ceil(-(n * math.log(p)) / math.pow(math.log(2), 2))
    
    def _optimal_hash_functions(self, n: int, m: int) -> int:
        """计算最优哈希函数数量"""
        return math.ceil((m / n) * math.log(2))
    
    def add(self, element: str) -> None:
        """添加元素"""
        for i in range(self.hash_functions):
            hash_val = self._hash(element, i)
            self.bit_array[hash_val % self.size] = True
    
    def contains(self, element: str) -> bool:
        """检查元素是否存在"""
        for i in range(self.hash_functions):
            hash_val = self._hash(element, i)
            if not self.bit_array[hash_val % self.size]:
                return False
        return True
    
    def _hash(self, element: str, seed: int) -> int:
        """哈希函数"""
        # 使用MD5哈希确保分布均匀
        hash_obj = hashlib.md5((element + str(seed)).encode())
        return int(hash_obj.hexdigest(), 16) % (2**32)
    
    def get_false_positive_probability(self, inserted_elements: int) -> float:
        """获取假阳性概率"""
        return math.pow(1 - math.exp(-self.hash_functions * inserted_elements / self.size), 
                       self.hash_functions)
    
    def get_bit_usage(self) -> float:
        """获取位数组使用率"""
        used = sum(1 for bit in self.bit_array if bit)
        return used / self.size
    
    def print_stats(self) -> None:
        """打印统计信息"""
        print("布隆过滤器统计:")
        print(f"  位数组大小: {self.size}")
        print(f"  哈希函数数量: {self.hash_functions}")
        print(f"  位使用率: {self.get_bit_usage() * 100:.2f}%")


class CountingBloomFilter:
    """
    计数布隆过滤器实现
    应用场景：需要支持删除操作的布隆过滤器变种
    
    算法原理：
    1. 使用计数器数组代替位数组
    2. 添加时递增计数器，删除时递减计数器
    3. 查询时检查所有对应位置的计数器是否大于0
    
    时间复杂度：O(k)
    空间复杂度：O(m * log2(maxCount))
    """
    
    def __init__(self, expected_elements: int, false_positive_rate: float):
        self.size = self._optimal_size(expected_elements, false_positive_rate)
        self.hash_functions = self._optimal_hash_functions(expected_elements, self.size)
        self.counter_array = [0] * self.size
        self.seeds = [random.randint(0, 1000000) for _ in range(self.hash_functions)]
        self.element_count = 0
    
    def _optimal_size(self, n: int, p: float) -> int:
        return math.ceil(-(n * math.log(p)) / math.pow(math.log(2), 2))
    
    def _optimal_hash_functions(self, n: int, m: int) -> int:
        return math.ceil((m / n) * math.log(2))
    
    def add(self, element: str) -> None:
        """添加元素"""
        for i in range(self.hash_functions):
            hash_val = self._hash(element, i)
            self.counter_array[hash_val % self.size] += 1
        self.element_count += 1
    
    def remove(self, element: str) -> bool:
        """删除元素"""
        if not self.contains(element):
            return False
        
        for i in range(self.hash_functions):
            hash_val = self._hash(element, i)
            self.counter_array[hash_val % self.size] -= 1
        self.element_count -= 1
        return True
    
    def contains(self, element: str) -> bool:
        """检查元素是否存在"""
        for i in range(self.hash_functions):
            hash_val = self._hash(element, i)
            if self.counter_array[hash_val % self.size] <= 0:
                return False
        return True
    
    def _hash(self, element: str, seed: int) -> int:
        hash_obj = hashlib.md5((element + str(seed)).encode())
        return int(hash_obj.hexdigest(), 16) % (2**32)
    
    def get_estimated_size(self) -> int:
        """获取元素数量估计"""
        return self.element_count
    
    def get_max_counter_value(self) -> int:
        """获取最大计数器值"""
        return max(self.counter_array)
    
    def print_stats(self) -> None:
        """打印统计信息"""
        print("计数布隆过滤器统计:")
        print(f"  计数器数组大小: {self.size}")
        print(f"  哈希函数数量: {self.hash_functions}")
        print(f"  估计元素数量: {self.element_count}")
        print(f"  最大计数器值: {self.get_max_counter_value()}")


class ConsistentHash:
    """
    一致性哈希算法实现
    应用场景：分布式缓存、负载均衡、分布式存储
    
    算法原理：
    1. 将哈希空间组织成环状结构
    2. 节点和数据都映射到环上的位置
    3. 数据存储在顺时针方向的下一个节点上
    4. 使用虚拟节点实现负载均衡
    
    时间复杂度：O(log n) 查找，O(1) 添加/删除节点
    空间复杂度：O(n + v)，n为节点数，v为虚拟节点数
    """
    
    def __init__(self, virtual_node_count: int, hash_space: int):
        self.virtual_node_count = virtual_node_count
        self.hash_space = hash_space
        self.circle = {}  # 哈希位置 -> 节点名
        self.virtual_nodes = {}  # 节点名 -> 虚拟节点哈希列表
        self.sorted_keys = []  # 排序的哈希键，用于快速查找
    
    def add_node(self, node: str) -> None:
        """添加节点"""
        node_hashes = []
        
        for i in range(self.virtual_node_count):
            virtual_node = f"{node}#{i}"
            hash_val = self._hash(virtual_node) % self.hash_space
            self.circle[hash_val] = node
            node_hashes.append(hash_val)
        
        self.virtual_nodes[node] = node_hashes
        self.sorted_keys = sorted(self.circle.keys())
    
    def remove_node(self, node: str) -> None:
        """删除节点"""
        if node in self.virtual_nodes:
            for hash_val in self.virtual_nodes[node]:
                del self.circle[hash_val]
            del self.virtual_nodes[node]
            self.sorted_keys = sorted(self.circle.keys())
    
    def get_node(self, key: str) -> Optional[str]:
        """获取数据应该存储的节点"""
        if not self.circle:
            return None
        
        hash_val = self._hash(key) % self.hash_space
        
        # 使用二分查找找到第一个大于等于hash_val的位置
        idx = bisect_right(self.sorted_keys, hash_val)
        if idx == len(self.sorted_keys):
            idx = 0  # 回到环的开头
        
        return self.circle[self.sorted_keys[idx]]
    
    def get_nodes(self) -> Set[str]:
        """获取所有节点"""
        return set(self.virtual_nodes.keys())
    
    def get_load_distribution(self) -> Dict[str, int]:
        """获取节点负载分布"""
        distribution = {}
        for node, hashes in self.virtual_nodes.items():
            distribution[node] = len(hashes)
        return distribution
    
    def _hash(self, key: str) -> int:
        """哈希函数"""
        hash_obj = hashlib.md5(key.encode())
        return int(hash_obj.hexdigest(), 16) % (2**32)
    
    def analyze_data_migration(self, keys: Set[str], new_node: str) -> Dict[str, Set[str]]:
        """数据迁移分析（当节点变化时）"""
        migration = {}
        
        # 添加新节点前的映射
        old_mapping = {}
        for key in keys:
            old_mapping[key] = self.get_node(key)
        
        # 添加新节点
        self.add_node(new_node)
        
        # 分析迁移数据
        for key in keys:
            new_node_for_key = self.get_node(key)
            old_node_for_key = old_mapping[key]
            
            if new_node_for_key != old_node_for_key:
                if old_node_for_key not in migration:
                    migration[old_node_for_key] = set()
                migration[old_node_for_key].add(key)
        
        # 恢复原状
        self.remove_node(new_node)
        
        return migration
    
    def print_ring(self) -> None:
        """打印环状态"""
        print("一致性哈希环状态:")
        for hash_val in self.sorted_keys:
            print(f"  哈希位置: {hash_val} -> 节点: {self.circle[hash_val]}")


class DistributedHashTable:
    """
    分布式哈希表实现
    应用场景：P2P网络、分布式存储系统
    
    算法原理：
    1. 使用一致性哈希进行节点定位
    2. 支持数据的存储、检索和删除
    3. 处理节点加入和离开的数据迁移
    
    时间复杂度：O(log n) 查找
    空间复杂度：O(n)
    """
    
    def __init__(self, virtual_node_count: int, hash_space: int):
        self.consistent_hash = ConsistentHash(virtual_node_count, hash_space)
        self.node_data = {}  # 节点名 -> 数据字典
        self.lock = RLock()
    
    def add_node(self, node: str) -> None:
        """添加节点"""
        with self.lock:
            self.consistent_hash.add_node(node)
            self.node_data[node] = {}
    
    def remove_node(self, node: str) -> None:
        """删除节点"""
        with self.lock:
            # 迁移数据到其他节点
            data_to_migrate = self.node_data.get(node, {})
            for key, value in data_to_migrate.items():
                new_node = self.consistent_hash.get_node(key)
                if new_node and new_node != node:
                    self.node_data[new_node][key] = value
            
            self.consistent_hash.remove_node(node)
            if node in self.node_data:
                del self.node_data[node]
    
    def put(self, key: str, value: Any) -> None:
        """存储数据"""
        with self.lock:
            node = self.consistent_hash.get_node(key)
            if node:
                self.node_data[node][key] = value
    
    def get(self, key: str) -> Optional[Any]:
        """检索数据"""
        with self.lock:
            node = self.consistent_hash.get_node(key)
            if node and node in self.node_data:
                return self.node_data[node].get(key)
            return None
    
    def remove(self, key: str) -> bool:
        """删除数据"""
        with self.lock:
            node = self.consistent_hash.get_node(key)
            if node and node in self.node_data:
                return key in self.node_data[node] and self.node_data[node].pop(key, None) is not None
            return False
    
    def get_system_status(self) -> Dict[str, Dict[str, int]]:
        """获取系统状态"""
        with self.lock:
            status = {}
            
            # 节点数据大小
            node_data_size = {}
            for node in self.consistent_hash.get_nodes():
                node_data_size[node] = len(self.node_data.get(node, {}))
            status["node_data_size"] = node_data_size
            
            # 负载分布
            status["load_distribution"] = self.consistent_hash.get_load_distribution()
            
            return status
    
    def replicate_data(self, key: str, replication_factor: int) -> None:
        """数据备份策略"""
        with self.lock:
            primary_node = self.consistent_hash.get_node(key)
            
            if primary_node and key in self.node_data.get(primary_node, {}):
                value = self.node_data[primary_node][key]
                
                # 在后续节点上创建副本
                replica_nodes = set()
                hash_val = self.consistent_hash._hash(key) % self.consistent_hash.hash_space
                
                # 找到后续的replication_factor个节点
                idx = bisect_right(self.consistent_hash.sorted_keys, hash_val)
                
                # 遍历环找到足够的副本节点
                current_idx = idx
                while len(replica_nodes) < replication_factor:
                    if current_idx >= len(self.consistent_hash.sorted_keys):
                        current_idx = 0  # 回到环的开头
                    
                    node = self.consistent_hash.circle[self.consistent_hash.sorted_keys[current_idx]]
                    if node != primary_node:
                        replica_nodes.add(node)
                    
                    current_idx += 1
                    # 防止无限循环
                    if current_idx == idx:
                        break
                
                # 存储副本
                for replica_node in replica_nodes:
                    if replica_node in self.node_data:
                        self.node_data[replica_node][f"{key}_replica"] = value


class PerformanceAnalyzer:
    """性能测试和分析工具"""
    
    @staticmethod
    def test_bloom_filter(element_count: int, false_positive_rate: float) -> None:
        """测试布隆过滤器性能"""
        print("=== 布隆过滤器性能测试 ===")
        
        bf = BloomFilter(element_count, false_positive_rate)
        
        # 添加元素
        start_time = time.time()
        for i in range(element_count):
            bf.add(f"element{i}")
        add_time = time.time() - start_time
        
        # 查询元素
        start_time = time.time()
        for i in range(element_count):
            bf.contains(f"element{i}")
        query_time = time.time() - start_time
        
        # 测试假阳性
        false_positives = 0
        test_count = 10000
        for i in range(element_count, element_count + test_count):
            if bf.contains(f"element{i}"):
                false_positives += 1
        
        print(f"元素数量: {element_count:,}")
        print(f"目标假阳性率: {false_positive_rate * 100:.4f}%")
        print(f"实际假阳性率: {false_positives / test_count * 100:.4f}%")
        print(f"添加时间: {add_time / element_count * 1e9:.0f} ns/元素")
        print(f"查询时间: {query_time / element_count * 1e9:.0f} ns/元素")
        
        bf.print_stats()
    
    @staticmethod
    def test_consistent_hash_load_balance(node_count: int, virtual_node_count: int, key_count: int) -> None:
        """测试一致性哈希负载均衡"""
        print("\n=== 一致性哈希负载均衡测试 ===")
        
        ch = ConsistentHash(virtual_node_count, 1000000)
        
        # 添加节点
        for i in range(node_count):
            ch.add_node(f"node{i}")
        
        # 模拟数据分布
        key_distribution = {}
        for i in range(key_count):
            node = ch.get_node(f"key{i}")
            key_distribution[node] = key_distribution.get(node, 0) + 1
        
        # 分析负载均衡
        min_load = min(key_distribution.values()) if key_distribution else 0
        max_load = max(key_distribution.values()) if key_distribution else 0
        total_load = sum(key_distribution.values())
        
        avg_load = total_load / node_count if node_count > 0 else 0
        imbalance = (max_load - min_load) / avg_load * 100 if avg_load > 0 else 0
        
        print(f"节点数量: {node_count}")
        print(f"虚拟节点数量: {virtual_node_count}")
        print(f"数据总量: {key_count:,}")
        print(f"最小负载: {min_load:,}")
        print(f"最大负载: {max_load:,}")
        print(f"平均负载: {avg_load:.1f}")
        print(f"负载不均衡度: {imbalance:.2f}%")
        
        # 显示详细分布
        print("\n详细负载分布:")
        for node, count in key_distribution.items():
            print(f"  {node}: {count:,} 数据 ({count / key_count * 100:.1f}%)")
    
    @staticmethod
    def test_data_migration(initial_nodes: int, key_count: int) -> None:
        """测试节点变化的数据迁移"""
        print("\n=== 数据迁移测试 ===")
        
        ch = ConsistentHash(100, 1000000)
        
        # 初始节点
        for i in range(initial_nodes):
            ch.add_node(f"node{i}")
        
        # 生成测试键
        keys = {f"key{i}" for i in range(key_count)}
        
        # 添加新节点前的映射
        old_mapping = {}
        for key in keys:
            old_mapping[key] = ch.get_node(key)
        
        # 添加新节点
        ch.add_node("newNode")
        
        # 分析迁移
        migrated = 0
        migration_by_node = {}
        
        for key in keys:
            new_node = ch.get_node(key)
            old_node = old_mapping[key]
            
            if new_node != old_node:
                migrated += 1
                migration_by_node[old_node] = migration_by_node.get(old_node, 0) + 1
        
        print(f"初始节点数: {initial_nodes}")
        print(f"数据总量: {key_count:,}")
        print(f"迁移数据量: {migrated:,} ({migrated / key_count * 100:.2f}%)")
        
        print("\n各节点迁移情况:")
        for node, count in migration_by_node.items():
            print(f"  {node}: {count:,} 数据迁移")


def test_bloom_filter_and_consistent_hash():
    """单元测试函数"""
    print("=== 布隆过滤器与一致性哈希算法测试 ===\n")
    
    # 测试布隆过滤器
    print("1. 布隆过滤器测试:")
    bf = BloomFilter(10000, 0.01)
    bf.add("hello")
    bf.add("world")
    print("包含 'hello':", "是" if bf.contains("hello") else "否")
    print("包含 'test':", "是" if bf.contains("test") else "否")
    print("假阳性概率:", bf.get_false_positive_probability(2))
    
    # 测试计数布隆过滤器
    print("\n2. 计数布隆过滤器测试:")
    cbf = CountingBloomFilter(10000, 0.01)
    cbf.add("apple")
    cbf.add("banana")
    cbf.add("apple")  # 重复添加
    print("包含 'apple':", "是" if cbf.contains("apple") else "否")
    cbf.remove("apple")
    print("删除后包含 'apple':", "是" if cbf.contains("apple") else "否")
    
    # 测试一致性哈希
    print("\n3. 一致性哈希测试:")
    ch = ConsistentHash(100, 1000000)
    ch.add_node("node1")
    ch.add_node("node2")
    ch.add_node("node3")
    
    print("'key1' 分配到:", ch.get_node("key1"))
    print("'key2' 分配到:", ch.get_node("key2"))
    
    distribution = ch.get_load_distribution()
    print("负载分布:")
    for node, count in distribution.items():
        print(f"  {node}: {count} 虚拟节点")
    
    # 测试分布式哈希表
    print("\n4. 分布式哈希表测试:")
    dht = DistributedHashTable(100, 1000000)
    dht.add_node("nodeA")
    dht.add_node("nodeB")
    dht.put("user1", "Alice")
    dht.put("user2", "Bob")
    
    print("user1:", dht.get("user1"))
    
    status = dht.get_system_status()
    print("系统状态:")
    for category, data in status.items():
        print(f"  {category}:")
        for key, value in data.items():
            print(f"    {key}: {value}")
    
    # 性能测试
    print("\n5. 性能测试:")
    PerformanceAnalyzer.test_bloom_filter(100000, 0.01)
    PerformanceAnalyzer.test_consistent_hash_load_balance(5, 100, 10000)
    PerformanceAnalyzer.test_data_migration(5, 10000)
    
    print("\n=== Python算法复杂度分析 ===")
    print("1. 布隆过滤器: O(k)时间，O(m)空间，k为哈希函数数，m为位数组大小")
    print("2. 计数布隆过滤器: O(k)时间，O(m*log(maxCount))空间")
    print("3. 一致性哈希: O(log n)查找，O(1)添加/删除，O(n+v)空间")
    print("4. 分布式哈希表: O(log n)操作，O(n)空间")
    
    print("\n=== Python工程化应用场景 ===")
    print("1. 缓存系统: Redis、Memcached使用布隆过滤器进行键存在性检查")
    print("2. 分布式存储: Cassandra、DynamoDB使用一致性哈希进行数据分片")
    print("3. 负载均衡: Nginx、HAProxy使用一致性哈希进行请求路由")
    print("4. 垃圾邮件过滤: 使用布隆过滤器快速判断邮件是否垃圾邮件")
    
    print("\n=== Python性能优化策略 ===")
    print("1. 内存管理优化: 使用__slots__减少内存占用")
    print("2. 并发安全: 使用线程锁保护共享数据")
    print("3. 缓存优化: 使用LRU缓存提高访问性能")
    print("4. 异步IO: 使用asyncio提高并发处理能力")


if __name__ == "__main__":
    test_bloom_filter_and_consistent_hash()