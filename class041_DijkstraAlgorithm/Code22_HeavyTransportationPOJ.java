package class064;

import java.util.*;

// POJ 1797 Heavy Transportation
// 你的任务是找出从交叉点1（Hugo的位置）到交叉点n（客户的位置）可以运输的最大重量
// 测试链接：http://poj.org/problem?id=1797
public class Code22_HeavyTransportationPOJ {

    // 使用修改的Dijkstra算法求解最大载重路径
    // 时间复杂度: O((V+E)logV) 其中V是节点数，E是边数
    // 空间复杂度: O(V+E) 存储图和距离数组
    public static int heavyTransportation(int n, int[][] edges) {
        // 构建邻接表表示的图
        ArrayList<ArrayList<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
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
        
        // maxWeight[i] 表示从源节点1到节点i的最大载重量
        int[] maxWeight = new int[n + 1];
        // 初始化载重量为0
        Arrays.fill(maxWeight, 0);
        // 源节点到自己的载重量为无穷大
        maxWeight[1] = Integer.MAX_VALUE;
        
        // visited[i] 表示节点i是否已经确定了最大载重量
        boolean[] visited = new boolean[n + 1];
        
        // 优先队列，按载重量从大到小排序
        // 0 : 当前节点
        // 1 : 源点到当前点的最大载重量
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> b[1] - a[1]);
        heap.add(new int[] { 1, Integer.MAX_VALUE });
        
        while (!heap.isEmpty()) {
            // 取出载重量最大的节点
            int[] record = heap.poll();
            int u = record[0];
            int weight = record[1];
            
            // 如果已经处理过，跳过
            if (visited[u]) {
                continue;
            }
            // 标记为已处理
            visited[u] = true;
            
            // 遍历u的所有邻居节点
            for (int[] edge : graph.get(u)) {
                int v = edge[0];  // 邻居节点
                int w = edge[1];  // 边的权重（载重量）
                
                // 计算通过当前路径到达新点的最大载重量
                // 是当前路径上所有边载重量的最小值
                int newWeight = Math.min(maxWeight[u], w);
                
                // 如果新的载重量更大，则更新
                if (!visited[v] && newWeight > maxWeight[v]) {
                    maxWeight[v] = newWeight;
                    heap.add(new int[] { v, maxWeight[v] });
                }
            }
        }
        
        return maxWeight[n];
    }

    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 3;
        int[][] edges1 = {{1,2,3},{1,3,2},{2,3,4}};
        System.out.println("测试用例1结果: " + heavyTransportation(n1, edges1)); // 期望输出: 3
    }
}