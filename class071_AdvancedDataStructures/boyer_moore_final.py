#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
最终版BM (Boyer-Moore) 字符串匹配算法实现
包含坏字符规则和好后缀规则
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
        BM字符串匹配算法
        
        Args:
            text (str): 文本串
            pattern (str): 模式串
            
        Returns:
            int: 模式串在文本串中首次出现的索引，如果不存在则返回-1
            
        Raises:
            ValueError: 如果输入参数为None
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
        
        # 构建好后缀规则表
        good_suffix = BoyerMooreAlgorithm._build_good_suffix_table(pattern)
        
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
            bad_char_shift = max(1, j - bad_char[ord(text[i + j])])
            
            # 计算好后缀规则的移动距离
            good_suffix_shift = good_suffix[j]
            
            # 取两个规则中的最大移动距离
            i += max(bad_char_shift, good_suffix_shift)
        
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
    def _build_good_suffix_table(pattern):
        """
        构建好后缀规则表
        
        Args:
            pattern (str): 模式串
            
        Returns:
            list: 好后缀表，good_suffix[j]表示当j位置出现不匹配时的移动距离
        """
        m = len(pattern)
        # 初始化好后缀表
        good_suffix = [m] * m
        
        # 计算后缀数组
        suffix = BoyerMooreAlgorithm._compute_suffix_array(pattern)
        
        # case 1: 模式串的某一个后缀匹配了模式串的前缀
        for i in range(m - 1, -1, -1):
            # 如果从位置i开始的后缀等于整个模式串的前缀
            if suffix[i] == m - i:
                # 对于所有可能的位置，设置移动距离
                for j in range(m - 1 - i):
                    if good_suffix[j] == m:
                        good_suffix[j] = m - 1 - i
        
        # case 2: 模式串的某一个子串等于以j为边界的后缀
        for i in range(m - 1):
            # 当在位置i发生不匹配时，应该移动的距离
            good_suffix[m - 1 - suffix[i]] = m - 1 - i
        
        return good_suffix
    
    @staticmethod
    def _compute_suffix_array(pattern):
        """
        计算后缀数组：suffix[i]表示模式串从位置i开始的后缀与模式串本身的最长公共后缀长度
        
        Args:
            pattern (str): 模式串
            
        Returns:
            list: 后缀数组
        """
        m = len(pattern)
        suffix = [0] * m
        
        # 最后一个位置的后缀就是整个模式串，长度为m
        suffix[m - 1] = m
        
        # 从倒数第二个位置开始向前计算
        for i in range(m - 2, -1, -1):
            j = 0  # 从模式串开头开始比较
            k = i  # 从位置i开始比较
            
            # 比较pattern[i:]和pattern[0:]
            while j < m and k < m and pattern[k] == pattern[j]:
                j += 1
                k += 1
            
            suffix[i] = j
        
        return suffix
    
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
        
        # 构建坏字符规则表和好后缀规则表
        bad_char = BoyerMooreAlgorithm._build_bad_char_table(pattern)
        good_suffix = BoyerMooreAlgorithm._build_good_suffix_table(pattern)
        
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
                bad_char_shift = max(1, j - bad_char[ord(text[i + j])])
                
                # 计算好后缀规则的移动距离
                good_suffix_shift = good_suffix[j]
                
                # 取两个规则中的最大移动距离
                i += max(bad_char_shift, good_suffix_shift)
        
        return matches

# 测试代码
if __name__ == "__main__":
    # 测试用例1：基本匹配
    text1 = "hello world"
    pattern1 = "world"
    print(f"测试1 - 查找'world'在'hello world'中的位置: {BoyerMooreAlgorithm.search(text1, pattern1)}")  # 应该是6
    
    # 测试用例2：多次匹配
    text2 = "abababa"
    pattern2 = "aba"
    results2 = BoyerMooreAlgorithm.search_all(text2, pattern2)
    print(f"测试2 - 查找所有'aba'在'abababa'中的位置: {results2}")  # 应该是[0, 2, 4]
    
    # 测试用例3：无匹配
    text3 = "hello"
    pattern3 = "world"
    print(f"测试3 - 查找'world'在'hello'中的位置: {BoyerMooreAlgorithm.search(text3, pattern3)}")  # 应该是-1
    
    # 测试用例4：边界情况
    text4 = "test"
    pattern4 = ""
    results4 = BoyerMooreAlgorithm.search_all(text4, pattern4)
    print(f"测试4 - 查找空串在'test'中的位置: {results4}")  # 应该是[0, 1, 2, 3, 4]
    
    # 测试用例5：BM算法优势场景
    text5 = "GCATCGCAGAGAGTATACAGTACG"
    pattern5 = "GCAGAGAG"
    print(f"测试5 - 查找模式串在文本串中的位置: {BoyerMooreAlgorithm.search(text5, pattern5)}")  # 应该是5
    
    # 测试用例6：好后缀规则测试
    text6 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    pattern6 = "XYZ"
    print(f"测试6 - 查找'XYZ'在字母表中的位置: {BoyerMooreAlgorithm.search(text6, pattern6)}")  # 应该是23