package class061;

import java.util.*;

// UVa 10034 Freckles
// 题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=12&page=show_problem&problem=975
// 
// 题目描述:
// 在你的脸上有一些雀斑，你可以把它们看作是二维平面上的一些点。
// 你的任务是用一些直线将这些点连接起来，使得：
// 1. 每个点都至少被一条直线连接
// 2. 任意两个点之间都存在一条路径（直接或间接）
// 3. 使用的直线总长度最小
//
// 解题思路:
// 这是一个标准的最小生成树问题：
// 1. 将每个雀斑看作图中的一个节点
// 2. 任意两个雀斑之间都有一条边，权重为它们之间的欧几里得距离
// 3. 求这个完全图的最小生成树
// 4. 返回MST中所有边的权重之和
//
// 我们使用Kruskal算法：
// 1. 计算所有点对之间的距离，构造成边
// 2. 将所有边按权重升序排序
// 3. 使用并查集判断添加边是否会形成环
// 4. 依次选择不形成环的最小边，直到选择了n-1条边
//
// 时间复杂度: O(N^2 * log(N))，其中N是点的数量
// 空间复杂度: O(N^2)，用于存储所有边
// 是否为最优解: 是，这是解决该问题的标准方法

public class Code14_Freckles {
    
    static class Point {
        double x, y;
        
        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        // 计算到另一个点的距离
        double distance(Point other) {
            double dx = this.x - other.x;
            double dy = this.y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }
    }
    
    static class Edge {
        int u, v;
        double weight;
        
        Edge(int u, int v, double weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }
    }
    
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
    
    public static double freckles(Point[] points) {
        int n = points.length;
        
        // 如果只有一个点，不需要连接
        if (n <= 1) {
            return 0.0;
        }
        
        // 构造所有边
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dist = points[i].distance(points[j]);
                edges.add(new Edge(i, j, dist));
            }
        }
        
        // 按权重升序排序所有边
        Collections.sort(edges, (a, b) -> Double.compare(a.weight, b.weight));
        
        // 初始化并查集
        UnionFind uf = new UnionFind(n);
        
        double totalCost = 0.0;
        int edgesUsed = 0;
        
        // 遍历所有边
        for (Edge edge : edges) {
            int u = edge.u;
            int v = edge.v;
            double weight = edge.weight;
            
            // 如果两个点不在同一集合中，说明连接它们不会形成环
            if (uf.union(u, v)) {
                totalCost += weight;
                edgesUsed++;
                
                // 如果已经选择了n-1条边，则已形成最小生成树
                if (edgesUsed == n - 1) {
                    break;
                }
            }
        }
        
        return totalCost;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        Point[] points1 = {
            new Point(1.0, 1.0),
            new Point(2.0, 2.0),
            new Point(2.0, 4.0)
        };
        System.out.printf("测试用例1结果: %.2f\n", freckles(points1)); // 预期输出: 3.41
        
        // 测试用例2
        Point[] points2 = {
            new Point(1.0, 1.0),
            new Point(2.0, 2.0),
            new Point(3.0, 3.0),
            new Point(4.0, 4.0)
        };
        System.out.printf("测试用例2结果: %.2f\n", freckles(points2)); // 预期输出: 4.24
    }
}