#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
最长定差子序列 - LeetCode 1218
题目来源：https://leetcode.cn/problems/longest-arithmetic-subsequence-of-given-difference/
难度：中等
题目描述：给你一个整数数组 arr 和一个整数 difference，请你找出并返回 arr 中最长等差子序列的长度，
该子序列中相邻元素之间的差等于 difference。
子序列 是指在不改变其余元素顺序的情况下，通过删除一些元素或不删除任何元素而从 arr 派生出来的序列。

核心思路：
1. 这道题是LIS问题的变种，但更特殊，因为我们需要的是固定差值的等差数列子序列
2. 可以使用哈希表来优化动态规划的过程
3. 对于每个元素arr[i]，我们需要查找是否存在arr[i] - difference这个元素，如果存在，则当前元素可以接在它后面形成更长的等差数列

复杂度分析：
时间复杂度：O(n)，其中n是数组的长度，我们只需要遍历一次数组，每次查询和更新哈希表的操作都是O(1)
空间复杂度：O(n)，哈希表最多存储n个元素
"""

from typing import List, Dict
import time
import random


def longestSubsequence(arr: List[int], difference: int) -> int:
    """
    最优解法：使用哈希表优化动态规划
    
    参数:
        arr: 输入数组
        difference: 固定差值
    返回:
        最长等差子序列的长度
    """
    # 边界情况处理
    if not arr:
        return 0
    
    # 使用哈希表存储每个数字最后出现时的最长子序列长度
    # key: 数字值, value: 以该数字结尾的最长等差子序列长度
    dp: Dict[int, int] = {}
    
    max_length = 1  # 至少有一个元素
    
    # 遍历数组中的每个元素
    for num in arr:
        # 查找前驱元素：num - difference
        prev = num - difference
        # 如果前驱元素存在，则当前元素可以接在它后面形成更长的子序列
        # 否则，当前元素自身形成一个长度为1的子序列
        current_length = dp.get(prev, 0) + 1
        
        # 更新当前元素的最长子序列长度
        # 对于重复元素，我们保留最大的长度
        if num not in dp or current_length > dp[num]:
            dp[num] = current_length
        
        # 更新全局最大长度
        max_length = max(max_length, current_length)
    
    return max_length


def longestSubsequenceDP(arr: List[int], difference: int) -> int:
    """
    动态规划解法（未优化，用于对比）
    时间复杂度：O(n²)
    空间复杂度：O(n)
    
    参数:
        arr: 输入数组
        difference: 固定差值
    返回:
        最长等差子序列的长度
    """
    if not arr:
        return 0
    
    n = len(arr)
    dp = [1] * n  # 初始化每个元素长度为1
    
    max_length = 1
    
    # 填充dp数组
    for i in range(1, n):
        for j in range(i):
            if arr[i] - arr[j] == difference:
                dp[i] = max(dp[i], dp[j] + 1)
        max_length = max(max_length, dp[i])
    
    return max_length


def longestSubsequenceOptimized(arr: List[int], difference: int) -> int:
    """
    使用哈希表优化的进阶解法 - 考虑到可能有重复元素
    
    参数:
        arr: 输入数组
        difference: 固定差值
    返回:
        最长等差子序列的长度
    """
    if not arr:
        return 0
    
    # 使用哈希表记录每个数字最后出现时的最长子序列长度
    dp: Dict[int, int] = {}
    max_length = 1
    
    for num in arr:
        # 当前数字可以接在num - difference后面
        prev_length = dp.get(num - difference, 0)
        current_length = prev_length + 1
        
        # 如果当前数字已经在哈希表中，且之前记录的长度更大，则不更新
        if num not in dp or current_length > dp[num]:
            dp[num] = current_length
        
        max_length = max(max_length, current_length)
    
    return max_length


def runAllSolutionsTest(arr: List[int], difference: int):
    """
    运行所有解法的对比测试
    
    参数:
        arr: 输入数组
        difference: 固定差值
    """
    print(f"\n对比测试：{arr}，difference = {difference}")
    
    # 测试哈希表优化解法
    start_time = time.time()
    result1 = longestSubsequence(arr, difference)
    end_time = time.time()
    print(f"哈希表优化解法结果: {result1}")
    print(f"耗时: {(end_time - start_time) * 1_000_000:.2f} μs")
    
    # 测试优化进阶解法
    start_time = time.time()
    result3 = longestSubsequenceOptimized(arr, difference)
    end_time = time.time()
    print(f"进阶优化解法结果: {result3}")
    print(f"耗时: {(end_time - start_time) * 1_000_000:.2f} μs")
    
    # 对于小型数组，也测试O(n²)的DP解法
    if len(arr) <= 1000:  # 避免大数组导致超时
        start_time = time.time()
        result2 = longestSubsequenceDP(arr, difference)
        end_time = time.time()
        print(f"传统DP解法结果: {result2}")
        print(f"耗时: {(end_time - start_time) * 1_000_000:.2f} μs")
    else:
        print("数组长度较大，跳过传统DP解法测试")
    
    print("-" * 40)


def performanceTest(n: int, difference: int):
    """
    性能测试函数
    
    参数:
        n: 数组长度
        difference: 固定差值
    """
    # 生成随机测试数据
    arr = [random.randint(-5000, 5000) for _ in range(n)]
    
    print(f"\n性能测试：数组长度 = {n}")
    
    # 测试哈希表优化解法
    start_time = time.time()
    result1 = longestSubsequence(arr, difference)
    end_time = time.time()
    print(f"哈希表优化解法耗时: {(end_time - start_time) * 1000:.3f} ms, 结果: {result1}")
    
    # 对于小型数组，也测试O(n²)的DP解法
    if n <= 5000:  # 限制数组大小以避免超时
        try:
            start_time = time.time()
            result2 = longestSubsequenceDP(arr, difference)
            end_time = time.time()
            print(f"传统DP解法耗时: {(end_time - start_time) * 1000:.3f} ms, 结果: {result2}")
        except Exception:
            print("传统DP解法执行超时")
    else:
        print("数组长度超过阈值，跳过传统DP解法性能测试")


def testCase():
    """
    测试用例
    """
    # 测试用例1
    arr1 = [1, 2, 3, 4]
    difference1 = 1
    print("测试用例1：")
    print(f"输入数组: {arr1}, difference: {difference1}")
    print(f"结果: {longestSubsequence(arr1, difference1)}，预期: 4")
    print()
    
    # 测试用例2
    arr2 = [1, 3, 5, 7]
    difference2 = 1
    print("测试用例2：")
    print(f"输入数组: {arr2}, difference: {difference2}")
    print(f"结果: {longestSubsequence(arr2, difference2)}，预期: 1")
    print()
    
    # 测试用例3
    arr3 = [1, 5, 7, 8, 5, 3, 4, 2, 1]
    difference3 = -2
    print("测试用例3：")
    print(f"输入数组: {arr3}, difference: {difference3}")
    print(f"结果: {longestSubsequence(arr3, difference3)}，预期: 4")
    print()
    
    # 测试用例4：边界情况
    arr4 = [1]
    difference4 = 0
    print("测试用例4：")
    print(f"输入数组: {arr4}, difference: {difference4}")
    print(f"结果: {longestSubsequence(arr4, difference4)}，预期: 1")
    print()
    
    # 测试用例5：负数差值
    arr5 = [3, 0, -3, 4, -4, 7, 6]
    difference5 = 3
    print("测试用例5：")
    print(f"输入数组: {arr5}, difference: {difference5}")
    print(f"结果: {longestSubsequence(arr5, difference5)}，预期: 2")
    
    # 运行所有解法的对比测试
    runAllSolutionsTest(arr1, difference1)
    runAllSolutionsTest(arr2, difference2)
    runAllSolutionsTest(arr3, difference3)
    runAllSolutionsTest(arr4, difference4)
    runAllSolutionsTest(arr5, difference5)
    
    # 性能测试
    print("性能测试:")
    print("-" * 40)
    performanceTest(1000, 5)
    performanceTest(10000, 5)
    
    # 特殊测试用例：所有元素相同
    print("\n特殊测试用例：所有元素相同")
    arr_same = [5, 5, 5, 5, 5]
    diff_same = 0
    print(f"输入数组: {arr_same}, difference: {diff_same}")
    print(f"结果: {longestSubsequence(arr_same, diff_same)}")
    
    # 特殊测试用例：严格递减序列，负差值
    print("\n特殊测试用例：严格递减序列，负差值")
    arr_decreasing = [10, 8, 6, 4, 2]
    diff_decreasing = -2
    print(f"输入数组: {arr_decreasing}, difference: {diff_decreasing}")
    print(f"结果: {longestSubsequence(arr_decreasing, diff_decreasing)}")


if __name__ == "__main__":
    """
    主函数入口
    """
    testCase()