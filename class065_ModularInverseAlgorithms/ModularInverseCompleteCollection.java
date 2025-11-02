package class099;

import java.util.*;
import java.math.BigInteger;

/**
 * 模逆元完整题目集（综合版）
 * 包含从各大OJ平台收集的模逆元相关题目
 * 提供Java、C++、Python三种语言的实现思路
 * 
 * 模逆元定义：对于整数a和模数m，如果存在整数x使得 a*x ≡ 1 (mod m)，则称x为a在模m意义下的乘法逆元
 * 模逆元存在的充要条件：gcd(a, m) = 1，即a和m互质
 * 
 * 时间复杂度分析：
 * - 扩展欧几里得算法：O(log(min(a, m)))
 * - 费马小定理：O(log m)（仅当m为质数时）
 * - 线性递推：O(n)（批量计算1~n的逆元）
 * 
 * 空间复杂度分析：
 * - 扩展欧几里得算法：O(log(min(a, m)))（递归栈）
 * - 费马小定理：O(1)
 * - 线性递推：O(n)（存储逆元数组）
 * 
 * 工程化考量：
 * 1. 异常处理：处理负数输入、模数为0、逆元不存在等情况
 * 2. 性能优化：预计算、缓存、选择合适的算法
 * 3. 代码可读性：详细注释、有意义的变量名、模块化设计
 * 4. 边界测试：空输入、极端值、重复数据等
 * 5. 单元测试：确保算法正确性
 * 6. 性能测试：大规模数据下的表现
 * 
 * 语言特性差异：
 * - Java：使用long类型避免溢出，处理负数取模
 * - C++：使用long long类型，注意负数取模
 * - Python：内置大整数支持，使用pow(a, b, mod)进行快速幂
 * 
 * 应用场景：
 * - 密码学：RSA加密算法
 * - 组合数学：组合数计算
 * - 算法竞赛：数论题目
 * - 机器学习：大规模矩阵运算优化
 * - 图像处理：安全传输
 * - 自然语言处理：文本加密
 */
public class ModularInverseCompleteCollection {
    
    private static final int MOD = 1000000007;
    
    // ==================== 基础算法实现 ====================
    
    /**
     * 扩展欧几里得算法求模逆元（通用方法）
     * 适用于任何模数，是最通用的模逆元求解方法
     * 
     * 时间复杂度: O(log(min(a, m)))
     * 空间复杂度: O(log(min(a, m)))（递归调用栈）
     * 
     * @param a 要求逆元的数
     * @param m 模数
     * @return 如果存在逆元，返回最小正整数解；否则返回-1
     * 
     * 异常情况处理：
     * - 模数为0时抛出异常
     * - 负数输入会进行预处理
     * - 逆元不存在时返回-1
     */
    public static long modInverseExtendedGcd(long a, long m) {
        // 参数验证
        if (m == 0) {
            throw new IllegalArgumentException("Modulus cannot be zero");
        }
        
        // 处理负数情况，确保a和m都是正数
        a = (a % m + m) % m;
        m = Math.abs(m);
        
        long[] x = new long[1];
        long[] y = new long[1];
        long gcd = extendedGcd(a, m, x, y);
        
        // 逆元存在的充要条件是a和m互质
        if (gcd != 1) {
            return -1; // 逆元不存在
        }
        
        // 确保结果为正数
        return (x[0] % m + m) % m;
    }
    
    /**
     * 扩展欧几里得算法实现
     * 求解 ax + by = gcd(a, b)
     * 
     * @param a 系数a
     * @param b 系数b
     * @param x 用于返回x的解（数组引用传递）
     * @param y 用于返回y的解（数组引用传递）
     * @return gcd(a, b)
     */
    public static long extendedGcd(long a, long b, long[] x, long[] y) {
        if (b == 0) {
            x[0] = 1;
            y[0] = 0;
            return a;
        }
        
        long[] x1 = new long[1];
        long[] y1 = new long[1];
        long gcd = extendedGcd(b, a % b, x1, y1);
        
        x[0] = y1[0];
        y[0] = x1[0] - (a / b) * y1[0];
        
        return gcd;
    }
    
    /**
     * 快速幂运算
     * 计算base^exp mod mod
     * 
     * 时间复杂度: O(log exp)
     * 空间复杂度: O(1)
     * 
     * @param base 底数
     * @param exp 指数
     * @param mod 模数
     * @return base^exp mod mod
     */
    public static long power(long base, long exp, long mod) {
        if (mod == 0) throw new IllegalArgumentException("Modulus cannot be zero");
        if (exp < 0) throw new IllegalArgumentException("Exponent cannot be negative");
        
        long result = 1;
        base %= mod;
        
        while (exp > 0) {
            if ((exp & 1) == 1) {
                result = (result * base) % mod;
            }
            base = (base * base) % mod;
            exp >>= 1;
        }
        return result;
    }
    
    /**
     * 费马小定理求模逆元（仅当模数为质数时）
     * 根据费马小定理：a^(p-1) ≡ 1 (mod p)，所以 a^(-1) ≡ a^(p-2) (mod p)
     * 
     * 时间复杂度: O(log p)
     * 空间复杂度: O(1)
     * 
     * @param a 要求逆元的数
     * @param p 质数模数
     * @return a在模p意义下的逆元
     */
    public static long modInverseFermat(long a, long p) {
        if (p <= 1) throw new IllegalArgumentException("Prime modulus must be greater than 1");
        a = (a % p + p) % p;
        return power(a, p - 2, p);
    }
    
    /**
     * 线性递推求1~n所有整数的模逆元
     * 递推公式：inv[i] = (p - p/i) * inv[p%i] % p
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     * 
     * @param n 范围上限
     * @param p 模数（质数）
     * @return 逆元数组
     */
    public static long[] buildInverseAll(int n, int p) {
        long[] inv = new long[n + 1];
        inv[1] = 1;
        for (int i = 2; i <= n; i++) {
            inv[i] = (p - (p / i) * inv[p % i] % p) % p;
        }
        return inv;
    }
    
    // ==================== 各大OJ平台题目实现 ====================
    
    /**
     * 题目1: ZOJ 3609 Modular Inverse
     * 链接: http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=3609
     * 难度: 简单
     * 题意: 给定a和m，求a在模m意义下的乘法逆元
     */
    public static void solveZOJ3609() {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        while (t-- > 0) {
            long a = sc.nextLong();
            long m = sc.nextLong();
            long result = modInverseExtendedGcd(a, m);
            System.out.println(result == -1 ? "Not Exist" : result);
        }
        sc.close();
    }
    
    /**
     * 题目2: 洛谷 P3811 【模板】乘法逆元
     * 链接: https://www.luogu.com.cn/problem/P3811
     * 难度: 模板
     * 题意: 给定n和p，求1~n所有整数在模p意义下的乘法逆元
     */
    public static void solveLuoguP3811() {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int p = sc.nextInt();
        long[] inv = buildInverseAll(n, p);
        for (int i = 1; i <= n; i++) {
            System.out.println(inv[i]);
        }
        sc.close();
    }
    
    /**
     * 题目3: 洛谷 P2613 【模板】有理数取余
     * 链接: https://www.luogu.com.cn/problem/P2613
     * 难度: 模板
     * 题意: 计算两个大整数的除法结果模19260817
     */
    public static void solveLuoguP2613() {
        Scanner sc = new Scanner(System.in);
        BigInteger a = new BigInteger(sc.next());
        BigInteger b = new BigInteger(sc.next());
        BigInteger mod = new BigInteger("19260817");
        
        if (b.equals(BigInteger.ZERO)) {
            System.out.println("Angry!");
        } else {
            BigInteger result = a.multiply(b.modInverse(mod)).mod(mod);
            System.out.println(result);
        }
        sc.close();
    }
    
    /**
     * 题目4: POJ 1845 Sumdiv
     * 链接: http://poj.org/problem?id=1845
     * 难度: 中等
     * 题意: 计算A^B的所有约数之和模9901
     */
    public static int sumDiv(int A, int B) {
        final int MOD = 9901;
        if (A == 0) return 0;
        if (B == 0) return 1;
        
        long result = 1;
        // 质因数分解
        for (int i = 2; i * i <= A; i++) {
            if (A % i == 0) {
                int cnt = 0;
                while (A % i == 0) {
                    cnt++;
                    A /= i;
                }
                // 计算等比数列和: (i^(cnt*B+1)-1)/(i-1) mod MOD
                long numerator = (power(i, (long)cnt * B + 1, MOD) - 1 + MOD) % MOD;
                long denominator = modInverseExtendedGcd(i - 1, MOD);
                if (denominator == -1) {
                    // 当i-1 ≡ 0 mod MOD时，等比数列和为cnt*B+1
                    result = result * (cnt * B + 1) % MOD;
                } else {
                    result = result * numerator % MOD * denominator % MOD;
                }
            }
        }
        
        if (A > 1) {
            long numerator = (power(A, B + 1, MOD) - 1 + MOD) % MOD;
            long denominator = modInverseExtendedGcd(A - 1, MOD);
            if (denominator == -1) {
                result = result * (B + 1) % MOD;
            } else {
                result = result * numerator % MOD * denominator % MOD;
            }
        }
        
        return (int) result;
    }
    
    /**
     * 题目5: Codeforces 1445D. Divide and Sum
     * 链接: https://codeforces.com/problemset/problem/1445/D
     * 难度: 中等
     * 题意: 计算所有划分方案的f(p)值之和
     */
    public static long divideAndSum(int[] arr) {
        final int MOD = 998244353;
        int n = arr.length / 2;
        Arrays.sort(arr);
        
        // 预处理阶乘和阶乘逆元
        long[] fact = new long[2 * n + 1];
        long[] invFact = new long[2 * n + 1];
        fact[0] = 1;
        for (int i = 1; i <= 2 * n; i++) {
            fact[i] = fact[i - 1] * i % MOD;
        }
        invFact[2 * n] = power(fact[2 * n], MOD - 2, MOD);
        for (int i = 2 * n - 1; i >= 0; i--) {
            invFact[i] = invFact[i + 1] * (i + 1) % MOD;
        }
        
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum = (sum + arr[n + i] - arr[i]) % MOD;
        }
        sum = (sum % MOD + MOD) % MOD;
        
        // 计算组合数C(2n-1, n-1)
        long comb = fact[2 * n - 1] * invFact[n - 1] % MOD * invFact[n] % MOD;
        
        return sum * comb % MOD;
    }
    
    /**
     * 题目6: AtCoder ABC182E. Throne
     * 链接: https://atcoder.jp/contests/abc182/tasks/abc182_e
     * 难度: 中等
     * 题意: 在圆桌上移动，求到达特定位置的最小步数
     */
    public static long throne(long N, long S, long K) {
        // 解方程: (S + K*x) ≡ 0 (mod N)
        // 即 K*x ≡ -S (mod N)
        long g = gcd(K, N);
        if (S % g != 0) return -1;
        
        long newN = N / g;
        long newK = K / g;
        long newS = (-S) / g;
        
        long inv = modInverseExtendedGcd(newK, newN);
        if (inv == -1) return -1;
        
        long x = (inv * newS % newN + newN) % newN;
        return x;
    }
    
    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    /**
     * 题目7: HackerRank Number of Sequences
     * 链接: https://www.hackerrank.com/contests/hourrank-17/challenges/number-of-sequences
     * 难度: 中等
     * 题意: 计算满足特定条件的序列数量
     */
    public static long numberOfSequences(int n, int[] constraints) {
        final int MOD = 1000000007;
        long result = 1;
        
        for (int i = 1; i <= n; i++) {
            int count = 0;
            for (int j = i; j <= n; j += i) {
                if (constraints[j - 1] != -1 && constraints[j - 1] % i != 0) {
                    return 0;
                }
                if (constraints[j - 1] == -1) {
                    count++;
                }
            }
            if (count > 0) {
                result = result * power(i, count - 1, MOD) % MOD;
            }
        }
        
        return result;
    }
    
    /**
     * 题目8: SPOJ MODULOUS
     * 链接: https://www.spoj.com/problems/MODULOUS/
     * 难度: 中等
     * 题意: 计算模运算表达式
     */
    public static long modulous(long a, long b, long m) {
        // 计算 (a^b) mod m
        return power(a, b, m);
    }
    
    /**
     * 题目9: CodeChef FOMBINATORIAL
     * 链接: https://www.codechef.com/problems/FOMBINATORIAL
     * 难度: 中等
     * 题意: 计算组合数取模
     */
    public static long fombinatorial(int n, int m, int mod) {
        // 预处理阶乘和阶乘逆元
        long[] fact = new long[n + 1];
        long[] invFact = new long[n + 1];
        fact[0] = 1;
        for (int i = 1; i <= n; i++) {
            fact[i] = fact[i - 1] * i % mod;
        }
        invFact[n] = power(fact[n], mod - 2, mod);
        for (int i = n - 1; i >= 0; i--) {
            invFact[i] = invFact[i + 1] * (i + 1) % mod;
        }
        
        // 计算组合数C(n, m)
        return fact[n] * invFact[m] % mod * invFact[n - m] % mod;
    }
    
    /**
     * 题目10: USACO 2009 Feb Gold Bulls and Cows
     * 链接: http://www.usaco.org/index.php?page=viewproblem2&cpid=862
     * 难度: 中等
     * 题意: 计算满足特定条件的排列数
     */
    public static long bullsAndCows(int n, int k) {
        final int MOD = 1000000007;
        // 使用组合数学和模逆元计算
        long[] fact = new long[n + 1];
        long[] invFact = new long[n + 1];
        fact[0] = 1;
        for (int i = 1; i <= n; i++) {
            fact[i] = fact[i - 1] * i % MOD;
        }
        invFact[n] = power(fact[n], MOD - 2, MOD);
        for (int i = n - 1; i >= 0; i--) {
            invFact[i] = invFact[i + 1] * (i + 1) % MOD;
        }
        
        long result = 0;
        for (int i = 0; i <= k; i++) {
            long term = fact[n - i] * invFact[i] % MOD * invFact[k - i] % MOD;
            if (i % 2 == 0) {
                result = (result + term) % MOD;
            } else {
                result = (result - term + MOD) % MOD;
            }
        }
        
        return result;
    }
    
    // ==================== 测试函数 ====================
    
    public static void main(String[] args) {
        System.out.println("=== 模逆元完整题目集测试 ===");
        
        // 测试基础算法
        System.out.println("基础算法测试:");
        System.out.println("modInverseExtendedGcd(3, 11) = " + modInverseExtendedGcd(3, 11)); // 4
        System.out.println("modInverseFermat(5, 13) = " + modInverseFermat(5, 13)); // 8
        
        // 测试各大OJ题目
        System.out.println("\n各大OJ题目测试:");
        
        // POJ 1845
        System.out.println("POJ 1845 Sumdiv: sumDiv(2, 3) = " + sumDiv(2, 3)); // 15
        
        // AtCoder ABC182E
        System.out.println("AtCoder ABC182E Throne: throne(10, 4, 3) = " + throne(10, 4, 3));
        
        // CodeChef FOMBINATORIAL
        System.out.println("CodeChef FOMBINATORIAL: fombinatorial(5, 2, MOD) = " + fombinatorial(5, 2, MOD));
        
        // 边界测试
        System.out.println("\n边界测试:");
        System.out.println("modInverseExtendedGcd(0, 5) = " + modInverseExtendedGcd(0, 5)); // -1
        System.out.println("modInverseExtendedGcd(6, 8) = " + modInverseExtendedGcd(6, 8)); // -1
        
        // 性能测试
        System.out.println("\n性能测试:");
        long start = System.currentTimeMillis();
        long[] inv = buildInverseAll(1000000, MOD);
        long end = System.currentTimeMillis();
        System.out.println("线性递推计算1~1000000的逆元耗时: " + (end - start) + "ms");
        
        System.out.println("测试完成!");
    }
    
    // ==================== C++实现思路 ====================
    /**
     * C++实现注意事项：
     * 1. 使用long long类型避免溢出
     * 2. 负数取模处理：(x % mod + mod) % mod
     * 3. 快速幂使用位运算优化
     * 4. 扩展欧几里得算法使用引用传递参数
     */
    
    // ==================== Python实现思路 ====================
    /**
     * Python实现注意事项：
     * 1. 使用内置pow函数进行快速幂：pow(a, b, mod)
     * 2. 使用math.gcd计算最大公约数
     * 3. 负数取模自动处理
     * 4. 支持大整数运算，无需担心溢出
     */
}