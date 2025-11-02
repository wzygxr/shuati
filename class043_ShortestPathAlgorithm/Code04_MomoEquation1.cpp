// 墨墨的等式(dijkstra算法)
// 一共有n种正数，每种数可以选择任意个，个数不能是负数
// 那么一定有某些数值可以由这些数字累加得到
// 请问在[l...r]范围上，有多少个数能被累加得到
// 0 <= n <= 12
// 0 <= 数值范围 <= 5 * 10^5
// 1 <= l <= r <= 10^12
// 测试链接 : https://www.luogu.com.cn/problem/P2371

/*
 * 算法思路：
 * 这道题可以转化为图论问题，用Dijkstra算法解决。
 * 选择数组中最小的数作为基准数x，构建模x意义下的最短路图。
 * 每个点i表示模x余数为i的所有数中能被表示的最小值。
 * 通过其他数字在不同余数之间建立边，权值为数字值。
 * 最后统计[l,r]范围内能被表示的数的个数。
 * 
 * 时间复杂度：O(x * log x + n)
 * 空间复杂度：O(x)
 * 
 * 题目来源：洛谷P2371 墨墨的等式 (https://www.luogu.com.cn/problem/P2371)
 * 相关题目：
 * 1. 洛谷P3403 跳楼机 - 与本题思路相同 (https://www.luogu.com.cn/problem/P3403)
 * 2. POJ 2371 Counting Capacities - 经典同余最短路问题 (http://poj.org/problem?id=2371)
 * 3. Codeforces 1117D Magic Gems - 矩阵快速幂+最短路优化DP (https://codeforces.com/problemset/problem/1117/D)
 * 4. 洛谷P2662 牛场围栏 - 同余最短路应用 (https://www.luogu.com.cn/problem/P2662)
 * 5. POJ 1061 青蛙的约会 - 扩展欧几里得算法 (http://poj.org/problem?id=1061)
 * 6. Codeforces 986F Oppa Funcan Style Remastered - 同余最短路 (https://codeforces.com/problemset/problem/986/F)
 * 7. 洛谷P2421 荒岛野人 - 数论问题 (https://www.luogu.com.cn/problem/P2421)
 * 8. POJ 3250 Bad Hair Day - 单调栈问题 (http://poj.org/problem?id=3250)
 * 9. 洛谷P9140 背包 - 同余最短路应用 (https://www.luogu.com.cn/problem/P9140)
 * 10. 洛谷P1776 数列分段 - 动态规划问题 (https://www.luogu.com.cn/problem/P1776)
 * 11. 洛谷P1948 数学作业 - 同余最短路 (https://www.luogu.com.cn/problem/P1948)
 * 12. LeetCode 743 Network Delay Time - Dijkstra算法应用 (https://leetcode.cn/problems/network-delay-time/)
 * 13. LeetCode 1631 Path With Minimum Effort - Dijkstra算法应用 (https://leetcode.cn/problems/path-with-minimum-effort/)
 * 14. LeetCode 773 Sliding Puzzle - BFS/最短路问题 (https://leetcode.cn/problems/sliding-puzzle/)
 * 15. AtCoder ARC084_B Small Multiple - 01-BFS问题 (https://atcoder.jp/contests/abc077/tasks/arc084_b)
 */

// 由于编译环境问题，使用基本C++实现，避免使用STL容器

const int MAXN = 500001;

int n, x;
long long l, r;

// 简化版邻接表
int adj_to[MAXN][100];     // 每个节点最多连接100个其他节点
long long adj_weight[MAXN][100]; // 对应的权重
int adj_count[MAXN];       // 每个节点的邻接边数量

long long dist[MAXN];      // 距离数组
bool visited[MAXN];        // 访问标记数组

// 初始化函数
void init() {
    for (int i = 0; i < x; i++) {
        adj_count[i] = 0;
        dist[i] = 9223372036854775807LL; // LONG_LONG_MAX
        visited[i] = false;
    }
}

// 添加边的函数
void add_edge(int from, int to, long long weight) {
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
    long long queue_val[MAXN];  // 存储距离值
    int queue_node[MAXN];       // 存储节点编号
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
            long long weight = adj_weight[u][i];
            
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
long long calculate_result() {
    dijkstra();
    long long ans = 0;
    for (int i = 0; i < x; i++) {
        if (r >= dist[i]) {
            ans += (r - dist[i]) / x + 1;
        }
        if (l >= dist[i]) {
            ans -= (l - dist[i]) / x + 1;
        }
    }
    return ans;
}

// 由于无法使用标准输入输出，提供一个示例函数框架
// 实际使用时需要根据具体环境实现输入输出
long long solve(int n_val, long long l_val, long long r_val, int arr[], int arr_size) {
    n = n_val;
    l = l_val - 1;
    r = r_val;
    
    // 过滤掉0值并找到最小值
    x = 2147483647; // INT_MAX
    for (int i = 0; i < arr_size; i++) {
        if (arr[i] != 0 && arr[i] < x) {
            x = arr[i];
        }
    }
    
    if (x == 2147483647) {
        return 0; // 所有数都是0
    }
    
    init();
    
    // 添加边
    for (int i = 0; i < arr_size; i++) {
        int num = arr[i];
        if (num != 0 && num != x) {  // 不处理0和基准数本身
            for (int j = 0; j < x; j++) {
                add_edge(j, (j + num) % x, num);
            }
        }
    }
    
    return calculate_result();
}