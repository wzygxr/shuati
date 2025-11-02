#include <bits/stdc++.h>
using namespace std;

// Codeforces 916E. Jamie and Tree
// 题目链接：https://codeforces.com/problemset/problem/916E
//
// 题目描述：
// 给定一棵包含n个节点的树，每个节点有一个权值。支持以下操作：
// 1. 将根节点换为x
// 2. 将包含u和v的最小子树中每个节点权值加x
// 3. 查询以v为根的子树的总和
//
// 数据范围：
// 1 ≤ n, q ≤ 10^5
// 1 ≤ 节点权值 ≤ 10^7
//
// 解题思路：
// 1. 使用树链剖分处理换根操作
// 2. 对于换根操作，需要分类讨论当前查询节点与根节点的位置关系
// 3. 使用线段树维护区间和，支持区间加法
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分预处理
// 2. 对于换根操作，记录当前根节点
// 3. 对于子树修改操作，根据当前根节点位置分类处理
// 4. 对于子树查询操作，同样需要分类处理
//
// 时间复杂度分析：
// - 树链剖分预处理：O(n)
// - 每次操作：O(log²n)
// - 总体复杂度：O(n + q log²n)
//
// 空间复杂度：O(n)
//
// 是否为最优解：
// 是的，树链剖分结合线段树是解决此类换根操作问题的经典方法。
//
// 相关题目链接：
// 1. Codeforces 916E：https://codeforces.com/problemset/problem/916E
// 2. 洛谷P3979：https://www.luogu.com.cn/problem/P3979
// 3. Codeforces 165D：https://codeforces.com/problemset/problem/165/D

const int MAXN = 100001;

// 图的邻接表表示
int head[MAXN], next_edge[MAXN * 2], to_edge[MAXN * 2];
int cnt_edge = 0;

// 树链剖分相关数组
int fa[MAXN], dep[MAXN], siz[MAXN], son[MAXN], top[MAXN], dfn[MAXN], rnk[MAXN];
int cnt_dfn = 0;

// 节点权值
long long val[MAXN];

// 线段树相关
long long sum[MAXN * 4];    // 区间和
long long add[MAXN * 4];    // 懒标记

// 当前根节点
int root = 1;

// 添加边
void addEdge(int u, int v) {
    next_edge[++cnt_edge] = head[u];
    to_edge[cnt_edge] = v;
    head[u] = cnt_edge;
}

// 第一次DFS：计算父节点、深度、子树大小、重儿子
void dfs1(int u, int father) {
    fa[u] = father;
    dep[u] = dep[father] + 1;
    siz[u] = 1;
    son[u] = 0;
    
    for (int e = head[u]; e != 0; e = next_edge[e]) {
        int v = to_edge[e];
        if (v == father) continue;
        
        dfs1(v, u);
        siz[u] += siz[v];
        if (son[u] == 0 || siz[son[u]] < siz[v]) {
            son[u] = v;
        }
    }
}

// 第二次DFS：计算重链顶部、DFS序
void dfs2(int u, int topNode) {
    top[u] = topNode;
    dfn[u] = ++cnt_dfn;
    rnk[cnt_dfn] = u;
    
    if (son[u] != 0) {
        dfs2(son[u], topNode);
    }
    
    for (int e = head[u]; e != 0; e = next_edge[e]) {
        int v = to_edge[e];
        if (v == fa[u] || v == son[u]) continue;
        dfs2(v, v);
    }
}

// 线段树操作
void up(int i) {
    sum[i] = sum[i << 1] + sum[i << 1 | 1];
}

void lazy(int i, long long v, int n) {
    sum[i] += v * n;
    add[i] += v;
}

void down(int i, int ln, int rn) {
    if (add[i] != 0) {
        lazy(i << 1, add[i], ln);
        lazy(i << 1 | 1, add[i], rn);
        add[i] = 0;
    }
}

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

void addRange(int ql, int qr, long long v, int l, int r, int i) {
    if (ql <= l && r <= qr) {
        lazy(i, v, r - l + 1);
        return;
    }
    int mid = (l + r) >> 1;
    down(i, mid - l + 1, r - mid);
    if (ql <= mid) addRange(ql, qr, v, l, mid, i << 1);
    if (qr > mid) addRange(ql, qr, v, mid + 1, r, i << 1 | 1);
    up(i);
}

long long queryRange(int ql, int qr, int l, int r, int i) {
    if (ql <= l && r <= qr) {
        return sum[i];
    }
    int mid = (l + r) >> 1;
    down(i, mid - l + 1, r - mid);
    long long res = 0;
    if (ql <= mid) res += queryRange(ql, qr, l, mid, i << 1);
    if (qr > mid) res += queryRange(ql, qr, mid + 1, r, i << 1 | 1);
    return res;
}

// 判断节点u是否是节点v的祖先
bool isAncestor(int u, int v) {
    while (dep[v] > dep[u]) {
        v = fa[v];
    }
    return u == v;
}

// 找到节点u和v的LCA
int findLCA(int u, int v) {
    while (top[u] != top[v]) {
        if (dep[top[u]] < dep[top[v]]) {
            swap(u, v);
        }
        u = fa[top[u]];
    }
    return dep[u] < dep[v] ? u : v;
}

// 找到节点u到根节点路径上，深度最小的节点，使得该节点是节点v的祖先
int findAncestorOnPath(int u, int v) {
    int lca = findLCA(u, v);
    if (lca == v) return v;
    if (lca == u) return u;
    
    // 从v向上跳，直到找到u的祖先
    int temp = v;
    while (dep[temp] > dep[lca]) {
        if (isAncestor(u, temp)) {
            return temp;
        }
        temp = fa[temp];
    }
    return lca;
}

// 子树修改操作（考虑换根）
void subtreeAdd(int u, long long v) {
    if (u == root) {
        // 如果修改的是根节点的子树，就是整棵树
        addRange(1, cnt_dfn, v, 1, cnt_dfn, 1);
    } else if (isAncestor(u, root)) {
        // 如果u是当前根节点的祖先
        // 需要修改整棵树，然后减去u到root路径上u的儿子节点的子树
        addRange(1, cnt_dfn, v, 1, cnt_dfn, 1);
        
        // 找到u到root路径上u的直接儿子
        int temp = root;
        while (dep[temp] > dep[u] + 1) {
            temp = fa[temp];
        }
        if (fa[temp] == u) {
            // 减去这个儿子的子树
            addRange(dfn[temp], dfn[temp] + siz[temp] - 1, -v, 1, cnt_dfn, 1);
        }
    } else {
        // 正常情况，直接修改u的子树
        addRange(dfn[u], dfn[u] + siz[u] - 1, v, 1, cnt_dfn, 1);
    }
}

// 子树查询操作（考虑换根）
long long subtreeSum(int u) {
    if (u == root) {
        // 如果查询的是根节点的子树，就是整棵树
        return queryRange(1, cnt_dfn, 1, cnt_dfn, 1);
    } else if (isAncestor(u, root)) {
        // 如果u是当前根节点的祖先
        // 需要查询整棵树，然后减去u到root路径上u的儿子节点的子树
        long long total = queryRange(1, cnt_dfn, 1, cnt_dfn, 1);
        
        // 找到u到root路径上u的直接儿子
        int temp = root;
        while (dep[temp] > dep[u] + 1) {
            temp = fa[temp];
        }
        if (fa[temp] == u) {
            // 减去这个儿子的子树
            total -= queryRange(dfn[temp], dfn[temp] + siz[temp] - 1, 1, cnt_dfn, 1);
        }
        return total;
    } else {
        // 正常情况，直接查询u的子树
        return queryRange(dfn[u], dfn[u] + siz[u] - 1, 1, cnt_dfn, 1);
    }
}

// 包含u和v的最小子树修改
void minSubtreeAdd(int u, int v, long long x) {
    int lca = findLCA(u, v);
    
    // 找到包含u和v的最小子树的根节点
    int subtreeRoot = lca;
    if (isAncestor(subtreeRoot, root)) {
        // 如果当前根节点在子树中，需要找到真正的子树根节点
        int anc1 = findAncestorOnPath(u, root);
        int anc2 = findAncestorOnPath(v, root);
        
        if (dep[anc1] > dep[anc2]) {
            subtreeRoot = anc1;
        } else {
            subtreeRoot = anc2;
        }
    }
    
    subtreeAdd(subtreeRoot, x);
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, q;
    cin >> n >> q;
    
    // 读取节点权值
    for (int i = 1; i <= n; i++) {
        cin >> val[i];
    }
    
    // 读取边信息
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        addEdge(u, v);
        addEdge(v, u);
    }
    
    // 树链剖分
    dfs1(1, 0);
    dfs2(1, 1);
    
    // 构建线段树
    build(1, n, 1);
    
    // 处理操作
    for (int i = 0; i < q; i++) {
        int op;
        cin >> op;
        
        if (op == 1) {
            // 换根操作
            cin >> root;
        } else if (op == 2) {
            // 最小子树修改
            int u, v;
            long long x;
            cin >> u >> v >> x;
            minSubtreeAdd(u, v, x);
        } else {
            // 子树查询
            int v;
            cin >> v;
            cout << subtreeSum(v) << endl;
        }
    }
    
    return 0;
}