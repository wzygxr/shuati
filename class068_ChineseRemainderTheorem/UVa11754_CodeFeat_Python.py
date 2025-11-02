#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
UVa 11754 Code Feat
链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2854
题目大意：给定C个条件，每个条件形如N除以X的余数在集合Y中，求前S个满足条件的数

算法思路：
这道题是扩展中国剩余定理的变种。
题目给出C个条件，每个条件是N % X_i ∈ Y_i，求前S个满足所有条件的正整数N。

解法思路：
1. 对于每个条件，我们有N ≡ y (mod x)，其中y ∈ Y_i
2. 我们可以枚举所有可能的组合，对每个组合使用扩展中国剩余定理求解
3. 由于解可能很大，我们需要找到最小正解，然后生成后续解

时间复杂度：取决于枚举的组合数和EXCRT的复杂度
空间复杂度：O(C + sum of |Y_i|)

适用场景：
1. 约束满足问题
2. 数论问题

注意事项：
1. 需要处理多个余数集合的情况
2. 需要生成前S个解
3. 需要考虑解的周期性

工程化考虑：
1. 输入校验：检查输入是否合法
2. 异常处理：处理无解的情况
3. 优化：可以使用优先队列优化

与其他算法的关联：
1. 扩展中国剩余定理：核心算法
2. 枚举：用于处理多个余数集合
3. 优先队列：可选的优化方法

实际应用：
1. 密码学中的约束满足问题
2. 调度问题
"""

import sys
from collections import deque
import itertools

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
    while True:
        line = input().strip()
        C, S = map(int, line.split())
        
        if C == 0 and S == 0:
            break
        
        mods = []
        remainders = []
        
        for _ in range(C):
            line = input().strip()
            parts = list(map(int, line.split()))
            x = parts[0]
            k = parts[1]
            remainder_set = set(parts[2:2+k])
            
            mods.append(x)
            remainders.append(list(remainder_set))
        
        # 枚举所有可能的组合
        solutions = []
        
        # 生成所有组合的笛卡尔积
        combinations = list(itertools.product(*remainders))
        
        # 对每个组合使用扩展中国剩余定理
        for combination in combinations:
            r = list(combination)
            
            solution = excrt(mods, r)
            if solution != -1:
                # 生成所有解：solution + k * lcm(mods)
                period = 1
                for mod in mods:
                    period = lcm(period, mod)
                
                current = solution
                while current > 0 and len(solutions) < S * 10:  # 多生成一些解用于排序
                    solutions.append(current)
                    current += period
        
        # 排序并取前S个解
        solutions.sort()
        for i in range(min(S, len(solutions))):
            print(solutions[i])
        print()

if __name__ == "__main__":
    try:
        main()
    except EOFError:
        pass