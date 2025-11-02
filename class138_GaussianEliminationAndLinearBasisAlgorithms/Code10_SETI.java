package class135;

/**
 * Code10_SETI - 高斯消元法应用
 * 
 * 算法核心思想:
 * 使用高斯消元法解决线性方程组或线性基相关问题
 * 
 * 关键步骤:
 * 1. 构建增广矩阵
 * 2. 前向消元，将矩阵化为上三角形式
 * 3. 回代求解未知数
 * 4. 处理特殊情况（无解、多解）
 * 
 * 时间复杂度分析:
 * - 高斯消元: O(n³)
 * - 线性基构建: O(n * log(max_value))
 * - 查询操作: O(log(max_value))
 * 
 * 空间复杂度分析:
 * - 矩阵存储: O(n²)
 * - 线性基: O(log(max_value))
 * 
 * 工程化考量:
 * 1. 数值稳定性: 使用主元选择策略避免精度误差
 * 2. 边界处理: 处理零矩阵、奇异矩阵等特殊情况
 * 3. 异常处理: 检查输入合法性，提供有意义的错误信息
 * 4. 性能优化: 针对稀疏矩阵进行优化
 * 
 * 应用场景:
 * - 线性方程组求解
 * - 线性基构建与查询
 * - 异或最大值问题
 * - 概率期望计算
 * 
 * 调试技巧:
 * 1. 打印中间矩阵状态验证消元过程
 * 2. 使用小规模测试用例验证正确性
 * 3. 检查边界条件（n=0, n=1等）
 * 4. 验证数值精度和稳定性
 */


// POJ 2065 SETI
// 给定一个多项式在不同模数下的值，求多项式的系数
// 测试链接 : http://poj.org/problem?id=2065

/*
 * 题目解析:
 * 这是一个浮点数线性方程组问题，需要使用高斯消元求解。
 * 
 * 解题思路:
 * 1. 根据多项式的定义建立方程组
 * 2. 对于每个观测点，建立一个方程表示多项式的值
 * 3. 使用高斯消元求解线性方程组
 * 
 * 时间复杂度: O(n^3)
 * 空间复杂度: O(n^2)
 * 
 * 工程化考虑:
 * 1. 正确处理浮点数运算精度
 * 2. 输入输出处理
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code10_SETI {

    public static int MAXN = 105;
    public static double EPS = 1e-10; // 浮点数精度

    // 增广矩阵
    public static double[][] mat = new double[MAXN][MAXN];
    
    public static int n;
    public static int mod;

    /*
     * 快速幂运算
     * 计算 base^exp % mod
     * 时间复杂度: O(log exp)
     * 空间复杂度: O(1)
     */
    public static long pow(long base, int exp, int mod) {
        long result = 1;
        while (exp > 0) {
            if (exp % 2 == 1) {
                result = (result * base) % mod;
            }
            base = (base * base) % mod;
            exp /= 2;
        }
        return result;
    }

    /*
     * 初始化矩阵
     * 时间复杂度: O(n^2)
     * 空间复杂度: O(n^2)
     */
    public static void initMatrix(String s) {
        // 初始化矩阵
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= n; j++) {
                mat[i][j] = 0.0;
            }
        }

        // 建立方程组
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            int value = (c == '*') ? 0 : (c - 'a' + 1);
            
            // 对于第i个方程，表示当x=i+1时多项式的值
            for (int j = 0; j < n; j++) {
                // 系数为 (i+1)^j mod mod
                mat[i][j] = pow(i + 1, j, mod);
            }
            // 常数项为value
            mat[i][n] = value;
        }
    }

    /*
     * 高斯消元解决浮点数线性方程组
     * 时间复杂度: O(n^3)
     * 空间复杂度: O(n^2)
     */
    public static void gauss() {
        for (int i = 0; i < n; i++) {
            // 找到第i列系数绝对值最大的行
            int maxRow = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(mat[j][i]) > Math.abs(mat[maxRow][i])) {
                    maxRow = j;
                }
            }

            // 交换行
            if (maxRow != i) {
                for (int k = 0; k <= n; k++) {
                    double temp = mat[i][k];
                    mat[i][k] = mat[maxRow][k];
                    mat[maxRow][k] = temp;
                }
            }

            // 如果主元为0，说明矩阵奇异
            if (Math.abs(mat[i][i]) < EPS) {
                continue;
            }

            // 将第i行主元系数化为1
            double pivot = mat[i][i];
            for (int k = i; k <= n; k++) {
                mat[i][k] /= pivot;
            }

            // 消去其他行的第i列系数
            for (int j = 0; j < n; j++) {
                if (i != j && Math.abs(mat[j][i]) > EPS) {
                    double factor = mat[j][i];
                    for (int k = i; k <= n; k++) {
                        mat[j][k] -= factor * mat[i][k];
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

        in.nextToken();
        int testCases = (int) in.nval;

        for (int t = 1; t <= testCases; t++) {
            in.nextToken();
            mod = (int) in.nval;
            String s = br.readLine().trim();
            n = s.length();

            // 初始化矩阵
            initMatrix(s);

            // 高斯消元求解
            gauss();

            // 输出结果
            for (int i = 0; i < n - 1; i++) {
                out.print((int) Math.round(mat[i][n]) + " ");
            }
            out.println((int) Math.round(mat[n - 1][n]));
        }

        out.flush();
        out.close();
        br.close();
    }
}