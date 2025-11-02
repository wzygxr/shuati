package class064;

import java.util.*;

// Aizu OJ GRL_1_A: Single Source Shortest Path
// 对于给定的加权图G(V,E)和源点r，找到从源点到每个顶点的源最短路径
// 测试链接：https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=GRL_1_A
public class Code18_SingleSourceShortestPathAizu {

    // 使用Dijkstra算法求解单源最短路径
    // 时间复杂度: O((V+E)logV) 其中V是节点数，E是边数
    // 空间复杂度: O(V+E) 存储图和距离数组
    public static int[] singleSourceShortestPath(int V, int[][] edges, int r) {
        // 构建邻接表表示的图
        ArrayList<ArrayList<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < V; i++) {
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
        
        // distance[i] 表示从源节点r到节点i的最短距离
        int[] distance = new int[V];
        // 初始化距离为无穷大
        Arrays.fill(distance, Integer.MAX_VALUE);
        // 源节点到自己的距离为0
        distance[r] = 0;
        
        // visited[i] 表示节点i是否已经确定了最短距离
        boolean[] visited = new boolean[V];
        
        // 优先队列，按距离从小到大排序
        // 0 : 当前节点
        // 1 : 源点到当前点距离
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        heap.add(new int[] { r, 0 });
        
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
        int V1 = 4;
        int[][] edges1 = {{0,1,1},{0,2,4},{1,2,2},{2,3,1},{1,3,5}};
        int r1 = 0;
        int[] result1 = singleSourceShortestPath(V1, edges1, r1);
        for (int i = 0; i < result1.length; i++) {
            if (result1[i] == Integer.MAX_VALUE) {
                System.out.println("INF");
            } else {
                System.out.println(result1[i]);
            }
        }
    }
}