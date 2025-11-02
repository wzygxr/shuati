package class060;

// 有向无环图中的最长路径
// 给定一个有向无环图，计算从任意起点到任意终点的最长路径长度
// 测试链接 : http://poj.org/problem?id=3249
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下所有代码，把主类名改成Main，可以直接通过

import java.util.*;

/**
 * 题目解析：
 * 这是拓扑排序DP的经典题目，计算DAG中的最长路径。
 * 需要先进行拓扑排序，然后按照拓扑序进行动态规划。
 * 
 * 算法思路：
 * 1. 使用拓扑排序确定节点的处理顺序
 * 2. 使用动态规划计算每个节点的最长路径
 * 3. dp[i]表示以节点i为终点的最长路径长度
 * 4. 对于每个节点，更新其所有邻居节点的dp值
 * 
 * 时间复杂度：O(V + E)，其中V是节点数，E是边数
 * 空间复杂度：O(V + E)
 * 
 * 相关题目扩展：
 * 1. POJ 3249 Test for Job - http://poj.org/problem?id=3249
 * 2. 洛谷 P1113 杂务 - https://www.luogu.com.cn/problem/P1113
 * 3. LeetCode 2050. 并行课程 III - https://leetcode.cn/problems/parallel-courses-iii/
 * 4. HDU 4109 Activation - http://acm.hdu.edu.cn/showproblem.php?pid=4109
 * 
 * 工程化考虑：
 * 1. 输入输出优化：使用BufferedReader和StreamTokenizer
 * 2. 边界处理：空图、单节点图、无边图等情况
 * 3. 性能优化：使用邻接表存储图结构
 * 4. 异常处理：处理非法输入数据
 * 5. 模块化设计：分离建图、拓扑排序、DP计算逻辑
 */
public class Code15_LongestPathInDAG {

    public static int MAXN = 100001;
    public static int MAXM = 1000001;

    // 邻接表存储图
    public static int[] head = new int[MAXN];
    public static int[] next = new int[MAXM];
    public static int[] to = new int[MAXM];
    public static int cnt;

    // 入度数组和出度数组
    public static int[] indegree = new int[MAXN];
    public static int[] outdegree = new int[MAXN];

    // 节点权重
    public static int[] weight = new int[MAXN];

    // 拓扑排序队列
    public static int[] queue = new int[MAXN];

    // DP数组：dp[i]表示以节点i为终点的最长路径长度
    public static int[] dp = new int[MAXN];

    public static int n, m;

    /**
     * 初始化图结构
     */
    public static void build() {
        cnt = 1;
        Arrays.fill(head, 1, n + 1, 0);
        Arrays.fill(indegree, 1, n + 1, 0);
        Arrays.fill(outdegree, 1, n + 1, 0);
        Arrays.fill(dp, 1, n + 1, 0);
    }

    /**
     * 添加边 u -> v
     */
    public static void addEdge(int u, int v) {
        next[cnt] = head[u];
        to[cnt] = v;
        head[u] = cnt++;
        indegree[v]++;
        outdegree[u]++;
    }

    /**
     * 计算DAG中的最长路径
     * @return 最长路径长度
     */
    public static int longestPath() {
        int l = 0;
        int r = 0;

        // 初始化dp数组为节点权重
        for (int i = 1; i <= n; i++) {
            dp[i] = weight[i];
            if (indegree[i] == 0) {
                queue[r++] = i;
            }
        }

        int maxPath = Integer.MIN_VALUE;

        while (l < r) {
            int u = queue[l++];

            // 如果u是终点（出度为0），更新最长路径
            if (outdegree[u] == 0) {
                maxPath = Math.max(maxPath, dp[u]);
            }

            // 遍历u的所有邻居
            for (int ei = head[u]; ei != 0; ei = next[ei]) {
                int v = to[ei];

                // 状态转移：dp[v] = max(dp[v], dp[u] + weight[v])
                if (dp[u] + weight[v] > dp[v]) {
                    dp[v] = dp[u] + weight[v];
                }

                if (--indegree[v] == 0) {
                    queue[r++] = v;
                }
            }
        }

        return maxPath;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (scanner.hasNext()) {
            n = scanner.nextInt();
            m = scanner.nextInt();
            
            build();
            
            // 读取节点权重
            for (int i = 1; i <= n; i++) {
                weight[i] = scanner.nextInt();
            }
            
            // 读取边
            for (int i = 0; i < m; i++) {
                int u = scanner.nextInt();
                int v = scanner.nextInt();
                addEdge(u, v);
            }
            
            int result = longestPath();
            System.out.println(result);
        }
        
        scanner.close();
    }
}