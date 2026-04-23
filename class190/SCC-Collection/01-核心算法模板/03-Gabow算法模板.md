# Gabow算法模板 - 强连通分量求解

## 算法概述

**Gabow算法**是Tarjan算法的一种变体，由Harold N. Gabow于2000年提出。该算法使用两个栈来追踪SCC，在某些特定场景下比Tarjan算法更易于理解和实现。

## 核心原理

### 与Tarjan的区别
- **Tarjan**: 使用`dfn`和`low`两个数组，通过比较判断是否找到SCC根
- **Gabow**: 使用两个栈`S`和`P`，通过栈的弹出操作识别SCC

### 算法思想
- 栈`S`: 存储当前DFS路径上的所有节点（类似Tarjan的栈）
- 栈`P`: 存储当前SCC的候选根节点，用于快速识别SCC边界

## 完整代码实现（逐行注释）

```cpp
#include <iostream>      // 标准输入输出
#include <vector>        // 动态数组
#include <stack>         // 栈容器
#include <cstring>       // 内存操作
using namespace std;     // 标准命名空间

const int MAXN = 1e5 + 5;    // 最大节点数

vector<int> adj[MAXN];       // 邻接表，存储原图

// Gabow算法核心数据结构
stack<int> S;                // 主栈，存储当前DFS路径上的节点
                             // 功能类似Tarjan的栈，但出栈时机不同
                             
stack<int> P;                // 辅助栈，存储SCC的候选根节点
                             // 这是Gabow算法的特色，替代了Tarjan的low数组
                             // P的栈顶始终是当前SCC的最早访问节点

int scc_id[MAXN];            // 节点所属SCC编号
int scc_cnt = 0;             // SCC计数器
int scc_size[MAXN];          // 每个SCC的大小

bool in_S[MAXN];             // 标记节点是否在栈S中
int preorder[MAXN];          // 节点的先序编号（类似Tarjan的dfn）
int pre_cnt = 0;             // 先序计数器

/**
 * Gabow算法核心DFS函数
 * 
 * 算法流程：
 * 1. 节点u入栈S和P，标记先序编号
 * 2. 遍历u的所有邻接节点v
 *    - 如果v未访问，递归处理
 *    - 如果v在栈S中，弹出P中比v晚的节点
 * 3. 如果P的栈顶等于u，说明u是SCC根，弹出S中的节点直到u
 * 
 * 与Tarjan的对比：
 * - Tarjan用low数组记录最小dfn
 * - Gabow用栈P维护SCC边界，弹出P来更新边界
 */
void gabow(int u) {
    // 步骤1：初始化，u入两个栈
    preorder[u] = ++pre_cnt;     // 分配先序编号，记录访问顺序
    S.push(u);                   // u入主栈S
    in_S[u] = true;              // 标记u在S中
    P.push(u);                   // u入辅助栈P，作为当前SCC的候选根
    
    // 步骤2：遍历所有邻接节点
    for (int v : adj[u]) {
        if (preorder[v] == 0) {  // v未访问（树边）
            gabow(v);            // 递归访问v
        }
        // 如果v已访问且在S中（回边，形成环）
        else if (in_S[v]) {      // in_S[v]为true表示v在当前DFS路径上
            // 关键操作：弹出P中所有在v之后入栈的节点
            // 这些节点不可能是SCC的根（因为v能到达它们）
            while (preorder[P.top()] > preorder[v]) {
                P.pop();         // 弹出P的栈顶，更新SCC边界
            }
            // 循环结束后，P的栈顶是u的SCC中最早访问的节点
        }
        // 如果v已访问但不在S中（横叉边），不做处理
    }
    
    // 步骤3：判断u是否是SCC的根
    // P的栈顶等于u，说明u是当前SCC中最早访问的节点
    if (P.top() == u) {          // u是SCC的根
        ++scc_cnt;               // SCC计数+1
        int v;
        do {
            v = S.top();         // 从S中弹出节点
            S.pop();
            in_S[v] = false;     // 标记不在S中
            scc_id[v] = scc_cnt; // 标记所属SCC
            scc_size[scc_cnt]++; // 统计SCC大小
        } while (v != u);        // 直到u出栈
        
        P.pop();                 // u也从P中弹出
    }
}

/* ==================== 扩展：带权Gabow算法 ==================== */

int node_val[MAXN];          // 节点权值
int scc_val[MAXN];           // SCC总权值

void gabow_weighted(int u) {
    preorder[u] = ++pre_cnt;
    S.push(u);
    in_S[u] = true;
    P.push(u);
    
    for (int v : adj[u]) {
        if (preorder[v] == 0) {
            gabow_weighted(v);
        } else if (in_S[v]) {
            while (preorder[P.top()] > preorder[v]) {
                P.pop();
            }
        }
    }
    
    if (P.top() == u) {
        ++scc_cnt;
        int v;
        do {
            v = S.top();
            S.pop();
            in_S[v] = false;
            scc_id[v] = scc_cnt;
            scc_size[scc_cnt]++;
            scc_val[scc_cnt] += node_val[v];   // 累加权值
        } while (v != u);
        P.pop();
    }
}

/* ==================== 算法解析与面试要点 ==================== */

/**
 * Gabow算法核心逻辑解析
 * 
 * 为什么P的栈顶就是SCC的根？
 * - P中存储的是当前DFS路径上可能作为SCC根的节点
 * - 当遇到回边v->u时，弹出P中在v之后的节点，因为这些节点不可能是最小根
 * - 最终P的栈顶就是当前SCC中dfn最小的节点
 * 
 * 与Tarjan的等价性：
 * - Tarjan的low[u] = min{dfn[u], min{low[v]}, min{dfn[w]}}
 * - Gabow的P栈维护了当前可达的最小dfn节点
 * - 两者在数学上是等价的，只是实现方式不同
 */

/* ==================== 主函数示例 ==================== */

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, m;
    cin >> n >> m;
    
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);
    }
    
    // 初始化
    memset(preorder, 0, sizeof(preorder));
    memset(scc_id, 0, sizeof(scc_id));
    memset(scc_size, 0, sizeof(scc_size));
    memset(in_S, false, sizeof(in_S));
    pre_cnt = scc_cnt = 0;
    while (!S.empty()) S.pop();
    while (!P.empty()) P.pop();
    
    // 执行Gabow算法
    for (int i = 1; i <= n; i++) {
        if (preorder[i] == 0) {
            gabow(i);
        }
    }
    
    cout << "SCC总数: " << scc_cnt << endl;
    
    return 0;
}
```

## 三种算法对比

| 特性 | Tarjan | Kosaraju | Gabow |
|------|--------|----------|-------|
| **遍历次数** | 1次 | 2次 | 1次 |
| **额外空间** | 1个栈 | 2个图+1个栈 | 2个栈 |
| **核心结构** | dfn+low | finish_order | preorder+P栈 |
| **代码长度** | 中等 | 较长 | 中等 |
| **面试解释** | 较难 | 容易 | 中等 |
| **实际效率** | 最优 | 一般 | 接近Tarjan |

## 笔试面试要点

### Q1: Gabow算法中P栈的作用是什么？
**答**: P栈存储当前SCC的候选根节点。当遇到回边时，弹出P中在回边目标之后的节点，确保P的栈顶始终是当前可达的最早节点。

### Q2: 什么情况下选择Gabow而不是Tarjan？
**答**: 
- 需要维护SCC边界信息的场景
- 某些特定的并行算法实现
- 面试中展示对SCC算法的深入理解

### Q3: Gabow算法的正确性如何保证？
**答**: P栈的操作保证了当节点u完成DFS时，如果P的栈顶是u，则u是当前SCC的最小根。弹出P的操作等价于Tarjan中low数组的更新。

## ML/DL关联

### 双栈机制在图神经网络中的启发

Gabow的双栈思想可以启发设计新的GNN消息传递机制：

```python
class DualStackGNN(nn.Module):
    """
    双栈图神经网络，受Gabow算法启发
    使用主栈和辅助栈分别维护节点状态和社区边界
    """
    def __init__(self, in_dim, hidden_dim):
        super().__init__()
        self.main_stack_update = GCNConv(in_dim, hidden_dim)
        self.boundary_update = GCNConv(in_dim, hidden_dim)
        
    def forward(self, x, edge_index):
        # 主栈更新：所有邻居消息
        h_main = self.main_stack_update(x, edge_index)
        # 边界更新：只更新SCC边界节点
        h_boundary = self.boundary_update(x, edge_index)
        # 融合两种表示
        return h_main + h_boundary
```

## 模板速记

```
Gabow双栈记心间，
S栈存路径P存根，
遇到回边弹P栈，
P顶等于当前点，
SCC找到出S栈。
```

## 适用场景

- 教学演示SCC算法的多样性
- 需要显式维护SCC边界的场景
- 与其他双栈算法结合的问题
