### 题目链接
[POJ 1523 SPF](http://poj.org/problem?id=1523)

### 题目描述
给定一个无向图，求出所有割点以及除去每个割点后的连通块数量。

### 笔试/面试考察点分析
- 考察割点判定的核心逻辑
- 考察点双连通分量的提取方法
- 考察无向图连通性分析
- 常见坑点：重边/自环处理、根节点特殊处理

### 解题思路
1. 使用Tarjan算法求割点
2. 对于每个割点，统计删除后连通块数量
3. 利用点双连通分量缩点，将图转化为树结构
4. 树结构中每个割点的子树数量即为删除后连通块数量

### 完整代码实现
```cpp
#include <iostream>
#include <vector>
#include <stack>
#include <algorithm>
#include <cstring>
using namespace std;

const int MAXN = 1005; // 节点最大数量
vector<int> adj[MAXN]; // 邻接表
int dfn[MAXN], low[MAXN], timestamp = 0; // 时间戳数组
bool is_cut[MAXN]; // 标记是否为割点
int cut_cnt = 0; // 割点总数
int child[MAXN]; // 子节点数量（根节点割点判定）

// Tarjan算法求割点
void tarjan(int u, int fa) {
    dfn[u] = low[u] = ++timestamp; // 初始化时间戳
    child[u] = 0; // 子节点计数
    for (int v : adj[u]) { // 遍历邻接节点
        if (v == fa) continue; // 跳过父节点
        if (!dfn[v]) { // 未访问过
            child[u]++; // 子节点+1
            tarjan(v, u); // 递归遍历
            low[u] = min(low[u], low[v]); // 更新low值
            // 割点判定
            if (fa == -1 && child[u] >= 2) is_cut[u] = true; // 根节点
            if (fa != -1 && low[v] >= dfn[u]) is_cut[u] = true; // 非根节点
        } else { // 已访问过，更新low值
            low[u] = min(low[u], dfn[v]);
        }
    }
}

// 统计割点总数
void count_cut(int n) {
    cut_cnt = 0;
    for (int i = 1; i <= n; i++) {
        if (is_cut[i]) cut_cnt++;
    }
}

int main() {
    int n, m, case_num = 0;
    while (cin >> n && n != 0) {
        // 初始化
        memset(dfn, 0, sizeof(dfn));
        memset(low, 0, sizeof(low));
        memset(is_cut, false, sizeof(is_cut));
        for (int i = 1; i <= n; i++) adj[i].clear();
        timestamp = 0;
        
        // 读取边
        int u, v;
        while (cin >> u && u != 0) {
            cin >> v;
            adj[u].push_back(v);
            adj[v].push_back(u);
        }
        
        // 处理每个连通块
        for (int i = 1; i <= n; i++) {
            if (!dfn[i]) {
                tarjan(i, -1);
            }
        }
        
        // 统计割点
        count_cut(n);
        
        // 输出结果
        cout << "Network #" << ++case_num << endl;
        if (cut_cnt == 0) {
            cout << "  No SPF nodes" << endl;
        } else {
            for (int i = 1; i <= n; i++) {
                if (is_cut[i]) {
                    // 计算删除割点后的连通块数量
                    int cnt = 0;
                    for (int v : adj[i]) {
                        if (low[v] >= dfn[i]) cnt++;
                    }
                    cout << "  SPF node " << i << " leaves " << cnt << " subnets" << endl;
                }
            }
        }
        cout << endl;
    }
    return 0;
}
```

### 代码逐行注释
```cpp
#include <iostream>
#include <vector>
#include <stack>
#include <algorithm>
#include <cstring>
using namespace std;

const int MAXN = 1005; // 节点最大数量，适配题目数据范围
vector<int> adj[MAXN]; // 邻接表存储无向图
int dfn[MAXN], low[MAXN], timestamp = 0; // dfn[u]节点u的发现时间，low[u]节点u可回溯的最早时间
bool is_cut[MAXN]; // 标记节点是否为割点
int cut_cnt = 0; // 割点总数统计
int child[MAXN]; // 统计根节点的子节点数量

// Tarjan算法求割点：核心逻辑
// u：当前节点，fa：父节点
void tarjan(int u, int fa) {
    dfn[u] = low[u] = ++timestamp; // 初始化时间戳：发现时间=可回溯最早时间
    child[u] = 0; // 子节点计数初始化
    for (int v : adj[u]) { // 遍历当前节点的所有邻接节点
        if (v == fa) continue; // 跳过父节点，避免循环访问
        if (!dfn[v]) { // 邻接节点未被访问过
            child[u]++; // 子节点数量+1
            tarjan(v, u); // 递归遍历子节点
            low[u] = min(low[u], low[v]); // 回溯更新low[u]：取子节点可回溯的最早时间
            // 割点判定逻辑
            // 情况1：根节点，且子节点数≥2（去掉根节点，子树变为多个连通块）
            if (fa == -1 && child[u] >= 2) is_cut[u] = true;
            // 情况2：非根节点，存在子节点v，low[v] ≥ dfn[u]（去掉u，v所在子树独立）
            if (fa != -1 && low[v] >= dfn[u]) is_cut[u] = true;
        } else { // 邻接节点已被访问过，更新low[u]为较小的时间戳
            low[u] = min(low[u], dfn[v]);
        }
    }
}

// 统计割点总数
void count_cut(int n) {
    cut_cnt = 0;
    for (int i = 1; i <= n; i++) {
        if (is_cut[i]) cut_cnt++;
    }
}

int main() {
    int n, m, case_num = 0;
    while (cin >> n && n != 0) { // 多组数据输入
        // 初始化核心数组，避免脏数据
        memset(dfn, 0, sizeof(dfn));
        memset(low, 0, sizeof(low));
        memset(is_cut, false, sizeof(is_cut));
        for (int i = 1; i <= n; i++) adj[i].clear(); // 清空邻接表
        timestamp = 0; // 时间戳重置
        
        // 读取边数据
        int u, v;
        while (cin >> u && u != 0) {
            cin >> v;
            adj[u].push_back(v);
            adj[v].push_back(u); // 无向图双向存储
        }
        
        // 处理每个连通块（非连通图场景）
        for (int i = 1; i <= n; i++) {
            if (!dfn[i]) { // 未访问过的节点作为连通块根节点
                tarjan(i, -1); // 根节点父节点设为-1
            }
        }
        
        // 统计割点总数
        count_cut(n);
        
        // 输出结果
        cout << "Network #" << ++case_num << endl;
        if (cut_cnt == 0) {
            cout << "  No SPF nodes" << endl;
        } else {
            for (int i = 1; i <= n; i++) {
                if (is_cut[i]) {
                    // 计算删除割点后的连通块数量：统计子树数量
                    int cnt = 0;
                    for (int v : adj[i]) {
                        if (low[v] >= dfn[i]) cnt++;
                    }
                    cout << "  SPF node " << i << " leaves " << cnt << " subnets" << endl;
                }
            }
        }
        cout << endl;
    }
    return 0;
}
```

### 时间/空间复杂度分析
- 时间复杂度：O(V + E)，其中V为节点数，E为边数
- 空间复杂度：O(V)，用于存储邻接表和时间戳数组

### 同类题目拓展
- [POJ 2942 Knights of the Round Table](http://poj.org/problem?id=2942)（点双连通分量+二分图染色）
- [HDU 3844 Mining Your Own Business](http://acm.hdu.edu.cn/showproblem.php?pid=3844)（点双连通分量+逃生装置安装）

### ML/DL关联思考
- 割点识别可用于图结构数据的关键节点提取
- 点双连通分量可作为图社区划分的基础
- 在GNN模型中，割点可作为重要的节点特征，提升模型对图结构的理解
- 点双连通分量的社区特征可用于图分类任务，提升模型性能