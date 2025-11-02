#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
有趣的数列 - 卡特兰数因子计数法

问题描述：
求第n项卡特兰数，要求答案对p取模

数学背景：
这是卡特兰数的一个重要扩展应用，提供了更通用的计数模型。
使用因子计数法来处理大数和非质数模数的情况。

解法思路：
1. 使用公式2 + 质因子计数法
2. 利用欧拉筛生成[2 ~ 2*n]范围上所有数的最小质因子
3. 如果x为质数，minpf[x] == 0
4. 如果x为合数，x的最小质因子为minpf[x]

相关题目链接：
- 洛谷 P3200 有趣的数列: https://www.luogu.com.cn/problem/P3200
- LeetCode 96. 不同的二叉搜索树: https://leetcode.cn/problems/unique-binary-search-trees/
- LeetCode 22. 括号生成: https://leetcode.cn/problems/generate-parentheses/
- Codeforces 1204E Natasha, Sasha and the Prefix Sums: https://codeforces.com/problemset/problem/1204/E
- AtCoder ABC205 E: https://atcoder.jp/contests/abc205/tasks/abc205_e

时间复杂度分析：
- 欧拉筛：O(n)
- 质因子计数：O(n log n)

空间复杂度分析：
- 存储质因子表：O(n)

工程化考量：
- 1 <= n <= 10^6
- 1 <= p <= 10^9
- p可能不为质数
"""

MAXN = 2000001

# 如果minpf[i] == 0，代表i是质数
# 如果minpf[i] != 0，代表i是合数，并且最小质因子是minpf[i]
minpf = [0] * MAXN

# 质数表
prime = [0] * MAXN

# 质数表大小
cnt = 0

# 因子计数
counts = [0] * MAXN

# 来自讲解097，欧拉筛，时间复杂度O(n)
def euler(n):
    global cnt
    for i in range(2, n + 1):
        minpf[i] = 0
    cnt = 0
    for i in range(2, n + 1):
        # minpf[i] == 0代表i为质数，收集进质数表
        # minpf数组替代了讲解097中visit数组的作用
        if minpf[i] == 0:
            prime[cnt] = i
            cnt += 1
        for j in range(cnt):
            if i * prime[j] > n:
                break
            # 此时收集(i * prime[j])这个数的最小质因子为prime[j]
            # minpf[i * prime[j]] != 0，也标记了(i * prime[j])是合数
            # 讲解097欧拉筛的部分，重点解释了这个过程，看完必懂
            minpf[i * prime[j]] = prime[j]
            if i % prime[j] == 0:
                break

def power(x, p, mod):
    """快速幂运算"""
    ans = 1
    while p > 0:
        if (p & 1) == 1:
            ans = (ans * x) % mod
        x = (x * x) % mod
        p >>= 1
    return ans

# 使用的是公式2 + 质因子计数法
def compute(n, mod):
    """使用公式2 + 质因子计数法计算卡特兰数"""
    global cnt
    # 利用欧拉筛生成[2 ~ 2*n]范围上所有数的最小质因子
    # 如果x为质数，minpf[x] == 0
    # 如果x为合数，x的最小质因子为minpf[x]
    euler(2 * n)
    # 分母每个因子设置计数
    for i in range(2, n + 1):
        counts[i] = -1
    # 分子每个因子设置计数
    for i in range(n + 2, 2 * n + 1):
        counts[i] = 1
    # 从大到小的每个数统计计数
    # 合数根据最小质因子来分解，变成更小数字的计数
    # 质数无法分解，计数确定，最后快速幂计算乘积
    for i in range(2 * n, 1, -1):
        if minpf[i] != 0:
            counts[minpf[i]] += counts[i]
            counts[i // minpf[i]] += counts[i]
            counts[i] = 0
    # 每个质数的幂，都乘起来，就是最后答案
    ans = 1
    for i in range(2, 2 * n + 1):
        if counts[i] != 0:
            ans = ans * power(i, counts[i], mod) % mod
    return int(ans)

# 测试函数
if __name__ == "__main__":
    n, mod = map(int, input().split())
    print(compute(n, mod))