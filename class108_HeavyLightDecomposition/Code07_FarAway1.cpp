#include <bits/stdc++.h>
using namespace std;

// 遥远的国度，C++版
// 题目来源：洛谷P3979 遥远的国度
// 题目链接：https://www.luogu.com.cn/problem/P3979
//
// 题目描述：
// 一共有n个节点，给定n-1条边，节点连成一棵树，给定树的初始头节点，给定每个点的点权
// 一共有m条操作，每种操作是如下3种类型中的一种
// 操作 1 x     : 树的头节点变成x，整棵树需要重新组织
// 操作 2 x y v : x到y的路径上，所有节点的值改成v
// 操作 3 x     : 在当前树的状态下，打印u的子树中的最小值
// 1 <= n、m <= 10^5
// 任何时候节点值一定是正数
//
// 解题思路：
// 这是一道复杂的树链剖分应用题，涉及换根操作和子树查询。
// 树链剖分可以高效处理路径更新操作，但换根操作需要特殊处理。
// 关键在于理解换根后子树的结构变化，并正确计算查询结果。
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
// 2. 使用线段树维护每个节点的值，支持区间更新和区间查询最小值
// 3. 对于操作1：记录新的根节点，不需要实际重构树
// 4. 对于操作2：使用树链剖分将路径更新转化为区间更新
// 5. 对于操作3：根据当前根节点位置，分情况计算子树最小值：
//    - 如果查询节点就是根节点，则返回整棵树的最小值
//    - 如果根节点不在查询节点的子树中，则返回查询节点子树的最小值
//    - 如果根节点在查询节点的子树中，则需要排除根节点所在子树的部分
//
// 时间复杂度分析：
// - 树链剖分预处理：O(n)
// - 每次操作：O(log²n)
// - 总体复杂度：O(m log²n)
// 空间复杂度：O(n)
//
// 是否为最优解：
// 是的，这是该问题的最优解之一。树链剖分能够将树上路径操作转化为区间操作，
// 再结合线段树的数据结构，可以高效处理大量查询和更新操作。
//
// 相关题目链接：
// 1. 洛谷P3979 遥远的国度（本题）：https://www.luogu.com.cn/problem/P3979
// 2. 洛谷P3976 [AHOI2015]旅游：https://www.luogu.com.cn/problem/P3976
// 3. 洛谷P2486 [SDOI2011]染色：https://www.luogu.com.cn/problem/P2486
// 4. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
// 5. HackerEarth Tree Query：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/algorithm/tree-query/
//
// Java实现参考：Code07_FarAway1.java
// Python实现参考：暂无
// C++实现参考：Code07_FarAway1.cpp（当前文件）

const int MAXN = 100001;
int n, m;
int arr[MAXN];

// 链式前向星存图
int head[MAXN], nxt[MAXN << 1], to[MAXN << 1], cntg = 0;

// 树链剖分相关数组
int fa[MAXN];     // 父节点
int dep[MAXN];    // 深度
int siz[MAXN];    // 子树大小
int son[MAXN];    // 重儿子
int top[MAXN];    // 所在重链的顶部节点
int dfn[MAXN];    // dfs序
int seg[MAXN];    // dfs序对应的节点
int cntd = 0;     // dfs序计数器

// 线段树相关数组
int minv[MAXN << 2];   // 区间最小值
int change[MAXN << 2]; // 懒标记

void addEdge(int u, int v) {
    ++cntg;
    nxt[cntg] = head[u];
    to[cntg] = v;
    head[u] = cntg;
}

// 第一次DFS，计算父节点、深度、子树大小和重儿子
void dfs1(int u, int f) {
    fa[u] = f;
    dep[u] = dep[f] + 1;
    siz[u] = 1;
    
    for (int e = head[u]; e; e = nxt[e]) {
        int v = to[e];
        if (v != f) {
            dfs1(v, u);
        }
    }
    
    for (int e = head[u]; e; e = nxt[e]) {
        int v = to[e];
        if (v != f) {
            siz[u] += siz[v];
            if (son[u] == 0 || siz[son[u]] < siz[v]) {
                son[u] = v;
            }
        }
    }
}

// 第二次DFS，计算重链顶端和dfs序
void dfs2(int u, int t) {
    top[u] = t;
    dfn[u] = ++cntd;
    seg[cntd] = u;
    
    if (son[u] == 0) return;
    
    dfs2(son[u], t);  // 先处理重儿子
    
    for (int e = head[u]; e; e = nxt[e]) {
        int v = to[e];
        if (v != fa[u] && v != son[u]) {
            dfs2(v, v);  // 轻儿子作为新重链的顶端
        }
    }
}

// 线段树向上更新
void up(int i) {
    minv[i] = min(minv[i << 1], minv[i << 1 | 1]);
}

// 线段树懒更新
void lazy(int i, int v) {
    minv[i] = v;
    change[i] = v;
}

// 线段树下传懒标记
void down(int i) {
    if (change[i] != 0) {
        lazy(i << 1, change[i]);
        lazy(i << 1 | 1, change[i]);
        change[i] = 0;
    }
}

// 构建线段树
void build(int l, int r, int i) {
    if (l == r) {
        minv[i] = arr[seg[l]];
        return;
    }
    int mid = (l + r) >> 1;
    build(l, mid, i << 1);
    build(mid + 1, r, i << 1 | 1);
    up(i);
}

// 区间更新
void update(int jobl, int jobr, int jobv, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        lazy(i, jobv);
        return;
    }
    down(i);
    int mid = (l + r) >> 1;
    if (jobl <= mid) {
        update(jobl, jobr, jobv, l, mid, i << 1);
    }
    if (jobr > mid) {
        update(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
    }
    up(i);
}

// 区间查询最小值
int query(int jobl, int jobr, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        return minv[i];
    }
    down(i);
    int mid = (l + r) >> 1;
    int ans = INT_MAX;
    if (jobl <= mid) {
        ans = min(ans, query(jobl, jobr, l, mid, i << 1));
    }
    if (jobr > mid) {
        ans = min(ans, query(jobl, jobr, mid + 1, r, i << 1 | 1));
    }
    return ans;
}

// 路径更新：将从节点x到节点y的路径上所有节点值改为v
void pathUpdate(int x, int y, int v) {
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) swap(x, y);
        update(dfn[top[x]], dfn[x], v, 1, n, 1);
        x = fa[top[x]];
    }
    update(min(dfn[x], dfn[y]), max(dfn[x], dfn[y]), v, 1, n, 1);
}

// 已知root一定在u的子树上
// 找到u哪个儿子的子树里有root，返回那个儿子的编号
int findSon(int root, int u) {
    while (top[root] != top[u]) {
        if (fa[top[root]] == u) {
            return top[root];
        }
        root = fa[top[root]];
    }
    return son[u];
}

// 假设树的头节点变成root，在当前树的状态下，查询u的子树中的最小值
int treeQuery(int root, int u) {
    if (root == u) {
        return minv[1];
    } else if (dfn[root] < dfn[u] || dfn[u] + siz[u] <= dfn[root]) {
        return query(dfn[u], dfn[u] + siz[u] - 1, 1, n, 1);
    } else {
        int uson = findSon(root, u);
        int ans = INT_MAX;
        if (1 <= dfn[uson] - 1) {
            ans = min(ans, query(1, dfn[uson] - 1, 1, n, 1));
        }
        if (dfn[uson] + siz[uson] <= n) {
            ans = min(ans, query(dfn[uson] + siz[uson], n, 1, n, 1));
        }
        return ans;
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    cin >> n >> m;
    
    // 读取边信息
    for (int i = 1, u, v; i < n; i++) {
        cin >> u >> v;
        addEdge(u, v);
        addEdge(v, u);
    }
    
    // 读取节点初始值
    for (int i = 1; i <= n; i++) {
        cin >> arr[i];
    }
    
    // 树链剖分
    dfs1(1, 0);
    dfs2(1, 1);
    
    // 构建线段树
    build(1, n, 1);
    
    int root;
    cin >> root;
    
    // 处理操作
    for (int i = 1, op, x, y, v; i <= m; i++) {
        cin >> op;
        if (op == 1) {
            cin >> root;
        } else if (op == 2) {
            cin >> x >> y >> v;
            pathUpdate(x, y, v);
        } else {  // op == 3
            cin >> x;
            cout << treeQuery(root, x) << "\n";
        }
    }
    
    return 0;
}