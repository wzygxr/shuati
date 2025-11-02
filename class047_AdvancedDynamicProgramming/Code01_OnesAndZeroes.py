"""
一和零 (Ones and Zeroes) - 多维费用背包问题 - Python实现

题目描述：
给你一个二进制字符串数组 strs 和两个整数 m 和 n。
请你找出并返回 strs 的最大子集的长度，该子集中最多有 m 个 0 和 n 个 1。
如果 x 的所有元素也是 y 的元素，集合 x 是集合 y 的子集。

题目来源：LeetCode 474. 一和零
测试链接：https://leetcode.cn/problems/ones-and-zeroes/

解题思路：
这是一个典型的多维费用背包问题，每个字符串有两个维度的费用：0的数量和1的数量。
我们需要在不超过m个0和n个1的限制下，选择尽可能多的字符串。

算法实现：
1. 动态规划：使用三维DP表存储状态
2. 空间优化：使用二维数组滚动更新

时间复杂度分析：
- 动态规划：O(l * m * n)，其中l为字符串数量
- 空间优化：O(m * n)，空间复杂度最优

空间复杂度分析：
- 动态规划：O(l * m * n)，三维DP表
- 空间优化：O(m * n)，二维DP表
"""

def count_zeros_and_ones(s: str) -> tuple:
    """
    统计字符串中0和1的数量
    
    Args:
        s: 输入字符串
        
    Returns:
        tuple: (0的数量, 1的数量)
    """
    zeros = s.count('0')
    ones = len(s) - zeros
    return zeros, ones

def find_max_form1(strs: list, m: int, n: int) -> int:
    """
    动态规划解法
    
    Args:
        strs: 二进制字符串数组
        m: 最大0的数量
        n: 最大1的数量
        
    Returns:
        int: 最大子集长度
    """
    length = len(strs)
    # dp[i][j][k] 表示前i个字符串，使用j个0和k个1的最大子集长度
    dp = [[[0] * (n + 1) for _ in range(m + 1)] for _ in range(length + 1)]
    
    for i in range(1, length + 1):
        zeros, ones = count_zeros_and_ones(strs[i - 1])
        
        for j in range(m + 1):
            for k in range(n + 1):
                # 不选当前字符串
                dp[i][j][k] = dp[i - 1][j][k]
                # 选当前字符串（如果0和1的数量足够）
                if j >= zeros and k >= ones:
                    dp[i][j][k] = max(dp[i][j][k], 
                                     dp[i - 1][j - zeros][k - ones] + 1)
    
    return dp[length][m][n]

def find_max_form2(strs: list, m: int, n: int) -> int:
    """
    空间优化的动态规划解法
    
    Args:
        strs: 二进制字符串数组
        m: 最大0的数量
        n: 最大1的数量
        
    Returns:
        int: 最大子集长度
    """
    # dp[j][k] 表示使用j个0和k个1的最大子集长度
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    for s in strs:
        zeros, ones = count_zeros_and_ones(s)
        
        # 从后往前更新，避免重复使用同一个字符串
        for j in range(m, zeros - 1, -1):
            for k in range(n, ones - 1, -1):
                dp[j][k] = max(dp[j][k], dp[j - zeros][k - ones] + 1)
    
    return dp[m][n]

# 测试函数
if __name__ == "__main__":
    # 测试用例1
    strs1 = ["10", "0001", "111001", "1", "0"]
    m1, n1 = 5, 3
    print("测试用例1:")
    print("字符串数组:", strs1)
    print(f"m = {m1}, n = {n1}")
    print("方法1结果:", find_max_form1(strs1, m1, n1))
    print("方法2结果:", find_max_form2(strs1, m1, n1))
    print()
    
    # 测试用例2
    strs2 = ["10", "0", "1"]
    m2, n2 = 1, 1
    print("测试用例2:")
    print("字符串数组:", strs2)
    print(f"m = {m2}, n = {n2}")
    print("方法1结果:", find_max_form1(strs2, m2, n2))
    print("方法2结果:", find_max_form2(strs2, m2, n2))