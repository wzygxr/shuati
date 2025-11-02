package class138;

/**
 * 最小密度路径问题 - 01分数规划解法
 * 
 * <h3>题目信息</h3>
 * <ul>
 *   <li><strong>题目来源</strong>：Luogu P1730</li>
 *   <li><strong>题目描述</strong>：给定一个有向图，每条边有两个权值a[i]和b[i]。
 *   定义路径密度为路径上所有a[i]的和除以所有b[i]的和。要求找到所有简单路径中密度最小的值。</li>
 *   <li><strong>数据范围</strong>：
 *     <ul>
 *       <li>1 <= n <= 50（节点数）</li>
 *       <li>1 <= m <= 300（边数）</li>
 *       <li>0 <= a[i], b[i] <= 100000</li>
 *       <li>b[i] > 0（确保分母不为零）</li>
 *     </ul>
 *   </li>
 *   <li><strong>测试链接</strong>：<a href="https://www.luogu.com.cn/problem/P1730">Luogu P1730</a></li>
 * </ul>
 * 
 * <h3>算法思路</h3>
 * <p>使用Floyd变形算法直接计算所有点对之间的最小密度路径：</p>
 * <ol>
 *   <li><strong>Floyd变形</strong>：在标准的Floyd算法基础上，维护路径的a值和与b值和</li>
 *   <li><strong>动态规划状态</strong>：f[i][j][k]表示从i到j，只经过编号不超过k的点的最小密度</li>
 *   <li><strong>状态转移</strong>：通过中间点k更新路径密度</li>
 * </ol>
 * 
 * <h3>数学原理</h3>
 * <p>我们需要找到所有简单路径中密度最小的路径，即最小化 R = (Σa[i]) / (Σb[i])。</p>
 * <p>由于数据规模较小（n <= 50），可以直接使用Floyd算法计算所有点对之间的最小密度路径。</p>
 * 
 * <h3>复杂度分析</h3>
 * <ul>
 *   <li><strong>时间复杂度</strong>：O(n³)，标准的Floyd算法复杂度</li>
 *   <li><strong>空间复杂度</strong>：O(n³)，需要存储三维DP数组</li>
 * </ul>
 * 
 * <h3>注意事项</h3>
 * <ul>
 *   <li>由于n较小，可以直接使用O(n³)的算法</li>
 *   <li>需要处理浮点数精度问题</li>
 *   <li>确保分母b[i]不为零</li>
 * </ul>
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code07_MinimumDensityPath {

    // 常量定义
    public static final int MAXN = 51;   // 最大节点数
    public static final int MAXM = 301;  // 最大边数
    public static final double INF = 1e20; // 无穷大值，用于初始化
    public static final double PRECISION = 1e-9; // 精度控制
    
    // 链式前向星存储图结构
    public static int[] head = new int[MAXN];  // 每个节点的第一条边
    public static int[] next = new int[MAXM];  // 每条边的下一条边
    public static int[] to = new int[MAXM];    // 每条边的目标节点
    public static int[] a = new int[MAXM];     // 每条边的a权值
    public static int[] b = new int[MAXM];     // 每条边的b权值
    public static int cnt;  // 边的计数器
    
    /**
     * Floyd变形算法的DP数组
     * f[i][j][k]：从节点i到节点j，只经过编号不超过k的节点的最小密度
     * sumA[i][j][k]：对应路径的a权值和
     * sumB[i][j][k]：对应路径的b权值和
     */
    public static double[][][] f = new double[MAXN][MAXN][MAXN];
    public static double[][][] sumA = new double[MAXN][MAXN][MAXN];
    public static double[][][] sumB = new double[MAXN][MAXN][MAXN];
    
    // 全局变量，存储节点数和边数
    public static int n, m;
    
    /**
     * 初始化图的存储结构和Floyd数组
     */
    public static void prepare() {
        cnt = 1;
        Arrays.fill(head, 1, n + 1, 0);
        
        // 初始化Floyd数组为无穷大
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                for (int k = 0; k <= n; k++) {
                    f[i][j][k] = INF;
                    sumA[i][j][k] = 0;
                    sumB[i][j][k] = 0;
                }
            }
        }
        
        // 初始化直接相连的边
        for (int e = 1; e < cnt; e++) {
            int u = to[e ^ 1]; // 反向边（获取起点）
            int v = to[e];     // 正向边（获取终点）
            // 直接边的密度 = a[e] / b[e]
            f[u][v][0] = (double) a[e] / b[e];
            sumA[u][v][0] = a[e];
            sumB[u][v][0] = b[e];
        }
    }
    
    /**
     * 向图中添加一条无向边（实际存储为两条有向边）
     * 
     * @param u 边的起点
     * @param v 边的终点
     * @param aa 边的a权值
     * @param bb 边的b权值
     */
    public static void addEdge(int u, int v, int aa, int bb) {
        // 添加正向边：u -> v
        next[cnt] = head[u];
        to[cnt] = v;
        a[cnt] = aa;
        b[cnt] = bb;
        head[u] = cnt++;
        
        // 添加反向边：v -> u（便于查找）
        next[cnt] = head[v];
        to[cnt] = u;
        a[cnt] = aa;
        b[cnt] = bb;
        head[v] = cnt++;
    }
    
    /**
     * Floyd变形算法，计算所有点对之间的最小密度路径
     * 
     * <p>算法流程：</p>
     * <ol>
     *   <li>外层循环枚举中间点k</li>
     *   <li>内层循环枚举起点i和终点j</li>
     *   <li>通过中间点k更新路径密度</li>
     * </ol>
     */
    public static void floyd() {
        for (int k = 1; k <= n; k++) {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    // 通过中间点k转移：路径i->k->j
                    double newSumA = sumA[i][k][k-1] + sumA[k][j][k-1];
                    double newSumB = sumB[i][k][k-1] + sumB[k][j][k-1];
                    double newDensity = newSumA / newSumB;
                    
                    if (newDensity < f[i][j][k-1]) {
                        // 如果通过k的路径密度更小，更新DP数组
                        f[i][j][k] = newDensity;
                        sumA[i][j][k] = newSumA;
                        sumB[i][j][k] = newSumB;
                    } else {
                        // 否则保持原来的路径
                        f[i][j][k] = f[i][j][k-1];
                        sumA[i][j][k] = sumA[i][j][k-1];
                        sumB[i][j][k] = sumB[i][j][k-1];
                    }
                }
            }
        }
    }
    
    /**
     * 主函数：处理输入输出，执行Floyd算法
     * 
     * <p>算法流程：</p>
     * <ol>
     *   <li>读取输入数据（节点数、边数、边权）</li>
     *   <li>初始化图结构和Floyd数组</li>
     *   <li>运行Floyd算法计算所有点对的最小密度路径</li>
     *   <li>找到全局最小密度并输出</li>
     * </ol>
     * 
     * @param args 命令行参数
     * @throws IOException 输入输出异常
     */
    public static void main(String[] args) throws IOException {
        // 初始化输入输出流
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        try {
            // 读取节点数和边数
            in.nextToken();
            n = (int) in.nval;
            in.nextToken();
            m = (int) in.nval;
            
            // 读取每条边的信息并添加到图中
            for (int i = 1; i <= m; i++) {
                in.nextToken();
                int u = (int) in.nval;
                in.nextToken();
                int v = (int) in.nval;
                in.nextToken();
                int aa = (int) in.nval;
                in.nextToken();
                int bb = (int) in.nval;
                addEdge(u, v, aa, bb);
            }
            
            // 初始化图结构和Floyd数组
            prepare();
            
            // 运行Floyd算法计算所有点对的最小密度路径
            floyd();
            
            // 寻找全局最小密度
            double minDensity = INF;
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    if (f[i][j][n] < minDensity) {
                        minDensity = f[i][j][n];
                    }
                }
            }
            
            // 输出结果，保留10位小数
            out.printf("%.10f\n", minDensity);
            out.flush();
            
        } finally {
            // 确保资源正确关闭
            try {
                out.close();
                br.close();
            } catch (Exception e) {
                // 忽略关闭时的异常
            }
        }
    }
}