package class142;

import java.io.*;
import java.util.*;

/**
 * USACO 2005 December Gold Layout 差分约束系统解法
 * 
 * 题目链接：http://www.usaco.org/index.php?page=viewproblem2&cpid=239
 * 
 * 题目描述：
 * 有N头奶牛排成一队，编号为1到N。奶牛们希望与它们的朋友挨在一起。
 * 给出两种约束条件：
 * 1. ML条约束：好友关系，第i对好友a和b希望它们之间的距离不超过d
 * 2. MD条约束：情敌关系，第i对情敌a和b希望它们之间的距离至少为d
 * 求第1头和第N头奶牛之间的最大距离，如果无解输出-1，如果可以任意远输出-2。
 * 
 * 解题思路：
 * 这是一个典型的差分约束系统问题。差分约束系统是线性规划的一种特殊形式，
 * 可以通过图论中的最短路径算法来解决。对于不等式组：
 * x[i] - x[j] <= c[k] (i=1,2,...,n; j=1,2,...,n; k=1,2,...,m)
 * 我们可以构造一个图，对于每个不等式x[i] - x[j] <= c[k]，从节点j向节点i连一条权值为c[k]的有向边。
 * 然后从一个超级源点向所有节点连权值为0的边，确保图的连通性，最后求从超级源点到各点的最短路径即可得到解。
 * 
 * 算法步骤：
 * 1. 建立图模型：
 *    - 基本约束：dist[i] - dist[i-1] >= 0 => dist[i-1] - dist[i] <= 0 (从i向i-1连权值为0的边)
 *    - 好友约束：dist[b] - dist[a] <= d (从a向b连权值为d的边)
 *    - 情敌约束：dist[b] - dist[a] >= d => dist[a] - dist[b] <= -d (从b向a连权值为-d的边)
 * 2. 添加超级源点：向所有点连权值为0的边，确保图的连通性
 * 3. 使用SPFA算法求最短路：
 *    - 如果存在负环，则无解（输出-1）
 *    - 如果第N头奶牛不可达，则可以任意远（输出-2）
 *    - 否则返回dist[N]作为第1头和第N头奶牛之间的最大距离
 * 
 * 时间复杂度：O(n * m)，其中n是奶牛数，m是约束条件数
 * 空间复杂度：O(n + m)
 * 
 * 相关题目：
 * 1. USACO 2005 December Gold Layout - 本题
 *    链接：http://www.usaco.org/index.php?page=viewproblem2&cpid=239
 *    来源：USACO
 *    内容：奶牛排队布局问题，包含好友和情敌关系约束
 * 2. POJ 3169 Layout - 同题
 *    链接：http://poj.org/problem?id=3169
 *    来源：POJ
 *    内容：与USACO Layout相同的问题
 * 3. 洛谷 P4878 [USACO05DEC] Layout G
 *    链接：https://www.luogu.com.cn/problem/P4878
 *    来源：洛谷
 *    内容：USACO Layout问题的洛谷版本
 * 4. LibreOJ #10054. 「一本通 2.3 例 2」Layout
 *    链接：https://loj.ac/p/10054
 *    来源：LibreOJ
 *    内容：差分约束系统应用题，奶牛排队布局
 * 5. AtCoder ABC137 E - Coins Respawn
 *    链接：https://atcoder.jp/contests/abc137/tasks/abc137_e
 *    来源：AtCoder
 *    内容：在有向图中寻找从起点到终点的最大收益路径，可转化为差分约束问题
 * 6. Codeforces 1473E - Minimum Path
 *    链接：https://codeforces.com/contest/1473/problem/E
 *    来源：Codeforces
 *    内容：图论问题，涉及最短路径变换，可使用差分约束思想解决
 */
public class USACO_Layout {
    static final int MAXN = 1005;
    static final int MAXM = 20005;
    static final int INF = 0x3f3f3f3f;
    
    // 链式前向星存储图
    static int[] head = new int[MAXN];
    static int[] next = new int[MAXM];
    static int[] to = new int[MAXM];
    static int[] weight = new int[MAXM];
    static int cnt = 1;
    
    // SPFA相关数组
    static int[] dist = new int[MAXN];
    static boolean[] inQueue = new boolean[MAXN];
    static int[] count = new int[MAXN];
    
    /**
     * 添加边到图中
     * @param u 起点
     * @param v 终点
     * @param w 边权
     */
    static void addEdge(int u, int v, int w) {
        next[cnt] = head[u];
        to[cnt] = v;
        weight[cnt] = w;
        head[u] = cnt++;
    }
    
    /**
     * SPFA算法求最短路并判断负环
     * SPFA (Shortest Path Faster Algorithm) 是Bellman-Ford算法的队列优化版本
     * 用于求解单源最短路径问题，可以处理负权边，同时能检测负环
     * 
     * @param start 起点
     * @param n 节点数
     * @return 如果存在负环返回false，否则返回true
     */
    static boolean spfa(int start, int n) {
        Arrays.fill(dist, INF);
        Arrays.fill(inQueue, false);
        Arrays.fill(count, 0);
        
        Queue<Integer> queue = new LinkedList<>();
        dist[start] = 0;
        inQueue[start] = true;
        queue.offer(start);
        count[start] = 1;
        
        while (!queue.isEmpty()) {
            int u = queue.poll();
            inQueue[u] = false;
            
            // 遍历u的所有邻接点
            for (int i = head[u]; i > 0; i = next[i]) {
                int v = to[i];
                int w = weight[i];
                
                // 松弛操作（最短路）
                if (dist[v] > dist[u] + w) {
                    dist[v] = dist[u] + w;
                    
                    // 如果节点v不在队列中，加入队列
                    if (!inQueue[v]) {
                        queue.offer(v);
                        inQueue[v] = true;
                        count[v]++;
                        
                        // 如果入队次数超过n次，说明存在负环，无解
                        // 这是因为在没有负环的情况下，任何点最多入队n-1次
                        if (count[v] > n) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] parts = br.readLine().split(" ");
        int n = Integer.parseInt(parts[0]); // 奶牛数量
        int ml = Integer.parseInt(parts[1]); // 好友约束数量
        int md = Integer.parseInt(parts[2]); // 情敌约束数量
        
        // 初始化链式前向星
        Arrays.fill(head, 0);
        cnt = 1;
        
        // 添加基本约束：dist[i] - dist[i-1] >= 0
        // 转化为：dist[i-1] - dist[i] <= 0
        // 这确保了奶牛按编号顺序排队
        for (int i = 2; i <= n; i++) {
            addEdge(i, i - 1, 0);
        }
        
        // 添加好友约束：dist[b] - dist[a] <= d
        // 表示编号为a和b的奶牛之间的距离不超过d
        for (int i = 0; i < ml; i++) {
            String[] constraint = br.readLine().split(" ");
            int a = Integer.parseInt(constraint[0]);
            int b = Integer.parseInt(constraint[1]);
            int d = Integer.parseInt(constraint[2]);
            
            // 从a向b连权值为d的边，表示dist[b] - dist[a] <= d
            addEdge(a, b, d);
        }
        
        // 添加情敌约束：dist[b] - dist[a] >= d
        // 转化为：dist[a] - dist[b] <= -d
        // 表示编号为a和b的奶牛之间的距离至少为d
        for (int i = 0; i < md; i++) {
            String[] constraint = br.readLine().split(" ");
            int a = Integer.parseInt(constraint[0]);
            int b = Integer.parseInt(constraint[1]);
            int d = Integer.parseInt(constraint[2]);
            
            // 从b向a连权值为-d的边，表示dist[a] - dist[b] <= -d
            addEdge(b, a, -d);
        }
        
        // 添加超级源点，向所有点连权值为0的边
        // 这确保了图的连通性，使得从超级源点可以到达所有节点
        int superSource = 0;
        for (int i = 1; i <= n; i++) {
            addEdge(superSource, i, 0);
        }
        
        // 使用SPFA求最短路
        if (!spfa(superSource, n + 1)) {
            // 存在负环，无解
            // 负环表示约束条件之间存在矛盾，无法满足所有约束
            out.println(-1);
        } else if (dist[n] == INF) {
            // 第N头奶牛不可达，可以任意远
            // 这表示第1头和第N头奶牛之间没有约束条件限制它们的距离
            out.println(-2);
        } else {
            // 返回第1头和第N头奶牛之间的最大距离
            out.println(dist[n]);
        }
        
        out.flush();
        out.close();
        br.close();
    }
}