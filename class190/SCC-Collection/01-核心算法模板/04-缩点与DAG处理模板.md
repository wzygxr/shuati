# 缩点与DAG处理模板

## 缩点概述

**缩点（Graph Condensation/Shrink）** 是将强连通分量（SCC）收缩为单个节点的技术。缩点后，原图变成**有向无环图（DAG）**，使许多复杂问题得以简化。

## 缩点的核心思想

### 为什么缩点后一定是DAG？
- 原图中每个SCC内部是强连通的
- SCC之间的边形成有向无环结构（如果有环，这些SCC应该合并）
- 因此缩点后的图不存在环，是DAG

### 缩点的价值
1. **简化问题**：DAG上可以进行拓扑排序、DP等操作
2. **降低复杂度**：将复杂图问题转化为DAG问题
3. **提取结构**：识别图的核心骨架结构

## 完整代码实现（逐行注释）

```cpp
#include <iostream>      // 标准输入输出
#include <vector>        // 动态数组
#include <queue>         // 队列（拓扑排序用）
#include <algorithm>     // 算法库
#include <cstring>       // 内存操作
using namespace std;     // 标准命名空间

const int MAXN = 1e5 + 5;    // 最大节点数
const int MAXM = 2e5 + 5;    // 最大边数

/* ==================== 原图数据结构 ==================== */

vector<int> adj[MAXN];       // 原图邻接表
int n, m;                    // 节点数和边数

/* ==================== Tarjan相关变量 ==================== */

int dfn[MAXN], low[MAXN];    // Tarjan核心数组
int timestamp = 0;           // 时间戳
stack<int> stk;              // 节点栈
bool in_stack[MAXN];         // 栈内标记
int scc_id[MAXN];            // 节点所属SCC编号
int scc_cnt = 0;             // SCC总数
int scc_size[MAXN];          // SCC大小

/* ==================== 缩点后DAG数据结构 ==================== */

vector<int> dag[MAXN];       // 缩点后的DAG邻接表
                             // dag[i]存储SCC i的出边邻居（SCC编号）
                             // 这是缩点的核心数据结构

int in_deg[MAXN];            // DAG中每个SCC的入度
                             // 拓扑排序必需

int out_deg[MAXN];           // DAG中每个SCC的出度
                             // 某些题目需要（如找汇点）

int scc_val[MAXN];           // 每个SCC的权值（如节点权和）
int node_val[MAXN];          // 原节点权值

/* ==================== Tarjan算法（标准实现） ==================== */

void tarjan(int u) {
    dfn[u] = low[u] = ++timestamp;
    stk.push(u);
    in_stack[u] = true;
    
    for (int v : adj[u]) {
        if (!dfn[v]) {
            tarjan(v);
            low[u] = min(low[u], low[v]);
        } else if (in_stack[v]) {
            low[u] = min(low[u], dfn[v]);
        }
    }
    
    if (dfn[u] == low[u]) {
        ++scc_cnt;
        int v;
        do {
            v = stk.top(); stk.pop();
            in_stack[v] = false;
            scc_id[v] = scc_cnt;
            scc_size[scc_cnt]++;
            scc_val[scc_cnt] += node_val[v];   // 累加SCC权值
        } while (v != u);
    }
}

/* ==================== 缩点核心函数 ==================== */

/**
 * 缩点函数：将原图收缩为DAG
 * 
 * 核心逻辑：
 * 1. 遍历原图的每条边u->v
 * 2. 如果scc_id[u] != scc_id[v]，在DAG中添加边scc_id[u] -> scc_id[v]
 * 3. 统计每个SCC的入度和出度
 * 
 * 时间复杂度：O(E)，只需遍历所有边一次
 * 
 * 面试常考：如何处理重边？
 * 答：可以选择去重（用set）或保留（不影响正确性，只影响效率）
 */
void shrink() {
    // 遍历原图的所有边
    for (int u = 1; u <= n; u++) {           // 遍历每个节点
        for (int v : adj[u]) {               // 遍历u的所有出边
            int su = scc_id[u];              // u所在的SCC
            int sv = scc_id[v];              // v所在的SCC
            
            if (su != sv) {                  // 如果属于不同SCC
                dag[su].push_back(sv);       // 在DAG中添加边
                in_deg[sv]++;                // sv的入度+1
                out_deg[su]++;               // su的出度+1
            }
            // 如果su == sv，说明是SCC内部的边，缩点后忽略
        }
    }
    // 注意：此时dag中可能有重边（原图中多条边连接同一对SCC）
    // 大多数题目不影响结果，如果需要可以去重
}

/* ==================== 去重版缩点 ==================== */

/**
 * 去重缩点：去除DAG中的重边
 * 适用于对边数敏感的场景（如DAG边数统计）
 * 
 * 实现方式：对dag的每个邻接表排序去重
 */
void shrink_unique() {
    for (int u = 1; u <= n; u++) {
        for (int v : adj[u]) {
            int su = scc_id[u];
            int sv = scc_id[v];
            if (su != sv) {
                dag[su].push_back(sv);
            }
        }
    }
    
    // 对每个SCC的出边去重
    for (int i = 1; i <= scc_cnt; i++) {
        sort(dag[i].begin(), dag[i].end());           // 排序
        dag[i].erase(unique(dag[i].begin(), dag[i].end()), dag[i].end());   // 去重
        
        // 重新统计入度（如果需要）
        for (int v : dag[i]) {
            in_deg[v]++;
        }
    }
}

/* ==================== DAG拓扑排序 ==================== */

/**
 * DAG拓扑排序（Kahn算法）
 * 
 * 应用：
 * - 检测DAG（如果结果节点数 < scc_cnt，说明有环，但缩点后一定是DAG）
 * - 为DP提供处理顺序
 * - 求DAG的层次结构
 * 
 * 返回值：拓扑序数组
 */
vector<int> topological_sort() {
    vector<int> topo;            // 存储拓扑序
    queue<int> q;                // 入度为0的节点队列
    
    // 初始化：所有入度为0的SCC入队
    for (int i = 1; i <= scc_cnt; i++) {
        if (in_deg[i] == 0) {    // 入度为0，是DAG的起点
            q.push(i);
        }
    }
    
    while (!q.empty()) {
        int u = q.front();       // 取出队首
        q.pop();
        topo.push_back(u);       // 加入拓扑序
        
        // 遍历u的所有出边
        for (int v : dag[u]) {
            in_deg[v]--;         // 移除边u->v，v的入度-1
            if (in_deg[v] == 0) {   // 如果v的入度变为0
                q.push(v);       // v入队
            }
        }
    }
    
    return topo;                 // 返回拓扑序
}

/* ==================== DAG上DP：最长路 ==================== */

/**
 * DAG最长路（带权）
 * 
 * 问题：求DAG上从任意起点出发的最长路径（点权/边权）
 * 
 * 解法：按拓扑序DP
 * dp[v] = max(dp[v], dp[u] + weight)
 * 
 * 应用场景：
 * - 缩点后求原图的最长路径
 * - 关键路径分析
 * - 项目调度（PERT图）
 */
int dag_longest_path() {
    vector<int> topo = topological_sort();   // 获取拓扑序
    vector<int> dp(scc_cnt + 1, 0);          // dp[i]表示到SCC i的最长路
    
    // 初始化：入度为0的SCC可以独立作为起点
    for (int i = 1; i <= scc_cnt; i++) {
        if (in_deg[i] == 0) {                // 注意：此时in_deg已被topo修改
            dp[i] = scc_val[i];              // 起点SCC的权值
        }
    }
    
    // 按拓扑序进行DP
    for (int u : topo) {
        for (int v : dag[u]) {
            // 更新v的最长路：经过u到v vs 原来的v
            dp[v] = max(dp[v], dp[u] + scc_val[v]);
        }
    }
    
    // 返回最大值
    int ans = 0;
    for (int i = 1; i <= scc_cnt; i++) {
        ans = max(ans, dp[i]);
    }
    return ans;
}

/* ==================== DAG上DP：最短路 ==================== */

/**
 * DAG最短路
 * 
 * 时间复杂度：O(V + E)，优于Dijkstra的O(ElogV)
 */
int dag_shortest_path(int start, int end) {
    vector<int> topo = topological_sort();
    vector<int> dist(scc_cnt + 1, INT_MAX);   // 距离数组
    
    dist[start] = 0;                          // 起点距离为0
    
    // 找到起点在拓扑序中的位置
    bool found_start = false;
    for (int u : topo) {
        if (u == start) found_start = true;
        if (!found_start) continue;           // 跳过起点之前的节点
        
        if (dist[u] == INT_MAX) continue;     // 不可达节点
        
        for (int v : dag[u]) {
            // 假设边权为1，实际题目可能不同
            if (dist[v] > dist[u] + 1) {
                dist[v] = dist[u] + 1;
            }
        }
    }
    
    return dist[end];
}

/* ==================== DAG上DP：路径计数 ==================== */

/**
 * DAG路径计数
 * 
 * 问题：求从起点到终点的所有路径数量
 * 
 * 注意：结果可能很大，需要取模
 */
vector<int> dag_path_count(int start) {
    vector<int> topo = topological_sort();
    vector<int> cnt(scc_cnt + 1, 0);          // 路径计数
    
    cnt[start] = 1;                           // 起点到自己有1条路径
    
    for (int u : topo) {
        for (int v : dag[u]) {
            cnt[v] += cnt[u];                 // 累加路径数
            // cnt[v] %= MOD;                   // 如果需要取模
        }
    }
    
    return cnt;
}

/* ==================== 特殊应用：找DAG的源点和汇点 ==================== */

/**
 * 源点：入度为0的SCC（没有前驱）
 * 汇点：出度为0的SCC（没有后继）
 * 
 * 应用：
 * - 受欢迎的牛（唯一汇点）
 * - 最小点覆盖（选择所有源点）
 */
void find_sources_and_sinks(vector<int>& sources, vector<int>& sinks) {
    for (int i = 1; i <= scc_cnt; i++) {
        if (in_deg[i] == 0) sources.push_back(i);   // 源点
        if (out_deg[i] == 0) sinks.push_back(i);    // 汇点
    }
}

/* ==================== 主函数示例 ==================== */

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    cin >> n >> m;
    
    // 读入权值（如果题目有）
    for (int i = 1; i <= n; i++) {
        cin >> node_val[i];
    }
    
    // 建图
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);
    }
    
    // 步骤1：Tarjan求SCC
    for (int i = 1; i <= n; i++) {
        if (!dfn[i]) tarjan(i);
    }
    
    // 步骤2：缩点构造DAG
    shrink();
    
    // 步骤3：DAG上求解问题
    // 例：求最长路
    int ans = dag_longest_path();
    cout << "DAG最长路: " << ans << endl;
    
    // 例：找源点和汇点
    vector<int> sources, sinks;
    find_sources_and_sinks(sources, sinks);
    cout << "源点数量: " << sources.size() << endl;
    cout << "汇点数量: " << sinks.size() << endl;
    
    return 0;
}
```

## 缩点问题解题套路

### 通用解题步骤

```
1. Tarjan求SCC（获得scc_id和scc_cnt）
2. 缩点构造DAG（遍历原图边，连接不同SCC）
3. 在DAG上求解：
   - 拓扑排序
   - DP求最长/最短路
   - 统计源点/汇点
4. 根据题目要求输出结果
```

### 常见题型

| 题型 | 解法 | 代表题目 |
|------|------|----------|
| 最长路径 | 拓扑序+DP | P3387 【模板】缩点 |
| 最少覆盖点 | 找所有源点 | 受欢迎的牛 |
| 可达性判断 | SCC内互相可达 | 学校网络 |
| 路径计数 | 拓扑序+计数DP | - |
| 最小权覆盖 | SCC内取最小 | Checkposts |

## 面试高频问题

### Q1: 缩点后的图为什么一定是DAG？
**答**: 反证法。如果缩点后有环，则环上的SCC可以互相到达，应该合并为一个SCC，矛盾。

### Q2: 如何处理缩点后的重边？
**答**: 不影响正确性的情况下可以保留；如果需要去重，可以用排序+unique或set。

### Q3: SCC缩点和并查集的区别？
**答**: 
- 并查集处理无向图的连通性
- SCC处理有向图的强连通性
- 两者都用于合并等价类，但应用场景不同

## ML/DL关联

### 缩点在图神经网络中的应用

```python
class SCCPooling(nn.Module):
    """
    SCC-based Graph Pooling
    使用缩点技术进行图池化，将SCC池化为超节点
    """
    def __init__(self):
        super().__init__()
        
    def forward(self, x, edge_index):
        # 1. 计算SCC
        scc_id = tarjan(edge_index)
        
        # 2. 聚合SCC内部特征（mean/max/sum pooling）
        scc_x = scatter_mean(x, scc_id, dim=0)
        
        # 3. 构建SCC级别的边（缩点）
        scc_edge_index = self.shrink_edges(edge_index, scc_id)
        
        return scc_x, scc_edge_index
```

### 缩点在图分类中的价值
1. **层次化表示**：原图 -> SCC图 -> 全局特征
2. **降维**：将n节点图降为scc_cnt节点图
3. **结构特征**：SCC数量、SCC大小分布作为图的全局特征
