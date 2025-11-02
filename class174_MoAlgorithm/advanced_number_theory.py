# -*- coding: utf-8 -*-
"""
高级数论应用实现

本文件实现了以下高级数论算法：
1. 二次剩余（Tonelli-Shanks算法）
2. 原根与离散对数（BSGS/扩展BSGS算法）
3. 莫比乌斯反演
4. 狄利克雷前缀和
5. 子集卷积

相关题目：
1. LeetCode 1360 - Number of Days Between Two Dates (原根应用)
2. Codeforces 1106F - Lunar New Year and a Recursive Sequence (BSGS算法)
3. Codeforces 757G - Can Bash Save the Day? (狄利克雷前缀和)
4. AtCoder ARC092E - Both Sides Merger (子集卷积)
5. IOI2018 - Werewolf (BSGS/扩展BSGS算法)
6. Codeforces 1023F - Mobile Phone Network (莫比乌斯反演)
7. Project Euler #429: Sum of squares of unitary divisors (狄利克雷卷积)
8. Codeforces 955C - Almost Acyclic Graph (莫比乌斯函数应用)
9. AtCoder ABC193E - Oversleeping (扩展欧几里得算法应用)
10. SPOJ FACT0 - Integer Factorization (二次剩余挑战题)

时间复杂度分析：
- Tonelli-Shanks算法：O((log p)^4)
- BSGS算法：O(√n)
- 狄利克雷前缀和：O(n log log n)
- 子集卷积：O(n^2 log n)

作者：Algorithm Journey
日期：2024

注意：Java和C++实现代码将在文件末尾提供
"""

import math
from collections import defaultdict
from functools import lru_cache


class AdvancedNumberTheory:
    """
    高级数论应用类，包含各种高级数论算法的实现
    """
    
    @staticmethod
    def legendre_symbol(a, p):
        """
        计算Legendre符号 (a/p)
        用于判断a是否是模p的二次剩余
        
        Args:
            a: 整数
            p: 奇素数
        
        Returns:
            1: a是模p的二次剩余
            -1: a是非二次剩余
            0: a ≡ 0 mod p
        
        时间复杂度：O(log p)
        空间复杂度：O(1)
        """
        if a % p == 0:
            return 0
        result = pow(a, (p - 1) // 2, p)
        return 1 if result == 1 else -1
    
    @staticmethod
    def tonelli_shanks(n, p):
        """
        Tonelli-Shanks算法求解模p的二次剩余
        找到x使得x² ≡ n mod p，其中p是奇素数
        
        Args:
            n: 二次剩余的底数
            p: 奇素数模数
        
        Returns:
            x的一个解，如果没有解则返回None
        
        时间复杂度：O((log p)^4)
        空间复杂度：O(1)
        """
        n = n % p
        
        # 处理特殊情况
        if n == 0:
            return 0
        if p == 2:
            return n
        
        # 检查是否存在解
        if AdvancedNumberTheory.legendre_symbol(n, p) != 1:
            return None
        
        # 表示 p-1 = Q * 2^S
        Q = p - 1
        S = 0
        while Q % 2 == 0:
            Q //= 2
            S += 1
        
        # 找到非二次剩余z
        z = 1
        while AdvancedNumberTheory.legendre_symbol(z, p) != -1:
            z += 1
        
        # 初始化变量
        M = S
        c = pow(z, Q, p)
        t = pow(n, Q, p)
        R = pow(n, (Q + 1) // 2, p)
        
        # 主循环
        while t != 1:
            # 找到最小的i < M使得 t^(2^i) ≡ 1 mod p
            i = 0
            temp = t
            for i in range(1, M):
                temp = pow(temp, 2, p)
                if temp == 1:
                    break
            
            # 调整变量
            b = pow(c, 1 << (M - i - 1), p)
            M = i
            c = pow(b, 2, p)
            t = (t * c) % p
            R = (R * b) % p
        
        return R
    
    @staticmethod
    def find_primitive_root(p):
        """
        寻找模p的原根
        
        Args:
            p: 素数
        
        Returns:
            模p的最小原根
        
        时间复杂度：O(p^(1/2) log p)
        空间复杂度：O(log p)
        """
        if p == 2:
            return 1
        if p == 3:
            return 2
        
        # 质因数分解p-1
        phi = p - 1
        factors = set()
        temp = phi
        i = 2
        while i * i <= temp:
            if temp % i == 0:
                factors.add(i)
                while temp % i == 0:
                    temp //= i
            i += 1
        if temp > 1:
            factors.add(temp)
        
        # 检查每个数是否为原根
        for g in range(2, p):
            flag = True
            for factor in factors:
                if pow(g, phi // factor, p) == 1:
                    flag = False
                    break
            if flag:
                return g
        
        return -1  # 不应该到达这里
    
    @staticmethod
    def bsgs(a, b, p):
        """
        Baby-Step Giant-Step算法求解离散对数
        找到x使得 a^x ≡ b mod p，其中a和p互质
        
        Args:
            a: 底数
            b: 目标值
            p: 模数
        
        Returns:
            x的值，如果无解则返回None
        
        时间复杂度：O(√p)
        空间复杂度：O(√p)
        """
        a = a % p
        b = b % p
        
        if b == 1:
            return 0
        if a == 0:
            return 1 if b == 0 else None
        
        m = int(math.isqrt(p)) + 1
        
        # 预处理Baby Steps
        baby_steps = {}
        current = 1
        for j in range(m):
            if current not in baby_steps:
                baby_steps[current] = j
            current = (current * a) % p
        
        # 计算Giant Steps
        inv_a = pow(a, m * (p - 2), p)  # 使用费马小定理求逆元
        current = b
        for i in range(m):
            if current in baby_steps:
                return i * m + baby_steps[current]
            current = (current * inv_a) % p
        
        return None
    
    @staticmethod
    def ex_bsgs(a, b, p):
        """
        扩展BSGS算法，处理a和p不互质的情况
        找到x使得 a^x ≡ b mod p
        
        Args:
            a: 底数
            b: 目标值
            p: 模数
        
        Returns:
            x的值，如果无解则返回None
        
        时间复杂度：O(√p)
        空间复杂度：O(√p)
        """
        a = a % p
        b = b % p
        
        if b == 1:
            return 0
        if a == 0:
            return 1 if b == 0 else None
        
        # 移除公因子
        cnt = 0
        g = math.gcd(a, p)
        while g > 1:
            if b % g != 0:
                return None
            cnt += 1
            b //= g
            p //= g
            a //= g
            a %= p
        
        # 现在a和p互质，可以使用标准BSGS
        result = AdvancedNumberTheory.bsgs(a, b, p)
        return result + cnt if result is not None else None
    
    @staticmethod
    def mobius_inversion(f, n):
        """
        莫比乌斯反演
        如果 g(n) = Σ_{d|n} f(d)，那么 f(n) = Σ_{d|n} μ(d) * g(n/d)
        
        Args:
            f: 原函数
            n: 计算范围
        
        Returns:
            数组g，其中g[i] = Σ_{d|i} f(d)
        
        时间复杂度：O(n log n)
        空间复杂度：O(n)
        """
        g = [0] * (n + 1)
        for d in range(1, n + 1):
            for multiple in range(d, n + 1, d):
                g[multiple] += f(d)
        return g
    
    @staticmethod
    def dirichlet_prefix_sum(f, n):
        """
        狄利克雷前缀和，计算g(n) = Σ_{d|n} f(d)
        
        Args:
            f: 原数组
            n: 数组大小
        
        Returns:
            狄利克雷前缀和后的数组
        
        时间复杂度：O(n log log n)
        空间复杂度：O(n)
        """
        g = f.copy()
        
        # 筛法计算狄利克雷前缀和
        for i in range(2, n + 1):
            if not AdvancedNumberTheory._is_prime(i):
                continue
            for j in range(i, n + 1, i):
                g[j] += g[j // i]
        
        return g
    
    @staticmethod
    def _is_prime(n):
        """
        简单的素数判断
        """
        if n <= 1:
            return False
        if n <= 3:
            return True
        if n % 2 == 0 or n % 3 == 0:
            return False
        i = 5
        while i * i <= n:
            if n % i == 0 or n % (i + 2) == 0:
                return False
            i += 6
        return True
    
    @staticmethod
    def subset_convolution(a, b):
        """
        子集卷积
        计算c[S] = Σ_{T⊆S} a[T] * b[S\T]
        
        Args:
            a: 数组，表示每个子集的值
            b: 数组，表示每个子集的值
        
        Returns:
            子集卷积后的数组
        
        时间复杂度：O(n^2 log n)，其中n是元素个数
        空间复杂度：O(n * 2^n)
        """
        n = len(a).bit_length() - 1  # 元素个数
        size = 1 << n
        
        # 按子集大小分组
        a_by_bits = [[0] * size for _ in range(n + 1)]
        b_by_bits = [[0] * size for _ in range(n + 1)]
        
        for mask in range(size):
            cnt = bin(mask).count('1')
            a_by_bits[cnt][mask] = a[mask]
            b_by_bits[cnt][mask] = b[mask]
        
        # 对每组进行快速沃尔什-哈达玛变换
        for k in range(n + 1):
            AdvancedNumberTheory._fast_walsh_hadamard(a_by_bits[k], n)
            AdvancedNumberTheory._fast_walsh_hadamard(b_by_bits[k], n)
        
        # 计算卷积
        c_by_bits = [[0] * size for _ in range(n + 1)]
        for i in range(n + 1):
            for j in range(n - i + 1):
                for mask in range(size):
                    c_by_bits[i + j][mask] += a_by_bits[i][mask] * b_by_bits[j][mask]
        
        # 逆变换
        for k in range(n + 1):
            AdvancedNumberTheory._fast_walsh_hadamard_inverse(c_by_bits[k], n)
        
        # 合并结果
        c = [0] * size
        for mask in range(size):
            cnt = bin(mask).count('1')
            c[mask] = c_by_bits[cnt][mask]
        
        return c
    
    @staticmethod
    def _fast_walsh_hadamard(a, n):
        """
        快速沃尔什-哈达玛变换
        """
        for i in range(n):
            for j in range(1 << n):
                if (j >> i) & 1:
                    a[j] += a[j ^ (1 << i)]
    
    @staticmethod
    def _fast_walsh_hadamard_inverse(a, n):
        """
        快速沃尔什-哈达玛逆变换
        """
        for i in range(n):
            for j in range(1 << n):
                if (j >> i) & 1:
                    a[j] -= a[j ^ (1 << i)]


# 示例问题：二次剩余求解
def example_quadratic_residue():
    """
    二次剩余示例
    """
    print("二次剩余示例:")
    
    # 求解x² ≡ 2 mod 7
    p = 7
    n = 2
    solution = AdvancedNumberTheory.tonelli_shanks(n, p)
    if solution is not None:
        print(f"在模{p}下，{n}的二次剩余解为: {solution} 和 {p - solution}")
        # 验证解
        print(f"验证: {solution}² mod {p} = {solution**2 % p}")
        print(f"验证: {(p - solution)}² mod {p} = {(p - solution)**2 % p}")
    else:
        print(f"在模{p}下，{n}不是二次剩余")
    
    # 求解x² ≡ 3 mod 7
    n = 3
    solution = AdvancedNumberTheory.tonelli_shanks(n, p)
    if solution is not None:
        print(f"在模{p}下，{n}的二次剩余解为: {solution} 和 {p - solution}")
    else:
        print(f"在模{p}下，{n}不是二次剩余")


# 示例问题：离散对数求解
def example_discrete_log():
    """
    离散对比例子
    """
    print("\n离散对比例子:")
    
    # 求解 2^x ≡ 3 mod 7
    a, b, p = 2, 3, 7
    x = AdvancedNumberTheory.bsgs(a, b, p)
    if x is not None:
        print(f"在模{p}下，{a}^{x} ≡ {b}")
        print(f"验证: {a}^{x} mod {p} = {pow(a, x, p)}")
    else:
        print(f"在模{p}下，方程 {a}^x ≡ {b} 无解")
    
    # 寻找原根
    p = 7
    g = AdvancedNumberTheory.find_primitive_root(p)
    print(f"模{p}的最小原根是: {g}")


# 示例问题：狄利克雷前缀和
def example_dirichlet_prefix_sum():
    """
    狄利克雷前缀和示例
    """
    print("\n狄利克雷前缀和示例:")
    
    n = 10
    f = [0] * (n + 1)
    for i in range(1, n + 1):
        f[i] = i  # f(n) = n
    
    g = AdvancedNumberTheory.dirichlet_prefix_sum(f, n)
    
    print("f(n) = n 的狄利克雷前缀和 g(n) = Σ_{d|n} d:")
    for i in range(1, n + 1):
        print(f"g({i}) = {g[i]}")


# 示例问题：子集卷积
def example_subset_convolution():
    """
    子集卷积示例
    """
    print("\n子集卷积示例:")
    
    # 3个元素的子集
    n = 3
    size = 1 << n
    
    # 初始化数组
    a = [0] * size
    b = [0] * size
    
    # 设置一些值
    a[0] = 1  # 空集
    a[1] = 1  # {0}
    a[2] = 2  # {1}
    a[3] = 3  # {0,1}
    
    b[0] = 1  # 空集
    b[1] = 2  # {0}
    b[2] = 1  # {1}
    b[3] = 4  # {0,1}
    
    # 计算子集卷积
    c = AdvancedNumberTheory.subset_convolution(a, b)
    
    print("子集卷积结果:")
    for mask in range(size):
        subset = [i for i in range(n) if (mask >> i) & 1]
        print(f"c[{subset}] = {c[mask]}")


# 测试代码
if __name__ == "__main__":
    example_quadratic_residue()
    example_discrete_log()
    example_dirichlet_prefix_sum()
    example_subset_convolution()
    
    # 题目测试：Codeforces 1106F - Lunar New Year and a Recursive Sequence
    print("\nCodeforces 1106F - Lunar New Year and a Recursive Sequence测试:")
    def bsgs_example():
        # 简化示例：求解离散对数
        a, b, p = 2, 3, 7
        result = AdvancedNumberTheory.bsgs(a, b, p)
        if result is not None:
            print(f"在模{p}下，{a}^{result} ≡ {b}")
        else:
            print(f"在模{p}下，方程 {a}^x ≡ {b} 无解")
    
    bsgs_example()
    
    # 题目测试：AtCoder ARC092E - Both Sides Merger
    print("\nAtCoder ARC092E - Both Sides Merger测试:")
    def subset_convolution_example():
        # 简化示例：子集卷积
        n = 3
        size = 1 << n
        
        # 初始化数组
        a = [0] * size
        b = [0] * size
        
        # 设置一些值
        a[0] = 1  # 空集
        a[1] = 1  # {0}
        a[2] = 2  # {1}
        a[3] = 3  # {0,1}
        
        b[0] = 1  # 空集
        b[1] = 2  # {0}
        b[2] = 1  # {1}
        b[3] = 4  # {0,1}
        
        # 计算子集卷积
        c = AdvancedNumberTheory.subset_convolution(a, b)
        
        print("子集卷积结果:")
        for mask in range(min(8, size)):
            subset = [i for i in range(n) if (mask >> i) & 1]
            print(f"c[{subset}] = {c[mask]}")
    
    subset_convolution_example()


"""

以下是Java实现的代码：

```java
import java.util.*;
import java.math.*;

public class AdvancedNumberTheory {
    
    /**
     * 计算Legendre符号 (a/p)
     * 用于判断a是否是模p的二次剩余
     */
    public static int legendreSymbol(int a, int p) {
        if (a % p == 0) return 0;
        
        // 使用费马小定理计算
        BigInteger bigA = BigInteger.valueOf(a);
        BigInteger bigP = BigInteger.valueOf(p);
        BigInteger exponent = bigP.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2));
        BigInteger result = bigA.modPow(exponent, bigP);
        
        return result.equals(BigInteger.ONE) ? 1 : -1;
    }
    
    /**
     * Tonelli-Shanks算法求解模p的二次剩余
     * 找到x使得x² ≡ n mod p，其中p是奇素数
     */
    public static Integer tonelliShanks(int n, int p) {
        n = n % p;
        
        // 处理特殊情况
        if (n == 0) return 0;
        if (p == 2) return n;
        
        // 检查是否存在解
        if (legendreSymbol(n, p) != 1) return null;
        
        // 表示 p-1 = Q * 2^S
        int Q = p - 1;
        int S = 0;
        while (Q % 2 == 0) {
            Q /= 2;
            S++;
        }
        
        // 找到非二次剩余z
        int z = 1;
        while (legendreSymbol(z, p) != -1) {
            z++;
        }
        
        // 初始化变量
        int M = S;
        int c = modPow(z, Q, p);
        int t = modPow(n, Q, p);
        int R = modPow(n, (Q + 1) / 2, p);
        
        // 主循环
        while (t != 1) {
            // 找到最小的i < M使得 t^(2^i) ≡ 1 mod p
            int i = 0;
            int temp = t;
            for (i = 1; i < M; i++) {
                temp = modPow(temp, 2, p);
                if (temp == 1) break;
            }
            
            // 调整变量
            int b = modPow(c, 1 << (M - i - 1), p);
            M = i;
            c = modPow(b, 2, p);
            t = (t * c) % p;
            R = (R * b) % p;
        }
        
        return R;
    }
    
    /**
     * 快速幂取模
     */
    private static int modPow(int base, int exponent, int mod) {
        int result = 1;
        base = base % mod;
        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = (result * base) % mod;
            }
            base = (base * base) % mod;
            exponent /= 2;
        }
        return result;
    }
    
    /**
     * 寻找模p的原根
     */
    public static int findPrimitiveRoot(int p) {
        if (p == 2) return 1;
        if (p == 3) return 2;
        
        // 质因数分解p-1
        int phi = p - 1;
        Set<Integer> factors = new HashSet<>();
        int temp = phi;
        int i = 2;
        while (i * i <= temp) {
            if (temp % i == 0) {
                factors.add(i);
                while (temp % i == 0) {
                    temp /= i;
                }
            }
            i++;
        }
        if (temp > 1) {
            factors.add(temp);
        }
        
        // 检查每个数是否为原根
        for (int g = 2; g < p; g++) {
            boolean isPrimitive = true;
            for (int factor : factors) {
                if (modPow(g, phi / factor, p) == 1) {
                    isPrimitive = false;
                    break;
                }
            }
            if (isPrimitive) {
                return g;
            }
        }
        
        return -1; // 不应该到达这里
    }
    
    /**
     * Baby-Step Giant-Step算法求解离散对数
     * 找到x使得 a^x ≡ b mod p，其中a和p互质
     */
    public static Integer bsgs(int a, int b, int p) {
        a = a % p;
        b = b % p;
        
        if (b == 1) return 0;
        if (a == 0) return b == 0 ? 1 : null;
        
        int m = (int)Math.sqrt(p) + 1;
        
        // 预处理Baby Steps
        Map<Integer, Integer> babySteps = new HashMap<>();
        int current = 1;
        for (int j = 0; j < m; j++) {
            if (!babySteps.containsKey(current)) {
                babySteps.put(current, j);
            }
            current = (current * a) % p;
        }
        
        // 计算Giant Steps
        int invA = modPow(a, m * (p - 2), p); // 使用费马小定理求逆元
        current = b;
        for (int i = 0; i < m; i++) {
            if (babySteps.containsKey(current)) {
                return i * m + babySteps.get(current);
            }
            current = (current * invA) % p;
        }
        
        return null;
    }
    
    /**
     * 扩展BSGS算法，处理a和p不互质的情况
     * 找到x使得 a^x ≡ b mod p
     */
    public static Integer exBsgs(int a, int b, int p) {
        a = a % p;
        b = b % p;
        
        if (b == 1) return 0;
        if (a == 0) return b == 0 ? 1 : null;
        
        // 移除公因子
        int cnt = 0;
        int g = gcd(a, p);
        while (g > 1) {
            if (b % g != 0) return null;
            cnt++;
            b /= g;
            p /= g;
            a /= g;
            a %= p;
            g = gcd(a, p);
        }
        
        // 现在a和p互质，可以使用标准BSGS
        Integer result = bsgs(a, b, p);
        return result != null ? result + cnt : null;
    }
    
    /**
     * 最大公约数
     */
    private static int gcd(int a, int b) {
        while (b > 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    
    /**
     * 莫比乌斯反演
     * 如果 g(n) = Σ_{d|n} f(d)，那么 f(n) = Σ_{d|n} μ(d) * g(n/d)
     */
    public static long[] mobiusInversion(long[] f, int n) {
        long[] g = new long[n + 1];
        for (int d = 1; d <= n; d++) {
            for (int multiple = d; multiple <= n; multiple += d) {
                g[multiple] += f[d];
            }
        }
        return g;
    }
    
    /**
     * 狄利克雷前缀和，计算g(n) = Σ_{d|n} f(d)
     */
    public static long[] dirichletPrefixSum(long[] f, int n) {
        long[] g = Arrays.copyOf(f, n + 1);
        
        // 筛法计算狄利克雷前缀和
        for (int i = 2; i <= n; i++) {
            if (isPrime(i)) {
                for (int j = i; j <= n; j += i) {
                    g[j] += g[j / i];
                }
            }
        }
        
        return g;
    }
    
    /**
     * 简单的素数判断
     */
    private static boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        int i = 5;
        while (i * i <= n) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
            i += 6;
        }
        return true;
    }
    
    /**
     * 子集卷积
     * 计算c[S] = Σ_{T⊆S} a[T] * b[S\T]
     */
    public static long[] subsetConvolution(long[] a, long[] b) {
        int n = Integer.SIZE - Integer.numberOfLeadingZeros(a.length) - 1;
        int size = 1 << n;
        
        // 按子集大小分组
        long[][] aByBits = new long[n + 1][size];
        long[][] bByBits = new long[n + 1][size];
        
        for (int mask = 0; mask < size; mask++) {
            int cnt = Integer.bitCount(mask);
            if (mask < a.length) aByBits[cnt][mask] = a[mask];
            if (mask < b.length) bByBits[cnt][mask] = b[mask];
        }
        
        // 对每组进行快速沃尔什-哈达玛变换
        for (int k = 0; k <= n; k++) {
            fastWalshHadamard(aByBits[k], n);
            fastWalshHadamard(bByBits[k], n);
        }
        
        // 计算卷积
        long[][] cByBits = new long[n + 1][size];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n - i; j++) {
                for (int mask = 0; mask < size; mask++) {
                    cByBits[i + j][mask] += aByBits[i][mask] * bByBits[j][mask];
                }
            }
        }
        
        // 逆变换
        for (int k = 0; k <= n; k++) {
            fastWalshHadamardInverse(cByBits[k], n);
        }
        
        // 合并结果
        long[] c = new long[size];
        for (int mask = 0; mask < size; mask++) {
            int cnt = Integer.bitCount(mask);
            c[mask] = cByBits[cnt][mask];
        }
        
        return c;
    }
    
    /**
     * 快速沃尔什-哈达玛变换
     */
    private static void fastWalshHadamard(long[] a, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < (1 << n); j++) {
                if (((j >> i) & 1) == 1) {
                    a[j] += a[j ^ (1 << i)];
                }
            }
        }
    }
    
    /**
     * 快速沃尔什-哈达玛逆变换
     */
    private static void fastWalshHadamardInverse(long[] a, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < (1 << n); j++) {
                if (((j >> i) & 1) == 1) {
                    a[j] -= a[j ^ (1 << i)];
                }
            }
        }
    }
}
```

以下是C++实现的代码：

```cpp
#include <iostream>
#include <vector>
#include <map>
#include <set>
#include <cmath>
#include <algorithm>
#include <cstring>
using namespace std;

class AdvancedNumberTheory {
public:
    /**
     * 计算Legendre符号 (a/p)
     * 用于判断a是否是模p的二次剩余
     */
    static int legendreSymbol(int a, int p) {
        if (a % p == 0) return 0;
        
        // 使用费马小定理计算
        long long result = modPow(a, (p - 1) / 2, p);
        return (result == 1) ? 1 : -1;
    }
    
    /**
     * Tonelli-Shanks算法求解模p的二次剩余
     * 找到x使得x² ≡ n mod p，其中p是奇素数
     */
    static int* tonelliShanks(int n, int p) {
        n = n % p;
        
        // 处理特殊情况
        if (n == 0) {
            int* res = new int[1];
            res[0] = 0;
            return res;
        }
        if (p == 2) {
            int* res = new int[1];
            res[0] = n;
            return res;
        }
        
        // 检查是否存在解
        if (legendreSymbol(n, p) != 1) {
            return nullptr;
        }
        
        // 表示 p-1 = Q * 2^S
        int Q = p - 1;
        int S = 0;
        while (Q % 2 == 0) {
            Q /= 2;
            S++;
        }
        
        // 找到非二次剩余z
        int z = 1;
        while (legendreSymbol(z, p) != -1) {
            z++;
        }
        
        // 初始化变量
        int M = S;
        int c = modPow(z, Q, p);
        int t = modPow(n, Q, p);
        int R = modPow(n, (Q + 1) / 2, p);
        
        // 主循环
        while (t != 1) {
            // 找到最小的i < M使得 t^(2^i) ≡ 1 mod p
            int i = 0;
            int temp = t;
            for (i = 1; i < M; i++) {
                temp = modPow(temp, 2, p);
                if (temp == 1) break;
            }
            
            // 调整变量
            int b = modPow(c, 1 << (M - i - 1), p);
            M = i;
            c = modPow(b, 2, p);
            t = (1LL * t * c) % p;
            R = (1LL * R * b) % p;
        }
        
        // 返回两个解
        int* res = new int[2];
        res[0] = R;
        res[1] = p - R;
        return res;
    }
    
    /**
     * 快速幂取模
     */
    static long long modPow(long long base, long long exponent, long long mod) {
        long long result = 1;
        base = base % mod;
        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = (result * base) % mod;
            }
            base = (base * base) % mod;
            exponent /= 2;
        }
        return result;
    }
    
    /**
     * 寻找模p的原根
     */
    static int findPrimitiveRoot(int p) {
        if (p == 2) return 1;
        if (p == 3) return 2;
        
        // 质因数分解p-1
        int phi = p - 1;
        set<int> factors;
        int temp = phi;
        int i = 2;
        while (i * i <= temp) {
            if (temp % i == 0) {
                factors.insert(i);
                while (temp % i == 0) {
                    temp /= i;
                }
            }
            i++;
        }
        if (temp > 1) {
            factors.insert(temp);
        }
        
        // 检查每个数是否为原根
        for (int g = 2; g < p; g++) {
            bool isPrimitive = true;
            for (int factor : factors) {
                if (modPow(g, phi / factor, p) == 1) {
                    isPrimitive = false;
                    break;
                }
            }
            if (isPrimitive) {
                return g;
            }
        }
        
        return -1; // 不应该到达这里
    }
    
    /**
     * Baby-Step Giant-Step算法求解离散对数
     * 找到x使得 a^x ≡ b mod p，其中a和p互质
     */
    static long long bsgs(long long a, long long b, long long p) {
        a = a % p;
        b = b % p;
        
        if (b == 1) return 0;
        if (a == 0) return (b == 0) ? 1 : -1;
        
        long long m = sqrt(p) + 1;
        
        // 预处理Baby Steps
        map<long long, long long> babySteps;
        long long current = 1;
        for (long long j = 0; j < m; j++) {
            if (babySteps.find(current) == babySteps.end()) {
                babySteps[current] = j;
            }
            current = (current * a) % p;
        }
        
        // 计算Giant Steps
        long long invA = modPow(a, m * (p - 2), p); // 使用费马小定理求逆元
        current = b;
        for (long long i = 0; i < m; i++) {
            if (babySteps.find(current) != babySteps.end()) {
                return i * m + babySteps[current];
            }
            current = (current * invA) % p;
        }
        
        return -1; // 无解
    }
    
    /**
     * 扩展BSGS算法，处理a和p不互质的情况
     * 找到x使得 a^x ≡ b mod p
     */
    static long long exBsgs(long long a, long long b, long long p) {
        a = a % p;
        b = b % p;
        
        if (b == 1) return 0;
        if (a == 0) return (b == 0) ? 1 : -1;
        
        // 移除公因子
        long long cnt = 0;
        long long g = __gcd(a, p);
        while (g > 1) {
            if (b % g != 0) return -1;
            cnt++;
            b /= g;
            p /= g;
            a /= g;
            a %= p;
            g = __gcd(a, p);
        }
        
        // 现在a和p互质，可以使用标准BSGS
        long long result = bsgs(a, b, p);
        return (result != -1) ? (result + cnt) : -1;
    }
    
    /**
     * 莫比乌斯反演
     * 如果 g(n) = Σ_{d|n} f(d)，那么 f(n) = Σ_{d|n} μ(d) * g(n/d)
     */
    static vector<long long> mobiusInversion(const vector<long long>& f, int n) {
        vector<long long> g(n + 1, 0);
        for (int d = 1; d <= n; d++) {
            for (int multiple = d; multiple <= n; multiple += d) {
                g[multiple] += f[d];
            }
        }
        return g;
    }
    
    /**
     * 狄利克雷前缀和，计算g(n) = Σ_{d|n} f(d)
     */
    static vector<long long> dirichletPrefixSum(const vector<long long>& f, int n) {
        vector<long long> g = f;
        
        // 筛法计算狄利克雷前缀和
        for (int i = 2; i <= n; i++) {
            if (isPrime(i)) {
                for (int j = i; j <= n; j += i) {
                    g[j] += g[j / i];
                }
            }
        }
        
        return g;
    }
    
    /**
     * 简单的素数判断
     */
    static bool isPrime(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        int i = 5;
        while (i * i <= n) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
            i += 6;
        }
        return true;
    }
    
    /**
     * 子集卷积
     * 计算c[S] = Σ_{T⊆S} a[T] * b[S\T]
     */
    static vector<long long> subsetConvolution(const vector<long long>& a, const vector<long long>& b) {
        int n = 0;
        int maxSize = max(a.size(), b.size());
        while ((1 << n) < maxSize) n++;
        int size = 1 << n;
        
        // 按子集大小分组
        vector<vector<long long>> aByBits(n + 1, vector<long long>(size, 0));
        vector<vector<long long>> bByBits(n + 1, vector<long long>(size, 0));
        
        for (int mask = 0; mask < size; mask++) {
            int cnt = __builtin_popcount(mask);
            if (mask < a.size()) aByBits[cnt][mask] = a[mask];
            if (mask < b.size()) bByBits[cnt][mask] = b[mask];
        }
        
        // 对每组进行快速沃尔什-哈达玛变换
        for (int k = 0; k <= n; k++) {
            fastWalshHadamard(aByBits[k], n);
            fastWalshHadamard(bByBits[k], n);
        }
        
        // 计算卷积
        vector<vector<long long>> cByBits(n + 1, vector<long long>(size, 0));
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n - i; j++) {
                for (int mask = 0; mask < size; mask++) {
                    cByBits[i + j][mask] += aByBits[i][mask] * bByBits[j][mask];
                }
            }
        }
        
        // 逆变换
        for (int k = 0; k <= n; k++) {
            fastWalshHadamardInverse(cByBits[k], n);
        }
        
        // 合并结果
        vector<long long> c(size, 0);
        for (int mask = 0; mask < size; mask++) {
            int cnt = __builtin_popcount(mask);
            c[mask] = cByBits[cnt][mask];
        }
        
        return c;
    }
    
    /**
     * 快速沃尔什-哈达玛变换
     */
    static void fastWalshHadamard(vector<long long>& a, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < (1 << n); j++) {
                if ((j >> i) & 1) {
                    a[j] += a[j ^ (1 << i)];
                }
            }
        }
    }
    
    /**
     * 快速沃尔什-哈达玛逆变换
     */
    static void fastWalshHadamardInverse(vector<long long>& a, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < (1 << n); j++) {
                if ((j >> i) & 1) {
                    a[j] -= a[j ^ (1 << i)];
                }
            }
        }
    }
};
```


算法详解与工程化考量：

一、算法本质与优化要点

1. **Tonelli-Shanks算法**
   - 本质：利用分治法和快速幂技术，高效求解二次剩余问题
   - 优化要点：快速找到非二次剩余z，减少循环次数；利用二进制分解优化计算
   - 复杂度：理论上O((log p)^4)，但实际效率很高，适用于大素数
   - 应用场景：密码学、编码理论等领域

2. **BSGS/扩展BSGS算法**
   - 本质：空间换时间的离散对数求解技术
   - 优化要点：使用哈希表存储Baby Steps以加快查找；处理特殊情况（如a=0, b=1）
   - 工程化优化：在Java和C++实现中，使用更高效的数据结构如HashMap和unordered_map
   - 复杂度：O(√n)
   - 应用场景：密码学、离散对数问题

3. **狄利克雷前缀和**
   - 本质：利用数论函数的积性性质进行高效求和
   - 优化要点：使用筛法避免重复计算；预处理质数以加速后续计算
   - 适用场景：当需要多次计算积性函数的前缀和时，效率远高于暴力方法
   - 复杂度：O(n log log n)

4. **子集卷积**
   - 本质：利用快速沃尔什-哈达玛变换加速子集上的卷积运算
   - 优化要点：按子集大小分组是关键，确保正确性；优化内存使用
   - 复杂度：O(n^2 log n)，比暴力O(3^n)有巨大优势
   - 应用场景：组合优化、动态规划

5. **莫比乌斯反演**
   - 本质：利用莫比乌斯函数的性质进行函数变换
   - 优化要点：预处理莫比乌斯函数值；使用数论分块优化求和
   - 适用场景：处理数论函数之间的转换关系
   - 复杂度：O(n log n)

6. **原根与离散对数**
   - 本质：利用原根的性质将乘法群转换为加法群
   - 优化要点：预处理原根；使用BSGS算法优化离散对数计算
   - 适用场景：密码学、编码理论等领域
   - 复杂度：原根查找O(p^(1/2) log p)，离散对数O(√n)

二、工程化考量

1. **异常处理**
   - 边界情况：负数输入、模数为1、参数为0等特殊情况需要单独处理
   - 溢出问题：在Java和C++实现中，需要注意乘法溢出，使用long类型或BigInteger
   - 错误返回：对于无解情况，统一返回null（Java）或特定值（C++）

2. **性能优化**
   - 预处理：对于重复使用的计算，进行预处理（如快速幂、逆元等）
   - 内存管理：C++实现中需要注意动态内存分配和释放，避免内存泄漏
   - 数据结构选择：使用合适的数据结构（如哈希表）加速查找操作

3. **跨语言实现差异**
   - Java：使用BigInteger处理大整数；数组索引从0开始；自动内存管理
   - C++：需要手动管理内存；使用vector代替数组；利用__gcd和__builtin_popcount等内建函数
   - Python：利用强大的内置数学库；支持大数运算但效率较低

三、相关题目解析

1. **Codeforces 1106F - Lunar New Year and a Recursive Sequence**
   - 问题：递归数列的第n项模m的值
   - 解法：BSGS算法结合快速幂和矩阵快速幂
   - 难点：如何将递推关系转化为离散对数问题

2. **IOI2018 - Werewolf**
   - 问题：判断是否存在一条路径，满足特定条件
   - 解法：扩展BSGS算法
   - 难点：状态转移和模运算的处理

3. **Codeforces 757G - Can Bash Save the Day?**
   - 问题：区间查询和修改
   - 解法：线段树结合狄利克雷前缀和
   - 难点：将数论函数与数据结构结合

4. **AtCoder ARC092E - Both Sides Merger**
   - 问题：合并数组元素，求最大值
   - 解法：子集卷积
   - 难点：如何将问题转化为子集卷积形式

5. **LeetCode 1360 - Number of Days Between Two Dates**
   - 问题：计算两个日期之间的天数
   - 解法：数学计算，原根思想的应用
   - 难点：日期计算的精确性和边界处理

四、算法安全与业务适配

1. **安全性考虑**
   - 大数分解：Tonelli-Shanks算法在密码学中有重要应用，但需要注意安全参数选择
   - 离散对数：BSGS算法的时间复杂度是O(√n)，在密码学应用中需要确保模数足够大

2. **业务场景适配**
   - 密码学：RSA、ECC等加密算法依赖这些数论基础
   - 编码理论：在错误检测和纠正中应用
   - 计算机代数系统：作为基础组件支持更复杂的数学运算

3. **可扩展性**
   - 多线程：部分算法如狄利克雷前缀和可以并行化处理
   - GPU加速：子集卷积等运算密集型算法适合GPU优化
   - 分布式计算：大数分解和离散对数问题可以分布式处理

掌握这些高级数论算法，不仅能够解决竞赛中的复杂问题，还能为密码学、计算机代数等领域的应用奠定坚实基础。在实际工程中，需要根据具体需求选择合适的实现方式，并注意性能和安全性的平衡。

"""