#include <bits/stdc++.h>
using namespace std;

// 重链剖分模版题，C++版
// 题目来源：洛谷P3384 【模板】重链剖分/树链剖分
// 题目链接：https://www.luogu.com.cn/problem/P3384
//
// 题目描述：
// 如题，已知一棵包含N个结点的树（连通且无环），每个节点上包含一个数值，需要支持以下操作：
// 操作 1 x y z : x到y的路径上，每个节点值增加z
// 操作 2 x y   : x到y的路径上，打印所有节点值的累加和
// 操作 3 x z   : x为头的子树上，每个节点值增加z
// 操作 4 x     : x为头的子树上，打印所有节点值的累加和
// 1 <= n、m <= 10^5
// 1 <= MOD <= 2^30
// 输入的值都为int类型
// 查询操作时，打印(查询结果 % MOD)，题目会给定MOD值
//
// 解题思路：
// 使用树链剖分将树上问题转化为线段树问题
// 1. 树链剖分：通过两次DFS将树划分为多条重链
// 2. 线段树：维护区间和，支持区间修改和区间查询
// 3. 路径操作：将树上路径操作转化为多个区间操作
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
// 2. 使用线段树维护每个区间的权值和，支持区间加法操作
// 3. 对于路径加法操作：将路径分解为多段重链进行区间更新
// 4. 对于子树加法操作：直接对子树对应的连续区间进行更新
// 5. 对于路径查询操作：将路径分解为多段重链进行区间查询
// 6. 对于子树查询操作：直接对子树对应的连续区间进行查询
//
// 时间复杂度分析：
// - 树链剖分预处理：O(n)
// - 每次操作：O(log²n)
// - 总体复杂度：O(m log²n)
// 空间复杂度：O(n)
//
// 是否为最优解：
// 是的，树链剖分是解决此类树上路径操作问题的经典方法，
// 时间复杂度已经达到了理论下限，是最优解之一。
//
// 相关题目链接：
// 1. 洛谷P3384 【模板】重链剖分/树链剖分（本题）：https://www.luogu.com.cn/problem/P3384
// 2. 洛谷P2590 [ZJOI2008]树的统计：https://www.luogu.com.cn/problem/P2590
// 3. 洛谷P3178 [HAOI2015]树上操作：https://www.luogu.com.cn/problem/P3178
// 4. 洛谷P2146 [NOI2015]软件包管理器：https://www.luogu.com.cn/problem/P2146
// 5. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
//
// Java实现参考：Code01_HLD1.java
// Python实现参考：Code01_HLD1.py
// C++实现参考：Code01_HLD1.cpp（当前文件）

const int MAXN = 100005;
int n, m, root, MOD;
int arr[MAXN];

// 邻接表存储树
int head[MAXN], next_edge[MAXN << 1], to_edge[MAXN << 1], cnt_edge = 0;

// 树链剖分相关数组
int fa[MAXN];     // 父节点
int dep[MAXN];    // 深度
int siz[MAXN];    // 子树大小
int son[MAXN];    // 重儿子
int top[MAXN];    // 所在重链的顶部节点
int dfn[MAXN];    // dfs序
int seg[MAXN];    // dfs序对应的节点
int cnt_dfn = 0;  // dfs序计数器

// 线段树相关数组
long long sum[MAXN << 2];    // 区间和
long long add_tag[MAXN << 2]; // 懒标记

void add_edge(int u, int v) {
    next_edge[++cnt_edge] = head[u];
    to_edge[cnt_edge] = v;
    head[u] = cnt_edge;
}

// 第一次dfs，计算fa, dep, siz, son
void dfs1(int u, int f) {
    fa[u] = f;
    dep[u] = dep[f] + 1;
    siz[u] = 1;
    
    for (int e = head[u]; e; e = next_edge[e]) {
        int v = to_edge[e];
        if (v != f) {
            dfs1(v, u);
            siz[u] += siz[v];
            if (son[u] == 0 || siz[son[u]] < siz[v]) {
                son[u] = v;
            }
        }
    }
}

// 第二次dfs，计算top, dfn, seg
void dfs2(int u, int t) {
    top[u] = t;
    dfn[u] = ++cnt_dfn;
    seg[cnt_dfn] = u;
    
    if (son[u] == 0) return;
    
    dfs2(son[u], t);  // 先处理重儿子
    
    for (int e = head[u]; e; e = next_edge[e]) {
        int v = to_edge[e];
        if (v != fa[u] && v != son[u]) {
            dfs2(v, v);  // 轻儿子作为新重链的顶端
        }
    }
}

// 线段树操作
void up(int i) {
    sum[i] = (sum[i << 1] + sum[i << 1 | 1]) % MOD;
}

void lazy(int i, long long v, int n) {
    sum[i] = (sum[i] + v * n) % MOD;
    add_tag[i] = (add_tag[i] + v) % MOD;
}

void down(int i, int ln, int rn) {
    if (add_tag[i] != 0) {
        lazy(i << 1, add_tag[i], ln);
        lazy(i << 1 | 1, add_tag[i], rn);
        add_tag[i] = 0;
    }
}

void build(int l, int r, int i) {
    if (l == r) {
        sum[i] = arr[seg[l]] % MOD;
        return;
    }
    int mid = (l + r) >> 1;
    build(l, mid, i << 1);
    build(mid + 1, r, i << 1 | 1);
    up(i);
}

void add(int jobl, int jobr, int jobv, int l, int r, int i) {
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

long long query(int jobl, int jobr, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        return sum[i];
    }
    int mid = (l + r) >> 1;
    down(i, mid - l + 1, r - mid);
    long long ans = 0;
    if (jobl <= mid) ans = (ans + query(jobl, jobr, l, mid, i << 1)) % MOD;
    if (jobr > mid) ans = (ans + query(jobl, jobr, mid + 1, r, i << 1 | 1)) % MOD;
    return ans;
}

// 路径加法: 从x到y的路径上所有节点值增加v
void path_add(int x, int y, int v) {
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) swap(x, y);
        add(dfn[top[x]], dfn[x], v, 1, n, 1);
        x = fa[top[x]];
    }
    if (dep[x] > dep[y]) swap(x, y);
    add(dfn[x], dfn[y], v, 1, n, 1);
}

// 子树加法: x的子树上所有节点值增加v
void subtree_add(int x, int v) {
    add(dfn[x], dfn[x] + siz[x] - 1, v, 1, n, 1);
}

// 路径查询: 查询从x到y的路径上所有节点值的和
long long path_sum(int x, int y) {
    long long ans = 0;
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) swap(x, y);
        ans = (ans + query(dfn[top[x]], dfn[x], 1, n, 1)) % MOD;
        x = fa[top[x]];
    }
    if (dep[x] > dep[y]) swap(x, y);
    ans = (ans + query(dfn[x], dfn[y], 1, n, 1)) % MOD;
    return ans;
}

// 子树查询: 查询x的子树上所有节点值的和
long long subtree_sum(int x) {
    return query(dfn[x], dfn[x] + siz[x] - 1, 1, n, 1);
}

int main() {
    scanf("%d%d%d%d", &n, &m, &root, &MOD);
    
    for (int i = 1; i <= n; i++) {
        scanf("%d", &arr[i]);
    }
    
    for (int i = 1; i < n; i++) {
        int u, v;
        scanf("%d%d", &u, &v);
        add_edge(u, v);
        add_edge(v, u);
    }
    
    dfs1(root, 0);
    dfs2(root, root);
    build(1, n, 1);
    
    for (int i = 1; i <= m; i++) {
        int op;
        scanf("%d", &op);
        
        if (op == 1) {
            int x, y, v;
            scanf("%d%d%d", &x, &y, &v);
            path_add(x, y, v);
        } else if (op == 2) {
            int x, y;
            scanf("%d%d", &x, &y);
            printf("%lld\n", path_sum(x, y));
        } else if (op == 3) {
            int x, v;
            scanf("%d%d", &x, &v);
            subtree_add(x, v);
        } else {
            int x;
            scanf("%d", &x);
            printf("%lld\n", subtree_sum(x));
        }
    }
    
    return 0;
}