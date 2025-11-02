"""
盈利计划 (Profitable Schemes) - 多维费用背包问题 - Python实现

题目描述：
集团里有 n 名员工，他们可以完成各种各样的工作创造利润。
第 i 种工作会产生 profit[i] 的利润，它要求 group[i] 名成员共同参与。
如果成员参与了其中一项工作，就不能参与另一项工作。
工作的任何至少产生 minProfit 利润的子集称为盈利计划，并且工作的成员总数最多为 n。
有多少种计划可以选择？因为答案很大，答案对 1000000007 取模。

题目来源：LeetCode 879. 盈利计划
测试链接：https://leetcode.cn/problems/profitable-schemes/

解题思路：
这是一个多维费用背包问题，有两个维度的限制：员工数量限制和利润要求。
我们需要计算满足员工数量不超过n且利润至少为minProfit的方案数。

算法实现：
1. 动态规划：使用三维DP表存储状态
2. 空间优化：使用二维数组滚动更新

时间复杂度分析：
- 动态规划：O(m * n * minProfit)，其中m为工作数量
- 空间优化：O(n * minProfit)，空间复杂度最优

空间复杂度分析：
- 动态规划：O(m * n * minProfit)，三维DP表
- 空间优化：O(n * minProfit)，二维DP表
"""

MOD = 10**9 + 7

from typing import List

def profitable_schemes1(n: int, min_profit: int, group: List[int], profit: List[int]) -> int:
    """
    动态规划解法
    
    Args:
        n: 员工数量上限
        min_profit: 最小利润要求
        group: 每个工作需要的员工数
        profit: 每个工作产生的利润
        
    Returns:
        int: 方案数
    """
    m = len(group)
    # dp[i][j][k] 表示前i个工作，使用j个员工，产生至少k利润的方案数
    dp = [[[0] * (min_profit + 1) for _ in range(n + 1)] for _ in range(m + 1)]
    
    # 初始化：0个工作，0个员工，0利润的方案数为1
    for j in range(n + 1):
        dp[0][j][0] = 1
    
    for i in range(1, m + 1):
        g = group[i - 1]
        p = profit[i - 1]
        
        for j in range(n + 1):
            for k in range(min_profit + 1):
                # 不选当前工作
                dp[i][j][k] = dp[i - 1][j][k]
                # 选当前工作（如果员工数量足够）
                if j >= g:
                    prev_profit = max(0, k - p)
                    dp[i][j][k] = (dp[i][j][k] + dp[i - 1][j - g][prev_profit]) % MOD
    
    return dp[m][n][min_profit]

def profitable_schemes2(n: int, min_profit: int, group: List[int], profit: List[int]) -> int:
    """
    空间优化的动态规划解法
    
    Args:
        n: 员工数量上限
        min_profit: 最小利润要求
        group: 每个工作需要的员工数
        profit: 每个工作产生的利润
        
    Returns:
        int: 方案数
    """
    m = len(group)
    # dp[j][k] 表示使用j个员工，产生至少k利润的方案数
    dp = [[0] * (min_profit + 1) for _ in range(n + 1)]
    
    # 初始化：0个员工，0利润的方案数为1
    for j in range(n + 1):
        dp[j][0] = 1
    
    for i in range(m):
        g = group[i]
        p = profit[i]
        
        # 从后往前更新，避免重复使用同一个工作
        for j in range(n, g - 1, -1):
            for k in range(min_profit, -1, -1):
                prev_profit = max(0, k - p)
                dp[j][k] = (dp[j][k] + dp[j - g][prev_profit]) % MOD
    
    return dp[n][min_profit]

# 测试函数
if __name__ == "__main__":
    # 测试用例1
    n1, min_profit1 = 5, 3
    group1 = [2, 2]
    profit1 = [2, 3]
    print("测试用例1:")
    print(f"n = {n1}, min_profit = {min_profit1}")
    print(f"group = {group1}, profit = {profit1}")
    print("方法1结果:", profitable_schemes1(n1, min_profit1, group1, profit1))
    print("方法2结果:", profitable_schemes2(n1, min_profit1, group1, profit1))
    print()
    
    # 测试用例2
    n2, min_profit2 = 10, 5
    group2 = [2, 3, 5]
    profit2 = [6, 7, 8]
    print("测试用例2:")
    print(f"n = {n2}, min_profit = {min_profit2}")
    print(f"group = {group2}, profit = {profit2}")
    print("方法1结果:", profitable_schemes1(n2, min_profit2, group2, profit2))
    print("方法2结果:", profitable_schemes2(n2, min_profit2, group2, profit2))