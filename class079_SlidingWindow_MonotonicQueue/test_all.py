#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
测试所有滑动窗口与单调队列相关的Python代码
"""

import sys
from typing import List
from collections import deque

# 重定向标准输出用于测试
original_stdout = sys.stdout

def test_code01():
    """测试滑动窗口最大值"""
    print("测试 Code01_SlidingWindowMaximum.py")
    
    # 导入函数
    from Code01_SlidingWindowMaximum import Solution
    
    # 测试用例
    solution = Solution()
    nums = [1, 3, -1, -3, 5, 3, 6, 7]
    k = 3
    expected = [3, 3, 5, 5, 6, 7]
    result = solution.maxSlidingWindow(nums, k)
    
    print(f"输入: nums = {nums}, k = {k}")
    print(f"期望输出: {expected}")
    print(f"实际输出: {result}")
    print(f"测试结果: {'通过' if result == expected else '失败'}")
    print()

def test_code02():
    """测试绝对差不超过限制的最长连续子数组"""
    print("测试 Code02_LongestSubarrayAbsoluteLimit.py")
    
    # 导入函数
    from Code02_LongestSubarrayAbsoluteLimit import Solution
    
    # 测试用例
    solution = Solution()
    nums = [8, 2, 4, 7]
    limit = 4
    expected = 2
    result = solution.longestSubarray(nums, limit)
    
    print(f"输入: nums = {nums}, limit = {limit}")
    print(f"期望输出: {expected}")
    print(f"实际输出: {result}")
    print(f"测试结果: {'通过' if result == expected else '失败'}")
    print()

def test_code04():
    """测试最小覆盖子串"""
    print("测试 Code04_MinimumWindowSubstring.py")
    
    # 导入函数
    from Code04_MinimumWindowSubstring import Solution
    
    # 测试用例
    solution = Solution()
    s = "ADOBECODEBANC"
    t = "ABC"
    expected = "BANC"
    result = solution.minWindow(s, t)
    
    print(f"输入: s = {s}, t = {t}")
    print(f"期望输出: {expected}")
    print(f"实际输出: {result}")
    print(f"测试结果: {'通过' if result == expected else '失败'}")
    print()

def test_code05():
    """测试滑动窗口中位数"""
    print("测试 Code05_SlidingWindowMedian.py")
    
    # 导入函数
    from Code05_SlidingWindowMedian import Solution
    
    # 测试用例
    solution = Solution()
    nums = [1, 3, -1, -3, 5, 3, 6, 7]
    k = 3
    # 正确的期望结果应该是每个窗口的中位数：
    # [1, 3, -1] -> 1 (中位数)
    # [3, -1, -3] -> -1 (中位数)
    # [-1, -3, 5] -> -1 (中位数)
    # [-3, 5, 3] -> 3 (中位数)
    # [5, 3, 6] -> 5 (中位数)
    # [3, 6, 7] -> 6 (中位数)
    expected = [1.0, -1.0, -1.0, 3.0, 5.0, 6.0]
    result = solution.medianSlidingWindow(nums, k)
    
    print(f"输入: nums = {nums}, k = {k}")
    print(f"期望输出: {expected}")
    print(f"实际输出: {result}")
    print(f"测试结果: {'通过' if result == expected else '失败'}")
    print()

def test_poj2823():
    """测试POJ2823"""
    print("测试 POJ2823_SlidingWindow.py")
    
    # 导入函数
    from POJ2823_SlidingWindow import get_max, get_min
    
    # 测试用例
    arr = [1, 3, -1, -3, 5, 3, 6, 7]
    n = len(arr)
    k = 3
    
    expected_min = [-1, -3, -3, -3, 3, 3]
    expected_max = [3, 3, 5, 5, 6, 7]
    
    result_min = get_min(arr, n, k)
    result_max = get_max(arr, n, k)
    
    print(f"输入: arr = {arr}, k = {k}")
    print(f"期望最小值: {expected_min}")
    print(f"实际最小值: {result_min}")
    print(f"期望最大值: {expected_max}")
    print(f"实际最大值: {result_max}")
    min_test = result_min == expected_min
    max_test = result_max == expected_max
    print(f"最小值测试结果: {'通过' if min_test else '失败'}")
    print(f"最大值测试结果: {'通过' if max_test else '失败'}")
    print(f"整体测试结果: {'通过' if min_test and max_test else '失败'}")
    print()

def test_luogu_p1886():
    """测试洛谷P1886"""
    print("测试 LuoguP1886_SlidingWindow.py")
    
    # 导入函数
    from LuoguP1886_SlidingWindow import get_max, get_min
    
    # 测试用例
    arr = [1, 3, -1, -3, 5, 3, 6, 7]
    n = len(arr)
    k = 3
    
    expected_min = [-1, -3, -3, -3, 3, 3]
    expected_max = [3, 3, 5, 5, 6, 7]
    
    result_min = get_min(arr, n, k)
    result_max = get_max(arr, n, k)
    
    print(f"输入: arr = {arr}, k = {k}")
    print(f"期望最小值: {expected_min}")
    print(f"实际最小值: {result_min}")
    print(f"期望最大值: {expected_max}")
    print(f"实际最大值: {result_max}")
    min_test = result_min == expected_min
    max_test = result_max == expected_max
    print(f"最小值测试结果: {'通过' if min_test else '失败'}")
    print(f"最大值测试结果: {'通过' if max_test else '失败'}")
    print(f"整体测试结果: {'通过' if min_test and max_test else '失败'}")
    print()

def test_code03():
    """测试接取落水的最小花盆"""
    print("测试 Code03_FallingWaterSmallestFlowerPot.py")
    
    # 导入函数
    from Code03_FallingWaterSmallestFlowerPot import compute, push, pop, ok
    
    # 测试用例
    arr = [[6, 3], [2, 4], [4, 10], [12, 15]]
    n = 4
    d = 5
    
    # 由于这是一个交互式题目，我们只测试辅助函数
    max_deque = deque()
    min_deque = deque()
    
    # 测试push函数
    push(max_deque, min_deque, arr, 0)
    push(max_deque, min_deque, arr, 1)
    
    print(f"测试push函数:")
    print(f"max_deque: {list(max_deque)}")
    print(f"min_deque: {list(min_deque)}")
    
    # 测试ok函数
    result_ok = ok(max_deque, min_deque, arr, d)
    print(f"测试ok函数，结果: {result_ok}")
    
    # 测试pop函数
    pop(max_deque, min_deque, 0)
    print(f"测试pop函数后:")
    print(f"max_deque: {list(max_deque)}")
    print(f"min_deque: {list(min_deque)}")
    
    print("Code03辅助函数测试完成")
    print()

if __name__ == "__main__":
    print("开始测试所有滑动窗口与单调队列相关的Python代码")
    print("=" * 50)
    
    try:
        test_code01()
        test_code02()
        test_code04()
        test_code05()
        test_poj2823()
        test_luogu_p1886()
        test_code03()
        
        print("=" * 50)
        print("所有测试完成!")
        
    except Exception as e:
        print(f"测试过程中出现错误: {e}")
        import traceback
        traceback.print_exc()