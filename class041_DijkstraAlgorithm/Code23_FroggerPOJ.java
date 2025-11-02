package class064;

import java.util.*;

// POJ 2253 Frogger
// Freddy Frog坐在湖中间的一块石头上，突然他注意到Fiona Frog坐在另一块石头上
// 他计划去拜访她，但由于水很脏且充满了游客的防晒霜，他想避免游泳，而是通过跳跃到达
// 你的任务是计算Freddy到达Fiona那里的最小跳跃距离
// 测试链接：http://poj.org/problem?id=2253
public class Code23_FroggerPOJ {

    // 使用修改的Dijkstra算法求解最小最大跳跃距离
    // 时间复杂度: O((V+E)logV) 其中V是节点数，E是边数
    // 空间复杂度: O(V+E) 存储图和距离数组
    public static double frogger(int n, int[][] stones) {
        // 构建邻接矩阵表示的图，存储两点间的欧几里得距离
        double[][] graph = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    int dx = stones[i][0] - stones[j][0];
                    int dy = stones[i][1] - stones[j][1];
                    graph[i][j] = Math.sqrt(dx * dx + dy * dy);
                }
            }
        }
        
        // maxJump[i] 表示从源节点0到节点i的路径上最大跳跃距离的最小值
        double[] maxJump = new double[n];
        // 初始化跳跃距离为无穷大
        Arrays.fill(maxJump, Double.MAX_VALUE);
        // 源节点到自己的跳跃距离为0
        maxJump[0] = 0;
        
        // visited[i] 表示节点i是否已经确定了最小最大跳跃距离
        boolean[] visited = new boolean[n];
        
        // 优先队列，按最大跳跃距离从小到大排序
        // 0 : 当前节点
        // 1 : 源点到当前点的最小最大跳跃距离
        PriorityQueue<double[]> heap = new PriorityQueue<>((a, b) -> Double.compare(a[1], b[1]));
        heap.add(new double[] { 0, 0 });
        
        while (!heap.isEmpty()) {
            // 取出最大跳跃距离最小的节点
            double[] record = heap.poll();
            int u = (int) record[0];
            double jump = record[1];
            
            // 如果已经处理过，跳过
            if (visited[u]) {
                continue;
            }
            // 标记为已处理
            visited[u] = true;
            
            // 遍历u的所有邻居节点
            for (int v = 0; v < n; v++) {
                if (u != v) {
                    // 计算通过当前路径到达新点的最大跳跃距离
                    // 是当前路径上所有跳跃距离的最大值
                    double newJump = Math.max(maxJump[u], graph[u][v]);
                    
                    // 如果新的最大跳跃距离更小，则更新
                    if (!visited[v] && newJump < maxJump[v]) {
                        maxJump[v] = newJump;
                        heap.add(new double[] { v, maxJump[v] });
                    }
                }
            }
        }
        
        return maxJump[1];
    }

    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 2;
        int[][] stones1 = {{0,0},{3,4}};
        System.out.printf("测试用例1结果: %.3f\n", frogger(n1, stones1)); // 期望输出: 5.000
    }
}