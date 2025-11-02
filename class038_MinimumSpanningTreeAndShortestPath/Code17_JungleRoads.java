package class061;

import java.util.*;

// POJ 1251. Jungle Roads
// 题目链接: http://poj.org/problem?id=1251
// 
// 题目描述:
// 热带岛屿上的村庄之间需要修建道路。每个村庄用大写字母表示。
// 输入给出每个村庄到其他村庄的道路修建成本。
// 求连接所有村庄的最小成本。
//
// 解题思路:
// 标准的最小生成树问题：
// 1. 将村庄看作图中的节点
// 2. 将道路修建成本看作边的权重
// 3. 使用Kruskal或Prim算法计算最小生成树
//
// 时间复杂度: O(E log E)，其中E是边数
// 空间复杂度: O(V + E)，其中V是顶点数
// 是否为最优解: 是，这是解决该问题的标准方法

public class Code17_JungleRoads {
    
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
    
    public static int jungleRoads(int n, List<Edge> edges) {
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
        
        while (true) {
            int n = scanner.nextInt();
            if (n == 0) break;
            
            List<Edge> edges = new ArrayList<>();
            
            for (int i = 0; i < n - 1; i++) {
                char village = scanner.next().charAt(0);
                int k = scanner.nextInt();
                
                int u = village - 'A';
                
                for (int j = 0; j < k; j++) {
                    char neighbor = scanner.next().charAt(0);
                    int cost = scanner.nextInt();
                    
                    int v = neighbor - 'A';
                    edges.add(new Edge(u, v, cost));
                }
            }
            
            int result = jungleRoads(n, edges);
            System.out.println(result);
        }
        
        scanner.close();
    }
}