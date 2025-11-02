package class064;

import java.util.*;

/**
 * UVa 10986 - Sending email
 * 
 * 题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1927
 * 
 * 题目描述:
 * 现在几乎每个人都有电子邮件地址，这使得两个不同地点的人们可以快速交流。
 * 你的任务是确定从一个城市发送电子邮件到另一个城市的最短时间。
 * 
 * 解题思路:
 * 这是一个标准的单源最短路径问题，使用Dijkstra算法解决。
 * 
 * 算法应用场景:
 * - 网络通信
 * - 交通路线规划
 * - 物流配送优化
 * 
 * 时间复杂度分析:
 * O((V + E) * log V)，其中V是节点数，E是边数
 * 
 * 空间复杂度分析:
 * O(V + E)，用于存储图和距离数组
 */
public class Code34_SendingEmailUva {
    
    /**
     * 使用Dijkstra算法计算从起始城市到目标城市的最短时间
     * 
     * 算法步骤:
     * 1. 构建图的邻接表表示
     * 2. 初始化距离数组，起始节点距离为0，其他节点为无穷大
     * 3. 使用优先队列维护待处理节点，按距离从小到大排序
     * 4. 不断取出距离最小的节点，更新其邻居节点的最短距离
     * 5. 返回目标节点的最短距离
     * 
     * 时间复杂度: O((V + E) * log V)
     * 空间复杂度: O(V + E)
     * 
     * @param n 城市数
     * @param edges 道路列表，每个元素为 [from, to, time]
     * @param start 起始城市
     * @param end 目标城市
     * @return 从起始城市到目标城市的最短时间，如果无法到达则返回-1
     */
    public static int sendEmail(int n, int[][] edges, int start, int end) {
        // 构建邻接表表示的图
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 添加道路到图中（无向图需要添加两条边）
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int w = edge[2];
            graph.get(u).add(new int[]{v, w});
            graph.get(v).add(new int[]{u, w});
        }
        
        // distance[i] 表示从起始城市到城市i的最短时间
        int[] distance = new int[n];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[start] = 0;
        
        // visited[i] 表示城市i是否已经确定了最短时间
        boolean[] visited = new boolean[n];
        
        // 优先队列，按距离从小到大排序
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        heap.add(new int[]{start, 0});
        
        // Dijkstra算法主循环
        while (!heap.isEmpty()) {
            // 取出距离起始城市最近的节点
            int[] current = heap.poll();
            int u = current[0];
            int dist = current[1];
            
            // 如果已经处理过，跳过
            if (visited[u]) {
                continue;
            }
            
            // 如果到达目标城市，直接返回结果
            if (u == end) {
                return dist;
            }
            
            // 标记为已处理
            visited[u] = true;
            
            // 遍历u的所有邻居城市
            for (int[] edge : graph.get(u)) {
                int v = edge[0];  // 邻居城市
                int w = edge[1];  // 道路通行时间
                
                // 如果邻居城市未访问且通过u到达v的时间更短，则更新
                if (!visited[v] && distance[u] + w < distance[v]) {
                    distance[v] = distance[u] + w;
                    heap.add(new int[]{v, distance[v]});
                }
            }
        }
        
        // 如果无法到达目标城市，返回-1
        return -1;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 示例测试用例
        int n = 4;
        int[][] edges = {{0, 1, 1}, {0, 2, 3}, {1, 2, 1}, {2, 3, 2}};
        int start = 0;
        int end = 3;
        
        int result = sendEmail(n, edges, start, end);
        if (result == -1) {
            System.out.println("unreachable");
        } else {
            System.out.println(result);
        }
    }
}