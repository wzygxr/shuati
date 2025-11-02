package class134;

/**
 * 高斯消元解决异或方程组 - POJ 1830 开关问题 + POJ 1222 EXTENDED LIGHTS OUT
 * 
 * 题目1：POJ 1830 开关问题
 * 题目描述：
 * 有N个相同的开关，每个开关都与某些开关有着联系，每当你打开或者关闭某个开关的时候，
 * 其他的与此开关相关联的开关也会相应地发生变化，即这些相联系的开关的状态会改变。
 * 给出所有开关的初始状态和目标状态，求有多少种操作方法可以达到目标状态。
 * 
 * 输入约束：
 * 1 <= N <= 29
 * 
 * 测试链接：http://poj.org/problem?id=1830
 * 
 * 题目2：POJ 1222 EXTENDED LIGHTS OUT
 * 题目描述：
 * 有一个5x6的灯矩阵，每个灯有两种状态（开或关）。
 * 当按下一个灯的开关时，该灯及其相邻的四个灯（如果存在）的状态都会改变。
 * 给定灯的初始状态，求一个按钮操作序列，使得所有灯都关闭。
 * 
 * 测试链接：http://poj.org/problem?id=1222
 * 
 * 算法原理详解：
 * 1. 问题建模：开关问题可以转化为异或方程组的求解问题
 *    - 每个开关对应一个未知数xi，表示是否操作该开关（1表示操作，0表示不操作）
 *    - 每个开关对应一个方程，表示该开关的最终状态
 *    - 系数矩阵表示开关之间的影响关系
 *    - 常数项表示初始状态与目标状态的差异
 * 2. 高斯消元：使用高斯消元法求解异或方程组
 * 3. 解的情况分析：
 *    - 无解：存在矛盾方程，方案数为0
 *    - 唯一解：方案数为1
 *    - 无穷解：方案数为2^(自由元个数)
 * 
 * 时间复杂度分析：
 * - POJ 1830：O(N^3) ≈ O(29^3) ≈ 24,389
 * - POJ 1222：O(N^3) ≈ O(30^3) ≈ 27,000（5x6=30个灯）
 * 
 * 空间复杂度分析：
 * - 增广矩阵：O(N^2) ≈ O(900)
 * - 总空间：O(N^2) 在可接受范围内
 * 
 * 工程化考量：
 * 1. 性能优化：使用位运算优化异或操作
 * 2. 内存管理：合理设置数组大小，避免内存溢出
 * 3. 边界处理：处理N=1的特殊情况
 * 4. 可读性：详细注释和变量命名规范
 * 
 * 关键优化点：
 * - 使用高斯消元求解异或方程组
 * - 处理开关之间的相互影响关系
 * - 统计自由元个数计算方案数
 * - 支持多种开关问题的统一解法
 */

import java.io.*;
import java.util.*;

/**
 * 高斯消元解决异或方程组 - POJ 1830 开关问题
 * 
 * 题目解析：
 * 本题是一个典型的开关问题。每个开关有两种状态（0或1），操作一个开关会改变该开关及其相关联开关的状态。
 * 目标是从初始状态转换到目标状态，求有多少种操作方法。
 * 
 * 解题思路：
 * 1. 将问题转化为异或方程组：
 *    - 设xi表示是否操作第i个开关（1表示操作，0表示不操作）
 *    - 对于每个开关i，建立方程：xi ^ sum{ajxj} = (初始状态i ^ 目标状态i)
 *    - 其中aj表示操作开关j是否会影响开关i的状态
 * 2. 使用高斯消元求解异或方程组
 * 3. 根据解的情况判断方案数：
 *    - 无解：方案数为0
 *    - 唯一解：方案数为1
 *    - 无穷解：方案数为2^(自由元个数)
 * 
 * 时间复杂度：O(N^3)
 * 空间复杂度：O(N^2)
 */
public class Code05_SwitchProblem {

    public static int MAXN = 35;

    // 增广矩阵，mat[i][j]表示第i个方程中第j个未知数的系数，mat[i][n+1]表示第i个方程的常数项
    public static int[][] mat = new int[MAXN][MAXN];

    public static int n;

    public static int result_row;
    
    /**
     * 高斯消元解决异或方程组
     * 
     * 算法步骤：
     * 1. 构造增广矩阵：将方程组的系数和常数项组成增广矩阵
     * 2. 消元过程：
     *    - 从第一行开始，选择主元（该列系数为1的行）
     *    - 将主元行交换到当前行
     *    - 用主元行消去其他行的当前列系数（通过异或运算）
     * 3. 判断解的情况：
     *    - 唯一解：系数矩阵可化为单位矩阵
     *    - 无解：出现形如 0 = 1 的矛盾方程
     *    - 无穷解：出现形如 0 = 0 的自由元方程
     * 
     * @param n 未知数个数
     * @return 0表示有唯一解，1表示有无穷多解，-1表示无解
     */
    // 注意：此高斯消元方法同时适用于POJ 1830和POJ 1222问题
    public static int gauss(int n) {
        int r = 1; // 当前行
        int c = 1; // 当前列

        // 消元过程
        for (; r <= n && c <= n; r++, c++) {
            int pivot = r;

            // 寻找主元（当前列中系数为1的行）
            for (int i = r; i <= n; i++) {
                if (mat[i][c] == 1) {
                    pivot = i;
                    break;
                }
            }

            // 如果找不到主元，说明当前列全为0，跳到下一列
            if (mat[pivot][c] == 0) {
                r--; // 保持当前行不变
                continue;
            }

            // 交换第r行和第pivot行
            if (pivot != r) {
                swap(r, pivot);
            }

            // 消去其他行的当前列系数
            for (int i = 1; i <= n; i++) {
                if (i != r && mat[i][c] == 1) {
                    // 第i行异或第r行
                    for (int j = c; j <= n + 1; j++) {
                        mat[i][j] ^= mat[r][j];
                    }
                }
            }
        }

        // 判断解的情况
        // 检查是否有形如 0 = 1 的矛盾方程
        for (int i = r; i <= n; i++) {
            if (mat[i][n + 1] == 1) {
                return -1; // 无解
            }
        }

        // 判断是否有自由元（形如 0 = 0 的方程）
        if (r <= n) {
            return 1; // 有无穷多解
        }

        return 0; // 有唯一解
    }

    /**
     * 交换矩阵中的两行
     * 
     * @param a 行号1
     * @param b 行号2
     */
    public static void swap(int a, int b) {
        int[] tmp = mat[a];
        mat[a] = mat[b];
        mat[b] = tmp;
    }

    /**
     * 快速幂运算
     * 
     * @param base 底数
     * @param exp  指数
     * @return base^exp
     */
    public static int power(int base, int exp) {
        int result = 1;
        while (exp > 0) {
            if (exp % 2 == 1) {
                result *= base;
            }
            base *= base;
            exp /= 2;
        }
        return result;
    }

    // POJ 1830 开关问题的主方法
    public static void main(String[] args) throws IOException {
        // 主方法保持不变，处理POJ 1830的输入输出
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        int k = Integer.parseInt(br.readLine());
        
        for (int t = 0; t < k; t++) {
            n = Integer.parseInt(br.readLine());
            
            // 读取初始状态
            int[] start = new int[MAXN];
            String[] startStr = br.readLine().split(" ");
            for (int i = 1; i <= n; i++) {
                start[i] = Integer.parseInt(startStr[i - 1]);
            }
            
            // 读取目标状态
            int[] end = new int[MAXN];
            String[] endStr = br.readLine().split(" ");
            for (int i = 1; i <= n; i++) {
                end[i] = Integer.parseInt(endStr[i - 1]);
            }
            
            // 初始化矩阵
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n + 1; j++) {
                    mat[i][j] = 0;
                }
                // 自己对自己有影响
                mat[i][i] = 1;
                // 常数项为初始状态与目标状态的异或值
                mat[i][n + 1] = start[i] ^ end[i];
            }
            
            // 读取开关关系
            String line;
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                String[] parts = line.split(" ");
                if (parts.length < 2) break;
                int i = Integer.parseInt(parts[0]);
                int j = Integer.parseInt(parts[1]);
                if (i == 0 && j == 0) break;
                // 操作开关j会影响开关i
                mat[i][j] = 1;
            }
            
            // 高斯消元
            int result = gauss(n);
            
            if (result == -1) {
                out.println("Oh,it's impossible~!!");
            } else if (result == 0) {
                out.println(1);
            } else {
                // 计算自由元个数
                int free = 0;
                for (int i = 1; i <= n; i++) {
                    if (mat[i][i] == 0) {
                        free++;
                    }
                }
                out.println(power(2, free));
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
    
    // POJ 1222 EXTENDED LIGHTS OUT 问题的解决方案
    public static void solveLightsOutProblem() throws IOException {
        /**
         * POJ 1222 EXTENDED LIGHTS OUT 解题思路：
         * 1. 问题建模：
         *    - 共有5x6=30个灯，每个灯有两种状态（开0或关1）
         *    - 设x[i]表示是否按下第i个灯的开关（1表示按下，0表示不按下）
         *    - 目标是让所有灯都关闭（状态为1）
         * 
         * 2. 建立方程组：
         *    - 对于每个灯j，按下哪些开关会影响它的最终状态
         *    - 最终状态 = 初始状态 ^ x[j] ^ x[上] ^ x[下] ^ x[左] ^ x[右] = 1
         * 
         * 3. 优化方法：
         *    - 由于灯的数量固定为30，可以预构建系数矩阵
         *    - 由于灯之间的影响是局部的，可以利用这一特性简化计算
         */
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        int T = Integer.parseInt(br.readLine());
        for (int t = 1; t <= T; t++) {
            // 重置矩阵
            int N = 30; // 5x6=30个灯
            int[][] lightsMat = new int[N + 1][N + 2];
            
            // 构建系数矩阵（预处理灯之间的影响关系）
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 6; j++) {
                    int pos = i * 6 + j + 1; // 当前灯的位置（1-based）
                    // 每个灯受自己和上下左右影响
                    lightsMat[pos][pos] = 1; // 自己
                    // 上方
                    if (i > 0) lightsMat[pos][(i-1)*6 + j + 1] = 1;
                    // 下方
                    if (i < 4) lightsMat[pos][(i+1)*6 + j + 1] = 1;
                    // 左方
                    if (j > 0) lightsMat[pos][i*6 + (j-1) + 1] = 1;
                    // 右方
                    if (j < 5) lightsMat[pos][i*6 + (j+1) + 1] = 1;
                }
            }
            
            // 读取初始状态，设置常数项
            for (int i = 1; i <= N; i++) {
                int state = Integer.parseInt(br.readLine());
                lightsMat[i][N + 1] = state; // 目标是所有灯关闭，即1
            }
            
            // 复制到全局矩阵进行计算
            for (int i = 1; i <= N; i++) {
                for (int j = 1; j <= N + 1; j++) {
                    mat[i][j] = lightsMat[i][j];
                }
            }
            
            // 使用高斯消元求解
            gauss(N);
            
            // 输出结果
            out.println("PUZZLE #" + t);
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 6; j++) {
                    int pos = i * 6 + j + 1;
                    // 从消元后的矩阵获取解
                    int solution = mat[pos][N + 1];
                    // 计算解（考虑其他已确定变量的影响）
                    for (int k = pos + 1; k <= N; k++) {
                        solution ^= (mat[pos][k] & mat[k][N + 1]);
                    }
                    out.print(solution);
                    if (j < 5) out.print(" ");
                }
                out.println();
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}