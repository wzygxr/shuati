package class195;

// 道路，C++版
// 一共n个点，一共m次操作，格式如下
// 操作 a b c d w: a~b范围每个点与c~d范围每个点之间，都增加权值为w的无向边
// 给定数字k，表示有k次机会，每次在通过一条边时，不用支付这条边的代价
// 所有操作完成后，打印1号点到n号点的最低代价
// 如果不存在通路，打印"CreationAugust is a sb!"
// 1 <= n <= 5 * 10^4
// 1 <= m <= 10^5
// 1 <= k <= 10
// 1 <= w <= 10^3
// 测试链接 : https://acm.hdu.edu.cn/showproblem.php?pid=5669
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// ===================== 分层图+线段树优化建图核心知识点（C++版） =====================
// 【问题分析】
// 本题包含两个难点：
// 1. 区间到区间的连边需要优化（使用线段树优化建图）
// 2. 有k次免费通过边的机会（使用分层图技术）
//
// 【分层图核心思想】
// 将问题转化为k+1层的图，第i层表示已经使用了i次免费机会
// 状态转移：
// 1. 正常通过边：从(i, u) -> (i, v)，代价+w
// 2. 使用免费机会：从(i, u) -> (i+1, v)，代价+0
//
// 【状态设计】
// dist[node][used] = 到达node节点，已经使用used次免费机会的最小代价
//
// 【C++实现优势】
// 1. 结构体+运算符重载实现优先队列，比Java更简洁
// 2. 数组访问速度更快，适合大规模图
//
// 【ML/DL关联价值】
// 1. 强化学习中的分层状态空间设计
// 2. 路径规划中的多目标优化

#include <bits/stdc++.h>

using namespace std;

// ===================== 常量定义区 =====================
// MAXN: 原始节点最大数量
const int MAXN = 50001;
// MAXT: 线段树节点最大数量
const int MAXT = MAXN * 10;
// MAXE: 最大边数
const int MAXE = MAXN * 20;
// MAXK: 最大免费次数+1
const int MAXK = 11;
// INF: 无穷大值
const int INF = 1 << 30;

// ===================== 输入变量区 =====================
// t: 测试用例数量
int t;
// n: 原始图的节点数量
int n;
// m: 操作数量
int m;
// k: 免费通过边的次数
int k;

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
// rootOut: 出树的根节点编号
int rootOut;
// rootIn: 入树的根节点编号
int rootIn;
// cntt: 当前总节点数
int cntt;

// ===================== 分层图Dijkstra变量区 =====================
// dist[i][j]: 到达节点i，使用j次免费机会的最小代价
int dist[MAXT][MAXK];
// vis[i][j]: 节点i使用j次免费机会的状态是否已访问
bool vis[MAXT][MAXK];

// ===================== Dijkstra优先队列节点结构体 =====================
// 【结构体功能】存储Dijkstra算法中的节点状态
// 【运算符重载】重载<实现小根堆
struct Node {
    // node: 节点编号
    int node;
    // time: 已使用的免费次数
    int time;
    // cost: 当前总代价
    int cost;

    // 【运算符重载】定义小于运算，使priority_queue按代价升序排列
    bool operator < (const Node &other) const {
        // 注意：priority_queue默认大根堆，所以用>实现升序
        return cost > other.cost;
    }
};

// heap: 优先队列
priority_queue<Node> heap;

// ===================== 核心函数：链式前向星加边 =====================
void addEdge(int u, int v, int w) {
    nxt[++cntg] = head[u];
    to[cntg] = v;
    weight[cntg] = w;
    head[u] = cntg;
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
        addEdge(ls[rt], rt, 0);
        addEdge(rs[rt], rt, 0);
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
        addEdge(rt, ls[rt], 0);
        addEdge(rt, rs[rt], 0);
    }
    return rt;
}

// ===================== 核心函数：单点→区间连边 =====================
void xToRange(int jobx, int jobl, int jobr, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        addEdge(jobx, i, 0);
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
    if (jobl <= l && r <= jobr) {
        addEdge(i, jobx, 0);
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

// ===================== 核心函数：区间→区间连边 =====================
void rangeToRange(int a, int b, int c, int d, int w) {
    int x = ++cntt;
    int y = ++cntt;
    rangeToX(a, b, x, 1, n, rootOut);
    xToRange(y, c, d, 1, n, rootIn);
    addEdge(x, y, w);
}

// ===================== 核心函数：分层图Dijkstra =====================
// 【功能】计算从start到target的最小代价，可以使用k次免费机会
int dijkstra(int start, int target) {
    // 清空优先队列
    while (!heap.empty()) {
        heap.pop();
    }
    // 初始化距离数组
    for (int i = 1; i <= cntt; i++) {
        for (int j = 0; j <= k; j++) {
            dist[i][j] = INF;
            vis[i][j] = false;
        }
    }
    // 起点入队
    dist[start][0] = 0;
    heap.push({start, 0, 0});
    // Dijkstra主循环
    while (!heap.empty()) {
        Node cur = heap.top();
        heap.pop();
        int node = cur.node;
        int time = cur.time;
        int cost = cur.cost;
        // 如果已经访问过，跳过
        if (!vis[node][time]) {
            vis[node][time] = true;
            // 到达目标，返回代价
            if (node == target) {
                return cost;
            }
            // 遍历所有邻接边
            for (int e = head[node]; e > 0; e = nxt[e]) {
                int v = to[e];
                int w = weight[e];
                // 不使用免费机会
                if (!vis[v][time] && dist[v][time] > cost + w) {
                    dist[v][time] = cost + w;
                    heap.push({v, time, dist[v][time]});
                }
                // 使用免费机会
                if (time < k && !vis[v][time + 1] && dist[v][time + 1] > cost) {
                    dist[v][time + 1] = cost;
                    heap.push({v, time + 1, dist[v][time + 1]});
                }
            }
        }
    }
    return -1;  // 无法到达
}

// ===================== 核心函数：清空图 =====================
void clear() {
    for (int i = 1; i <= cntt; i++) {
        head[i] = 0;
    }
    cntg = cntt = 0;
    while (!heap.empty()) {
        heap.pop();
    }
}

// ===================== 主函数 =====================
int main() {
    // 加速输入输出
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    // 读入测试用例数
    cin >> t;
    for (int test = 1; test <= t; test++) {
        // 读入n, m, k
        cin >> n >> m >> k;
        // 初始化节点计数器
        cntt = n;
        // 构建出树和入树
        rootOut = buildOut(1, n);
        rootIn = buildIn(1, n);
        // 处理m个操作
        for (int i = 1, a, b, c, d, w; i <= m; i++) {
            cin >> a >> b >> c >> d >> w;
            // 建立双向边
            rangeToRange(a, b, c, d, w);
            rangeToRange(c, d, a, b, w);
        }
        // 运行Dijkstra
        int ans = dijkstra(1, n);
        // 输出结果
        if (ans == -1) {
            cout << "CreationAugust is a sb!" << "\n";
        } else {
            cout << ans << "\n";
        }
        // 清空图，准备下一个测试用例
        clear();
    }
    return 0;
}
