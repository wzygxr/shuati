#include <bits/stdc++.h>
using namespace std;

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
// 使用树链剖分将树上问题转化为线段树问题
// 1. 树链剖分：通过两次DFS将树划分为多条重链
// 2. 线段树：维护区间状态，支持区间覆盖
// 3. 路径操作：将树上路径操作转化为多个区间操作
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
// 2. 使用线段树维护每个区间的安装状态，支持区间覆盖操作
// 3. 对于安装操作：将从根节点到目标节点路径上的所有节点标记为已安装
// 4. 对于卸载操作：将目标节点的子树中所有节点标记为未安装
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
// 1. 洛谷P2146 [NOI2015]软件包管理器（本题）：https://www.luogu.com.cn/problem/P2146
// 2. 洛谷P3979 遥远的国度：https://www.luogu.com.cn/problem/P3979
// 3. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
// 4. HackerEarth Tree Queries：https://www.hackerearth.com/practice/data-structures/trees/binary-and-nary-trees/practice-problems/
//
// Java实现参考：Code04_PackageManager1.java
// Python实现参考：Code04_PackageManager1.py
// C++实现参考：Code04_PackageManager1.cpp（当前文件）

const int MAXN = 100001;
int n, m;

// 链式前向星存图
int head[MAXN], nxt[MAXN << 1], to[MAXN << 1], cntg = 0;

// 树链剖分相关数组
int fa[MAXN];     // 父节点
int dep[MAXN];    // 深度
int siz[MAXN];    // 子树大小
int son[MAXN];    // 重儿子
int top[MAXN];    // 所在重链的顶部节点
int dfn[MAXN];    // dfs序
int cntd = 0;     // dfs序计数器

// 线段树相关数组
int sum[MAXN << 2];      // 区间和
bool update[MAXN << 2];  // 懒标记
int change[MAXN << 2];   // 改变的值

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
}

// 线段树懒更新
void lazy(int i, int v, int n) {
    sum[i] = v * n;
    update[i] = true;
    change[i] = v;
}

// 线段树下传懒标记
void down(int i, int ln, int rn) {
    if (update[i]) {
        lazy(i << 1, change[i], ln);
        lazy(i << 1 | 1, change[i], rn);
        update[i] = false;
    }
}

// 区间更新
void updateRange(int jobl, int jobr, int jobv, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        lazy(i, jobv, r - l + 1);
        return;
    }
    int mid = (l + r) >> 1;
    down(i, mid - l + 1, r - mid);
    if (jobl <= mid) updateRange(jobl, jobr, jobv, l, mid, i << 1);
    if (jobr > mid) updateRange(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
    up(i);
}

// 区间查询
long long query(int jobl, int jobr, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        return sum[i];
    }
    int mid = (l + r) >> 1;
    down(i, mid - l + 1, r - mid);
    long long ans = 0;
    if (jobl <= mid) ans += query(jobl, jobr, l, mid, i << 1);
    if (jobr > mid) ans += query(jobl, jobr, mid + 1, r, i << 1 | 1);
    return ans;
}

// 路径更新：将从节点x到节点y的路径上所有节点值改为v
void pathUpdate(int x, int y, int v) {
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) swap(x, y);
        updateRange(dfn[top[x]], dfn[x], v, 1, n, 1);
        x = fa[top[x]];
    }
    if (dep[x] > dep[y]) swap(x, y);
    updateRange(dfn[x], dfn[y], v, 1, n, 1);
}

// 安装操作：安装软件包x
int install(int x) {
    int pre = sum[1];
    pathUpdate(1, x, 1);  // 从根节点1到x的路径上所有节点标记为已安装
    int post = sum[1];
    return abs(post - pre);
}

// 卸载操作：卸载软件包x
int uninstall(int x) {
    int pre = sum[1];
    updateRange(dfn[x], dfn[x] + siz[x] - 1, 0, 1, n, 1);  // x的子树中所有节点标记为未安装
    int post = sum[1];
    return abs(post - pre);
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    cin >> n;
    
    // 读取依赖关系（注意题目中的编号从0开始，但我们的实现从1开始）
    for (int u = 2, v; u <= n; u++) {
        cin >> v;
        v++;  // 转换为从1开始的编号
        addEdge(v, u);
        addEdge(u, v);
    }
    
    // 树链剖分
    dfs1(1, 0);
    dfs2(1, 1);
    
    cin >> m;
    
    // 处理操作
    string op;
    int x;
    for (int i = 1; i <= m; i++) {
        cin >> op >> x;
        x++;  // 转换为从1开始的编号
        if (op == "install") {
            cout << install(x) << '\n';
        } else {  // uninstall
            cout << uninstall(x) << '\n';
        }
    }
    
    return 0;
}