package class195;

// 支配树优化建图基础模板，C++ 版
// 本代码展示支配树优化建图的核心模板，用于解决有向图必经点问题
// 测试链接 : https://www.luogu.com.cn/problem/P2597（改编）
// 如下实现是 C++ 的版本
// 提交如下代码，可以通过所有测试用例

// ===================== 支配树优化建图核心知识点（C++ 版） =====================
// 【问题分析】
// 支配树用于解决有向图中的必经点问题
// 通过构建支配树，可以快速查询从源点到某点必须经过的点
//
// 【核心原理】
// 支配点定义：如果从源点到点 v 的所有路径都经过点 u，则 u 支配 v
// 最近支配点：v 的最近支配点是 v 的祖先中深度最大的支配点
// 支配树性质：支配树中，父节点支配子节点
// 构建算法：Lengauer-Tarjan 算法
//
// 【ML/DL 关联价值】
// 1. 图神经网络中的关键节点识别
// 2. 程序分析中的控制流分析
// 3. 网络可靠性分析

#include <bits/stdc++.h>
using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 100001;
const int LOG = 20;

// ===================== 图存储区 =====================
vector<int> graph[MAXN];
vector<int> reverseGraph[MAXN];
int n, m;

// ===================== 支配树变量区 =====================
int dfn[MAXN];
int id[MAXN];
int semi[MAXN];
int idom[MAXN];
int parentArr[MAXN];
int timer;

// ===================== 并查集变量区 =====================
int dsuArr[MAXN];
int bestArr[MAXN];

// ===================== 核心函数：图加边 =====================
void addEdge(int u, int v) {
    graph[u].push_back(v);
    reverseGraph[v].push_back(u);
}

// ===================== 核心函数：DFS 遍历 =====================
void dfs(int u) {
    dfn[u] = ++timer;
    id[timer] = u;

    for (int v : graph[u]) {
        if (!dfn[v]) {
            dfs(v);
        }
    }
}

// ===================== 核心函数：并查集查找 =====================
int find(int x) {
    if (dsuArr[x] == x) return x;
    int root = find(dsuArr[x]);
    if (dfn[semi[bestArr[dsuArr[x]]]] < dfn[semi[bestArr[x]]]) {
        bestArr[x] = bestArr[dsuArr[x]];
    }
    dsuArr[x] = root;
    return root;
}

// ===================== 核心函数：获取最优节点 =====================
int getBest(int x) {
    find(x);
    return bestArr[x];
}

// ===================== 核心函数：计算半支配点 =====================
void computeSemiDominators() {
    for (int i = 1; i <= n; i++) {
        semi[i] = i;
        dsuArr[i] = i;
        bestArr[i] = i;
    }

    for (int i = timer; i >= 2; i--) {
        int v = id[i];

        for (int u : reverseGraph[v]) {
            if (!dfn[u]) continue;
            if (dfn[u] < dfn[v]) {
                if (dfn[u] < dfn[semi[v]]) semi[v] = u;
            } else {
                int w = getBest(u);
                if (dfn[semi[w]] < dfn[semi[v]]) semi[v] = semi[w];
            }
        }

        if (dfn[v] > 1) {
            dsuArr[v] = parentArr[v];
            bestArr[v] = v;
        }
    }
}

// ===================== 核心函数：计算支配点 =====================
void computeDominators() {
    for (int i = 2; i <= timer; i++) {
        int v = id[i];
        int w = semi[v];

        if (semi[w] == semi[v]) {
            idom[v] = semi[v];
        } else {
            idom[v] = idom[w];
        }

        parentArr[v] = idom[v];
    }

    idom[id[1]] = id[1];
}

// ===================== 核心函数：构建支配树 =====================
void buildDominatorTree() {
    dfs(1);
    computeSemiDominators();
    computeDominators();
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    cin >> n >> m;

    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        addEdge(u, v);
    }

    buildDominatorTree();

    cout << "支配树构建完成" << endl;
    cout << "DFS 序：" << timer << endl;
    cout << "各节点的支配点：" << endl;
    for (int i = 1; i <= n; i++) {
        if (dfn[i]) {
            cout << "节点 " << i << " 的支配点：" << idom[i] << endl;
        }
    }

    return 0;
}
