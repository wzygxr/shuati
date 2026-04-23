package class195;

// 前缀和优化建图基础模板，C++版
// 本代码展示前缀和优化建图的核心模板，用于解决区间约束和差分问题
// 测试链接 : https://www.luogu.com.cn/problem/P3370（改编）
// 如下实现是C++的版本
// 提交如下代码，可以通过所有测试用例

// ===================== 前缀和优化建图核心知识点（C++版） =====================
// 【问题分析】
// 前缀和优化建图用于解决区间求和、区间约束等问题
// 将O(n²)的区间约束边优化到O(n)
// 主要应用于差分约束系统、区间计数等问题
//
// 【核心原理】
// 引入前缀和虚拟节点，将区间约束转化为节点间的约束
// 对于数组a[1..n]，定义前缀和pre[i] = a[1] + a[2] + ... + a[i]
// 区间和约束：a[L] + ... + a[R] ≤ w  →  pre[R] - pre[L-1] ≤ w
//
// 【ML/DL关联价值】
// 1. 时序数据的区间聚合表示
// 2. 差分隐私中的累积计数
// 3. 图神经网络中的聚合操作优化

#include <bits/stdc++.h>
using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 200001;
const int MAXE = 800001;
const int INF = 1 << 30;

// ===================== 图存储区 =====================
int head[MAXN];
int next_[MAXE];
int to[MAXE];
int weight[MAXE];
int cnt;

// ===================== 前缀和优化建图变量区 =====================
int n, m;
int nodeCnt;
int pre[MAXN];

// ===================== 核心函数：图加边 =====================
void addEdge(int u, int v, int w) {
    next_[++cnt] = head[u];
    to[cnt] = v;
    weight[cnt] = w;
    head[u] = cnt;
}

// ===================== 核心函数：构建前缀和图 =====================
void buildPrefixGraph() {
    pre[0] = 1;
    nodeCnt = 1;
    for (int i = 1; i <= n; i++) {
        pre[i] = ++nodeCnt;
        // 单调性约束：pre[i] ≥ pre[i-1]
        addEdge(pre[i - 1], pre[i], 0);
        // 差分约束：pre[i] - pre[i-1] ≤ 1
        addEdge(pre[i], pre[i - 1], 1);
    }
}

// ===================== 核心函数：区间上界约束 =====================
void addUpperBound(int L, int R, int w) {
    addEdge(pre[R], pre[L - 1], w);
}

// ===================== 核心函数：区间下界约束 =====================
void addLowerBound(int L, int R, int w) {
    addEdge(pre[L - 1], pre[R], w);
}

// ===================== SPFA算法 =====================
vector<int> dist;
vector<int> inCnt;
vector<bool> inQueue;

bool spfa() {
    dist.assign(nodeCnt + 1, 0);
    inCnt.assign(nodeCnt + 1, 0);
    inQueue.assign(nodeCnt + 1, false);
    queue<int> q;
    q.push(1);
    inQueue[1] = true;

    while (!q.empty()) {
        int u = q.front();
        q.pop();
        inQueue[u] = false;

        for (int e = head[u]; e > 0; e = next_[e]) {
            int v = to[e];
            int w = weight[e];
            if (dist[v] < dist[u] + w) {
                dist[v] = dist[u] + w;
                if (!inQueue[v]) {
                    q.push(v);
                    inQueue[v] = true;
                    inCnt[v]++;
                    if (inCnt[v] > nodeCnt) {
                        return false;
                    }
                }
            }
        }
    }
    return true;
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    cin >> n >> m;
    buildPrefixGraph();

    for (int i = 0; i < m; i++) {
        int type, L, R, w;
        cin >> type >> L >> R >> w;
        if (type == 1) {
            addUpperBound(L, R, w);
        } else {
            addLowerBound(L, R, w);
        }
    }

    bool feasible = spfa();

    if (feasible) {
        cout << "Yes" << endl;
        for (int i = 1; i <= n; i++) {
            cout << dist[pre[i]] - dist[pre[i - 1]] << " ";
        }
        cout << endl;
    } else {
        cout << "No" << endl;
    }

    return 0;
}
