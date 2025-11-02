package class061;

import java.util.Arrays;

// LeetCode 1584. Min Cost to Connect All Points
// 题目链接: https://leetcode.cn/problems/min-cost-to-connect-all-points/
// 
// 题目描述:
// 给你一个points数组，表示 2D 平面上的一些点，其中 points[i] = [xi, yi]。
// 连接点 [xi, yi] 和点 [xj, yj] 的费用为它们之间的曼哈顿距离：|xi - xj| + |yi - yj|，
// 其中 |val| 表示 val 的绝对值。请你返回将所有点连接的最小总费用。
// 只有任意两点之间有且仅有一条简单路径时，才认为所有点都已连接。
//
// 解题思路:
// 这是一个典型的最小生成树问题，但与传统MST问题不同的是，这里没有直接给出边，
// 而是给出了点的坐标，需要我们计算任意两点之间的曼哈顿距离作为边的权重。
// 使用Kruskal算法：
// 1. 计算所有点对之间的曼哈顿距离，构造成边
// 2. 将所有边按权重升序排序
// 3. 使用并查集判断添加边是否会形成环
// 4. 依次选择不形成环的最小边，直到选择了n-1条边
//
// 时间复杂度: O(N^2 * log(N))，其中N是点的数量
// 空间复杂度: O(N^2)，用于存储所有边
// 是否为最优解: 是，这是解决该问题的标准方法

public class Code07_MinCostToConnectAllPoints {
    
    public static int minCostConnectPoints(int[][] points) {
        int n = points.length;
        
        // 如果只有一个点，不需要连接
        if (n <= 1) {
            return 0;
        }
        
        // 构造所有边，edges[i][0]和edges[i][1]是点的索引，edges[i][2]是曼哈顿距离
        int[][] edges = new int[n * (n - 1) / 2][3];
        int idx = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int dist = Math.abs(points[i][0] - points[j][0]) + Math.abs(points[i][1] - points[j][1]);
                edges[idx][0] = i;
                edges[idx][1] = j;
                edges[idx][2] = dist;
                idx++;
            }
        }
        
        // 按权重升序排序所有边
        Arrays.sort(edges, (a, b) -> a[2] - b[2]);
        
        // 初始化并查集
        UnionFind uf = new UnionFind(n);
        
        int totalCost = 0;
        int edgesUsed = 0;
        
        // 遍历所有边
        for (int[] edge : edges) {
            int point1 = edge[0];
            int point2 = edge[1];
            int cost = edge[2];
            
            // 如果两个点不在同一集合中，说明连接它们不会形成环
            if (uf.union(point1, point2)) {
                totalCost += cost;
                edgesUsed++;
                
                // 如果已经选择了n-1条边，则已形成最小生成树
                if (edgesUsed == n - 1) {
                    return totalCost;
                }
            }
        }
        
        return totalCost;
    }
    
    // 并查集数据结构实现
    static class UnionFind {
        private int[] parent;
        private int[] rank;
        
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            
            // 初始化，每个节点的父节点是自己
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }
        
        // 查找根节点（带路径压缩优化）
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // 路径压缩
            }
            return parent[x];
        }
        
        // 合并两个集合（按秩合并优化）
        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            // 如果已经在同一集合中，返回false
            if (rootX == rootY) {
                return false;
            }
            
            // 按秩合并，将秩小的树合并到秩大的树下
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            
            return true;
        }
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int[][] points1 = {{0, 0}, {2, 2}, {3, 10}, {5, 2}, {7, 0}};
        System.out.println("测试用例1结果: " + minCostConnectPoints(points1)); // 预期输出: 20
        
        // 测试用例2
        int[][] points2 = {{3, 12}, {-2, 5}, {-4, 1}};
        System.out.println("测试用例2结果: " + minCostConnectPoints(points2)); // 预期输出: 18
        
        // 测试用例3
        int[][] points3 = {{0, 0}, {1, 1}, {1, 0}, {-1, 1}};
        System.out.println("测试用例3结果: " + minCostConnectPoints(points3)); // 预期输出: 4
    }
}