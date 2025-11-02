package class143;

// Dijkstra算法扩展练习题：K 站中转内最便宜的航班
// 有 n 个城市通过一些航班连接。给你一个数组 flights ，其中 flights[i] = [fromi, toi, pricei] ，
// 表示该航班都从城市 fromi 开始，以价格 pricei 抵达 toi。
// 现在给定所有的城市和航班，以及出发城市 src 和目的地 dst，你的任务是找到出一条最多经过 k 站中转的路线，
// 使得从 src 到 dst 的价格最便宜，并返回该价格。如果不存在这样的路线，则输出 -1。
// 测试链接: https://leetcode.cn/problems/cheapest-flights-within-k-stops/
// 
// 算法思路：
// 这是一道限制边数的最短路径问题，可以使用修改版的Dijkstra算法解决。
// 1. 使用优先队列存储状态(城市, 费用, 中转次数)
// 2. 优先队列按费用排序
// 3. 使用二维数组dist[城市][中转次数]记录到达每个城市使用不同中转次数的最小费用
// 4. 当中转次数超过k时，不再扩展该状态
//
// 具体实现：
// 1. 构建图的邻接表表示
// 2. 使用优先队列存储待处理的状态，按费用从小到大排序
// 3. 从起始城市开始，逐步扩展到其他城市
// 4. 对于每个状态，更新其相邻城市的最小费用
// 5. 限制中转次数不超过k
//
// 时间复杂度：O(K * E * log(K * E))，其中K是最大中转次数，E是边数
// 空间复杂度：O(N * K + E)，其中N是城市数
//
// 相关题目链接：
// 1. LeetCode 787. K 站中转内最便宜的航班 - https://leetcode.cn/problems/cheapest-flights-within-k-stops/
// 2. LeetCode 743. 网络延迟时间 - https://leetcode.cn/problems/network-delay-time/
// 3. 洛谷 P4779 单源最短路径 - https://www.luogu.com.cn/problem/P4779
// 4. POJ 2387 Til the Cows Come Home - http://poj.org/problem?id=2387
// 5. Codeforces 20C Dijkstra? - https://codeforces.com/problemset/problem/20/C
// 6. 洛谷 P3371 单源最短路径 - https://www.luogu.com.cn/problem/P3371
// 7. HDU 2544 最短路 - https://acm.hdu.edu.cn/showproblem.php?pid=2544
// 8. AtCoder ABC070 D - Transit Tree Path - https://atcoder.jp/contests/abc070/tasks/abc070_d
// 9. 牛客 NC50439 最短路 - https://ac.nowcoder.com/acm/problem/50439
// 10. SPOJ SHPATH - https://www.spoj.com/problems/SHPATH/
// 11. ZOJ 2818 The Traveling Judges Problem - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827366818
// 12. 51Nod 1018 最短路 - https://www.51nod.com/Challenge/Problem.html#problemId=1018
// 13. 洛谷 P1144 最短路计数 - https://www.luogu.com.cn/problem/P1144
// 14. LeetCode 542. 01 矩阵 - https://leetcode.cn/problems/01-matrix/
// 15. LeetCode 773. 滑动谜题 - https://leetcode.cn/problems/sliding-puzzle/

/*
 * 算法思路：
 * 这是一道限制边数的最短路径问题，可以使用修改版的Dijkstra算法解决。
 * 1. 使用优先队列存储状态(城市, 费用, 中转次数)
 * 2. 优先队列按费用排序
 * 3. 使用二维数组dist[城市][中转次数]记录到达每个城市使用不同中转次数的最小费用
 * 4. 当中转次数超过k时，不再扩展该状态
 * 
 * 时间复杂度：O(K * E * log(K * E))，其中K是最大中转次数，E是边数
 * 空间复杂度：O(N * K + E)，其中N是城市数
 * 
 * 示例：
 * 输入:
 * n = 3, edges = [[0,1,100],[1,2,100],[0,2,500]]
 * src = 0, dst = 2, k = 1
 * 输出: 200
 * 
 * 输入:
 * n = 3, edges = [[0,1,100],[1,2,100],[0,2,500]]
 * src = 0, dst = 2, k = 0
 * 输出: 500
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class Code08_DijkstraExtended {

    // 最大节点数
    public static int MAXN = 101;
    
    // 最大边数
    public static int MAXM = 5001;
    
    // 节点数、起始节点、目标节点、最大中转次数
    public static int n, src, dst, k;
    
    // 邻接表表示图
    public static ArrayList<Edge>[] graph = new ArrayList[MAXN];
    
    // 距离数组，dist[i][j]表示到达节点i使用j次中转的最小费用
    public static int[][] dist = new int[MAXN][MAXN];
    
    // 初始化图
    public static void init() {
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
    }
    
    // 添加边
    public static void addEdge(int from, int to, int price) {
        graph[from].add(new Edge(to, price));
    }
    
    // 修改版Dijkstra算法实现
    public static int dijkstraWithStops() {
        // 初始化距离数组为无穷大
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        }
        
        // 起点距离为0，中转次数为0
        dist[src][0] = 0;
        
        // 优先队列，按费用排序
        // 状态：[城市, 费用, 中转次数]
        PriorityQueue<State> pq = new PriorityQueue<>((a, b) -> a.cost - b.cost);
        pq.offer(new State(src, 0, 0));
        
        // 当优先队列不为空时，继续处理
        while (!pq.isEmpty()) {
            // 取出费用最小的状态
            State curr = pq.poll();
            int u = curr.city;
            int cost = curr.cost;
            int stops = curr.stops;
            
            // 如果到达目标城市，返回费用
            if (u == dst) {
                return cost;
            }
            
            // 如果中转次数超过限制，跳过
            if (stops > k) {
                continue;
            }
            
            // 更新相邻节点的距离
            for (Edge edge : graph[u]) {
                int v = edge.to;
                int price = edge.price;
                
                // 如果找到更便宜的路径
                if (cost + price < dist[v][stops + 1]) {
                    dist[v][stops + 1] = cost + price;
                    pq.offer(new State(v, dist[v][stops + 1], stops + 1));
                }
            }
        }
        
        // 无法到达目标城市，返回-1
        return -1;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取节点数和边数
        in.nextToken();
        n = (int) in.nval;
        in.nextToken();
        int m = (int) in.nval;
        
        // 初始化图
        init();
        
        // 读取边信息并构图
        for (int i = 0; i < m; i++) {
            in.nextToken();
            int from = (int) in.nval;
            in.nextToken();
            int to = (int) in.nval;
            in.nextToken();
            int price = (int) in.nval;
            addEdge(from, to, price);
        }
        
        // 读取起始城市、目标城市和最大中转次数
        in.nextToken();
        src = (int) in.nval;
        in.nextToken();
        dst = (int) in.nval;
        in.nextToken();
        k = (int) in.nval;
        
        // 计算结果并输出
        out.println(dijkstraWithStops());
        out.flush();
        out.close();
        br.close();
    }
    
    // 边的定义
    static class Edge {
        int to, price;
        
        Edge(int to, int price) {
            this.to = to;
            this.price = price;
        }
    }
    
    // 状态的定义
    static class State {
        int city, cost, stops;
        
        State(int city, int cost, int stops) {
            this.city = city;
            this.cost = cost;
            this.stops = stops;
        }
    }
}