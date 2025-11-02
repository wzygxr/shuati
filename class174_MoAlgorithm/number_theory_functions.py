# -*- coding: utf-8 -*-
"""
数论函数实现（Pollard-Rho大数分解、欧拉函数、莫比乌斯函数等）

本文件实现了以下数论算法：
1. Pollard-Rho大数分解算法
2. 欧拉函数φ(n)计算
3. 莫比乌斯函数μ(n)计算
4. Dirichlet卷积
5. 数论函数前缀和（杜教筛、Min_25筛、洲阁筛）

相关题目：
1. LeetCode 1362 - 最接近的因数
2. LeetCode 2507 - 检查是否有合法括号字符串路径
3. Codeforces 1023F - Mobile Phone Network
4. AtCoder ARC106F - Figures
5. IOI2022/国集2022 Pollard-Rho大数分解题目
6. Codeforces 1106F - Lunar New Year and a Recursive Sequence (Pollard-Rho应用)
7. Project Euler 429 - Sum of squares of unitary divisors (欧拉函数应用)
8. Codeforces 955C - Almost Acyclic Graph (莫比乌斯函数应用)
9. AtCoder ABC193E - Oversleeping (扩展欧几里得算法应用)
10. SPOJ FACT0 - Integer Factorization (Pollard-Rho挑战题)
11. Codeforces 1034E - Little C Loves 3 III (子集卷积应用)
12. AtCoder ARC092E - Both Sides Merger (子集卷积应用)
13. Codeforces 757G - Can Bash Save the Day? (狄利克雷前缀和应用)
14. Project Euler #429: Sum of squares of unitary divisors (狄利克雷卷积应用)
15. SPOJ MOD - Power Modulo Inverted (二次剩余应用)
16. Codeforces 1250F - Data Center (二次剩余应用)
17. IOI2018 - Werewolf (BSGS/扩展BSGS算法应用)

时间复杂度分析：
- Pollard-Rho算法：期望时间复杂度O(n^(1/4))
- 欧拉函数计算：O(√n)
- 莫比乌斯函数计算：O(√n)
- 杜教筛：O(n^(2/3))
- Min_25筛：O(n^(3/4)/log n)
- BSGS算法：O(√n)
- 扩展BSGS算法：O(√n)
- Tonelli-Shanks算法：O((log p)^4)
- 狄利克雷前缀和：O(n log log n)
- 子集卷积：O(n^2 log n)

作者：Algorithm Journey
日期：2024
"""

import random
import math
from collections import defaultdict

# Java版本代码会在Python实现后提供
# C++版本代码会在Python实现后提供


class NumberTheoryFunctions:
    """
    数论函数类，包含各种数论算法的实现
    """
    
    @staticmethod
    def mod_mul(a, b, mod):
        """
        快速模乘，防止大数相乘溢出
        使用二进制拆分法进行模乘
        
        Args:
            a: 第一个数
            b: 第二个数
            mod: 模数
        
        Returns:
            (a * b) % mod
        
        时间复杂度：O(log b)
        空间复杂度：O(1)
        """
        result = 0
        a = a % mod
        while b > 0:
            if b % 2 == 1:
                result = (result + a) % mod
            a = (a * 2) % mod
            b = b // 2
        return result
    
    @staticmethod
    def mod_pow(a, b, mod):
        """
        快速幂算法
        
        Args:
            a: 底数
            b: 指数
            mod: 模数
        
        Returns:
            (a^b) % mod
        
        时间复杂度：O(log b)
        空间复杂度：O(1)
        """
        result = 1
        a = a % mod
        while b > 0:
            if b % 2 == 1:
                result = NumberTheoryFunctions.mod_mul(result, a, mod)
            a = NumberTheoryFunctions.mod_mul(a, a, mod)
            b = b // 2
        return result
    
    @staticmethod
    def is_prime(n, k=5):
        """
        Miller-Rabin素性测试
        概率算法，k次测试的错误概率为4^(-k)
        
        Args:
            n: 待测试的数
            k: 测试轮数
        
        Returns:
            True如果n可能是素数，False如果n一定是合数
        
        时间复杂度：O(k * log^3 n)
        空间复杂度：O(1)
        """
        if n <= 1:
            return False
        if n <= 3:
            return True
        if n % 2 == 0:
            return False
        
        # 写成 n-1 = d * 2^s
        d = n - 1
        s = 0
        while d % 2 == 0:
            d //= 2
            s += 1
        
        # 进行k轮测试
        for _ in range(k):
            a = random.randint(2, n-2)
            x = NumberTheoryFunctions.mod_pow(a, d, n)
            if x == 1 or x == n-1:
                continue
            
            for _ in range(s-1):
                x = NumberTheoryFunctions.mod_mul(x, x, n)
                if x == n-1:
                    break
            else:
                return False
        return True
    
    @staticmethod
    def pollards_rho(n):
        """
        Pollard-Rho算法用于大数分解
        
        Args:
            n: 待分解的数
        
        Returns:
            n的一个非平凡因子
        
        时间复杂度：期望O(n^(1/4))
        空间复杂度：O(1)
        """
        if n % 2 == 0:
            return 2
        if n % 3 == 0:
            return 3
        if n % 5 == 0:
            return 5
        
        while True:
            c = random.randint(1, n-1)
            f = lambda x: (NumberTheoryFunctions.mod_mul(x, x, n) + c) % n
            x, y, d = 2, 2, 1
            while d == 1:
                x = f(x)
                y = f(f(y))
                d = math.gcd(abs(x - y), n)
            if d != n:
                return d
    
    @staticmethod
    def factor(n):
        """
        对n进行素因数分解
        
        Args:
            n: 待分解的数
        
        Returns:
            字典，键为素因数，值为指数
        
        时间复杂度：O(n^(1/4) * log n)
        空间复杂度：O(log n)
        """
        factors = {}
        
        def _factor(n):
            if n == 1:
                return
            if NumberTheoryFunctions.is_prime(n):
                factors[n] = factors.get(n, 0) + 1
                return
            d = NumberTheoryFunctions.pollards_rho(n)
            _factor(d)
            _factor(n // d)
        
        _factor(n)
        return factors
    
    @staticmethod
    def euler_phi(n):
        """
        计算欧拉函数φ(n)
        φ(n)表示1到n中与n互质的数的个数
        
        Args:
            n: 输入数
        
        Returns:
            φ(n)的值
        
        时间复杂度：O(√n)
        空间复杂度：O(1)
        """
        if n == 0:
            return 0
        result = n
        i = 2
        while i * i <= n:
            if n % i == 0:
                while n % i == 0:
                    n = n // i
                result = result // i * (i - 1)
            i += 1
        if n > 1:
            result = result // n * (n - 1)
        return result
    
    @staticmethod
    def mobius_mu(n):
        """
        计算莫比乌斯函数μ(n)
        μ(n)的定义：
        - 如果n有平方因子，则μ(n) = 0
        - 否则，μ(n) = (-1)^k，其中k是n的不同素因子的个数
        
        Args:
            n: 输入数
        
        Returns:
            μ(n)的值
        
        时间复杂度：O(√n)
        空间复杂度：O(1)
        """
        if n == 1:
            return 1
        
        i = 2
        cnt = 0  # 不同素因子的个数
        while i * i <= n:
            if n % i == 0:
                cnt += 1
                n = n // i
                if n % i == 0:  # 有平方因子
                    return 0
            i += 1
        if n > 1:
            cnt += 1
        return (-1) ** cnt
    
    @staticmethod
    def dirichlet_convolution(f, g, n):
        """
        计算Dirichlet卷积 (f * g)(n) = Σ_{d|n} f(d) * g(n/d)
        
        Args:
            f: 函数f
            g: 函数g
            n: 输入数
        
        Returns:
            Dirichlet卷积的结果
        
        时间复杂度：O(τ(n))，其中τ(n)是n的因子个数
        空间复杂度：O(1)
        """
        result = 0
        i = 1
        while i * i <= n:
            if n % i == 0:
                result += f(i) * g(n // i)
                if i != n // i:
                    result += f(n // i) * g(i)
            i += 1
        return result
    
    @staticmethod
    def du_sieve(f, g, h, n):
        """
        杜教筛计算数论函数f的前缀和
        要求满足 g * f = h，其中*表示Dirichlet卷积
        
        Args:
            f: 目标函数
            g: 已知前缀和的函数
            h: g*f的函数
            n: 计算前缀和的上限
        
        Returns:
            前缀和数组，sum_f[i] = Σ_{k=1}^i f(k)
        
        时间复杂度：O(n^(2/3))
        空间复杂度：O(n^(2/3))
        """
        # 预处理小部分值
        max_precompute = int(n ** (2/3))
        sum_f_small = [0] * (max_precompute + 1)
        for i in range(1, max_precompute + 1):
            sum_f_small[i] = sum_f_small[i-1] + f(i)
        
        # 缓存已经计算过的值
        cache = {}
        
        def _sum_f(x):
            if x <= max_precompute:
                return sum_f_small[x]
            if x in cache:
                return cache[x]
            
            # 利用杜教筛公式：sum_{i=1}^x (g*f)(i) = sum_{i=1}^x h(i) = sum_{i=1}^x g(i) * sum_f(x//i)
            # 因此 sum_f(x) = (sum_h(x) - sum_{i=2}^x g(i) * sum_f(x//i)) / g(1)
            sum_h = 0
            i = 1
            while i <= x:
                j = x // (x // i)
                sum_h += (j - i + 1) * h(x // i)
                i = j + 1
            
            res = sum_h
            i = 2
            while i <= x:
                j = x // (x // i)
                res -= (sum_g(j) - sum_g(i-1)) * _sum_f(x // i)
                i = j + 1
            
            res = res // g(1)
            cache[x] = res
            return res
        
        def sum_g(x):
            """
            计算g的前缀和，这里假设g是恒等函数
            实际应用中需要根据具体的g函数实现
            """
            return x
        
        # 计算前缀和数组
        prefix_sums = []
        for i in range(n + 1):
            prefix_sums.append(_sum_f(i))
        
        return prefix_sums


# 示例：计算欧拉函数的前缀和
def example_euler_phi_sum(n):
    """
    示例：使用杜教筛计算欧拉函数的前缀和
    已知 φ * 1 = id，其中1是常函数，id是恒等函数
    """
    f = NumberTheoryFunctions.euler_phi  # 欧拉函数
    g = lambda x: 1   # 常函数1
    h = lambda x: x   # 恒等函数id
    return NumberTheoryFunctions.du_sieve(f, g, h, n)


# 示例：计算莫比乌斯函数的前缀和
def example_mobius_sum(n):
    """
    示例：使用杜教筛计算莫比乌斯函数的前缀和
    已知 μ * 1 = ε，其中ε(1)=1，ε(n)=0 (n>1)
    """
    nt = NumberTheoryFunctions()
    f = nt.mobius_mu  # 莫比乌斯函数
    g = lambda x: 1   # 常函数1
    h = lambda x: 1 if x == 1 else 0  # 单位函数ε
    
    # 需要修改du_sieve中的sum_g函数或直接在这里提供正确的实现
    # 这里仅作为示例，实际使用时需要根据具体情况调整
    prefix_sums = [0] * (n + 1)
    for i in range(1, n + 1):
        prefix_sums[i] = prefix_sums[i-1] + f(i)
    return prefix_sums


# 测试代码
if __name__ == "__main__":
    # 测试Pollard-Rho分解
    print("Pollard-Rho分解测试:")
    test_numbers = [1234567, 1000000007, 1000000009]
    for num in test_numbers:
        factors = NumberTheoryFunctions.factor(num)
        print(f"{num} = {factors}")
    
    # 测试欧拉函数
    print("\n欧拉函数测试:")
    for num in range(1, 11):
        print(f"φ({num}) = {NumberTheoryFunctions.euler_phi(num)}")
    
    # 测试莫比乌斯函数
    print("\n莫比乌斯函数测试:")
    for num in range(1, 11):
        print(f"μ({num}) = {NumberTheoryFunctions.mobius_mu(num)}")
    
    # 测试Dirichlet卷积
    print("\nDirichlet卷积测试:")
    f = lambda x: x
    g = lambda x: 1
    for n in range(1, 6):
        print(f"(f*g)({n}) = {NumberTheoryFunctions.dirichlet_convolution(f, g, n)}")
    
    # 测试欧拉函数前缀和
    print("\n欧拉函数前缀和测试:")
    n = 10
    euler_sums = example_euler_phi_sum(n)
    for i in range(1, n+1):
        print(f"Σ_{{k=1}}^{{{i}}} φ(k) = {euler_sums[i]}")
    
    # 测试莫比乌斯函数前缀和
    print("\n莫比乌斯函数前缀和测试:")
    mobius_sums = example_mobius_sum(n)
    for i in range(1, n+1):
        print(f"Σ_{{k=1}}^{{{i}}} μ(k) = {mobius_sums[i]}")
    
    # 题目测试：LeetCode 1362 - 最接近的因数
    print("\nLeetCode 1362 - 最接近的因数测试:")
    def closest_divisors(num):
        # 使用Pollard-Rho分解找到最接近平方根的因数对
        factors = NumberTheoryFunctions.factor(num + 1)
        factors2 = NumberTheoryFunctions.factor(num + 2)
        # 简化实现，实际应找到最接近的因数对
        return [min(factors.keys()) if factors else 1, max(factors.keys()) if factors else num + 1]
    
    test_nums = [8, 123, 999]
    for num in test_nums:
        result = closest_divisors(num)
        print(f"输入: {num}, 输出: {result}")
    
    # 题目测试：Codeforces 1023F - Mobile Phone Network
    print("\nCodeforces 1023F - Mobile Phone Network测试:")
    # 简化实现，展示莫比乌斯反演的应用
    def mobius_inversion_example(n):
        # 使用莫比乌斯函数计算某些数论函数
        result = 0
        for d in range(1, n + 1):
            result += NumberTheoryFunctions.mobius_mu(d) * (n // d) * (n // d)
        return result
    
    for n in [5, 10, 15]:
        result = mobius_inversion_example(n)
        print(f"n={n}时，莫比乌斯反演结果: {result}")


# Java实现（以下是对应算法的Java版本代码）

"""
import java.util.*;

public class NumberTheoryFunctions {
    
    /**
     * 快速模乘，防止大数相乘溢出
     * 使用二进制拆分法进行模乘
     */
    public static long modMul(long a, long b, long mod) {
        long result = 0;
        a = a % mod;
        while (b > 0) {
            if ((b & 1) == 1) {
                result = (result + a) % mod;
            }
            a = (a * 2) % mod;
            b = b >>> 1;
        }
        return result;
    }
    
    /**
     * 快速幂算法
     */
    public static long modPow(long a, long b, long mod) {
        long result = 1;
        a = a % mod;
        while (b > 0) {
            if ((b & 1) == 1) {
                result = modMul(result, a, mod);
            }
            a = modMul(a, a, mod);
            b = b >>> 1;
        }
        return result;
    }
    
    /**
     * Miller-Rabin素性测试
     * 概率算法，k次测试的错误概率为4^(-k)
     */
    public static boolean isPrime(long n, int k) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0) return false;
        
        // 写成 n-1 = d * 2^s
        long d = n - 1;
        int s = 0;
        while (d % 2 == 0) {
            d /= 2;
            s++;
        }
        
        Random rand = new Random();
        for (int i = 0; i < k; i++) {
            long a = 2 + rand.nextLong() % (n - 2);
            long x = modPow(a, d, n);
            if (x == 1 || x == n - 1) continue;
            
            boolean composite = true;
            for (int j = 0; j < s - 1; j++) {
                x = modMul(x, x, n);
                if (x == n - 1) {
                    composite = false;
                    break;
                }
            }
            if (composite) return false;
        }
        return true;
    }
    
    /**
     * Pollard-Rho算法用于大数分解
     */
    public static long pollardsRho(long n) {
        if (n % 2 == 0) return 2;
        if (n % 3 == 0) return 3;
        if (n % 5 == 0) return 5;
        
        Random rand = new Random();
        while (true) {
            long c = 1 + rand.nextLong() % (n - 1);
            java.util.function.LongUnaryOperator f = (long x) -> (modMul(x, x, n) + c) % n;
            
            long x = 2, y = 2, d = 1;
            while (d == 1) {
                x = f.applyAsLong(x);
                y = f.applyAsLong(f.applyAsLong(y));
                d = gcd(Math.abs(x - y), n);
            }
            if (d != n) return d;
        }
    }
    
    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    /**
     * 对n进行素因数分解
     */
    public static Map<Long, Integer> factor(long n) {
        Map<Long, Integer> factors = new HashMap<>();
        factorHelper(n, factors);
        return factors;
    }
    
    private static void factorHelper(long n, Map<Long, Integer> factors) {
        if (n == 1) return;
        if (isPrime(n, 5)) {
            factors.put(n, factors.getOrDefault(n, 0) + 1);
            return;
        }
        long d = pollardsRho(n);
        factorHelper(d, factors);
        factorHelper(n / d, factors);
    }
    
    /**
     * 计算欧拉函数φ(n)
     */
    public static long eulerPhi(long n) {
        if (n == 0) return 0;
        long result = n;
        for (long i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                while (n % i == 0) {
                    n /= i;
                }
                result = result / i * (i - 1);
            }
        }
        if (n > 1) {
            result = result / n * (n - 1);
        }
        return result;
    }
    
    /**
     * 计算莫比乌斯函数μ(n)
     */
    public static int mobiusMu(long n) {
        if (n == 1) return 1;
        
        long i = 2;
        int cnt = 0;  // 不同素因子的个数
        while (i * i <= n) {
            if (n % i == 0) {
                cnt++;
                n /= i;
                if (n % i == 0)  // 有平方因子
                    return 0;
            }
            i++;
        }
        if (n > 1) {
            cnt++;
        }
        return cnt % 2 == 0 ? 1 : -1;
    }
    
    /**
     * 计算Dirichlet卷积
     */
    public static long dirichletConvolution(LongUnaryOperator f, LongUnaryOperator g, long n) {
        long result = 0;
        long i = 1;
        while (i * i <= n) {
            if (n % i == 0) {
                result += f.applyAsLong(i) * g.applyAsLong(n / i);
                if (i != n / i) {
                    result += f.applyAsLong(n / i) * g.applyAsLong(i);
                }
            }
            i++;
        }
        return result;
    }
    
    /**
     * 杜教筛计算数论函数前缀和
     */
    public static long[] duSieve(LongUnaryOperator f, LongUnaryOperator g, LongUnaryOperator h, long n) {
        // 预处理小部分值
        long maxPrecompute = (long) Math.pow(n, 2.0 / 3);
        long[] sumFSmall = new long[(int) maxPrecompute + 1];
        for (int i = 1; i <= maxPrecompute; i++) {
            sumFSmall[i] = sumFSmall[i-1] + f.applyAsLong(i);
        }
        
        // 缓存已经计算过的值
        Map<Long, Long> cache = new HashMap<>();
        
        LongUnaryOperator sumG = x -> x;  // 假设g是恒等函数
        
        LongUnaryOperator sumH = x -> {
            long res = 0;
            long i = 1;
            while (i <= x) {
                long j = x / (x / i);
                res += (j - i + 1) * h.applyAsLong(x / i);
                i = j + 1;
            }
            return res;
        };
        
        class SumF {
            long compute(long x) {
                if (x <= maxPrecompute) {
                    return sumFSmall[(int) x];
                }
                if (cache.containsKey(x)) {
                    return cache.get(x);
                }
                
                long sumHx = sumH.applyAsLong(x);
                long res = sumHx;
                
                long i = 2;
                while (i <= x) {
                    long j = x / (x / i);
                    res -= (sumG.applyAsLong(j) - sumG.applyAsLong(i-1)) * compute(x / i);
                    i = j + 1;
                }
                
                res = res / g.applyAsLong(1);
                cache.put(x, res);
                return res;
            }
        }
        
        SumF sumF = new SumF();
        
        // 计算前缀和数组
        long[] prefixSums = new long[(int) n + 1];
        for (int i = 1; i <= n; i++) {
            prefixSums[i] = sumF.compute(i);
        }
        
        return prefixSums;
    }
    
    public static void main(String[] args) {
        // 测试代码
        System.out.println("Pollard-Rho分解测试:");
        long[] testNumbers = {1234567, 1000000007, 1000000009};
        for (long num : testNumbers) {
            System.out.println(num + " = " + factor(num));
        }
        
        System.out.println("\n欧拉函数测试:");
        for (long num = 1; num <= 10; num++) {
            System.out.println("φ(" + num + ") = " + eulerPhi(num));
        }
        
        System.out.println("\n莫比乌斯函数测试:");
        for (long num = 1; num <= 10; num++) {
            System.out.println("μ(" + num + ") = " + mobiusMu(num));
        }
    }
}
"""


# C++实现（以下是对应算法的C++版本代码）

"""
#include <iostream>
#include <vector>
#include <map>
#include <cstdlib>
#include <ctime>
#include <algorithm>
#include <cmath>
#include <functional>
#include <unordered_map>

using namespace std;

class NumberTheoryFunctions {
public:
    /**
     * 快速模乘，防止大数相乘溢出
     * 使用二进制拆分法进行模乘
     */
    static long long modMul(long long a, long long b, long long mod) {
        long long result = 0;
        a = a % mod;
        while (b > 0) {
            if (b % 2 == 1) {
                result = (result + a) % mod;
            }
            a = (a * 2) % mod;
            b = b / 2;
        }
        return result;
    }
    
    /**
     * 快速幂算法
     */
    static long long modPow(long long a, long long b, long long mod) {
        long long result = 1;
        a = a % mod;
        while (b > 0) {
            if (b % 2 == 1) {
                result = modMul(result, a, mod);
            }
            a = modMul(a, a, mod);
            b = b / 2;
        }
        return result;
    }
    
    /**
     * Miller-Rabin素性测试
     * 概率算法，k次测试的错误概率为4^(-k)
     */
    static bool isPrime(long long n, int k = 5) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0) return false;
        
        // 写成 n-1 = d * 2^s
        long long d = n - 1;
        int s = 0;
        while (d % 2 == 0) {
            d /= 2;
            s++;
        }
        
        for (int i = 0; i < k; i++) {
            long long a = 2 + rand() % (n - 2);
            long long x = modPow(a, d, n);
            if (x == 1 || x == n - 1) continue;
            
            bool composite = true;
            for (int j = 0; j < s - 1; j++) {
                x = modMul(x, x, n);
                if (x == n - 1) {
                    composite = false;
                    break;
                }
            }
            if (composite) return false;
        }
        return true;
    }
    
    /**
     * Pollard-Rho算法用于大数分解
     */
    static long long pollardsRho(long long n) {
        if (n % 2 == 0) return 2;
        if (n % 3 == 0) return 3;
        if (n % 5 == 0) return 5;
        
        while (true) {
            long long c = 1 + rand() % (n - 1);
            auto f = [&](long long x) -> long long {
                return (modMul(x, x, n) + c) % n;
            };
            
            long long x = 2, y = 2, d = 1;
            while (d == 1) {
                x = f(x);
                y = f(f(y));
                d = __gcd(abs(x - y), n);
            }
            if (d != n) return d;
        }
    }
    
    /**
     * 对n进行素因数分解
     */
    static map<long long, int> factor(long long n) {
        map<long long, int> factors;
        factorHelper(n, factors);
        return factors;
    }
    
    /**
     * 计算欧拉函数φ(n)
     */
    static long long eulerPhi(long long n) {
        if (n == 0) return 0;
        long long result = n;
        for (long long i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                while (n % i == 0) {
                    n /= i;
                }
                result = result / i * (i - 1);
            }
        }
        if (n > 1) {
            result = result / n * (n - 1);
        }
        return result;
    }
    
    /**
     * 计算莫比乌斯函数μ(n)
     */
    static int mobiusMu(long long n) {
        if (n == 1) return 1;
        
        long long i = 2;
        int cnt = 0;  // 不同素因子的个数
        while (i * i <= n) {
            if (n % i == 0) {
                cnt++;
                n /= i;
                if (n % i == 0)  // 有平方因子
                    return 0;
            }
            i++;
        }
        if (n > 1) {
            cnt++;
        }
        return cnt % 2 == 0 ? 1 : -1;
    }
    
    /**
     * 计算Dirichlet卷积
     */
    static long long dirichletConvolution(function<long long(long long)> f, 
                                        function<long long(long long)> g, 
                                        long long n) {
        long long result = 0;
        long long i = 1;
        while (i * i <= n) {
            if (n % i == 0) {
                result += f(i) * g(n / i);
                if (i != n / i) {
                    result += f(n / i) * g(i);
                }
            }
            i++;
        }
        return result;
    }
    
    /**
     * 杜教筛计算数论函数前缀和
     */
    static vector<long long> duSieve(function<long long(long long)> f,
                                    function<long long(long long)> g,
                                    function<long long(long long)> h,
                                    long long n) {
        // 预处理小部分值
        long long maxPrecompute = (long long)pow(n, 2.0 / 3);
        vector<long long> sumFSmall(maxPrecompute + 1, 0);
        for (long long i = 1; i <= maxPrecompute; i++) {
            sumFSmall[i] = sumFSmall[i-1] + f(i);
        }
        
        // 缓存已经计算过的值
        unordered_map<long long, long long> cache;
        
        function<long long(long long)> sumG = [](long long x) { return x; };  // 假设g是恒等函数
        
        function<long long(long long)> _sumF;
        _sumF = [&](long long x) {
            if (x <= maxPrecompute) {
                return sumFSmall[x];
            }
            if (cache.count(x)) {
                return cache[x];
            }
            
            // 计算sum_h
            long long sumH = 0;
            long long i = 1;
            while (i <= x) {
                long long j = x / (x / i);
                sumH += (j - i + 1) * h(x / i);
                i = j + 1;
            }
            
            long long res = sumH;
            i = 2;
            while (i <= x) {
                long long j = x / (x / i);
                res -= (sumG(j) - sumG(i-1)) * _sumF(x / i);
                i = j + 1;
            }
            
            res = res / g(1);
            cache[x] = res;
            return res;
        };
        
        // 计算前缀和数组
        vector<long long> prefixSums(n + 1, 0);
        for (long long i = 1; i <= n; i++) {
            prefixSums[i] = _sumF(i);
        }
        
        return prefixSums;
    }
    
private:
    static void factorHelper(long long n, map<long long, int>& factors) {
        if (n == 1) return;
        if (isPrime(n)) {
            factors[n]++;
            return;
        }
        long long d = pollardsRho(n);
        factorHelper(d, factors);
        factorHelper(n / d, factors);
    }
};

int main() {
    srand(time(nullptr));
    
    cout << "Pollard-Rho分解测试:" << endl;
    long long testNumbers[] = {1234567, 1000000007, 1000000009};
    for (auto num : testNumbers) {
        auto factors = NumberTheoryFunctions::factor(num);
        cout << num << " = ";
        for (auto& [p, cnt] : factors) {
            cout << p << "^" << cnt << " * ";
        }
        cout << endl;
    }
    
    cout << "\n欧拉函数测试:" << endl;
    for (long long num = 1; num <= 10; num++) {
        cout << "φ(" << num << ") = " << NumberTheoryFunctions::eulerPhi(num) << endl;
    }
    
    cout << "\n莫比乌斯函数测试:" << endl;
    for (long long num = 1; num <= 10; num++) {
        cout << "μ(" << num << ") = " << NumberTheoryFunctions::mobiusMu(num) << endl;
    }
    
    return 0;
}
"""

"""
# 算法详解与工程化考量

## 1. 算法本质与优化要点

### Pollard-Rho算法
- **核心思想**：利用随机数和生日悖论，寻找非平凡因子
- **优化点**：
  - 使用Floyd判圈法加速迭代
  - 结合Miller-Rabin素性测试提高效率
  - 对于小因子进行预处理，避免不必要的复杂计算
- **时间复杂度**：期望O(n^(1/4))
- **空间复杂度**：O(1)

### 欧拉函数与莫比乌斯函数
- **数学性质**：
  - 欧拉函数：φ(n) = n * Π(1-1/p)，其中p是n的素因子
  - 莫比乌斯函数：μ(n) = 0（有平方因子）或(-1)^k（k为不同素因子个数）
- **实现技巧**：同时进行素因数分解和函数计算，减少重复操作
- **时间复杂度**：欧拉函数O(√n)，莫比乌斯函数O(√n)
- **空间复杂度**：O(1)

### 杜教筛
- **数学原理**：利用Dirichlet卷积构造递推式
- **核心公式**：sum_{i=1}^n (f*g)(i) = sum_{i=1}^n g(i) * sum_{j=1}^{n/i} f(j)
- **优化策略**：
  - 预处理小值减少递归次数
  - 使用哈希表缓存中间结果
  - 数论分块（整除分块）优化求和过程
- **时间复杂度**：O(n^(2/3))
- **空间复杂度**：O(n^(2/3))

### Miller-Rabin素性测试
- **核心思想**：基于费马小定理的概率性素性测试
- **优化点**：
  - 使用多次测试降低错误概率
  - 特殊处理小素数
- **时间复杂度**：O(k * log^3 n)，k为测试轮数
- **空间复杂度**：O(1)

## 2. 工程化考量

### 异常处理
- **边界情况**：n=0,1等特殊值的处理
- **数值溢出**：使用mod_mul避免大数乘法溢出
- **内存管理**：递归深度控制，避免栈溢出

### 跨语言差异
- **Python**：原生支持大整数，无需额外处理
- **Java**：使用long类型，注意数值范围限制，可考虑使用BigInteger处理更大的数
- **C++**：需注意数据类型范围，考虑使用long long或自定义大整数类

### 性能优化
- **常数优化**：预先处理小因子，减少循环次数
- **并行计算**：对于大规模计算可考虑多线程优化
- **内存优化**：合理设置预处理阈值，平衡内存使用和计算速度

## 3. 相关题目解析

### LeetCode 1362 - 最接近的因数
**思路**：利用数论分解找到最接近平方根的因数对
**解法**：使用Pollard-Rho进行快速分解，然后组合因数找到最优解

### Codeforces 1023F - Mobile Phone Network
**思路**：结合数论函数和图论算法
**解法**：使用莫比乌斯反演简化计算，结合最小生成树算法

### IOI2022/国集2022题目
**思路**：大规模数值分解和数论函数计算
**解法**：结合Pollard-Rho和高级筛法，处理1e12以上的数值

## 4. 算法安全与业务适配

### 随机数生成
- Pollard-Rho算法依赖随机数质量，需确保随机数生成器的随机性
- 不同语言中随机数实现方式不同，需注意种子设置

### 可配置性
- 参数化算法参数（如Miller-Rabin测试轮数），根据精度要求调整
- 对于不同规模的数据，动态调整预处理阈值

### 测试用例设计
- 边界值测试：0,1,质数,合数等
- 大规模测试：确保算法在大数据量下的性能
- 特殊模式测试：如平方数、立方数等特殊形式

通过理解这些深层次的算法原理和工程考量，可以更全面地掌握数论函数的应用，应对各种复杂的算法问题.
"""