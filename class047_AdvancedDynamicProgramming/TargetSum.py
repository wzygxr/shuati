"""
目标和 (Target Sum) - 多维费用背包问题 - Python实现

题目描述：
给你一个非负整数数组 nums 和一个整数 target。
向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个表达式。
返回可以通过上述方法构造的、运算结果等于 target 的不同表达式的数目。

题目来源：LeetCode 494. 目标和
测试链接：https://leetcode.cn/problems/target-sum/

解题思路：
这是一个典型的多维费用背包问题，可以转化为子集和问题。
假设我们选择一部分数字加上正号，另一部分数字加上负号。
设正数集合的和为 P，负数集合的和为 N，数组总和为 S。
则有：P - N = target，且 P + N = S
联立可得：P = (S + target) / 2
所以问题转化为：在数组中选择一些数字，使其和等于 (S + target) / 2 的方案数。

算法实现：
1. 动态规划：转化为子集和问题，使用背包DP
2. 记忆化搜索：直接枚举所有可能的符号组合

时间复杂度分析：
- 动态规划：O(n * sum)，其中n是数组长度，sum是数组元素和
- 记忆化搜索：O(n * sum)，使用偏移量处理负数

空间复杂度分析：
- 动态规划：O(sum)，一维DP数组
- 记忆化搜索：O(n * sum)，二维记忆化数组
"""

from typing import List
from functools import lru_cache

def find_target_sum_ways1(nums: List[int], target: int) -> int:
    """
    动态规划解法（转化为子集和问题）
    
    Args:
        nums: 非负整数数组
        target: 目标值
        
    Returns:
        int: 表达式数目
    """
    total_sum = sum(nums)
    
    # 如果target的绝对值大于sum，不可能达到目标
    if abs(target) > total_sum:
        return 0
    
    # 如果(S + target)是奇数，无法整除，无解
    if (total_sum + target) % 2 != 0:
        return 0
    
    # 转化为子集和问题，目标和为(S + target) / 2
    s = (total_sum + target) // 2
    if s < 0:
        return 0
    
    # dp[i]表示和为i的方案数
    dp = [0] * (s + 1)
    dp[0] = 1  # 和为0的方案数为1（什么都不选）
    
    # 遍历每个数字
    for num in nums:
        # 从后往前更新，避免重复使用同一个数字
        for j in range(s, num - 1, -1):
            dp[j] += dp[j - num]
    
    return dp[s]

def find_target_sum_ways2(nums: List[int], target: int) -> int:
    """
    记忆化搜索解法
    
    Args:
        nums: 非负整数数组
        target: 目标值
        
    Returns:
        int: 表达式数目
    """
    n = len(nums)
    
    @lru_cache(maxsize=None)
    def dfs(i: int, current_sum: int) -> int:
        # 边界条件：处理完所有数字
        if i == n:
            return 1 if current_sum == target else 0
        
        # 两种选择：加法或减法
        return dfs(i + 1, current_sum + nums[i]) + \
               dfs(i + 1, current_sum - nums[i])
    
    return dfs(0, 0)

# 测试函数
if __name__ == "__main__":
    # 测试用例1
    nums1 = [1, 1, 1, 1, 1]
    target1 = 3
    print("测试用例1:")
    print(f"数组: {nums1}, 目标值: {target1}")
    print("动态规划结果:", find_target_sum_ways1(nums1, target1))
    print("记忆化搜索结果:", find_target_sum_ways2(nums1, target1))
    print()
    
    # 测试用例2
    nums2 = [1]
    target2 = 1
    print("测试用例2:")
    print(f"数组: {nums2}, 目标值: {target2}")
    print("动态规划结果:", find_target_sum_ways1(nums2, target2))
    print("记忆化搜索结果:", find_target_sum_ways2(nums2, target2))
    print()
    
    # 测试用例3
    nums3 = [1, 0]
    target3 = 1
    print("测试用例3:")
    print(f"数组: {nums3}, 目标值: {target3}")
    print("动态规划结果:", find_target_sum_ways1(nums3, target3))
    print("记忆化搜索结果:", find_target_sum_ways2(nums3, target3))