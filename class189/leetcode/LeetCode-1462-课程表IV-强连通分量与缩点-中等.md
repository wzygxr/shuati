# LeetCode 1462 - 课程表 IV

## 题目链接
https://leetcode.cn/problems/course-schedule-iv/

## 题目描述

你总共需要上 `n` 门课，课程编号依次为 `0` 到 `n-1`。

有的课会有直接的先修课程，比如如果想上课程 A，你必须先上课程 B。

给你 `n` 和一个直接先修课程列表 `prerequisites`，其中 `prerequisites[i] = [ai, bi]` 表示课程 `bi` 是课程 `ai` 的先修课程。

给你一个查询数组 `queries`，其中 `queries[j] = [uj, vj]`。

对于第 `j` 个查询，请你判断课程 `uj` 是否是课程 `vj` 的先修课程，如果是返回 `true`，否则返回 `false`。

请返回一个布尔数组作为答案，数组中第 `j` 个元素即为第 `j` 个查询的答案。

注意：如果课程 a 是课程 b 的先修课程，且课程 b 是课程 c 的先修课程，那么课程 a 也是课程 c 的先修课程（传递性）。

### 示例 1

**输入：**
```
n = 2, prerequisites = [[1,0]], queries = [[0,1],[1,0]]
```

**输出：**
```
[false,true]
```

**解释：**
- 课程 0 不是课程 1 的先修课程
- 课程 1 是课程 0 的先修课程

### 示例 2

**输入：**
```
n = 2, prerequisites = [], queries = [[1,0],[0,1]]
```

**输出：**
```
[false,false]
```

**解释：**
- 没有先修课程对，所以所有查询都返回 false

### 示例 3

**输入：**
```
n = 3, prerequisites = [[1,2],[1,0],[2,0]], queries = [[1,0],[1,2]]
```

**输出：**
```
[true,true]
```

### 数据范围
- `2 <= n <= 100`
- `0 <= prerequisites.length <= (n * (n - 1) / 2)`
- `prerequisites[i].length == 2`
- `0 <= ai, bi <= n - 1`
- `ai != bi`
- `0 <= queries.length <= 10^4`
- `queries[i].length == 2`
- `0 <= ui, vi <= n - 1`
- `ui != vi`

---

## 笔试/面试考察点分析

### 核心考察点
1. **传递闭包**：利用图的传递性判断可达性
2. **Floyd算法**：多源最短/可达性问题的经典解法
3. **拓扑排序+DFS**：利用DAG特性处理可达性

### 与SCC的关联
```
本题如果存在环（即存在SCC大小>1），则SCC内所有课程互相可达
可以先求SCC缩点，然后在DAG上处理可达性查询
```

### 面试口述要点
```
"这道题是判断课程之间的先修关系是否成立。由于先修关系具有传递性，
我们需要计算传递闭包。可以用Floyd算法预处理所有点对的可达性，
然后O(1)回答每个查询。时间复杂度O(n^3 + q)，由于n<=100，完全可以接受。"
```

---

## 解题思路

### 方法一：Floyd算法求传递闭包（推荐）

由于 `n <= 100`，可以使用Floyd算法预处理所有点对的可达性。

**步骤：**
1. 构建有向图的邻接矩阵
2. 使用Floyd算法计算传递闭包
3. 对于每个查询，直接返回 `reach[u][v]`

### 方法二：拓扑排序+DFS（利用DAG性质）

**步骤：**
1. 求SCC缩点，将图变为DAG
2. 对DAG进行拓扑排序
3. 从每个节点出发DFS，预处理其所有可达节点
4. 回答查询

---

## 完整代码实现

### Java实现（Floyd算法）

```java
// 导入List接口，用于表示图的邻接关系
import java.util.List;

/**
 * LeetCode 1462 - 课程表 IV
 * 
 * 本题核心：计算图的传递闭包，判断节点间的可达性
 * 方法：Floyd-Warshall算法，时间复杂度O(n^3)，适用于n<=100的场景
 */
class Solution {
    
    /**
     * 主方法：判断课程先修关系
     * 
     * @param n 课程数量（节点数）
     * @param prerequisites 先修课程关系，[a,b]表示b是a的先修课，即b->a
     * @param queries 查询列表，[u,v]询问u是否是v的先修课
     * @return 每个查询的结果列表
     */
    public List<Boolean> checkIfPrerequisite(
            int n, 
            int[][] prerequisites, 
            int[][] queries) {
        
        // 构建可达性矩阵：reach[i][j]表示课程i是否是课程j的先修课
        // 即图中是否存在从i到j的路径
        // 初始化：false表示默认不可达
        boolean[][] reach = new boolean[n][n];
        
        // 步骤1：根据直接先修关系初始化可达性矩阵
        // 如果[b,a]在prerequisites中，表示b->a，设置reach[b][a] = true
        for (int[] pre : prerequisites) {
            // pre[0]是课程a，pre[1]是课程b，表示b是a的先修课
            // 即从b可以到达a，所以设置reach[b][a] = true
            reach[pre[1]][pre[0]] = true;
        }
        
        // 步骤2：使用Floyd算法计算传递闭包
        // 核心思想：如果i可以到达k，且k可以到达j，则i可以到达j
        // 三重循环，k作为中间节点
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    // 如果i可以到达k，且k可以到达j，则i可以到达j
                    // 这是传递性的体现
                    if (reach[i][k] && reach[k][j]) {
                        reach[i][j] = true;
                    }
                }
            }
        }
        
        // 步骤3：处理查询
        // 创建结果列表，大小为查询数量
        List<Boolean> ans = new java.util.ArrayList<>();
        
        // 遍历每个查询
        for (int[] q : queries) {
            // q[0]是u，q[1]是v
            // 查询：u是否是v的先修课？即是否存在u->v的路径
            // 直接查reach矩阵即可，O(1)时间
            ans.add(reach[q[0]][q[1]]);
        }
        
        // 返回答案列表
        return ans;
    }
}
```

### C++实现（Floyd算法）

```cpp
#include <vector>
using namespace std;

/**
 * LeetCode 1462 - 课程表 IV
 * Floyd算法求传递闭包
 */
class Solution {
public:
    vector<bool> checkIfPrerequisite(
        int n, 
        vector<vector<int>>& prerequisites, 
        vector<vector<int>>& queries
    ) {
        // 可达性矩阵：reach[i][j]表示i是否是j的先修课
        vector<vector<bool>> reach(n, vector<bool>(n, false));
        
        // 初始化直接先修关系
        for (auto& pre : prerequisites) {
            // pre[1]是pre[0]的先修课，即pre[1] -> pre[0]
            reach[pre[1]][pre[0]] = true;
        }
        
        // Floyd算法计算传递闭包
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    // 如果i能到k，k能到j，则i能到j
                    if (reach[i][k] && reach[k][j]) {
                        reach[i][j] = true;
                    }
                }
            }
        }
        
        // 处理查询
        vector<bool> ans;
        for (auto& q : queries) {
            ans.push_back(reach[q[0]][q[1]]);
        }
        
        return ans;
    }
};
```

### Python实现（Floyd算法）

```python
from typing import List

class Solution:
    """
    LeetCode 1462 - 课程表 IV
    使用Floyd算法计算传递闭包
    """
    
    def checkIfPrerequisite(
        self, 
        n: int, 
        prerequisites: List[List[int]], 
        queries: List[List[int]]
    ) -> List[bool]:
        """
        判断课程先修关系
        
        Args:
            n: 课程数量
            prerequisites: 先修课程关系列表
            queries: 查询列表
            
        Returns:
            每个查询的结果列表
        """
        # 初始化可达性矩阵
        # reach[i][j]表示课程i是否是课程j的先修课
        reach = [[False] * n for _ in range(n)]
        
        # 初始化直接先修关系
        # [a, b]表示b是a的先修课，即b -> a
        for a, b in prerequisites:
            reach[b][a] = True
        
        # Floyd算法计算传递闭包
        # k作为中间节点
        for k in range(n):
            for i in range(n):
                for j in range(n):
                    # 如果i能到k，且k能到j，则i能到j
                    if reach[i][k] and reach[k][j]:
                        reach[i][j] = True
        
        # 处理查询
        # 直接查reach矩阵，O(1)时间
        return [reach[u][v] for u, v in queries]
```

### Java实现（拓扑排序+SCC优化）

```java
import java.util.*;

/**
 * 方法二：拓扑排序 + SCC缩点优化
 * 适用于存在环的情况，可以先缩点再处理
 */
class SolutionSCC {
    
    // Tarjan算法相关变量
    private int[] dfn;      // DFS序
    private int[] low;      // 最早回溯时间
    private int timestamp;  // 时间戳
    private Deque<Integer> stack;  // 栈
    private boolean[] inStack;     // 是否在栈中
    private int[] sccId;    // 节点所属SCC
    private int sccCount;   // SCC数量
    private List<Integer>[] graph; // 原图
    
    public List<Boolean> checkIfPrerequisite(
            int n, 
            int[][] prerequisites, 
            int[][] queries) {
        
        // 步骤1：建图
        graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] pre : prerequisites) {
            // b -> a
            graph[pre[1]].add(pre[0]);
        }
        
        // 步骤2：Tarjan求SCC
        tarjanSCC(n);
        
        // 步骤3：如果图已经是DAG（无环），直接用拓扑排序+DFS
        // 如果存在SCC，则SCC内所有节点互相可达
        
        // 构建DAG（缩点后的图）
        Set<Integer>[] dag = new HashSet[sccCount];
        for (int i = 0; i < sccCount; i++) {
            dag[i] = new HashSet<>();
        }
        
        // 统计SCC大小
        int[] sccSize = new int[sccCount];
        for (int i = 0; i < n; i++) {
            sccSize[sccId[i]]++;
        }
        
        // 构建DAG的边
        for (int u = 0; u < n; u++) {
            for (int v : graph[u]) {
                if (sccId[u] != sccId[v]) {
                    dag[sccId[u]].add(sccId[v]);
                }
            }
        }
        
        // 步骤4：在DAG上预处理可达性
        // 从每个SCC出发DFS，记录可达的SCC
        boolean[][] sccReach = new boolean[sccCount][sccCount];
        for (int i = 0; i < sccCount; i++) {
            dfsDAG(i, i, sccReach, dag);
        }
        
        // 步骤5：处理查询
        List<Boolean> ans = new ArrayList<>();
        for (int[] q : queries) {
            int u = q[0], v = q[1];
            int sccU = sccId[u], sccV = sccId[v];
            
            // 如果u和v在同一个SCC，则u是v的先修课（互相可达）
            if (sccU == sccV) {
                ans.add(sccSize[sccU] > 1 || hasEdge(u, v));
            } else {
                // 检查sccU是否能到达sccV
                ans.add(sccReach[sccU][sccV]);
            }
        }
        
        return ans;
    }
    
    // Tarjan算法求SCC
    private void tarjanSCC(int n) {
        dfn = new int[n];
        low = new int[n];
        stack = new ArrayDeque<>();
        inStack = new boolean[n];
        sccId = new int[n];
        timestamp = 0;
        sccCount = 0;
        
        for (int i = 0; i < n; i++) {
            if (dfn[i] == 0) {
                tarjan(i);
            }
        }
    }
    
    private void tarjan(int u) {
        dfn[u] = low[u] = ++timestamp;
        stack.push(u);
        inStack[u] = true;
        
        for (int v : graph[u]) {
            if (dfn[v] == 0) {
                tarjan(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (inStack[v]) {
                low[u] = Math.min(low[u], dfn[v]);
            }
        }
        
        if (dfn[u] == low[u]) {
            sccCount++;
            int v;
            do {
                v = stack.pop();
                inStack[v] = false;
                sccId[v] = sccCount - 1;
            } while (v != u);
        }
    }
    
    // DFS遍历DAG
    private void dfsDAG(int start, int u, boolean[][] reach, Set<Integer>[] dag) {
        reach[start][u] = true;
        for (int v : dag[u]) {
            if (!reach[start][v]) {
                dfsDAG(start, v, reach, dag);
            }
        }
    }
    
    // 检查u和v之间是否有直接边
    private boolean hasEdge(int u, int v) {
        for (int x : graph[u]) {
            if (x == v) return true;
        }
        return false;
    }
}
```

---

## 时间/空间复杂度分析

### Floyd算法版本

**时间复杂度：**
- 初始化可达性矩阵：$O(m)$，$m$为先修关系数量
- Floyd算法：$O(n^3)$，三重循环
- 处理查询：$O(q)$，$q$为查询数量
- **总时间复杂度：** $O(n^3 + m + q)$

**空间复杂度：**
- 可达性矩阵：$O(n^2)$
- **总空间复杂度：** $O(n^2)$

### SCC优化版本

**时间复杂度：**
- Tarjan求SCC：$O(n + m)$
- 构建DAG：$O(n + m)$
- DAG上DFS预处理：$O(sccCount^2 + sccCount \times edgesInDAG)$
- **总时间复杂度：** $O(n^2 + m)$

**空间复杂度：**
- 图存储：$O(n + m)$
- SCC相关数组：$O(n)$
- DAG可达性：$O(sccCount^2)$
- **总空间复杂度：** $O(n^2 + m)$

---

## 同类题目拓展

### 相关LeetCode题目
| 题号 | 题目名称 | 难度 | 说明 |
|------|----------|------|------|
| 207 | 课程表 | 中等 | 判断是否有环，拓扑排序基础 |
| 210 | 课程表 II | 中等 | 输出拓扑序 |
| 1192 | 查找集群中的关键连接 | 困难 | 桥边问题，边双连通分量 |

### SCC相关题目
| 平台 | 题号 | 题目名称 | 说明 |
|------|------|----------|------|
| 洛谷 | P2341 | 受欢迎的牛 | SCC缩点经典题 |
| POJ | 2186 | Popular Cows | 同P2341 |

---

## ML/DL关联思考

### 1. 知识图谱中的传递推理

本题场景与**知识图谱的传递推理**高度相关：

```python
def transitive_inference_knowledge_graph(kg_edges, queries):
    """
    在知识图谱中进行传递推理
    
    Args:
        kg_edges: 知识图谱边，如[("A", "is_a", "B"), ("B", "is_a", "C")]
        queries: 查询，如[("A", "is_a", "C")]
    
    Returns:
        每个查询是否可以通过传递推理得到
    """
    # 构建实体到ID的映射
    entities = set()
    for h, r, t in kg_edges:
        entities.add(h)
        entities.add(t)
    
    entity_to_id = {e: i for i, e in enumerate(entities)}
    n = len(entities)
    
    # 按关系类型分组构建图
    relation_graphs = {}
    for h, r, t in kg_edges:
        if r not in relation_graphs:
            relation_graphs[r] = [[False] * n for _ in range(n)]
        u, v = entity_to_id[h], entity_to_id[t]
        relation_graphs[r][u][v] = True
    
    # 对每个关系计算传递闭包
    for r, graph in relation_graphs.items():
        # Floyd算法
        for k in range(n):
            for i in range(n):
                for j in range(n):
                    if graph[i][k] and graph[k][j]:
                        graph[i][j] = True
    
    # 回答查询
    results = []
    for h, r, t in queries:
        if r in relation_graphs:
            u, v = entity_to_id.get(h), entity_to_id.get(t)
            results.append(relation_graphs[r][u][v])
        else:
            results.append(False)
    
    return results
```

**应用场景：**
- **知识图谱补全**：推断缺失的关系
- **本体推理**：在层次化本体中进行概念推导
- **推荐系统**：用户兴趣的传递推断

### 2. 神经网络中的可达性学习

传统Floyd算法可以用神经网络近似：

```python
import torch
import torch.nn as nn

class NeuralReachability(nn.Module):
    """
    使用神经网络学习图的可达性
    可以作为GNN的辅助模块
    """
    
    def __init__(self, n_nodes, hidden_dim=64):
        super().__init__()
        self.n_nodes = n_nodes
        
        # 节点嵌入
        self.node_emb = nn.Embedding(n_nodes, hidden_dim)
        
        # 可达性预测网络
        self.predictor = nn.Sequential(
            nn.Linear(hidden_dim * 2, hidden_dim),
            nn.ReLU(),
            nn.Linear(hidden_dim, 1),
            nn.Sigmoid()
        )
    
    def forward(self, edge_index):
        """
        根据边索引学习可达性
        
        Args:
            edge_index: [2, num_edges] 边的起点和终点
        
        Returns:
            reachability_matrix: [n_nodes, n_nodes] 预测的可达性
        """
        # 获取所有节点嵌入
        node_ids = torch.arange(self.n_nodes)
        embeddings = self.node_emb(node_ids)  # [n_nodes, hidden_dim]
        
        # 计算所有点对的嵌入拼接
        # src_emb[i, j] = embeddings[i]
        src_emb = embeddings.unsqueeze(1).expand(-1, self.n_nodes, -1)
        # dst_emb[i, j] = embeddings[j]
        dst_emb = embeddings.unsqueeze(0).expand(self.n_nodes, -1, -1)
        
        # 拼接
        pair_emb = torch.cat([src_emb, dst_emb], dim=-1)
        
        # 预测可达性
        reachability = self.predictor(pair_emb).squeeze(-1)
        
        return reachability
    
    def loss(self, predicted, actual):
        """二元交叉熵损失"""
        return nn.BCELoss()(predicted, actual.float())
```

**价值：**
- **大规模图**：传统O(n^3)不可行时，用神经网络近似
- **概率可达性**：学习存在噪声时的可达概率
- **端到端训练**：与其他任务联合训练

---

## 总结

本题是**传递闭包计算**的经典题目，核心掌握点：

1. **Floyd算法**：$O(n^3)$计算所有点对可达性，适用于n较小的情况
2. **传递性**：关系的传递是题目核心，理解传递闭包概念
3. **复杂度权衡**：$n \leq 100$时Floyd最优，$n$更大时需考虑其他方法
4. **SCC关联**：虽然本题可用简单方法解决，但与SCC缩点思想相通

**笔试技巧**：Floyd算法模板简单，直接三重循环即可。
**面试重点**：解释传递闭包的概念，以及为什么Floyd算法有效。
