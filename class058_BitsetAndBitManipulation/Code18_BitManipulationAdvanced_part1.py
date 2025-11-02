"""
高级位操作算法实现 - 第一部分
包含LeetCode多个高级位操作相关题目的解决方案

题目列表:
1. LeetCode 78 - 子集
2. LeetCode 90 - 子集 II
3. LeetCode 187 - 重复的DNA序列
4. LeetCode 190 - 颠倒二进制位
5. LeetCode 318 - 最大单词长度乘积
"""

import time
from typing import List, Set, Tuple
import sys
from functools import lru_cache

class BitManipulationAdvanced:
    """高级位操作算法类"""
    
    @staticmethod
    def subsets(nums: List[int]) -> List[List[int]]:
        """
        LeetCode 78 - 子集
        给定一组不含重复元素的整数数组 nums，返回该数组所有可能的子集（幂集）。
        
        方法: 位掩码法
        时间复杂度: O(n * 2^n)
        空间复杂度: O(2^n)
        
        原理: 使用二进制位表示每个元素是否在子集中
        从0到2^n-1的每个数字对应一个子集
        """
        n = len(nums)
        total = 1 << n  # 2^n个子集
        result = []
        
        for mask in range(total):
            subset = []
            for i in range(n):
                if mask & (1 << i):
                    subset.append(nums[i])
            result.append(subset)
        
        return result
    
    @staticmethod
    def subsets_with_dup(nums: List[int]) -> List[List[int]]:
        """
        LeetCode 90 - 子集 II
        给定一个可能包含重复元素的整数数组 nums，返回该数组所有可能的子集（幂集）。
        
        方法: 排序 + 位掩码 + 去重
        时间复杂度: O(n * 2^n)
        空间复杂度: O(2^n)
        
        原理: 先排序，然后使用位掩码生成子集，使用集合去重
        """
        nums.sort()
        n = len(nums)
        total = 1 << n
        unique_subsets = set()
        
        for mask in range(total):
            subset = []
            for i in range(n):
                if mask & (1 << i):
                    subset.append(nums[i])
            # 将列表转换为元组以便放入集合
            unique_subsets.add(tuple(subset))
        
        # 将元组转换回列表
        result = [list(subset) for subset in unique_subsets]
        return result
    
    @staticmethod
    def find_repeated_dna_sequences(s: str) -> List[str]:
        """
        LeetCode 187 - 重复的DNA序列
        所有 DNA 都由一系列缩写为 'A'，'C'，'G' 和 'T' 的核苷酸组成。
        编写一个函数来找出所有目标子串，目标子串的长度为 10，且在 DNA 字符串 s 中出现超过一次。
        
        方法: 滑动窗口 + 位编码
        时间复杂度: O(n)
        空间复杂度: O(n)
        
        原理: 使用2位表示每个字符，10个字符需要20位，可以用整数表示
        """
        if len(s) < 10:
            return []
        
        # 字符到数字的映射
        mapping = {'A': 0, 'C': 1, 'G': 2, 'T': 3}
        count = {}
        result = []
        
        # 计算第一个窗口的哈希值
        hash_val = 0
        for i in range(10):
            hash_val = (hash_val << 2) | mapping[s[i]]
        count[hash_val] = 1
        
        # 滑动窗口
        for i in range(10, len(s)):
            # 移除最左边的字符，添加新字符
            hash_val = ((hash_val << 2) & 0xFFFFF) | mapping[s[i]]
            
            if hash_val in count:
                count[hash_val] += 1
                if count[hash_val] == 2:  # 第一次重复出现
                    result.append(s[i-9:i+1])
            else:
                count[hash_val] = 1
        
        return result
    
    @staticmethod
    def reverse_bits(n: int) -> int:
        """
        LeetCode 190 - 颠倒二进制位
        颠倒给定的 32 位无符号整数的二进制位。
        
        方法: 逐位反转
        时间复杂度: O(1) - 固定32位
        空间复杂度: O(1)
        """
        result = 0
        
        for i in range(32):
            result = (result << 1) | (n & 1)
            n >>= 1
        
        return result
    
    @staticmethod
    def max_product(words: List[str]) -> int:
        """
        LeetCode 318 - 最大单词长度乘积
        给定一个字符串数组 words，找到 length(word[i]) * length(word[j]) 的最大值，
        并且这两个单词不含有公共字母。你可以认为每个单词只包含小写字母。
        
        方法: 位掩码 + 预计算
        时间复杂度: O(n^2 + n * L)
        空间复杂度: O(n)
        
        原理: 使用26位表示每个单词包含的字母，没有公共字母即位掩码与运算为0
        """
        n = len(words)
        masks = [0] * n
        lengths = [0] * n
        
        # 预计算每个单词的位掩码和长度
        for i in range(n):
            mask = 0
            for c in words[i]:
                mask |= (1 << (ord(c) - ord('a')))
            masks[i] = mask
            lengths[i] = len(words[i])
        
        max_product = 0
        # 检查所有单词对
        for i in range(n):
            for j in range(i + 1, n):
                if (masks[i] & masks[j]) == 0:  # 没有公共字母
                    max_product = max(max_product, lengths[i] * lengths[j])
        
        return max_product