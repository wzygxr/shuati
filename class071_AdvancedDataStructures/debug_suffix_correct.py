#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
正确实现后缀数组计算
"""

def compute_suffix_array(pattern):
    """
    正确计算后缀数组：suffix[i]表示模式串从位置i开始的后缀与模式串本身的最长公共后缀长度
    """
    m = len(pattern)
    suffix = [0] * m
    
    # 最后一个位置的后缀就是整个模式串，长度为m
    suffix[m - 1] = m
    
    # 从倒数第二个位置开始向前计算
    for i in range(m - 2, -1, -1):
        j = i + 1  # 从i+1位置开始比较
        k = 0      # 匹配长度
        
        # 比较pattern[i:]和pattern[j:]
        while j < m and pattern[i + k] == pattern[j]:
            k += 1
            j += 1
        
        suffix[i] = k
    
    return suffix

# 测试
pattern5 = "GCAGAGAG"
print(f"模式串: {pattern5}")

suffix = compute_suffix_array(pattern5)
print(f"正确的后缀数组: {suffix}")

# 手动验证
print("手动验证:")
for i in range(len(pattern5)):
    suffix_str = pattern5[i:]
    match_len = 0
    for k in range(len(suffix_str)):
        if k < len(pattern5) and suffix_str[k] == pattern5[k]:
            match_len += 1
        else:
            break
    print(f"位置{i}的后缀'{suffix_str}'与模式串的最长公共后缀长度: {match_len}")