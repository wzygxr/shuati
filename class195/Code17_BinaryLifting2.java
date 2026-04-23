package class195;

// 倍增优化建图基础模板，C++ 版
// 本代码展示倍增优化建图的核心模板，用于解决长距离跳跃问题
// 测试链接 : https://www.luogu.com.cn/problem/P3243（改编）
// 如下实现是 C++ 的版本
// 提交如下代码，可以通过所有测试用例

// ===================== 倍增优化建图核心知识点（C++ 版） =====================
// 【问题分析】
// 倍增优化建图用于解决长距离跳跃、传递闭包等问题
// 通过二进制拆分，将 O(n) 的跳跃优化到 O(logn)
//
// 【核心原理】
// 二进制拆分：将距离 k 拆分为 2 的幂次和
// 倍增数组：fa[i][j] 表示从 i 出发跳 2^j 步到达的点
// 状态转移：fa[i][j] = fa[fa[i][j-1]][j-1]
//
// 【ML/DL 关联价值】
// 1. 图神经网络中的长距离依赖
// 2. 序列模型中的跳跃连接
// 3. 层次化特征提取

#include <bits/stdc++.h>
using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 100001;
const int LOG = 20;
const int MAXE = 2000001;
const int INF = 1 << 30;

// ===================== 图存储区 =====================
int head[MAXN];
int next_[MAXE];
int to[MAXE];
int weight[MAXE];
int cnt;

// ===================== 倍增优化建图变量区 =====================
int n, m;
int fa[MAXN][LOG];
int minVal[MAXN][LOG];
int maxVal[MAXN][LOG];

// ===================== 拓扑排序变量区 =====================
int indegree[MAXN];
int topoOrder[MAXN];
int topoCnt;

// ===================== 核心函数：图加边 =====================
void addEdge(int u, int v, int w) {
    next_[++cnt] = head[u];
    to[cnt] = v;
    weight[cnt] = w;
    head[u] = cnt;
    indegree[v]++;
}

// ===================== 核心函数：拓扑排序 =====================
void topologicalSort() {
    queue<int> q;
    // 将所有入度为 0 的点入队
    for (int i = 1; i <= n; i++) {
        if (indegree[i] == 0) {
            q.push(i);
        }
    }

    // BFS 拓扑排序
    while (!q.empty()) {
        int u = q.front();
        q.pop();
        topoOrder[++topoCnt] = u;

        for (int e = head[u]; e > 0; e = next_[e]) {
            int v = to[e];
            indegree[v]--;
            if (indegree[v] == 0) {
                q.push(v);
            }
        }
    }
}

// ===================== 核心函数：构建倍增数组 =====================
void buildBinaryLifting() {
    // 按拓扑序计算倍增数组
    for (int i = 1; i <= topoCnt; i++) {
        int u = topoOrder[i];
        // 初始化：跳 2^0 = 1 步
        for (int e = head[u]; e > 0; e = next_[e]) {
            int v = to[e];
            int w = weight[e];
            if (fa[u][0] == 0) {
                fa[u][0] = v;
                minVal[u][0] = w;
                maxVal[u][0] = w;
            } else {
                minVal[u][0] = min(minVal[u][0], w);
                maxVal[u][0] = max(maxVal[u][0], w);
            }
        }

        // 动态规划计算 2^1, 2^2, ..., 2^(LOG-1)
        for (int j = 1; j < LOG; j++) {
            if (fa[u][j - 1] != 0) {
                int mid = fa[u][j - 1];
                fa[u][j] = fa[mid][j - 1];
                // 合并路径上的最值
                if (fa[u][j] != 0) {
                    minVal[u][j] = min(minVal[u][j - 1], minVal[mid][j - 1]);
                    maxVal[u][j] = max(maxVal[u][j - 1], maxVal[mid][j - 1]);
                }
            }
        }
    }
}

// ===================== 核心函数：查询跳 k 步 =====================
int jumpKSteps(int u, int k) {
    for (int j = 0; j < LOG; j++) {
        if ((k >> j) & 1) {
            u = fa[u][j];
            if (u == 0) {
                return 0; // 超出范围
            }
        }
    }
    return u;
}

// ===================== 核心函数：查询路径最小值 =====================
int queryMin(int u, int k) {
    int result = INF;
    for (int j = 0; j < LOG; j++) {
        if ((k >> j) & 1) {
            result = min(result, minVal[u][j]);
            u = fa[u][j];
            if (u == 0) {
                return -1; // 超出范围
            }
        }
    }
    return result == INF ? -1 : result;
}

// ===================== 核心函数：查询路径最大值 =====================
int queryMax(int u, int k) {
    int result = -INF;
    for (int j = 0; j < LOG; j++) {
        if ((k >> j) & 1) {
            result = max(result, maxVal[u][j]);
            u = fa[u][j];
            if (u == 0) {
                return -1; // 超出范围
            }
        }
    }
    return result == -INF ? -1 : result;
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    // 读入节点数和边数
    cin >> n >> m;

    // 读入 m 条边
    for (int i = 0; i < m; i++) {
        int u, v, w;
        cin >> u >> v >> w;
        addEdge(u, v, w);
    }

    // 拓扑排序
    topologicalSort();

    // 构建倍增数组
    buildBinaryLifting();

    // 查询示例
    int queryCount;
    cin >> queryCount;
    for (int i = 0; i < queryCount; i++) {
        int u, k;
        cin >> u >> k;
        int target = jumpKSteps(u, k);
        int minV = queryMin(u, k);
        int maxV = queryMax(u, k);
        cout << "从 " << u << " 跳 " << k << " 步：到达 " << target << "，最小值 " << minV << "，最大值 " << maxV << endl;
    }

    return 0;
}
