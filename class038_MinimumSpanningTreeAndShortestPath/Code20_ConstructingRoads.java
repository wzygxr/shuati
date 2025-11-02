package class061;

import java.util.*;

// POJ 2421. Constructing Roads
// 题目链接: http://poj.org/problem?id=2421
// 
// 题目描述:
// 有N个村庄，已知所有村庄之间的距离。
// 有些村庄之间已经存在道路。
// 求连接所有村庄的最小成本。
//
// 解题思路:
// 标准的最小生成树问题，但部分边已经存在：
// 1. 将已存在的边的权重设为0
// 2. 使用Kruskal算法计算最小生成树
// 3. 已存在的边会被优先选择（权重为0）
//
// 时间复杂度: O(E log E)，其中E是边数
// 空间复杂度: O(V + E)
// 是否为最优解: 是，这是解决该问题的标准方法

public class Code20_ConstructingRoads {
    
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
    
    static class Edge implements Comparable<Edge> {
        int u, v, w;
        Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
        
        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.w, other.w);
        }
    }
    
    public static int constructingRoads(int n, int[][] dist, List<int[]> existingRoads) {
        List<Edge> edges = new ArrayList<>();
        
        // 构建所有可能的边
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                edges.add(new Edge(i, j, dist[i][j]));
            }
        }
        
        // 将已存在道路的权重设为0
        for (int[] road : existingRoads) {
            int u = road[0] - 1; // 转换为0-based索引
            int v = road[1] - 1;
            // 找到对应的边并设置权重为0
            for (Edge edge : edges) {
                if ((edge.u == u && edge.v == v) || (edge.u == v && edge.v == u)) {
                    edge.w = 0;
                    break;
                }
            }
        }
        
        Collections.sort(edges);
        
        UnionFind uf = new UnionFind(n);
        int totalCost = 0;
        int edgesUsed = 0;
        
        for (Edge edge : edges) {
            if (uf.union(edge.u, edge.v)) {
                totalCost += edge.w;
                edgesUsed++;
                
                if (edgesUsed == n - 1) {
                    break;
                }
            }
        }
        
        return totalCost;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int n = scanner.nextInt();
        int[][] dist = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = scanner.nextInt();
            }
        }
        
        int q = scanner.nextInt();
        List<int[]> existingRoads = new ArrayList<>();
        for (int i = 0; i < q; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            existingRoads.add(new int[]{u, v});
        }
        
        int result = constructingRoads(n, dist, existingRoads);
        System.out.println(result);
        
        scanner.close();
    }
}