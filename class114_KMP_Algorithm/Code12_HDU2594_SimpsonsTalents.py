#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
HDU 2594 Simpsons' Hidden Talents

题目来源：HDU (杭州电子科技大学在线评测系统)
题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=2594

题目描述：
给定两个字符串s1和s2，找到最长的字符串s，使得s既是s1的后缀，又是s2的前缀。
输出这个字符串s及其长度。如果不存在这样的字符串，输出0。

示例：
输入：s1 = "abcabc", s2 = "bcabca"
输出："bca" 3

算法思路：
使用KMP算法的思想来解决这个问题。
1. 将s1和s2连接成一个新字符串，中间用特殊字符分隔
2. 构建新字符串的next数组
3. 通过分析next数组找到最长的公共前后缀

时间复杂度：O(n + m)，其中n是s1的长度，m是s2的长度
空间复杂度：O(n + m)
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


def find_longest_common_suffix_prefix(s1, s2):
    """
    找到s1的后缀和s2的前缀的最长公共部分

    :param s1: 第一个字符串
    :param s2: 第二个字符串
    :return: 最长公共部分及其长度
    """
    # 边界条件处理
    if not s1 or not s2:
        return ["0"]

    # 构造新字符串：s1 + "#" + s2
    # 使用特殊字符"#"来分隔两个字符串，确保不会产生虚假匹配
    combined = s1 + "#" + s2

    # 构建next数组
    next_array = build_next_array(combined)

    # 最长公共部分的长度就是next[n-1]
    common_length = next_array[len(combined) - 1]

    # 确保公共部分不会超过任何一个字符串的长度
    common_length = min(common_length, min(len(s1), len(s2)))

    if common_length == 0:
        return ["0"]

    # 返回公共部分及其长度
    common_part = s1[len(s1) - common_length:]
    return [common_part, str(common_length)]


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    s1_1 = "abcabc"
    s2_1 = "bcabca"
    result1 = find_longest_common_suffix_prefix(s1_1, s2_1)
    print("测试用例1:")
    print(f"s1: {s1_1}")
    print(f"s2: {s2_1}")
    if len(result1) == 1:
        print(f"输出: {result1[0]}")
    else:
        print(f"输出: {result1[0]} {result1[1]}")
    print("预期输出: bca 3\n")

    # 测试用例2
    s1_2 = "hello"
    s2_2 = "world"
    result2 = find_longest_common_suffix_prefix(s1_2, s2_2)
    print("测试用例2:")
    print(f"s1: {s1_2}")
    print(f"s2: {s2_2}")
    if len(result2) == 1:
        print(f"输出: {result2[0]}")
    else:
        print(f"输出: {result2[0]} {result2[1]}")
    print("预期输出: 0\n")

    # 测试用例3
    s1_3 = "abc"
    s2_3 = "abcdef"
    result3 = find_longest_common_suffix_prefix(s1_3, s2_3)
    print("测试用例3:")
    print(f"s1: {s1_3}")
    print(f"s2: {s2_3}")
    if len(result3) == 1:
        print(f"输出: {result3[0]}")
    else:
        print(f"输出: {result3[0]} {result3[1]}")
    print("预期输出: abc 3\n")

    # 测试用例4
    s1_4 = "abcdef"
    s2_4 = "def"
    result4 = find_longest_common_suffix_prefix(s1_4, s2_4)
    print("测试用例4:")
    print(f"s1: {s1_4}")
    print(f"s2: {s2_4}")
    if len(result4) == 1:
        print(f"输出: {result4[0]}")
    else:
        print(f"输出: {result4[0]} {result4[1]}")
    print("预期输出: def 3")