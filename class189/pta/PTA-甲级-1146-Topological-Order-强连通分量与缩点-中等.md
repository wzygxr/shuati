# PTA 甲级 1146 Topological Order - 强连通分量与缩点

## 题目信息
- **平台**: PTA (浙江大学程序设计自动评测系统)
- **题号**: 甲级 1146
- **标题**: Topological Order
- **难度**: 中等
- **时间限制**: 400ms
- **内存限制**: 64MB

## 题目链接
- https://pintia.cn/problem-sets/994805342720868352/problems/994805482919633920

## 题目描述
给定一个有向无环图（DAG），判断给定的序列是否是该图的一个拓扑序。

如果对于图中的每条边(u, v)，u在序列中都出现在v之前，则该序列是一个拓扑序。

## 输入格式
第一行给出两个整数N（节点数，≤ 1000）和M（边数，≤ 10000）。

接下来M行，每行给出一个边连接的两个节点编号（节点编号从1到N）。

接下来一行给出查询数量K（≤ 100）。

接下来K行，每行给出一个包含N个整数的序列，表示一个待验证的拓扑序。

## 输出格式
对于每个非拓扑序的查询，输出其编号（从0开始计数）。

所有编号按升序输出在一行中，用空格分隔。

## 样例输入
```
6 6
2 3
1 5
5 6
3 4
4 6
1 2
5
1 5 2 3 6 4
5 1 2 6 3 4
5 1 2 3 6 4
5 2 1 6 3 4
1 2 3 4 5 6
```

## 样例输出
```
0 1 3
```

## 笔试/面试考察点分析

### 核心考察点
1. **拓扑排序的定义与性质**：DAG的线性化表示
2. **拓扑序验证算法**：O(M)时间验证一个序列是否为拓扑序
3. **缩点的关联**：拓扑排序是缩点后DAG的核心应用
4. **批量查询处理**：高效处理多个验证请求

### 面试高频提问
1. **拓扑排序与强连通分量的关系**？
   - 强连通分量缩点后得到DAG
   - DAG上才能进行拓扑排序
   - 这是缩点→DAG→拓扑排序的经典应用链

2. **如何快速判断一个序列是否为拓扑序**？
   - 方法1：Kahn算法生成拓扑序，对比是否相同
   - 方法2：预处理每个节点的位置，检查所有边
   - 本题适合方法2，O(N+M)预处理，O(M)验证

3. **如果不是DAG，如何判断拓扑序**？
   - 需要先缩点，将强连通分量视为一个整体
   - 在缩点后的DAG上判断拓扑序

## 解题思路

### 核心思想：位置数组+边检查
1. 预处理：对于待验证序列，记录每个节点的位置`pos[node]`
2. 检查：对于每条边(u, v)，验证`pos[u] < pos[v]`
3. 如果所有边都满足，则是拓扑序；否则不是

### 时间复杂度
- 预处理位置：O(N)
- 验证边：O(M)
- 每个查询总复杂度：O(N+M)

## 完整代码实现

```cpp
#include <iostream>
#include <vector>
#include <cstring>
using namespace std;

const int MAXN = 1005; // 最大节点数，根据题目N ≤ 1000设定
const int MAXM = 10005; // 最大边数，根据题目M ≤ 10000设定

// 边结构体，存储有向边的起点和终点
struct Edge {
    int u, v; // u:起点 v:终点
};

vector<Edge> edges; // 存储所有边的数组，用于批量验证
int pos[MAXN]; // 位置数组：pos[i]表示节点i在待验证序列中的位置

/**
 * 验证给定序列是否为拓扑序
 * @param n 节点数量
 * @return true如果是拓扑序，false否则
 * 笔试面试要点：拓扑序验证是DAG的基础操作
 * ML关联：拓扑序可作为图神经网络的节点处理顺序
 */
bool checkTopologicalOrder(int n) {
    // 遍历所有边，检查每条边是否满足拓扑序性质
    for (const Edge& e : edges) {
        // 拓扑序要求：对于边(u,v)，u的位置必须在v之前
        if (pos[e.u] > pos[e.v]) {
            // 发现逆序边，不是拓扑序
            return false;
        }
    }
    // 所有边都满足条件，是拓扑序
    return true;
}

int main() {
    int n, m; // n:节点数 m:边数
    cin >> n >> m; // 读取节点数和边数
    
    // 读取M条有向边
    for (int i = 0; i < m; i++) {
        int u, v; // 边的起点和终点
        cin >> u >> v; // 读取边
        edges.push_back({u, v}); // 存储边到数组
    }
    
    int k; // 查询数量
    cin >> k; // 读取查询数
    
    vector<int> invalidQueries; // 存储非拓扑序的查询编号
    
    // 处理每个查询
    for (int queryId = 0; queryId < k; queryId++) {
        // 读取当前查询的序列
        for (int i = 1; i <= n; i++) {
            int node; // 序列中的第i个节点
            cin >> node; // 读取节点
            pos[node] = i; // 记录节点node在序列中的位置为i
        }
        
        // 验证当前序列是否为拓扑序
        if (!checkTopologicalOrder(n)) {
            // 不是拓扑序，记录查询编号
            invalidQueries.push_back(queryId);
        }
    }
    
    // 输出结果：所有非拓扑序的查询编号
    for (int i = 0; i < invalidQueries.size(); i++) {
        if (i > 0) cout << " "; // 编号之间用空格分隔
        cout << invalidQueries[i]; // 输出编号
    }
    
    return 0; // 程序正常结束
}
```

## Python实现

```python
import sys
from collections import defaultdict

def check_topological_order(edges, pos, n):
    """
    验证序列是否为拓扑序
    :param edges: 边列表
    :param pos: 节点位置字典
    :param n: 节点数
    :return: True如果是拓扑序
    笔试要点：拓扑序验证的核心逻辑
    """
    for u, v in edges:  # 遍历所有边
        # 检查是否满足拓扑序：起点在终点之前
        if pos[u] > pos[v]:
            return False  # 发现逆序
    return True  # 所有边都满足

def main():
    # 读取节点数和边数
    n, m = map(int, input().split())
    
    edges = []  # 存储所有边
    
    # 读取M条有向边
    for _ in range(m):
        u, v = map(int, input().split())
        edges.append((u, v))  # 添加有向边
    
    k = int(input())  # 读取查询数量
    
    invalid_queries = []  # 非拓扑序的查询编号
    
    # 处理K个查询
    for query_id in range(k):
        sequence = list(map(int, input().split()))  # 读取序列
        
        # 构建位置映射：节点->位置
        pos = {}
        for idx, node in enumerate(sequence, 1):  # 位置从1开始
            pos[node] = idx
        
        # 验证是否为拓扑序
        if not check_topological_order(edges, pos, n):
            invalid_queries.append(query_id)  # 记录非拓扑序编号
    
    # 输出结果
    print(' '.join(map(str, invalid_queries)))

if __name__ == "__main__":
    main()
```

## 进阶：关联强连通分量的版本

如果题目给的不是DAG，而是普通有向图，需要先缩点：

```cpp
// 缩点后判断拓扑序的扩展代码
#include <iostream>
#include <vector>
#include <stack>
#include <cstring>
using namespace std;

const int MAXN = 1005;

vector<int> adj[MAXN]; // 原始图邻接表
vector<int> adj_dag[MAXN]; // 缩点后DAG邻接表
int dfn[MAXN], low[MAXN], timestamp = 0; // Tarjan算法数组
bool in_stack[MAXN]; // 栈标记
stack<int> st; // Tarjan栈
int scc_id[MAXN], scc_cnt = 0; // 强连通分量编号和计数

// Tarjan算法求强连通分量
void tarjan(int u) {
    dfn[u] = low[u] = ++timestamp; // 初始化时间戳
    st.push(u); // 节点入栈
    in_stack[u] = true; // 标记在栈中
    
    for (int v : adj[u]) { // 遍历出边
        if (!dfn[v]) { // 未访问
            tarjan(v); // 递归访问
            low[u] = min(low[u], low[v]); // 更新low值
        } else if (in_stack[v]) { // 已访问且在栈中
            low[u] = min(low[u], dfn[v]); // 用dfn更新low
        }
    }
    
    // 发现强连通分量根节点
    if (dfn[u] == low[u]) {
        scc_cnt++; // 分量计数+1
        int v;
        do {
            v = st.top();
            st.pop();
            in_stack[v] = false;
            scc_id[v] = scc_cnt; // 标记所属分量
        } while (v != u);
    }
}

// 缩点：构建DAG
void shrink(int n) {
    for (int u = 1; u <= n; u++) {
        for (int v : adj[u]) {
            if (scc_id[u] != scc_id[v]) { // 不同分量间加边
                adj_dag[scc_id[u]].push_back(scc_id[v]);
            }
        }
    }
}
```

## 时间/空间复杂度分析

### 时间复杂度
- **预处理位置**：O(N) 每个查询
- **验证边**：O(M) 每个查询
- **每个查询总复杂度**：O(N+M)
- **K个查询总复杂度**：O(K × (N+M))

### 空间复杂度
- **边存储**：O(M)
- **位置数组**：O(N)
- **总空间复杂度**：O(N+M)

## 同类题目拓展

### 拓扑排序核心题
- **PTA 甲级 1146** - 本题，拓扑序验证
- **PTA 乙级 1131** - 拓扑排序基础
- **洛谷 P1347 排序** - 拓扑排序+判环

### 缩点+拓扑排序综合题
- **洛谷 P3387 【模板】缩点** - 缩点后DAG上DP
- **AcWing 367 学校网络** - 缩点+最少加边
- **POJ 1236 Network of Schools** - 经典缩点题

## 面试变种方向
1. **输出所有拓扑序**：DFS枚举所有可能
2. **字典序最小拓扑序**：优先队列维护入度为0节点
3. **判断唯一拓扑序**：检查每一层是否只有一个入度为0节点

## ML/DL关联思考

### 1. 拓扑序在GNN中的应用
- **节点处理顺序**：DAG上的拓扑序可指导GNN的消息传递顺序
- **层次化传播**：按拓扑层次逐层传播节点特征
- **代码示例**：
```python
# 按拓扑序进行消息传递
for node in topological_order:
    for neighbor in adj[node]:
        # 按拓扑序聚合邻居特征
        aggregated[node] += message_passing(neighbor)
```

### 2. 缩点+拓扑排序的ML价值
- **图粗化**：强连通分量缩点降低图规模
- **层次化表示**：缩点后的DAG提供层次化图结构
- **特征传播优化**：在DAG上按拓扑序传播更高效

### 3. 面试标准答案：拓扑排序服务ML任务
**问题**：如何用拓扑排序优化图神经网络？
**答案框架**：
1. 对于DAG输入，拓扑序确保信息单向传播，避免循环依赖
2. 对于一般图，先缩点得到DAG，再拓扑排序，分层传播
3. 拓扑序可作为节点的位置编码，增强GNN的位置感知能力
4. 在时序图（Temporal Graph）中，时间顺序天然形成拓扑序
