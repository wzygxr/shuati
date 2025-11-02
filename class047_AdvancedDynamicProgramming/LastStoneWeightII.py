"""
最后一块石头的重量 II (Last Stone Weight II) - 背包问题变形 - Python实现

题目描述：
有一堆石头，用整数数组 stones 表示。其中 stones[i] 表示第 i 块石头的重量。
每一回合，从中选出任意两块石头，然后将它们一起粉碎。
假设石头的重量分别为 x 和 y，且 x <= y。粉碎的可能结果如下：
如果 x == y，那么两块石头都会被完全粉碎；
如果 x != y，那么重量为 x 的石头完全粉碎，重量为 y 的石头新重量为 y-x。
最后，最多只会剩下一块石头。返回此石头最小的可能重量。如果没有石头剩下，就返回 0。

题目来源：LeetCode 1049. 最后一块石头的重量 II
测试链接：https://leetcode.cn/problems/last-stone-weight-ii/

解题思路：
这道题可以转化为经典的背包问题。我们希望最后剩下的石头重量最小，相当于要将石头分成两堆，使得两堆的重量差最小。
设石头总重量为 sum，其中一堆的重量为 s，则另一堆的重量为 sum - s。
两堆重量差为 |s - (sum - s)| = |2*s - sum|。
要使差值最小，就要使 s 尽可能接近 sum/2。
所以问题转化为：在石头中选择一些，使其总重量尽可能接近 sum/2，但不超过 sum/2。
这就变成了一个 0-1 背包问题，背包容量为 sum/2，物品重量和价值都为 stones[i]。

算法实现：
1. 动态规划：使用背包DP求解最大可装重量
2. 记忆化搜索：递归枚举所有选择方案

时间复杂度分析：
- 动态规划：O(n * sum)，其中n是数组长度，sum是数组元素和
- 记忆化搜索：O(n * sum)，每个状态计算一次

空间复杂度分析：
- 动态规划：O(sum)，一维DP数组
- 记忆化搜索：O(n * sum)，二维记忆化数组
"""

from typing import List
from functools import lru_cache

def last_stone_weight_ii1(stones: List[int]) -> int:
    """
    动态规划解法
    
    Args:
        stones: 石头重量数组
        
    Returns:
        int: 最后剩下的石头最小重量
    """
    total_sum = sum(stones)
    
    # 背包容量为 sum/2
    target = total_sum // 2
    
    # dp[i] 表示容量为 i 的背包最多能装的石头重量
    dp = [0] * (target + 1)
    
    # 遍历每个石头
    for stone in stones:
        # 从后往前更新，避免重复使用同一个石头
        for j in range(target, stone - 1, -1):
            dp[j] = max(dp[j], dp[j - stone] + stone)
    
    # 两堆石头的重量差
    return total_sum - 2 * dp[target]

def last_stone_weight_ii2(stones: List[int]) -> int:
    """
    记忆化搜索解法
    
    Args:
        stones: 石头重量数组
        
    Returns:
        int: 最后剩下的石头最小重量
    """
    n = len(stones)
    total_sum = sum(stones)
    target = total_sum // 2
    
    @lru_cache(maxsize=None)
    def dfs(i: int, current_weight: int) -> int:
        # 边界条件：处理完所有石头或背包已满
        if i == n or current_weight == 0:
            return 0
        
        # 不选择当前石头
        max_weight = dfs(i + 1, current_weight)
        
        # 选择当前石头（如果容量足够）
        if current_weight >= stones[i]:
            max_weight = max(max_weight, dfs(i + 1, current_weight - stones[i]) + stones[i])
        
        return max_weight
    
    max_weight = dfs(0, target)
    return total_sum - 2 * max_weight

# 测试函数
if __name__ == "__main__":
    # 测试用例1
    stones1 = [2, 7, 4, 1, 8, 1]
    print("测试用例1:")
    print(f"石头重量: {stones1}")
    print("动态规划结果:", last_stone_weight_ii1(stones1))
    print("记忆化搜索结果:", last_stone_weight_ii2(stones1))
    print()
    
    # 测试用例2
    stones2 = [31, 26, 33, 21, 40]
    print("测试用例2:")
    print(f"石头重量: {stones2}")
    print("动态规划结果:", last_stone_weight_ii1(stones2))
    print("记忆化搜索结果:", last_stone_weight_ii2(stones2))
    print()
    
    # 测试用例3
    stones3 = [1, 2]
    print("测试用例3:")
    print(f"石头重量: {stones3}")
    print("动态规划结果:", last_stone_weight_ii1(stones3))
    print("记忆化搜索结果:", last_stone_weight_ii2(stones3))