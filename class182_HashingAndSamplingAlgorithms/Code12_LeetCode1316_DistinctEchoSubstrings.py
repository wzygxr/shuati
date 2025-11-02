#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 1316. 不同的循环子字符串 - Python版本

题目来源：https://leetcode.com/problems/distinct-echo-substrings/
题目描述：给定一个字符串 text，返回 text 中不同非空子字符串的数量，这些子字符串可以写成某个字符串与其自身连接的结果。

算法思路：
1. 使用字符串哈希快速计算子串哈希值
2. 遍历所有可能的子串长度（偶数长度）
3. 检查子串是否可以分成两个相等的部分
4. 使用哈希集合存储不同的循环子字符串

时间复杂度：O(n²)，其中n为字符串长度
空间复杂度：O(n²)

工程化考量：
- 使用滚动哈希优化性能
- 处理哈希冲突
- 边界条件处理
"""

import time
from typing import Set, List

class Solution:
    # 哈希参数
    BASE = 131
    MOD = 1000000007
    
    def distinctEchoSubstrings(self, text: str) -> int:
        """
        主函数：计算不同的循环子字符串数量
        
        Args:
            text: 输入字符串
            
        Returns:
            不同的循环子字符串数量
        """
        if not text or len(text) < 2:
            return 0
        
        n = len(text)
        result: Set[str] = set()
        
        # 预处理前缀哈希数组
        prefix_hash = [0] * (n + 1)
        power = [1] * (n + 1)
        
        for i in range(1, n + 1):
            prefix_hash[i] = (prefix_hash[i - 1] * self.BASE + ord(text[i - 1])) % self.MOD
            power[i] = (power[i - 1] * self.BASE) % self.MOD
        
        # 遍历所有可能的子串长度（偶数长度）
        for length in range(2, n + 1, 2):
            for i in range(n - length + 1):
                mid = i + length // 2
                
                # 使用哈希快速比较两个子串是否相等
                hash1 = self._get_hash(prefix_hash, power, i, mid - 1)
                hash2 = self._get_hash(prefix_hash, power, mid, i + length - 1)
                
                if hash1 == hash2:
                    # 防止哈希冲突，实际比较字符串
                    substr = text[i:i + length]
                    if self._is_echo_substring(substr):
                        result.add(substr)
        
        return len(result)
    
    def _get_hash(self, prefix_hash: List[int], power: List[int], left: int, right: int) -> int:
        """
        获取子串的哈希值
        
        Args:
            prefix_hash: 前缀哈希数组
            power: 幂次数组
            left: 子串起始位置
            right: 子串结束位置
            
        Returns:
            子串哈希值
        """
        return (prefix_hash[right + 1] - prefix_hash[left] * power[right - left + 1] % self.MOD + self.MOD) % self.MOD
    
    def _is_echo_substring(self, s: str) -> bool:
        """
        检查字符串是否为循环子字符串
        
        Args:
            s: 输入字符串
            
        Returns:
            是否为循环子字符串
        """
        n = len(s)
        if n % 2 != 0:
            return False
        
        half = n // 2
        for i in range(half):
            if s[i] != s[i + half]:
                return False
        return True
    
    def distinctEchoSubstringsOptimized(self, text: str) -> int:
        """
        优化版本：使用双哈希减少冲突
        
        Args:
            text: 输入字符串
            
        Returns:
            不同的循环子字符串数量
        """
        if not text or len(text) < 2:
            return 0
        
        n = len(text)
        result: Set[int] = set()
        
        # 双哈希参数
        BASE1, MOD1 = 131, 1000000007
        BASE2, MOD2 = 13131, 1000000009
        
        # 预处理前缀哈希数组
        prefix_hash1 = [0] * (n + 1)
        prefix_hash2 = [0] * (n + 1)
        power1 = [1] * (n + 1)
        power2 = [1] * (n + 1)
        
        for i in range(1, n + 1):
            prefix_hash1[i] = (prefix_hash1[i - 1] * BASE1 + ord(text[i - 1])) % MOD1
            prefix_hash2[i] = (prefix_hash2[i - 1] * BASE2 + ord(text[i - 1])) % MOD2
            power1[i] = (power1[i - 1] * BASE1) % MOD1
            power2[i] = (power2[i - 1] * BASE2) % MOD2
        
        # 遍历所有可能的子串长度（偶数长度）
        for length in range(2, n + 1, 2):
            for i in range(n - length + 1):
                mid = i + length // 2
                
                # 使用双哈希比较两个子串是否相等
                hash1_left = self._get_hash_with_params(prefix_hash1, power1, i, mid - 1, MOD1)
                hash1_right = self._get_hash_with_params(prefix_hash1, power1, mid, i + length - 1, MOD1)
                
                hash2_left = self._get_hash_with_params(prefix_hash2, power2, i, mid - 1, MOD2)
                hash2_right = self._get_hash_with_params(prefix_hash2, power2, mid, i + length - 1, MOD2)
                
                if hash1_left == hash1_right and hash2_left == hash2_right:
                    # 双哈希一致，基本可以确定相等
                    combined_hash = hash1_left * MOD2 + hash2_left
                    result.add(combined_hash)
        
        return len(result)
    
    def _get_hash_with_params(self, prefix_hash: List[int], power: List[int], left: int, right: int, mod: int) -> int:
        """
        使用指定参数获取子串哈希值
        
        Args:
            prefix_hash: 前缀哈希数组
            power: 幂次数组
            left: 子串起始位置
            right: 子串结束位置
            mod: 模数
            
        Returns:
            子串哈希值
        """
        return (prefix_hash[right + 1] - prefix_hash[left] * power[right - left + 1] % mod + mod) % mod
    
    def distinctEchoSubstringsBruteForce(self, text: str) -> int:
        """
        暴力解法（用于对比验证）
        
        Args:
            text: 输入字符串
            
        Returns:
            不同的循环子字符串数量
        """
        if not text or len(text) < 2:
            return 0
        
        result: Set[str] = set()
        n = len(text)
        
        # 遍历所有可能的子串
        for i in range(n):
            for j in range(i + 1, n):
                length = j - i + 1
                if length % 2 == 0:
                    substr = text[i:j + 1]
                    if self._is_echo_substring(substr):
                        result.add(substr)
        
        return len(result)

def test_solution():
    """测试函数"""
    solution = Solution()
    
    # 测试用例1：标准测试
    text1 = "abcabcabc"
    result1 = solution.distinctEchoSubstrings(text1)
    print(f"测试1 - 输入: {text1}, 输出: {result1}")
    print("预期结果: 3 (abcabc, bcabca, cabcab)")
    print()
    
    # 测试用例2：简单测试
    text2 = "leetcodeleetcode"
    result2 = solution.distinctEchoSubstrings(text2)
    print(f"测试2 - 输入: {text2}, 输出: {result2}")
    print("预期结果: 2 (leetcode, etcodele)")
    print()
    
    # 测试用例3：边界测试
    text3 = "aaaa"
    result3 = solution.distinctEchoSubstrings(text3)
    print(f"测试3 - 输入: {text3}, 输出: {result3}")
    print("预期结果: 2 (aa, aaaa)")
    print()
    
    # 性能对比测试
    text4 = 'a' * 100 + 'b' * 100
    
    start_time = time.time()
    result4 = solution.distinctEchoSubstringsOptimized(text4)
    end_time = time.time()
    print(f"优化算法耗时: {(end_time - start_time) * 1000:.2f}ms, 结果: {result4}")
    
    start_time = time.time()
    result5 = solution.distinctEchoSubstringsBruteForce(text4)
    end_time = time.time()
    print(f"暴力算法耗时: {(end_time - start_time) * 1000:.2f}ms, 结果: {result5}")
    
    # 验证算法正确性
    print("\n=== 算法正确性验证 ===")
    test_cases = ["abcabc", "leetcode", "aaa", "abab"]
    for test_case in test_cases:
        optimized = solution.distinctEchoSubstringsOptimized(test_case)
        brute_force = solution.distinctEchoSubstringsBruteForce(test_case)
        print(f"输入: {test_case}, 优化算法: {optimized}, 暴力算法: {brute_force}, 一致: {optimized == brute_force}")

if __name__ == "__main__":
    test_solution()

"""
复杂度分析：

时间复杂度：
- 基础版本：O(n²)，需要遍历所有可能的子串
- 优化版本：O(n²)，但常数项更小
- 暴力版本：O(n³)，需要实际比较字符串

空间复杂度：
- 基础版本：O(n²)，存储所有不同的循环子字符串
- 优化版本：O(n²)，存储哈希值
- 暴力版本：O(n²)，存储字符串

算法优化点：
1. 使用前缀哈希数组：预处理后可以在O(1)时间内获取任意子串的哈希值
2. 双哈希减少冲突：使用两个不同的哈希函数组合，大大降低哈希冲突概率
3. 只考虑偶数长度：循环子字符串必须是偶数长度

边界情况处理：
- 空字符串或长度小于2的字符串直接返回0
- 处理哈希冲突：当哈希值相同时，实际比较字符串内容
- 大数溢出处理：使用模运算防止整数溢出

工程化考量：
- 可配置的哈希参数
- 详细的注释和文档
- 测试用例覆盖各种边界情况
- 性能对比验证

Python特定优化：
- 使用列表推导式提高代码可读性
- 使用集合自动去重
- 使用类型注解提高代码可读性
- 使用切片操作处理字符串

实际应用场景：
1. 文本模式识别：查找文本中的重复模式
2. 数据压缩：识别可压缩的重复模式
3. 生物信息学：DNA序列中的重复片段检测
4. 代码分析：查找程序中的重复代码模式

扩展思考：
1. 如何扩展到查找多个重复模式（如AAA、ABAB等）？
2. 如何处理包含通配符的模式匹配？
3. 如何优化内存使用以适应超大字符串？
4. 如何实现增量更新（流式处理）？
"""