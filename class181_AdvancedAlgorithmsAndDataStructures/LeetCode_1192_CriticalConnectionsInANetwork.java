package class008_AdvancedAlgorithmsAndDataStructures.circle_square_tree_problems;

import java.util.*;

/**
 * LeetCode 1192. Critical Connections in a Network
 * 
 * 题目描述：
 * 给你一个 n 台计算机的网络，服务器从 0 到 n-1 编号。同时给你一个数组 connections，
 * 其中 connections[i] = [a, b] 表示服务器 a 和 b 之间有一条连接。
 * 在这个网络中，任何服务器都可以通过网络直接或间接访问任何其他服务器。
 * 关键连接是指如果删除该连接，某些服务器将无法访问其他服务器。
 * 请你返回所有关键连接（桥）。
 * 
 * 解题思路：
 * 这个问题可以使用圆方树的思想来解决。
 * 关键连接就是图中的桥（割边），删除后会使图不连通。
 * 我们可以使用Tarjan算法来找出所有的桥。
 * 
 * 时间复杂度：O(V + E)
 * 空间复杂度：O(V + E)
 */
public class LeetCode_1192_CriticalConnectionsInANetwork {
    
    static class Solution {
        private List<List<Integer>> graph;
        private int[] dfn; // DFS时间戳
        private int[] low; // 最小时间戳
        private boolean[] visited;
        private int time;
        private List<List<Integer>> criticalConnections;
        
        public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
            // 构建邻接表
            graph = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }
            
            for (List<Integer> connection : connections) {
                int u = connection.get(0);
                int v = connection.get(1);
                graph.get(u).add(v);
                graph.get(v).add(u);
            }
            
            // 初始化
            dfn = new int[n];
            low = new int[n];
            visited = new boolean[n];
            time = 0;
            criticalConnections = new ArrayList<>();
            
            Arrays.fill(dfn, -1);
            Arrays.fill(low, -1);
            
            // 对每个未访问的节点进行DFS
            for (int i = 0; i < n; i++) {
                if (dfn[i] == -1) {
                    tarjan(i, -1);
                }
            }
            
            return criticalConnections;
        }
        
        // Tarjan算法找桥
        private void tarjan(int u, int parent) {
            dfn[u] = low[u] = ++time;
            visited[u] = true;
            
            for (int v : graph.get(u)) {
                if (v == parent) continue;
                
                if (dfn[v] == -1) {
                    // 树边
                    tarjan(v, u);
                    low[u] = Math.min(low[u], low[v]);
                    
                    // 判断是否为桥
                    if (low[v] > dfn[u]) {
                        criticalConnections.add(Arrays.asList(u, v));
                    }
                } else {
                    // 回边
                    low[u] = Math.min(low[u], dfn[v]);
                }
            }
        }
        
        // 另一种解法：使用圆方树
        public List<List<Integer>> criticalConnections2(int n, List<List<Integer>> connections) {
            // 构建邻接表
            List<List<Integer>> graph = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }
            
            for (List<Integer> connection : connections) {
                int u = connection.get(0);
                int v = connection.get(1);
                graph.get(u).add(v);
                graph.get(v).add(u);
            }
            
            // 使用圆方树的思想找桥
            return findBridgesWithCircleSquareTree(n, graph);
        }
        
        // 使用圆方树找桥
        private List<List<Integer>> findBridgesWithCircleSquareTree(int n, List<List<Integer>> graph) {
            List<List<Integer>> bridges = new ArrayList<>();
            int[] dfn = new int[n];
            int[] low = new int[n];
            boolean[] visited = new boolean[n];
            int[] time = {0};
            
            // 对每个未访问的节点进行DFS
            for (int i = 0; i < n; i++) {
                if (!visited[i]) {
                    dfsForBridges(i, -1, graph, dfn, low, visited, time, bridges);
                }
            }
            
            return bridges;
        }
        
        // DFS找桥
        private void dfsForBridges(int u, int parent, List<List<Integer>> graph, 
                                  int[] dfn, int[] low, boolean[] visited, 
                                  int[] time, List<List<Integer>> bridges) {
            visited[u] = true;
            dfn[u] = low[u] = ++time[0];
            
            for (int v : graph.get(u)) {
                if (v == parent) continue;
                
                if (!visited[v]) {
                    dfsForBridges(v, u, graph, dfn, low, visited, time, bridges);
                    low[u] = Math.min(low[u], low[v]);
                    
                    // 如果是桥，添加到结果中
                    if (low[v] > dfn[u]) {
                        bridges.add(Arrays.asList(u, v));
                    }
                } else {
                    low[u] = Math.min(low[u], dfn[v]);
                }
            }
        }
        
        // 使用并查集优化的方法
        public List<List<Integer>> criticalConnections3(int n, List<List<Integer>> connections) {
            // 构建邻接表
            List<List<Integer>> graph = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }
            
            for (List<Integer> connection : connections) {
                int u = connection.get(0);
                int v = connection.get(1);
                graph.get(u).add(v);
                graph.get(v).add(u);
            }
            
            // 使用并查集找桥
            return findBridgesWithUnionFind(n, graph);
        }
        
        // 使用并查集找桥
        private List<List<Integer>> findBridgesWithUnionFind(int n, List<List<Integer>> graph) {
            List<List<Integer>> bridges = new ArrayList<>();
            
            // 对每条边，检查删除后图是否仍然连通
            for (int i = 0; i < n; i++) {
                for (int j : graph.get(i)) {
                    if (i < j) { // 避免重复检查
                        // 删除边(i, j)
                        graph.get(i).remove(Integer.valueOf(j));
                        graph.get(j).remove(Integer.valueOf(i));
                        
                        // 检查图是否仍然连通
                        if (!isConnected(n, graph, i)) {
                            bridges.add(Arrays.asList(i, j));
                        }
                        
                        // 恢复边(i, j)
                        graph.get(i).add(j);
                        graph.get(j).add(i);
                    }
                }
            }
            
            return bridges;
        }
        
        // 检查图是否连通
        private boolean isConnected(int n, List<List<Integer>> graph, int start) {
            boolean[] visited = new boolean[n];
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(start);
            visited[start] = true;
            int visitedCount = 1;
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (int v : graph.get(u)) {
                    if (!visited[v]) {
                        visited[v] = true;
                        visitedCount++;
                        queue.offer(v);
                    }
                }
            }
            
            return visitedCount == n;
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例1
        int n1 = 4;
        List<List<Integer>> connections1 = new ArrayList<>();
        connections1.add(Arrays.asList(0, 1));
        connections1.add(Arrays.asList(1, 2));
        connections1.add(Arrays.asList(2, 0));
        connections1.add(Arrays.asList(1, 3));
        System.out.println("测试用例1:");
        System.out.println("节点数: " + n1);
        System.out.println("连接: " + connections1);
        System.out.println("关键连接: " + solution.criticalConnections(n1, connections1));
        System.out.println("另一种解法结果: " + solution.criticalConnections2(n1, connections1));
        System.out.println();
        
        // 测试用例2
        int n2 = 2;
        List<List<Integer>> connections2 = new ArrayList<>();
        connections2.add(Arrays.asList(0, 1));
        System.out.println("测试用例2:");
        System.out.println("节点数: " + n2);
        System.out.println("连接: " + connections2);
        System.out.println("关键连接: " + solution.criticalConnections(n2, connections2));
        System.out.println("另一种解法结果: " + solution.criticalConnections2(n2, connections2));
        System.out.println();
        
        // 测试用例3
        int n3 = 6;
        List<List<Integer>> connections3 = new ArrayList<>();
        connections3.add(Arrays.asList(0, 1));
        connections3.add(Arrays.asList(1, 2));
        connections3.add(Arrays.asList(2, 0));
        connections3.add(Arrays.asList(1, 3));
        connections3.add(Arrays.asList(3, 4));
        connections3.add(Arrays.asList(4, 5));
        connections3.add(Arrays.asList(5, 3));
        System.out.println("测试用例3:");
        System.out.println("节点数: " + n3);
        System.out.println("连接: " + connections3);
        System.out.println("关键连接: " + solution.criticalConnections(n3, connections3));
        System.out.println("另一种解法结果: " + solution.criticalConnections2(n3, connections3));
    }
}