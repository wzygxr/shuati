package class098;

/**
 * HDU 5950 Recursive sequence
 * 
 * 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=5950
 * 
 * 题目大意: 
 * 给定递推式 f[n] = 2*f[n-2] + f[n-1] + n^4，以及f[1]和f[2]的值，求f[n]
 * 
 * 解法分析:
 * 该问题是一个非齐次线性递推关系，包含多项式项n^4，需要扩展状态矩阵来处理
 * 
 * 数学原理:
 * 1. 递推关系: f[n] = f[n-1] + 2*f[n-2] + n^4
 * 2. 需要同时处理f[n]项和n^4项的递推
 * 3. 利用二项式定理展开(n+1)^4来处理多项式项
 * 
 * 时间复杂度: O(logn)
 * 空间复杂度: O(1)
 * 
 * 优化思路:
 * 1. 扩展状态矩阵同时处理递推项和多项式项
 * 2. 利用二项式定理构造转移矩阵
 * 
 * 工程化考虑:
 * 1. 模运算: 防止整数溢出
 * 2. 边界条件处理: n=1, n=2的特殊情况
 * 3. 快速幂优化: 计算3的各次幂
 */

import java.util.Scanner;

public class Code11_RecursiveSequence {
    
    static final long MOD = 1000000007L;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int testCases = scanner.nextInt();
        
        for (int i = 0; i < testCases; i++) {
            int n = scanner.nextInt();
            long a = scanner.nextLong();
            long b = scanner.nextLong();
            
            // 边界条件处理
            if (n == 1) {
                System.out.println(a);
            } else if (n == 2) {
                System.out.println(b);
            } else {
                System.out.println(solve(n, a, b));
            }
        }
    }
    
    /**
     * 使用矩阵快速幂解决递推序列问题
     * 
     * 算法思路:
     * 1. 扩展状态矩阵同时处理递推项和多项式项
     * 2. 利用二项式定理展开(n+1)^4来处理多项式项
     * 3. 构造转移矩阵表示状态转移关系
     * 4. 使用矩阵快速幂计算转移矩阵的(n-2)次幂
     * 5. 初始状态向量乘以结果矩阵得到最终状态
     * 
     * 状态矩阵设计:
     * [f(n), f(n-1), (n+1)^4, (n+1)^3, (n+1)^2, (n+1)^1, 1]
     * 
     * 转移矩阵设计:
     * 利用二项式定理展开:
     * (n+1)^4 = n^4 + 4*n^3 + 6*n^2 + 4*n + 1
     * (n+1)^3 = n^3 + 3*n^2 + 3*n + 1
     * (n+1)^2 = n^2 + 2*n + 1
     * (n+1)^1 = n + 1
     * 
     * 时间复杂度: O(logn)
     * 空间复杂度: O(1)
     * 
     * @param n 项数
     * @param a f(1)的值
     * @param b f(2)的值
     * @return f(n)的值
     */
    // 使用矩阵快速幂解决问题
    public static long solve(int n, long a, long b) {
        // 初始状态: [f(2), f(1), 81, 27, 9, 3, 1]
        // 其中81=3^4, 27=3^3, 9=3^2, 3=3^1, 1=1
        long[] start = {b, a, pow(3, 4), pow(3, 3), pow(3, 2), 3, 1};
        
        // 转移矩阵
        int[][] base = {
            {1, 2, 1, 4, 6, 4, 1},  // f(n) = f(n-1) + 2*f(n-2) + (n+1)^4
            {1, 0, 0, 0, 0, 0, 0},  // f(n-1)
            {0, 0, 1, 4, 6, 4, 1},  // (n+1)^4 展开
            {0, 0, 0, 1, 3, 3, 1},  // (n+1)^3 展开
            {0, 0, 0, 0, 1, 2, 1},  // (n+1)^2 展开
            {0, 0, 0, 0, 0, 1, 1},  // (n+1)^1 展开
            {0, 0, 0, 0, 0, 0, 1}   // 1
        };
        
        long[] result = multiply(start, power(base, n - 2));
        return result[0];
    }
    
    /**
     * 快速幂算法
     * 
     * 算法原理:
     * 利用二进制分解指数，通过不断平方和累积结果实现快速计算
     * 例如: base^13，13的二进制为1101
     * base^13 = base^8 * base^4 * base^1 (对应二进制位为1的位置)
     * 
     * 时间复杂度: O(log exp)
     * 空间复杂度: O(1)
     * 
     * 实现技巧:
     * - 使用位运算优化指数分解 (exp >>= 1)
     * - 使用位运算检查二进制位是否为1 ((exp & 1) == 1)
     * - 每步都进行模运算防止溢出
     * 
     * @param base 底数
     * @param exp 指数
     * @return base的exp次幂
     */
    // 快速幂
    public static long pow(long base, int exp) {
        long result = 1;
        while (exp > 0) {
            if ((exp & 1) == 1) {
                result = (result * base) % MOD;
            }
            base = (base * base) % MOD;
            exp >>= 1;
        }
        return result;
    }
    
    /**
     * 向量与矩阵相乘
     * 
     * 算法原理:
     * 对于向量A(1×n)和矩阵B(n×m)，结果向量C(1×m)中:
     * C[j] = Σ(A[i] * B[i][j]) for i in 0..n-1
     * 
     * 时间复杂度: O(n×m)
     * 空间复杂度: O(m)
     * 
     * @param a 向量 (1×n)
     * @param b 矩阵 (n×m)
     * @return 向量与矩阵的乘积 (1×m)
     * 
     * 算法特点:
     * - 使用long类型防止中间计算溢出
     * - 每步都进行模运算防止溢出
     */
    // 向量与矩阵相乘
    public static long[] multiply(long[] a, int[][] b) {
        int n = a.length;      // 向量长度
        int m = b[0].length;   // 结果向量长度
        long[] ans = new long[m];
        
        // 计算向量与矩阵乘法
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                // 使用long类型防止中间计算溢出，每步都进行模运算
                ans[j] = (ans[j] + (long) a[i] * b[i][j]) % MOD;
            }
        }
        return ans;
    }
    
    /**
     * 矩阵相乘
     * 
     * 算法原理:
     * 对于矩阵A(n×k)和矩阵B(k×m)，结果矩阵C(n×m)中:
     * C[i][j] = Σ(A[i][k] * B[k][j]) for k in 0..k-1
     * 
     * 时间复杂度: O(n×m×k)
     * 空间复杂度: O(n×m)
     * 
     * @param a 第一个矩阵 (n×k)
     * @param b 第二个矩阵 (k×m)
     * @return 两个矩阵的乘积 (n×m)
     * 
     * 算法特点:
     * - 使用long类型临时变量防止整数溢出
     * - 每步都进行模运算防止溢出
     */
    // 矩阵相乘
    public static int[][] multiply(int[][] a, int[][] b) {
        int n = a.length;      // 结果矩阵行数
        int m = b[0].length;   // 结果矩阵列数
        int k = a[0].length;   // 中间维度（a的列数，b的行数）
        int[][] ans = new int[n][m];
        
        // 三重循环计算矩阵乘法
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int c = 0; c < k; c++) {
                    // 使用long类型防止中间计算溢出，每步都进行模运算
                    ans[i][j] = (int) (((long) a[i][c] * b[c][j] + ans[i][j]) % MOD);
                }
            }
        }
        return ans;
    }
    
    /**
     * 矩阵快速幂
     * 
     * 算法原理:
     * 利用二进制分解指数，通过不断平方和累积结果实现快速计算
     * 例如: A^13，13的二进制为1101
     * A^13 = A^8 * A^4 * A^1 (对应二进制位为1的位置)
     * 
     * 时间复杂度: O(n^3 * logp) - n为矩阵维度
     * 空间复杂度: O(n^2)
     * 
     * 实现技巧:
     * - 使用位运算优化指数分解 (p >>= 1)
     * - 使用位运算检查二进制位是否为1 ((p & 1) != 0)
     * - 结果初始化为单位矩阵
     * 
     * @param m 底数矩阵（必须是方阵）
     * @param p 指数
     * @return 矩阵m的p次幂
     */
    // 矩阵快速幂
    public static int[][] power(int[][] m, int p) {
        int n = m.length;  // 矩阵维度
        // 构造单位矩阵作为初始结果
        int[][] ans = new int[n][n];
        for (int i = 0; i < n; i++) {
            ans[i][i] = 1;  // 单位矩阵对角线元素为1
        }
        
        // 快速幂算法核心实现
        for (; p != 0; p >>= 1) {  // 指数不断右移一位（除以2）
            if ((p & 1) != 0) {    // 如果当前位为1，则累乘到结果中
                ans = multiply(ans, m);
            }
            m = multiply(m, m);    // 底数不断平方
        }
        return ans;
    }
}