#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 372. 超级次方
链接：https://leetcode.cn/problems/super-pow/
题目大意：计算 a^b mod 1337，其中b是一个非常大的数，用数组表示

算法思路：
这道题可以使用快速幂和欧拉定理来解决。
根据欧拉定理：如果a和m互质，则 a^φ(m) ≡ 1 (mod m)
其中φ(m)是欧拉函数。

对于这道题，m=1337=7×191，φ(1337)=6×190=1140
所以 a^b ≡ a^(b mod 1140) (mod 1337)，当a和1337互质时

但我们还需要处理a和1337不互质的情况。

另一种思路是使用中国剩余定理：
由于1337=7×191，且gcd(7,191)=1，我们可以分别计算：
x1 = a^b mod 7
x2 = a^b mod 191
然后使用中国剩余定理合并结果。

时间复杂度：O(n)，其中n是数组b的长度
空间复杂度：O(1)

适用场景：
1. 大数幂运算
2. 密码学中的模幂运算

注意事项：
1. 需要处理大数运算
2. 需要考虑a和模数是否互质

工程化考虑：
1. 输入校验：检查输入是否合法
2. 异常处理：处理边界情况
3. 大数处理：使用模运算防止溢出

与其他算法的关联：
1. 快速幂：核心算法
2. 欧拉定理：用于优化指数
3. 中国剩余定理：可选的解法

实际应用：
1. RSA加密算法中的模幂运算
2. 大数计算
"""

# 快速幂
def quick_power(a, b, mod):
    a %= mod
    result = 1
    while b > 0:
        if (b & 1) == 1:
            result = (result * a) % mod
        a = (a * a) % mod
        b >>= 1
    return result

# 扩展欧几里得算法
def exgcd(a, b):
    if b == 0:
        return a, 1, 0
    else:
        d, x, y = exgcd(b, a % b)
        return d, y, x - (a // b) * y

# 求逆元
def mod_inverse(a, mod):
    d, x, y = exgcd(a, mod)
    if d != 1:
        return -1  # 逆元不存在
    else:
        return (x % mod + mod) % mod

# 中国剩余定理
def crt(r1, r2):
    # 1337 = 7 * 191
    m1, m2 = 7, 191
    M = m1 * m2
    
    # M1 = M / m1 = 191
    M1 = M // m1
    # M2 = M / m2 = 7
    M2 = M // m2
    
    # 求逆元
    inv_M1 = mod_inverse(M1, m1)
    inv_M2 = mod_inverse(M2, m2)
    
    # 计算解
    x = (r1 * M1 * inv_M1 + r2 * M2 * inv_M2) % M
    
    return x

def super_pow(a, b):
    """
    计算 a^b mod 1337
    :param a: 底数
    :param b: 指数数组
    :return: 结果
    """
    if a == 0:
        return 0
    
    # 使用中国剩余定理分解
    # 1337 = 7 * 191
    a1 = a % 7
    a2 = a % 191
    
    # 计算 b mod φ(7) = b mod 6
    b1 = 0
    for digit in b:
        b1 = (b1 * 10 + digit) % 6
    
    # 计算 b mod φ(191) = b mod 190
    b2 = 0
    for digit in b:
        b2 = (b2 * 10 + digit) % 190
    
    # 分别计算
    r1 = quick_power(a1, b1, 7)
    r2 = quick_power(a2, b2, 191)
    
    # 使用中国剩余定理合并结果
    return crt(r1, r2)

def main():
    # 测试用例
    print(super_pow(2, [3]))  # 期望输出: 8
    print(super_pow(2, [1, 0]))  # 期望输出: 1024
    print(super_pow(1, [4, 3, 3, 8, 5, 2]))  # 期望输出: 1
    print(super_pow(2147483647, [2, 0, 0]))  # 期望输出: 1198

if __name__ == "__main__":
    main()