#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Codeforces 126B Password

题目来源：Codeforces
题目链接：https://codeforces.com/problemset/problem/126/B

题目描述：
给定一个字符串s，找到一个子串，它既是前缀又是后缀，同时在字符串中间也出现过。
如果有多个这样的子串，输出最长的那个。如果没有这样的子串，输出"Just a legend"。

示例：
输入："fixprefixsuffix"
输出："fix"

输入："abcdabc"
输出："Just a legend"

算法思路：
使用KMP算法的next数组来解决这个问题。
1. 构建字符串的next数组
2. 通过next[n-1]找到最长的既是前缀又是后缀的子串
3. 检查这个子串是否在中间出现过
4. 如果没有，则通过next数组继续查找更短的候选子串

时间复杂度：O(n)
空间复杂度：O(n)
"""


def build_next_array(s):
    """
    构建KMP算法的next数组（部分匹配表）

    next[i]表示s[0...i]子串的最长相等前后缀的长度

    :param s: 输入字符串
    :return: next数组
    """
    length = len(s)
    next_array = [0] * length

    # 初始化
    next_array[0] = 0
    prefix_len = 0  # 当前最长相等前后缀的长度
    i = 1  # 当前处理的位置

    # 从位置1开始处理
    while i < length:
        # 如果当前字符匹配，可以延长相等前后缀
        if s[i] == s[prefix_len]:
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


def is_substring_present(s, length, next_array):
    """
    检查指定长度的前缀是否在字符串中间出现过

    :param s: 字符串
    :param length: 子串长度
    :param next_array: next数组
    :return: 是否在中间出现过
    """
    # 在next数组中查找是否有等于length的值（除了最后一个位置）
    for i in range(len(s) - 1):
        if next_array[i] == length:
            return True
    return False


def find_password(s):
    """
    找到符合条件的最长子串

    :param s: 输入字符串
    :return: 符合条件的最长子串，如果不存在则返回"Just a legend"
    """
    # 边界条件处理
    if len(s) <= 2:
        return "Just a legend"

    # 构建next数组
    next_array = build_next_array(s)

    # 从最长的候选子串开始检查
    candidate_length = next_array[len(s) - 1]

    # 检查是否有符合条件的子串
    while candidate_length > 0:
        # 检查这个长度的子串是否在中间出现过
        if is_substring_present(s, candidate_length, next_array):
            return s[:candidate_length]
        # 尝试更短的候选子串
        candidate_length = next_array[candidate_length - 1]

    return "Just a legend"


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    s1 = "fixprefixsuffix"
    result1 = find_password(s1)
    print("测试用例1:")
    print(f"输入字符串: {s1}")
    print(f"输出: {result1}")
    print("预期输出: fix\n")

    # 测试用例2
    s2 = "abcdabc"
    result2 = find_password(s2)
    print("测试用例2:")
    print(f"输入字符串: {s2}")
    print(f"输出: {result2}")
    print("预期输出: Just a legend\n")

    # 测试用例3
    s3 = "abcabcabcabc"
    result3 = find_password(s3)
    print("测试用例3:")
    print(f"输入字符串: {s3}")
    print(f"输出: {result3}")
    print("预期输出: abcabcabc\n")

    # 测试用例4
    s4 = "aaaa"
    result4 = find_password(s4)
    print("测试用例4:")
    print(f"输入字符串: {s4}")
    print(f"输出: {result4}")
    print("预期输出: aaa\n")

    # 测试用例5
    s5 = "abc"
    result5 = find_password(s5)
    print("测试用例5:")
    print(f"输入字符串: {s5}")
    print(f"输出: {result5}")
    print("预期输出: Just a legend")