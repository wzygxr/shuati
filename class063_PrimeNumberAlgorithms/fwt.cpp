// FWT (Fast Walsh-Hadamard Transform) 算法实现

/*
 * 算法简介:
 * FWT是快速沃尔什-哈达玛变换，用于计算位运算卷积。
 * 它可以高效地计算OR、AND、XOR三种位运算的卷积。
 * 
 * 适用场景:
 * 1. 位运算卷积计算
 * 2. 子集枚举问题
 * 3. 组合计数问题
 * 
 * 核心思想:
 * 1. 利用沃尔什-哈达玛矩阵的性质
 * 2. 通过分治方法实现快速变换
 * 3. 支持三种不同的位运算: OR、AND、XOR
 * 
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n)
 */

class FWT {
private:
    static const long long MOD = 1000000007;
    
public:
    /**
     * XOR FWT变换
     * @param a 输入数组
     * @param n 数组长度
     * @param inv 是否为逆变换
     */
    static void xorFWT(long long* a, int n, bool inv) {
        for (int l = 1; l < n; l <<= 1) {
            for (int i = 0; i < n; i += (l << 1)) {
                for (int j = 0; j < l; j++) {
                    long long x = a[i + j];
                    long long y = a[i + j + l];
                    a[i + j] = (x + y) % MOD;
                    a[i + j + l] = (x - y + MOD) % MOD;
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
     * OR FWT变换
     * @param a 输入数组
     * @param n 数组长度
     * @param inv 是否为逆变换
     */
    static void orFWT(long long* a, int n, bool inv) {
        for (int l = 1; l < n; l <<= 1) {
            for (int i = 0; i < n; i += (l << 1)) {
                for (int j = 0; j < l; j++) {
                    if (!inv) {
                        a[i + j + l] = (a[i + j + l] + a[i + j]) % MOD;
                    } else {
                        a[i + j + l] = (a[i + j + l] - a[i + j] + MOD) % MOD;
                    }
                }
            }
        }
    }
    
    /**
     * AND FWT变换
     * @param a 输入数组
     * @param n 数组长度
     * @param inv 是否为逆变换
     */
    static void andFWT(long long* a, int n, bool inv) {
        for (int l = 1; l < n; l <<= 1) {
            for (int i = 0; i < n; i += (l << 1)) {
                for (int j = 0; j < l; j++) {
                    if (!inv) {
                        a[i + j] = (a[i + j] + a[i + j + l]) % MOD;
                    } else {
                        a[i + j] = (a[i + j] - a[i + j + l] + MOD) % MOD;
                    }
                }
            }
        }
    }
    
    /**
     * XOR卷积
     * @param a 第一个数组
     * @param a_len 第一个数组长度
     * @param b 第二个数组
     * @param b_len 第二个数组长度
     * @param result 结果数组
     * @return 结果数组长度
     */
    static int xorConvolution(long long* a, int a_len, long long* b, int b_len, long long* result) {
        int n = 1;
        while (n < (a_len > b_len ? a_len : b_len)) n <<= 1;
        
        long long fa[1024] = {0};
        long long fb[1024] = {0};
        
        for (int i = 0; i < a_len && i < 1024; i++) fa[i] = a[i];
        for (int i = 0; i < b_len && i < 1024; i++) fb[i] = b[i];
        
        xorFWT(fa, n, false);
        xorFWT(fb, n, false);
        
        for (int i = 0; i < n && i < 1024; i++) {
            fa[i] = fa[i] * fb[i] % MOD;
        }
        
        xorFWT(fa, n, true);
        
        for (int i = 0; i < n && i < 1024; i++) {
            result[i] = fa[i];
        }
        
        return n;
    }
    
    /**
     * OR卷积
     * @param a 第一个数组
     * @param a_len 第一个数组长度
     * @param b 第二个数组
     * @param b_len 第二个数组长度
     * @param result 结果数组
     * @return 结果数组长度
     */
    static int orConvolution(long long* a, int a_len, long long* b, int b_len, long long* result) {
        int n = 1;
        while (n < (a_len > b_len ? a_len : b_len)) n <<= 1;
        
        long long fa[1024] = {0};
        long long fb[1024] = {0};
        
        for (int i = 0; i < a_len && i < 1024; i++) fa[i] = a[i];
        for (int i = 0; i < b_len && i < 1024; i++) fb[i] = b[i];
        
        orFWT(fa, n, false);
        orFWT(fb, n, false);
        
        for (int i = 0; i < n && i < 1024; i++) {
            fa[i] = fa[i] * fb[i] % MOD;
        }
        
        orFWT(fa, n, true);
        
        for (int i = 0; i < n && i < 1024; i++) {
            result[i] = fa[i];
        }
        
        return n;
    }
    
    /**
     * AND卷积
     * @param a 第一个数组
     * @param a_len 第一个数组长度
     * @param b 第二个数组
     * @param b_len 第二个数组长度
     * @param result 结果数组
     * @return 结果数组长度
     */
    static int andConvolution(long long* a, int a_len, long long* b, int b_len, long long* result) {
        int n = 1;
        while (n < (a_len > b_len ? a_len : b_len)) n <<= 1;
        
        long long fa[1024] = {0};
        long long fb[1024] = {0};
        
        for (int i = 0; i < a_len && i < 1024; i++) fa[i] = a[i];
        for (int i = 0; i < b_len && i < 1024; i++) fb[i] = b[i];
        
        andFWT(fa, n, false);
        andFWT(fb, n, false);
        
        for (int i = 0; i < n && i < 1024; i++) {
            fa[i] = fa[i] * fb[i] % MOD;
        }
        
        andFWT(fa, n, true);
        
        for (int i = 0; i < n && i < 1024; i++) {
            result[i] = fa[i];
        }
        
        return n;
    }
    
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
     * 洛谷P4717 【模板】快速莫比乌斯/沃尔什变换 (FMT/FWT)
     * 题目来源: https://www.luogu.com.cn/problem/P4717
     * 题目描述: 给定长度为2^n两个序列A,B，设C_i=Σ(j⊕k=i)A_j×B_k，分别当⊕是or, and, xor时求出C。
     * 解题思路: 直接使用FWT算法分别计算OR、AND、XOR三种卷积
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     * 
     * @param n 整数n
     * @param a 数组A
     * @param a_len 数组A长度
     * @param b 数组B
     * @param b_len 数组B长度
     * @param results 包含OR、AND、XOR卷积结果的二维数组
     * @return 每种卷积结果的长度
     */
    static int solveP4717(int n, long long* a, int a_len, long long* b, int b_len, long long results[3][1024]) {
        int or_len = orConvolution(a, a_len, b, b_len, results[0]);
        int and_len = andConvolution(a, a_len, b, b_len, results[1]);
        int xor_len = xorConvolution(a, a_len, b, b_len, results[2]);
        
        return or_len; // 返回结果数组长度（三种卷积结果长度相同）
    }
};