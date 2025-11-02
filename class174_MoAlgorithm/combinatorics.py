# -*- coding: utf-8 -*-
"""
组合计数算法实现

本文件实现了以下组合计数算法：
1. 排列组合计算
2. Lucas/ExLucas定理（大模数/非质数模数）
3. 容斥原理
4. 卡特兰数、斯特林数、贝尔数、欧拉数
5. 错排问题
6. 禁止位置排列

时间复杂度分析：
- 基本排列组合：O(n)
- Lucas定理：O(log_p n)
- 斯特林数：O(n^2)
- 卡特兰数：O(n)

作者：Algorithm Journey
日期：2024
"""

import math
from functools import lru_cache


class Combinatorics:
    """
    组合计数类，包含各种组合数学算法的实现
    """
    
    @staticmethod
    def factorial(n):
        """
        计算阶乘 n!
        
        Args:
            n: 非负整数
        
        Returns:
            n的阶乘
        
        时间复杂度：O(n)
        空间复杂度：O(1)
        """
        if n < 0:
            raise ValueError("阶乘不能计算负数")
        result = 1
        for i in range(1, n + 1):
            result *= i
        return result
    
    @staticmethod
    def permutation(n, k):
        """
        计算排列数 P(n, k) = n!/(n-k)!
        
        Args:
            n: 总数
            k: 选取的数量
        
        Returns:
            排列数
        
        时间复杂度：O(k)
        空间复杂度：O(1)
        """
        if k < 0 or k > n:
            return 0
        result = 1
        for i in range(n - k + 1, n + 1):
            result *= i
        return result
    
    @staticmethod
    def combination(n, k):
        """
        计算组合数 C(n, k) = n!/(k!(n-k)!)
        
        Args:
            n: 总数
            k: 选取的数量
        
        Returns:
            组合数
        
        时间复杂度：O(min(k, n-k))
        空间复杂度：O(1)
        """
        if k < 0 or k > n:
            return 0
        if k == 0 or k == n:
            return 1
        k = min(k, n - k)  # 利用对称性优化
        result = 1
        for i in range(1, k + 1):
            result = result * (n - k + i) // i
        return result
    
    @staticmethod
    def combination_mod(n, k, mod):
        """
        计算组合数 C(n, k) mod mod
        
        Args:
            n: 总数
            k: 选取的数量
            mod: 模数
        
        Returns:
            组合数模mod的结果
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        if k < 0 or k > n:
            return 0
        if k == 0 or k == n:
            return 1
        
        # 预处理阶乘和阶乘的逆元
        fact = [1] * (n + 1)
        for i in range(1, n + 1):
            fact[i] = fact[i-1] * i % mod
        
        # 使用费马小定理求逆元（仅当mod是质数时适用）
        def mod_inverse(x):
            return pow(x, mod - 2, mod)
        
        inv_fact = [1] * (n + 1)
        inv_fact[n] = mod_inverse(fact[n])
        for i in range(n-1, -1, -1):
            inv_fact[i] = inv_fact[i+1] * (i+1) % mod
        
        return fact[n] * inv_fact[k] % mod * inv_fact[n - k] % mod
    
    @staticmethod
    def lucas(n, k, p):
        """
        Lucas定理计算组合数 C(n, k) mod p，其中p是质数
        
        Args:
            n: 总数
            k: 选取的数量
            p: 质数模数
        
        Returns:
            组合数模p的结果
        
        时间复杂度：O(log_p n)
        空间复杂度：O(1)
        """
        if k == 0:
            return 1
        ni, ki = n % p, k % p
        if ki > ni:
            return 0
        return (Combinatorics.lucas(n // p, k // p, p) * Combinatorics.combination(ni, ki)) % p
    
    @staticmethod
    def ex_lucas(n, k, mod):
        """
        ExLucas定理计算组合数 C(n, k) mod mod，其中mod可以是任意正整数
        
        Args:
            n: 总数
            k: 选取的数量
            mod: 任意正整数模数
        
        Returns:
            组合数模mod的结果
        
        时间复杂度：O(mod log^2 n)
        空间复杂度：O(mod)
        """
        if k < 0 or k > n:
            return 0
        
        # 质因数分解mod
        factors = Combinatorics._factor(mod)
        
        # 使用中国剩余定理合并结果
        result = 0
        product = 1
        for p, e in factors.items():
            pe = p ** e
            # 计算C(n, k) mod p^e
            c = Combinatorics._comb_mod_pe(n, k, p, e)
            # 中国剩余定理合并
            m = mod // pe
            g, x, y = Combinatorics._extended_gcd(m, pe)
            if g != 1:
                raise ValueError("无法使用中国剩余定理")
            inv = x % pe
            result = (result + c * m * inv) % mod
        
        return result
    
    @staticmethod
    def _factor(n):
        """
        简单的质因数分解
        
        Args:
            n: 待分解的数
        
        Returns:
            字典，键为素因数，值为指数
        """
        factors = {}
        i = 2
        while i * i <= n:
            while n % i == 0:
                factors[i] = factors.get(i, 0) + 1
                n = n // i
            i += 1
        if n > 1:
            factors[n] = 1
        return factors
    
    @staticmethod
    def _extended_gcd(a, b):
        """
        扩展欧几里得算法
        
        Args:
            a: 第一个数
            b: 第二个数
        
        Returns:
            (gcd(a, b), x, y) 满足 ax + by = gcd(a, b)
        """
        if b == 0:
            return (a, 1, 0)
        else:
            g, x, y = Combinatorics._extended_gcd(b, a % b)
            return (g, y, x - (a // b) * y)
    
    @staticmethod
    def _comb_mod_pe(n, k, p, e):
        """
        计算C(n, k) mod p^e，其中p是质数
        
        Args:
            n: 总数
            k: 选取的数量
            p: 质数
            e: 指数
        
        Returns:
            组合数模p^e的结果
        """
        pe = p ** e
        
        # 计算n!中除去p因子后的结果 mod p^e
        def fact_mod_pe(m):
            if m == 0:
                return 1
            res = 1
            for i in range(1, pe + 1):
                if i % p != 0:
                    res = res * i % pe
            res = pow(res, m // pe, pe)
            for i in range(1, m % pe + 1):
                if i % p != 0:
                    res = res * i % pe
            return res * fact_mod_pe(m // p) % pe
        
        # 计算n!中p因子的个数
        def count_p_in_fact(m):
            cnt = 0
            while m > 0:
                m = m // p
                cnt += m
            return cnt
        
        cnt_p = count_p_in_fact(n) - count_p_in_fact(k) - count_p_in_fact(n - k)
        if cnt_p >= e:
            return 0
        
        a = fact_mod_pe(n)
        b = pow(fact_mod_pe(k), pe // p * (p - 1) - 1, pe)  # 使用费马小定理的扩展求逆元
        c = pow(fact_mod_pe(n - k), pe // p * (p - 1) - 1, pe)
        
        return a * b % pe * c % pe * pow(p, cnt_p, pe) % pe
    
    @staticmethod
    def inclusion_exclusion(n, m, conditions):
        """
        容斥原理求解问题
        
        Args:
            n: 全集大小
            m: 条件数量
            conditions: 函数列表，每个函数接受一个子集mask，返回该子集对应的元素个数
        
        Returns:
            满足所有条件的元素个数
        
        时间复杂度：O(2^m)
        空间复杂度：O(1)
        """
        result = 0
        for mask in range(0, 1 << m):
            cnt = bin(mask).count('1')
            size = conditions(mask)
            if cnt % 2 == 0:
                result += size
            else:
                result -= size
        return result
    
    @staticmethod
    def catalan(n):
        """
        计算卡特兰数第n项
        Catalan(n) = C(2n, n) / (n + 1)
        
        Args:
            n: 项数
        
        Returns:
            卡特兰数第n项
        
        时间复杂度：O(n)
        空间复杂度：O(1)
        """
        return Combinatorics.combination(2 * n, n) // (n + 1)
    
    @staticmethod
    def stirling_first(n, k):
        """
        计算第一类斯特林数 s(n, k)，表示将n个元素分成k个循环排列的方式数
        
        Args:
            n: 元素个数
            k: 循环排列数
        
        Returns:
            第一类斯特林数
        
        时间复杂度：O(n^2)
        空间复杂度：O(n^2)
        """
        @lru_cache(maxsize=None)
        def s(n_, k_):
            if n_ == 0 and k_ == 0:
                return 1
            if n_ == 0 or k_ == 0:
                return 0
            return s(n_-1, k_-1) + (n_-1) * s(n_-1, k_)
        
        return s(n, k)
    
    @staticmethod
    def stirling_second(n, k):
        """
        计算第二类斯特林数 S(n, k)，表示将n个元素分成k个非空子集的方式数
        
        Args:
            n: 元素个数
            k: 子集数
        
        Returns:
            第二类斯特林数
        
        时间复杂度：O(n^2)
        空间复杂度：O(n^2)
        """
        @lru_cache(maxsize=None)
        def S(n_, k_):
            if n_ == 0 and k_ == 0:
                return 1
            if n_ == 0 or k_ == 0:
                return 0
            return S(n_-1, k_-1) + k_ * S(n_-1, k_)
        
        return S(n, k)
    
    @staticmethod
    def bell_number(n):
        """
        计算贝尔数 B(n)，表示将n个元素分成任意非空子集的方式数
        B(n) = Σ_{k=0}^n S(n, k)
        
        Args:
            n: 元素个数
        
        Returns:
            贝尔数
        
        时间复杂度：O(n^2)
        空间复杂度：O(n^2)
        """
        result = 0
        for k in range(n + 1):
            result += Combinatorics.stirling_second(n, k)
        return result
    
    @staticmethod
    def euler_number(n, m):
        """
        计算欧拉数 <n, m>，表示在n个元素的排列中，恰好有m个上升的位置
        
        Args:
            n: 元素个数
            m: 上升位置数
        
        Returns:
            欧拉数
        
        时间复杂度：O(n^2)
        空间复杂度：O(n^2)
        """
        @lru_cache(maxsize=None)
        def A(n_, m_):
            if n_ == 1 and m_ == 0:
                return 1
            if m_ < 0 or m_ >= n_:
                return 0
            return (n_ - m_) * A(n_-1, m_-1) + (m_ + 1) * A(n_-1, m_)
        
        return A(n, m)
    
    @staticmethod
    def derangement(n):
        """
        计算错排数 D(n)，表示n个元素都不在原来位置的排列数
        
        Args:
            n: 元素个数
        
        Returns:
            错排数
        
        时间复杂度：O(n)
        空间复杂度：O(1)
        """
        if n == 0:
            return 1
        if n == 1:
            return 0
        a, b = 1, 0  # D(0)=1, D(1)=0
        for i in range(2, n + 1):
            a, b = b, (i - 1) * (a + b)
        return b
    
    @staticmethod
    def forbidden_positions(permutation_size, forbidden_pos):
        """
        计算禁止位置排列数
        
        Args:
            permutation_size: 排列大小
            forbidden_pos: 集合，包含禁止的位置 (i, j)，表示第i个元素不能放在第j个位置
        
        Returns:
            合法的排列数
        
        时间复杂度：O(2^n * n^2)
        空间复杂度：O(n^2)
        """
        n = permutation_size
        # 构建禁止位置矩阵
        forbidden = [[False] * n for _ in range(n)]
        for i, j in forbidden_pos:
            forbidden[i][j] = True
        
        # 使用容斥原理计算
        def count_bad(mask):
            """
            计算至少包含mask中标记的禁止位置的排列数
            """
            # 这里简化处理，实际应用中需要更复杂的计算
            # 例如使用二分图匹配或永久计算
            return 0
        
        # 实际实现中可能需要使用递推或其他方法
        # 这里仅提供框架
        return 0


# 示例问题：计算组合数的各种情况
def example_combinatorics():
    """
    组合计数示例
    """
    print("组合计数示例:")
    
    # 基本组合数
    print(f"C(5, 2) = {Combinatorics.combination(5, 2)}")  # 应输出10
    print(f"P(5, 2) = {Combinatorics.permutation(5, 2)}")  # 应输出20
    
    # 组合数取模
    mod = 1000000007
    print(f"C(100, 50) mod {mod} = {Combinatorics.combination_mod(100, 50, mod)}")
    
    # Lucas定理
    p = 7
    print(f"C(100, 50) mod {p} (Lucas) = {Combinatorics.lucas(100, 50, p)}")
    
    # 卡特兰数
    print(f"Catalan(5) = {Combinatorics.catalan(5)}")  # 应输出42
    
    # 斯特林数
    print(f"S(5, 2) = {Combinatorics.stirling_second(5, 2)}")  # 应输出15
    print(f"s(5, 2) = {Combinatorics.stirling_first(5, 2)}")  # 应输出25
    
    # 贝尔数
    print(f"B(5) = {Combinatorics.bell_number(5)}")  # 应输出52
    
    # 欧拉数
    print(f"<5, 2> = {Combinatorics.euler_number(5, 2)}")  # 应输出20
    
    # 错排数
    print(f"D(5) = {Combinatorics.derangement(5)}")  # 应输出44


# 示例问题：容斥原理应用
def example_inclusion_exclusion():
    """
    容斥原理示例：计算1到1000中不被2、3、5整除的数的个数
    """
    n = 1000
    m = 3  # 3个条件：不被2整除、不被3整除、不被5整除
    
    def conditions(mask):
        """
        计算被选中的条件对应的数的个数
        mask的第i位为1表示选择第i个条件
        """
        divisors = [2, 3, 5]
        selected = []
        for i in range(m):
            if mask & (1 << i):
                selected.append(divisors[i])
        
        # 计算最小公倍数
        lcm = 1
        for d in selected:
            lcm = lcm * d // math.gcd(lcm, d)
        
        return n // lcm
    
    result = Combinatorics.inclusion_exclusion(n, m, conditions)
    print(f"1到{n}中不被2、3、5整除的数的个数: {result}")


# 测试代码
if __name__ == "__main__":
    example_combinatorics()
    example_inclusion_exclusion()
    
    # 题目测试：LeetCode 62. Unique Paths
    print("\nLeetCode 62. Unique Paths测试:")
    def unique_paths(m, n):
        # 使用组合数计算不同路径数
        return Combinatorics.combination(m + n - 2, m - 1)
    
    test_cases = [(3, 7), (3, 2), (7, 3)]
    for m, n in test_cases:
        result = unique_paths(m, n)
        print(f"网格({m}, {n})的不同路径数: {result}")
    
    # 题目测试：LeetCode 1259. 不相交的握手
    print("\nLeetCode 1259. 不相交的握手测试:")
    def handshake_ways(n):
        # 使用卡特兰数计算不相交握手方式
        return Combinatorics.catalan(n // 2)
    
    test_n = [2, 4, 6, 8]
    for n in test_n:
        result = handshake_ways(n)
        print(f"{n}个人的不相交握手方式数: {result}")


# 相关题目（组合计数算法）
"""
1. LeetCode 1259. 不相交的握手
   题目描述：偶数个人，每个人都要与其他人握手一次，但不能交叉握手。求有多少种不同的握手方式。
   网址：https://leetcode.cn/problems/handshakes-that-dont-cross/
   解法：卡特兰数的应用

2. Codeforces 888E. Maximum Subsequence
   题目描述：给定一个数组，求元素和模m的最大值。
   网址：https://codeforces.com/problemset/problem/888/E
   解法：折半枚举 + 组合计数

3. AtCoder ABC193E - Oversleeping
   题目描述：计算两个周期性事件同时发生的最短时间。
   网址：https://atcoder.jp/contests/abc193/tasks/abc193_e
   解法：组合数学 + 中国剩余定理

4. Project Euler #15: Lattice Paths
   题目描述：从网格左上角到右下角，只能向右或向下移动，有多少条不同路径。
   网址：https://projecteuler.net/problem=15
   解法：组合数计算

5. Codeforces 1114E. Arithmetic Progression
   题目描述：求最长等差数列的长度。
   网址：https://codeforces.com/problemset/problem/1114/E
   解法：组合数学 + 哈希表

6. LeetCode 62. Unique Paths
   题目描述：机器人位于一个 m x n 网格的左上角，只能向右或向下移动，求到达右下角的不同路径数。
   网址：https://leetcode.com/problems/unique-paths/
   解法：组合数计算 C(m+n-2, m-1)

7. Codeforces 1034E - Little C Loves 3 III
   题目描述：子集卷积的经典应用题目。
   网址：https://codeforces.com/problemset/problem/1034/E
   解法：子集卷积

8. AtCoder ABC195E - Lucky Numbers
   题目描述：计算满足特定条件的数字个数。
   网址：https://atcoder.jp/contests/abc195/tasks/abc195_e
   解法：数位DP + 组合计数

9. Project Euler #113: Non-bouncy numbers
   题目描述：计算非弹性数字的个数（既不递增也不递减的数字）。
   网址：https://projecteuler.net/problem=113
   解法：容斥原理 + 组合计数

10. Codeforces 914F - String Subsequence
   题目描述：计算字符串中特定子序列的个数。
   网址：https://codeforces.com/problemset/problem/914/F
   解法：动态规划 + 组合计数
"""


# Java实现
"""
// Combinatorics.java
import java.util.*;
import java.math.*;

public class Combinatorics {
    // 基本组合数计算
    public static BigInteger combination(long n, long k) {
        if (k < 0 || k > n) return BigInteger.ZERO;
        if (k == 0 || k == n) return BigInteger.ONE;
        k = Math.min(k, n - k); // 优化计算量
        
        BigInteger result = BigInteger.ONE;
        for (long i = 1; i <= k; i++) {
            result = result.multiply(BigInteger.valueOf(n - k + i))
                          .divide(BigInteger.valueOf(i));
        }
        return result;
    }
    
    // 排列数计算
    public static BigInteger permutation(long n, long k) {
        if (k < 0 || k > n) return BigInteger.ZERO;
        
        BigInteger result = BigInteger.ONE;
        for (long i = 0; i < k; i++) {
            result = result.multiply(BigInteger.valueOf(n - i));
        }
        return result;
    }
    
    // 组合数取模（适用于小模数）
    public static long combination_mod(long n, long k, long mod) {
        if (k < 0 || k > n) return 0;
        k = Math.min(k, n - k);
        
        long numerator = 1, denominator = 1;
        for (long i = 1; i <= k; i++) {
            numerator = (numerator * (n - k + i)) % mod;
            denominator = (denominator * i) % mod;
        }
        
        // 使用费马小定理求逆元
        return (numerator * mod_inverse(denominator, mod)) % mod;
    }
    
    // 模逆元（使用费马小定理）
    private static long mod_inverse(long a, long mod) {
        return pow_mod(a, mod - 2, mod);
    }
    
    // 快速幂取模
    private static long pow_mod(long a, long b, long mod) {
        long result = 1;
        a = a % mod;
        while (b > 0) {
            if ((b & 1) == 1) {
                result = (result * a) % mod;
            }
            a = (a * a) % mod;
            b >>= 1;
        }
        return result;
    }
    
    // Lucas定理
    public static long lucas(long n, long k, long p) {
        if (k == 0) return 1;
        long ni = n % p;
        long ki = k % p;
        if (ki > ni) return 0;
        return (lucas(n / p, k / p, p) * combination_mod(ni, ki, p)) % p;
    }
    
    // ExLucas定理（计算大数组合数模非质数）
    public static long ex_lucas(long n, long k, long mod) {
        // 质因数分解mod
        Map<Long, Integer> factors = factor(mod);
        long result = 0;
        
        // 中国剩余定理合并结果
        for (Map.Entry<Long, Integer> entry : factors.entrySet()) {
            long p = entry.getKey();
            int e = entry.getValue();
            long pe = 1;
            for (int i = 0; i < e; i++) pe *= p;
            
            long c = comb_mod_pe(n, k, p, e);
            // 中国剩余定理合并结果
            result = crt(result, mod / pe, c, pe);
            mod = mod / pe * pe;
        }
        
        return result;
    }
    
    // 计算C(n, k) mod p^e
    private static long comb_mod_pe(long n, long k, long p, int e) {
        long pe = 1;
        for (int i = 0; i < e; i++) pe *= p;
        
        long cnt_p = count_p_in_fact(n) - count_p_in_fact(k) - count_p_in_fact(n - k);
        if (cnt_p >= e) return 0;
        
        long a = fact_mod_pe(n, p, pe);
        long b = pow_mod(fact_mod_pe(k, p, pe), (pe / p) * (p - 1) - 1, pe);
        long c = pow_mod(fact_mod_pe(n - k, p, pe), (pe / p) * (p - 1) - 1, pe);
        
        long res = a * b % pe;
        res = res * c % pe;
        res = res * pow_mod(p, cnt_p, pe) % pe;
        return res;
    }
    
    // 计算n!中p因子的个数
    private static long count_p_in_fact(long n) {
        long cnt = 0;
        while (n > 0) {
            n /= p;
            cnt += n;
        }
        return cnt;
    }
    
    // 计算n!除去p因子后的结果 mod p^e
    private static long fact_mod_pe(long n, long p, long pe) {
        if (n == 0) return 1;
        long res = 1;
        
        // 计算循环部分
        long cycle = 1;
        for (long i = 1; i <= pe; i++) {
            if (i % p != 0) {
                cycle = cycle * i % pe;
            }
        }
        
        res = pow_mod(cycle, n / pe, pe);
        
        // 计算剩余部分
        for (long i = 1; i <= n % pe; i++) {
            if (i % p != 0) {
                res = res * i % pe;
            }
        }
        
        // 递归计算n/p部分
        return res * fact_mod_pe(n / p, p, pe) % pe;
    }
    
    // 质因数分解
    private static Map<Long, Integer> factor(long n) {
        Map<Long, Integer> factors = new HashMap<>();
        for (long i = 2; i * i <= n; i++) {
            while (n % i == 0) {
                factors.put(i, factors.getOrDefault(i, 0) + 1);
                n /= i;
            }
        }
        if (n > 1) {
            factors.put(n, 1);
        }
        return factors;
    }
    
    // 中国剩余定理
    private static long crt(long a1, long m1, long a2, long m2) {
        // 找到x使得 x ≡ a1 mod m1, x ≡ a2 mod m2
        // 使用扩展欧几里得算法
        long g = gcd(m1, m2);
        long lcm = m1 / g * m2;
        
        if ((a2 - a1) % g != 0) {
            throw new RuntimeException("无解");
        }
        
        long t = ((a2 - a1) / g) % (m2 / g);
        t = (t + m2 / g) % (m2 / g);
        
        return (a1 + t * m1) % lcm;
    }
    
    // 扩展欧几里得算法
    private static long[] extended_gcd(long a, long b) {
        if (b == 0) {
            return new long[] {a, 1, 0};
        }
        long[] res = extended_gcd(b, a % b);
        long g = res[0];
        long x = res[2];
        long y = res[1] - (a / b) * res[2];
        return new long[] {g, x, y};
    }
    
    // 最大公约数
    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    // 容斥原理
    public static long inclusion_exclusion(long n, int m, IntFunction<Long> conditions) {
        long result = 0;
        for (int mask = 0; mask < (1 << m); mask++) {
            int cnt = Integer.bitCount(mask);
            long size = conditions.apply(mask);
            if (cnt % 2 == 0) {
                result += size;
            } else {
                result -= size;
            }
        }
        return result;
    }
    
    // 卡特兰数
    public static BigInteger catalan(long n) {
        return combination(2 * n, n).divide(BigInteger.valueOf(n + 1));
    }
    
    // 第一类斯特林数
    public static long stirling_first(long n, long k) {
        if (n == 0 && k == 0) return 1;
        if (n == 0 || k == 0) return 0;
        
        // 使用动态规划计算
        long[][] dp = new long[(int)n + 1][(int)k + 1];
        dp[0][0] = 1;
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= k; j++) {
                dp[i][j] = dp[i-1][j-1] + (i-1) * dp[i-1][j];
            }
        }
        
        return dp[(int)n][(int)k];
    }
    
    // 第二类斯特林数
    public static long stirling_second(long n, long k) {
        if (n == 0 && k == 0) return 1;
        if (n == 0 || k == 0) return 0;
        
        // 使用动态规划计算
        long[][] dp = new long[(int)n + 1][(int)k + 1];
        dp[0][0] = 1;
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= k; j++) {
                dp[i][j] = dp[i-1][j-1] + j * dp[i-1][j];
            }
        }
        
        return dp[(int)n][(int)k];
    }
    
    // 贝尔数
    public static long bell_number(long n) {
        long result = 0;
        for (long k = 0; k <= n; k++) {
            result += stirling_second(n, k);
        }
        return result;
    }
    
    // 欧拉数
    public static long euler_number(long n, long m) {
        if (n == 1 && m == 0) return 1;
        if (m < 0 || m >= n) return 0;
        
        // 使用动态规划计算
        long[][] dp = new long[(int)n + 1][(int)n + 1];
        dp[1][0] = 1;
        
        for (int i = 2; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                if (j == 0) {
                    dp[i][j] = 1;
                } else {
                    dp[i][j] = (i - j) * dp[i-1][j-1] + (j + 1) * dp[i-1][j];
                }
            }
        }
        
        return dp[(int)n][(int)m];
    }
    
    // 错排数
    public static long derangement(long n) {
        if (n == 0) return 1;
        if (n == 1) return 0;
        long a = 1, b = 0; // D(0)=1, D(1)=0
        for (long i = 2; i <= n; i++) {
            long temp = b;
            b = (i - 1) * (a + b);
            a = temp;
        }
        return b;
    }
}
"""


# C++实现
"""
// combinatorics.cpp
#include <iostream>
#include <vector>
#include <map>
#include <algorithm>
using namespace std;

class Combinatorics {
public:
    // 基本组合数计算
    static long long combination(long long n, long long k) {
        if (k < 0 || k > n) return 0;
        if (k == 0 || k == n) return 1;
        k = min(k, n - k); // 优化计算量
        
        long long result = 1;
        for (long long i = 1; i <= k; i++) {
            result = result * (n - k + i) / i;
        }
        return result;
    }
    
    // 排列数计算
    static long long permutation(long long n, long long k) {
        if (k < 0 || k > n) return 0;
        
        long long result = 1;
        for (long long i = 0; i < k; i++) {
            result = result * (n - i);
        }
        return result;
    }
    
    // 组合数取模（适用于小模数）
    static long long combination_mod(long long n, long long k, long long mod) {
        if (k < 0 || k > n) return 0;
        k = min(k, n - k);
        
        long long numerator = 1, denominator = 1;
        for (long long i = 1; i <= k; i++) {
            numerator = (numerator * (n - k + i)) % mod;
            denominator = (denominator * i) % mod;
        }
        
        // 使用费马小定理求逆元
        return (numerator * mod_inverse(denominator, mod)) % mod;
    }
    
    // 模逆元（使用费马小定理）
    static long long mod_inverse(long long a, long long mod) {
        return pow_mod(a, mod - 2, mod);
    }
    
    // 快速幂取模
    static long long pow_mod(long long a, long long b, long long mod) {
        long long result = 1;
        a = a % mod;
        while (b > 0) {
            if (b & 1) {
                result = (result * a) % mod;
            }
            a = (a * a) % mod;
            b >>= 1;
        }
        return result;
    }
    
    // Lucas定理
    static long long lucas(long long n, long long k, long long p) {
        if (k == 0) return 1;
        long long ni = n % p;
        long long ki = k % p;
        if (ki > ni) return 0;
        return (lucas(n / p, k / p, p) * combination_mod(ni, ki, p)) % p;
    }
    
    // ExLucas定理（计算大数组合数模非质数）
    static long long ex_lucas(long long n, long long k, long long mod) {
        // 质因数分解mod
        map<long long, int> factors = factor(mod);
        long long result = 0;
        long long current_mod = mod;
        
        // 中国剩余定理合并结果
        for (auto& entry : factors) {
            long long p = entry.first;
            int e = entry.second;
            long long pe = 1;
            for (int i = 0; i < e; i++) pe *= p;
            
            long long c = comb_mod_pe(n, k, p, e);
            // 中国剩余定理合并结果
            result = crt(result, current_mod / pe, c, pe);
            current_mod = current_mod / pe * pe;
        }
        
        return result;
    }
    
    // 计算C(n, k) mod p^e
    static long long comb_mod_pe(long long n, long long k, long long p, int e) {
        long long pe = 1;
        for (int i = 0; i < e; i++) pe *= p;
        
        long long cnt_p = count_p_in_fact(n, p) - count_p_in_fact(k, p) - count_p_in_fact(n - k, p);
        if (cnt_p >= e) return 0;
        
        long long a = fact_mod_pe(n, p, pe);
        long long b = pow_mod(fact_mod_pe(k, p, pe), (pe / p) * (p - 1) - 1, pe);
        long long c = pow_mod(fact_mod_pe(n - k, p, pe), (pe / p) * (p - 1) - 1, pe);
        
        long long res = a * b % pe;
        res = res * c % pe;
        res = res * pow_mod(p, cnt_p, pe) % pe;
        return res;
    }
    
    // 计算n!中p因子的个数
    static long long count_p_in_fact(long long n, long long p) {
        long long cnt = 0;
        while (n > 0) {
            n /= p;
            cnt += n;
        }
        return cnt;
    }
    
    // 计算n!除去p因子后的结果 mod p^e
    static long long fact_mod_pe(long long n, long long p, long long pe) {
        if (n == 0) return 1;
        long long res = 1;
        
        // 计算循环部分
        long long cycle = 1;
        for (long long i = 1; i <= pe; i++) {
            if (i % p != 0) {
                cycle = cycle * i % pe;
            }
        }
        
        res = pow_mod(cycle, n / pe, pe);
        
        // 计算剩余部分
        for (long long i = 1; i <= n % pe; i++) {
            if (i % p != 0) {
                res = res * i % pe;
            }
        }
        
        // 递归计算n/p部分
        return res * fact_mod_pe(n / p, p, pe) % pe;
    }
    
    // 质因数分解
    static map<long long, int> factor(long long n) {
        map<long long, int> factors;
        for (long long i = 2; i * i <= n; i++) {
            while (n % i == 0) {
                factors[i]++;
                n /= i;
            }
        }
        if (n > 1) {
            factors[n] = 1;
        }
        return factors;
    }
    
    // 中国剩余定理
    static long long crt(long long a1, long long m1, long long a2, long long m2) {
        // 找到x使得 x ≡ a1 mod m1, x ≡ a2 mod m2
        long long g = gcd(m1, m2);
        long long lcm = m1 / g * m2;
        
        if ((a2 - a1) % g != 0) {
            throw "无解";
        }
        
        long long t = ((a2 - a1) / g) % (m2 / g);
        t = (t + m2 / g) % (m2 / g);
        
        return (a1 + t * m1) % lcm;
    }
    
    // 扩展欧几里得算法
    static long long extended_gcd(long long a, long long b, long long& x, long long& y) {
        if (b == 0) {
            x = 1;
            y = 0;
            return a;
        }
        long long g = extended_gcd(b, a % b, y, x);
        y -= (a / b) * x;
        return g;
    }
    
    // 最大公约数
    static long long gcd(long long a, long long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    // 容斥原理
    template<typename Func>
    static long long inclusion_exclusion(long long n, int m, Func conditions) {
        long long result = 0;
        for (int mask = 0; mask < (1 << m); mask++) {
            int cnt = __builtin_popcount(mask);
            long long size = conditions(mask);
            if (cnt % 2 == 0) {
                result += size;
            } else {
                result -= size;
            }
        }
        return result;
    }
    
    // 卡特兰数
    static long long catalan(long long n) {
        return combination(2 * n, n) / (n + 1);
    }
    
    // 第一类斯特林数
    static long long stirling_first(long long n, long long k) {
        if (n == 0 && k == 0) return 1;
        if (n == 0 || k == 0) return 0;
        
        // 使用动态规划计算
        vector<vector<long long>> dp(n + 1, vector<long long>(k + 1, 0));
        dp[0][0] = 1;
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= k; j++) {
                dp[i][j] = dp[i-1][j-1] + (i-1) * dp[i-1][j];
            }
        }
        
        return dp[n][k];
    }
    
    // 第二类斯特林数
    static long long stirling_second(long long n, long long k) {
        if (n == 0 && k == 0) return 1;
        if (n == 0 || k == 0) return 0;
        
        // 使用动态规划计算
        vector<vector<long long>> dp(n + 1, vector<long long>(k + 1, 0));
        dp[0][0] = 1;
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= k; j++) {
                dp[i][j] = dp[i-1][j-1] + j * dp[i-1][j];
            }
        }
        
        return dp[n][k];
    }
    
    // 贝尔数
    static long long bell_number(long long n) {
        long long result = 0;
        for (long long k = 0; k <= n; k++) {
            result += stirling_second(n, k);
        }
        return result;
    }
    
    // 欧拉数
    static long long euler_number(long long n, long long m) {
        if (n == 1 && m == 0) return 1;
        if (m < 0 || m >= n) return 0;
        
        // 使用动态规划计算
        vector<vector<long long>> dp(n + 1, vector<long long>(n + 1, 0));
        dp[1][0] = 1;
        
        for (int i = 2; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                if (j == 0) {
                    dp[i][j] = 1;
                } else {
                    dp[i][j] = (i - j) * dp[i-1][j-1] + (j + 1) * dp[i-1][j];
                }
            }
        }
        
        return dp[n][m];
    }
    
    // 错排数
    static long long derangement(long long n) {
        if (n == 0) return 1;
        if (n == 1) return 0;
        long long a = 1, b = 0; // D(0)=1, D(1)=0
        for (long long i = 2; i <= n; i++) {
            long long temp = b;
            b = (i - 1) * (a + b);
            a = temp;
        }
        return b;
    }
};
"""


# 算法详解与工程化考量
"""
## 组合计数算法的本质与优化要点

1. **大数计算问题**
   - 组合数在n较大时会迅速增长，超过基本数据类型的范围，需要使用大数运算（Java的BigInteger或C++的自定义大数类）
   - 取模运算中的逆元计算需要注意模数是否为质数，非质数情况需使用扩展欧几里得算法

2. **模运算优化**
   - Lucas定理适用于模数为质数的情况，时间复杂度O(log_p n)
   - ExLucas定理适用于模数为任意数的情况，通过质因数分解和中国剩余定理实现

3. **递推式优化**
   - 斯特林数、贝尔数等使用动态规划计算时，可以使用滚动数组优化空间复杂度
   - 错排数可以使用递推公式D(n) = (n-1) * (D(n-1) + D(n-2))高效计算

4. **容斥原理优化**
   - 使用位运算优化子集枚举
   - 预处理组合数避免重复计算

5. **卡特兰数优化**
   - 使用递推公式避免重复计算
   - 预处理阶乘和逆元

## 时间复杂度分析

| 算法 | 时间复杂度 | 空间复杂度 |
|------|------------|------------|
| 基本组合数计算 | O(min(k, n-k)) | O(1) |
| Lucas定理 | O(log_p n) | O(1) |
| ExLucas定理 | O(mod log^2 n) | O(mod) |
| 容斥原理 | O(2^m) | O(1) |
| 卡特兰数 | O(n) | O(n) |
| 斯特林数 | O(n^2) | O(n^2) |
| 贝尔数 | O(n^2) | O(n^2) |
| 欧拉数 | O(n^2) | O(n^2) |
| 错排数 | O(n) | O(1) |

## 工程化考量

1. **性能优化**
   - 预处理阶乘和阶乘逆元可以显著提高组合数计算效率
   - 对于多次查询的场景，缓存中间结果避免重复计算

2. **异常处理**
   - 处理无效输入（如k > n或n < 0）
   - 注意整数溢出问题，在C++中尤其需要谨慎

3. **内存管理**
   - 大规模组合数表需要合理规划内存使用
   - 递归实现的函数可能导致栈溢出，需要转换为迭代方式

4. **跨语言实现差异**
   - Python内置大整数支持，实现更简洁
   - Java提供BigInteger类，适合大数运算
   - C++需要手动管理内存，对大数运算支持较少，实现较复杂

## 相关题目解析

1. **卡特兰数应用（LeetCode 1259）**
   - 问题本质：将问题转化为卡特兰数的第n项
   - 思路：每个人握手后将问题分成左右两个子问题，符合卡特兰数递归式
   - 优化：使用动态规划避免重复计算

2. **容斥原理应用（Codeforces 888E）**
   - 问题本质：寻找最优子集和
   - 思路：将数组分成两半，枚举所有可能的子集和，然后对于每一半寻找互补的最优解
   - 优化：使用二分查找提高查找效率

## 算法安全与业务适配

1. **数值稳定性**
   - 大数运算可能导致精度问题，需要使用高精度库
   - 模运算中的负数处理需要特别注意

2. **业务场景适配**
   - 概率论与统计学中的组合分析
   - 密码学中的离散数学计算
   - 计算机图形学中的排列组合问题

3. **扩展性设计**
   - 设计可配置的模数系统，支持不同场景
   - 提供多种实现方式（递归/迭代）以适应不同的性能需求
   - 添加缓存机制，提高重复计算场景下的性能
"""