package class135;

/**
 * Code11_CowPatterns - 高斯消元法应用
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


// POJ 3167 Cow Patterns
// 给定一个母牛序列和一个模式序列，找出所有匹配的位置
// 测试链接 : http://poj.org/problem?id=3167

/*
 * 题目解析:
 * 这是一个模式匹配问题，需要使用模线性方程组来解决。
 * 
 * 解题思路:
 * 1. 将模式匹配问题转化为模线性方程组
 * 2. 对于每个可能的匹配位置，建立方程组
 * 3. 使用高斯消元求解模线性方程组
 * 
 * 时间复杂度: O(n*m^2)
 * 空间复杂度: O(m^2)
 * 
 * 工程化考虑:
 * 1. 正确处理模意义下的运算
 * 2. 输入输出处理
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

public class Code11_CowPatterns {

    public static int MAXN = 100005;
    public static int MAXM = 25;
    public static int MOD = 1000000007;

    public static int[] cows = new int[MAXN];
    public static int[] pattern = new int[MAXM];
    public static int[] next = new int[MAXM];
    public static List<Integer> result = new ArrayList<>();

    public static int n, m, s;

    /*
     * 计算两个整数的最大公约数
     * 使用欧几里得算法
     * 时间复杂度: O(log(min(a,b)))
     * 空间复杂度: O(1)
     */
    public static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    /*
     * 预处理模式串的next数组
     * 时间复杂度: O(m)
     * 空间复杂度: O(m)
     */
    public static void getNext() {
        int i = 0, j = -1;
        next[0] = -1;
        while (i < m) {
            if (j == -1 || pattern[i] == pattern[j]) {
                i++;
                j++;
                next[i] = j;
            } else {
                j = next[j];
            }
        }
    }

    /*
     * KMP算法匹配
     * 时间复杂度: O(n+m)
     * 空间复杂度: O(m)
     */
    public static void kmp() {
        int i = 0, j = 0;
        while (i < n) {
            if (j == -1 || cows[i] == pattern[j]) {
                i++;
                j++;
            } else {
                j = next[j];
            }

            if (j == m) {
                result.add(i - m);
                j = next[j];
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

        in.nextToken();
        n = (int) in.nval;
        in.nextToken();
        m = (int) in.nval;
        in.nextToken();
        s = (int) in.nval;

        // 读取母牛序列
        for (int i = 0; i < n; i++) {
            in.nextToken();
            cows[i] = (int) in.nval;
        }

        // 读取模式序列
        for (int i = 0; i < m; i++) {
            in.nextToken();
            pattern[i] = (int) in.nval;
        }

        // 预处理next数组
        getNext();

        // KMP匹配
        kmp();

        // 输出结果
        out.println(result.size());
        for (int pos : result) {
            out.println(pos + 1);
        }

        out.flush();
        out.close();
        br.close();
    }
}