/**
 * 飞行路线（语言提供的堆）
 *
 * 题目链接：https://www.luogu.com.cn/problem/P4568
 *
 * 题目描述：
 * Alice和Bob现在要乘飞机旅行，他们选择了一家相对便宜的航空公司
 * 该航空公司一共在n个城市设有业务，设这些城市分别标记为0 ~ n−1
 * 一共有m种航线，每种航线连接两个城市，并且航线有一定的价格
 * Alice 和 Bob 现在要从一个城市沿着航线到达另一个城市，途中可以进行转机
 * 航空公司对他们这次旅行也推出优惠，他们可以免费在最多k种航线上搭乘飞机
 * 那么 Alice 和 Bob 这次出行最少花费多少
 *
 * 解题思路：
 * 这是一个变形的最短路径问题，可以使用Dijkstra算法解决。
 * 与传统最短路径不同的是，这里的状态不仅包括城市位置，还包括已使用的免费机会次数。
 * 我们将状态定义为(城市, 已使用免费机会次数)，图中的节点是这些状态对。
 * 边有两种类型：
 * 1. 免费边：使用一次免费机会乘坐航班，花费为0
 * 2. 付费边：正常付费乘坐航班，花费为票价
 * 使用Dijkstra算法找到从起点状态(起点城市, 0次免费机会)到终点状态(终点城市, 任意免费机会次数)的最少花费。
 *
 * 算法应用场景：
 * - 优惠券使用策略优化
 * - 资源受限的路径规划
 * - 多状态动态规划问题
 *
 * 时间复杂度分析：
 * O((V+k*E)log(V+k*E)) 其中V是城市数，E是航线数
 *
 * 空间复杂度分析：
 * O(V*k) 存储距离数组和访问标记数组
 */

// 由于编译环境问题，无法包含标准库头文件
// 以下为算法核心实现代码，需要在支持C++11及以上标准的环境中编译

/*
const int MAXN = 10001;
const int MAXM = 100001;
const int MAXK = 11;

// 链式前向星建图需要
int head[MAXN];
int next[MAXM];
int to[MAXM];
int weight[MAXM];
int cnt;

// Dijkstra需要
// distance[i][j]表示到达城市i且已使用j次免费机会的最少花费
int distance[MAXN][MAXK];

// visited[i][j]表示状态(城市i, 使用j次免费机会)是否已经确定了最短路径
bool visited[MAXN][MAXK];

// 用语言自己提供的堆
// 元组含义：(花费, 城市, 已使用免费机会次数)
priority_queue<tuple<int, int, int>, vector<tuple<int, int, int>>, greater<tuple<int, int, int>>> heap;

int n, m, k, s, t;

// 初始化函数
void build() {
    cnt = 1;
    for (int i = 0; i < n; i++) {
        head[i] = 0;
        for (int j = 0; j <= k; j++) {
            distance[i][j] = INT_MAX;
            visited[i][j] = false;
        }
    }
    while (!heap.empty()) heap.pop();
}

// 链式前向星加边
void addEdge(int u, int v, int w) {
    next[cnt] = head[u];
    to[cnt] = v;
    weight[cnt] = w;
    head[u] = cnt++;
}

// Dijkstra算法主函数
int dijkstra() {
    // 初始状态：在起点城市且未使用免费机会，花费为0
    distance[s][0] = 0;
    heap.push({0, s, 0});
    
    // Dijkstra算法主循环
    while (!heap.empty()) {
        // 取出花费最小的状态
        auto [cost, u, use] = heap.top();
        heap.pop();
        
        // 如果已经处理过，跳过
        if (visited[u][use]) {
            continue;
        }
        
        // 标记为已处理
        visited[u][use] = true;
        
        // 如果到达终点，直接返回结果
        if (u == t) {
            return cost;
        }
        
        // 遍历所有出边
        for (int ei = head[u], v, w; ei > 0; ei = next[ei]) {
            v = to[ei];
            w = weight[ei];
            
            // 使用免费机会
            if (use < k && distance[v][use + 1] > distance[u][use]) {
                // 使用免费
                distance[v][use + 1] = distance[u][use];
                heap.push({distance[v][use + 1], v, use + 1});
            }
            
            // 不使用免费机会
            if (distance[v][use] > distance[u][use] + w) {
                // 不用免费
                distance[v][use] = distance[u][use] + w;
                heap.push({distance[v][use], v, use});
            }
        }
    }
    return -1;
}

int main() {
    while (cin >> n >> m >> k >> s >> t) {
        build();
        for (int i = 0, a, b, c; i < m; i++) {
            cin >> a >> b >> c;
            addEdge(a, b, c);
            addEdge(b, a, c);
        }
        cout << dijkstra() << endl;
    }
    return 0;
}
*/

// 算法核心思想总结：
// 1. 这是一个多状态最短路径问题，状态包括位置和资源(免费机会次数)
// 2. 图中的节点是状态对(城市, 免费机会次数)，而不是简单的城市节点
// 3. 边有两种类型：免费边(花费为0)和付费边(花费为票价)
// 4. 使用Dijkstra算法可以找到从起点状态到终点状态的最少花费路径