#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
51Nod 1079 中国剩余定理
链接：https://www.51nod.com/Challenge/Problem.html#!#problemId=1079
题目大意：给定一些质数p和对应余数m，求满足所有条件的最小正整数K
          一个正整数K，给出K Mod 一些质数的结果，求符合条件的最小的K。
          例如，K % 2 = 1, K % 3 = 2, K % 5 = 3。符合条件的最小的K = 23。

算法思路：
这是一个标准的中国剩余定理应用题。题目保证给出的质数两两互质，可以直接应用中国剩余定理。

解法步骤：
1. 使用中国剩余定理求解同余方程组
2. 注意输入的质数都是质数，所以两两互质

时间复杂度：O(n^2 log max(mi))，其中n为方程个数，log项来自于扩展欧几里得算法
空间复杂度：O(n)

适用场景：
1. 数论问题求解
2. 密码学中的大数计算

注意事项：
1. 题目保证所有模数都是质数，所以两两互质
2. 需要处理大数运算

工程化考虑：
1. 输入校验：检查输入是否合法
2. 异常处理：处理无解的情况
3. 大数处理：Python天然支持大整数运算

与其他算法的关联：
1. 中国剩余定理：核心算法
2. 扩展欧几里得算法：用于求逆元

实际应用：
1. RSA加密算法中的中国剩余定理加速
2. 多精度整数运算的并行处理
"""

# 扩展欧几里得算法
def exgcd(a, b):
    if b == 0:
        return a, 1, 0
    else:
        d, x, y = exgcd(b, a % b)
        return d, y, x - (a // b) * y

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
    lcm = 1
    # 计算所有模数的乘积
    for i in range(n):
        lcm *= m[i]
    
    ans = 0
    for i in range(n):
        # 计算 Mi = lcm / m[i]
        Mi = lcm // m[i]
        # 计算 Mi 在模 m[i] 意义下的逆元
        inv_Mi = mod_inverse(Mi, m[i])
        if inv_Mi is None:
            return -1  # 无解
        # 计算 ci = ri * Mi * inv_Mi
        ci = (r[i] * Mi * inv_Mi) % lcm
        ans = (ans + ci) % lcm
    
    return ans

def main():
    # 读取输入
    n = int(input())
    m = []
    r = []
    for _ in range(n):
        mi, ri = map(int, input().split())
        m.append(mi)
        r.append(ri)
    
    # 求解并输出结果
    result = crt(m, r)
    print(result)

if __name__ == "__main__":
    main()