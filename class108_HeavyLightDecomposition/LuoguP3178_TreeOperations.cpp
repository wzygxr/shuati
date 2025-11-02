```cpp
#include <cstdio>
#include <cstring>
#include <algorithm>
using namespace std;

// 洛谷P3178 树上操作
// 题目描述：
// 有一棵节点数为N的树，以节点1为根节点，初始时每个节点都有一个权值。
// 现在有三种操作：
// 1. 操作1：格式：1 x a，表示将节点x的权值加上a
// 2. 操作2：格式：2 x a，表示将以节点x为根的子树上的所有节点的权值加上a
// 3. 操作3：格式：3 x y，表示查询从节点x到节点y的路径上的所有节点的权值和
// 测试链接：https://www.luogu.com.cn/problem/P3178

const int MAXN = 100001;

// 图相关
int head[MAXN], next_edge[MAXN << 1], to_edge[MAXN << 1], cnt_edge = 0;

// 树链剖分相关
int fa[MAXN], dep[MAXN], siz[MAXN], son[MAXN], top[MAXN], dfn[MAXN], rnk[MAXN], cnt_dfn = 0;

// 节点权值
long long val[MAXN];

// 线段树相关
long long sum[MAXN << 2], add_tag[MAXN << 2];

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
}

void lazy(int i, long long v, int n) {
    sum[i] += v * n;
    add_tag[i] += v;
}

void down(int i, int ln, int rn) {
    if (add_tag[i] != 0) {
        lazy(i << 1, add_tag[i], ln);
        lazy(i << 1 | 1, add_tag[i], rn);
        add_tag[i] = 0;
    }
}

// 构建线段树
void build(int l, int r, int i) {
    if (l == r) {
        sum[i] = val[rnk[l]];
        return;
    }
    int mid = (l + r) >> 1;
    build(l, mid, i << 1);
    build(mid + 1, r, i << 1 | 1);
    up(i);
}

// 区间加法
void add(int jobl, int jobr, long long jobv, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        lazy(i, jobv, r - l + 1);
        return;
    }
    int mid = (l + r) >> 1;
    down(i, mid - l + 1, r - mid);
    if (jobl <= mid) add(jobl, jobr, jobv, l, mid, i << 1);
    if (jobr > mid) add(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
    up(i);
}

// 区间查询
long long query(int jobl, int jobr, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        return sum[i];
    }
    int mid = (l + r) >> 1;
    down(i, mid - l + 1, r - mid);
    long long ans = 0;
    if (jobl <= mid) ans += query(jobl, jobr, l, mid, i << 1);
    if (jobr > mid) ans += query(jobl, jobr, mid + 1, r, i << 1 | 1);
    return ans;
}

// 单点加法：将节点x的权值加上v
void node_add(int x, long long v, int n) {
    add(dfn[x], dfn[x], v, 1, n, 1);
}

// 子树加法：将以节点x为根的子树上的所有节点的权值加上v
void subtree_add(int x, long long v, int n) {
    add(dfn[x], dfn[x] + siz[x] - 1, v, 1, n, 1);
}

// 路径查询：查询从节点x到节点y的路径上的所有节点的权值和
long long path_sum(int x, int y, int n) {
    long long ans = 0;
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) swap(x, y);
        ans += query(dfn[top[x]], dfn[x], 1, n, 1);
        x = fa[top[x]];
    }
    if (dep[x] > dep[y]) swap(x, y);
    ans += query(dfn[x], dfn[y], 1, n, 1);
    return ans;
}

int main() {
    int n, m;
    scanf("%d%d", &n, &m);
    
    // 读取节点初始权值
    for (int i = 1; i <= n; i++) {
        scanf("%lld", &val[i]);
    }
    
    // 读取边信息
    for (int i = 1; i < n; i++) {
        int u, v;
        scanf("%d%d", &u, &v);
        add_edge(u, v);
        add_edge(v, u);
    }
    
    // 树链剖分
    dfs1(1, 0);
    dfs2(1, 1);
    
    // 构建线段树
    build(1, n, 1);
    
    // 处理操作
    for (int i = 0; i < m; i++) {
        int op, x, y;
        long long a;
        scanf("%d", &op);
        
        if (op == 1) {
            scanf("%d%lld", &x, &a);
            node_add(x, a, n);
        } else if (op == 2) {
            scanf("%d%lld", &x, &a);
            subtree_add(x, a, n);
        } else { // op == 3
            scanf("%d%d", &x, &y);
            printf("%lld\n", path_sum(x, y, n));
        }
    }
    
    return 0;
}
```