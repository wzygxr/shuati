package class064;

import java.util.*;

// UVa OJ 10986 Sending email
// 给定一个网络和节点之间的延迟，找出从源节点到目标节点的最短时间
// 如果无法到达目标节点，则返回"unreachable"
// 测试链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1927
public class Code19_SendingEmailUva {

    // 使用Dijkstra算法求解单源最短路径
    // 时间复杂度: O((V+E)logV) 其中V是节点数，E是边数
    // 空间复杂度: O(V+E) 存储图和距离数组
    public static String sendingEmail(int n, int[][] edges, int start, int end) {
        // 构建邻接表表示的图
        ArrayList<ArrayList<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
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
        
        // distance[i] 表示从源节点start到节点i的最短距离
        int[] distance = new int[n];
        // 初始化距离为无穷大
        Arrays.fill(distance, Integer.MAX_VALUE);
        // 源节点到自己的距离为0
        distance[start] = 0;
        
        // visited[i] 表示节点i是否已经确定了最短距离
        boolean[] visited = new boolean[n];
        
        // 优先队列，按距离从小到大排序
        // 0 : 当前节点
        // 1 : 源点到当前点距离
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        heap.add(new int[] { start, 0 });
        
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
        
        // 如果无法到达终点，返回"unreachable"
        if (distance[end] == Integer.MAX_VALUE) {
            return "unreachable";
        }
        
        // 返回最短距离
        return String.valueOf(distance[end]);
    }

    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 4;
        int[][] edges1 = {{0,1,1},{1,2,1},{2,3,1},{3,0,1},{0,2,3}};
        int start1 = 0;
        int end1 = 3;
        System.out.println("测试用例1结果: " + sendingEmail(n1, edges1, start1, end1)); // 期望输出: 1
        
        // 测试用例2
        int n2 = 2;
        int[][] edges2 = {{0,1,5}};
        int start2 = 0;
        int end2 = 1;
        System.out.println("测试用例2结果: " + sendingEmail(n2, edges2, start2, end2)); // 期望输出: 5
    }
}