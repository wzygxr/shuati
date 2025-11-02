package class161;

// 重链剖分模版题，C++版（Java注释版本）
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
// C++实现参考：Code01_HLD2.java（当前文件，注释版本）

//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 100001;
//int n, m, root, MOD;
//int arr[MAXN];
//
//int head[MAXN];
//int nxt[MAXN << 1];
//int to[MAXN << 1];
//int cntg = 0;
//
//int fa[MAXN];
//int dep[MAXN];
//int siz[MAXN;
//int son[MAXN];
//int top[MAXN;
//int dfn[MAXN;
//int seg[MAXN;
//int cntd = 0;
//
//long long sum[MAXN << 2;
//long long addTag[MAXN << 2;
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
//    for (int e = head[u], v; e > 0; e = nxt[e]) {
//        v = to[e;
//        if (v != f) {
//            dfs1(v, u;
//        }
//    }
//    for (int e = head[u], v; e > 0; e = nxt[e]) {
//        v = to[e;
//        if (v != f) {
//            siz[u] += siz[v;
//            if (son[u] == 0 || siz[son[u]] < siz[v]) {
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
//    if (son[u] == 0) {
//        return;
//    }
//    dfs2(son[u], t;
//    for (int e = head[u], v; e > 0; e = nxt[e]) {
//        v = to[e;
//        if (v != fa[u] && v != son[u]) {
//            dfs2(v, v;
//        }
//    }
//}
//
//void up(int i) {
//    sum[i] = (sum[i << 1] + sum[i << 1 | 1]) % MOD;
//}
//
//void lazy(int i, long long v, int n) {
//    sum[i] = (sum[i] + v * n) % MOD;
//    addTag[i] = (addTag[i] + v) % MOD;
//}
//
//void down(int i, int ln, int rn) {
//    if (addTag[i] != 0) {
//        lazy(i << 1, addTag[i], ln;
//        lazy(i << 1 | 1, addTag[i], rn;
//        addTag[i] = 0;
//    }
//}
//
//void build(int l, int r, int i) {
//    if (l == r) {
//        sum[i] = arr[seg[l]] % MOD;
//    } else {
//        int mid = (l + r) / 2;
//        build(l, mid, i << 1;
//        build(mid + 1, r, i << 1 | 1;
//        up(i;
//    }
//}
//
//void add(int jobl, int jobr, int jobv, int l, int r, int i) {
//    if (jobl <= l && r <= jobr) {
//        lazy(i, jobv, r - l + 1;
//    } else {
//        int mid = (l + r) / 2;
//        down(i, mid - l + 1, r - mid;
//        if (jobl <= mid) {
//            add(jobl, jobr, jobv, l, mid, i << 1;
//        }
//        if (jobr > mid) {
//            add(jobl, jobr, jobv, mid + 1, r, i << 1 | 1;
//        }
//        up(i;
//    }
//}
//
//long long query(int jobl, int jobr, int l, int r, int i) {
//    if (jobl <= l && r <= jobr) {
//        return sum[i;
//    }
//    int mid = (l + r) / 2;
//    down(i, mid - l + 1, r - mid;
//    long long ans = 0;
//    if (jobl <= mid) {
//        ans = (ans + query(jobl, jobr, l, mid, i << 1)) % MOD;
//    }
//    if (jobr > mid) {
//        ans = (ans + query(jobl, jobr, mid + 1, r, i << 1 | 1)) % MOD;
//    }
//    return ans;
//}
//
//void pathAdd(int x, int y, int v) {
//    while (top[x] != top[y]) {
//        if (dep[top[x]] <= dep[top[y]]) {
//            add(dfn[top[y]], dfn[y], v, 1, n, 1;
//            y = fa[top[y]];
//        } else {
//            add(dfn[top[x]], dfn[x], v, 1, n, 1;
//            x = fa[top[x]];
//        }
//    }
//    add(min(dfn[x], dfn[y]), max(dfn[x], dfn[y]), v, 1, n, 1;
//}
//
//void subtreeAdd(int x, int v) {
//    add(dfn[x], dfn[x] + siz[x] - 1, v, 1, n, 1;
//}
//
//long long pathSum(int x, int y) {
//    long long ans = 0;
//    while (top[x] != top[y]) {
//        if (dep[top[x]] <= dep[top[y]]) {
//            ans = (ans + query(dfn[top[y]], dfn[y], 1, n, 1)) % MOD;
//            y = fa[top[y]];
//        } else {
//            ans = (ans + query(dfn[top[x]], dfn[x], 1, n, 1)) % MOD;
//            x = fa[top[x]];
//        }
//    }
//    ans = (ans + query(min(dfn[x], dfn[y]), max(dfn[x], dfn[y]), 1, n, 1)) % MOD;
//    return ans;
//}
//
//long long subtreeSum(int x) {
//    return query(dfn[x], dfn[x] + siz[x] - 1, 1, n, 1;
//}
//
//int main() {
//    ios::sync_with_stdio(false;
//    cin.tie(nullptr;
//    cin >> n >> m >> root >> MOD;
//    for (int i = 1; i <= n; i++) {
//        cin >> arr[i;
//    }
//    for (int i = 1, u, v; i < n; i++) {
//        cin >> u >> v;
//        addEdge(u, v;
//        addEdge(v, u;
//    }
//    dfs1(root, 0;
//    dfs2(root, root;
//    build(1, n, 1;
//    for (int i = 1, op, x, y, v; i <= m; i++) {
//        cin >> op;
//        if (op == 1) {
//            cin >> x >> y >> v;
//            pathAdd(x, y, v;
//        } else if (op == 2) {
//            cin >> x >> y;
//            cout << pathSum(x, y) << "\n";
//        } else if (op == 3) {
//            cin >> x >> v;
//            subtreeAdd(x, v;
//        } else {
//            cin >> x;
//            cout << subtreeSum(x) << "\n";
//        }
//    }
//    return 0;
//}