package class143;

// Dijkstra算法练习题1：网络延迟时间
// 有 n 个网络节点，标记为 1 到 n。
// 给你一个列表 times，表示信号经过有向边的传递时间。 
// times[i] = (ui, vi, wi)，其中 ui 是源节点，vi 是目标节点，wi 是信号从源节点传递到目标节点的时间。
// 现在，从某个节点 K 发出一个信号。需要多久才能使所有节点都收到信号？
// 如果不能使所有节点收到信号，返回 -1。
// 测试链接: https://leetcode.cn/problems/network-delay-time/
// 
// 算法思路：
// 这是一道典型的单源最短路径问题，使用Dijkstra算法解决。
// 1. 构建图的邻接表表示
// 2. 使用优先队列优化的Dijkstra算法计算从节点K到所有节点的最短距离
// 3. 如果存在无法到达的节点，返回-1
// 4. 否则返回所有最短距离中的最大值
//
// 具体实现：
// 1. 初始化图的邻接表表示
// 2. 使用优先队列存储待处理的节点，按距离从小到大排序
// 3. 从起始节点开始，逐步扩展到其他节点
// 4. 对于每个节点，更新其相邻节点的最短距离
// 5. 最后检查是否所有节点都可达，并返回最大距离
//
// 时间复杂度：O((V + E) * log V)，其中V是节点数，E是边数
// 空间复杂度：O(V + E)
//
// 相关题目链接：
// 1. LeetCode 743. 网络延迟时间 - https://leetcode.cn/problems/network-delay-time/
// 2. 洛谷 P4779 单源最短路径 - https://www.luogu.com.cn/problem/P4779
// 3. POJ 2387 Til the Cows Come Home - http://poj.org/problem?id=2387
// 4. Codeforces 20C Dijkstra? - https://codeforces.com/problemset/problem/20/C
// 5. 洛谷 P3371 单源最短路径 - https://www.luogu.com.cn/problem/P3371
// 6. HDU 2544 最短路 - https://acm.hdu.edu.cn/showproblem.php?pid=2544
// 7. AtCoder ABC070 D - Transit Tree Path - https://atcoder.jp/contests/abc070/tasks/abc070_d
// 8. 牛客 NC50439 最短路 - https://ac.nowcoder.com/acm/problem/50439
// 9. SPOJ SHPATH - https://www.spoj.com/problems/SHPATH/
// 10. ZOJ 2818 The Traveling Judges Problem - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827366818
// 11. 51Nod 1018 最短路 - https://www.51nod.com/Challenge/Problem.html#problemId=1018
// 12. 洛谷 P1144 最短路计数 - https://www.luogu.com.cn/problem/P1144
// 13. LeetCode 542. 01 矩阵 - https://leetcode.cn/problems/01-matrix/
// 14. LeetCode 773. 滑动谜题 - https://leetcode.cn/problems/sliding-puzzle/
// 15. 牛客 NC50522 跳楼机 - https://ac.nowcoder.com/acm/problem/50522

/*
 * 算法思路：
 * 这是一道典型的单源最短路径问题，使用Dijkstra算法解决。
 * 1. 构建图的邻接表表示
 * 2. 使用优先队列优化的Dijkstra算法计算从节点K到所有节点的最短距离
 * 3. 如果存在无法到达的节点，返回-1
 * 4. 否则返回所有最短距离中的最大值
 * 
 * 时间复杂度：O((V + E) * log V)，其中V是节点数，E是边数
 * 空间复杂度：O(V + E)
 * 
 * 示例：
 * 输入：times = [[2,1,1],[2,3,1],[3,4,1]], n = 4, k = 2
 * 输出：2
 * 
 * 输入：times = [[1,2,1]], n = 2, k = 1
 * 输出：1
 * 
 * 输入：times = [[1,2,1]], n = 2, k = 2
 * 输出：-1
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

public class Code06_DijkstraExample1 {

    // 最大节点数
    public static int MAXN = 101;
    
    // 最大边数
    public static int MAXM = 6001;
    
    // 节点数和起始节点
    public static int n, k;
    
    // 邻接表表示图
    public static ArrayList<Edge>[] graph = new ArrayList[MAXN];
    
    // 距离数组
    public static int[] dist = new int[MAXN];
    
    // 访问标记数组
    public static boolean[] visited = new boolean[MAXN];
    
    // 初始化图
    public static void init() {
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
    }
    
    // 添加边
    public static void addEdge(int from, int to, int weight) {
        graph[from].add(new Edge(to, weight));
    }
    
    // Dijkstra算法实现
    public static int dijkstra() {
        // 初始化距离数组为无穷大
        Arrays.fill(dist, 1, n + 1, Integer.MAX_VALUE);
        // 初始化访问标记数组为false
        Arrays.fill(visited, false);
        
        // 起点距离为0
        dist[k] = 0;
        
        // 优先队列，按距离排序，存储待处理的节点
        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> a.dist - b.dist);
        pq.offer(new Node(k, 0));
        
        // 当优先队列不为空时，继续处理
        while (!pq.isEmpty()) {
            // 取出距离最小的节点
            Node curr = pq.poll();
            int u = curr.id;
            
            // 如果已经访问过，跳过（避免重复处理）
            if (visited[u]) {
                continue;
            }
            
            // 标记为已访问
            visited[u] = true;
            
            // 遍历当前节点的所有邻接节点
            for (Edge edge : graph[u]) {
                int v = edge.to;
                int w = edge.weight;
                
                // 松弛操作：如果通过当前节点u可以缩短到节点v的距离，则更新
                if (!visited[v] && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    // 将更新后的节点加入优先队列
                    pq.offer(new Node(v, dist[v]));
                }
            }
        }
        
        // 计算最大距离：遍历所有节点的最短距离，找出最大值
        int maxDist = 0;
        for (int i = 1; i <= n; i++) {
            // 如果存在无法到达的节点，返回-1
            if (dist[i] == Integer.MAX_VALUE) {
                return -1; 
            }
            // 更新最大距离
            maxDist = Math.max(maxDist, dist[i]);
        }
        
        // 返回所有节点都能到达时的最大距离
        return maxDist;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取边数和节点数
        in.nextToken();
        int m = (int) in.nval;
        in.nextToken();
        n = (int) in.nval;
        in.nextToken();
        k = (int) in.nval;
        
        // 初始化图
        init();
        
        // 读取边信息并构图
        for (int i = 0; i < m; i++) {
            in.nextToken();
            int u = (int) in.nval;
            in.nextToken();
            int v = (int) in.nval;
            in.nextToken();
            int w = (int) in.nval;
            addEdge(u, v, w);
        }
        
        // 计算结果并输出
        out.println(dijkstra());
        out.flush();
        out.close();
        br.close();
    }
    
    // 边的定义
    static class Edge {
        int to, weight;
        
        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }
    
    // 节点的定义
    static class Node {
        int id, dist;
        
        Node(int id, int dist) {
            this.id = id;
            this.dist = dist;
        }
    }
}