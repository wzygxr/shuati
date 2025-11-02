#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
卡特兰数模板 - 出栈序列计数问题

问题描述：
有n个元素按顺序进栈，求所有可能的出栈序列数量
进栈顺序规定为1、2、3..n，返回有多少种不同的出栈顺序

数学背景：
这是卡特兰数的经典应用之一。卡特兰数是组合数学中常出现在各种计数问题中的数列。
前几项为：1, 1, 2, 5, 14, 42, 132, 429, 1430, 4862, ...

解法思路：
1. 动态规划方法：C(n) = Σ(i=0 to n-1) C(i) * C(n-1-i)
2. 组合公式：C(n) = C(2n, n) / (n+1)
3. 递推公式：C(n) = C(n-1) * (4*n-2) / (n+1)

相关题目链接：
- 洛谷 P1044 栈: https://www.luogu.com.cn/problem/P1044
- Vijos P1122 出栈序列统计: https://vijos.org/p/1122
- 51Nod 1174: https://www.51nod.com/Challenge/Problem.html#problemId=1174
- 牛客网 NC20652 出栈序列: https://www.nowcoder.com/practice/96bd6684e0c54b8380e4a4bff97e60bb
- HDU 1023 Train Problem II: http://acm.hdu.edu.cn/showproblem.php?pid=1023
- POJ 1095 Trees Made to Order: http://poj.org/problem?id=1095
- SPOJ CARD: https://www.spoj.com/problems/CARD/
- LeetCode 96. 不同的二叉搜索树: https://leetcode.cn/problems/unique-binary-search-trees/
- LeetCode 22. 括号生成: https://leetcode.cn/problems/generate-parentheses/

时间复杂度分析：
- 动态规划方法：O(n²)
- 递推公式：O(n)
- 组合公式：O(n)

空间复杂度分析：
- 动态规划方法：O(n)
- 递推公式：O(1)
- 组合公式：O(1)

工程化考量：
- 当n较小时，不需要取模处理
- 当n较大时，卡特兰数会变得非常大，需要对 1000000007 取模
"""

MOD = 1000000007
MAXN = 1000001

# 阶乘余数表
fac = [0] * MAXN

# 阶乘逆元表
inv1 = [0] * MAXN

# 连续数逆元表
inv2 = [0] * MAXN

def build1(n):
    """来自讲解099，题目3，生成阶乘余数表、阶乘逆元表"""
    fac[0] = inv1[0] = 1
    fac[1] = 1
    for i in range(2, n + 1):
        fac[i] = (i * fac[i - 1]) % MOD
    
    inv1[n] = pow(fac[n], MOD - 2, MOD)
    for i in range(n - 1, 0, -1):
        inv1[i] = ((i + 1) * inv1[i + 1]) % MOD

def build2(n):
    """来自讲解099，题目2，生成连续数逆元表"""
    inv2[1] = 1
    for i in range(2, n + 2):
        inv2[i] = MOD - inv2[MOD % i] * (MOD // i) % MOD

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
    return (((fac[n] * inv1[k]) % MOD) * inv1[n - k]) % MOD

def compute1(n):
    """公式1"""
    build1(2 * n)
    return (c(2 * n, n) - c(2 * n, n - 1) + MOD) % MOD

def compute2(n):
    """公式2"""
    build1(2 * n)
    return c(2 * n, n) * power(n + 1, MOD - 2) % MOD

def compute3(n):
    """公式3"""
    build2(n)
    f = [0] * (n + 1)
    f[0] = f[1] = 1
    for i in range(2, n + 1):
        f[i] = f[i - 1] * (4 * i - 2) % MOD * inv2[i + 1] % MOD
    return f[n]

def compute4(n):
    """公式4"""
    f = [0] * (n + 1)
    f[0] = f[1] = 1
    for i in range(2, n + 1):
        for l in range(i):
            r = i - 1 - l
            f[i] = (f[i] + f[l] * f[r] % MOD) % MOD
    return f[n]

# 测试函数
if __name__ == "__main__":
    n = int(input())
    print(compute1(n))
    # print(compute2(n))
    # print(compute3(n))
    # print(compute4(n))