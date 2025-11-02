package class138;

/**
 * 最优比率环问题 - 观光奶牛 (Sightseeing Cows)
 * 
 * <h3>题目信息</h3>
 * <ul>
 *   <li><strong>题目来源</strong>：Luogu P2868，POJ 3621</li>
 *   <li><strong>题目描述</strong>：给定一个有向图，每个节点有点权value[i]，每条边有边权weight[i]。
 *   要求找到一个环，使得环上所有点权之和与边权之和的比值最大。</li>
 *   <li><strong>数据范围</strong>：
 *     <ul>
 *       <li>1 <= n <= 1000（节点数）</li>
 *       <li>1 <= m <= 5000（边数）</li>
 *       <li>1 <= value[i], weight[i] <= 1000</li>
 *     </ul>
 *   </li>
 *   <li><strong>测试链接</strong>：
 *     <a href="https://www.luogu.com.cn/problem/P2868">Luogu P2868</a>，
 *     <a href="http://poj.org/problem?id=3621">POJ 3621</a>
 *   </li>
 * </ul>
 * 
 * <h3>算法思路</h3>
 * <p>使用01分数规划 + 二分查找 + DFS判正环的方法：</p>
 * <ol>
 *   <li><strong>01分数规划</strong>：将比值最大化问题转化为判定性问题</li>
 *   <li><strong>二分查找</strong>：在可能的比值范围内进行二分</li>
 *   <li><strong>DFS判正环</strong>：通过DFS递归判断是否存在正环</li>
 * </ol>
 * 
 * <h3>数学原理</h3>
 * <p>我们需要最大化 R = (Σvalue[i]) / (Σweight[e])，其中i∈环C，e∈环C</p>
 * <p>对于给定的L，判断是否存在环C使得 R > L：</p>
 * <ul>
 *   <li>等价于：Σvalue[i] > L * Σweight[e]</li>
 *   <li>等价于：Σ(value[i] - L * weight[e]) > 0</li>
 * </ul>
 * <p>这相当于将每条边的权值更新为(value[i] - L * weight[e])，然后判断图中是否存在正环。</p>
 * 
 * <h3>复杂度分析</h3>
 * <ul>
 *   <li><strong>时间复杂度</strong>：O(log(1/ε) * n * m)，其中ε是精度要求（本题中取1e-9）</li>
 *   <li><strong>空间复杂度</strong>：O(n + m)，使用链式前向星存储图结构</li>
 * </ul>
 * 
 * <h3>注意事项</h3>
 * <ul>
 *   <li>使用DFS判正环比SPFA更高效，具有更强的剪枝能力</li>
 *   <li>需要处理精度问题，避免浮点数比较误差</li>
 *   <li>使用超级源点技术，从所有节点开始搜索</li>
 * </ul>
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code06_SightseeingCows {

    // 常量定义
    public static final int MAXN = 1001;  // 最大节点数
    public static final int MAXM = 5001; // 最大边数
    
    // 足够小代表无效解
    public static final double NA = -1e9;
    
    // 最小精度控制，用于二分结束条件
    public static final double PRECISION = 1e-9;
    
    // 链式前向星存储图结构
    public static int[] head = new int[MAXN];  // 每个节点的第一条边
    public static int[] next = new int[MAXM];  // 每条边的下一条边
    public static int[] to = new int[MAXM];    // 每条边的目标节点
    public static double[] weight = new double[MAXM]; // 每条边的权值
    public static int cnt;  // 边的计数器
    
    // 点权数组，存储每个节点的权值
    public static int[] value = new int[MAXN];
    
    // DFS判正环需要的数据结构
    public static double[] dist = new double[MAXN]; // 每个点的累积边权
    public static boolean[] path = new boolean[MAXN]; // 每个点是否在当前递归路径上
    
    // 全局变量，存储节点数和边数
    public static int n, m;
    
    /**
     * 初始化图的存储结构
     * 重置链式前向星的计数器，清空head数组
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
     * 检查函数：判断给定的比率值x是否可行
     * 
     * <p>核心思想：将原图的边权更新为(value[i] - x * weight[e])，然后判断图中是否存在正环。</p>
     * <p>如果存在正环，说明存在比值大于x的环；否则说明当前比值过大。</p>
     * 
     * @param x 当前尝试的比率值
     * @return 如果存在比值大于x的环，返回true；否则返回false
     */
    public static boolean check(double x) {
        // 初始化距离数组和路径标记数组
        Arrays.fill(dist, 1, n + 1, 0);
        Arrays.fill(path, 1, n + 1, false);
        
        // 从超级源点0开始DFS搜索
        return dfs(0, x);
    }
    
    /**
     * DFS递归判断正环
     * 
     * <p>这是SPFA算法的递归实现版本，具有更强的剪枝能力：</p>
     * <ul>
     *   <li>只有当可以松弛时才继续递归，避免不必要的搜索</li>
     *   <li>使用路径标记检测环的存在</li>
     *   <li>从超级源点出发，确保访问所有连通分量</li>
     * </ul>
     * 
     * @param u 当前访问的节点
     * @param x 当前尝试的比率值
     * @return 如果从当前节点出发存在正环，返回true；否则返回false
     */
    public static boolean dfs(int u, double x) {
        if (u == 0) {
            // 超级源点0：具有通往所有节点的虚拟边
            // 从每个节点开始DFS搜索，确保覆盖所有连通分量
            for (int i = 1; i <= n; i++) {
                if (dfs(i, x)) {
                    return true;
                }
            }
        } else {
            // 标记当前节点在递归路径上
            path[u] = true;
            
            // 遍历当前节点的所有出边
            for (int e = head[u]; e != 0; e = next[e]) {
                int v = to[e];
                // 更新边权：value[i] - x * weight[e]
                double w = value[v] - x * weight[e];
                
                // 只有当可以松弛时才继续递归（强剪枝）
                if (dist[v] < dist[u] + w) {
                    dist[v] = dist[u] + w; // 更新累积边权
                    
                    // 如果v已经在当前递归路径上，说明找到了正环
                    // 或者从v出发递归找到了正环
                    if (path[v] || dfs(v, x)) {
                        return true;
                    }
                }
            }
            
            // 回溯：标记当前节点不在递归路径上
            path[u] = false;
        }
        return false;
    }
    
    /**
     * 主函数：处理输入输出，执行二分查找算法
     * 
     * <p>算法流程：</p>
     * <ol>
     *   <li>读取输入数据（节点数、边数、点权、边权）</li>
     *   <li>初始化二分查找的左右边界</li>
     *   <li>进行二分查找，每次调用check函数判断当前比率是否可行</li>
     *   <li>输出结果，保留两位小数</li>
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
            
            // 初始化图的存储结构
            prepare();
            
            // 读取每个节点的点权
            for (int i = 1; i <= n; i++) {
                in.nextToken();
                value[i] = (int) in.nval;
            }
            
            // 读取每条边的信息并添加到图中
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
            // 左边界为0，右边界为最大可能比值（根据数据范围估算）
            double left = 0.0;
            double right = 1000.0;
            double result = 0.0;
            
            // 二分查找过程
            while (left < right && right - left >= PRECISION) {
                double mid = (left + right) / 2.0;
                
                if (check(mid)) {
                    // 如果存在比值大于mid的环，记录当前结果并尝试更大的值
                    result = mid;
                    left = mid + PRECISION;
                } else {
                    // 否则尝试更小的值
                    right = mid - PRECISION;
                }
            }
            
            // 输出结果，保留两位小数
            out.printf("%.2f\n", result);
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