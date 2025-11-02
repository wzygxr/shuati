package class120;

// POJ 3107 Godfather
// 题目描述：给定一棵树，找出所有的重心节点
// 算法思想：直接应用树的重心查找算法
// 测试链接：http://poj.org/problem?id=3107
// 时间复杂度：O(n)
// 空间复杂度：O(n)

import java.util.*;

public class Code20_POJ3107 {
    
    static int n;
    static List<List<Integer>> graph;
    static int[] size;
    static int[] maxSub;
    static int minMaxSub; // 最小的最大子树大小
    
    // 计算子树大小和最大子树大小
    static void dfs(int u, int parent) {
        size[u] = 1;
        maxSub[u] = 0;
        
        for (int v : graph.get(u)) {
            if (v != parent) {
                dfs(v, u);
                size[u] += size[v];
                maxSub[u] = Math.max(maxSub[u], size[v]);
            }
        }
        
        // 计算父方向的子树大小
        maxSub[u] = Math.max(maxSub[u], n - size[u]);
        // 更新最小的最大子树大小
        minMaxSub = Math.min(minMaxSub, maxSub[u]);
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        
        // 初始化邻接表
        graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 读取边
        for (int i = 0; i < n - 1; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            graph.get(u).add(v);
            graph.get(v).add(u);
        }
        
        // 初始化size和maxSub数组
        size = new int[n + 1];
        maxSub = new int[n + 1];
        minMaxSub = Integer.MAX_VALUE;
        
        // 第一次DFS计算子树信息
        dfs(1, -1);
        
        // 收集所有重心
        List<Integer> centroids = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            if (maxSub[i] == minMaxSub) {
                centroids.add(i);
            }
        }
        
        // 排序输出
        Collections.sort(centroids);
        for (int i = 0; i < centroids.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(centroids.get(i));
        }
        System.out.println();
        
        scanner.close();
    }
}