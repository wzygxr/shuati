package src.class065_ShortestPathAlgorithms;

import java.util.*;

/**
 * AtCoder ABC362 D - Shortest Path 3
 * 题目链接: https://atcoder.jp/contests/abc362/tasks/abc362_d
 * 
 * 题目描述:
 * 给定一个无向图，每个顶点和每条边都有权重。路径的权重定义为路径上出现的顶点和边的权重之和。
 * 找到从顶点1到顶点N的最短路径。
 * 
 * 算法思路:
 * 这是一个带点权和边权的最短路径问题。我们可以将点权加到边权上，然后使用Dijkstra算法求解。
 * 对于每条边(u,v,w)，我们将边权更新为 w + vertex_weight[u] + vertex_weight[v]。
 * 但需要注意的是，起点和终点的点权只计算一次，所以我们需要特殊处理。
 * 
 * 时间复杂度: O((V+E)logV)，其中V是顶点数，E是边数
 * 空间复杂度: O(V+E)
 */
public class Code30_ShortestPath3AtCoder {
    
    /**
     * 求解带点权和边权的最短路径
     * 
     * @param n 顶点数
     * @param vertexWeights 顶点权重数组，vertexWeights[i]表示顶点i+1的权重
     * @param edges 边数组，每个元素为{u, v, w}表示顶点u和v之间的边，权重为w
     * @return 从顶点1到顶点n的最短路径权重，如果无法到达则返回-1
     */
    public static long shortestPath3(int n, long[] vertexWeights, int[][] edges) {
        // 构建邻接表表示的图
        List<List<long[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 添加边到图中
        for (int[] edge : edges) {
            int u = edge[0] - 1;  // 转换为0-based索引
            int v = edge[1] - 1;  // 转换为0-based索引
            int w = edge[2];
            
            // 无向图，需要添加两条边
            // 边的权重 = 边权 + 起点点权 + 终点点权
            graph.get(u).add(new long[]{v, w + vertexWeights[u] + vertexWeights[v]});
            graph.get(v).add(new long[]{u, w + vertexWeights[u] + vertexWeights[v]});
        }
        
        // 使用Dijkstra算法求最短路径
        // distance[i] 表示从源节点1到节点i的最短距离
        long[] distance = new long[n];
        Arrays.fill(distance, Long.MAX_VALUE);
        
        // 源节点到自己的距离为起点的点权
        distance[0] = vertexWeights[0];
        
        // visited[i] 表示节点i是否已经确定了最短距离
        boolean[] visited = new boolean[n];
        
        // 优先队列，按距离从小到大排序
        // 0 : 当前节点
        // 1 : 源点到当前点距离
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[1], b[1]));
        pq.add(new long[]{0, vertexWeights[0]});
        
        while (!pq.isEmpty()) {
            // 取出距离源点最近的节点
            long[] record = pq.poll();
            int u = (int) record[0];
            long dist = record[1];
            
            // 如果已经处理过，跳过
            if (visited[u]) {
                continue;
            }
            // 标记为已处理
            visited[u] = true;
            
            // 遍历u的所有邻居节点
            for (long[] edge : graph.get(u)) {
                int v = (int) edge[0];  // 邻居节点
                long w = edge[1];       // 边的权重（已包含点权）
                
                // 如果邻居节点未访问且通过u到达v的距离更短，则更新
                // 注意：这里不需要减去终点的点权，因为在最终结果中需要加上终点的点权
                if (!visited[v] && distance[u] + w - vertexWeights[u] < distance[v]) {
                    distance[v] = distance[u] + w - vertexWeights[u];
                    pq.add(new long[]{v, distance[v]});
                }
            }
        }
        
        // 如果无法到达终点，返回-1
        if (distance[n - 1] == Long.MAX_VALUE) {
            return -1;
        }
        
        // 返回最短距离
        return distance[n - 1];
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 3;
        long[] vertexWeights1 = {2, 1, 3};
        int[][] edges1 = {{1, 2, 1}, {2, 3, 2}};
        System.out.println("测试用例1结果: " + shortestPath3(n1, vertexWeights1, edges1)); // 期望输出: 5
        
        // 测试用例2
        int n2 = 4;
        long[] vertexWeights2 = {1, 100, 1, 100};
        int[][] edges2 = {{1, 2, 10}, {2, 3, 10}, {3, 4, 10}};
        System.out.println("测试用例2结果: " + shortestPath3(n2, vertexWeights2, edges2)); // 期望输出: 122
    }
}