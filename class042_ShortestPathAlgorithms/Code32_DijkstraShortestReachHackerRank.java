package class065_ShortestPathAlgorithms;

import java.util.*;

/**
 * HackerRank Dijkstra: Shortest Reach 2
 * 题目链接: https://www.hackerrank.com/challenges/dijkstrashortreach/problem
 * 
 * 题目描述:
 * 给定一个无向图和一个起始节点，确定从起始节点到所有其他节点的最短路径长度。
 * 如果无法到达某个节点，则返回-1。
 * 
 * 算法思路:
 * 使用标准的Dijkstra算法求解单源最短路径问题。
 * 
 * 时间复杂度: O((V+E)logV)，其中V是节点数，E是边数
 * 空间复杂度: O(V+E)
 */
public class Code32_DijkstraShortestReachHackerRank {
    
    /**
     * 求解单源最短路径
     * 
     * @param n 节点数
     * @param edges 边数组，每个元素为{u, v, w}表示节点u和v之间的边，权重为w
     * @param s 起始节点
     * @return 从起始节点到所有其他节点的最短距离数组，无法到达的节点距离为-1
     */
    public static int[] shortestReach(int n, int[][] edges, int s) {
        // 构建邻接表表示的图
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 添加边到图中
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int w = edge[2];
            // 无向图，需要添加两条边
            graph.get(u).add(new int[]{v, w});
            graph.get(v).add(new int[]{u, w});
        }
        
        // distance[i] 表示从源节点s到节点i的最短距离
        int[] distance = new int[n + 1];
        // 初始化距离为-1，表示无法到达
        Arrays.fill(distance, -1);
        // 源节点到自己的距离为0
        distance[s] = 0;
        
        // visited[i] 表示节点i是否已经确定了最短距离
        boolean[] visited = new boolean[n + 1];
        
        // 优先队列，按距离从小到大排序
        // 0 : 当前节点
        // 1 : 源点到当前点距离
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        pq.add(new int[]{s, 0});
        
        while (!pq.isEmpty()) {
            // 取出距离源点最近的节点
            int[] record = pq.poll();
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
                if (!visited[v] && (distance[v] == -1 || distance[u] + w < distance[v])) {
                    distance[v] = distance[u] + w;
                    pq.add(new int[]{v, distance[v]});
                }
            }
        }
        
        // 构造结果数组，排除起始节点
        int[] result = new int[n - 1];
        int index = 0;
        for (int i = 1; i <= n; i++) {
            if (i != s) {
                result[index++] = distance[i];
            }
        }
        
        return result;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 4;
        int[][] edges1 = {{1, 2, 24}, {1, 4, 20}, {3, 1, 3}, {4, 3, 12}};
        int s1 = 1;
        int[] result1 = shortestReach(n1, edges1, s1);
        System.out.println("测试用例1结果: " + Arrays.toString(result1)); // 期望输出: [24, 3, 15]
    }
}