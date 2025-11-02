#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
POJ 2891 Strange Way to Express Integers
链接：http://poj.org/problem?id=2891
题目大意：给定n个形如 x ≡ ri (mod mi) 的同余方程，求最小非负整数解，mi不一定两两互质

算法思路：
扩展中国剩余定理用于求解如下形式的一元线性同余方程组（其中m1,m2,...,mk不一定两两互质）：
x ≡ a1 (mod m1)
x ≡ a2 (mod m2)
...
x ≡ ak (mod mk)

解法思路：合并方程
假设我们已经求出前k-1个方程组成的同余方程组的一个解为x，且前k-1个方程模数的最小公倍数为M，
那么前k-1个方程的通解为 x + t * M (t为整数)。
考虑第k个方程 x ≡ ak (mod mk)，将其与前面的通解合并：
x + t * M ≡ ak (mod mk)
t * M ≡ ak - x (mod mk)
这是一个线性同余方程，可以用扩展欧几里得算法求解t。
解出t后，将通解代入得到新的解和新的模数（最小公倍数）。

时间复杂度：O(n log max(mi))，其中n为方程个数，log项来自于扩展欧几里得算法
空间复杂度：O(n)

与普通中国剩余定理的区别：
1. 普通CRT要求模数两两互质，EXCRT不要求
2. EXCRT通过合并方程的方式求解，普通CRT通过构造解的方式求解
3. EXCRT的核心是解线性同余方程，普通CRT的核心是求逆元

适用场景：
1. 模数不互质的同余方程组求解
2. 一些实际问题的建模，如调度问题、周期性问题等

注意事项：
1. 需要熟练掌握扩展欧几里得算法
2. 注意处理无解的情况（当线性同余方程无解时）
3. 大数运算时要注意防止溢出

工程化考虑：
1. 输入校验：检查输入是否合法
2. 异常处理：处理无解的情况
3. 大数处理：Python天然支持大整数运算

与其他算法的关联：
1. 扩展欧几里得算法：核心算法
2. 线性同余方程：EXCRT本质上是解线性同余方程组
3. 最小公倍数：用于合并方程

实际应用：
1. 资源调度问题
2. 周期性任务的协调
3. 密码学中的大数计算
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

# 线性同余方程 ax ≡ b (mod m) 求解
def linear_congruence(a, b, m):
    """
    求解线性同余方程 ax ≡ b (mod m)
    :param a: 系数
    :param b: 余数
    :param m: 模数
    :return: 方程的解，无解返回-1
    """
    d, x, y = exgcd(a, m)
    if b % d != 0:
        return -1  # 无解
    x = x * (b // d) % (m // d)
    return x

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
        k = linear_congruence(mod, b, m[i])
        if k == -1:
            return -1  # 无解
        
        # 更新解和模数
        ans = ans + k * mod
        mod = lcm(mod, m[i])
        ans = (ans % mod + mod) % mod
    
    return ans

def main():
    try:
        while True:
            line = input().strip()
            n = int(line)
            m = []
            r = []
            for _ in range(n):
                mi, ri = map(int, input().split())
                m.append(mi)
                r.append(ri)
            
            # 求解并输出结果
            result = excrt(m, r)
            print(result)
    except EOFError:
        pass

if __name__ == "__main__":
    main()