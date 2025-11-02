package class098;

/**
 * 矩阵乘法与矩阵快速幂演示
 * 
 * 本程序演示了矩阵乘法、矩阵快速幂以及如何使用矩阵快速幂解决斐波那契数列问题
 * 
 * 核心知识点:
 * 1. 矩阵乘法的实现
 * 2. 矩阵快速幂算法
 * 3. 斐波那契数列的矩阵表示
 * 
 * 数学原理:
 * 斐波那契数列递推关系: F(n) = F(n-1) + F(n-2)
 * 矩阵表示形式:
 * [F(n)  ]   = [1 1] * [F(n-1)]
 * [F(n-1)]     [1 0]   [F(n-2)]
 * 
 * 通过不断展开可得:
 * [F(n)  ]   = [1 1]^(n-1) * [F(1)]
 * [F(n-1)]     [1 0]         [F(0)]
 * 
 * 应用场景:
 * 1. 线性递推关系求解
 * 2. 动态规划优化
 * 3. 图论中的路径计数
 * 4. 密码学中的大指数运算
 * 
 * 时间复杂度分析:
 * 1. 矩阵乘法: O(n^3)
 * 2. 矩阵快速幂: O(n^3 * logp)
 * 3. 斐波那契数列: O(logn)
 * 
 * 空间复杂度: O(n^2)
 */
public class Code02_BigShow {

    public static void main(String[] args) {
        System.out.println("f1() : ");
        System.out.println("矩阵乘法展示开始");
        f1();
        System.out.println("矩阵乘法展示结束");
        System.out.println();

        System.out.println("f2() : ");
        System.out.println("矩阵快速幂展示开始");
        f2();
        System.out.println("矩阵快速幂展示结束");
        System.out.println();

        System.out.println("f3() : ");
        System.out.println("求斐波那契数列第n项");
        System.out.println("用矩阵乘法解决");
        System.out.println("展示开始");
        f3();
        System.out.println("展示结束");
        System.out.println();

        System.out.println("f4() : ");
        System.out.println("求斐波那契数列第n项");
        System.out.println("用矩阵快速幂解决");
        System.out.println("展示开始");
        f4();
        System.out.println("展示结束");
        System.out.println();
    }

    /**
     * 矩阵相乘实现
     * 
     * 算法原理:
     * 对于矩阵A(n×k)和矩阵B(k×m)，结果矩阵C(n×m)中:
     * C[i][j] = Σ(A[i][c] * B[c][j]) for c in 0..k-1
     * 
     * 时间复杂度: O(n×k×m)
     * 空间复杂度: O(n×m)
     * 
     * 注意事项:
     * - 矩阵乘法要求第一个矩阵的列数等于第二个矩阵的行数
     * - 矩阵乘法不满足交换律，即A*B ≠ B*A
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
     * 矩阵快速幂实现
     * 
     * 算法原理:
     * 利用二进制分解指数，通过不断平方和累积结果实现快速计算
     * 例如: A^13，13的二进制为1101
     * A^13 = A^8 * A^4 * A^1 (对应二进制位为1的位置)
     * 
     * 数学基础:
     * 单位矩阵I满足: I * A = A * I = A
     * 
     * 时间复杂度: O(n^3 * logp) - n为矩阵维度
     * 空间复杂度: O(n^2)
     * 
     * 实现技巧:
     * - 使用位运算优化指数分解
     * - 结果初始化为单位矩阵
     * 
     * @param m 底数矩阵（必须是方阵）
     * @param p 指数
     * @return 矩阵m的p次幂
     */
    // 矩阵快速幂
    // 要求矩阵m是正方形矩阵
    public static int[][] power(int[][] m, int p) {
        int n = m.length;  // 矩阵维度
        // 构造单位矩阵作为初始结果
        // 对角线全是1、剩下数字都是0的正方形矩阵，称为单位矩阵
        // 相当于正方形矩阵中的1，矩阵a * 单位矩阵 = 矩阵a
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

    /**
     * 打印二维矩阵
     * 
     * 功能: 格式化输出二维矩阵，便于观察结果
     * 
     * 实现细节:
     * - 根据数字大小调整输出格式，保证对齐
     * - 每行元素间用空格分隔
     * 
     * @param m 要打印的矩阵
     */
    // 打印二维矩阵
    public static void print(int[][] m) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                // 根据数字大小调整输出格式，保证对齐
                if (m[i][j] < 10) {
                    System.out.print(m[i][j] + "   ");
                } else if (m[i][j] < 100) {
                    System.out.print(m[i][j] + "  ");
                } else {
                    System.out.print(m[i][j] + " ");
                }
            }
            System.out.println();  // 每行结束后换行
        }
    }

    /**
     * 矩阵乘法演示
     * 
     * 演示不同维度矩阵的乘法运算:
     * 1. 2×2 矩阵乘以 2×2 矩阵
     * 2. 2×2 矩阵乘以 2×3 矩阵
     * 3. 3×2 矩阵乘以 2×2 矩阵
     * 4. 1×3 矩阵乘以 3×3 矩阵
     * 
     * 通过具体例子验证矩阵乘法的正确性
     */
    // 矩阵乘法的展示
    public static void f1() {
        int[][] a = { { 1, 3 }, { 4, 2 } };
        int[][] b = { { 2, 3 }, { 3, 2 } };
        //        2  3
        //        3  2
        //
        // 1  3  11  9
        // 4  2  14 16
        int[][] ans1 = multiply(a, b);
        print(ans1);
        System.out.println("======");
        int[][] c = { { 2, 4 }, { 3, 2 } };
        int[][] d = { { 2, 3, 2 }, { 3, 2, 3 } };
        //         2  3  2
        //         3  2  3
        //
        // 2  4   16 14 16
        // 3  2   12 13 12
        int[][] ans2 = multiply(c, d);
        print(ans2);
        System.out.println("======");
        int[][] e = { { 2, 4 }, { 1, 2 }, { 3, 1 } };
        int[][] f = { { 2, 3 }, { 4, 1 } };
        //          2  3
        //          4  1
        //
        // 2  4    20 10
        // 1  2    10  5
        // 3  1    10 10
        int[][] ans3 = multiply(e, f);
        print(ans3);
        System.out.println("======");
        int[][] g = { { 3, 1, 2 } };
        int[][] h = { { 1, 2, 1 }, { 3, 2, 1 }, { 4, 2, -2 } };
        //           1  2  1
        //           3  2  1
        //           4  2 -2
        //
        // 3  1  2  14 12  0
        int[][] ans4 = multiply(g, h);
        print(ans4);
    }
    
    /**
     * 矩阵快速幂演示
     * 
     * 对比普通矩阵连乘与矩阵快速幂的结果:
     * 1. 使用普通连乘计算矩阵的5次幂
     * 2. 使用矩阵快速幂计算矩阵的5次幂
     * 3. 验证两种方法结果一致
     * 
     * 通过对比展示矩阵快速幂的效率优势
     */
    // 矩阵快速幂用法的展示
    public static void f2() {
        // 只有正方形矩阵可以求幂
        int[][] a = { { 1, 2 }, { 3, 4 } };
        // 连乘得到矩阵a的5次方
        int[][] b = multiply(a, multiply(a, multiply(a, multiply(a, a))));
        print(b);
        System.out.println("======");
        // 矩阵快速幂得到矩阵a的5次方
        print(power(a, 5));
    }

    /**
     * 使用矩阵乘法解决斐波那契数列问题
     * 
     * 数学原理:
     * 斐波那契数列递推关系: F(n) = F(n-1) + F(n-2)
     * 矩阵表示形式:
     * [F(n)  ]   = [1 1] * [F(n-1)]
     * [F(n-1)]     [1 0]   [F(n-2)]
     * 
     * 通过逐步计算展示矩阵乘法如何计算斐波那契数列:
     * 1. 初始状态 [F(1), F(0)] = [1, 0]
     * 2. 乘以转移矩阵一次得到 [F(2), F(1)] = [1, 1]
     * 3. 乘以转移矩阵两次得到 [F(3), F(2)] = [2, 1]
     * 4. 乘以转移矩阵三次得到 [F(4), F(3)] = [3, 2]
     */
    // 用矩阵乘法解决斐波那契第n项的问题
    public static void f3() {
        // 0  1  1  2  3  5  8 13 21 34...
        // 0  1  2  3  4  5  6  7  8  9
        int[][] start = { { 1, 0 } };  // 初始状态向量 [F(1), F(0)]
        int[][] m = {
                { 1, 1 },  // 转移矩阵
                { 1, 0 }
                };
        int[][] a = multiply(start, m);
        //       1  1
        //       1  0
        //
        // 1  0  1  1  -> [F(2), F(1)]
        print(a);
        System.out.println("======");
        int[][] b = multiply(a, m);
        //       1  1
        //       1  0
        //
        // 1  1  2  1  -> [F(3), F(2)]
        print(b);
        System.out.println("======");
        int[][] c = multiply(b, m);
        //       1  1
        //       1  0
        //
        // 2  1  3  2  -> [F(4), F(3)]
        print(c);
        System.out.println("======");
        int[][] d = multiply(c, m);
        //       1  1
        //       1  0
        //
        // 3  2  5  3  -> [F(5), F(4)]
        print(d);
    }

    /**
     * 使用矩阵快速幂解决斐波那契数列问题
     * 
     * 数学原理:
     * 斐波那契数列递推关系: F(n) = F(n-1) + F(n-2)
     * 矩阵表示形式:
     * [F(n)  ]   = [1 1]^(n-1) * [F(1)]
     * [F(n-1)]     [1 0]         [F(0)]
     * 
     * 通过矩阵快速幂优化计算:
     * 1. 初始状态向量 [F(1), F(0)] = [1, 0]
     * 2. 转移矩阵 [[1,1],[1,0]]
     * 3. 计算转移矩阵的(n-1)次幂
     * 4. 初始向量乘以结果矩阵得到 [F(n), F(n-1)]
     * 
     * 时间复杂度: O(logn)
     * 空间复杂度: O(1)
     */
    // 用矩阵快速幂解决斐波那契第n项的问题
    public static void f4() {
        // 0  1  1  2  3  5  8 13 21 34...
        // 0  1  2  3  4  5  6  7  8  9
        int[][] start = { { 1, 0 } };  // 初始状态向量 [F(1), F(0)]
        int[][] m = {
                { 1, 1 },  // 转移矩阵
                { 1, 0 }
                };
        int[][] a = multiply(start, power(m, 1));
        print(a);  // 计算F(2)
        System.out.println("======");
        int[][] b = multiply(start, power(m, 2));
        print(b);  // 计算F(3)
        System.out.println("======");
        int[][] c = multiply(start, power(m, 3));
        print(c);  // 计算F(4)
        System.out.println("======");
        int[][] d = multiply(start, power(m, 4));
        print(d);  // 计算F(5)
    }

}