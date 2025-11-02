#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
洛谷 P3868【TJOI2009】猜数字
链接：https://www.luogu.com.cn/problem/P3868
题目大意：
现在有两组数字，a1,a2,...,an 和 b1,b2,...,bn。其中，ai两两互质。
现在请求出一个最小的正整数N，使得bi | (N - ai) 对所有i成立。

转化思路：
条件bi | (N - ai)等价于N ≡ ai (mod bi)
这就转化为了标准的中国剩余定理问题

算法思路：
中国剩余定理用于求解如下形式的一元线性同余方程组（其中m1,m2,...,mk两两互质）：
x ≡ a1 (mod m1)
x ≡ a2 (mod m2)
...
x ≡ ak (mod mk)

解法步骤：
1. 计算所有模数的乘积 M = m1 * m2 * ... * mk
2. 对于第i个方程：
   a. 计算 Mi = M / mi
   b. 计算 Mi 在模 mi 意义下的逆元 Mi^(-1)
   c. 计算 ci = Mi * Mi^(-1)
3. 方程组在模 M 意义下的唯一解为 x = (a1*c1 + a2*c2 + ... + ak*ck) mod M

时间复杂度：O(n^2 log max(mi))，其中n为方程个数，log项来自于扩展欧几里得算法
空间复杂度：O(n)

特殊处理：
1. 输入的ai可能是负数，需要转换为正数：ai = (ai % mi + mi) % mi
2. 本题可以使用扩展中国剩余定理解决，因为扩展中国剩余定理不要求模数两两互质，适用范围更广

工程化考虑：
1. 输入校验：检查输入是否合法
2. 异常处理：处理无解的情况
3. 大数处理：Python天然支持大整数运算

适用场景：
1. 密码学中的大数计算
2. 周期性问题的求解
3. 数论问题的建模

与其他算法的关联：
1. 扩展欧几里得算法：用于求解逆元和线性同余方程
2. 快速幂：在某些变种中用于求逆元
3. 线性同余方程：CRT本质上是解线性同余方程组

实际应用：
1. RSA加密算法中的中国剩余定理加速
2. 多精度整数运算的并行处理
3. 信号处理中的采样定理
"""

import sys

# 扩展欧几里得算法
def exgcd(a, b):
    if b == 0:
        return a, 1, 0
    else:
        d, x, y = exgcd(b, a % b)
        return d, y, x - (a // b) * y

# 求最大公约数
def gcd(a, b):
    if b == 0:
        return a
    return gcd(b, a % b)

# 求最小公倍数
def lcm(a, b):
    return a // gcd(a, b) * b

# 求逆元
def mod_inverse(a, m):
    d, x, y = exgcd(a, m)
    if d != 1:
        return None  # 逆元不存在
    else:
        return (x % m + m) % m

# 中国剩余定理模版
def crt(m, r):
    """
    中国剩余定理求解同余方程组
    :param m: 模数数组
    :param r: 余数数组
    :return: 最小正整数解
    """
    n = len(m)
    lcm_val = 1
    # 计算所有模数的乘积
    for i in range(n):
        lcm_val *= m[i]
    
    ans = 0
    for i in range(n):
        # 计算 Mi = lcm / m[i]
        Mi = lcm_val // m[i]
        # 计算 Mi 在模 m[i] 意义下的逆元
        inv_Mi = mod_inverse(Mi, m[i])
        if inv_Mi is None:
            return -1  # 无解
        # 计算 ci = ri * Mi * inv_Mi
        ci = (r[i] * Mi * inv_Mi) % lcm_val
        ans = (ans + ci) % lcm_val
    
    return ans

# 扩展中国剩余定理模版
def excrt(m, r):
    """
    扩展中国剩余定理求解同余方程组
    :param m: 模数数组
    :param r: 余数数组
    :return: 最小非负整数解，无解返回-1
    """
    n = len(m)
    # 初始解为0，模数为1
    ans = 0
    mod = 1
    
    for i in range(n):
        # 当前方程：x ≡ r[i] (mod m[i])
        # 之前的通解：x = ans + k * mod
        # 合并：ans + k * mod ≡ r[i] (mod m[i])
        # 即：k * mod ≡ r[i] - ans (mod m[i])
        
        # 计算 b = r[i] - ans
        b = (r[i] - ans) % m[i]
        # 求解线性同余方程：k * mod ≡ b (mod m[i])
        d, x, y = exgcd(mod, m[i])
        if b % d != 0:
            return -1  # 无解
        x = x * (b // d) % (m[i] // d)
        
        # 更新解和模数
        ans = ans + x * mod
        mod = lcm(mod, m[i])
        ans = (ans % mod + mod) % mod
    
    return ans

def main():
    # 读取输入
    n = int(input())
    r = list(map(int, input().split()))
    m = list(map(int, input().split()))
    
    # 题目输入的余数可能为负所以转化成非负数
    for i in range(n):
        r[i] = (r[i] % m[i] + m[i]) % m[i]
    
    # 使用扩展中国剩余定理解决
    result = excrt(m, r)
    print(result)

if __name__ == "__main__":
    main()