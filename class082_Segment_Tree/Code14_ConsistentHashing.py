import hashlib
import bisect
import time

class ConsistentHashing:
    """
    一致性哈希算法实现（Python版本）
    
    题目来源：分布式系统设计面试题
    应用场景：负载均衡、分布式缓存、分布式存储系统
    
    核心思想：
    1. 将哈希空间组织成一个虚拟的圆环（0 ~ 2^32-1）
    2. 服务器节点通过哈希函数映射到环上
    3. 数据通过哈希函数映射到环上，顺时针找到最近的服务器节点
    4. 虚拟节点技术解决数据分布不均问题
    
    时间复杂度：
    - 添加节点：O(k) k为虚拟节点数
    - 删除节点：O(k)
    - 查找节点：O(log n) n为环上节点总数
    
    空间复杂度：O(n*k) n为物理节点数，k为虚拟节点数
    
    工程化考量：
    1. 虚拟节点解决数据倾斜问题
    2. 支持节点的动态增删
    3. 数据迁移最小化
    4. 容错性和可扩展性
    """
    
    def __init__(self, virtual_nodes):
        """
        初始化一致性哈希环
        
        Args:
            virtual_nodes: 每个物理节点的虚拟节点数量
        """
        self.virtual_nodes = virtual_nodes
        # 哈希环，存储虚拟节点到物理节点的映射
        self.ring = {}
        # 排序的哈希键列表，用于快速查找
        self.sorted_keys = []
        # 物理节点集合
        self.physical_nodes = set()
    
    def add_node(self, node):
        """
        添加物理节点
        
        Args:
            node: 物理节点标识
        """
        if node in self.physical_nodes:
            return  # 节点已存在
        
        self.physical_nodes.add(node)
        
        # 为每个物理节点创建虚拟节点
        for i in range(self.virtual_nodes):
            virtual_node = f"{node}#{i}"
            hash_val = self._get_hash(virtual_node)
            self.ring[hash_val] = node
            # 插入排序位置
            bisect.insort(self.sorted_keys, hash_val)
    
    def remove_node(self, node):
        """
        删除物理节点
        
        Args:
            node: 物理节点标识
        """
        if node not in self.physical_nodes:
            return  # 节点不存在
        
        self.physical_nodes.remove(node)
        
        # 删除该节点的所有虚拟节点
        for i in range(self.virtual_nodes):
            virtual_node = f"{node}#{i}"
            hash_val = self._get_hash(virtual_node)
            if hash_val in self.ring:
                del self.ring[hash_val]
                # 从排序列表中删除
                index = bisect.bisect_left(self.sorted_keys, hash_val)
                if index < len(self.sorted_keys) and self.sorted_keys[index] == hash_val:
                    del self.sorted_keys[index]
    
    def get_node(self, key):
        """
        根据key查找对应的物理节点
        
        Args:
            key: 数据key
            
        Returns:
            物理节点标识
        """
        if not self.ring:
            return None
        
        hash_val = self._get_hash(key)
        
        # 在环上查找大于等于该hash的第一个节点
        index = bisect.bisect_left(self.sorted_keys, hash_val)
        
        # 如果没找到，则返回环的第一个节点（环形结构）
        if index == len(self.sorted_keys):
            index = 0
        
        return self.ring[self.sorted_keys[index]]
    
    def _get_hash(self, key):
        """
        哈希函数：使用MD5哈希然后取模
        
        Args:
            key: 输入字符串
            
        Returns:
            哈希值（0 ~ 2^32-1）
        """
        # 使用MD5哈希
        md5_hash = hashlib.md5(key.encode('utf-8')).hexdigest()
        # 取前8个字符（32位）作为哈希值
        hash_val = int(md5_hash[:8], 16)
        # 确保为正数
        return hash_val & 0x7FFFFFFF
    
    def print_ring(self):
        """获取环上节点分布情况（用于调试）"""
        print("一致性哈希环状态：")
        for hash_val in self.sorted_keys:
            print(f"位置 {hash_val} -> {self.ring[hash_val]}")
    
    def get_physical_node_count(self):
        """获取物理节点数量"""
        return len(self.physical_nodes)
    
    def get_virtual_node_count(self):
        """获取虚拟节点数量"""
        return len(self.ring)


def test_consistent_hashing():
    """
    测试函数
    """
    # 创建一致性哈希环，每个物理节点有3个虚拟节点
    ch = ConsistentHashing(3)
    
    # 添加物理节点
    ch.add_node("Server-A")
    ch.add_node("Server-B")
    ch.add_node("Server-C")
    
    # 测试数据分布
    test_keys = ["user1", "user2", "user3", "data1", "data2", "data3"]
    
    print("=== 初始节点分布测试 ===")
    for key in test_keys:
        node = ch.get_node(key)
        print(f"Key: {key} -> Node: {node}")
    
    # 测试节点删除
    print("\n=== 删除Server-B后测试 ===")
    ch.remove_node("Server-B")
    
    for key in test_keys:
        node = ch.get_node(key)
        print(f"Key: {key} -> Node: {node}")
    
    # 测试节点添加
    print("\n=== 添加Server-D后测试 ===")
    ch.add_node("Server-D")
    
    for key in test_keys:
        node = ch.get_node(key)
        print(f"Key: {key} -> Node: {node}")
    
    # 性能测试
    print("\n=== 性能测试 ===")
    start_time = time.time()
    
    for i in range(10000):
        ch.get_node(f"test{i}")
    
    end_time = time.time()
    print(f"10000次查找耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    # 打印环状态（调试用）
    ch.print_ring()
    
    print(f"物理节点数量: {ch.get_physical_node_count()}")
    print(f"虚拟节点数量: {ch.get_virtual_node_count()}")


if __name__ == "__main__":
    test_consistent_hashing()