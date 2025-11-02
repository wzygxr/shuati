package class061;

import java.util.*;

// POJ 1679 The Unique MST
// 题目链接: http://poj.org/problem?id=1679
// 
// 题目描述:
// 给定一个连通的无向图，判断最小生成树是否唯一。
// 如果唯一输出最小生成树的值，如果不唯一输出"Not Unique!"。
//
// 解题思路:
// 判断最小生成树唯一性的方法：
// 1. 先用Kruskal算法求出一个最小生成树
// 2. 对于最小生成树中的每条边，尝试用其他权重相同的边替换它
// 3. 如果能找到一种替换方案使得仍然能得到最小生成树，则说明MST不唯一
// 
// 另一种更简单的方法：
// 1. 求出最小生成树的权值
// 2. 求出次小生成树的权值
// 3. 如果两者相等，则MST不唯一；否则唯一
//
// 我们使用第二种方法：
// 1. 先用Kruskal算法求出最小生成树
// 2. 记录最小生成树中任意两点间路径上的最大边权
// 3. 遍历所有不在MST中的边，尝试用它替换MST中的某条边
// 4. 计算替换后的生成树权值（原权值 + 新边权 - 被替换边权）
// 5. 找到最小的替换值，即为次小生成树的权值
//
// 时间复杂度: O(E * log E + V^2)，其中E是边数，V是顶点数
// 空间复杂度: O(V^2)
// 是否为最优解: 是，这是解决该问题的标准方法

public class Code12_TheUniqueMST {
    
    static class Edge {
        int u, v, weight;
        
        Edge(int u, int v, int weight) {
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
    
    public static String findUniqueMST(int n, int m, Edge[] edges) {
        // 按权重排序
        Arrays.sort(edges, (a, b) -> a.weight - b.weight);
        
        // 构建最小生成树
        UnionFind uf = new UnionFind(n);
        boolean[] inMST = new boolean[m]; // 标记边是否在MST中
        List<Integer>[] adj = new List[n]; // 邻接表表示MST
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }
        
        int mstCost = 0;
        int edgesUsed = 0;
        
        // Kruskal算法构建MST
        for (int i = 0; i < m; i++) {
            int u = edges[i].u;
            int v = edges[i].v;
            int weight = edges[i].weight;
            
            if (uf.union(u, v)) {
                inMST[i] = true;
                adj[u].add(i);
                adj[v].add(i);
                mstCost += weight;
                edgesUsed++;
                
                if (edgesUsed == n - 1) {
                    break;
                }
            }
        }
        
        // 如果无法构建生成树
        if (edgesUsed != n - 1) {
            return "Not Unique!"; // 实际上题目保证图连通，这里只是为了完整性
        }
        
        // 计算MST中任意两点间路径上的最大边权
        int[][] maxWeight = new int[n][n];
        boolean[][] visited = new boolean[n][n];
        
        // 对每个节点进行DFS，计算它到其他节点路径上的最大边权
        for (int i = 0; i < n; i++) {
            dfs(i, i, -1, 0, adj, edges, maxWeight, visited);
        }
        
        // 计算次小生成树的权值
        int secondMST = Integer.MAX_VALUE;
        for (int i = 0; i < m; i++) {
            if (!inMST[i]) { // 对于不在MST中的边
                int u = edges[i].u;
                int v = edges[i].v;
                int weight = edges[i].weight;
                
                // 用这条边替换MST中u到v路径上的最大边
                int newCost = mstCost + weight - maxWeight[u][v];
                secondMST = Math.min(secondMST, newCost);
            }
        }
        
        // 如果次小生成树的权值等于最小生成树的权值，则MST不唯一
        if (secondMST == mstCost) {
            return "Not Unique!";
        } else {
            return String.valueOf(mstCost);
        }
    }
    
    // DFS计算节点间路径上的最大边权
    private static void dfs(int start, int current, int parent, int maxEdge, List<Integer>[] adj, Edge[] edges, int[][] maxWeight, boolean[][] visited) {
        visited[start][current] = true;
        maxWeight[start][current] = maxEdge;
        
        for (int edgeIndex : adj[current]) {
            int next = (edges[edgeIndex].u == current) ? edges[edgeIndex].v : edges[edgeIndex].u;
            if (next != parent && !visited[start][next]) {
                int newMax = Math.max(maxEdge, edges[edgeIndex].weight);
                dfs(start, next, current, newMax, adj, edges, maxWeight, visited);
            }
        }
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 4, m1 = 5;
        Edge[] edges1 = {
            new Edge(0, 1, 1),
            new Edge(0, 2, 2),
            new Edge(0, 3, 3),
            new Edge(1, 2, 4),
            new Edge(2, 3, 5)
        };
        System.out.println("测试用例1结果: " + findUniqueMST(n1, m1, edges1)); // 预期输出: 6
        
        // 测试用例2
        int n2 = 3, m2 = 3;
        Edge[] edges2 = {
            new Edge(0, 1, 1),
            new Edge(1, 2, 2),
            new Edge(0, 2, 2)
        };
        System.out.println("测试用例2结果: " + findUniqueMST(n2, m2, edges2)); // 预期输出: Not Unique!
    }
}