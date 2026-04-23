# LeetCode 1557 - 可以到达所有点的最少点数目

## 题目链接
https://leetcode.cn/problems/minimum-number-of-vertices-to-reach-all-nodes/

## 题目描述

给你一个 **有向无环图（DAG）**，`n` 个节点编号为 `0` 到 `n-1`，以及一个数组 `edges`，其中 `edges[i] = [fromi, toi]` 表示一条从 `fromi` 指向 `toi` 的有向边。

请找到能够遍历图中所有节点的 **最少** 起始节点数量。

从某个节点出发，可以遍历图中所有节点意味着：从该节点出发，沿着有向边可以到达图中的每一个节点。

### 示例 1

**输入：**
```
n = 6, edges = [[0,1],[0,2],[2,5],[3,4],[4,2]]
```

**输出：**
```
[0,3]
```

**解释：**
- 从节点 0 出发，可以到达节点 0, 1, 2, 5
- 从节点 3 出发，可以到达节点 3, 4, 2, 5
- 节点 0 和 3 共同覆盖所有节点

### 示例 2

**输入：**
```
n = 5, edges = [[0,1],[2,1],[3,1],[1,4],[2,4]]
```

**输出：**
```
[0,2,3]
```

**解释：**
- 从节点 0 出发，可以到达 {0, 1, 4}
- 从节点 2 出发，可以到达 {2, 1, 4}
- 从节点 3 出发，可以到达 {3, 1, 4}
- 节点 0, 2, 3 共同覆盖所有节点

### 数据范围
- `2 <= n <= 10^5`
- `1 <= edges.length <= min(10^5, n * (n - 1) / 2)`
- `edges[i].length == 2`
- `0 <= fromi, toi < n`
- 所有点对 `(fromi, toi)` 互不相同

---

## 笔试/面试考察点分析

### 核心考察点
1. **入度分析**：在DAG中，入度为0的节点必须作为起点
2. **DAG性质**：有向无环图中，源点（入度为0）是必须的起始点
3. **缩点思想**：如果图不是DAG，需要先求SCC缩点

### 关键Insight
```
【核心观察】
在DAG中：
1. 入度为0的节点没有前驱，无法从其他节点到达
2. 因此入度为0的节点必须被选为起始点
3. 入度>0的节点可以从其前驱到达，不需要作为起点
4. 结论：答案就是所有入度为0的节点
```

### 面试口述要点
```
"这道题的关键是观察DAG的入度性质。入度为0的节点没有前驱节点，
所以无法从其他任何节点到达它们，必须把它们作为起始点。
而入度大于0的节点可以从其前驱到达，所以只需要所有入度为0的节点
作为起点，就能覆盖整个图。"
```

### 变形思考
```
如果图不是DAG（存在环）怎么办？
答案：先用Tarjan求SCC缩点，将图变为DAG，然后统计缩点后入度为0的SCC
```

---

## 解题思路

### 方法一：直接统计入度（适用于DAG）

**步骤：**
1. 初始化所有节点的入度为0
2. 遍历所有边，统计每个节点的入度
3. 收集所有入度为0的节点作为答案

### 方法二：SCC缩点（适用于一般图）

**步骤：**
1. 使用Tarjan算法求所有SCC
2. 缩点构建DAG
3. 统计DAG中入度为0的SCC
4. 答案为这些SCC中的所有原始节点

---

## 完整代码实现

### Java实现（直接入度统计）

```java
import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 1557 - 可以到达所有点的最少点数目
 * 
 * 核心思路：DAG中入度为0的节点必须作为起点
 * 时间复杂度：O(n + m)，空间复杂度：O(n)
 */
class Solution {
    
    /**
     * 主方法：找到最少起始节点
     * 
     * @param n 节点数量（0到n-1）
     * @param edges 有向边列表，edges[i] = [from, to]
     * @return 最少起始节点列表
     */
    public List<Integer> findSmallestSetOfVertices(int n, List<List<Integer>> edges) {
        // 步骤1：初始化入度数组
        // inDegree[i]表示节点i的入度（有多少条边指向i）
        int[] inDegree = new int[n];
        
        // 步骤2：遍历所有边，统计入度
        // 对于每条边[from, to]，节点to的入度加1
        for (List<Integer> edge : edges) {
            int from = edge.get(0);  // 边的起点
            int to = edge.get(1);    // 边的终点
            inDegree[to]++;          // 终点入度加1
        }
        
        // 步骤3：收集所有入度为0的节点
        // 这些节点就是答案，因为它们没有前驱，必须作为起点
        List<Integer> result = new ArrayList<>();
        
        for (int i = 0; i < n; i++) {
            // 如果节点i的入度为0，说明没有边指向它
            // 它无法从其他任何节点到达，必须作为起始点
            if (inDegree[i] == 0) {
                result.add(i);
            }
        }
        
        // 返回结果列表
        return result;
    }
}
```

### C++实现（直接入度统计）

```cpp
#include <vector>
using namespace std;

/**
 * LeetCode 1557 - 可以到达所有点的最少点数目
 * DAG入度分析法
 */
class Solution {
public:
    vector<int> findSmallestSetOfVertices(int n, vector<vector<int>>& edges) {
        // 入度数组
        vector<int> inDegree(n, 0);
        
        // 统计入度
        for (auto& edge : edges) {
            inDegree[edge[1]]++;
        }
        
        // 收集入度为0的节点
        vector<int> result;
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                result.push_back(i);
            }
        }
        
        return result;
    }
};
```

### Python实现（直接入度统计）

```python
from typing import List

class Solution:
    """
    LeetCode 1557 - 可以到达所有点的最少点数目
    """
    
    def findSmallestSetOfVertices(self, n: int, edges: List[List[int]]) -> List[int]:
        """
        找到最少起始节点
        
        Args:
            n: 节点数量
            edges: 有向边列表
            
        Returns:
            最少起始节点列表
        """
        # 初始化入度数组
        in_degree = [0] * n
        
        # 统计入度
        for from_node, to_node in edges:
            in_degree[to_node] += 1
        
        # 收集入度为0的节点
        return [i for i in range(n) if in_degree[i] == 0]
```

### Java实现（SCC缩点版本 - 适用于一般图）

```java
import java.util.*;

/**
 * SCC缩点版本 - 适用于可能存在环的一般图
 * 如果图不是DAG，先用Tarjan求SCC缩点，再统计入度
 */
class SolutionSCC {
    
    // Tarjan算法相关
    private int[] dfn, low, sccId;
    private boolean[] inStack;
    private Deque<Integer> stack;
    private int timestamp, sccCount;
    private List<Integer>[] graph;
    
    public List<Integer> findSmallestSetOfVertices(int n, List<List<Integer>> edges) {
        // 步骤1：建图
        graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (List<Integer> edge : edges) {
            graph[edge.get(0)].add(edge.get(1));
        }
        
        // 步骤2：Tarjan求SCC
        tarjan(n);
        
        // 步骤3：缩点统计入度
        // 如果只有一个SCC，任意一个节点都可以作为起点
        if (sccCount == 1) {
            return Arrays.asList(0);
        }
        
        // 统计每个SCC的入度
        int[] sccInDegree = new int[sccCount];
        
        for (int u = 0; u < n; u++) {
            for (int v : graph[u]) {
                if (sccId[u] != sccId[v]) {
                    sccInDegree[sccId[v]]++;
                }
            }
        }
        
        // 步骤4：收集入度为0的SCC中的所有节点
        List<Integer> result = new ArrayList<>();
        boolean[] sccAdded = new boolean[sccCount];
        
        for (int i = 0; i < n; i++) {
            int scc = sccId[i];
            // 如果该SCC入度为0且还未添加过节点
            if (sccInDegree[scc] == 0 && !sccAdded[scc]) {
                result.add(i);
                sccAdded[scc] = true;  // 每个SCC只添加一个代表节点
            }
        }
        
        return result;
    }
    
    private void tarjan(int n) {
        dfn = new int[n];
        low = new int[n];
        sccId = new int[n];
        inStack = new boolean[n];
        stack = new ArrayDeque<>();
        timestamp = 0;
        sccCount = 0;
        
        for (int i = 0; i < n; i++) {
            if (dfn[i] == 0) {
                dfs(i);
            }
        }
    }
    
    private void dfs(int u) {
        dfn[u] = low[u] = ++timestamp;
        stack.push(u);
        inStack[u] = true;
        
        for (int v : graph[u]) {
            if (dfn[v] == 0) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (inStack[v]) {
                low[u] = Math.min(low[u], dfn[v]);
            }
        }
        
        if (dfn[u] == low[u]) {
            int v;
            do {
                v = stack.pop();
                inStack[v] = false;
                sccId[v] = sccCount;
            } while (v != u);
            sccCount++;
        }
    }
}
```

---

## 时间/空间复杂度分析

### 直接入度统计版本

**时间复杂度：**
- 初始化入度数组：$O(n)$
- 遍历所有边统计入度：$O(m)$，$m$为边数
- 收集入度为0的节点：$O(n)$
- **总时间复杂度：** $O(n + m)$

**空间复杂度：**
- 入度数组：$O(n)$
- 结果列表：最坏$O(n)$
- **总空间复杂度：** $O(n)$

### SCC缩点版本

**时间复杂度：**
- Tarjan求SCC：$O(n + m)$
- 缩点统计入度：$O(n + m)$
- **总时间复杂度：** $O(n + m)$

**空间复杂度：**
- 图存储：$O(n + m)$
- Tarjan相关数组：$O(n)$
- SCC入度：$O(sccCount)$
- **总空间复杂度：** $O(n + m)$

---

## 同类题目拓展

### 相关问题
| 题号 | 题目名称 | 难度 | 说明 |
|------|----------|------|------|
| 207 | 课程表 | 中等 | 拓扑排序基础 |
| 210 | 课程表 II | 中等 | 输出拓扑序 |
| 802 | 找到最终的安全状态 | 中等 | 反向图+拓扑排序 |

### 缩点相关题目
| 平台 | 题号 | 题目名称 | 说明 |
|------|------|----------|------|
| 洛谷 | P2746 | 校园网络 | 缩点+最少加边 |
| POJ | 1236 | Network of Schools | 同P2746 |

---

## ML/DL关联思考

### 1. 网络覆盖问题与图神经网络

本题可以建模为**图覆盖问题**，在GNN中有广泛应用：

```python
import torch
import torch.nn as nn
import torch.nn.functional as F

class NetworkCoverageGNN(nn.Module):
    """
    使用GNN学习最优覆盖节点
    可用于社交网络影响最大化等问题
    """
    
    def __init__(self, in_dim, hidden_dim, n_layers=2):
        super().__init__()
        
        # GNN层
        self.convs = nn.ModuleList()
        self.convs.append(nn.Linear(in_dim, hidden_dim))
        for _ in range(n_layers - 1):
            self.convs.append(nn.Linear(hidden_dim, hidden_dim))
        
        # 节点选择预测器
        self.selector = nn.Sequential(
            nn.Linear(hidden_dim, hidden_dim // 2),
            nn.ReLU(),
            nn.Linear(hidden_dim // 2, 1),
            nn.Sigmoid()
        )
    
    def forward(self, x, edge_index, n_select):
        """
        Args:
            x: 节点特征 [n_nodes, in_dim]
            edge_index: 边索引 [2, n_edges]
            n_select: 需要选择的节点数
        
        Returns:
            selected_nodes: 被选中的节点索引
        """
        # 消息传递
        h = x
        for conv in self.convs:
            h = self.message_passing(h, edge_index)
            h = F.relu(conv(h))
        
        # 预测每个节点的重要性分数
        scores = self.selector(h).squeeze(-1)  # [n_nodes]
        
        # 选择top-k节点
        _, selected = torch.topk(scores, n_select)
        
        return selected
    
    def message_passing(self, x, edge_index):
        """简单的消息传递"""
        row, col = edge_index
        agg = torch.zeros_like(x)
        agg.index_add_(0, row, x[col])
        return agg


def greedy_coverage_selection(graph, n_select):
    """
    贪心算法选择覆盖节点
    每次选择能覆盖最多未覆盖节点的节点
    """
    n = len(graph)
    covered = [False] * n
    selected = []
    
    for _ in range(n_select):
        best_node = -1
        best_cover = 0
        
        for u in range(n):
            if u in selected:
                continue
            
            # 计算选择u能新覆盖多少节点
            cover_count = 0
            for v in graph[u]:
                if not covered[v]:
                    cover_count += 1
            
            if cover_count > best_cover:
                best_cover = cover_count
                best_node = u
        
        if best_node == -1:
            break
        
        selected.append(best_node)
        # 标记覆盖
        for v in graph[best_node]:
            covered[v] = True
        covered[best_node] = True
    
    return selected
```

**应用场景：**
- **社交网络影响最大化**：选择最少KOL覆盖最多用户
- **传感器网络部署**：选择最少位置监控最大区域
- **广告投放优化**：选择最少渠道触达最多用户

### 2. DAG结构在深度学习中的价值

```python
def dag_level_encoding(dag_edges, n_nodes):
    """
    为DAG节点计算层级编码
    可用于Transformer的位置编码
    """
    # 初始化层级
    level = [0] * n_nodes
    in_degree = [0] * n_nodes
    
    # 统计入度
    for u, v in dag_edges:
        in_degree[v] += 1
    
    # 拓扑排序计算层级
    from collections import deque
    queue = deque()
    
    for i in range(n_nodes):
        if in_degree[i] == 0:
            queue.append(i)
    
    while queue:
        u = queue.popleft()
        for v in dag_edges.get(u, []):
            level[v] = max(level[v], level[u] + 1)
            in_degree[v] -= 1
            if in_degree[v] == 0:
                queue.append(v)
    
    return level


class DAGTransformer(nn.Module):
    """
    基于DAG结构的Transformer
    使用拓扑层级作为位置编码
    """
    
    def __init__(self, d_model, nhead, num_layers):
        super().__init__()
        self.transformer = nn.TransformerEncoder(
            nn.TransformerEncoderLayer(d_model, nhead),
            num_layers
        )
        self.level_emb = nn.Embedding(100, d_model)  # 假设最多100层
    
    def forward(self, x, dag_edges):
        """
        Args:
            x: 节点特征 [n_nodes, d_model]
            dag_edges: DAG边
        """
        # 计算层级编码
        levels = dag_level_encoding(dag_edges, x.size(0))
        level_encoding = self.level_emb(torch.tensor(levels))
        
        # 加上层级位置编码
        x = x + level_encoding
        
        # Transformer处理
        output = self.transformer(x.unsqueeze(1)).squeeze(1)
        
        return output
```

---

## 总结

本题是**DAG入度分析**的经典题目，核心掌握点：

1. **入度为0的节点必须作为起点**：因为没有前驱节点可以到达它们
2. **DAG的性质**：有向无环保证了这个结论的正确性
3. **缩点思想**：如果图有环，先求SCC缩点转化为DAG
4. **复杂度优化**：直接统计入度即可，$O(n + m)$

**笔试技巧**：直接统计入度，代码简洁高效。
**面试重点**：解释为什么入度为0的节点是必须的。
