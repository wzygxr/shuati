package class195;

// 炸弹，C++版
// 一共有n个炸弹，所有炸弹排成一条直线，给定每个炸弹的坐标xi、爆炸半径ri
// 炸弹A引爆时，如果炸弹B在其影响范围里，那么炸弹B也会引爆，进而引发一连串的爆炸
// 炸弹i如果作为初始引爆的炸弹，最终会引爆多少个炸弹记为query(i)
// 计算i = 1 2 .. n时，i * query(i)的累加和，答案对 1000000007 取余
// 1 <= n <= 5 * 10^5
// -(10^18) <= xi <= +(10^18)，题目依次输入的坐标保证严格递增
// 0 <= ri <= 2 * 10^18
// 测试链接 : https://www.luogu.com.cn/problem/P5025
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// ===================== Tarjan强连通分量+线段树优化建图核心知识点（C++版） =====================
// 【问题分析】
// 本题需要计算每个炸弹作为起点时，最终能引爆多少个炸弹
// 炸弹之间的引爆关系形成有向图，需要处理强连通分量(SCC)
//
// 【线段树优化建图】
// 每个炸弹i可以引爆坐标在[xi-ri, xi+ri]范围内的所有炸弹
// 如果直接连边，边数为O(n^2)，需要线段树优化到O(nlogn)
//
// 【Tarjan算法】
// 求强连通分量，将图缩点形成DAG
// 在DAG上可以进行动态规划，计算每个SCC能到达的范围
//
// 【ML/DL关联价值】
// 1. 图神经网络中的社区发现(对应SCC检测)
// 2. 大规模图数据的压缩和降维(对应缩点)

#include <bits/stdc++.h>

using namespace std;

// 使用long long别名
using ll = long long;

// ===================== 常量定义区 =====================
const int MAXN = 500001;
const int MAXT = MAXN * 5;
const int MAXE = MAXN * 20;
const int INF = 1 << 30;
const int MOD = 1000000007;

// ===================== 输入变量区 =====================
int n;
ll location[MAXN];
ll radius[MAXN];

// ===================== 边存储区 =====================
int a[MAXE];
int b[MAXE];
int cnte;

// ===================== 链式前向星存图区 =====================
int head[MAXT];
int nxt[MAXE];
int to[MAXE];
int cntg;

// ===================== 线段树优化建图核心变量区 =====================
int rangel[MAXT];
int ranger[MAXT];
int ls[MAXT];
int rs[MAXT];
int root;
int cntt;

// ===================== Tarjan算法变量区 =====================
int dfn[MAXT];
int low[MAXT];
int cntd;
int sta[MAXT];
int top;

// ===================== 强连通分量变量区 =====================
int belong[MAXT];
int mostl[MAXT];
int mostr[MAXT];
int sccCnt;

// ===================== 核心函数：链式前向星加边 =====================
void addEdge(int u, int v) {
    nxt[++cntg] = head[u];
    to[cntg] = v;
    head[u] = cntg;
}

// ===================== 核心函数：保存边（用于缩点） =====================
void saveEdge(int u, int v) {
    a[++cnte] = u;
    b[cnte] = v;
}

// ===================== 核心函数：二分查找 =====================
int lower(ll num) {
    int l = 1, r = n, mid, ans = n + 1;
    while (l <= r) {
        mid = (l + r) >> 1;
        if (location[mid] >= num) {
            ans = mid;
            r = mid - 1;
        } else {
            l = mid + 1;
        }
    }
    return ans;
}

// ===================== 核心函数：构建线段树 =====================
int build(int l, int r) {
    int rt;
    if (l == r) {
        rt = l;
    } else {
        rt = ++cntt;
        int mid = (l + r) >> 1;
        ls[rt] = build(l, mid);
        rs[rt] = build(mid + 1, r);
        // 子节点向父节点连边
        addEdge(ls[rt], rt);
        addEdge(rs[rt], rt);
        // 保存边用于后续缩点
        saveEdge(ls[rt], rt);
        saveEdge(rs[rt], rt);
    }
    // 记录节点代表的区间范围
    rangel[rt] = l;
    ranger[rt] = r;
    return rt;
}

// ===================== 核心函数：区间→单点连边 =====================
void rangeToX(int jobl, int jobr, int jobx, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        addEdge(i, jobx);
        saveEdge(i, jobx);
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

// ===================== 核心函数：Tarjan算法 =====================
void tarjan(int u) {
    dfn[u] = low[u] = ++cntd;
    sta[++top] = u;
    for (int e = head[u]; e > 0; e = nxt[e]) {
        int v = to[e];
        if (dfn[v] == 0) {
            tarjan(v);
            low[u] = min(low[u], low[v]);
        } else {
            if (belong[v] == 0) {
                low[u] = min(low[u], dfn[v]);
            }
        }
    }
    // 找到一个SCC
    if (dfn[u] == low[u]) {
        sccCnt++;
        mostl[sccCnt] = INF;
        mostr[sccCnt] = -INF;
        int pop;
        do {
            pop = sta[top--];
            belong[pop] = sccCnt;
            // 更新SCC的区间范围
            mostl[sccCnt] = min(mostl[sccCnt], rangel[pop]);
            mostr[sccCnt] = max(mostr[sccCnt], ranger[pop]);
        } while (pop != u);
    }
}

// ===================== 核心函数：缩点 =====================
void condense() {
    cntg = 0;
    for (int i = 1; i <= sccCnt; i++) {
        head[i] = 0;
    }
    // 重建图，SCC之间连边
    for (int i = 1; i <= cnte; i++) {
        int scc1 = belong[a[i]];
        int scc2 = belong[b[i]];
        if (scc1 != scc2) {
            addEdge(scc1, scc2);
        }
    }
}

// ===================== 核心函数：DAG上DP =====================
void dpOnDAG() {
    for (int u = sccCnt; u > 0; u--) {
        for (int e = head[u]; e > 0; e = nxt[e]) {
            int v = to[e];
            // 传递区间范围
            mostl[v] = min(mostl[v], mostl[u]);
            mostr[v] = max(mostr[v], mostr[u]);
        }
    }
}

// ===================== 核心函数：查询 =====================
int query(int u) {
    int scc = belong[u];
    int num = mostr[scc] - mostl[scc] + 1;
    return num;
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    // 读入n
    cin >> n;
    cntt = n;
    // 读入每个炸弹的坐标和半径
    for (int i = 1; i <= n; i++) {
        cin >> location[i] >> radius[i];
    }
    // 构建线段树
    root = build(1, n);
    // 建立炸弹之间的引爆关系
    for (int i = 1; i <= n; i++) {
        // 计算炸弹i能引爆的范围
        int l = lower(location[i] - radius[i]);
        int r = lower(location[i] + radius[i] + 1) - 1;
        rangeToX(l, r, i, 1, n, root);
    }
    // 运行Tarjan求SCC
    for (int i = 1; i <= cntt; i++) {
        if (dfn[i] == 0) {
            tarjan(i);
        }
    }
    // 缩点
    condense();
    // DAG上DP
    dpOnDAG();
    // 计算答案
    ll ans = 0;
    for (int i = 1; i <= n; i++) {
        ans = (ans + 1LL * query(i) * i) % MOD;
    }
    cout << ans << "\n";
    return 0;
}
