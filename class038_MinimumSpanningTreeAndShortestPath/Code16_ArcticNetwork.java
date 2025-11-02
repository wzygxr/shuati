package class061;

import java.util.*;

// UVa 10369. Arctic Network
// 题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=15&page=show_problem&problem=1310
// 
// 题目描述:
// 北极的哨所之间需要建立通信网络。有两种通信方式：
// 1. 无线电通信：距离不超过D
// 2. 卫星通信：不受距离限制，但只有S个卫星频道
// 求最小的D，使得所有哨所都能通信。
//
// 解题思路:
// 与无线通讯网类似的问题：
// 1. 构建完全图，边权为哨所之间的距离
// 2. 使用Kruskal算法计算最小生成树
// 3. 由于有S个卫星频道，可以省去S-1条最长的边
// 4. 最小生成树中第P-S大的边权就是答案
//
// 时间复杂度: O(P^2 * log P)，其中P是哨所数量
// 空间复杂度: O(P^2)
// 是否为最优解: 是，这是解决该问题的标准方法

public class Code16_ArcticNetwork {
    
    static class UnionFind {
        private int[] parent;
        private int[] rank;
        
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }
        
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
        
        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) return false;
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
    
    static class Point {
        int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
    
    public static double arcticNetwork(int s, int p, Point[] outposts) {
        // 构建所有边
        List<double[]> edges = new ArrayList<>();
        
        for (int i = 0; i < p; i++) {
            for (int j = i + 1; j < p; j++) {
                double dist = distance(outposts[i], outposts[j]);
                edges.add(new double[]{dist, i, j});
            }
        }
        
        // 按距离排序
        edges.sort((a, b) -> Double.compare(a[0], b[0]));
        
        UnionFind uf = new UnionFind(p);
        List<Double> mstEdges = new ArrayList<>();
        
        // 构建最小生成树
        for (double[] edge : edges) {
            double dist = edge[0];
            int u = (int) edge[1];
            int v = (int) edge[2];
            
            if (uf.union(u, v)) {
                mstEdges.add(dist);
            }
        }
        
        // 有S个卫星频道，可以省去S-1条最长的边
        return mstEdges.get(p - s - 1);
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        
        while (n-- > 0) {
            int s = scanner.nextInt();
            int p = scanner.nextInt();
            
            Point[] outposts = new Point[p];
            for (int i = 0; i < p; i++) {
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                outposts[i] = new Point(x, y);
            }
            
            double result = arcticNetwork(s, p, outposts);
            System.out.printf("%.2f\n", result);
        }
        
        scanner.close();
    }
}