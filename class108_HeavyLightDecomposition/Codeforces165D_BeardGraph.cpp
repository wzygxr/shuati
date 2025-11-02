#include <cstdio>
#include <cstring>
#include <algorithm>
using namespace std;

// Codeforces 165D Beard Graph
// 题目描述：
// 给定一棵n个节点的树，节点编号从1到n。
// 初始时树上所有边都是白色的。
// 现在有三种操作：
// 1. 0 i : 将第i条边的颜色翻转（白变黑，黑变白）
// 2. 1 a b : 询问从节点a到节点b的路径上是否存在白色的边，如果存在则输出1，否则输出0
// 3. 2 a b : 询问从节点a到节点b的路径上有多少条白色边
// 测试链接：https://codeforces.com/problemset/problem/165/D

const int MAXN = 100001;

// 图相关
int head[MAXN], next_edge[MAXN << 1], to_edge[MAXN << 1], edge_id[MAXN << 1], cnt_edge = 0;

// 树链剖分相关
int fa[MAXN], dep[MAXN], siz[MAXN], son[MAXN], top[MAXN], dfn[MAXN], rnk[MAXN], cnt_dfn = 0;

// 边的颜色状态：1表示白色，0表示黑色
int edge_color[MAXN];

// 边到节点的映射
int edge_to_node[MAXN];

// 线段树相关
int sum[MAXN << 2]; // 白色边的数量
bool has_white[MAXN << 2]; // 是否存在白色边

// 添加边
void add_edge(int u, int v, int id) {
    next_edge[++cnt_edge] = head[u];
    to_edge[cnt_edge] = v;
    edge_id[cnt_edge] = id;
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
    has_white[i] = has_white[i << 1] || has_white[i << 1 | 1];
}

// 构建线段树
void build(int l, int r, int i) {
    if (l == r) {
        // 叶子节点不需要特殊处理，初始值为0
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
        sum[i] = jobv;
        has_white[i] = (jobv > 0);
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
int query_sum(int jobl, int jobr, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        return sum[i];
    }
    int mid = (l + r) >> 1;
    int ans = 0;
    if (jobl <= mid) ans += query_sum(jobl, jobr, l, mid, i << 1);
    if (jobr > mid) ans += query_sum(jobl, jobr, mid + 1, r, i << 1 | 1);
    return ans;
}

// 区间查询是否存在白色边
bool query_has_white(int jobl, int jobr, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        return has_white[i];
    }
    int mid = (l + r) >> 1;
    bool ans = false;
    if (jobl <= mid) ans = ans || query_has_white(jobl, jobr, l, mid, i << 1);
    if (jobr > mid) ans = ans || query_has_white(jobl, jobr, mid + 1, r, i << 1 | 1);
    return ans;
}

// 翻转边的颜色
void flip_edge(int edge_id) {
    edge_color[edge_id] = 1 - edge_color[edge_id];
    // 更新线段树中对应节点的值
    int node = edge_to_node[edge_id];
    update(dfn[node], edge_color[edge_id], 1, cnt_dfn, 1);
}

// 查询路径上是否存在白色边
bool path_has_white(int x, int y) {
    bool ans = false;
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) swap(x, y);
        ans = ans || query_has_white(dfn[top[x]], dfn[x], 1, cnt_dfn, 1);
        x = fa[top[x]];
    }
    if (dep[x] > dep[y]) swap(x, y);
    if (x != y) { // 排除LCA节点本身
        ans = ans || query_has_white(dfn[x] + 1, dfn[y], 1, cnt_dfn, 1);
    }
    return ans;
}

// 查询路径上白色边的数量
int path_white_count(int x, int y) {
    int ans = 0;
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) swap(x, y);
        ans += query_sum(dfn[top[x]], dfn[x], 1, cnt_dfn, 1);
        x = fa[top[x]];
    }
    if (dep[x] > dep[y]) swap(x, y);
    if (x != y) { // 排除LCA节点本身
        ans += query_sum(dfn[x] + 1, dfn[y], 1, cnt_dfn, 1);
    }
    return ans;
}

int main() {
    int n;
    scanf("%d", &n);
    
    // 读取边信息
    static int u[MAXN], v[MAXN];
    for (int i = 1; i < n; i++) {
        scanf("%d%d", &u[i], &v[i]);
        add_edge(u[i], v[i], i);
        add_edge(v[i], u[i], i);
    }
    
    // 初始化所有边为白色
    memset(edge_color, 1, sizeof(edge_color));
    
    // 树链剖分，以节点1为根
    dfs1(1, 0);
    dfs2(1, 1);
    
    // 建立边到节点的映射（将边权转移到深度更深的节点上）
    for (int i = 1; i < n; i++) {
        int node = (dep[u[i]] > dep[v[i]]) ? u[i] : v[i];
        edge_to_node[i] = node;
    }
    
    // 构建线段树
    build(1, n, 1);
    
    // 初始化线段树中的边权值
    for (int i = 1; i < n; i++) {
        update(dfn[edge_to_node[i]], 1, 1, n, 1);
    }
    
    int m;
    scanf("%d", &m);
    for (int i = 0; i < m; i++) {
        int op, x, y;
        scanf("%d", &op);
        
        if (op == 0) {
            int edge_id;
            scanf("%d", &edge_id);
            // 翻转边的颜色
            flip_edge(edge_id);
        } else if (op == 1) {
            scanf("%d%d", &x, &y);
            printf("%d\n", path_has_white(x, y) ? 1 : 0);
        } else { // op == 2
            scanf("%d%d", &x, &y);
            printf("%d\n", path_white_count(x, y));
        }
    }
    
    return 0;
}