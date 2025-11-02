package number_theory;

/**
 * 矩阵运算类
 * 
 * 算法简介:
 * 实现矩阵的基本运算，包括加法、减法、乘法、转置、求逆、行列式计算等。
 * 同时实现高斯消元法和异或方程组求解。
 * 
 * 适用场景:
 * 1. 线性方程组求解
 * 2. 图论问题（邻接矩阵幂运算）
 * 3. 递推关系求解
 * 4. 自动机状态转移
 * 
 * 核心思想:
 * 1. 矩阵乘法结合律优化递推计算
 * 2. 高斯消元法求解线性方程组
 * 3. 异或方程组的特殊求解方法
 * 4. 矩阵快速幂优化递推关系
 * 
 * 时间复杂度: 
 * - 矩阵乘法: O(n^3)
 * - 矩阵快速幂: O(n^3 log k)
 * - 高斯消元: O(n^3)
 * 空间复杂度: O(n^2)
 */
public class MatrixOperations {
    private static final long MOD = 1000000007;
    
    /**
     * 矩阵乘法
     * @param a 第一个矩阵
     * @param b 第二个矩阵
     * @return 乘积矩阵
     */
    public static long[][] multiply(long[][] a, long[][] b) {
        int n = a.length;
        int m = b[0].length;
        int p = b.length;
        
        long[][] result = new long[n][m];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < p; k++) {
                    result[i][j] = (result[i][j] + a[i][k] * b[k][j]) % MOD;
                }
            }
        }
        
        return result;
    }
    
    /**
     * 矩阵快速幂
     * @param base 底数矩阵
     * @param exp 指数
     * @return 幂运算结果
     */
    public static long[][] matrixPow(long[][] base, long exp) {
        int n = base.length;
        long[][] result = new long[n][n];
        
        // 初始化为单位矩阵
        for (int i = 0; i < n; i++) {
            result[i][i] = 1;
        }
        
        while (exp > 0) {
            if ((exp & 1) == 1) {
                result = multiply(result, base);
            }
            base = multiply(base, base);
            exp >>= 1;
        }
        
        return result;
    }
    
    /**
     * 高斯消元法求解线性方程组
     * @param a 增广矩阵
     * @return 方程组的解，无解返回null
     */
    public static long[] gaussianElimination(long[][] a) {
        int n = a.length;
        int m = a[0].length - 1;
        
        for (int i = 0; i < Math.min(n, m); i++) {
            // 选择主元
            int pivot = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(a[j][i]) > Math.abs(a[pivot][i])) {
                    pivot = j;
                }
            }
            
            // 交换行
            if (pivot != i) {
                long[] temp = a[i];
                a[i] = a[pivot];
                a[pivot] = temp;
            }
            
            // 消元
            if (a[i][i] == 0) continue;
            
            for (int j = i + 1; j < n; j++) {
                long factor = a[j][i] * modInverse(a[i][i], MOD) % MOD;
                for (int k = i; k <= m; k++) {
                    a[j][k] = (a[j][k] - factor * a[i][k] % MOD + MOD) % MOD;
                }
            }
        }
        
        // 回代求解
        long[] result = new long[m];
        for (int i = Math.min(n, m) - 1; i >= 0; i--) {
            long sum = 0;
            for (int j = i + 1; j < m; j++) {
                sum = (sum + a[i][j] * result[j]) % MOD;
            }
            
            if (a[i][i] == 0) {
                if (a[i][m] != 0) return null; // 无解
                result[i] = 0;
            } else {
                result[i] = (a[i][m] - sum + MOD) % MOD * modInverse(a[i][i], MOD) % MOD;
            }
        }
        
        return result;
    }
    
    /**
     * 异或方程组求解（XOR Gaussian Elimination）
     * @param a 系数矩阵（01矩阵）
     * @return 方程组的解，无解返回null
     */
    public static int[] xorGaussianElimination(int[][] a) {
        int n = a.length;
        int m = a[0].length - 1;
        
        for (int i = 0; i < Math.min(n, m); i++) {
            // 选择主元
            int pivot = i;
            for (int j = i + 1; j < n; j++) {
                if (a[j][i] > a[pivot][i]) {
                    pivot = j;
                }
            }
            
            // 交换行
            if (pivot != i) {
                int[] temp = a[i];
                a[i] = a[pivot];
                a[pivot] = temp;
            }
            
            // 消元
            if (a[i][i] == 0) continue;
            
            for (int j = i + 1; j < n; j++) {
                if (a[j][i] == 1) {
                    for (int k = i; k <= m; k++) {
                        a[j][k] ^= a[i][k];
                    }
                }
            }
        }
        
        // 回代求解
        int[] result = new int[m];
        for (int i = Math.min(n, m) - 1; i >= 0; i--) {
            int sum = 0;
            for (int j = i + 1; j < m; j++) {
                sum ^= (a[i][j] & result[j]);
            }
            
            if (a[i][i] == 0) {
                if (a[i][m] != sum) return null; // 无解
                result[i] = 0;
            } else {
                result[i] = a[i][m] ^ sum;
            }
        }
        
        return result;
    }
    
    /**
     * 计算矩阵的行列式
     * @param a 方阵
     * @return 行列式值
     */
    public static long determinant(long[][] a) {
        int n = a.length;
        long[][] temp = new long[n][n];
        
        // 复制矩阵
        for (int i = 0; i < n; i++) {
            System.arraycopy(a[i], 0, temp[i], 0, n);
        }
        
        long result = 1;
        for (int i = 0; i < n; i++) {
            // 选择主元
            int pivot = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(temp[j][i]) > Math.abs(temp[pivot][i])) {
                    pivot = j;
                }
            }
            
            // 交换行
            if (pivot != i) {
                long[] t = temp[i];
                temp[i] = temp[pivot];
                temp[pivot] = t;
                result = -result;
            }
            
            if (temp[i][i] == 0) return 0;
            
            result = result * temp[i][i] % MOD;
            
            // 消元
            for (int j = i + 1; j < n; j++) {
                long factor = temp[j][i] * modInverse(temp[i][i], MOD) % MOD;
                for (int k = i; k < n; k++) {
                    temp[j][k] = (temp[j][k] - factor * temp[i][k] % MOD + MOD) % MOD;
                }
            }
        }
        
        return (result + MOD) % MOD;
    }
    
    /**
     * 矩阵转置
     * @param a 矩阵
     * @return 转置矩阵
     */
    public static long[][] transpose(long[][] a) {
        int n = a.length;
        int m = a[0].length;
        long[][] result = new long[m][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                result[j][i] = a[i][j];
            }
        }
        
        return result;
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
     * 洛谷P3390 【模板】矩阵快速幂
     * 题目来源: https://www.luogu.com.cn/problem/P3390
     * 题目描述: 给定n×n的矩阵A，求A^k。
     * 解题思路: 直接使用矩阵快速幂算法
     * 时间复杂度: O(n^3 log k)
     * 空间复杂度: O(n^2)
     * 
     * @param n 矩阵大小
     * @param k 指数
     * @param a 矩阵A
     * @return A^k
     */
    public static long[][] solveP3390(int n, long k, long[][] a) {
        return matrixPow(a, k);
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试矩阵乘法
        long[][] a = {{1, 2}, {3, 4}};
        long[][] b = {{5, 6}, {7, 8}};
        long[][] result = multiply(a, b);
        System.out.println("Matrix multiplication result:");
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                System.out.print(result[i][j] + " ");
            }
            System.out.println();
        }
        
        // 测试洛谷P3390题目
        int n1 = 2;
        long k1 = 3;
        long[][] a1 = {{1, 2}, {3, 4}};
        long[][] result1 = solveP3390(n1, k1, a1);
        System.out.println("P3390 result:");
        for (int i = 0; i < result1.length; i++) {
            for (int j = 0; j < result1[0].length; j++) {
                System.out.print(result1[i][j] + " ");
            }
            System.out.println();
        }
        
        // 边界情况测试
        // 测试单位矩阵
        int n2 = 2, k2 = 5;
        long[][] identity = {{1, 0}, {0, 1}};
        long[][] result2 = solveP3390(n2, k2, identity);
        System.out.println("Boundary test 1 - identity matrix to power 5:");
        for (int i = 0; i < result2.length; i++) {
            for (int j = 0; j < result2[0].length; j++) {
                System.out.print(result2[i][j] + " ");
            }
            System.out.println();
        }
        
        // 测试零矩阵
        long[][] zero = {{0, 0}, {0, 0}};
        long[][] result3 = solveP3390(n2, k2, zero);
        System.out.println("Boundary test 2 - zero matrix to power 5:");
        for (int i = 0; i < result3.length; i++) {
            for (int j = 0; j < result3[0].length; j++) {
                System.out.print(result3[i][j] + " ");
            }
            System.out.println();
        }
        
        // 测试1x1矩阵
        int n3 = 1, k3 = 10;
        long[][] single = {{3}};
        long[][] result4 = solveP3390(n3, k3, single);
        System.out.println("Boundary test 3 - 1x1 matrix to power 10: " + result4[0][0]);
    }
}