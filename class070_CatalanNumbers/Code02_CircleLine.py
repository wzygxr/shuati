#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
圆上连线问题 - 卡特兰数应用

问题描述：
圆上有2n个点，这些点成对连接起来，形成n条线段，任意两条线段不能相交，返回连接的方法数

数学背景：
这是卡特兰数的经典应用之一。任选一个人，他与某个人握手将圆分为两部分，满足卡特兰数的递推关系。
前几项为：1, 1, 2, 5, 14, 42, 132, 429, 1430, 4862, ...

解法思路：
1. 动态规划方法：C(n) = Σ(i=0 to n-1) C(i) * C(n-1-i)
2. 组合公式：C(n) = C(2n, n) / (n+1)
3. 递推公式：C(n) = C(n-1) * (4*n-2) / (n+1)

相关题目链接：
- 洛谷 P1976 圆上的点: https://www.luogu.com.cn/problem/P1976
- UVa 991 Safe Salutations: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=932
- LeetCode 1259. Handshakes That Don't Cross: https://leetcode.com/problems/handshakes-that-dont-cross/
- AtCoder ABC205 E: https://atcoder.jp/contests/abc205/tasks/abc205_e
- SGU 161 Convex Polygon: http://acm.sgu.ru/problem.php?contest=0&problem=161

时间复杂度分析：
- 动态规划方法：O(n²)
- 递推公式：O(n)
- 组合公式：O(n)

空间复杂度分析：
- 动态规划方法：O(n)
- 递推公式：O(1)
- 组合公式：O(1)

工程化考量：
- 注意！答案不对 10^9 + 7 取模！而是对 10^8 + 7 取模！
- 1 <= n <= 2999
"""

MOD = 100000007
MAXN = 1000001

# 阶乘余数表
fac = [0] * MAXN

# 阶乘逆元表
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

def compute(n):
    """这里用公式1"""
    build(2 * n)
    return (c(2 * n, n) - c(2 * n, n - 1) + MOD) % MOD

# 测试函数
if __name__ == "__main__":
    n = int(input())
    print(compute(n))