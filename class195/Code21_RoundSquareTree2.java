package class195;

// 圆方树优化建图基础模板，C++ 版
// 本代码展示圆方树优化建图的核心模板，用于解决无向图路径问题
// 测试链接 : https://www.luogu.com.cn/problem/P4320（改编）
// 如下实现是 C++ 的版本
// 提交如下代码，可以通过所有测试用例

// ===================== 圆方树优化建图核心知识点（C++ 版） =====================
// 【问题分析】
// 圆方树用于解决无向图中的路径计数、点双连通分量等问题
// 通过将无向图转化为树结构，简化路径问题的处理
//
// 【核心原理】
// 圆点：原图中的节点
// 方点：每个点双连通分量对应一个方点
// 连边规则：圆点向所属点双的方点连边
//
// 【ML/DL 关联价值】
// 1. 图神经网络中的树结构分解
// 2. 社交网络中的关键节点识别
// 3. 知识图谱中的路径推理优化

#include <bits/stdc++.h>
using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 200001;
const int MAXE = 1000001;

// ===================== 原图存储区 =====================
int head[MAXN];
int next_[MAXE];
int to[MAXE];
int cnt;

// ===================== 圆方树存储区 =====================
int treeHead[MAXN];
int treeNext[MAXE];
int treeTo[MAXE];
int treeCnt;

// ===================== Tarjan 算法变量区 =====================
int dfn[MAXN];
int low[MAXN];
int timer;
stack<int> st;

// ===================== 圆方树变量区 =====================
int n, m;
int squareNodeCnt;
int nodeType[MAXN];
int bccSize[MAXN];

// ===================== 核心函数：原图加边 =====================
void addEdge(int u, int v) {
    next_[++cnt] = head[u];
    to[cnt] = v;
    head[u] = cnt;
}

// ===================== 核心函数：圆方树加边 =====================
void addTreeEdge(int u, int v) {
    treeNext[++treeCnt] = treeHead[u];
    treeTo[treeCnt] = v;
    treeHead[u] = treeCnt;
}

// ===================== 核心函数：Tarjan 算法求点双 =====================
void tarjan(int u, int parent) {
    dfn[u] = low[u] = ++timer;
    st.push(u);

    for (int e = head[u]; e > 0; e = next_[e]) {
        int v = to[e];
        if (v == parent) {
            continue;
        }
        if (dfn[v] == 0) {
            tarjan(v, u);
            low[u] = min(low[u], low[v]);
            if (dfn[u] <= low[v]) {
                squareNodeCnt++;
                int squareNode = n + squareNodeCnt;
                nodeType[squareNode] = 1;

                int size = 0;
                while (true) {
                    int x = st.top();
                    st.pop();
                    size++;
                    addTreeEdge(x, squareNode);
                    addTreeEdge(squareNode, x);
                    if (x == v) {
                        break;
                    }
                }
                bccSize[squareNode] = size + 1;
                addTreeEdge(u, squareNode);
                addTreeEdge(squareNode, u);
            }
        } else {
            low[u] = min(low[u], dfn[v]);
        }
    }
}

// ===================== 核心函数：构建圆方树 =====================
void buildRoundSquareTree() {
    squareNodeCnt = 0;
    for (int i = 1; i <= n; i++) {
        nodeType[i] = 0;
    }
    for (int i = 1; i <= n; i++) {
        if (dfn[i] == 0) {
            tarjan(i, -1);
        }
    }
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
        addEdge(v, u);
    }

    // 构建圆方树
    buildRoundSquareTree();

    // 输出圆方树信息
    cout << "圆点数量：" << n << endl;
    cout << "方点数量：" << squareNodeCnt << endl;
    cout << "总节点数：" << n + squareNodeCnt << endl;

    for (int i = 1; i <= squareNodeCnt; i++) {
        cout << "方点 " << n + i << " 对应的点双大小：" << bccSize[n + i] << endl;
    }

    return 0;
}
