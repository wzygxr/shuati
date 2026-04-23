# 【HDU】-5739-Fantasia-圆方树-进阶题

## （1）题目原始链接
- **题目链接**：http://acm.hdu.edu.cn/showproblem.php?pid=5739

## （2）题目完整描述
### 题目大意
给定一张有 $n$ 个点的无向图，每个点有点权 $w_i$。定义：
- 连通图的权值为所有顶点点权的乘积
- 无向图的权值为这个无向图的所有极大连通子图权值和

要求计算：$	ext{ans} = \\sum_{i=1}^{n} (i 	imes z_i) \\mod (10^9+7)$，其中 $z_i$ 是删去节点 $i$ 后此图的权值。

### 输入格式
第一行输入 $T$（测试用例数）。
每个测试用例：
- 第一行输入 $n, m$（节点数、边数）
- 第二行输入 $n$ 个整数 $w_1, w_2, \\dots, w_n$（每个点的权值）
- 接下来 $m$ 行，每行输入两个整数 $u, v$（表示无向边）

### 输出格式
对于每个测试用例，输出答案 $	ext{ans} \\mod (10^9+7)$。

### 数据范围
- $1 \\leq T \\leq 10$
- $1 \\leq n \\leq 10^5$
- $0 \\leq m \\leq 2 	imes 10^5$
- $1 \\leq w_i \\leq 10^9$

### 样例输入
```
1
3 2
1 2 3
1 2
2 3
```

### 样例输出
```
14
```

## （3）笔试/面试考察点分析
### 核心考察点
1. **圆方树构建**：基于点双连通分量的圆方树构建流程，圆节点与点双分量的对应关系
2. **割点定位**：通过圆方树中方节点（原始节点）的连接关系定位割点
3. **树上DP**：圆方树中的动态规划，计算连通子图权值
4. **复杂度分析**：大规模图（$10^5$ 节点）下圆方树构建的时间/空间复杂度优化
5. **ML/DL关联**：圆方树在图结构数据预处理中的应用，简化GNN模型的消息传递流程

### 常见坑点
- 未处理重边/自环场景，导致圆方树构建错误
- 未正确处理割点的多个点双分量归属，导致DP计算错误
- 模运算溢出，未使用longlong类型存储中间结果

## （4）解题思路
### 笔试答题逻辑
1. **圆方树构建**：使用Tarjan算法求点双连通分量，构建圆方树。每个点双分量对应一个圆节点，圆节点连接其包含的所有方节点（原始节点）。
2. **割点判定**：圆方树中，连接多个圆节点的方节点即为割点。
3. **树上DP**：在圆方树上进行动态规划，计算每个节点删除后的连通子图权值和。
4. **结果计算**：根据DP结果计算最终答案，注意模运算。

### 面试口述逻辑
- **圆方树构建**：“首先使用Tarjan算法提取所有点双连通分量，每个点双分量对应一个圆节点，圆节点连接其包含的所有原始节点（方节点），这样就将无向图转化为树结构。”
- **割点定位**：“圆方树中，割点对应连接多个圆节点的方节点，因为割点属于多个点双分量。”
- **DP求解**：“在圆方树上进行动态规划，每个节点的DP值表示以该节点为根的子树的连通子图权值和。删除割点后，其连接的每个圆节点对应的子树会成为独立的连通分量，因此需要分别计算这些子树的权值和。”

## （5）完整代码实现
```cpp
#include <iostream>
#include <vector>
#include <stack>
#include <algorithm>
#include <cstring>
using namespace std;

const int MOD = 1e9 + 7;
const int MAXN = 1e5 + 5;
const int MAXM = 4e5 + 5;

// 原始无向图边结构体
struct Edge {
    int to, rev, id;
    Edge(int _to, int _rev, int _id) : to(_to), rev(_rev), id(_id) {}
};
vector<Edge> adj[MAXN]; // 原始无向图邻接表
vector<pair<int, int>> tree[MAXN * 2]; // 圆方树邻接表（节点数最多为2*n）

int dfn[MAXN], low[MAXN], timestamp = 0;
stack<pair<int, int>> edge_stack;
int node_cnt; // 圆方树总节点数（初始为n，圆节点从n+1开始编号）
bool is_cut[MAXN]; // 标记原始节点是否为割点
long long w[MAXN]; // 每个点的权值
long long dp[MAXN * 2]; // 树上DP数组，dp[u]表示以u为根的子树的连通子图权值和
long long ans[MAXN]; // ans[i]表示删除节点i后的权值和

// 判断节点是否为圆节点
bool is_circle(int u, int n) {
    return u > n;
}

// Tarjan算法求点双连通分量，同步构建圆方树
void tarjan(int u, int fa_node, int n) {
    dfn[u] = low[u] = ++timestamp;
    int child = 0;
    for (auto &e : adj[u]) {
        int v = e.to, id = e.id;
        if (!dfn[v]) {
            edge_stack.push({u, v});
            child++;
            tarjan(v, u, n);
            low[u] = min(low[u], low[v]);
            
            // 割点判定+点双提取+圆方树构建
            if ((fa_node == -1 && child >= 2) || (fa_node != -1 && low[v] >= dfn[u])) {
                is_cut[u] = true;
                node_cnt++;
                int circle_node = node_cnt;
                while (true) {
                    auto [x, y] = edge_stack.top();
                    edge_stack.pop();
                    tree[x].emplace_back(circle_node, 1);
                    tree[circle_node].emplace_back(x, 1);
                    tree[y].emplace_back(circle_node, 1);
                    tree[circle_node].emplace_back(y, 1);
                    if (x == u && y == v) break;
                }
            }
        } else if (v != fa_node && dfn[v] < dfn[u]) {
            edge_stack.push({u, v});
            low[u] = min(low[u], dfn[v]);
        }
    }
}

// 处理根节点所在的点双分量
void handle_root(int root, int n) {
    if (!edge_stack.empty()) {
        node_cnt++;
        int circle_node = node_cnt;
        while (!edge_stack.empty()) {
            auto [x, y] = edge_stack.top();
            edge_stack.pop();
            tree[x].emplace_back(circle_node, 1);
            tree[circle_node].emplace_back(x, 1);
            tree[y].emplace_back(circle_node, 1);
            tree[circle_node].emplace_back(y, 1);
        }
    }
}

// 树上DP：计算每个节点删除后的权值和
void dfs_dp(int u, int father, int n) {
    if (u <= n) { // 方节点（原始节点）
        dp[u] = w[u]; // 初始权值为自身点权
        long long product = 1;
        for (auto &[v, w_edge] : tree[u]) {
            if (v == father) continue;
            dfs_dp(v, u, n);
            product = product * (dp[v] + 1) % MOD; // 子树的连通子图权值和+1（不选该子树的情况）
        }
        dp[u] = (product - 1 + MOD) % MOD; // 减去不选任何子树的情况
        
        // 计算删除该节点后的权值和
        long long sum = 0;
        long long total_product = 1;
        for (auto &[v, w_edge] : tree[u]) {
            if (v == father) continue;
            sum = (sum + dp[v]) % MOD;
            total_product = total_product * (dp[v] + 1) % MOD;
        }
        ans[u] = sum;
        
        // 若该节点不是割点，删除后所有连通子图权值和为0（因为删除非割点不会增加连通分量）
        if (!is_cut[u]) {
            ans[u] = 0;
        }
    } else { // 圆节点（点双分量）
        dp[u] = 1;
        for (auto &[v, w_edge] : tree[u]) {
            if (v == father) continue;
            dfs_dp(v, u, n);
            dp[u] = dp[u] * (dp[v] + 1) % MOD;
        }
        dp[u] = (dp[u] - 1 + MOD) % MOD;
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    int T;
    cin >> T;
    while (T--) {
        int n, m;
        cin >> n >> m;
        
        // 初始化
        memset(dfn, 0, sizeof(dfn));
        memset(low, 0, sizeof(low));
        memset(is_cut, false, sizeof(is_cut));
        memset(dp, 0, sizeof(dp));
        memset(ans, 0, sizeof(ans));
        for (int i = 1; i <= n; i++) adj[i].clear();
        for (int i = 1; i <= 2 * n; i++) tree[i].clear();
        timestamp = 0;
        node_cnt = n;
        while (!edge_stack.empty()) edge_stack.pop();
        
        // 输入点权
        for (int i = 1; i <= n; i++) {
            cin >> w[i];
            w[i] %= MOD;
        }
        
        // 输入边
        for (int i = 1; i <= m; i++) {
            int u, v;
            cin >> u >> v;
            adj[u].emplace_back(v, adj[v].size(), i);
            adj[v].emplace_back(u, adj[u].size() - 1, i);
        }
        
        // 构建圆方树
        for (int i = 1; i <= n; i++) {
            if (!dfn[i]) {
                tarjan(i, -1, n);
                handle_root(i, n);
            }
        }
        
        // 树上DP
        for (int i = 1; i <= n; i++) {
            if (!is_cut[i]) {
                ans[i] = 0;
            } else {
                dfs_dp(i, 0, n);
            }
        }
        
        // 计算最终答案
        long long total_ans = 0;
        for (int i = 1; i <= n; i++) {
            total_ans = (total_ans + 1LL * i * ans[i]) % MOD;
        }
        cout << total_ans << '\n';
    }
    
    return 0;
}
```

## （6）代码逐行注释
### 核心代码注释说明
```cpp
// Tarjan算法求点双连通分量，同步构建圆方树
void tarjan(int u, int fa_node, int n) {
    dfn[u] = low[u] = ++timestamp; // 初始化时间戳
    int child = 0; // 统计根节点子节点数
    for (auto &e : adj[u]) { // 遍历所有邻接边
        int v = e.to, id = e.id;
        if (!dfn[v]) { // 未访问节点
            edge_stack.push({u, v}); // 边入栈
            child++;
            tarjan(v, u, n); // 递归遍历
            low[u] = min(low[u], low[v]); // 更新low值
            
            // 割点判定：根节点子节点数≥2或非根节点low[v]≥dfn[u]
            if ((fa_node == -1 && child >= 2) || (fa_node != -1 && low[v] >= dfn[u])) {
                is_cut[u] = true; // 标记为割点
                node_cnt++; // 新增圆节点
                int circle_node = node_cnt;
                // 提取点双分量，构建圆方树
                while (true) {
                    auto [x, y] = edge_stack.top();
                    edge_stack.pop();
                    // 圆节点连接方节点
                    tree[x].emplace_back(circle_node, 1);
                    tree[circle_node].emplace_back(x, 1);
                    tree[y].emplace_back(circle_node, 1);
                    tree[circle_node].emplace_back(y, 1);
                    if (x == u && y == v) break; // 直到弹出当前边
                }
            }
        } else if (v != fa_node && dfn[v] < dfn[u]) { // 非树边且非父节点
            edge_stack.push({u, v}); // 边入栈
            low[u] = min(low[u], dfn[v]); // 更新low值
        }
    }
}
```

```cpp
// 树上DP：计算每个节点删除后的权值和
void dfs_dp(int u, int father, int n) {
    if (u <= n) { // 方节点（原始节点）
        dp[u] = w[u]; // 初始权值为自身点权
        long long product = 1;
        for (auto &[v, w_edge] : tree[u]) {
            if (v == father) continue;
            dfs_dp(v, u, n); // 递归处理子节点
            product = product * (dp[v] + 1) % MOD; // 子树的连通子图权值和+1（不选该子树的情况）
        }
        dp[u] = (product - 1 + MOD) % MOD; // 减去不选任何子树的情况
        
        // 计算删除该节点后的权值和
        long long sum = 0;
        long long total_product = 1;
        for (auto &[v, w_edge] : tree[u]) {
            if (v == father) continue;
            sum = (sum + dp[v]) % MOD; // 子树的连通子图权值和
            total_product = total_product * (dp[v] + 1) % MOD;
        }
        ans[u] = sum;
        
        // 若该节点不是割点，删除后所有连通子图权值和为0（因为删除非割点不会增加连通分量）
        if (!is_cut[u]) {
            ans[u] = 0;
        }
    } else { // 圆节点（点双分量）
        dp[u] = 1;
        for (auto &[v, w_edge] : tree[u]) {
            if (v == father) continue;
            dfs_dp(v, u, n);
            dp[u] = dp[u] * (dp[v] + 1) % MOD; // 点双分量的连通子图权值和为各子树的乘积
        }
        dp[u] = (dp[u] - 1 + MOD) % MOD; // 减去不选任何子树的情况
    }
}
```

## （7）时间/空间复杂度分析
### 时间复杂度
- **圆方树构建**：$O(n + m)$，Tarjan算法的时间复杂度为线性
- **树上DP**：$O(n)$，圆方树的节点数最多为 $2n$，每个节点遍历一次
- **总时间复杂度**：$O(n + m)$，适合大规模图（$10^5$ 节点）

### 空间复杂度
- **圆方树存储**：$O(n + m)$，圆方树的边数最多为 $2m$
- **辅助数组**：$O(n)$，存储dfn、low、is_cut、dp、ans等数组
- **总空间复杂度**：$O(n + m)$

## （8）同类题目拓展
### 相似题目
- **洛谷P4320 道路相遇**：圆方树+LCA求解路径必经点
- **Codeforces 487E Tourists**：圆方树+动态树维护最小值
- **BZOJ4316 小C的独立集**：仙人掌图+圆方树+最大独立集

### 笔试面试变种方向
- 带权圆方树的路径权值和求解
- 动态圆方树维护（新增/删除边）
- 圆方树与其他树结构的综合应用

## （9）ML/DL关联思考
### 核心关联点
1. **图结构简化**：圆方树将复杂无向图转化为树结构，降低GNN模型的消息传递复杂度，提升大规模图的训练与推理效率。
2. **特征提取**：圆节点（点双分量）对应原始图的局部社区特征，方节点（原始节点）对应原始图的全局节点特征，两者结合可辅助GNN模型提取更精准的图特征。
3. **模型适配**：圆方树的树结构特性，可适配RNN、Transformer等序列模型，实现图嵌入的高效提取。

### 面试标准答案
当被问到“如何用圆方树优化无向图数据的机器学习预处理流程”时：
> “圆方树可以将复杂无向图转化为树结构，简化GNN模型的消息传递流程。具体来说：
> 1. **结构简化**：将无向图转化为树结构，减少GNN模型的消息传递次数，降低计算复杂度。
> 2. **特征增强**：圆节点对应点双分量，可提取局部社区特征；方节点对应原始节点，可提取全局节点特征，两者结合提升模型的特征表达能力。
> 3. **效率提升**：树结构的消息传递复杂度为线性，适合大规模图数据的处理。”