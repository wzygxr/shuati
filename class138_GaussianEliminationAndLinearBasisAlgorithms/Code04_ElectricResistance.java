package class135;

/**
 * Code04_ElectricResistance - 高斯消元法应用
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


// HDU 3976 Electric resistance
// 给定一个由n个节点和m个电阻组成的电路，求节点1和节点n之间的等效电阻
// 测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=3976

/*
 * 题目解析:
 * 这是一个使用高斯消元解决电路问题的经典题目。
 * 利用基尔霍夫电流定律和欧姆定律建立线性方程组求解。
 * 
 * 解题思路:
 * 1. 以每个节点的电势为未知数
 * 2. 根据基尔霍夫电流定律（流入电流等于流出电流）建立方程
 * 3. 根据欧姆定律 I = (Ux - Uy) / R 建立电流与电势的关系
 * 4. 设节点1电势为1，节点n电势为0，建立线性方程组
 * 5. 使用高斯消元求解线性方程组
 * 6. 等效电阻 = (节点1电势 - 节点n电势) / 总电流
 * 
 * 时间复杂度: O(n^3)
 * 空间复杂度: O(n^2)
 * 
 * 工程化考虑:
 * 1. 正确处理浮点数运算精度
 * 2. 特殊处理节点1和节点n的方程
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

public class Code04_ElectricResistance {

    static class Edge {
        int to;
        double r; // 电阻值

        Edge(int to, double r) {
            this.to = to;
            this.r = r;
        }
    }

    public static int MAXN = 51;
    public static double EPS = 1e-10; // 浮点数精度

    // 邻接表存储图
    public static List<Edge>[] graph = new List[MAXN];
    
    // 增广矩阵
    public static double[][] mat = new double[MAXN][MAXN];
    
    public static int n, m;

    /*
     * 初始化图
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     */
    public static void initGraph() {
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
    }

    /*
     * 添加边
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     */
    public static void addEdge(int u, int v, double r) {
        graph[u].add(new Edge(v, r));
        graph[v].add(new Edge(u, r));
    }

    /*
     * 建立方程组
     * 时间复杂度: O(n + m)
     * 空间复杂度: O(n^2)
     */
    public static void buildEquations() {
        // 初始化矩阵
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n + 1; j++) {
                mat[i][j] = 0.0;
            }
        }

        // 对于每个节点建立方程
        for (int i = 1; i <= n; i++) {
            // 特殊处理节点1和节点n
            if (i == 1 || i == n) {
                // 节点1设电势为1，节点n设电势为0
                mat[i][i] = 1.0;
                if (i == 1) {
                    mat[i][n + 1] = 1.0;
                } else {
                    mat[i][n + 1] = 0.0;
                }
                continue;
            }

            // 对于其他节点，根据基尔霍夫电流定律建立方程
            // 流入电流之和等于流出电流之和，即总和为0
            for (Edge e : graph[i]) {
                int j = e.to;
                double r = e.r;
                // 系数为 1/R
                mat[i][j] += 1.0 / r;
                // 对角线系数为 -Σ(1/R)
                mat[i][i] -= 1.0 / r;
            }
        }
    }

    /*
     * 高斯消元解决浮点数线性方程组
     * 时间复杂度: O(n^3)
     * 空间复杂度: O(n^2)
     */
    public static void gauss() {
        for (int i = 1; i <= n; i++) {
            // 找到第i列系数绝对值最大的行
            int maxRow = i;
            for (int j = i + 1; j <= n; j++) {
                if (Math.abs(mat[j][i]) > Math.abs(mat[maxRow][i])) {
                    maxRow = j;
                }
            }

            // 交换行
            if (maxRow != i) {
                for (int k = 1; k <= n + 1; k++) {
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
            for (int k = i; k <= n + 1; k++) {
                mat[i][k] /= pivot;
            }

            // 消去其他行的第i列系数
            for (int j = 1; j <= n; j++) {
                if (i != j && Math.abs(mat[j][i]) > EPS) {
                    double factor = mat[j][i];
                    for (int k = i; k <= n + 1; k++) {
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

        int cases = 1;
        while (true) {
            try {
                in.nextToken();
                n = (int) in.nval;
                in.nextToken();
                m = (int) in.nval;
            } catch (Exception e) {
                break;
            }

            // 初始化图
            initGraph();

            // 读取边信息
            for (int i = 0; i < m; i++) {
                in.nextToken();
                int u = (int) in.nval;
                in.nextToken();
                int v = (int) in.nval;
                in.nextToken();
                double r = in.nval;
                addEdge(u, v, r);
            }

            // 建立方程组
            buildEquations();

            // 高斯消元求解
            gauss();

            // 计算等效电阻
            // 总电流 = Σ((节点1电势 - 相邻节点电势) / 电阻)
            double totalCurrent = 0.0;
            for (Edge e : graph[1]) {
                int v = e.to;
                double r = e.r;
                totalCurrent += (mat[1][n + 1] - mat[v][n + 1]) / r;
            }

            // 等效电阻 = 电压 / 电流 = 1.0 / totalCurrent
            double equivalentResistance = 1.0 / totalCurrent;
            
            out.printf("Case #%d: %.2f\n", cases++, equivalentResistance);
        }

        out.flush();
        out.close();
        br.close();
    }
}