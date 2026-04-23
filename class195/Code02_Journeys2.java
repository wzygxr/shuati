package class195;

// 旅程，C++版
// 一共n个点，给定起点p，一共m次操作，格式如下
// 操作 a b c d : a~b范围每个点与c~d范围每个点之间，都增加一条无向边
// 所有操作完成后，计算起点p到每个点经过的最少边数，题目保证整体连通
// 1 <= n <= 5 * 10^5
// 1 <= m <= 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P6348
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// ===================== 区间到区间连边优化建图核心知识点（C++版） =====================
// 【问题分析】
// 本题需要建立"区间→区间"的边，即a~b范围内的每个点都要与c~d范围内的每个点连边
// 如果直接建图，边数为O((b-a+1)*(d-c+1))，最坏情况下是O(n^2)，无法接受
//
// 【优化建图核心思想】
// 使用"虚拟节点"技巧，将"区间→区间"的边转化为"区间→虚拟节点→区间"的边
// 具体做法：
// 1. 创建虚拟节点x，从a~b区间向x连边（利用出树）
// 2. 创建虚拟节点y，从y向c~d区间连边（利用入树）
// 3. 从x向y连一条权值为1的边
// 这样只需要O(logn)条边就能表示区间之间的连接
//
// 【为什么用0-1 BFS】
// 本题所有边的权值只有0和1，适合使用0-1 BFS（双端队列BFS）
// 0-1 BFS的时间复杂度为O(V+E)，比Dijkstra的O((V+E)logV)更优
//
// 【C++实现优势】
// 1. deque比Java的ArrayDeque更高效
// 2. 数组访问速度更快，适合大规模图
//
// 【ML/DL关联价值】
// 1. 图神经网络中批量处理节点间的消息传递，用虚拟节点减少计算量
// 2. 知识图谱中实体关系的批量建模，降低图卷积的计算复杂度

#include <bits/stdc++.h>

using namespace std;

// ===================== 常量定义区 =====================
// MAXN: 原始节点最大数量
const int MAXN = 500001;
// MAXT: 线段树节点最大数量
const int MAXT = MAXN * 10;
// MAXE: 最大边数
const int MAXE = MAXN * 20;
// INF: 无穷大值
const int INF = 1 << 30;

// ===================== 输入变量区 =====================
// n: 原始图的节点数量
int n;
// m: 操作数量
int m;
// p: 起点
int p;

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

// ===================== 0-1 BFS变量区 =====================
// dist[i]: 从起点p到节点i的最少边数
int dist[MAXT];
// deq: 双端队列，用于0-1 BFS
deque<int> deq;

// ===================== 核心函数：链式前向星加边 =====================
// 功能：添加一条从u到v、权值为w的有向边
void addEdge(int u, int v, int w) {
    // 边计数器+1
    nxt[++cntg] = head[u];
    // 设置边的目标节点
    to[cntg] = v;
    // 设置边的权值
    weight[cntg] = w;
    // 更新头指针
    head[u] = cntg;
}

// ===================== 核心函数：构建出树（Out-Tree） =====================
// 【功能】构建用于"单点→区间"连边的出树
// 【边方向】子节点→父节点（边权0）
int buildOut(int l, int r) {
    // rt: 当前节点编号
    int rt;
    if (l == r) {
        // 叶子节点对应原始节点
        rt = l;
    } else {
        // 内部节点分配新编号
        rt = ++cntt;
        // 计算中点
        int mid = (l + r) >> 1;
        // 递归构建左右子树
        ls[rt] = buildOut(l, mid);
        rs[rt] = buildOut(mid + 1, r);
        // 子节点向父节点连0边
        addEdge(ls[rt], rt, 0);
        addEdge(rs[rt], rt, 0);
    }
    return rt;
}

// ===================== 核心函数：构建入树（In-Tree） =====================
// 【功能】构建用于"区间→单点"连边的入树
// 【边方向】父节点→子节点（边权0）
int buildIn(int l, int r) {
    // rt: 当前节点编号
    int rt;
    if (l == r) {
        // 叶子节点对应原始节点
        rt = l;
    } else {
        // 内部节点分配新编号
        rt = ++cntt;
        // 计算中点
        int mid = (l + r) >> 1;
        // 递归构建左右子树
        ls[rt] = buildIn(l, mid);
        rs[rt] = buildIn(mid + 1, r);
        // 父节点向子节点连0边
        addEdge(rt, ls[rt], 0);
        addEdge(rt, rs[rt], 0);
    }
    return rt;
}

// ===================== 核心函数：单点→区间连边 =====================
// 【功能】从单点jobx向区间[jobl, jobr]内的所有点连边，边权为0
void xToRange(int jobx, int jobl, int jobr, int l, int r, int i) {
    // 完全覆盖判断
    if (jobl <= l && r <= jobr) {
        // 向当前线段树节点连边
        addEdge(jobx, i, 0);
    } else {
        // 部分覆盖，递归处理
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
// 【功能】从区间[jobl, jobr]内的所有点向单点jobx连边，边权为0
void rangeToX(int jobl, int jobr, int jobx, int l, int r, int i) {
    // 完全覆盖判断
    if (jobl <= l && r <= jobr) {
        // 从当前线段树节点向目标点连边
        addEdge(i, jobx, 0);
    } else {
        // 部分覆盖，递归处理
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
// 【功能】建立a~b区间与c~d区间之间的无向边
// 【优化原理】使用两个虚拟节点x和y，将O(n^2)条边优化为O(logn)条边
void rangeToRange(int a, int b, int c, int d) {
    // 创建虚拟节点x和y
    int x = ++cntt;
    int y = ++cntt;
    // a~b区间→x（利用出树）
    rangeToX(a, b, x, 1, n, rootOut);
    // y→c~d区间（利用入树）
    xToRange(y, c, d, 1, n, rootIn);
    // x→y，边权1
    addEdge(x, y, 1);
}

// ===================== 核心函数：0-1 BFS算法 =====================
// 【功能】计算从起点p到所有节点的最少边数
// 【适用条件】边权只有0和1的图
void bfs01() {
    // 初始化所有节点距离为无穷大
    for (int i = 1; i <= cntt; i++) {
        dist[i] = INF;
    }
    // 起点距离为0
    dist[p] = 0;
    // 清空队列
    deq.clear();
    // 起点加入队首
    deq.push_front(p);
    // 主循环
    while (!deq.empty()) {
        // 取出队首元素
        int u = deq.front();
        deq.pop_front();
        // 遍历所有邻接边
        for (int e = head[u]; e > 0; e = nxt[e]) {
            int v = to[e];
            int w = weight[e];
            // 松弛操作
            if (dist[v] > dist[u] + w) {
                dist[v] = dist[u] + w;
                // 根据边权决定加入队首还是队尾
                if (w == 0) {
                    deq.push_front(v);
                } else {
                    deq.push_back(v);
                }
            }
        }
    }
}

// ===================== 主函数 =====================
int main() {
    // 加速输入输出
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    // 读入n, m, p
    cin >> n >> m >> p;
    // 初始化节点计数器
    cntt = n;
    // 构建出树和入树
    rootOut = buildOut(1, n);
    rootIn = buildIn(1, n);
    // 处理m个操作
    for (int i = 1, a, b, c, d; i <= m; i++) {
        cin >> a >> b >> c >> d;
        // 建立双向边
        rangeToRange(a, b, c, d);
        rangeToRange(c, d, a, b);
    }
    // 运行0-1 BFS
    bfs01();
    // 输出结果
    for (int i = 1; i <= n; i++) {
        cout << dist[i] << "\n";
    }
    return 0;
}
