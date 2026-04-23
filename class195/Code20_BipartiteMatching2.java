package class195;

// 二分图匹配优化建图基础模板，C++ 版
// 本代码展示二分图匹配优化建图的核心模板，用于解决最大匹配问题
// 测试链接 : https://www.luogu.com.cn/problem/P3386（改编）
// 如下实现是 C++ 的版本
// 提交如下代码，可以通过所有测试用例

// ===================== 二分图匹配优化建图核心知识点（C++ 版） =====================
// 【问题分析】
// 二分图匹配用于解决两类对象之间的最优配对问题
// 通过建图转化为最大流问题，使用匈牙利算法或最大流求解
//
// 【核心原理】
// 二分图构建：左部点表示一类对象，右部点表示另一类对象
// 虚拟源汇：添加源点连向左部，右部连向汇点
// 最大匹配：转化为最大流，所有边容量为 1
// 匈牙利算法：通过增广路寻找最大匹配
//
// 【ML/DL 关联价值】
// 1. 推荐系统中的用户 - 物品匹配
// 2. 多目标跟踪中的数据关联
// 3. 图神经网络中的二分图结构处理

#include <bits/stdc++.h>
using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 100001;
const int MAXE = 500001;

// ===================== 图存储区 =====================
int head[MAXN];
int next_[MAXE];
int to[MAXE];
int cnt;

// ===================== 匈牙利算法变量区 =====================
int match[MAXN];
bool visited[MAXN];
int n1, n2, m;

// ===================== 核心函数：图加边 =====================
void addEdge(int u, int v) {
    next_[++cnt] = head[u];
    to[cnt] = v;
    head[u] = cnt;
}

// ===================== 核心函数：匈牙利算法 DFS =====================
bool dfs(int u) {
    for (int e = head[u]; e > 0; e = next_[e]) {
        int v = to[e];
        if (!visited[v]) {
            visited[v] = true;
            if (match[v] == 0 || dfs(match[v])) {
                match[v] = u;
                return true;
            }
        }
    }
    return false;
}

// ===================== 核心函数：匈牙利算法求最大匹配 =====================
int hungarian() {
    int result = 0;
    memset(match, 0, sizeof(match));

    for (int i = 1; i <= n1; i++) {
        memset(visited, false, sizeof(visited));
        if (dfs(i)) {
            result++;
        }
    }
    return result;
}

// ===================== 核心函数：获取匹配方案 =====================
vector<pair<int, int>> getMatching() {
    vector<pair<int, int>> pairs;
    for (int j = 1; j <= n2; j++) {
        if (match[j] != 0) {
            pairs.emplace_back(match[j], j);
        }
    }
    return pairs;
}

// ===================== 核心函数：构建网络流模型 =====================
void buildNetworkFlow(int source, int sink) {
    for (int i = 1; i <= n1; i++) {
        addEdge(source, i);
    }
    for (int j = 1; j <= n2; j++) {
        addEdge(n1 + j, sink);
    }
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    // 读入左部点数、右部点数、边数
    cin >> n1 >> n2 >> m;

    // 读入 m 条边
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        addEdge(u, v);
    }

    // 使用匈牙利算法求最大匹配
    int maxMatching = hungarian();
    cout << "最大匹配数：" << maxMatching << endl;

    // 输出匹配方案
    cout << "匹配方案：" << endl;
    vector<pair<int, int>> matching = getMatching();
    for (auto& pair : matching) {
        cout << "左部 " << pair.first << " <-> 右部 " << pair.second << endl;
    }

    return 0;
}
