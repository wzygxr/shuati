package class120;

// Codeforces 1406C. Link Cut Centroids
// 题目描述：给定一棵树，执行一次操作：切断一条边，然后添加一条新边，使得新树只有一个重心
// 算法思想：如果树原本有两个重心，切断连接它们的路径上的一条边，然后将其中一个重心连接到另一个重心的子树中
// 测试链接：https://codeforces.com/problemset/problem/1406/C
// 时间复杂度：O(n)
// 空间复杂度：O(n)

import java.util.*;

public class Code19_Codeforces1406C {
    
    static int n;
    static List<List<Integer>> graph;
    static int[] size;
    static int[] maxSub;
    static boolean[] visited;
    
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
        
        maxSub[u] = Math.max(maxSub[u], n - size[u]);
    }
    
    // 找到树的所有重心
    static List<Integer> findCentroids() {
        int minMaxSub = Integer.MAX_VALUE;
        List<Integer> centroids = new ArrayList<>();
        
        for (int i = 1; i <= n; i++) {
            if (maxSub[i] < minMaxSub) {
                minMaxSub = maxSub[i];
                centroids.clear();
                centroids.add(i);
            } else if (maxSub[i] == minMaxSub) {
                centroids.add(i);
            }
        }
        
        return centroids;
    }
    
    // 找到一个子节点用于连接
    static int findChild(int u, int parent) {
        for (int v : graph.get(u)) {
            if (v != parent) {
                return v;
            }
        }
        return -1; // 不应该到达这里
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int t = scanner.nextInt(); // 测试用例数量
        
        while (t-- > 0) {
            n = scanner.nextInt();
            
            // 初始化数据结构
            graph = new ArrayList<>();
            for (int i = 0; i <= n; i++) {
                graph.add(new ArrayList<>());
            }
            size = new int[n + 1];
            maxSub = new int[n + 1];
            
            // 读取边
            List<int[]> edges = new ArrayList<>();
            for (int i = 0; i < n - 1; i++) {
                int u = scanner.nextInt();
                int v = scanner.nextInt();
                graph.get(u).add(v);
                graph.get(v).add(u);
                edges.add(new int[]{u, v});
            }
            
            // 计算子树信息
            dfs(1, -1);
            
            // 找到重心
            List<Integer> centroids = findCentroids();
            
            // 如果只有一个重心，无需操作
            if (centroids.size() == 1) {
                // 输出任意一条边
                int[] edge = edges.get(0);
                System.out.println(edge[0] + " " + edge[1]);
                System.out.println(edge[0] + " " + edge[1]);
            } else {
                // 有两个重心，centroids[0]和centroids[1]
                int c1 = centroids.get(0);
                int c2 = centroids.get(1);
                
                // 找到c1在c2方向上的子节点
                int child = -1;
                for (int v : graph.get(c2)) {
                    if (v != c1 && size[v] > size[c2]) {
                        child = v;
                        break;
                    }
                }
                if (child == -1) {
                    // 如果没找到，任选c1的一个子节点
                    child = findChild(c1, c2);
                }
                
                // 切断c1和child的边，连接c2和child
                System.out.println(c1 + " " + child);
                System.out.println(c2 + " " + child);
            }
        }
        
        scanner.close();
    }
}