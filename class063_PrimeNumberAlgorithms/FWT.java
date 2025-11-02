package number_theory;

/**
 * FWT (Fast Walsh-Hadamard Transform) 算法实现
 * 
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
public class FWT {
    private static final long MOD = 1000000007;
    
    /**
     * XOR FWT变换
     * @param a 输入数组
     * @param inv 是否为逆变换
     */
    public static void xorFWT(long[] a, boolean inv) {
        int n = a.length;
        for (int l = 1; l < n; l <<= 1) {
            for (int i = 0; i < n; i += (l << 1)) {
                for (int j = 0; j < l; j++) {
                    long x = a[i + j];
                    long y = a[i + j + l];
                    a[i + j] = (x + y) % MOD;
                    a[i + j + l] = (x - y + MOD) % MOD;
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
     * OR FWT变换
     * @param a 输入数组
     * @param inv 是否为逆变换
     */
    public static void orFWT(long[] a, boolean inv) {
        int n = a.length;
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
     * @param inv 是否为逆变换
     */
    public static void andFWT(long[] a, boolean inv) {
        int n = a.length;
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
     * @param b 第二个数组
     * @return XOR卷积结果
     */
    public static long[] xorConvolution(long[] a, long[] b) {
        int n = 1;
        while (n < Math.max(a.length, b.length)) n <<= 1;
        
        long[] fa = new long[n];
        long[] fb = new long[n];
        
        for (int i = 0; i < a.length; i++) fa[i] = a[i];
        for (int i = 0; i < b.length; i++) fb[i] = b[i];
        
        xorFWT(fa, false);
        xorFWT(fb, false);
        
        for (int i = 0; i < n; i++) {
            fa[i] = fa[i] * fb[i] % MOD;
        }
        
        xorFWT(fa, true);
        return fa;
    }
    
    /**
     * OR卷积
     * @param a 第一个数组
     * @param b 第二个数组
     * @return OR卷积结果
     */
    public static long[] orConvolution(long[] a, long[] b) {
        int n = 1;
        while (n < Math.max(a.length, b.length)) n <<= 1;
        
        long[] fa = new long[n];
        long[] fb = new long[n];
        
        for (int i = 0; i < a.length; i++) fa[i] = a[i];
        for (int i = 0; i < b.length; i++) fb[i] = b[i];
        
        orFWT(fa, false);
        orFWT(fb, false);
        
        for (int i = 0; i < n; i++) {
            fa[i] = fa[i] * fb[i] % MOD;
        }
        
        orFWT(fa, true);
        return fa;
    }
    
    /**
     * AND卷积
     * @param a 第一个数组
     * @param b 第二个数组
     * @return AND卷积结果
     */
    public static long[] andConvolution(long[] a, long[] b) {
        int n = 1;
        while (n < Math.max(a.length, b.length)) n <<= 1;
        
        long[] fa = new long[n];
        long[] fb = new long[n];
        
        for (int i = 0; i < a.length; i++) fa[i] = a[i];
        for (int i = 0; i < b.length; i++) fb[i] = b[i];
        
        andFWT(fa, false);
        andFWT(fb, false);
        
        for (int i = 0; i < n; i++) {
            fa[i] = fa[i] * fb[i] % MOD;
        }
        
        andFWT(fa, true);
        return fa;
    }
    
    /**
     * 子集卷积 (Subset Convolution)
     * @param a 第一个数组
     * @param b 第二个数组
     * @return 子集卷积结果
     */
    public static long[] subsetConvolution(long[] a, long[] b) {
        int n = a.length;
        int logN = 0;
        while ((1 << logN) < n) logN++;
        
        // 按照位数分组
        long[][] fa = new long[logN + 1][n];
        long[][] fb = new long[logN + 1][n];
        
        for (int i = 0; i < n; i++) {
            int bits = Integer.bitCount(i);
            fa[bits][i] = a[i];
            fb[bits][i] = b[i];
        }
        
        // 对每一层进行OR FWT
        for (int i = 0; i <= logN; i++) {
            orFWT(fa[i], false);
            orFWT(fb[i], false);
        }
        
        // 卷积计算
        long[][] result = new long[logN + 1][n];
        for (int i = 0; i <= logN; i++) {
            for (int j = 0; j <= i; j++) {
                for (int k = 0; k < n; k++) {
                    result[i][k] = (result[i][k] + fa[j][k] * fb[i - j][k]) % MOD;
                }
            }
        }
        
        // 逆变换
        for (int i = 0; i <= logN; i++) {
            orFWT(result[i], true);
        }
        
        // 提取结果
        long[] res = new long[n];
        for (int i = 0; i < n; i++) {
            int bits = Integer.bitCount(i);
            res[i] = result[bits][i];
        }
        
        return res;
    }
    
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
     * 洛谷P4717 【模板】快速莫比乌斯/沃尔什变换 (FMT/FWT)
     * 题目来源: https://www.luogu.com.cn/problem/P4717
     * 题目描述: 给定长度为2^n两个序列A,B，设C_i=Σ(j⊕k=i)A_j×B_k，分别当⊕是or, and, xor时求出C。
     * 解题思路: 直接使用FWT算法分别计算OR、AND、XOR三种卷积
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     * 
     * @param n 整数n
     * @param a 数组A
     * @param b 数组B
     * @return 包含OR、AND、XOR卷积结果的二维数组
     */
    public static long[][] solveP4717(int n, long[] a, long[] b) {
        long[] orResult = orConvolution(a, b);
        long[] andResult = andConvolution(a, b);
        long[] xorResult = xorConvolution(a, b);
        
        return new long[][]{orResult, andResult, xorResult};
    }
    
    /**
     * 洛谷P6097 【模板】子集卷积
     * 题目来源: https://www.luogu.com.cn/problem/P6097
     * 题目描述: 给定两个长度为2^n的序列a和b，求出序列c，其中c_k=Σ(i&j=0,i|j=k)a_i*b_j
     * 解题思路: 使用子集卷积算法，通过按位数分组和OR卷积来实现
     * 时间复杂度: O(n^2 * 2^n)
     * 空间复杂度: O(n * 2^n)
     * 
     * @param n 集合大小
     * @param a 序列a
     * @param b 序列b
     * @return 序列c
     */
    public static long[] solveP6097(int n, long[] a, long[] b) {
        return subsetConvolution(a, b);
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试XOR卷积
        long[] a = {1, 2, 3, 4};
        long[] b = {1, 1, 1, 1};
        long[] result = xorConvolution(a, b);
        System.out.print("XOR convolution result: ");
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i] + " ");
        }
        System.out.println();
        
        // 测试洛谷P4717题目
        int n = 2;  // 2^2 = 4个元素
        long[] a1 = {1, 2, 3, 4};
        long[] b1 = {1, 2, 3, 4};
        long[][] results = solveP4717(n, a1, b1);
        
        System.out.print("OR convolution result: ");
        for (int i = 0; i < results[0].length; i++) {
            System.out.print(results[0][i] + " ");
        }
        System.out.println();
        
        System.out.print("AND convolution result: ");
        for (int i = 0; i < results[1].length; i++) {
            System.out.print(results[1][i] + " ");
        }
        System.out.println();
        
        System.out.print("XOR convolution result: ");
        for (int i = 0; i < results[2].length; i++) {
            System.out.print(results[2][i] + " ");
        }
        System.out.println();
        
        // 边界情况测试
        // 测试空数组
        long[] empty1 = {};
        long[] empty2 = {};
        long[][] emptyResults = solveP4717(0, empty1, empty2);
        System.out.print("Boundary test 1 - XOR convolution of empty arrays: ");
        for (int i = 0; i < emptyResults[2].length; i++) {
            System.out.print(emptyResults[2][i] + " ");
        }
        System.out.println();
        
        // 测试单元素数组
        long[] single1 = {5};
        long[] single2 = {3};
        long[][] singleResults = solveP4717(0, single1, single2);
        System.out.println("Boundary test 2 - XOR convolution of single elements: " + singleResults[2][0]);
        
        // 测试较大数组
        long[] large1 = {1, 2, 3, 4, 5, 6, 7, 8};
        long[] large2 = {8, 7, 6, 5, 4, 3, 2, 1};
        long[][] largeResults = solveP4717(3, large1, large2);
        System.out.print("Boundary test 3 - XOR convolution of larger arrays: ");
        for (int i = 0; i < Math.min(8, largeResults[2].length); i++) {
            System.out.print(largeResults[2][i] + " ");
        }
        System.out.println();
        
        // 测试洛谷P6097题目
        int n2 = 2;  // 2^2 = 4个元素
        long[] a2 = {1, 2, 3, 4};
        long[] b2 = {1, 2, 3, 4};
        long[] result2 = solveP6097(n2, a2, b2);
        System.out.print("P6097 subset convolution result: ");
        for (int i = 0; i < result2.length; i++) {
            System.out.print(result2[i] + " ");
        }
        System.out.println();
    }
}