#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
BM (Boyer-Moore) 字符串匹配算法实现 - 简化正确版本
仅使用坏字符规则，确保正确性
时间复杂度：
  - 最好情况: O(n/m)，其中n是文本长度，m是模式长度
  - 最坏情况: O(n*m)
  - 平均情况: O(n)
空间复杂度：O(k)，其中k是字符集大小
"""

class BoyerMooreAlgorithm:
    ALPHABET_SIZE = 256  # ASCII字符集大小
    
    @staticmethod
    def search(text, pattern):
        """
        BM字符串匹配算法（仅使用坏字符规则）
        
        Args:
            text (str): 文本串
            pattern (str): 模式串
            
        Returns:
            int: 模式串在文本串中首次出现的索引，如果不存在则返回-1
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
        
        # 构建坏字符规则表
        bad_char = BoyerMooreAlgorithm._build_bad_char_table(pattern)
        
        # 开始匹配
        i = 0  # 文本串中的位置
        while i <= n - m:
            j = m - 1  # 从模式串的最后一个字符开始匹配
            
            # 从右向左匹配
            while j >= 0 and pattern[j] == text[i + j]:
                j -= 1
            
            # 找到完全匹配
            if j < 0:
                return i
            
            # 计算坏字符规则的移动距离
            # 如果字符不在模式串中，移动j+1个位置
            # 如果字符在模式串中，移动j - bad_char[text[i+j]]个位置
            char_index = ord(text[i + j])
            if bad_char[char_index] == -1:
                # 字符不在模式串中，移动j+1个位置
                i += j + 1
            else:
                # 字符在模式串中，移动max(1, j - bad_char[char_index])个位置
                shift = j - bad_char[char_index]
                if shift < 1:
                    shift = 1
                i += shift
        
        return -1  # 未找到匹配
    
    @staticmethod
    def _build_bad_char_table(pattern):
        """
        构建坏字符规则表
        
        Args:
            pattern (str): 模式串
            
        Returns:
            list: 坏字符表，bad_char[c]表示字符c在模式串中最右边出现的位置
        """
        m = len(pattern)
        bad_char = [-1] * BoyerMooreAlgorithm.ALPHABET_SIZE
        
        # 记录每个字符最右边出现的位置
        for i in range(m):
            bad_char[ord(pattern[i])] = i
        
        return bad_char
    
    @staticmethod
    def search_all(text, pattern):
        """
        查找模式串在文本串中所有出现的位置
        
        Args:
            text (str): 文本串
            pattern (str): 模式串
            
        Returns:
            list: 包含所有匹配位置的列表
        """
        if text is None or pattern is None:
            raise ValueError("文本串和模式串不能为None")
        
        n = len(text)
        m = len(pattern)
        
        if m == 0:
            # 空模式串匹配每个位置的开始
            return list(range(n + 1))
        
        if n < m:
            return []  # 无匹配
        
        # 构建坏字符规则表
        bad_char = BoyerMooreAlgorithm._build_bad_char_table(pattern)
        
        # 存储所有匹配位置
        matches = []
        
        i = 0  # 文本串中的位置
        while i <= n - m:
            j = m - 1  # 从模式串的最后一个字符开始匹配
            
            # 从右向左匹配
            while j >= 0 and pattern[j] == text[i + j]:
                j -= 1
            
            # 找到完全匹配
            if j < 0:
                matches.append(i)
                # 移动一个位置继续查找
                i += 1
            else:
                # 计算坏字符规则的移动距离
                char_index = ord(text[i + j])
                if bad_char[char_index] == -1:
                    # 字符不在模式串中，移动j+1个位置
                    i += j + 1
                else:
                    # 字符在模式串中，移动max(1, j - bad_char[char_index])个位置
                    shift = j - bad_char[char_index]
                    if shift < 1:
                        shift = 1
                    i += shift
        
        return matches

# 测试代码
if __name__ == "__main__":
    # 测试用例1：基本匹配
    text1 = "hello world"
    pattern1 = "world"
    result1 = BoyerMooreAlgorithm.search(text1, pattern1)
    print(f"测试1 - 查找'world'在'hello world'中的位置: {result1}")  # 应该是6
    
    # 测试用例2：多次匹配
    text2 = "abababa"
    pattern2 = "aba"
    results2 = BoyerMooreAlgorithm.search_all(text2, pattern2)
    print(f"测试2 - 查找所有'aba'在'abababa'中的位置: {results2}")  # 应该是[0, 2, 4]
    
    # 测试用例3：无匹配
    text3 = "hello"
    pattern3 = "world"
    result3 = BoyerMooreAlgorithm.search(text3, pattern3)
    print(f"测试3 - 查找'world'在'hello'中的位置: {result3}")  # 应该是-1
    
    # 测试用例4：边界情况
    text4 = "test"
    pattern4 = ""
    results4 = BoyerMooreAlgorithm.search_all(text4, pattern4)
    print(f"测试4 - 查找空串在'test'中的位置: {results4}")  # 应该是[0, 1, 2, 3, 4]
    
    # 测试用例5：BM算法优势场景
    text5 = "GCATCGCAGAGAGTATACAGTACG"
    pattern5 = "GCAGAGAG"
    result5 = BoyerMooreAlgorithm.search(text5, pattern5)
    print(f"测试5 - 查找模式串在文本串中的位置: {result5}")  # 应该是5
    
    # 测试用例6：简单测试
    text6 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    pattern6 = "XYZ"
    result6 = BoyerMooreAlgorithm.search(text6, pattern6)
    print(f"测试6 - 查找'XYZ'在字母表中的位置: {result6}")  # 应该是23