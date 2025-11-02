package class098;

/**
 * POJ 3233 Matrix Power Series
 * 题目链接: http://poj.org/problem?id=3233
 * 题目大意: 给定一个n×n的矩阵A和正整数k，求S = A + A^2 + A^3 + ... + A^k
 * 解法: 使用矩阵快速幂和分治法求解
 * 时间复杂度: O(n^3 * logk)
 * 空间复杂度: O(n^2)
 * 
 * 优化思路:
 * 1. 使用分治法优化求和过程
 * 2. 当k为偶数时: S(k) = (A^(k/2) + I) * S(k/2)
 * 3. 当k为奇数时: S(k) = S(k-1) + A^k
 * 
 * 工程化考虑:
 * 1. 异常处理: 检查输入参数的有效性
 * 2. 边界条件: k=0, k=1的特殊情况
 * 3. 模运算: 防止整数溢出
 * 4. 内存优化: 复用矩阵对象减少内存分配
 * 
 * 与其他解法对比:
 * 1. 暴力解法: 直接计算每一项然后求和，时间复杂度O(k*n^3)
 * 2. 本解法: 使用分治和矩阵快速幂，时间复杂度O(n^3 * logk)
 * 3. 最优性: 当k较大时，本解法明显优于暴力解法
 */
import java.util.Scanner;

public class Code12_MatrixPowerSeriesDetailed {

    static int n, k, mod;
    static int[][] A;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        try {
            n = scanner.nextInt();
            k = scanner.nextInt();
            mod = scanner.nextInt();
            
            // 参数校验 - 工程化异常防御
            if (n <= 0 || k < 0 || mod <= 0) {
                throw new IllegalArgumentException("参数不合法: n必须大于0, k必须大于等于0, mod必须大于0");
            }
            
            A = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    A[i][j] = scanner.nextInt() % mod;
                    // 处理负数的情况
                    if (A[i][j] < 0) {
                        A[i][j] += mod;
                    }
                }
            }
            
            long startTime = System.currentTimeMillis();
            int[][] result = matrixPowerSeries(A, k);
            long endTime = System.currentTimeMillis();
            
            System.out.println("计算耗时: " + (endTime - startTime) + "ms");
            printMatrix(result);
        } catch (Exception e) {
            System.err.println("程序运行出错: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    /**
     * 矩阵加法
     * 时间复杂度: O(n^2) - 需要遍历矩阵中的每个元素
     * 空间复杂度: O(n^2) - 需要存储结果矩阵
     * 
     * 算法特点:
     * - 逐元素相加并取模
     * - 防止整数溢出（通过取模运算）
     * 
     * 注意事项:
     * - 假设输入矩阵a和b的维度相同
     * - 在实际工程中应添加维度检查
     */
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
     * 时间复杂度: O(n^3) - 三重循环，每层循环次数与矩阵维度相关
     * 空间复杂度: O(n^2) - 需要存储结果矩阵
     * 
     * 算法特点:
     * - 标准的矩阵乘法实现
     * - 使用long类型临时变量防止整数溢出
     * - 每一步计算后都进行模运算
     * 
     * 优化思路:
     * - 对于大型矩阵，可以考虑分块矩阵乘法（Strassen算法）降低理论复杂度至O(n^log₂7)≈O(n^2.807)
     * - 缓存友好的实现可以优化内存访问模式
     * 
     * 边界检查:
     * - 此实现假设矩阵乘法可行（a的列数等于b的行数）
     */
    public static int[][] matrixMultiply(int[][] a, int[][] b) {
        int[][] res = new int[a.length][b[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                for (int c = 0; c < a[0].length; c++) {
                    res[i][j] = (int) ((res[i][j] + (long) a[i][c] * b[c][j]) % mod);
                }
            }
        }
        return res;
    }

    /**
     * 构造单位矩阵
     * 时间复杂度: O(n^2) - 需要初始化n×n矩阵
     * 空间复杂度: O(n^2) - 需要存储单位矩阵
     * 
     * 数学性质:
     * - 单位矩阵I满足: I * A = A * I = A
     * - 主对角线上元素为1，其余为0
     * 
     * 应用场景:
     * - 矩阵快速幂的初始结果
     * - 作为矩阵乘法的单位元
     */
    public static int[][] identityMatrix(int size) {
        int[][] res = new int[size][size];
        for (int i = 0; i < size; i++) {
            res[i][i] = 1;
        }
        return res;
    }

    /**
     * 矩阵快速幂
     * 时间复杂度: O(n^3 * logp) - 分析：
     *   - 快速幂算法将幂运算分解为O(logp)次乘法
     *   - 每次矩阵乘法的复杂度为O(n^3)
     *   - 总时间复杂度 = O(logp) * O(n^3) = O(n^3 * logp)
     * 
     * 空间复杂度: O(n^2) - 存储矩阵需要O(n^2)空间
     * 
     * 算法原理:
     * - 利用二进制分解指数
     * - 例如: A^5 = A^(4+1) = A^4 * A^1
     * - 通过不断平方和累积结果实现快速计算
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
     * 异常场景处理:
     * - 处理了exp=0的边界情况，返回零矩阵
     * - 处理了exp=1的边界情况，直接返回原矩阵
     * 
     * 性能优化点:
     * - 使用位移运算替代除法: exp >> 1 比 exp / 2 更高效
     * - 使用位运算检查奇偶性: (exp & 1) 比 exp % 2 更高效
     * - 递归分治策略避免了O(k)次矩阵幂计算
     */
    public static int[][] matrixPowerSeries(int[][] base, int exp) {
        // 边界条件处理
        if (exp == 0) {
            // 返回零矩阵
            int[][] zero = new int[base.length][base[0].length];
            return zero;
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
            int[][] identity = identityMatrix(base.length);
            int[][] factor = matrixAdd(power, identity);
            return matrixMultiply(factor, sub);
        }
    }

    /**
     * 打印矩阵
     * 时间复杂度: O(n^2) - 需要遍历矩阵中的每个元素
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