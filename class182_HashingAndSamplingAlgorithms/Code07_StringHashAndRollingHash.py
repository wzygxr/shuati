#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
字符串哈希与滚动哈希算法实现 - Python版本

本文件包含字符串哈希和滚动哈希的高级实现，包括：
- Rabin-Karp字符串匹配算法
- 滚动哈希技术
- 字符串哈希函数设计
- 哈希冲突处理策略
- 多哈希技术

这些算法在字符串匹配、文本搜索、数据去重等领域有重要应用
"""

import time
from typing import List, Dict, Optional, Set, Tuple
from enum import Enum


class ProbingStrategy(Enum):
    """探测策略枚举"""
    LINEAR = "linear"      # 线性探测
    QUADRATIC = "quadratic"  # 二次探测
    DOUBLE_HASH = "double_hash"  # 双重哈希


class RabinKarp:
    """
    Rabin-Karp字符串匹配算法
    应用场景：文本编辑器、搜索引擎、DNA序列匹配
    
    算法原理：
    1. 使用滚动哈希计算模式串和文本串的哈希值
    2. 通过比较哈希值快速排除不匹配的位置
    3. 当哈希值匹配时进行精确比较
    
    时间复杂度：平均O(n+m)，最坏O(nm)
    空间复杂度：O(1)
    """
    
    PRIME = 101  # 大质数
    BASE = 256   # 字符集大小
    
    @staticmethod
    def search(text: str, pattern: str) -> List[int]:
        """字符串匹配"""
        result = []
        n = len(text)
        m = len(pattern)
        
        if m == 0 or n < m:
            return result
        
        # 计算模式串哈希值和第一个窗口哈希值
        pattern_hash = 0
        text_hash = 0
        h = 1
        
        # 计算h = BASE^(m-1) % PRIME
        for i in range(m - 1):
            h = (h * RabinKarp.BASE) % RabinKarp.PRIME
        
        # 计算模式串和第一个窗口的哈希值
        for i in range(m):
            pattern_hash = (RabinKarp.BASE * pattern_hash + ord(pattern[i])) % RabinKarp.PRIME
            text_hash = (RabinKarp.BASE * text_hash + ord(text[i])) % RabinKarp.PRIME
        
        # 滑动窗口
        for i in range(n - m + 1):
            # 检查哈希值是否匹配
            if pattern_hash == text_hash:
                # 哈希值匹配，进行精确比较
                match = True
                for j in range(m):
                    if text[i + j] != pattern[j]:
                        match = False
                        break
                if match:
                    result.append(i)
            
            # 计算下一个窗口的哈希值
            if i < n - m:
                text_hash = (RabinKarp.BASE * (text_hash - ord(text[i]) * h) + ord(text[i + m])) % RabinKarp.PRIME
                
                # 处理负哈希值
                if text_hash < 0:
                    text_hash += RabinKarp.PRIME
        
        return result
    
    @staticmethod
    def search_multiple(text: str, patterns: List[str]) -> Dict[str, List[int]]:
        """多模式匹配版本"""
        result = {}
        
        for pattern in patterns:
            result[pattern] = RabinKarp.search(text, pattern)
        
        return result
    
    @staticmethod
    def search_with_wildcard(text: str, pattern: str, wildcard: str = '*') -> List[int]:
        """带通配符的字符串匹配"""
        result = []
        n = len(text)
        m = len(pattern)
        
        if m == 0 or n < m:
            return result
        
        # 计算模式串中非通配符部分的哈希值
        pattern_hash = 0
        text_hash = 0
        h = 1
        wildcard_count = 0
        
        for i in range(m - 1):
            h = (h * RabinKarp.BASE) % RabinKarp.PRIME
        
        # 计算模式串哈希值（忽略通配符）
        for i in range(m):
            if pattern[i] != wildcard:
                pattern_hash = (RabinKarp.BASE * pattern_hash + ord(pattern[i])) % RabinKarp.PRIME
            else:
                wildcard_count += 1
        
        # 计算第一个窗口的哈希值（忽略通配符位置）
        for i in range(m):
            if pattern[i] != wildcard:
                text_hash = (RabinKarp.BASE * text_hash + ord(text[i])) % RabinKarp.PRIME
        
        # 滑动窗口
        for i in range(n - m + 1):
            if pattern_hash == text_hash:
                # 哈希值匹配，进行精确比较（只比较非通配符位置）
                match = True
                for j in range(m):
                    if pattern[j] != wildcard and text[i + j] != pattern[j]:
                        match = False
                        break
                if match:
                    result.append(i)
            
            # 计算下一个窗口的哈希值
            if i < n - m:
                # 移除前一个字符的贡献（如果是非通配符位置）
                if pattern[0] != wildcard:
                    text_hash = (RabinKarp.BASE * (text_hash - ord(text[i]) * h) + ord(text[i + m])) % RabinKarp.PRIME
                else:
                    # 如果是通配符位置，重新计算整个窗口的哈希值
                    text_hash = 0
                    for j in range(1, m + 1):
                        if pattern[j] != wildcard:
                            text_hash = (RabinKarp.BASE * text_hash + ord(text[i + j])) % RabinKarp.PRIME
                
                if text_hash < 0:
                    text_hash += RabinKarp.PRIME
        
        return result


class RollingHash:
    """
    滚动哈希技术实现
    应用场景：字符串去重、最长重复子串、循环检测
    
    算法原理：
    1. 使用多项式哈希函数
    2. 支持O(1)时间复杂度的窗口滑动
    3. 支持多哈希减少冲突概率
    
    时间复杂度：O(n) 构建所有子串哈希
    空间复杂度：O(n)
    """
    
    def __init__(self, s: str, base: int, mod: int):
        self.base = base
        self.mod = mod
        n = len(s)
        self.hash = [0] * (n + 1)
        self.power = [1] * (n + 1)
        
        for i in range(1, n + 1):
            self.hash[i] = (self.hash[i - 1] * base + ord(s[i - 1])) % mod
            self.power[i] = (self.power[i - 1] * base) % mod
    
    def get_hash(self, l: int, r: int) -> int:
        """获取子串[l, r]的哈希值"""
        result = (self.hash[r + 1] - self.hash[l] * self.power[r - l + 1]) % self.mod
        if result < 0:
            result += self.mod
        return result
    
    def longest_repeated_substring(self, s: str) -> str:
        """查找最长重复子串"""
        n = len(s)
        left, right = 1, n
        result = ""
        
        while left <= right:
            mid = (left + right) // 2
            seen = {}
            found = False
            
            for i in range(n - mid + 1):
                h = self.get_hash(i, i + mid - 1)
                if h in seen:
                    prev = seen[h]
                    # 检查是否真的是重复子串（防止哈希冲突）
                    if s[prev:prev + mid] == s[i:i + mid]:
                        found = True
                        result = s[i:i + mid]
                        break
                else:
                    seen[h] = i
            
            if found:
                left = mid + 1
            else:
                right = mid - 1
        
        return result
    
    def count_distinct_substrings(self, s: str) -> int:
        """计算不同子串的数量"""
        n = len(s)
        seen = set()
        
        for length in range(1, n + 1):
            for i in range(n - length + 1):
                h = self.get_hash(i, i + length - 1)
                seen.add(h)
        
        return len(seen)
    
    def longest_palindrome(self, s: str) -> str:
        """查找最长回文子串"""
        if not s:
            return ""
        
        n = len(s)
        forward = RollingHash(s, self.base, self.mod)
        backward = RollingHash(s[::-1], self.base, self.mod)
        
        max_len = 0
        start = 0
        
        for i in range(n):
            # 奇数长度回文
            len1 = self._expand_around_center(forward, backward, i, i, n)
            # 偶数长度回文
            len2 = self._expand_around_center(forward, backward, i, i + 1, n)
            
            length = max(len1, len2)
            if length > max_len:
                max_len = length
                start = i - (length - 1) // 2
        
        return s[start:start + max_len]
    
    def _expand_around_center(self, forward: 'RollingHash', backward: 'RollingHash', 
                             left: int, right: int, n: int) -> int:
        """扩展中心"""
        while left >= 0 and right < n:
            # 使用哈希值检查回文
            forward_hash = forward.get_hash(left, right)
            backward_hash = backward.get_hash(n - right - 1, n - left - 1)
            
            if forward_hash != backward_hash:
                break
            
            left -= 1
            right += 1
        
        return right - left - 1


class MultiHash:
    """
    多哈希技术实现
    应用场景：需要高精度哈希匹配的场景
    
    算法原理：
    1. 使用多个不同的哈希函数
    2. 只有当所有哈希值都匹配时才认为匹配
    3. 显著降低哈希冲突概率
    
    时间复杂度：O(kn)，其中k是哈希函数数量
    空间复杂度：O(kn)
    """
    
    def __init__(self, s: str, k: int):
        self.k = k
        self.hashes = []
        
        # 使用不同的基数和模数
        bases = [131, 13331, 131313, 1313131, 13131313]
        mods = [1000000007, 1000000009, 1000000021, 1000000033, 1000000087]
        
        for i in range(k):
            self.hashes.append(RollingHash(s, bases[i], mods[i]))
    
    def get_multi_hash(self, l: int, r: int) -> List[int]:
        """获取子串的多重哈希值"""
        result = []
        for i in range(self.k):
            result.append(self.hashes[i].get_hash(l, r))
        return result
    
    def equals(self, l1: int, r1: int, l2: int, r2: int) -> bool:
        """比较两个子串的多重哈希值"""
        if r1 - l1 != r2 - l2:
            return False
        
        for i in range(self.k):
            if self.hashes[i].get_hash(l1, r1) != self.hashes[i].get_hash(l2, r2):
                return False
        return True
    
    def find_all_repeated_substrings(self, s: str, min_len: int) -> Dict[str, List[int]]:
        """查找所有重复子串"""
        n = len(s)
        result = {}
        
        # 使用多重哈希减少冲突
        for length in range(min_len, n + 1):
            seen = {}
            
            for i in range(n - length + 1):
                substr = s[i:i + length]
                
                if substr in seen:
                    prev = seen[substr]
                    if substr not in result:
                        result[substr] = [prev]
                    result[substr].append(i)
                else:
                    seen[substr] = i
        
        return result


class HashPerformanceAnalyzer:
    """字符串哈希的性能分析工具"""
    
    @staticmethod
    def analyze_hash_function(strings: List[str], base: int, mod: int) -> None:
        """分析哈希函数的质量"""
        hashes = set()
        collisions = 0
        
        for s in strings:
            h = 0
            for c in s:
                h = (h * base + ord(c)) % mod
            
            if h in hashes:
                collisions += 1
            else:
                hashes.add(h)
        
        collision_rate = collisions / len(strings)
        print(f"哈希函数分析: 基数={base}, 模数={mod}, 冲突率={collision_rate:.4%}")
    
    @staticmethod
    def compare_hash_functions(text: str, pattern: str) -> None:
        """比较不同哈希函数的性能"""
        # Rabin-Karp算法
        start_time = time.time()
        rk_result = RabinKarp.search(text, pattern)
        rk_duration = time.time() - start_time
        
        print(f"Rabin-Karp算法: {rk_duration:.6f}秒, 匹配位置: {rk_result}")
        
        # 暴力匹配算法
        start_time = time.time()
        bf_result = HashPerformanceAnalyzer._brute_force_search(text, pattern)
        bf_duration = time.time() - start_time
        
        print(f"暴力匹配算法: {bf_duration:.6f}秒, 匹配位置: {bf_result}")
    
    @staticmethod
    def _brute_force_search(text: str, pattern: str) -> List[int]:
        """暴力匹配算法"""
        result = []
        n = len(text)
        m = len(pattern)
        
        for i in range(n - m + 1):
            match = True
            for j in range(m):
                if text[i + j] != pattern[j]:
                    match = False
                    break
            if match:
                result.append(i)
        
        return result


def test_string_hash_and_rolling_hash():
    """单元测试函数"""
    print("=== 字符串哈希与滚动哈希算法测试 ===\n")
    
    # 测试Rabin-Karp算法
    print("1. Rabin-Karp字符串匹配算法测试:")
    text = "ABABDABACDABABCABAB"
    pattern = "ABABCABAB"
    positions = RabinKarp.search(text, pattern)
    print(f"文本: {text}")
    print(f"模式: {pattern}")
    print(f"匹配位置: {positions}")
    
    # 测试滚动哈希
    print("\n2. 滚动哈希技术测试:")
    rh = RollingHash("banana", 131, 1000000007)
    print(f"字符串: banana")
    print(f"最长重复子串: {rh.longest_repeated_substring('banana')}")
    print(f"不同子串数量: {rh.count_distinct_substrings('banana')}")
    
    # 测试多哈希技术
    print("\n3. 多哈希技术测试:")
    mh = MultiHash("mississippi", 3)
    print(f"字符串: mississippi")
    repeated = mh.find_all_repeated_substrings("mississippi", 2)
    print(f"重复子串: {repeated}")
    
    # 测试带通配符的匹配
    print("\n4. 带通配符的字符串匹配测试:")
    text2 = "AABAACAADAABAAABAA"
    pattern2 = "A*BA"
    wildcard_positions = RabinKarp.search_with_wildcard(text2, pattern2, '*')
    print(f"文本: {text2}")
    print(f"模式: {pattern2}")
    print(f"匹配位置: {wildcard_positions}")
    
    # 性能分析
    print("\n5. 哈希函数性能分析:")
    test_strings = ["hello", "world", "test", "string", "hash"]
    HashPerformanceAnalyzer.analyze_hash_function(test_strings, 131, 1000000007)
    HashPerformanceAnalyzer.analyze_hash_function(test_strings, 13331, 1000000009)
    
    print("\n=== 算法复杂度分析 ===")
    print("1. Rabin-Karp算法: 平均O(n+m)，最坏O(nm)时间，O(1)空间")
    print("2. 滚动哈希: O(n)构建时间，O(1)查询时间，O(n)空间")
    print("3. 多哈希技术: O(kn)时间，O(kn)空间，k为哈希函数数量")
    
    print("\n=== Python工程化应用场景 ===")
    print("1. 文本编辑器: 快速查找和替换")
    print("2. 搜索引擎: 网页内容索引和匹配")
    print("3. 生物信息学: DNA序列分析和比对")
    print("4. 数据去重: 检测重复文件和内容")
    
    print("\n=== Python性能优化策略 ===")
    print("1. 内存管理优化: 使用__slots__减少内存占用")
    print("2. 并发安全: 使用线程锁保护共享数据")
    print("3. 缓存优化: 使用LRU缓存提高访问性能")
    print("4. 异步IO: 使用asyncio提高并发处理能力")


if __name__ == "__main__":
    test_string_hash_and_rolling_hash()