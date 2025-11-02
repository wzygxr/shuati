package class065;

import java.util.*;

// 洛谷 P3385 【模板】负环 - SPFA算法实现
// 题目链接: https://www.luogu.com.cn/problem/P3385
// 题目描述: 给定一个有向图，请求出图中是否存在从顶点 1 出发能到达的负环。
// 负环的定义是：一条边权之和为负数的回路。
//
// SPFA算法核心思想:
// Bellman-Ford算法的队列优化版本
// 只对距离发生变化的节点进行松弛操作
// 使用队列维护待松弛的节点
// 通过记录节点入队次数检测负环
//
// 时间复杂度: 平均O(E)，最坏O(VE)，其中V是节点数，E是边数
// 空间复杂度: O(V+E)，需要邻接表存储图和队列维护节点

public class Code08_SPFALuogu3385 {
    
    public static int MAXN = 2001;
    public static int MAXM = 6001;
    
    // 链式前向星建图需要
    public static int[] head = new int[MAXN];
    public static int[] next = new int[MAXM];
    public static int[] to = new int[MAXM];
    public static int[] weight = new int[MAXM];
    public static int cnt;
    
    // SPFA需要
    public static int[] distance = new int[MAXN];
    public static int[] updateCnt = new int[MAXN];
    public static boolean[] inQueue = new boolean[MAXN];
    public static Queue<Integer> queue = new LinkedList<>();
    
    // 初始化函数
    public static void build(int n) {
        cnt = 1;
        Arrays.fill(head, 1, n + 1, 0);
        Arrays.fill(distance, 1, n + 1, Integer.MAX_VALUE);
        Arrays.fill(updateCnt, 1, n + 1, 0);
        Arrays.fill(inQueue, 1, n + 1, false);
    }
    
    // 添加边的函数
    public static void addEdge(int u, int v, int w) {
        next[cnt] = head[u];
        to[cnt] = v;
        weight[cnt] = w;
        head[u] = cnt++;
    }
    
    // SPFA算法检测负环
    public static boolean spfa(int n) {
        // 初始化源点（节点1）的距离为0
        distance[1] = 0;
        queue.offer(1);
        inQueue[1] = true;
        updateCnt[1]++;
        
        while (!queue.isEmpty()) {
            int u = queue.poll();
            inQueue[u] = false;
            
            // 遍历从节点u出发的所有边
            for (int i = head[u]; i > 0; i = next[i]) {
                int v = to[i];
                int w = weight[i];
                
                // 如果通过节点u可以缩短到节点v的距离
                if (distance[u] + w < distance[v]) {
                    distance[v] = distance[u] + w;
                    
                    // 如果节点v不在队列中
                    if (!inQueue[v]) {
                        // 松弛次数超过n-1说明存在负环
                        if (++updateCnt[v] > n - 1) {
                            return true;
                        }
                        queue.offer(v);
                        inQueue[v] = true;
                    }
                }
            }
        }
        
        return false;
    }
    
    // 测试函数
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int cases = scanner.nextInt();
        
        for (int i = 0; i < cases; i++) {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            
            build(n);
            
            for (int j = 0; j < m; j++) {
                int u = scanner.nextInt();
                int v = scanner.nextInt();
                int w = scanner.nextInt();
                addEdge(u, v, w);
            }
            
            System.out.println(spfa(n) ? "YES" : "NO");
        }
        
        scanner.close();
    }
}

/* ----------------------------- 补充题目1: LeetCode 743. 网络延迟时间 - SPFA实现 ----------------------------- */
// 使用SPFA算法解决网络延迟时间问题，与Bellman-Ford相比效率更高
class NetworkDelayTimeSPFA {
    public int networkDelayTime(int[][] times, int n, int k) {
        // 构建邻接表
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] time : times) {
            graph.get(time[0]).add(new int[]{time[1], time[2]});
        }
        
        // 初始化距离数组
        int[] distance = new int[n + 1];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[k] = 0;
        
        // 初始化队列和访问数组
        Queue<Integer> queue = new LinkedList<>();
        boolean[] inQueue = new boolean[n + 1];
        queue.offer(k);
        inQueue[k] = true;
        
        // SPFA松弛操作
        while (!queue.isEmpty()) {
            int u = queue.poll();
            inQueue[u] = false;
            
            for (int[] edge : graph.get(u)) {
                int v = edge[0];
                int w = edge[1];
                
                // 松弛操作
                if (distance[u] != Integer.MAX_VALUE && distance[v] > distance[u] + w) {
                    distance[v] = distance[u] + w;
                    if (!inQueue[v]) {
                        queue.offer(v);
                        inQueue[v] = true;
                    }
                }
            }
        }
        
        // 计算最大距离
        int maxDelay = 0;
        for (int i = 1; i <= n; i++) {
            if (distance[i] == Integer.MAX_VALUE) {
                return -1; // 存在无法到达的节点
            }
            maxDelay = Math.max(maxDelay, distance[i]);
        }
        
        return maxDelay;
    }
}

/* ----------------------------- 补充题目2: 差分约束系统 ----------------------------- */
// 题目描述: 求解一组形如 xj - xi ≤ ck 的不等式组
// SPFA算法解决思路:
// 1. 对于每个不等式 xj - xi ≤ ck，添加一条边i->j，权重为ck
// 2. 添加超级源点0，到所有其他点的边权重为0
// 3. 运行SPFA算法检测是否存在负权环
// 4. 如果不存在负权环，distance数组即为一组可行解

class DifferenceConstraintSolver {
    // 差分约束系统检测是否有解
    public boolean hasSolution(int[][] constraints, int n) {
        // 构建图 (包含超级源点0)
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 添加约束条件: 对于xj - xi ≤ ck，添加边i->j，权重为ck
        for (int[] constraint : constraints) {
            int i = constraint[0];
            int j = constraint[1];
            int ck = constraint[2];
            graph.get(i).add(new int[]{j, ck});
        }
        
        // 添加超级源点0到所有其他点的边，权重为0
        for (int i = 1; i <= n; i++) {
            graph.get(0).add(new int[]{i, 0});
        }
        
        // 初始化距离数组
        int[] distance = new int[n + 1];
        Arrays.fill(distance, 0); // 超级源点到自己的距离为0
        
        // 记录节点入队次数
        int[] count = new int[n + 1];
        boolean[] inQueue = new boolean[n + 1];
        Queue<Integer> queue = new LinkedList<>();
        
        // 将超级源点加入队列
        queue.offer(0);
        inQueue[0] = true;
        count[0]++;
        
        // SPFA检测负环
        while (!queue.isEmpty()) {
            int u = queue.poll();
            inQueue[u] = false;
            
            for (int[] edge : graph.get(u)) {
                int v = edge[0];
                int w = edge[1];
                
                // 松弛操作
                if (distance[v] > distance[u] + w) {
                    distance[v] = distance[u] + w;
                    
                    // 如果v不在队列中，加入队列
                    if (!inQueue[v]) {
                        queue.offer(v);
                        inQueue[v] = true;
                        count[v]++;
                        
                        // 如果入队次数超过n，存在负环
                        if (count[v] > n) {
                            return false;
                        }
                    }
                }
            }
        }
        
        // 不存在负环，系统有解
        return true;
    }
}

/* ----------------------------- SPFA算法工程实践建议 ----------------------------- */
// 1. 适用场景:
//    - 存在负权边的单源最短路径问题
//    - 需要检测负权环的场景
//    - 图中边数相对节点数较多的情况
//
// 2. 性能优化技巧:
//    - 使用双端队列(Deque)优化: 对于可能成为最短路径的节点放在队首
//    - 使用栈代替队列: 在某些情况下可以更快地检测到负环
//    - 记录每个节点的访问时间戳: 避免重复处理
//    - 距离数组优化: 使用滚动数组减少空间占用
//
// 3. 算法实现注意事项:
//    - 必须使用inQueue数组避免重复入队
//    - 负环检测需要正确记录每个节点的入队次数
//    - 处理大权重时要防止整数溢出
//    - 对于无法到达的节点需要特殊处理
//
// 4. 与其他算法的对比:
//    - 相比Bellman-Ford: SPFA通常更高效，但最坏情况仍然是O(VE)
//    - 相比Dijkstra: SPFA可以处理负权边，但一般情况下效率较低
//    - 在实际应用中，SPFA在大多数情况下比Bellman-Ford更快，但不如Dijkstra
//
// 5. 常见的SPFA变种:
//    - LLL优化: 队列中维护平均距离，小于平均值的节点放在队首
//    - SLF优化: 新入队的节点如果距离比队首节点小，放在队首
//    - 这两种优化可以在一定程度上提高SPFA的效率

/* ----------------------------- 极端场景处理 ----------------------------- */
// 1. 负权环问题:
//    - 在存在负权环的图中，最短路径是没有意义的
//    - 需要正确检测并处理这种情况
// 2. 大规模图数据:
//    - 在大规模图中，SPFA可能退化为O(VE)，需要考虑其他算法
// 3. 无向图处理:
//    - 对于无向图，需要将每条边视为两条有向边
// 4. 存在负权边但无负环的情况:
//    - 这是SPFA算法的优势场景，能比Bellman-Ford更高效地处理