package class008_AdvancedAlgorithmsAndDataStructures.closest_pair_problems;

import java.util.*;

/**
 * LeetCode 1584. 连接所有点的最小费用 (Min Cost to Connect All Points)
 * 
 * 题目来源：https://leetcode.cn/problems/min-cost-to-connect-all-points/
 * 
 * 题目描述：
 * 给你一个points数组，表示 2D 平面上的一些点，其中 points[i] = [xi, yi] 。
 * 连接点 [xi, yi] 和点 [xj, yj] 的费用为它们之间的曼哈顿距离：|xi - xj| + |yi - yj|，
 * 其中 |val| 表示 val 的绝对值。请你返回将所有点连接的最小总费用。
 * 只有任意两点之间有且仅有一条简单路径时，才认为所有点都已连接。
 * 
 * 算法思路：
 * 这是一个最小生成树（Minimum Spanning Tree, MST）问题，可以使用以下算法解决：
 * 1. Kruskal算法：使用并查集和贪心策略
 * 2. Prim算法：使用优先队列和贪心策略
 * 3. 最近点对算法的变种：通过构建完全图然后应用MST算法
 * 
 * 时间复杂度：
 * - Kruskal算法：O(E log E) = O(n² log n)，其中E是边数，n是点数
 * - Prim算法：O(E log V) = O(n² log n)，其中V是顶点数
 * - 空间复杂度：O(n²)
 * 
 * 应用场景：
 * 1. 网络设计：最小化网络连接成本
 * 2. 电路设计：最小化电路板布线长度
 * 3. 交通运输：最小化道路建设成本
 * 
 * 相关题目：
 * 1. LeetCode 1135. 最低成本联通所有城市
 * 2. LeetCode 743. 网络延迟时间
 * 3. LeetCode 612. 平面上的最短距离
 */
public class LeetCode_1584_MinCostToConnectAllPoints {
    
    /**
     * 并查集类，用于Kruskal算法中检测环
     */
    static class UnionFind {
        private int[] parent;
        private int[] rank;
        private int components;
        
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            components = n;
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }
        
        /**
         * 查找元素的根节点（带路径压缩优化）
         * @param x 元素
         * @return 根节点
         */
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // 路径压缩
            }
            return parent[x];
        }
        
        /**
         * 合并两个集合（按秩合并优化）
         * @param x 第一个元素
         * @param y 第二个元素
         * @return 如果合并成功返回true，如果已在同一集合返回false
         */
        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX == rootY) {
                return false; // 已在同一集合，不能合并
            }
            
            // 按秩合并
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            
            components--;
            return true;
        }
        
        /**
         * 获取连通分量数量
         * @return 连通分量数量
         */
        public int getComponents() {
            return components;
        }
    }
    
    /**
     * 边类，用于表示图中的边
     */
    static class Edge {
        int from;
        int to;
        int cost;
        
        public Edge(int from, int to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }
    }
    
    /**
     * 方法1：Kruskal算法解决最小生成树问题
     * 时间复杂度：O(n² log n)
     * 空间复杂度：O(n²)
     * @param points 点坐标数组
     * @return 连接所有点的最小费用
     */
    public static int minCostConnectPointsKruskal(int[][] points) {
        int n = points.length;
        if (n <= 1) return 0;
        
        // 创建所有边
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int cost = Math.abs(points[i][0] - points[j][0]) + Math.abs(points[i][1] - points[j][1]);
                edges.add(new Edge(i, j, cost));
            }
        }
        
        // 按费用排序边
        edges.sort((a, b) -> Integer.compare(a.cost, b.cost));
        
        // 使用并查集构建最小生成树
        UnionFind uf = new UnionFind(n);
        int totalCost = 0;
        
        for (Edge edge : edges) {
            if (uf.union(edge.from, edge.to)) {
                totalCost += edge.cost;
                // 如果所有点都已连接，提前结束
                if (uf.getComponents() == 1) {
                    break;
                }
            }
        }
        
        return totalCost;
    }
    
    /**
     * 方法2：Prim算法解决最小生成树问题
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n)
     * @param points 点坐标数组
     * @return 连接所有点的最小费用
     */
    public static int minCostConnectPointsPrim(int[][] points) {
        int n = points.length;
        if (n <= 1) return 0;
        
        // 使用优先队列存储边（费用，目标点）
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        
        // 记录已访问的点
        boolean[] visited = new boolean[n];
        int totalCost = 0;
        int edgesUsed = 0;
        
        // 从第一个点开始
        pq.offer(new int[]{0, 0}); // {cost, pointIndex}
        
        while (!pq.isEmpty() && edgesUsed < n) {
            int[] current = pq.poll();
            int cost = current[0];
            int pointIndex = current[1];
            
            // 如果点已访问，跳过
            if (visited[pointIndex]) {
                continue;
            }
            
            // 标记点为已访问
            visited[pointIndex] = true;
            totalCost += cost;
            edgesUsed++;
            
            // 添加与当前点相连的所有边到优先队列
            for (int i = 0; i < n; i++) {
                if (!visited[i]) {
                    int edgeCost = Math.abs(points[pointIndex][0] - points[i][0]) + 
                                  Math.abs(points[pointIndex][1] - points[i][1]);
                    pq.offer(new int[]{edgeCost, i});
                }
            }
        }
        
        return totalCost;
    }
    
    /**
     * 方法3：优化的Prim算法（使用距离数组）
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n)
     * @param points 点坐标数组
     * @return 连接所有点的最小费用
     */
    public static int minCostConnectPointsOptimizedPrim(int[][] points) {
        int n = points.length;
        if (n <= 1) return 0;
        
        // 距离数组，记录每个点到已构建MST的最小距离
        int[] minDist = new int[n];
        Arrays.fill(minDist, Integer.MAX_VALUE);
        
        // 记录已访问的点
        boolean[] visited = new boolean[n];
        int totalCost = 0;
        
        // 从第一个点开始
        minDist[0] = 0;
        
        for (int i = 0; i < n; i++) {
            // 找到距离最小的未访问点
            int currentPoint = -1;
            for (int j = 0; j < n; j++) {
                if (!visited[j] && (currentPoint == -1 || minDist[j] < minDist[currentPoint])) {
                    currentPoint = j;
                }
            }
            
            // 标记点为已访问并累加费用
            visited[currentPoint] = true;
            totalCost += minDist[currentPoint];
            
            // 更新其他点到已构建MST的最小距离
            for (int j = 0; j < n; j++) {
                if (!visited[j]) {
                    int cost = Math.abs(points[currentPoint][0] - points[j][0]) + 
                              Math.abs(points[currentPoint][1] - points[j][1]);
                    minDist[j] = Math.min(minDist[j], cost);
                }
            }
        }
        
        return totalCost;
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        System.out.println("=== 测试 LeetCode 1584. 连接所有点的最小费用 ===");
        
        // 测试用例1
        int[][] points1 = {{0,0},{2,2},{3,10},{5,2},{7,0}};
        System.out.println("测试用例1:");
        System.out.println("点集: " + Arrays.deepToString(points1));
        System.out.println("Kruskal算法结果: " + minCostConnectPointsKruskal(points1));
        System.out.println("Prim算法结果: " + minCostConnectPointsPrim(points1));
        System.out.println("优化Prim算法结果: " + minCostConnectPointsOptimizedPrim(points1));
        System.out.println("期望结果: 20");
        System.out.println();
        
        // 测试用例2
        int[][] points2 = {{3,12},{-2,5},{-4,1}};
        System.out.println("测试用例2:");
        System.out.println("点集: " + Arrays.deepToString(points2));
        System.out.println("Kruskal算法结果: " + minCostConnectPointsKruskal(points2));
        System.out.println("Prim算法结果: " + minCostConnectPointsPrim(points2));
        System.out.println("优化Prim算法结果: " + minCostConnectPointsOptimizedPrim(points2));
        System.out.println("期望结果: 18");
        System.out.println();
        
        // 测试用例3：边界情况
        int[][] points3 = {{0,0}};
        System.out.println("测试用例3 (单点):");
        System.out.println("点集: " + Arrays.deepToString(points3));
        System.out.println("Kruskal算法结果: " + minCostConnectPointsKruskal(points3));
        System.out.println("Prim算法结果: " + minCostConnectPointsPrim(points3));
        System.out.println("优化Prim算法结果: " + minCostConnectPointsOptimizedPrim(points3));
        System.out.println("期望结果: 0");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        Random random = new Random(42);
        int n = 1000;
        int[][] points = new int[n][2];
        for (int i = 0; i < n; i++) {
            points[i][0] = random.nextInt(1000000) - 500000; // [-500000, 499999]
            points[i][1] = random.nextInt(1000000) - 500000; // [-500000, 499999]
        }
        
        long startTime = System.nanoTime();
        int result1 = minCostConnectPointsKruskal(points);
        long endTime = System.nanoTime();
        System.out.println("Kruskal算法处理" + n + "个点时间: " + (endTime - startTime) / 1_000_000.0 + " ms, 结果: " + result1);
        
        startTime = System.nanoTime();
        int result2 = minCostConnectPointsPrim(points);
        endTime = System.nanoTime();
        System.out.println("Prim算法处理" + n + "个点时间: " + (endTime - startTime) / 1_000_000.0 + " ms, 结果: " + result2);
        
        startTime = System.nanoTime();
        int result3 = minCostConnectPointsOptimizedPrim(points);
        endTime = System.nanoTime();
        System.out.println("优化Prim算法处理" + n + "个点时间: " + (endTime - startTime) / 1_000_000.0 + " ms, 结果: " + result3);
    }
}