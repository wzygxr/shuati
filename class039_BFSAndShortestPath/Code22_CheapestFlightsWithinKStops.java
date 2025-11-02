package class062;

import java.util.*;

// K站中转内最便宜的航班
// 有 n 个城市通过一些航班连接。给你一个数组 flights ，其中 flights[i] = [fromi, toi, pricei] 
// 表示该航班都从城市 fromi 开始，以价格 pricei 抵达 toi。
// 现在给定所有的城市和航班，以及出发城市 src 和目的地 dst，你的任务是找到出一条最多经过 k 站中转的路线，
// 使得从 src 到 dst 的 价格最便宜 ，并返回该价格。 如果不存在这样的路线，则返回 -1。
// 测试链接 : https://leetcode.cn/problems/cheapest-flights-within-k-stops/
// 
// 算法思路：
// 使用带层数限制的BFS（实际上是Dijkstra算法的变种）。由于有中转站数量限制，需要在状态中记录当前中转站数量。
// 使用优先队列按照价格排序，但需要注意中转站数量的限制。
// 
// 时间复杂度：O(E * K)，其中E是边的数量，K是最大中转站数
// 空间复杂度：O(V * K)，其中V是顶点数，K是最大中转站数
// 
// 工程化考量：
// 1. 状态表示：(当前城市, 已用中转站数, 累计价格)
// 2. 剪枝优化：对于同一城市，如果已用中转站数更多且价格更高，可以剪枝
// 3. 图表示：使用邻接表存储图结构
// 4. 边界情况：起点就是终点，中转站数为0
public class Code22_CheapestFlightsWithinKStops {

    public static int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        // 构建图的邻接表表示
        List<int[]>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] flight : flights) {
            int from = flight[0];
            int to = flight[1];
            int price = flight[2];
            graph[from].add(new int[]{to, price});
        }
        
        // 边界情况：起点就是终点
        if (src == dst) {
            return 0;
        }
        
        // 使用优先队列，按价格排序（小顶堆）
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[2] - b[2]);
        // 状态：(当前城市, 已用中转站数, 累计价格)
        pq.offer(new int[]{src, -1, 0}); // 起点不算中转站，所以从-1开始
        
        // 记录到达每个城市的最小价格（考虑中转站数）
        // dist[i][j] 表示到达城市i用了j次中转站的最小价格
        int[][] dist = new int[n][k + 2];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        }
        dist[src][0] = 0;
        
        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int city = current[0];
            int stops = current[1];
            int cost = current[2];
            
            // 如果到达目的地，返回价格（因为使用优先队列，第一次到达就是最小价格）
            if (city == dst) {
                return cost;
            }
            
            // 如果中转站数已用完，跳过
            if (stops == k) {
                continue;
            }
            
            // 遍历所有邻居
            for (int[] neighbor : graph[city]) {
                int nextCity = neighbor[0];
                int price = neighbor[1];
                int nextStops = stops + 1;
                int nextCost = cost + price;
                
                // 剪枝：如果价格更高且中转站数更多，跳过
                if (nextStops <= k + 1 && nextCost < dist[nextCity][nextStops + 1]) {
                    dist[nextCity][nextStops + 1] = nextCost;
                    pq.offer(new int[]{nextCity, nextStops, nextCost});
                }
            }
        }
        
        return -1;
    }
    
    // 优化版本：使用BFS + 剪枝，避免使用优先队列的开销
    public static int findCheapestPriceBFS(int n, int[][] flights, int src, int dst, int k) {
        // 构建图的邻接表表示
        List<int[]>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] flight : flights) {
            int from = flight[0];
            int to = flight[1];
            int price = flight[2];
            graph[from].add(new int[]{to, price});
        }
        
        // 边界情况：起点就是终点
        if (src == dst) {
            return 0;
        }
        
        // 使用BFS，按层搜索（每层代表一次中转）
        Queue<int[]> queue = new LinkedList<>();
        // 记录到达每个城市的最小价格
        int[] minCost = new int[n];
        Arrays.fill(minCost, Integer.MAX_VALUE);
        minCost[src] = 0;
        
        queue.offer(new int[]{src, 0}); // (当前城市, 累计价格)
        int stops = 0;
        
        while (!queue.isEmpty() && stops <= k) {
            int size = queue.size();
            
            // 临时数组，记录当前层的最小价格
            int[] tempCost = minCost.clone();
            
            // 处理当前层的所有城市
            for (int i = 0; i < size; i++) {
                int[] current = queue.poll();
                int city = current[0];
                int cost = current[1];
                
                // 遍历所有邻居
                for (int[] neighbor : graph[city]) {
                    int nextCity = neighbor[0];
                    int price = neighbor[1];
                    int nextCost = cost + price;
                    
                    // 如果找到更小的价格
                    if (nextCost < tempCost[nextCity]) {
                        tempCost[nextCity] = nextCost;
                        queue.offer(new int[]{nextCity, nextCost});
                    }
                }
            }
            
            // 更新最小价格数组
            minCost = tempCost;
            stops++;
        }
        
        return minCost[dst] == Integer.MAX_VALUE ? -1 : minCost[dst];
    }
    
    // 单元测试
    public static void main(String[] args) {
        // 测试用例1：标准情况
        int n1 = 3;
        int[][] flights1 = {{0,1,100},{1,2,100},{0,2,500}};
        int src1 = 0, dst1 = 2, k1 = 1;
        System.out.println("测试用例1 - 最便宜价格: " + findCheapestPrice(n1, flights1, src1, dst1, k1)); // 期望输出: 200
        
        // 测试用例2：无法到达
        int n2 = 3;
        int[][] flights2 = {{0,1,100},{1,2,100},{0,2,500}};
        int src2 = 2, dst2 = 0, k2 = 1;
        System.out.println("测试用例2 - 最便宜价格: " + findCheapestPrice(n2, flights2, src2, dst2, k2)); // 期望输出: -1
        
        // 测试用例3：中转站数为0
        int n3 = 3;
        int[][] flights3 = {{0,1,100},{1,2,100},{0,2,500}};
        int src3 = 0, dst3 = 2, k3 = 0;
        System.out.println("测试用例3 - 最便宜价格: " + findCheapestPrice(n3, flights3, src3, dst3, k3)); // 期望输出: 500
        
        // 测试用例4：复杂情况
        int n4 = 4;
        int[][] flights4 = {{0,1,1},{0,2,5},{1,2,1},{2,3,1}};
        int src4 = 0, dst4 = 3, k4 = 1;
        System.out.println("测试用例4 - 最便宜价格: " + findCheapestPrice(n4, flights4, src4, dst4, k4)); // 期望输出: 6
    }
}