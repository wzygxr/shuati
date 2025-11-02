#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
生成字符串问题 - 卡特兰数变形应用

问题描述：
有n个1和m个0，要组成n+m长度的数列，保证任意前缀上，1的数量 >= 0的数量
返回有多少种排列方法，答案对 20100403 取模

数学背景：
这是卡特兰数问题的变形，结果为第min(n,m)项卡特兰数的变形。
当n = m时就是标准卡特兰数。

解法思路：
1. 使用组合公式：C(n+m, m) - C(n+m, m-1)
2. 利用预处理阶乘和逆元表优化计算

相关题目链接：
- 洛谷 P1641 生成字符串: https://www.luogu.com.cn/problem/P1641
- LeetCode 96. 不同的二叉搜索树: https://leetcode.cn/problems/unique-binary-search-trees/
- LeetCode 22. 括号生成: https://leetcode.cn/problems/generate-parentheses/
- HDU 1023 Train Problem II: http://acm.hdu.edu.cn/showproblem.php?pid=1023
- POJ 2084 Catalan Numbers: http://poj.org/problem?id=2084

时间复杂度分析：
- 预处理阶乘和逆元：O(n+m)
- 计算组合数：O(1)

空间复杂度分析：
- 存储阶乘和逆元表：O(n+m)

工程化考量：
- 1 <= m <= n <= 10^6
- 答案对 20100403 取模
"""

MOD = 20100403
MAXN = 2000001

fac = [0] * MAXN
inv = [0] * MAXN

def build(n):
    fac[0] = inv[0] = 1
    fac[1] = 1
    for i in range(2, n + 1):
        fac[i] = (i * fac[i - 1]) % MOD
    
    inv[n] = pow(fac[n], MOD - 2, MOD)
    for i in range(n - 1, 0, -1):
        inv[i] = ((i + 1) * inv[i + 1]) % MOD

def power(x, p):
    """快速幂运算"""
    ans = 1
    while p > 0:
        if (p & 1) == 1:
            ans = (ans * x) % MOD
        x = (x * x) % MOD
        p >>= 1
    return ans

def c(n, k):
    """计算组合数C(n, k)"""
    return (((fac[n] * inv[k]) % MOD) * inv[n - k]) % MOD

def compute(n, m):
    build(n + m)
    return (c(n + m, m) - c(n + m, m - 1) + MOD) % MOD

# 测试函数
if __name__ == "__main__":
    n, m = map(int, input().split())
    print(compute(n, m))