# 01背包问题（模板）
# 
# 问题描述：
# 给定一个正数t，表示背包的容量
# 有m个货物，每个货物可以选择一次
# 每个货物有自己的体积costs[i]和价值values[i]
# 返回在不超过总容量的情况下，怎么挑选货物能达到价值最大
# 返回最大的价值
# 
# 解题思路：
# 01背包问题是动态规划中的经典问题，每个物品只能选择一次。
# 状态定义：dp[i][j]表示前i个物品，背包容量为j时能获得的最大价值
# 状态转移方程：
# - 不选择第i个物品：dp[i][j] = dp[i-1][j]
# - 选择第i个物品：dp[i][j] = dp[i-1][j-cost[i]] + val[i]（前提是j >= cost[i]）
# - dp[i][j] = max(dp[i-1][j], dp[i-1][j-cost[i]] + val[i])
# 
# 优化：通过观察状态转移方程，发现dp[i][j]只依赖于dp[i-1]这一行，
# 所以可以用一维数组优化空间复杂度，但需要倒序遍历背包容量以确保每个物品只使用一次。
# 
# 时间复杂度：O(n * t)，其中n是物品数量，t是背包容量
# 空间复杂度：O(t)
# 
# 测试链接 : https://www.luogu.com.cn/problem/P1048

import sys

# 全局变量
MAXM = 101
MAXT = 1001

# 物品的体积（成本）
cost = [0] * MAXM

# 物品的价值
val = [0] * MAXM

# DP数组，用于空间优化版本
dp = [0] * MAXT

t = 0
n = 0

# 严格位置依赖的动态规划
# n个物品编号1~n，第i号物品的花费cost[i]、价值val[i]
# cost、val数组是全局变量，已经把数据读入了
# 时间复杂度: O(n * t)，空间复杂度: O(n * t)
def compute1():
    """
    严格位置依赖的动态规划解法
    
    解题思路：
    使用二维DP数组，dp[i][j]表示前i个物品，背包容量为j时能获得的最大价值
    状态转移方程：
    - 不选择第i个物品：dp[i][j] = dp[i-1][j]
    - 选择第i个物品：dp[i][j] = dp[i-1][j-cost[i]] + val[i]（前提是j >= cost[i]）
    - dp[i][j] = max(dp[i-1][j], dp[i-1][j-cost[i]] + val[i])
    
    时间复杂度：O(n * t)
    空间复杂度：O(n * t)
    
    Returns:
        最大价值
    """
    # 创建二维dp数组
    # dp[i][j] 表示前i个物品，背包容量为j时能获得的最大价值
    dp = [[0 for _ in range(t + 1)] for _ in range(n + 1)]
    
    # 遍历每个物品
    for i in range(1, n + 1):
        # 遍历每个背包容量
        for j in range(t + 1):
            # 不选择第i个物品
            dp[i][j] = dp[i - 1][j]
            
            # 选择第i个物品（前提是背包容量足够）
            if j - cost[i] >= 0:
                # 要i号物品
                dp[i][j] = max(dp[i][j], dp[i - 1][j - cost[i]] + val[i])
    
    # 返回前n个物品，背包容量为t时能获得的最大价值
    return dp[n][t]

# 空间压缩版本
# 通过观察状态转移方程，发现dp[i][j]只依赖于dp[i-1]这一行
# 所以可以用一维数组优化空间复杂度
# 时间复杂度: O(n * t)，空间复杂度: O(t)
def compute2():
    """
    空间压缩版本
    
    解题思路：
    通过观察状态转移方程，发现dp[i][j]只依赖于dp[i-1]这一行
    所以可以用一维数组优化空间复杂度
    但需要倒序遍历背包容量以确保每个物品只使用一次
    
    时间复杂度：O(n * t)
    空间复杂度：O(t)
    
    Returns:
        最大价值
    """
    # 初始化dp数组
    for i in range(t + 1):
        dp[i] = 0
    
    # 遍历物品
    for i in range(1, n + 1):
        # 倒序遍历背包容量，确保每个物品只使用一次
        # 如果正序遍历，前面的状态会被更新，导致一个物品被多次使用
        for j in range(t, cost[i] - 1, -1):
            # 状态转移方程：
            # dp[j] = max(不选择当前物品, 选择当前物品)
            # 不选择当前物品：dp[j]（保持原值）
            # 选择当前物品：dp[j - cost[i]] + val[i]
            dp[j] = max(dp[j], dp[j - cost[i]] + val[i])
    
    # 返回背包容量为t时能获得的最大价值
    return dp[t]

# LeetCode 416. 分割等和子集 (Partition Equal Subset Sum)
# 题目描述：给定一个只包含正整数的非空数组，判断是否可以将这个数组分割成两个子集，
# 使得两个子集的元素和相等。
# 链接：https://leetcode.cn/problems/partition-equal-subset-sum/
# 解题思路：
# 1. 如果数组总和为奇数，则无法分割成两个相等的子集，返回false
# 2. 如果数组总和为偶数，则问题转化为01背包问题：
#    - 背包容量为 sum/2
#    - 每个数字既是重量也是价值
#    - 判断是否能装满背包
# 时间复杂度: O(n * sum)，其中n是数组长度，sum是数组元素和
# 空间复杂度: O(sum)
def canPartition(nums):
    """
    判断是否可以将数组分割成两个和相等的子集
    
    解题思路：
    1. 如果数组总和为奇数，则无法分割成两个相等的子集，返回false
    2. 如果数组总和为偶数，则问题转化为01背包问题：
       - 背包容量为 sum/2
       - 每个数字既是重量也是价值
       - 判断是否能装满背包
    
    Args:
        nums: 正整数数组
    
    Returns:
        是否可以分割成两个和相等的子集
    """
    # 计算数组总和
    total_sum = sum(nums)
    
    # 如果总和为奇数，无法分割成两个相等子集
    if total_sum % 2 == 1:
        return False
    
    # 目标和为总和的一半
    target = total_sum // 2
    
    # dp[j] 表示是否能组成和为j的子集
    dp = [False] * (target + 1)
    # 初始状态：和为0的子集总是存在（空集）
    dp[0] = True
    
    # 遍历每个数字
    for num in nums:
        # 01背包需要倒序遍历，确保每个物品只使用一次
        for j in range(target, num - 1, -1):
            # 状态转移方程：
            # dp[j] = dp[j] || dp[j - num]
            # dp[j]表示不选择当前数字能否组成和为j的子集
            # dp[j - num]表示选择当前数字能否组成和为j-num的子集
            dp[j] = dp[j] or dp[j - num]
    
    # 返回是否能组成和为target的子集
    return dp[target]

'''
示例:
输入: nums = [1,5,11,5]
输出: true
解释: 数组可以分割成 [1, 5, 5] 和 [11]。

输入: nums = [1,2,3,5]
输出: false
解释: 数组不能分割成两个元素和相等的子集。

时间复杂度: O(n * sum)，其中n是数组长度，sum是数组元素和
空间复杂度: O(sum)
'''

# 主函数用于测试洛谷P1048采药问题
if __name__ == "__main__":
    # 注意：Python中没有标准的输入流处理方式，这里仅作示例
    # 实际使用时需要根据具体平台调整输入方式
    pass