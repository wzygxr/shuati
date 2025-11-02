package class064;

import java.util.*;

// 洛谷 P4779 【模板】单源最短路径（标准版）
// 给定一个 n 个点，m 条有向边的带非负权图，请你计算从 s 出发，到每个点的距离
// 数据保证你能从 s 出发到任意点
// 测试链接：https://www.luogu.com.cn/problem/P4779
public class Code25_SingleSourceShortestPathLuogu {

    // 使用Dijkstra算法求解单源最短路径
    // 时间复杂度: O((V+E)logV) 其中V是节点数，E是边数
    // 空间复杂度: O(V+E) 存储图和距离数组
    public static int[] dijkstra(int n, int m, int s, int[][] edges) {
        // 构建邻接表表示的图
        ArrayList<ArrayList<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 添加边到图中
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int w = edge[2];
            // 有向图
            graph.get(u).add(new int[] { v, w });
        }
        
        // distance[i] 表示从源节点s到节点i的最短距离
        int[] distance = new int[n + 1];
        // 初始化距离为无穷大
        Arrays.fill(distance, Integer.MAX_VALUE);
        // 源节点到自己的距离为0
        distance[s] = 0;
        
        // visited[i] 表示节点i是否已经确定了最短距离
        boolean[] visited = new boolean[n + 1];
        
        // 优先队列，按距离从小到大排序
        // 0 : 当前节点
        // 1 : 源点到当前点距离
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        heap.add(new int[] { s, 0 });
        
        while (!heap.isEmpty()) {
            // 取出距离源点最近的节点
            int[] record = heap.poll();
            int u = record[0];
            int dist = record[1];
            
            // 如果已经处理过，跳过
            if (visited[u]) {
                continue;
            }
            // 标记为已处理
            visited[u] = true;
            
            // 遍历u的所有邻居节点
            for (int[] edge : graph.get(u)) {
                int v = edge[0];  // 邻居节点
                int w = edge[1];  // 边的权重
                
                // 如果邻居节点未访问且通过u到达v的距离更短，则更新
                if (!visited[v] && distance[u] + w < distance[v]) {
                    distance[v] = distance[u] + w;
                    heap.add(new int[] { v, distance[v] });
                }
            }
        }
        
        return distance;
    }

    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 4, m1 = 6, s1 = 1;
        int[][] edges1 = {{1,2,2},{2,3,2},{2,4,1},{1,3,5},{3,4,3},{1,4,4}};
        int[] result1 = dijkstra(n1, m1, s1, edges1);
        for (int i = 1; i <= n1; i++) {
            System.out.print(result1[i]);
            if (i < n1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }
}