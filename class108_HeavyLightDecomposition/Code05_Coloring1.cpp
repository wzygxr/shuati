#include <bits/stdc++.h>
using namespace std;

// 染色，C++版
// 题目来源：洛谷P2486 [SDOI2011]染色
// 题目链接：https://www.luogu.com.cn/problem/P2486
//
// 题目描述：
// 一共有n个节点，给定n-1条边，节点连成一棵树，每个节点给定初始颜色值
// 连续相同颜色被认为是一段，变化了就认为是另一段
// 比如，112221，有三个颜色段，分别为 11、222、1
// 一共有m条操作，每种操作是如下2种类型中的一种
// 操作 C x y z : x到y的路径上，每个节点的颜色都改为z
// 操作 Q x y   : x到y的路径上，打印有几个颜色段
// 1 <= n、m <= 10^5
// 1 <= 任何时候的颜色值 <= 10^9
//
// 解题思路：
// 这是一道经典的树链剖分应用题，需要处理树上路径的染色和查询操作。
// 树上路径操作可以通过树链剖分转化为区间操作，再结合线段树进行高效处理。
// 关键在于线段树节点需要维护区间左端颜色、右端颜色以及颜色段数，并正确处理区间合并时的边界情况。
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
// 2. 使用线段树维护每个区间的颜色信息：
//    - 区间颜色段数
//    - 区间左端颜色
//    - 区间右端颜色
//    - 懒标记（颜色更新）
// 3. 对于染色操作：更新路径上所有节点的颜色
// 4. 对于查询操作：统计路径上的颜色段数，注意路径连接处颜色相同的合并
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
// 1. 洛谷P2486 [SDOI2011]染色（本题）：https://www.luogu.com.cn/problem/P2486
// 2. 洛谷P2146 [NOI2015]软件包管理器：https://www.luogu.com.cn/problem/P2146
// 3. 洛谷P2590 [ZJOI2008]树的统计：https://www.luogu.com.cn/problem/P2590
// 4. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
// 5. HackerEarth Tree Query：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/algorithm/tree-query/
//
// Java实现参考：Code05_Coloring1.java
// Python实现参考：Code_LuoguP2486_Coloring.py
// C++实现参考：Code05_Coloring1.cpp（当前文件）

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
int sum[MAXN << 2];     // 区间颜色段数
int lcolor[MAXN << 2];  // 区间左端颜色
int rcolor[MAXN << 2];  // 区间右端颜色
int change[MAXN << 2];  // 懒标记

void addEdge(int u, int v) {
    nxt[++cntg] = head[u];
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
    sum[i] = sum[i << 1] + sum[i << 1 | 1];
    if (rcolor[i << 1] == lcolor[i << 1 | 1]) {
        sum[i]--;
    }
    lcolor[i] = lcolor[i << 1];
    rcolor[i] = rcolor[i << 1 | 1];
}

// 线段树懒更新
void lazy(int i, int v) {
    sum[i] = 1;
    lcolor[i] = v;
    rcolor[i] = v;
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
        sum[i] = 1;
        lcolor[i] = rcolor[i] = arr[seg[l]];
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

// 区间查询
int query(int jobl, int jobr, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        return sum[i];
    }
    down(i);
    int mid = (l + r) >> 1;
    if (jobr <= mid) {
        return query(jobl, jobr, l, mid, i << 1);
    } else if (jobl > mid) {
        return query(jobl, jobr, mid + 1, r, i << 1 | 1);
    } else {
        int ans = query(jobl, jobr, l, mid, i << 1) + query(jobl, jobr, mid + 1, r, i << 1 | 1);
        if (rcolor[i << 1] == lcolor[i << 1 | 1]) {
            ans--;
        }
        return ans;
    }
}

// 查询单点颜色
int pointColor(int jobi, int l, int r, int i) {
    if (l == r) {
        return lcolor[i];
    }
    down(i);
    int mid = (l + r) >> 1;
    if (jobi <= mid) {
        return pointColor(jobi, l, mid, i << 1);
    } else {
        return pointColor(jobi, mid + 1, r, i << 1 | 1);
    }
}

// 路径更新：将从节点x到节点y的路径上所有节点颜色改为v
void pathUpdate(int x, int y, int v) {
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) swap(x, y);
        update(dfn[top[x]], dfn[x], v, 1, n, 1);
        x = fa[top[x]];
    }
    if (dep[x] > dep[y]) swap(x, y);
    update(dfn[x], dfn[y], v, 1, n, 1);
}

// 路径颜色段数查询：查询从节点x到节点y的路径上有几个颜色段
int pathColors(int x, int y) {
    int ans = 0, sonc, fac;
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) swap(x, y);
        ans += query(dfn[top[x]], dfn[x], 1, n, 1);
        sonc = pointColor(dfn[top[x]], 1, n, 1);
        fac = pointColor(dfn[fa[top[x]]], 1, n, 1);
        if (sonc == fac) ans--;
        x = fa[top[x]];
    }
    ans += query(min(dfn[x], dfn[y]), max(dfn[x], dfn[y]), 1, n, 1);
    return ans;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    cin >> n >> m;
    
    // 读取节点初始颜色
    for (int i = 1; i <= n; i++) {
        cin >> arr[i];
    }
    
    // 读取边信息
    for (int i = 1, u, v; i < n; i++) {
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
    string op;
    int x, y, z;
    for (int i = 1; i <= m; i++) {
        cin >> op >> x >> y;
        if (op == "C") {
            cin >> z;
            pathUpdate(x, y, z);
        } else {  // Q
            cout << pathColors(x, y) << "\n";
        }
    }
    
    return 0;
}