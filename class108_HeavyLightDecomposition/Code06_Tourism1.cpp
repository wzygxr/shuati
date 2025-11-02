#include <bits/stdc++.h>
using namespace std;

// 旅游，C++版
// 题目来源：洛谷P3976 [AHOI2015]旅游
// 题目链接：https://www.luogu.com.cn/problem/P3976
//
// 题目描述：
// 一共有n个城市，给定n-1条边，城市连成一棵树，每个城市给定初始的宝石价格
// 一共有m条操作，操作类型如下
// 操作 x y v : 城市x到城市y的最短路途中，你可以选择一城买入宝石
//              继续行进的过程中，再选一城卖出宝石，以此获得利润
//              打印你能获得的最大利润，如果为负数，打印0
//              当你结束旅途后，沿途所有城市的宝石价格增加v
// 1 <= n、m <= 5 * 10^4
// 0 <= 任何时候的宝石价格 <= 10^9
//
// 解题思路：
// 这是一道较为复杂的树链剖分应用题，需要处理树上路径的最大利润查询和区间增值操作。
// 树上路径操作可以通过树链剖分转化为区间操作，再结合线段树进行高效处理。
// 关键在于线段树节点需要维护区间最大值、最小值以及左右方向的最大利润，并正确处理区间合并时的边界情况。
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
// 2. 使用线段树维护每个区间的宝石价格信息：
//    - 区间最大值
//    - 区间最小值
//    - 从左到右的最大利润（买入在左，卖出在右）
//    - 从右到左的最大利润（买入在右，卖出在左）
//    - 懒标记（价格增值）
// 3. 对于每次操作：
//    - 查询路径上买入卖出的最大利润
//    - 更新路径上所有节点的价格
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
// 1. 洛谷P3976 [AHOI2015]旅游（本题）：https://www.luogu.com.cn/problem/P3976
// 2. 洛谷P2486 [SDOI2011]染色：https://www.luogu.com.cn/problem/P2486
// 3. 洛谷P2146 [NOI2015]软件包管理器：https://www.luogu.com.cn/problem/P2146
// 4. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
// 5. HackerEarth Tree Query：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/algorithm/tree-query/
//
// Java实现参考：Code06_Tourism1.java
// Python实现参考：暂无
// C++实现参考：Code06_Tourism1.cpp（当前文件）

const int MAXN = 50001;
const int INF = 1000000001;
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
int maxv[MAXN << 2];    // 区间最大值
int minv[MAXN << 2];    // 区间最小值
int lprofit[MAXN << 2]; // 从左到右的最大利润
int rprofit[MAXN << 2]; // 从右到左的最大利润
int addTag[MAXN << 2];  // 懒标记

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
    int l = i << 1, r = i << 1 | 1;
    maxv[i] = max(maxv[l], maxv[r]);
    minv[i] = min(minv[l], minv[r]);
    lprofit[i] = max({lprofit[l], lprofit[r], maxv[r] - minv[l]});
    rprofit[i] = max({rprofit[l], rprofit[r], maxv[l] - minv[r]});
}

// 线段树懒更新
void lazy(int i, int v) {
    maxv[i] += v;
    minv[i] += v;
    addTag[i] += v;
}

// 线段树下传懒标记
void down(int i) {
    if (addTag[i] != 0) {
        lazy(i << 1, addTag[i]);
        lazy(i << 1 | 1, addTag[i]);
        addTag[i] = 0;
    }
}

// 构建线段树
void build(int l, int r, int i) {
    if (l == r) {
        maxv[i] = minv[i] = arr[seg[l]];
        return;
    }
    int mid = (l + r) >> 1;
    build(l, mid, i << 1);
    build(mid + 1, r, i << 1 | 1);
    up(i);
}

// 区间增值
void add(int jobl, int jobr, int jobv, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        lazy(i, jobv);
        return;
    }
    down(i);
    int mid = (l + r) >> 1;
    if (jobl <= mid) {
        add(jobl, jobr, jobv, l, mid, i << 1);
    }
    if (jobr > mid) {
        add(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
    }
    up(i);
}

// 合并两个区间的利润信息
void merge(int ans[], int rmax, int rmin, int rlpro, int rrpro) {
    int lmax = ans[0];
    int lmin = ans[1];
    int llpro = ans[2];
    int lrpro = ans[3];
    ans[0] = max(lmax, rmax);
    ans[1] = min(lmin, rmin);
    ans[2] = max({llpro, rlpro, rmax - lmin});
    ans[3] = max({lrpro, rrpro, lmax - rmin});
}

// 区间查询
void query(int ans[], int jobl, int jobr, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        merge(ans, maxv[i], minv[i], lprofit[i], rprofit[i]);
        return;
    }
    down(i);
    int mid = (l + r) >> 1;
    if (jobl <= mid) {
        query(ans, jobl, jobr, l, mid, i << 1);
    }
    if (jobr > mid) {
        query(ans, jobl, jobr, mid + 1, r, i << 1 | 1);
    }
}

// 区间查询接口
void query(int ans[], int jobl, int jobr) {
    ans[0] = -INF;
    ans[1] = INF;
    ans[2] = 0;
    ans[3] = 0;
    query(ans, jobl, jobr, 1, n, 1);
}

// 复制数组
void clone(int* a, int* b) {
    a[0] = b[0];
    a[1] = b[1];
    a[2] = b[2];
    a[3] = b[3];
}

// 计算从x到y路径上的最大利润，并将路径上所有节点的价格增加v
int compute(int x, int y, int v) {
    int tmpx = x, tmpy = y;
    int xpath[4] = {-INF, INF, 0, 0};
    int ypath[4] = {-INF, INF, 0, 0};
    int cur[4];
    
    // 查询路径上的最大利润
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) swap(x, y);
        if (dep[top[x]] >= dep[top[y]]) {
            query(cur, dfn[top[x]], dfn[x]);
            merge(cur, xpath[0], xpath[1], xpath[2], xpath[3]);
            clone(xpath, cur);
            x = fa[top[x]];
        } else {
            query(cur, dfn[top[y]], dfn[y]);
            merge(cur, ypath[0], ypath[1], ypath[2], ypath[3]);
            clone(ypath, cur);
            y = fa[top[y]];
        }
    }
    
    if (dep[x] <= dep[y]) {
        query(cur, dfn[x], dfn[y]);
        merge(cur, ypath[0], ypath[1], ypath[2], ypath[3]);
        clone(ypath, cur);
    } else {
        query(cur, dfn[y], dfn[x]);
        merge(cur, xpath[0], xpath[1], xpath[2], xpath[3]);
        clone(xpath, cur);
    }
    
    int ans = max({xpath[3], ypath[2], ypath[0] - xpath[1]});
    
    // 更新路径上所有节点的价格
    x = tmpx;
    y = tmpy;
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) swap(x, y);
        add(dfn[top[x]], dfn[x], v, 1, n, 1);
        x = fa[top[x]];
    }
    add(min(dfn[x], dfn[y]), max(dfn[x], dfn[y]), v, 1, n, 1);
    
    return ans;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    cin >> n;
    
    // 读取节点初始宝石价格
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
    
    cin >> m;
    
    // 处理操作
    for (int i = 1, x, y, v; i <= m; i++) {
        cin >> x >> y >> v;
        cout << compute(x, y, v) << "\n";
    }
    
    return 0;
}