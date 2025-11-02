// 异象石 (LOJ 10132)
// 题目描述：
// 在一个圆上有n个点，按顺时针编号为0到n-1。
// 有m次操作，每次操作会在两个点之间连一条弦。
// 每次操作后，求所有弦将圆分割成多少个区域。
// 测试链接 : https://loj.ac/problem/10132

const int MAXN = 100001;
const int LIMIT = 17;

int n, power;

// 链式前向星存储树结构
int head[MAXN], next[MAXN << 1], to[MAXN << 1], cnt;

// 深度数组和倍增跳跃数组，用于LCA计算
int deep[MAXN];
int stjump[MAXN][LIMIT];

// 子树大小数组
int size[MAXN];

// dfs序数组
int dfn[MAXN];
int dfn2[MAXN];
int dfc;

// 虚树相关
int stack[MAXN];
int top;

// 被选中的点数组
int chosen[MAXN];
int chosen_cnt;

// 计算log2(n)的函数
int log2(int n) {
    int ans = 0;
    while ((1 << ans) <= (n >> 1)) {
        ans++;
    }
    return ans;
}

// 初始化函数
void build() {
    power = log2(n);
    cnt = 1;
    for (int i = 0; i <= n; i++) {
        head[i] = 0;
        chosen[i] = 0;
    }
    dfc = 0;
    chosen_cnt = 0;
}

// 添加边的函数
void addEdge(int u, int v) {
    next[cnt] = head[u];
    to[cnt] = v;
    head[u] = cnt++;
}

// 第一次DFS，预处理深度、dfs序和子树大小
void dfs1(int u, int f) {
    deep[u] = deep[f] + 1;
    stjump[u][0] = f;
    dfn[u] = ++dfc;
    size[u] = 1;
    // 预处理倍增数组
    for (int p = 1; p <= power; p++) {
        stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
    }
    // 遍历所有子节点
    for (int e = head[u]; e != 0; e = next[e]) {
        if (to[e] != f) {
            dfs1(to[e], u);
            size[u] += size[to[e]];
        }
    }
    dfn2[u] = dfc;
}

// 计算最近公共祖先(LCA)
int lca(int a, int b) {
    // 确保a的深度不小于b
    if (deep[a] < deep[b]) {
        int tmp = a;
        a = b;
        b = tmp;
    }
    // 将a向上跳到与b同一深度
    for (int p = power; p >= 0; p--) {
        if (deep[stjump[a][p]] >= deep[b]) {
            a = stjump[a][p];
        }
    }
    // 如果a和b在同一位置，说明b是a的祖先
    if (a == b) {
        return a;
    }
    // 同时向上跳，直到找到最近公共祖先
    for (int p = power; p >= 0; p--) {
        if (stjump[a][p] != stjump[b][p]) {
            a = stjump[a][p];
            b = stjump[b][p];
        }
    }
    return stjump[a][0];
}

// 计算两点间的距离
int dis(int a, int b) {
    int l = lca(a, b);
    return deep[a] + deep[b] - 2 * deep[l];
}