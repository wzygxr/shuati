#include <cstdio>
#include <cstring>
using namespace std;

// 最小密度路径
// 给定一个有向图，每条边有两个权值a[i]和b[i]
// 定义路径密度为路径上所有a[i]的和除以所有b[i]的和
// 求所有简单路径中密度最小的值
// 1 <= n <= 50
// 1 <= m <= 300
// 0 <= a[i], b[i] <= 100000
// b[i] > 0
// 测试链接 : https://www.luogu.com.cn/problem/P1730

const int MAXN = 51;
const int MAXM = 301;
const double INF = 1e20;
const double sml = 1e-9;

// 链式前向星
int head[MAXN];
int next_edge[MAXM];
int to[MAXM];
int a[MAXM];
int b[MAXM];
int cnt;

// floyd数组，f[i][j][k]表示从i到j，只经过编号不超过k的点的最小密度
// sumA[i][j][k]表示从i到j，只经过编号不超过k的点的最小密度路径的a值和
// sumB[i][j][k]表示从i到j，只经过编号不超过k的点的最小密度路径的b值和
double f[MAXN][MAXN][MAXN];
double sumA[MAXN][MAXN][MAXN];
double sumB[MAXN][MAXN][MAXN];

int n, m;

void prepare() {
    cnt = 1;
    memset(head, 0, sizeof(head));
    
    // 初始化floyd数组
    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= n; j++) {
            for (int k = 0; k <= n; k++) {
                f[i][j][k] = INF;
                sumA[i][j][k] = 0;
                sumB[i][j][k] = 0;
            }
        }
    }
    
    // 初始化直接相连的边
    for (int e = 1; e < cnt; e++) {
        int u = to[e ^ 1]; // 反向边
        int v = to[e];
        if (b[e] > 0) { // 避免除零错误
            f[u][v][0] = (double)a[e] / b[e];
            sumA[u][v][0] = a[e];
            sumB[u][v][0] = b[e];
        }
    }
}

void addEdge(int u, int v, int aa, int bb) {
    next_edge[cnt] = head[u];
    to[cnt] = v;
    a[cnt] = aa;
    b[cnt] = bb;
    head[u] = cnt++;
    
    // 添加反向边，便于查找
    next_edge[cnt] = head[v];
    to[cnt] = u;
    a[cnt] = aa;
    b[cnt] = bb;
    head[v] = cnt++;
}

// Floyd变形，计算所有点对之间的最小密度路径
void floyd() {
    for (int k = 1; k <= n; k++) {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                // 通过点k转移
                double newSumA = sumA[i][k][k-1] + sumA[k][j][k-1];
                double newSumB = sumB[i][k][k-1] + sumB[k][j][k-1];
                
                if (newSumB > 0) { // 避免除零错误
                    double newDensity = newSumA / newSumB;
                    
                    if (newDensity < f[i][j][k-1]) {
                        f[i][j][k] = newDensity;
                        sumA[i][j][k] = newSumA;
                        sumB[i][j][k] = newSumB;
                    } else {
                        f[i][j][k] = f[i][j][k-1];
                        sumA[i][j][k] = sumA[i][j][k-1];
                        sumB[i][j][k] = sumB[i][j][k-1];
                    }
                }
            }
        }
    }
}

int main() {
    scanf("%d%d", &n, &m);
    
    for (int i = 1; i <= m; i++) {
        int u, v, aa, bb;
        scanf("%d%d%d%d", &u, &v, &aa, &bb);
        addEdge(u, v, aa, bb);
    }
    
    prepare();
    floyd();
    
    // 寻找最小密度
    double ans = INF;
    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= n; j++) {
            if (f[i][j][n] < ans) {
                ans = f[i][j][n];
            }
        }
    }
    
    printf("%.10f\n", ans);
    
    return 0;
}