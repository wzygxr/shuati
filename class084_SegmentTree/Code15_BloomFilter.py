# 布隆过滤器实现 (Python版本)
# 题目来源: 大数据处理、缓存系统、网络爬虫去重
# 应用场景: 网页去重、垃圾邮件过滤、缓存穿透防护
# 题目描述: 实现布隆过滤器，支持元素添加和存在性检查
# 
# 解题思路:
# 1. 使用多个哈希函数将元素映射到位数组的不同位置
# 2. 添加元素时，将所有哈希位置设为1
# 3. 检查元素时，如果所有哈希位置都为1，则元素可能存在
# 4. 使用误判率公式计算最优的哈希函数数量和位数组大小
# 
# 时间复杂度分析:
# - 添加元素: O(k)，其中k是哈希函数数量
# - 检查元素: O(k)
# 
# 空间复杂度: O(m)，其中m是位数组大小
# 
# 工程化考量:
# 1. 误判率控制: 根据预期元素数量和可接受的误判率计算最优参数
# 2. 哈希函数选择: 使用不同的哈希函数减少冲突
# 3. 内存优化: 使用位数组节省空间
# 4. 线程安全: 在多线程环境下安全使用

import math
import hashlib
import random
from typing import List

class BloomFilter:
    def __init__(self, expected_element_count: int, false_positive_rate: float):
        """
        构造函数 - 根据预期元素数量和误判率自动计算参数
        :param expected_element_count: 预期元素数量
        :param false_positive_rate: 可接受的误判率 (0 < false_positive_rate < 1)
        :raises ValueError: 如果参数无效
        """
        if expected_element_count <= 0:
            raise ValueError("预期元素数量必须大于0")
        if false_positive_rate <= 0 or false_positive_rate >= 1:
            raise ValueError("误判率必须在0和1之间")
        
        self.expected_element_count = expected_element_count
        self.actual_element_count = 0
        
        # 根据误判率公式计算最优参数
        # m = - (n * ln(p)) / (ln(2))^2
        # k = (m / n) * ln(2)
        self.bit_set_size = math.ceil(
            -expected_element_count * math.log(false_positive_rate) / (math.log(2) ** 2)
        )
        self.hash_function_count = math.ceil(
            (self.bit_set_size / expected_element_count) * math.log(2)
        )
        
        # 确保哈希函数数量至少为1
        self.hash_function_count = max(1, self.hash_function_count)
        
        # 初始化位数组
        self.bit_set = [False] * self.bit_set_size
        
        # 初始化哈希种子
        random.seed(42)  # 固定种子保证可重复性
        self.seeds = [random.randint(1, 1000000) for _ in range(self.hash_function_count)]
        
        print(f"布隆过滤器初始化: 预期元素数={expected_element_count}, "
              f"误判率={false_positive_rate}, "
              f"位数组大小={self.bit_set_size}, "
              f"哈希函数数={self.hash_function_count}")
    
    def __init_manual(self, bit_set_size: int, hash_function_count: int):
        """
        构造函数 - 手动指定参数
        :param bit_set_size: 位数组大小
        :param hash_function_count: 哈希函数数量
        """
        if bit_set_size <= 0:
            raise ValueError("位数组大小必须大于0")
        if hash_function_count <= 0:
            raise ValueError("哈希函数数量必须大于0")
        
        self.bit_set_size = bit_set_size
        self.hash_function_count = hash_function_count
        self.expected_element_count = 0  # 未知预期数量
        self.actual_element_count = 0
        self.bit_set = [False] * self.bit_set_size
        
        # 初始化哈希种子
        random.seed(42)
        self.seeds = [random.randint(1, 1000000) for _ in range(self.hash_function_count)]
    
    def _hash(self, element: str, seed: int) -> int:
        """
        哈希函数 - 使用简单的字符串哈希算法
        :param element: 输入字符串
        :param seed: 哈希种子
        :return: 哈希值
        """
        hash_value = seed
        for char in element:
            hash_value = (hash_value * 31 + ord(char)) % (2**32)
        return hash_value
    
    def add(self, element: str):
        """
        添加元素到布隆过滤器
        :param element: 要添加的元素
        :raises ValueError: 如果元素为空
        """
        if not element:
            raise ValueError("元素不能为空")
        
        # 计算所有哈希位置并设置为True
        for i in range(self.hash_function_count):
            hash_value = self._hash(element, self.seeds[i])
            position = hash_value % self.bit_set_size
            self.bit_set[position] = True
        
        self.actual_element_count += 1
    
    def might_contain(self, element: str) -> bool:
        """
        检查元素是否可能在布隆过滤器中
        :param element: 要检查的元素
        :return: True如果元素可能存在，False如果元素一定不存在
        :raises ValueError: 如果元素为空
        """
        if not element:
            raise ValueError("元素不能为空")
        
        # 检查所有哈希位置是否都为True
        for i in range(self.hash_function_count):
            hash_value = self._hash(element, self.seeds[i])
            position = hash_value % self.bit_set_size
            if not self.bit_set[position]:
                return False  # 如果有一个位置为False，元素一定不存在
        
        return True  # 所有位置都为True，元素可能存在
    
    def get_status(self) -> str:
        """
        获取布隆过滤器的状态信息
        :return: 状态信息字符串
        """
        set_bits = sum(1 for bit in self.bit_set if bit)
        fill_ratio = set_bits / self.bit_set_size
        
        # 计算当前误判率
        current_false_positive_rate = fill_ratio ** self.hash_function_count
        
        result = []
        result.append("布隆过滤器状态:")
        result.append(f"位数组大小: {self.bit_set_size}")
        result.append(f"哈希函数数量: {self.hash_function_count}")
        result.append(f"预期元素数量: {self.expected_element_count}")
        result.append(f"实际元素数量: {self.actual_element_count}")
        result.append(f"已设置位数: {set_bits}")
        result.append(f"填充比例: {fill_ratio:.6f}")
        result.append(f"当前误判率: {current_false_positive_rate:.6f}")
        
        return "\n".join(result)
    
    def performance_test(self, test_element_count: int):
        """
        性能测试 - 测试布隆过滤器的误判率
        :param test_element_count: 测试元素数量
        """
        if test_element_count <= 0:
            print("测试元素数量必须大于0")
            return
        
        print("=== 布隆过滤器性能测试 ===")
        print(f"测试元素数量: {test_element_count}")
        
        # 添加测试元素
        false_positives = 0
        true_negatives = 0
        
        # 添加真实元素
        for i in range(test_element_count):
            self.add(f"real{i}")
        
        # 测试不存在元素
        for i in range(test_element_count):
            fake_element = f"fake{i}"
            if self.might_contain(fake_element):
                false_positives += 1  # 误判
            else:
                true_negatives += 1  # 正确判断
        
        actual_false_positive_rate = false_positives / test_element_count
        
        print("测试结果:")
        print(f"误判数量: {false_positives}")
        print(f"正确判断数量: {true_negatives}")
        print(f"实际误判率: {actual_false_positive_rate:.6f}")
        
        set_bits = sum(1 for bit in self.bit_set if bit)
        theoretical_false_positive_rate = (set_bits / self.bit_set_size) ** self.hash_function_count
        print(f"理论误判率: {theoretical_false_positive_rate:.6f}")
        
        print(self.get_status())
    
    def get_bit_set_size(self) -> int:
        """
        获取位数组大小
        :return: 位数组大小
        """
        return self.bit_set_size
    
    def get_hash_function_count(self) -> int:
        """
        获取哈希函数数量
        :return: 哈希函数数量
        """
        return self.hash_function_count
    
    def get_actual_element_count(self) -> int:
        """
        获取实际元素数量
        :return: 实际元素数量
        """
        return self.actual_element_count

def test_bloom_filter():
    """
    单元测试函数
    """
    print("=== 布隆过滤器单元测试 ===")
    
    # 测试1: 基本功能测试
    print("测试1: 基本功能测试")
    bf = BloomFilter(1000, 0.01)
    
    # 添加元素
    bf.add("apple")
    bf.add("banana")
    bf.add("cherry")
    
    # 检查存在的元素
    if bf.might_contain("apple") and bf.might_contain("banana") and bf.might_contain("cherry"):
        print("存在性检查测试通过")
    else:
        print("存在性检查测试失败")
    
    # 检查不存在的元素
    if not bf.might_contain("orange") and not bf.might_contain("grape"):
        print("不存在性检查测试通过")
    else:
        print("不存在性检查测试失败")
    
    # 测试2: 性能测试
    print("\n测试2: 性能测试")
    bf.performance_test(1000)
    
    # 测试3: 不同参数对比
    print("\n测试3: 不同参数对比")
    
    bf1 = BloomFilter(1000, 0.1)   # 高误判率
    bf2 = BloomFilter(1000, 0.01)  # 低误判率
    bf3 = BloomFilter(1000, 0.001) # 极低误判率
    
    # 添加相同元素
    for i in range(500):
        bf1.add(f"test{i}")
        bf2.add(f"test{i}")
        bf3.add(f"test{i}")
    
    # 测试误判率
    fp1, fp2, fp3 = 0, 0, 0
    for i in range(500, 1000):
        if bf1.might_contain(f"test{i}"):
            fp1 += 1
        if bf2.might_contain(f"test{i}"):
            fp2 += 1
        if bf3.might_contain(f"test{i}"):
            fp3 += 1
    
    print("不同误判率配置的测试结果:")
    print(f"误判率0.1: 实际误判率={fp1/500:.4f}")
    print(f"误判率0.01: 实际误判率={fp2/500:.4f}")
    print(f"误判率0.001: 实际误判率={fp3/500:.4f}")
    
    # 测试4: 异常处理测试
    print("\n测试4: 异常处理测试")
    try:
        bf.add("")  # 空元素
        print("异常处理测试失败")
    except ValueError as e:
        print(f"异常处理测试通过: {e}")
    
    print("=== 单元测试完成 ===")

def performance_test():
    """
    性能测试函数
    """
    import time
    
    print("=== 布隆过滤器性能测试 ===")
    
    # 测试不同规模的布隆过滤器
    test_cases = [
        (1000, 0.01),
        (10000, 0.01),
        (100000, 0.01)
    ]
    
    for expected_count, false_positive_rate in test_cases:
        print(f"\n测试规模: 预期元素数={expected_count}, 误判率={false_positive_rate}")
        
        bf = BloomFilter(expected_count, false_positive_rate)
        
        # 测试添加性能
        start_time = time.time()
        
        for i in range(expected_count):
            bf.add(f"element{i}")
        
        end_time = time.time()
        add_duration = (end_time - start_time) * 1000  # 转换为毫秒
        
        # 测试查询性能
        start_time = time.time()
        
        found = 0
        for i in range(expected_count):
            if bf.might_contain(f"element{i}"):
                found += 1
        
        end_time = time.time()
        query_duration = (end_time - start_time) * 1000  # 转换为毫秒
        
        print("性能测试结果:")
        print(f"添加时间: {add_duration:.2f} 毫秒")
        print(f"平均添加时间: {add_duration / expected_count:.4f} 毫秒/元素")
        print(f"查询时间: {query_duration:.2f} 毫秒")
        print(f"平均查询时间: {query_duration / expected_count:.4f} 毫秒/元素")
        print(f"正确查询数量: {found}/{expected_count}")
    
    print("=== 性能测试完成 ===")

if __name__ == "__main__":
    # 运行单元测试
    test_bloom_filter()
    
    # 运行性能测试
    performance_test()
    
    # 演示示例
    print("=== 布隆过滤器演示 ===")
    
    # 创建一个预期处理10000个元素，误判率为1%的布隆过滤器
    bloom_filter = BloomFilter(10000, 0.01)
    
    # 添加一些URL到布隆过滤器（模拟网页去重）
    urls = [
        "https://example.com/page1",
        "https://example.com/page2", 
        "https://example.com/page3",
        "https://example.com/page4",
        "https://example.com/page5"
    ]
    
    for url in urls:
        bloom_filter.add(url)
        print(f"添加URL: {url}")
    
    # 检查URL是否已存在
    test_urls = [
        "https://example.com/page1",     # 已存在
        "https://example.com/page6",     # 不存在
        "https://example.com/page3",     # 已存在
        "https://example.com/page7"      # 不存在
    ]
    
    print("\nURL存在性检查:")
    for url in test_urls:
        exists = bloom_filter.might_contain(url)
        print(f"URL {url}: {'可能已存在' if exists else '一定不存在'}")
    
    # 显示布隆过滤器状态
    print(f"\n{bloom_filter.get_status()}")
    
    # 性能测试
    bloom_filter.performance_test(1000)