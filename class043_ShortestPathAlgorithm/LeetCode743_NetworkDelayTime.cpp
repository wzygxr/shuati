/**
 * LeetCode 743 - 网络延迟时间
 * 题目描述：
 * 有 n 个网络节点，标记为 1 到 n。
 * 给你一个列表 times，表示信号经过 有向 边的传递时间。times[i] = (u_i, v_i, w_i)，
 * 其中 u_i 是源节点，v_i 是目标节点，w_i 是一个信号从源节点传递到目标节点的时间。
 * 现在，从某个节点 k 发出一个信号。需要多久才能使所有节点都收到信号？如果不能使所有节点收到信号，返回 -1。
 * 
 * 算法：Dijkstra算法
 * 时间复杂度：O((V + E) * log V)，其中V是节点数，E是边数
 * 空间复杂度：O(V + E)
 * 
 * 相关题目链接：
 * 1. LeetCode 743. 网络延迟时间 - https://leetcode.cn/problems/network-delay-time/
 * 2. LeetCode 787. K 站中转内最便宜的航班 - https://leetcode.cn/problems/cheapest-flights-within-k-stops/
 * 3. 洛谷 P4779 单源最短路径 - https://www.luogu.com.cn/problem/P4779
 * 4. POJ 2387 Til the Cows Come Home - http://poj.org/problem?id=2387
 * 5. Codeforces 20C Dijkstra? - https://codeforces.com/problemset/problem/20/C
 * 6. 洛谷 P3371 单源最短路径 - https://www.luogu.com.cn/problem/P3371
 * 7. HDU 2544 最短路 - https://acm.hdu.edu.cn/showproblem.php?pid=2544
 * 8. AtCoder ABC070 D - Transit Tree Path - https://atcoder.jp/contests/abc070/tasks/abc070_d
 * 9. 牛客 NC50439 最短路 - https://ac.nowcoder.com/acm/problem/50439
 * 10. SPOJ SHPATH - https://www.spoj.com/problems/SHPATH/
 * 11. ZOJ 2818 The Traveling Judges Problem - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827366818
 * 12. 51Nod 1018 最短路 - https://www.51nod.com/Challenge/Problem.html#problemId=1018
 * 13. 洛谷 P1144 最短路计数 - https://www.luogu.com.cn/problem/P1144
 * 14. LeetCode 542. 01 矩阵 - https://leetcode.cn/problems/01-matrix/
 * 15. LeetCode 773. 滑动谜题 - https://leetcode.cn/problems/sliding-puzzle/
 */

// 由于编译环境限制，使用基本C++语法实现

const int MAXN = 101;
const int INF = 2147483647;

// 使用基本数组模拟邻接表
int graph_to[MAXN][MAXN];      // 存储邻接点
int graph_weight[MAXN][MAXN];  // 存储边权重
int graph_size[MAXN];          // 存储每个节点的邻接点数量

int dist[MAXN];                // 距离数组
bool visited[MAXN];            // 访问标记数组

// 使用基本数组模拟优先队列
// 分别存储distance, node两个字段
int pq_distance[10000];  // 存储距离
int pq_node[10000];      // 存储节点
int pq_size = 0;         // 优先队列大小

// 向优先队列添加元素
void pq_push(int distance, int node) {
    pq_distance[pq_size] = distance;
    pq_node[pq_size] = node;
    pq_size++;
    
    // 简单的插入排序，按距离从小到大排序
    for (int i = pq_size - 1; i > 0; i--) {
        if (pq_distance[i] < pq_distance[i - 1]) {
            // 交换距离
            int temp = pq_distance[i];
            pq_distance[i] = pq_distance[i - 1];
            pq_distance[i - 1] = temp;
            
            // 交换节点
            temp = pq_node[i];
            pq_node[i] = pq_node[i - 1];
            pq_node[i - 1] = temp;
        } else {
            break;
        }
    }
}

// 从优先队列取出距离最小的元素
void pq_pop(int& distance, int& node) {
    distance = pq_distance[0];
    node = pq_node[0];
    
    // 移除第一个元素
    for (int i = 1; i < pq_size; i++) {
        pq_distance[i - 1] = pq_distance[i];
        pq_node[i - 1] = pq_node[i];
    }
    pq_size--;
}

// 检查优先队列是否为空
bool pq_empty() {
    return pq_size == 0;
}

/**
 * 解决网络延迟时间问题的主函数
 * 算法思路：
 * 1. 这是一道典型的单源最短路径问题，使用Dijkstra算法解决
 * 2. 从源节点k开始，计算到所有其他节点的最短距离
 * 3. 如果存在无法到达的节点，返回-1
 * 4. 否则返回所有最短距离中的最大值
 * 
 * 具体实现：
 * 1. 构建图的邻接表表示
 * 2. 使用优先队列优化的Dijkstra算法计算从节点k到所有节点的最短距离
 * 3. 如果存在无法到达的节点，返回-1
 * 4. 否则返回所有最短距离中的最大值
 * 
 * @param times 边的传递时间列表
 * @param timesSize 边的数量
 * @param n 节点数量
 * @param k 源节点
 * @return 所有节点都收到信号所需的最短时间，如果无法覆盖所有节点则返回-1
 */
int networkDelayTime(int times[][3], int timesSize, int n, int k) {
    // 初始化邻接表
    for (int i = 1; i <= n; i++) {
        graph_size[i] = 0;
    }
    
    // 填充邻接表，每个节点存储其连接的边和对应的权重
    for (int i = 0; i < timesSize; i++) {
        int u = times[i][0];
        int v = times[i][1];
        int w = times[i][2];
        // 添加从节点u到节点v的边，权重为w
        graph_to[u][graph_size[u]] = v;
        graph_weight[u][graph_size[u]] = w;
        graph_size[u]++;
    }
    
    // 初始化优先队列
    pq_size = 0;
    
    // 初始化距离数组，初始值设为无穷大
    // dist[i] 表示从源节点k到节点i的最短距离
    for (int i = 1; i <= n; i++) {
        dist[i] = INF;
        visited[i] = false;
    }
    
    // 源节点到自身的距离为0
    dist[k] = 0;
    // 将源节点加入优先队列，距离为0
    pq_push(0, k);
    
    // Dijkstra算法核心逻辑
    while (!pq_empty()) {
        // 取出距离最小的节点
        int currentDist, currentNode;
        pq_pop(currentDist, currentNode);
        
        // 如果当前距离大于已记录的距离，说明这是一个旧的、不是最优的路径，可以跳过
        // 这是为了避免重复处理已经更新过的节点
        if (currentDist > dist[currentNode]) {
            continue;
        }
        
        // 标记为已访问
        visited[currentNode] = true;
        
        // 遍历当前节点的所有邻居
        for (int i = 0; i < graph_size[currentNode]; i++) {
            int nextNode = graph_to[currentNode][i];
            int weight = graph_weight[currentNode][i];
            
            // 如果邻居已访问过，跳过
            if (visited[nextNode]) {
                continue;
            }
            
            // 计算通过当前节点到达邻居的新距离
            // 新距离 = 当前节点距离 + 当前节点到邻居的边权重
            int newDist = currentDist + weight;
            
            // 如果找到更短的路径，则更新距离并将邻居节点加入优先队列
            if (newDist < dist[nextNode]) {
                dist[nextNode] = newDist;
                pq_push(newDist, nextNode);
            }
        }
    }
    
    // 找出所有节点中的最大距离，即为网络延迟时间
    // 这是因为所有节点都收到信号的时间取决于最后一个收到信号的节点
    int maxDelay = 0;
    for (int i = 1; i <= n; i++) {
        // 如果有节点无法到达，返回-1
        if (dist[i] == INF) {
            return -1;
        }
        // 更新最大延迟时间
        if (dist[i] > maxDelay) {
            maxDelay = dist[i];
        }
    }
    
    // 返回所有节点都收到信号所需的最短时间
    return maxDelay;
}

// 由于编译环境限制，这里不提供main函数
// 在实际使用中，需要根据具体环境提供输入输出方式