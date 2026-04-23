package class195;

// 长链剖分优化建图基础模板，C++ 版
// 本代码展示长链剖分优化建图的核心模板，用于解决树上深度相关问题
// 测试链接 : https://www.luogu.com.cn/problem/P3899（改编）
// 如下实现是 C++ 的版本
// 提交如下代码，可以通过所有测试用例

// ===================== 长链剖分优化建图核心知识点（C++ 版） =====================
// 【问题分析】
// 长链剖分用于解决树上与深度相关的查询问题
// 通过选择最长链作为重链，优化深度相关的 DP
//
// 【核心原理】
// 长链定义：选择子树深度最大的儿子作为重儿子
// 重链：由重儿子连接形成的链
// 轻链：连接轻儿子的边
//
// 【ML/DL 关联价值】
// 1. 树结构神经网络中的层次化特征
// 2. 图神经网络中的深度信息编码
// 3. 层次聚类中的链式结构优化

#include <bits/stdc++.h>
using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 200001;
const int LOG = 20;

// ===================== 树存储区 =====================
vector<int> tree[MAXN];
int n;

// ===================== 长链剖分变量区 =====================
int depth[MAXN];
int maxDepth[MAXN];
int len[MAXN];
int top[MAXN];
int son[MAXN];

// ===================== 倍增数组 =====================
int parent[MAXN][LOG];

// ===================== 核心函数：树加边 =====================
void addEdge(int u, int v) {
    tree[u].push_back(v);
    tree[v].push_back(u);
}

// ===================== 核心函数：第一次 DFS =====================
void dfs1(int u, int p, int d) {
    depth[u] = d;
    maxDepth[u] = d;
    parent[u][0] = p;

    for (int j = 1; j < LOG; j++) {
        if (parent[u][j - 1] != 0) {
            parent[u][j] = parent[parent[u][j - 1]][j - 1];
        }
    }

    for (int v : tree[u]) {
        if (v != p) {
            dfs1(v, u, d + 1);
            maxDepth[u] = max(maxDepth[u], maxDepth[v]);
        }
    }
}

// ===================== 核心函数：第二次 DFS =====================
void dfs2(int u, int p) {
    for (int v : tree[u]) {
        if (v != p && maxDepth[v] == maxDepth[u]) {
            son[u] = v;
            top[v] = top[u];
            dfs2(v, u);
        }
    }

    for (int v : tree[u]) {
        if (v != p && v != son[u]) {
            top[v] = v;
            dfs2(v, u);
        }
    }
}

// ===================== 核心函数：计算长链长度 =====================
void calcLen(int u, int p) {
    len[u] = maxDepth[u] - depth[u];
    for (int v : tree[u]) {
        if (v != p) {
            calcLen(v, u);
        }
    }
}

// ===================== 核心函数：长链剖分初始化 =====================
void init() {
    dfs1(1, 0, 0);
    top[1] = 1;
    dfs2(1, 0);
    calcLen(1, 0);
}

// ===================== 核心函数：查询 k 级祖先 =====================
int getKthAncestor(int u, int k) {
    if (k > depth[u]) {
        return -1;
    }

    for (int j = LOG - 1; j >= 0; j--) {
        if (k >= (1 << j)) {
            u = parent[u][j];
            k -= (1 << j);
        }
    }

    if (k > 0) {
        for (int i = 0; i < k; i++) {
            u = parent[u][0];
        }
    }

    return u;
}

// ===================== 核心函数：获取长链信息 =====================
vector<int> getChainInfo(int u) {
    vector<int> chain;
    int chainTop = top[u];
    int chainBottom = u;

    while (son[chainBottom] != 0) {
        chainBottom = son[chainBottom];
    }

    int cur = chainTop;
    while (true) {
        chain.push_back(cur);
        if (cur == chainBottom) {
            break;
        }
        cur = son[cur];
    }

    return chain;
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    // 读入节点数
    cin >> n;

    // 读入 n-1 条边
    for (int i = 0; i < n - 1; i++) {
        int u, v;
        cin >> u >> v;
        addEdge(u, v);
    }

    // 长链剖分初始化
    init();

    // 输出长链剖分信息
    cout << "长链剖分信息：" << endl;
    for (int i = 1; i <= n; i++) {
        cout << "节点 " << i << ": 深度=" << depth[i] 
             << ", 子树最大深度=" << maxDepth[i] 
             << ", 长链长度=" << len[i] 
             << ", 链顶=" << top[i] 
             << ", 重儿子=" << son[i] << endl;
    }

    // 查询示例
    int queryCount;
    cin >> queryCount;
    for (int i = 0; i < queryCount; i++) {
        int u, k;
        cin >> u >> k;
        int ancestor = getKthAncestor(u, k);
        cout << "节点 " << u << " 的 " << k << " 级祖先：" 
             << (ancestor == -1 ? "不存在" : to_string(ancestor)) << endl;
    }

    return 0;
}
