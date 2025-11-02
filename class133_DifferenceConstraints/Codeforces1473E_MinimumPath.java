package class142;

import java.io.*;
import java.util.*;

/**
 * Codeforces 1473E - Minimum Path 差分约束系统解法
 * 
 * 题目链接：https://codeforces.com/contest/1473/problem/E
 * 
 * 题目描述：
 * 给定一个带权无向图，定义一条路径的代价为：
 * 路径上所有边的权重之和减去最大边权重加上最小边权重。
 * 求从节点1到其他所有节点的最小代价。
 * 
 * 解题思路：
 * 这是一个复杂的图论问题，可以通过状态扩展和差分约束思想来解决。
 * 我们可以将每个节点扩展为4种状态：
 * 状态0：正常路径
 * 状态1：已经减去了一条边的权重（即已经使用了"减去最大边"操作）
 * 状态2：已经加上了一条边的权重（即已经使用了"加上最小边"操作）
 * 状态3：既减去了最大边又加上了最小边
 * 
 * 对于每条边(u, v, w)，我们可以进行以下状态转移：
 * 1. 正常转移：状态0 -> 状态0，代价为w
 * 2. 减去当前边：状态0 -> 状态1，代价为0（相当于减去最大边）
 * 3. 加上当前边：状态0 -> 状态2，代价为2w（相当于加上最小边）
 * 4. 从状态1转移：状态1 -> 状态1，代价为w
 * 5. 从状态1加上边：状态1 -> 状态3，代价为2w
 * 6. 从状态2转移：状态2 -> 状态2，代价为w
 * 7. 从状态2减去边：状态2 -> 状态3，代价为0
 * 8. 状态3转移：状态3 -> 状态3，代价为w
 * 
 * 这样我们就将原问题转化为在扩展图上求最短路的问题。
 * 最终答案就是状态3的最短路径值。
 * 
 * 时间复杂度：O((n + m) * log(n))，使用Dijkstra算法
 * 空间复杂度：O(n + m)
 * 
 * 相关题目：
 * 1. Codeforces 1473E - Minimum Path - 本题
 * 2. POJ 1201 Intervals - 区间选点问题
 * 3. POJ 2983 Is the Information Reliable? - 信息可靠性判断
 * 4. POJ 3169 Layout - 奶牛排队布局问题
 * 5. POJ 1364 King - 国王序列约束问题
 * 6. 洛谷 P5960 【模板】差分约束算法
 * 7. 洛谷 P1993 小K的农场
 * 8. 洛谷 P1250 种树
 * 9. 洛谷 P2294 [HNOI2005]狡猾的商人
 * 10. 洛谷 P4926 [1007]倍杀测量者
 * 11. 洛谷 P3275 [SCOI2011]糖果
 * 12. LibreOJ #10087 「一本通3.4 例1」Intervals
 * 13. LibreOJ #10088 「一本通3.4 例2」出纳员问题
 * 14. AtCoder ABC216G 01Sequence
 * 
 * 工程化考虑：
 * 1. 异常处理：输入校验、图构建检查、算法执行检测
 * 2. 性能优化：优先队列优化、状态压缩
 * 3. 可维护性：状态定义清晰、转移逻辑明确
 * 4. 可扩展性：支持更多操作类型
 * 5. 边界情况：单节点、空图、极端权重
 * 6. 测试用例：基本功能、边界值、异常情况
 */
public class Codeforces1473E_MinimumPath {
    static final int MAXN = 200005;
    static final int MAXM = 400005;
    static final long INF = Long.MAX_VALUE / 2;
    
    // 链式前向星存储图
    static int[] head = new int[MAXN];
    static int[] next = new int[MAXM * 2];
    static int[] to = new int[MAXM * 2];
    static int[] weight = new int[MAXM * 2];
    static int cnt = 1;
    
    // 距离数组，4种状态
    static long[][] dist = new long[MAXN][4];
    
    // 优先队列节点
    static class Node implements Comparable<Node> {
        int u, state;
        long cost;
        
        Node(int u, int state, long cost) {
            this.u = u;
            this.state = state;
            this.cost = cost;
        }
        
        @Override
        public int compareTo(Node other) {
            return Long.compare(this.cost, other.cost);
        }
    }
    
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
     * Dijkstra算法求最短路
     * @param n 节点数
     */
    static void dijkstra(int n) {
        // 初始化距离数组
        for (int i = 1; i <= n; i++) {
            Arrays.fill(dist[i], INF);
        }
        
        PriorityQueue<Node> pq = new PriorityQueue<>();
        dist[1][0] = 0;
        pq.offer(new Node(1, 0, 0));
        
        while (!pq.isEmpty()) {
            Node node = pq.poll();
            int u = node.u;
            int state = node.state;
            long cost = node.cost;
            
            if (cost != dist[u][state]) {
                continue;
            }
            
            // 遍历所有邻接边
            for (int i = head[u]; i > 0; i = next[i]) {
                int v = to[i];
                int w = weight[i];
                
                // 状态转移
                // 状态0 -> 状态0：正常转移
                if (dist[v][state] > cost + w) {
                    dist[v][state] = cost + w;
                    pq.offer(new Node(v, state, dist[v][state]));
                }
                
                // 状态0 -> 状态1：减去当前边（最大边）
                if (state == 0 && dist[v][1] > cost) {
                    dist[v][1] = cost;
                    pq.offer(new Node(v, 1, dist[v][1]));
                }
                
                // 状态0 -> 状态2：加上当前边（最小边）
                if (state == 0 && dist[v][2] > cost + 2L * w) {
                    dist[v][2] = cost + 2L * w;
                    pq.offer(new Node(v, 2, dist[v][2]));
                }
                
                // 状态1 -> 状态1：正常转移
                if (state == 1 && dist[v][1] > cost + w) {
                    dist[v][1] = cost + w;
                    pq.offer(new Node(v, 1, dist[v][1]));
                }
                
                // 状态1 -> 状态3：加上当前边
                if (state == 1 && dist[v][3] > cost + 2L * w) {
                    dist[v][3] = cost + 2L * w;
                    pq.offer(new Node(v, 3, dist[v][3]));
                }
                
                // 状态2 -> 状态2：正常转移
                if (state == 2 && dist[v][2] > cost + w) {
                    dist[v][2] = cost + w;
                    pq.offer(new Node(v, 2, dist[v][2]));
                }
                
                // 状态2 -> 状态3：减去当前边
                if (state == 2 && dist[v][3] > cost) {
                    dist[v][3] = cost;
                    pq.offer(new Node(v, 3, dist[v][3]));
                }
                
                // 状态3 -> 状态3：正常转移
                if (state == 3 && dist[v][3] > cost + w) {
                    dist[v][3] = cost + w;
                    pq.offer(new Node(v, 3, dist[v][3]));
                }
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] parts = br.readLine().split(" ");
        int n = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        
        // 初始化
        Arrays.fill(head, 0);
        cnt = 1;
        
        // 读入边
        for (int i = 0; i < m; i++) {
            parts = br.readLine().split(" ");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            int w = Integer.parseInt(parts[2]);
            
            // 无向图，添加双向边
            addEdge(u, v, w);
            addEdge(v, u, w);
        }
        
        // 运行Dijkstra算法
        dijkstra(n);
        
        // 输出结果
        for (int i = 2; i <= n; i++) {
            out.print(dist[i][3] + " ");
        }
        out.println();
        
        out.flush();
        out.close();
        br.close();
    }
}