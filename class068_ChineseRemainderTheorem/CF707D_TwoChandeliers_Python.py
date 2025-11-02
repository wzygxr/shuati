#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Codeforces 707D Two chandeliers
链接：https://codeforces.com/contest/1483/problem/D
题目大意：有两个循环亮灯的序列，每天亮一种颜色的灯，老板会在两个灯颜色相同时生气，
          求第k次生气在第几天

算法思路：
这道题可以转化为扩展中国剩余定理问题。
设两个序列的长度分别为n和m，第i天第一个序列亮第((i-1) mod n + 1)个灯，
第二个序列亮第((i-1) mod m + 1)个灯。
当两个灯颜色相同时老板生气。

我们可以枚举所有颜色相同的配对，对于每一对(i,j)满足a[i] = b[j]，
我们需要找到满足以下条件的第k个正整数x：
x ≡ i (mod n)
x ≡ j (mod m)

这就转化为了扩展中国剩余定理问题。

时间复杂度：O(n*m*log(max(n,m)))
空间复杂度：O(n+m)

适用场景：
1. 周期性事件分析
2. 调度问题

注意事项：
1. 需要使用扩展中国剩余定理处理模数不互质的情况
2. 需要处理大数运算

工程化考虑：
1. 输入校验：检查输入是否合法
2. 异常处理：处理无解的情况
3. 优化：可以使用二分查找优化

与其他算法的关联：
1. 扩展中国剩余定理：核心算法
2. 二分查找：可选的优化方法

实际应用：
1. 资源调度
2. 周期性任务协调
"""

import sys
from collections import defaultdict
import math

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

# 龟速乘法，防止溢出
def multiply(a, b, mod):
    a = (a % mod + mod) % mod
    b = (b % mod + mod) % mod
    ans = 0
    while b != 0:
        if (b & 1) != 0:
            ans = (ans + a) % mod
        a = (a + a) % mod
        b >>= 1
    return ans

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

def main():
    # 读取输入
    n, m, k = map(int, input().split())
    a = list(map(int, input().split()))
    b = list(map(int, input().split()))
    
    # 转换为1-indexed
    a = [0] + a
    b = [0] + b
    
    # 构建颜色到位置的映射
    color_to_pos_a = defaultdict(list)
    color_to_pos_b = defaultdict(list)
    
    for i in range(1, n + 1):
        color_to_pos_a[a[i]].append(i)
    
    for i in range(1, m + 1):
        color_to_pos_b[b[i]].append(i)
    
    # 收集所有可能的解
    solutions = []
    
    # 枚举所有颜色相同的配对
    for color, pos_a in color_to_pos_a.items():
        if color in color_to_pos_b:
            pos_b = color_to_pos_b[color]
            
            # 对每一对位置，求解同余方程组
            for i in pos_a:
                for j in pos_b:
                    # x ≡ i (mod n)
                    # x ≡ j (mod m)
                    mods = [n, m]
                    remainders = [i, j]
                    solution = excrt(mods, remainders)
                    if solution != -1:
                        # 生成所有解：solution + k * lcm(n, m)
                        period = lcm(n, m)
                        current = solution
                        while current <= k * period:
                            solutions.append(current)
                            current += period
                            if len(solutions) > k:
                                break
    
    # 排序并取第k个解
    solutions.sort()
    if k <= len(solutions):
        print(solutions[k - 1])
    else:
        print(-1)

if __name__ == "__main__":
    main()