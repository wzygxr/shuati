#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
摆动序列 - LeetCode 376
题目来源：https://leetcode.cn/problems/wiggle-subsequence/
难度：中等
题目描述：如果连续数字之间的差严格地在正数和负数之间交替，则数字序列称为摆动序列。
第一个差（如果存在的话）可能是正数或负数。少于两个元素的序列也是摆动序列。
例如，[1,7,4,9,2,5] 是一个摆动序列，因为差值 (6,-3,5,-7,3) 是正负交替出现的。
相反, [1,4,7,2,5] 和 [1,7,4,5,5] 不是摆动序列，第一个序列是因为它的前两个差值都是正数，
第二个序列是因为它的最后一个差值为零。

核心思路：
1. 这道题可以使用贪心算法来解决，因为我们只需要记录序列的趋势变化
2. 摆动序列的关键在于相邻元素的差值交替变化
3. 我们可以维护当前的趋势（上升、下降或初始状态），然后遍历数组，统计趋势变化的次数

复杂度分析：
时间复杂度：O(n)，其中n是数组的长度，我们只需要遍历一次数组
空间复杂度：O(1)，只使用了常数级别的额外空间
"""

from typing import List


def wiggleMaxLength(nums: List[int]) -> int:
    """
    计算最长摆动子序列的长度 - 动态规划优化版本
    
    参数:
        nums: 输入数组
    返回:
        最长摆动子序列的长度
    """
    # 边界情况：如果数组长度小于2，直接返回数组长度
    if not nums:
        return 0
    if len(nums) == 1:
        return 1
    
    up = 1   # 以最后一个差值为正的最长摆动子序列长度
    down = 1 # 以最后一个差值为负的最长摆动子序列长度
    
    # 遍历数组，从第二个元素开始
    for i in range(1, len(nums)):
        if nums[i] > nums[i-1]:
            # 当前是上升趋势，最长摆动子序列长度等于之前下降趋势的长度加1
            up = down + 1
        elif nums[i] < nums[i-1]:
            # 当前是下降趋势，最长摆动子序列长度等于之前上升趋势的长度加1
            down = up + 1
        # 如果相等，不做任何操作，保持up和down不变
    
    # 返回较大的值，因为最后一个差值可能是正也可能是负
    return max(up, down)


def wiggleMaxLengthGreedy(nums: List[int]) -> int:
    """
    贪心算法解法 - 记录趋势变化
    
    参数:
        nums: 输入数组
    返回:
        最长摆动子序列的长度
    """
    if not nums:
        return 0
    if len(nums) == 1:
        return 1
    
    count = 1    # 至少有一个元素
    prev_diff = 0 # 前一个差值
    curr_diff = 0 # 当前差值
    
    for i in range(1, len(nums)):
        curr_diff = nums[i] - nums[i-1]
        
        # 如果当前差值与前一个差值符号不同，说明出现了摆动
        if (curr_diff > 0 and prev_diff <= 0) or (curr_diff < 0 and prev_diff >= 0):
            count += 1
            prev_diff = curr_diff
    
    return count


def wiggleMaxLengthDP(nums: List[int]) -> int:
    """
    动态规划解法 - 标准DP
    
    参数:
        nums: 输入数组
    返回:
        最长摆动子序列的长度
    """
    if not nums:
        return 0
    n = len(nums)
    if n == 1:
        return 1
    
    # dp[i][0]: 以nums[i]结尾且最后一个差值为正的最长摆动子序列长度
    # dp[i][1]: 以nums[i]结尾且最后一个差值为负的最长摆动子序列长度
    dp = [[1] * 2 for _ in range(n)]
    
    max_len = 1
    
    for i in range(1, n):
        for j in range(i):
            if nums[i] > nums[j]:
                # 如果nums[i] > nums[j]，可以接在以nums[j]结尾且最后一个差值为负的序列后面
                dp[i][0] = max(dp[i][0], dp[j][1] + 1)
            elif nums[i] < nums[j]:
                # 如果nums[i] < nums[j]，可以接在以nums[j]结尾且最后一个差值为正的序列后面
                dp[i][1] = max(dp[i][1], dp[j][0] + 1)
        
        # 更新最大值
        max_len = max(max_len, max(dp[i][0], dp[i][1]))
    
    return max_len


def testAllSolutions(nums: List[int]):
    """
    测试所有解法并比较结果
    
    参数:
        nums: 输入数组
    """
    print(f"输入数组: {nums}")
    print(f"解法1（动态规划优化版）: {wiggleMaxLength(nums)}")
    print(f"解法2（贪心算法）: {wiggleMaxLengthGreedy(nums)}")
    print(f"解法3（常规动态规划）: {wiggleMaxLengthDP(nums)}")
    print()


def testCase():
    """
    测试用例
    """
    # 测试用例1
    nums1 = [1, 7, 4, 9, 2, 5]
    print("测试用例1：")
    print(f"预期结果：6")
    print(f"实际结果：{wiggleMaxLength(nums1)}")
    print()
    
    # 测试用例2
    nums2 = [1, 17, 5, 10, 13, 15, 10, 5, 16, 8]
    print("测试用例2：")
    print(f"预期结果：7")
    print(f"实际结果：{wiggleMaxLength(nums2)}")
    print()
    
    # 测试用例3
    nums3 = [1, 2, 3, 4, 5, 6, 7, 8, 9]
    print("测试用例3：")
    print(f"预期结果：2")
    print(f"实际结果：{wiggleMaxLength(nums3)}")
    print()
    
    # 测试用例4：边界情况
    nums4 = [1]  # 只有一个元素
    print("测试用例4：")
    print(f"预期结果：1")
    print(f"实际结果：{wiggleMaxLength(nums4)}")
    print()
    
    # 测试用例5：边界情况
    nums5 = [1, 1]  # 所有元素相同
    print("测试用例5：")
    print(f"预期结果：1")
    print(f"实际结果：{wiggleMaxLength(nums5)}")
    print()
    
    # 详细测试所有解法
    print("详细比较所有解法：")
    print("-" * 40)
    
    testAllSolutions(nums1)
    testAllSolutions(nums2)
    testAllSolutions(nums3)
    testAllSolutions(nums4)
    testAllSolutions(nums5)


if __name__ == "__main__":
    """
    主函数入口
    """
    testCase()
    
    # 性能测试
    print("性能测试：")
    print("-" * 40)
    
    # 测试大规模数据
    import time
    import random
    
    # 生成一个随机数组
    large_nums = [random.randint(1, 10000) for _ in range(1000)]
    
    start_time = time.time()
    result1 = wiggleMaxLength(large_nums)
    end_time = time.time()
    print(f"解法1（动态规划优化版）性能：{(end_time - start_time) * 1000:.3f} ms")
    
    start_time = time.time()
    result2 = wiggleMaxLengthGreedy(large_nums)
    end_time = time.time()
    print(f"解法2（贪心算法）性能：{(end_time - start_time) * 1000:.3f} ms")
    
    start_time = time.time()
    result3 = wiggleMaxLengthDP(large_nums)
    end_time = time.time()
    print(f"解法3（常规动态规划）性能：{(end_time - start_time) * 1000:.3f} ms")
    
    print(f"所有解法结果一致: {result1 == result2 == result3}")
    print(f"结果: {result1}")