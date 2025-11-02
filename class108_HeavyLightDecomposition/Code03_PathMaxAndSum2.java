package class161;

// 路径最大值与累加和，C++版
// 题目来源：洛谷P2590 [ZJOI2008]树的统计
// 题目链接：https://www.luogu.com.cn/problem/P2590
//
// 题目描述：
// 一共有n个节点，给定n-1条边，节点连成一棵树，每个节点给定权值
// 一共有m条操作，每种操作是如下3种类型中的一种
// 操作 CHANGE x y : x的权值修改为y
// 操作 QMAX x y   : x到y的路径上，打印节点值的最大值
// 操作 QSUM x y   : x到y的路径上，打印节点值的累加和
// 1 <= n <= 3 * 10^4
// 0 <= m <= 2 * 10^5
// -30000 <= 节点权值 <= +30000
//
// 解题思路：
// 使用树链剖分将树上问题转化为线段树问题
// 1. 树链剖分：通过两次DFS将树划分为多条重链
// 2. 线段树：维护区间和与区间最大值
// 3. 路径操作：将树上路径操作转化为多个区间操作
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
// 2. 使用线段树维护每个区间的权值和与最大值
// 3. 对于修改操作：更新节点权值
// 4. 对于查询操作：计算路径上的权值和或最大值
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
// 1. 洛谷P2590 [ZJOI2008]树的统计（本题）：https://www.luogu.com.cn/problem/P2590
// 2. 洛谷P3384 【模板】重链剖分/树链剖分：https://www.luogu.com.cn/problem/P3384
// 3. 洛谷P3178 [HAOI2015]树上操作：https://www.luogu.com.cn/problem/P3178
// 4. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
// 5. HackerEarth Tree Query with Multiple Operations：https://www.hackerearth.com/practice/data-structures/advanced-data-structures/segment-trees/practice-problems/algorithm/tree-query-2/
//
// Java实现参考：Code03_PathMaxAndSum1.java
// Python实现参考：Code03_PathMaxAndSum1.py
// C++实现参考：Code03_PathMaxAndSum2.java（当前文件）

//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 30001;
//const int INF = 10000001;
//int n, m;
//int arr[MAXN];
//
//int head[MAXN];
//int nxt[MAXN << 1;
//int to[MAXN << 1;
//int cntg = 0;
//
//int fa[MAXN;
//int dep[MAXN;
//int siz[MAXN;
//int son[MAXN;
//int top[MAXN;
//int dfn[MAXN;
//int seg[MAXN;
//int cntd = 0;
//
//int maxv[MAXN << 2;
//int sumv[MAXN << 2;
//
//void addEdge(int u, int v) {
//    nxt[++cntg] = head[u;
//    to[cntg] = v;
//    head[u] = cntg;
//}
//
//void dfs1(int u, int f) {
//    fa[u] = f;
//    dep[u] = dep[f] + 1;
//    siz[u] = 1;
//    for (int e = head[u], v; e; e = nxt[e]) {
//        v = to[e;
//        if (v != f) {
//            dfs1(v, u;
//        }
//    }
//    for (int e = head[u], v; e; e = nxt[e]) {
//        v = to[e;
//        if (v != f) {
//            siz[u] += siz[v];
//            if (!son[u] || siz[son[u]] < siz[v]) {
//                son[u] = v;
//            }
//        }
//    }
//}
//
//void dfs2(int u, int t) {
//    top[u] = t;
//    dfn[u] = ++cntd;
//    seg[cntd] = u;
//    if (!son[u]) return;
//    dfs2(son[u], t;
//    for (int e = head[u], v; e; e = nxt[e]) {
//        v = to[e;
//        if (v != fa[u] && v != son[u]) {
//            dfs2(v, v;
//        }
//    }
//}
//
//void up(int i) {
//    sumv[i] = sumv[i << 1] + sumv[i << 1 | 1;
//    maxv[i] = max(maxv[i << 1], maxv[i << 1 | 1;
//}
//
//void build(int l, int r, int i) {
//    if (l == r) {
//        sumv[i] = maxv[i] = arr[seg[l]];
//    } else {
//        int mid = (l + r) / 2;
//        build(l, mid, i << 1;
//        build(mid + 1, r, i << 1 | 1;
//        up(i;
//    }
//}
//
//void update(int jobi, int jobv, int l, int r, int i) {
//    if (l == r) {
//        sumv[i] = maxv[i] = jobv;
//    } else {
//        int mid = (l + r) / 2;
//        if (jobi <= mid) {
//            update(jobi, jobv, l, mid, i << 1;
//        } else {
//            update(jobi, jobv, mid + 1, r, i << 1 | 1;
//        }
//        up(i;
//    }
//}
//
//int queryMax(int jobl, int jobr, int l, int r, int i) {
//    if (jobl <= l && r <= jobr) {
//        return maxv[i;
//    }
//    int mid = (l + r) / 2, ans = -INF;
//    if (jobl <= mid) {
//        ans = max(ans, queryMax(jobl, jobr, l, mid, i << 1;
//    }
//    if (jobr > mid) {
//        ans = max(ans, queryMax(jobl, jobr, mid + 1, r, i << 1 | 1;
//    }
//    return ans;
//}
//
//int querySum(int jobl, int jobr, int l, int r, int i) {
//    if (jobl <= l && r <= jobr) {
//        return sumv[i;
//    }
//    int mid = (l + r) / 2, ans = 0;
//    if (jobl <= mid) {
//        ans += querySum(jobl, jobr, l, mid, i << 1;
//    }
//    if (jobr > mid) {
//        ans += querySum(jobl, jobr, mid + 1, r, i << 1 | 1;
//    }
//    return ans;
//}
//
//void pointUpdate(int u, int v) {
//    update(dfn[u], v, 1, n, 1;
//}
//
//int pathMax(int x, int y) {
//    int ans = -INF;
//    while (top[x] != top[y]) {
//        if (dep[top[x]] <= dep[top[y]]) {
//            ans = max(ans, queryMax(dfn[top[y]], dfn[y], 1, n, 1;
//            y = fa[top[y]];
//        } else {
//            ans = max(ans, queryMax(dfn[top[x]], dfn[x], 1, n, 1;
//            x = fa[top[x]];
//        }
//    }
//    return max(ans, queryMax(min(dfn[x], dfn[y]), max(dfn[x], dfn[y]), 1, n, 1;
//}
//
//int pathSum(int x, int y) {
//    int ans = 0;
//    while (top[x] != top[y]) {
//        if (dep[top[x]] <= dep[top[y]]) {
//            ans += querySum(dfn[top[y]], dfn[y], 1, n, 1;
//            y = fa[top[y]];
//        } else {
//            ans += querySum(dfn[top[x]], dfn[x], 1, n, 1;
//            x = fa[top[x]];
//        }
//    }
//    return ans + querySum(min(dfn[x], dfn[y]), max(dfn[x], dfn[y]), 1, n, 1;
//}
//
//int main() {
//    ios::sync_with_stdio(false;
//    cin.tie(nullptr;
//    cin >> n;
//    for (int i = 1, u, v; i < n; i++) {
//        cin >> u >> v;
//        addEdge(u, v;
//        addEdge(v, u;
//    }
//    for (int i = 1; i <= n; i++) {
//        cin >> arr[i];
//    }
//    dfs1(1, 0;
//    dfs2(1, 1;
//    build(1, n, 1;
//    cin >> m;
//    string op;
//    int x, y;
//    while (m--) {
//        cin >> op >> x >> y;
//        if (op == "CHANGE") {
//            pointUpdate(x, y;
//        } else if(op == "QMAX") {
//            cout << pathMax(x, y) << '\n';
//        } else {
//            cout << pathSum(x, y) << '\n';
//        }
//    }
//    return 0;
//}