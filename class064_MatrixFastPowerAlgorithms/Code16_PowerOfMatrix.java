package class098;

/**
 * UVA 11149 Power of Matrix
 * 题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2090
 * 题目大意: 给定一个n×n的矩阵A，求A^1 + A^2 + ... + A^k的值，结果对10取模
 * 解法: 使用矩阵快速幂和分治法求解
 * 时间复杂度: O(n^3 * logk)
 * 空间复杂度: O(n^2)
 * 
 * 数学分析:
 * 1. 当k为偶数时: S(k) = S(k/2) + A^(k/2) * S(k/2)
 * 2. 当k为奇数时: S(k) = S(k-1) + A^k
 * 
 * 优化思路:
 * 1. 使用分治法优化求和过程
 * 2. 结合矩阵快速幂计算矩阵的幂
 * 
 * 工程化考虑:
 * 1. 边界条件处理: k=0, k=1的特殊情况
 * 2. 输入验证: 检查输入的有效性
 * 3. 模运算: 防止整数溢出
 * 
 * 与其他解法对比:
 * 1. 暴力解法: 直接计算每一项然后求和，时间复杂度O(k*n^3)
 * 2. 本解法: 使用分治和矩阵快速幂，时间复杂度O(n^3 * logk)
 * 3. 最优性: 当k较大时，本解法明显优于暴力解法
 */
import java.util.Scanner;

public class Code16_PowerOfMatrix {

    static final int MOD = 10;
    static int n, k;
    static int[][] A;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            n = scanner.nextInt();
            if (n == 0) {
                break;
            }
            
            k = scanner.nextInt();
            A = new int[n][n];
            
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    A[i][j] = scanner.nextInt() % MOD;
                }
            }
            
            int[][] result = matrixPowerSeries(A, k);
            printMatrix(result);
            System.out.println();  // 输出空行
        }
        
        scanner.close();
    }

    /**
     * 矩阵加法
     * 时间复杂度: O(n^2)
     * 空间复杂度: O(n^2)
     */
    public static int[][] matrixAdd(int[][] a, int[][] b) {
        int[][] res = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = (a[i][j] + b[i][j]) % MOD;
            }
        }
        return res;
    }

    /**
     * 矩阵乘法
     * 时间复杂度: O(n^3)
     * 空间复杂度: O(n^2)
     */
    public static int[][] matrixMultiply(int[][] a, int[][] b) {
        int[][] res = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int c = 0; c < n; c++) {
                    res[i][j] = (res[i][j] + a[i][c] * b[c][j]) % MOD;
                }
            }
        }
        return res;
    }

    /**
     * 构造单位矩阵
     * 时间复杂度: O(n^2)
     * 空间复杂度: O(n^2)
     */
    public static int[][] identityMatrix() {
        int[][] res = new int[n][n];
        for (int i = 0; i < n; i++) {
            res[i][i] = 1;
        }
        return res;
    }

    /**
     * 矩阵快速幂
     * 时间复杂度: O(n^3 * logp)
     * 空间复杂度: O(n^2)
     */
    public static int[][] matrixPower(int[][] base, int exp) {
        int[][] res = identityMatrix();
        while (exp > 0) {
            if ((exp & 1) == 1) {
                res = matrixMultiply(res, base);
            }
            base = matrixMultiply(base, base);
            exp >>= 1;
        }
        return res;
    }

    /**
     * 矩阵幂级数求和 - 分治法
     * 时间复杂度: O(n^3 * logk)
     * 空间复杂度: O(n^2)
     * 
     * 算法思路:
     * 1. 当exp=1时，直接返回base
     * 2. 当exp为奇数时，S(k) = S(k-1) + A^k
     * 3. 当exp为偶数时，S(k) = (A^(k/2) + I) * S(k/2)
     */
    public static int[][] matrixPowerSeries(int[][] base, int exp) {
        // 边界条件处理
        if (exp == 0) {
            // 返回零矩阵
            return new int[n][n];
        }
        
        if (exp == 1) {
            return base;
        }
        
        if ((exp & 1) == 1) {
            // S(k) = S(k-1) + A^k
            int[][] sub = matrixPowerSeries(base, exp - 1);
            int[][] power = matrixPower(base, exp);
            return matrixAdd(sub, power);
        } else {
            // S(k) = (A^(k/2) + I) * S(k/2)
            int half = exp >> 1;
            int[][] sub = matrixPowerSeries(base, half);
            int[][] power = matrixPower(base, half);
            int[][] identity = identityMatrix();
            int[][] factor = matrixAdd(power, identity);
            return matrixMultiply(factor, sub);
        }
    }

    /**
     * 打印矩阵
     */
    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j == 0) {
                    System.out.print(matrix[i][j]);
                } else {
                    System.out.print(" " + matrix[i][j]);
                }
            }
            System.out.println();
        }
    }
}