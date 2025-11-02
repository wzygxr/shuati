package class098;

/**
 * 求斐波那契数列第n项
 * 
 * 题目描述:
 * 斐波那契数列定义如下：
 * F(0) = 0，F(1) = 1
 * F(n) = F(n - 1) + F(n - 2)，其中 n > 1
 * 给定n，计算F(n)的值
 * 
 * 解法分析:
 * 1. 普通解法：使用动态规划，时间复杂度O(n)
 * 2. 矩阵快速幂解法：利用矩阵表示递推关系，时间复杂度O(logn)
 * 
 * 矩阵推导:
 * 斐波那契递推关系可以表示为矩阵形式：
 * [F(n)  ]   = [1 1] * [F(n-1)]
 * [F(n-1)]     [1 0]   [F(n-2)]
 * 
 * 通过不断展开可得：
 * [F(n)  ]   = [1 1]^(n-1) * [F(1)]
 * [F(n-1)]     [1 0]         [F(0)]
 * 
 * 测试链接: https://leetcode.cn/problems/fibonacci-number/
 * 
 * 工程化考虑:
 * 1. 边界条件处理: n=0, n=1的特殊情况
 * 2. 输入验证: 检查n的有效性
 * 3. 模运算: 防止整数溢出（本题未涉及）
 * 
 * 与其他解法对比:
 * 1. 递归解法: 时间复杂度O(2^n)，空间复杂度O(n)
 * 2. 动态规划: 时间复杂度O(n)，空间复杂度O(1)
 * 3. 矩阵快速幂: 时间复杂度O(logn)，空间复杂度O(1)
 * 4. 最优性: 当n较大时，矩阵快速幂明显优于其他解法
 */
public class Code02_FibonacciNumber {

    /**
     * 使用动态规划计算斐波那契数列第n项
     * 
     * 算法思路:
     * 从F(0)和F(1)开始，逐步计算到F(n)
     * 使用两个变量保存前两项的值，避免使用数组存储所有值
     * 
     * 时间复杂度: O(n) - 需要计算n次
     * 空间复杂度: O(1) - 只使用常数额外空间
     * 
     * @param n 斐波那契数列项数
     * @return F(n)的值
     */
    // 时间复杂度O(n)，普通解法，讲解066，题目1
    public static int fib1(int n) {
        // 边界条件处理
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        
        // 使用两个变量保存前两项的值
        int lastLast = 0, last = 1;
        for (int i = 2, cur; i <= n; i++) {
            cur = lastLast + last;  // 计算当前项
            lastLast = last;        // 更新前一项
            last = cur;             // 更新当前项
        }
        return last;
    }

    /**
     * 使用矩阵快速幂计算斐波那契数列第n项
     * 
     * 算法思路:
     * 1. 将斐波那契递推关系转换为矩阵形式
     * 2. 使用矩阵快速幂计算转移矩阵的(n-1)次幂
     * 3. 初始状态向量乘以结果矩阵得到最终答案
     * 
     * 数学原理:
     * [F(n)  ]   = [1 1]^(n-1) * [F(1)]
     * [F(n-1)]     [1 0]         [F(0)]
     * 
     * 时间复杂度: O(logn) - 使用矩阵快速幂优化
     * 空间复杂度: O(1) - 只使用常数额外空间
     * 
     * @param n 斐波那契数列项数
     * @return F(n)的值
     */
    // 时间复杂度O(logn)，矩阵快速幂的解法
    public static int fib2(int n) {
        // 边界条件处理
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        
        // 初始状态向量 [F(1), F(0)] = [1, 0]
        int[][] start = { { 1, 0 } };
        
        // 转移矩阵 [[1,1],[1,0]]
        int[][] base = {
                { 1, 1 },
                { 1, 0 }
                };
        
        // 计算 start * base^(n-1) 得到 [F(n), F(n-1)]
        int[][] ans = multiply(start, power(base, n - 1));
        return ans[0][0];  // 返回F(n)
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
     * 注意事项:
     * - 矩阵乘法要求第一个矩阵的列数等于第二个矩阵的行数
     * 
     * @param a 第一个矩阵 (n×k)
     * @param b 第二个矩阵 (k×m)
     * @return 两个矩阵的乘积 (n×m)
     */
    // 矩阵相乘
    // a的列数一定要等于b的行数
    public static int[][] multiply(int[][] a, int[][] b) {
        int n = a.length;      // 结果矩阵行数
        int m = b[0].length;   // 结果矩阵列数
        int k = a[0].length;   // 中间维度（a的列数，b的行数）
        int[][] ans = new int[n][m];
        
        // 三重循环计算矩阵乘法
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int c = 0; c < k; c++) {
                    ans[i][j] += a[i][c] * b[c][j];
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