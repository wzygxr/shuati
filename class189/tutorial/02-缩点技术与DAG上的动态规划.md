# 缩点技术与DAG上的动态规划

## 一、缩点技术详解

### 1.1 为什么需要缩点？

**问题背景**：
有向图可能存在环，导致：
- 无法进行拓扑排序
- 无法直接使用动态规划
- 问题复杂度增加

**解决方案**：
- 找出所有强连通分量(SCC)
- 将每个SCC收缩为单个节点
- 得到有向无环图(DAG)

### 1.2 缩点的正确性

**定理**：缩点后得到的一定是DAG。

**证明**（反证法）：
1. 假设缩点后存在环 C₁ → C₂ → ... → Cₖ → C₁
2. 则环上任意两个分量互相可达
3. 根据SCC定义，这些分量应合并为一个SCC
4. 矛盾！因此缩点后无环。

### 1.3 缩点算法实现

```cpp
// 缩点：构建DAG
void shrink(int n) {
    for (int u = 1; u <= n; u++) {  // 遍历原图所有节点
        for (int v : adj[u]) {      // 遍历u的所有出边
            // 如果u和v属于不同的SCC，在DAG中添加边
            if (scc_id[u] != scc_id[v]) {
                dag[scc_id[u]].push_back(scc_id[v]);
                in_deg[scc_id[v]]++;    // 入度+1
                out_deg[scc_id[u]]++;   // 出度+1
            }
        }
    }
}
```

**注意**：缩点后可能出现重边，某些题目需要去重。

## 二、DAG上的动态规划

### 2.1 为什么DAG可以DP？

**关键性质**：DAG存在拓扑序，满足：
- 如果存在边 u → v，则在拓扑序中u一定在v之前
- 这意味着：计算v时，所有能到达v的节点都已计算完毕

**DP的一般形式**：
```
dp[v] = 基于所有u→v的dp[u]的某种聚合
```

### 2.2 拓扑排序

**Kahn算法**：
```cpp
vector<int> topologicalSort() {
    queue<int> q;
    vector<int> topo;
    
    // 入度为0的节点入队
    for (int i = 1; i <= n; i++) {
        if (in_deg[i] == 0) q.push(i);
    }
    
    while (!q.empty()) {
        int u = q.front(); q.pop();
        topo.push_back(u);
        
        for (int v : dag[u]) {
            if (--in_deg[v] == 0) q.push(v);
        }
    }
    
    return topo;
}
```

### 2.3 DAG最长路

**问题**：求DAG上权值和最大的路径。

**DP定义**：`dp[u]`表示以u为终点的最长路权值和。

**转移方程**：
```cpp
dp[v] = max(dp[v], dp[u] + weight[v])  // 对所有u→v的边
```

**代码实现**：
```cpp
int longestPathDAG() {
    vector<int> dp(scc_cnt + 1);
    auto topo = topologicalSort();
    
    for (int u : topo) {
        for (int v : dag[u]) {
            dp[v] = max(dp[v], dp[u] + scc_weight[v]);
        }
    }
    
    return *max_element(dp.begin(), dp.end());
}
```

### 2.4 DAG最短路

**DP定义**：`dp[u]`表示以u为终点的最短路权值和。

**转移方程**：
```cpp
dp[v] = min(dp[v], dp[u] + weight[v])
```

**注意**：DAG最短路可以用DP，比Dijkstra更高效（O(N+M) vs O((N+M)logN)）。

### 2.5 路径计数

**问题**：求从起点到终点的路径数量。

**DP定义**：`dp[u]`表示到达u的路径数。

**转移方程**：
```cpp
dp[v] += dp[u]  // 对所有u→v的边
```

**代码实现**：
```cpp
int countPaths(int start, int end) {
    vector<long long> dp(scc_cnt + 1, 0);
    dp[start] = 1;
    
    auto topo = topologicalSort();
    
    for (int u : topo) {
        for (int v : dag[u]) {
            dp[v] = (dp[v] + dp[u]) % MOD;
        }
    }
    
    return dp[end];
}
```

## 三、经典问题：最少加边使图强连通

### 3.1 问题描述

给定有向图，最少需要添加多少条边，使得图强连通？

### 3.2 解题思路

1. **求SCC并缩点**，得到DAG
2. **统计入度为0的SCC数量** `in_zero`
3. **统计出度为0的SCC数量** `out_zero`
4. **答案**：`max(in_zero, out_zero)`

**特殊情况**：
- 如果原图已经强连通，则`scc_cnt == 1`，答案为0

### 3.3 为什么这样是正确的？

**直观理解**：
- 入度为0的SCC：没有外部节点能到达它，必须添加边指向它
- 出度为0的SCC：它无法到达外部节点，必须添加边从它出发
- 每个入度为0的SCC需要一条入边
- 每个出度为0的SCC需要一条出边
- 最优策略：将入度为0和出度为0的SCC连成环

**证明概要**：
- 下界：至少需要`max(in_zero, out_zero)`条边
- 上界：可以通过构造证明`max(in_zero, out_zero)`条边足够

### 3.4 代码实现

```cpp
int minEdgesToMakeStronglyConnected(int n) {
    // 1. Tarjan求SCC
    for (int i = 1; i <= n; i++) {
        if (!dfn[i]) tarjan(i);
    }
    
    // 2. 缩点
    shrink(n);
    
    // 3. 统计入度和出度为0的SCC
    int in_zero = 0, out_zero = 0;
    for (int i = 1; i <= scc_cnt; i++) {
        if (in_deg[i] == 0) in_zero++;
        if (out_deg[i] == 0) out_zero++;
    }
    
    // 4. 特判：如果原图已经强连通
    if (scc_cnt == 1) return 0;
    
    // 5. 返回答案
    return max(in_zero, out_zero);
}
```

## 四、经典问题：受欢迎的牛

### 4.1 问题描述

给定有向图，找出被所有其他节点都能到达的节点。

### 4.2 解题思路

1. **求SCC并缩点**，得到DAG
2. **找出度为0的SCC**
3. **判断唯一性**：如果只有一个出度为0的SCC，则其中所有节点都是"受欢迎的牛"
4. **否则**：不存在满足条件的节点

### 4.3 为什么出度为0的SCC中的节点能被所有节点到达？

**证明**：
1. 在DAG中，从任意节点出发，沿着边走下去
2. 由于无环，最终一定会停在出度为0的节点
3. 因此所有节点都能到达出度为0的SCC
4. 如果出度为0的SCC唯一，则其中的节点能被所有节点到达

### 4.4 代码实现

```cpp
int popularCows(int n) {
    // 1. Tarjan求SCC
    for (int i = 1; i <= n; i++) {
        if (!dfn[i]) tarjan(i);
    }
    
    // 2. 缩点并统计出度
    shrink(n);
    
    // 3. 找出度为0的SCC
    int target_scc = -1, count = 0;
    for (int i = 1; i <= scc_cnt; i++) {
        if (out_deg[i] == 0) {
            target_scc = i;
            count++;
        }
    }
    
    // 4. 判断唯一性
    if (count != 1) return 0;
    
    // 5. 统计该SCC中的节点数
    int ans = 0;
    for (int i = 1; i <= n; i++) {
        if (scc_id[i] == target_scc) ans++;
    }
    
    return ans;
}
```

## 五、带权图的缩点DP

### 5.1 问题描述

每个节点有一个权值，求缩点后DAG上的最长路（节点权值和最大）。

### 5.2 代码实现

```cpp
// 计算每个SCC的总权值
void calcSCCWeight(int n, vector<int>& node_weight) {
    for (int i = 1; i <= scc_cnt; i++) {
        scc_weight[i] = 0;
    }
    for (int i = 1; i <= n; i++) {
        scc_weight[scc_id[i]] += node_weight[i];
    }
}

// DAG最长路DP
int longestPathWithWeight() {
    vector<long long> dp(scc_cnt + 1, 0);
    auto topo = topologicalSort();
    
    for (int u : topo) {
        dp[u] += scc_weight[u];  // 加上当前SCC的权值
        for (int v : dag[u]) {
            dp[v] = max(dp[v], dp[u]);
        }
    }
    
    return *max_element(dp.begin(), dp.end());
}
```

## 六、笔试面试技巧

### 6.1 快速识别缩点题目

**关键词**：
- "有向图" + "环"
- "最少加边"、"使图强连通"
- "受欢迎的节点"、"明星节点"
- "缩点"、"强连通分量"

### 6.2 代码模板选择

**笔试**：
- 提前背熟Tarjan模板
- 直接默写，确保正确性

**面试**：
- 先讲思路，再写代码
- 重点解释`dfn`和`low`的作用
- 说明缩点后的应用场景

### 6.3 常见错误

1. **忘记初始化**：每个测试用例前清空数组
2. **栈溢出**：大规模图使用迭代版Tarjan
3. **缩点重边**：某些题目需要特殊处理
4. **拓扑序DP顺序**：确保按拓扑序进行DP

## 七、ML/DL中的缩点应用

### 7.1 图神经网络加速

```python
import torch
import torch_geometric

# 原始大图
edge_index = ...  # [2, num_edges]
x = ...           # [num_nodes, feature_dim]

# 计算SCC并缩点
scc_labels = tarjan(edge_index, num_nodes)  # [num_nodes]
num_scc = scc_labels.max() + 1

# 粗化图
coarse_edge_index, coarse_x = torch_geometric.nn.pool.max_pool(
    scc_labels, edge_index, x
)

# 在粗化图上运行GNN（更快）
out = gnn(coarse_x, coarse_edge_index)

# 还原到原图（如果需要）
out = out[scc_labels]
```

**价值**：
- 粗化图节点数减少，GNN计算更快
- 保留连通性信息
- 可构建多尺度GNN架构

### 7.2 节点重要性分析

```python
def node_importance_by_scc(graph):
    scc_list = tarjan(graph)
    importance = {}
    
    for i, scc in enumerate(scc_list):
        # 大SCC中的节点更重要
        size_score = len(scc) / graph.num_nodes
        
        # 在DAG中的位置也重要（源点/汇点）
        if is_source_scc(i, dag):
            position_score = 1.0
        elif is_sink_scc(i, dag):
            position_score = 0.8
        else:
            position_score = 0.5
        
        for node in scc:
            importance[node] = size_score * position_score
    
    return importance
```

### 7.3 推荐系统中的社区发现

- SCC对应紧密的用户群体
- 缩点后的DAG表示群体间的影响关系
- 可用于分层推荐策略
