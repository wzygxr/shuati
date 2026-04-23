package class195;

// 美丽的树，树剖优化建图，C++版
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

// ===================== 树链剖分+线段树优化建图核心知识点（C++版） =====================
// 【问题分析】
// 本题需要在树上给节点赋值，满足路径上的最值约束
// 约束类型：路径上最小/最大值必须是特定节点
//
// 【树链剖分(HLD)】
// 将树分解为若干条重链，将路径查询转化为O(logn)个区间
// 配合线段树，可以高效处理路径上的区间操作
//
// 【线段树优化建图】
// 对于"路径上除c外所有节点值 > c"的约束
// 需要建立c到路径上其他所有节点的边
// 使用线段树优化，将O(n)条边优化到O(logn)条
//
// 【拓扑排序判环】
// 将所有约束转化为有向边后，用拓扑排序检测环
// 无环则说明存在合法赋值方案
//
// 【ML/DL关联价值】
// 1. 树结构数据的层次化处理
// 2. 约束传播与一致性检验
// 3. 图神经网络中的树形结构建模

#include <bits/stdc++.h>

using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 200001;
const int MAXT = MAXN * 10;
const int MAXE = MAXN * 50;

// ===================== 输入变量区 =====================
int n, m;

// ===================== 原始树的存储区 =====================
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

// ===================== 线段树优化建图核心变量区 =====================
int ls[MAXT];
int rs[MAXT];
int rootOut, rootIn;
int cntt;

// ===================== 树链剖分变量区 =====================
int fa[MAXN];
int dep[MAXN];
int siz[MAXN];
int son[MAXN];
int top[MAXN];
int dfn[MAXN];
int seg[MAXN];
int cntd;

// ===================== 拓扑排序与答案区 =====================
int que[MAXT];
int ans[MAXN];

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

// ===================== 核心函数：构建出树 =====================
int buildOut(int l, int r) {
    int rt;
    if (l == r) {
        rt = l;
    } else {
        rt = ++cntt;
        int mid = (l + r) >> 1;
        ls[rt] = buildOut(l, mid);
        rs[rt] = buildOut(mid + 1, r);
        // 子节点向父节点连边
        addEdge2(ls[rt], rt);
        addEdge2(rs[rt], rt);
    }
    return rt;
}

// ===================== 核心函数：构建入树 =====================
int buildIn(int l, int r) {
    int rt;
    if (l == r) {
        rt = l;
    } else {
        rt = ++cntt;
        int mid = (l + r) >> 1;
        ls[rt] = buildIn(l, mid);
        rs[rt] = buildIn(mid + 1, r);
        // 父节点向子节点连边
        addEdge2(rt, ls[rt]);
        addEdge2(rt, rs[rt]);
    }
    return rt;
}

// ===================== 核心函数：单点→区间连边 =====================
void xToRange(int jobx, int jobl, int jobr, int l, int r, int i) {
    if (jobl > jobr) {
        return;
    }
    if (jobl <= l && r <= jobr) {
        addEdge2(jobx, i);
    } else {
        int mid = (l + r) >> 1;
        if (jobl <= mid) {
            xToRange(jobx, jobl, jobr, l, mid, ls[i]);
        }
        if (jobr > mid) {
            xToRange(jobx, jobl, jobr, mid + 1, r, rs[i]);
        }
    }
}

// ===================== 核心函数：区间→单点连边 =====================
void rangeToX(int jobl, int jobr, int jobx, int l, int r, int i) {
    if (jobl > jobr) {
        return;
    }
    if (jobl <= l && r <= jobr) {
        addEdge2(i, jobx);
    } else {
        int mid = (l + r) >> 1;
        if (jobl <= mid) {
            rangeToX(jobl, jobr, jobx, l, mid, ls[i]);
        }
        if (jobr > mid) {
            rangeToX(jobl, jobr, jobx, mid + 1, r, rs[i]);
        }
    }
}

// ===================== 核心函数：树链剖分第一次DFS =====================
void dfs1(int u, int f) {
    fa[u] = f;
    dep[u] = dep[f] + 1;
    siz[u] = 1;
    for (int e = head1[u], v; e > 0; e = next1[e]) {
        v = to1[e];
        if (v != f) {
            dfs1(v, u);
            siz[u] += siz[v];
            // 更新重儿子
            if (son[u] == 0 || siz[son[u]] < siz[v]) {
                son[u] = v;
            }
        }
    }
}

// ===================== 核心函数：树链剖分第二次DFS =====================
void dfs2(int u, int t) {
    top[u] = t;
    dfn[u] = ++cntd;
    seg[cntd] = u;
    // 叶子节点直接返回
    if (son[u] == 0) {
        return;
    }
    // 先遍历重儿子
    dfs2(son[u], t);
    // 再遍历轻儿子
    for (int e = head1[u], v; e > 0; e = next1[e]) {
        v = to1[e];
        if (v != fa[u] && v != son[u]) {
            dfs2(v, v);
        }
    }
}

// ===================== 核心函数：路径约束设置 =====================
void pathSet(int op, int x, int y, int z) {
    if (op == 1) {
        // c是最小值，c要小于路径上其他所有节点
        if (x <= z && z <= y) {
            xToRange(z, x, z - 1, 1, n, rootIn);
            xToRange(z, z + 1, y, 1, n, rootIn);
        } else {
            xToRange(z, x, y, 1, n, rootIn);
        }
    } else {
        // c是最大值，c要大于路径上其他所有节点
        if (x <= z && z <= y) {
            rangeToX(x, z - 1, z, 1, n, rootOut);
            rangeToX(z + 1, y, z, 1, n, rootOut);
        } else {
            rangeToX(x, y, z, 1, n, rootOut);
        }
    }
}

// ===================== 核心函数：树上路径处理 =====================
void link(int op, int a, int b, int c) {
    // 将路径拆分为若干条重链区间
    while (top[a] != top[b]) {
        // 每次处理深度较大的重链
        if (dep[top[a]] < dep[top[b]]) {
            swap(a, b);
        }
        // 处理从top[a]到a的重链区间
        pathSet(op, dfn[top[a]], dfn[a], dfn[c]);
        a = fa[top[a]];
    }
    // 处理最后一段在同一重链上的路径
    pathSet(op, min(dfn[a], dfn[b]), max(dfn[a], dfn[b]), dfn[c]);
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
            ans[seg[u]] = ++val;
        }
        // 遍历所有邻接边
        for (int e = head2[u]; e > 0; e = next2[e]) {
            int v = to2[e];
            if (--indegree[v] == 0) {
                que[++qsiz] = v;
            }
        }
    }
    // 如果所有节点都被访问，说明无环，存在合法方案
    return qsiz == cntt;
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    // 读入n和m
    cin >> n >> m;
    cntt = n;
    // 构建线段树
    rootOut = buildOut(1, n);
    rootIn = buildIn(1, n);
    // 读入树的边
    for (int i = 1, u, v; i < n; i++) {
        cin >> u >> v;
        addEdge1(u, v);
        addEdge1(v, u);
    }
    // 树链剖分
    dfs1(1, 0);
    dfs2(1, 1);
    // 处理m条关系
    for (int i = 1, op, a, b, c; i <= m; i++) {
        cin >> op >> a >> b >> c;
        link(op, a, b, c);
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
