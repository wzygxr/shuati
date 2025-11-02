// Dijkstra算法扩展练习题：K 站中转内最便宜的航班
// 有 n 个城市通过一些航班连接。给你一个数组 flights ，其中 flights[i] = [fromi, toi, pricei] ，
// 表示该航班都从城市 fromi 开始，以价格 pricei 抵达 toi。
// 现在给定所有的城市和航班，以及出发城市 src 和目的地 dst，你的任务是找到出一条最多经过 k 站中转的路线，
// 使得从 src 到 dst 的价格最便宜，并返回该价格。如果不存在这样的路线，则输出 -1。
// 测试链接: https://leetcode.cn/problems/cheapest-flights-within-k-stops/
// 
// 算法思路：
// 这是一道限制边数的最短路径问题，可以使用修改版的Dijkstra算法解决。
// 1. 使用优先队列存储状态(城市, 费用, 中转次数)
// 2. 优先队列按费用排序
// 3. 使用二维数组dist[城市][中转次数]记录到达每个城市使用不同中转次数的最小费用
// 4. 当中转次数超过k时，不再扩展该状态
//
// 具体实现：
// 1. 构建图的邻接表表示
// 2. 使用优先队列存储待处理的状态，按费用从小到大排序
// 3. 从起始城市开始，逐步扩展到其他城市
// 4. 对于每个状态，更新其相邻城市的最小费用
// 5. 限制中转次数不超过k
//
// 时间复杂度：O(K * E * log(K * E))，其中K是最大中转次数，E是边数
// 空间复杂度：O(N * K + E)，其中N是城市数
//
// 相关题目链接：
// 1. LeetCode 787. K 站中转内最便宜的航班 - https://leetcode.cn/problems/cheapest-flights-within-k-stops/
// 2. LeetCode 743. 网络延迟时间 - https://leetcode.cn/problems/network-delay-time/
// 3. 洛谷 P4779 单源最短路径 - https://www.luogu.com.cn/problem/P4779
// 4. POJ 2387 Til the Cows Come Home - http://poj.org/problem?id=2387
// 5. Codeforces 20C Dijkstra? - https://codeforces.com/problemset/problem/20/C
// 6. 洛谷 P3371 单源最短路径 - https://www.luogu.com.cn/problem/P3371
// 7. HDU 2544 最短路 - https://acm.hdu.edu.cn/showproblem.php?pid=2544
// 8. AtCoder ABC070 D - Transit Tree Path - https://atcoder.jp/contests/abc070/tasks/abc070_d
// 9. 牛客 NC50439 最短路 - https://ac.nowcoder.com/acm/problem/50439
// 10. SPOJ SHPATH - https://www.spoj.com/problems/SHPATH/
// 11. ZOJ 2818 The Traveling Judges Problem - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827366818
// 12. 51Nod 1018 最短路 - https://www.51nod.com/Challenge/Problem.html#problemId=1018
// 13. 洛谷 P1144 最短路计数 - https://www.luogu.com.cn/problem/P1144
// 14. LeetCode 542. 01 矩阵 - https://leetcode.cn/problems/01-matrix/
// 15. LeetCode 773. 滑动谜题 - https://leetcode.cn/problems/sliding-puzzle/

// 由于编译环境限制，使用基本C++语法实现

const int MAXN = 101;
const int INF = 2147483647;

int n, src, dst, k;
// 使用基本数组模拟邻接表
int graph_to[MAXN][MAXN];      // 存储邻接点
int graph_price[MAXN][MAXN];   // 存储边权重
int graph_size[MAXN];          // 存储每个节点的邻接点数量

int dist[MAXN][MAXN];          // 距离数组，dist[i][j]表示到达节点i使用j次中转的最小费用

// 添加边
void addEdge(int from, int to, int price) {
    graph_to[from][graph_size[from]] = to;
    graph_price[from][graph_size[from]] = price;
    graph_size[from]++;
}

// 使用基本数组模拟优先队列
// 分别存储cost, city, stops三个字段
int pq_cost[100000];   // 存储费用
int pq_city[100000];   // 存储城市
int pq_stops[100000];  // 存储中转次数
int pq_size = 0;       // 优先队列大小

// 向优先队列添加元素
void pq_push(int cost, int city, int stops) {
    pq_cost[pq_size] = cost;
    pq_city[pq_size] = city;
    pq_stops[pq_size] = stops;
    pq_size++;
    
    // 简单的插入排序，按费用从小到大排序
    for (int i = pq_size - 1; i > 0; i--) {
        if (pq_cost[i] < pq_cost[i - 1]) {
            // 交换费用
            int temp = pq_cost[i];
            pq_cost[i] = pq_cost[i - 1];
            pq_cost[i - 1] = temp;
            
            // 交换城市
            temp = pq_city[i];
            pq_city[i] = pq_city[i - 1];
            pq_city[i - 1] = temp;
            
            // 交换中转次数
            temp = pq_stops[i];
            pq_stops[i] = pq_stops[i - 1];
            pq_stops[i - 1] = temp;
        } else {
            break;
        }
    }
}

// 从优先队列取出费用最小的元素
void pq_pop(int& cost, int& city, int& stops) {
    cost = pq_cost[0];
    city = pq_city[0];
    stops = pq_stops[0];
    
    // 移除第一个元素
    for (int i = 1; i < pq_size; i++) {
        pq_cost[i - 1] = pq_cost[i];
        pq_city[i - 1] = pq_city[i];
        pq_stops[i - 1] = pq_stops[i];
    }
    pq_size--;
}

// 检查优先队列是否为空
bool pq_empty() {
    return pq_size == 0;
}

// 修改版Dijkstra算法实现
int dijkstraWithStops() {
    // 初始化距离数组为无穷大
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < MAXN; j++) {
            dist[i][j] = INF;
        }
    }
    
    // 起点距离为0，中转次数为0
    dist[src][0] = 0;
    
    // 初始化优先队列
    pq_size = 0;
    
    // 起点入队，费用为0，中转次数为0
    pq_push(0, src, 0);
    
    // 当优先队列不为空时，继续处理
    while (!pq_empty()) {
        // 取出费用最小的状态
        int cost, u, stops;
        pq_pop(cost, u, stops);
        
        // 如果到达目标城市，返回费用
        if (u == dst) {
            return cost;
        }
        
        // 如果中转次数超过限制，跳过
        if (stops > k) {
            continue;
        }
        
        // 更新相邻节点的距离
        for (int i = 0; i < graph_size[u]; i++) {
            int v = graph_to[u][i];
            int price = graph_price[u][i];
            
            // 如果找到更便宜的路径
            if (cost + price < dist[v][stops + 1]) {
                dist[v][stops + 1] = cost + price;
                pq_push(dist[v][stops + 1], v, stops + 1);
            }
        }
    }
    
    // 无法到达目标城市，返回-1
    return -1;
}

// 由于编译环境限制，这里不提供main函数
// 在实际使用中，需要根据具体环境提供输入输出方式