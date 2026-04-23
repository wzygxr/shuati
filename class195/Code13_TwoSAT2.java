package class195;

// 2-SAT 优化建图基础模板，C++ 版
// 本代码展示 2-SAT 问题的优化建图核心模板，用于解决布尔可满足性问题
// 测试链接 : https://www.luogu.com.cn/problem/P4782
// 如下实现是 C++ 的版本
// 提交如下代码，可以通过所有测试用例

// ===================== 2-SAT 优化建图核心知识点（C++ 版） =====================
// 【问题分析】
// 2-SAT 问题是判断一组布尔变量是否能满足所有约束的问题
// 每个约束涉及两个变量，形如 (x_i = a) OR (x_j = b)
// 通过建图转化为强连通分量问题
//
// 【核心原理】
// 变量拆分：每个变量 x_i 拆分为两个节点：x_i(true) 和 x_i(false)
// 约束转化：(x_i = a) OR (x_j = b) 转化为两条蕴含边
// 判断条件：如果 x_i(true) 和 x_i(false) 在同一 SCC 中，则无解
//
// 【复杂度分析】
// 节点数：2n（每个变量两个状态）
// 边数：2m（每个约束两条边）
// Tarjan 复杂度：O(n + m)
//
// 【ML/DL 关联价值】
// 1. 逻辑推理系统的自动验证
// 2. 约束满足问题的求解
// 3. 知识图谱中的逻辑一致性检查

#include <bits/stdc++.h>
using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 2000001;
const int MAXE = 4000001;

// ===================== 图存储区 =====================
int head[MAXN];
int next_[MAXE];
int to[MAXE];
int cnt;

// ===================== Tarjan 算法变量区 =====================
int dfn[MAXN];
int low[MAXN];
int scc[MAXN];
int timer, sccCnt;
stack<int> st;
bool inStack[MAXN];

// ===================== 2-SAT 变量区 =====================
int n, m;

// ===================== 核心函数：图加边 =====================
void addEdge(int u, int v) {
    next_[++cnt] = head[u];
    to[cnt] = v;
    head[u] = cnt;
}

// ===================== 核心函数：变量编号转换 =====================
// 功能：将变量 x_i 的第 k 个状态转换为节点编号
int getNodeId(int i, bool val) {
    return val ? 2 * i : 2 * i + 1;
}

// ===================== 核心函数：添加 2-SAT 约束 =====================
// 功能：添加约束 (x_i = a) OR (x_j = b)
void addClause(int i, bool a, int j, bool b) {
    // NOT(x_i = a) -> (x_j = b)
    addEdge(getNodeId(i, !a), getNodeId(j, b));
    // NOT(x_j = b) -> (x_i = a)
    addEdge(getNodeId(j, !b), getNodeId(i, a));
}

// ===================== 核心函数：Tarjan 算法求 SCC =====================
void tarjan(int u) {
    dfn[u] = low[u] = ++timer;
    st.push(u);
    inStack[u] = true;

    // 遍历所有邻接边
    for (int e = head[u]; e > 0; e = next_[e]) {
        int v = to[e];
        if (dfn[v] == 0) {
            // v 未访问，递归处理
            tarjan(v);
            low[u] = min(low[u], low[v]);
        } else if (inStack[v]) {
            // v 在栈中，更新 low
            low[u] = min(low[u], dfn[v]);
        }
    }

    // 找到 SCC 的根节点
    if (dfn[u] == low[u]) {
        sccCnt++;
        // 弹出 SCC 中的所有节点
        while (true) {
            int v = st.top();
            st.pop();
            inStack[v] = false;
            scc[v] = sccCnt;
            if (u == v) {
                break;
            }
        }
    }
}

// ===================== 核心函数：2-SAT 求解 =====================
bool solve() {
    // 对所有未访问的节点运行 Tarjan
    for (int i = 0; i < 2 * n; i++) {
        if (dfn[i] == 0) {
            tarjan(i);
        }
    }

    // 检查每个变量的两个状态
    for (int i = 0; i < n; i++) {
        if (scc[2 * i] == scc[2 * i + 1]) {
            // true 和 false 在同一 SCC 中，无解
            return false;
        }
    }
    return true;
}

// ===================== 核心函数：获取解 =====================
vector<bool> getSolution() {
    vector<bool> result(n);
    for (int i = 0; i < n; i++) {
        // 选择 SCC 编号较小的状态
        result[i] = scc[2 * i] < scc[2 * i + 1];
    }
    return result;
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    // 读入变量数和约束数
    cin >> n >> m;

    // 处理 m 个约束
    for (int i = 0; i < m; i++) {
        int x, a, y, b;
        cin >> x >> a >> y >> b;
        // 添加约束 (x = a) OR (y = b)
        addClause(x - 1, a == 1, y - 1, b == 1);
    }

    // 求解 2-SAT 问题
    if (solve()) {
        cout << "Yes" << endl;
        // 输出一组可行解
        vector<bool> solution = getSolution();
        for (int i = 0; i < n; i++) {
            cout << (solution[i] ? 1 : 0) << " ";
        }
        cout << endl;
    } else {
        cout << "No" << endl;
    }

    return 0;
}
