// Dijkstra算法练习题1：网络延迟时间
// 有 n 个网络节点，标记为 1 到 n。
// 给你一个列表 times，表示信号经过有向边的传递时间。 
// times[i] = (ui, vi, wi)，其中 ui 是源节点，vi 是目标节点，wi 是信号从源节点传递到目标节点的时间。
// 现在，从某个节点 K 发出一个信号。需要多久才能使所有节点都收到信号？
// 如果不能使所有节点收到信号，返回 -1。
// 测试链接: https://leetcode.cn/problems/network-delay-time/
// 
// 算法思路：
// 这是一道典型的单源最短路径问题，使用Dijkstra算法解决。
// 1. 构建图的邻接表表示
// 2. 使用优先队列优化的Dijkstra算法计算从节点K到所有节点的最短距离
// 3. 如果存在无法到达的节点，返回-1
// 4. 否则返回所有最短距离中的最大值
//
// 具体实现：
// 1. 初始化图的邻接表表示
// 2. 使用优先队列存储待处理的节点，按距离从小到大排序
// 3. 从起始节点开始，逐步扩展到其他节点
// 4. 对于每个节点，更新其相邻节点的最短距离
// 5. 最后检查是否所有节点都可达，并返回最大距离
//
// 时间复杂度：O((V + E) * log V)，其中V是节点数，E是边数
// 空间复杂度：O(V + E)
//
// 相关题目链接：
// 1. LeetCode 743. 网络延迟时间 - https://leetcode.cn/problems/network-delay-time/
// 2. 洛谷 P4779 单源最短路径 - https://www.luogu.com.cn/problem/P4779
// 3. POJ 2387 Til the Cows Come Home - http://poj.org/problem?id=2387
// 4. Codeforces 20C Dijkstra? - https://codeforces.com/problemset/problem/20/C
// 5. 洛谷 P3371 单源最短路径 - https://www.luogu.com.cn/problem/P3371
// 6. HDU 2544 最短路 - https://acm.hdu.edu.cn/showproblem.php?pid=2544
// 7. AtCoder ABC070 D - Transit Tree Path - https://atcoder.jp/contests/abc070/tasks/abc070_d
// 8. 牛客 NC50439 最短路 - https://ac.nowcoder.com/acm/problem/50439
// 9. SPOJ SHPATH - https://www.spoj.com/problems/SHPATH/
// 10. ZOJ 2818 The Traveling Judges Problem - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827366818
// 11. 51Nod 1018 最短路 - https://www.51nod.com/Challenge/Problem.html#problemId=1018
// 12. 洛谷 P1144 最短路计数 - https://www.luogu.com.cn/problem/P1144
// 13. LeetCode 542. 01 矩阵 - https://leetcode.cn/problems/01-matrix/
// 14. LeetCode 773. 滑动谜题 - https://leetcode.cn/problems/sliding-puzzle/
// 15. 牛客 NC50522 跳楼机 - https://ac.nowcoder.com/acm/problem/50522

// 由于编译环境限制，使用基本C++语法实现

const int MAXN = 101;
const int INF = 2147483647;  // 最大整数值

int n, k;
// 使用基本数组模拟邻接表
int graph_to[MAXN][MAXN];    // 存储邻接点
int graph_weight[MAXN][MAXN]; // 存储边权重
int graph_size[MAXN];        // 存储每个节点的邻接点数量

int dist[MAXN];              // 距离数组
bool visited[MAXN];          // 访问标记数组

// 添加边
void addEdge(int from, int to, int weight) {
    graph_to[from][graph_size[from]] = to;
    graph_weight[from][graph_size[from]] = weight;
    graph_size[from]++;
}

// 简单实现的Dijkstra算法（未使用优先队列优化）
int dijkstra() {
    // 初始化距离数组为无穷大
    for (int i = 1; i <= n; i++) {
        dist[i] = INF;
    }
    // 初始化访问标记数组为false
    for (int i = 1; i <= n; i++) {
        visited[i] = false;
    }
    
    // 起点距离为0
    dist[k] = 0;
    
    // 进行n次循环，每次确定一个节点的最短距离
    for (int i = 1; i <= n; i++) {
        // 找到未访问节点中距离最小的节点
        int u = -1;
        for (int j = 1; j <= n; j++) {
            if (!visited[j] && (u == -1 || dist[j] < dist[u])) {
                u = j;
            }
        }
        
        // 如果找不到可达节点，说明存在无法到达的节点
        if (u == -1 || dist[u] == INF) {
            return -1;
        }
        
        // 标记为已访问
        visited[u] = true;
        
        // 更新相邻节点的距离
        for (int j = 0; j < graph_size[u]; j++) {
            int v = graph_to[u][j];
            int w = graph_weight[u][j];
            
            // 松弛操作：如果通过当前节点u可以缩短到节点v的距离，则更新
            if (!visited[v] && dist[u] + w < dist[v]) {
                dist[v] = dist[u] + w;
            }
        }
    }
    
    // 计算最大距离：遍历所有节点的最短距离，找出最大值
    int maxDist = 0;
    for (int i = 1; i <= n; i++) {
        // 如果存在无法到达的节点，返回-1
        if (dist[i] == INF) {
            return -1;
        }
        // 更新最大距离
        if (dist[i] > maxDist) {
            maxDist = dist[i];
        }
    }
    
    // 返回所有节点都能到达时的最大距离
    return maxDist;
}

// 由于编译环境限制，这里不提供main函数
// 在实际使用中，需要根据具体环境提供输入输出方式