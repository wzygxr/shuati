package class064;

import java.util.*;

/**
 * 次短路径问题
 * 
 * 题目：严格次短路径（Strictly Second Shortest Path）
 * 来源：洛谷 P2865 [USACO06NOV] Roadblocks G
 * 链接：https://www.luogu.com.cn/problem/P2865
 * 
 * 题目描述：
 * 贝茜把家搬到了一个小农场，但她常常回到FJ的农场去拜访她的朋友。
 * 贝茜很喜欢路边的风景，不想那么快地结束她的旅途，于是她每次回农场，都会选择第二短的路径。
 * 贝茜的乡村有R条双向道路，每条路都连接了所有的N个农场中的某两个。
 * 贝茜在1号农场，她的朋友们在N号农场。
 * 假设次短路径长度严格大于最短路径长度，求次短路径的长度。
 * 
 * 解题思路：
 * 1. 方法1：删除最短路径上的边，重新计算最短路径
 * 2. 方法2：维护两个距离数组：最短距离和次短距离
 * 3. 方法3：使用A*算法寻找第K短路
 * 
 * 算法应用场景：
 * - 交通导航中的备选路线规划
 * - 网络路由中的路径多样性
 * - 机器人路径规划中的备选路径
 * 
 * 时间复杂度分析：
 * - 方法1：O(E * (V+E)logV)，效率较低
 * - 方法2：O((V+E)logV)，效率较高
 * - 方法3：O(E*K*log(E*K))，适合第K短路
 */
public class Code29_SecondShortestPath {
    
    /**
     * 方法1：删除最短路径上的边，重新计算最短路径
     * 
     * 算法步骤：
     * 1. 首先计算最短路径和路径上的边
     * 2. 对于最短路径上的每条边，删除后重新计算最短路径
     * 3. 取所有重新计算的最短路径中的最小值作为次短路径
     * 
     * 时间复杂度：O(E * (V+E)logV)
     * 空间复杂度：O(V+E)
     * 
     * @param n 节点总数
     * @param edges 边列表，格式为 [u, v, w]
     * @param source 源点
     * @param target 目标点
     * @return 次短路径长度，如果不存在返回-1
     */
    public static int secondShortestPath1(int n, int[][] edges, int source, int target) {
        // 首先计算最短路径和路径上的边
        List<Integer> shortestPath = findShortestPath(n, edges, source, target);
        if (shortestPath.isEmpty()) {
            return -1; // 无法到达目标点
        }
        
        // 提取最短路径上的边
        Set<String> pathEdges = new HashSet<>();
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            int u = shortestPath.get(i);
            int v = shortestPath.get(i + 1);
            pathEdges.add(u + "," + v);
            pathEdges.add(v + "," + u); // 无向图
        }
        
        int secondShortest = Integer.MAX_VALUE;
        
        // 对于最短路径上的每条边，删除后重新计算最短路径
        for (String edgeStr : pathEdges) {
            String[] parts = edgeStr.split(",");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            
            // 创建删除该边后的新边列表
            List<int[]> newEdges = new ArrayList<>();
            for (int[] edge : edges) {
                if ((edge[0] == u && edge[1] == v) || (edge[0] == v && edge[1] == u)) {
                    continue; // 跳过被删除的边
                }
                newEdges.add(edge);
            }
            
            // 重新计算最短路径
            int newDist = dijkstra(n, newEdges.toArray(new int[0][0]), source, target);
            if (newDist != Integer.MAX_VALUE && newDist > getShortestDistance(n, edges, source, target)) {
                secondShortest = Math.min(secondShortest, newDist);
            }
        }
        
        return secondShortest == Integer.MAX_VALUE ? -1 : secondShortest;
    }
    
    /**
     * 方法2：维护两个距离数组（最优解法）
     * 
     * 算法步骤：
     * 1. 维护两个距离数组：dist1（最短距离）和dist2（次短距离）
     * 2. 使用优先队列，每个节点可能被访问两次（最短和次短）
     * 3. 对于每个邻居节点，更新最短和次短距离
     * 
     * 时间复杂度：O((V+E)logV)
     * 空间复杂度：O(V+E)
     */
    public static int secondShortestPath2(int n, int[][] edges, int source, int target) {
        // 构建邻接表（无向图）
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            graph.get(u).add(new int[]{v, w});
            graph.get(v).add(new int[]{u, w});
        }
        
        // 最短距离数组
        int[] dist1 = new int[n + 1];
        Arrays.fill(dist1, Integer.MAX_VALUE);
        dist1[source] = 0;
        
        // 次短距离数组
        int[] dist2 = new int[n + 1];
        Arrays.fill(dist2, Integer.MAX_VALUE);
        
        // 优先队列，存储(距离, 节点)
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        heap.offer(new int[]{0, source});
        
        while (!heap.isEmpty()) {
            int[] record = heap.poll();
            int d = record[0];
            int u = record[1];
            
            // 如果当前距离大于次短距离，跳过
            if (d > dist2[u]) {
                continue;
            }
            
            for (int[] edge : graph.get(u)) {
                int v = edge[0];
                int w = edge[1];
                int newDist = d + w;
                
                if (newDist < dist1[v]) {
                    // 发现更短路径，更新最短距离
                    dist2[v] = dist1[v]; // 原来的最短距离变为次短
                    dist1[v] = newDist;
                    heap.offer(new int[]{newDist, v});
                } else if (newDist > dist1[v] && newDist < dist2[v]) {
                    // 发现次短路径
                    dist2[v] = newDist;
                    heap.offer(new int[]{newDist, v});
                }
            }
        }
        
        return dist2[target] == Integer.MAX_VALUE ? -1 : dist2[target];
    }
    
    /**
     * 方法3：A*算法寻找第K短路（通用解法）
     * 
     * 算法步骤：
     * 1. 使用A*算法寻找第2短路
     * 2. 需要预先计算终点到所有节点的最短距离作为启发式函数
     * 
     * 时间复杂度：O(E*K*log(E*K))
     * 空间复杂度：O(V+E)
     */
    public static int secondShortestPath3(int n, int[][] edges, int source, int target) {
        return findKthShortestPath(n, edges, source, target, 2);
    }
    
    // 辅助方法：计算最短路径
    private static List<Integer> findShortestPath(int n, int[][] edges, int source, int target) {
        // 构建邻接表
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            graph.get(u).add(new int[]{v, w});
            graph.get(v).add(new int[]{u, w});
        }
        
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;
        
        int[] parent = new int[n + 1];
        Arrays.fill(parent, -1);
        
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
                    parent[v] = u;
                    heap.offer(new int[]{v, dist[v]});
                }
            }
        }
        
        // 重构路径
        List<Integer> path = new ArrayList<>();
        if (dist[target] == Integer.MAX_VALUE) {
            return path;
        }
        
        int current = target;
        while (current != -1) {
            path.add(current);
            current = parent[current];
        }
        Collections.reverse(path);
        return path;
    }
    
    // 辅助方法：Dijkstra算法计算最短距离
    private static int dijkstra(int n, int[][] edges, int source, int target) {
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            graph.get(u).add(new int[]{v, w});
            graph.get(v).add(new int[]{u, w});
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
        
        return dist[target];
    }
    
    // 辅助方法：获取最短距离
    private static int getShortestDistance(int n, int[][] edges, int source, int target) {
        return dijkstra(n, edges, source, target);
    }
    
    // 辅助方法：A*算法寻找第K短路
    private static int findKthShortestPath(int n, int[][] edges, int source, int target, int K) {
        // 构建原图和反向图
        List<List<int[]>> graph = new ArrayList<>();
        List<List<int[]>> reverseGraph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
            reverseGraph.add(new ArrayList<>());
        }
        
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            graph.get(u).add(new int[]{v, w});
            reverseGraph.get(v).add(new int[]{u, w});
        }
        
        // 计算启发式函数（终点到各点的最短距离）
        int[] heuristic = dijkstraHeuristic(n, reverseGraph, target);
        
        // A*算法寻找第K短路
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        heap.offer(new int[]{heuristic[source], 0, source});
        
        int[] count = new int[n + 1];
        
        while (!heap.isEmpty()) {
            int[] record = heap.poll();
            int estimated = record[0];
            int currentDist = record[1];
            int u = record[2];
            
            if (u == target) {
                count[u]++;
                if (count[u] == K) {
                    return currentDist;
                }
            }
            
            if (count[u] > K) continue;
            count[u]++;
            
            for (int[] edge : graph.get(u)) {
                int v = edge[0], w = edge[1];
                int newDist = currentDist + w;
                int newEstimated = newDist + heuristic[v];
                heap.offer(new int[]{newEstimated, newDist, v});
            }
        }
        
        return -1;
    }
    
    // 辅助方法：计算启发式函数
    private static int[] dijkstraHeuristic(int n, List<List<int[]>> graph, int target) {
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[target] = 0;
        
        boolean[] visited = new boolean[n + 1];
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        heap.offer(new int[]{target, 0});
        
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
     * 测试用例
     */
    public static void main(String[] args) {
        // 测试用例1：洛谷P2865示例
        System.out.println("=== 测试用例1：严格次短路径 ===");
        int n1 = 4;
        int[][] edges1 = {
            {1, 2, 100}, {2, 4, 200}, {2, 3, 250}, {3, 4, 100}
        };
        int source1 = 1, target1 = 4;
        
        System.out.println("方法1结果（删除边法）: " + secondShortestPath1(n1, edges1, source1, target1));
        System.out.println("方法2结果（双距离数组）: " + secondShortestPath2(n1, edges1, source1, target1));
        System.out.println("方法3结果（A*算法）: " + secondShortestPath3(n1, edges1, source1, target1));
        
        // 算法分析
        System.out.println("\n=== 算法分析 ===");
        System.out.println("方法1：删除边法");
        System.out.println("  - 优点：思路简单直观");
        System.out.println("  - 缺点：效率较低，O(E * (V+E)logV)");
        System.out.println("  - 适用：小规模图");
        
        System.out.println("方法2：双距离数组法");
        System.out.println("  - 优点：效率高，O((V+E)logV)");
        System.out.println("  - 缺点：实现稍复杂");
        System.out.println("  - 适用：大规模图，推荐使用");
        
        System.out.println("方法3：A*算法");
        System.out.println("  - 优点：通用性强，可求第K短路");
        System.out.println("  - 缺点：需要启发式函数，实现复杂");
        System.out.println("  - 适用：需要第K短路的场景");
    }
}