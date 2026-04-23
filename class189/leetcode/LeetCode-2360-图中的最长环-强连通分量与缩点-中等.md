# LeetCode 2360 - 图中的最长环

## 题目链接
https://leetcode.cn/problems/longest-cycle-in-a-graph/

## 题目描述

给你一个 `n` 个节点的 **有向图**，节点编号为 `0` 到 `n - 1`，其中每个节点 **至多** 有一条出边。

图用一个下标从 **0** 开始的数组 `edges` 表示，其中 `edges[i]` 表示存在一条从节点 `i` 到节点 `edges[i]` 的有向边。如果节点 `i` 没有出边，则 `edges[i] == -1`。

请你返回图中的 **最长** 环的长度。如果不存在环，返回 `-1`。

### 示例 1

**输入：**
```
edges = [3,3,4,2,3]
```

**输出：**
```
3
```

**解释：**
- 最长环是 2 -> 4 -> 3 -> 2
- 环的长度为 3

### 示例 2

**输入：**
```
edges = [2,-1,3,1]
```

**输出：**
```
-1
```

**解释：**
- 图中没有环

### 数据范围
- `n == edges.length`
- `2 <= n <= 10^5`
- `-1 <= edges[i] < n`
- `edges[i] != i`

---

## 笔试/面试考察点分析

### 核心考察点
1. **环检测**：如何高效检测图中的环
2. **每个节点至多一条出边**：这是关键约束条件，简化了问题
3. **DFS时序标记**：记录节点访问时间，用于计算环长度
4. **拓扑排序/DFS**：两种方法都可以解决

### 关键Insight
```
【核心观察】
由于每个节点至多有一条出边：
1. 从任意节点出发，路径不会分叉，最多只有一个环
2. 可以用DFS或并查集检测环
3. 环的长度 = 当前节点访问序号 - 环入口节点访问序号 + 1
```

### 与SCC的关联
```
本题本质是求最大SCC的大小
由于每个节点至多一条出边，SCC只能是：
1. 单个节点（无自环）
2. 简单环
因此最大SCC大小就是最长环长度
```

### 面试口述要点
```
"这道题的关键是观察约束条件：每个节点至多一条出边。
这意味着从任意节点出发，路径是唯一的，最多只有一个环。
我可以用DFS遍历，记录每个节点的访问时间戳。
当遇到一个已访问的节点时，如果它还在当前递归栈中，
就找到了一个环，环的长度等于当前时间戳减去该节点的访问时间戳加1。"
```

---

## 解题思路

### 方法一：DFS时序标记（推荐）

**步骤：**
1. 初始化访问状态数组和时间戳数组
2. 对每个未访问节点进行DFS
3. DFS过程中记录节点访问时间戳
4. 遇到已访问且在递归栈中的节点，计算环长度
5. 更新最大环长度

### 方法二：拓扑排序去环

**步骤：**
1. 统计所有节点的入度
2. 拓扑排序去除非环节点
3. 剩余节点都在环中，统计最大环

---

## 完整代码实现

### Java实现（DFS时序标记）

```java
/**
 * LeetCode 2360 - 图中的最长环
 * 
 * 核心思路：DFS时序标记，利用每个节点至多一条出边的特性
 * 时间复杂度：O(n)，空间复杂度：O(n)
 */
class Solution {
    
    // 最大环长度，初始为-1表示不存在环
    private int maxCycleLength = -1;
    
    // 访问状态数组：0=未访问, 1=访问中（在当前递归栈）, 2=已处理完
    private int[] visited;
    
    // 访问时间戳数组：记录每个节点是在第几步被访问的
    private int[] visitTime;
    
    // 全局时间戳计数器
    private int currentTime;
    
    /**
     * 主方法：找到最长环
     * 
     * @param edges 边的数组，edges[i]表示从i到edges[i]的边，-1表示无出边
     * @return 最长环的长度，无环返回-1
     */
    public int longestCycle(int[] edges) {
        int n = edges.length;
        
        // 步骤1：初始化数组
        // visited数组记录访问状态：0=未访问, 1=访问中, 2=已完成
        visited = new int[n];
        
        // visitTime数组记录每个节点被访问的时间戳
        // 用于计算环的长度
        visitTime = new int[n];
        
        // 当前时间戳，从1开始
        currentTime = 0;
        
        // 步骤2：对每个未访问节点进行DFS
        for (int i = 0; i < n; i++) {
            // 如果节点i未访问，从i开始DFS
            if (visited[i] == 0) {
                dfs(edges, i);
            }
        }
        
        // 返回找到的最长环长度
        return maxCycleLength;
    }
    
    /**
     * DFS遍历图
     * 
     * @param edges 边数组
     * @param u 当前节点
     */
    private void dfs(int[] edges, int u) {
        // 标记节点u为"访问中"状态
        // 表示u在当前DFS递归栈中
        visited[u] = 1;
        
        // 记录u的访问时间戳
        visitTime[u] = ++currentTime;
        
        // 获取u的出边指向的节点v
        int v = edges[u];
        
        // 如果u有出边（v != -1）
        if (v != -1) {
            // 情况1：v未访问，继续DFS
            if (visited[v] == 0) {
                dfs(edges, v);
            }
            // 情况2：v正在访问中（在当前递归栈中）
            // 说明找到了一个环！
            else if (visited[v] == 1) {
                // 计算环的长度
                // 环长度 = 当前时间戳 - v的访问时间戳 + 1
                // 这表示从v到u的路径长度（包含v和u）
                int cycleLength = currentTime - visitTime[v] + 1;
                
                // 更新最大环长度
                maxCycleLength = Math.max(maxCycleLength, cycleLength);
            }
            // 情况3：v已处理完，不需要处理
        }
        
        // 标记节点u为"已完成"状态
        // 表示u及其后续节点都已处理完毕
        visited[u] = 2;
    }
}
```

### C++实现（DFS时序标记）

```cpp
#include <vector>
#include <algorithm>
using namespace std;

/**
 * LeetCode 2360 - 图中的最长环
 * DFS时序标记法
 */
class Solution {
public:
    int longestCycle(vector<int>& edges) {
        int n = edges.size();
        
        // visited: 0=未访问, 1=访问中, 2=已完成
        vector<int> visited(n, 0);
        // 访问时间戳
        vector<int> visitTime(n, 0);
        
        int currentTime = 0;
        int maxCycle = -1;
        
        // DFS函数（lambda）
        function<void(int)> dfs = [&](int u) {
            visited[u] = 1;
            visitTime[u] = ++currentTime;
            
            int v = edges[u];
            if (v != -1) {
                if (visited[v] == 0) {
                    dfs(v);
                } else if (visited[v] == 1) {
                    // 找到环
                    int cycleLen = currentTime - visitTime[v] + 1;
                    maxCycle = max(maxCycle, cycleLen);
                }
            }
            
            visited[u] = 2;
        };
        
        // 对每个未访问节点进行DFS
        for (int i = 0; i < n; i++) {
            if (visited[i] == 0) {
                dfs(i);
            }
        }
        
        return maxCycle;
    }
};
```

### Python实现（DFS时序标记）

```python
from typing import List

class Solution:
    """
    LeetCode 2360 - 图中的最长环
    DFS时序标记法
    """
    
    def longestCycle(self, edges: List[int]) -> int:
        """
        找到最长环的长度
        
        Args:
            edges: 边数组，edges[i]表示从i出发的边指向的节点
            
        Returns:
            最长环长度，无环返回-1
        """
        n = len(edges)
        
        # 访问状态：0=未访问, 1=访问中, 2=已完成
        visited = [0] * n
        
        # 访问时间戳
        visit_time = [0] * n
        
        current_time = 0
        max_cycle = -1
        
        def dfs(u: int) -> None:
            nonlocal current_time, max_cycle
            
            # 标记为访问中
            visited[u] = 1
            current_time += 1
            visit_time[u] = current_time
            
            v = edges[u]
            if v != -1:
                if visited[v] == 0:
                    # 未访问，继续DFS
                    dfs(v)
                elif visited[v] == 1:
                    # 找到环
                    cycle_len = current_time - visit_time[v] + 1
                    max_cycle = max(max_cycle, cycle_len)
            
            # 标记为已完成
            visited[u] = 2
        
        # 对每个未访问节点进行DFS
        for i in range(n):
            if visited[i] == 0:
                dfs(i)
        
        return max_cycle
```

### Java实现（拓扑排序法）

```java
import java.util.*;

/**
 * 方法二：拓扑排序去环
 * 先去除不在环中的节点，然后统计剩余节点的最大环
 */
class SolutionTopo {
    
    public int longestCycle(int[] edges) {
        int n = edges.length;
        
        // 步骤1：计算入度
        int[] inDegree = new int[n];
        for (int i = 0; i < n; i++) {
            if (edges[i] != -1) {
                inDegree[edges[i]]++;
            }
        }
        
        // 步骤2：拓扑排序去除非环节点
        Queue<Integer> queue = new LinkedList<>();
        boolean[] removed = new boolean[n];
        
        // 入度为0的节点入队
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        // BFS去除非环节点
        while (!queue.isEmpty()) {
            int u = queue.poll();
            removed[u] = true;
            
            int v = edges[u];
            if (v != -1) {
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    queue.offer(v);
                }
            }
        }
        
        // 步骤3：在剩余节点中找最长环
        int maxCycle = -1;
        boolean[] visited = new boolean[n];
        
        for (int i = 0; i < n; i++) {
            // 只考虑未被移除且未访问的节点
            if (!removed[i] && !visited[i]) {
                // 从i出发遍历环
                int cycleLen = 0;
                int cur = i;
                
                while (!visited[cur]) {
                    visited[cur] = true;
                    cycleLen++;
                    cur = edges[cur];
                }
                
                maxCycle = Math.max(maxCycle, cycleLen);
            }
        }
        
        return maxCycle;
    }
}
```

---

## 时间/空间复杂度分析

### DFS时序标记版本

**时间复杂度：**
- 每个节点最多访问一次：$O(n)$
- 每条边最多遍历一次：$O(n)$（因为每个节点至多一条出边）
- **总时间复杂度：** $O(n)$

**空间复杂度：**
- 访问状态数组：$O(n)$
- 时间戳数组：$O(n)$
- 递归栈：最坏$O(n)$
- **总空间复杂度：** $O(n)$

### 拓扑排序版本

**时间复杂度：**
- 统计入度：$O(n)$
- 拓扑排序：$O(n)$
- 统计环长度：$O(n)$
- **总时间复杂度：** $O(n)$

**空间复杂度：**
- 入度数组：$O(n)$
- 队列：$O(n)$
- 访问标记：$O(n)$
- **总空间复杂度：** $O(n)$

---

## 同类题目拓展

### 相关LeetCode题目
| 题号 | 题目名称 | 难度 | 说明 |
|------|----------|------|------|
| 207 | 课程表 | 中等 | 环检测基础 |
| 802 | 找到最终的安全状态 | 中等 | 拓扑排序去环 |
| 2192 | 有向无环图中一个节点的所有祖先 | 中等 | DAG可达性 |

### SCC相关题目
| 平台 | 题号 | 题目名称 | 说明 |
|------|------|----------|------|
| 洛谷 | B3609 | 强连通分量模板 | SCC基础 |
| HDU | 1269 | 迷宫城堡 | SCC模板 |

---

## ML/DL关联思考

### 1. 环路检测与图神经网络

环结构在图神经网络中具有重要意义：

```python
import torch
import torch.nn as nn
import torch.nn.functional as F

class CycleAwareGNN(nn.Module):
    """
    感知环结构的GNN
    显式编码环信息增强节点表示
    """
    
    def __init__(self, in_dim, hidden_dim, out_dim):
        super().__init__()
        
        # 标准GNN层
        self.conv1 = nn.Linear(in_dim, hidden_dim)
        self.conv2 = nn.Linear(hidden_dim, hidden_dim)
        
        # 环感知层：聚合环上节点信息
        self.cycle_conv = nn.Linear(hidden_dim, hidden_dim)
        
        # 输出层
        self.output = nn.Linear(hidden_dim * 2, out_dim)
    
    def forward(self, x, edge_index, cycles):
        """
        Args:
            x: 节点特征 [n_nodes, in_dim]
            edge_index: 边索引 [2, n_edges]
            cycles: 环的列表，每个环是节点索引列表
        
        Returns:
            节点输出特征
        """
        # 标准GNN消息传递
        h = self.conv1(x)
        h = F.relu(h)
        h = self.message_passing(h, edge_index)
        h = F.relu(self.conv2(h))
        
        # 计算环感知特征
        cycle_feat = torch.zeros_like(h)
        for cycle in cycles:
            if len(cycle) > 0:
                # 环上节点的平均特征
                cycle_emb = h[cycle].mean(dim=0)
                for node in cycle:
                    cycle_feat[node] += cycle_emb
        
        cycle_feat = F.relu(self.cycle_conv(cycle_feat))
        
        # 拼接标准特征和环感知特征
        combined = torch.cat([h, cycle_feat], dim=-1)
        
        return self.output(combined)
    
    def message_passing(self, x, edge_index):
        """简单的消息传递"""
        row, col = edge_index
        out = torch.zeros_like(x)
        out.index_add_(0, row, x[col])
        return out


def detect_cycles_dfs(edges, n):
    """
    使用DFS检测图中所有环
    返回环的列表
    """
    graph = [[] for _ in range(n)]
    for u, v in enumerate(edges):
        if v != -1:
            graph[u].append(v)
    
    cycles = []
    visited = [0] * n  # 0=未访问, 1=访问中, 2=已完成
    path = []
    path_set = set()
    
    def dfs(u):
        visited[u] = 1
        path.append(u)
        path_set.add(u)
        
        for v in graph[u]:
            if visited[v] == 0:
                dfs(v)
            elif visited[v] == 1 and v in path_set:
                # 找到环
                cycle_start = path.index(v)
                cycle = path[cycle_start:]
                cycles.append(cycle)
        
        path.pop()
        path_set.remove(u)
        visited[u] = 2
    
    for i in range(n):
        if visited[i] == 0:
            dfs(i)
    
    return cycles
```

**应用场景：**
- **化学分子图**：环结构（苯环等）对分子性质至关重要
- **社交网络**：识别小团体（clique-like结构）
- **推荐系统**：发现用户兴趣环（A喜欢B，B喜欢C，C喜欢A）

### 2. 最长环与图表示学习

```python
class LongestCycleEncoder(nn.Module):
    """
    编码最长环信息的图神经网络
    """
    
    def __init__(self, node_dim, cycle_dim):
        super().__init__()
        
        # 节点编码器
        self.node_encoder = nn.Sequential(
            nn.Linear(node_dim, cycle_dim),
            nn.ReLU(),
            nn.Linear(cycle_dim, cycle_dim)
        )
        
        # 环编码器（LSTM处理环上序列）
        self.cycle_lstm = nn.LSTM(cycle_dim, cycle_dim, batch_first=True)
        
        # 融合层
        self.fusion = nn.Sequential(
            nn.Linear(cycle_dim * 2, cycle_dim),
            nn.ReLU(),
            nn.Linear(cycle_dim, cycle_dim)
        )
    
    def forward(self, node_feats, cycles, longest_cycle_idx):
        """
        Args:
            node_feats: 节点特征
            cycles: 所有环
            longest_cycle_idx: 最长环的索引
        """
        # 编码节点
        node_encoded = self.node_encoder(node_feats)
        
        # 编码最长环
        longest_cycle = cycles[longest_cycle_idx]
        cycle_feats = node_encoded[longest_cycle].unsqueeze(0)
        cycle_encoded, _ = self.cycle_lstm(cycle_feats)
        cycle_repr = cycle_encoded.mean(dim=1)  # [1, cycle_dim]
        
        # 将最长环信息传播到所有节点
        cycle_expanded = cycle_repr.expand(node_feats.size(0), -1)
        
        # 融合
        combined = torch.cat([node_encoded, cycle_expanded], dim=-1)
        output = self.fusion(combined)
        
        return output
```

---

## 总结

本题是**环检测与最长环**的经典题目，核心掌握点：

1. **约束条件利用**：每个节点至多一条出边，路径唯一，简化问题
2. **DFS时序标记**：记录访问时间戳，快速计算环长度
3. **三状态标记**：未访问、访问中、已完成，区分不同情况
4. **拓扑排序**：作为替代方法，同样高效

**笔试技巧**：DFS时序标记，代码简洁，$O(n)$时间。
**面试重点**：解释为什么时间戳可以计算环长度，约束条件如何简化问题。
