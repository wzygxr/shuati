// LintCode 1577. 子树计数
// 题目描述：给定一棵树，计算以每个节点为根的子树的重心。
// 算法思想：对于每个子树，找到其重心。利用树的重心的性质：子树的重心一定在原树重心到该子树根的路径上。
// 测试链接：https://www.lintcode.com/problem/1577/
// 时间复杂度：O(n^2)，对于每个节点都要重新计算子树的重心
// 空间复杂度：O(n)

import java.util.*;

public class Code22_LintCode1577 {
    private int n; // 节点数
    private List<List<Integer>> graph; // 邻接表
    private int[] res; // 结果数组，res[i]表示以节点i为根的子树的重心
    private boolean[] visited; // 标记数组
    private int[] size; // 子树大小
    private int[] maxSubtree; // 最大子树大小
    private int minMaxSubtree; // 当前子树的最小最大子树大小
    private int centroid; // 当前子树的重心
    
    /**
     * 计算以每个节点为根的子树的重心
     * @param n 节点数
     * @param edges 边列表
     * @return 结果数组
     */
    public int[] getSubtreeCentroid(int n, int[][] edges) {
        this.n = n;
        // 构建邻接表
        graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph.get(u).add(v);
            graph.get(v).add(u);
        }
        
        res = new int[n];
        // 对每个节点作为根，计算其子树的重心
        for (int i = 0; i < n; i++) {
            visited = new boolean[n];
            size = new int[n];
            maxSubtree = new int[n];
            minMaxSubtree = Integer.MAX_VALUE;
            centroid = -1;
            
            // 计算子树大小
            dfs(i, -1);
            
            // 找到重心
            findCentroid(i, -1, size[i]);
            
            res[i] = centroid;
        }
        
        return res;
    }
    
    /**
     * 计算子树大小
     * @param u 当前节点
     * @param parent 父节点
     */
    private void dfs(int u, int parent) {
        visited[u] = true;
        size[u] = 1;
        maxSubtree[u] = 0;
        for (int v : graph.get(u)) {
            if (!visited[v] && v != parent) {
                dfs(v, u);
                size[u] += size[v];
                maxSubtree[u] = Math.max(maxSubtree[u], size[v]);
            }
        }
    }
    
    /**
     * 寻找子树的重心
     * @param u 当前节点
     * @param parent 父节点
     * @param totalSize 子树总大小
     */
    private void findCentroid(int u, int parent, int totalSize) {
        // 计算父方向的子树大小
        int max = Math.max(maxSubtree[u], totalSize - size[u]);
        
        // 更新重心
        if (max < minMaxSubtree || (max == minMaxSubtree && u < centroid)) {
            minMaxSubtree = max;
            centroid = u;
        }
        
        for (int v : graph.get(u)) {
            if (v != parent && visited[v]) {
                findCentroid(v, u, totalSize);
            }
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code22_LintCode1577 solution = new Code22_LintCode1577();
        
        // 测试用例1
        int n1 = 3;
        int[][] edges1 = {{0, 1}, {0, 2}};
        int[] res1 = solution.getSubtreeCentroid(n1, edges1);
        System.out.println("测试用例1结果: " + Arrays.toString(res1));
        // 期望输出: [0, 0, 0]，因为任何子树的重心都是0
        
        // 测试用例2
        int n2 = 4;
        int[][] edges2 = {{0, 1}, {1, 2}, {1, 3}};
        int[] res2 = solution.getSubtreeCentroid(n2, edges2);
        System.out.println("测试用例2结果: " + Arrays.toString(res2));
        // 期望输出: [1, 1, 1, 1]，因为以1为中心的树，所有子树的重心都是1
    }
}

// 注意：
// 1. 树的重心是指：对于节点u，删除u后剩余的各个连通块的大小不超过原树大小的一半
// 2. 本算法对于每个节点都重新计算子树的重心，时间复杂度为O(n^2)
// 3. 对于更大的数据规模，可以利用树的重心的性质进行优化，如利用点分治的思想