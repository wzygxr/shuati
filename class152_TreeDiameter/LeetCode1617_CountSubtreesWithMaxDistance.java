package class121;

// LeetCode 1617. 统计子树中城市之间最大距离
// 题目：有 n 个城市，编号从 1 到 n。给你一个整数 n 和一个数组 edges，
// 其中 edges[i] = [ui, vi] 表示城市 ui 和 vi 之间有一条双向道路。
// 题目保证所有城市形成一棵树。
// 对于每个 d（1 ≤ d ≤ n-1），请你统计有多少个连通子图（子树）的直径恰好为 d。
// 返回一个长度为 n-1 的数组 answer，其中 answer[d-1] 表示直径为 d 的子树数目。
// 来源：LeetCode
// 链接：https://leetcode.cn/problems/count-subtrees-with-max-distance-between-cities/

import java.util.*;

public class LeetCode1617_CountSubtreesWithMaxDistance {
    
    /**
     * 主方法：统计所有直径的子树数量
     * @param n 城市数量
     * @param edges 道路列表
     * @return 长度为n-1的数组，answer[d-1]表示直径为d的子树数目
     * 
     * 时间复杂度：O(n^3 * 2^n)，其中n是城市数量
     * 空间复杂度：O(n^2)
     */
    public int[] countSubgraphsForEachDiameter(int n, int[][] edges) {
        // 构建邻接矩阵
        int[][] dist = new int[n][n];
        
        // 初始化距离矩阵
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
            dist[i][i] = 0;
        }
        
        // 填充直接相连的边
        for (int[] edge : edges) {
            int u = edge[0] - 1; // 转换为0-based索引
            int v = edge[1] - 1;
            dist[u][v] = 1;
            dist[v][u] = 1;
        }
        
        // Floyd-Warshall算法计算所有点对最短距离
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE) {
                        dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                    }
                }
            }
        }
        
        // 结果数组
        int[] result = new int[n - 1];
        
        // 枚举所有非空子集（使用位掩码）
        for (int mask = 1; mask < (1 << n); mask++) {
            // 检查子集是否连通
            if (isConnected(mask, dist, n)) {
                // 计算子集的直径
                int diameter = getDiameter(mask, dist, n);
                if (diameter > 0 && diameter <= n - 1) {
                    result[diameter - 1]++;
                }
            }
        }
        
        return result;
    }
    
    /**
     * 检查子集是否连通
     * @param mask 位掩码表示的子集
     * @param dist 距离矩阵
     * @param n 总节点数
     * @return 子集是否连通
     */
    private boolean isConnected(int mask, int[][] dist, int n) {
        // 找到子集中的第一个节点
        int start = -1;
        for (int i = 0; i < n; i++) {
            if ((mask & (1 << i)) != 0) {
                start = i;
                break;
            }
        }
        
        if (start == -1) return false; // 空子集
        
        // BFS检查连通性
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[n];
        queue.offer(start);
        visited[start] = true;
        
        int count = 1; // 已访问节点数
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            for (int neighbor = 0; neighbor < n; neighbor++) {
                // 检查邻居是否在子集中且直接相连
                if ((mask & (1 << neighbor)) != 0 && 
                    dist[current][neighbor] == 1 && 
                    !visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                    count++;
                }
            }
        }
        
        // 检查是否所有子集节点都被访问
        int subsetSize = Integer.bitCount(mask);
        return count == subsetSize;
    }
    
    /**
     * 计算子集的直径
     * @param mask 位掩码表示的子集
     * @param dist 距离矩阵
     * @param n 总节点数
     * @return 子集的直径
     */
    private int getDiameter(int mask, int[][] dist, int n) {
        int diameter = 0;
        
        // 遍历子集中的所有点对，找到最大距离
        for (int i = 0; i < n; i++) {
            if ((mask & (1 << i)) != 0) {
                for (int j = i + 1; j < n; j++) {
                    if ((mask & (1 << j)) != 0 && dist[i][j] != Integer.MAX_VALUE) {
                        diameter = Math.max(diameter, dist[i][j]);
                    }
                }
            }
        }
        
        return diameter;
    }
    
    /**
     * 优化的方法：使用树形DP思想
     * @param n 城市数量
     * @param edges 道路列表
     * @return 结果数组
     * 
     * 时间复杂度：O(n^2 * 2^n)
     * 空间复杂度：O(n * 2^n)
     */
    public int[] countSubgraphsForEachDiameterOptimized(int n, int[][] edges) {
        // 构建邻接表
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        for (int[] edge : edges) {
            int u = edge[0] - 1;
            int v = edge[1] - 1;
            graph[u].add(v);
            graph[v].add(u);
        }
        
        // 结果数组
        int[] result = new int[n - 1];
        
        // 枚举所有非空子集
        for (int mask = 1; mask < (1 << n); mask++) {
            // 找到子集中的节点
            List<Integer> nodes = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    nodes.add(i);
                }
            }
            
            // 如果子集大小小于2，直径至少为1
            if (nodes.size() < 2) continue;
            
            // 构建子图
            Map<Integer, List<Integer>> subgraph = buildSubgraph(mask, graph, n);
            
            // 检查连通性并计算直径
            if (isConnectedSubgraph(subgraph, nodes.get(0), nodes.size())) {
                int diameter = calculateDiameter(subgraph, nodes);
                if (diameter > 0 && diameter <= n - 1) {
                    result[diameter - 1]++;
                }
            }
        }
        
        return result;
    }
    
    /**
     * 构建子图的邻接表
     * @param mask 位掩码
     * @param graph 原图邻接表
     * @param n 节点总数
     * @return 子图邻接表
     */
    private Map<Integer, List<Integer>> buildSubgraph(int mask, List<Integer>[] graph, int n) {
        Map<Integer, List<Integer>> subgraph = new HashMap<>();
        
        for (int i = 0; i < n; i++) {
            if ((mask & (1 << i)) != 0) {
                subgraph.put(i, new ArrayList<>());
                for (int neighbor : graph[i]) {
                    if ((mask & (1 << neighbor)) != 0) {
                        subgraph.get(i).add(neighbor);
                    }
                }
            }
        }
        
        return subgraph;
    }
    
    /**
     * 检查子图是否连通
     * @param subgraph 子图邻接表
     * @param start 起始节点
     * @param expectedSize 期望的连通分量大小
     * @return 是否连通
     */
    private boolean isConnectedSubgraph(Map<Integer, List<Integer>> subgraph, int start, int expectedSize) {
        if (!subgraph.containsKey(start)) return false;
        
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(start);
        visited.add(start);
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            for (int neighbor : subgraph.get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
        
        return visited.size() == expectedSize;
    }
    
    /**
     * 计算子图的直径
     * @param subgraph 子图邻接表
     * @param nodes 节点列表
     * @return 直径
     */
    private int calculateDiameter(Map<Integer, List<Integer>> subgraph, List<Integer> nodes) {
        if (nodes.size() == 1) return 0;
        
        // 使用两次BFS法计算直径
        int start = nodes.get(0);
        
        // 第一次BFS找到最远节点
        int[] firstBFS = bfs(subgraph, start);
        int farthest = firstBFS[0];
        
        // 第二次BFS找到直径
        int[] secondBFS = bfs(subgraph, farthest);
        
        return secondBFS[1];
    }
    
    /**
     * BFS计算从起点开始的最远节点和距离
     * @param subgraph 子图
     * @param start 起点
     * @return [最远节点, 距离]
     */
    private int[] bfs(Map<Integer, List<Integer>> subgraph, int start) {
        Map<Integer, Integer> distance = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();
        
        distance.put(start, 0);
        queue.offer(start);
        
        int farthestNode = start;
        int maxDistance = 0;
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            int currentDist = distance.get(current);
            
            if (currentDist > maxDistance) {
                maxDistance = currentDist;
                farthestNode = current;
            }
            
            for (int neighbor : subgraph.get(current)) {
                if (!distance.containsKey(neighbor)) {
                    distance.put(neighbor, currentDist + 1);
                    queue.offer(neighbor);
                }
            }
        }
        
        return new int[]{farthestNode, maxDistance};
    }
    
    // 测试方法
    public static void main(String[] args) {
        LeetCode1617_CountSubtreesWithMaxDistance solution = new LeetCode1617_CountSubtreesWithMaxDistance();
        
        // 测试用例1: n=4, edges=[[1,2],[2,3],[2,4]]
        // 树结构：
        //   1
        //   |
        //   2
        //  / \
        // 3   4
        // 预期输出：[3,4,0]（直径1:3个，直径2:4个，直径3:0个）
        int n1 = 4;
        int[][] edges1 = {{1,2},{2,3},{2,4}};
        int[] result1 = solution.countSubgraphsForEachDiameter(n1, edges1);
        System.out.println("测试用例1结果: " + Arrays.toString(result1));
        
        // 测试用例2: n=2, edges=[[1,2]]
        // 树结构：1-2
        // 预期输出：[1]（直径1:1个）
        int n2 = 2;
        int[][] edges2 = {{1,2}};
        int[] result2 = solution.countSubgraphsForEachDiameter(n2, edges2);
        System.out.println("测试用例2结果: " + Arrays.toString(result2));
        
        // 测试用例3: n=3, edges=[[1,2],[2,3]]
        // 树结构：1-2-3
        // 预期输出：[2,1]（直径1:2个，直径2:1个）
        int n3 = 3;
        int[][] edges3 = {{1,2},{2,3}};
        int[] result3 = solution.countSubgraphsForEachDiameter(n3, edges3);
        System.out.println("测试用例3结果: " + Arrays.toString(result3));
        
        // 使用优化方法测试
        System.out.println("=== 使用优化方法 ===");
        int[] result1Opt = solution.countSubgraphsForEachDiameterOptimized(n1, edges1);
        System.out.println("测试用例1(优化)结果: " + Arrays.toString(result1Opt));
        
        int[] result2Opt = solution.countSubgraphsForEachDiameterOptimized(n2, edges2);
        System.out.println("测试用例2(优化)结果: " + Arrays.toString(result2Opt));
        
        int[] result3Opt = solution.countSubgraphsForEachDiameterOptimized(n3, edges3);
        System.out.println("测试用例3(优化)结果: " + Arrays.toString(result3Opt));
    }
}