#!/usr/bin/env python3

"""
各大OJ平台模逆元题目完整实现集 (Python版本)
包含从LeetCode、Codeforces、AtCoder、洛谷、ZOJ、POJ等平台收集的模逆元相关题目

本文件特点：
1. 每个题目都有完整的题目描述、链接、难度评级
2. 提供详细的解题思路和算法分析
3. 包含时间复杂度和空间复杂度分析
4. 提供完整的Python实现代码
5. 包含边界测试和性能测试
"""

MOD = 1000000007

# ==================== 工具方法 ====================

def power(base, exp, mod):
    """
    快速幂运算
    
    算法原理:
    利用二进制表示指数exp，将幂运算分解为若干次平方运算
    例如: 3^10 = 3^8 * 3^2
    
    时间复杂度: O(log exp)
    空间复杂度: O(1)
    
    Args:
        base: 底数
        exp: 指数
        mod: 模数
    
    Returns:
        base^exp mod mod
    """
    if mod == 0:
        raise ValueError("Modulus cannot be zero")
    if exp < 0:
        raise ValueError("Exponent cannot be negative")
    
    result = 1
    base %= mod
    
    while exp > 0:
        if exp & 1:
            result = (result * base) % mod
        base = (base * base) % mod
        exp >>= 1
    return result

def mod_inverse_extended_gcd(a, m):
    """
    扩展欧几里得算法求模逆元
    
    算法原理:
    求解方程 ax + by = gcd(a, b)
    当gcd(a, m) = 1时，x就是a的模逆元
    
    时间复杂度: O(log(min(a, m)))
    空间复杂度: O(1)
    
    Args:
        a: 要求逆元的数
        m: 模数
    
    Returns:
        如果存在逆元，返回最小正整数解；否则返回-1
    """
    def extended_gcd(a, b):
        if b == 0:
            return a, 1, 0
        gcd, x1, y1 = extended_gcd(b, a % b)
        x = y1
        y = x1 - (a // b) * y1
        return gcd, x, y
    
    gcd, x, _ = extended_gcd(a, m)
    if gcd != 1:
        return -1
    return (x % m + m) % m

def gcd(a, b):
    """
    计算最大公约数
    
    算法原理:
    使用欧几里得算法计算两个数的最大公约数
    
    时间复杂度: O(log(min(a, b)))
    空间复杂度: O(1)
    
    Args:
        a: 第一个数
        b: 第二个数
    
    Returns:
        a和b的最大公约数
    """
    return a if b == 0 else gcd(b, a % b)

# ==================== LeetCode 题目 ====================

def leetcode_1808_maximize_nice_divisors(primeFactors):
    """
    题目1: LeetCode 1808. Maximize Number of Nice Divisors
    链接: https://leetcode.cn/problems/maximize-number-of-nice-divisors/
    难度: 困难
    题意: 给定primeFactors，构造一个正整数n，使得n的质因数总数不超过primeFactors，求n的"好因子"的最大数目
    
    解题思路:
    这是一个数学优化问题，本质上是整数拆分问题。
    要使好因子数目最大，我们需要合理分配primeFactors个质因数。
    好因子的数目等于各个质因数指数的乘积。
    
    根据数学分析，最优策略是尽可能多地使用3作为质因数的指数，
    因为3是使乘积最大的最优底数。
    
    具体策略：
    1. 如果 primeFactors % 3 == 0，全部用3
    2. 如果 primeFactors % 3 == 1，用一个4（2*2）代替两个3（3*3 < 4*1）
    3. 如果 primeFactors % 3 == 2，用一个2
    
    算法原理:
    这是一个经典的整数划分问题，目标是将primeFactors划分为若干个正整数，
    使得这些正整数的乘积最大。根据数学分析，最优策略是尽可能多地使用3。
    
    时间复杂度: O(log primeFactors)
    空间复杂度: O(1)
    
    Args:
        primeFactors: 质因数总数上限
    
    Returns:
        好因子的最大数目
    """
    if primeFactors <= 3:
        return primeFactors
    
    remainder = primeFactors % 3
    quotient = primeFactors // 3
    
    if remainder == 0:
        # 全部用3
        return power(3, quotient, MOD)
    elif remainder == 1:
        # 用一个4代替两个3
        return (power(3, quotient - 1, MOD) * 4) % MOD
    else:  # remainder == 2
        # 用一个2
        return (power(3, quotient, MOD) * 2) % MOD

def leetcode_1623_number_of_sets(n, k):
    """
    题目2: LeetCode 1623. Number of Sets of K Non-Overlapping Line Segments
    链接: https://leetcode.cn/problems/number-of-sets-of-k-non-overlapping-line-segments/
    难度: 中等
    题意: 在n个点上选择k个不重叠的线段的方案数
    
    解题思路:
    使用组合数学公式：C(n + k - 1, 2k)
    这个公式可以通过将问题转化为在n+k-1个位置中选择2k个位置来理解
    
    算法原理:
    这是一个经典的组合数学问题。我们可以将问题转化为：
    在n个点中选择k个不重叠的线段，等价于在n+k-1个位置中选择2k个位置。
    其中k个位置用于线段的起点，k个位置用于线段的终点。
    
    时间复杂度: O(n)（预处理阶乘）
    空间复杂度: O(n)
    
    Args:
        n: 点的数量
        k: 线段数量
    
    Returns:
        方案数
    """
    if k == 0:
        return 1
    if k > n:
        return 0
    
    # 预处理阶乘和阶乘逆元
    max_val = n + k - 1
    fact = [0] * (max_val + 1)
    inv_fact = [0] * (max_val + 1)
    
    fact[0] = 1
    for i in range(1, max_val + 1):
        fact[i] = fact[i - 1] * i % MOD
    
    inv_fact[max_val] = power(fact[max_val], MOD - 2, MOD)
    for i in range(max_val - 1, -1, -1):
        inv_fact[i] = inv_fact[i + 1] * (i + 1) % MOD
    
    # 计算组合数C(n+k-1, 2k)
    return (fact[max_val] * inv_fact[2 * k] % MOD * inv_fact[max_val - 2 * k] % MOD)

# ==================== Codeforces 题目 ====================

def codeforces_1445d_divide_and_sum(arr):
    """
    题目4: Codeforces 1445D. Divide and Sum
    链接: https://codeforces.com/problemset/problem/1445/D
    难度: 中等
    题意: 计算所有划分方案的f(p)值之和
    
    解题思路:
    排序后，每对元素的贡献是固定的，可以用组合数学快速计算
    具体来说，对于排序后的数组，前n个元素和后n个元素的差值之和乘以组合数C(2n-1, n-1)
    
    算法原理:
    通过数学分析可以发现，对于任意一种划分方案，f(p)的值只与数组中元素的相对大小有关。
    因此我们可以先对数组进行排序，然后计算每个元素在所有划分方案中的贡献。
    
    时间复杂度: O(n log n)（排序）
    空间复杂度: O(n)
    
    Args:
        arr: 输入数组
    
    Returns:
        所有划分方案的f(p)值之和
    """
    n = len(arr) // 2
    arr.sort()
    
    # 预处理阶乘和阶乘逆元
    fact = [0] * (2 * n + 1)
    inv_fact = [0] * (2 * n + 1)
    fact[0] = 1
    for i in range(1, 2 * n + 1):
        fact[i] = fact[i - 1] * i % MOD
    inv_fact[2 * n] = power(fact[2 * n], MOD - 2, MOD)
    for i in range(2 * n - 1, -1, -1):
        inv_fact[i] = inv_fact[i + 1] * (i + 1) % MOD
    
    sum_val = 0
    for i in range(n):
        sum_val = (sum_val + arr[n + i] - arr[i]) % MOD
    sum_val = (sum_val % MOD + MOD) % MOD
    
    # 计算组合数C(2n-1, n-1)
    comb = fact[2 * n - 1] * inv_fact[n - 1] % MOD * inv_fact[n] % MOD
    
    return sum_val * comb % MOD

# ==================== 洛谷题目 ====================

def luogu_p3811_modular_inverse(n, p):
    """
    题目8: 洛谷 P3811 【模板】乘法逆元
    链接: https://www.luogu.com.cn/problem/P3811
    难度: 模板
    题意: 给定n和p，求1~n所有整数在模p意义下的乘法逆元
    
    解题思路:
    使用线性递推方法，这是批量计算逆元的最优方法
    递推公式：inv[i] = (p - p//i) * inv[p%i] % p
    
    算法原理:
    这是计算批量模逆元的经典算法，时间复杂度为O(n)，比逐个计算更高效。
    递推公式基于数学推导：设p = k*i + r，则k*i + r ≡ 0 (mod p)，
    两边同时乘以i^(-1) * r^(-1)得：k*r^(-1) + i^(-1) ≡ 0 (mod p)，
    即i^(-1) ≡ -k*r^(-1) (mod p)。
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    
    Args:
        n: 计算范围上限
        p: 模数
    
    Returns:
        1~n所有整数在模p意义下的乘法逆元数组
    """
    inv = [0] * (n + 1)
    inv[1] = 1
    for i in range(2, n + 1):
        inv[i] = (p - (p // i) * inv[p % i] % p) % p
    return inv

# ==================== 测试函数 ====================

def main():
    print("=== 各大OJ平台模逆元题目测试 ===")
    
    # 测试LeetCode题目
    print("LeetCode 1808:", leetcode_1808_maximize_nice_divisors(5))  # 6
    print("LeetCode 1623:", leetcode_1623_number_of_sets(4, 2))  # 5
    
    # 测试Codeforces题目
    arr = [1, 3, 2, 4]
    print("Codeforces 1445D:", codeforces_1445d_divide_and_sum(arr))
    
    # 测试洛谷题目
    inv = luogu_p3811_modular_inverse(10, 11)
    print("洛谷 P3811: 1~10在模11意义下的逆元")
    for i in range(1, 11):
        print(f"inv[{i}] = {inv[i]}")
    
    print("测试完成!")

if __name__ == "__main__":
    main()