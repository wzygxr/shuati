package class061;

import java.util.Arrays;
import java.util.PriorityQueue;

// LeetCode 1168. Optimize Water Distribution in a Village (Prim算法实现)
// 题目链接: https://leetcode.cn/problems/optimize-water-distribution-in-a-village/
// 
// 题目描述:
// 村里面一共有 n 栋房子。我们希望通过建造水井和铺设管道来为所有房子供水。
// 对于每个房子 i，我们有两种可选的供水方案：
// 一种是直接在房子内建造水井，成本为 wells[i-1] 
// 另一种是从另一口井铺设管道引水，数组 pipes 给出了在房子间铺设管道的成本，
// 其中每个 pipes[j] = [house1j, house2j, costj] 代表用管道将 house1j 和 house2j连接在一起的成本。
// 连接是双向的。请返回为所有房子都供水的最低总成本。
//
// 解题思路:
// 这个问题可以通过Prim算法解决。我们可以引入一个虚拟节点0，它与每个房子i之间有一条权重为wells[i-1]的边，
// 表示在房子i处建造水井的成本。这样问题就转化为在这个图中找到最小生成树。
// 使用Prim算法：
// 1. 从节点0开始（虚拟节点，代表可以打井）
// 2. 使用优先队列维护从已选节点集合到未选节点集合的最小边
// 3. 不断选择最小边，直到所有节点都被包含在生成树中
//
// 时间复杂度: O((V + E) * log V)，其中V是节点数，E是边数
// 空间复杂度: O(V + E)
// 是否为最优解: 是，这是解决该问题的高效方法

public class Code08_OptimizeWaterDistributionPrim {
    
    public static int minCostToSupplyWater(int n, int[] wells, int[][] pipes) {
        // 构建图的邻接表表示
        // 节点0是虚拟节点，代表可以直接打井
        // 节点1到n代表n栋房子
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]); // [节点, 成本]
        
        // 添加从虚拟节点0到各房子的边（表示在房子处打井）
        for (int i = 0; i < n; i++) {
            pq.offer(new int[]{i + 1, wells[i]}); // [房子编号, 打井成本]
        }
        
        // 构建邻接表
        // graph[i] 存储与节点i相连的边 [相邻节点, 边的成本]
        java.util.ArrayList<int[]>[] graph = new java.util.ArrayList[n + 1];
        for (int i = 0; i <= n; i++) {
            graph[i] = new java.util.ArrayList<>();
        }
        
        // 添加管道边
        for (int[] pipe : pipes) {
            int house1 = pipe[0];
            int house2 = pipe[1];
            int cost = pipe[2];
            graph[house1].add(new int[]{house2, cost});
            graph[house2].add(new int[]{house1, cost});
        }
        
        // Prim算法
        boolean[] visited = new boolean[n + 1]; // 标记节点是否已访问
        visited[0] = true; // 虚拟节点初始时标记为已访问
        
        int totalCost = 0;
        int edgesUsed = 0;
        
        // 当优先队列不为空且还未选择n条边时继续
        while (!pq.isEmpty() && edgesUsed < n) {
            int[] edge = pq.poll();
            int node = edge[0];
            int cost = edge[1];
            
            // 如果节点已访问，跳过
            if (visited[node]) {
                continue;
            }
            
            // 将节点标记为已访问
            visited[node] = true;
            totalCost += cost;
            edgesUsed++;
            
            // 将与当前节点相连的所有边加入优先队列
            for (int[] neighbor : graph[node]) {
                int nextNode = neighbor[0];
                int nextCost = neighbor[1];
                if (!visited[nextNode]) {
                    pq.offer(new int[]{nextNode, nextCost});
                }
            }
        }
        
        return totalCost;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 3;
        int[] wells1 = {1, 2, 2};
        int[][] pipes1 = {{1, 2, 1}, {2, 3, 1}};
        System.out.println("测试用例1结果: " + minCostToSupplyWater(n1, wells1, pipes1)); // 预期输出: 3
        
        // 测试用例2
        int n2 = 2;
        int[] wells2 = {1, 1};
        int[][] pipes2 = {{1, 2, 1}, {1, 2, 2}};
        System.out.println("测试用例2结果: " + minCostToSupplyWater(n2, wells2, pipes2)); // 预期输出: 2
        
        // 测试用例3
        int n3 = 5;
        int[] wells3 = {46012, 72474, 64965, 751, 33304};
        int[][] pipes3 = {{2, 1, 6719}, {3, 2, 75312}, {5, 3, 44918}};
        System.out.println("测试用例3结果: " + minCostToSupplyWater(n3, wells3, pipes3)); // 预期输出: 92516
    }
}