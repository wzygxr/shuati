package class064;

import java.util.*;

/**
 * SPOJ - EZDIJKST: Easy Dijkstra Problem
 * 
 * 题目链接: https://www.spoj.com/problems/EZDIJKST/
 * 
 * 题目描述:
 * 给定一个有向带权图，确定指定顶点之间的最短路径。
 * 输入格式:
 * 第一行包含测试用例的数量。
 * 每个测试用例的第一行包含节点数n (1 <= n <= 10000)。
 * 第二行包含边数m (1 <= m <= 100000)。
 * 接下来的m行每行包含三个整数a, b, c，表示从节点a到节点b有一条权重为c的边。
 * 然后是包含源节点和目标节点的行。
 * 
 * 解题思路:
 * 这是一个标准的单源最短路径问题，可以直接使用Dijkstra算法解决。
 * 
 * 算法应用场景:
 * - 网络路由
 * - GPS导航
 * - 社交网络分析
 * 
 * 时间复杂度分析:
 * O((V + E) * log V)，其中V是节点数，E是边数
 * 
 * 空间复杂度分析:
 * O(V + E)，用于存储图和距离数组
 */
public class Code32_EasyDijkstraSPOJ {
    
    /**
     * 使用Dijkstra算法求解最短路径
     * 
     * 算法步骤:
     * 1. 构建图的邻接表表示
     * 2. 初始化距离数组，源节点距离为0，其他节点为无穷大
     * 3. 使用优先队列维护待处理节点，按距离从小到大排序
     * 4. 不断取出距离最小的节点，更新其邻居节点的最短距离
     * 5. 返回目标节点的最短距离
     * 
     * 时间复杂度: O((V + E) * log V)
     * 空间复杂度: O(V + E)
     * 
     * @param n 节点数
     * @param edges 边的列表，每个元素为 [from, to, weight]
     * @param start 起始节点
     * @param end 目标节点
     * @return 从起始节点到目标节点的最短距离，如果无法到达则返回-1
     */
    public static int dijkstra(int n, int[][] edges, int start, int end) {
        // 构建邻接表表示的图
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 添加边到图中
        for (int[] edge : edges) {
            graph.get(edge[0]).add(new int[]{edge[1], edge[2]});
        }
        
        // distance[i] 表示从源节点到节点i的最短距离
        int[] distance = new int[n + 1];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[start] = 0;
        
        // visited[i] 表示节点i是否已经确定了最短距离
        boolean[] visited = new boolean[n + 1];
        
        // 优先队列，按距离从小到大排序
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        heap.add(new int[]{start, 0});
        
        // Dijkstra算法主循环
        while (!heap.isEmpty()) {
            // 取出距离源点最近的节点
            int[] current = heap.poll();
            int u = current[0];
            int dist = current[1];
            
            // 如果已经处理过，跳过
            if (visited[u]) {
                continue;
            }
            
            // 如果到达目标节点，直接返回结果
            if (u == end) {
                return dist;
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
                    heap.add(new int[]{v, distance[v]});
                }
            }
        }
        
        // 如果无法到达目标节点，返回-1
        return -1;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 示例测试用例
        int n = 4;
        int[][] edges = {{1, 2, 1}, {1, 3, 3}, {2, 3, 1}, {3, 4, 2}};
        int start = 1;
        int end = 4;
        
        int result = dijkstra(n, edges, start, end);
        System.out.println("从节点 " + start + " 到节点 " + end + " 的最短距离为: " + result);
    }
}