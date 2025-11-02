#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
幼儿园篮球题（二项式反演解法）
题目：洛谷 P2791 幼儿园篮球题
链接：https://www.luogu.com.cn/problem/P2791
描述：蔡徐坤专属篮球场上有N个篮球，其中M个是没气的。
ikun们会从N个篮球中准备n个球放在场地上，其中恰好有m个是没气的。
蔡徐坤会在这n个篮球中随机选出k个投篮。如果投进了x个，则这次表演的失败度为x^L。
求期望失败度。

解题思路：
使用二项式反演和第二类斯特林数解决期望计算问题。
E[x^L] = Σ(i=0 to L) S(L,i) * i! * C(m,i) * C(n-m,k-i) / C(n,k)
其中S(L,i)是第二类斯特林数，表示将L个不同的球放入i个相同的盒子且每个盒子非空的方案数。

时间复杂度：O(L^2 + k) - 预处理斯特林数O(L^2)，计算期望O(k)
空间复杂度：O(L^2) - 存储斯特林数数组
"""

MOD = 998244353
MAXL = 200005

# 阶乘数组和逆元数组
fact = [1] * MAXL
invFact = [1] * MAXL

# 第二类斯特林数
stirling = [[0] * MAXL for _ in range(MAXL)]

def pow_mod(base, exp, mod):
    """
    快速幂运算
    :param base: 底数
    :param exp: 指数
    :param mod: 模数
    :return: base^exp % mod
    """
    result = 1
    while exp > 0:
        if exp % 2 == 1:
            result = result * base % mod
        base = base * base % mod
        exp //= 2
    return result

def precompute(maxL):
    """
    预处理阶乘、阶乘逆元和第二类斯特林数
    :param maxL: 最大L值
    """
    global fact, invFact, stirling
    
    # 预处理阶乘和逆元
    fact[0] = 1
    for i in range(1, maxL + 1):
        fact[i] = fact[i-1] * i % MOD
    
    invFact[maxL] = pow_mod(fact[maxL], MOD-2, MOD)
    for i in range(maxL-1, -1, -1):
        invFact[i] = invFact[i+1] * (i+1) % MOD
    
    # 预处理第二类斯特林数
    stirling[0][0] = 1
    for i in range(1, maxL + 1):
        for j in range(1, i + 1):
            stirling[i][j] = (stirling[i-1][j-1] + j * stirling[i-1][j]) % MOD

def comb(n, k):
    """
    计算组合数C(n, k)
    :param n: 总数
    :param k: 选取数量
    :return: C(n, k) % MOD
    """
    if k > n or k < 0:
        return 0
    if n < MAXL:
        return fact[n] * invFact[k] % MOD * invFact[n-k] % MOD
    
    # 大数情况，直接计算
    result = 1
    for i in range(k):
        result = result * (n - i) % MOD
        result = result * pow_mod(i + 1, MOD - 2, MOD) % MOD
    return result

def expectedValue(n, m, k, L):
    """
    计算期望失败度E[x^L]
    :param n: 总球数
    :param m: 没气的球数
    :param k: 投篮数
    :param L: 失败度参数
    :return: E[x^L] % MOD
    """
    result = 0
    for i in range(min(L + 1, k + 1)):
        # E[x^L] = Σ(i=0 to L) S(L,i) * i! * C(m,i) * C(n-m,k-i) / C(n,k)
        term = stirling[L][i] * fact[i] % MOD
        term = term * comb(m, i) % MOD
        term = term * comb(n - m, k - i) % MOD
        term = term * pow_mod(comb(n, k), MOD - 2, MOD) % MOD
        result = (result + term) % MOD
    return result

def main():
    line = input().split()
    N, M, S, L = int(line[0]), int(line[1]), int(line[2]), int(line[3])
    
    # 预处理
    precompute(L)
    
    for _ in range(S):
        line = input().split()
        n, m, k = int(line[0]), int(line[1]), int(line[2])
        print(expectedValue(n, m, k, L))

if __name__ == "__main__":
    main()