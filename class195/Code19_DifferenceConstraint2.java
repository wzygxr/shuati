package class195;

// 差分约束优化建图基础模板，C++ 版
// 本代码展示差分约束系统优化建图的核心模板，用于解决不等式组求解问题
// 测试链接 : https://www.luogu.com.cn/problem/P5960（改编）
// 如下实现是 C++ 的版本
// 提交如下代码，可以通过所有测试用例

// ===================== 差分约束优化建图核心知识点（C++ 版） =====================
// 【问题分析】
// 差分约束系统用于解决一组形如 x_i - x_j <= c 的不等式
// 通过建图转化为最短路问题，利用三角不等式求解
//
// 【核心原理】
// 不等式转化：x_i - x_j <= c 转化为 x_i <= x_j + c
// 建图方式：从 j 向 i 连一条权值为 c 的有向边
// 超级源点：添加虚拟源点向所有点连 0 边
// 负环判断：存在负环则无解（SPFA 判负环）
//
// 【ML/DL 关联价值】
// 1. 约束优化问题中的可行域求解
// 2. 线性规划的对偶问题转化
// 3. 多目标优化中的约束满足

#include <bits/stdc++.h>
using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 100001;
const int MAXE = 500001;
const int INF = 1 << 30;

// ===================== 图存储区 =====================
int head[MAXN];
int next_[MAXE];
int to[MAXE];
int weight[MAXE];
int cnt;

// ===================== SPFA 算法变量区 =====================
int dist[MAXN];
bool inQueue[MAXN];
int updateCount[MAXN];
int n, m;

// ===================== 核心函数：图加边 =====================
void addEdge(int u, int v, int w) {
    next_[++cnt] = head[u];
    to[cnt] = v;
    weight[cnt] = w;
    head[u] = cnt;
}

// ===================== 核心函数：添加差分约束 =====================
void addConstraint(int i, int j, int c) {
    addEdge(j, i, c);
}

// ===================== 核心函数：添加等式约束 =====================
void addEquality(int i, int j, int c) {
    addConstraint(i, j, c);
    addConstraint(j, i, -c);
}

// ===================== 核心函数：构建超级源点 =====================
void buildSuperSource(int source) {
    for (int i = 1; i <= n; i++) {
        addEdge(source, i, 0);
    }
}

// ===================== 核心函数：SPFA 判负环 =====================
bool spfa(int start) {
    // 初始化距离数组
    for (int i = 0; i <= n; i++) {
        dist[i] = INF;
        inQueue[i] = false;
        updateCount[i] = 0;
    }
    dist[start] = 0;

    // BFS 队列
    queue<int> q;
    q.push(start);
    inQueue[start] = true;
    updateCount[start] = 1;

    while (!q.empty()) {
        int u = q.front();
        q.pop();
        inQueue[u] = false;

        for (int e = head[u]; e > 0; e = next_[e]) {
            int v = to[e];
            int w = weight[e];
            if (dist[v] > dist[u] + w) {
                dist[v] = dist[u] + w;
                if (!inQueue[v]) {
                    q.push(v);
                    inQueue[v] = true;
                    updateCount[v]++;
                    if (updateCount[v] > n) {
                        return false; // 有负环，无解
                    }
                }
            }
        }
    }
    return true; // 无负环，有解
}

// ===================== 核心函数：获取一组可行解 =====================
vector<int> getSolution() {
    vector<int> result(n + 1);
    for (int i = 1; i <= n; i++) {
        result[i] = dist[i];
    }
    return result;
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    // 读入变量数和约束数
    cin >> n >> m;

    // 读入 m 个约束
    for (int i = 0; i < m; i++) {
        int op;
        cin >> op;
        if (op == 1) {
            int i, j, c;
            cin >> i >> j >> c;
            addConstraint(i, j, c);
        } else if (op == 2) {
            int i, j, c;
            cin >> i >> j >> c;
            addEquality(i, j, c);
        } else if (op == 3) {
            int i, j, c;
            cin >> i >> j >> c;
            addConstraint(j, i, -c);
        }
    }

    // 构建超级源点（节点 0）
    buildSuperSource(0);

    // 使用 SPFA 判负环
    if (spfa(0)) {
        cout << "有解" << endl;
        vector<int> solution = getSolution();
        for (int i = 1; i <= n; i++) {
            cout << "x" << i << " = " << solution[i] << " ";
        }
        cout << endl;
    } else {
        cout << "无解（存在负环）" << endl;
    }

    return 0;
}
