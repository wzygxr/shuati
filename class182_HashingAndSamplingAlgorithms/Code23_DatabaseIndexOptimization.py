"""
数据库索引优化 - 完美哈希应用
题目链接：无特定链接，这是一个工程化应用场景

题目描述：
在数据库系统中，索引是提高查询性能的关键技术。对于静态数据集（如配置表、字典表），
可以使用完美哈希技术构建最优的索引结构，实现O(1)时间复杂度的查找操作。

算法核心思想：
1. 使用两级哈希结构：第一级哈希将记录分配到桶中，第二级为每个桶构建完美哈希
2. 第一级使用通用哈希函数，第二级为每个桶构建自定义哈希函数
3. 通过调整哈希参数确保每个桶内无冲突
4. 优化存储结构，减少内存占用

时间复杂度：O(1) 查找
空间复杂度：O(n)

工程化考量：
1. 静态数据集：完美哈希适用于数据不频繁变化的场景
2. 构建成本：构建过程较复杂，但查询效率极高
3. 内存优化：使用紧凑的数据结构存储哈希参数
4. 适用场景：适合配置表、字典表等静态数据的索引
"""

import random
import hashlib
from typing import List, Dict, Any, Optional

class Record:
    """数据库记录类"""
    
    def __init__(self, id: int, key: str, fields: Dict[str, Any]):
        self.id = id
        self.key = key
        self.fields = fields.copy()
    
    def get_id(self) -> int:
        return self.id
    
    def get_key(self) -> str:
        return self.key
    
    def get_field(self, field_name: str) -> Any:
        return self.fields.get(field_name)
    
    def get_field_names(self) -> set:
        return set(self.fields.keys())
    
    def __str__(self) -> str:
        return f"Record(id={self.id}, key='{self.key}', fields={self.fields})"
    
    def __repr__(self) -> str:
        return self.__str__()


class PerfectHashIndex:
    """完美哈希索引实现"""
    
    def __init__(self, records: List[Record]):
        """
        构造函数 - 为给定的静态数据集构建完美哈希索引
        :param records: 静态数据集（不能包含重复键）
        :raises ValueError: 如果数据集为空或包含重复键
        """
        if not records:
            raise ValueError("数据集不能为空")
        
        self.n = len(records)
        self.key_to_record = {}
        
        # 检查重复键
        key_set = set()
        for record in records:
            if record.get_key() in key_set:
                raise ValueError(f"数据集包含重复键: {record.get_key()}")
            key_set.add(record.get_key())
            self.key_to_record[record.get_key()] = record
        
        # 初始化第一级哈希参数
        self._initialize_first_level()
        
        # 构建完美哈希索引结构
        self._build_perfect_hash(records)
    
    def _initialize_first_level(self):
        """初始化第一级哈希参数"""
        random.seed(42)
        
        # 选择足够大的质数
        self.p1 = self._next_prime(self.n * self.n)
        self.m1 = self.n  # 第一级桶数量等于元素数量
        
        self.a1 = random.randint(1, self.p1 - 1)  # a ∈ [1, p-1]
        self.b1 = random.randint(0, self.p1 - 1)  # b ∈ [0, p-1]
    
    def _build_perfect_hash(self, records: List[Record]):
        """
        构建完美哈希索引结构
        :param records: 静态数据集
        """
        # 第一级哈希：将记录分配到桶中
        buckets = [[] for _ in range(self.m1)]
        
        # 分配记录到桶中
        for record in records:
            bucket_index = self._first_level_hash(record.get_key())
            buckets[bucket_index].append(record)
        
        # 初始化第二级结构
        self.a2 = [0] * self.m1
        self.b2 = [0] * self.m1
        self.p2 = [0] * self.m1
        self.m2 = [0] * self.m1
        self.second_level_tables: List[Optional[List[Optional[Record]]]] = [None] * self.m1
        
        random.seed(42)
        
        # 为每个桶构建第二级完美哈希
        for i in range(self.m1):
            bucket = buckets[i]
            bucket_size = len(bucket)
            
            if bucket_size == 0:
                # 空桶
                self.m2[i] = 0
                self.second_level_tables[i] = []
                continue
            
            # 计算第二级哈希表大小：桶大小的平方（确保无冲突）
            self.m2[i] = bucket_size * bucket_size
            
            # 选择质数
            self.p2[i] = self._next_prime(self.m2[i])
            
            collision_free = False
            attempts = 0
            
            # 尝试不同的哈希参数直到无冲突
            while not collision_free and attempts < 100:
                self.a2[i] = random.randint(1, self.p2[i] - 1)
                self.b2[i] = random.randint(0, self.p2[i] - 1)
                
                table: List[Optional[Record]] = [None] * self.m2[i]
                collision_free = True
                
                # 测试当前参数是否会产生冲突
                for record in bucket:
                    hash_val = self._second_level_hash(record.get_key(), i)
                    if table[hash_val] is not None:
                        # 发生冲突，重新选择参数
                        collision_free = False
                        break
                    table[hash_val] = record
                
                if collision_free:
                    # 无冲突，保存结果
                    self.second_level_tables[i] = table
                
                attempts += 1
            
            if not collision_free:
                raise RuntimeError(f"无法为桶 {i} 构建无冲突的完美哈希")
        
        print(f"完美哈希索引构建完成，数据集大小: {self.n}")
    
    def _first_level_hash(self, key: str) -> int:
        """
        第一级哈希函数
        :param key: 键
        :return: 桶索引
        """
        hash_val = 0
        for c in key:
            hash_val = (hash_val * 31 + ord(c)) % self.p1
        hash_val = (self.a1 * hash_val + self.b1) % self.p1
        return hash_val % self.m1
    
    def _second_level_hash(self, key: str, bucket_index: int) -> int:
        """
        第二级哈希函数
        :param key: 键
        :param bucket_index: 桶索引
        :return: 第二级哈希表中的位置
        """
        hash_val = 0
        for c in key:
            hash_val = (hash_val * 31 + ord(c)) % self.p2[bucket_index]
        hash_val = (self.a2[bucket_index] * hash_val + self.b2[bucket_index]) % self.p2[bucket_index]
        return hash_val % self.m2[bucket_index]
    
    def find_by_key(self, key: str) -> Optional[Record]:
        """
        根据键查找记录
        :param key: 要查找的键
        :return: 记录对象，如果不存在则返回None
        """
        if key is None:
            return None
        
        # 第一级哈希确定桶
        bucket_index = self._first_level_hash(key)
        
        # 检查桶是否为空
        if self.m2[bucket_index] == 0:
            return None
        
        # 第二级哈希定位记录
        position = self._second_level_hash(key, bucket_index)
        
        # 检查该位置是否存储了目标记录
        table = self.second_level_tables[bucket_index]
        if table is not None:
            record = table[position]
            if record is not None and key == record.get_key():
                return record
        
        return None
    
    def _next_prime(self, n: int) -> int:
        """
        查找下一个质数
        :param n: 起始数字
        :return: 大于等于n的最小质数
        """
        if n <= 2:
            return 2
        if n % 2 == 0:
            n += 1
        
        while not self._is_prime(n):
            n += 2
        return n
    
    def _is_prime(self, n: int) -> bool:
        """
        判断是否为质数
        :param n: 数字
        :return: True如果是质数
        """
        if n <= 1:
            return False
        if n <= 3:
            return True
        if n % 2 == 0 or n % 3 == 0:
            return False
        
        i = 5
        while i * i <= n:
            if n % i == 0 or n % (i + 2) == 0:
                return False
            i += 6
        return True
    
    def get_status(self) -> str:
        """
        获取索引的状态信息
        :return: 状态信息字符串
        """
        status = "完美哈希索引状态:\n"
        status += f"数据集大小: {self.n}\n"
        status += f"第一级桶数量: {self.m1}\n"
        status += f"第一级哈希参数: a1={self.a1}, b1={self.b1}, p1={self.p1}\n"
        
        # 统计桶分布
        empty_buckets = 0
        max_bucket_size = 0
        total_second_level_size = 0
        
        for i in range(self.m1):
            if self.m2[i] == 0:
                empty_buckets += 1
            else:
                bucket_size = int(self.m2[i] ** 0.5)
                max_bucket_size = max(max_bucket_size, bucket_size)
                total_second_level_size += self.m2[i]
        
        status += f"空桶数量: {empty_buckets}\n"
        status += f"最大桶大小: {max_bucket_size}\n"
        status += f"第二级总空间: {total_second_level_size}\n"
        status += f"空间利用率: {self.n / total_second_level_size * 100:.2f}%\n"
        
        return status


class PerformanceTest:
    """性能测试类"""
    
    @staticmethod
    def test_perfect_hash_index(index: PerfectHashIndex, test_keys: List[str]):
        """
        测试完美哈希索引的性能
        :param index: 完美哈希索引
        :param test_keys: 测试键列表
        """
        print("=== 完美哈希索引性能测试 ===")
        print(f"测试键数量: {len(test_keys)}")
        
        # 测试查找性能
        import time
        start_time = time.perf_counter()
        
        found = 0
        not_found = 0
        
        for key in test_keys:
            record = index.find_by_key(key)
            if record is not None:
                found += 1
            else:
                not_found += 1
        
        end_time = time.perf_counter()
        duration = (end_time - start_time) * 1e9  # 转换为纳秒
        
        print("测试结果:")
        print(f"找到记录: {found}")
        print(f"未找到记录: {not_found}")
        print(f"总查找时间: {duration:.0f} 纳秒")
        print(f"平均查找时间: {duration / len(test_keys):.2f} 纳秒/次")
        print(f"查找吞吐量: {len(test_keys) / (duration / 1e9):.2f} 次/秒")
    
    @staticmethod
    def compare_with_dict(records: List[Record], test_keys: List[str]):
        """
        与字典性能对比
        :param records: 记录列表
        :param test_keys: 测试键列表
        """
        print("\n=== 与字典性能对比 ===")
        
        # 构建字典索引
        dict_index = {}
        for record in records:
            dict_index[record.get_key()] = record
        
        # 测试字典查找性能
        import time
        start_time = time.perf_counter()
        
        found = 0
        not_found = 0
        
        for key in test_keys:
            record = dict_index.get(key)
            if record is not None:
                found += 1
            else:
                not_found += 1
        
        end_time = time.perf_counter()
        duration = (end_time - start_time) * 1e9  # 转换为纳秒
        
        print("字典测试结果:")
        print(f"找到记录: {found}")
        print(f"未找到记录: {not_found}")
        print(f"总查找时间: {duration:.0f} 纳秒")
        print(f"平均查找时间: {duration / len(test_keys):.2f} 纳秒/次")
        print(f"查找吞吐量: {len(test_keys) / (duration / 1e9):.2f} 次/秒")


def main():
    """主函数"""
    print("=== 数据库索引优化 - 完美哈希应用测试 ===\n")
    
    # 创建测试数据
    records = []
    
    # 创建国家信息记录
    china_fields = {
        "name": "中国",
        "capital": "北京",
        "population": 1400000000,
        "area": 9600000
    }
    records.append(Record(1, "CN", china_fields))
    
    usa_fields = {
        "name": "美国",
        "capital": "华盛顿",
        "population": 330000000,
        "area": 9800000
    }
    records.append(Record(2, "US", usa_fields))
    
    japan_fields = {
        "name": "日本",
        "capital": "东京",
        "population": 126000000,
        "area": 378000
    }
    records.append(Record(3, "JP", japan_fields))
    
    germany_fields = {
        "name": "德国",
        "capital": "柏林",
        "population": 83000000,
        "area": 357000
    }
    records.append(Record(4, "DE", germany_fields))
    
    france_fields = {
        "name": "法国",
        "capital": "巴黎",
        "population": 67000000,
        "area": 644000
    }
    records.append(Record(5, "FR", france_fields))
    
    uk_fields = {
        "name": "英国",
        "capital": "伦敦",
        "population": 67000000,
        "area": 242000
    }
    records.append(Record(6, "GB", uk_fields))
    
    canada_fields = {
        "name": "加拿大",
        "capital": "渥太华",
        "population": 38000000,
        "area": 10000000
    }
    records.append(Record(7, "CA", canada_fields))
    
    australia_fields = {
        "name": "澳大利亚",
        "capital": "堪培拉",
        "population": 25000000,
        "area": 7692000
    }
    records.append(Record(8, "AU", australia_fields))
    
    brazil_fields = {
        "name": "巴西",
        "capital": "巴西利亚",
        "population": 213000000,
        "area": 8515000
    }
    records.append(Record(9, "BR", brazil_fields))
    
    india_fields = {
        "name": "印度",
        "capital": "新德里",
        "population": 1380000000,
        "area": 3287000
    }
    records.append(Record(10, "IN", india_fields))
    
    # 构建完美哈希索引
    index = PerfectHashIndex(records)
    
    # 显示索引状态
    print(index.get_status())
    
    # 测试查找功能
    print("\n=== 查找功能测试 ===")
    test_keys = ["CN", "US", "JP", "XX", "YY"]
    for key in test_keys:
        record = index.find_by_key(key)
        if record is not None:
            print(f"找到记录: {record}")
        else:
            print(f"未找到键: {key}")
    
    # 性能测试
    test_key_list = ["CN"] * 1000  # 重复查找同一个键
    
    PerformanceTest.test_perfect_hash_index(index, test_key_list)
    PerformanceTest.compare_with_dict(records, test_key_list)
    
    print("\n=== 算法复杂度分析 ===")
    print("时间复杂度:")
    print("- 构建索引: O(n^2) 最坏情况，实际中通常为 O(n)")
    print("- 查找记录: O(1) 最坏情况")
    print("空间复杂度: O(n)")
    
    print("\n=== 工程化应用场景 ===")
    print("1. 配置表索引：系统配置、参数设置等静态数据")
    print("2. 字典表索引：国家、地区、货币等字典数据")
    print("3. 权限表索引：用户角色、权限映射等相对静态数据")
    print("4. 编译器符号表：变量、函数名等编译时确定的数据")
    
    print("\n=== 选择策略指南 ===")
    print("1. 数据静态性：仅适用于数据不频繁变化的场景")
    print("2. 查询频繁性：适用于查询远多于更新的场景")
    print("3. 内存敏感性：完美哈希通常比字典占用更少内存")
    print("4. 构建成本：构建过程较复杂，但查询性能极优")


if __name__ == "__main__":
    main()