#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
哈希算法实现 (Python版本)

包括双模哈希、三模哈希、前缀哈希等实现

哈希算法在计算机科学中有着广泛的应用，包括：
1. 数据结构：哈希表、布隆过滤器
2. 密码学：数字签名、消息认证
3. 数据完整性：校验和、数字指纹
4. 数据库：索引、分区
5. 网络：负载均衡、缓存
"""

import math
from typing import List, Tuple

class HashingAlgorithms:
    # 常用的大质数，用于哈希计算
    MOD1 = 1000000007  # 10^9 + 7
    MOD2 = 1000000009  # 10^9 + 9
    MOD3 = 998244353   # 常用的NTT模数
    BASE = 31          # 哈希基数
    
    class SingleHash:
        """单模哈希"""
        
        def __init__(self, s: str, mod: int):
            """
            初始化单模哈希
            :param s: 输入字符串
            :param mod: 模数
            """
            self.mod = mod
            n = len(s)
            self.hash = [0] * (n + 1)
            self.pow = [0] * (n + 1)
            
            # 预计算幂次
            self.pow[0] = 1
            for i in range(1, n + 1):
                self.pow[i] = (self.pow[i - 1] * HashingAlgorithms.BASE) % mod
            
            # 计算前缀哈希
            for i in range(n):
                self.hash[i + 1] = (self.hash[i] * HashingAlgorithms.BASE + (ord(s[i]) - ord('a') + 1)) % mod
        
        def get_hash(self, l: int, r: int) -> int:
            """
            获取子串的哈希值
            :param l: 左边界（包含）
            :param r: 右边界（包含）
            :return: 子串哈希值
            """
            result = (self.hash[r + 1] - self.hash[l] * self.pow[r - l + 1]) % self.mod
            if result < 0:
                result += self.mod
            return result
    
    class DoubleHash:
        """双模哈希"""
        
        def __init__(self, s: str):
            """
            初始化双模哈希
            :param s: 输入字符串
            """
            self.hash1 = HashingAlgorithms.SingleHash(s, HashingAlgorithms.MOD1)
            self.hash2 = HashingAlgorithms.SingleHash(s, HashingAlgorithms.MOD2)
        
        def get_hash(self, l: int, r: int) -> Tuple[int, int]:
            """
            获取子串的双模哈希值
            :param l: 左边界（包含）
            :param r: 右边界（包含）
            :return: 子串双模哈希值（两个值组成的元组）
            """
            return (self.hash1.get_hash(l, r), self.hash2.get_hash(l, r))
        
        def equals(self, l1: int, r1: int, l2: int, r2: int) -> bool:
            """
            比较两个子串是否相等
            :param l1: 第一个子串左边界
            :param r1: 第一个子串右边界
            :param l2: 第二个子串左边界
            :param r2: 第二个子串右边界
            :return: 是否相等
            """
            hash1 = self.get_hash(l1, r1)
            hash2 = self.get_hash(l2, r2)
            return hash1[0] == hash2[0] and hash1[1] == hash2[1]
    
    class TripleHash:
        """三模哈希"""
        
        def __init__(self, s: str):
            """
            初始化三模哈希
            :param s: 输入字符串
            """
            self.hash1 = HashingAlgorithms.SingleHash(s, HashingAlgorithms.MOD1)
            self.hash2 = HashingAlgorithms.SingleHash(s, HashingAlgorithms.MOD2)
            self.hash3 = HashingAlgorithms.SingleHash(s, HashingAlgorithms.MOD3)
        
        def get_hash(self, l: int, r: int) -> Tuple[int, int, int]:
            """
            获取子串的三模哈希值
            :param l: 左边界（包含）
            :param r: 右边界（包含）
            :return: 子串三模哈希值（三个值组成的元组）
            """
            return (self.hash1.get_hash(l, r), 
                   self.hash2.get_hash(l, r), 
                   self.hash3.get_hash(l, r))
        
        def equals(self, l1: int, r1: int, l2: int, r2: int) -> bool:
            """
            比较两个子串是否相等
            :param l1: 第一个子串左边界
            :param r1: 第一个子串右边界
            :param l2: 第二个子串左边界
            :param r2: 第二个子串右边界
            :return: 是否相等
            """
            hash1 = self.get_hash(l1, r1)
            hash2 = self.get_hash(l2, r2)
            return (hash1[0] == hash2[0] and 
                   hash1[1] == hash2[1] and 
                   hash1[2] == hash2[2])
    
    class PersistentPrefixHash:
        """持久化前缀哈希"""
        
        def __init__(self, mod: int):
            """
            初始化持久化前缀哈希
            :param mod: 模数
            """
            self.mod = mod
            self.hashes = [[0]]  # 每个版本的哈希值
            self.powers = [[1]]  # 每个版本的幂次值
        
        def append(self, version: int, c: str) -> int:
            """
            在指定版本后添加字符
            :param version: 版本号
            :param c: 添加的字符
            :return: 新版本号
            """
            prev_hash = self.hashes[version]
            prev_pow = self.powers[version]
            n = len(prev_hash)
            
            # 创建新版本
            new_hash = prev_hash[:]
            new_pow = prev_pow[:]
            
            # 计算新添加的字符的哈希
            new_pow.append((new_pow[-1] * HashingAlgorithms.BASE) % self.mod)
            new_hash.append((new_hash[-1] * HashingAlgorithms.BASE + (ord(c) - ord('a') + 1)) % self.mod)
            
            self.hashes.append(new_hash)
            self.powers.append(new_pow)
            
            return len(self.hashes) - 1
        
        def get_hash(self, version: int, l: int, r: int) -> int:
            """
            获取指定版本中子串的哈希值
            :param version: 版本号
            :param l: 左边界（包含）
            :param r: 右边界（包含）
            :return: 子串哈希值
            """
            hash_vals = self.hashes[version]
            pow_vals = self.powers[version]
            result = (hash_vals[r + 1] - hash_vals[l] * pow_vals[r - l + 1]) % self.mod
            if result < 0:
                result += self.mod
            return result
    
    @staticmethod
    def collision_probability(mod: int, n: int) -> float:
        """
        计算哈希碰撞概率
        :param mod: 模数
        :param n: 字符串数量
        :return: 碰撞概率
        """
        # 使用生日悖论近似计算
        # P(碰撞) ≈ 1 - e^(-n*(n-1)/(2*mod))
        if mod <= 0 or n <= 1:
            return 0.0
        exponent = -((n * (n - 1)) / (2 * mod))
        return 1.0 - math.exp(exponent)
    
    @staticmethod
    def handle_overflow(value: int, mod: int) -> int:
        """
        处理无符号整数溢出
        :param value: 可能溢出的值
        :param mod: 模数
        :return: 正确的模运算结果
        """
        value %= mod
        if value < 0:
            value += mod
        return value


def main():
    """测试方法"""
    # 测试字符串
    test = "ababababab"
    print(f"测试字符串: {test}")
    
    # 单模哈希测试
    print("\n=== 单模哈希测试 ===")
    single_hash = HashingAlgorithms.SingleHash(test, HashingAlgorithms.MOD1)
    print(f"子串[0,1]的哈希值: {single_hash.get_hash(0, 1)}")
    print(f"子串[2,3]的哈希值: {single_hash.get_hash(2, 3)}")
    print(f"子串[4,5]的哈希值: {single_hash.get_hash(4, 5)}")
    
    # 双模哈希测试
    print("\n=== 双模哈希测试 ===")
    double_hash = HashingAlgorithms.DoubleHash(test)
    hash1 = double_hash.get_hash(0, 1)
    hash2 = double_hash.get_hash(2, 3)
    hash3 = double_hash.get_hash(4, 5)
    print(f"子串[0,1]的双模哈希值: [{hash1[0]}, {hash1[1]}]")
    print(f"子串[2,3]的双模哈希值: [{hash2[0]}, {hash2[1]}]")
    print(f"子串[4,5]的双模哈希值: [{hash3[0]}, {hash3[1]}]")
    print(f"子串[0,1]和[2,3]是否相等: {double_hash.equals(0, 1, 2, 3)}")
    print(f"子串[0,1]和[4,5]是否相等: {double_hash.equals(0, 1, 4, 5)}")
    
    # 三模哈希测试
    print("\n=== 三模哈希测试 ===")
    triple_hash = HashingAlgorithms.TripleHash(test)
    thash1 = triple_hash.get_hash(0, 1)
    thash2 = triple_hash.get_hash(2, 3)
    print(f"子串[0,1]的三模哈希值: [{thash1[0]}, {thash1[1]}, {thash1[2]}]")
    print(f"子串[2,3]的三模哈希值: [{thash2[0]}, {thash2[1]}, {thash2[2]}]")
    print(f"子串[0,1]和[2,3]是否相等: {triple_hash.equals(0, 1, 2, 3)}")
    
    # 哈希碰撞概率测试
    print("\n=== 哈希碰撞概率测试 ===")
    n1 = 1000000  # 100万个字符串
    print(f"使用模数 {HashingAlgorithms.MOD1} 时，{n1} 个字符串的碰撞概率: " + 
          f"{HashingAlgorithms.collision_probability(HashingAlgorithms.MOD1, n1):.10f}")
    print(f"使用模数 {HashingAlgorithms.MOD2} 时，{n1} 个字符串的碰撞概率: " + 
          f"{HashingAlgorithms.collision_probability(HashingAlgorithms.MOD2, n1):.10f}")
    print(f"使用模数 {HashingAlgorithms.MOD3} 时，{n1} 个字符串的碰撞概率: " + 
          f"{HashingAlgorithms.collision_probability(HashingAlgorithms.MOD3, n1):.10f}")
    
    # 持久化前缀哈希测试
    print("\n=== 持久化前缀哈希测试 ===")
    persistent_hash = HashingAlgorithms.PersistentPrefixHash(HashingAlgorithms.MOD1)
    version0 = 0
    version1 = persistent_hash.append(version0, 'a')
    version2 = persistent_hash.append(version1, 'b')
    version3 = persistent_hash.append(version2, 'a')
    
    print(f"版本0的哈希值: {persistent_hash.get_hash(version0, 0, 0)}")  # 空字符串
    print(f"版本1的前缀[0,0]哈希值: {persistent_hash.get_hash(version1, 0, 0)}")  # "a"
    print(f"版本2的前缀[0,1]哈希值: {persistent_hash.get_hash(version2, 0, 1)}")  # "ab"
    print(f"版本3的前缀[0,2]哈希值: {persistent_hash.get_hash(version3, 0, 2)}")  # "aba"


if __name__ == "__main__":
    main()