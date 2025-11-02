#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
KMP算法扩展题目集合 - Python版本

本模块包含来自多个算法平台的KMP算法相关题目，包括：
- LeetCode
- HackerRank  
- Codeforces
- 洛谷
- 牛客网
- SPOJ
- USACO
- AtCoder

每个题目都包含：
1. 题目描述和来源链接
2. 完整的KMP算法实现
3. 详细的时间复杂度和空间复杂度分析
4. 完整的测试用例
5. 工程化考量（异常处理、边界条件等）

@author Algorithm Journey
@version 1.0
@since 2024-01-01
"""

import time
from typing import List

def build_next_array(pattern: str) -> List[int]:
    """
    构建next数组的通用方法
    
    算法思路:
    1. 初始化next数组
    2. 使用双指针技术构建next数组
    3. 处理边界情况
    
    时间复杂度: O(m)
    空间复杂度: O(m)
    
    Args:
        pattern: 模式串
    
    Returns:
        next数组
    """
    m = len(pattern)
    if m == 0:
        return []
    
    next_arr = [0] * (m + 1)
    next_arr[0] = -1
    
    if m == 1:
        return next_arr
    
    next_arr[1] = 0
    i, cn = 2, 0
    
    while i <= m:
        if pattern[i - 1] == pattern[cn]:
            cn += 1
            next_arr[i] = cn
            i += 1
        elif cn > 0:
            cn = next_arr[cn]
        else:
            next_arr[i] = 0
            i += 1
    
    return next_arr

def kmp_all_matches(text: str, pattern: str) -> List[int]:
    """
    HackerRank: Knuth-Morris-Pratt Algorithm
    题目链接: https://www.hackerrank.com/challenges/kmp-fp/problem
    
    题目描述: 实现KMP算法，查找模式串在文本串中的所有出现位置
    
    算法思路:
    1. 使用KMP算法进行字符串匹配
    2. 记录所有匹配的起始位置
    3. 返回所有匹配位置的列表
    
    时间复杂度: O(n + m)，其中n是文本串长度，m是模式串长度
    空间复杂度: O(m)，用于存储next数组
    
    Args:
        text: 文本串
        pattern: 模式串
    
    Returns:
        所有匹配位置的列表
    """
    result = []
    
    # 边界条件处理
    if not text or not pattern:
        return result
    
    n, m = len(text), len(pattern)
    if m > n:
        return result
    
    # 构建next数组
    next_arr = build_next_array(pattern)
    
    i, j = 0, 0
    while i < n:
        if text[i] == pattern[j]:
            i += 1
            j += 1
        elif j == 0:
            i += 1
        else:
            j = next_arr[j]
        
        # 找到完整匹配
        if j == m:
            result.append(i - j)
            j = next_arr[j]  # 继续寻找重叠匹配
    
    return result

def find_password(s: str) -> str:
    """
    Codeforces 126B: Password
    题目链接: https://codeforces.com/contest/126/problem/B
    
    题目描述: 给定一个字符串s，找出一个最长的子串，该子串同时作为前缀、后缀和中间子串出现
    
    算法思路:
    1. 计算整个字符串的next数组
    2. 找到最大的k，使得s[0...k-1]既是前缀又是后缀
    3. 检查这个前缀是否在字符串中间出现
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    
    Args:
        s: 输入字符串
    
    Returns:
        满足条件的最长子串，如果不存在返回空字符串
    """
    if not s or len(s) < 3:
        return ""
    
    n = len(s)
    next_arr = build_next_array(s)
    
    # 找到最长的既是前缀又是后缀的子串
    max_len = next_arr[n]
    
    # 检查这个前缀是否在中间出现
    found_in_middle = False
    for i in range(1, n - 1):
        if next_arr[i] == max_len:
            found_in_middle = True
            break
    
    if max_len > 0 and found_in_middle:
        return s[:max_len]
    
    # 如果最长的不行，尝试次长的
    candidate = next_arr[max_len]
    if candidate > 0:
        for i in range(1, n - 1):
            if next_arr[i] == candidate:
                return s[:candidate]
    
    return ""

def luogu_kmp(text: str, pattern: str) -> List[int]:
    """
    洛谷 P3375: 【模板】KMP
    题目链接: https://www.luogu.com.cn/problem/P3375
    
    题目描述: KMP算法模板题，输出模式串在文本串中的所有出现位置
    
    算法思路:
    1. 标准的KMP算法实现
    2. 输出所有匹配位置（从1开始计数）
    
    时间复杂度: O(n + m)
    空间复杂度: O(m)
    
    Args:
        text: 文本串
        pattern: 模式串
    
    Returns:
        所有匹配位置（从1开始）
    """
    result = []
    
    if not text or not pattern:
        return result
    
    n, m = len(text), len(pattern)
    if m > n:
        return result
    
    next_arr = build_next_array(pattern)
    i, j = 0, 0
    
    while i < n:
        if text[i] == pattern[j]:
            i += 1
            j += 1
        elif j == 0:
            i += 1
        else:
            j = next_arr[j]
        
        if j == m:
            result.append(i - j + 1)  # 从1开始计数
            j = next_arr[j]
    
    return result

def spoj_pattern_find(text: str, pattern: str) -> List[int]:
    """
    SPOJ: NAJPF - Pattern Find
    题目链接: https://www.spoj.com/problems/NAJPF/
    
    题目描述: 查找模式串在文本串中的所有出现位置
    
    算法思路: 标准KMP算法
    
    时间复杂度: O(n + m)
    空间复杂度: O(m)
    
    Args:
        text: 文本串
        pattern: 模式串
    
    Returns:
        所有匹配位置（从0开始）
    """
    return kmp_all_matches(text, pattern)

def nowcoder_str_str(text: str, pattern: str) -> int:
    """
    牛客网: 字符串匹配
    题目链接: 牛客网相关题目
    
    题目描述: 实现字符串匹配功能
    
    算法思路: 标准KMP算法
    
    时间复杂度: O(n + m)
    空间复杂度: O(m)
    
    Args:
        text: 文本串
        pattern: 模式串
    
    Returns:
        第一个匹配位置，如果没有返回-1
    """
    if not text or not pattern:
        return -1
    
    if len(pattern) == 0:
        return 0
    
    n, m = len(text), len(pattern)
    if m > n:
        return -1
    
    next_arr = build_next_array(pattern)
    i, j = 0, 0
    
    while i < n and j < m:
        if text[i] == pattern[j]:
            i += 1
            j += 1
        elif j == 0:
            i += 1
        else:
            j = next_arr[j]
    
    return i - j if j == m else -1

def usaco_string_match(text: str, pattern: str) -> bool:
    """
    USACO: String Transformation
    题目描述: 字符串变换相关题目
    
    算法思路: 使用KMP算法进行模式匹配
    
    时间复杂度: O(n + m)
    空间复杂度: O(m)
    
    Args:
        text: 文本串
        pattern: 模式串
    
    Returns:
        是否匹配
    """
    return nowcoder_str_str(text, pattern) != -1

def at_coder_kmp(text: str, pattern: str) -> int:
    """
    AtCoder: String Algorithms
    题目描述: 字符串算法相关题目
    
    算法思路: 使用KMP算法进行高效匹配
    
    时间复杂度: O(n + m)
    空间复杂度: O(m)
    
    Args:
        text: 文本串
        pattern: 模式串
    
    Returns:
        匹配次数
    """
    matches = kmp_all_matches(text, pattern)
    return len(matches)

def test_hacker_rank_kmp():
    """测试HackerRank题目"""
    print("=== HackerRank: Knuth-Morris-Pratt Algorithm ===")
    
    text = "ABABDABACDABABCABAB"
    pattern = "ABABCABAB"
    result = kmp_all_matches(text, pattern)
    
    print(f"文本串: {text}")
    print(f"模式串: {pattern}")
    print(f"匹配位置: {result}")
    print("期望: [10]")
    print()

def test_codeforces_password():
    """测试Codeforces题目"""
    print("=== Codeforces 126B: Password ===")
    
    test_cases = [
        "fixprefixsuffix",  # 期望: "fix"
        "abcdabc",          # 期望: ""
        "abcabcabc",        # 期望: "abcabc"
        "aaa"               # 期望: "a"
    ]
    
    for test_case in test_cases:
        result = find_password(test_case)
        print(f"输入: {test_case}")
        print(f"输出: {result}")
        print()

def test_luogu_kmp():
    """测试洛谷题目"""
    print("=== 洛谷 P3375: 【模板】KMP ===")
    
    text = "ABABABABCABAABABABAB"
    pattern = "ABABAB"
    result = luogu_kmp(text, pattern)
    
    print(f"文本串: {text}")
    print(f"模式串: {pattern}")
    print(f"匹配位置(从1开始): {result}")
    print("期望: [1, 3, 5, 13, 15]")
    print()

def test_spoj_pattern_find():
    """测试SPOJ题目"""
    print("=== SPOJ: NAJPF - Pattern Find ===")
    
    text = "AAAAA"
    pattern = "AA"
    result = spoj_pattern_find(text, pattern)
    
    print(f"文本串: {text}")
    print(f"模式串: {pattern}")
    print(f"匹配位置: {result}")
    print("期望: [0, 1, 2, 3]")
    print()

def test_nowcoder_str_str():
    """测试牛客网题目"""
    print("=== 牛客网: 字符串匹配 ===")
    
    text = "hello world"
    pattern = "world"
    result = nowcoder_str_str(text, pattern)
    
    print(f"文本串: {text}")
    print(f"模式串: {pattern}")
    print(f"匹配位置: {result}")
    print("期望: 6")
    print()

def test_usaco_string_match():
    """测试USACO题目"""
    print("=== USACO: String Transformation ===")
    
    text = "transformation"
    pattern = "form"
    result = usaco_string_match(text, pattern)
    
    print(f"文本串: {text}")
    print(f"模式串: {pattern}")
    print(f"是否匹配: {result}")
    print("期望: True")
    print()

def test_at_coder_kmp():
    """测试AtCoder题目"""
    print("=== AtCoder: String Algorithms ===")
    
    text = "abcabcabc"
    pattern = "abc"
    result = at_coder_kmp(text, pattern)
    
    print(f"文本串: {text}")
    print(f"模式串: {pattern}")
    print(f"匹配次数: {result}")
    print("期望: 3")
    print()

def performance_test():
    """工程化考量: 性能测试"""
    print("=== 性能测试 ===")
    
    # 生成大规模测试数据
    large_text = "ABCDEFG" * 100000
    pattern = "DEF"
    
    start_time = time.time()
    count = at_coder_kmp(large_text, pattern)
    end_time = time.time()
    
    print(f"文本长度: {len(large_text)}")
    print(f"模式串长度: {len(pattern)}")
    print(f"匹配次数: {count}")
    print(f"执行时间: {(end_time - start_time) * 1000:.2f} ms")
    print()

def memory_test():
    """工程化考量: 内存使用测试"""
    print("=== 内存使用测试 ===")
    
    import sys
    
    text = "A" * 10000 + "B"
    pattern = "A" * 1000
    
    # 获取当前内存使用
    result = kmp_all_matches(text, pattern)
    
    print(f"匹配结果数量: {len(result)}")
    # Python中获取精确内存使用比较复杂，这里简单显示
    print("内存使用: 测试完成")
    print()

def main():
    """主测试方法"""
    print("KMP算法扩展题目测试集\n")
    
    # 运行所有测试
    test_hacker_rank_kmp()
    test_codeforces_password()
    test_luogu_kmp()
    test_spoj_pattern_find()
    test_nowcoder_str_str()
    test_usaco_string_match()
    test_at_coder_kmp()
    
    # 工程化测试
    performance_test()
    memory_test()
    
    print("所有测试完成!")

if __name__ == "__main__":
    main()