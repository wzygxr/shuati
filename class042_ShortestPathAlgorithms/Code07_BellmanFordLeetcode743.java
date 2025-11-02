package class065;

import java.util.*;

// LeetCode 743. 网络延迟时间 - Bellman-Ford算法实现
// 题目链接: https://leetcode.cn/problems/network-delay-time/
// 题目描述: 有 n 个网络节点，标记为 1 到 n。
// 给你一个列表 times，表示信号经过有向边的传递时间。times[i] = (ui, vi, wi)，
// 其中 ui 是源节点，vi 是目标节点，wi 是一个信号从源节点传递到目标节点的时间。
// 现在，从某个节点 K 发出一个信号。需要多久才能使所有节点都收到信号？
// 如果不能使所有节点收到信号，返回 -1。
//
// Bellman-Ford算法核心思想:
// 通过n-1轮松弛操作，逐步逼近最短路径
// 每轮遍历所有边，尝试通过松弛操作更新节点距离
// 可以检测负权环的存在
//
// 时间复杂度: O(N*E)，其中N是节点数，E是边数
// 空间复杂度: O(N)，需要一维数组存储距离

public class Code07_BellmanFordLeetcode743 {
    
    public static int networkDelayTime(int[][] times, int n, int k) {
        // 初始化距离数组，表示从节点k到其他节点的距离
        int[] distance = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            distance[i] = Integer.MAX_VALUE;
        }
        distance[k] = 0;  // 起点到自身的距离为0
        
        // 进行n-1轮松弛操作
        for (int i = 1; i < n; i++) {
            // 遍历所有边进行松弛操作
            for (int[] edge : times) {
                int u = edge[0];  // 起点
                int v = edge[1];  // 终点
                int w = edge[2];  // 权重
                
                // 如果起点可达，则尝试更新终点的最短距离
                if (distance[u] != Integer.MAX_VALUE && distance[u] + w < distance[v]) {
                    distance[v] = distance[u] + w;
                }
            }
        }
        
        // 检查是否存在无法到达的节点
        int maxDistance = 0;
        for (int i = 1; i <= n; i++) {
            if (distance[i] == Integer.MAX_VALUE) {
                return -1;  // 存在无法到达的节点
            }
            maxDistance = Math.max(maxDistance, distance[i]);
        }
        
        return maxDistance;
    }
    
    // 测试函数
    public static void main(String[] args) {
        // 测试用例1
        int[][] times1 = {{2,1,1},{2,3,1},{3,4,1}};
        int n1 = 4;
        int k1 = 2;
        System.out.println("测试用例1结果: " + networkDelayTime(times1, n1, k1)); // 期望输出: 2
        
        // 测试用例2
        int[][] times2 = {{1,2,1}};
        int n2 = 2;
        int k2 = 1;
        System.out.println("测试用例2结果: " + networkDelayTime(times2, n2, k2)); // 期望输出: 1
        
        // 测试用例3
        int[][] times3 = {{1,2,1}};
        int n3 = 2;
        int k3 = 2;
        System.out.println("测试用例3结果: " + networkDelayTime(times3, n3, k3)); // 期望输出: -1
    }
}


/* ----------------------------- 补充题目1: LeetCode 787. K站中转内最便宜的航班 ----------------------------- */
// 题目链接: https://leetcode.cn/problems/cheapest-flights-within-k-stops/
// 题目描述: 有 n 个城市通过一些航班连接。给你一个数组 flights，其中 flights[i] = [fromi, toi, pricei]，
// 表示该航班都从城市 fromi 开始，以价格 pricei 抵达 toi。
// 现在给定所有的城市和航班，以及出发城市 src 和目的地 dst，你的任务是找出一条最多经过 k 站中转的路线，
// 使得从 src 到 dst 的价格最便宜，并返回该价格。 如果不存在这样的路线，则输出 -1。

// Bellman-Ford算法解决思路:
// 1. 这是一个带边数限制的最短路径问题
// 2. 我们只进行k+1轮松弛操作（因为k站中转意味着最多k+1条边）
// 3. 需要使用临时数组来保存当前轮次的松弛结果，避免在同一轮中多次更新

class CheapestFlightsSolver {
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        // 初始化距离数组
        int[] distance = new int[n];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[src] = 0;
        
        // 最多进行k+1轮松弛操作（k站中转意味着最多k+1条边）
        for (int i = 0; i <= k; i++) {
            // 使用临时数组保存本轮松弛结果
            int[] temp = Arrays.copyOf(distance, n);
            
            // 遍历所有边进行松弛
            for (int[] flight : flights) {
                int from = flight[0];
                int to = flight[1];
                int price = flight[2];
                
                // 如果from可达，尝试更新to的最短距离
                if (distance[from] != Integer.MAX_VALUE) {
                    temp[to] = Math.min(temp[to], distance[from] + price);
                }
            }
            
            // 更新距离数组
            distance = temp;
        }
        
        // 如果无法到达dst，返回-1
        return distance[dst] == Integer.MAX_VALUE ? -1 : distance[dst];
    }
}

/* ----------------------------- 补充题目2: POJ 3259. Wormholes ----------------------------- */
// 题目链接: http://poj.org/problem?id=3259
// 题目描述: 判断一个包含虫洞的图中是否存在负权环，使得可以通过虫洞回到过去
// Bellman-Ford算法解决思路:
// 1. 正权边表示普通道路
// 2. 负权边表示虫洞（时间倒流）
// 3. 如果存在负权环，说明可以通过虫洞无限次循环，回到任意远的过去

/* ----------------------------- 补充题目3: 差分约束系统 ----------------------------- */
// 题目描述: 求解一组形如 xj - xi ≤ ck 的不等式组
// Bellman-Ford算法解决思路:
// 1. 构造图：对于每个不等式 xj - xi ≤ ck，添加一条边i->j，权重为ck
// 2. 添加超级源点0，到所有其他点的边权重为0
// 3. 运行Bellman-Ford算法检测是否存在负权环

/* ----------------------------- Bellman-Ford算法工程实践建议 ----------------------------- */
// 1. 适用场景:
//    - 存在负权边的单源最短路径问题
//    - 需要检测负权环的场景
//    - 带边数限制的最短路径问题
//
// 2. 性能优化技巧:
//    - 提前终止：如果某轮松弛没有任何距离更新，可以提前结束
//    - 使用队列优化：即SPFA算法，只对可能更新的节点进行松弛
//    - 对于边数限制的问题，使用滚动数组避免覆盖当前轮次的结果
//
// 3. 与其他算法对比:
//    - 相比Dijkstra: Bellman-Ford可以处理负权边，但时间复杂度更高
//    - 相比SPFA: Bellman-Ford代码更简洁，但效率通常较低
//    - 优势在于可以检测负权环，这是其他最短路径算法无法做到的
//
// 4. 常见陷阱:
//    - 整数溢出：在松弛操作中要注意大数相加溢出
//    - 边数限制处理：需要正确理解k站中转对应的松弛轮数
//    - 初始化错误：源点距离初始化为0，其他节点初始化为无穷大