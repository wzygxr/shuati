#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
简化版BM算法用于调试
"""

def search(text, pattern):
    """
    简化版BM字符串匹配算法，只使用坏字符规则
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
        
        # 只使用坏字符规则移动
        i += bad_char_shift
    
    return -1  # 未找到匹配

def build_bad_char_table(pattern):
    """
    构建坏字符规则表
    
    Args:
        pattern (str): 模式串
        
    Returns:
        list: 坏字符表
    """
    m = len(pattern)
    bad_char = [-1] * 256
    
    # 记录每个字符最右边出现的位置
    for i in range(m):
        bad_char[ord(pattern[i])] = i
    
    return bad_char

# 测试代码
if __name__ == "__main__":
    # 测试用例1：基本匹配
    text1 = "hello world"
    pattern1 = "world"
    print(f"测试1 - 查找'world'在'hello world'中的位置: {search(text1, pattern1)}")  # 应该是6
    
    # 测试用例5：BM算法优势场景
    text5 = "GCATCGCAGAGAGTATACAGTACG"
    pattern5 = "GCAGAGAG"
    print(f"测试5 - 查找模式串在文本串中的位置: {search(text5, pattern5)}")  # 应该是5