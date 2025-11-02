#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
等差数列划分 II - 子序列 - LeetCode 446
题目来源：https://leetcode.cn/problems/arithmetic-slices-ii-subsequence/
难度：困难
题目描述：给你一个整数数组 nums，请你返回所有长度至少为 3 的等差子序列的数目。
注意：子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。
另外，子序列中的元素在原数组中可能不连续，但等差子序列需要满足元素之间的差值相等。

核心思路：
1. 这道题是LIS问题的变种，我们需要计算所有可能的等差数列子序列数目
2. 使用动态规划+哈希表的方法：dp[i][d] 表示以nums[i]结尾且公差为d的等差数列子序列的数目（至少有两个元素）
3. 对于每个元素nums[i]，遍历之前的所有元素nums[j]，计算差值d = nums[i] - nums[j]，并更新dp[i][d] += dp[j][d] + 1
4. 其中+1表示nums[j]和nums[i]形成的新的二元组，dp[j][d]表示可以接在已有等差序列后面的数目

复杂度分析：
时间复杂度：O(n²)，其中n是数组的长度，对于每个元素，我们需要遍历之前的所有元素
空间复杂度：O(n²)，最坏情况下，每个元素对应的不同公差数量接近n
"""

from typing import List, Dict
import time
import random


def numberOfArithmeticSlices(nums: List[int]) -> int:
    """
    最优解法：动态规划+哈希表
    
    参数:
        nums: 整数数组
    返回:
        所有长度至少为3的等差子序列的数目
    """
    # 边界情况处理
    if len(nums) < 3:
        return 0
    
    n = len(nums)
    total = 0  # 记录所有长度至少为3的等差子序列数目
    
    # dp[i] 是一个字典，键为公差，值为以nums[i]结尾且具有该公差的等差子序列数目（至少有两个元素）
    dp: List[Dict[int, int]] = [{} for _ in range(n)]
    
    # 填充dp数组
    for i in range(1, n):
        for j in range(i):
            # 计算公差
            diff = nums[i] - nums[j]
            
            # 获取以nums[j]结尾且公差为diff的等差子序列数目
            count_j = dp[j].get(diff, 0)
            
            # 以nums[i]结尾且公差为diff的等差子序列数目 = 
            # 以nums[j]结尾且公差为diff的等差子序列数目（将nums[i]添加到这些序列后面） + 1（nums[j]和nums[i]形成的新二元组）
            dp[i][diff] = dp[i].get(diff, 0) + count_j + 1
            
            # 只有当count_j >= 1时，才能形成长度至少为3的等差子序列
            # 因为count_j表示以nums[j]结尾且公差为diff的等差子序列数目（至少有两个元素）
            # 所以将nums[i]添加到这些序列后面，就形成了长度至少为3的等差子序列
            total += count_j
    
    return total


def numberOfArithmeticSlicesAlternative(nums: List[int]) -> int:
    """
    另一种实现方式，逻辑相同但写法略有不同
    
    参数:
        nums: 整数数组
    返回:
        所有长度至少为3的等差子序列的数目
    """
    if len(nums) < 3:
        return 0
    
    n = len(nums)
    total = 0
    
    # 使用列表的字典存储状态
    dp: List[Dict[int, int]] = [{} for _ in range(n)]
    
    for i in range(n):
        for j in range(i):
            # 计算公差
            diff = nums[i] - nums[j]
            
            # 从dp[j]中获取公差为diff的序列数目
            prev_count = dp[j].get(diff, 0)
            
            # 更新dp[i]中的状态
            dp[i][diff] = dp[i].get(diff, 0) + prev_count + 1
            
            # 累加可以形成长度>=3的子序列数目
            total += prev_count
    
    return total


def numberOfArithmeticSlicesExplained(nums: List[int]) -> int:
    """
    解释性更强的版本，添加了详细的中间变量说明
    
    参数:
        nums: 整数数组
    返回:
        所有长度至少为3的等差子序列的数目
    """
    if len(nums) < 3:
        return 0
    
    n = len(nums)
    result = 0
    
    # dp[i][d]表示以nums[i]结尾，公差为d的等差子序列的数量（至少包含两个元素）
    dp: List[Dict[int, int]] = [{} for _ in range(n)]
    
    for i in range(n):
        for j in range(i):
            # 计算公差
            diff = nums[i] - nums[j]
            
            # 获取以nums[j]结尾且公差为diff的等差子序列数目
            sequences_ending_at_j = dp[j].get(diff, 0)
            
            # 新的序列数目：已有的序列数目 + 1（nums[j], nums[i]这个新的二元组）
            new_sequences_count = sequences_ending_at_j + 1
            
            # 更新dp[i][diff]
            dp[i][diff] = dp[i].get(diff, 0) + new_sequences_count
            
            # 对于每个以nums[j]结尾且公差为diff的序列，加上nums[i]后就形成了一个长度至少为3的序列
            # 因此，将sequences_ending_at_j加到结果中
            result += sequences_ending_at_j
    
    return result


def runAllSolutionsTest(nums: List[int]):
    """
    运行所有解法的对比测试
    
    参数:
        nums: 输入数组
    """
    print(f"\n对比测试：{nums}")
    
    # 测试动态规划+哈希表解法
    start_time = time.time()
    result1 = numberOfArithmeticSlices(nums)
    end_time = time.time()
    print(f"动态规划+哈希表解法结果: {result1}")
    print(f"耗时: {(end_time - start_time) * 1_000_000:.2f} μs")
    
    # 测试另一种实现方式
    start_time = time.time()
    result2 = numberOfArithmeticSlicesAlternative(nums)
    end_time = time.time()
    print(f"另一种实现方式结果: {result2}")
    print(f"耗时: {(end_time - start_time) * 1_000_000:.2f} μs")
    
    # 测试解释性更强的版本
    start_time = time.time()
    result3 = numberOfArithmeticSlicesExplained(nums)
    end_time = time.time()
    print(f"解释性版本结果: {result3}")
    print(f"耗时: {(end_time - start_time) * 1_000_000:.2f} μs")
    
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
    result1 = numberOfArithmeticSlices(nums)
    end_time = time.time()
    print(f"动态规划+哈希表解法耗时: {(end_time - start_time) * 1000:.3f} ms, 结果: {result1}")


def testCase():
    """
    测试用例
    """
    # 测试用例1
    arr1 = [2, 4, 6, 8, 10]
    print("测试用例1：")
    print(f"输入数组: {arr1}")
    print(f"结果: {numberOfArithmeticSlices(arr1)}，预期: 7")
    print()
    
    # 测试用例2
    arr2 = [7, 7, 7, 7, 7]
    print("测试用例2：")
    print(f"输入数组: {arr2}")
    print(f"结果: {numberOfArithmeticSlices(arr2)}，预期: 16")
    print()
    
    # 测试用例3：边界情况
    arr3 = [1, 2, 3]
    print("测试用例3：")
    print(f"输入数组: {arr3}")
    print(f"结果: {numberOfArithmeticSlices(arr3)}，预期: 1")
    
    # 运行所有解法的对比测试
    runAllSolutionsTest(arr1)
    runAllSolutionsTest(arr2)
    runAllSolutionsTest(arr3)
    
    # 性能测试
    print("性能测试:")
    print("-" * 40)
    performanceTest(100)
    performanceTest(200)
    
    # 特殊测试用例：完全相同的元素
    print("\n特殊测试用例：完全相同的元素")
    arr_same = [5, 5, 5, 5, 5]
    print(f"输入数组: {arr_same}")
    print(f"结果: {numberOfArithmeticSlices(arr_same)}")
    
    # 特殊测试用例：降序数组
    print("\n特殊测试用例：降序数组")
    arr_desc = [10, 8, 6, 4, 2]
    print(f"输入数组: {arr_desc}")
    print(f"结果: {numberOfArithmeticSlices(arr_desc)}")


if __name__ == "__main__":
    """
    主函数入口
    """
    testCase()