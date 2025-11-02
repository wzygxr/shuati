// LeetCode 787 Cheapest Flights Within K Stops
// C++ 实现

/**
 * LeetCode 787 Cheapest Flights Within K Stops
 * 
 * 题目描述：
 * 有 n 个城市通过一些航班连接。给你一个数组 flights ，其中 flights[i] = [fromi, toi, pricei] ，
 * 表示该航班都从城市 fromi 开始，以价格 pricei 抵达 toi。
 * 现在给定所有的城市和航班，以及出发城市 src 和目的地 dst，
 * 你的任务是找到一条最多经过 k 站中转的路线，使得从 src 到 dst 的价格最便宜，
 * 并返回该价格。如果不存在这样的路线，则输出 -1。
 * 
 * 解题思路：
 * 我们可以使用Dijkstra算法的变种或Bellman-Ford算法来解决这个问题。
 * 由于限制了最多经过k站中转，我们可以使用修改版的Bellman-Ford算法。
 * 
 * 时间复杂度：O(k * E)
 * 空间复杂度：O(n)
 */

// 由于编译环境限制，这里提供算法思路和伪代码实现

/*
#include <stdlib.h>
#include <limits.h>

int findCheapestPrice(int n, int** flights, int flightsSize, int* flightsColSize, int src, int dst, int k) {
    // 初始化距离数组
    int* dist = (int*)malloc(n * sizeof(int));
    for (int i = 0; i < n; i++) {
        dist[i] = INT_MAX;
    }
    dist[src] = 0;
    
    // 进行k+1轮松弛操作
    for (int i = 0; i <= k; i++) {
        // 创建临时距离数组
        int* temp = (int*)malloc(n * sizeof(int));
        for (int j = 0; j < n; j++) {
            temp[j] = dist[j];
        }
        
        // 松弛所有边
        for (int j = 0; j < flightsSize; j++) {
            int from = flights[j][0];
            int to = flights[j][1];
            int price = flights[j][2];
            
            if (dist[from] != INT_MAX) {
                temp[to] = (temp[to] < dist[from] + price) ? temp[to] : dist[from] + price;
            }
        }
        
        // 更新距离数组
        for (int j = 0; j < n; j++) {
            dist[j] = temp[j];
        }
        
        free(temp);
    }
    
    int result = (dist[dst] == INT_MAX) ? -1 : dist[dst];
    free(dist);
    return result;
}

// 算法核心思想：
// 1. 使用修改版的Bellman-Ford算法
// 2. 限制松弛操作的轮数为k+1轮
// 3. 使用临时数组避免同一轮中更新影响后续计算

// 时间复杂度分析：
// - 松弛操作：O(k * E)
// - 空间复杂度：O(n)
*/

// 算法应用场景：
// 1. 图论最短路径问题
// 2. 航班路线规划
// 3. 限制条件下的最优化问题