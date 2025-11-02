package class064;

import java.util.*;

/**
 * POJ 2253 Frogger
 * 
 * 题目链接: http://poj.org/problem?id=2253
 * 
 * 题目描述:
 * 一只小青蛙住在一条繁忙的河边。有一天，它想去看望它的朋友，它的朋友住在河的另一边。
 * 这条河可以看作是一个二维平面，青蛙可以从一个石头跳到另一个石头。
 * 每次跳跃的长度是两个石头之间的欧几里得距离。
 * 青蛙希望找到一条路径，使得路径上最长的跳跃距离尽可能小。
 * 
 * 解题思路:
 * 这是一个变形的最短路径问题，称为瓶颈路径问题。
 * 我们需要找到从起点到终点的路径，使得路径上边权的最大值最小。
 * 可以使用修改版的Dijkstra算法来解决。
 * 
 * 算法应用场景:
 * - 网络传输中的最大延迟路径
 * - 机器人路径规划中的最大步长限制
 * - 游戏中的角色移动路径优化
 * 
 * 时间复杂度分析:
 * O((V + E) * log V)，其中V是节点数，E是边数
 * 
 * 空间复杂度分析:
 * O(V + E)，用于存储图和距离数组
 */
public class Code35_FroggerPOJ {
    
    /**
     * 使用修改版Dijkstra算法计算青蛙跳跃的最小最大距离
     * 
     * 算法步骤:
     * 1. 构建图的邻接表表示，边权为两点间的欧几里得距离
     * 2. 初始化距离数组，起始节点距离为0，其他节点为无穷大
     * 3. 使用优先队列维护待处理节点，按距离从小到大排序
     * 4. 不断取出距离最小的节点，更新其邻居节点的最短距离
     * 5. 返回目标节点的最短距离
     * 
     * 时间复杂度: O((V + E) * log V)
     * 空间复杂度: O(V + E)
     * 
     * @param stones 石头的坐标数组，每个元素为 [x, y]
     * @param start 起始石头索引
     * @param end 目标石头索引
     * @return 青蛙跳跃的最小最大距离
     */
    public static double frogger(int[][] stones, int start, int end) {
        int n = stones.length;
        
        // 构建邻接表表示的图
        List<List<double[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 计算所有石头之间的距离并添加边
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // 计算两点间的欧几里得距离
                double dist = Math.sqrt(Math.pow(stones[i][0] - stones[j][0], 2) + 
                                       Math.pow(stones[i][1] - stones[j][1], 2));
                graph.get(i).add(new double[]{j, dist});
                graph.get(j).add(new double[]{i, dist});
            }
        }
        
        // distance[i] 表示从起始石头到石头i的最小最大跳跃距离
        double[] distance = new double[n];
        Arrays.fill(distance, Double.MAX_VALUE);
        distance[start] = 0;
        
        // visited[i] 表示石头i是否已经确定了最短距离
        boolean[] visited = new boolean[n];
        
        // 优先队列，按距离从小到大排序
        PriorityQueue<double[]> heap = new PriorityQueue<>((a, b) -> Double.compare(a[1], b[1]));
        heap.add(new double[]{start, 0});
        
        // 修改版Dijkstra算法主循环
        while (!heap.isEmpty()) {
            // 取出最小最大跳跃距离的节点
            double[] current = heap.poll();
            int u = (int) current[0];
            double dist = current[1];
            
            // 如果已经处理过，跳过
            if (visited[u]) {
                continue;
            }
            
            // 如果到达目标石头，直接返回结果
            if (u == end) {
                return dist;
            }
            
            // 标记为已处理
            visited[u] = true;
            
            // 遍历u的所有邻居石头
            for (double[] edge : graph.get(u)) {
                int v = (int) edge[0];  // 邻居石头
                double w = edge[1];     // 跳跃距离
                
                // 新的距离是当前路径上的最大跳跃距离
                double newDist = Math.max(dist, w);
                
                // 如果新的最大跳跃距离更小，则更新
                if (!visited[v] && newDist < distance[v]) {
                    distance[v] = newDist;
                    heap.add(new double[]{v, distance[v]});
                }
            }
        }
        
        // 理论上不会执行到这里
        return -1;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 示例测试用例
        int[][] stones = {{0, 0}, {1, 0}, {2, 0}, {3, 0}};
        int start = 0;
        int end = 3;
        
        double result = frogger(stones, start, end);
        System.out.printf("青蛙跳跃的最小最大距离为: %.3f\n", result);
    }
}