package class064;

import java.util.*;

/**
 * Bellman-Ford算法与Dijkstra算法对比
 * 
 * 题目：带负权边的最短路径问题
 * 来源：各大算法平台通用问题
 * 
 * 题目描述：
 * 给定一个带权有向图，图中可能包含负权边，需要计算从源点到所有其他节点的最短距离。
 * 如果图中存在负权回路，则无法计算最短路径。
 * 
 * 解题思路：
 * 1. Dijkstra算法：适用于非负权图，时间复杂度O((V+E)logV)
 * 2. Bellman-Ford算法：适用于含负权边图，时间复杂度O(V*E)
 * 3. SPFA算法：Bellman-Ford的队列优化版本，平均时间复杂度O(E)
 * 
 * 算法对比分析：
 * - Dijkstra算法：贪心策略，不能处理负权边
 * - Bellman-Ford算法：动态规划思想，可以检测负权回路
 * - SPFA算法：实际效率较高，但最坏情况下退化为O(V*E)
 * 
 * 时间复杂度分析：
 * - Dijkstra: O((V+E)logV)
 * - Bellman-Ford: O(V*E)
 * - SPFA: 平均O(E)，最坏O(V*E)
 * 
 * 空间复杂度分析：
 * - 均为O(V+E)
 */
public class Code27_BellmanFordComparison {
    
    /**
     * Dijkstra算法实现（仅适用于非负权图）
     * 
     * 算法步骤：
     * 1. 初始化距离数组，源点距离为0，其他点为无穷大
     * 2. 使用优先队列维护待处理节点
     * 3. 每次取出距离最小的节点，更新其邻居节点的距离
     * 
     * 时间复杂度：O((V+E)logV)
     * 空间复杂度：O(V+E)
     */
    public static int[] dijkstra(int n, int[][] edges, int source) {
        // 构建邻接表
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            graph.get(u).add(new int[]{v, w});
        }
        
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;
        
        boolean[] visited = new boolean[n + 1];
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        heap.offer(new int[]{source, 0});
        
        while (!heap.isEmpty()) {
            int[] record = heap.poll();
            int u = record[0];
            
            if (visited[u]) continue;
            visited[u] = true;
            
            for (int[] edge : graph.get(u)) {
                int v = edge[0], w = edge[1];
                if (!visited[v] && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    heap.offer(new int[]{v, dist[v]});
                }
            }
        }
        
        return dist;
    }
    
    /**
     * Bellman-Ford算法实现（可处理负权边）
     * 
     * 算法步骤：
     * 1. 初始化距离数组，源点距离为0
     * 2. 进行V-1次松弛操作
     * 3. 检查是否存在负权回路
     * 
     * 时间复杂度：O(V*E)
     * 空间复杂度：O(V+E)
     */
    public static int[] bellmanFord(int n, int[][] edges, int source) {
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;
        
        // 进行V-1次松弛操作
        for (int i = 1; i < n; i++) {
            boolean updated = false;
            for (int[] edge : edges) {
                int u = edge[0], v = edge[1], w = edge[2];
                if (dist[u] != Integer.MAX_VALUE && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    updated = true;
                }
            }
            // 如果没有更新，提前结束
            if (!updated) break;
        }
        
        // 检查负权回路
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            if (dist[u] != Integer.MAX_VALUE && dist[u] + w < dist[v]) {
                throw new RuntimeException("图中存在负权回路");
            }
        }
        
        return dist;
    }
    
    /**
     * SPFA算法（Bellman-Ford的队列优化）
     * 
     * 算法步骤：
     * 1. 初始化距离数组和队列
     * 2. 将源点加入队列
     * 3. 不断从队列取出节点进行松弛操作
     * 4. 检查负权回路
     * 
     * 时间复杂度：平均O(E)，最坏O(V*E)
     * 空间复杂度：O(V+E)
     */
    public static int[] spfa(int n, int[][] edges, int source) {
        // 构建邻接表
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            graph.get(u).add(new int[]{v, w});
        }
        
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;
        
        boolean[] inQueue = new boolean[n + 1];
        int[] count = new int[n + 1]; // 记录入队次数，用于检测负权回路
        
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(source);
        inQueue[source] = true;
        count[source]++;
        
        while (!queue.isEmpty()) {
            int u = queue.poll();
            inQueue[u] = false;
            
            for (int[] edge : graph.get(u)) {
                int v = edge[0], w = edge[1];
                if (dist[u] != Integer.MAX_VALUE && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    
                    if (!inQueue[v]) {
                        queue.offer(v);
                        inQueue[v] = true;
                        count[v]++;
                        
                        // 如果某个节点入队次数超过V次，说明存在负权回路
                        if (count[v] > n) {
                            throw new RuntimeException("图中存在负权回路");
                        }
                    }
                }
            }
        }
        
        return dist;
    }
    
    /**
     * 算法性能对比测试
     */
    public static void compareAlgorithms() {
        // 测试用例1：非负权图
        System.out.println("=== 测试用例1：非负权图 ===");
        int n1 = 5;
        int[][] edges1 = {
            {1, 2, 2}, {1, 3, 4}, {2, 3, 1},
            {2, 4, 7}, {3, 4, 3}, {3, 5, 5}, {4, 5, 2}
        };
        int source1 = 1;
        
        System.out.println("Dijkstra算法结果:");
        int[] result1 = dijkstra(n1, edges1, source1);
        printArray(result1, 1, n1);
        
        System.out.println("Bellman-Ford算法结果:");
        int[] result2 = bellmanFord(n1, edges1, source1);
        printArray(result2, 1, n1);
        
        System.out.println("SPFA算法结果:");
        int[] result3 = spfa(n1, edges1, source1);
        printArray(result3, 1, n1);
        
        // 测试用例2：含负权边图（无负权回路）
        System.out.println("\n=== 测试用例2：含负权边图 ===");
        int n2 = 4;
        int[][] edges2 = {
            {1, 2, 3}, {1, 3, 5}, {2, 3, -2},
            {2, 4, 4}, {3, 4, 1}
        };
        int source2 = 1;
        
        System.out.println("Dijkstra算法结果（可能不正确）:");
        try {
            int[] result4 = dijkstra(n2, edges2, source2);
            printArray(result4, 1, n2);
        } catch (Exception e) {
            System.out.println("Dijkstra算法无法处理负权边");
        }
        
        System.out.println("Bellman-Ford算法结果:");
        int[] result5 = bellmanFord(n2, edges2, source2);
        printArray(result5, 1, n2);
        
        System.out.println("SPFA算法结果:");
        int[] result6 = spfa(n2, edges2, source2);
        printArray(result6, 1, n2);
        
        // 测试用例3：含负权回路图
        System.out.println("\n=== 测试用例3：含负权回路图 ===");
        int n3 = 3;
        int[][] edges3 = {
            {1, 2, 1}, {2, 3, -2}, {3, 1, -1}
        };
        int source3 = 1;
        
        System.out.println("Bellman-Ford算法检测负权回路:");
        try {
            int[] result7 = bellmanFord(n3, edges3, source3);
            printArray(result7, 1, n3);
        } catch (RuntimeException e) {
            System.out.println("检测到负权回路: " + e.getMessage());
        }
        
        System.out.println("SPFA算法检测负权回路:");
        try {
            int[] result8 = spfa(n3, edges3, source3);
            printArray(result8, 1, n3);
        } catch (RuntimeException e) {
            System.out.println("检测到负权回路: " + e.getMessage());
        }
    }
    
    private static void printArray(int[] arr, int start, int end) {
        for (int i = start; i <= end; i++) {
            System.out.print((arr[i] == Integer.MAX_VALUE ? "INF" : arr[i]) + " ");
        }
        System.out.println();
    }
    
    /**
     * 算法选择指南
     */
    public static void algorithmSelectionGuide() {
        System.out.println("\n=== 最短路径算法选择指南 ===");
        System.out.println("1. 如果图中所有边权重非负：");
        System.out.println("   - 优先选择Dijkstra算法（效率最高）");
        System.out.println("   - 时间复杂度：O((V+E)logV)");
        
        System.out.println("2. 如果图中包含负权边但无负权回路：");
        System.out.println("   - 选择Bellman-Ford或SPFA算法");
        System.out.println("   - Bellman-Ford：O(V*E)，实现简单");
        System.out.println("   - SPFA：平均O(E)，实际效率较高");
        
        System.out.println("3. 如果需要检测负权回路：");
        System.out.println("   - 必须使用Bellman-Ford或SPFA算法");
        System.out.println("   - Dijkstra算法无法检测负权回路");
        
        System.out.println("4. 图规模较大时：");
        System.out.println("   - 非负权图：Dijkstra算法");
        System.out.println("   - 含负权边图：SPFA算法");
        
        System.out.println("5. 特殊场景：");
        System.out.println("   - 多源最短路径：Floyd算法");
        System.out.println("   - 稀疏图：SPFA可能比Dijkstra更快");
    }
    
    // 测试主函数
    public static void main(String[] args) {
        compareAlgorithms();
        algorithmSelectionGuide();
        
        // 性能测试
        System.out.println("\n=== 性能测试建议 ===");
        System.out.println("对于V=1000, E=10000的图：");
        System.out.println("Dijkstra算法：约10000*log(1000) ≈ 100000次操作");
        System.out.println("Bellman-Ford算法：约1000*10000 = 10,000,000次操作");
        System.out.println("SPFA算法：平均约10000次操作，最坏10,000,000次操作");
    }
}