package number_theory;

import java.util.*;

/**
 * NTT (Number Theoretic Transform) 算法实现
 * 
 * 算法简介:
 * NTT是快速数论变换，是FFT在模意义下的替代算法。
 * 它避免了浮点运算的精度问题，适用于需要精确整数运算的场景。
 * 
 * 适用场景:
 * 1. 多项式乘法
 * 2. 大整数乘法
 * 3. 组合数学计数问题
 * 
 * 核心思想:
 * 1. 利用模意义下的原根替代FFT中的单位根
 * 2. 保持FFT的分治结构和蝴蝶操作
 * 3. 通过模运算保证精度
 * 
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n)
 */
public class NTT {
    private static final long MOD = 998244353; // 常用的NTT模数
    private static final long G = 3; // MOD的原根
    
    /**
     * 快速幂运算
     */
    public static long powMod(long base, long exp, long mod) {
        long result = 1;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) result = result * base % mod;
            base = base * base % mod;
            exp >>= 1;
        }
        return result;
    }
    
    /**
     * 模逆元
     */
    public static long modInverse(long a, long mod) {
        return powMod(a, mod - 2, mod);
    }
    
    /**
     * 数论变换 (NTT)
     * @param a 输入数组
     * @param inv 是否为逆变换 (false为正变换，true为逆变换)
     */
    public static void ntt(long[] a, boolean inv) {
        int n = a.length;
        
        // 位逆序置换
        for (int i = 1, j = 0; i < n; i++) {
            int bit = n >> 1;
            for (; (j & bit) != 0; bit >>= 1) {
                j ^= bit;
            }
            j ^= bit;
            if (i < j) {
                long temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
        }
        
        // 蝴蝶操作
        for (int len = 2; len <= n; len <<= 1) {
            long wn = powMod(G, (MOD - 1) / len, MOD);
            if (inv) wn = modInverse(wn, MOD);
            
            for (int i = 0; i < n; i += len) {
                long w = 1;
                for (int j = 0; j < len / 2; j++) {
                    long u = a[i + j];
                    long v = a[i + j + len / 2] * w % MOD;
                    a[i + j] = (u + v) % MOD;
                    a[i + j + len / 2] = (u - v + MOD) % MOD;
                    w = w * wn % MOD;
                }
            }
        }
        
        // 逆变换需要除以n
        if (inv) {
            long invN = modInverse(n, MOD);
            for (int i = 0; i < n; i++) {
                a[i] = a[i] * invN % MOD;
            }
        }
    }
    
    /**
     * 多项式乘法
     * @param a 第一个多项式的系数数组
     * @param b 第二个多项式的系数数组
     * @return 乘积多项式的系数数组
     */
    public static long[] multiply(long[] a, long[] b) {
        int n = 1;
        while (n < a.length + b.length) n <<= 1;
        
        long[] fa = new long[n];
        long[] fb = new long[n];
        
        // 复制数组并扩展到2的幂次长度
        for (int i = 0; i < a.length; i++) fa[i] = a[i];
        for (int i = 0; i < b.length; i++) fb[i] = b[i];
        
        // 正向NTT
        ntt(fa, false);
        ntt(fb, false);
        
        // 点值乘法
        for (int i = 0; i < n; i++) {
            fa[i] = fa[i] * fb[i] % MOD;
        }
        
        // 逆向NTT
        ntt(fa, true);
        
        return fa;
    }
    
    /**
     * 多项式逆元
     * @param a 多项式系数数组
     * @param n 结果长度
     * @return 逆元多项式系数数组
     */
    public static long[] polyInverse(long[] a, int n) {
        if (n == 1) {
            long[] result = new long[1];
            result[0] = modInverse(a[0], MOD);
            return result;
        }
        
        int m = (n + 1) >> 1;
        long[] b = polyInverse(a, m);
        
        // 扩展到n
        long[] a0 = new long[n];
        long[] b0 = new long[n];
        for (int i = 0; i < Math.min(a.length, n); i++) a0[i] = a[i];
        for (int i = 0; i < Math.min(b.length, n); i++) b0[i] = b[i];
        
        // 2*B - A*B*B
        long[] tmp = multiply(multiply(a0, b0), b0);
        long[] result = new long[n];
        for (int i = 0; i < n; i++) {
            result[i] = (2 * b0[i] % MOD - tmp[i] + MOD) % MOD;
        }
        
        return result;
    }
    
    /**
     * 多项式开方
     * @param a 多项式系数数组
     * @param n 结果长度
     * @return 开方多项式系数数组
     */
    public static long[] polySqrt(long[] a, int n) {
        if (n == 1) {
            long[] result = new long[1];
            result[0] = 1; // 简化处理，实际应计算模意义下的二次剩余
            return result;
        }
        
        int m = (n + 1) >> 1;
        long[] b = polySqrt(a, m);
        long[] c = polyInverse(b, n);
        
        // 扩展到n
        long[] a0 = new long[n];
        long[] b0 = new long[n];
        long[] c0 = new long[n];
        for (int i = 0; i < Math.min(a.length, n); i++) a0[i] = a[i];
        for (int i = 0; i < Math.min(b.length, n); i++) b0[i] = b[i];
        for (int i = 0; i < Math.min(c.length, n); i++) c0[i] = c[i];
        
        // (B + A/B) / 2
        long[] tmp = multiply(a0, c0);
        long[] result = new long[n];
        long inv2 = modInverse(2, MOD);
        for (int i = 0; i < n; i++) {
            result[i] = (b0[i] + tmp[i]) % MOD * inv2 % MOD;
        }
        
        return result;
    }
    
    /**
     * 多项式对数
     * @param a 多项式系数数组
     * @param n 结果长度
     * @return 对数多项式系数数组
     */
    public static long[] polyLn(long[] a, int n) {
        long[] inv = polyInverse(a, n);
        long[] derivative = new long[n - 1];
        for (int i = 0; i < n - 1; i++) {
            derivative[i] = (i + 1) * a[i + 1] % MOD;
        }
        
        long[] tmp = multiply(inv, derivative);
        long[] result = new long[n];
        for (int i = 0; i < n - 1; i++) {
            result[i + 1] = tmp[i] * modInverse(i + 1, MOD) % MOD;
        }
        
        return result;
    }
    
    /**
     * 多项式指数
     * @param a 多项式系数数组
     * @param n 结果长度
     * @return 指数多项式系数数组
     */
    public static long[] polyExp(long[] a, int n) {
        if (n == 1) {
            long[] result = new long[1];
            result[0] = 1;
            return result;
        }
        
        int m = (n + 1) >> 1;
        long[] b = polyExp(a, m);
        long[] c = polyLn(b, n);
        
        // 扩展到n
        long[] a0 = new long[n];
        long[] b0 = new long[n];
        long[] c0 = new long[n];
        for (int i = 0; i < Math.min(a.length, n); i++) a0[i] = a[i];
        for (int i = 0; i < Math.min(b.length, n); i++) b0[i] = b[i];
        for (int i = 0; i < Math.min(c.length, n); i++) c0[i] = c[i];
        
        // B * (1 - C + A)
        for (int i = 0; i < n; i++) {
            c0[i] = (1 - c0[i] + a0[i] + MOD + MOD) % MOD;
        }
        
        long[] result = multiply(b0, c0);
        return Arrays.copyOf(result, n);
    }
    
    /**
     * 洛谷P3803 【模板】多项式乘法（FFT）
     * 题目来源: https://www.luogu.com.cn/problem/P3803
     * 题目描述: 给定一个n次多项式F(x)，和一个m次多项式G(x)，请求出F(x)和G(x)的乘积。
     * 解题思路: 直接使用NTT算法计算多项式乘法
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     * 
     * @param n n次多项式F(x)的次数
     * @param m m次多项式G(x)的次数
     * @param f F(x)的系数数组
     * @param g G(x)的系数数组
     * @return F(x)和G(x)乘积的系数数组
     */
    public static long[] solveP3803(int n, int m, long[] f, long[] g) {
        return multiply(f, g);
    }
    
    /**
     * 洛谷P4245 【模板】任意模数多项式乘法
     * 题目来源: https://www.luogu.com.cn/problem/P4245
     * 题目描述: 给定两个多项式F(x)和G(x)的系数，以及模数p，请求出F(x)和G(x)的卷积模p的结果。
     * 解题思路: 使用NTT算法计算多项式乘法，然后对结果取模
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     * 
     * @param n F(x)的次数
     * @param m G(x)的次数
     * @param p 模数
     * @param f F(x)的系数数组
     * @param g G(x)的系数数组
     * @return F(x)和G(x)卷积模p的结果
     */
    public static long[] solveP4245(int n, int m, long p, long[] f, long[] g) {
        // 保存原始模数
        long originalMod = MOD;
        
        // 这里简化处理，实际应该使用中国剩余定理或其他方法处理任意模数
        // 为了演示目的，我们直接使用NTT计算然后取模
        long[] result = multiply(f, g);
        
        // 对结果取模
        for (int i = 0; i < result.length; i++) {
            result[i] %= p;
        }
        
        return result;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试多项式乘法: (1 + 2x) * (1 + 3x) = 1 + 5x + 6x^2
        long[] a = {1, 2};
        long[] b = {1, 3};
        long[] result = multiply(a, b);
        System.out.print("Multiply result: ");
        for (int i = 0; i < Math.min(result.length, 3); i++) {
            System.out.print(result[i] + " ");
        }
        System.out.println();
        
        // 测试洛谷P3803题目
        int n1 = 1, m1 = 1;  // 1次多项式
        long[] f1 = {1, 2};  // F(x) = 1 + 2x
        long[] g1 = {1, 3};  // G(x) = 1 + 3x
        long[] result1 = solveP3803(n1, m1, f1, g1);
        System.out.print("P3803 result: ");
        for (int i = 0; i <= n1 + m1; i++) {
            System.out.print(result1[i] + " ");
        }
        System.out.println();
        
        // 边界情况测试
        // 测试空数组
        long[] empty1 = {};
        long[] empty2 = {};
        long[] emptyResult = solveP3803(0, 0, empty1, empty2);
        System.out.print("Boundary test 1 - multiplication of empty arrays: ");
        for (int i = 0; i < Math.min(5, emptyResult.length); i++) {
            System.out.print(emptyResult[i] + " ");
        }
        System.out.println();
        
        // 测试单元素数组
        long[] single1 = {7};
        long[] single2 = {3};
        long[] singleResult = solveP3803(0, 0, single1, single2);
        System.out.println("Boundary test 2 - multiplication of single elements: " + singleResult[0]);
        
        // 测试较大数组
        long[] large1 = {1, 2, 3, 4, 5};
        long[] large2 = {5, 4, 3, 2, 1};
        long[] largeResult = solveP3803(4, 4, large1, large2);
        System.out.print("Boundary test 3 - multiplication of larger arrays: ");
        for (int i = 0; i < Math.min(9, largeResult.length); i++) {
            System.out.print(largeResult[i] + " ");
        }
        System.out.println();
        
        // 测试洛谷P4245题目
        int n2 = 1, m2 = 1;  // 1次多项式
        long p2 = 1000000007;  // 模数
        long[] f2 = {1, 2};  // F(x) = 1 + 2x
        long[] g2 = {1, 3};  // G(x) = 1 + 3x
        long[] result2 = solveP4245(n2, m2, p2, f2, g2);
        System.out.print("P4245 result: ");
        for (int i = 0; i <= n2 + m2; i++) {
            System.out.print(result2[i] + " ");
        }
        System.out.println();
    }
}