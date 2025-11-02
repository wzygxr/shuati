```cpp
#include <cstdio>
#include <cstring>
#include <algorithm>
using namespace std;

// 洛谷P2590 树的统计
// 题目描述：
// 一棵树上有n个节点，节点编号为1到n，每个节点都有一个点权。
// 现在有三种操作：
// 1. CHANGE u t : 把结点u的权值改为t
// 2. QMAX u v : 询问从点u到点v的路径上的节点的最大权值
// 3. QSUM u v : 询问从点u到点v的路径上的节点的权值和
// 测试链接：https://www.luogu.com.cn/problem/P2590

const int MAXN = 30001;

// 图相关
int head[MAXN], next_edge[MAXN << 1], to_edge[MAXN << 1], cnt_edge = 0;

// 树链剖分相关
int fa[MAXN], dep[MAXN], siz[MAXN], son[MAXN], top[MAXN], dfn[MAXN], rnk[MAXN], cnt_dfn = 0;

// 节点权值
int val[MAXN];

// 线段树相关
int sum[MAXN << 2], max_val[MAXN << 2];

// 添加边
void add_edge(int u, int v) {
    next_edge[++cnt_edge] = head[u];
    to_edge[cnt_edge] = v;
    head[u] = cnt_edge;
}

// 第一次dfs，计算树链剖分所需信息
void dfs1(int u, int f) {
    fa[u] = f;
    dep[u] = dep[f] + 1;
    siz[u] = 1;
    son[u] = 0;
    
    for (int e = head[u], v; e; e = next_edge[e]) {
        v = to_edge[e];
        if (v != f) {
            dfs1(v, u);
            siz[u] += siz[v];
            if (son[u] == 0 || siz[son[u]] < siz[v]) {
                son[u] = v;
            }
        }
    }
}

// 第二次dfs，计算重链剖分
void dfs2(int u, int t) {
    top[u] = t;
    dfn[u] = ++cnt_dfn;
    rnk[cnt_dfn] = u;
    
    if (son[u] == 0) return;
    dfs2(son[u], t);
    
    for (int e = head[u], v; e; e = next_edge[e]) {
        v = to_edge[e];
        if (v != fa[u] && v != son[u]) {
            dfs2(v, v);
        }
    }
}

// 线段树操作
void up(int i) {
    sum[i] = sum[i << 1] + sum[i << 1 | 1];
    max_val[i] = max(max_val[i << 1], max_val[i << 1 | 1]);
}

// 构建线段树
void build(int l, int r, int i) {
    if (l == r) {
        sum[i] = max_val[i] = val[rnk[l]];
        return;
    }
    int mid = (l + r) >> 1;
    build(l, mid, i << 1);
    build(mid + 1, r, i << 1 | 1);
    up(i);
}

// 单点更新
void update(int jobx, int jobv, int l, int r, int i) {
    if (l == r) {
        sum[i] = max_val[i] = jobv;
        return;
    }
    int mid = (l + r) >> 1;
    if (jobx <= mid) {
        update(jobx, jobv, l, mid, i << 1);
    } else {
        update(jobx, jobv, mid + 1, r, i << 1 | 1);
    }
    up(i);
}

// 区间查询和
int querySum(int jobl, int jobr, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        return sum[i];
    }
    int mid = (l + r) >> 1;
    int ans = 0;
    if (jobl <= mid) ans += querySum(jobl, jobr, l, mid, i << 1);
    if (jobr > mid) ans += querySum(jobl, jobr, mid + 1, r, i << 1 | 1);
    return ans;
}

// 区间查询最大值
int queryMax(int jobl, int jobr, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        return max_val[i];
    }
    int mid = (l + r) >> 1;
    int ans = -2147483647; // INT_MIN
    if (jobl <= mid) ans = max(ans, queryMax(jobl, jobr, l, mid, i << 1));
    if (jobr > mid) ans = max(ans, queryMax(jobl, jobr, mid + 1, r, i << 1 | 1));
    return ans;
}

// 查询路径上的节点权值和
int pathSum(int x, int y, int n) {
    int ans = 0;
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) swap(x, y);
        ans += querySum(dfn[top[x]], dfn[x], 1, n, 1);
        x = fa[top[x]];
    }
    if (dep[x] > dep[y]) swap(x, y);
    ans += querySum(dfn[x], dfn[y], 1, n, 1);
    return ans;
}

// 查询路径上的节点最大权值
int pathMax(int x, int y, int n) {
    int ans = -2147483647; // INT_MIN
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) swap(x, y);
        ans = max(ans, queryMax(dfn[top[x]], dfn[x], 1, n, 1));
        x = fa[top[x]];
    }
    if (dep[x] > dep[y]) swap(x, y);
    ans = max(ans, queryMax(dfn[x], dfn[y], 1, n, 1));
    return ans;
}

int main() {
    int n;
    scanf("%d", &n);
    
    // 读取边信息
    for (int i = 1; i < n; i++) {
        int u, v;
        scanf("%d%d", &u, &v);
        add_edge(u, v);
        add_edge(v, u);
    }
    
    // 读取节点权值
    for (int i = 1; i <= n; i++) {
        scanf("%d", &val[i]);
    }
    
    // 树链剖分
    dfs1(1, 0);
    dfs2(1, 1);
    
    // 构建线段树
    build(1, n, 1);
    
    int q;
    scanf("%d", &q);
    for (int i = 0; i < q; i++) {
        char op[10];
        int u, v;
        scanf("%s%d%d", op, &u, &v);
        
        if (op[0] == 'C') { // CHANGE
            update(dfn[u], v, 1, n, 1);
        } else if (op[1] == 'M') { // QMAX
            printf("%d\n", pathMax(u, v, n));
        } else { // QSUM
            printf("%d\n", pathSum(u, v, n));
        }
    }
    
    return 0;
}
```