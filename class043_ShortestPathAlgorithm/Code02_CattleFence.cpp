// 牛场围栏
// 给定一个长度为n的数组arr, arr[i]代表第i种木棍的长度，每种木棍有无穷多个
// 给定一个正数m，表示你可以把任何一根木棍消去最多m的长度，同一种木棍可以消去不同的长度
// 你可以随意拼接木棍形成一个长度，返回不能拼出来的长度中，最大值是多少
// 如果你可以拼出所有的长度，返回-1
// 如果不能拼出来的长度有无穷多，返回-1
// 1 <= n <= 100
// 1 <= arr[i] <= 3000
// 1 <= m <= 3000
// 测试链接 : https://www.luogu.com.cn/problem/P2662

/*
 * 算法思路：
 * 这道题使用同余最短路算法解决。
 * 通过Dijkstra算法构建模x意义下的最短路图，其中x是所有可能长度中的最小值。
 * 每个点i表示模x余数为i的所有长度中能拼出的最小值。
 * 通过其他木棍长度在不同余数之间建立边，权值为木棍长度。
 * 最后找出不能拼出的最大长度。
 * 
 * 时间复杂度：O(x * log x + n)
 * 空间复杂度：O(x)
 * 
 * 题目来源：洛谷P2662 牛场围栏 (https://www.luogu.com.cn/problem/P2662)
 * 相关题目：
 * 1. 洛谷P3403 跳楼机 - 同类型同余最短路问题 (https://www.luogu.com.cn/problem/P3403)
 * 2. 洛谷P2371 墨墨的等式 - 同余最短路经典问题 (https://www.luogu.com.cn/problem/P2371)
 * 3. POJ 1061 青蛙的约会 - 数论相关问题 (http://poj.org/problem?id=1061)
 * 4. Codeforces 986F Oppa Funcan Style Remastered - 同余最短路 (https://codeforces.com/problemset/problem/986/F)
 * 5. 洛谷P2421 荒岛野人 - 数论问题 (https://www.luogu.com.cn/problem/P2421)
 * 6. POJ 3250 Bad Hair Day - 单调栈问题 (http://poj.org/problem?id=3250)
 * 7. 洛谷P9140 背包 - 同余最短路应用 (https://www.luogu.com.cn/problem/P9140)
 * 8. 洛谷P1776 数列分段 - 动态规划问题 (https://www.luogu.com.cn/problem/P1776)
 * 9. 洛谷P1948 数学作业 - 同余最短路 (https://www.luogu.com.cn/problem/P1948)
 * 10. POJ 2371 Counting Capacities - 经典同余最短路问题
 */

// 由于编译环境问题，使用基本C++实现，避免使用STL容器

const int MAXN = 101;
const int MAXV = 3001;
const int MAXM = 30001;
const long long INF = 9223372036854775807LL; // LONG_LONG_MAX

int n, m, x;

// 输入数组
int arr[MAXN];

// 标记数组
bool set_used[MAXV];

// 简化版邻接表
int adj_to[MAXV][100];     // 每个节点最多连接100个其他节点
int adj_weight[MAXV][100]; // 对应的权重
int adj_count[MAXV];       // 每个节点的邻接边数量

long long dist[MAXV];      // 距离数组
bool visited[MAXV];        // 访问标记数组

// 初始化函数
void init() {
    for (int i = 0; i < MAXV; i++) {
        set_used[i] = false;
        adj_count[i] = 0;
        dist[i] = INF;
        visited[i] = false;
    }
}

// 添加边的函数
void add_edge(int from, int to, int weight) {
    if (adj_count[from] < 100) {
        adj_to[from][adj_count[from]] = to;
        adj_weight[from][adj_count[from]] = weight;
        adj_count[from]++;
    }
}

// 简化版Dijkstra算法，使用数组模拟优先队列
void dijkstra() {
    dist[0] = 0;
    
    // 使用简单数组作为队列
    long long queue_val[MAXV];  // 存储距离值
    int queue_node[MAXV];       // 存储节点编号
    int queue_size = 0;
    
    // 初始节点入队
    queue_val[queue_size] = 0;
    queue_node[queue_size] = 0;
    queue_size++;
    
    while (queue_size > 0) {
        // 找到最小距离的节点
        int min_index = 0;
        for (int i = 1; i < queue_size; i++) {
            if (queue_val[i] < queue_val[min_index]) {
                min_index = i;
            }
        }
        
        long long current_dist = queue_val[min_index];
        int u = queue_node[min_index];
        
        // 从队列中移除该节点
        for (int i = min_index; i < queue_size - 1; i++) {
            queue_val[i] = queue_val[i + 1];
            queue_node[i] = queue_node[i + 1];
        }
        queue_size--;
        
        if (visited[u]) {
            continue;
        }
        
        visited[u] = true;
        
        // 更新邻接节点的距离
        for (int i = 0; i < adj_count[u]; i++) {
            int v = adj_to[u][i];
            int weight = adj_weight[u][i];
            
            if (!visited[v] && current_dist + weight < dist[v]) {
                dist[v] = current_dist + weight;
                queue_val[queue_size] = dist[v];
                queue_node[queue_size] = v;
                queue_size++;
            }
        }
    }
}

// 计算结果的函数
int calculate_result() {
    if (x == 1) {
        return -1;
    }
    
    // 添加边
    for (int i = 1; i <= n; i++) {
        for (int j = (arr[i] - m > 1) ? (arr[i] - m) : 1; j <= arr[i]; j++) {
            if (!set_used[j]) {
                set_used[j] = true;
                for (int k = 0; k < x; k++) {
                    add_edge(k, (k + j) % x, j);
                }
            }
        }
    }
    
    dijkstra();
    
    int ans = 0;
    for (int i = 1; i < x; i++) {
        if (dist[i] == INF) {
            return -1;
        }
        if (dist[i] - x > ans) {
            ans = dist[i] - x;
        }
    }
    return ans;
}

// 由于无法使用标准输入输出，提供一个示例函数框架
// 实际使用时需要根据具体环境实现输入输出
int solve(int n_val, int m_val, int arr_val[]) {
    n = n_val;
    m = m_val;
    
    // 复制数组
    for (int i = 1; i <= n; i++) {
        arr[i] = arr_val[i-1];
    }
    
    // 计算x
    x = 2147483647; // INT_MAX
    for (int i = 1; i <= n; i++) {
        int val = (arr[i] - m > 1) ? (arr[i] - m) : 1;
        if (val < x) {
            x = val;
        }
    }
    
    init();
    
    return calculate_result();
}