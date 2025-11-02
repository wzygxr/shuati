#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
最长等差数列 - LeetCode 1027
题目来源：https://leetcode.cn/problems/longest-arithmetic-subsequence/
难度：中等
题目描述：给你一个整数数组 nums，返回 nums 中最长等差子序列的长度。
注意：子序列是指从数组中删除一些元素（可以不删除任何元素）而不改变其余元素的顺序得到的序列。
等差子序列是指元素之间的差值都相等的序列。

核心思路：
1. 这道题是LIS问题的变种，我们需要找到具有相同差值的最长子序列
2. 使用动态规划+哈希表的方法：dp[i][d] 表示以nums[i]结尾且公差为d的最长等差数列长度
3. 对于每个元素nums[i]，遍历之前的所有元素nums[j]，计算差值d = nums[i] - nums[j]，并更新dp[i][d]

复杂度分析：
时间复杂度：O(n²)，其中n是数组的长度，对于每个元素，我们需要遍历之前的所有元素
空间复杂度：O(n²)，最坏情况下，每个元素对应的不同公差数量接近n
"""

from typing import List, Dict
import time
import random


def longestArithSeqLength(nums: List[int]) -> int:
    """
    最优解法：动态规划+哈希表
    
    参数:
        nums: 整数数组
    返回:
        最长等差子序列的长度
    """
    # 边界情况处理
    if len(nums) <= 1:
        return len(nums)
    
    n = len(nums)
    max_length = 2  # 至少有两个元素时，长度至少为2
    
    # 使用数组的字典来存储每个位置的公差对应的最长长度
    # dp[i] 表示以nums[i]结尾的所有可能公差对应的最长等差子序列长度
    dp: List[Dict[int, int]] = [{} for _ in range(n)]
    
    # 填充dp数组
    for i in range(1, n):
        for j in range(i):
            # 计算公差
            diff = nums[i] - nums[j]
            
            # 如果dp[j]中存在公差为diff的记录，则dp[i][diff] = dp[j][diff] + 1
            # 否则，dp[i][diff] = 2（至少有nums[j]和nums[i]两个元素）
            prev_length = dp[j].get(diff, 1)
            dp[i][diff] = prev_length + 1
            
            # 更新最大长度
            max_length = max(max_length, dp[i][diff])
    
    return max_length


def longestArithSeqLength2(nums: List[int]) -> int:
    """
    另一种实现方式，使用列表的字典（更简洁的版本）
    
    参数:
        nums: 整数数组
    返回:
        最长等差子序列的长度
    """
    if len(nums) <= 1:
        return len(nums)
    
    n = len(nums)
    max_length = 2
    
    # dp[i] 记录以nums[i]结尾的所有可能公差及其对应的最长序列长度
    dp: List[Dict[int, int]] = [{} for _ in range(n)]
    
    for i in range(n):
        for j in range(i):
            diff = nums[i] - nums[j]
            # 以nums[i]结尾且公差为diff的最长序列长度 = 
            # 以nums[j]结尾且公差为diff的最长序列长度 + 1
            dp[i][diff] = dp[j].get(diff, 1) + 1
            max_length = max(max_length, dp[i][diff])
    
    return max_length


def longestArithSeqLengthBruteForce(nums: List[int]) -> int:
    """
    暴力解法优化版：使用哈希表记录元素出现的位置
    
    参数:
        nums: 整数数组
    返回:
        最长等差子序列的长度
    """
    if len(nums) <= 2:
        return len(nums)
    
    n = len(nums)
    max_length = 2
    
    # 使用哈希表存储每个值及其出现的索引列表
    value_to_indices: Dict[int, List[int]] = {}
    for i in range(n):
        if nums[i] not in value_to_indices:
            value_to_indices[nums[i]] = []
        value_to_indices[nums[i]].append(i)
    
    # 枚举所有可能的前两个元素
    for i in range(n):
        for j in range(i + 1, j + 1 if n > 20 else n):  # 对于大数组限制j的范围
            prev = nums[i]
            curr = nums[j]
            diff = curr - prev
            next_val = curr + diff
            length = 2
            current_j = j
            
            # 查找下一个元素
            while next_val in value_to_indices:
                # 找到在current_j之后出现的next_val
                found = False
                for idx in value_to_indices[next_val]:
                    if idx > current_j:
                        current_j = idx  # 更新current_j为下一个元素的索引
                        prev = curr
                        curr = next_val
                        next_val = curr + diff
                        length += 1
                        found = True
                        break
                
                if not found:
                    break
            
            max_length = max(max_length, length)
    
    return max_length


def runAllSolutionsTest(nums: List[int]):
    """
    运行所有解法的对比测试
    
    参数:
        nums: 输入数组
    """
    print(f"\n对比测试：{nums}")
    
    # 测试动态规划+哈希表解法
    start_time = time.time()
    result1 = longestArithSeqLength(nums)
    end_time = time.time()
    print(f"动态规划+哈希表解法结果: {result1}")
    print(f"耗时: {(end_time - start_time) * 1_000_000:.2f} μs")
    
    # 测试另一种实现方式
    start_time = time.time()
    result2 = longestArithSeqLength2(nums)
    end_time = time.time()
    print(f"另一种实现方式结果: {result2}")
    print(f"耗时: {(end_time - start_time) * 1_000_000:.2f} μs")
    
    # 对于小型数组，也测试暴力优化解法
    if len(nums) <= 20:  # 避免大数组导致超时
        start_time = time.time()
        result3 = longestArithSeqLengthBruteForce(nums)
        end_time = time.time()
        print(f"暴力优化解法结果: {result3}")
        print(f"耗时: {(end_time - start_time) * 1_000_000:.2f} μs")
    else:
        print("数组长度较大，跳过暴力优化解法测试")
    
    print("-" * 40)


def performanceTest(n: int):
    """
    性能测试函数
    
    参数:
        n: 数组长度
    """
    # 生成随机测试数据
    nums = [random.randint(0, 1000) for _ in range(n)]
    
    print(f"\n性能测试：数组长度 = {n}")
    
    # 测试动态规划+哈希表解法
    start_time = time.time()
    result1 = longestArithSeqLength(nums)
    end_time = time.time()
    print(f"动态规划+哈希表解法耗时: {(end_time - start_time) * 1000:.3f} ms, 结果: {result1}")


def testCase():
    """
    测试用例
    """
    # 测试用例1
    arr1 = [3, 6, 9, 12]
    print("测试用例1：")
    print(f"输入数组: {arr1}")
    print(f"结果: {longestArithSeqLength(arr1)}，预期: 4")
    print()
    
    # 测试用例2
    arr2 = [9, 4, 7, 2, 10]
    print("测试用例2：")
    print(f"输入数组: {arr2}")
    print(f"结果: {longestArithSeqLength(arr2)}，预期: 3")
    print()
    
    # 测试用例3
    arr3 = [20, 1, 15, 3, 10, 5, 8]
    print("测试用例3：")
    print(f"输入数组: {arr3}")
    print(f"结果: {longestArithSeqLength(arr3)}，预期: 4")
    print()
    
    # 测试用例4：边界情况
    arr4 = [1, 3, 5]
    print("测试用例4：")
    print(f"输入数组: {arr4}")
    print(f"结果: {longestArithSeqLength(arr4)}，预期: 3")
    
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
    
    # 特殊测试用例：完全相同的元素
    print("\n特殊测试用例：完全相同的元素")
    arr_same = [5, 5, 5, 5, 5]
    print(f"输入数组: {arr_same}")
    print(f"结果: {longestArithSeqLength(arr_same)}")
    
    # 特殊测试用例：降序数组
    print("\n特殊测试用例：降序数组")
    arr_desc = [10, 8, 6, 4, 2]
    print(f"输入数组: {arr_desc}")
    print(f"结果: {longestArithSeqLength(arr_desc)}")


if __name__ == "__main__":
    """
    主函数入口
    """
    testCase()