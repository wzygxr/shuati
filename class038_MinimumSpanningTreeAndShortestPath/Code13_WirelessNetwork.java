package class061;

import java.util.*;

// 洛谷P1991 无线通讯网
// 题目链接: https://www.luogu.com.cn/problem/P1991
// 
// 题目描述:
// 国防部计划用无线网络连接若干个边防哨所。2 种不同的通讯技术用来搭建无线网络；
// 每个边防哨所都要配备一台无线接收机，对于任意两个哨所，如果它们的距离不超过D就能直接通讯，
// 否则必须借助卫星电话。现在有S台卫星电话，请你分配这S台卫星电话，使得任意两个哨所都能通讯。
// 返回D的最小值。
//
// 解题思路:
// 这个问题可以转化为最小生成树问题：
// 1. 如果两个哨所之间的距离不超过D，它们可以直接通讯
// 2. 我们有S台卫星电话，可以连接任意两个哨所
// 3. 要使得所有哨所都能通讯，我们需要构建一个连通图
// 4. 使用卫星电话可以减少需要直接通讯的边数
// 5. 如果我们有S台卫星电话，我们可以减少S-1条最大权值的边
// 6. 因此，我们需要找到最小生成树中第(P-S)大的边权值
//
// 具体步骤：
// 1. 计算所有哨所之间的距离
// 2. 构建完全图，边权为距离
// 3. 求最小生成树
// 4. 在MST的n-1条边中，第(n-1-(S-1)) = (n-S)大的边就是答案
//
// 时间复杂度: O(P^2 * log(P))，其中P是哨所数量
// 空间复杂度: O(P^2)
// 是否为最优解: 是，这是解决该问题的标准方法

public class Code13_WirelessNetwork {
    
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
    
    public static double wirelessNetwork(int s, Point[] points) {
        int p = points.length;
        
        // 如果卫星电话数量大于等于哨所数量-1，不需要直接通讯
        if (s >= p - 1) {
            return 0.0;
        }
        
        // 构建所有边
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < p; i++) {
            for (int j = i + 1; j < p; j++) {
                double dist = points[i].distance(points[j]);
                edges.add(new Edge(i, j, dist));
            }
        }
        
        // 按权重排序
        Collections.sort(edges, (a, b) -> Double.compare(a.weight, b.weight));
        
        // 使用Kruskal算法构建最小生成树
        UnionFind uf = new UnionFind(p);
        List<Double> mstEdges = new ArrayList<>(); // 存储MST中的边权值
        
        for (Edge edge : edges) {
            if (uf.union(edge.u, edge.v)) {
                mstEdges.add(edge.weight);
                if (mstEdges.size() == p - 1) {
                    break;
                }
            }
        }
        
        // 我们有s个卫星电话，可以省去s-1条最大的边
        // 所以答案是第(p-1-(s-1)) = (p-s)大的边
        Collections.sort(mstEdges);
        // 确保索引不超出范围
        int index = p - 1 - (s - 1);
        if (index >= mstEdges.size()) {
            index = mstEdges.size() - 1;
        }
        return mstEdges.get(index);
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int s1 = 2;
        Point[] points1 = {
            new Point(0, 100),
            new Point(0, 300),
            new Point(0, 600),
            new Point(150, 750)
        };
        System.out.printf("测试用例1结果: %.2f\n", wirelessNetwork(s1, points1)); // 预期输出: 212.13
        
        // 测试用例2
        int s2 = 1;
        Point[] points2 = {
            new Point(0, 1),
            new Point(0, 2),
            new Point(0, 4),
            new Point(0, 8)
        };
        System.out.printf("测试用例2结果: %.2f\n", wirelessNetwork(s2, points2)); // 预期输出: 7.00
    }
}