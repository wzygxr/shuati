package class135;

/**
 * Code12_FaceTheRightWay - 高斯消元法应用
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


// POJ 3276 Face The Right Way
// 有一排牛，有的面朝前，有的面朝后，每次可以选K头连续的牛翻转方向
// 求最少的操作次数以及对应的K值
// 测试链接 : http://poj.org/problem?id=3276

/*
 * 题目解析:
 * 这是一个开关问题，可以用贪心+枚举的方法解决。
 * 
 * 解题思路:
 * 1. 枚举所有可能的K值（1到N）
 * 2. 对于每个K值，使用贪心策略从左到右处理
 * 3. 如果当前牛面朝后，则必须翻转以它为起点的K头牛
 * 4. 记录最少操作次数及对应的K值
 * 
 * 时间复杂度: O(n^2)
 * 空间复杂度: O(n)
 * 
 * 工程化考虑:
 * 1. 正确实现贪心策略
 * 2. 优化枚举过程
 * 3. 输入输出处理
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code12_FaceTheRightWay {

    public static int MAXN = 5005;

    public static char[] cows = new char[MAXN];
    public static int[] flip = new int[MAXN]; // 记录每个位置是否翻转

    public static int n;

    /*
     * 计算使用K头牛翻转时的最少操作次数
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     */
    public static int calculate(int k) {
        // 初始化
        for (int i = 0; i <= n; i++) {
            flip[i] = 0;
        }

        int res = 0; // 操作次数
        int sum = 0; // 当前位置的翻转次数

        for (int i = 0; i <= n - k; i++) {
            // 更新当前位置的翻转次数
            sum += flip[i];
            
            // 如果当前牛面朝后，则需要翻转
            if ((sum % 2 == 0 && cows[i] == 'B') || (sum % 2 == 1 && cows[i] == 'F')) {
                res++;
                flip[i] = 1; // 标记在位置i进行翻转
                sum++; // 更新翻转次数
            }
            
            // 移除超出窗口的翻转影响
            if (i - k + 1 >= 0) {
                sum -= flip[i - k + 1];
            }
        }

        // 检查最后K-1头牛是否都面朝前
        for (int i = n - k + 1; i < n; i++) {
            sum += flip[i];
            if ((sum % 2 == 0 && cows[i] == 'B') || (sum % 2 == 1 && cows[i] == 'F')) {
                return -1; // 无解
            }
            if (i - k + 1 >= 0) {
                sum -= flip[i - k + 1];
            }
        }

        return res;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

        in.nextToken();
        n = (int) in.nval;

        // 读取牛的方向
        String line = br.readLine();
        for (int i = 0; i < n; i++) {
            cows[i] = line.charAt(i);
        }

        int minFlips = n + 1;
        int bestK = 1;

        // 枚举所有可能的K值
        for (int k = 1; k <= n; k++) {
            int flips = calculate(k);
            if (flips != -1 && flips < minFlips) {
                minFlips = flips;
                bestK = k;
            }
        }

        // 输出结果
        out.println(bestK + " " + minFlips);

        out.flush();
        out.close();
        br.close();
    }
}