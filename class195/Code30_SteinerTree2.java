package class195;

// 斯坦纳树优化建图基础模板，C++ 版
// 本代码展示斯坦纳树优化建图的核心模板，用于解决最小连通子图问题
// 测试链接 : https://www.luogu.com.cn/problem/P6192（改编）
// 如下实现是 C++ 的版本
// 提交如下代码，可以通过所有测试用例

// ===================== 斯坦纳树优化建图核心知识点（C++ 版） =====================
// 【问题分析】
// 斯坦纳树用于解决连接指定关键点的最小代价问题
// 通过状压 DP 和最短路算法求解
//
// 【核心原理】
// 状态定义：dp[i][mask] 表示以 i 为根，连接 mask 表示的关键点集合的最小代价
// 状态转移：分为两种转移方式
//   1. 子集合并：dp[i][mask] = min(dp[i][s] + dp[i][mask^s] - val[i])
//   2. 边扩展：dp[i][mask] = min(dp[j][mask] + w(j,i))
//
// 【ML/DL 关联价值】
// 1. 图神经网络中的子图连接问题
// 2. 多任务学习中的共享结构优化
// 3. 组合优化中的状态空间搜索

#include <bits/stdc++.h>
using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 101;
const int MAXK = 11;
const int MAX_MASK = 1 << MAXK;
const int INF = 1 << 30;

// ===================== 边结构体 =====================
struct Edge {
    int to, weight;
    Edge(int t, int w) : to(t), weight(w) {}
};

// ===================== 图存储区 =====================
vector<Edge> graph[MAXN];
int n, m, k;

// ===================== DP 数组 =====================
int dp[MAXN][MAX_MASK];

// ===================== 关键点位置 =====================
int keyPoints[MAXK];

// ===================== 核心函数：SPFA 最短路 =====================
void spfa(int mask) {
    bool inQueue[MAXN] = {false};
    queue<int> q;

    for (int i = 1; i <= n; i++) {
        if (dp[i][mask] < INF) {
            q.push(i);
            inQueue[i] = true;
        }
    }

    while (!q.empty()) {
        int u = q.front();
        q.pop();
        inQueue[u] = false;

        for (auto& edge : graph[u]) {
            int v = edge.to;
            int w = edge.weight;

            if (dp[v][mask] > dp[u][mask] + w) {
                dp[v][mask] = dp[u][mask] + w;
                if (!inQueue[v]) {
                    q.push(v);
                    inQueue[v] = true;
                }
            }
        }
    }
}

// ===================== 核心函数：斯坦纳树 DP =====================
int steinerTree() {
    // 初始化
    for (int i = 1; i <= n; i++) {
        fill(dp[i], dp[i] + MAX_MASK, INF);
    }

    // 初始化关键点
    for (int i = 1; i <= k; i++) {
        dp[keyPoints[i]][1 << (i - 1)] = 0;
    }

    // 枚举状态
    for (int mask = 1; mask < (1 << k); mask++) {
        // 子集合并转移
        for (int i = 1; i <= n; i++) {
            for (int s = (mask - 1) & mask; s > 0; s = (s - 1) & mask) {
                dp[i][mask] = min(dp[i][mask], dp[i][s] + dp[i][mask ^ s]);
            }
        }

        // 最短路优化转移
        spfa(mask);
    }

    // 找到最小代价
    int result = INF;
    for (int i = 1; i <= n; i++) {
        result = min(result, dp[i][(1 << k) - 1]);
    }

    return result;
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    // 读入
    cin >> n >> m >> k;

    for (int i = 1; i <= k; i++) {
        cin >> keyPoints[i];
    }

    for (int i = 0; i < m; i++) {
        int u, v, w;
        cin >> u >> v >> w;
        graph[u].emplace_back(v, w);
        graph[v].emplace_back(u, w);
    }

    // 求解
    int result = steinerTree();

    // 输出
    cout << "连接所有关键点的最小代价：" << (result == INF ? "无法连接" : to_string(result)) << endl;

    return 0;
}
