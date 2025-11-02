package class064;

import java.util.*;

/**
 * ============================================================================
 * 多源最短路径问题 - Dijkstra算法扩展实现
 * ============================================================================
 * 
 * 题目：多源最短路径（Multi-Source Shortest Path）
 * 来源：各大算法平台通用问题，如LeetCode、Codeforces、洛谷等
 * 
 * 题目描述：
 * 给定一个带权有向图，包含n个节点和m条边，同时给定k个源点，需要计算从每个源点到
 * 所有其他节点的最短距离。
 * 
 * 输入格式：
 * - n: 节点数量，编号从1到n
 * - edges: 边列表，每条边格式为[u, v, w]，表示从u到v的有向边，权重为w
 * - sources: 源点列表，包含k个源点编号
 * 
 * 输出格式：
 * - 距离矩阵，dist[i][j]表示从源点i到节点j的最短距离
 * - 如果节点不可达，则距离为Integer.MAX_VALUE
 * 
 * 解题思路分析：
 * ============================================================================
 * 1. 方法1：对每个源点单独运行Dijkstra算法
 *    - 时间复杂度：O(K*(V+E)logV)，其中K是源点数量
 *    - 空间复杂度：O(V+E)
 *    - 优点：实现简单直观，易于理解和调试
 *    - 缺点：当源点数量K较大时，时间复杂度较高
 * 
 * 2. 方法2：虚拟超级源点法
 *    - 创建一个虚拟源点，连接到所有实际源点，边权为0
 *    - 然后运行一次Dijkstra算法
 *    - 时间复杂度：O((V+E)logV)
 *    - 空间复杂度：O(V+E)
 *    - 优点：时间复杂度与源点数量无关，适合源点较多的情况
 *    - 缺点：需要处理虚拟节点的边界情况
 * 
 * 算法选择策略：
 * - 当源点数量K较小时（K < logV），推荐使用方法1
 * - 当源点数量K较大时（K ≥ logV），推荐使用方法2
 * 
 * 算法应用场景：
 * ============================================================================
 * 1. 多数据中心网络路由：多个数据中心之间的最短路径计算
 * 2. 多仓库物流配送优化：多个仓库到各个配送点的最短路径规划
 * 3. 社交网络分析：多个影响源在社交网络中的传播路径分析
 * 4. 交通网络规划：多个起点到各个终点的最短路径查询
 * 
 * 复杂度分析：
 * ============================================================================
 * 时间复杂度分析：
 * - 方法1：O(K*(V+E)logV)，其中K是源点数量
 * - 方法2：O((V+E)logV)，与源点数量无关
 * 
 * 空间复杂度分析：
 * - 方法1：O(K*V) 存储多个距离数组
 * - 方法2：O(V+E) 图结构和距离数组
 * 
 * 工程化考量：
 * ============================================================================
 * 1. 内存优化：对于大规模图，可以使用稀疏矩阵表示
 * 2. 性能优化：使用优先队列（最小堆）优化Dijkstra算法
 * 3. 边界处理：处理节点不可达、负权边等特殊情况
 * 4. 错误处理：验证输入数据的合法性
 * 
 * 测试用例设计：
 * ============================================================================
 * 1. 基础测试：单源点、简单图结构
 * 2. 边界测试：空图、单节点图、完全图
 * 3. 性能测试：大规模图、稠密图、稀疏图
 * 4. 特殊测试：存在不可达节点、负权边检测
 * 
 * 相关题目链接：
 * ============================================================================
 * - LeetCode 1334: 阈值距离内邻居最少的城市
 * - Codeforces 938G: Shortest Path Queries
 * - 洛谷 P3371: 单源最短路径（弱化版）
 * - HDU 2544: 最短路
 * 
 * 作者：算法工程化项目组
 * 创建时间：2025-10-29
 * 版本：v1.0
 */
public class Code26_MultiSourceShortestPath {
    
    /**
     * 方法1：对每个源点单独运行Dijkstra算法
     * 
     * 算法步骤：
     * 1. 对于每个源点，运行标准的Dijkstra算法
     * 2. 记录从该源点到所有其他节点的最短距离
     * 3. 返回所有源点的最短距离矩阵
     * 
     * 时间复杂度：O(K*(V+E)logV)
     * 空间复杂度：O(K*V)
     * 
     * @param n 节点总数
     * @param edges 边列表，格式为 [u, v, w]
     * @param sources 源点列表
     * @return 距离矩阵，dist[i][j]表示从源点i到节点j的最短距离
     */
    public static int[][] multiSourceDijkstra1(int n, int[][] edges, int[] sources) {
        // 构建邻接表表示的图
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int w = edge[2];
            graph.get(u).add(new int[]{v, w});
        }
        
        // 距离矩阵，dist[i][j]表示从源点i到节点j的最短距离
        int[][] dist = new int[sources.length][n + 1];
        
        // 对每个源点运行Dijkstra算法
        for (int idx = 0; idx < sources.length; idx++) {
            int source = sources[idx];
            int[] distance = new int[n + 1];
            Arrays.fill(distance, Integer.MAX_VALUE);
            distance[source] = 0;
            
            boolean[] visited = new boolean[n + 1];
            PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
            heap.offer(new int[]{source, 0});
            
            while (!heap.isEmpty()) {
                int[] record = heap.poll();
                int u = record[0];
                
                if (visited[u]) continue;
                visited[u] = true;
                
                for (int[] edge : graph.get(u)) {
                    int v = edge[0];
                    int w = edge[1];
                    
                    if (!visited[v] && distance[u] + w < distance[v]) {
                        distance[v] = distance[u] + w;
                        heap.offer(new int[]{v, distance[v]});
                    }
                }
            }
            
            // 存储当前源点的距离数组
            dist[idx] = distance;
        }
        
        return dist;
    }
    
    /**
     * 方法2：虚拟超级源点法
     * 
     * 算法步骤：
     * 1. 创建一个虚拟源点0，连接到所有实际源点，边权为0
     * 2. 从虚拟源点0运行Dijkstra算法
     * 3. 得到的距离数组就是从虚拟源点到各点的最短距离
     * 4. 由于虚拟源点到实际源点的距离为0，所以这等价于多源最短路径
     * 
     * 时间复杂度：O((V+E)logV)
     * 空间复杂度：O(V+E)
     * 
     * @param n 节点总数
     * @param edges 边列表，格式为 [u, v, w]
     * @param sources 源点列表
     * @return 距离数组，dist[i]表示从最近的源点到节点i的最短距离
     */
    public static int[] multiSourceDijkstra2(int n, int[][] edges, int[] sources) {
        // 构建扩展图（包含虚拟源点0）
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 添加原始边
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int w = edge[2];
            graph.get(u).add(new int[]{v, w});
        }
        
        // 添加虚拟源点到所有实际源点的边（权重为0）
        for (int source : sources) {
            graph.get(0).add(new int[]{source, 0});
        }
        
        // 从虚拟源点0运行Dijkstra算法
        int[] distance = new int[n + 1];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[0] = 0;
        
        boolean[] visited = new boolean[n + 1];
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        heap.offer(new int[]{0, 0});
        
        while (!heap.isEmpty()) {
            int[] record = heap.poll();
            int u = record[0];
            
            if (visited[u]) continue;
            visited[u] = true;
            
            for (int[] edge : graph.get(u)) {
                int v = edge[0];
                int w = edge[1];
                
                if (!visited[v] && distance[u] + w < distance[v]) {
                    distance[v] = distance[u] + w;
                    heap.offer(new int[]{v, distance[v]});
                }
            }
        }
        
        return distance;
    }
    
    /**
     * 方法3：多源最短路径的优化版本（使用反向索引堆）
     * 
     * 算法优化点：
     * 1. 使用链式前向星存储图，节省空间
     * 2. 使用反向索引堆优化优先队列操作
     * 3. 支持大规模图的快速计算
     * 
     * 时间复杂度：O((V+E)logV)
     * 空间复杂度：O(V+E)
     */
    public static class OptimizedMultiSourceDijkstra {
        private static final int MAXN = 100005;
        private static final int MAXM = 200005;
        
        // 链式前向星数据结构
        private static int[] head = new int[MAXN];
        private static int[] next = new int[MAXM];
        private static int[] to = new int[MAXM];
        private static int[] weight = new int[MAXM];
        private static int cnt;
        
        // 反向索引堆数据结构
        private static int[] heap = new int[MAXN];
        private static int[] where = new int[MAXN];
        private static int heapSize;
        private static int[] distance = new int[MAXN];
        
        /**
         * 初始化数据结构
         */
        public static void build(int n) {
            cnt = 1;
            heapSize = 0;
            Arrays.fill(head, 1, n + 2, 0); // n+2因为包含虚拟源点0
            Arrays.fill(where, 1, n + 2, -1);
            Arrays.fill(distance, 1, n + 2, Integer.MAX_VALUE);
        }
        
        /**
         * 添加边
         */
        public static void addEdge(int u, int v, int w) {
            next[cnt] = head[u];
            to[cnt] = v;
            weight[cnt] = w;
            head[u] = cnt++;
        }
        
        /**
         * 多源最短路径计算
         */
        public static int[] calculate(int n, int[][] edges, int[] sources) {
            build(n);
            
            // 添加原始边
            for (int[] edge : edges) {
                addEdge(edge[0], edge[1], edge[2]);
            }
            
            // 添加虚拟源点到实际源点的边
            for (int source : sources) {
                addEdge(0, source, 0);
            }
            
            // 从虚拟源点开始Dijkstra算法
            addOrUpdateOrIgnore(0, 0);
            
            while (!isEmpty()) {
                int u = pop();
                for (int ei = head[u]; ei > 0; ei = next[ei]) {
                    addOrUpdateOrIgnore(to[ei], distance[u] + weight[ei]);
                }
            }
            
            return Arrays.copyOfRange(distance, 1, n + 1);
        }
        
        private static void addOrUpdateOrIgnore(int v, int c) {
            if (where[v] == -1) {
                heap[heapSize] = v;
                where[v] = heapSize++;
                distance[v] = c;
                heapInsert(where[v]);
            } else if (where[v] >= 0) {
                distance[v] = Math.min(distance[v], c);
                heapInsert(where[v]);
            }
        }
        
        private static void heapInsert(int i) {
            while (distance[heap[i]] < distance[heap[(i - 1) / 2]]) {
                swap(i, (i - 1) / 2);
                i = (i - 1) / 2;
            }
        }
        
        private static int pop() {
            int ans = heap[0];
            swap(0, --heapSize);
            heapify(0);
            where[ans] = -2;
            return ans;
        }
        
        private static void heapify(int i) {
            int l = i * 2 + 1;
            while (l < heapSize) {
                int best = l + 1 < heapSize && distance[heap[l + 1]] < distance[heap[l]] ? l + 1 : l;
                best = distance[heap[best]] < distance[heap[i]] ? best : i;
                if (best == i) break;
                swap(best, i);
                i = best;
                l = i * 2 + 1;
            }
        }
        
        private static void swap(int i, int j) {
            int tmp = heap[i];
            heap[i] = heap[j];
            heap[j] = tmp;
            where[heap[i]] = i;
            where[heap[j]] = j;
        }
        
        private static boolean isEmpty() {
            return heapSize == 0;
        }
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1：简单多源最短路径
        int n1 = 4;
        int[][] edges1 = {
            {1, 2, 1}, {2, 3, 2}, {3, 4, 3},
            {1, 3, 4}, {2, 4, 5}
        };
        int[] sources1 = {1, 3}; // 两个源点：1和3
        
        System.out.println("=== 测试用例1 ===");
        System.out.println("方法1结果（单独Dijkstra）：");
        int[][] result1 = multiSourceDijkstra1(n1, edges1, sources1);
        for (int i = 0; i < sources1.length; i++) {
            System.out.printf("从源点%d到各点的距离: ", sources1[i]);
            for (int j = 1; j <= n1; j++) {
                System.out.printf("%d ", result1[i][j]);
            }
            System.out.println();
        }
        
        System.out.println("方法2结果（虚拟源点法）：");
        int[] result2 = multiSourceDijkstra2(n1, edges1, sources1);
        System.out.print("从最近源点到各点的距离: ");
        for (int j = 1; j <= n1; j++) {
            System.out.printf("%d ", result2[j]);
        }
        System.out.println();
        
        System.out.println("方法3结果（优化版本）：");
        int[] result3 = OptimizedMultiSourceDijkstra.calculate(n1, edges1, sources1);
        System.out.print("从最近源点到各点的距离: ");
        for (int j = 0; j < n1; j++) {
            System.out.printf("%d ", result3[j]);
        }
        System.out.println();
        
        // 性能对比分析
        System.out.println("\n=== 性能分析 ===");
        System.out.println("方法1：适合源点数量较少的情况，实现简单");
        System.out.println("方法2：适合源点数量较多的情况，效率更高");
        System.out.println("方法3：适合大规模图，内存使用更优");
    }
}