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

#define MAXN 105
#define INF 0x3f3f3f3f

// 简单的优先队列实现（最小堆）
int heap[MAXN * MAXN][3];  // 存储 [累计价格, 当前城市, 已用中转站数]
int heap_size;

// 向最小堆中添加元素
void heap_push(int cost, int city, int stops) {
    heap[heap_size][0] = cost;
    heap[heap_size][1] = city;
    heap[heap_size][2] = stops;
    heap_size++;
    
    // 向上调整
    int i = heap_size - 1;
    while (i > 0) {
        int parent = (i - 1) / 2;
        if (heap[parent][0] <= heap[i][0]) break;
        // 交换
        int temp[3];
        temp[0] = heap[parent][0];
        temp[1] = heap[parent][1];
        temp[2] = heap[parent][2];
        heap[parent][0] = heap[i][0];
        heap[parent][1] = heap[i][1];
        heap[parent][2] = heap[i][2];
        heap[i][0] = temp[0];
        heap[i][1] = temp[1];
        heap[i][2] = temp[2];
        i = parent;
    }
}

// 从最小堆中取出最小元素
void heap_pop(int* result) {
    result[0] = heap[0][0];
    result[1] = heap[0][1];
    result[2] = heap[0][2];
    
    heap[0][0] = heap[heap_size-1][0];
    heap[0][1] = heap[heap_size-1][1];
    heap[0][2] = heap[heap_size-1][2];
    heap_size--;
    
    // 向下调整
    int i = 0;
    while (true) {
        int smallest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        
        if (left < heap_size && heap[left][0] < heap[smallest][0])
            smallest = left;
        if (right < heap_size && heap[right][0] < heap[smallest][0])
            smallest = right;
        
        if (smallest == i) break;
        
        // 交换
        int temp[3];
        temp[0] = heap[smallest][0];
        temp[1] = heap[smallest][1];
        temp[2] = heap[smallest][2];
        heap[smallest][0] = heap[i][0];
        heap[smallest][1] = heap[i][1];
        heap[smallest][2] = heap[i][2];
        heap[i][0] = temp[0];
        heap[i][1] = temp[1];
        heap[i][2] = temp[2];
        i = smallest;
    }
}

// 图的邻接表表示
int graph[MAXN][MAXN][2];  // graph[i][j][0] = to_city, graph[i][j][1] = price
int graph_size[MAXN];      // 每个城市的邻接点数量

// 记录到达每个城市的最小价格（考虑中转站数）
int dist[MAXN][MAXN];      // dist[i][j] 表示到达城市i用了j次中转站的最小价格

// 使用优先队列的BFS解法
int findCheapestPrice(int n, int** flights, int flightsSize, int* flightsColSize, int src, int dst, int k) {
    // 初始化图
    int i, j;
    for (i = 0; i < n; i++) {
        graph_size[i] = 0;
        for (j = 0; j <= k + 1; j++) {
            dist[i][j] = INF;
        }
    }
    
    // 构建图的邻接表表示
    for (i = 0; i < flightsSize; i++) {
        int from = flights[i][0];
        int to = flights[i][1];
        int price = flights[i][2];
        graph[from][graph_size[from]][0] = to;
        graph[from][graph_size[from]][1] = price;
        graph_size[from]++;
    }
    
    // 边界情况：起点就是终点
    if (src == dst) {
        return 0;
    }
    
    // 初始化优先队列
    heap_size = 0;
    heap_push(0, src, -1);  // 起点不算中转站，所以从-1开始
    dist[src][0] = 0;
    
    while (heap_size > 0) {
        int current[3];
        heap_pop(current);
        int cost = current[0];
        int city = current[1];
        int stops = current[2];
        
        // 如果到达目的地，返回价格（因为使用优先队列，第一次到达就是最小价格）
        if (city == dst) {
            return cost;
        }
        
        // 如果中转站数已用完，跳过
        if (stops == k) {
            continue;
        }
        
        // 遍历所有邻居
        for (i = 0; i < graph_size[city]; i++) {
            int neighbor = graph[city][i][0];
            int price = graph[city][i][1];
            int next_stops = stops + 1;
            int next_cost = cost + price;
            
            // 剪枝：如果价格更高且中转站数更多，跳过
            if (next_stops <= k + 1 && next_cost < dist[neighbor][next_stops + 1]) {
                dist[neighbor][next_stops + 1] = next_cost;
                heap_push(next_cost, neighbor, next_stops);
            }
        }
    }
    
    return -1;
}