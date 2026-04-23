package class195;

// 监狱，C++版
// 一共有n个房间，给定n-1条无向边，所有房间组成一棵树
// 一共有m个囚犯，每个囚犯给出卧室房间号和工作室房间号，囚犯初始时都在卧室
// 每个囚犯的卧室和工作室一定不同，任何两个囚犯既不共用卧室，也不共用工作室
// 但是有可能某个房间，作为一个囚犯的卧室，同时作为另一个囚犯的工作室
// 你的任务是让所有囚犯从自己的卧室出发，只走最短路去自己的工作室
// 你可以随意下达指令，每条指令只能选择一个囚犯，沿一条边移动一步
// 任何时刻不能让任何房间里出现两个囚犯，打印你能否完成任务
// 1 <= n、m <= 1.2 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P9520
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// ===================== 监狱问题核心知识点（C++版） =====================
// 【问题分析】
// 本题需要判断是否存在一种调度方案，使得所有囚犯能够同时从卧室移动到工作室
// 且不发生冲突（同一时刻同一房间不能有多个囚犯）
//
// 【约束建模】
// 将每个囚犯建模为一个节点，需要建立囚犯之间的相对顺序约束
// 如果两个囚犯的路径有交集，则需要确定他们的先后顺序
//
// 【倍增优化建图】
// 使用倍增表来高效处理路径上的约束关系
// stout[u][p]: 出表节点
// stin[u][p]: 入表节点
//
// 【拓扑排序判环】
// 将所有约束转化为有向边后，用拓扑排序检测环
// 无环则说明存在合法调度方案
//
// 【ML/DL关联价值】
// 1. 多智能体路径规划
// 2. 约束满足问题(CSP)
// 3. 冲突检测与消解

#include <bits/stdc++.h>

using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 120001;
const int MAXT = MAXN * 50;
const int MAXE = MAXN * 200;
const int MAXP = 18;
int t, n, m;

// ===================== 标签编号区 =====================
int startTag[MAXN];
int endTag[MAXN];

// ===================== 原始树存储区 =====================
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
int stjump[MAXN][MAXP];
int cntd;

// ===================== 倍增优化建图变量区 =====================
int stout[MAXN][MAXP];
int stin[MAXN][MAXP];
int cntt;

// ===================== 拓扑排序变量区 =====================
int que[MAXT];

// ===================== 核心函数：原始树加边 =====================
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

// ===================== 核心函数：构建倍增表 =====================
void build(int u, int fa) {
    dep[u] = dep[fa] + 1;
    dfn[u] = ++cntd;
    siz[u] = 1;
    stjump[u][0] = fa;
    // 构建出表节点
    stout[u][0] = ++cntt;
    addEdge2(startTag[u], cntt);
    addEdge2(startTag[fa], cntt);
    // 构建入表节点
    stin[u][0] = ++cntt;
    addEdge2(cntt, endTag[u]);
    addEdge2(cntt, endTag[fa]);
    // 构建更高层
    for (int p = 1; p < MAXP; p++) {
        stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
        stout[u][p] = ++cntt;
        addEdge2(stout[u][p - 1], cntt);
        addEdge2(stout[stjump[u][p - 1]][p - 1], cntt);
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
int nearest(int x, int y) {
    if (isAncestor(y, x)) {
        return kthAncestor(x, dep[x] - dep[y] - 1);
    } else {
        return stjump[y][0];
    }
}

// ===================== 核心函数：路径约束设置 =====================
void pathSet(int x, int y, int prisoner) {
    if (dep[x] < dep[y]) {
        swap(x, y);
    }
    addEdge2(startTag[y], prisoner);
    addEdge2(prisoner, endTag[y]);
    // 利用倍增表将x向上跳到与y同深度
    for (int p = MAXP - 1; p >= 0; p--) {
        if (dep[stjump[x][p]] >= dep[y]) {
            addEdge2(stout[x][p], prisoner);
            addEdge2(prisoner, stin[x][p]);
            x = stjump[x][p];
        }
    }
    if (x == y) {
        return;
    }
    // x和y同时向上跳
    for (int p = MAXP - 1; p >= 0; p--) {
        if (stjump[x][p] != stjump[y][p]) {
            addEdge2(stout[x][p], prisoner);
            addEdge2(stout[y][p], prisoner);
            addEdge2(prisoner, stin[x][p]);
            addEdge2(prisoner, stin[y][p]);
            x = stjump[x][p];
            y = stjump[y][p];
        }
    }
    addEdge2(stout[x][0], prisoner);
    addEdge2(prisoner, stin[x][0]);
}

// ===================== 核心函数：建立囚犯约束 =====================
void link(int x, int y) {
    // 创建囚犯节点
    int prisoner = ++cntt;
    // 囚犯与卧室、工作室的约束
    addEdge2(prisoner, startTag[x]);
    addEdge2(prisoner, endTag[x]);
    addEdge2(startTag[y], prisoner);
    addEdge2(endTag[y], prisoner);
    // 如果x和y不是父子关系，需要处理路径上的约束
    if (stjump[x][0] != y && stjump[y][0] != x) {
        int a = nearest(y, x);
        int b = nearest(x, y);
        pathSet(a, b, prisoner);
    }
}

// ===================== 核心函数：拓扑排序判环 =====================
bool topo() {
    int qi = 1, qsiz = 0;
    // 将所有入度为0的节点入队
    for (int i = 1; i <= cntt; i++) {
        if (indegree[i] == 0) {
            que[++qsiz] = i;
        }
    }
    // 拓扑排序主循环
    while (qi <= qsiz) {
        int u = que[qi++];
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

// ===================== 核心函数：清空数据 =====================
void clear() {
    for (int i = 1; i <= n; i++) {
        head1[i] = 0;
    }
    for (int i = 1; i <= cntt; i++) {
        head2[i] = indegree[i] = 0;
    }
    cnt1 = cnt2 = cntt = cntd = 0;
    dep[1] = 0;
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    // 读入测试用例数
    cin >> t;
    for (int c = 1; c <= t; c++) {
        // 读入n
        cin >> n;
        cntt = n << 1;
        // 初始化标签
        for (int i = 1; i <= n; i++) {
            startTag[i] = i;
            endTag[i] = i + n;
        }
        // 读入树的边
        for (int i = 1, u, v; i < n; i++) {
            cin >> u >> v;
            addEdge1(u, v);
            addEdge1(v, u);
        }
        // 构建倍增表
        build(1, 1);
        // 读入m个囚犯
        cin >> m;
        for (int i = 1, x, y; i <= m; i++) {
            cin >> x >> y;
            link(x, y);
        }
        // 拓扑排序判断是否存在合法方案
        bool ans = topo();
        cout << (ans ? "Yes" : "No") << "\n";
        clear();
    }
    return 0;
}
