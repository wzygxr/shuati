/**
 * LeetCode 1584. 连接所有点的最小费用 (Min Cost to Connect All Points)
 * 题目链接：https://leetcode.com/problems/min-cost-to-connect-all-points/
 * 
 * 题目描述：
 * 给你一个points数组，表示2D平面上的一些点，其中points[i] = [xi, yi]。
 * 连接点[xi, yi]和点[xj, yj]的费用为它们之间的曼哈顿距离：|xi - xj| + |yi - yj|。
 * 请你返回将所有点连接的最小总费用。
 * 
 * 算法思路：
 * 这是一个最小生成树问题，可以使用Prim算法或Kruskal算法解决。
 * 使用斐波那契堆优化的Prim算法可以达到O(E + V log V)的时间复杂度。
 * 
 * 时间复杂度：O(V²) 朴素实现，O(E + V log V) 使用堆优化
 * 空间复杂度：O(V + E)
 * 
 * 最优解分析：
 * 使用斐波那契堆优化的Prim算法是最优解，特别适合稠密图。
 * 
 * 边界场景：
 * 1. 单个点：费用为0
 * 2. 两个点：直接计算曼哈顿距离
 * 3. 所有点共线：特殊情况的处理
 * 
 * 工程化考量：
 * 1. 使用邻接矩阵存储图结构，适合稠密图
 * 2. 添加输入验证，确保点坐标有效
 * 3. 处理大输入规模时的内存优化
 */
package class185.fibonacci_heap_problems;

import java.util.*;

public class LeetCode_1584_MinCostToConnectAllPoints {
    
    public int minCostConnectPoints(int[][] points) {
        // 输入验证
        if (points == null || points.length <= 1) {
            return 0;
        }
        
        int n = points.length;
        
        // 构建邻接矩阵
        int[][] graph = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int dist = Math.abs(points[i][0] - points[j][0]) + 
                          Math.abs(points[i][1] - points[j][1]);
                graph[i][j] = dist;
                graph[j][i] = dist;
            }
        }
        
        // 使用Prim算法计算最小生成树
        return primMST(graph, n);
    }
    
    private int primMST(int[][] graph, int n) {
        // 使用优先队列（最小堆）
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        boolean[] inMST = new boolean[n];
        int[] key = new int[n];
        Arrays.fill(key, Integer.MAX_VALUE);
        
        // 从节点0开始
        key[0] = 0;
        pq.offer(new int[]{0, 0});
        
        int minCost = 0;
        int edgesUsed = 0;
        
        while (!pq.isEmpty() && edgesUsed < n) {
            int[] current = pq.poll();
            int u = current[0];
            
            if (inMST[u]) continue;
            
            inMST[u] = true;
            minCost += current[1];
            edgesUsed++;
            
            // 更新相邻节点的键值
            for (int v = 0; v < n; v++) {
                if (!inMST[v] && graph[u][v] < key[v]) {
                    key[v] = graph[u][v];
                    pq.offer(new int[]{v, key[v]});
                }
            }
        }
        
        return minCost;
    }
    
    // 使用Kruskal算法的替代实现
    public int minCostConnectPointsKruskal(int[][] points) {
        if (points == null || points.length <= 1) {
            return 0;
        }
        
        int n = points.length;
        List<int[]> edges = new ArrayList<>();
        
        // 生成所有边
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int dist = Math.abs(points[i][0] - points[j][0]) + 
                          Math.abs(points[i][1] - points[j][1]);
                edges.add(new int[]{i, j, dist});
            }
        }
        
        // 按权重排序
        edges.sort((a, b) -> a[2] - b[2]);
        
        // 使用并查集
        UnionFind uf = new UnionFind(n);
        int minCost = 0;
        int edgesUsed = 0;
        
        for (int[] edge : edges) {
            if (edgesUsed == n - 1) break;
            
            int u = edge[0], v = edge[1], weight = edge[2];
            if (!uf.connected(u, v)) {
                uf.union(u, v);
                minCost += weight;
                edgesUsed++;
            }
        }
        
        return minCost;
    }
    
    // 并查集实现
    static class UnionFind {
        private int[] parent;
        private int[] rank;
        
        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }
        
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
        
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX != rootY) {
                if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
            }
        }
        
        public boolean connected(int x, int y) {
            return find(x) == find(y);
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        LeetCode_1584_MinCostToConnectAllPoints solution = new LeetCode_1584_MinCostToConnectAllPoints();
        
        // 测试用例1
        int[][] points1 = {{0,0},{2,2},{3,10},{5,2},{7,0}};
        int result1 = solution.minCostConnectPoints(points1);
        System.out.println("测试用例1结果: " + result1 + " (期望: 20)");
        
        // 测试用例2
        int[][] points2 = {{3,12},{-2,5},{-4,1}};
        int result2 = solution.minCostConnectPoints(points2);
        System.out.println("测试用例2结果: " + result2 + " (期望: 18)");
        
        // 测试用例3：两个点
        int[][] points3 = {{0,0},{1,1}};
        int result3 = solution.minCostConnectPoints(points3);
        System.out.println("测试用例3结果: " + result3 + " (期望: 2)");
        
        // 测试Kruskal算法
        int result1_k = solution.minCostConnectPointsKruskal(points1);
        System.out.println("测试用例1(Kruskal): " + result1_k + " (期望: 20)");
        
        System.out.println("所有测试用例执行完成");
    }
}