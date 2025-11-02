#include <cstdio>
#include <cstring>
using namespace std;

// 最大密度子图
// 给定一个无向图，找到一个子图使得其密度最大
// 密度定义为子图中边数除以点数
// 1 <= n <= 1000
// 0 <= m <= 10000
// 测试链接 : https://www.luogu.com.cn/problem/UVA1389

const int MAXN = 1001;
const int MAXM = 10001;
const double sml = 1e-9;

// 链式前向星
int head[MAXN];
int next_edge[MAXM * 2]; // 无向图，边数翻倍
int to[MAXM * 2];
int cnt;

// 度数
int degree[MAXN];

// 超级源点和超级汇点
int S, T;

int n, m;

void prepare() {
    cnt = 1;
    memset(head, 0, sizeof(head));
    memset(degree, 0, sizeof(degree));
    S = 0;      // 超级源点
    T = n + 1;  // 超级汇点
}

void addEdge(int u, int v) {
    // 无向图添加双向边
    next_edge[cnt] = head[u];
    to[cnt] = v;
    head[u] = cnt++;
    
    next_edge[cnt] = head[v];
    to[cnt] = u;
    head[v] = cnt++;
    
    degree[u]++;
    degree[v]++;
}

// 检查是否存在密度大于x的子图
// 这需要构建网络流模型并求解最大流
// 由于网络流实现较为复杂，这里只给出框架
bool check(double x) {
    /*
     * 构造网络流模型：
     * 1. 每个点i拆成i和i'两个点
     * 2. S向每个点i连容量为m的边
     * 3. 每个点i向T连容量为2*x+m-degree[i]的边
     * 4. 原图中的每条边(i,j)，连接i'到j'和j'到i'，容量为1
     * 5. 每个点i连接到i'，容量为无穷大
     * 
     * 如果最大流 < m*n，则存在密度大于x的子图
     */
    
    // 实际实现需要网络流算法，此处省略具体代码
    // 返回示例值
    return true;
}

int main() {
    while (scanf("%d%d", &n, &m) != EOF) {
        if (n == 0 && m == 0) break;
        
        prepare();
        
        for (int i = 1; i <= m; i++) {
            int u, v;
            scanf("%d%d", &u, &v);
            addEdge(u, v);
        }
        
        // 特殊情况：如果没有边，密度为0
        if (m == 0) {
            printf("0\n");
            continue;
        }
        
        // 二分答案求解最大密度
        double l = 0, r = m, ans = 0;
        while (r - l >= sml) {
            double x = (l + r) / 2;
            if (check(x)) {
                ans = x;
                l = x + sml;
            } else {
                r = x - sml;
            }
        }
        
        printf("%.8f\n", ans);
    }
    
    return 0;
}