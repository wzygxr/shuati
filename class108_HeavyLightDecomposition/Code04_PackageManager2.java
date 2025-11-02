package class161;

// 软件包管理器，C++版
// 题目来源：洛谷P2146 [NOI2015]软件包管理器
// 题目链接：https://www.luogu.com.cn/problem/P2146
// 
// 题目描述：
// 一共有n个软件，编号0~n-1，0号软件不依赖任何软件，其他每个软件都仅依赖一个软件
// 依赖关系由数组形式给出，题目保证不会出现循环依赖
// 一开始所有软件都是没有安装的，如果a依赖b，那么安装a需要安装b，同时卸载b需要卸载a
// 一共有m条操作，每种操作是如下2种类型中的一种
// 操作 install x    : 安装x，如果x已经安装打印0，否则打印有多少个软件的状态需要改变
// 操作 uninstall x  : 卸载x，如果x没有安装打印0，否则打印有多少个软件的状态需要改变
// 1 <= n、m <= 10^6
//
// 解题思路：
// 这是一道经典的树链剖分应用题。我们可以将软件依赖关系看作一棵树，其中0号软件是根节点。
// 对于安装操作，我们需要将从根节点到目标节点路径上的所有节点都标记为已安装。
// 对于卸载操作，我们需要将以目标节点为根的子树中的所有节点都标记为未安装。
// 使用树链剖分配合线段树可以高效地完成这两种操作。
//
// 算法步骤：
// 1. 构建依赖关系树，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
// 2. 使用线段树维护节点安装状态，支持区间更新和区间查询
// 3. 对于安装操作：更新从根到目标节点路径上的所有节点为已安装
// 4. 对于卸载操作：更新以目标节点为根的子树为未安装
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
// 1. 洛谷P2146 [NOI2015]软件包管理器（本题）：https://www.luogu.com.cn/problem/P2146
// 2. 洛谷P3979 遥远的国度：https://www.luogu.com.cn/problem/P3979
// 3. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
// 4. HackerEarth Tree Queries：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/approximate/tree-query/
//
// Java实现参考：Code04_PackageManager1.java
// Python实现参考：Code_LuoguP2146_PackageManager.py
// C++实现参考：Code04_PackageManager2.java（当前文件）
//
// 提交记录：
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 100001;
//int n, m;
//
//int head[MAXN];
//int nxt[MAXN << 1];
//int to[MAXN << 1];
//int cntg = 0;
//
//int fa[MAXN];
//int dep[MAXN];
//int siz[MAXN];
//int son[MAXN];
//int top[MAXN];
//int dfn[MAXN];
//int cntd = 0;
//
//int sum[MAXN << 2];
//bool update[MAXN << 2];
//int change[MAXN << 2];
//
//void addEdge(int u, int v) {
//    nxt[++cntg] = head[u];
//    to[cntg] = v;
//    head[u] = cntg;
//}
//
//void dfs1(int u, int f) {
//    fa[u] = f;
//    dep[u] = dep[f] + 1;
//    siz[u] = 1;
//    for (int e = head[u], v; e > 0; e = nxt[e]) {
//        v = to[e];
//        if (v != f) dfs1(v, u);
//    }
//    for (int e = head[u], v; e > 0; e = nxt[e]) {
//        v = to[e];
//        if (v != f) {
//            siz[u] += siz[v];
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
//    if (son[u] == 0) return;
//    dfs2(son[u], t);
//    for (int e = head[u], v; e > 0; e = nxt[e]) {
//        v = to[e];
//        if (v != fa[u] && v != son[u]) {
//            dfs2(v, v);
//        }
//    }
//}
//
//void up(int i) {
//    sum[i] = sum[i << 1] + sum[i << 1 | 1];
//}
//
//void lazy(int i, int v, int n) {
//    sum[i] = v * n;
//    update[i] = true;
//    change[i] = v;
//}
//
//void down(int i, int ln, int rn) {
//    if (update[i]) {
//        lazy(i << 1, change[i], ln);
//        lazy(i << 1 | 1, change[i], rn);
//        update[i] = false;
//    }
//}
//
//void updateRange(int jobl, int jobr, int jobv, int l, int r, int i) {
//    if (jobl <= l && r <= jobr) {
//        lazy(i, jobv, r - l + 1);
//    } else {
//        int mid = (l + r) / 2;
//        down(i, mid - l + 1, r - mid);
//        if (jobl <= mid) updateRange(jobl, jobr, jobv, l, mid, i << 1);
//        if (jobr > mid) updateRange(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
//        up(i);
//    }
//}
//
//long long query(int jobl, int jobr, int l, int r, int i) {
//    if (jobl <= l && r <= jobr) return sum[i];
//    int mid = (l + r) / 2;
//    down(i, mid - l + 1, r - mid);
//    long long ans = 0;
//    if (jobl <= mid) ans += query(jobl, jobr, l, mid, i << 1);
//    if (jobr > mid) ans += query(jobl, jobr, mid + 1, r, i << 1 | 1);
//    return ans;
//}
//
//void pathUpdate(int x, int v) {
//    int y = 1;
//    while (top[x] != top[y]) {
//        if (dep[top[x]] <= dep[top[y]]) {
//            updateRange(dfn[top[y]], dfn[y], v, 1, n, 1);
//            y = fa[top[y]];
//        } else {
//            updateRange(dfn[top[x]], dfn[x], v, 1, n, 1);
//            x = fa[top[x]];
//        }
//    }
//    updateRange(min(dfn[x], dfn[y]), max(dfn[x], dfn[y]), v, 1, n, 1);
//}
//
//int install(int x) {
//    int pre = sum[1];
//    pathUpdate(x, 1);
//    int post = sum[1];
//    return abs(post - pre);
//}
//
//int uninstall(int x) {
//    int pre = sum[1];
//    updateRange(dfn[x], dfn[x] + siz[x] - 1, 0, 1, n, 1);
//    int post = sum[1];
//    return abs(post - pre);
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n;
//    for (int u = 2, v; u <= n; u++) {
//        cin >> v;
//        v++;
//        addEdge(v, u);
//    }
//    dfs1(1, 0);
//    dfs2(1, 1);
//    cin >> m;
//    string op;
//    int x;
//    for (int i = 1; i <= m; i++) {
//        cin >> op >> x;
//        x++;
//        if (op == "install") {
//            cout << install(x) << '\n';
//        } else {
//            cout << uninstall(x) << '\n';
//        }
//    }
//    return 0;
//}