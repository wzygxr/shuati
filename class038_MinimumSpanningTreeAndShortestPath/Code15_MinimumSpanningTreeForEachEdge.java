package class061;

import java.util.*;

// Codeforces 609E. Minimum spanning tree for each edge
// 题目链接: https://codeforces.com/problemset/problem/609/E
// 
// 题目描述:
// 给定一个带权无向连通图，对于图中的每条边，计算包含该边的最小生成树的权值。
// 如果包含该边后图不连通，输出-1。
//
// 解题思路:
// 1. 首先计算原图的最小生成树MST
// 2. 对于每条边e：
//    - 如果e在MST中，那么包含e的最小生成树权值就是MST权值
//    - 如果e不在MST中，那么需要找到MST中连接e两端点的路径上的最大边权
//    - 用e替换这条最大边，得到的新生成树权值为MST权值 - 最大边权 + e的权值
// 3. 使用LCA和树上倍增算法快速查询任意两点间路径的最大边权
//
// 时间复杂度: O((n + m) log n)，其中n是顶点数，m是边数
// 空间复杂度: O(n log n)
// 是否为最优解: 是，这是解决该问题的标准方法

public class Code15_MinimumSpanningTreeForEachEdge {
    
    static class Edge {
        int u, v, w, id;
        Edge(int u, int v, int w, int id) {
            this.u = u;
            this.v = v;
            this.w = w;
            this.id = id;
        }
    }
    
    static class UnionFind {
        private int[] parent;
        private int[] rank;
        
        public UnionFind(int n) {
            parent = new int[n + 1];
            rank = new int[n + 1];
            for (int i = 1; i <= n; i++) {
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
    
    static final int LOG = 20;
    static List<int[]>[] tree;
    static int[] depth;
    static int[][] up;
    static int[][] maxEdge;
    
    public static void dfs(int u, int p, int w) {
        depth[u] = depth[p] + 1;
        up[u][0] = p;
        maxEdge[u][0] = w;
        
        for (int i = 1; i < LOG; i++) {
            up[u][i] = up[up[u][i-1]][i-1];
            maxEdge[u][i] = Math.max(maxEdge[u][i-1], maxEdge[up[u][i-1]][i-1]);
        }
        
        for (int[] edge : tree[u]) {
            int v = edge[0];
            int weight = edge[1];
            if (v != p) {
                dfs(v, u, weight);
            }
        }
    }
    
    public static int queryMaxEdge(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        int maxW = 0;
        
        // 将u提升到与v同一深度
        for (int i = LOG - 1; i >= 0; i--) {
            if (depth[u] - (1 << i) >= depth[v]) {
                maxW = Math.max(maxW, maxEdge[u][i]);
                u = up[u][i];
            }
        }
        
        if (u == v) return maxW;
        
        // 同时提升u和v直到它们的父节点相同
        for (int i = LOG - 1; i >= 0; i--) {
            if (up[u][i] != up[v][i]) {
                maxW = Math.max(maxW, maxEdge[u][i]);
                maxW = Math.max(maxW, maxEdge[v][i]);
                u = up[u][i];
                v = up[v][i];
            }
        }
        
        maxW = Math.max(maxW, maxEdge[u][0]);
        maxW = Math.max(maxW, maxEdge[v][0]);
        
        return maxW;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        
        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            int w = scanner.nextInt();
            edges[i] = new Edge(u, v, w, i);
        }
        
        // 按权重排序用于Kruskal
        Edge[] sortedEdges = edges.clone();
        Arrays.sort(sortedEdges, (a, b) -> Integer.compare(a.w, b.w));
        
        UnionFind uf = new UnionFind(n);
        long mstWeight = 0;
        boolean[] inMST = new boolean[m];
        
        tree = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) {
            tree[i] = new ArrayList<>();
        }
        
        // 构建最小生成树
        for (Edge e : sortedEdges) {
            if (uf.union(e.u, e.v)) {
                mstWeight += e.w;
                inMST[e.id] = true;
                tree[e.u].add(new int[]{e.v, e.w});
                tree[e.v].add(new int[]{e.u, e.w});
            }
        }
        
        // 初始化LCA数组
        depth = new int[n + 1];
        up = new int[n + 1][LOG];
        maxEdge = new int[n + 1][LOG];
        
        depth[0] = -1;
        dfs(1, 0, 0);
        
        // 处理每条边
        long[] result = new long[m];
        for (int i = 0; i < m; i++) {
            if (inMST[i]) {
                result[i] = mstWeight;
            } else {
                int maxW = queryMaxEdge(edges[i].u, edges[i].v);
                result[i] = mstWeight - maxW + edges[i].w;
            }
        }
        
        for (int i = 0; i < m; i++) {
            System.out.println(result[i]);
        }
        
        scanner.close();
    }
}