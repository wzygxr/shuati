#!/usr/bin/env python3

"""
ZOJ 3609 Modular Inverse
题目链接: http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=3609

题目描述:
给定两个整数a和m，求a在模m意义下的乘法逆元x，使得 a*x ≡ 1 (mod m)
如果不存在这样的x，输出"Not Exist"

解题思路:
方法1: 扩展欧几里得算法
方法2: 费马小定理（当m为质数时）

时间复杂度:
- 扩展欧几里得算法: O(log(min(a, m)))
- 费马小定理: O(log m)

空间复杂度: O(1)

样例输入:
3
3 11
4 12
5 13

样例输出:
4
Not Exist
8
"""


def extended_gcd(a, b):
    """
    扩展欧几里得算法
    求解 ax + by = gcd(a, b)
    
    Args:
        a: 系数a
        b: 系数b
        
    Returns:
        (gcd, x, y): gcd(a, b)和对应的x, y值
    """
    if b == 0:
        return a, 1, 0
    
    gcd, x1, y1 = extended_gcd(b, a % b)
    x = y1
    y = x1 - (a // b) * y1
    
    return gcd, x, y


def mod_inverse(a, m):
    """
    使用扩展欧几里得算法求模逆元
    
    Args:
        a: 要求逆元的数
        m: 模数
        
    Returns:
        如果存在逆元，返回最小正整数解；否则返回-1
    """
    gcd, x, y = extended_gcd(a, m)
    
    # 如果gcd不为1，则逆元不存在
    if gcd != 1:
        return -1
    
    # 确保结果为正数
    return (x % m + m) % m


def power(base, exp, mod):
    """
    快速幂运算
    计算base^exp mod mod
    
    Args:
        base: 底数
        exp: 指数
        mod: 模数
        
    Returns:
        base^exp mod mod
    """
    result = 1
    base %= mod
    
    while exp > 0:
        if exp & 1:
            result = (result * base) % mod
        base = (base * base) % mod
        exp >>= 1
    
    return result


def mod_inverse_fermat(a, p):
    """
    使用费马小定理求模逆元（当模数为质数时）
    根据费马小定理: a^(p-1) ≡ 1 (mod p)
    所以 a^(-1) ≡ a^(p-2) (mod p)
    
    Args:
        a: 要求逆元的数
        p: 质数模数
        
    Returns:
        a在模p意义下的逆元
    """
    return power(a, p - 2, p)


def main():
    """主函数"""
    t = int(input())
    
    for _ in range(t):
        a, m = map(int, input().split())
        
        result = mod_inverse(a, m)
        if result == -1:
            print("Not Exist")
        else:
            print(result)


if __name__ == "__main__":
    main()