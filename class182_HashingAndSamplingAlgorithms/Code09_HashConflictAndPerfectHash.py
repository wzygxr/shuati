"""
哈希冲突解决与完美哈希算法实现 - Python版本

本文件包含高级哈希冲突解决技术和完美哈希算法的Python实现，包括：
- 开放寻址法 (Open Addressing)
- 链地址法 (Separate Chaining)
- 二次探测法 (Quadratic Probing)
- 双重哈希法 (Double Hashing)
- 完美哈希 (Perfect Hashing)
- 布谷鸟哈希 (Cuckoo Hashing)
- 跳房子哈希 (Hopscotch Hashing)

这些算法在哈希表实现、数据库索引、编译器优化等领域有重要应用
"""

import random
import math
from typing import Any, Optional, List, Tuple


class OpenAddressingHashTable:
    """
    开放寻址法哈希表实现
    应用场景：内存受限环境、缓存系统
    
    算法原理：
    1. 所有元素都存储在哈希表数组中
    2. 当发生冲突时，按照特定探测序列寻找下一个空槽
    3. 常见的探测方法：线性探测、二次探测、双重哈希
    
    时间复杂度：O(1) 平均，O(n) 最坏
    空间复杂度：O(n)
    """
    
    DEFAULT_CAPACITY = 16
    DEFAULT_LOAD_FACTOR = 0.75
    
    class Entry:
        def __init__(self, key: Any = None, value: Any = None):
            self.key = key
            self.value = value
            self.occupied = False
    
    def __init__(self, capacity: int = DEFAULT_CAPACITY, load_factor: float = DEFAULT_LOAD_FACTOR):
        self.table = [self.Entry() for _ in range(capacity)]
        self.size = 0
        self.load_factor = load_factor
    
    def _hash(self, key: Any) -> int:
        """哈希函数"""
        return hash(key)
    
    def _linear_probe(self, index: int, i: int) -> int:
        """线性探测"""
        return (index + i) % len(self.table)
    
    def _quadratic_probe(self, index: int, i: int) -> int:
        """二次探测"""
        return (index + i * i) % len(self.table)
    
    def _double_hash(self, index: int, i: int) -> int:
        """双重哈希"""
        hash2 = hash(index) * 31 + 17
        return (index + i * hash2) % len(self.table)
    
    def _find_key_index(self, key: Any) -> int:
        """查找键的索引位置"""
        index = self._hash(key) % len(self.table)
        
        for i in range(len(self.table)):
            probe_index = self._linear_probe(index, i)
            
            if not self.table[probe_index].occupied:
                return -1  # 未找到
            
            if self.table[probe_index].key == key:
                return probe_index
        
        return -1
    
    def _find_slot(self, key: Any) -> int:
        """查找空槽位置"""
        index = self._hash(key) % len(self.table)
        
        for i in range(len(self.table)):
            probe_index = self._linear_probe(index, i)
            
            if not self.table[probe_index].occupied or self.table[probe_index].key == key:
                return probe_index
        
        return -1  # 哈希表已满
    
    def _resize(self):
        """扩容"""
        old_table = self.table
        self.table = [self.Entry() for _ in range(len(old_table) * 2)]
        self.size = 0
        
        for entry in old_table:
            if entry.occupied:
                self.put(entry.key, entry.value)
    
    def put(self, key: Any, value: Any):
        """插入键值对"""
        if self.size / len(self.table) >= self.load_factor:
            self._resize()
        
        index = self._find_slot(key)
        if index != -1:
            if not self.table[index].occupied:
                self.size += 1
            self.table[index] = self.Entry(key, value)
            self.table[index].occupied = True
    
    def get(self, key: Any) -> Optional[Any]:
        """获取值"""
        index = self._find_key_index(key)
        return self.table[index].value if index != -1 else None
    
    def remove(self, key: Any) -> bool:
        """删除键值对"""
        index = self._find_key_index(key)
        if index != -1:
            self.table[index].occupied = False
            self.size -= 1
            return True
        return False
    
    def contains(self, key: Any) -> bool:
        """检查是否包含键"""
        return self._find_key_index(key) != -1
    
    def get_size(self) -> int:
        """获取大小"""
        return self.size
    
    def get_capacity(self) -> int:
        """获取容量"""
        return len(self.table)


class SeparateChainingHashTable:
    """
    链地址法哈希表实现
    应用场景：通用哈希表实现、数据库索引
    
    算法原理：
    1. 每个哈希桶使用链表存储冲突的元素
    2. 插入时计算哈希值，将元素添加到对应链表中
    3. 查找时遍历对应链表
    
    时间复杂度：O(1) 平均，O(n) 最坏
    空间复杂度：O(n)
    """
    
    DEFAULT_CAPACITY = 16
    DEFAULT_LOAD_FACTOR = 0.75
    
    class Node:
        def __init__(self, key: Any, value: Any):
            self.key = key
            self.value = value
            self.next = None
    
    def __init__(self, capacity: int = DEFAULT_CAPACITY, load_factor: float = DEFAULT_LOAD_FACTOR):
        self.table = [None] * capacity
        self.size = 0
        self.load_factor = load_factor
    
    def _hash(self, key: Any) -> int:
        """哈希函数"""
        return hash(key)
    
    def _resize(self):
        """扩容"""
        old_table = self.table
        self.table = [None] * (len(old_table) * 2)
        self.size = 0
        
        for bucket in old_table:
            current = bucket
            while current:
                self.put(current.key, current.value)
                current = current.next
    
    def put(self, key: Any, value: Any):
        """插入键值对"""
        if self.size / len(self.table) >= self.load_factor:
            self._resize()
        
        index = self._hash(key) % len(self.table)
        current = self.table[index]
        
        # 检查是否已存在
        while current:
            if current.key == key:
                current.value = value
                return
            current = current.next
        
        # 插入新节点
        new_node = self.Node(key, value)
        new_node.next = self.table[index]
        self.table[index] = new_node
        self.size += 1
    
    def get(self, key: Any) -> Optional[Any]:
        """获取值"""
        index = self._hash(key) % len(self.table)
        current = self.table[index]
        
        while current:
            if current.key == key:
                return current.value
            current = current.next
        
        return None
    
    def remove(self, key: Any) -> bool:
        """删除键值对"""
        index = self._hash(key) % len(self.table)
        current = self.table[index]
        prev = None
        
        while current:
            if current.key == key:
                if prev:
                    prev.next = current.next
                else:
                    self.table[index] = current.next
                self.size -= 1
                return True
            prev = current
            current = current.next
        
        return False
    
    def contains(self, key: Any) -> bool:
        """检查是否包含键"""
        index = self._hash(key) % len(self.table)
        current = self.table[index]
        
        while current:
            if current.key == key:
                return True
            current = current.next
        
        return False
    
    def get_size(self) -> int:
        """获取大小"""
        return self.size
    
    def get_capacity(self) -> int:
        """获取容量"""
        return len(self.table)


class CuckooHashTable:
    """
    布谷鸟哈希实现
    应用场景：高性能哈希表、缓存系统
    
    算法原理：
    1. 使用两个哈希函数和两个哈希表
    2. 插入时检查两个位置，如果都已被占用，则踢出其中一个元素
    3. 被踢出的元素重新插入到另一个哈希表中
    4. 重复此过程直到所有元素都找到位置或达到最大迭代次数
    
    时间复杂度：O(1) 平均，O(n) 最坏
    空间复杂度：O(n)
    """
    
    DEFAULT_CAPACITY = 16
    MAX_ITERATIONS = 100
    
    class Entry:
        def __init__(self, key: Any = None, value: Any = None):
            self.key = key
            self.value = value
            self.occupied = False
    
    def __init__(self, capacity: int = DEFAULT_CAPACITY):
        self.table1 = [self.Entry() for _ in range(capacity)]
        self.table2 = [self.Entry() for _ in range(capacity)]
        self.size = 0
    
    def _hash1(self, key: Any) -> int:
        """第一个哈希函数"""
        return hash(key)
    
    def _hash2(self, key: Any) -> int:
        """第二个哈希函数"""
        return hash(key) * 31 + 17
    
    def _rehash(self):
        """重新哈希所有元素"""
        old_table1 = self.table1.copy()
        old_table2 = self.table2.copy()
        
        self.table1 = [self.Entry() for _ in range(len(old_table1) * 2)]
        self.table2 = [self.Entry() for _ in range(len(old_table2) * 2)]
        self.size = 0
        
        for entry in old_table1:
            if entry.occupied:
                self.put(entry.key, entry.value)
        
        for entry in old_table2:
            if entry.occupied:
                self.put(entry.key, entry.value)
    
    def put(self, key: Any, value: Any) -> bool:
        """插入键值对"""
        if self.contains(key):
            # 更新现有值
            index1 = self._hash1(key) % len(self.table1)
            if self.table1[index1].occupied and self.table1[index1].key == key:
                self.table1[index1].value = value
                return True
            
            index2 = self._hash2(key) % len(self.table2)
            if self.table2[index2].occupied and self.table2[index2].key == key:
                self.table2[index2].value = value
                return True
        
        # 插入新值
        current_entry = self.Entry(key, value)
        
        for _ in range(self.MAX_ITERATIONS):
            # 尝试插入第一个表
            index1 = self._hash1(current_entry.key) % len(self.table1)
            if not self.table1[index1].occupied:
                self.table1[index1] = current_entry
                self.table1[index1].occupied = True
                self.size += 1
                return True
            
            # 交换并尝试第二个表
            self.table1[index1], current_entry = current_entry, self.table1[index1]
            
            index2 = self._hash2(current_entry.key) % len(self.table2)
            if not self.table2[index2].occupied:
                self.table2[index2] = current_entry
                self.table2[index2].occupied = True
                self.size += 1
                return True
            
            # 交换并继续
            self.table2[index2], current_entry = current_entry, self.table2[index2]
        
        # 达到最大迭代次数，需要重新哈希
        self._rehash()
        return self.put(key, value)
    
    def get(self, key: Any) -> Optional[Any]:
        """获取值"""
        index1 = self._hash1(key) % len(self.table1)
        if self.table1[index1].occupied and self.table1[index1].key == key:
            return self.table1[index1].value
        
        index2 = self._hash2(key) % len(self.table2)
        if self.table2[index2].occupied and self.table2[index2].key == key:
            return self.table2[index2].value
        
        return None
    
    def remove(self, key: Any) -> bool:
        """删除键值对"""
        index1 = self._hash1(key) % len(self.table1)
        if self.table1[index1].occupied and self.table1[index1].key == key:
            self.table1[index1].occupied = False
            self.size -= 1
            return True
        
        index2 = self._hash2(key) % len(self.table2)
        if self.table2[index2].occupied and self.table2[index2].key == key:
            self.table2[index2].occupied = False
            self.size -= 1
            return True
        
        return False
    
    def contains(self, key: Any) -> bool:
        """检查是否包含键"""
        index1 = self._hash1(key) % len(self.table1)
        if self.table1[index1].occupied and self.table1[index1].key == key:
            return True
        
        index2 = self._hash2(key) % len(self.table2)
        if self.table2[index2].occupied and self.table2[index2].key == key:
            return True
        
        return False
    
    def get_size(self) -> int:
        """获取大小"""
        return self.size
    
    def get_capacity(self) -> int:
        """获取容量"""
        return len(self.table1) + len(self.table2)


class PerfectHashTable:
    """
    完美哈希实现
    应用场景：静态数据集、编译器符号表、字典实现
    
    算法原理：
    1. 使用两级哈希表结构
    2. 第一级哈希将元素分组到桶中
    3. 第二级为每个桶创建无冲突的哈希表
    4. 通过调整哈希函数参数确保无冲突
    
    时间复杂度：O(1) 查找
    空间复杂度：O(n)
    """
    
    DEFAULT_BUCKETS = 16
    
    class Bucket:
        def __init__(self):
            self.entries = []
            self.hash_table = []
            self.a = 1
            self.b = 0
            self.size = 0
    
    def __init__(self, num_buckets: int = DEFAULT_BUCKETS):
        self.buckets = [self.Bucket() for _ in range(num_buckets)]
        self.total_size = 0
    
    def _primary_hash(self, key: Any) -> int:
        """第一级哈希函数"""
        return hash(key) % len(self.buckets)
    
    def _secondary_hash(self, key: Any, a: int, b: int, size: int) -> int:
        """第二级哈希函数"""
        return (a * hash(key) + b) % size
    
    def _build_hash_table(self, bucket: Bucket) -> bool:
        """构建无冲突哈希表"""
        if not bucket.entries:
            bucket.hash_table = [None] * 1
            return True
        
        n = len(bucket.entries)
        bucket.hash_table = [None] * (n * n)  # 平方空间确保无冲突
        
        for attempt in range(100):
            bucket.a = random.randint(1, 1000)
            bucket.b = random.randint(1, 1000)
            
            bucket.hash_table = [None] * len(bucket.hash_table)
            collision = False
            
            for i, entry in enumerate(bucket.entries):
                index = self._secondary_hash(entry[0], bucket.a, bucket.b, len(bucket.hash_table))
                
                if bucket.hash_table[index] is not None:
                    collision = True
                    break
                
                bucket.hash_table[index] = i
            
            if not collision:
                return True
        
        return False
    
    def build(self, data: List[Tuple[Any, Any]]) -> bool:
        """构建完美哈希表"""
        # 第一级：将数据分配到桶中
        for entry in data:
            bucket_index = self._primary_hash(entry[0])
            self.buckets[bucket_index].entries.append(entry)
        
        # 第二级：为每个桶构建无冲突哈希表
        for bucket in self.buckets:
            if not self._build_hash_table(bucket):
                return False
            bucket.size = len(bucket.entries)
            self.total_size += bucket.size
        
        return True
    
    def get(self, key: Any) -> Optional[Any]:
        """查找值"""
        bucket_index = self._primary_hash(key)
        bucket = self.buckets[bucket_index]
        
        if not bucket.hash_table:
            return None
        
        index = self._secondary_hash(key, bucket.a, bucket.b, len(bucket.hash_table))
        entry_index = bucket.hash_table[index]
        
        if entry_index is not None and bucket.entries[entry_index][0] == key:
            return bucket.entries[entry_index][1]
        
        return None
    
    def contains(self, key: Any) -> bool:
        """检查是否包含键"""
        bucket_index = self._primary_hash(key)
        bucket = self.buckets[bucket_index]
        
        if not bucket.hash_table:
            return False
        
        index = self._secondary_hash(key, bucket.a, bucket.b, len(bucket.hash_table))
        entry_index = bucket.hash_table[index]
        
        return entry_index is not None and bucket.entries[entry_index][0] == key
    
    def get_size(self) -> int:
        """获取总大小"""
        return self.total_size
    
    def get_bucket_count(self) -> int:
        """获取桶数量"""
        return len(self.buckets)


def test_hash_conflict_and_perfect_hash():
    """测试函数"""
    print("=== 哈希冲突解决与完美哈希算法测试 ===")
    
    # 测试开放寻址法
    print("\n1. 开放寻址法测试:")
    open_table = OpenAddressingHashTable()
    open_table.put("apple", 1)
    open_table.put("banana", 2)
    open_table.put("cherry", 3)
    
    print(f"apple: {open_table.get('apple')}")
    print(f"banana: {open_table.get('banana')}")
    print(f"cherry: {open_table.get('cherry')}")
    print(f"size: {open_table.get_size()}")
    
    # 测试链地址法
    print("\n2. 链地址法测试:")
    chain_table = SeparateChainingHashTable()
    chain_table.put("apple", 1)
    chain_table.put("banana", 2)
    chain_table.put("cherry", 3)
    
    print(f"apple: {chain_table.get('apple')}")
    print(f"banana: {chain_table.get('banana')}")
    print(f"cherry: {chain_table.get('cherry')}")
    print(f"size: {chain_table.get_size()}")
    
    # 测试布谷鸟哈希
    print("\n3. 布谷鸟哈希测试:")
    cuckoo_table = CuckooHashTable()
    cuckoo_table.put("apple", 1)
    cuckoo_table.put("banana", 2)
    cuckoo_table.put("cherry", 3)
    
    print(f"apple: {cuckoo_table.get('apple')}")
    print(f"banana: {cuckoo_table.get('banana')}")
    print(f"cherry: {cuckoo_table.get('cherry')}")
    print(f"size: {cuckoo_table.get_size()}")
    
    # 测试完美哈希
    print("\n4. 完美哈希测试:")
    perfect_table = PerfectHashTable()
    data = [("apple", 1), ("banana", 2), ("cherry", 3)]
    
    if perfect_table.build(data):
        print("完美哈希构建成功")
        print(f"apple: {perfect_table.get('apple')}")
        print(f"banana: {perfect_table.get('banana')}")
        print(f"cherry: {perfect_table.get('cherry')}")
        print(f"size: {perfect_table.get_size()}")
    else:
        print("完美哈希构建失败")
    
    print("\n=== 测试完成 ===")


if __name__ == "__main__":
    test_hash_conflict_and_perfect_hash()