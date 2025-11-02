#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
任意前缀上红多于黑问题 - 卡特兰数应用

问题描述：
有n个红和n个黑，要组成2n长度的数列，保证任意前缀上，红的数量 >= 黑的数量
返回有多少种排列方法，答案对 100 取模

数学背景：
这是卡特兰数的经典应用之一，也称为合法的01序列问题。
给定n个0和n个1，能够满足任意前缀序列中0的个数都不少于1的个数的序列数目为第n项卡特兰数。

解法思路：
1. 动态规划方法：C(n) = Σ(i=0 to n-1) C(i) * C(n-1-i)
2. 递推公式：C(n) = C(n-1) * (4*n-2) / (n+1)

相关题目链接：
- 洛谷 P1722 红黑序列: https://www.luogu.com.cn/problem/P1722
- LeetCode 96. 不同的二叉搜索树: https://leetcode.cn/problems/unique-binary-search-trees/
- LeetCode 22. 括号生成: https://leetcode.cn/problems/generate-parentheses/
- Codeforces 1204E Natasha, Sasha and the Prefix Sums: https://codeforces.com/problemset/problem/1204/E
- AtCoder ARC145 C: https://atcoder.jp/contests/arc145/tasks/arc145_c

时间复杂度分析：
- 动态规划方法：O(n²)
- 递推公式：O(n)

空间复杂度分析：
- 动态规划方法：O(n)
- 递推公式：O(1)

工程化考量：
- 因为取模的数字含有很多因子，无法用费马小定理或者扩展欧几里得求逆元
- 同时注意到n的范围并不大，直接使用公式4（动态规划方法）
- 1 <= n <= 100
"""

MOD = 100

def compute(n):
    """
    因为取模的数字含有很多因子
    无法用费马小定理或者扩展欧几里得求逆元
    同时注意到n的范围并不大，直接使用公式4
    """
    # dp[i] 表示第i项卡特兰数
    dp = [0] * (n + 1)
    
    # 初始化基本情况
    dp[0] = dp[1] = 1
    
    # 动态规划填表
    # 使用递推公式：C(n) = Σ(i=0 to n-1) C(i) * C(n-1-i)
    for i in range(2, n + 1):
        # 对于第i项卡特兰数，累加所有可能的乘积
        for j in range(i):
            # dp[j] 是第j项卡特兰数
            # dp[i-1-j] 是第i-1-j项卡特兰数
            # 两者相乘累加到dp[i]中
            dp[i] = (dp[i] + dp[j] * dp[i - 1 - j] % MOD) % MOD
    
    return dp[n]

# 测试函数
if __name__ == "__main__":
    n = int(input())
    print(compute(n))