#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
SPOJ PERIOD - Period

题目来源：SPOJ (Sphere Online Judge)
题目链接：https://www.spoj.com/problems/PERIOD/

题目描述：
对于给定字符串S的每个前缀，我们需要知道它是否是周期字符串。
也就是说，对于每个i (2 <= i <= N) 我们要找到满足条件的最小的K (K > 1)，
使得长度为i的前缀可以写成某个字符串重复K次的形式。
如果不存在这样的K，则输出0。

例如：对于字符串 "aabaab"，长度为6的前缀 "aabaab" 可以写成 "aab" 重复2次，
所以K=2。

算法思路：
使用KMP算法的next数组来解决这个问题。
对于长度为i的前缀，如果i % (i - next[i]) == 0且next[i] > 0，
则该前缀是周期字符串，周期长度为i - next[i]，周期数为i / (i - next[i])。

时间复杂度：O(N)，其中N是字符串长度
空间复杂度：O(N)，用于存储next数组

KMP算法的核心思想是利用已经匹配过的信息，避免不必要的字符比较。
next数组（部分匹配表）记录了模式串中每个位置的最长相等前后缀长度，
这使得在匹配失败时，可以知道模式串应该向右移动多少位，而不是简单地回退一位。
"""


def build_next_array(pattern):
    """
    构建KMP算法的next数组（部分匹配表）

    next[i]表示pattern[0...i-1]子串的最长相等前后缀的长度

    算法思路：
    1. 初始化next[0] = 0
    2. 使用双指针i和j，i指向当前处理的位置，j指向前缀的末尾
    3. 如果pattern[i] == pattern[j]，说明前缀和后缀可以延长，next[i] = j + 1
    4. 如果pattern[i] != pattern[j]，需要回退j指针到next[j-1]，直到匹配或j=0

    :param pattern: 模式串
    :return: next数组
    """
    n = len(pattern)
    next_array = [0] * n  # 修改为长度为n

    # 初始化
    next_array[0] = 0
    prefix_len = 0  # 当前最长相等前后缀的长度
    i = 1  # 当前处理的位置

    # 从位置1开始处理
    while i < n:
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


def calculate_periods(s):
    """
    计算字符串每个前缀的周期

    :param s: 输入字符串
    :return: 每个前缀的周期数数组
    """
    n = len(s)
    next_array = build_next_array(s)
    periods = [0] * (n + 1)

    for i in range(2, n + 1):
        len_val = i - next_array[i - 1]  # 周期长度，注意索引是i-1
        # 添加条件：周期长度小于前缀长度且前缀长度能被周期长度整除
        if len_val < i and i % len_val == 0:
            periods[i] = i // len_val  # 周期数 = 总长度 // 周期长度
        else:
            periods[i] = 0  # 不是周期字符串
    
    return periods

def verify_period(s, length, period):
    """
    验证周期数计算是否正确的辅助方法
    
    参数:
        s (str): 输入字符串
        length (int): 前缀长度
        period (int): 周期数
    
    返回:
        bool: 是否确实可以通过重复period次某个子串得到该前缀
    """
    if period == 0:
        return True  # 非周期字符串
    
    # 周期长度应该是前缀长度除以周期数
    cycle_length = length // period
    sub_str = s[:cycle_length]
    # 构建重复period次的字符串并比较
    repeated_str = sub_str * period
    
    result = repeated_str == s[:length]
    print(f"  调试信息: 前缀='{s[:length]}', 周期数={period}, 周期长度={cycle_length}, 子串='{sub_str}', 重复串='{repeated_str}', 匹配结果={result}")
    return result

# 测试方法
def test_calculate_periods():
    """
    测试calculate_periods函数的正确性
    """
    # 测试用例1: "aabaab" 预期结果：对于长度为6的前缀，K=2
    s1 = "aabaab"
    print("测试用例1:")
    print(f"输入字符串: {s1}")
    periods1 = calculate_periods(s1)
    for i in range(2, len(s1) + 1):
        print(f"前缀长度 {i}: {periods1[i]}")
        # 验证结果正确性
        assert verify_period(s1, i, periods1[i]), "测试用例1失败！"
    print()
    
    # 测试用例2: "abababab" 预期结果：每个前缀的周期数都是其长度/2
    s2 = "abababab"
    print("测试用例2:")
    print(f"输入字符串: {s2}")
    periods2 = calculate_periods(s2)
    for i in range(2, len(s2) + 1):
        print(f"前缀长度 {i}: {periods2[i]}")
        # 验证结果正确性
        assert verify_period(s2, i, periods2[i]), "测试用例2失败！"
    print()
    
    # 测试用例3: "abcdef" 预期结果：所有前缀都不是周期字符串，输出0
    s3 = "abcdef"
    print("测试用例3:")
    print(f"输入字符串: {s3}")
    periods3 = calculate_periods(s3)
    for i in range(2, len(s3) + 1):
        print(f"前缀长度 {i}: {periods3[i]}")
        # 验证结果正确性
        assert verify_period(s3, i, periods3[i]), "测试用例3失败！"
    print()
    
    # 测试用例4: "aaaaa" 预期结果：每个前缀都有周期，周期数等于其长度
    s4 = "aaaaa"
    print("测试用例4:")
    print(f"输入字符串: {s4}")
    periods4 = calculate_periods(s4)
    for i in range(2, len(s4) + 1):
        print(f"前缀长度 {i}: {periods4[i]}")
        # 验证结果正确性
        assert verify_period(s4, i, periods4[i]), "测试用例4失败！"

if __name__ == "__main__":
    # 运行测试用例
    test_calculate_periods()
    
    # 极端测试用例
    print("极端测试用例:")
    # 空字符串
    empty_str = ""
    print(f"空字符串: {empty_str}")
    periods_empty = calculate_periods(empty_str)
    print(f"结果: {periods_empty}")
    
    # 单个字符
    single_char = "a"
    print(f"单个字符: {single_char}")
    periods_single = calculate_periods(single_char)
    print(f"结果: {periods_single[1] if len(periods_single) > 1 else '无'}")
    
    # 长字符串性能测试
    long_str = "a" * 1000
    print(f"长字符串 (1000个'a')")
    import time
    start_time = time.time()
    periods_long = calculate_periods(long_str)
    end_time = time.time()
    print(f"执行时间: {end_time - start_time:.6f} 秒")
    print(f"长度为1000的前缀周期数: {periods_long[1000]}")