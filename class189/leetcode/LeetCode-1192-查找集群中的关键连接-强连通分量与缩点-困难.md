# LeetCode 1192 - 查找集群中的关键连接

## 题目链接
https://leetcode.cn/problems/critical-connections-in-a-network/

## 题目描述

$N$ 台服务器，通过一些连接连接成一个网络，连接用 `connections[i] = [a, b]` 表示服务器 $a$ 和服务器 $b$ 之间的连接。任意服务器都可以直接或间接到达其他任意服务器。

**关键连接**是指：如果移除此连接，会导致网络不再连通的连接。

请返回所有关键连接。

### 示例

**输入**：
```
n = 4, connections = [[0,1],[1,2],[2,0],[1,3]]
```

**输出**：
```
[[1,3]]
```

**解释**：
- 连接 `[1,3]` 是关键连接，因为移除它后服务器 3 无法到达其他服务器
- 连接 `[0,1]`, `[1,2]`, `[2,0]` 构成环，不是关键连接

### 提示
- $2 \le n \le 10^5$
- $n-1 \le \text{connections.length} \le 10^5$
- $0 \le a_i, b_i \le n-1$, $a_i \neq b_i$
- 不存在重边

---

## 笔试/面试考察点分析

### 核心考察点
1. **桥边（Bridge）的定义**：删除后使图不连通的边
2. **Tarjan算法求桥边**：与求SCC类似，但用于无向图
3. **无向图与有向图的区别**：无向图中`(u,v)`和`(v,u)`是同一条边

### 桥边 vs 强连通分量
```
【关键区别】
- 强连通分量（SCC）：有向图中的概念，要求双向可达
- 桥边（Bridge）：无向图中的概念，删除后连通分量数增加
- 联系：桥边不在任何环上，而SCC内的边都在环上
```

### 面试口述要点
```
"这道题要求找桥边（关键连接）。使用Tarjan算法的思想，
 对无向图进行DFS，用dfn记录发现时间，low记录能回溯到的最早祖先。
 对于边(u,v)，如果dfn[u] < low[v]，说明v无法回到u或u的祖先，
 那么(u,v)就是桥边。注意无向图中要处理父边的问题，避免误判。"
```

---

## 解题思路

### 步骤1：建图
使用邻接表存储无向图，注意每条边存两次。

### 步骤2：Tarjan求桥边
核心逻辑：
1. DFS遍历，记录`dfn[u]`和`low[u]`
2. 对于边`(u,v)`：
   - 如果`dfn[u] < low[v]`，则`(u,v)`是桥边
   - 含义：v的子树无法通过回边到达u或u的祖先
3. 注意：无向图中要避免走回父边

### 步骤3：返回结果
收集所有桥边，按要求格式返回。

---

## 完整代码实现

### Java实现

```java
import java.util.ArrayList; // 动态数组
import java.util.Arrays;     // 数组工具
import java.util.List;       // 列表接口

class Solution {
    
    // 邻接表存储图
    private List<Integer>[] graph;
    
    // dfn: DFS序，记录节点被发现的时间
    private int[] dfn;
    
    // low: 能回溯到的最早祖先的dfn值
    private int[] low;
    
    // 时间戳计数器
    private int timestamp;
    
    // 存储结果：所有关键连接（桥边）
    private List<List<Integer>> bridges;

    public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
        // 初始化图
        graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        // 建图：无向图，每条边添加两次
        for (List<Integer> conn : connections) {
            int u = conn.get(0);
            int v = conn.get(1);
            graph[u].add(v); // u -> v
            graph[v].add(u); // v -> u
        }
        
        // 初始化数组
        dfn = new int[n];
        low = new int[n];
        timestamp = 0;
        bridges = new ArrayList<>();
        
        // 从节点0开始DFS（图是连通的，所以只需要一次）
        // -1表示没有父节点
        tarjan(0, -1);
        
        return bridges;
    }
    
    /**
     * Tarjan算法求桥边
     * @param u 当前节点
     * @param parent 父节点（用于避免走回父边）
     */
    private void tarjan(int u, int parent) {
        // 初始化dfn和low为当前时间戳
        dfn[u] = low[u] = ++timestamp;
        
        // 遍历所有邻接点
        for (int v : graph[u]) {
            // 跳过父边：避免在无向图中走回头路
            // 这是无向图与有向图Tarjan的主要区别
            if (v == parent) {
                continue;
            }
            
            if (dfn[v] == 0) {
                // 树边：v未访问，继续DFS
                tarjan(v, u);
                
                // 回溯时更新low[u]
                low[u] = Math.min(low[u], low[v]);
                
                // 判断(u,v)是否为桥边
                // 如果dfn[u] < low[v]，说明v的子树无法回到u或u的祖先
                // 那么(u,v)就是桥边
                if (dfn[u] < low[v]) {
                    List<Integer> bridge = new ArrayList<>();
                    bridge.add(u);
                    bridge.add(v);
                    bridges.add(bridge);
                }
            } else {
                // 回边：v已访问且在栈中（在DFS树上v是u的祖先）
                // 用v的dfn更新low[u]
                low[u] = Math.min(low[u], dfn[v]);
            }
        }
    }
}
```

### Python实现

```python
from typing import List

class Solution:
    def criticalConnections(self, n: int, connections: List[List[int]]) -> List[List[int]]:
        # 建图
        graph = [[] for _ in range(n)]
        for u, v in connections:
            graph[u].append(v)
            graph[v].append(u)
        
        # Tarjan相关数组
        dfn = [0] * n      # DFS序
        low = [0] * n      # 能回溯到的最早祖先
        timestamp = [0]    # 时间戳（用列表实现引用传递）
        bridges = []       # 结果
        
        def tarjan(u: int, parent: int):
            """
            Tarjan算法求桥边
            u: 当前节点
            parent: 父节点（避免走回父边）
            """
            timestamp[0] += 1
            dfn[u] = low[u] = timestamp[0]
            
            for v in graph[u]:
                # 跳过父边
                if v == parent:
                    continue
                
                if dfn[v] == 0:
                    # 树边
                    tarjan(v, u)
                    # 回溯更新
                    low[u] = min(low[u], low[v])
                    
                    # 判断桥边
                    if dfn[u] < low[v]:
                        bridges.append([u, v])
                else:
                    # 回边
                    low[u] = min(low[u], dfn[v])
        
        # 图是连通的，从0开始即可
        tarjan(0, -1)
        
        return bridges
```

### C++实现

```cpp
class Solution {
public:
    vector<vector<int>> criticalConnections(int n, vector<vector<int>>& connections) {
        // 建图
        vector<vector<int>> graph(n);
        for (auto& conn : connections) {
            int u = conn[0], v = conn[1];
            graph[u].push_back(v);
            graph[v].push_back(u);
        }
        
        // Tarjan数组
        vector<int> dfn(n, 0);
        vector<int> low(n, 0);
        int timestamp = 0;
        vector<vector<int>> bridges;
        
        // Tarjan DFS
        function<void(int, int)> tarjan = [&](int u, int parent) {
            dfn[u] = low[u] = ++timestamp;
            
            for (int v : graph[u]) {
                if (v == parent) continue; // 跳过父边
                
                if (!dfn[v]) {
                    tarjan(v, u);
                    low[u] = min(low[u], low[v]);
                    
                    if (dfn[u] < low[v]) {
                        bridges.push_back({u, v});
                    }
                } else {
                    low[u] = min(low[u], dfn[v]);
                }
            }
        };
        
        tarjan(0, -1);
        return bridges;
    }
};
```

---

## 时间/空间复杂度分析

### 时间复杂度
- **建图**：$O(n + m)$，$m$为边数
- **Tarjan算法**：$O(n + m)$，每个节点和边访问一次
- **总时间复杂度**：$O(n + m)$

### 空间复杂度
- **图存储**：$O(n + m)$
- **Tarjan数组**：$O(n)$
- **总空间复杂度**：$O(n + m)$

---

## 同类题目拓展

### 桥边相关
| 题号 | 题目名称 | 平台 | 说明 |
|------|----------|------|------|
| 1489 | 找到最小生成树里的关键边和伪关键边 | LeetCode | 桥边思想的扩展 |

### 双连通分量（与桥边相关）
| 题号 | 题目名称 | 平台 | 说明 |
|------|----------|------|------|
| P2863 | 冗余路径 | 洛谷 | 边双连通分量 |
| P3388 | 割点 | 洛谷 | 点双连通分量 |

---

## ML/DL关联思考

### 1. 网络鲁棒性分析
关键连接在网络分析中对应**网络的脆弱点**：

```python
def analyze_network_robustness(network_graph):
    """
    分析网络的关键脆弱点
    
    Args:
        network_graph: 网络拓扑图（服务器/路由器连接关系）
    
    Returns:
        critical_edges: 关键连接列表
        robustness_score: 网络鲁棒性评分
    """
    n = len(network_graph.nodes())
    
    # 1. 找桥边
    bridges = find_bridges(network_graph)
    
    # 2. 计算鲁棒性指标
    # 桥边越多，网络越脆弱
    bridge_ratio = len(bridges) / network_graph.number_of_edges()
    
    # 3. 计算移除桥边后的连通性
    subgraph = network_graph.copy()
    subgraph.remove_edges_from(bridges)
    
    # 计算最大连通分量比例
    largest_cc = max(nx.connected_components(subgraph), key=len)
    connectivity_ratio = len(largest_cc) / n
    
    # 鲁棒性评分（越高越鲁棒）
    robustness_score = connectivity_ratio * (1 - bridge_ratio)
    
    return {
        'critical_edges': bridges,
        'robustness_score': robustness_score,
        'recommendations': generate_recommendations(bridges)
    }
```

**应用场景：**
- **数据中心网络设计**：避免单点故障
- **通信网络规划**：识别关键链路并增加冗余
- **电力网络分析**：识别关键输电线路

### 2. 图神经网络的边重要性预测
可以用GNN预测图中哪些边可能是桥边：

```python
class BridgePredictionGNN(nn.Module):
    """预测图中边是否为桥边的GNN模型"""
    
    def __init__(self, in_dim, hidden_dim):
        super().__init__()
        self.conv1 = GCNConv(in_dim, hidden_dim)
        self.conv2 = GCNConv(hidden_dim, hidden_dim)
        
        # 边分类器：基于两端节点特征预测边类型
        self.edge_classifier = nn.Sequential(
            nn.Linear(hidden_dim * 2, hidden_dim),
            nn.ReLU(),
            nn.Linear(hidden_dim, 2)  # 二分类：桥边/非桥边
        )
    
    def forward(self, x, edge_index, edge_list):
        # 节点特征学习
        h = self.conv1(x, edge_index).relu()
        h = self.conv2(h, edge_index)
        
        # 边特征：两端节点特征拼接
        edge_features = []
        for u, v in edge_list:
            edge_feat = torch.cat([h[u], h[v]], dim=-1)
            edge_features.append(edge_feat)
        edge_features = torch.stack(edge_features)
        
        # 预测边类型
        return self.edge_classifier(edge_features)

# 训练数据准备
def prepare_training_data(graphs):
    """
    为桥边预测任务准备训练数据
    """
    dataset = []
    for graph in graphs:
        # 用Tarjan算法标注桥边（ground truth）
        bridges = set(map(tuple, find_bridges(graph)))
        
        edge_labels = []
        for edge in graph.edges():
            # 1表示桥边，0表示非桥边
            label = 1 if edge in bridges or (edge[1], edge[0]) in bridges else 0
            edge_labels.append(label)
        
        dataset.append({
            'graph': graph,
            'edge_labels': edge_labels
        })
    
    return dataset
```

**应用价值：**
- **大规模图分析**：Tarjan算法$O(V+E)$，对于超大图可用GNN近似
- **动态图预测**：预测新增边是否会成为桥边
- **边重要性排序**：优先保护预测的桥边

---

## 总结

本题是**Tarjan算法求桥边的经典应用**，核心掌握点：

1. **桥边判定条件**：`dfn[u] < low[v]`表示(u,v)是桥边
2. **无向图处理**：跳过父边，避免误判
3. **与SCC的区别**：桥边用于无向图，SCC用于有向图
4. **ML应用**：网络鲁棒性分析、边重要性预测

**笔试技巧**：背模板，注意无向图要跳过父边。
**面试重点**：解释桥边判定条件、与SCC的区别。
