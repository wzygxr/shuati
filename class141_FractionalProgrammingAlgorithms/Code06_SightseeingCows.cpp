#include <cstdio>
#include <cstring>
using namespace std;

// 观光奶牛 (Sightseeing Cows)
// 给定一个有向图，每个点有一个点权value[i]，每条边有一个边权weight[i]
// 找到一个环，使得环上点权和除以边权和最大
// 输出最大比率值，保留两位小数
// 1 <= n <= 1000
// 1 <= m <= 5000
// 1 <= value[i], weight[i] <= 1000
// 测试链接 : https://www.luogu.com.cn/problem/P2868
// 测试链接 : http://poj.org/problem?id=3621

const int MAXN = 1001;
const int MAXM = 5001;
const double NA = -1e9;
const double sml = 1e-9;

// 链式前向星
int head[MAXN], next[MAXM], to[MAXM], cnt;
double weight[MAXM];

// 点权
int value[MAXN];

// dfs判断正环，每个点的累积点权
double dist[MAXN];

// dfs判断正环，每个点是否是递归路径上的点
bool path[MAXN];

int n, m;

void prepare() {
    cnt = 1;
    for (int i = 0; i < MAXN; i++) head[i] = 0;
}

void addEdge(int u, int v, double w) {
    next[cnt] = head[u];
    to[cnt] = v;
    weight[cnt] = w;
    head[u] = cnt++;
}

// 其实就是spfa的递归版，时间复杂度O(n * m)
bool dfs(int u, double x) {
    if (u == 0) {
        // 认为0号点是超级源点，具有通往所有点的有向边
        for (int i = 1; i <= n; i++) {
            if (dfs(i, x)) {
                return true;
            }
        }
    } else {
        path[u] = true;
        for (int e = head[u]; e != 0; e = next[e]) {
            int v = to[e];
            double w = weight[e] - x; // 边权减去比率值
            // 只有v的累积点权变大，才会递归，非常强的剪枝
            // 如果递归路径回到v，并且此时是v的累积点权更大的情况，说明遇到了正环
            // 或者后续递归找到了正环
            // 直接返回true
            if (dist[v] < dist[u] + w) {
                dist[v] = dist[u] + w;
                if (path[v] || dfs(v, x)) {
                    return true;
                }
            }
        }
        path[u] = false;
    }
    return false;
}

// 所有边权都减去x，返回图中是否存在正环
bool check(double x) {
    for (int i = 1; i <= n; i++) {
        dist[i] = 0;
        path[i] = false;
    }
    return dfs(0, x);
}

int main() {
    scanf("%d%d", &n, &m);
    prepare();
    for (int i = 1; i <= n; i++) {
        scanf("%d", &value[i]);
    }
    for (int i = 1; i <= m; i++) {
        int u, v;
        double w;
        scanf("%d%d%lf", &u, &v, &w);
        addEdge(u, v, w);
    }
    double l = 0, r = 1000, x, ans = 0;
    while (l < r && r - l >= sml) {
        x = (l + r) / 2;
        // 如果存在正环，说明可以找到更大的比率值，向右二分
        // 如果不存在正环，说明当前比率值过大，向左二分
        if (check(x)) {
            ans = x;
            l = x + sml;
        } else {
            r = x - sml;
        }
    }
    printf("%.2f\n", ans);
    return 0;
}