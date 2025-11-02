package class064;

import java.util.*;

/**
 * 带约束条件的最短路径问题
 * 
 * 题目：K站中转内最便宜的航班（LeetCode 787）
 * 链接：https://leetcode.cn/problems/cheapest-flights-within-k-stops/
 * 
 * 题目描述：
 * 有 n 个城市通过一些航班连接。给你一个数组 flights，
 * 其中 flights[i] = [fromi, toi, pricei] ，表示该航班从城市 fromi 到城市 toi，价格为 pricei。
 * 请你找到出一条最多经过 k 站中转的路线，使得从城市 src 到城市 dst 的价格最便宜，并返回该价格。
 * 如果不存在这样的路线，则返回 -1。
 * 
 * 解题思路：
 * 1. 方法1：动态规划 + Dijkstra算法
 * 2. 方法2：Bellman-Ford算法变种
 * 3. 方法3：BFS + 剪枝
 * 
 * 算法应用场景：
 * - 航班路线规划（中转次数限制）
 * - 网络路由（跳数限制）
 * - 物流配送（中转站限制）
 * 
 * 时间复杂度分析：
 * - 方法1：O(K * E * log(V))
 * - 方法2：O(K * E)
 * - 方法3：O(V^K) 最坏情况，但实际剪枝后效率较高
 */
public class Code30_ConstrainedShortestPath {
    
    /**
     * 方法1：动态规划 + Dijkstra算法（最优解法）
     * 
     * 算法步骤：
     * 1. 使用动态规划思想，dp[k][v]表示经过k次中转到达城市v的最小成本
     * 2. 使用优先队列进行状态扩展，每个状态包含(当前城市, 已用中转次数, 当前成本)
     * 3. 当到达目标城市且中转次数不超过K时，更新最小成本
     * 
     * 时间复杂度：O(K * E * log(V))
     * 空间复杂度：O(V * K)
     */
    public static int findCheapestPrice1(int n, int[][] flights, int src, int dst, int k) {
        // 构建邻接表
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] flight : flights) {
            int from = flight[0], to = flight[1], price = flight[2];
            graph.get(from).add(new int[]{to, price});
        }
        
        // dp数组：dp[stops][city]表示经过stops次中转到达city的最小成本
        int[][] dp = new int[k + 2][n];
        for (int i = 0; i <= k + 1; i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }
        dp[0][src] = 0;
        
        // 优先队列：存储(中转次数, 当前城市, 当前成本)
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[2] - b[2]);
        heap.offer(new int[]{0, src, 0});
        
        while (!heap.isEmpty()) {
            int[] state = heap.poll();
            int stops = state[0];
            int city = state[1];
            int cost = state[2];
            
            // 如果到达目标城市，返回结果
            if (city == dst) {
                return cost;
            }
            
            // 如果中转次数已用完，跳过
            if (stops > k) {
                continue;
            }
            
            // 遍历所有邻居城市
            for (int[] flight : graph.get(city)) {
                int nextCity = flight[0];
                int price = flight[1];
                int newCost = cost + price;
                int newStops = stops + (nextCity == dst ? 0 : 1); // 目标城市不算中转
                
                if (newStops <= k + 1 && newCost < dp[newStops][nextCity]) {
                    dp[newStops][nextCity] = newCost;
                    heap.offer(new int[]{newStops, nextCity, newCost});
                }
            }
        }
        
        return -1;
    }
    
    /**
     * 方法2：Bellman-Ford算法变种
     * 
     * 算法步骤：
     * 1. 进行K+1次松弛操作（K次中转对应K+1条边）
     * 2. 每次迭代更新从源点到各城市的最小成本
     * 3. 使用临时数组避免同一轮次内的相互影响
     * 
     * 时间复杂度：O(K * E)
     * 空间复杂度：O(V)
     */
    public static int findCheapestPrice2(int n, int[][] flights, int src, int dst, int k) {
        // 距离数组
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;
        
        // 进行K+1次松弛操作
        for (int i = 0; i <= k; i++) {
            // 使用临时数组避免同一轮次内的相互影响
            int[] temp = dist.clone();
            boolean updated = false;
            
            for (int[] flight : flights) {
                int from = flight[0], to = flight[1], price = flight[2];
                
                if (dist[from] != Integer.MAX_VALUE && dist[from] + price < temp[to]) {
                    temp[to] = dist[from] + price;
                    updated = true;
                }
            }
            
            dist = temp;
            // 如果没有更新，提前结束
            if (!updated) break;
        }
        
        return dist[dst] == Integer.MAX_VALUE ? -1 : dist[dst];
    }
    
    /**
     * 方法3：BFS + 剪枝
     * 
     * 算法步骤：
     * 1. 使用BFS进行层次遍历，每层代表一次中转
     * 2. 维护每个城市的最小成本，进行剪枝优化
     * 3. 当中转次数超过K时停止搜索
     * 
     * 时间复杂度：O(V^K) 最坏情况，但实际剪枝后效率较高
     * 空间复杂度：O(V)
     */
    public static int findCheapestPrice3(int n, int[][] flights, int src, int dst, int k) {
        // 构建邻接表
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] flight : flights) {
            int from = flight[0], to = flight[1], price = flight[2];
            graph.get(from).add(new int[]{to, price});
        }
        
        // 最小成本数组
        int[] minCost = new int[n];
        Arrays.fill(minCost, Integer.MAX_VALUE);
        minCost[src] = 0;
        
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{src, 0}); // [当前城市, 当前成本]
        
        int stops = 0;
        
        while (!queue.isEmpty() && stops <= k) {
            int size = queue.size();
            
            // 当前层的临时最小成本
            int[] tempCost = minCost.clone();
            
            for (int i = 0; i < size; i++) {
                int[] state = queue.poll();
                int city = state[0];
                int cost = state[1];
                
                for (int[] flight : graph.get(city)) {
                    int nextCity = flight[0];
                    int price = flight[1];
                    int newCost = cost + price;
                    
                    // 剪枝：如果新成本不小于已知最小成本，跳过
                    if (newCost < tempCost[nextCity]) {
                        tempCost[nextCity] = newCost;
                        queue.offer(new int[]{nextCity, newCost});
                    }
                }
            }
            
            minCost = tempCost;
            stops++;
        }
        
        return minCost[dst] == Integer.MAX_VALUE ? -1 : minCost[dst];
    }
    
    /**
     * 扩展：多约束条件的最短路径问题
     * 
     * 题目：带时间和成本约束的路径规划
     * 需要同时考虑时间约束和成本约束，找到满足所有约束条件的最优路径
     */
    public static class MultiConstraintShortestPath {
        
        /**
         * 多约束最短路径算法
         * 
         * @param n 节点数
         * @param edges 边列表，格式为 [u, v, time, cost]
         * @param src 源点
         * @param dst 目标点
         * @param maxTime 最大时间约束
         * @param maxCost 最大成本约束
         * @return 满足约束的最小成本，不存在返回-1
         */
        public static int multiConstraintShortestPath(int n, int[][] edges, int src, int dst, 
                                                     int maxTime, int maxCost) {
            // 构建邻接表
            List<List<int[]>> graph = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }
            
            for (int[] edge : edges) {
                int u = edge[0], v = edge[1], time = edge[2], cost = edge[3];
                graph.get(u).add(new int[]{v, time, cost});
                graph.get(v).add(new int[]{u, time, cost}); // 无向图
            }
            
            // 优先队列：存储(当前成本, 已用时间, 当前节点)
            PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
            heap.offer(new int[]{0, 0, src});
            
            // 记录每个节点在特定时间下的最小成本
            Map<Integer, Map<Integer, Integer>> nodeState = new HashMap<>();
            for (int i = 0; i < n; i++) {
                nodeState.put(i, new HashMap<>());
            }
            nodeState.get(src).put(0, 0);
            
            while (!heap.isEmpty()) {
                int[] state = heap.poll();
                int cost = state[0];
                int time = state[1];
                int city = state[2];
                
                if (city == dst) {
                    return cost;
                }
                
                for (int[] edge : graph.get(city)) {
                    int nextCity = edge[0];
                    int edgeTime = edge[1];
                    int edgeCost = edge[2];
                    
                    int newTime = time + edgeTime;
                    int newCost = cost + edgeCost;
                    
                    // 检查约束条件
                    if (newTime > maxTime || newCost > maxCost) {
                        continue;
                    }
                    
                    // 剪枝：如果存在更优的状态，跳过
                    Map<Integer, Integer> stateMap = nodeState.get(nextCity);
                    boolean shouldAdd = true;
                    
                    for (Map.Entry<Integer, Integer> entry : stateMap.entrySet()) {
                        int existingTime = entry.getKey();
                        int existingCost = entry.getValue();
                        
                        if (newTime >= existingTime && newCost >= existingCost) {
                            shouldAdd = false;
                            break;
                        }
                    }
                    
                    if (shouldAdd) {
                        // 移除被新状态支配的旧状态
                        stateMap.entrySet().removeIf(entry -> 
                            entry.getKey() >= newTime && entry.getValue() >= newCost);
                        
                        stateMap.put(newTime, newCost);
                        heap.offer(new int[]{newCost, newTime, nextCity});
                    }
                }
            }
            
            return -1;
        }
    }
    
    /**
     * 测试用例
     */
    public static void main(String[] args) {
        // 测试用例1：LeetCode 787示例
        System.out.println("=== 测试用例1：K站中转内最便宜的航班 ===");
        int n1 = 4;
        int[][] flights1 = {
            {0, 1, 100}, {1, 2, 100}, {2, 0, 100}, {1, 3, 600}, {2, 3, 200}
        };
        int src1 = 0, dst1 = 3, k1 = 1;
        
        System.out.println("方法1结果（动态规划+Dijkstra）: " + 
            findCheapestPrice1(n1, flights1, src1, dst1, k1));
        System.out.println("方法2结果（Bellman-Ford变种）: " + 
            findCheapestPrice2(n1, flights1, src1, dst1, k1));
        System.out.println("方法3结果（BFS+剪枝）: " + 
            findCheapestPrice3(n1, flights1, src1, dst1, k1));
        
        // 测试用例2：多约束最短路径
        System.out.println("\n=== 测试用例2：多约束最短路径 ===");
        int n2 = 4;
        int[][] edges2 = {
            {0, 1, 2, 10}, {0, 2, 5, 20}, {1, 3, 3, 15}, {2, 3, 1, 30}
        };
        int src2 = 0, dst2 = 3, maxTime = 6, maxCost = 40;
        
        int result = MultiConstraintShortestPath.multiConstraintShortestPath(
            n2, edges2, src2, dst2, maxTime, maxCost);
        System.out.println("多约束最短路径结果: " + result);
        
        // 算法分析
        System.out.println("\n=== 算法分析 ===");
        System.out.println("方法1：动态规划+Dijkstra");
        System.out.println("  - 优点：效率高，O(K * E * log(V))");
        System.out.println("  - 缺点：空间复杂度较高");
        System.out.println("  - 适用：中等规模图");
        
        System.out.println("方法2：Bellman-Ford变种");
        System.out.println("  - 优点：实现简单，空间效率高");
        System.out.println("  - 缺点：时间复杂度O(K * E)");
        System.out.println("  - 适用：小规模图或稀疏图");
        
        System.out.println("方法3：BFS+剪枝");
        System.out.println("  - 优点：实现简单，适合约束严格的情况");
        System.out.println("  - 缺点：最坏情况指数级复杂度");
        System.out.println("  - 适用：约束非常严格的情况");
    }
}