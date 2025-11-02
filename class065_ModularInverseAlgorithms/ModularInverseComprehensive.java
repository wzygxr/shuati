package class099;

import java.util.Scanner;
import java.util.Arrays;

/**
 * 模逆元综合实现（完整版）
 * 包含多种求解模逆元的方法及相关的算法题目实现
 * 
 * 模逆元的定义：
 * 对于整数a和模数m，如果存在整数x使得 a*x ≡ 1 (mod m)，则称x为a在模m意义下的乘法逆元
 * 
 * 模逆元存在的充要条件：gcd(a, m) = 1，即a和m互质
 * 
 * 应用场景：
 * 1. 数论计算中除法取模
 * 2. 组合数学中计算组合数取模
 * 3. 密码学中RSA算法等
 * 4. 算法竞赛中的各种数学题
 * 5. 机器学习中的大规模矩阵运算优化
 * 6. 图像处理中的安全传输
 * 7. 自然语言处理中的文本加密
 */
public class ModularInverseComprehensive {
    
    private static final long MOD = 1000000007L;
    
    /**
     * 扩展欧几里得算法
     * 求解 ax + by = gcd(a, b)
     * 该算法是求模逆元的基础，可以处理任何模数的情况
     * 
     * @param a 系数a
     * @param b 系数b
     * @param x 用于返回x的解（使用数组引用传递）
     * @param y 用于返回y的解（使用数组引用传递）
     * @return gcd(a, b)
     * 
     * 时间复杂度: O(log(min(a, b)))
     * 空间复杂度: O(log(min(a, b))) - 递归调用栈
     */
    public static long extendedGcd(long a, long b, long[] x, long[] y) {
        // 参数验证
        if (x == null || y == null || x.length == 0 || y.length == 0) {
            throw new IllegalArgumentException("x and y arrays must be non-null and non-empty");
        }
        
        // 基本情况：当b为0时，gcd(a,0)=a，此时x=1,y=0
        if (b == 0) {
            x[0] = 1;
            y[0] = 0;
            return a;
        }
        
        // 递归求解子问题
        long[] x1 = new long[1];
        long[] y1 = new long[1];
        long gcd = extendedGcd(b, a % b, x1, y1);
        
        // 通过子问题的解推导原问题的解
        // 数学推导：gcd(a,b) = gcd(b, a%b)
        // 如果 b*x1 + (a%b)*y1 = gcd
        // 则 a*y1 + b*(x1 - (a/b)*y1) = gcd
        x[0] = y1[0];
        y[0] = x1[0] - (a / b) * y1[0];
        
        return gcd;
    }
    
    /**
     * 使用扩展欧几里得算法求模逆元
     * 适用于模数不一定是质数的情况，是最通用的模逆元求解方法
     * 
     * 时间复杂度: O(log(min(a, mod)))
     * 空间复杂度: O(log(min(a, mod))) - 递归调用栈
     * 
     * @param a 要求逆元的数
     * @param mod 模数
     * @return 如果存在逆元，返回最小正整数解；否则返回-1
     * 
     * 异常情况处理：
     * - 模数为0时抛出异常
     * - 负数输入会进行预处理
     */
    public static long modInverseExtendedGcd(long a, long mod) {
        // 参数验证
        if (mod == 0) {
            throw new IllegalArgumentException("Modulus cannot be zero");
        }
        
        // 处理负数情况，确保a和mod都是正数
        a = (a % mod + mod) % mod;
        mod = Math.abs(mod);
        
        long[] x = new long[1];
        long[] y = new long[1];
        long gcd = extendedGcd(a, mod, x, y);
        
        // 逆元存在的充要条件是a和mod互质
        if (gcd != 1) {
            return -1; // 逆元不存在
        }
        
        // 确保结果为正数，因为扩展欧几里得算法可能返回负数解
        return (x[0] % mod + mod) % mod;
    }
    
    /**
     * 快速幂运算
     * 计算base^exp mod mod
     * 用于高效计算大指数幂的模运算，是费马小定理求逆元的基础
     * 
     * 时间复杂度: O(log exp)
     * 空间复杂度: O(1)
     * 
     * @param base 底数
     * @param exp 指数
     * @param mod 模数
     * @return base^exp mod mod
     * 
     * 优化点：
     * - 使用位运算代替除法和取模
     * - 预先对底数取模防止溢出
     */
    public static long power(long base, long exp, long mod) {
        // 参数验证
        if (mod == 0) {
            throw new IllegalArgumentException("Modulus cannot be zero");
        }
        if (exp < 0) {
            throw new IllegalArgumentException("Exponent cannot be negative");
        }
        
        long result = 1;
        base %= mod; // 预先对底数取模，防止中间结果过大
        
        // 快速幂的核心逻辑：将指数分解为二进制位
        while (exp > 0) {
            // 如果当前二进制位为1，则将结果乘以当前的base
            if ((exp & 1) == 1) {
                result = (result * base) % mod;
            }
            // 平方base，对应二进制位左移一位
            base = (base * base) % mod;
            // 右移一位，相当于除以2取整
            exp >>= 1;
        }
        return result;
    }
    
    /**
     * 使用费马小定理求模逆元（当模数为质数时）
     * 根据费马小定理: a^(p-1) ≡ 1 (mod p)，其中p是质数
     * 所以 a^(-1) ≡ a^(p-2) (mod p)
     * 
     * 时间复杂度: O(log(p))
     * 空间复杂度: O(1)
     * 
     * @param a 要求逆元的数
     * @param p 质数模数
     * @return 如果p是质数且a与p互质，返回a在模p意义下的逆元；否则返回-1
     * 
     * 注意事项：
     * - 仅适用于模数为质数的情况
     * - 比扩展欧几里得算法更高效（在质数模数下）
     */
    public static long modInverseFermat(long a, long p) {
        // 参数验证
        if (p <= 1) {
            throw new IllegalArgumentException("Prime modulus must be greater than 1");
        }
        
        // 处理负数情况
        a = (a % p + p) % p;
        
        // 使用费马小定理求逆元的前提是p是质数且a与p互质
        // 这里简化处理，直接计算a^(p-2) mod p
        // 注意：在实际应用中，应该先验证p是否为质数
        try {
            return power(a, p - 2, p);
        } catch (Exception e) {
            return -1; // 计算失败，逆元不存在
        }
    }
    
    /**
     * 使用线性递推方法计算1~n所有整数在模p意义下的乘法逆元
     * 这是批量计算逆元的最优方法，比逐个计算效率高得多
     * 
     * 递推公式推导：
     * 设 p = k*i + r，其中 k = p / i（整除），r = p % i
     * 则有 k*i + r ≡ 0 (mod p)
     * 两边同时乘以 i^(-1) * r^(-1) 得：
     * k*r^(-1) + i^(-1) ≡ 0 (mod p)
     * 即 i^(-1) ≡ -k*r^(-1) (mod p)
     * 由于 r < i，所以 r 的逆元在计算 i 的逆元时已经计算过了
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     * 
     * @param n 要计算逆元的范围上限
     * @param p 模数（通常为质数）
     * @param inv 用于存储逆元的数组（需要预先分配大小为n+1）
     * 
     * 工程应用：
     * - 在组合数学问题中预处理阶乘逆元
     * - 大规模数据处理中的批量模运算
     */
    public static void buildInverseAll(int n, int p, long[] inv) {
        inv[1] = 1;
        for (int i = 2; i <= n; i++) {
            inv[i] = (p - (p / i) * inv[p % i] % p) % p;
        }
    }
    
    /**
     * 计算组合数C(n, k) mod p
     * 使用预处理阶乘和逆元的方法，这是组合数取模的标准方法
     * 
     * 公式：C(n,k) = n!/(k!*(n-k)!) ≡ n! * inv(k!) * inv((n-k)!) mod p
     * 
     * 时间复杂度: O(1) 查询时间
     * 空间复杂度: O(n) 预处理空间
     * 
     * @param n 组合数上标
     * @param k 组合数下标
     * @param fact 阶乘数组
     * @param invFact 阶乘逆元数组
     * @param p 模数
     * @return C(n, k) mod p
     * 
     * 边界情况处理：
     * - k > n 或 k < 0 时返回0
     * - k = 0 或 k = n 时返回1
     */
    public static long combination(int n, int k, long[] fact, long[] invFact, long p) {
        if (k > n || k < 0) return 0;
        return (fact[n] * invFact[k] % p) * invFact[n - k] % p;
    }
    
    /**
     * 预处理阶乘和阶乘逆元
     * 这是组合数学问题中的基础预处理步骤
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     * 
     * @param n 阶乘上限
     * @param p 模数
     * @param fact 阶乘数组（需要预先分配大小为n+1）
     * @param invFact 阶乘逆元数组（需要预先分配大小为n+1）
     * 
     * 优化点：
     * - 从后向前计算阶乘逆元，利用递推关系 invFact[i] = invFact[i+1] * (i+1) mod p
     */
    public static void preprocessFactorial(int n, long p, long[] fact, long[] invFact) {
        fact[0] = 1;
        for (int i = 1; i <= n; i++) {
            fact[i] = fact[i - 1] * i % p;
        }
        
        invFact[n] = power(fact[n], p - 2, p);
        for (int i = n - 1; i >= 0; i--) {
            invFact[i] = invFact[i + 1] * (i + 1) % p;
        }
    }
    
    /**
     * ZOJ 3609 Modular Inverse 题目实现
     * 题目链接: http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=3609
     * 
     * 题目描述:
     * 给定两个整数a和m，求a在模m意义下的乘法逆元x，使得 a*x ≡ 1 (mod m)
     * 如果不存在这样的x，输出"Not Exist"
     * 
     * 这是模逆元的基础题目，直接应用扩展欧几里得算法
     */
    public static void solveZOJ3609() {
        Scanner scanner = new Scanner(System.in);
        int t = scanner.nextInt();
        
        for (int i = 0; i < t; i++) {
            long a = scanner.nextLong();
            long m = scanner.nextLong();
            
            long result = modInverseExtendedGcd(a, m);
            if (result == -1) {
                System.out.println("Not Exist");
            } else {
                System.out.println(result);
            }
        }
        scanner.close();
    }
    
    /**
     * 洛谷 P3811 【模板】乘法逆元 题目实现
     * 题目链接: https://www.luogu.com.cn/problem/P3811
     * 
     * 题目描述:
     * 给定 n, p 求 1∼n 中所有整数在模 p 意义下的乘法逆元。
     * 
     * 这是批量求逆元的典型题目，使用线性递推方法效率最高
     */
    public static void solveLuoguP3811() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int p = scanner.nextInt();
        
        long[] inv = new long[n + 1];
        buildInverseAll(n, p, inv);
        
        for (int i = 1; i <= n; i++) {
            System.out.println(inv[i]);
        }
        scanner.close();
    }
    
    /**
     * LeetCode 1808. Maximize Number of Nice Divisors 题目实现
     * 题目链接: https://leetcode.cn/problems/maximize-number-of-nice-divisors/
     * 
     * 题目描述:
     * 给你一个正整数 primeFactors 。你需要构造一个正整数 n ，它满足以下条件：
     * 1. n 质因数（质因数需要考虑重复的情况）的数目 不超过 primeFactors 个。
     * 2. n 好因子的数目最大化。
     * 
     * 解题思路:
     * 这是一个数学优化问题，本质上是整数拆分问题。
     * 要使好因子数目最大，我们需要合理分配primeFactors个质因数。
     * 好因子的数目等于各个质因数指数的乘积。
     */
    public static int maxNiceDivisors(int primeFactors) {
        // 特殊情况处理
        if (primeFactors <= 3) {
            return primeFactors;
        }
        
        int remainder = primeFactors % 3;
        int quotient = primeFactors / 3;
        
        if (remainder == 0) {
            // 全部用3
            return (int) (power(3, quotient, MOD));
        } else if (remainder == 1) {
            // 用一个4代替两个3
            return (int) ((power(3, quotient - 1, MOD) * 4) % MOD);
        } else {  // remainder == 2
            // 用一个2
            return (int) ((power(3, quotient, MOD) * 2) % MOD);
        }
    }
    
    /**
     * POJ 1845 Sumdiv 题目实现
     * 题目链接: http://poj.org/problem?id=1845
     * 
     * 题目描述:
     * 计算A^B的所有约数之和模9901
     * 
     * 解题思路:
     * 1. 质因数分解：A = p1^a1 * p2^a2 * ... * pn^an
     * 2. A^B的质因数分解：A^B = p1^(a1*B) * p2^(a2*B) * ... * pn^(an*B)
     * 3. 约数和公式：sum = (1 + p1 + p1^2 + ... + p1^(a1*B)) * ... * (1 + pn + pn^2 + ... + pn^(an*B))
     * 4. 等比数列求和：使用快速幂和模逆元计算等比数列和
     */
    public static int sumDiv(int A, int B) {
        final int MOD = 9901;
        
        // 质因数分解
        int[] factors = new int[A + 1];
        for (int i = 2; i <= A; i++) {
            while (A % i == 0) {
                factors[i]++;
                A /= i;
            }
        }
        
        int result = 1;
        for (int i = 2; i < factors.length; i++) {
            if (factors[i] > 0) {
                int exponent = factors[i] * B;
                // 计算等比数列和: (p^(e+1) - 1) / (p - 1) mod MOD
                if (i % MOD == 1) {
                    // 特殊情况处理：当p ≡ 1 mod MOD时，等比数列和为 e+1
                    result = (result * (exponent + 1) % MOD) % MOD;
                } else {
                    long numerator = (power(i, exponent + 1, MOD) - 1 + MOD) % MOD;
                    long denominator = modInverseExtendedGcd(i - 1, MOD);
                    result = (int) ((result * numerator % MOD) * denominator % MOD);
                }
            }
        }
        
        return result;
    }
    
    /**
     * LeetCode 1623. Number of Sets of K Non-Overlapping Line Segments 题目实现
     * 题目链接: https://leetcode.cn/problems/number-of-sets-of-k-non-overlapping-line-segments/
     * 
     * 题目描述:
     * 在n个点上选择k个不重叠的线段的方案数
     * 
     * 解题思路:
     * 使用组合数学公式：C(n + k - 1, 2k)
     */
    public static int numberOfSets(int n, int k) {
        final int MOD = 1000000007;
        int max = n + k - 1;
        
        // 预处理阶乘和阶乘逆元
        long[] fact = new long[max + 1];
        long[] invFact = new long[max + 1];
        preprocessFactorial(max, MOD, fact, invFact);
        
        // 计算C(n+k-1, 2k)
        return (int) combination(n + k - 1, 2 * k, fact, invFact, MOD);
    }
    
    /**
     * Codeforces 1445D. Divide and Sum 题目实现
     * 题目链接: https://codeforces.com/problemset/problem/1445/D
     * 
     * 题目描述:
     * 计算所有划分方案的f(p)值之和
     * 
     * 解题思路:
     * 排序后，每对元素的贡献是固定的，可以用组合数学快速计算
     */
    public static long divideAndSum(int[] arr) {
        final int MOD = 998244353;
        int n = arr.length / 2;
        Arrays.sort(arr);
        
        // 预处理阶乘和阶乘逆元
        long[] fact = new long[2 * n + 1];
        long[] invFact = new long[2 * n + 1];
        preprocessFactorial(2 * n, MOD, fact, invFact);
        
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum = (sum + arr[n + i] - arr[i]) % MOD;
        }
        sum = (sum % MOD + MOD) % MOD;
        
        // 计算组合数C(2n-1, n-1)
        long comb = combination(2 * n - 1, n - 1, fact, invFact, MOD);
        
        return (sum * comb) % MOD;
    }
    
    /**
     * 主函数
     * 用于测试各种模逆元求解方法和相关题目
     */
    public static void main(String[] args) {
        // 测试各种模逆元求解方法
        System.out.println("=== 模逆元求解方法测试 ===");
        
        // 测试扩展欧几里得算法求模逆元
        long a1 = 3, m1 = 11;
        long result1 = modInverseExtendedGcd(a1, m1);
        System.out.println("扩展欧几里得算法: " + a1 + " 在模 " + m1 + " 意义下的逆元是 " + result1);
        
        // 测试费马小定理求模逆元
        long a2 = 5, p2 = 13;  // 13是质数
        long result2 = modInverseFermat(a2, p2);
        System.out.println("费马小定理: " + a2 + " 在模 " + p2 + " 意义下的逆元是 " + result2);
        
        // 测试线性递推求所有逆元
        int n = 10, p = 11;
        long[] inv = new long[n + 1];
        buildInverseAll(n, p, inv);
        System.out.println("线性递推求1~" + n + "在模" + p + "意义下的逆元:");
        for (int i = 1; i <= n; i++) {
            System.out.println("inv[" + i + "] = " + inv[i]);
        }
        
        // 测试LeetCode 1808题目
        System.out.println("\n=== LeetCode 1808测试 ===");
        int[] testCases = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        for (int primeFactors : testCases) {
            int result = maxNiceDivisors(primeFactors);
            System.out.println("primeFactors = " + primeFactors + ", max nice divisors = " + result);
        }
        
        // 测试组合数计算
        System.out.println("\n=== 组合数计算测试 ===");
        int n2 = 10, k = 3;
        long[] fact = new long[n2 + 1];
        long[] invFact = new long[n2 + 1];
        preprocessFactorial(n2, MOD, fact, invFact);
        long comb = combination(n2, k, fact, invFact, MOD);
        System.out.println("C(" + n2 + ", " + k + ") mod " + MOD + " = " + comb);
        
        // 测试POJ 1845 Sumdiv
        System.out.println("\n=== POJ 1845 Sumdiv测试 ===");
        System.out.println("sumDiv(2, 3) = " + sumDiv(2, 3));  // 应该输出 15
        System.out.println("sumDiv(4, 2) = " + sumDiv(4, 2));  // 应该输出 21
        
        // 测试LeetCode 1623
        System.out.println("\n=== LeetCode 1623测试 ===");
        System.out.println("numberOfSets(4, 2) = " + numberOfSets(4, 2));  // 应该输出 5
        System.out.println("numberOfSets(3, 1) = " + numberOfSets(3, 1));  // 应该输出 3
        
        // 测试代码健壮性
        System.out.println("\n=== 边界情况测试 ===");
        System.out.println("modInverseExtendedGcd(0, 5) = " + modInverseExtendedGcd(0, 5));  // 应该输出 -1
        System.out.println("modInverseExtendedGcd(6, 8) = " + modInverseExtendedGcd(6, 8));  // 应该输出 -1
    }
}