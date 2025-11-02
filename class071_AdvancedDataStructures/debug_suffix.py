#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
调试后缀数组计算
"""

def compute_suffix_array(pattern):
    """
    计算后缀数组：suffix[i]表示模式串从位置i开始的后缀与模式串本身的最长公共后缀长度
    """
    m = len(pattern)
    suffix = [0] * m
    
    # 最后一个位置的后缀就是整个模式串，长度为m
    suffix[m - 1] = m
    
    # 从倒数第二个位置开始向前计算
    for i in range(m - 2, -1, -1):
        # 如果pattern[i+1]与pattern[m-1]不相等，则后缀长度为0
        if pattern[i] != pattern[m - 1]:
            suffix[i] = 0
        else:
            # 否则，后缀长度为pattern[i+1]位置的后缀长度+1
            suffix[i] = suffix[i + 1] + 1
    
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
    j = 0
    for i in range(m - 1, -1, -1):
        # 如果从位置i开始的后缀等于整个模式串的前缀
        if suffix[i] == m - i:
            # 对于所有小于m-1-i的位置，如果还没有设置移动距离，则设置为m-1-i
            while j < m - 1 - i:
                if good_suffix[j] == m:
                    good_suffix[j] = m - 1 - i
                j += 1
    
    print(f"Case 1后的good_suffix: {good_suffix}")
    
    # case 2: 模式串的某一个子串等于以j为边界的后缀
    for i in range(m - 1):
        # 当在位置i发生不匹配时，应该移动的距离
        good_suffix[m - 1 - suffix[i]] = m - 1 - i
    
    print(f"Case 2后的good_suffix: {good_suffix}")
    
    return good_suffix

# 测试
pattern5 = "GCAGAGAG"
print(f"模式串: {pattern5}")

suffix = compute_suffix_array(pattern5)
print(f"后缀数组: {suffix}")

good_suffix = build_good_suffix_table(pattern5)
print(f"好后缀表: {good_suffix}")