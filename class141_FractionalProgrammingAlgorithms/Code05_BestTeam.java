package class138;

/**
 * 最佳团体问题 - 树形背包 + 01分数规划解法
 * 
 * 题目描述：
 * 给定一棵树，节点编号0~n，0号节点是整棵树的头
 * 编号1~n的节点，每个节点都有招募花费和战斗值，0号节点这两个值都是0
 * 当招募某个节点时，必须招募该节点及其所有祖先节点
 * 除了0号节点之外，一共可以招募k个人，希望让战斗值之和/招募花费之和的比值尽量大
 * 
 * 数据范围：
 * 1 <= k <= n <= 2500
 * 0 <= 招募花费、战斗值 <= 10^4
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P4322
 * 
 * 算法思路：01分数规划 + 树形背包 + DFN序优化
 * 
 * 01分数规划的数学原理：
 * 我们需要找到k个节点的集合S(不包括0号节点)，使得(Σstrength[i])/(Σcost[i])最大
 * 且S满足树形依赖关系（选子必选父）
 * 
 * 对于给定的L，判断是否存在合法集合S，使得Σstrength[i]/Σcost[i] > L
 * 等价于：Σ(strength[i] - L * cost[i]) > 0
 * 
 * 我们使用树形背包来解决依赖关系的选择问题，同时通过DFN序优化树形DP
 * 
 * 时间复杂度：O(log(1/ε) * n^2)，其中ε是精度要求
 * 空间复杂度：O(n^2)
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code05_BestTeam {

    // 常量定义
    public static final int MAXN = 3001;  // 最大节点数
    public static final int LIMIT = 10000; // 最大可能的比值上限
    public static final double NA = -1e9;  // 无效解标记
    public static final double PRECISION = 1e-6; // 精度控制

    // 链式前向星存储树结构
    public static int[] head = new int[MAXN];  // 每个节点的第一条边
    public static int[] next = new int[MAXN];  // 每条边的下一条边
    public static int[] to = new int[MAXN];    // 每条边的目标节点
    public static int edgeCnt;  // 边的计数器

    // 节点属性
    public static int[] cost = new int[MAXN];     // 招募花费，下标为节点原始编号
    public static int[] strength = new int[MAXN]; // 战斗值，下标为节点原始编号

    // DFN序相关
    public static int[] dfn = new int[MAXN]; // dfn[a] = b，表示原始a号节点的dfn编号为b
    public static int dfnCnt;  // dfn序计数
    public static int[] size = new int[MAXN]; // 子树大小，下标为节点dfn编号

    // 01分数规划和树形DP相关
    public static double[] value = new double[MAXN]; // (战斗值 - x * 招募花费)的结余
    public static double[][] dp = new double[MAXN][MAXN]; // 树形DP数组

    public static int k, n;  // 需要招募的人数和总人数

    /**
     * 初始化图的存储结构
     */
    public static void prepare() {
        edgeCnt = 1;
        dfnCnt = 0;
        Arrays.fill(head, 1, n + 1, 0);
    }

    /**
     * 向树中添加一条有向边（父节点到子节点）
     * 
     * @param u 父节点
     * @param v 子节点
     */
    public static void addEdge(int u, int v) {
        next[edgeCnt] = head[u];
        to[edgeCnt] = v;
        head[u] = edgeCnt++;
    }

    /**
     * 计算每个节点的DFN编号和子树大小
     * 使用后序遍历，将树结构转换为线性结构，便于树形背包DP
     * 
     * @param u 当前节点
     * @return 当前子树的大小
     */
    public static int dfs(int u) {
        int i = ++dfnCnt;
        dfn[u] = i;  // 记录当前节点的DFN编号
        size[i] = 1; // 初始子树大小为1（包含自己）
        
        // 遍历所有子节点
        for (int e = head[u], v; e != 0; e = next[e]) {
            v = to[e];
            size[i] += dfs(v);  // 累加子节点的子树大小
        }
        
        return size[i];
    }

    /**
     * 检查是否存在一种招募方式，使得比值大于x
     * 使用树形背包DP来求解最大结余和
     * 
     * @param x 当前尝试的比值
     * @return 是否存在这样的招募方式
     */
    public static boolean check(double x) {
        // 计算每个节点的结余值：战斗值 - x * 招募花费
        for (int i = 0; i <= n; i++) {
            value[dfn[i]] = (double) strength[i] - x * cost[i];
        }
        
        // 初始化越界位置为无效解
        for (int j = 1; j <= k; j++) {
            dp[dfnCnt + 1][j] = NA;
        }
        
        // 树形背包DP核心逻辑（基于DFN序的后序遍历）
        for (int i = dfnCnt; i >= 2; i--) {  // 从后往前处理，0号节点的DFN是1，跳过
            for (int j = 1; j <= k; j++) {  // 枚举招募人数
                // 两种选择：
                // 1. 不选当前节点所在子树：dp[i + size[i]][j]
                // 2. 选当前节点：value[i] + dp[i + 1][j - 1]
                dp[i][j] = Math.max(
                    dp[i + size[i]][j],  // 不选当前子树
                    value[i] + dp[i + 1][j - 1]  // 选当前节点，然后从子节点中选j-1个
                );
            }
        }
        
        // 原始的0号节点的DFN编号是1，其他节点的DFN编号从2开始
        // 0号节点的战斗值和招募花费都是0，我们需要从其他节点中招募k个
        return dp[2][k] >= 0;  // 如果最大结余和>=0，说明存在比值>=x的方案
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
            
            // 读取输入：需要招募的人数k和总人数n
            in.nextToken();
            k = (int) in.nval;
            in.nextToken();
            n = (int) in.nval;
            
            // 初始化树结构
            prepare();
            
            // 读取每个节点的信息：招募花费、战斗值和父节点
            for (int i = 1; i <= n; i++) {
                in.nextToken();
                cost[i] = (int) in.nval;
                in.nextToken();
                strength[i] = (int) in.nval;
                in.nextToken();
                int parent = (int) in.nval;
                addEdge(parent, i);  // 添加父节点到子节点的边
            }
            
            // 计算DFN序和子树大小
            dfs(0);
            
            // 初始化二分查找的左右边界
            double left = 0.0;
            double right = LIMIT;
            double result = 0.0;
            
            // 二分查找最优比值
            while (left < right && right - left >= PRECISION) {
                double mid = (left + right) / 2.0;
                
                if (check(mid)) {
                    // 如果存在比值>=mid的方案，尝试更大的值
                    result = mid;
                    left = mid + PRECISION;
                } else {
                    // 否则尝试更小的值
                    right = mid - PRECISION;
                }
            }
            
            // 输出结果，保留3位小数
            out.printf("%.3f\n", result);
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
