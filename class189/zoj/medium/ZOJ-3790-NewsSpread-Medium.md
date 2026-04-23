# ZOJ-3790-NewsSpread-Medium

## 问题链接
http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=3790

## 问题描述
给定一个有向图，代表社交网络。新闻最初从某些节点开始传播。当一个节点收到新闻后，它会在下一时刻将其转发给所有邻居节点。我们想知道经过足够长的时间后，哪些节点收到了新闻。

更具体地说，如果有向图中存在强连通分量，那么一旦该强连通分量中的任何一个节点收到新闻，整个强连通分量都会收到新闻。

因此，问题转化为：给定一些初始节点，问经过强连通分量内部传播后，总共能到达多少个节点。

## 输入格式
- 第一行：三个整数 N, M, K，分别表示节点数、边数、初始节点数
- 接下来 M 行：每行包含两个整数 u, v，表示存在从 u 到 v 的有向边
- 接下来 K 行：每行包含一个整数 x，表示初始节点

## 输出格式
- 一行：一个整数，表示最终能到达的节点总数

## 样例输入
```
5 5 1
1 2
2 3
3 1
2 4
4 5
1
```

## 样例输出
```
4
```

## 笔试/面试考察点分析
- 强连通分量的识别与传播特性
- 缩点后DAG的BFS/DFS遍历
- 图论中的可达性问题
- 复杂度分析：O(N+M)
- 与机器学习中信息传播模型的关联

## 解题思路
1. 使用 Tarjan 算法找出所有强连通分量
2. 将强连通分量缩点，构建缩点后的 DAG
3. 将初始节点映射到对应的强连通分量
4. 在缩点后的 DAG 上进行 BFS 或 DFS，找到所有可达的强连通分量
5. 统计所有可达强连通分量中的节点总数

## 完整代码实现

```cpp
#include <iostream>
#include <vector>
#include <stack>
#include <algorithm>
#include <cstring>
#include <queue>
using namespace std;

const int MAXN = 100005; // 数组最大长度，笔试按数据范围调整，面试需说明取值依据
vector<int> adj[MAXN]; // 原始有向图邻接表，ML中图数据常用邻接表存储
vector<int> adj_shrink[MAXN]; // 缩点后DAG的邻接表，简化图问题的关键
int dfn[MAXN], low[MAXN], timestamp = 0; // Tarjan算法：dfn时间戳/low最早回溯时间戳，核心数组
bool in_stack[MAXN]; // Tarjan算法：标记节点是否在栈中，判断环的关键
stack<int> st; // Tarjan算法：存储当前遍历路径节点
int scc_id[MAXN], scc_cnt = 0; // scc_id[i]节点i所属分量ID/scc_cnt分量总数，缩点核心标识
int scc_size[MAXN]; // 每个强连通分量的节点数，笔试高频统计需求

// Tarjan算法求强连通分量：一次DFS完成SCC识别，笔试最常用模板
// 笔试中需快速手写，面试高频考察low数组更新逻辑；ML中可用于图核心结构提取
void tarjan(int u) {
    dfn[u] = low[u] = ++timestamp; // 初始化时间戳：当前节点发现时间=最早回溯时间
    st.push(u); // 节点入栈，记录遍历路径
    in_stack[u] = true; // 标记栈内状态，避免重复处理
    for (int v : adj[u]) { // 遍历所有出边
        if (!dfn[v]) { // 未访问过的节点，递归遍历
            tarjan(v);
            low[u] = min(low[u], low[v]); // 回溯更新low[u]：取子节点low最小值
        } else if (in_stack[v]) { // 已访问且在栈中（属于当前强连通分量）
            low[u] = min(low[u], dfn[v]); // 用子节点发现时间更新low[u]
        }
    }
    // 找到强连通分量的根节点（low[u]=dfn[u]），出栈标记分量
    if (dfn[u] == low[u]) {
        scc_cnt++; // 分量计数+1
        int v;
        do {
            v = st.top();
            st.pop();
            in_stack[v] = false; // 标记出栈
            scc_id[v] = scc_cnt; // 记录节点所属分量ID
            scc_size[scc_cnt]++; // 统计分量节点数
        } while (v != u); // 直到当前节点出栈，分量标记完成
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, m, k; // n节点数，m边数，k初始节点数，笔试需注意输入格式正确性
    cin >> n >> m >> k;
    
    for (int i = 0; i < m; i++) { // 建原始图
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);
    }

    // Tarjan算法求SCC
    for (int i = 1; i <= n; i++) {
        if (!dfn[i]) tarjan(i); // 未访问节点启动Tarjan
    }
    
    // 缩点：构建DAG
    for (int u = 1; u <= n; u++) {
        for (int v : adj[u]) {
            if (scc_id[u] != scc_id[v]) { // 边的两端属于不同分量，DAG加边
                adj_shrink[scc_id[u]].push_back(scc_id[v]); // 缩点后DAG加边
            }
        }
    }
    
    // 标记初始强连通分量
    bool start_scc[MAXN];
    memset(start_scc, false, sizeof(start_scc));
    for (int i = 0; i < k; i++) {
        int x;
        cin >> x;
        start_scc[scc_id[x]] = true; // 标记初始节点所属的强连通分量
    }
    
    // BFS遍历DAG，找到所有可达的强连通分量
    bool visited[MAXN];
    memset(visited, false, sizeof(visited));
    queue<int> q;
    
    // 将所有初始强连通分量加入队列
    for (int i = 1; i <= scc_cnt; i++) {
        if (start_scc[i]) {
            if (!visited[i]) {
                visited[i] = true;
                q.push(i);
            }
        }
    }
    
    int total_nodes = 0; // 统计最终能到达的节点总数
    
    // BFS遍历缩点后的DAG
    while (!q.empty()) {
        int curr_scc = q.front();
        q.pop();
        
        // 加上当前强连通分量的节点数
        total_nodes += scc_size[curr_scc];
        
        // 遍历当前强连通分量的所有邻居分量
        for (int next_scc : adj_shrink[curr_scc]) {
            if (!visited[next_scc]) {
                visited[next_scc] = true;
                q.push(next_scc);
            }
        }
    }
    
    cout << total_nodes << endl; // 输出最终能到达的节点总数
    
    return 0;
}
```

## 代码逐行注释
- 代码中几乎每一行都有中文注释，解释了：
  - 该行代码的功能
  - 为什么这么写
  - 笔试面试中可能的优化点
  - 与强连通分量/缩点核心逻辑的关联
  - 若涉及ML/DL需说明该代码在特征提取/模型优化中的作用

## 时间/空间复杂度分析
- 时间复杂度：O(N+M)，其中N为节点数，M为边数
  - Tarjan算法：O(N+M)，每个节点和边只访问一次
  - 缩点建图：O(M)，遍历所有边建立DAG
  - BFS遍历：O(SCC_CNT + DAG_EDGES) = O(N+M)
  - 总体：O(N+M)
- 空间复杂度：O(N+M)
  - 邻接表：O(N+M)
  - Tarjan辅助数组：O(N)
  - BFS辅助数组：O(N)
  - 总体：O(N+M)

## 同类题目拓展
- POJ 2186 - Popular Cows（类似缩点后DAG性质分析）
- HDU 2767 - Proving Equivalences（强连通分量+缩点后变成强连通的最少加边数）
- Codeforces 427C - Checkposts（强连通分量最小点权问题）
- LeetCode 1192 - Critical Connections in a Network（桥边查找）

## ML/DL关联思考
- 在社交网络分析中，强连通分量代表紧密联系的社区
- 新闻传播模型可映射到图神经网络中的信息传播机制
- 缩点后的DAG结构可用于设计分层信息传播模型
- 强连通分量内的信息同步可简化GNN的消息传递过程