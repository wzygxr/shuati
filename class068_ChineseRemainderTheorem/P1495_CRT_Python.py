#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
洛谷 P1495【模板】中国剩余定理（CRT）/ 曹冲养猪
链接：https://www.luogu.com.cn/problem/P1495
题目大意：求解同余方程组 x ≡ ai (mod mi)，其中mi两两互质

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

算法原理：
中国剩余定理是数论中的一个基本定理，用于求解模数两两互质的一元线性同余方程组。
其核心思想是利用模数两两互质的性质，通过构造特定的解来求出方程组的解。

时间复杂度：O(n^2 log max(mi))，其中n为方程个数，log项来自于扩展欧几里得算法
空间复杂度：O(n)

适用场景：
1. 密码学中的一些算法（如RSA）
2. 大整数计算的并行处理
3. 一些数论问题

注意事项：
1. 要求所有模数两两互质
2. 当模数较大时要注意防止溢出
3. 需要实现扩展欧几里得算法来求逆元

工程化考虑：
1. 输入校验：检查输入是否合法
2. 异常处理：处理无解的情况
3. 大数处理：Python天然支持大整数运算
4. 乘法优化：Python的大整数运算已经优化得很好

与其他算法的关联：
1. 扩展欧几里得算法：用于求解逆元和线性同余方程
2. 快速幂：在某些变种中用于求逆元
3. 线性同余方程：CRT本质上是解线性同余方程组

实际应用：
1. RSA加密算法中的中国剩余定理加速
2. 多精度整数运算的并行处理
3. 信号处理中的采样定理

相关题目：
1. 51Nod 1079 - 中国剩余定理
   链接：https://www.51nod.com/Challenge/Problem.html#!#problemId=1079
   题目大意：给定一些质数p和对应余数m，求满足所有条件的最小正整数K

2. POJ 1006 Biorhythms
   链接：http://poj.org/problem?id=1006
   题目大意：人的体力、情感和智力周期分别为23天、28天和33天，已知某一天三个指标的数值，求下一次三个指标同时达到峰值的天数

3. UVA 756 Biorhythms
   链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=697
   题目大意：与POJ 1006相同

4. 牛客网 - NC15857 同余方程
   链接：https://ac.nowcoder.com/acm/problem/15857
   题目大意：求解同余方程组，模数两两互质
"""

import sys

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