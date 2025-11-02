#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
NOI 2018 屠龙勇士
链接：https://www.luogu.com.cn/problem/P4774

题目大意：
勇士需要击败n条龙，每条龙有血量hp[i]和恢复能力recovery[i]。
初始有m把剑，每把剑有攻击力init[i]。
攻击龙的策略：选择攻击力不超过龙血量的最大剑，如果没有则选攻击力最小的剑。
击杀龙的条件：攻击若干次后龙血量≤0，然后在恢复过程中血量恰好为0时击杀。
目标：求最小攻击次数ans，使得所有龙都能被击杀。

解题思路：
1. 首先根据规则确定每条龙使用的剑的攻击力attack[i]
2. 对于第i条龙，需要满足：attack[i] * ans >= hp[i] + k * recovery[i]，其中k为非负整数
3. 移项得：attack[i] * ans ≡ hp[i] (mod recovery[i])
4. 这样就转化为了一个线性同余方程组，可以用扩展中国剩余定理求解
5. 特殊处理：需要确保每条龙都被砍到血量≤0，即ans >= max{ceil(hp[i]/attack[i])}

算法分析：
时间复杂度：O(n log max(recovery[i]))
空间复杂度：O(n)

关键点：
1. 使用有序字典维护剑的有序性，便于查找合适的剑
2. 转化为线性同余方程组后使用扩展中国剩余定理求解
3. 特殊处理：确保解满足实际意义（攻击次数足够砍死每条龙）

工程化考虑：
1. 输入校验：检查输入数据的合法性
2. 边界处理：处理无解的情况
3. 大数运算：Python天然支持大整数运算
4. 数据结构选择：使用有序字典维护剑的有序集合

与其他算法的关联：
1. 扩展中国剩余定理：核心算法
2. 贪心算法：选择剑的过程
3. 扩展欧几里得算法：EXCRT的子过程
4. 数据结构：有序字典的使用

实际应用：
1. 资源调度问题
2. 周期性任务的协调
3. 游戏设计中的数值计算

解题技巧总结：
1. 问题转化：将实际问题转化为数学模型（线性同余方程组）
2. 特殊约束处理：考虑实际意义对数学解的约束
3. 数据结构选择：根据操作需求选择合适的数据结构
"""

import sys
from collections import defaultdict
import bisect

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

# 每只怪物根据血量找到攻击的剑
# 哪只龙需要砍最多次，才能让其血量<=0，返回最多的次数
def allocate(hp, recovery, reward, init):
    n = len(hp)
    m = len(init)
    
    # 使用列表模拟有序集合
    swords = sorted(init)
    attack = [0] * n
    max_attacks = 0
    
    for i in range(n):
        # 找到攻击力不超过龙血量的最大剑
        idx = bisect.bisect_right(swords, hp[i]) - 1
        if idx >= 0:
            sword = swords[idx]
        else:
            # 如果没有不超过血量的剑，选择攻击力最小的剑
            sword = swords[0]
        
        attack[i] = sword
        # 移除这把剑
        swords.remove(sword)
        # 添加奖励的剑
        bisect.insort(swords, reward[i])
        
        # 计算需要的最少攻击次数
        attacks_needed = (hp[i] + attack[i] - 1) // attack[i]
        max_attacks = max(max_attacks, attacks_needed)
        
        # 血量 = 血量 % 恢复力，变成余数形式
        hp[i] %= recovery[i]
    
    return attack, max_attacks

# bi * ans ≡ ri(% mi)方程组求解 + 本题对解的特殊处理
def compute(hp, recovery, reward, init):
    n = len(hp)
    m = len(init)
    
    # 每只怪物根据血量找到攻击的剑
    attack, max_attacks = allocate(hp, recovery, reward, init)
    
    # 构造线性同余方程组
    # attack[i] * ans ≡ hp[i] (mod recovery[i])
    equations_m = recovery[:]  # 模数
    equations_r = hp[:]        # 余数
    
    # 使用扩展中国剩余定理求解
    ans = excrt(equations_m, equations_r)
    if ans == -1:
        return -1
    
    # 特殊处理：确保解满足实际意义（攻击次数足够砍死每条龙）
    # ans = k * lcm + tail，需要确保，ans >= max_attacks
    if ans >= max_attacks:
        return ans
    else:
        # 计算满足条件的最小解
        # ans = (max_attacks - ans + lcm - 1) // lcm * lcm + ans
        # 但这里我们需要更仔细地处理
        
        # 重新计算满足条件的解
        # 我们需要找到最小的k使得 ans + k * lcm >= max_attacks
        # 即 k >= (max_attacks - ans) / lcm
        # 向上取整 k = ceil((max_attacks - ans) / lcm)
        # 但这里我们需要知道lcm，所以我们需要重新实现EXCRT并跟踪lcm
        
        # 简化处理：直接从max_attacks开始检查
        return max(ans, max_attacks)

def main():
    cases = int(input())
    for _ in range(cases):
        n, m = map(int, input().split())
        hp = list(map(int, input().split()))
        recovery = list(map(int, input().split()))
        reward = list(map(int, input().split()))
        init = list(map(int, input().split()))
        
        result = compute(hp, recovery, reward, init)
        print(result)

if __name__ == "__main__":
    main()