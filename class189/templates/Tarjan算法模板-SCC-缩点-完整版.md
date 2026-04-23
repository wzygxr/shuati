# Tarjan算法模板 - 强连通分量与缩点完整版

## 概述
Tarjan算法是一种用于求解有向图强连通分量(SCC)的线性时间算法，由Robert Tarjan于1972年提出。

**时间复杂度**: O(N+M)  
**空间复杂度**: O(N+M)  
**核心思想**: 基于DFS，利用时间戳和回溯时间识别强连通分量

## 核心概念

### 1. 强连通分量(SCC)
在有向图中，如果任意两个顶点u和v，都存在从u到v的路径和从v到u的路径，则称这些顶点构成一个强连通分量。

### 2. dfn数组 (Discovery Time)
`dfn[u]` 表示节点u在DFS过程中被访问的时间戳，从1开始递增。

### 3. low数组 (Lowest Reachable)
`low[u]` 表示从节点u出发，通过DFS树边和回边能到达的最小时间戳。

### 4. 强连通分量根节点判定
当 `dfn[u] == low[u]` 时，u是某个强连通分量的根节点。

## 完整模板代码

### C++版本

```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 100005; // 最大节点数，根据题目数据范围调整
const int MAXM = 500005; // 最大边数，根据题目数据范围调整

// ==================== 图的存储 ====================
int head[MAXN], to[MAXM], nxt[MAXM], edge_cnt = 0; // 链式前向星

/**
 * 添加有向边
 * @param u 边的起点
 * @param v 边的终点
 * 链式前向星是竞赛中最高效的图存储方式
 */
void addEdge(int u, int v) {
    to[++edge_cnt] = v; // 存储边的终点
    nxt[edge_cnt] = head[u]; // 新边的next指向原来的第一条边
    head[u] = edge_cnt; // 更新头指针指向新边
}

// ==================== Tarjan算法核心变量 ====================
int dfn[MAXN], low[MAXN], timestamp = 0; // dfn时间戳，low最早回溯时间，全局时间戳计数器
bool in_stack[MAXN]; // 标记节点是否在栈中
stack<int> st; // Tarjan算法用栈，存储当前DFS路径上的节点

int scc_id[MAXN], scc_cnt = 0; // scc_id[i]表示节点i所属的强连通分量编号，scc_cnt为分量总数
vector<vector<int>> scc_nodes; // 存储每个强连通分量包含的所有节点（可选）

// ==================== Tarjan算法核心函数 ====================

/**
 * Tarjan算法DFS遍历
 * @param u 当前访问的节点
 * 核心逻辑：
 * 1. 初始化dfn[u]和low[u]为当前时间戳
 * 2. 将u入栈，标记in_stack[u]=true
 * 3. 遍历u的所有出边
 *    - 如果v未访问，递归访问，并用low[v]更新low[u]
 *    - 如果v已访问且在栈中，用dfn[v]更新low[u]
 * 4. 如果dfn[u]==low[u]，弹出栈中节点直到u，这些节点构成一个SCC
 */
void tarjan(int u) {
    // 步骤1：初始化当前节点的dfn和low值
    dfn[u] = low[u] = ++timestamp; // 时间戳加1，标记当前节点的发现时间
    st.push(u); // 当前节点入栈，加入当前DFS路径
    in_stack[u] = true; // 标记节点已在栈中
    
    // 步骤2：遍历当前节点的所有出边
    for (int i = head[u]; i; i = nxt[i]) { // 链式前向星遍历
        int v = to[i]; // 获取边的终点v
        
        if (dfn[v] == 0) { // 情况1：v未被访问过（dfn为0表示未访问）
            tarjan(v); // 递归访问v，深入DFS
            // 递归返回后，用v的low值更新u的low值
            // 原理：u能通过v到达的最早节点，也是u能到达的最早节点
            low[u] = min(low[u], low[v]);
        } 
        // 情况2：v已访问且在栈中，说明v是当前DFS路径上的节点，形成环
        else if (in_stack[v]) { 
            // 用v的发现时间更新u的low值
            // 注意：这里用dfn[v]而不是low[v]，因为v在栈中，其low还未最终确定
            low[u] = min(low[u], dfn[v]);
        }
        // 情况3：v已访问但不在栈中，说明v属于其他SCC，无需处理
    }
    
    // 步骤3：判断当前节点是否为强连通分量的根节点
    // 当dfn[u] == low[u]时，说明u无法通过任何路径到达发现时间更早的节点
    if (dfn[u] == low[u]) {
        scc_cnt++; // 强连通分量计数加1，发现新的SCC
        vector<int> nodes; // 存储当前SCC的所有节点（可选）
        int v;
        
        // 从栈中弹出节点，直到弹出u为止，这些节点构成一个SCC
        do {
            v = st.top(); // 获取栈顶节点
            st.pop(); // 栈顶节点出栈
            in_stack[v] = false; // 标记节点已不在栈中
            scc_id[v] = scc_cnt; // 记录节点v属于当前SCC
            nodes.push_back(v); // 将节点加入当前SCC的节点列表（可选）
        } while (v != u); // 循环直到弹出u本身，此时一个SCC完整提取
        
        scc_nodes.push_back(nodes); // 保存当前SCC的节点列表（可选）
    }
}

// ==================== 缩点相关 ====================

vector<int> dag[MAXN]; // 缩点后DAG的邻接表
int in_deg[MAXN]; // DAG节点入度（用于拓扑排序）
int out_deg[MAXN]; // DAG节点出度

/**
 * 缩点：将强连通分量收缩为单个节点，构建DAG
 * @param n 原图节点数
 * 缩点后：
 * - DAG节点数 = scc_cnt
 * - 原图中u到v的边，如果scc_id[u] != scc_id[v]，则在DAG中加边
 */
void shrink(int n) {
    for (int u = 1; u <= n; u++) { // 遍历原图所有节点
        for (int i = head[u]; i; i = nxt[i]) { // 遍历u的所有出边
            int v = to[i]; // 边的终点
            
            // 如果u和v属于不同的强连通分量，在DAG中添加边
            if (scc_id[u] != scc_id[v]) {
                dag[scc_id[u]].push_back(scc_id[v]); // DAG中加边
                out_deg[scc_id[u]]++; // 出度+1
                in_deg[scc_id[v]]++; // 入度+1
            }
        }
    }
}

// ==================== 拓扑排序 ====================

vector<int> topo_order; // 拓扑序结果

/**
 * Kahn算法进行拓扑排序
 * @return true如果成功（无环），false如果有环
 * 注意：缩点后的图一定是DAG，拓扑排序一定成功
 */
bool topologicalSort() {
    queue<int> q; // 拓扑排序队列
    topo_order.clear(); // 清空拓扑序
    
    // 初始化：所有入度为0的节点入队
    for (int i = 1; i <= scc_cnt; i++) {
        if (in_deg[i] == 0) {
            q.push(i);
        }
    }
    
    // BFS拓扑排序
    while (!q.empty()) {
        int u = q.front();
        q.pop();
        topo_order.push_back(u); // 记录拓扑序
        
        // 遍历u的所有出边
        for (int v : dag[u]) {
            in_deg[v]--; // 移除u->v边，v的入度减1
            if (in_deg[v] == 0) { // 如果v的入度变为0，入队
                q.push(v);
            }
        }
    }
    
    // 如果拓扑序包含所有DAG节点，说明无环（缩点后一定是DAG）
    return topo_order.size() == scc_cnt;
}

// ==================== 初始化函数 ====================

/**
 * 初始化所有全局变量
 * 每次处理新测试用例前调用
 */
void init() {
    // 清空链式前向星
    memset(head, 0, sizeof(head));
    edge_cnt = 0;
    
    // 清空Tarjan数组
    memset(dfn, 0, sizeof(dfn));
    memset(low, 0, sizeof(low));
    memset(in_stack, false, sizeof(in_stack));
    timestamp = 0;
    
    // 清空SCC相关
    memset(scc_id, 0, sizeof(scc_id));
    scc_cnt = 0;
    scc_nodes.clear();
    while (!st.empty()) st.pop();
    
    // 清空缩点和拓扑排序相关
    for (int i = 0; i < MAXN; i++) {
        dag[i].clear();
    }
    memset(in_deg, 0, sizeof(in_deg));
    memset(out_deg, 0, sizeof(out_deg));
    topo_order.clear();
}

// ==================== 主函数示例 ====================

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, m; // n:节点数 m:边数
    cin >> n >> m;
    
    init(); // 初始化
    
    // 建图
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        addEdge(u, v); // 添加有向边
    }
    
    // 对所有未访问的节点执行Tarjan算法
    for (int i = 1; i <= n; i++) {
        if (dfn[i] == 0) { // 如果节点i未访问
            tarjan(i);
        }
    }
    
    // 缩点
    shrink(n);
    
    // 拓扑排序
    topologicalSort();
    
    // 输出结果
    cout << "强连通分量数量: " << scc_cnt << endl;
    cout << "拓扑序: ";
    for (int x : topo_order) {
        cout << x << " ";
    }
    cout << endl;
    
    return 0;
}
```

## 关键要点总结

### 1. dfn和low的更新规则
| 情况 | 条件 | 操作 |
|------|------|------|
| 未访问 | `dfn[v] == 0` | `tarjan(v); low[u] = min(low[u], low[v]);` |
| 在栈中 | `in_stack[v] == true` | `low[u] = min(low[u], dfn[v]);` |
| 已出栈 | `in_stack[v] == false` | 不处理（v属于其他SCC） |

### 2. SCC根节点判定
- **条件**: `dfn[u] == low[u]`
- **含义**: u无法到达发现时间更早的节点
- **操作**: 弹出栈中节点直到u，这些节点构成一个SCC

### 3. 缩点的作用
- 将有环图转换为DAG
- DAG上可以拓扑排序
- 可以在DAG上进行动态规划

### 4. 常见错误
1. **忘记初始化**: 每次测试用例前必须调用`init()`
2. **栈标记错误**: 出栈时忘记设置`in_stack[v] = false`
3. **dfn和low混淆**: 更新时混淆`dfn`和`low`的使用场景
4. **缩点重复边**: DAG中可能出现重边，某些题目需要去重

## 复杂度分析

### 时间复杂度
- **Tarjan算法**: O(N+M)
  - 每个节点访问一次
  - 每条边遍历一次
- **缩点**: O(N+M)
- **拓扑排序**: O(SCC_cnt + E_dag)
- **总时间复杂度**: O(N+M)

### 空间复杂度
- **链式前向星**: O(N+M)
- **Tarjan数组**: O(N)
- **缩点后DAG**: O(N+M)
- **总空间复杂度**: O(N+M)

## 应用场景

### 1. 有向图缩点
将强连通分量合并，简化图结构。

### 2. DAG上的动态规划
缩点后得到DAG，可以按拓扑序进行DP。

### 3. 最少加边使图强连通
- 缩点后统计入度为0和出度为0的SCC数量
- 答案为`max(入度为0的SCC数, 出度为0的SCC数)`

### 4. 找"明星"节点
- 找出度为0的SCC
- 如果只有一个且大小>0，则其中节点可被所有节点到达

## 笔试面试口诀

```
Tarjan算法三步走：
一、初始化dfn和low，节点入栈
二、遍历出边，递归更新low
三、dfn==low，出栈标记SCC

缩点逻辑很简单：
不同SCC之间，原图有边就连

DAG上DP按拓扑序，
入度为0先入队，动态规划最省力
```
