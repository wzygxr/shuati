#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Manacher算法实现
用于在O(n)时间复杂度内查找字符串中的最长回文子串
核心思想：利用已知回文子串的信息，避免重复计算

时间复杂度：O(n)
空间复杂度：O(n)
"""

from typing import List, Set

class ManacherAlgorithm:
    @staticmethod
    def _preprocess(s: str) -> str:
        """
        预处理字符串，在每个字符之间插入特殊字符（如'#'）
        这样可以统一处理奇数长度和偶数长度的回文子串
        
        Args:
            s (str): 原始字符串
            
        Returns:
            str: 预处理后的字符串
        """
        if not s:
            return "^$"
        
        result = ["^"]
        for char in s:
            result.extend(['#', char])
        result.extend(['#', '$'])
        return ''.join(result)
    
    @staticmethod
    def find_longest_palindromic_substring(s: str) -> str:
        """
        使用Manacher算法查找最长回文子串
        
        Args:
            s (str): 输入字符串
            
        Returns:
            str: 最长回文子串
            
        Raises:
            ValueError: 如果输入字符串为None
        """
        if s is None:
            raise ValueError("输入字符串不能为None")
        
        if len(s) <= 1:
            return s  # 空字符串或单字符字符串的最长回文子串就是自身
        
        # 预处理字符串
        T = ManacherAlgorithm._preprocess(s)
        n = len(T)
        
        # P[i]表示以T[i]为中心的最长回文子串的半径（不包括中心）
        P = [0] * n
        
        # C是当前回文子串的中心，R是当前回文子串的右边界
        C, R = 0, 0
        
        # 最大回文子串的中心索引和半径
        max_len, center_index = 0, 0
        
        # 遍历预处理后的字符串，跳过^和$
        for i in range(1, n - 1):
            # 计算i关于C的对称点
            i_mirror = 2 * C - i  # C - (i - C)
            
            # 利用回文的对称性初始化P[i]
            # 如果i在R的范围内，可以利用对称点的信息
            # 否则初始化为0
            if R > i:
                P[i] = min(R - i, P[i_mirror])
            else:
                P[i] = 0
            
            # 尝试扩展回文子串
            # 注意这里是直接比较字符，而不是像暴力方法那样每次都检查边界
            try:
                while T[i + 1 + P[i]] == T[i - 1 - P[i]]:
                    P[i] += 1
            except IndexError:
                pass  # 边界情况处理
            
            # 如果扩展后的回文子串的右边界超过R，则更新C和R
            if i + P[i] > R:
                C, R = i, i + P[i]
            
            # 更新最长回文子串的信息
            if P[i] > max_len:
                max_len = P[i]
                center_index = i
        
        # 计算原始字符串中最长回文子串的起始和结束位置
        # 注意预处理字符串中的索引转换
        start = (center_index - max_len) // 2  # 转换为原始字符串的索引
        return s[start:start + max_len]
    
    @staticmethod
    def count_palindromic_substrings(s: str) -> int:
        """
        计算字符串中回文子串的数量（包括单个字符）
        
        Args:
            s (str): 输入字符串
            
        Returns:
            int: 回文子串的数量
            
        Raises:
            ValueError: 如果输入字符串为None
        """
        if s is None:
            raise ValueError("输入字符串不能为None")
        
        if not s:
            return 0
        
        # 预处理字符串
        T = ManacherAlgorithm._preprocess(s)
        n = len(T)
        P = [0] * n
        C, R = 0, 0
        count = 0
        
        for i in range(1, n - 1):
            i_mirror = 2 * C - i
            if R > i:
                P[i] = min(R - i, P[i_mirror])
            else:
                P[i] = 0
            
            try:
                while T[i + 1 + P[i]] == T[i - 1 - P[i]]:
                    P[i] += 1
            except IndexError:
                pass
            
            if i + P[i] > R:
                C, R = i, i + P[i]
            
            # P[i]表示半径，每个半径对应一个回文子串
            # 注意：这里需要除以2因为预处理字符串中的'#'不代表实际字符
            count += (P[i] + 1) // 2
        
        return count
    
    @staticmethod
    def find_all_distinct_palindromic_substrings(s: str) -> Set[str]:
        """
        查找所有不同的回文子串
        
        Args:
            s (str): 输入字符串
            
        Returns:
            Set[str]: 包含所有不同回文子串的集合
            
        Raises:
            ValueError: 如果输入字符串为None
        """
        if s is None:
            raise ValueError("输入字符串不能为None")
        
        result = set()
        if not s:
            return result
        
        # 预处理字符串
        T = ManacherAlgorithm._preprocess(s)
        n = len(T)
        P = [0] * n
        C, R = 0, 0
        
        for i in range(1, n - 1):
            i_mirror = 2 * C - i
            if R > i:
                P[i] = min(R - i, P[i_mirror])
            else:
                P[i] = 0
            
            try:
                while T[i + 1 + P[i]] == T[i - 1 - P[i]]:
                    P[i] += 1
            except IndexError:
                pass
            
            if i + P[i] > R:
                C, R = i, i + P[i]
            
            # 提取所有以i为中心的回文子串
            # 从1开始（半径至少为1）到P[i]
            for r in range(1, P[i] + 1):
                start = (i - r) // 2
                end = start + r
                palindrome = s[start:end]
                result.add(palindrome)
        
        return result


# 测试代码
if __name__ == "__main__":
    # 测试用例1：基本功能测试
    text1 = "babad"
    print("=== 测试用例1 ===")
    print(f"文本: {text1}")
    print(f"最长回文子串: {ManacherAlgorithm.find_longest_palindromic_substring(text1)}")  # "bab" 或 "aba"
    print(f"回文子串数量: {ManacherAlgorithm.count_palindromic_substrings(text1)}")  # 7
    print(f"不同回文子串: {ManacherAlgorithm.find_all_distinct_palindromic_substrings(text1)}")
    
    # 测试用例2：边界情况
    text2 = "cbbd"
    print("\n=== 测试用例2 ===")
    print(f"文本: {text2}")
    print(f"最长回文子串: {ManacherAlgorithm.find_longest_palindromic_substring(text2)}")  # "bb"
    
    # 测试用例3：单个字符
    text3 = "a"
    print("\n=== 测试用例3 ===")
    print(f"文本: {text3}")
    print(f"最长回文子串: {ManacherAlgorithm.find_longest_palindromic_substring(text3)}")  # "a"
    
    # 测试用例4：重复字符
    text4 = "aaa"
    print("\n=== 测试用例4 ===")
    print(f"文本: {text4}")
    print(f"最长回文子串: {ManacherAlgorithm.find_longest_palindromic_substring(text4)}")  # "aaa"
    print(f"回文子串数量: {ManacherAlgorithm.count_palindromic_substrings(text4)}")  # 6
    
    # 测试用例5：较长文本
    text5 = "mississippi"
    print("\n=== 测试用例5 ===")
    print(f"文本: {text5}")
    print(f"最长回文子串: {ManacherAlgorithm.find_longest_palindromic_substring(text5)}")  # "ississi"