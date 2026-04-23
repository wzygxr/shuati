# UVA796-Critical Links-入门

## 题目信息

- **题目链接**: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=737
- **难度**: 入门
- **考察点**: 割边（桥）模板、排序输出

## 题目描述

### 题目背景

Critical Links

### 题目描述

给定一个无向图，输出所有割边（Critical Links）。

### 输入格式

输入包含多个测试用例。每个测试用例：
- 第一行：节点数 n
- 接下来 n 行：每行格式为 "node (k): ..."

### 输出格式

对于每个测试用例：
- 输出 "X critical links" 其中 X 是割边数量
- 按起点升序输出所有割边，每行格式 "a-b"

### 数据范围

- 节点编号：0 到 n-1

### 样例输入

```
3
0 (1): 1
1 (1): 0
2 (1): 1
8
0 (1): 1
1 (1): 0
2 (1): 3
3 (1): 2
4 (1): 5
5 (1): 4
6 (1): 7
7 (1): 6
```

### 样例输出

```
1 critical links
0-1
3 critical links
2-3
4-5
6-7
```

## 笔试面试考察点分析

1. **割边基础概念**：无向图中删除后使图不连通的边
2. **输入格式处理**：特殊格式需要解析
3. **Tarjan算法应用**：求割边
4. **排序输出**：按起点排序

## 解题思路

### 步骤1：解析输入

题目输入格式特殊，需要解析：
- 格式："node (k): neighbor1 neighbor2 ..."
- 需要提取节点编号和邻居

### 步骤2：Tarjan求割边

使用标准Tarjan算法求割边。

### 步骤3：排序输出

按割边的较小端点排序后输出。

## 完整代码实现（C++）

```cpp
#include <bits/stdc++.h>
using namespace std;

// ==================== UVA796 Critical Links ====================
// 适用场景：求无向图所有割边
// 考察点：割边模板、输入解析、排序输出
// 时间复杂度：O(n + m)
// 空间复杂度：O(n + m)

// ==================== 常量定义 ====================
const int MAXN = 10005;      // 最大节点数
const int MAXM = 100005;     // 最大边数

// ==================== 边的结构体定义 ====================
struct Edge {
    int to;     // 边的终点
    int id;     // 边的编号
    int rev;    // 反向边索引
    Edge(int _to, int _id, int _rev) : to(_to), id(_id), rev(_rev) {}
};

// ==================== 全局变量声明 ====================
vector<Edge> adj[MAXN];

int dfn[MAXN];
int low[MAXN];
int timestamp = 0;

bool is_bridge[MAXM];

vector<pair<int, int>> bridges;

// ==================== Tarjan算法核心函数 ====================
void tarjan(int u, int edge_id) {
    // 初始化时间戳
    dfn[u] = low[u] = ++timestamp;
    
    // 遍历所有邻接边
    for (auto &e : adj[u]) {
        int v = e.to;
        int id = e.id;
        
        if (!dfn[v]) {
            tarjan(v, id);
            low[u] = min(low[u], low[v]);
            
            // 割边判定：low[v] > dfn[u]
            if (low[v] > dfn[u]) {
                is_bridge[id] = true;
                int a = min(u, v);
                int b = max(u, v);
                bridges.emplace_back(a, b);
            }
        }
        // 排除反向边
        else if (id != edge_id) {
            low[u] = min(low[u], dfn[v]);
        }
    }
}

// ==================== 输入解析函数 ====================
// 解析格式：node (k): neighbor1 neighbor2 ...
// 面试问题：如何处理这种特殊输入格式？
// 答：逐字符读取，提取数字，处理括号和冒号
void parse_input(int n) {
    // 清除上一组数据
    for (int i = 0; i < MAXN; i++) {
        adj[i].clear();
    }
    memset(dfn, 0, sizeof(dfn));
    memset(is_bridge, false, sizeof(is_bridge));
    bridges.clear();
    timestamp = 0;
    
    // 读取并解析每一行
    for (int i = 0; i < n; i++) {
        string line;
        getline(cin, line);
        
        // 解析节点编号
        int node = 0, idx = 0;
        while (idx < line.size() && line[idx] >= '0' && line[idx] <= '9') {
            node = node * 10 + (line[idx] - '0');
            idx++;
        }
        
        // 查找邻居列表开始位置（冒号后）
        size_t colon_pos = line.find(':');
        if (colon_pos == string::npos) continue;
        
        // 提取邻居
        string neighbors = line.substr(colon_pos + 1);
        stringstream ss(neighbors);
        int neighbor;
        int edge_cnt = bridges.size();
        
        while (ss >> neighbor) {
            // 跳过非数字字符
            if (neighbor < 0 || neighbor >= n) continue;
            
            // 检查是否已添加（避免重复）
            bool found = false;
            for (auto &p : bridges) {
                if ((min(node, neighbor) == p.first && max(node, neighbor) == p.second)) {
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                // 添加边
                int edge_id = bridges.size() + 1;
                adj[node].push_back(Edge(neighbor, edge_id, adj[neighbor].size()));
                adj[neighbor].push_back(Edge(node, edge_id, adj[node].size() - 1));
            }
        }
    }
}

// ==================== 主函数 ====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(NULL);
    
    int n;
    while (cin >> n) {
        // 清除缓存
        string line;
        getline(cin, line);
        
        // 清除数据
        for (int i = 0; i < MAXN; i++) {
            adj[i].clear();
        }
        memset(dfn, 0, sizeof(dfn));
        memset(is_bridge, false, sizeof(is_bridge));
        bridges.clear();
        timestamp = 0;
        
        // 读取所有边
        for (int i = 0; i < n; i++) {
            getline(cin, line);
            
            // 解析当前行
            // 格式：node (k): neighbor1 neighbor2 ...
            int node = 0, pos = 0;
            while (pos < line.size() && isdigit(line[pos])) {
                node = node * 10 + (line[pos] - '0');
                pos++;
            }
            
            // 查找冒号位置
            size_t colon = line.find(':');
            if (colon == string::npos) continue;
            
            // 提取邻居列表
            string rest = line.substr(colon + 1);
            stringstream ss(rest);
            int nb;
            while (ss >> nb) {
                // 检查是否已添加（处理重边）
                bool added = false;
                for (auto &b : bridges) {
                    if ((min(node, nb) == b.first && max(node, nb) == b.second)) {
                        added = true;
                        break;
                    }
                }
                
                if (!added) {
                    int id = bridges.size() + 1;
                    adj[node].push_back(Edge(nb, id, adj[nb].size()));
                    adj[nb].push_back(Edge(node, id, adj[node].size() - 1));
                }
            }
        }
        
        // Tarjan求割边
        for (int i = 0; i < n; i++) {
            if (!dfn[i]) {
                tarjan(i, 0);
            }
        }
        
        // 排序输出
        sort(bridges.begin(), bridges.end());
        
        cout << bridges.size() << " critical links\n";
        for (auto &b : bridges) {
            cout << b.first << "-" << b.second << "\n";
        }
        cout << "\n";
    }
    
    return 0;
}
```

## 代码逐行注释

### 输入解析要点

1. **格式理解**：`node (k): neighbor1 neighbor2 ...`
2. **字符处理**：提取数字，处理括号和冒号
3. **去重**：可能存在重边，需要检查

### Tarjan算法

与标准模板相同：
- dfn和low数组
- 割边判定：`low[v] > dfn[u]`

## 时间空间复杂度分析

- **时间复杂度**：O(n + m)
- **空间复杂度**：O(n + m)

## 同类题目拓展

| 平台 | 题目 | 难度 |
|------|------|------|
| 洛谷 | P1656 割边 | 入门 |
| 洛谷 | P8436 边双连通分量 | 入门 |
| POJ | POJ3352 Road Construction | 中等 |

## 机器学习/深度学习关联

### 割边应用

1. **关键连接识别**
2. **图结构分析**
3. **异常检测**
