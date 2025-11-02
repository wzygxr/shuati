#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Catalan Numbers
计算第n项卡特兰数
测试链接：https://www.hackerearth.com/problem/algorithm/catalan-numbers-1/
"""

def compute_catalan(n: int) -> int:
    """
    计算第n项卡特兰数
    
    题目解析：
    1. 卡特兰数是组合数学中一个常出现在各种计数问题中的数列
    2. 前几项为：1, 1, 2, 5, 14, 42, 132, 429, 1430, 4862, ...
    3. 有多种计算方法，包括递推公式和组合公式
    
    时间复杂度分析：
    1. 使用递推公式：C(n) = Σ(i=0 to n-1) C(i) * C(n-1-i)
    2. 双重循环，外层循环n次，内层循环最多n次
    3. 总时间复杂度：O(n²)
    
    空间复杂度分析：
    1. 使用了一个长度为n+1的数组存储中间结果
    2. 空间复杂度：O(n)
    
    参数:
        n (int): 第n项
    返回:
        int: 第n项卡特兰数
    """
    # 边界条件处理
    if n <= 1:
        return 1
    
    # dp[i] 表示第i项卡特兰数
    dp = [0] * (n + 1)
    
    # 初始化基本情况
    dp[0] = 1  # 第0项卡特兰数为1
    dp[1] = 1  # 第1项卡特兰数为1
    
    # 动态规划填表
    # 使用递推公式：C(n) = Σ(i=0 to n-1) C(i) * C(n-1-i)
    for i in range(2, n + 1):
        # 对于第i项卡特兰数，累加所有可能的乘积
        for j in range(i):
            # dp[j] 是第j项卡特兰数
            # dp[i-1-j] 是第i-1-j项卡特兰数
            # 两者相乘累加到dp[i]中
            dp[i] += dp[j] * dp[i - 1 - j]
    
    return dp[n]

def compute_catalan_optimized(n: int) -> int:
    """
    使用卡特兰数的另一种递推公式计算
    C(0) = 1
    C(n) = C(n-1) * (4*n-2) / (n+1)
    
    时间复杂度：O(n)
    空间复杂度：O(1)
    
    参数:
        n (int): 第n项
    返回:
        int: 第n项卡特兰数
    """
    if n <= 1:
        return 1
    
    catalan = 1
    for i in range(2, n + 1):
        catalan = catalan * (4 * i - 2) // (i + 1)
    return catalan

def compute_catalan_combination(n: int) -> int:
    """
    使用组合公式计算卡特兰数
    C(n) = C(2n, n) / (n+1)
    
    时间复杂度：O(n)
    空间复杂度：O(1)
    
    参数:
        n (int): 第n项
    返回:
        int: 第n项卡特兰数
    """
    if n <= 1:
        return 1
    
    # 计算组合数C(2n, n)
    result = 1
    for i in range(n):
        result = result * (2 * n - i) // (i + 1)
    
    # 除以(n+1)
    return result // (n + 1)

def main() -> None:
    """
    主函数 - 测试所有实现
    """
    print("卡特兰数计算测试：")
    for i in range(11):
        result1 = compute_catalan(i)
        result2 = compute_catalan_optimized(i)
        result3 = compute_catalan_combination(i)
        print(f"C({i}) = {result1}, optimized = {result2}, combination = {result3}")

if __name__ == "__main__":
    main()