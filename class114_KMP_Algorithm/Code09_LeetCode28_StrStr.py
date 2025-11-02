#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 28. 实现 strStr()

题目来源：LeetCode (力扣)
题目链接：https://leetcode.cn/problems/implement-strstr/

题目描述：
给你两个字符串 haystack 和 needle，请你在 haystack 字符串中找出 needle 字符串的第一个匹配项的下标（下标从 0 开始）。
如果 needle 不是 haystack 的一部分，则返回 -1。

示例：
输入：haystack = "sadbutsad", needle = "sad"
输出：0

输入：haystack = "leetcode", needle = "leeto"
输出：-1

算法思路：
使用KMP算法进行字符串匹配，避免在匹配失败时文本串指针的回溯。

时间复杂度：O(n + m)，其中n是文本串长度，m是模式串长度
空间复杂度：O(m)，用于存储next数组
"""


def build_next_array(pattern):
    """
    构建KMP算法的next数组（部分匹配表）

    next[i]表示pattern[0...i]子串的最长相等前后缀的长度

    算法思路：
    1. 初始化next[0] = 0
    2. 使用双指针i和j，i指向当前处理的位置，j指向前缀的末尾
    3. 如果pattern[i] == pattern[j]，说明前缀和后缀可以延长，next[i] = j + 1
    4. 如果pattern[i] != pattern[j]，需要回退j指针到next[j-1]，直到匹配或j=0

    :param pattern: 模式串
    :return: next数组
    """
    length = len(pattern)
    next_array = [0] * length

    # 初始化
    next_array[0] = 0
    prefix_len = 0  # 当前最长相等前后缀的长度
    i = 1  # 当前处理的位置

    # 从位置1开始处理
    while i < length:
        # 如果当前字符匹配，可以延长相等前后缀
        if pattern[i] == pattern[prefix_len]:
            prefix_len += 1
            next_array[i] = prefix_len
            i += 1
        # 如果不匹配且前缀长度大于0，需要回退
        elif prefix_len > 0:
            prefix_len = next_array[prefix_len - 1]
        # 如果不匹配且前缀长度为0，next[i] = 0
        else:
            next_array[i] = 0
            i += 1

    return next_array


def kmp_search(text, pattern):
    """
    KMP算法核心实现

    算法步骤：
    1. 预处理模式串，生成next数组
    2. 使用双指针同时遍历文本串和模式串
    3. 当字符匹配时，两个指针都向前移动
    4. 当字符不匹配且模式串指针不为0时，根据next数组调整模式串指针
    5. 当字符不匹配且模式串指针为0时，文本串指针向前移动
    6. 当模式串指针等于模式串长度时，说明匹配成功，返回起始位置

    :param text: 文本串
    :param pattern: 模式串
    :return: 第一次匹配的起始位置，如果未找到则返回-1
    """
    text_length = len(text)
    pattern_length = len(pattern)

    # 边界条件处理
    if pattern_length == 0:
        return 0

    if text_length < pattern_length:
        return -1

    # 构建next数组
    next_array = build_next_array(pattern)

    text_index = 0  # 文本串指针
    pattern_index = 0  # 模式串指针

    # 匹配过程
    while text_index < text_length and pattern_index < pattern_length:
        # 字符匹配，两个指针都向前移动
        if text[text_index] == pattern[pattern_index]:
            text_index += 1
            pattern_index += 1
        # 字符不匹配且模式串指针不为0，根据next数组调整模式串指针
        elif pattern_index > 0:
            pattern_index = next_array[pattern_index - 1]
        # 字符不匹配且模式串指针为0，文本串指针向前移动
        else:
            text_index += 1

    # 如果模式串指针等于模式串长度，说明匹配成功
    if pattern_index == pattern_length:
        return text_index - pattern_index

    return -1


def str_str(haystack, needle):
    """
    在文本串haystack中查找模式串needle第一次出现的位置

    :param haystack: 文本串
    :param needle: 模式串
    :return: 第一次匹配的起始位置，如果未找到则返回-1
    """
    return kmp_search(haystack, needle)


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    haystack1 = "sadbutsad"
    needle1 = "sad"
    result1 = str_str(haystack1, needle1)
    print(f"测试用例1: haystack=\"{haystack1}\", needle=\"{needle1}\"")
    print(f"预期输出: 0, 实际输出: {result1}")
    print()

    # 测试用例2
    haystack2 = "leetcode"
    needle2 = "leeto"
    result2 = str_str(haystack2, needle2)
    print(f"测试用例2: haystack=\"{haystack2}\", needle=\"{needle2}\"")
    print(f"预期输出: -1, 实际输出: {result2}")
    print()

    # 测试用例3
    haystack3 = "hello"
    needle3 = "ll"
    result3 = str_str(haystack3, needle3)
    print(f"测试用例3: haystack=\"{haystack3}\", needle=\"{needle3}\"")
    print(f"预期输出: 2, 实际输出: {result3}")
    print()

    # 测试用例4 - 空模式串
    haystack4 = "abc"
    needle4 = ""
    result4 = str_str(haystack4, needle4)
    print(f"测试用例4: haystack=\"{haystack4}\", needle=\"{needle4}\"")
    print(f"预期输出: 0, 实际输出: {result4}")
    print()

    # 测试用例5 - 模式串比文本串长
    haystack5 = "a"
    needle5 = "aa"
    result5 = str_str(haystack5, needle5)
    print(f"测试用例5: haystack=\"{haystack5}\", needle=\"{needle5}\"")
    print(f"预期输出: -1, 实际输出: {result5}")
    print()

    print("所有测试用例完成！")