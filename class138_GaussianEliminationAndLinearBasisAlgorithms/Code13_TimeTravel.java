package class135;

/**
 * Code13_TimeTravel - 高斯消元法应用
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


// HDU 4418 Time travel
// 在一个有向图中，从起点到终点的期望步数
// 测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=4418

/*
 * 题目解析:
 * 这是一个期望DP问题，需要使用高斯消元求解线性方程组。
 * 
 * 解题思路:
 * 1. 将图转换为状态转移方程
 * 2. 建立线性方程组表示期望步数
 * 3. 使用高斯消元求解线性方程组
 * 
 * 时间复杂度: O(n^3)
 * 空间复杂度: O(n^2)
 * 
 * 工程化考虑:
 * 1. 正确处理期望DP的状态转移
 * 2. 特殊处理终点状态
 * 3. 输入输出处理
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

public class Code13_TimeTravel {

    public static int MAXN = 205;
    public static double EPS = 1e-10; // 浮点数精度

    // 邻接表存储图
    public static List<Integer>[] graph = new List[MAXN];
    
    // 增广矩阵
    public static double[][] mat = new double[MAXN][MAXN];
    
    public static int n, m, start, end, d;
    public static int[] prob = new int[MAXN];

    /*
     * 初始化图
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     */
    public static void initGraph() {
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
    }

    /*
     * 建立方程组
     * 时间复杂度: O(n^2)
     * 空间复杂度: O(n^2)
     */
    public static void buildEquations() {
        // 初始化矩阵
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= n; j++) {
                mat[i][j] = 0.0;
            }
        }

        // 对于每个状态建立方程
        for (int i = 0; i < n; i++) {
            // 特殊处理终点状态
            if (i == end) {
                mat[i][i] = 1.0;
                mat[i][n] = 0.0;
                continue;
            }

            // 对于其他状态，根据状态转移建立方程
            mat[i][i] = 1.0; // 自身系数为1
            double totalProb = 0.0;
            
            // 遍历所有可能的转移
            for (int j = 1; j <= d; j++) {
                if (prob[j] > 0) {
                    int next = (i + j) % n;
                    mat[i][next] -= prob[j] / 100.0; // 转移概率
                    totalProb += prob[j] / 100.0;
                }
            }
            
            // 常数项为1加上各项转移概率的期望
            mat[i][n] = 1.0 + totalProb;
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
            n = (int) in.nval;
            in.nextToken();
            m = (int) in.nval;
            in.nextToken();
            start = (int) in.nval;
            in.nextToken();
            end = (int) in.nval;
            in.nextToken();
            d = (int) in.nval;

            // 初始化图
            initGraph();

            // 读取概率分布
            for (int i = 1; i <= d; i++) {
                in.nextToken();
                prob[i] = (int) in.nval;
            }

            // 建立方程组
            buildEquations();

            // 高斯消元求解
            gauss();

            // 输出结果
            if (Math.abs(mat[start][start] - 1.0) < EPS) {
                out.println("Impossible !");
            } else {
                out.printf("%.2f\n", mat[start][n]);
            }
        }

        out.flush();
        out.close();
        br.close();
    }
}