package class064;

import java.util.*;

/**
 * K站中转内最便宜的航班
 * 
 * 题目链接: https://leetcode.cn/problems/cheapest-flights-within-k-stops/
 * 
 * 题目描述：
 * 有 n 个城市通过一些航班连接。给你一个数组 flights ，其中 flights[i] = [fromi, toi, pricei] ，
 * 表示该航班都从城市 fromi 开始，以价格 pricei 抵达 toi。
 * 现在给定所有的城市和航班，以及出发城市 src 和目的地 dst，
 * 你的任务是找到出一条最多经过 k 站中转的路线，使得从 src 到 dst 的 价格最便宜 ，并返回该价格。
 * 如果不存在这样的路线，则输出 -1。
 * 
 * 解题思路：
 * 这是一个变形的最短路径问题，可以使用Dijkstra算法解决。
 * 与传统最短路径不同的是，这里的状态不仅包括城市位置，还包括经过的航班次数。
 * 我们将状态定义为(城市, 经过的航班次数)，图中的节点是这些状态对。
 * 边表示航班，权重为票价。
 * 使用Dijkstra算法找到从起点状态(起点城市, 0次航班)到终点状态(终点城市, 最多k+1次航班)的最少花费。
 * 
 * 算法应用场景：
 * - 航班预订系统
 * - 交通路线规划
 * - 资源受限的路径优化问题
 * 
 * 时间复杂度分析：
 * O(k * E * log(k * E)) 其中E是航班数
 * 
 * 空间复杂度分析：
 * O(k * E) 存储距离数组和访问标记数组
 */
public class Code07_CheapestFlightsWithinKStops {

    /**
     * 使用Dijkstra算法求解K站中转内最便宜的航班
     * 
     * 算法核心思想：
     * 1. 将问题转化为图论中的最短路径问题
     * 2. 状态定义为(城市, 经过的航班次数)，图中的节点是这些状态对
     * 3. 边表示航班，权重为票价
     * 4. 使用Dijkstra算法找到从起点状态到终点状态的最少花费
     * 
     * 算法步骤：
     * 1. 初始化距离数组，起点状态距离为0，其他状态为无穷大
     * 2. 使用优先队列维护待处理状态，按花费从小到大排序
     * 3. 不断取出花费最小的状态，通过乘坐航班扩展新状态
     * 4. 当处理到终点城市时，直接返回结果（剪枝优化）
     * 5. 当中转次数达到上限时，停止扩展
     * 
     * 时间复杂度: O(k * E * log(k * E)) 其中E是航班数
     * 空间复杂度: O(k * E)
     * 
     * @param n 城市数量
     * @param flights 航班信息，flights[i] = [起点城市, 终点城市, 票价]
     * @param src 起点城市
     * @param dst 终点城市
     * @param k 最多中转次数
     * @return 最便宜的价格，如果不存在这样的路线则返回-1
     */
    public static int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        // 构建邻接表表示的图
        // graph[i] 存储从城市i出发的所有航班信息
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 添加边到图中
        // 对于每个航班，将其添加到起点城市的邻居列表中
        for (int[] flight : flights) {
            // flight[0] 起点城市
            // flight[1] 终点城市
            // flight[2] 票价
            graph.get(flight[0]).add(new int[]{flight[1], flight[2]});
        }
        
        // distance[i][j]表示到达城市i且经过j次航班的最少价格
        // 初始化为最大值，表示尚未访问
        int[][] distance = new int[n][k + 2];
        for (int i = 0; i < n; i++) {
            Arrays.fill(distance[i], Integer.MAX_VALUE);
        }
        
        // visited[i][j]表示状态(城市i, 经过j次航班)是否已经确定了最短路径
        // 用于避免重复处理已经确定最短路径的状态
        boolean[][] visited = new boolean[n][k + 2];
        
        // 优先队列，按价格从小到大排序
        // 数组含义：[0] 当前城市, [1] 到达当前城市的花费, [2] 经过的航班次数
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        
        // 初始状态：在起点城市且未经过任何航班，花费为0
        distance[src][0] = 0;
        // 将起点状态加入优先队列
        heap.add(new int[]{src, 0, 0});
        
        // Dijkstra算法主循环
        while (!heap.isEmpty()) {
            // 取出花费最小的状态
            int[] record = heap.poll();
            int city = record[0];    // 当前城市
            int cost = record[1];    // 当前花费
            int stops = record[2];   // 经过的航班次数
            
            // 如果已经处理过，跳过
            // 这是为了避免同一状态多次处理导致的重复计算
            if (visited[city][stops]) {
                continue;
            }
            
            // 标记为已处理，表示已确定从起点到该状态的最少花费
            visited[city][stops] = true;
            
            // 如果到达终点，直接返回结果
            // 常见剪枝优化：发现终点直接返回，不用等都结束
            // 这是因为Dijkstra算法的特性保证了第一次到达终点时就是最优解
            if (city == dst) {
                return cost;
            }
            
            // 如果中转次数已达到上限，不能再继续转机
            // k是最多中转次数，stops是已经经过的航班次数
            // 当stops == k+1时，表示已经经过了k+1次航班，不能再转机了
            if (stops == k + 1) {
                continue;
            }
            
            // 遍历所有出边（从当前城市出发的所有航班）
            for (int[] edge : graph.get(city)) {
                int nextCity = edge[0];     // 下一个城市
                int price = edge[1];        // 航班票价
                int nextCost = cost + price; // 到达下一个城市的总花费
                int nextStops = stops + 1;   // 经过的航班次数加1
                
                // 如果新的花费更小且未超过中转次数限制，则更新
                // 松弛操作：如果 nextCost < distance[nextCity][nextStops]，则更新distance[nextCity][nextStops]
                if (nextCost < distance[nextCity][nextStops]) {
                    distance[nextCity][nextStops] = nextCost;
                    // 将乘坐航班后的新状态加入优先队列
                    heap.add(new int[]{nextCity, nextCost, nextStops});
                }
            }
        }
        
        // 不存在满足条件的路线
        return -1;
    }

    // 测试用例
    public static void main(String[] args) {
        // 示例1
        // 输入: n = 4, flights = [[0,1,100],[1,2,100],[2,0,100],[1,3,600],[2,3,200]], src = 0, dst = 3, k = 1
        // 输出: 700
        // 解释: 最优路径是 0 -> 1 -> 3，花费100+600=700，中转1次
        int n1 = 4;
        int[][] flights1 = {{0,1,100},{1,2,100},{2,0,100},{1,3,600},{2,3,200}};
        int src1 = 0, dst1 = 3, k1 = 1;
        System.out.println(findCheapestPrice(n1, flights1, src1, dst1, k1)); // 输出: 700
        
        // 示例2
        // 输入: n = 3, flights = [[0,1,100],[1,2,100],[0,2,500]], src = 0, dst = 2, k = 1
        // 输出: 200
        // 解释: 最优路径是 0 -> 1 -> 2，花费100+100=200，中转1次
        int n2 = 3;
        int[][] flights2 = {{0,1,100},{1,2,100},{0,2,500}};
        int src2 = 0, dst2 = 2, k2 = 1;
        System.out.println(findCheapestPrice(n2, flights2, src2, dst2, k2)); // 输出: 200
        
        // 示例3
        // 输入: n = 3, flights = [[0,1,100],[1,2,100],[0,2,500]], src = 0, dst = 2, k = 0
        // 输出: 500
        // 解释: 由于k=0，不能中转，只能直飞，花费500
        int n3 = 3;
        int[][] flights3 = {{0,1,100},{1,2,100},{0,2,500}};
        int src3 = 0, dst3 = 2, k3 = 0;
        System.out.println(findCheapestPrice(n3, flights3, src3, dst3, k3)); // 输出: 500
    }
}