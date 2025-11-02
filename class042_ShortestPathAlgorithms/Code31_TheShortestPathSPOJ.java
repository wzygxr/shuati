package class065_ShortestPathAlgorithms;

import java.util.*;

/**
 * SPOJ SHPATH - The Shortest Path
 * 题目链接: https://www.spoj.com/problems/SHPATH/
 * 
 * 题目描述:
 * 给定一个城市的地图，城市之间有道路连接，每条道路都有一个成本。目标是找到城市对之间的最小成本路径。
 * 
 * 算法思路:
 * 这是一个标准的单源最短路径问题，可以使用Dijkstra算法解决。
 * 由于需要处理多个查询，我们可以对每个查询都运行一次Dijkstra算法。
 * 
 * 时间复杂度: O(Q * (V + E) * logV)，其中Q是查询数，V是城市数，E是道路数
 * 空间复杂度: O(V + E)
 */
public class Code31_TheShortestPathSPOJ {
    
    /**
     * 求解城市间的最短路径
     * 
     * @param n 城市数量
     * @param cityNames 城市名称数组
     * @param roads 道路数组，每个元素为{city1, city2, cost}表示两个城市之间的道路及其成本
     * @param queries 查询数组，每个元素为{startCity, endCity}表示查询的起点和终点城市
     * @return 每个查询的最短路径成本数组
     */
    public static int[] theShortestPath(int n, String[] cityNames, int[][] roads, String[][] queries) {
        // 创建城市名称到索引的映射
        Map<String, Integer> cityIndexMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            cityIndexMap.put(cityNames[i], i);
        }
        
        // 构建邻接表表示的图
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 添加道路到图中
        for (int[] road : roads) {
            int city1 = road[0] - 1;  // 转换为0-based索引
            int city2 = road[1] - 1;  // 转换为0-based索引
            int cost = road[2];
            
            // 无向图，需要添加两条边
            graph.get(city1).add(new int[]{city2, cost});
            graph.get(city2).add(new int[]{city1, cost});
        }
        
        // 处理每个查询
        int[] results = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            String startCity = queries[i][0];
            String endCity = queries[i][1];
            
            // 获取起点和终点的索引
            int start = cityIndexMap.get(startCity);
            int end = cityIndexMap.get(endCity);
            
            // 使用Dijkstra算法求最短路径
            results[i] = dijkstra(graph, start, end, n);
        }
        
        return results;
    }
    
    /**
     * Dijkstra算法实现
     * 
     * @param graph 图的邻接表表示
     * @param start 起点
     * @param end 终点
     * @param n 顶点数
     * @return 从起点到终点的最短距离，如果无法到达则返回-1
     */
    private static int dijkstra(List<List<int[]>> graph, int start, int end, int n) {
        // distance[i] 表示从源节点到节点i的最短距离
        int[] distance = new int[n];
        Arrays.fill(distance, Integer.MAX_VALUE);
        // 源节点到自己的距离为0
        distance[start] = 0;
        
        // visited[i] 表示节点i是否已经确定了最短距离
        boolean[] visited = new boolean[n];
        
        // 优先队列，按距离从小到大排序
        // 0 : 当前节点
        // 1 : 源点到当前点距离
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        pq.add(new int[]{start, 0});
        
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
            
            // 如果到达终点，直接返回距离
            if (u == end) {
                return distance[u];
            }
            
            // 遍历u的所有邻居节点
            for (int[] edge : graph.get(u)) {
                int v = edge[0];  // 邻居节点
                int w = edge[1];  // 边的权重
                
                // 如果邻居节点未访问且通过u到达v的距离更短，则更新
                if (!visited[v] && distance[u] + w < distance[v]) {
                    distance[v] = distance[u] + w;
                    pq.add(new int[]{v, distance[v]});
                }
            }
        }
        
        // 如果无法到达终点，返回-1
        return -1;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 4;
        String[] cityNames1 = {"a", "b", "c", "d"};
        int[][] roads1 = {{1, 2, 1}, {2, 3, 2}, {3, 4, 3}, {1, 4, 10}};
        String[][] queries1 = {{"a", "d"}, {"b", "c"}};
        int[] result1 = theShortestPath(n1, cityNames1, roads1, queries1);
        System.out.println("测试用例1结果: " + Arrays.toString(result1)); // 期望输出: [6, 2]
    }
}