#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
最长的斐波那契子序列的长度 - LeetCode 873
题目来源：https://leetcode.cn/problems/length-of-longest-fibonacci-subsequence/
难度：中等
题目描述：如果序列 X_1, X_2, ..., X_n 满足下列条件，就说它是斐波那契式的：
    n >= 3
    对于所有 i + 2 <= n，都有 X_i + X_{i+1} = X_{i+2}
给定一个严格递增的正整数数组形成序列 arr ，找到 arr 中最长的斐波那契式的子序列的长度。
如果没有这样的子序列，返回 0。
子序列是指从原数组中删除一些元素（可以删除任何元素，包括零个元素），剩下的元素保持原来的顺序而不改变。

核心思路：
1. 这道题是LIS问题的变种，我们需要找到满足斐波那契关系的最长子序列
2. 使用动态规划+哈希表的方法：dp[i][j] 表示以arr[i]和arr[j]结尾的最长斐波那契子序列的长度
3. 对于每对(i,j)，我们查找arr[j]-arr[i]是否存在于数组中且索引小于i，如果存在则可以形成更长的子序列

复杂度分析：
时间复杂度：O(n²)，其中n是数组的长度，我们需要填充大小为n×n的dp数组
空间复杂度：O(n²)，用于存储dp数组，以及O(n)用于哈希表存储值到索引的映射
"""

from typing import List, Dict, Set
import time
import random


def lenLongestFibSubseq(arr: List[int]) -> int:
    """
    最优解法：动态规划+哈希表
    
    参数:
        arr: 严格递增的正整数数组
    返回:
        最长斐波那契子序列的长度，如果不存在返回0
    """
    # 边界情况处理
    if len(arr) < 3:
        return 0
    
    n = len(arr)
    # 创建哈希表，存储值到索引的映射，用于快速查找
    value_to_index: Dict[int, int] = {}
    for i in range(n):
        value_to_index[arr[i]] = i
    
    # dp[i][j] 表示以arr[i]和arr[j]结尾的最长斐波那契子序列的长度
    # 初始化为2，表示至少有两个元素
    dp = [[2] * n for _ in range(n)]
    max_length = 0
    
    # 填充dp数组
    for j in range(n):
        for k in range(j + 1, n):
            # 查找潜在的第一个元素 arr[i] = arr[k] - arr[j]
            target = arr[k] - arr[j]
            # 确保target存在且严格小于arr[j]（因为数组严格递增）
            if target < arr[j] and target in value_to_index:
                i = value_to_index[target]
                # 更新dp[j][k]
                dp[j][k] = dp[i][j] + 1
                # 更新最大长度
                max_length = max(max_length, dp[j][k])
    
    # 如果maxLength至少为3，则返回，否则返回0
    return max_length if max_length >= 3 else 0


def lenLongestFibSubseqAlternative(arr: List[int]) -> int:
    """
    另一种动态规划解法，使用不同的遍历顺序
    
    参数:
        arr: 严格递增的正整数数组
    返回:
        最长斐波那契子序列的长度，如果不存在返回0
    """
    if len(arr) < 3:
        return 0
    
    n = len(arr)
    map_: Dict[int, int] = {}
    for i in range(n):
        map_[arr[i]] = i
    
    dp = [[0] * n for _ in range(n)]
    max_length = 0
    
    # 遍历所有可能的三元组 (i,j,k) 其中 i < j < k
    for k in range(2, n):
        for j in range(1, k):
            target = arr[k] - arr[j]
            if target in map_ and map_[target] < j:
                i = map_[target]
                dp[j][k] = dp[i][j] + 1
                max_length = max(max_length, dp[j][k])
    
    # 如果存在斐波那契子序列，长度至少为3
    return max_length + 2 if max_length > 0 else 0


def lenLongestFibSubseqBruteForce(arr: List[int]) -> int:
    """
    暴力解法（仅供对比，时间复杂度高）
    
    参数:
        arr: 严格递增的正整数数组
    返回:
        最长斐波那契子序列的长度，如果不存在返回0
    """
    if len(arr) < 3:
        return 0
    
    n = len(arr)
    set_: Set[int] = set(arr)
    
    max_length = 0
    
    # 枚举所有可能的前两个元素
    for i in range(n):
        for j in range(i + 1, n):
            a = arr[i]
            b = arr[j]
            length = 2
            next_val = a + b
            
            # 检查是否可以形成更长的斐波那契序列
            while next_val in set_:
                a = b
                b = next_val
                next_val = a + b
                length += 1
            
            if length >= 3:
                max_length = max(max_length, length)
    
    return max_length


def runAllSolutionsTest(arr: List[int]):
    """
    运行所有解法的对比测试
    
    参数:
        arr: 输入数组
    """
    print(f"\n对比测试：{arr}")
    
    # 测试动态规划+哈希表解法
    start_time = time.time()
    result1 = lenLongestFibSubseq(arr)
    end_time = time.time()
    print(f"动态规划+哈希表解法结果: {result1}")
    print(f"耗时: {(end_time - start_time) * 1_000_000:.2f} μs")
    
    # 测试另一种动态规划解法
    start_time = time.time()
    result2 = lenLongestFibSubseqAlternative(arr)
    end_time = time.time()
    print(f"另一种动态规划解法结果: {result2}")
    print(f"耗时: {(end_time - start_time) * 1_000_000:.2f} μs")
    
    # 对于小型数组，也测试暴力解法
    if len(arr) <= 20:  # 避免大数组导致超时
        start_time = time.time()
        result3 = lenLongestFibSubseqBruteForce(arr)
        end_time = time.time()
        print(f"暴力解法结果: {result3}")
        print(f"耗时: {(end_time - start_time) * 1_000_000:.2f} μs")
    else:
        print("数组长度较大，跳过暴力解法测试")
    
    print("-" * 40)


def performanceTest(n: int):
    """
    性能测试函数
    
    参数:
        n: 数组长度
    """
    # 生成严格递增的随机测试数据
    arr = [1]
    for _ in range(n-1):
        arr.append(arr[-1] + random.randint(1, 10))  # 确保严格递增
    
    print(f"\n性能测试：数组长度 = {n}")
    
    # 测试动态规划+哈希表解法
    start_time = time.time()
    result1 = lenLongestFibSubseq(arr)
    end_time = time.time()
    print(f"动态规划+哈希表解法耗时: {(end_time - start_time) * 1000:.3f} ms, 结果: {result1}")


def testCase():
    """
    测试用例
    """
    # 测试用例1
    arr1 = [1, 2, 3, 4, 5, 6, 7, 8]
    print("测试用例1：")
    print(f"输入数组: {arr1}")
    print(f"结果: {lenLongestFibSubseq(arr1)}，预期: 5")
    print()
    
    # 测试用例2
    arr2 = [1, 3, 7, 11, 12, 14, 18]
    print("测试用例2：")
    print(f"输入数组: {arr2}")
    print(f"结果: {lenLongestFibSubseq(arr2)}，预期: 3")
    print()
    
    # 测试用例3：边界情况
    arr3 = [1, 2, 3]
    print("测试用例3：")
    print(f"输入数组: {arr3}")
    print(f"结果: {lenLongestFibSubseq(arr3)}，预期: 3")
    print()
    
    # 测试用例4：没有斐波那契子序列
    arr4 = [1, 4, 5]
    print("测试用例4：")
    print(f"输入数组: {arr4}")
    print(f"结果: {lenLongestFibSubseq(arr4)}，预期: 0")
    
    # 运行所有解法的对比测试
    runAllSolutionsTest(arr1)
    runAllSolutionsTest(arr2)
    runAllSolutionsTest(arr3)
    runAllSolutionsTest(arr4)
    
    # 性能测试
    print("性能测试:")
    print("-" * 40)
    performanceTest(100)
    performanceTest(200)
    
    # 特殊测试用例：较大的斐波那契序列
    print("\n特殊测试用例：较大的斐波那契序列")
    arr_fib = [1, 2, 3, 5, 8, 13, 21, 34, 55]
    print(f"输入数组: {arr_fib}")
    print(f"结果: {lenLongestFibSubseq(arr_fib)}")


if __name__ == "__main__":
    """
    主函数入口
    """
    testCase()