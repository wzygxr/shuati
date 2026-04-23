### 题目链接
[AcWing 395 冗余路径](https://www.acwing.com/problem/content/397/)

### 题目描述
给定一个无向图，求最少需要添加多少条边，使得图变为边双连通图。

### 笔试/面试考察点分析
- 考察边双连通分量的应用
- 考察割边判定
- 考察图的连通性分析
- 常见坑点：重边/自环处理、根节点特殊处理

### 解题思路
1. 使用Tarjan算法求割边
2. 将每个边双连通分量缩为一个点
3. 构建缩点后的树结构
4. 统计树中叶子节点数量
5. 最少需要添加的边数为(叶子节点数量+1)/2

### 完整代码实现
```cpp
#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
using namespace std;

const int MAXN = 5005; // 节点最大数量
vector<int> adj[MAXN]; // 邻接表
int dfn[MAXN], low[MAXN], timestamp; // 时间戳数组
bool is_bridge[MAXN * 2]; // 标记是否为割边
int bridge_cnt; // 割边总数
int dcc_cnt; // 边双连通分量数量
int dcc_id[MAXN]; // 每个节点所属边双连通分量ID
int degree[MAXN]; // 缩点后树的度数

// Tarjan算法求割边
void tarjan(int u, int fa) {
    dfn[u] = low[u] = ++timestamp; // 初始化时间戳
    for (int i = 0; i < adj[u].size(); i++) { // 遍历邻接节点
        int v = adj[u][i];
        if (v == fa) continue; // 跳过父节点
        if (!dfn[v]) { // 未访问过
            tarjan(v, u); // 递归遍历
            low[u] = min(low[u], low[v]); // 更新low值
            // 割边判定
            if (low[v] > dfn[u]) {
                is_bridge[i] = true;
                is_bridge[(i ^ 1)] = true; // 无向图双向标记
                bridge_cnt++;
            }
        } else { // 已访问过，更新low值
            low[u] = min(low[u], dfn[v]);
        }
    }
}

// 缩点
void dfs(int u, int id) {
    dcc_id[u] = id;
    for (int i = 0; i < adj[u].size(); i++) {
        int v = adj[u][i];
        if (dcc_id[v] == -1 && !is_bridge[i]) {
            dfs(v, id);
        }
    }
}

int main() {
    int n, m;
    while (cin >> n >> m && n != 0) {
        // 初始化
        memset(dfn, 0, sizeof(dfn));
        memset(low, 0, sizeof(low));
        memset(is_bridge, false, sizeof(is_bridge));
        memset(dcc_id, -1, sizeof(dcc_id));
        memset(degree, 0, sizeof(degree));
        for (int i = 1; i <= n; i++) adj[i].clear();
        timestamp = 0;
        bridge_cnt = 0;
        dcc_cnt = 0;
        
        // 读取边
        for (int i = 0; i < m; i++) {
            int u, v;
            cin >> u >> v;
            adj[u].push_back(v);
            adj[v].push_back(u);
        }
        
        // 处理每个连通块
        for (int i = 1; i <= n; i++) {
            if (!dfn[i]) {
                tarjan(i, -1);
            }
        }
        
        // 缩点
        for (int i = 1; i <= n; i++) {
            if (dcc_id[i] == -1) {
                dcc_cnt++;
                dfs(i, dcc_cnt);
            }
        }
        
        // 统计缩点后树的度数
        for (int u = 1; u <= n; u++) {
            for (int i = 0; i < adj[u].size(); i++) {
                int v = adj[u][i];
                if (dcc_id[u] != dcc_id[v]) {
                    degree[dcc_id[u]]++;
                }
            }
        }
        
        // 统计叶子节点数量
        int leaf = 0;
        for (int i = 1; i <= dcc_cnt; i++) {
            if (degree[i] == 1) leaf++;
        }
        
        // 计算最少需要添加的边数
        int ans = (leaf + 1) / 2;
        cout << ans << endl;
    }
    return 0;
}
```

### 代码逐行注释
```cpp
#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
using namespace std;

const int MAXN = 5005; // 节点最大数量，适配题目数据范围
vector<int> adj[MAXN]; // 邻接表存储无向图
int dfn[MAXN], low[MAXN], timestamp; // dfn[u]节点u的发现时间，low[u]节点u可回溯的最早时间
bool is_bridge[MAXN * 2]; // 标记是否为割边
int bridge_cnt; // 割边总数统计
int dcc_cnt; // 边双连通分量数量
int dcc_id[MAXN]; // 每个节点所属边双连通分量ID
int degree[MAXN]; // 缩点后树的度数

// Tarjan算法求割边：核心逻辑
// u：当前节点，fa：父节点
void tarjan(int u, int fa) {
    dfn[u] = low[u] = ++timestamp; // 初始化时间戳：发现时间=可回溯最早时间
    for (int i = 0; i < adj[u].size(); i++) { // 遍历当前节点的所有邻接节点
        int v = adj[u][i]; // 邻接节点
        if (v == fa) continue; // 跳过父节点，避免循环访问
        if (!dfn[v]) { // 邻接节点未被访问过
            tarjan(v, u); // 递归遍历子节点
            low[u] = min(low[u], low[v]); // 回溯更新low[u]：取子节点可回溯的最早时间
            // 割边判定逻辑
            if (low[v] > dfn[u]) { // 当low[v] > dfn[u]时，u-v边为割边
                is_bridge[i] = true; // 标记当前边为割边
                is_bridge[(i ^ 1)] = true; // 无向图双向标记（i^1为反向边）
                bridge_cnt++; // 割边总数+1
            }
        } else { // 邻接节点已被访问过，更新low[u]为较小的时间戳
            low[u] = min(low[u], dfn[v]);
        }
    }
}

// 缩点：将每个边双连通分量缩为一个点
// u：当前节点，id：边双连通分量ID
void dfs(int u, int id) {
    dcc_id[u] = id; // 标记当前节点所属边双连通分量ID
    for (int i = 0; i < adj[u].size(); i++) { // 遍历邻接节点
        int v = adj[u][i]; // 邻接节点
        if (dcc_id[v] == -1 && !is_bridge[i]) { // 未标记且不是割边
            dfs(v, id); // 递归遍历
        }
    }
}

int main() {
    int n, m;
    while (cin >> n >> m && n != 0) { // 多组数据输入
        // 初始化核心数组，避免脏数据
        memset(dfn, 0, sizeof(dfn));
        memset(low, 0, sizeof(low));
        memset(is_bridge, false, sizeof(is_bridge));
        memset(dcc_id, -1, sizeof(dcc_id));
        memset(degree, 0, sizeof(degree));
        for (int i = 1; i <= n; i++) adj[i].clear(); // 清空邻接表
        timestamp = 0; // 时间戳重置
        bridge_cnt = 0; // 割边总数重置
        dcc_cnt = 0; // 边双连通分量数量重置
        
        // 读取边数据
        for (int i = 0; i < m; i++) {
            int u, v;
            cin >> u >> v;
            adj[u].push_back(v);
            adj[v].push_back(u); // 无向图双向存储
        }
        
        // 处理每个连通块（非连通图场景）
        for (int i = 1; i <= n; i++) {
            if (!dfn[i]) { // 未访问过的节点作为连通块根节点
                tarjan(i, -1); // 根节点父节点设为-1
            }
        }
        
        // 缩点：将每个边双连通分量缩为一个点
        for (int i = 1; i <= n; i++) {
            if (dcc_id[i] == -1) { // 未标记的节点
                dcc_cnt++; // 边双连通分量数量+1
                dfs(i, dcc_cnt); // 缩点
            }
        }
        
        // 统计缩点后树的度数
        for (int u = 1; u <= n; u++) {
            for (int i = 0; i < adj[u].size(); i++) {
                int v = adj[u][i]; // 邻接节点
                if (dcc_id[u] != dcc_id[v]) { // 两个节点属于不同边双连通分量
                    degree[dcc_id[u]]++; // 度数+1
                }
            }
        }
        
        // 统计叶子节点数量（度数为1的节点）
        int leaf = 0;
        for (int i = 1; i <= dcc_cnt; i++) {
            if (degree[i] == 1) leaf++;
        }
        
        // 计算最少需要添加的边数：(叶子节点数量+1)/2向上取整
        int ans = (leaf + 1) / 2;
        cout << ans << endl;
    }
    return 0;
}
```

### 时间/空间复杂度分析
- 时间复杂度：O(V + E)，其中V为节点数，E为边数
- 空间复杂度：O(V + E)，用于存储邻接表和边双连通分量

### 同类题目拓展
- [POJ 1523 SPF](http://poj.org/problem?id=1523)（割点判定）
- [HDU 3844 Mining Your Own Business](http://acm.hdu.edu.cn/showproblem.php?pid=3844)（点双连通分量应用）

### ML/DL关联思考
- 边双连通分量可作为图社区划分的基础
- 割边判定可用于图结构数据的关键边提取
- 在GNN模型中，边双连通分量可作为局部聚合单元，提升模型对图拓扑的理解
- 割边的关键边特征可用于图异常检测任务，提升模型性能