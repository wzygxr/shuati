package class098;

/**
 * SPOJ FIBOSUM - Fibonacci Sum
 * 
 * 题目链接: https://www.spoj.com/problems/FIBOSUM/
 * 
 * 题目大意: 
 * 给定n和m，计算斐波那契数列第n项到第m项的和
 * F(0) = 0, F(1) = 1, F(n) = F(n-1) + F(n-2) for n >= 2
 * 
 * 解法分析:
 * 使用矩阵快速幂优化计算斐波那契数列前n项和
 * 
 * 数学原理:
 * 利用扩展的矩阵表示同时计算斐波那契数列项和前缀和
 * 
 * 时间复杂度: O(logm)
 * 空间复杂度: O(1)
 * 
 * 优化思路:
 * 1. 利用前缀和性质: sum(n,m) = prefixSum(m) - prefixSum(n-1)
 * 2. 扩展状态矩阵同时计算项值和前缀和
 * 
 * 工程化考虑:
 * 1. 模运算: 防止整数溢出
 * 2. 边界条件处理: n<0, n=0, n=1的特殊情况
 * 3. 负数取模处理: (a-b+MOD)%MOD
 */

import java.util.Scanner;

public class Code10_FibonacciSum {
    
    static final int MOD = 1000000007;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int testCases = scanner.nextInt();
        
        for (int i = 0; i < testCases; i++) {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            // 利用前缀和性质: sum(n,m) = prefixSum(m) - prefixSum(n-1)
            // 加上MOD是为了处理负数取模的情况
            System.out.println((solve(m) - solve(n - 1) + MOD) % MOD);
        }
    }
    
    /**
     * 计算斐波那契数列前n项的和
     * 
     * 算法思路:
     * 1. 扩展状态矩阵同时计算斐波那契数列项和前缀和
     * 2. 初始状态 [F(1), F(0), S(1)] = [1, 0, 1]
     * 3. 转移矩阵表示状态转移关系
     * 4. 使用矩阵快速幂计算转移矩阵的(n-1)次幂
     * 5. 初始状态向量乘以结果矩阵得到最终状态
     * 
     * 数学原理:
     * 扩展状态矩阵:
     * [F(n+1)]   [1 1 0] [F(n)  ]
     * [F(n)  ] = [1 0 0] [F(n-1)]
     * [S(n)  ]   [1 1 1] [S(n-1)]
     * 
     * 时间复杂度: O(logn)
     * 空间复杂度: O(1)
     * 
     * @param n 项数
     * @return 前n项斐波那契数列的和
     */
    // 计算斐波那契数列前n项的和
    public static int solve(int n) {
        // 边界条件处理
        if (n < 0) return 0;
        if (n == 0) return 0;
        if (n == 1) return 1;
        
        // 构造初始状态矩阵 [F(1), F(0), S(1)] = [1, 0, 1]
        // 其中S(n)表示前n项斐波那契数列的和
        long[][] start = {{1, 0, 1}};
        
        // 转移矩阵
        // [F(n+1)]   [1 1 0] [F(n)  ]
        // [F(n)  ] = [1 0 0] [F(n-1)]
        // [S(n)  ]   [1 1 1] [S(n-1)]
        int[][] base = {
            {1, 1, 0},
            {1, 0, 0},
            {1, 1, 1}
        };
        
        long[][] result = multiply(start, power(base, n - 1));
        return (int) result[0][2]; // 返回S(n)
    }
    
    /**
     * 矩阵相乘 (long[][] × int[][])
     * 
     * 算法原理:
     * 对于矩阵A(n×k)和矩阵B(k×m)，结果矩阵C(n×m)中:
     * C[i][j] = Σ(A[i][k] * B[k][j]) for k in 0..k-1
     * 
     * 时间复杂度: O(n×m×k)
     * 空间复杂度: O(n×m)
     * 
     * @param a 第一个矩阵 (long[][])
     * @param b 第二个矩阵 (int[][])
     * @return 两个矩阵的乘积 (long[][])
     * 
     * 算法特点:
     * - 每步都进行模运算防止溢出
     */
    // 矩阵相乘
    public static long[][] multiply(long[][] a, int[][] b) {
        int n = a.length;      // 结果矩阵行数
        int m = b[0].length;   // 结果矩阵列数
        int k = a[0].length;   // 中间维度（a的列数，b的行数）
        long[][] ans = new long[n][m];
        
        // 三重循环计算矩阵乘法
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int c = 0; c < k; c++) {
                    ans[i][j] = (ans[i][j] + a[i][c] * b[c][j]) % MOD;
                }
            }
        }
        return ans;
    }
    
    /**
     * 矩阵相乘 (int[][] × int[][])
     * 
     * 算法原理:
     * 对于矩阵A(n×k)和矩阵B(k×m)，结果矩阵C(n×m)中:
     * C[i][j] = Σ(A[i][k] * B[k][j]) for k in 0..k-1
     * 
     * 时间复杂度: O(n×m×k)
     * 空间复杂度: O(n×m)
     * 
     * @param a 第一个矩阵 (int[][])
     * @param b 第二个矩阵 (int[][])
     * @return 两个矩阵的乘积 (int[][])
     * 
     * 算法特点:
     * - 使用long类型临时变量防止中间计算溢出
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