# LeetCode 677. 键值映射 - Python实现
# 
# 题目描述：
# 实现一个 MapSum 类，支持 insert 和 sum 操作。
# 
# 测试链接：https://leetcode.cn/problems/map-sum-pairs/
# 
# 算法思路：
# 1. 使用前缀树存储键值对，每个节点存储经过该节点的所有键的值之和
# 2. 插入操作：更新键对应的值，并更新路径上所有节点的和
# 3. 求和操作：查找前缀对应的节点，返回该节点的和值
# 
# 时间复杂度分析：
# - 插入操作：O(L)，其中L是键的长度
# - 求和操作：O(L)，其中L是前缀的长度
# 
# 空间复杂度分析：
# - 前缀树空间：O(N*L)，其中N是键的数量，L是平均键长度
# - 总体空间复杂度：O(N*L)
# 
# 是否最优解：是
# 理由：使用前缀树可以高效处理前缀相关的键值操作
# 
# 工程化考虑：
# 1. 异常处理：处理空键和非法值
# 2. 边界情况：键为空或前缀为空的情况
# 3. 极端输入：大量键或长键的情况
# 4. 内存管理：合理管理前缀树内存
# 
# 语言特性差异：
# Python：使用字典实现前缀树，代码简洁灵活
# Java：使用数组实现，性能较高但空间固定
# C++：可使用指针实现，更节省空间
# 
# 调试技巧：
# 1. 验证插入和求和操作的正确性
# 2. 测试重复插入相同键的情况
# 3. 单元测试覆盖各种边界条件

class TrieNode:
    """
    前缀树节点类
    
    算法思路：
    使用字典存储子节点，支持任意字符集
    包含经过该节点的所有键的值之和
    
    时间复杂度分析：
    - 初始化：O(1)
    - 空间复杂度：O(1) 每个节点
    """
    def __init__(self):
        self.children = {}  # 字符 -> TrieNode
        self.value = 0      # 经过该节点的所有键的值之和

class MapSum:
    """
    MapSum 类实现
    
    算法思路：
    使用前缀树存储键值对，支持高效的插入和前缀求和操作
    
    时间复杂度分析：
    - 插入：O(L)，L为键的长度
    - 求和：O(L)，L为前缀的长度
    
    空间复杂度分析：
    - 总体：O(N*L)，N为键数，L为平均长度
    """
    
    def __init__(self):
        """
        初始化 MapSum
        
        时间复杂度：O(1)
        空间复杂度：O(1)
        """
        self.root = TrieNode()
        self.key_value_map = {}  # 存储键的原始值，用于更新操作
    
    def insert(self, key: str, val: int) -> None:
        """
        插入键值对
        
        算法步骤：
        1. 计算值的差异（新值 - 旧值）
        2. 更新键值映射
        3. 更新前缀树路径上所有节点的和值
        
        时间复杂度：O(L)，其中L是键的长度
        空间复杂度：O(L)，最坏情况下需要创建新节点
        
        :param key: 键
        :param val: 值
        :raises ValueError: 如果key为None
        """
        if key is None:
            raise ValueError("键不能为None")
        
        # 空键不应该被插入，或者应该被特殊处理
        if len(key) == 0:
            return  # 忽略空键
        
        # 计算值的差异
        delta = val - self.key_value_map.get(key, 0)
        
        # 更新键值映射
        self.key_value_map[key] = val
        
        # 更新前缀树
        node = self.root
        node.value += delta  # 更新根节点值
        
        for char in key:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
            node.value += delta
    
    def sum(self, prefix: str) -> int:
        """
        计算以指定前缀开头的所有键的值之和
        
        算法步骤：
        1. 遍历前缀，找到对应的节点
        2. 返回该节点的和值
        
        时间复杂度：O(L)，其中L是前缀的长度
        空间复杂度：O(1)
        
        :param prefix: 前缀
        :return: 所有以prefix开头的键的值之和
        """
        if prefix is None:
            return 0
        
        # 空前缀应该返回所有键的值之和
        if len(prefix) == 0:
            return self.root.value
        
        node = self.root
        for char in prefix:
            if char not in node.children:
                return 0
            node = node.children[char]
        
        return node.value

class MapSumOptimized:
    """
    优化版本：使用固定字符集，性能更高
    
    算法思路：
    假设只处理小写字母，使用固定大小的子节点数组
    适用于字符集固定的场景
    
    时间复杂度分析：
    - 插入：O(L)
    - 求和：O(L)
    
    空间复杂度分析：
    - 总体：O(N*L)
    """
    
    def __init__(self):
        self.root = OptimizedTrieNode()
        self.key_value_map = {}
    
    def insert(self, key: str, val: int) -> None:
        if key is None:
            return
        
        delta = val - self.key_value_map.get(key, 0)
        self.key_value_map[key] = val
        
        node = self.root
        for char in key:
            index = ord(char) - ord('a')
            if index < 0 or index >= 26:
                continue  # 跳过非小写字母
            
            if node.children[index] is None:
                node.children[index] = OptimizedTrieNode()
            node = node.children[index]
            node.value += delta
    
    def sum(self, prefix: str) -> int:
        if prefix is None:
            return 0
        
        node = self.root
        for char in prefix:
            index = ord(char) - ord('a')
            if index < 0 or index >= 26:
                return 0
            if node.children[index] is None:
                return 0
            node = node.children[index]
        
        return node.value

class OptimizedTrieNode:
    """
    优化版本的前缀树节点类
    
    使用固定大小的数组存储子节点
    适用于小写字母字符集
    """
    def __init__(self):
        self.children = [None] * 26  # 26个小写字母
        self.value = 0

def test_map_sum():
    """
    单元测试函数
    
    测试用例设计：
    1. 基础功能测试
    2. 更新操作测试
    3. 边界情况测试
    4. 性能对比测试
    """
    # 测试用例1：基础功能测试
    map_sum = MapSum()
    map_sum.insert("apple", 3)
    assert map_sum.sum("ap") == 3, "基础插入测试失败"
    
    map_sum.insert("app", 2)
    assert map_sum.sum("ap") == 5, "多个键测试失败"
    
    # 测试用例2：更新操作测试
    map_sum.insert("apple", 5)
    assert map_sum.sum("ap") == 7, "更新操作测试失败"
    
    # 测试用例3：前缀不存在测试
    assert map_sum.sum("banana") == 0, "前缀不存在测试失败"
    
    # 测试用例4：空键和空前缀测试
    map_sum.insert("", 10)  # 空键应该被忽略
    assert map_sum.sum("") == 7, "空键处理测试失败"  # 空键不应该影响结果
    
    # 测试用例5：优化版本测试
    optimized = MapSumOptimized()
    optimized.insert("test", 100)
    assert optimized.sum("te") == 100, "优化版本测试失败"
    
    # 测试用例6：特殊字符处理
    map_sum.insert("test123", 50)  # 包含数字
    map_sum.insert("TEST", 30)     # 大写字母
    # 这些插入应该被正确处理
    
    print("所有单元测试通过！")

def performance_test():
    """
    性能测试函数
    
    测试大规模数据下的性能表现：
    1. 大量键值对插入
    2. 频繁的前缀求和操作
    3. 优化版本对比
    """
    import time
    import random
    import string
    
    # 生成测试数据
    n = 10000
    keys = []
    values = []
    
    for i in range(n):
        # 生成随机键（长度5-10）
        key_length = random.randint(5, 10)
        key = ''.join(random.choices(string.ascii_lowercase, k=key_length))
        keys.append(key)
        values.append(random.randint(1, 1000))
    
    # 标准版本性能测试
    map_sum = MapSum()
    
    start_time = time.time()
    for i in range(n):
        map_sum.insert(keys[i], values[i])
    insert_time = time.time() - start_time
    
    start_time = time.time()
    for i in range(n):
        map_sum.sum(keys[i][:3])  # 使用前3个字符作为前缀
    sum_time = time.time() - start_time
    
    # 优化版本性能测试
    optimized = MapSumOptimized()
    
    start_time = time.time()
    for i in range(n):
        optimized.insert(keys[i], values[i])
    optimized_insert_time = time.time() - start_time
    
    start_time = time.time()
    for i in range(n):
        optimized.sum(keys[i][:3])
    optimized_sum_time = time.time() - start_time
    
    print(f"标准版本 - 插入{n}个键耗时: {insert_time:.3f}秒")
    print(f"标准版本 - 求和{n}次耗时: {sum_time:.3f}秒")
    print(f"优化版本 - 插入{n}个键耗时: {optimized_insert_time:.3f}秒")
    print(f"优化版本 - 求和{n}次耗时: {optimized_sum_time:.3f}秒")
    
    # 性能对比
    if optimized_insert_time > 0:
        insert_speedup = insert_time / optimized_insert_time
        print(f"插入操作性能提升: {insert_speedup:.1f}倍")
    
    if optimized_sum_time > 0:
        sum_speedup = sum_time / optimized_sum_time
        print(f"求和操作性能提升: {sum_speedup:.1f}倍")

def edge_case_test():
    """
    边界情况测试函数
    
    测试各种边界条件：
    1. 空键和空前缀
    2. 特殊字符处理
    3. 极端数值
    4. 重复操作
    """
    map_sum = MapSum()
    
    # 测试空键
    map_sum.insert("", 100)
    assert map_sum.sum("") == 0, "空键测试失败"
    
    # 测试空前缀
    map_sum.insert("test", 50)
    assert map_sum.sum("") == 50, "空前缀测试失败"
    
    # 测试特殊字符
    map_sum.insert("test-key", 25)  # 包含连字符
    map_sum.insert("test key", 30)  # 包含空格
    # 这些插入应该被正确处理
    
    # 测试极端数值
    map_sum.insert("large", 10**9)
    map_sum.insert("negative", -100)
    assert map_sum.sum("large") == 10**9, "大数值测试失败"
    assert map_sum.sum("negative") == -100, "负数值测试失败"
    
    # 测试重复插入
    map_sum.insert("repeat", 10)
    map_sum.insert("repeat", 20)
    map_sum.insert("repeat", 30)
    assert map_sum.sum("repeat") == 30, "重复插入测试失败"
    
    print("边界情况测试通过！")

if __name__ == "__main__":
    # 运行单元测试
    test_map_sum()
    
    # 运行边界情况测试
    edge_case_test()
    
    # 运行性能测试
    performance_test()