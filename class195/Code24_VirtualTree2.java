package class195;

// 虚树优化建图基础模板，C++ 版
// 本代码展示虚树优化建图的核心模板，用于解决树上关键点问题
// 测试链接 : https://www.luogu.com.cn/problem/P2495（改编）
// 如下实现是 C++ 的版本
// 提交如下代码，可以通过所有测试用例

// ===================== 虚树优化建图核心知识点（C++ 版） =====================
// 【问题分析】
// 虚树用于解决树上只涉及部分关键点的问题
// 通过只保留关键点和它们的 LCA，大幅减少节点数
//
// 【核心原理】
// 关键点选择：保留所有询问的关键点
// LCA 添加：添加所有关键点对的 LCA
// 拓扑保持：保持原树中的祖先 - 后代关系
//
// 【ML/DL 关联价值】
// 1. 图神经网络中的关键子图提取
// 2. 树结构数据中的特征压缩
// 3. 层次聚类中的关键节点选择

#include <bits/stdc++.h>
using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 200001;
const int LOG = 20;

// ===================== 原树存储区 =====================
vector<pair<int, int>> tree[MAXN];
int n;

// ===================== 虚树存储区 =====================
vector<pair<int, int>> virtualTree[MAXN];
vector<int> keyPoints;

// ===================== 倍增 LCA 变量区 =====================
int parent[MAXN][LOG];
int depth[MAXN];
int dist[MAXN];

// ===================== 虚树变量区 =====================
int stk[MAXN];
int top;
bool isKey[MAXN];

// ===================== 核心函数：原树加边 =====================
void addEdge(int u, int v, int w) {
    tree[u].emplace_back(v, w);
    tree[v].emplace_back(u, w);
}

// ===================== 核心函数：虚树加边 =====================
void addVirtualEdge(int u, int v, int w) {
    virtualTree[u].emplace_back(v, w);
}

// ===================== 核心函数：DFS 预处理 =====================
void dfs(int u, int p, int d, int distance) {
    depth[u] = d;
    parent[u][0] = p;
    dist[u] = distance;

    for (int j = 1; j < LOG; j++) {
        if (parent[u][j - 1] != 0) {
            parent[u][j] = parent[parent[u][j - 1]][j - 1];
        }
    }

    for (auto& edge : tree[u]) {
        int v = edge.first;
        int w = edge.second;
        if (v != p) {
            dfs(v, u, d + 1, distance + w);
        }
    }
}

// ===================== 核心函数：LCA 查询 =====================
int getLCA(int u, int v) {
    if (depth[u] < depth[v]) {
        swap(u, v);
    }

    int diff = depth[u] - depth[v];
    for (int j = 0; j < LOG; j++) {
        if (diff & (1 << j)) {
            u = parent[u][j];
        }
    }

    if (u == v) return u;

    for (int j = LOG - 1; j >= 0; j--) {
        if (parent[u][j] != parent[v][j]) {
            u = parent[u][j];
            v = parent[v][j];
        }
    }

    return parent[u][0];
}

// ===================== 核心函数：计算两点距离 =====================
int getDistance(int u, int v) {
    int lca = getLCA(u, v);
    return dist[u] + dist[v] - 2 * dist[lca];
}

// ===================== 核心函数：构建虚树 =====================
void buildVirtualTree() {
    for (int key : keyPoints) {
        virtualTree[key].clear();
        isKey[key] = true;
    }

    sort(keyPoints.begin(), keyPoints.end());

    vector<int> allPoints = keyPoints;
    for (size_t i = 0; i < keyPoints.size() - 1; i++) {
        int lca = getLCA(keyPoints[i], keyPoints[i + 1]);
        if (find(allPoints.begin(), allPoints.end(), lca) == allPoints.end()) {
            allPoints.push_back(lca);
        }
    }
    sort(allPoints.begin(), allPoints.end());

    top = 0;
    stk[++top] = allPoints[0];

    for (size_t i = 1; i < allPoints.size(); i++) {
        int u = allPoints[i];
        while (top > 1 && depth[stk[top - 1]] >= depth[u]) {
            top--;
        }
        if (top > 0) {
            int d = getDistance(stk[top], u);
            addVirtualEdge(stk[top], u, d);
        }
        stk[++top] = u;
    }
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    // 读入节点数
    cin >> n;

    // 读入 n-1 条边
    for (int i = 0; i < n - 1; i++) {
        int u, v, w;
        cin >> u >> v >> w;
        addEdge(u, v, w);
    }

    // DFS 预处理
    dfs(1, 0, 0, 0);

    // 读入关键点
    int k;
    cin >> k;
    for (int i = 0; i < k; i++) {
        int point;
        cin >> point;
        keyPoints.push_back(point);
    }

    // 构建虚树
    buildVirtualTree();

    // 输出虚树信息
    cout << "虚树节点数：" << keyPoints.size() << endl;
    cout << "虚树边信息：" << endl;
    for (int u : keyPoints) {
        for (auto& edge : virtualTree[u]) {
            cout << u << " -> " << edge.first << " (距离：" << edge.second << ")" << endl;
        }
    }

    return 0;
}
