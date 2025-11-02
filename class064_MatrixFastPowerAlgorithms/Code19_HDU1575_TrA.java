package class098;

/**
 * HDU 1575 - Tr A
 * 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1575
 * 题目大意: 给定一个n×n的矩阵A，求A^k的迹（主对角线元素之和）mod 9973
 * 解法: 使用矩阵快速幂求解
 * 时间复杂度: O(n^3 * logk)
 * 空间复杂度: O(n^2)
 * 
 * 数学分析:
 * 1. 矩阵的迹定义为矩阵主对角线元素之和
 * 2. 对于矩阵幂A^k，其迹等于A^k的主对角线元素之和
 * 3. 使用矩阵快速幂计算A^k，然后求迹
 * 
 * 优化思路:
 * 1. 使用矩阵快速幂将时间复杂度从O(k*n^3)降低到O(n^3 * logk)
 * 2. 注意模运算防止溢出
 * 
 * 工程化考虑:
 * 1. 边界条件处理: k=0的特殊情况（单位矩阵的迹为n）
 * 2. 输入验证: 检查矩阵维度和k的有效性
 * 3. 模运算: 防止整数溢出
 * 
 * 与其他解法对比:
 * 1. 暴力解法: 直接计算A^k然后求迹，时间复杂度O(k*n^3)
 * 2. 矩阵快速幂: 时间复杂度O(n^3 * logk)
 * 3. 最优性: 当k较大时，矩阵快速幂明显优于暴力解法
 */
import java.util.Scanner;

public class Code19_HDU1575_TrA {

    static final int MOD = 9973;
    static int n, k;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int T = scanner.nextInt(); // 测试用例数量
        
        for (int t = 0; t < T; t++) {
            n = scanner.nextInt();
            k = scanner.nextInt();
            
            int[][] A = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    A[i][j] = scanner.nextInt() % MOD;
                }
            }
            
            int result = solve(A);
            System.out.println(result);
        }
        
        scanner.close();
    }

    /**
     * 计算A^k的迹mod 9973
     * 时间复杂度: O(n^3 * logk)
     * 空间复杂度: O(n^2)
     * 
     * 算法思路:
     * 1. 使用矩阵快速幂计算A^k
     * 2. 计算结果矩阵的迹（主对角线元素之和）
     * 3. 对结果取模
     */
    public static int solve(int[][] A) {
        // 特殊情况处理: k=0时，A^0是单位矩阵，迹为n
        if (k == 0) {
            return n % MOD;
        }
        
        // 计算A^k
        int[][] result = matrixPower(A, k);
        
        // 计算迹
        int trace = 0;
        for (int i = 0; i < n; i++) {
            trace = (trace + result[i][i]) % MOD;
        }
        
        return trace;
    }

    /**
     * 矩阵乘法
     * 时间复杂度: O(n^3)
     * 空间复杂度: O(n^2)
     * 
     * 算法特点:
     * - 标准的矩阵乘法实现
     * - 使用long类型临时变量防止整数溢出
     * - 每一步计算后都进行模运算
     * 
     * 优化思路:
     * - 对于大型矩阵，可以考虑分块矩阵乘法（Strassen算法）
     * - 缓存友好的实现可以优化内存访问模式
     */
    public static int[][] matrixMultiply(int[][] a, int[][] b) {
        int[][] res = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                long sum = 0;
                for (int c = 0; c < n; c++) {
                    sum = (sum + (long) a[i][c] * b[c][j]) % MOD;
                }
                res[i][j] = (int) sum;
            }
        }
        return res;
    }

    /**
     * 构造单位矩阵
     * 时间复杂度: O(n^2)
     * 空间复杂度: O(n^2)
     * 
     * 数学性质:
     * - 单位矩阵I满足: I * A = A * I = A
     * - 主对角线上元素为1，其余为0
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
     * 时间复杂度: O(n^3 * logk)
     * 空间复杂度: O(n^2)
     * 
     * 算法原理:
     * - 利用二进制分解指数
     * - 通过不断平方和累积结果实现快速计算
     * 
     * 实现技巧:
     * - 使用位移运算优化指数分解
     * - 使用位运算检查二进制位是否为1
     * - 结果初始化为单位矩阵
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
}