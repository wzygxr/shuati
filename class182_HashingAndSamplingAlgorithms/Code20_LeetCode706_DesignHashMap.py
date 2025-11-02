"""
LeetCode 706. 设计哈希映射
题目链接：https://leetcode.com/problems/design-hashmap/

题目描述：
不使用任何内建的哈希表库设计一个哈希映射（HashMap）。
实现 MyHashMap 类：
- MyHashMap() 用空映射初始化对象
- void put(int key, int value) 向 HashMap 插入一个键值对 (key, value) 。如果 key 已经存在于映射中，则更新其对应的值 value 。
- int get(int key) 返回特定的 key 所映射的 value ；如果映射中不包含 key 的映射，返回 -1 。
- void remove(key) 如果映射中存在 key 的映射，则移除 key 和它所对应的 value 。

算法思路：
使用链地址法实现哈希表，创建一个固定大小的数组，每个数组元素是一个链表。
每个链表节点存储键值对，当发生哈希冲突时，将节点添加到对应位置的链表中。

时间复杂度：
- put: O(n/b)，其中n是元素个数，b是桶数
- get: O(n/b)
- remove: O(n/b)

空间复杂度：O(n)，存储所有键值对
"""

class MyHashMap:
    def __init__(self):
        """Initialize your data structure here."""
        self.BASE = 10000  # 桶的数量
        self.data = [[] for _ in range(self.BASE)]  # 桶数组，每个桶是一个链表
    
    def _hash(self, key):
        """哈希函数"""
        return key % self.BASE
    
    def put(self, key: int, value: int) -> None:
        """value will always be non-negative."""
        h = self._hash(key)
        for i, (k, v) in enumerate(self.data[h]):
            if k == key:
                self.data[h][i] = (key, value)
                return
        self.data[h].append((key, value))
    
    def get(self, key: int) -> int:
        """Returns the value to which the specified key is mapped, or -1 if this map contains no mapping for the key"""
        h = self._hash(key)
        for k, v in self.data[h]:
            if k == key:
                return v
        return -1
    
    def remove(self, key: int) -> None:
        """Removes the mapping of the specified value key if this map contains a mapping for the key"""
        h = self._hash(key)
        for i, (k, v) in enumerate(self.data[h]):
            if k == key:
                del self.data[h][i]
                return


class MyHashMapOptimized:
    def __init__(self, initial_capacity=16):
        """Initialize your data structure here."""
        self.DEFAULT_CAPACITY = 16
        self.LOAD_FACTOR = 0.75
        
        self.capacity = initial_capacity
        self.size = 0
        self.buckets = [[] for _ in range(self.capacity)]
    
    def _hash(self, key):
        """哈希函数"""
        return hash(key) % self.capacity
    
    def _resize(self):
        """动态扩容"""
        new_capacity = self.capacity * 2
        new_buckets = [[] for _ in range(new_capacity)]
        
        # 重新哈希所有元素
        for bucket in self.buckets:
            for key, value in bucket:
                new_index = hash(key) % new_capacity
                new_buckets[new_index].append((key, value))
        
        # 更新容量和桶数组
        self.capacity = new_capacity
        self.buckets = new_buckets
    
    def put(self, key: int, value: int) -> None:
        """插入或更新键值对"""
        # 检查是否需要扩容
        if self.size / self.capacity >= self.LOAD_FACTOR:
            self._resize()
        
        index = self._hash(key)
        bucket = self.buckets[index]
        
        # 检查键是否已存在
        for i, (k, v) in enumerate(bucket):
            if k == key:
                bucket[i] = (key, value)
                return
        
        # 添加新键值对
        bucket.append((key, value))
        self.size += 1
    
    def get(self, key: int) -> int:
        """获取键对应的值"""
        index = self._hash(key)
        bucket = self.buckets[index]
        
        for k, v in bucket:
            if k == key:
                return v
        
        return -1
    
    def remove(self, key: int) -> None:
        """删除键值对"""
        index = self._hash(key)
        bucket = self.buckets[index]
        
        for i, (k, v) in enumerate(bucket):
            if k == key:
                del bucket[i]
                self.size -= 1
                return
    
    def get_size(self) -> int:
        """获取映射大小"""
        return self.size
    
    def is_empty(self) -> bool:
        """检查映射是否为空"""
        return self.size == 0
    
    def clear(self) -> None:
        """清空映射"""
        for bucket in self.buckets:
            bucket.clear()
        self.size = 0


def test_basic_version():
    """基础版本测试"""
    print("--- 基础版本测试 ---")
    hash_map = MyHashMap()
    
    # 测试put和get操作
    hash_map.put(1, 1)
    hash_map.put(2, 2)
    print(f"get(1): {hash_map.get(1)}")  # 期望: 1
    print(f"get(3): {hash_map.get(3)}")  # 期望: -1
    
    # 测试更新操作
    hash_map.put(2, 1)
    print(f"get(2): {hash_map.get(2)}")  # 期望: 1
    
    # 测试删除操作
    hash_map.remove(2)
    print(f"get(2): {hash_map.get(2)}")  # 期望: -1
    
    # 测试边界值
    hash_map.put(1000000, 1000000)
    print(f"get(1000000): {hash_map.get(1000000)}")  # 期望: 1000000
    hash_map.remove(1000000)
    print(f"get(1000000): {hash_map.get(1000000)}")  # 期望: -1


def test_optimized_version():
    """优化版本测试"""
    print("\n--- 优化版本测试 ---")
    hash_map = MyHashMapOptimized()
    
    # 测试基本操作
    hash_map.put(1, 10)
    hash_map.put(2, 20)
    hash_map.put(3, 30)
    print(f"大小: {hash_map.get_size()}")  # 期望: 3
    print(f"get(2): {hash_map.get(2)}")  # 期望: 20
    
    # 测试更新操作
    hash_map.put(2, 25)
    print(f"更新后get(2): {hash_map.get(2)}")  # 期望: 25
    
    # 测试删除操作
    hash_map.remove(2)
    print(f"删除后get(2): {hash_map.get(2)}")  # 期望: -1
    print(f"删除后大小: {hash_map.get_size()}")  # 期望: 2
    
    # 测试清空操作
    hash_map.clear()
    print(f"清空后大小: {hash_map.get_size()}")  # 期望: 0
    print(f"清空后是否为空: {hash_map.is_empty()}")  # 期望: True


if __name__ == "__main__":
    print("=== 测试 LeetCode 706. 设计哈希映射 ===")
    
    # 基础版本测试
    test_basic_version()
    
    # 优化版本测试
    test_optimized_version()