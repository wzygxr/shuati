### 题目链接
https://leetcode.cn/problems/critical-connections-in-a-network/

### 题目描述
给定一个无向图，找到所有的关键连接（桥）。

### 笔试/面试考察点分析
- 考察桥的概念和应用
- Tarjan算法求桥的实现
- 时间复杂度分析（O(n+m)）
- 空间复杂度分析（O(n+m)）
- 结果的输出格式（按边的顺序输出）

### 解题思路
1. 使用Tarjan算法遍历无向图
2. 对于每条边，判断是否为桥：low[v] > dfn[u]
3. 收集所有桥并输出

### 代码实现
```cpp
#include <vector>
#include <algorithm>
using namespace std;

class Solution {
public:
    vector<vector<int>> criticalConnections(int n, vector<vector<int>>& connections) {
        vector<vector<int>> adj(n);
        for (auto& conn : connections) {
            adj[conn[0]].push_back(conn[1]);
            adj[conn[1]].push_back(conn[0]);
        }
        
        vector<int> dfn(n, -1), low(n, -1);
        vector<vector<int>> bridges;
        int timestamp = 0;
        
        function<void(int, int)> tarjan = [&](int u, int parent) {
            dfn[u] = low[u] = timestamp++;
            for (int v : adj[u]) {
                if (v == parent) continue;
                if (dfn[v] == -1) {
                    tarjan(v, u);
                    low[u] = min(low[u], low[v]);
                    if (low[v] > dfn[u]) {
                        bridges.push_back({u, v});
                    }
                } else {
                    low[u] = min(low[u], dfn[v]);
                }
            }
        };
        
        for (int i = 0; i < n; ++i) {
            if (dfn[i] == -1) {
                tarjan(i, -1);
            }
        }
        
        return bridges;
    }
};
```

### 代码逐行注释
```cpp
#include <vector>
#include <algorithm>
using namespace std;

class Solution {
public:
    vector<vector<int>> criticalConnections(int n, vector<vector<int>>& connections) {
        vector<vector<int>> adj(n); // 邻接表存储无向图
        for (auto& conn : connections) {
            adj[conn[0]].push_back(conn[1]);
            adj[conn[1]].push_back(conn[0]);
        }
        
        vector<int> dfn(n, -1), low(n, -1); // 时间戳数组
        vector<vector<int>> bridges; // 存储桥的结果
        int timestamp = 0; // 时间戳计数器
        
        // Tarjan算法求桥
        function<void(int, int)> tarjan = [&](int u, int parent) {
            dfn[u] = low[u] = timestamp++;
            for (int v : adj[u]) {
                if (v == parent) continue; // 跳过父节点
                if (dfn[v] == -1) { // 邻接节点未被访问
                    tarjan(v, u); // 递归遍历
                    low[u] = min(low[u], low[v]); // 更新low值
                    if (low[v] > dfn[u]) { // 判断是否为桥
                        bridges.push_back({u, v});
                    }
                } else { // 邻接节点已被访问
                    low[u] = min(low[u], dfn[v]); // 更新low值
                }
            }
        };
        
        // 遍历所有连通块
        for (int i = 0; i < n; ++i) {
            if (dfn[i] == -1) {
                tarjan(i, -1);
            }
        }
        
        return bridges;
    }
};
```

### 时间/空间复杂度分析
- 时间复杂度：O(n+m)，n为节点数，m为边数
- 空间复杂度：O(n+m)，存储邻接表和时间戳数组

### 同类题目拓展
- POJ 3177 冗余路径（边双连通分量）
- POJ 2117 电力（割点应用）
- 洛谷 3225 矿场搭建（点双连通分量）

### ML/DL关联思考
- 桥的识别可用于图结构数据的关键边提取
- 边双连通分量可用于图的社区划分
- 在GNN中，桥和边双连通分量可作为图的结构特征，提升模型性能