package class195;

// 沙漠，C++版
// 一共n个数字，所有数字都在 1 ~ 10^9 的范围，这是范围说明
// 接下来给定s条设置说明，格式 x v ，表示第x个数的值确定是v
// 接下来给定m条关系说明，格式 l r k x1 x2 ... xk 含义如下
// 第l到第r个数字，其中有k个数字，分别是第x1、第x2 .. 第xk个数字
// 这k个数字中的每一个，都比剩下的(r - l + 1 - k)个数字要大，严格大于
// 根据上面的说明，找到没有矛盾的，给每个数字赋值的方案，任何一个方案即可
// 如果存在方案打印"TAK"，然后打印每个数字，不存在方案打印"NIE"
// 1 <= n、s <= 10^5
// 1 <= m <= 2 * 10^5
// 所有k的累加和 <= 3 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P3588
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// ===================== 差分约束+线段树优化建图核心知识点（C++版） =====================
// 【问题分析】
// 本题需要判断约束系统是否有解，如果有解则给出一组可行解
// 约束类型：某些数字必须严格大于其他数字
//
// 【差分约束建模】
// 将"a > b"转化为"a >= b + 1"，即差分约束系统中的边 b -> a，权值为1
//
// 【线段树优化】
// 对于每个约束，需要建立O(k * (r-l+1-k))条边，最坏O(n^2)
// 优化方法：引入虚拟节点，将边数优化到O(k * logn)
//
// 【拓扑排序判环】
// 差分约束系统有解 <=> 图中无正权环
//
// 【ML/DL关联价值】
// 1. 约束满足问题(CSP)的图论建模方法
// 2. 神经网络中的约束优化
// 3. 知识图谱中的逻辑推理和一致性检验

#include <bits/stdc++.h>

using namespace std;

// ===================== 常量定义区 =====================
// MAXN: 原始节点最大数量
const int MAXN = 100001;
// MAXT: 线段树节点最大数量
const int MAXT = MAXN * 5;
// MAXE: 最大边数
const int MAXE = MAXN * 20;
// LIMIT: 数字的最大值上限
const int LIMIT = 1000000000;

// ===================== 输入变量区 =====================
// n: 数字个数
int n;
// s: 确定值的数量
int s;
// m: 关系说明数量
int m;

// ===================== 链式前向星存图区 =====================
// head[u]: 节点u的第一条边的编号
int head[MAXT];
// nxt[e]: 边e的下一条边的编号
int nxt[MAXE];
// to[e]: 边e指向的目标节点
int to[MAXE];
// weight[e]: 边e的权值
int weight[MAXE];
// cntg: 边的计数器
int cntg;

// ===================== 线段树优化建图核心变量区 =====================
// ls[i]: 节点i的左子节点编号
int ls[MAXT];
// rs[i]: 节点i的右子节点编号
int rs[MAXT];
// root: 线段树根节点
int root;
// cntt: 当前总节点数
int cntt;

// ===================== 拓扑排序与差分约束变量区 =====================
// val[i]: 节点i的确定值
int val[MAXT];
// atMost[i]: 节点i的最大可行值
int atMost[MAXT];
// indegree[i]: 节点i的入度
int indegree[MAXT];
// que: 拓扑排序队列
int que[MAXT];

// ===================== 核心函数：链式前向星加边 =====================
void addEdge(int u, int v, int w) {
    nxt[++cntg] = head[u];
    to[cntg] = v;
    weight[cntg] = w;
    head[u] = cntg;
    indegree[v]++;
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
        addEdge(rt, ls[rt], 0);
        addEdge(rt, rs[rt], 0);
    }
    return rt;
}

// ===================== 核心函数：单点→区间连边 =====================
void xToRange(int jobx, int jobl, int jobr, int jobw, int l, int r, int i) {
    if (jobl > jobr) {
        return;
    }
    if (jobl <= l && r <= jobr) {
        addEdge(jobx, i, jobw);
    } else {
        int mid = (l + r) >> 1;
        if (jobl <= mid) {
            xToRange(jobx, jobl, jobr, jobw, l, mid, ls[i]);
        }
        if (jobr > mid) {
            xToRange(jobx, jobl, jobr, jobw, mid + 1, r, rs[i]);
        }
    }
}

// ===================== 核心函数：拓扑排序+最长路 =====================
bool topo() {
    int qi = 1, qsiz = 0;
    // 将所有入度为0的节点入队
    for (int i = 1; i <= cntt; i++) {
        if (indegree[i] == 0) {
            que[++qsiz] = i;
        }
        atMost[i] = val[i] == 0 ? LIMIT : val[i];
    }
    // 拓扑排序主循环
    while (qi <= qsiz) {
        int u = que[qi++];
        for (int e = head[u]; e > 0; e = nxt[e]) {
            int v = to[e];
            int w = weight[e];
            // 最长路更新
            if (atMost[v] > atMost[u] + w) {
                atMost[v] = atMost[u] + w;
                // 矛盾检测
                if ((val[v] != 0 && atMost[v] < val[v]) || atMost[v] < 1) {
                    return false;
                }
            }
            if (--indegree[v] == 0) {
                que[++qsiz] = v;
            }
        }
    }
    return qsiz == cntt;
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    // 读入n, s, m
    cin >> n >> s >> m;
    // 初始化节点计数器
    cntt = n;
    // 构建线段树
    root = build(1, n);
    // 处理s条确定值设置
    for (int i = 1; i <= s; i++) {
        int x, v;
        cin >> x >> v;
        val[x] = v;
    }
    // 处理m条关系说明
    for (int i = 1; i <= m; i++) {
        int l, r, k;
        cin >> l >> r >> k;
        // 创建虚拟节点vnode
        int vnode = ++cntt;
        for (int j = 1; j <= k; j++) {
            int x;
            cin >> x;
            // x >= vnode
            addEdge(x, vnode, 0);
            // vnode >= [l, x-1]区间内的数字 + 1
            xToRange(vnode, l, x - 1, -1, 1, n, root);
            l = x + 1;
        }
        // vnode >= [l, r]区间内的数字 + 1
        xToRange(vnode, l, r, -1, 1, n, root);
    }
    // 运行拓扑排序
    bool check = topo();
    // 输出结果
    if (check) {
        cout << "TAK" << "\n";
        for (int i = 1; i <= n; i++) {
            cout << atMost[i] << " ";
        }
        cout << "\n";
    } else {
        cout << "NIE" << "\n";
    }
    return 0;
}
