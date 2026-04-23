package class195;

// 遗产，C++版
// 一共n个点，给定起点s，一共q条操作，操作类型如下
// 操作 1 x y w   : 从点x到点y增加有向边，边权是w
// 操作 2 x l r w : 从点x到l~r范围的每个点增加有向边，边权都是w
// 操作 3 x l r w : 从l~r范围的每个点到点x增加有向边，边权都是w
// 所有操作完成后，计算起点s到每个点的最短距离并打印，如果不连通打印-1
// 1 <= n、q <= 10^5
// 1 <= w <= 10^9
// 测试链接 : https://www.luogu.com.cn/problem/CF786B
// 测试链接 : https://codeforces.com/problemset/problem/786B
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// ===================== 线段树优化建图核心知识点（C++版） =====================
// 【优化建图核心思想】
// 当题目需要建立"单点→区间"或"区间→单点"的边时，直接建图边数为O(n*m)，会超时超内存
// 线段树优化建图通过构建两棵线段树（入树+出树），将边数优化到O(m*logn)
// 
// 【入树（In-Tree）设计原理】
// 功能：实现"区间→单点"的连边，父节点向子节点连0边
// 原理：区间包含子区间，区间约束可通过根节点向下传递到目标单点
// 应用：操作3中"区间到单点"的连边，通过入树只需O(logn)条边
//
// 【出树（Out-Tree）设计原理】
// 功能：实现"单点→区间"的连边，子节点向父节点连0边
// 原理：子区间属于父区间，单点约束可通过子节点向上传递到区间根节点
// 应用：操作2中"单点到区间"的连边，通过出树只需O(logn)条边
//
// 【C++与Java实现差异】
// 1. C++使用数组而非ArrayList，效率更高
// 2. C++的priority_queue默认是大根堆，需要重载运算符实现小根堆
// 3. C++使用ios::sync_with_stdio(false)加速输入输出
//
// 【ML/DL关联价值】
// 1. 大规模图神经网络(GNN)中，用线段树优化邻居采样，将稠密图转化为稀疏图
// 2. 知识图谱中万亿级关系的压缩存储，降低内存占用
// 3. 时序图神经网络中，区间关系的批量处理优化

#include <bits/stdc++.h>

using namespace std;

// ===================== 类型定义区 =====================
// 使用long long定义ll别名，方便代码书写
using ll = long long;

// ===================== 常量定义区 =====================
// MAXN: 原始节点最大数量，根据题目数据范围1e5设定
const int MAXN = 100001;
// MAXT: 线段树节点最大数量，约为原始节点的10倍
const int MAXT = MAXN * 10;
// MAXE: 最大边数，线段树内部边约2*n，操作边约q*logn
const int MAXE = MAXN * 30;
// INF: 无穷大值，用于最短路初始化，取1LL<<60确保不会溢出
const ll INF = 1LL << 60;

// ===================== 输入变量区 =====================
// n: 原始图的节点数量
int n;
// q: 操作数量
int q;
// s: 起点
int s;

// ===================== 链式前向星存图区 =====================
// head[u]: 节点u的第一条边的编号，0表示没有边
int head[MAXT];
// nxt[e]: 边e的下一条边的编号
int nxt[MAXE];
// to[e]: 边e指向的目标节点
int to[MAXE];
// weight[e]: 边e的权值
int weight[MAXE];
// cntg: 边的计数器，从1开始计数
int cntg;

// ===================== 线段树优化建图核心变量区 =====================
// ls[i]: 节点i的左子节点编号
int ls[MAXT];
// rs[i]: 节点i的右子节点编号
int rs[MAXT];
// rootOut: 出树的根节点编号，用于"单点→区间"连边
int rootOut;
// rootIn: 入树的根节点编号，用于"区间→单点"连边
int rootIn;
// cntt: 当前总节点数（原始节点+线段树新增节点）
int cntt;

// ===================== Dijkstra算法变量区 =====================
// dist[i]: 从起点s到节点i的最短距离
dist[MAXT];
// vis[i]: 节点i是否已被确定最短距离
bool vis[MAXT];

// ===================== Dijkstra优先队列节点结构体 =====================
// 【结构体功能】存储Dijkstra算法中的节点信息
// 【运算符重载】C++ priority_queue默认是大根堆，通过重载<实现小根堆
struct Node {
    // u: 节点编号
    int u;
    // d: 当前距离
    ll d;

    // 【运算符重载】定义小于运算，使priority_queue按距离升序排列（小根堆）
    // 返回true表示this的优先级低于other，即this排在other后面
    bool operator < (const Node &other) const {
        // 注意：priority_queue是反过来的，所以用>实现升序
        return d > other.d;
    }
};

// heap: 优先队列，按距离升序排列
priority_queue<Node> heap;

// ===================== 核心函数：链式前向星加边 =====================
// 功能：添加一条从u到v、权值为w的有向边
// 笔试面试注意：链式前向星是标准模板，务必熟练掌握
void addEdge(int u, int v, int w) {
    // ++cntg: 边计数器+1，获取新边的编号
    // nxt[cntg] = head[u]: 新边的下一条边指向u原来的第一条边
    nxt[++cntg] = head[u];
    // to[cntg] = v: 新边指向目标节点v
    to[cntg] = v;
    // weight[cntg] = w: 设置新边的权值
    weight[cntg] = w;
    // head[u] = cntg: 更新u的第一条边为新边
    head[u] = cntg;
}

// ===================== 核心函数：构建出树（Out-Tree） =====================
// 【出树功能】实现"单点→区间"的连边，子节点向父节点连0边
// 【设计原理】子区间属于父区间，单点约束可通过子节点向上传递到区间根节点
// 参数：l-当前区间左端点，r-当前区间右端点
// 返回：当前线段树节点的编号
int buildOut(int l, int r) {
    // rt: 当前节点的编号
    int rt;
    if (l == r) {
        // 【叶子节点处理】叶子节点对应原始节点，编号直接等于原始节点编号l
        rt = l;
    } else {
        // 【内部节点处理】分配新节点编号
        rt = ++cntt;
        // mid: 区间中点，用于二分分割
        int mid = (l + r) >> 1;
        // 递归构建左子树，区间[l, mid]
        ls[rt] = buildOut(l, mid);
        // 递归构建右子树，区间[mid+1, r]
        rs[rt] = buildOut(mid + 1, r);
        // 【核心边】左子节点→当前节点，边权0（子区间属于父区间）
        addEdge(ls[rt], rt, 0);
        // 【核心边】右子节点→当前节点，边权0
        addEdge(rs[rt], rt, 0);
        // 【原理说明】子节点向父节点连0边，表示从叶子节点可以走到代表大区间的父节点
    }
    // 返回当前节点编号
    return rt;
}

// ===================== 核心函数：构建入树（In-Tree） =====================
// 【入树功能】实现"区间→单点"的连边，父节点向子节点连0边
// 【设计原理】区间包含子区间，区间约束可通过根节点向下传递到目标单点
// 参数：l-当前区间左端点，r-当前区间右端点
// 返回：当前线段树节点的编号
int buildIn(int l, int r) {
    // rt: 当前节点的编号
    int rt;
    if (l == r) {
        // 【叶子节点处理】叶子节点对应原始节点，编号直接等于原始节点编号l
        rt = l;
    } else {
        // 【内部节点处理】分配新节点编号
        rt = ++cntt;
        // mid: 区间中点
        int mid = (l + r) >> 1;
        // 递归构建左子树
        ls[rt] = buildIn(l, mid);
        // 递归构建右子树
        rs[rt] = buildIn(mid + 1, r);
        // 【核心边】当前节点→左子节点，边权0（父区间包含子区间）
        addEdge(rt, ls[rt], 0);
        // 【核心边】当前节点→右子节点，边权0
        addEdge(rt, rs[rt], 0);
        // 【原理说明】父节点向子节点连0边，表示从代表大区间的节点可以走到子区间
    }
    // 返回当前节点编号
    return rt;
}

// ===================== 核心函数：单点→区间连边 =====================
// 【功能】从单点jobx向区间[jobl, jobr]内的所有点连边，边权为jobw
// 【复杂度】O(logn)条边，替代O(n)条直接连边
// 【原理】利用出树的结构，单点→出树节点→区间内的所有叶子节点
// 参数：jobx-源单点，jobl-目标区间左端点，jobr-目标区间右端点，jobw-边权
//       l-当前线段树节点代表的区间左端点，r-右端点，i-当前线段树节点编号
void xToRange(int jobx, int jobl, int jobr, int jobw, int l, int r, int i) {
    // 【完全覆盖判断】当前节点代表的区间完全包含在目标区间内
    if (jobl <= l && r <= jobr) {
        // 直接从源点向当前线段树节点连边，边权jobw
        // 由于出树的子节点→父节点边权为0，从当前节点可以到达区间内所有叶子节点
        addEdge(jobx, i, jobw);
    } else {
        // 【部分覆盖处理】需要递归到子节点
        // mid: 当前区间中点
        int mid = (l + r) >> 1;
        // 如果目标区间与左子树有交集，递归左子树
        if (jobl <= mid) {
            xToRange(jobx, jobl, jobr, jobw, l, mid, ls[i]);
        }
        // 如果目标区间与右子树有交集，递归右子树
        if (jobr > mid) {
            xToRange(jobx, jobl, jobr, jobw, mid + 1, r, rs[i]);
        }
    }
}

// ===================== 核心函数：区间→单点连边 =====================
// 【功能】从区间[jobl, jobr]内的所有点向单点jobx连边，边权为jobw
// 【复杂度】O(logn)条边，替代O(n)条直接连边
// 【原理】利用入树的结构，区间内的所有叶子节点→入树节点→单点
// 参数：jobl-源区间左端点，jobr-源区间右端点，jobx-目标单点，jobw-边权
//       l-当前线段树节点代表的区间左端点，r-右端点，i-当前线段树节点编号
void rangeToX(int jobl, int jobr, int jobx, int jobw, int l, int r, int i) {
    // 【完全覆盖判断】当前节点代表的区间完全包含在源区间内
    if (jobl <= l && r <= jobr) {
        // 直接从当前线段树节点向目标单点连边，边权jobw
        // 由于入树的父节点→子节点边权为0，区间内所有叶子节点可以到达当前节点
        addEdge(i, jobx, jobw);
    } else {
        // 【部分覆盖处理】需要递归到子节点
        // mid: 当前区间中点
        int mid = (l + r) >> 1;
        // 如果源区间与左子树有交集，递归左子树
        if (jobl <= mid) {
            rangeToX(jobl, jobr, jobx, jobw, l, mid, ls[i]);
        }
        // 如果源区间与右子树有交集，递归右子树
        if (jobr > mid) {
            rangeToX(jobl, jobr, jobx, jobw, mid + 1, r, rs[i]);
        }
    }
}

// ===================== 核心函数：Dijkstra最短路算法 =====================
// 【功能】计算从起点s到所有节点的最短距离
// 【适用条件】边权非负（本题边权w>=1，满足条件）
// 【复杂度】O((V+E)logV)，V为节点数，E为边数
void dijkstra() {
    // 【初始化】所有节点的距离设为无穷大
    // 注意：cntt是优化建图后的总节点数，不是原始节点数n
    for (int i = 1; i <= cntt; i++) {
        dist[i] = INF;
    }
    // 起点距离设为0
    dist[s] = 0;
    // 起点入堆
    heap.push({s, 0});
    // 【主循环】堆不为空时继续
    while (!heap.empty()) {
        // 取出距离最小的节点
        Node cur = heap.top();
        heap.pop();
        // u: 当前节点编号
        int u = cur.u;
        // d: 当前距离
        ll d = cur.d;
        // 【访问判断】如果该节点已被处理过，跳过
        if (!vis[u]) {
            // 标记为已访问
            vis[u] = true;
            // 【松弛操作】遍历u的所有邻接边
            for (int e = head[u]; e > 0; e = nxt[e]) {
                // v: 边的目标节点
                int v = to[e];
                // w: 边的权值
                int w = weight[e];
                // 【松弛条件】如果v未被访问且可以通过u获得更短距离
                if (!vis[v] && dist[v] > d + w) {
                    // 更新v的最短距离
                    dist[v] = d + w;
                    // 将v入堆
                    heap.push({v, dist[v]});
                }
            }
        }
    }
}

// ===================== 主函数 =====================
int main() {
    // 【加速输入输出】关闭同步，解除绑定，提高IO效率
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    // 【读入n】原始节点数量
    cin >> n;
    // 【读入q】操作数量
    cin >> q;
    // 【读入s】起点
    cin >> s;
    // 【初始化】cntt从n开始，1~n是原始节点编号
    cntt = n;
    // 【构建出树】用于操作2的"单点→区间"连边
    rootOut = buildOut(1, n);
    // 【构建入树】用于操作3的"区间→单点"连边
    rootIn = buildIn(1, n);
    // 【处理q个操作】
    for (int i = 1, op, x, y, l, r, w; i <= q; i++) {
        // 读入操作类型
        cin >> op;
        if (op == 1) {
            // 【操作1】从点x到点y增加有向边，边权是w
            cin >> x >> y >> w;
            // 直接加边，无需优化
            addEdge(x, y, w);
        } else if (op == 2) {
            // 【操作2】从点x到l~r范围的每个点增加有向边，边权都是w
            // 使用出树优化，将O(n)条边优化为O(logn)条边
            cin >> x >> l >> r >> w;
            // 调用xToRange，利用出树实现"单点→区间"连边
            xToRange(x, l, r, w, 1, n, rootIn);
        } else {
            // 【操作3】从l~r范围的每个点到点x增加有向边，边权都是w
            // 使用入树优化，将O(n)条边优化为O(logn)条边
            cin >> x >> l >> r >> w;
            // 调用rangeToX，利用入树实现"区间→单点"连边
            rangeToX(l, r, x, w, 1, n, rootOut);
        }
    }
    // 【运行Dijkstra】计算从s到所有节点的最短距离
    dijkstra();
    // 【输出结果】只输出原始节点1~n的最短距离
    for (int i = 1; i <= n; i++) {
        // 如果距离为INF，说明不可达，输出-1
        cout << (dist[i] == INF ? -1 : dist[i]) << " ";
    }
    return 0;
}
