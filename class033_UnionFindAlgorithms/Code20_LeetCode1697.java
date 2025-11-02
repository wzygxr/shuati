package class056;

import java.util.*;

/**
 * LeetCode 1697. 检查边长度限制的路径是否存在
 * 链接: https://leetcode.cn/problems/checking-existence-of-edge-length-limited-paths/
 * 难度: 困难
 * 
 * 题目描述:
 * 给你一个 n 个节点的无向图，节点编号为 0 到 n-1。图中由 edges 数组表示边，其中 edges[i] = [ui, vi, disi] 表示节点 ui 和 vi 之间有一条长度为 disi 的无向边。
 * 同时给你一个数组 queries ，其中 queries[j] = [pj, qj, limitj] ，你的任务是对于每个查询 queries[j] ，判断是否存在从 pj 到 qj 的路径，且路径上每条边的长度都严格小于 limitj 。
 * 注意：查询之间是独立的，即每个查询都是针对原始图进行的。
 * 
 * 示例 1:
 * 输入: n = 3, edges = [[0,1,2],[1,2,4],[2,0,8],[2,0,1]], queries = [[0,1,2],[0,2,5]]
 * 输出: [false,true]
 * 解释:
 * 查询1: 路径 0-1 的边长度为2，不小于2，所以不存在满足条件的路径。
 * 查询2: 路径 0-2-0-1 的边长度为1+8+2=11，但边长度都小于5？实际上应该选择路径 0-2 的边长度为1（因为[2,0,1]这条边存在）。
 * 
 * 示例 2:
 * 输入: n = 5, edges = [[0,1,10],[1,2,5],[2,3,9],[3,4,13]], queries = [[0,4,14],[1,4,13]]
 * 输出: [true,false]
 * 解释: 
 * 查询1: 路径 0-1-2-3-4 的边长度都小于14，所以存在。
 * 查询2: 路径中必须包含长度为13的边，但13不小于13，所以不存在。
 * 
 * 约束条件:
 * 2 <= n <= 10^5
 * 1 <= edges.length <= 10^5
 * 1 <= queries.length <= 10^5
 * edges[i].length == 3
 * queries[i].length == 3
 * 0 <= ui, vi, pj, qj <= n - 1
 * ui != vi
 * pj != qj
 * 1 <= disi, limitj <= 10^9
 */
public class Code20_LeetCode1697 {
    
    /**
     * 方法1: 离线查询 + 并查集（推荐解法）
     * 时间复杂度: O((E + Q) * α(N))，其中E是边数，Q是查询数，N是节点数
     * 空间复杂度: O(N + E + Q)
     * 
     * 解题思路:
     * 1. 将查询按limit从小到大排序（离线处理）
     * 2. 将边按权重从小到大排序
     * 3. 使用并查集维护连通性
     * 4. 对于每个查询，添加所有权重小于limit的边
     * 5. 检查两个节点是否连通
     */
    public boolean[] distanceLimitedPathsExist(int n, int[][] edges, int[][] queries) {
        int m = queries.length;
        
        // 为查询添加索引，以便恢复原始顺序
        int[][] indexedQueries = new int[m][4];
        for (int i = 0; i < m; i++) {
            indexedQueries[i][0] = queries[i][0];
            indexedQueries[i][1] = queries[i][1];
            indexedQueries[i][2] = queries[i][2];
            indexedQueries[i][3] = i; // 原始索引
        }
        
        // 按limit对查询排序
        Arrays.sort(indexedQueries, (a, b) -> Integer.compare(a[2], b[2]));
        
        // 按权重对边排序
        Arrays.sort(edges, (a, b) -> Integer.compare(a[2], b[2]));
        
        // 初始化并查集
        UnionFind uf = new UnionFind(n);
        boolean[] result = new boolean[m];
        
        int edgeIndex = 0;
        // 处理每个查询
        for (int[] query : indexedQueries) {
            int p = query[0];
            int q = query[1];
            int limit = query[2];
            int originalIndex = query[3];
            
            // 添加所有权重小于limit的边
            while (edgeIndex < edges.length && edges[edgeIndex][2] < limit) {
                uf.union(edges[edgeIndex][0], edges[edgeIndex][1]);
                edgeIndex++;
            }
            
            // 检查连通性
            result[originalIndex] = uf.isConnected(p, q);
        }
        
        return result;
    }
    
    /**
     * 标准并查集实现（路径压缩 + 按秩合并）
     */
    static class UnionFind {
        private int[] parent;
        private int[] rank;
        
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
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
        
        public boolean isConnected(int x, int y) {
            return find(x) == find(y);
        }
    }
    
    /**
     * 方法2: 在线查询 - 使用最小生成树 + LCA（适用于需要支持在线查询的场景）
     * 时间复杂度: O((E log E) + Q log N) 用于预处理，每次查询O(log N)
     * 空间复杂度: O(N + E)
     * 
     * 解题思路:
     * 1. 构建图的最小生成树（Kruskal算法）
     * 2. 在最小生成树上预处理LCA（最近公共祖先）
     * 3. 对于每个查询，找到路径上的最大边权
     * 4. 检查最大边权是否小于limit
     */
    public boolean[] distanceLimitedPathsExistLCA(int n, int[][] edges, int[][] queries) {
        // 构建最小生成树
        List<int[]>[] mst = buildMST(n, edges);
        
        // 预处理LCA
        LCASolver lcaSolver = new LCASolver(n, mst);
        
        boolean[] result = new boolean[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int u = queries[i][0];
            int v = queries[i][1];
            int limit = queries[i][2];
            
            // 找到u到v路径上的最大边权
            int maxWeight = lcaSolver.queryMaxWeight(u, v);
            result[i] = maxWeight < limit;
        }
        
        return result;
    }
    
    /**
     * 使用Kruskal算法构建最小生成树
     */
    private List<int[]>[] buildMST(int n, int[][] edges) {
        // 按权重排序边
        Arrays.sort(edges, (a, b) -> Integer.compare(a[2], b[2]));
        
        UnionFind uf = new UnionFind(n);
        @SuppressWarnings("unchecked")
        List<int[]>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int weight = edge[2];
            
            if (!uf.isConnected(u, v)) {
                uf.union(u, v);
                // 添加无向边
                graph[u].add(new int[]{v, weight});
                graph[v].add(new int[]{u, weight});
            }
        }
        
        return graph;
    }
    
    /**
     * LCA求解器，用于在树上查询路径最大边权
     */
    static class LCASolver {
        private int n;
        private int LOG;
        private int[] depth;
        private int[][] parent;
        private int[][] maxWeight;
        
        public LCASolver(int n, List<int[]>[] tree) {
            this.n = n;
            this.LOG = (int) (Math.log(n) / Math.log(2)) + 1;
            this.depth = new int[n];
            this.parent = new int[n][LOG];
            this.maxWeight = new int[n][LOG];
            
            // 初始化
            for (int i = 0; i < n; i++) {
                Arrays.fill(parent[i], -1);
                Arrays.fill(maxWeight[i], 0);
            }
            
            // BFS预处理
            boolean[] visited = new boolean[n];
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(0);
            visited[0] = true;
            depth[0] = 0;
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                
                for (int[] edge : tree[u]) {
                    int v = edge[0];
                    int weight = edge[1];
                    
                    if (!visited[v]) {
                        visited[v] = true;
                        depth[v] = depth[u] + 1;
                        parent[v][0] = u;
                        maxWeight[v][0] = weight;
                        queue.offer(v);
                    }
                }
            }
            
            // 动态规划预处理
            for (int j = 1; j < LOG; j++) {
                for (int i = 0; i < n; i++) {
                    if (parent[i][j-1] != -1) {
                        parent[i][j] = parent[parent[i][j-1]][j-1];
                        maxWeight[i][j] = Math.max(maxWeight[i][j-1], 
                                                  maxWeight[parent[i][j-1]][j-1]);
                    }
                }
            }
        }
        
        public int queryMaxWeight(int u, int v) {
            if (depth[u] < depth[v]) {
                int temp = u;
                u = v;
                v = temp;
            }
            
            int max = 0;
            
            // 将u提升到与v同一深度
            int diff = depth[u] - depth[v];
            for (int j = 0; j < LOG; j++) {
                if ((diff & (1 << j)) != 0) {
                    max = Math.max(max, maxWeight[u][j]);
                    u = parent[u][j];
                }
            }
            
            if (u == v) {
                return max;
            }
            
            // 同时提升u和v
            for (int j = LOG - 1; j >= 0; j--) {
                if (parent[u][j] != parent[v][j]) {
                    max = Math.max(max, maxWeight[u][j]);
                    max = Math.max(max, maxWeight[v][j]);
                    u = parent[u][j];
                    v = parent[v][j];
                }
            }
            
            max = Math.max(max, maxWeight[u][0]);
            max = Math.max(max, maxWeight[v][0]);
            
            return max;
        }
    }
    
    /**
     * 方法3: 使用二分答案 + BFS/DFS（适用于小规模数据）
     * 时间复杂度: O(Q * (E + V))，对于大规模数据会超时
     * 空间复杂度: O(E + V)
     * 
     * 解题思路:
     * 1. 对于每个查询，使用二分查找确定可行的最大边权
     * 2. 或者直接使用BFS/DFS检查是否存在满足条件的路径
     */
    public boolean[] distanceLimitedPathsExistBFS(int n, int[][] edges, int[][] queries) {
        // 构建邻接表
        @SuppressWarnings("unchecked")
        List<int[]>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int weight = edge[2];
            graph[u].add(new int[]{v, weight});
            graph[v].add(new int[]{u, weight});
        }
        
        boolean[] result = new boolean[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int u = queries[i][0];
            int v = queries[i][1];
            int limit = queries[i][2];
            
            result[i] = bfs(graph, u, v, limit);
        }
        
        return result;
    }
    
    private boolean bfs(List<int[]>[] graph, int start, int end, int limit) {
        int n = graph.length;
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(start);
        visited[start] = true;
        
        while (!queue.isEmpty()) {
            int u = queue.poll();
            
            if (u == end) {
                return true;
            }
            
            for (int[] edge : graph[u]) {
                int v = edge[0];
                int weight = edge[1];
                
                if (!visited[v] && weight < limit) {
                    visited[v] = true;
                    queue.offer(v);
                }
            }
        }
        
        return false;
    }
    
    // 测试方法
    public static void main(String[] args) {
        Code20_LeetCode1697 solution = new Code20_LeetCode1697();
        
        // 测试用例1
        int n1 = 3;
        int[][] edges1 = {
            {0, 1, 2}, {1, 2, 4}, {2, 0, 8}, {2, 0, 1}
        };
        int[][] queries1 = {
            {0, 1, 2}, {0, 2, 5}
        };
        
        boolean[] result1 = solution.distanceLimitedPathsExist(n1, edges1, queries1);
        System.out.println("测试用例1: " + Arrays.toString(result1));
        // 预期: [false, true]
        
        // 测试用例2
        int n2 = 5;
        int[][] edges2 = {
            {0, 1, 10}, {1, 2, 5}, {2, 3, 9}, {3, 4, 13}
        };
        int[][] queries2 = {
            {0, 4, 14}, {1, 4, 13}
        };
        
        boolean[] result2 = solution.distanceLimitedPathsExist(n2, edges2, queries2);
        System.out.println("测试用例2: " + Arrays.toString(result2));
        // 预期: [true, false]
        
        // 测试用例3: 大规模数据测试（简化版）
        int n3 = 1000;
        int[][] edges3 = generateRandomEdges(n3, 5000);
        int[][] queries3 = generateRandomQueries(n3, 100);
        
        long startTime = System.currentTimeMillis();
        boolean[] result3 = solution.distanceLimitedPathsExist(n3, edges3, queries3);
        long endTime = System.currentTimeMillis();
        System.out.println("测试用例3 - 处理时间: " + (endTime - startTime) + "ms");
        
        System.out.println("离线查询 + 并查集解法总结:");
        System.out.println("1. 核心思想: 将查询按限制排序，逐步添加边");
        System.out.println("2. 优势: 避免重复计算，时间复杂度优秀");
        System.out.println("3. 适用场景: 大规模数据，查询独立");
        System.out.println("4. 注意事项: 需要保存查询的原始顺序");
    }
    
    // 辅助方法：生成随机边
    private static int[][] generateRandomEdges(int n, int edgeCount) {
        Random random = new Random();
        int[][] edges = new int[edgeCount][3];
        
        for (int i = 0; i < edgeCount; i++) {
            int u = random.nextInt(n);
            int v = random.nextInt(n);
            while (u == v) {
                v = random.nextInt(n);
            }
            int weight = random.nextInt(1000) + 1;
            edges[i] = new int[]{u, v, weight};
        }
        
        return edges;
    }
    
    // 辅助方法：生成随机查询
    private static int[][] generateRandomQueries(int n, int queryCount) {
        Random random = new Random();
        int[][] queries = new int[queryCount][3];
        
        for (int i = 0; i < queryCount; i++) {
            int u = random.nextInt(n);
            int v = random.nextInt(n);
            while (u == v) {
                v = random.nextInt(n);
            }
            int limit = random.nextInt(1000) + 1;
            queries[i] = new int[]{u, v, limit};
        }
        
        return queries;
    }
    
    /**
     * 性能优化建议:
     * 1. 对于大规模数据，优先使用离线查询 + 并查集
     * 2. 如果查询需要在线处理，考虑使用LCA方法
     * 3. 避免使用BFS/DFS处理大规模数据
     * 4. 注意内存使用，避免创建过多临时对象
     * 
     * 工程化考量:
     * 1. 异常处理: 检查节点编号是否越界
     * 2. 边界情况: 处理n=0或n=1的情况
     * 3. 内存优化: 对于超大n，考虑使用更紧凑的数据结构
     * 4. 并发安全: 如果需要在多线程环境使用，需要额外处理
     */
}