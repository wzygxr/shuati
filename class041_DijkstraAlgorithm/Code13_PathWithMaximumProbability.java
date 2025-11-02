package class064;

import java.util.*;

// 路径中最大概率问题
// 给你一个由 n 个节点（下标从 0 开始）组成的无向加权图，记为 G
// 该图由边的列表表示，其中 edges[i] = [a, b] 表示连接节点 a 和 b 的无向边，且该边遍历成功的概率为 succProb[i]
// 指定两个节点分别作为起点 start 和终点 end，要求找出从起点到终点成功的最大概率
// 如果不存在从 start 到 end 的路径，返回 0
// 测试链接：https://leetcode.cn/problems/path-with-maximum-probability
public class Code13_PathWithMaximumProbability {

    // 使用Dijkstra算法求解最大概率路径
    // 时间复杂度: O((V+E)logV) 其中V是节点数，E是边数
    // 空间复杂度: O(V+E) 存储图和距离数组
    public static double maxProbability(int n, int[][] edges, double[] succProb, int start, int end) {
        // 构建邻接表表示的图
        ArrayList<ArrayList<double[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 添加边到图中
        for (int i = 0; i < edges.length; i++) {
            int u = edges[i][0];
            int v = edges[i][1];
            double prob = succProb[i];
            // 无向图，需要添加两条边
            graph.get(u).add(new double[] { v, prob });
            graph.get(v).add(new double[] { u, prob });
        }
        
        // probability[i] 表示从起点start到节点i的最大概率
        double[] probability = new double[n];
        // 初始化概率为0
        Arrays.fill(probability, 0.0);
        // 起点到自己的概率为1
        probability[start] = 1.0;
        
        // visited[i] 表示节点i是否已经确定了最大概率
        boolean[] visited = new boolean[n];
        
        // 优先队列，按概率从大到小排序
        // 0 : 当前节点
        // 1 : 起点到当前点的概率
        PriorityQueue<double[]> heap = new PriorityQueue<>((a, b) -> Double.compare(b[1], a[1]));
        heap.add(new double[] { start, 1.0 });
        
        while (!heap.isEmpty()) {
            // 取出概率最大的节点
            double[] record = heap.poll();
            int u = (int) record[0];
            double prob = record[1];
            
            // 如果已经处理过，跳过
            if (visited[u]) {
                continue;
            }
            // 标记为已处理
            visited[u] = true;
            
            // 遍历u的所有邻居节点
            for (double[] edge : graph.get(u)) {
                int v = (int) edge[0];  // 邻居节点
                double edgeProb = edge[1];  // 边的概率
                
                // 如果邻居节点未访问且通过u到达v的概率更大，则更新
                if (!visited[v] && probability[u] * edgeProb > probability[v]) {
                    probability[v] = probability[u] * edgeProb;
                    heap.add(new double[] { v, probability[v] });
                }
            }
        }
        
        return probability[end];
    }

    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 3;
        int[][] edges1 = {{0,1},{1,2},{0,2}};
        double[] succProb1 = {0.5, 0.5, 0.2};
        int start1 = 0, end1 = 2;
        System.out.println("测试用例1结果: " + maxProbability(n1, edges1, succProb1, start1, end1)); // 期望输出: 0.25
        
        // 测试用例2
        int n2 = 3;
        int[][] edges2 = {{0,1},{1,2},{0,2}};
        double[] succProb2 = {0.5, 0.5, 0.3};
        int start2 = 0, end2 = 2;
        System.out.println("测试用例2结果: " + maxProbability(n2, edges2, succProb2, start2, end2)); // 期望输出: 0.3
        
        // 测试用例3
        int n3 = 3;
        int[][] edges3 = {{0,1}};
        double[] succProb3 = {0.5};
        int start3 = 0, end3 = 2;
        System.out.println("测试用例3结果: " + maxProbability(n3, edges3, succProb3, start3, end3)); // 期望输出: 0.0
    }
}