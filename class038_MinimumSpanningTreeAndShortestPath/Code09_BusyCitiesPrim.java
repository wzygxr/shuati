package class061;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;
import java.util.PriorityQueue;

// 洛谷P2330 [SCOI2005]繁忙的都市 (Prim算法实现)
// 题目链接: https://www.luogu.com.cn/problem/P2330
// 
// 题目描述:
// 城市C是一个非常繁忙的大都市，城市中的道路十分的拥挤，于是市长决定对其中的道路进行改造。
// 城市C的道路是这样分布的：城市中有n个交叉路口，有些交叉路口之间有道路相连，
// 两个交叉路口之间最多有一条道路相连接。这些道路是双向的，且把所有的交叉路口直接或间接的连接起来了。
// 每条道路都有一个分值，分值越小表示这个道路越繁忙，越需要进行改造。
// 但是市政府的资金有限，市长希望进行改造的道路越少越好，于是他提出下面的要求：
// 1. 改造的那些道路能够把所有的交叉路口直接或间接的连通起来
// 2. 在满足要求1的情况下，改造的道路尽量少
// 3. 在满足要求1、2的情况下，改造的那些道路中分值最大的道路分值尽量小
// 任务：选择哪些道路应当被修建，返回选出了几条道路以及分值最大的那条道路的分值是多少
//
// 解题思路:
// 这是一个典型的最小生成树问题。要求选出的边数最少且最大边权最小，
// 这正是最小生成树的性质。使用Prim算法：
// 1. 从任意一个节点开始（这里选择节点1）
// 2. 使用优先队列维护从已选节点集合到未选节点集合的最小边
// 3. 不断选择最小边，直到所有节点都被包含在生成树中
// 4. 记录选出的边数和最大边权
//
// 时间复杂度: O((V + E) * log V)，其中V是节点数，E是边数
// 空间复杂度: O(V + E)
// 是否为最优解: 是，这是解决该问题的高效方法

public class Code09_BusyCitiesPrim {
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) in.nval;
            in.nextToken();
            int m = (int) in.nval;
            
            // 构建邻接表
            // graph[i] 存储与节点i相连的边 [相邻节点, 边的分值]
            java.util.ArrayList<int[]>[] graph = new java.util.ArrayList[n + 1];
            for (int i = 1; i <= n; i++) {
                graph[i] = new java.util.ArrayList<>();
            }
            
            // 读取边信息
            for (int i = 0; i < m; i++) {
                in.nextToken();
                int u = (int) in.nval;
                in.nextToken();
                int v = (int) in.nval;
                in.nextToken();
                int w = (int) in.nval;
                graph[u].add(new int[]{v, w});
                graph[v].add(new int[]{u, w});
            }
            
            // 使用Prim算法求最小生成树
            int[] result = prim(n, graph);
            out.println(result[0] + " " + result[1]);
        }
        
        out.flush();
        out.close();
        br.close();
    }
    
    /**
     * 使用Prim算法求解最小生成树
     * 
     * @param n 节点数
     * @param graph 邻接表表示的图
     * @return [选出的边数, 最大边权]
     */
    public static int[] prim(int n, java.util.ArrayList<int[]>[] graph) {
        // 使用优先队列实现Prim算法
        // 队列中元素为 [节点, 边的分值]
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        
        // 从节点1开始
        for (int[] edge : graph[1]) {
            pq.offer(edge);
        }
        
        boolean[] visited = new boolean[n + 1]; // 标记节点是否已访问
        visited[1] = true; // 节点1初始时标记为已访问
        
        int edgesCount = 0;  // 选出的边数
        int maxWeight = 0;   // 最大边权
        
        // 当优先队列不为空且还未选择n-1条边时继续
        while (!pq.isEmpty() && edgesCount < n - 1) {
            int[] edge = pq.poll();
            int node = edge[0];
            int weight = edge[1];
            
            // 如果节点已访问，跳过
            if (visited[node]) {
                continue;
            }
            
            // 将节点标记为已访问
            visited[node] = true;
            edgesCount++;
            maxWeight = Math.max(maxWeight, weight);
            
            // 将与当前节点相连的所有边加入优先队列
            for (int[] neighbor : graph[node]) {
                int nextNode = neighbor[0];
                if (!visited[nextNode]) {
                    pq.offer(neighbor);
                }
            }
        }
        
        return new int[]{edgesCount, maxWeight};
    }
}