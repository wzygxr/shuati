package class186;

import java.util.*;

/**
 * Binary Lifting算法实现k-th祖先查询
 * 时间复杂度:
 *   - 预处理: O(n log n)
 *   - 查询: O(log k)
 * 空间复杂度: O(n log n)
 */
public class BinaryLiftingKthAncestor {
    private List<Integer>[] graph; // 图的邻接表表示
    private int[][] up; // up[k][u]表示u的2^k级祖先
    private int[] depth; // 每个节点的深度
    private int log; // 最大层数，log2(n)的上界
    private int n; // 节点数量
    private int root; // 根节点

    /**
     * 构造函数
     * @param n 节点数量
     * @param edges 边集合
     * @param root 根节点
     */
    public BinaryLiftingKthAncestor(int n, List<int[]> edges, int root) {
        this.n = n;
        this.root = root;
        
        // 计算需要的最大层数
        this.log = 0;
        while ((1 << log) <= n) {
            log++;
        }
        log++;
        
        // 初始化邻接表
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
        
        // 初始化up表和深度数组
        up = new int[log][n];
        depth = new int[n];
        
        // 初始化up[0]为-1
        Arrays.fill(up[0], -1);
        
        // 预处理
        dfs(root, -1, 0);
        
        // 填充up表
        for (int k = 1; k < log; k++) {
            for (int u = 0; u < n; u++) {
                if (up[k-1][u] != -1) {
                    up[k][u] = up[k-1][up[k-1][u]];
                } else {
                    up[k][u] = -1;
                }
            }
        }
    }
    
    /**
     * DFS遍历，填充up[0]和depth数组
     * @param u 当前节点
     * @param parent 父节点
     * @param currentDepth 当前深度
     */
    private void dfs(int u, int parent, int currentDepth) {
        up[0][u] = parent;
        depth[u] = currentDepth;
        
        for (int v : graph[u]) {
            if (v != parent) {
                dfs(v, u, currentDepth + 1);
            }
        }
    }
    
    /**
     * 获取节点u的k级祖先
     * @param u 节点
     * @param k 祖先的级数
     * @return u的k级祖先，如果不存在则返回-1
     */
    public int getKthAncestor(int u, int k) {
        // 如果k大于节点深度，返回-1
        if (k > depth[u]) {
            return -1;
        }
        
        // 二进制分解k
        for (int i = 0; i < log; i++) {
            if ((k & (1 << i)) != 0) {
                u = up[i][u];
                if (u == -1) {
                    return -1;
                }
            }
        }
        
        return u;
    }
    
    /**
     * 计算两个节点的最近公共祖先(LCA)
     * @param u 第一个节点
     * @param v 第二个节点
     * @return u和v的LCA
     */
    public int lca(int u, int v) {
        // 确保u的深度大于等于v的深度
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        // 将u提升到与v同一深度
        u = getKthAncestor(u, depth[u] - depth[v]);
        
        // 如果u和v相同，说明已经找到LCA
        if (u == v) {
            return u;
        }
        
        // 同时提升u和v
        for (int k = log - 1; k >= 0; k--) {
            if (up[k][u] != -1 && up[k][u] != up[k][v]) {
                u = up[k][u];
                v = up[k][v];
            }
        }
        
        // LCA是u和v的父节点
        return up[0][u];
    }
    
    /**
     * 获取节点深度
     * @param u 节点
     * @return 节点的深度
     */
    public int getDepth(int u) {
        return depth[u];
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试用例
        int n = 6;
        int root = 0;
        List<int[]> edges = Arrays.asList(
            new int[]{0, 1},
            new int[]{0, 2},
            new int[]{1, 3},
            new int[]{1, 4},
            new int[]{2, 5}
        );
        
        BinaryLiftingKthAncestor bl = new BinaryLiftingKthAncestor(n, edges, root);
        
        // 测试k-th祖先查询
        System.out.println("k-th祖先查询结果：");
        System.out.printf("节点3的1级祖先: %d\n", bl.getKthAncestor(3, 1)); // 应该是1
        System.out.printf("节点3的2级祖先: %d\n", bl.getKthAncestor(3, 2)); // 应该是0
        System.out.printf("节点3的3级祖先: %d\n", bl.getKthAncestor(3, 3)); // 应该是-1
        
        // 测试LCA查询
        System.out.println("\nLCA查询结果：");
        System.out.printf("LCA(3, 4) = %d\n", bl.lca(3, 4)); // 应该是1
        System.out.printf("LCA(3, 5) = %d\n", bl.lca(3, 5)); // 应该是0
        System.out.printf("LCA(4, 5) = %d\n", bl.lca(4, 5)); // 应该是0
    }
}