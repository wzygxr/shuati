package class064;

import java.util.*;

/**
 * 严格次短路
 * 
 * 题目链接: https://www.luogu.com.cn/problem/P2865
 * 
 * 题目描述：
 * 给定一个包含 N 个点、M 条边的无向图，节点编号为 1~N。
 * 求从节点 1 到节点 N 的严格次短路径长度。
 * 
 * 解题思路：
 * 这是一个在Dijkstra算法基础上扩展的问题，不仅要计算最短路径，还要计算严格次短路径。
 * 严格次短路径是指长度严格大于最短路径的最短路径。
 * 我们需要维护每个节点的最短距离和严格次短距离。
 * 使用Dijkstra算法扩展版本来解决这个问题。
 * 
 * 算法应用场景：
 * - 网络路由中的备用路径选择
 * - 交通导航中的备选路线规划
 * - 图论中的路径优化问题
 * 
 * 时间复杂度分析：
 * O((V+E)logV) 其中V是节点数，E是边数
 * 
 * 空间复杂度分析：
 * O(V+E) 存储图、距离数组
 */
public class Code11_SecondShortestPath {

    /**
     * 使用Dijkstra算法求解严格次短路径
     * 
     * 算法核心思想：
     * 1. 在传统Dijkstra算法基础上，增加次短路径计算功能
     * 2. 维护两个数组：distance[i][0]记录最短距离，distance[i][1]记录严格次短距离
     * 3. 当发现更短路径时，更新最短距离，并将原最短距离赋给次短距离
     * 4. 当发现比最短路径长但比次短路径短的路径时，更新次短距离
     * 
     * 算法步骤：
     * 1. 初始化距离数组，最短距离和次短距离都为无穷大
     * 2. 使用优先队列维护待处理节点，按距离从小到大排序
     * 3. 不断取出距离最小的节点，更新其邻居节点的最短距离和次短距离
     * 4. 返回终点的严格次短距离
     * 
     * 时间复杂度: O((V+E)logV) 其中V是节点数，E是边数
     * 空间复杂度: O(V+E)
     * 
     * @param n 节点数量
     * @param edges 边的信息，edges[i] = [起点, 终点, 权重]
     * @return 从节点1到节点n的严格次短路径长度
     */
    public static int secondShortestPath(int n, int[][] edges) {
        // 构建邻接表表示的图
        // graph[i] 存储节点i的所有邻居节点及其边权重
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 添加边到图中（无向图）
        // 对于每条边，将其添加到两个节点的邻居列表中
        for (int[] edge : edges) {
            int u = edge[0];  // 边的起点
            int v = edge[1];  // 边的终点
            int w = edge[2];  // 边的权重
            // 添加u到v的边
            graph.get(u).add(new int[]{v, w});
            // 添加v到u的边（无向图）
            graph.get(v).add(new int[]{u, w});
        }
        
        // distance[i][0]表示从节点1到节点i的最短距离
        // distance[i][1]表示从节点1到节点i的严格次短距离
        // 初始化为最大值，表示尚未访问
        int[][] distance = new int[n + 1][2];
        for (int i = 0; i <= n; i++) {
            distance[i][0] = Integer.MAX_VALUE;  // 最短距离
            distance[i][1] = Integer.MAX_VALUE;  // 严格次短距离
        }
        
        // 初始状态：在节点1，最短距离为0
        distance[1][0] = 0;
        
        // 优先队列，按距离从小到大排序
        // 数组含义：[0] 当前节点, [1] 源点到当前节点的距离, [2] 距离类型（0表示最短距离，1表示次短距离）
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        // 将起点加入优先队列，类型为最短距离
        heap.add(new int[]{1, 0, 0});
        
        // Dijkstra算法主循环
        while (!heap.isEmpty()) {
            // 取出距离最小的节点
            int[] record = heap.poll();
            int u = record[0];     // 当前节点
            int dist = record[1];  // 当前距离
            int type = record[2];  // 距离类型（0表示最短距离，1表示次短距离）
            
            // 遍历所有邻居节点
            for (int[] edge : graph.get(u)) {
                int v = edge[0];   // 邻居节点
                int w = edge[1];   // 边的权重
                // 通过u到达v的新距离
                int newDist = dist + w;
                
                // 情况1：当前路径比当前最短路短，更新最短路，把原来的最短路赋给次小路
                if (newDist < distance[v][0]) {
                    // 将原来的最短距离赋给次短距离
                    distance[v][1] = distance[v][0];
                    // 更新最短距离
                    distance[v][0] = newDist;
                    // 将更新后的次短距离加入优先队列
                    heap.add(new int[]{v, distance[v][1], 1});
                    // 将更新后的最短距离加入优先队列
                    heap.add(new int[]{v, distance[v][0], 0});
                } 
                // 情况2：等于最短路，直接跳过，因为要求的是严格次短路
                else if (newDist == distance[v][0]) {
                    continue;
                } 
                // 情况3：比最短路长，比次短路短，更新次短路
                else if (newDist < distance[v][1]) {
                    // 更新次短距离
                    distance[v][1] = newDist;
                    // 将更新后的次短距离加入优先队列
                    heap.add(new int[]{v, distance[v][1], 1});
                }
                // 情况4：等于或比次短路长，跳过
                // 不需要处理，因为不会产生更优的解
            }
        }
        
        // 返回终点的严格次短距离
        return distance[n][1];
    }

    // 测试用例
    public static void main(String[] args) {
        // 示例
        // 输入: n = 4, edges = [[1, 2, 100], [2, 3, 200], [2, 4, 250], [3, 4, 100]]
        // 输出: 450
        // 解释: 最短路径1->2->4长度为350，严格次短路径1->2->3->4长度为450
        int n = 4;
        int[][] edges = {{1, 2, 100}, {2, 3, 200}, {2, 4, 250}, {3, 4, 100}};
        System.out.println(secondShortestPath(n, edges)); // 输出: 450
    }
}