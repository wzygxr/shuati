package class195;

// 美丽的树，倍增优化建图，C++版
// 一共n个节点，给定n-1条无向边，所有节点组成一棵树，1号节点是根
// 你需要给每个节点赋值，但是不能破坏如下的m条关系，关系的格式如下
// 关系 1 a b c : 节点a到节点b的路径上，值最小的节点必须是节点c，输入保证c一定在路径上
// 关系 2 a b c : 节点a到节点b的路径上，值最大的节点必须是节点c，输入保证c一定在路径上
// 如果存在赋值方案，并且这些值是1到n的一个排列，打印一种方案即可，否则打印-1
// 2 <= n、m <= 2 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/CF1904F
// 测试链接 : https://codeforces.com/problemset/problem/1904/F
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// ===================== 倍增+区间表优化建图核心知识点（C++版） =====================
// 【问题分析】
// 本题需要在树上给节点赋值，满足路径上的最值约束
// 与树剖版本不同，这里使用倍增+区间表来优化建图
//
// 【倍增算法】
// 预处理每个节点的2^k级祖先，支持O(logn)的LCA查询
// 倍增表可以将任意路径拆分为O(logn)个区间
//
// 【区间表优化建图】
// 类似线段树，但使用倍增表来组织区间
// stout[u][p]: 表示从u向上2^p层的出表节点
// stin[u][p]: 表示从u向上2^p层的入表节点
//
// 【路径处理】
// 利用倍增表将路径拆分为若干区间，然后建立约束边
//
// 【ML/DL关联价值】
// 1. 层次化表示学习
// 2. 树结构数据的快速检索

#include <bits/stdc++.h>

using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 200001;
const int MAXT = MAXN * 41;
const int MAXE = MAXN * 201;
const int MAXP = 17;

// ===================== 输入变量区 =====================
int n, m;

// ===================== 原树存储区 =====================
int head1[MAXN];
int next1[MAXN << 1];
int to1[MAXN << 1];
int cnt1;

// ===================== 关系图存储区 =====================
int indegree[MAXT];
int head2[MAXT];
int next2[MAXE];
int to2[MAXE];
int cnt2;

// ===================== 树上倍增变量区 =====================
int dep[MAXN];
int dfn[MAXN];
int siz[MAXN];
// stjump[u][p]: 节点u的2^p级祖先
int stjump[MAXN][MAXP];
int cntd;

// ===================== 倍增优化建图变量区 =====================
// stout[u][p]: 出表节点，用于区间→单点连边
int stout[MAXN][MAXP];
// stin[u][p]: 入表节点，用于单点→区间连边
int stin[MAXN][MAXP];
int cntt;

// ===================== 拓扑排序与答案区 =====================
int que[MAXT];
int ans[MAXN];

// ===================== 核心函数：原树加边 =====================
void addEdge1(int u, int v) {
    next1[++cnt1] = head1[u];
    to1[cnt1] = v;
    head1[u] = cnt1;
}

// ===================== 核心函数：关系图加边 =====================
void addEdge2(int u, int v) {
    indegree[v]++;
    next2[++cnt2] = head2[u];
    to2[cnt2] = v;
    head2[u] = cnt2;
}

// ===================== 核心函数：构建倍增表和区间表 =====================
void build(int u, int fa) {
    dep[u] = dep[fa] + 1;
    dfn[u] = ++cntd;
    siz[u] = 1;
    // 初始化2^0级祖先
    stjump[u][0] = fa;
    // 构建出表节点
    stout[u][0] = ++cntt;
    addEdge2(u, cntt);
    addEdge2(fa, cntt);
    // 构建入表节点
    stin[u][0] = ++cntt;
    addEdge2(cntt, u);
    addEdge2(cntt, fa);
    // 构建更高层的倍增表
    for (int p = 1; p < MAXP; p++) {
        // 计算2^p级祖先
        stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
        // 构建出表节点
        stout[u][p] = ++cntt;
        addEdge2(stout[u][p - 1], cntt);
        addEdge2(stout[stjump[u][p - 1]][p - 1], cntt);
        // 构建入表节点
        stin[u][p] = ++cntt;
        addEdge2(cntt, stin[u][p - 1]);
        addEdge2(cntt, stin[stjump[u][p - 1]][p - 1]);
    }
    // 递归处理子节点
    for (int e = head1[u]; e > 0; e = next1[e]) {
        int v = to1[e];
        if (v != fa) {
            build(v, u);
            siz[u] += siz[v];
        }
    }
}

// ===================== 核心函数：判断祖先关系 =====================
bool isAncestor(int a, int b) {
    return dfn[a] <= dfn[b] && dfn[b] < dfn[a] + siz[a];
}

// ===================== 核心函数：求k级祖先 =====================
int kthAncestor(int x, int k) {
    for (int p = 0; p < MAXP; p++) {
        if (((k >> p) & 1) != 0) {
            x = stjump[x][p];
        }
    }
    return x;
}

// ===================== 核心函数：求最近公共祖先方向 =====================
// 返回从y走向x的下一个节点
int nearest(int x, int y) {
    if (isAncestor(y, x)) {
        // y是x的祖先，返回x的(dep[x]-dep[y]-1)级祖先
        return kthAncestor(x, dep[x] - dep[y] - 1);
    } else {
        // y不是x的祖先，返回y的父节点
        return stjump[y][0];
    }
}

// ===================== 核心函数：路径出表连边 =====================
// 建立从路径x到y上所有节点（除y外）到c的边
void pathOut(int x, int y, int c) {
    if (dep[x] < dep[y]) {
        swap(x, y);
    }
    // y直接连边
    addEdge2(y, c);
    // 利用倍增表将x向上跳到与y同深度
    for (int p = MAXP - 1; p >= 0; p--) {
        if (dep[stjump[x][p]] >= dep[y]) {
            addEdge2(stout[x][p], c);
            x = stjump[x][p];
        }
    }
    if (x == y) {
        return;
    }
    // x和y同时向上跳
    for (int p = MAXP - 1; p >= 0; p--) {
        if (stjump[x][p] != stjump[y][p]) {
            addEdge2(stout[x][p], c);
            addEdge2(stout[y][p], c);
            x = stjump[x][p];
            y = stjump[y][p];
        }
    }
    // 最后一步
    addEdge2(stout[x][0], c);
}

// ===================== 核心函数：路径入表连边 =====================
// 建立从c到路径x到y上所有节点（除y外）的边
void pathIn(int x, int y, int c) {
    if (dep[x] < dep[y]) {
        swap(x, y);
    }
    // y直接连边
    addEdge2(c, y);
    // 利用倍增表将x向上跳到与y同深度
    for (int p = MAXP - 1; p >= 0; p--) {
        if (dep[stjump[x][p]] >= dep[y]) {
            addEdge2(c, stin[x][p]);
            x = stjump[x][p];
        }
    }
    if (x == y) {
        return;
    }
    // x和y同时向上跳
    for (int p = MAXP - 1; p >= 0; p--) {
        if (stjump[x][p] != stjump[y][p]) {
            addEdge2(c, stin[x][p]);
            addEdge2(c, stin[y][p]);
            x = stjump[x][p];
            y = stjump[y][p];
        }
    }
    // 最后一步
    addEdge2(c, stin[x][0]);
}

// ===================== 核心函数：路径最小值约束 =====================
void pathMin(int a, int b, int c) {
    if (a != c) {
        pathIn(a, nearest(a, c), c);
    }
    if (b != c) {
        pathIn(b, nearest(b, c), c);
    }
}

// ===================== 核心函数：路径最大值约束 =====================
void pathMax(int a, int b, int c) {
    if (a != c) {
        pathOut(a, nearest(a, c), c);
    }
    if (b != c) {
        pathOut(b, nearest(b, c), c);
    }
}

// ===================== 核心函数：拓扑排序 =====================
bool topo() {
    int qi = 1, qsiz = 0;
    // 将所有入度为0的节点入队
    for (int i = 1; i <= cntt; i++) {
        if (indegree[i] == 0) {
            que[++qsiz] = i;
        }
    }
    int val = 0;
    // 拓扑排序主循环
    while (qi <= qsiz) {
        int u = que[qi++];
        // 如果是原始节点，赋予当前值
        if (u <= n) {
            ans[u] = ++val;
        }
        // 遍历所有邻接边
        for (int e = head2[u]; e > 0; e = next2[e]) {
            int v = to2[e];
            if (--indegree[v] == 0) {
                que[++qsiz] = v;
            }
        }
    }
    // 如果所有节点都被访问，说明无环
    return qsiz == cntt;
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    // 读入n和m
    cin >> n >> m;
    cntt = n;
    // 读入树的边
    for (int i = 1, u, v; i < n; i++) {
        cin >> u >> v;
        addEdge1(u, v);
        addEdge1(v, u);
    }
    // 构建倍增表和区间表
    build(1, 1);
    // 处理m条关系
    for (int i = 1, op, a, b, c; i <= m; i++) {
        cin >> op >> a >> b >> c;
        if (op == 1) {
            pathMin(a, b, c);
        } else {
            pathMax(a, b, c);
        }
    }
    // 拓扑排序判断并计算答案
    bool check = topo();
    if (check) {
        for (int i = 1; i <= n; i++) {
            cout << ans[i] << " ";
        }
    } else {
        cout << -1 << "\n";
    }
    return 0;
}
