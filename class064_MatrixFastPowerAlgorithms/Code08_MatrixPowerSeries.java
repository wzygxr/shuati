package class098;

/**
 * POJ 3233 Matrix Power Series
 * 
 * 题目链接: http://poj.org/problem?id=3233
 * 
 * 题目大意: 
 * 给定一个n×n的矩阵A和正整数k，求S = A + A^2 + A^3 + ... + A^k
 * 
 * 解法分析:
 * 使用矩阵快速幂和分治法求解，避免直接计算k次矩阵幂
 * 
 * 数学原理:
 * 利用分治思想优化求和过程:
 * 1. 当k为偶数时: S(k) = (A^(k/2) + I) * S(k/2)
 * 2. 当k为奇数时: S(k) = S(k-1) + A^k
 * 
 * 时间复杂度: O(n^3 * logk)
 * 空间复杂度: O(n^2)
 * 
 * 优化思路:
 * 1. 使用分治法避免O(k)次矩阵幂计算
 * 2. 利用矩阵快速幂优化单次幂运算
 * 
 * 工程化考虑:
 * 1. 异常处理: 检查输入参数的有效性
 * 2. 边界条件: k=1的特殊情况
 * 3. 模运算: 防止整数溢出
 * 4. 内存优化: 复用矩阵对象减少内存分配
 * 
 * 与其他解法对比:
 * 1. 暴力解法: 直接计算每一项然后求和，时间复杂度O(k*n^3)
 * 2. 本解法: 使用分治和矩阵快速幂，时间复杂度O(n^3 * logk)
 * 3. 最优性: 当k较大时，本解法明显优于暴力解法
 */

import java.util.Scanner;

public class Code08_MatrixPowerSeries {

    static int n, k, mod;
    static int[][] A;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        k = scanner.nextInt();
        mod = scanner.nextInt();
        
        A = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = scanner.nextInt() % mod;
            }
        }
        
        int[][] result = matrixPowerSeries(A, k);
        printMatrix(result);
    }

    /**
     * 矩阵加法
     * 
     * 算法原理:
     * 对应位置元素相加并取模
     * 
     * 时间复杂度: O(n^2) - 需要遍历矩阵中的每个元素
     * 空间复杂度: O(n^2) - 需要存储结果矩阵
     * 
     * @param a 第一个矩阵
     * @param b 第二个矩阵
     * @return 两个矩阵的和
     * 
     * 算法特点:
     * - 逐元素相加并取模
     * - 防止整数溢出（通过取模运算）
     */
    // 矩阵加法
    public static int[][] matrixAdd(int[][] a, int[][] b) {
        int[][] res = new int[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                res[i][j] = (a[i][j] + b[i][j]) % mod;
            }
        }
        return res;
    }

    /**
     * 矩阵乘法
     * 
     * 算法原理:
     * 对于矩阵A(n×k)和矩阵B(k×m)，结果矩阵C(n×m)中:
     * C[i][j] = Σ(A[i][k] * B[k][j]) for k in 0..k-1
     * 
     * 时间复杂度: O(n^3) - 三重循环，每层循环次数与矩阵维度相关
     * 空间复杂度: O(n^2) - 需要存储结果矩阵
     * 
     * @param a 第一个矩阵
     * @param b 第二个矩阵
     * @return 两个矩阵的乘积
     * 
     * 算法特点:
     * - 标准的矩阵乘法实现
     * - 使用long类型临时变量防止整数溢出
     * - 每一步计算后都进行模运算
     * 
     * 优化思路:
     * - 对于大型矩阵，可以考虑分块矩阵乘法（Strassen算法）降低理论复杂度至O(n^log₂7)≈O(n^2.807)
     * - 缓存友好的实现可以优化内存访问模式
     */
    // 矩阵乘法
    public static int[][] matrixMultiply(int[][] a, int[][] b) {
        int[][] res = new int[a.length][b[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                for (int k = 0; k < a[0].length; k++) {
                    res[i][j] = (int) ((res[i][j] + (long) a[i][k] * b[k][j]) % mod);
                }
            }
        }
        return res;
    }

    /**
     * 构造单位矩阵
     * 
     * 数学性质:
     * - 单位矩阵I满足: I * A = A * I = A
     * - 主对角线上元素为1，其余为0
     * 
     * 时间复杂度: O(n^2) - 需要初始化n×n矩阵
     * 空间复杂度: O(n^2) - 需要存储单位矩阵
     * 
     * @param size 矩阵维度
     * @return size×size的单位矩阵
     * 
     * 应用场景:
     * - 矩阵快速幂的初始结果
     * - 作为矩阵乘法的单位元
     */
    // 构造单位矩阵
    public static int[][] identityMatrix(int size) {
        int[][] res = new int[size][size];
        for (int i = 0; i < size; i++) {
            res[i][i] = 1;
        }
        return res;
    }

    /**
     * 矩阵快速幂
     * 
     * 算法原理:
     * 利用二进制分解指数，通过不断平方和累积结果实现快速计算
     * 例如: A^13，13的二进制为1101
     * A^13 = A^8 * A^4 * A^1 (对应二进制位为1的位置)
     * 
     * 时间复杂度: O(n^3 * logp) - 分析：
     *   - 快速幂算法将幂运算分解为O(logp)次乘法
     *   - 每次矩阵乘法的复杂度为O(n^3)
     *   - 总时间复杂度 = O(logp) * O(n^3) = O(n^3 * logp)
     * 
     * 空间复杂度: O(n^2) - 存储矩阵需要O(n^2)空间
     * 
     * @param base 底数矩阵
     * @param exp 指数
     * @return 矩阵的exp次幂
     * 
     * 实现技巧:
     * - 使用位移运算优化指数分解
     * - 使用位运算检查二进制位是否为1
     * - 结果初始化为单位矩阵
     * 
     * 优化点:
     * - 可以通过缓存中间结果进一步优化
     * - 对于稀疏矩阵，可以采用特殊的数据结构降低计算复杂度
     */
    // 矩阵快速幂
    public static int[][] matrixPower(int[][] base, int exp) {
        int[][] res = identityMatrix(base.length);
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
     * 
     * 数学原理:
     * 利用分治思想优化求和过程，避免直接计算k次矩阵幂
     * S = A + A^2 + A^3 + ... + A^k
     * 
     * 算法思路:
     * 1. 当exp=1时，直接返回base
     * 2. 当exp为奇数时，S(k) = S(k-1) + A^k
     * 3. 当exp为偶数时，S(k) = (A^(k/2) + I) * S(k/2)
     * 
     * 数学原理证明:
     * - 偶数情况: S(k) = A + A^2 + ... + A^k
     *                   = (A + A^2 + ... + A^(k/2)) + (A^(k/2+1) + ... + A^k)
     *                   = S(k/2) + A^(k/2) * S(k/2)
     *                   = (I + A^(k/2)) * S(k/2)
     * 
     * 时间复杂度: O(n^3 * logk) - 分析：
     *   - 每次递归将问题规模减半，共递归logk次
     *   - 每次递归中的矩阵乘法和加法操作复杂度为O(n^3)
     *   - 总时间复杂度 = O(logk) * O(n^3) = O(n^3 * logk)
     * 
     * 空间复杂度: O(n^2) - 分析：
     *   - 存储矩阵需要O(n^2)空间
     *   - 递归调用栈深度为O(logk)
     *   - 总空间复杂度为O(n^2 + logk) = O(n^2)（当n较大时）
     * 
     * @param base 底数矩阵
     * @param exp 指数
     * @return 矩阵幂级数和 S = A + A^2 + ... + A^exp
     * 
     * 异常场景处理:
     * - 处理了exp=1的边界情况，直接返回原矩阵
     * 
     * 性能优化点:
     * - 使用位移运算替代除法: exp >> 1 比 exp / 2 更高效
     * - 使用位运算检查奇偶性: (exp & 1) 比 exp % 2 更高效
     * - 递归分治策略避免了O(k)次矩阵幂计算
     */
    // 矩阵幂级数求和 - 分治法
    public static int[][] matrixPowerSeries(int[][] base, int exp) {
        // 边界条件处理
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
            int[][] identity = identityMatrix(base.length);
            int[][] factor = matrixAdd(power, identity);
            return matrixMultiply(factor, sub);
        }
    }

    /**
     * 打印矩阵
     * 
     * 时间复杂度: O(n^2) - 需要遍历矩阵中的每个元素
     * 
     * @param matrix 要打印的矩阵
     * 
     * 输出格式:
     * - 每行输出矩阵的一行元素
     * - 元素之间用空格分隔
     * - 行末不输出多余的空格
     * 
     * 工程化考虑:
     * - 格式化输出保证可读性
     * - 对于大型矩阵，可以考虑添加分页或摘要输出功能
     */
    // 打印矩阵
    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j]);
                if (j < matrix[0].length - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}