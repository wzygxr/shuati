package class064;

import java.util.*;

// ACWing 849. Dijkstra求最短路I
// 给定一个 n 个点 m 条边的有向图，图中可能存在重边和自环，所有边权均为正值
// 请你求出 1 号点到 n 号点的最短距离，如果无法从 1 号点走到 n 号点，则输出-1
// 测试链接：https://www.acwing.com/problem/content/851/
public class Code20_DijkstraAcwing1 {

    // 使用Dijkstra算法求解单源最短路径（朴素版本）
    // 时间复杂度: O(V^2) 其中V是节点数
    // 空间复杂度: O(V^2) 存储邻接矩阵
    public static int dijkstra(int n, int[][] edges) {
        // 构建邻接矩阵表示的图
        int[][] graph = new int[n + 1][n + 1];
        // 初始化为无穷大
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                graph[i][j] = Integer.MAX_VALUE / 2; // 防止溢出
            }
        }
        
        // 添加边到图中
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int w = edge[2];
            // 有向图，可能存在重边，取最小值
            graph[u][v] = Math.min(graph[u][v], w);
        }
        
        // distance[i] 表示从源节点1到节点i的最短距离
        int[] distance = new int[n + 1];
        // 初始化距离为无穷大
        Arrays.fill(distance, Integer.MAX_VALUE / 2); // 防止溢出
        // 源节点到自己的距离为0
        distance[1] = 0;
        
        // visited[i] 表示节点i是否已经确定了最短距离
        boolean[] visited = new boolean[n + 1];
        
        // Dijkstra算法主循环
        for (int i = 1; i <= n; i++) {
            // 找到未访问节点中距离最小的节点
            int u = -1;
            for (int j = 1; j <= n; j++) {
                if (!visited[j] && (u == -1 || distance[j] < distance[u])) {
                    u = j;
                }
            }
            
            // 如果找不到有效节点，说明无法到达
            if (u == -1) {
                break;
            }
            
            // 标记为已访问
            visited[u] = true;
            
            // 更新u的所有邻居节点的距离
            for (int v = 1; v <= n; v++) {
                if (!visited[v] && graph[u][v] != Integer.MAX_VALUE / 2) {
                    distance[v] = Math.min(distance[v], distance[u] + graph[u][v]);
                }
            }
        }
        
        // 如果无法到达终点，返回-1
        if (distance[n] == Integer.MAX_VALUE / 2) {
            return -1;
        }
        
        // 返回最短距离
        return distance[n];
    }

    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 3;
        int[][] edges1 = {{1,2,2},{2,3,1},{1,3,4}};
        System.out.println("测试用例1结果: " + dijkstra(n1, edges1)); // 期望输出: 3
    }
}