"""
模逆元完整题目集（Python版）
包含从各大OJ平台收集的模逆元相关题目

模逆元定义：对于整数a和模数m，如果存在整数x使得 a*x ≡ 1 (mod m)，则称x为a在模m意义下的乘法逆元
模逆元存在的充要条件：gcd(a, m) = 1，即a和m互质

时间复杂度分析：
- 扩展欧几里得算法：O(log(min(a, m)))
- 费马小定理：O(log m)（仅当m为质数时）
- 线性递推：O(n)（批量计算1~n的逆元）

空间复杂度分析：
- 扩展欧几里得算法：O(log(min(a, m)))（递归栈）
- 费马小定理：O(1)
- 线性递推：O(n)（存储逆元数组）

Python特性注意事项：
1. 使用内置pow函数进行快速幂：pow(a, b, mod)
2. 使用math.gcd计算最大公约数
3. 负数取模自动处理
4. 支持大整数运算，无需担心溢出
5. 使用列表推导式和生成器表达式
6. 使用装饰器进行性能测试
"""

import math
import time
import sys
from typing import List, Tuple, Optional
from functools import lru_cache
import random

MOD = 10**9 + 7

# ==================== 基础算法实现 ====================

def mod_inverse_extended_gcd(a: int, m: int) -> int:
    """
    扩展欧几里得算法求模逆元（通用方法）
    适用于任何模数，是最通用的模逆元求解方法
    
    时间复杂度: O(log(min(a, m)))
    空间复杂度: O(log(min(a, m)))（递归调用栈）
    
    Args:
        a: 要求逆元的数
        m: 模数
    
    Returns:
        如果存在逆元，返回最小正整数解；否则返回-1
    
    Raises:
        ValueError: 模数为0时抛出异常
    """
    if m == 0:
        raise ValueError("Modulus cannot be zero")
    
    # 处理负数情况，确保a和m都是正数
    a = (a % m + m) % m
    m = abs(m)
    
    def extended_gcd(a: int, b: int) -> Tuple[int, int, int]:
        """扩展欧几里得算法实现"""
        if b == 0:
            return a, 1, 0
        gcd, x1, y1 = extended_gcd(b, a % b)
        x = y1
        y = x1 - (a // b) * y1
        return gcd, x, y
    
    gcd, x, y = extended_gcd(a, m)
    
    # 逆元存在的充要条件是a和m互质
    if gcd != 1:
        return -1  # 逆元不存在
    
    # 确保结果为正数
    return (x % m + m) % m

def power(base: int, exp: int, mod: int) -> int:
    """
    快速幂运算
    计算base^exp mod mod
    
    时间复杂度: O(log exp)
    空间复杂度: O(1)
    
    Args:
        base: 底数
        exp: 指数
        mod: 模数
    
    Returns:
        base^exp mod mod
    
    Raises:
        ValueError: 模数为0或指数为负数时抛出异常
    """
    if mod == 0:
        raise ValueError("Modulus cannot be zero")
    if exp < 0:
        raise ValueError("Exponent cannot be negative")
    
    # 使用Python内置pow函数，效率更高
    return pow(base, exp, mod)

def mod_inverse_fermat(a: int, p: int) -> int:
    """
    费马小定理求模逆元（仅当模数为质数时）
    根据费马小定理：a^(p-1) ≡ 1 (mod p)，所以 a^(-1) ≡ a^(p-2) (mod p)
    
    时间复杂度: O(log p)
    空间复杂度: O(1)
    
    Args:
        a: 要求逆元的数
        p: 质数模数
    
    Returns:
        a在模p意义下的逆元
    
    Raises:
        ValueError: 模数小于等于1时抛出异常
    """
    if p <= 1:
        raise ValueError("Prime modulus must be greater than 1")
    a = (a % p + p) % p
    return pow(a, p - 2, p)

def build_inverse_all(n: int, p: int) -> List[int]:
    """
    线性递推求1~n所有整数的模逆元
    递推公式：inv[i] = (p - p//i) * inv[p%i] % p
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    
    Args:
        n: 范围上限
        p: 模数（质数）
    
    Returns:
        逆元列表
    """
    inv = [0] * (n + 1)
    inv[1] = 1
    for i in range(2, n + 1):
        inv[i] = (p - (p // i) * inv[p % i] % p) % p
    return inv

# ==================== 各大OJ平台题目实现 ====================

def solve_zoj_3609():
    """
    题目1: ZOJ 3609 Modular Inverse
    链接: http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=3609
    难度: 简单
    题意: 给定a和m，求a在模m意义下的乘法逆元
    """
    t = int(input().strip())
    for _ in range(t):
        a, m = map(int, input().split())
        result = mod_inverse_extended_gcd(a, m)
        print("Not Exist" if result == -1 else result)

def solve_luogu_p3811():
    """
    题目2: 洛谷 P3811 【模板】乘法逆元
    链接: https://www.luogu.com.cn/problem/P3811
    难度: 模板
    题意: 给定n和p，求1~n所有整数在模p意义下的乘法逆元
    """
    n, p = map(int, input().split())
    inv = build_inverse_all(n, p)
    for i in range(1, n + 1):
        print(inv[i])

def solve_luogu_p2613():
    """
    题目3: 洛谷 P2613 【模板】有理数取余
    链接: https://www.luogu.com.cn/problem/P2613
    难度: 模板
    题意: 计算两个大整数的除法结果模19260817
    """
    a = int(input().strip())
    b = int(input().strip())
    mod = 19260817
    
    if b == 0:
        print("Angry!")
    else:
        # 使用Python内置的pow函数求逆元
        result = a * pow(b, mod - 2, mod) % mod
        print(result)

def sum_div(A: int, B: int) -> int:
    """
    题目4: POJ 1845 Sumdiv
    链接: http://poj.org/problem?id=1845
    难度: 中等
    题意: 计算A^B的所有约数之和模9901
    """
    MOD = 9901
    if A == 0:
        return 0
    if B == 0:
        return 1
    
    result = 1
    # 质因数分解
    for i in range(2, int(math.isqrt(A)) + 1):
        if A % i == 0:
            cnt = 0
            while A % i == 0:
                cnt += 1
                A //= i
            # 计算等比数列和: (i^(cnt*B+1)-1)/(i-1) mod MOD
            numerator = (pow(i, cnt * B + 1, MOD) - 1 + MOD) % MOD
            denominator = mod_inverse_extended_gcd(i - 1, MOD)
            if denominator == -1:
                # 当i-1 ≡ 0 mod MOD时，等比数列和为cnt*B+1
                result = result * (cnt * B + 1) % MOD
            else:
                result = result * numerator % MOD * denominator % MOD
    
    if A > 1:
        numerator = (pow(A, B + 1, MOD) - 1 + MOD) % MOD
        denominator = mod_inverse_extended_gcd(A - 1, MOD)
        if denominator == -1:
            result = result * (B + 1) % MOD
        else:
            result = result * numerator % MOD * denominator % MOD
    
    return result

def divide_and_sum(arr: List[int]) -> int:
    """
    题目5: Codeforces 1445D. Divide and Sum
    链接: https://codeforces.com/problemset/problem/1445/D
    难度: 中等
    题意: 计算所有划分方案的f(p)值之和
    """
    MOD = 998244353
    n = len(arr) // 2
    arr.sort()
    
    # 预处理阶乘和阶乘逆元
    fact = [1] * (2 * n + 1)
    inv_fact = [1] * (2 * n + 1)
    
    for i in range(1, 2 * n + 1):
        fact[i] = fact[i - 1] * i % MOD
    
    inv_fact[2 * n] = pow(fact[2 * n], MOD - 2, MOD)
    for i in range(2 * n - 1, -1, -1):
        inv_fact[i] = inv_fact[i + 1] * (i + 1) % MOD
    
    total_sum = 0
    for i in range(n):
        total_sum = (total_sum + arr[n + i] - arr[i]) % MOD
    
    total_sum = (total_sum % MOD + MOD) % MOD
    
    # 计算组合数C(2n-1, n-1)
    comb = fact[2 * n - 1] * inv_fact[n - 1] % MOD * inv_fact[n] % MOD
    
    return total_sum * comb % MOD

def throne(N: int, S: int, K: int) -> int:
    """
    题目6: AtCoder ABC182E. Throne
    链接: https://atcoder.jp/contests/abc182/tasks/abc182_e
    难度: 中等
    题意: 在圆桌上移动，求到达特定位置的最小步数
    """
    # 解方程: (S + K*x) ≡ 0 (mod N)
    # 即 K*x ≡ -S (mod N)
    g = math.gcd(K, N)
    if S % g != 0:
        return -1
    
    new_N = N // g
    new_K = K // g
    new_S = (-S) // g
    
    inv = mod_inverse_extended_gcd(new_K, new_N)
    if inv == -1:
        return -1
    
    x = (inv * new_S % new_N + new_N) % new_N
    return x

def fombinatorial(n: int, m: int, mod: int) -> int:
    """
    题目7: CodeChef FOMBINATORIAL
    链接: https://www.codechef.com/problems/FOMBINATORIAL
    难度: 中等
    题意: 计算组合数取模
    """
    # 预处理阶乘和阶乘逆元
    fact = [1] * (n + 1)
    inv_fact = [1] * (n + 1)
    
    for i in range(1, n + 1):
        fact[i] = fact[i - 1] * i % mod
    
    inv_fact[n] = pow(fact[n], mod - 2, mod)
    for i in range(n - 1, -1, -1):
        inv_fact[i] = inv_fact[i + 1] * (i + 1) % mod
    
    # 计算组合数C(n, m)
    return fact[n] * inv_fact[m] % mod * inv_fact[n - m] % mod

def bulls_and_cows(n: int, k: int) -> int:
    """
    题目8: USACO 2009 Feb Gold Bulls and Cows
    链接: http://www.usaco.org/index.php?page=viewproblem2&cpid=862
    难度: 中等
    题意: 计算满足特定条件的排列数
    """
    MOD = 10**9 + 7
    # 使用组合数学和模逆元计算
    fact = [1] * (n + 1)
    inv_fact = [1] * (n + 1)
    
    for i in range(1, n + 1):
        fact[i] = fact[i - 1] * i % MOD
    
    inv_fact[n] = pow(fact[n], MOD - 2, MOD)
    for i in range(n - 1, -1, -1):
        inv_fact[i] = inv_fact[i + 1] * (i + 1) % MOD
    
    result = 0
    for i in range(k + 1):
        term = fact[n - i] * inv_fact[i] % MOD * inv_fact[k - i] % MOD
        if i % 2 == 0:
            result = (result + term) % MOD
        else:
            result = (result - term + MOD) % MOD
    
    return result

def niuniu_arithmetic(n: int, mod: int) -> int:
    """
    题目9: 牛客练习赛68 B. 牛牛的算术
    链接: https://ac.nowcoder.com/acm/contest/11173/B
    难度: 中等
    题意: 计算表达式的值
    """
    # 计算 1! + 2! + ... + n! mod mod
    total_sum = 0
    fact = 1
    for i in range(1, n + 1):
        fact = fact * i % mod
        total_sum = (total_sum + fact) % mod
    return total_sum

def minimum_total(triangle: List[List[int]]) -> int:
    """
    题目10: LintCode 109 数字三角形
    链接: https://www.lintcode.com/problem/109/
    难度: 简单
    题意: 求从顶部到底部的最大路径和
    """
    n = len(triangle)
    dp = [[0] * n for _ in range(n)]
    
    for i in range(n - 1, -1, -1):
        for j in range(i + 1):
            if i == n - 1:
                dp[i][j] = triangle[i][j]
            else:
                dp[i][j] = triangle[i][j] + min(dp[i + 1][j], dp[i + 1][j + 1])
    
    return dp[0][0]

def solve_jisuanke_a1638():
    """
    题目11: 计蒜客 A1638 逆元
    链接: https://nanti.jisuanke.com/t/A1638
    难度: 简单
    题意: 求单个数的模逆元
    """
    t = int(input().strip())
    for _ in range(t):
        a, p = map(int, input().split())
        result = mod_inverse_extended_gcd(a, p)
        print("Not Exist" if result == -1 else result)

# ==================== 性能测试装饰器 ====================

def timer(func):
    """性能测试装饰器"""
    def wrapper(*args, **kwargs):
        start = time.time()
        result = func(*args, **kwargs)
        end = time.time()
        print(f"{func.__name__} 执行时间: {end - start:.6f}秒")
        return result
    return wrapper

# ==================== 测试函数 ====================

@timer
def test_basic_algorithms():
    """测试基础算法"""
    print("基础算法测试:")
    print(f"mod_inverse_extended_gcd(3, 11) = {mod_inverse_extended_gcd(3, 11)}")  # 4
    print(f"mod_inverse_fermat(5, 13) = {mod_inverse_fermat(5, 13)}")  # 8

@timer
def test_oj_problems():
    """测试各大OJ题目"""
    print("\n各大OJ题目测试:")
    
    # POJ 1845
    print(f"POJ 1845 Sumdiv: sum_div(2, 3) = {sum_div(2, 3)}")  # 15
    
    # AtCoder ABC182E
    print(f"AtCoder ABC182E Throne: throne(10, 4, 3) = {throne(10, 4, 3)}")
    
    # CodeChef FOMBINATORIAL
    print(f"CodeChef FOMBINATORIAL: fombinatorial(5, 2, MOD) = {fombinatorial(5, 2, MOD)}")

@timer
def test_edge_cases():
    """测试边界情况"""
    print("\n边界测试:")
    print(f"mod_inverse_extended_gcd(0, 5) = {mod_inverse_extended_gcd(0, 5)}")  # -1
    print(f"mod_inverse_extended_gcd(6, 8) = {mod_inverse_extended_gcd(6, 8)}")  # -1

@timer
def test_performance():
    """性能测试"""
    print("\n性能测试:")
    start = time.time()
    inv = build_inverse_all(1000000, MOD)
    end = time.time()
    print(f"线性递推计算1~1000000的逆元耗时: {end - start:.6f}秒")

def main():
    """主函数"""
    print("=== 模逆元完整题目集测试（Python版）===")
    
    test_basic_algorithms()
    test_oj_problems()
    test_edge_cases()
    test_performance()
    
    print("\n测试完成!")

if __name__ == "__main__":
    main()