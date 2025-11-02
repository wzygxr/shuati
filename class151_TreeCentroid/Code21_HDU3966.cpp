// HDU 3966 Aragorn's Story
// 题目描述：给定一棵树，支持两种操作：1. 路径上的所有节点权值增加k；2. 查询某个节点的权值
// 算法思想：树链剖分 + 线段树，树链剖分的第一步就是找到树的重心来分割树
// 测试链接：http://acm.hdu.edu.cn/showproblem.php?pid=3966
// 时间复杂度：O(n log^2 n)
// 空间复杂度：O(n)

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

const int MAXN = 50010;

int w[MAXN]; // 节点权值
int tree[MAXN * 4]; // 线段树
int lazy[MAXN * 4]; // 延迟标记
vector<int> graph[MAXN]; // 邻接表
int fa[MAXN]; // 父节点
int dep[MAXN]; // 深度
int siz[MAXN]; // 子树大小
int son[MAXN]; // 重儿子
int top[MAXN]; // 所在链的顶端
int dfn[MAXN]; // 时间戳
int rnk[MAXN]; // 时间戳对应的节点
int val[MAXN]; // 时间戳对应的权值
int cnt = 0; // 时间戳计数器
int n, m, q; // 节点数，边数，查询数

// 线段树更新操作
void pushDown(int rt, int l, int r) {
    if (lazy[rt] != 0) {
        int mid = (l + r) / 2;
        tree[rt * 2] += lazy[rt] * (mid - l + 1);
        tree[rt * 2 + 1] += lazy[rt] * (r - mid);
        lazy[rt * 2] += lazy[rt];
        lazy[rt * 2 + 1] += lazy[rt];
        lazy[rt] = 0;
    }
}

// 线段树区间更新
void update(int rt, int l, int r, int L, int R, int k) {
    if (L <= l && r <= R) {
        tree[rt] += k * (r - l + 1);
        lazy[rt] += k;
        return;
    }
    pushDown(rt, l, r);
    int mid = (l + r) / 2;
    if (L <= mid) update(rt * 2, l, mid, L, R, k);
    if (R > mid) update(rt * 2 + 1, mid + 1, r, L, R, k);
    tree[rt] = tree[rt * 2] + tree[rt * 2 + 1];
}

// 线段树单点查询
int query(int rt, int l, int r, int pos) {
    if (l == r) {
        return tree[rt];
    }
    pushDown(rt, l, r);
    int mid = (l + r) / 2;
    if (pos <= mid) return query(rt * 2, l, mid, pos);
    else return query(rt * 2 + 1, mid + 1, r, pos);
}

// 第一次DFS：计算父节点、深度、子树大小、重儿子
void dfs1(int u, int f) {
    fa[u] = f;
    dep[u] = dep[f] + 1;
    siz[u] = 1;
    son[u] = 0;
    int maxSize = 0;
    for (int v : graph[u]) {
        if (v != f) {
            dfs1(v, u);
            siz[u] += siz[v];
            if (siz[v] > maxSize) {
                maxSize = siz[v];
                son[u] = v;
            }
        }
    }
}

// 第二次DFS：分配时间戳，建立链
void dfs2(int u, int topf) {
    top[u] = topf;
    dfn[u] = ++cnt;
    rnk[cnt] = u;
    val[cnt] = w[u];
    if (son[u] != 0) {
        dfs2(son[u], topf); // 优先处理重儿子
        for (int v : graph[u]) {
            if (v != fa[u] && v != son[u]) {
                dfs2(v, v); // 轻儿子单独成链
            }
        }
    }
}

// 树链剖分的路径更新
void updatePath(int u, int v, int k) {
    while (top[u] != top[v]) {
        if (dep[top[u]] < dep[top[v]]) {
            swap(u, v);
        }
        update(1, 1, cnt, dfn[top[u]], dfn[u], k);
        u = fa[top[u]];
    }
    if (dep[u] > dep[v]) {
        swap(u, v);
    }
    update(1, 1, cnt, dfn[u], dfn[v], k);
}

// 初始化线段树
void build(int rt, int l, int r) {
    if (l == r) {
        tree[rt] = val[l];
        return;
    }
    int mid = (l + r) / 2;
    build(rt * 2, l, mid);
    build(rt * 2 + 1, mid + 1, r);
    tree[rt] = tree[rt * 2] + tree[rt * 2 + 1];
}

// 清空图
void clearGraph() {
    for (int i = 1; i <= n; i++) {
        graph[i].clear();
    }
}

int main() {
    while (cin >> n >> m >> q) {
        // 初始化
        for (int i = 1; i <= n; i++) {
            cin >> w[i];
        }
        clearGraph();
        
        // 读取边
        for (int i = 0; i < m; i++) {
            int u, v;
            cin >> u >> v;
            graph[u].push_back(v);
            graph[v].push_back(u);
        }
        
        // 树链剖分
        cnt = 0;
        dfs1(1, 0);
        dfs2(1, 1);
        
        // 建立线段树
        fill(tree, tree + MAXN * 4, 0);
        fill(lazy, lazy + MAXN * 4, 0);
        build(1, 1, cnt);
        
        // 处理查询
        for (int i = 0; i < q; i++) {
            char op[2];
            cin >> op;
            if (op[0] == 'Q') {
                int u;
                cin >> u;
                cout << query(1, 1, cnt, dfn[u]) << endl;
            } else {
                int u, v, k;
                cin >> u >> v >> k;
                if (op[0] == 'I') {
                    updatePath(u, v, k);
                } else if (op[0] == 'D') {
                    updatePath(u, v, -k);
                }
            }
        }
    }
    return 0;
}

// 注意：在HDU上提交时，需要将代码适配为HDU的格式