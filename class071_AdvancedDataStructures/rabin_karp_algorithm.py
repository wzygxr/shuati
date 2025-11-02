#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Rabin-Karp滚动哈希字符串匹配算法实现

Rabin-Karp算法是一种基于哈希的字符串搜索算法，由Richard M. Karp和Michael O. Rabin在1987年提出。
该算法通过使用滚动哈希技术，使得可以在O(1)时间内更新滑动窗口的哈希值，从而实现高效的字符串匹配。

时间复杂度：
  - 最好情况: O(n+m)，其中n是文本长度，m是模式长度
  - 最坏情况: O(n*m)，当有很多哈希冲突时
  - 平均情况: O(n+m)
空间复杂度：O(1) - 基本操作，O(n) - 存储所有匹配

应用场景：
- 字符串搜索
- 重复子串检测
- 多模式串匹配（通过适当扩展）
- 生物信息学中的DNA序列匹配
"""

from typing import List, Tuple

class RabinKarpAlgorithm:
    BASE = 256  # 字符集大小
    MOD = 1000000007  # 大素数，防止溢出
    
    @staticmethod
    def search(text: str, pattern: str) -> int:
        """
        Rabin-Karp字符串匹配算法
        
        Args:
            text (str): 文本串
            pattern (str): 模式串
            
        Returns:
            int: 模式串在文本串中首次出现的索引，如果不存在则返回-1
            
        Raises:
            ValueError: 如果文本串或模式串为None
        """
        if text is None or pattern is None:
            raise ValueError("文本串和模式串不能为None")
        
        n = len(text)
        m = len(pattern)
        
        # 边界条件检查
        if m == 0:
            return 0  # 空模式串匹配任何位置的开始
        if n < m:
            return -1  # 文本串比模式串短，不可能匹配
        
        # 计算pattern的哈希值和text前m个字符的哈希值
        pattern_hash = 0
        text_hash = 0
        highest_pow = 1  # BASE^(m-1) % MOD
        
        # 预计算最高位权值和初始哈希值
        for i in range(m - 1):
            highest_pow = (highest_pow * RabinKarpAlgorithm.BASE) % RabinKarpAlgorithm.MOD
        
        for i in range(m):
            pattern_hash = (pattern_hash * RabinKarpAlgorithm.BASE + ord(pattern[i])) % RabinKarpAlgorithm.MOD
            text_hash = (text_hash * RabinKarpAlgorithm.BASE + ord(text[i])) % RabinKarpAlgorithm.MOD
        
        # 滑动窗口匹配
        for i in range(n - m + 1):
            # 如果哈希值相同，进行精确比较以避免哈希冲突
            if pattern_hash == text_hash:
                match = True
                for j in range(m):
                    if text[i + j] != pattern[j]:
                        match = False
                        break
                if match:
                    return i  # 找到匹配
            
            # 更新滑动窗口的哈希值
            if i < n - m:
                # 移除最左边的字符
                text_hash = (text_hash - highest_pow * ord(text[i]) % RabinKarpAlgorithm.MOD + RabinKarpAlgorithm.MOD) % RabinKarpAlgorithm.MOD
                # 添加新的右边字符
                text_hash = (text_hash * RabinKarpAlgorithm.BASE + ord(text[i + m])) % RabinKarpAlgorithm.MOD
        
        return -1  # 未找到匹配
    
    @staticmethod
    def search_all(text: str, pattern: str) -> List[int]:
        """
        查找模式串在文本串中所有出现的位置
        
        Args:
            text (str): 文本串
            pattern (str): 模式串
            
        Returns:
            List[int]: 包含所有匹配位置的列表
            
        Raises:
            ValueError: 如果文本串或模式串为None
        """
        matches = []
        
        if text is None or pattern is None:
            raise ValueError("文本串和模式串不能为None")
        
        n = len(text)
        m = len(pattern)
        
        # 边界条件检查
        if m == 0:
            # 空模式串匹配每个位置的开始
            for i in range(n + 1):
                matches.append(i)
            return matches
        if n < m:
            return matches  # 无匹配
        
        # 计算pattern的哈希值和text前m个字符的哈希值
        pattern_hash = 0
        text_hash = 0
        highest_pow = 1  # BASE^(m-1) % MOD
        
        # 预计算最高位权值和初始哈希值
        for i in range(m - 1):
            highest_pow = (highest_pow * RabinKarpAlgorithm.BASE) % RabinKarpAlgorithm.MOD
        
        for i in range(m):
            pattern_hash = (pattern_hash * RabinKarpAlgorithm.BASE + ord(pattern[i])) % RabinKarpAlgorithm.MOD
            text_hash = (text_hash * RabinKarpAlgorithm.BASE + ord(text[i])) % RabinKarpAlgorithm.MOD
        
        # 滑动窗口匹配
        for i in range(n - m + 1):
            # 如果哈希值相同，进行精确比较以避免哈希冲突
            if pattern_hash == text_hash:
                match = True
                for j in range(m):
                    if text[i + j] != pattern[j]:
                        match = False
                        break
                if match:
                    matches.append(i)  # 记录匹配位置
            
            # 更新滑动窗口的哈希值
            if i < n - m:
                # 移除最左边的字符
                text_hash = (text_hash - highest_pow * ord(text[i]) % RabinKarpAlgorithm.MOD + RabinKarpAlgorithm.MOD) % RabinKarpAlgorithm.MOD
                # 添加新的右边字符
                text_hash = (text_hash * RabinKarpAlgorithm.BASE + ord(text[i + m])) % RabinKarpAlgorithm.MOD
        
        return matches
    
    @staticmethod
    def search_double_hash(text: str, pattern: str) -> int:
        """
        双哈希版本的Rabin-Karp算法，用于减少哈希冲突
        
        Args:
            text (str): 文本串
            pattern (str): 模式串
            
        Returns:
            int: 模式串在文本串中首次出现的索引，如果不存在则返回-1
            
        Raises:
            ValueError: 如果文本串或模式串为None
        """
        if text is None or pattern is None:
            raise ValueError("文本串和模式串不能为None")
        
        n = len(text)
        m = len(pattern)
        
        if m == 0:
            return 0
        if n < m:
            return -1
        
        # 使用两个不同的哈希参数
        BASE1, MOD1 = 256, 1000000007
        BASE2, MOD2 = 263, 1000000009
        
        pattern_hash1, text_hash1 = 0, 0
        pattern_hash2, text_hash2 = 0, 0
        highest_pow1, highest_pow2 = 1, 1
        
        # 预计算最高位权值
        for i in range(m - 1):
            highest_pow1 = (highest_pow1 * BASE1) % MOD1
            highest_pow2 = (highest_pow2 * BASE2) % MOD2
        
        # 计算初始哈希值
        for i in range(m):
            pattern_hash1 = (pattern_hash1 * BASE1 + ord(pattern[i])) % MOD1
            text_hash1 = (text_hash1 * BASE1 + ord(text[i])) % MOD1
            pattern_hash2 = (pattern_hash2 * BASE2 + ord(pattern[i])) % MOD2
            text_hash2 = (text_hash2 * BASE2 + ord(text[i])) % MOD2
        
        # 滑动窗口匹配
        for i in range(n - m + 1):
            # 如果两个哈希值都相同，进行精确比较
            if pattern_hash1 == text_hash1 and pattern_hash2 == text_hash2:
                match = True
                for j in range(m):
                    if text[i + j] != pattern[j]:
                        match = False
                        break
                if match:
                    return i  # 找到匹配
            
            # 更新滑动窗口的哈希值
            if i < n - m:
                # 更新第一个哈希值
                text_hash1 = (text_hash1 - highest_pow1 * ord(text[i]) % MOD1 + MOD1) % MOD1
                text_hash1 = (text_hash1 * BASE1 + ord(text[i + m])) % MOD1
                
                # 更新第二个哈希值
                text_hash2 = (text_hash2 - highest_pow2 * ord(text[i]) % MOD2 + MOD2) % MOD2
                text_hash2 = (text_hash2 * BASE2 + ord(text[i + m])) % MOD2
        
        return -1  # 未找到匹配
    
    @staticmethod
    def find_repeated_dna_sequences(s: str, length: int = 10) -> List[str]:
        """
        查找DNA序列中重复的子序列（LeetCode 187题的变体）
        
        Args:
            s (str): DNA序列
            length (int): 子序列长度，默认为10
            
        Returns:
            List[str]: 所有重复出现的子序列
        """
        if s is None or length <= 0:
            return []
        
        n = len(s)
        if n < length:
            return []
        
        seen = set()
        repeated = set()
        
        # 使用滚动哈希
        BASE = 4  # DNA只有4种碱基：A, C, G, T
        MOD = 1000000007
        
        # 映射字符到数字
        char_to_int = {'A': 0, 'C': 1, 'G': 2, 'T': 3}
        
        # 计算第一个子串的哈希值
        hash_val = 0
        pow_base = 1
        for i in range(length - 1):
            pow_base = (pow_base * BASE) % MOD
        
        for i in range(length):
            hash_val = (hash_val * BASE + char_to_int.get(s[i], 0)) % MOD
        
        seen.add(hash_val)
        
        # 滑动窗口计算其余子串的哈希值
        for i in range(1, n - length + 1):
            # 移除最左边的字符
            hash_val = (hash_val - pow_base * char_to_int.get(s[i - 1], 0) % MOD + MOD) % MOD
            # 添加新的右边字符
            hash_val = (hash_val * BASE + char_to_int.get(s[i + length - 1], 0)) % MOD
            
            if hash_val in seen:
                repeated.add(s[i:i + length])
            else:
                seen.add(hash_val)
        
        return list(repeated)


# 测试代码
if __name__ == "__main__":
    # 测试用例1：基本字符串匹配
    text1 = "ABABDABACDABABCABCABCABCABC"
    pattern1 = "ABABCABCABC"
    print("=== 测试用例1：基本字符串匹配 ===")
    print(f"文本: {text1}")
    print(f"模式: {pattern1}")
    print(f"首次匹配位置: {RabinKarpAlgorithm.search(text1, pattern1)}")
    print(f"所有匹配位置: {RabinKarpAlgorithm.search_all(text1, pattern1)}")
    
    # 测试用例2：双哈希版本
    print("\n=== 测试用例2：双哈希版本 ===")
    print(f"双哈希匹配位置: {RabinKarpAlgorithm.search_double_hash(text1, pattern1)}")
    
    # 测试用例3：DNA序列重复子串查找
    dna_sequence = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"
    print("\n=== 测试用例3：DNA序列重复子串查找 ===")
    print(f"DNA序列: {dna_sequence}")
    print(f"重复的10字符子串: {RabinKarpAlgorithm.find_repeated_dna_sequences(dna_sequence, 10)}")
    
    # 测试用例4：边界情况
    print("\n=== 测试用例4：边界情况 ===")
    print(f"空模式串匹配: {RabinKarpAlgorithm.search('abc', '')}")
    print(f"模式串比文本串长: {RabinKarpAlgorithm.search('ab', 'abc')}")
    print(f"单字符匹配: {RabinKarpAlgorithm.search('abc', 'b')}")