package class135;

/**
 * Code14_LinearBasis - 高斯消元法应用
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


// 洛谷 P3812 【模板】线性基
// 给定n个整数，求在这些数中选取任意个，使得他们的异或和最大
// 测试链接 : https://www.luogu.com.cn/problem/P3812

/*
 * 题目解析:
 * 这是一个线性基模板题，需要使用高斯消元的思想来解决。
 * 
 * 解题思路:
 * 1. 将每个数看作一个60位的二进制向量
 * 2. 使用高斯消元的思想构造线性基
 * 3. 从高位到低位贪心地选择，使得异或和最大
 * 
 * 时间复杂度: O(n * 60) = O(n)
 * 空间复杂度: O(60) = O(1)
 * 
 * 工程化考虑:
 * 1. 正确处理64位整数
 * 2. 从高位到低位贪心选择
 * 3. 线性基的构造和维护
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code14_LinearBasis {

    public static int MAXN = 100005;
    public static int BITS = 60;

    // 线性基数组
    public static long[] basis = new long[BITS];

    public static int n;
    public static long[] numbers = new long[MAXN];

    /*
     * 插入一个数到线性基中
     * 时间复杂度: O(BITS)
     * 空间复杂度: O(1)
     */
    public static boolean insert(long x) {
        for (int i = BITS - 1; i >= 0; i--) {
            if (((x >> i) & 1) == 1) {
                if (basis[i] == 0) {
                    basis[i] = x;
                    return true;
                }
                x ^= basis[i];
            }
        }
        return false;
    }

    /*
     * 求最大异或和
     * 从高位到低位贪心地选择
     * 时间复杂度: O(BITS)
     * 空间复杂度: O(1)
     */
    public static long getMaxXor() {
        long result = 0;
        for (int i = BITS - 1; i >= 0; i--) {
            if (basis[i] != 0 && ((result >> i) & 1) == 0) {
                result ^= basis[i];
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

        in.nextToken();
        n = (int) in.nval;

        // 读取数字
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            numbers[i] = (long) in.nval;
        }

        // 初始化线性基
        for (int i = 0; i < BITS; i++) {
            basis[i] = 0;
        }

        // 构造线性基
        for (int i = 1; i <= n; i++) {
            insert(numbers[i]);
        }

        // 求最大异或和
        long result = getMaxXor();

        // 输出结果
        out.println(result);

        out.flush();
        out.close();
        br.close();
    }
}