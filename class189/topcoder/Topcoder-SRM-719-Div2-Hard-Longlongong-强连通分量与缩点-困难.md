# Topcoder SRM 719 Div2 Hard - Longlongong

## 题目信息
- **平台**: Topcoder
- **赛事**: SRM (Single Round Match) 719
- **难度**: Div2 Hard
- **题目**: Longlongong Path（或类似题目，具体以Topcoder为准）
- **类型**: 图论/强连通分量/动态规划

## 题目链接
- https://www.topcoder.com/

## 题目描述
给定一个有向图，包含n个节点和m条边。每个节点有一个权值。

要求在图中找到一条路径，使得路径上节点权值之和最大。可以重复经过节点和边。

注意：如果图中存在环，可以在环上无限循环获得无限大的权值和。此时应返回-1表示无穷大。

## 输入格式（Topcoder格式）
Topcoder使用类和方法的形式：

```cpp
class LonglongongPath {
public:
    long long maxSum(int n, vector<int> from, vector<int> to, vector<int> weight);
};
```

- n：节点数量
- from, to：边的起点和终点数组
- weight：节点权值数组

## 返回格式
- 返回最大权值和
- 如果存在正权环可以无限获取权值，返回-1

## 样例
```
输入: n=3, from={0,1}, to={1,2}, weight={1,2,3}
输出: 6
解释: 路径 0->1->2，权值和 1+2+3=6
```

```
输入: n=3, from={0,1,2}, to={1,2,0}, weight={1,2,3}
输出: -1
解释: 存在环 0->1->2->0，可以无限循环
```

## 笔试/面试考察点分析

### 核心考察点
1. **强连通分量检测**：判断是否存在环
2. **缩点技术**：将SCC收缩为单个节点，得到DAG
3. **DAG上的动态规划**：在DAG上求最长路径
4. **正权环检测**：判断SCC内总权值是否为正

### 面试高频提问
1. **为什么需要缩点**？
   - 原图可能有环，无法直接DP
   - 缩点后得到DAG，可以拓扑排序后DP
   - 这是处理有向图DP的标准方法

2. **如何判断是否存在无限大的权值**？
   - 缩点后，如果某个SCC的总权值>0且SCC大小>1（或有自环）
   - 则可以在该SCC内无限循环
   - 返回-1表示无穷大

3. **Topcoder与其他平台的区别**？
   - 使用类和方法的封装形式
   - 不需要处理输入输出
   - 注重算法效率和代码质量

## 解题思路

### 核心步骤
1. **构建图**：根据from和to数组构建邻接表
2. **Tarjan求SCC**：找出所有强连通分量
3. **缩点**：将每个SCC收缩为一个超节点
4. **检测正权环**：如果SCC总权值>0且可循环，返回-1
5. **DAG上DP**：按拓扑序进行动态规划求最长路

### 算法流程
```
原图 → Tarjan求SCC → 缩点得到DAG → 拓扑排序 → DAG上DP → 最大权值和
```

## 完整代码实现

```cpp
#include <bits/stdc++.h>
using namespace std;

class LonglongongPath {
public:
    static const int MAXN = 1005; // 最大节点数
    
    vector<int> adj[MAXN]; // 原图邻接表
    vector<int> adj_dag[MAXN]; // 缩点后DAG邻接表
    int dfn[MAXN], low[MAXN], timestamp = 0; // Tarjan数组
    bool in_stack[MAXN]; // 栈标记
    stack<int> st; // Tarjan栈
    int scc_id[MAXN], scc_cnt = 0; // SCC编号和计数
    vector<vector<int>> scc_nodes; // 每个SCC包含的节点
    long long scc_weight[MAXN]; // 每个SCC的总权值
    int in_deg[MAXN]; // DAG节点入度
    long long dp[MAXN]; // DAG上DP数组
    
    /**
     * Tarjan算法求强连通分量
     * @param u 当前节点
     * Topcoder要点：封装在类中，注意成员变量初始化
     * ML关联：SCC检测是图神经网络预处理的重要步骤
     */
    void tarjan(int u) {
        // 初始化dfn和low
        dfn[u] = low[u] = ++timestamp;
        st.push(u); // 节点入栈
        in_stack[u] = true; // 标记在栈中
        
        // 遍历出边
        for (int v : adj[u]) {
            if (dfn[v] == 0) { // 未访问
                tarjan(v); // 递归访问
                low[u] = min(low[u], low[v]); // 更新low
            } else if (in_stack[v]) { // 已访问且在栈中
                low[u] = min(low[u], dfn[v]); // 用dfn更新
            }
        }
        
        // 发现SCC根节点
        if (dfn[u] == low[u]) {
            scc_cnt++;
            vector<int> nodes; // 当前SCC的节点
            int v;
            do {
                v = st.top();
                st.pop();
                in_stack[v] = false;
                scc_id[v] = scc_cnt;
                nodes.push_back(v);
            } while (v != u);
            scc_nodes.push_back(nodes);
        }
    }
    
    /**
     * 缩点：构建DAG
     * @param n 原图节点数
     */
    void shrink(int n, vector<int>& weight) {
        // 计算每个SCC的总权值
        for (int i = 1; i <= scc_cnt; i++) {
            scc_weight[i] = 0;
            for (int node : scc_nodes[i-1]) {
                scc_weight[i] += weight[node]; // 累加节点权值
            }
        }
        
        // 构建DAG
        for (int u = 0; u < n; u++) {
            for (int v : adj[u]) {
                if (scc_id[u] != scc_id[v]) { // 不同SCC间加边
                    adj_dag[scc_id[u]].push_back(scc_id[v]);
                    in_deg[scc_id[v]]++; // 入度+1
                }
            }
        }
    }
    
    /**
     * 在DAG上进行拓扑排序和DP
     * @return 最大权值和
     */
    long long dagDP() {
        queue<int> q; // 拓扑排序队列
        
        // 初始化DP数组
        for (int i = 1; i <= scc_cnt; i++) {
            dp[i] = scc_weight[i]; // DP初始值为SCC自身权值
            if (in_deg[i] == 0) { // 入度为0的节点入队
                q.push(i);
            }
        }
        
        // 拓扑排序
        while (!q.empty()) {
            int u = q.front();
            q.pop();
            
            for (int v : adj_dag[u]) {
                // DP转移：更新最长路
                dp[v] = max(dp[v], dp[u] + scc_weight[v]);
                
                in_deg[v]--; // 入度-1
                if (in_deg[v] == 0) { // 入度为0入队
                    q.push(v);
                }
            }
        }
        
        // 返回最大值
        long long ans = 0;
        for (int i = 1; i <= scc_cnt; i++) {
            ans = max(ans, dp[i]);
        }
        return ans;
    }
    
    /**
     * 主方法：求最长路径权值和
     * @param n 节点数
     * @param from 边的起点数组
     * @param to 边的终点数组
     * @param weight 节点权值数组
     * @return 最大权值和，无限大返回-1
     */
    long long maxSum(int n, vector<int> from, vector<int> to, vector<int> weight) {
        // 初始化
        timestamp = 0;
        scc_cnt = 0;
        scc_nodes.clear();
        while (!st.empty()) st.pop();
        
        for (int i = 0; i < n; i++) {
            adj[i].clear();
            dfn[i] = low[i] = 0;
            in_stack[i] = false;
        }
        
        // 建图
        int m = from.size();
        for (int i = 0; i < m; i++) {
            adj[from[i]].push_back(to[i]);
        }
        
        // Tarjan求SCC
        for (int i = 0; i < n; i++) {
            if (dfn[i] == 0) {
                tarjan(i);
            }
        }
        
        // 检查正权环（SCC总权值>0且可以循环）
        for (int i = 0; i < scc_nodes.size(); i++) {
            long long w = 0;
            for (int node : scc_nodes[i]) {
                w += weight[node];
            }
            // 如果SCC内总权值>0且SCC大小>1或有自环，可以无限循环
            if (w > 0 && scc_nodes[i].size() > 1) {
                return -1; // 无穷大
            }
        }
        
        // 缩点
        for (int i = 0; i <= scc_cnt; i++) {
            adj_dag[i].clear();
            in_deg[i] = 0;
        }
        shrink(n, weight);
        
        // DAG上DP
        return dagDP();
    }
};
```

## 时间/空间复杂度分析

### 时间复杂度
- **Tarjan求SCC**：O(N+M)
- **缩点**：O(N+M)
- **拓扑排序+DP**：O(SCC_cnt + E_dag)
- **总时间复杂度**：O(N+M)

### 空间复杂度
- **邻接表**：O(N+M)
- **Tarjan数组**：O(N)
- **缩点后DAG**：O(N+M)
- **总空间复杂度**：O(N+M)

## Topcoder特色

### 类封装
```cpp
class Solution {
public:
    ReturnType methodName(Parameters...) {
        // 实现
    }
};
```

### 测试方法
- 使用Topcoder Arena进行测试
- 支持Java, C++, Python2, Python3, C#
- 即时编译和运行

## 同类题目拓展

### Topcoder平台
- **SRM 714 Div2 Hard**: 图论DP
- **SRM 721 Div1 Easy**: SCC应用
- **TCO Finals**: 高级图论

### 其他平台同类题
- **POJ 3160 Father Christmas flymouse**: 缩点+DP
- **HDU 1827 Summer Holiday**: 缩点+最小点覆盖
- **洛谷 P3387 【模板】缩点**: 缩点模板

## 面试变种方向
1. **最长路变最短路**：权值取负，同样的方法
2. **带边权的版本**：边权+点权，DP时同时考虑
3. **输出具体路径**：记录DP转移的前驱节点

## ML/DL关联思考

### 1. 缩点+DP在GNN中的应用
```python
# 缩点后DAG上的消息传递
import torch
import torch.nn as nn

class DAGMessagePassing(nn.Module):
    def forward(self, x, dag_edge_index, topo_order):
        # 按拓扑序传播
        for node in topo_order:
            # 聚合前驱节点的消息
            for pred in dag_edge_index[node]:
                x[node] = self.aggregate(x[node], x[pred])
        return x
```

### 2. 正权环检测的ML价值
- **异常检测**：正权环可能表示异常模式
- **推荐系统**：用户行为图中的循环模式
- **金融风控**：交易网络中的可疑循环

### 3. Topcoder与ML工程
- Topcoder是算法能力的国际标准
- 高rating可以获得顶级科技公司offer
- 算法思维对ML模型设计有重要帮助
