// NTT (Number Theoretic Transform) 算法实现

/*
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

class NTT {
private:
    static const long long MOD = 998244353; // 常用的NTT模数
    static const long long G = 3; // MOD的原根
    
public:
    /**
     * 快速幂运算
     */
    static long long powMod(long long base, long long exp, long long mod) {
        long long result = 1;
        base %= mod;
        while (exp > 0) {
            if (exp & 1) result = result * base % mod;
            base = base * base % mod;
            exp >>= 1;
        }
        return result;
    }
    
    /**
     * 模逆元
     */
    static long long modInverse(long long a, long long mod) {
        return powMod(a, mod - 2, mod);
    }
    
    /**
     * 数论变换 (NTT)
     * @param a 输入数组
     * @param n 数组长度
     * @param inv 是否为逆变换 (false为正变换，true为逆变换)
     */
    static void ntt(long long* a, int n, bool inv) {
        // 位逆序置换
        for (int i = 1, j = 0; i < n; i++) {
            int bit = n >> 1;
            for (; (j & bit) != 0; bit >>= 1) {
                j ^= bit;
            }
            j ^= bit;
            if (i < j) {
                long long temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
        }
        
        // 蝴蝶操作
        for (int len = 2; len <= n; len <<= 1) {
            long long wn = powMod(G, (MOD - 1) / len, MOD);
            if (inv) wn = modInverse(wn, MOD);
            
            for (int i = 0; i < n; i += len) {
                long long w = 1;
                for (int j = 0; j < len / 2; j++) {
                    long long u = a[i + j];
                    long long v = a[i + j + len / 2] * w % MOD;
                    a[i + j] = (u + v) % MOD;
                    a[i + j + len / 2] = (u - v + MOD) % MOD;
                    w = w * wn % MOD;
                }
            }
        }
        
        // 逆变换需要除以n
        if (inv) {
            long long invN = modInverse(n, MOD);
            for (int i = 0; i < n; i++) {
                a[i] = a[i] * invN % MOD;
            }
        }
    }
    
    /**
     * 多项式乘法
     * @param a 第一个多项式的系数数组
     * @param a_len 第一个多项式长度
     * @param b 第二个多项式的系数数组
     * @param b_len 第二个多项式长度
     * @param result 结果数组
     * @return 结果数组长度
     */
    static int multiply(long long* a, int a_len, long long* b, int b_len, long long* result) {
        int n = 1;
        while (n < a_len + b_len) n <<= 1;
        
        // 使用固定大小数组替代动态分配
        long long fa[1024] = {0};
        long long fb[1024] = {0};
        
        // 复制数组并扩展到2的幂次长度
        for (int i = 0; i < a_len && i < 1024; i++) fa[i] = a[i];
        for (int i = 0; i < b_len && i < 1024; i++) fb[i] = b[i];
        
        // 正向NTT
        ntt(fa, n, false);
        ntt(fb, n, false);
        
        // 点值乘法
        for (int i = 0; i < n && i < 1024; i++) {
            fa[i] = fa[i] * fb[i] % MOD;
        }
        
        // 逆向NTT
        ntt(fa, n, true);
        
        // 复制结果
        for (int i = 0; i < n && i < 1024; i++) {
            result[i] = fa[i];
        }
        
        return n;
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
     * @param f_len F(x)系数数组长度
     * @param g G(x)的系数数组
     * @param g_len G(x)系数数组长度
     * @param result F(x)和G(x)乘积的系数数组
     * @return 结果数组长度
     */
    static int solveP3803(int n, int m, long long* f, int f_len, long long* g, int g_len, long long* result) {
        return multiply(f, f_len, g, g_len, result);
    }
};