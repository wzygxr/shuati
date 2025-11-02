package class064;

import java.util.*;
import java.io.*;

// Codeforces 20C Dijkstra?
// 给定一个带权无向图。你需要求出从点1到点n的最短路
// 如果无法从点1到达点n，则输出-1
// 否则输出最短路径（路径上的点序列）
// 测试链接：https://codeforces.com/problemset/problem/20/C
public class Code15_DijkstraCodeforces {

    // 使用Dijkstra算法求解最短路径
    // 时间复杂度: O((V+E)logV) 其中V是节点数，E是边数
    // 空间复杂度: O(V+E) 存储图和距离数组
    public static List<Integer> dijkstra(int n, int[][] edges) {
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
        
        // distance[i] 表示从源节点1到节点i的最短距离
        long[] distance = new long[n + 1];
        // 初始化距离为无穷大
        Arrays.fill(distance, Long.MAX_VALUE);
        // 源节点到自己的距离为0
        distance[1] = 0;
        
        // parent[i] 表示在最短路径树中节点i的父节点
        int[] parent = new int[n + 1];
        Arrays.fill(parent, -1);
        
        // visited[i] 表示节点i是否已经确定了最短距离
        boolean[] visited = new boolean[n + 1];
        
        // 优先队列，按距离从小到大排序
        // 0 : 当前节点
        // 1 : 源点到当前点距离
        PriorityQueue<long[]> heap = new PriorityQueue<>((a, b) -> Long.compare(a[1], b[1]));
        heap.add(new long[] { 1, 0 });
        
        while (!heap.isEmpty()) {
            // 取出距离源点最近的节点
            long[] record = heap.poll();
            int u = (int) record[0];
            long dist = record[1];
            
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
                    parent[v] = u;
                    heap.add(new long[] { v, distance[v] });
                }
            }
        }
        
        // 如果无法到达终点，返回空列表
        if (distance[n] == Long.MAX_VALUE) {
            return new ArrayList<>();
        }
        
        // 重构路径
        List<Integer> path = new ArrayList<>();
        int current = n;
        while (current != -1) {
            path.add(current);
            current = parent[current];
        }
        Collections.reverse(path);
        
        return path;
    }

    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 5;
        int[][] edges1 = {{1,2,2},{2,5,5},{2,3,4},{1,4,1},{4,3,3},{3,5,1}};
        List<Integer> result1 = dijkstra(n1, edges1);
        if (result1.isEmpty()) {
            System.out.println("-1");
        } else {
            for (int i = 0; i < result1.size(); i++) {
                System.out.print(result1.get(i));
                if (i < result1.size() - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}