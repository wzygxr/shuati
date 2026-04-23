# Kosaraju算法模板 - 强连通分量求解

## 算法概述

**Kosaraju算法**（也称Kosaraju-Sharir算法）由S. Rao Kosaraju于1978年提出，基于两次DFS遍历求有向图强连通分量。相比Tarjan算法，Kosaraju逻辑更清晰、更容易理解，是面试中解释SCC概念的优选算法。

## 核心原理

### 算法步骤
1. **第一次DFS**：在原图上按完成时间（后序）将节点入栈
2. **第二次DFS**：在逆图上按栈的逆序进行DFS，每次DFS访问到的节点构成一个SCC

### 为什么有效？
- 原图和逆图的强连通分量相同
- 按完成时间逆序遍历，确保每次从"最深"的SCC开始
- 逆图上DFS不会跨SCC访问（因为边的方向反转了）

## 完整代码实现（逐行注释）

```cpp
#include <iostream>      // 标准输入输出流
#include <vector>        // 动态数组容器
#include <stack>         // 栈容器
#include <algorithm>     // 标准算法库
#include <cstring>       // C字符串处理
using namespace std;     // 标准命名空间

const int MAXN = 1e5 + 5;    // 最大节点数，适配大多数题目

// 需要维护两个图：原图和逆图
vector<int> adj[MAXN];       // 原图的邻接表，adj[u]存储u的出边邻居
vector<int> adj_rev[MAXN];   // 逆图的邻接表（反向边），adj_rev[u]存储指向u的节点
                             // 逆图是Kosaraju的核心，第二次DFS在逆图上进行

int n, m;                    // n节点数，m边数

// 第一次DFS相关变量
bool visited[MAXN];          // 访问标记数组，visited[u]=true表示u已访问
                             // 区别于Tarjan的dfn数组，Kosaraju用bool标记

stack<int> finish_order;     // 存储第一次DFS的完成顺序（后序遍历结果）
                             // 完成时间越晚的节点越靠近栈顶

// 第二次DFS相关变量
int scc_id[MAXN];            // scc_id[u]表示节点u所属的SCC编号
int scc_cnt = 0;             // SCC计数器
int scc_size[MAXN];          // 每个SCC的节点数量

/**
 * 第一次DFS：在原图上进行后序遍历
 * 
 * 目标：记录每个节点的完成时间（后序），完成时间晚的节点先入栈
 * 
 * 面试口述要点：
 * - 类似于拓扑排序的DFS版本
 * - 节点u的所有子节点访问完成后，u入栈
 * - 这样保证DAG中出度为0的节点（汇点）在栈顶
 */
void dfs1(int u) {
    visited[u] = true;               // 标记u已访问，防止重复访问
    
    // 遍历u的所有出边邻居（在原图上DFS）
    for (int v : adj[u]) {           // 遍历adj[u]中的所有v
        if (!visited[v]) {           // 如果v未访问
            dfs1(v);                 // 递归访问v
        }
        // 已访问的节点直接跳过
    }
    
    // 所有子节点处理完毕，u入栈（后序遍历）
    // 关键：此时u的完成时间是当前最大的
    finish_order.push(u);            // u入栈，记录完成顺序
}

/**
 * 第二次DFS：在逆图上进行，按完成顺序的逆序遍历
 * 
 * @param u: 当前节点
 * @param id: 当前SCC的编号
 * 
 * 目标：从u出发，在逆图上能访问到的所有节点属于同一个SCC
 * 
 * 为什么是逆图？
 * - 原图中SCC A可以到达SCC B，但B不能到达A
 * - 逆图中B可以到达A，但A不能到达B
 * - 按完成顺序的逆序，确保先访问"汇点"SCC
 */
void dfs2(int u, int id) {
    scc_id[u] = id;                  // 标记u属于编号为id的SCC
    scc_size[id]++;                  // 该SCC的节点数+1
    
    // 在逆图上遍历（走反向边）
    for (int v : adj_rev[u]) {       // 遍历指向u的所有节点v
        if (scc_id[v] == 0) {        // 如果v还没有被分配到SCC
            dfs2(v, id);             // 递归访问v，属于同一个SCC
        }
    }
}

/**
 * Kosaraju算法主函数
 * 
 * 步骤：
 * 1. 在原图上DFS，记录完成顺序
 * 2. 按完成顺序的逆序在逆图上DFS，每次DFS得到一个SCC
 * 
 * 时间复杂度：O(V + E)
 * 空间复杂度：O(V + E)（需要存储逆图）
 */
void kosaraju(int n) {
    /* ===== 步骤1：第一次DFS，获取完成顺序 ===== */
    memset(visited, false, sizeof(visited));     // 清空访问标记
    while (!finish_order.empty()) finish_order.pop();   // 清空栈
    
    for (int i = 1; i <= n; i++) {               // 遍历所有节点
        if (!visited[i]) {                       // 如果节点i未访问
            dfs1(i);                             // 从i开始DFS
        }
    }
    // 此时finish_order栈中，完成时间晚的节点在栈顶
    
    /* ===== 步骤2：第二次DFS，在逆图上识别SCC ===== */
    memset(scc_id, 0, sizeof(scc_id));           // 清空SCC标记
    memset(scc_size, 0, sizeof(scc_size));       // 清空SCC大小
    scc_cnt = 0;                                 // SCC计数器归零
    
    while (!finish_order.empty()) {              // 按完成顺序逆序遍历
        int u = finish_order.top();              // 取栈顶节点
        finish_order.pop();                      // 弹出栈顶
        
        if (scc_id[u] == 0) {                    // 如果u还未被分配到SCC
            ++scc_cnt;                           // 发现新SCC
            dfs2(u, scc_cnt);                    // 在逆图上DFS，标记整个SCC
        }
        // 如果u已经被分配，跳过（属于之前已处理的SCC）
    }
}

/* ==================== 扩展：带权图的Kosaraju ==================== */

int node_val[MAXN];          // 节点权值
int scc_val[MAXN];           // SCC总权值

void dfs2_weighted(int u, int id) {
    scc_id[u] = id;
    scc_size[id]++;
    scc_val[id] += node_val[u];      // 累加节点权值到SCC
    
    for (int v : adj_rev[u]) {
        if (scc_id[v] == 0) {
            dfs2_weighted(v, id);
        }
    }
}

void kosaraju_weighted(int n) {
    // 第一次DFS
    memset(visited, false, sizeof(visited));
    while (!finish_order.empty()) finish_order.pop();
    
    for (int i = 1; i <= n; i++) {
        if (!visited[i]) {
            dfs1(i);
        }
    }
    
    // 第二次DFS（带权）
    memset(scc_id, 0, sizeof(scc_id));
    memset(scc_size, 0, sizeof(scc_size));
    memset(scc_val, 0, sizeof(scc_val));
    scc_cnt = 0;
    
    while (!finish_order.empty()) {
        int u = finish_order.top();
        finish_order.pop();
        
        if (scc_id[u] == 0) {
            ++scc_cnt;
            dfs2_weighted(u, scc_cnt);
        }
    }
}

/* ==================== 扩展：迭代版Kosaraju ==================== */

/**
 * 迭代版dfs1（使用显式栈，防止递归深度过大）
 * 适用于节点数1e6+或递归深度受限环境
 */
void dfs1_iterative(int start) {
    // 使用自定义栈模拟递归，pair<节点, 访问状态>
    // 状态0：首次访问；状态1：子节点处理完毕
    vector<pair<int, int>> stk;      // <节点, 状态>
    stk.push_back({start, 0});       // 初始状态入栈
    visited[start] = true;           // 标记已访问
    
    while (!stk.empty()) {
        auto &top = stk.back();      // 获取栈顶
        int u = top.first;           // 当前节点
        int &state = top.second;     // 状态引用，可修改
        
        if (state == 0) {            // 首次访问u
            state = 1;               // 标记为处理中
            
            // 将所有未访问的子节点入栈
            for (int v : adj[u]) {
                if (!visited[v]) {
                    visited[v] = true;
                    stk.push_back({v, 0});
                }
            }
        } else {                     // 子节点处理完毕
            finish_order.push(u);    // u入完成顺序栈
            stk.pop_back();          // 弹出u
        }
    }
}

/**
 * 迭代版dfs2（在逆图上迭代DFS）
 */
void dfs2_iterative(int start, int id) {
    vector<int> stk;                 // 简单栈，存储待访问节点
    stk.push_back(start);            // 起点入栈
    scc_id[start] = id;              // 标记起点
    scc_size[id]++;
    
    while (!stk.empty()) {
        int u = stk.back();          // 取栈顶
        stk.pop_back();              // 弹出
        
        // 遍历逆图上的邻居
        for (int v : adj_rev[u]) {
            if (scc_id[v] == 0) {    // 未分配SCC
                scc_id[v] = id;      // 标记
                scc_size[id]++;
                stk.push_back(v);    // 入栈
            }
        }
    }
}

/* ==================== 主函数示例 ==================== */

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    cin >> n >> m;
    
    // 读入边，同时构建原图和逆图
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;               // 有向边u->v
        
        adj[u].push_back(v);         // 原图：u的出边
        adj_rev[v].push_back(u);     // 逆图：v的入边（反向存储）
        // 关键：逆图的构建方式是对于边u->v，在adj_rev[v]中添加u
    }
    
    // 执行Kosaraju算法
    kosaraju(n);
    
    cout << "强连通分量总数: " << scc_cnt << endl;
    for (int i = 1; i <= scc_cnt; i++) {
        cout << "SCC " << i << " 大小: " << scc_size[i] << endl;
    }
    
    return 0;
}
```

## 与Tarjan算法的对比

| 特性 | Kosaraju | Tarjan |
|------|----------|--------|
| **遍历次数** | 2次DFS | 1次DFS |
| **图存储** | 需要原图+逆图 | 只需要原图 |
| **空间复杂度** | O(V+E) | O(V) |
| **代码复杂度** | 简单，逻辑清晰 | 稍复杂，需理解low数组 |
| **常数因子** | 较大（两次遍历） | 较小 |
| **面试解释难度** | 容易解释 | 需要画图解释low数组 |

## 笔试面试高频考点

### Q1: 为什么第二次DFS要在逆图上进行？
**答**: 逆图保证了SCC之间的可达性反转。原图中A能到B但B不能到A，逆图中B能到A但A不能到B。按完成顺序的逆序遍历，确保从"汇点"SCC开始，不会跨SCC访问。

### Q2: Kosaraju和Tarjan哪个更好？
**答**: 
- **笔试**: Tarjan更优，空间复杂度低，只需一次遍历
- **面试**: Kosaraju更易解释，逻辑清晰
- **实际**: 两者都是O(V+E)，根据题目约束选择

### Q3: 如何只存储一个图实现Kosaraju？
**答**: 可以不显式建逆图，在第二次DFS时反向遍历边。但实现复杂，不推荐。

## ML/DL关联

### 双向图卷积网络中的Kosaraju思想

```python
import torch
import torch.nn as nn

class BidirectionalGNN(nn.Module):
    """
    双向图神经网络，灵感来自Kosaraju的双向遍历
    同时学习原图和逆图的特征表示
    """
    def __init__(self, in_dim, hidden_dim):
        super().__init__()
        self.gcn_forward = GCNConv(in_dim, hidden_dim)   # 原图卷积
        self.gcn_backward = GCNConv(in_dim, hidden_dim)  # 逆图卷积
        
    def forward(self, x, edge_index, edge_index_rev):
        # 原图特征
        h_forward = self.gcn_forward(x, edge_index)
        # 逆图特征  
        h_backward = self.gcn_backward(x, edge_index_rev)
        # 融合双向特征
        return torch.cat([h_forward, h_backward], dim=1)
```

### SCC感知的图池化

Kosaraju的SCC识别可用于设计新的图池化操作：
- 将每个SCC池化为一个超节点
- 保留SCC内部结构信息
- 实现层次化的图表示学习

## 模板速记口诀

```
Kosaraju分两步，
原图DFS记顺序，
逆图DFS分SCC，
两次遍历就搞定。
```

## 经典例题

| 题号 | 题目 | 平台 | 要点 |
|------|------|------|------|
| P2341 | 受欢迎的牛 | 洛谷 | 求唯一出度为0的SCC |
| 1557 | 可以到达所有点的最少点数目 | 力扣 | SCC缩点找入度为0的SCC |
| 4C | Register System | Codeforces | SCC基本应用 |
