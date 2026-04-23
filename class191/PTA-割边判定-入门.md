# PTA-割边判定-入门

> 适用范围：程序设计课程练习、天梯赛、算法基础训练

---

## 1. 题目原始信息

### 1.1 题目链接

- **PTA**: https://pintia.cn/
- **天梯赛**: 搜索"割边"相关题目

### 1.2 题目描述

给定一个无向图，判断指定的某条边是否为割边。

**输入格式**：
```
n m q
u1 v1
u2 v2
...
um vm
e1 e2
...
eq1 eq2
```
- n: 节点数
- m: 边数
- q: 查询数
- 接下来m行：边的端点
- 接下来q行：每行两个整数，表示要查询的边

**输出格式**：
```
Yes
No
```
- 如果该边是割边，输出"Yes"，否则输出"No"

### 1.3 样例输入输出

**示例 1**：
```
输入：
4 3 2
1 2
2 3
3 4
1 2
3 4

输出：
No
Yes
```

---

## 2. 笔试面试考察点分析

### 2.1 核心考察点

| 考察维度 | 具体内容 | 难度 |
|---------|---------|------|
| 割边判定 | Tarjan算法 | ★★☆ |
| 单条边查询 | 快速判断某条边是否为割边 | ★☆☆ |

### 2.2 面试高频提问

1. **什么是割边？**
2. **Tarjan算法求割边的核心逻辑是什么？**

---

## 3. 解题思路

### 3.1 算法选择

使用 **Tarjan算法** 预处理所有割边，然后 O(1) 查询每条边。

### 3.2 核心原理

1. **dfn数组**：记录每个节点的发现时间
2. **low数组**：记录每个节点能回溯到的最小dfn值
3. **割边判定**：`low[v] > dfn[u]`

---

## 4. 完整代码实现

### 4.1 C++实现

```cpp
#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
using namespace std;

/**
 * PTA - 割边判定
 * 
 * 解题思路：使用Tarjan算法求割边，然后O(1)查询
 * 时间复杂度：O(n + m) 预处理 + O(q) 查询
 * 空间复杂度：O(n + m)
 */

// ==================== 常量定义 ====================
const int MAXN = 1005;
const int MAXM = 2005;

// ==================== 边结构体 ====================
struct Edge {
    int to;
    int id;
    int rev;
    Edge(int _to, int _id, int _rev) : to(_to), id(_id), rev(_rev) {}
};

// ==================== 全局变量 ====================
vector<Edge> adj[MAXN];
int dfn[MAXN], low[MAXN], timestamp = 0;
bool is_bridge[MAXM];

// ==================== Tarjan算法 ====================
void tarjan(int u, int edge_id) {
    dfn[u] = low[u] = ++timestamp;
    
    for (auto &e : adj[u]) {
        int v = e.to;
        int id = e.id;
        
        if (!dfn[v]) {
            tarjan(v, id);
            low[u] = min(low[u], low[v]);
            
            // 割边判定定理
            if (low[v] > dfn[u]) {
                is_bridge[id] = true;
            }
        } else if (id != edge_id) {
            low[u] = min(low[u], dfn[v]);
        }
    }
}

// ==================== 主函数 ====================
int main() {
    memset(dfn, 0, sizeof(dfn));
    memset(is_bridge, false, sizeof(is_bridge));
    
    int n, m, q;
    cin >> n >> m >> q;
    
    // 构建无向图
    for (int i = 1; i <= m; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(Edge(v, i, adj[v].size()));
        adj[v].push_back(Edge(u, i, adj[u].size() - 1));
    }
    
    // Tarjan预处理
    for (int i = 1; i <= n; i++) {
        if (!dfn[i]) {
            tarjan(i, 0);
        }
    }
    
    // 查询
    for (int i = 0; i < q; i++) {
        int u, v;
        cin >> u >> v;
        // 通过边编号判断（假设u<v）
        // PTA题目中边的编号通常按输入顺序
        // 需要找到对应边的编号
        // 这里简化：遍历u的邻接边找到v对应的边编号
        int bid = -1;
        for (auto &e : adj[u]) {
            if (e.to == v) {
                bid = e.id;
                break;
            }
        }
        if (bid > 0 && is_bridge[bid]) {
            cout << "Yes" << endl;
        } else {
            cout << "No" << endl;
        }
    }
    
    return 0;
}
```

---

## 5. 时间空间复杂度分析

### 5.1 时间复杂度

| 步骤 | 复杂度 | 说明 |
|------|--------|------|
| 构建邻接表 | O(m) | 遍历所有边一次 |
| Tarjan预处理 | O(n + m) | 一次DFS |
| q次查询 | O(q) | O(1)每次 |
| **总计** | **O(n + m + q)** | 线性时间 |

### 5.2 空间复杂度

O(n + m)，线性空间。

---

## 附录：文件信息

- **难度**：入门
- **考察点**：割边判定、Tarjan算法
- **创建时间**：2026年2月
