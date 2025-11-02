package class186;

import java.util.*;

/**
 * Tarjan离线算法实现最近公共祖先(LCA)查询
 * 时间复杂度: O(n + q)，其中n是节点数，q是查询次数
 * 空间复杂度: O(n + q)
 */
public class TarjanOfflineLCA {
    private List<Integer>[] graph; // 图的邻接表表示
    private List<int[]>[] queries; // 每个节点相关的查询
    private int[] parent; // 并查集的父节点数组
    private boolean[] visited; // 记录节点是否被访问过
    private int[] ancestor; // 存储每个节点的祖先
    private int[] result; // 存储查询结果
    private int n; // 节点数量
    private int q; // 查询数量

    /**
     * 构造函数
     * @param n 节点数量
     * @param edges 边集合
     * @param queryList 查询列表，每个查询包含两个节点u和v
     */
    public TarjanOfflineLCA(int n, List<int[]> edges, List<int[]> queryList) {
        this.n = n;
        this.q = queryList.size();
        
        // 初始化图的邻接表
        graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        // 添加边
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph[u].add(v);
            graph[v].add(u);
        }
        
        // 初始化查询数组
        queries = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            queries[i] = new ArrayList<>();
        }
        
        // 添加查询
        for (int i = 0; i < queryList.size(); i++) {
            int[] query = queryList.get(i);
            int u = query[0];
            int v = query[1];
            queries[u].add(new int[]{v, i});
            queries[v].add(new int[]{u, i});
        }
        
        // 初始化并查集和其他数组
        parent = new int[n];
        visited = new boolean[n];
        ancestor = new int[n];
        result = new int[q];
        
        // 初始化并查集，每个节点的父节点是自己
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }
    
    /**
     * 并查集的查找操作，带路径压缩
     * @param x 要查找的节点
     * @return x所在集合的根节点
     */
    private int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }
    
    /**
     * 并查集的合并操作
     * @param x 第一个节点
     * @param y 第二个节点
     */
    private void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX != rootY) {
            parent[rootY] = rootX;
        }
    }
    
    /**
     * Tarjan离线算法的主要实现
     * @param u 当前节点
     * @param parentU u的父节点
     */
    private void tarjan(int u, int parentU) {
        visited[u] = true;
        ancestor[u] = u;
        
        // 遍历u的所有邻接点
        for (int v : graph[u]) {
            // 避免回到父节点
            if (v != parentU) {
                tarjan(v, u);
                union(u, v);
                ancestor[find(u)] = u;
            }
        }
        
        // 处理与u相关的查询
        for (int[] query : queries[u]) {
            int v = query[0];
            int index = query[1];
            // 如果v已经被访问过，那么它们的LCA就是ancestor[find(v)]
            if (visited[v]) {
                result[index] = ancestor[find(v)];
            }
        }
    }
    
    /**
     * 执行查询并返回结果
     * @return 查询结果数组
     */
    public int[] solve() {
        // 从根节点开始遍历（假设根节点是0）
        tarjan(0, -1);
        return result;
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试用例
        int n = 6;
        List<int[]> edges = Arrays.asList(
            new int[]{0, 1},
            new int[]{0, 2},
            new int[]{1, 3},
            new int[]{1, 4},
            new int[]{2, 5}
        );
        
        List<int[]> queries = Arrays.asList(
            new int[]{3, 4},
            new int[]{3, 5},
            new int[]{4, 5}
        );
        
        TarjanOfflineLCA tarjan = new TarjanOfflineLCA(n, edges, queries);
        int[] results = tarjan.solve();
        
        System.out.println("LCA查询结果：");
        for (int i = 0; i < results.length; i++) {
            int u = queries.get(i)[0];
            int v = queries.get(i)[1];
            System.out.printf("LCA(%d, %d) = %d\n", u, v, results[i]);
        }
    }
}