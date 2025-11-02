# 一致性哈希算法实现 (Python版本)
# 题目来源: 分布式系统设计面试题
# 应用场景: 负载均衡、分布式缓存、分布式存储系统
# 题目描述: 实现一致性哈希算法，支持节点的动态增删和虚拟节点技术
# 
# 解题思路:
# 1. 使用哈希环存储节点和虚拟节点
# 2. 使用虚拟节点技术解决数据分布不均问题
# 3. 支持节点的动态添加和删除
# 4. 实现高效的数据查找和节点定位
# 
# 时间复杂度分析:
# - 添加节点: O(k)，其中k是虚拟节点数量
# - 删除节点: O(k)
# - 查找节点: O(log n)，其中n是节点总数
# 
# 空间复杂度: O(n * k)，其中n是物理节点数，k是每个节点的虚拟节点数
# 
# 工程化考量:
# 1. 异常处理: 验证节点和数据的有效性
# 2. 性能优化: 使用TreeMap实现高效的区间查找
# 3. 负载均衡: 虚拟节点技术确保数据均匀分布
# 4. 容错性: 支持节点的动态增删，最小化数据迁移

import hashlib
import bisect
import random
from typing import List, Dict, Set

class ConsistentHashing:
    def __init__(self, virtual_node_count: int = 3):
        """
        构造函数
        :param virtual_node_count: 每个物理节点的虚拟节点数量
        :raises ValueError: 如果虚拟节点数量小于等于0
        """
        if virtual_node_count <= 0:
            raise ValueError("虚拟节点数量必须大于0")
        
        self.virtual_node_count = virtual_node_count
        
        # 哈希环，存储虚拟节点到物理节点的映射
        self.hash_ring: Dict[int, str] = {}
        
        # 物理节点集合
        self.physical_nodes: Set[str] = set()
        
        # 排序的哈希值列表，用于快速查找
        self.sorted_hashes: List[int] = []
    
    def _hash(self, key: str) -> int:
        """
        哈希函数 - 使用MD5算法
        :param key: 输入字符串
        :return: 哈希值
        """
        # 使用MD5哈希，然后取前4个字节作为整数
        hash_obj = hashlib.md5(key.encode('utf-8'))
        hash_bytes = hash_obj.digest()
        
        # 将前4个字节转换为整数
        hash_int = int.from_bytes(hash_bytes[:4], byteorder='big')
        
        # 确保哈希值为正数
        return hash_int & 0x7fffffff
    
    def add_node(self, node: str):
        """
        添加物理节点
        :param node: 物理节点名称
        :raises ValueError: 如果节点名为空或已存在
        """
        if not node:
            raise ValueError("节点名不能为空")
        if node in self.physical_nodes:
            raise ValueError(f"节点 {node} 已存在")
        
        self.physical_nodes.add(node)
        
        # 为物理节点创建虚拟节点
        for i in range(self.virtual_node_count):
            virtual_node = f"{node}#{i}"
            node_hash = self._hash(virtual_node)
            
            # 添加到哈希环
            self.hash_ring[node_hash] = node
            
            # 维护排序的哈希值列表
            bisect.insort(self.sorted_hashes, node_hash)
        
        print(f"添加节点: {node}，虚拟节点数: {self.virtual_node_count}")
    
    def remove_node(self, node: str):
        """
        删除物理节点
        :param node: 物理节点名称
        :raises ValueError: 如果节点不存在
        """
        if node not in self.physical_nodes:
            raise ValueError(f"节点 {node} 不存在")
        
        self.physical_nodes.remove(node)
        
        # 删除该物理节点的所有虚拟节点
        hashes_to_remove = []
        for node_hash, physical_node in self.hash_ring.items():
            if physical_node == node:
                hashes_to_remove.append(node_hash)
        
        for node_hash in hashes_to_remove:
            del self.hash_ring[node_hash]
            # 从排序列表中删除
            index = bisect.bisect_left(self.sorted_hashes, node_hash)
            if index < len(self.sorted_hashes) and self.sorted_hashes[index] == node_hash:
                del self.sorted_hashes[index]
        
        print(f"删除节点: {node}")
    
    def get_node(self, key: str) -> str:
        """
        根据键查找对应的物理节点
        :param key: 数据键
        :return: 负责该键的物理节点
        :raises ValueError: 如果键为空或哈希环为空
        """
        if not key:
            raise ValueError("键不能为空")
        if not self.hash_ring:
            raise ValueError("哈希环为空，请先添加节点")
        
        key_hash = self._hash(key)
        
        # 在哈希环上顺时针查找第一个大于等于该哈希值的节点
        index = bisect.bisect_left(self.sorted_hashes, key_hash)
        
        # 如果没找到，则返回环上的第一个节点（环状结构）
        if index == len(self.sorted_hashes):
            index = 0
        
        node_hash = self.sorted_hashes[index]
        return self.hash_ring[node_hash]
    
    def get_status(self) -> str:
        """
        获取哈希环的状态信息
        :return: 哈希环状态字符串
        """
        result = []
        result.append("一致性哈希环状态:")
        result.append(f"物理节点数: {len(self.physical_nodes)}")
        result.append(f"虚拟节点数: {len(self.hash_ring)}")
        
        # 物理节点列表
        result.append(f"物理节点列表: {', '.join(sorted(self.physical_nodes))}")
        
        # 统计每个物理节点的虚拟节点分布
        node_distribution: Dict[str, int] = {}
        for physical_node in self.hash_ring.values():
            node_distribution[physical_node] = node_distribution.get(physical_node, 0) + 1
        
        distribution_str = ", ".join([f"{node}:{count}" for node, count in node_distribution.items()])
        result.append(f"虚拟节点分布: {distribution_str}")
        
        return "\n".join(result)
    
    def load_balance_test(self, data_count: int):
        """
        负载均衡测试
        模拟大量数据分布，检查负载均衡性
        :param data_count: 测试数据数量
        """
        if not self.physical_nodes:
            print("请先添加节点再进行负载均衡测试")
            return
        
        distribution: Dict[str, int] = {}
        
        # 初始化分布统计
        for node in self.physical_nodes:
            distribution[node] = 0
        
        # 模拟数据分布
        for i in range(data_count):
            key = f"key{random.randint(0, 1000000)}"
            node = self.get_node(key)
            distribution[node] += 1
        
        # 计算负载均衡指标
        total = data_count
        average = total / len(self.physical_nodes)
        variance = 0.0
        
        print(f"负载均衡测试结果 (数据量: {data_count}):")
        for node, count in distribution.items():
            deviation = abs(count - average)
            variance += deviation * deviation
            percentage = (count / total) * 100
            print(f"节点 {node}: {count} 数据 ({percentage:.2f}%)")
        
        std_dev = (variance / len(self.physical_nodes)) ** 0.5
        relative_std_dev = (std_dev / average) * 100
        print(f"标准差: {std_dev:.2f}, 相对标准差: {relative_std_dev:.2f}%")
    
    def get_physical_node_count(self) -> int:
        """
        获取物理节点数量
        :return: 物理节点数量
        """
        return len(self.physical_nodes)
    
    def get_virtual_node_count(self) -> int:
        """
        获取虚拟节点数量
        :return: 虚拟节点数量
        """
        return len(self.hash_ring)

def test_consistent_hashing():
    """
    单元测试函数
    """
    print("=== 一致性哈希算法单元测试 ===")
    
    # 测试1: 基本功能测试
    print("测试1: 基本功能测试")
    ch = ConsistentHashing(3)
    
    # 添加节点
    ch.add_node("Node-A")
    ch.add_node("Node-B")
    ch.add_node("Node-C")
    
    print(ch.get_status())
    
    # 测试数据分布
    test_distribution: Dict[str, int] = {}
    test_keys = ["user1", "user2", "user3", "data1", "data2", "file1"]
    
    for key in test_keys:
        node = ch.get_node(key)
        test_distribution[node] = test_distribution.get(node, 0) + 1
        print(f"键 '{key}' 分配到节点: {node}")
    
    print(f"测试数据分布: {test_distribution}")
    
    # 测试2: 负载均衡测试
    print("\n测试2: 负载均衡测试")
    ch.load_balance_test(1000)
    
    # 测试3: 节点删除测试
    print("\n测试3: 节点删除测试")
    ch.remove_node("Node-B")
    print(ch.get_status())
    
    # 重新测试数据分布
    test_distribution.clear()
    for key in test_keys:
        node = ch.get_node(key)
        test_distribution[node] = test_distribution.get(node, 0) + 1
        print(f"键 '{key}' 重新分配到节点: {node}")
    
    print(f"删除节点后数据分布: {test_distribution}")
    
    # 测试4: 异常处理测试
    print("\n测试4: 异常处理测试")
    try:
        ch.add_node("")  # 空节点名
        print("异常处理测试失败")
    except ValueError as e:
        print(f"异常处理测试通过: {e}")
    
    print("=== 单元测试完成 ===")

def performance_test():
    """
    性能测试函数
    """
    import time
    
    print("=== 一致性哈希算法性能测试 ===")
    
    ch = ConsistentHashing(5)
    
    # 添加多个节点
    for i in range(1, 11):
        ch.add_node(f"Server-{i}")
    
    # 测试查找性能
    test_count = 100000
    
    start_time = time.time()
    
    for i in range(test_count):
        key = f"key{random.randint(0, 1000000)}"
        ch.get_node(key)
    
    end_time = time.time()
    duration = (end_time - start_time) * 1000  # 转换为毫秒
    
    print("性能测试结果:")
    print(f"节点数量: {ch.get_physical_node_count()}")
    print(f"虚拟节点数量: {ch.get_virtual_node_count()}")
    print(f"查找次数: {test_count}")
    print(f"总查找时间: {duration:.2f} 毫秒")
    print(f"平均查找时间: {duration / test_count:.4f} 毫秒/次")
    print(f"查找吞吐量: {test_count / (duration / 1000):.2f} 次/秒")
    
    print("=== 性能测试完成 ===")

if __name__ == "__main__":
    # 运行单元测试
    test_consistent_hashing()
    
    # 运行性能测试
    performance_test()
    
    # 演示示例
    print("=== 一致性哈希算法演示 ===")
    
    demo = ConsistentHashing(5)
    
    # 添加节点
    demo.add_node("Server-1")
    demo.add_node("Server-2")
    demo.add_node("Server-3")
    
    print(demo.get_status())
    
    # 演示数据分布
    demo_keys = ["user:1001", "order:2001", "product:3001", "cache:4001"]
    for key in demo_keys:
        print(f"数据 '{key}' 分配到: {demo.get_node(key)}")
    
    # 负载均衡测试
    demo.load_balance_test(10000)