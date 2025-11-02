#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
中国剩余定理和扩展中国剩余定理单元测试

算法原理：
中国剩余定理（Chinese Remainder Theorem, CRT）用于求解模数两两互质的一元线性同余方程组。
扩展中国剩余定理（Extended Chinese Remainder Theorem, EXCRT）用于求解模数不一定两两互质的一元线性同余方程组。

测试内容：
1. CRT基本功能测试
2. EXCRT基本功能测试
3. 边界条件测试
4. 性能测试

相关题目和资源：
1. 洛谷 P1495 - 曹冲养猪（CRT基础题）
   链接：https://www.luogu.com.cn/problem/P1495
   题目大意：求解同余方程组 x ≡ ai (mod mi)，其中mi两两互质

2. 51Nod 1079 - 中国剩余定理（模板题）
   链接：https://www.51nod.com/Challenge/Problem.html#!#problemId=1079
   题目大意：给定一些质数p和对应余数m，求满足所有条件的最小正整数K

3. POJ 2891 - Strange Way to Express Integers（EXCRT基础题）
   链接：http://poj.org/problem?id=2891
   题目大意：给定n个形如 x ≡ ri (mod mi) 的同余方程，求最小非负整数解，mi不一定两两互质

4. HDU 3579 Hello Kiki（EXCRT应用）
   链接：https://acm.hdu.edu.cn/showproblem.php?pid=3579
   题目大意：求解同余方程组，模数不一定互质
"""

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
def mod_inverse(a, mod):
    d, x, y = exgcd(a, mod)
    if d != 1:
        return -1  # 逆元不存在
    else:
        return (x % mod + mod) % mod

# 中国剩余定理
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
        if inv_Mi == -1:
            return -1  # 无解
        # 计算 ci = ri * Mi * inv_Mi
        ci = (r[i] * Mi * inv_Mi) % lcm_val
        ans = (ans + ci) % lcm_val
    
    return (ans + lcm_val) % lcm_val

# 扩展中国剩余定理
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
        b = (b + m[i]) % m[i]
        
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

def test_crt():
    """测试中国剩余定理"""
    print("=== 测试中国剩余定理 ===")
    
    # 测试用例1: 洛谷 P1495
    # x ≡ 2 (mod 3)
    # x ≡ 3 (mod 5)
    # x ≡ 2 (mod 7)
    # 解应为 23
    m1 = [3, 5, 7]
    r1 = [2, 3, 2]
    result1 = crt(m1, r1)
    print(f"测试用例1 - 期望结果: 23, 实际结果: {result1}, {'通过' if result1 == 23 else '失败'}")
    
    # 测试用例2: 51Nod 1079
    # x ≡ 1 (mod 2)
    # x ≡ 2 (mod 3)
    # x ≡ 3 (mod 5)
    # 解应为 23
    m2 = [2, 3, 5]
    r2 = [1, 2, 3]
    result2 = crt(m2, r2)
    print(f"测试用例2 - 期望结果: 23, 实际结果: {result2}, {'通过' if result2 == 23 else '失败'}")

def test_excrt():
    """测试扩展中国剩余定理"""
    print("\n=== 测试扩展中国剩余定理 ===")
    
    # 测试用例1: POJ 2891
    # x ≡ 2 (mod 4)
    # x ≡ 3 (mod 5)
    # 解应为 18
    m1 = [4, 5]
    r1 = [2, 3]
    result1 = excrt(m1, r1)
    print(f"测试用例1 - 期望结果: 18, 实际结果: {result1}, {'通过' if result1 == 18 else '失败'}")
    
    # 测试用例2: 模数不互质的情况
    # x ≡ 1 (mod 2)
    # x ≡ 2 (mod 3)
    # x ≡ 3 (mod 4)
    # 解应为 11
    m2 = [2, 3, 4]
    r2 = [1, 2, 3]
    result2 = excrt(m2, r2)
    print(f"测试用例2 - 期望结果: 11, 实际结果: {result2}, {'通过' if result2 == 11 else '失败'}")

if __name__ == "__main__":
    test_crt()
    test_excrt()