#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
最简化的BM算法测试
"""

def search(text, pattern):
    """
    最简化的BM算法，只使用坏字符规则
    """
    n = len(text)
    m = len(pattern)
    
    # 构建坏字符规则表
    bad_char = build_bad_char_table(pattern)
    
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
        i += bad_char_shift
    
    return -1  # 未找到匹配

def build_bad_char_table(pattern):
    """
    构建坏字符规则表
    """
    m = len(pattern)
    bad_char = [-1] * 256
    
    # 记录每个字符最右边出现的位置
    for i in range(m):
        bad_char[ord(pattern[i])] = i
    
    return bad_char

# 测试用例5：BM算法优势场景
text5 = "GCATCGCAGAGAGTATACAGTACG"
pattern5 = "GCAGAGAG"
print(f"文本串: {text5}")
print(f"模式串: {pattern5}")

result = search(text5, pattern5)
print(f"查找结果: {result}")