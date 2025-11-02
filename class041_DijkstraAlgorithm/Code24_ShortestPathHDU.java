package class064;

import java.util.*;

// 杭电OJ 2544 最短路
// 在一个无向图中，求从节点1到节点N的最短路径
// 测试链接：http://acm.hdu.edu.cn/showproblem.php?pid=2544
public class Code24_ShortestPathHDU {

    // 使用Dijkstra算法求解单源最短路径
    // 时间复杂度: O((V+E)logV) 其中V是节点数，E是边数
    // 空间复杂度: O(V+E) 存储图和距离数组
    public static int shortestPath(int n, int[][] edges) {
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
            // 无向图，需要添加两条边
            graph.get(u).add(new int[] { v, w });
            graph.get(v).add(new int[] { u, w });
        }
        
        // distance[i] 表示从源节点1到节点i的最短距离
        int[] distance = new int[n + 1];
        // 初始化距离为无穷大
        Arrays.fill(distance, Integer.MAX_VALUE);
        // 源节点到自己的距离为0
        distance[1] = 0;
        
        // visited[i] 表示节点i是否已经确定了最短距离
        boolean[] visited = new boolean[n + 1];
        
        // 优先队列，按距离从小到大排序
        // 0 : 当前节点
        // 1 : 源点到当前点距离
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        heap.add(new int[] { 1, 0 });
        
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
        
        return distance[n];
    }

    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 4;
        int[][] edges1 = {{1,2,3},{2,3,2},{3,4,1},{1,4,5}};
        System.out.println("测试用例1结果: " + shortestPath(n1, edges1)); // 期望输出: 4
    }
}