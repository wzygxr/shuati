# Tarjan算法模板 - 强连通分量求解

## 算法概述

**Tarjan算法**是由Robert Tarjan于1972年提出的求有向图强连通分量的线性时间算法。核心思想是通过一次DFS遍历，利用`dfn`（时间戳）和`low`（最早回溯点）两个数组识别SCC。

## 核心原理

### 关键概念
- **dfn[u]**: 节点u的DFS访问序号（发现时间）
- **low[u]**: 节点u能回溯到的最早祖先的dfn值
- **SCC判定**: 当`dfn[u] == low[u]`时，u是当前SCC的根节点

### 为什么是`dfn[u] == low[u]`？
- `low[u]`表示u能到达的最小dfn
- 如果`low[u] == dfn[u]`，说明u无法到达比它更早的节点
- 栈中从u到栈顶的所有节点构成一个SCC

## 完整代码实现（逐行注释）

```cpp
#include <iostream>      // 标准输入输出流，用于读写数据
#include <vector>        // 动态数组容器，存储图的邻接关系
#include <stack>         // 栈容器，Tarjan核心数据结构
#include <algorithm>     // 标准算法库，提供min/max等函数
#include <cstring>       // C字符串处理，提供memset函数
using namespace std;     // 使用标准命名空间，避免重复写std::

const int MAXN = 1e5 + 5;    // 最大节点数常量，1e5+5适配大多数题目数据范围；面试需根据题目调整
                             // +5是为了防止数组越界，笔试常用技巧

vector<int> adj[MAXN];       // 邻接表数组，adj[u]存储u的所有出边邻居
                             // 使用vector动态扩展，比链式前向星更易理解
                             // ML场景：图神经网络的邻接表输入格式

int dfn[MAXN], low[MAXN];    // dfn[u]:u的DFS序号; low[u]:u能回溯到的最小dfn
                             // 这两个数组是Tarjan算法的核心，笔试必须理解
                             // 初始化值为0表示未访问

int timestamp = 0;           // 全局时间戳计数器，每次DFS新节点时递增
                             // 用于给dfn赋唯一递增值

stack<int> stk;              // 节点栈，存储当前DFS路径上的所有节点
                             // 用于识别同一个SCC内的节点
                             // 面试常考：为什么是栈而不是队列？

bool in_stack[MAXN];         // 标记节点是否在栈中，避免处理非当前SCC的已访问节点
                             // 关键：区分回边（在栈中）和横叉边（不在栈中）

int scc_id[MAXN];            // scc_id[u]表示节点u所属的SCC编号（1~scc_cnt）
                             // 缩点操作的核心数组，将原图映射到DAG

int scc_cnt = 0;             // SCC计数器，统计找到的强连通分量总数
                             // 同时也是缩点后DAG的节点数

int scc_size[MAXN];          // 每个SCC的节点数量，某些题目需要统计分量大小
                             // 如：求最大SCC、判断分量大小是否满足条件

/**
 * Tarjan算法核心DFS函数
 * 
 * 面试高频口述点：
 * 1. 用dfn记录访问顺序，low记录能到达的最小dfn
 * 2. 遇到回边（在栈中的节点）时更新low
 * 3. 当dfn[u]==low[u]时，u是SCC根，栈中到u的节点构成一个SCC
 * 
 * ML关联：
 * - 该递归结构类似GNN的消息传递机制
 * - low数组的更新逻辑类似于图卷积中的邻居聚合
 */
void tarjan(int u) {
    // 步骤1：初始化dfn和low，标记当前节点的发现时间
    dfn[u] = low[u] = ++timestamp;   // timestamp先自增再赋值，保证从1开始编号
                                     // dfn一旦赋值不再改变，是节点的唯一身份标识
    
    // 步骤2：当前节点入栈，标记在栈状态
    stk.push(u);                     // 入栈，表示u在当前DFS路径上
    in_stack[u] = true;              // 标记在栈中，用于后续区分回边和横叉边
    
    // 步骤3：遍历u的所有邻接节点（DFS递归核心）
    for (int v : adj[u]) {           // 遍历u的出边邻居v
        
        // 情况1：v未访问过（树边）
        if (dfn[v] == 0) {           // dfn[v]==0表示v尚未被DFS访问
            tarjan(v);               // 递归访问v，进入v的子树
            
            // 回溯时更新low[u]：u能到达的最小dfn取决于子节点v
            // 如果v能到达更早的节点，u也能通过v到达
            low[u] = min(low[u], low[v]);   // 关键：用子节点的low更新父节点
            
            // 面试追问：为什么用low[v]而不是dfn[v]？
            // 答：low[v]已经包含了v子树的所有回边信息，是v能到达的最小值
        }
        // 情况2：v已访问且在栈中（回边，构成环）
        else if (in_stack[v]) {      // in_stack[v]为true说明v在当前DFS路径上
            // v是u的祖先节点，u->v形成回边
            // 用dfn[v]更新low[u]，因为v是祖先，其dfn代表更早的访问时间
            low[u] = min(low[u], dfn[v]);   // 关键：回边用dfn更新，不是low
            
            // 面试追问：为什么回边用dfn[v]而不用low[v]？
            // 答：v已经在栈中，其low[v]已经计算完成，但dfn[v]更直接表示v的位置
            // 实际上用low[v]也可以（因为low[v]<=dfn[v]），但dfn[v]更高效
        }
        // 情况3：v已访问但不在栈中（横叉边）
        // 什么也不做，因为v属于已经处理完的SCC，与当前SCC无关
        // 面试常考：横叉边为什么不处理？答：v的SCC已确定，不会形成新的环
    }
    
    // 步骤4：SCC识别与出栈
    // 当dfn[u]==low[u]时，u是当前SCC的根节点（最早被发现的节点）
    if (dfn[u] == low[u]) {          // SCC根节点的判定条件
        ++scc_cnt;                   // 发现新SCC，计数器+1
        int v;                       // 临时变量，存储弹出的节点
        
        // 循环出栈，直到u出栈为止，这些节点构成一个SCC
        do {
            v = stk.top();           // 获取栈顶节点
            stk.pop();               // 弹出栈顶
            in_stack[v] = false;     // 标记不在栈中，防止被其他SCC误处理
            
            scc_id[v] = scc_cnt;     // 标记v属于当前SCC
            scc_size[scc_cnt]++;     // 当前SCC的节点数+1
            
        } while (v != u);            // 直到u出栈，该SCC的所有节点都已标记
    }
}

/* ==================== 扩展：带节点权值的SCC ==================== */

int node_val[MAXN];          // 每个节点的权值（题目给定）
int scc_val[MAXN];           // 每个SCC的总权值（分量内所有节点权值之和）
                             // 缩点后DAG上DP常用的权值

void tarjan_with_weight(int u) {
    dfn[u] = low[u] = ++timestamp;
    stk.push(u); in_stack[u] = true;
    
    for (int v : adj[u]) {
        if (!dfn[v]) {
            tarjan_with_weight(v);
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

/* ==================== 扩展：迭代版Tarjan（防栈溢出） ==================== */

// 用于处理大规模图（1e6+节点）或递归深度受限环境
// 笔试面试加分项，展示对栈溢出的处理能力

struct StackFrame {          // 模拟递归栈帧结构
    int u;                   // 当前节点
    int iter;                // 当前遍历到第几条边
    int state;               // 状态：0=首次访问，1=子节点返回后
};

void tarjan_iterative(int start) {
    vector<StackFrame> custom_stack;     // 自定义栈替代系统递归栈
    custom_stack.push_back({start, 0, 0});   // 初始状态入栈
    
    while (!custom_stack.empty()) {
        StackFrame &frame = custom_stack.back();   // 获取栈顶帧
        int u = frame.u;                         // 当前处理节点
        
        if (frame.state == 0) {                  // 首次访问u
            dfn[u] = low[u] = ++timestamp;       // 初始化时间戳
            stk.push(u);                         // 入节点栈
            in_stack[u] = true;
            frame.state = 1;                     // 标记为已初始化
        }
        
        // 继续遍历邻接边
        if (frame.iter < adj[u].size()) {
            int v = adj[u][frame.iter++];        // 获取下一个邻居，迭代器+1
            
            if (dfn[v] == 0) {                   // 树边，需要递归
                custom_stack.push_back({v, 0, 0});   // 子节点入栈
            } else if (in_stack[v]) {            // 回边
                low[u] = min(low[u], dfn[v]);    // 更新low
            }
            // 横叉边忽略
        } else {
            // u的所有邻居处理完毕，处理SCC出栈
            if (dfn[u] == low[u]) {              // SCC根节点
                ++scc_cnt;
                int v;
                do {
                    v = stk.top(); stk.pop();
                    in_stack[v] = false;
                    scc_id[v] = scc_cnt;
                } while (v != u);
            }
            
            // 更新父节点的low值（模拟递归回溯）
            if (custom_stack.size() > 1) {
                int parent_idx = custom_stack.size() - 2;
                int parent = custom_stack[parent_idx].u;
                low[parent] = min(low[parent], low[u]);
            }
            
            custom_stack.pop_back();             // 弹出当前帧
        }
    }
}

/* ==================== 主函数示例 ==================== */

int main() {
    ios::sync_with_stdio(false);     // 关闭C++IO同步，加速输入（笔试常用）
    cin.tie(nullptr);                // 解除cin/cout绑定，进一步加速
    
    int n, m;                        // n节点数，m边数
    cin >> n >> m;
    
    // 读入边信息，构建邻接表
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;               // 有向边u->v
        adj[u].push_back(v);         // 添加边到邻接表
    }
    
    // 初始化数组（多组数据时需清空）
    memset(dfn, 0, sizeof(dfn));     // dfn初始化为0表示未访问
    memset(low, 0, sizeof(low));
    memset(in_stack, false, sizeof(in_stack));
    memset(scc_id, 0, sizeof(scc_id));
    memset(scc_size, 0, sizeof(scc_size));
    timestamp = scc_cnt = 0;         // 全局计数器清零
    while (!stk.empty()) stk.pop();  // 清空栈
    
    // 对所有未访问节点执行Tarjan
    for (int i = 1; i <= n; i++) {   // 遍历所有节点（编号1~n）
        if (dfn[i] == 0) {           // 如果节点i未访问
            tarjan(i);               // 从i开始DFS
        }
    }
    
    cout << "强连通分量总数: " << scc_cnt << endl;
    for (int i = 1; i <= n; i++) {
        cout << "节点 " << i << " 属于 SCC " << scc_id[i] << endl;
    }
    
    return 0;
}
```

## 复杂度分析

### 时间复杂度
- **O(V + E)**：每个节点访问一次，每条边遍历一次
- DFS本质决定，与图的结构无关

### 空间复杂度
- **O(V)**：dfn、low、in_stack、scc_id数组各O(V)
- 栈空间最坏O(V)（当图为一个SCC时）

## 笔试面试高频考点

### Q1: 为什么用`min(low[u], dfn[v])`而不是`min(low[u], low[v])`处理回边？
**答**: 实际上两种写法都可以，因为v在栈中意味着v的SCC尚未确定，low[v]可能还在变化。但dfn[v]是固定的，更稳定。标准写法用dfn[v]。

### Q2: Tarjan算法能处理重边和自环吗？
**答**: 可以。重边会多次遍历，不影响正确性；自环会形成长度为1的SCC，low[u]会被更新为dfn[u]，不影响结果。

### Q3: 如何判断整个图是否是强连通的？
**答**: 执行Tarjan后检查`scc_cnt == 1`，即只有一个强连通分量。

## ML/DL关联

### 在图神经网络(GNN)中的应用

```python
# PyG风格的SCC预处理示例
import torch
from torch_geometric.data import Data

def tarjan_preprocessing(edge_index, num_nodes):
    """
    使用Tarjan算法识别SCC，合并冗余节点
    降低GNN计算复杂度，提升训练效率
    """
    # 1. 执行Tarjan算法获取scc_id
    scc_id = tarjan(edge_index, num_nodes)  # 返回每个节点的SCC编号
    
    # 2. 构建SCC级别的图（超节点图）
    scc_edge_index = build_scc_graph(edge_index, scc_id)
    
    # 3. 聚合SCC内部特征
    scc_features = scatter_mean(node_features, scc_id, dim=0)
    
    return scc_edge_index, scc_features
```

### SCC在图分类任务中的价值
- **结构特征**：SCC数量、最大SCC大小作为图的全局特征
- **降维加速**：将大图收缩为DAG，降低GNN消息传递的计算量
- **社区发现**：SCC天然形成紧密社区，可用于预训练任务

## 同类题目推荐

| 平台 | 题号 | 题目名 | 难度 | 考点 |
|------|------|--------|------|------|
| 洛谷 | P2863 | 冗余路径 | 普及/提高 | SCC计数 |
| 力扣 | 1192 | 查找集群中的关键连接 | 困难 | 桥与SCC |
| 牛客 | - | 强连通分量 | 中等 | 模板题 |
| Codeforces | 427C | Checkposts | 1700 | 最小权SCC覆盖 |

## 易错点总结

1. **忘记清空全局变量**：多组数据时timestamp、scc_cnt必须重置
2. **栈操作顺序错误**：SCC出栈要用do-while确保至少出一个节点
3. **横叉边误处理**：已访问但不在栈中的节点不更新low
4. **数组大小不足**：1e5+5要预留余量防止越界
