#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
调试BM算法
"""

def compute_suffix_array(pattern):
    """
    计算后缀数组
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

def build_good_suffix_table(pattern):
    """
    构建好后缀规则表
    """
    m = len(pattern)
    # 初始化好后缀表
    good_suffix = [m] * m
    
    # 计算后缀数组
    suffix = compute_suffix_array(pattern)
    print(f"后缀数组: {suffix}")
    
    # case 1: 模式串的某一个后缀匹配了模式串的前缀
    for i in range(m - 1, -1, -1):
        # 如果从位置i开始的后缀等于整个模式串的前缀
        if suffix[i] == m - i:
            # 对于所有可能的位置，设置移动距离
            for j in range(m - 1 - i):
                if good_suffix[j] == m:
                    good_suffix[j] = m - 1 - i
    
    print(f"Case 1后的good_suffix: {good_suffix}")
    
    # case 2: 模式串的某一个子串等于以j为边界的后缀
    for i in range(m - 1):
        # 当在位置i发生不匹配时，应该移动的距离
        if suffix[i] > 0:
            good_suffix[m - 1 - suffix[i]] = m - 1 - i
    
    print(f"Case 2后的good_suffix: {good_suffix}")
    
    return good_suffix

def search(text, pattern):
    """
    BM字符串匹配算法
    """
    n = len(text)
    m = len(pattern)
    
    # 构建好后缀规则表
    good_suffix = build_good_suffix_table(pattern)
    
    # 开始匹配
    i = 0  # 文本串中的位置
    while i <= n - m:
        j = m - 1  # 从模式串的最后一个字符开始匹配
        print(f"\n匹配位置: {i}")
        
        # 从右向左匹配
        while j >= 0 and pattern[j] == text[i + j]:
            j -= 1
        
        # 找到完全匹配
        if j < 0:
            print(f"  找到匹配位置: {i}")
            return i
        
        print(f"  不匹配位置: {j}")
        
        # 计算好后缀规则的移动距离
        good_suffix_shift = good_suffix[j]
        print(f"  好后缀移动距离: {good_suffix_shift}")
        
        i += good_suffix_shift
    
    return -1  # 未找到匹配

# 测试
pattern = "GCAGAGAG"
print(f"模式串: {pattern}")

result = search("GCATCGCAGAGAGTATACAGTACG", pattern)
print(f"结果: {result}")