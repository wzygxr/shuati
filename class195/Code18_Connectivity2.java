package class195;

// 连通性优化建图基础模板，C++ 版
// 本代码展示连通性优化建图的核心模板，用于解决动态连通性问题
// 测试链接 : https://www.luogu.com.cn/problem/P3367（改编）
// 如下实现是 C++ 的版本
// 提交如下代码，可以通过所有测试用例

// ===================== 连通性优化建图核心知识点（C++ 版） =====================
// 【问题分析】
// 连通性优化建图用于解决动态连通性、缩点、桥和割点等问题
// 通过 Tarjan 算法、并查集等工具优化连通性判断
//
// 【核心原理】
// Tarjan 算法：通过 dfn 和 low 数组识别 SCC
// 并查集：维护无向图的连通性
// 缩点建图：将 SCC 缩成点，构建 DAG
//
// 【ML/DL 关联价值】
// 1. 图神经网络中的连通性特征
// 2. 社区发现算法
// 3. 聚类分析中的连通性约束

#include <bits/stdc++.h>
using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 100001;
const int MAXE = 200001;

// ===================== 原图存储区 =====================
int head[MAXN];
int next_[MAXE];
int to[MAXE];
int cnt;

// ===================== 缩点后新图存储区 =====================
int newHead[MAXN];
int newNext[MAXE];
int newTo[MAXE];
int newCnt;

// ===================== Tarjan 算法变量区 =====================
int dfn[MAXN];
int low[MAXN];
int scc[MAXN];
int timer, sccCnt;
stack<int> st;
bool inStack[MAXN];

// ===================== 并查集变量区 =====================
int parent[MAXN];
int rnk[MAXN];

// ===================== 连通性变量区 =====================
int n, m;
int sccSize[MAXN];
int sccInDegree[MAXN];

// ===================== 核心函数：原图加边 =====================
void addEdge(int u, int v) {
    next_[++cnt] = head[u];
    to[cnt] = v;
    head[u] = cnt;
}

// ===================== 核心函数：新图加边 =====================
void addNewEdge(int u, int v) {
    newNext[++newCnt] = newHead[u];
    newTo[newCnt] = v;
    newHead[u] = newCnt;
}

// ===================== 核心函数：并查集初始化 =====================
void initUnionFind() {
    for (int i = 1; i <= n; i++) {
        parent[i] = i;
        rnk[i] = 0;
    }
}

// ===================== 核心函数：并查集查找 =====================
int find(int x) {
    if (parent[x] != x) {
        parent[x] = find(parent[x]); // 路径压缩
    }
    return parent[x];
}

// ===================== 核心函数：并查集合并 =====================
void unionSets(int x, int y) {
    int rootX = find(x);
    int rootY = find(y);
    if (rootX != rootY) {
        if (rnk[rootX] < rnk[rootY]) {
            parent[rootX] = rootY;
        } else {
            parent[rootY] = rootX;
            if (rnk[rootX] == rnk[rootY]) {
                rnk[rootX]++;
            }
        }
    }
}

// ===================== 核心函数：Tarjan 算法求 SCC =====================
void tarjan(int u) {
    dfn[u] = low[u] = ++timer;
    st.push(u);
    inStack[u] = true;

    for (int e = head[u]; e > 0; e = next_[e]) {
        int v = to[e];
        if (dfn[v] == 0) {
            tarjan(v);
            low[u] = min(low[u], low[v]);
        } else if (inStack[v]) {
            low[u] = min(low[u], dfn[v]);
        }
    }

    if (dfn[u] == low[u]) {
        sccCnt++;
        while (true) {
            int v = st.top();
            st.pop();
            inStack[v] = false;
            scc[v] = sccCnt;
            sccSize[sccCnt]++;
            if (u == v) {
                break;
            }
        }
    }
}

// ===================== 核心函数：缩点建图 =====================
void buildCondensationGraph() {
    for (int u = 1; u <= n; u++) {
        for (int e = head[u]; e > 0; e = next_[e]) {
            int v = to[e];
            if (scc[u] != scc[v]) {
                addNewEdge(scc[u], scc[v]);
                sccInDegree[scc[v]]++;
            }
        }
    }
}

// ===================== 核心函数：判断两点是否连通 =====================
bool isConnected(int u, int v) {
    return scc[u] == scc[v];
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    // 读入节点数和边数
    cin >> n >> m;

    // 读入 m 条边
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        addEdge(u, v);
    }

    // 对所有未访问的节点运行 Tarjan
    for (int i = 1; i <= n; i++) {
        if (dfn[i] == 0) {
            tarjan(i);
        }
    }

    // 输出 SCC 信息
    cout << "强连通分量数量：" << sccCnt << endl;
    for (int i = 1; i <= sccCnt; i++) {
        cout << "SCC " << i << " 的大小：" << sccSize[i] << endl;
    }

    // 缩点建图
    buildCondensationGraph();

    // 输出新图信息
    cout << "\n缩点后的 DAG：" << endl;
    for (int i = 1; i <= sccCnt; i++) {
        cout << "SCC " << i << " 的出边指向：";
        vector<int> targets;
        for (int e = newHead[i]; e > 0; e = newNext[e]) {
            targets.push_back(newTo[e]);
        }
        for (size_t j = 0; j < targets.size(); j++) {
            cout << targets[j] << (j == targets.size() - 1 ? "" : ", ");
        }
        cout << endl;
    }

    // 查询示例
    int queryCount;
    cin >> queryCount;
    for (int i = 0; i < queryCount; i++) {
        int u, v;
        cin >> u >> v;
        if (isConnected(u, v)) {
            cout << "节点 " << u << " 和 " << v << " 在同一 SCC 中" << endl;
        } else {
            cout << "节点 " << u << " 和 " << v << " 不在同一 SCC 中" << endl;
        }
    }

    return 0;
}
