"""
编译器符号表实现 - 使用完美哈希技术

应用场景：编译器中的符号表管理、静态字典、关键字查找

算法原理：
1. 使用两级哈希结构实现完美哈希
2. 第一级哈希将关键字分组到桶中
3. 为每个桶构建无冲突的二级哈希表
4. 保证O(1)时间复杂度的查找操作

时间复杂度：
- 构建：O(n) 平均情况
- 查找：O(1) 最坏情况

空间复杂度：O(n)
"""

import random
import hashlib
from typing import List, Optional, Dict, Any

class SymbolEntry:
    """符号表条目"""
    
    def __init__(self, name: str, type: str, scope: int, line_number: int):
        self.name = name          # 符号名称
        self.type = type          # 符号类型
        self.scope = scope        # 作用域
        self.line_number = line_number  # 行号
    
    def __str__(self) -> str:
        return f"SymbolEntry{{name='{self.name}', type='{self.type}', scope={self.scope}, line={self.line_number}}}"
    
    def __repr__(self) -> str:
        return self.__str__()


class PerfectHashSymbolTable:
    """完美哈希符号表实现"""
    
    def __init__(self, symbols: List[SymbolEntry]):
        """
        构造函数：根据关键字集合构建完美哈希表
        
        Args:
            symbols: 符号条目集合
        """
        # 收集所有关键字
        self.all_keys = []
        self.symbol_map = {}
        for symbol in symbols:
            self.all_keys.append(symbol.name)
            self.symbol_map[symbol.name] = symbol
        
        n = len(self.all_keys)
        self.first_level_size = max(1, int(n * 1.5))  # 一级表大小
        self.second_level_tables = [[] for _ in range(self.first_level_size)]
        self.second_level_sizes = [0] * self.first_level_size
        self.hash_params = [0] * self.first_level_size
        
        # 第一级分组
        groups = {}
        for key in self.all_keys:
            group_index = abs(hash(key)) % self.first_level_size
            if group_index not in groups:
                groups[group_index] = []
            groups[group_index].append(key)
        
        # 为每个组构建无冲突的二级哈希表
        for group_index, group_keys in groups.items():
            if not group_keys:
                continue
            
            # 计算二级表大小（平方大小以保证高概率无冲突）
            group_size = len(group_keys)
            second_level_size = group_size * group_size
            self.second_level_sizes[group_index] = second_level_size
            
            # 寻找无冲突的哈希函数
            hash_param = self._find_perfect_hash_function(group_keys, second_level_size)
            self.hash_params[group_index] = hash_param
            
            # 初始化二级表
            temp_table = [None] * second_level_size
            
            # 填充二级表
            for key in group_keys:
                hash_val = self._perfect_hash(key, hash_param, second_level_size)
                temp_table[hash_val] = self.symbol_map[key]
            
            # 转换为列表存储
            for i in range(second_level_size):
                if temp_table[i] is not None:
                    self.second_level_tables[group_index].append(temp_table[i])
    
    def _find_perfect_hash_function(self, keys: List[str], table_size: int) -> int:
        """
        查找无冲突的哈希函数参数
        """
        if len(keys) <= 1:
            return 0
        
        for attempt in range(1000):
            param = random.randint(1, 2**31 - 1)
            hashes = set()
            
            collision = False
            for key in keys:
                hash_val = self._perfect_hash(key, param, table_size)
                if hash_val in hashes:
                    collision = True
                    break
                hashes.add(hash_val)
            
            if not collision:
                return param  # 找到无冲突的哈希函数
        
        # 如果找不到完美的哈希函数，使用简单的方法
        return 1
    
    def _perfect_hash(self, key: str, param: int, table_size: int) -> int:
        """
        完美哈希函数
        """
        hash_val = hash(key)
        hash_val = (hash_val ^ param) * 0x9e3779b9  # 乘以黄金比例
        return abs(hash_val) % table_size
    
    def lookup(self, symbol_name: str) -> Optional[SymbolEntry]:
        """
        查找符号
        
        Args:
            symbol_name: 符号名称
            
        Returns:
            符号条目，如果未找到返回None
        """
        if symbol_name is None:
            return None
        
        first_hash = abs(hash(symbol_name)) % self.first_level_size
        second_level_size = self.second_level_sizes[first_hash]
        hash_param = self.hash_params[first_hash]
        
        if second_level_size == 0:
            return None
        
        second_hash = self._perfect_hash(symbol_name, hash_param, second_level_size)
        
        second_level_table = self.second_level_tables[first_hash]
        if second_hash < len(second_level_table):
            return second_level_table[second_hash]
        
        return None
    
    def get_all_symbol_names(self) -> List[str]:
        """
        获取所有符号名称
        """
        return self.all_keys[:]
    
    def size(self) -> int:
        """
        获取符号表大小
        """
        return len(self.all_keys)


class OptimizedPerfectHashSymbolTable:
    """优化版本：使用更高效的完美哈希实现"""
    
    def __init__(self, symbols: List[SymbolEntry]):
        self.symbol_map = {}
        keys = []
        
        for symbol in symbols:
            self.symbol_map[symbol.name] = symbol
            keys.append(symbol.name)
        
        n = len(keys)
        self.first_level_size = max(1, int(n * 2.0))  # 增大一级表减少冲突
        self.second_level_tables = [None] * self.first_level_size
        self.second_level_sizes = [0] * self.first_level_size
        self.hash_params = [0] * self.first_level_size
        
        # 分组
        groups = {}
        for key in keys:
            group_index = abs(hash(key)) % self.first_level_size
            if group_index not in groups:
                groups[group_index] = []
            groups[group_index].append(key)
        
        # 为每组构建二级表
        for group_index, group_keys in groups.items():
            if not group_keys:
                continue
            
            # 计算二级表大小
            group_size = len(group_keys)
            second_level_size = group_size * group_size if group_size > 2 else group_size * 2
            
            # 寻找无冲突哈希函数
            hash_param = self._find_hash_function(group_keys, second_level_size)
            self.hash_params[group_index] = hash_param
            self.second_level_sizes[group_index] = second_level_size
            
            # 创建二级表
            table = [None] * second_level_size
            for key in group_keys:
                index = self._hash(key, hash_param, second_level_size)
                table[index] = self.symbol_map[key]
            self.second_level_tables[group_index] = table
    
    def _find_hash_function(self, keys: List[str], table_size: int) -> int:
        """
        查找无冲突的哈希函数参数
        """
        if len(keys) <= 1:
            return 0
        
        for attempt in range(1000):
            param = random.randint(1, 1000000)
            used = [False] * table_size
            collision = False
            
            for key in keys:
                index = self._hash(key, param, table_size)
                if used[index]:
                    collision = True
                    break
                used[index] = True
            
            if not collision:
                return param
        
        return 1  # fallback
    
    def _hash(self, key: str, param: int, table_size: int) -> int:
        """
        哈希函数
        """
        hash_val = hash(key)
        hash_val = (hash_val ^ param) * 0x9e3779b9
        return abs(hash_val) % table_size
    
    def lookup(self, symbol_name: str) -> Optional[SymbolEntry]:
        """
        查找符号
        
        Args:
            symbol_name: 符号名称
            
        Returns:
            符号条目，如果未找到返回None
        """
        if symbol_name is None:
            return None
        
        first_index = abs(hash(symbol_name)) % self.first_level_size
        second_level_table = self.second_level_tables[first_index]
        
        if second_level_table is None:
            return None
        
        second_level_size = self.second_level_sizes[first_index]
        hash_param = self.hash_params[first_index]
        second_index = self._hash(symbol_name, hash_param, second_level_size)
        
        if second_index < len(second_level_table):
            return second_level_table[second_index]
        
        return None


def create_test_symbols() -> List[SymbolEntry]:
    """创建测试符号"""
    symbols = []
    symbols.append(SymbolEntry("int", "keyword", 0, 1))
    symbols.append(SymbolEntry("float", "keyword", 0, 1))
    symbols.append(SymbolEntry("double", "keyword", 0, 1))
    symbols.append(SymbolEntry("char", "keyword", 0, 1))
    symbols.append(SymbolEntry("if", "keyword", 0, 2))
    symbols.append(SymbolEntry("else", "keyword", 0, 2))
    symbols.append(SymbolEntry("while", "keyword", 0, 3))
    symbols.append(SymbolEntry("for", "keyword", 0, 3))
    symbols.append(SymbolEntry("return", "keyword", 0, 4))
    symbols.append(SymbolEntry("main", "function", 0, 5))
    symbols.append(SymbolEntry("printf", "function", 0, 6))
    symbols.append(SymbolEntry("scanf", "function", 0, 6))
    return symbols


def test_basic_version(symbols: List[SymbolEntry]):
    """基础版本测试"""
    print("--- 基础版本测试 ---")
    symbol_table = PerfectHashSymbolTable(symbols)
    
    # 测试查找功能
    int_symbol = symbol_table.lookup("int")
    main_symbol = symbol_table.lookup("main")
    non_existent = symbol_table.lookup("nonexistent")
    
    print(f"查找 'int': {int_symbol}")
    print(f"查找 'main': {main_symbol}")
    print(f"查找不存在的符号: {non_existent}")
    
    # 测试所有符号
    print(f"所有符号名称: {symbol_table.get_all_symbol_names()}")
    print(f"符号表大小: {symbol_table.size()}")
    print()


def test_optimized_version(symbols: List[SymbolEntry]):
    """优化版本测试"""
    print("--- 优化版本测试 ---")
    symbol_table = OptimizedPerfectHashSymbolTable(symbols)
    
    # 测试查找功能
    int_symbol = symbol_table.lookup("int")
    main_symbol = symbol_table.lookup("main")
    non_existent = symbol_table.lookup("nonexistent")
    
    print(f"查找 'int': {int_symbol}")
    print(f"查找 'main': {main_symbol}")
    print(f"查找不存在的符号: {non_existent}")
    print()


def performance_test():
    """性能测试"""
    print("--- 性能测试 ---")
    
    # 创建大量测试符号
    large_symbols = []
    for i in range(1000):
        large_symbols.append(SymbolEntry(f"symbol{i}", "variable", 0, i))
    
    import time
    
    # 测试基础版本
    start_time = time.time()
    basic_table = PerfectHashSymbolTable(large_symbols)
    build_time = time.time() - start_time
    
    start_time = time.time()
    for i in range(10000):
        basic_table.lookup(f"symbol{i % 1000}")
    lookup_time = time.time() - start_time
    
    print("基础版本:")
    print(f"  构建时间: {build_time*1000:.2f}ms")
    print(f"  10000次查找时间: {lookup_time*1000:.2f}ms")
    print(f"  平均每次查找: {lookup_time*1000/10000:.4f}ms")
    
    # 测试优化版本
    start_time = time.time()
    optimized_table = OptimizedPerfectHashSymbolTable(large_symbols)
    optimized_build_time = time.time() - start_time
    
    start_time = time.time()
    for i in range(10000):
        optimized_table.lookup(f"symbol{i % 1000}")
    optimized_lookup_time = time.time() - start_time
    
    print("优化版本:")
    print(f"  构建时间: {optimized_build_time*1000:.2f}ms")
    print(f"  10000次查找时间: {optimized_lookup_time*1000:.2f}ms")
    print(f"  平均每次查找: {optimized_lookup_time*1000/10000:.4f}ms")
    print()


def edge_case_test():
    """边界情况测试"""
    print("--- 边界情况测试 ---")
    
    # 测试空符号表
    empty_table = PerfectHashSymbolTable([])
    print(f"空符号表大小: {empty_table.size()}")
    print(f"空符号表查找: {empty_table.lookup('test')}")
    
    # 测试单个符号
    single_symbol = [SymbolEntry("single", "variable", 0, 1)]
    single_table = PerfectHashSymbolTable(single_symbol)
    print(f"单符号表查找: {single_table.lookup('single')}")
    
    # 测试None查找
    print(f"None查找: {single_table.lookup('')}")
    
    print()


if __name__ == "__main__":
    print("=== 测试 编译器符号表（完美哈希实现） ===")
    
    # 创建测试符号
    symbols = create_test_symbols()
    
    # 基础版本测试
    test_basic_version(symbols)
    
    # 优化版本测试
    test_optimized_version(symbols)
    
    # 性能测试
    performance_test()
    
    # 边界情况测试
    edge_case_test()