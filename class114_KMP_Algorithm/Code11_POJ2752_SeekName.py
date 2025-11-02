#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
POJ 2752 Seek the Name, Seek the Fame

题目来源：POJ (北京大学在线评测系统)
题目链接：http://poj.org/problem?id=2752

题目描述：
给定一个字符串，找到所有既是前缀又是后缀的子串。
输出这些子串的长度，按升序排列。

示例：
输入："alala"
输出："a", "ala", "alala"，对应的长度为1, 3, 5

算法思路：
使用KMP算法的next数组来解决这个问题。
1. 构建字符串的next数组
2. 通过next[n-1]找到最长的既是前缀又是后缀的子串
3. 通过递归应用next函数，找到所有符合条件的子串

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


def find_all_prefix_suffix_lengths(s):
    """
    找到所有既是前缀又是后缀的子串的长度

    :param s: 输入字符串
    :return: 所有符合条件的子串长度，按升序排列
    """
    result = []

    # 边界条件处理
    if len(s) == 0:
        return result

    # 构建next数组
    next_array = build_next_array(s)

    # 通过next数组找到所有符合条件的长度
    pos = len(s) - 1
    while pos >= 0:
        if next_array[pos] > 0:
            result.append(next_array[pos])
            pos = next_array[pos] - 1
        else:
            pos -= 1

    # 添加整个字符串的长度
    result.append(len(s))

    # 按升序排列
    result.sort()

    return result


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    s1 = "alala"
    result1 = find_all_prefix_suffix_lengths(s1)
    print("测试用例1:")
    print(f"输入字符串: {s1}")
    print("输出长度:", " ".join(map(str, result1)))
    substrings1 = [s1[:length] for length in result1]
    print("对应子串:", ", ".join(f'"{substr}"' for substr in substrings1))
    print()

    # 测试用例2
    s2 = "abcabcab"
    result2 = find_all_prefix_suffix_lengths(s2)
    print("测试用例2:")
    print(f"输入字符串: {s2}")
    print("输出长度:", " ".join(map(str, result2)))
    substrings2 = [s2[:length] for length in result2]
    print("对应子串:", ", ".join(f'"{substr}"' for substr in substrings2))
    print()

    # 测试用例3
    s3 = "aaaa"
    result3 = find_all_prefix_suffix_lengths(s3)
    print("测试用例3:")
    print(f"输入字符串: {s3}")
    print("输出长度:", " ".join(map(str, result3)))
    substrings3 = [s3[:length] for length in result3]
    print("对应子串:", ", ".join(f'"{substr}"' for substr in substrings3))
    print()

    # 测试用例4
    s4 = "abcdef"
    result4 = find_all_prefix_suffix_lengths(s4)
    print("测试用例4:")
    print(f"输入字符串: {s4}")
    print("输出长度:", " ".join(map(str, result4)))
    substrings4 = [s4[:length] for length in result4]
    print("对应子串:", ", ".join(f'"{substr}"' for substr in substrings4))