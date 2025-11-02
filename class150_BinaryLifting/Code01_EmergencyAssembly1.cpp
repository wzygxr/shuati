// 紧急集合问题
// 问题描述：
// 一共有n个节点，编号1 ~ n，一定有n-1条边连接形成一颗树
// 从一个点到另一个点的路径上有几条边，就需要耗费几个金币
// 每条查询(a, b, c)表示有三个人分别站在a、b、c点上
// 他们想集合在树上的某个点，并且想花费的金币总数最少
// 一共有m条查询，打印m个答案
// 1 <= n <= 5 * 10^5
// 1 <= m <= 5 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P4281

#include <stdio.h>
#include <algorithm>
using namespace std;

const int MAXN = 500001;
const int LIMIT = 19;

int power;
int head[MAXN];
int next[MAXN << 1];
int to[MAXN << 1];
int cnt;
int deep[MAXN];
int stjump[MAXN][LIMIT];

/**
 * 计算log2(n)的值
 * @param n 输入值
 * @return log2(n)的整数部分
 */
int log2(int n) {
    int ans = 0;
    while ((1 << ans) <= (n >> 1)) {
        ans++;
    }
    return ans;
}

/**
 * 初始化数据结构
 * @param n 节点数量
 */
void build(int n) {
    power = log2(n);
    cnt = 1;
    for (int i = 1; i <= n; i++) {
        head[i] = 0;
    }
}

/**
 * 添加一条边到邻接表中
 * @param u 起点
 * @param v 终点
 */
void addEdge(int u, int v) {
    next[cnt] = head[u];
    to[cnt] = v;
    head[u] = cnt++;
}

/**
 * 深度优先搜索，构建深度信息和倍增表
 * @param u 当前节点
 * @param f 父节点
 */
void dfs(int u, int f) {
    // 记录当前节点的深度
    deep[u] = deep[f] + 1;
    // 记录直接父节点（跳1步）
    stjump[u][0] = f;
    // 构建倍增表：stjump[u][p] = stjump[stjump[u][p-1]][p-1]
    // 即：向上跳2^p步 = 向上跳2^(p-1)步后再跳2^(p-1)步
    for (int p = 1; p <= power; p++) {
        stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
    }
    // 递归处理子节点
    for (int e = head[u]; e != 0; e = next[e]) {
        if (to[e] != f) {
            dfs(to[e], u);
        }
    }
}

/**
 * 使用倍增算法计算两个节点的最近公共祖先(LCA)
 * @param a 节点a
 * @param b 节点b
 * @return 节点a和b的最近公共祖先
 */
int lca(int a, int b) {
    // 确保a是深度更深的节点
    if (deep[a] < deep[b]) {
        int tmp = a;
        a = b;
        b = tmp;
    }
    // 将a提升到与b相同的深度
    for (int p = power; p >= 0; p--) {
        if (deep[stjump[a][p]] >= deep[b]) {
            a = stjump[a][p];
        }
    }
    // 如果a和b已经在同一节点，直接返回
    if (a == b) {
        return a;
    }
    // 同时向上跳跃，直到找到公共祖先
    for (int p = power; p >= 0; p--) {
        if (stjump[a][p] != stjump[b][p]) {
            a = stjump[a][p];
            b = stjump[b][p];
        }
    }
    // 返回最近公共祖先
    return stjump[a][0];
}

/**
 * 计算三个点的最优集合点
 * 算法思路：
 * 1. 计算三个点两两之间的LCA
 * 2. 找到深度最深的LCA作为集合点
 * 3. 计算总花费
 * @param a 第一个点
 * @param b 第二个点
 * @param c 第三个点
 * @param togather 返回最优集合点
 * @param cost 返回最小总花费
 */
void compute(int a, int b, int c, int& togather, long long& cost) {
    // 计算三个点两两之间的LCA
    int h1 = lca(a, b), h2 = lca(a, c), h3 = lca(b, c);
    // 找到深度最浅的LCA
    int high = (h1 != h2) ? (deep[h1] < deep[h2] ? h1 : h2) : h1;
    // 找到深度最深的LCA
    int low = (h1 != h2) ? (deep[h1] > deep[h2] ? h1 : h2) : h3;
    // 最优集合点是深度最深的LCA
    togather = low;
    // 计算总花费：三个点到集合点的距离之和
    // 距离公式：deep[a] + deep[b] + deep[c] - deep[high] * 2 - deep[low]
    cost = (long long)deep[a] + deep[b] + deep[c] - deep[high] * 2 - deep[low];
}

int main() {
    int n, m;
    scanf("%d %d", &n, &m);
    
    build(n);
    
    // 读取边信息并构建邻接表
    for (int i = 1, u, v; i < n; i++) {
        scanf("%d %d", &u, &v);
        addEdge(u, v);
        addEdge(v, u);
    }
    
    // 从节点1开始DFS，构建深度信息和倍增表
    dfs(1, 0);
    
    // 处理m次查询
    for (int i = 1, a, b, c; i <= m; i++) {
        scanf("%d %d %d", &a, &b, &c);
        int togather;
        long long cost;
        compute(a, b, c, togather, cost);
        printf("%d %lld\n", togather, cost);
    }
    
    return 0;
}