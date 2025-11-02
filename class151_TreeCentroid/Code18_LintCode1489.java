package class120;

// LintCode 1489. 树中的中心点
// 题目描述：给定一棵树，找出树的中心点（重心）
// 算法思想：直接应用树的重心查找算法
// 测试链接：https://www.lintcode.com/problem/1489/
// 时间复杂度：O(n)
// 空间复杂度：O(n)

import java.util.*;

public class Code18_LintCode1489 {
    
    // 邻接表存储树
    private List<List<Integer>> graph;
    // 子树大小
    private int[] size;
    // 每个节点的最大子树大小
    private int[] maxSub;
    // 树的节点数
    private int n;
    
    // 计算子树大小和最大子树大小
    private void dfs(int u, int parent) {
        size[u] = 1;
        maxSub[u] = 0;
        
        // 遍历所有邻居
        for (int v : graph.get(u)) {
            if (v != parent) {
                dfs(v, u);
                size[u] += size[v];
                maxSub[u] = Math.max(maxSub[u], size[v]);
            }
        }
        
        // 计算父方向的子树大小
        maxSub[u] = Math.max(maxSub[u], n - size[u]);
    }
    
    // 寻找树的中心点（重心）
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
        this.n = n;
        
        // 边界情况处理
        if (n == 1) {
            return Collections.singletonList(0);
        }
        if (n == 2) {
            return Arrays.asList(0, 1);
        }
        
        // 初始化邻接表
        graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 构建图
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph.get(u).add(v);
            graph.get(v).add(u);
        }
        
        // 初始化size和maxSub数组
        size = new int[n];
        maxSub = new int[n];
        
        // 第一次DFS计算子树信息
        dfs(0, -1);
        
        // 找到最小的最大子树大小
        int minMaxSub = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            minMaxSub = Math.min(minMaxSub, maxSub[i]);
        }
        
        // 收集所有重心
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (maxSub[i] == minMaxSub) {
                result.add(i);
            }
        }
        
        return result;
    }
    
    // 主方法用于测试
    public static void main(String[] args) {
        Code18_LintCode1489 solution = new Code18_LintCode1489();
        
        // 测试用例1
        int n1 = 4;
        int[][] edges1 = {{1, 0}, {1, 2}, {1, 3}};
        System.out.println("Test Case 1: " + solution.findMinHeightTrees(n1, edges1)); // Expected: [1]
        
        // 测试用例2
        int n2 = 6;
        int[][] edges2 = {{0, 3}, {1, 3}, {2, 3}, {4, 3}, {5, 4}};
        System.out.println("Test Case 2: " + solution.findMinHeightTrees(n2, edges2)); // Expected: [3, 4]
    }
}