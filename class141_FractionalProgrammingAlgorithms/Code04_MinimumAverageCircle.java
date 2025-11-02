package class138;

/**
 * 最小圈问题 - 01分数规划解法
 * 
 * 题目描述：给定一个有向带权图，求所有环的平均值中最小的平均值
 * 环的平均值定义为：环中边的权值和 / 环中边的数量
 * 
 * 数据范围：
 * 1 <= n <= 3000
 * 1 <= m <= 10000
 * -10^7 <= 边权 <= 10^7
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P3199
 * 
 * 算法思路：01分数规划 + 二分查找 + DFS判负环
 * 
 * 01分数规划的数学原理：
 * 我们需要找到环C，使得 (Σw(e))/|C| 最小，其中e∈C，|C|是环的边数
 * 
 * 对于给定的L，判断是否存在环C，使得 (Σw(e))/|C| < L
 * 等价于：Σw(e) < L * |C|
 * 等价于：Σ(w(e) - L) < 0
 * 
 * 这相当于在原图每条边的权值减去L后，判断图中是否存在负环
 * 
 * 时间复杂度：O(log(1/ε) * n * m)，其中ε是精度要求
 * 空间复杂度：O(n + m)
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code04_MinimumAverageCircle {

    // 常量定义
    public static final int MAXN = 3001;  // 最大节点数
    public static final int MAXM = 10001; // 最大边数
    public static final double MAXE = 1e7; // 最大边权绝对值
    public static final double PRECISION = 1e-9; // 精度控制

    // 链式前向星存储图
    public static int[] head = new int[MAXN];  // 每个节点的第一条边
    public static int[] next = new int[MAXM];  // 每条边的下一条边
    public static int[] to = new int[MAXM];    // 每条边的目标节点
    public static double[] weight = new double[MAXM]; // 每条边的权值
    public static int cnt;  // 边的计数器

    // DFS判断负环需要的数据结构
    public static double[] value = new double[MAXN]; // 每个点的累积边权
    public static boolean[] path = new boolean[MAXN]; // 每个点是否在当前递归路径上

    public static int n, m; // 节点数和边数

    /**
     * 初始化图的存储结构
     */
    public static void prepare() {
        cnt = 1;
        Arrays.fill(head, 1, n + 1, 0);
    }

    /**
     * 向图中添加一条有向边
     * 
     * @param u 边的起点
     * @param v 边的终点
     * @param w 边的权值
     */
    public static void addEdge(int u, int v, double w) {
        next[cnt] = head[u];
        to[cnt] = v;
        weight[cnt] = w;
        head[u] = cnt++;
    }

    /**
     * 检查是否存在一个环，其平均值小于L
     * 相当于在原图每条边减去L后，判断图中是否存在负环
     * 
     * @param L 当前的比值候选值
     * @return 是否存在这样的环
     */
    public static boolean check(double L) {
        Arrays.fill(value, 1, n + 1, 0);
        Arrays.fill(path, 1, n + 1, false);
        // 从超级源点0出发，访问所有节点
        return dfs(0, L);
    }

    /**
     * DFS判断负环
     * 这实际上是SPFA算法的递归实现，具有很强的剪枝能力
     * 
     * @param u 当前访问的节点
     * @param L 当前的比值候选值
     * @return 是否存在负环
     */
    public static boolean dfs(int u, double L) {
        if (u == 0) {
            // 超级源点，访问所有节点
            for (int i = 1; i <= n; i++) {
                if (dfs(i, L)) {
                    return true;
                }
            }
        } else {
            // 标记当前节点在递归路径上
            path[u] = true;
            
            // 遍历u的所有出边
            for (int e = head[u]; e != 0; e = next[e]) {
                int v = to[e];
                double w = weight[e] - L; // 边权减去L
                
                // 只有当可以松弛时才继续递归，这是一个重要的剪枝
                if (value[v] > value[u] + w) {
                    value[v] = value[u] + w; // 更新v的累积边权
                    
                    // 如果v已经在当前递归路径上，说明找到了负环
                    // 或者从v出发递归找到了负环
                    if (path[v] || dfs(v, L)) {
                        return true;
                    }
                }
            }
            
            // 回溯，标记当前节点不在递归路径上
            path[u] = false;
        }
        return false;
    }

    /**
     * 主函数
     * 
     * @param args 命令行参数
     * @throws IOException 输入输出异常
     */
    public static void main(String[] args) throws IOException {
        // 初始化输入输出流
        BufferedReader br = null;
        StreamTokenizer in = null;
        PrintWriter out = null;
        
        try {
            br = new BufferedReader(new InputStreamReader(System.in));
            in = new StreamTokenizer(br);
            out = new PrintWriter(new OutputStreamWriter(System.out));
            
            // 读取节点数和边数
            in.nextToken();
            n = (int) in.nval;
            in.nextToken();
            m = (int) in.nval;
            
            // 初始化图的存储结构
            prepare();
            
            // 读取每条边并添加到图中
            for (int i = 1; i <= m; i++) {
                in.nextToken();
                int u = (int) in.nval;
                in.nextToken();
                int v = (int) in.nval;
                in.nextToken();
                double w = in.nval;
                addEdge(u, v, w);
            }
            
            // 初始化二分查找的左右边界
            double left = -MAXE;
            double right = MAXE;
            double result = 0.0;
            
            // 二分查找过程
            while (left < right && right - left >= PRECISION) {
                double mid = (left + right) / 2.0;
                
                // 检查是否存在环的平均值小于mid
                if (check(mid)) {
                    // 如果存在，则调整右边界
                    right = mid - PRECISION;
                } else {
                    // 否则调整左边界，并记录当前结果
                    result = mid;
                    left = mid + PRECISION;
                }
            }
            
            // 输出结果，保留8位小数
            out.printf("%.8f\n", result);
            out.flush();
        } finally {
            // 确保资源正确关闭
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    // 忽略关闭时的异常
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // 忽略关闭时的异常
                }
            }
        }
    }
}
