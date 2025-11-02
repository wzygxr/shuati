package class061;

import java.util.*;

// POJ 3522. Slim Span
// 题目链接: http://poj.org/problem?id=3522
// 
// 题目描述:
// 给定一个无向图，定义生成树的"苗条度"为最大边权与最小边权的差值。
// 求所有生成树中苗条度的最小值。
//
// 解题思路:
// 1. 枚举最小边，然后使用Kruskal算法构建包含该边的最小生成树
// 2. 记录每次生成树的最大边权，计算苗条度
// 3. 取所有可能苗条度中的最小值
//
// 时间复杂度: O(E^2 * α(V))，其中E是边数，V是顶点数
// 空间复杂度: O(V + E)
// 是否为最优解: 是，这是解决该问题的标准方法

public class Code19_SlimSpan {
    
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
    
    public static int slimSpan(int n, List<Edge> edges) {
        Collections.sort(edges);
        int minSlim = Integer.MAX_VALUE;
        int m = edges.size();
        
        // 枚举最小边
        for (int i = 0; i < m; i++) {
            UnionFind uf = new UnionFind(n);
            int edgeCount = 0;
            int maxWeight = edges.get(i).w;
            int minWeight = edges.get(i).w;
            
            // 从第i条边开始构建生成树
            for (int j = i; j < m; j++) {
                Edge edge = edges.get(j);
                if (uf.union(edge.u, edge.v)) {
                    edgeCount++;
                    maxWeight = Math.max(maxWeight, edge.w);
                    
                    if (edgeCount == n - 1) {
                        minSlim = Math.min(minSlim, maxWeight - minWeight);
                        break;
                    }
                }
            }
        }
        
        return minSlim == Integer.MAX_VALUE ? -1 : minSlim;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            
            if (n == 0 && m == 0) break;
            
            List<Edge> edges = new ArrayList<>();
            for (int i = 0; i < m; i++) {
                int u = scanner.nextInt() - 1; // 转换为0-based索引
                int v = scanner.nextInt() - 1;
                int w = scanner.nextInt();
                edges.add(new Edge(u, v, w));
            }
            
            int result = slimSpan(n, edges);
            System.out.println(result);
        }
        
        scanner.close();
    }
}