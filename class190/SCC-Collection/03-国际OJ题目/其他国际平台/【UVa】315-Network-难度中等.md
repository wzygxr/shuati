# 【UVa】315 - Network

## 题目信息

- **题目链接**: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=251
- **难度**: 中等
- **算法标签**: 割点、点双连通分量、Tarjan算法

## 题目描述

一个计算机网络由若干台计算机和连接它们的线路组成。每台计算机用一个正整数编号。

如果一台计算机故障（被移除），会导致其他计算机之间无法通信，那么这台计算机称为"关键计算机"。

给定一个网络，找出所有关键计算机的数量。

## 输入输出格式

### 输入格式
多组测试数据
每组数据：
- 第一行一个整数n，表示计算机数量（n=0表示结束）
- 接下来若干行，每行第一个数u，后面跟着与u相连的计算机编号
- 每行以0结束

### 输出格式
对于每组数据，输出关键计算机的数量

### 数据范围
- $1 \leq n \leq 100$

## 笔试/面试考察点分析

### 核心考察点
1. **割点判定**：判断无向图中的割点
2. **Tarjan算法变体**：low[v] >= dfn[u]条件
3. **输入格式处理**：UVa特色的输入格式

### 面试口述要点
- 关键计算机 = 割点（Articulation Point）
- 使用Tarjan算法求割点
- 对于非根节点u，如果存在子节点v满足low[v] >= dfn[u]，则u是割点
- 对于根节点，需要至少两个子树才是割点

## 解题思路

### 割点判定算法
1. DFS遍历图，计算dfn和low
2. 对于每个节点，检查割点条件
3. 统计割点数量

## 完整代码实现

```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 105;

vector<int> adj[MAXN];
int dfn[MAXN], low[MAXN], timestamp;
bool is_cut[MAXN];  // 标记是否为割点

/**
 * Tarjan算法求割点
 * 
 * @param u 当前节点
 * @param parent 父节点（-1表示根）
 * @param is_root 是否为根节点
 */
void tarjan(int u, int parent, bool is_root) {
    dfn[u] = low[u] = ++timestamp;
    int child_count = 0;  // 子树数量
    
    for (int v : adj[u]) {
        if (!dfn[v]) {
            // 树边
            child_count++;
            tarjan(v, u, false);
            low[u] = min(low[u], low[v]);
            
            // 割点判定
            if (!is_root && low[v] >= dfn[u]) {
                is_cut[u] = true;
            }
        } else if (v != parent) {
            // 回边
            low[u] = min(low[u], dfn[v]);
        }
    }
    
    // 根节点特判：至少两个子树才是割点
    if (is_root && child_count >= 2) {
        is_cut[u] = true;
    }
}

int main() {
    int n;
    
    while (cin >> n && n != 0) {
        // 清空图
        for (int i = 1; i <= n; i++) {
            adj[i].clear();
        }
        
        cin.ignore();  // 忽略换行符
        
        // 读入边（UVa特色输入格式）
        string line;
        while (getline(cin, line) && line != "0") {
            stringstream ss(line);
            int u, v;
            ss >> u;
            while (ss >> v && v != 0) {
                adj[u].push_back(v);
                adj[v].push_back(u);
            }
        }
        
        // 初始化
        memset(dfn, 0, sizeof(dfn));
        memset(low, 0, sizeof(low));
        memset(is_cut, false, sizeof(is_cut));
        timestamp = 0;
        
        // 求割点（图可能不连通）
        for (int i = 1; i <= n; i++) {
            if (!dfn[i]) {
                tarjan(i, -1, true);
            }
        }
        
        // 统计割点数量
        int cut_count = 0;
        for (int i = 1; i <= n; i++) {
            if (is_cut[i]) cut_count++;
        }
        
        cout << cut_count << endl;
    }
    
    return 0;
}
```

## 复杂度分析

| 指标 | 复杂度 | 说明 |
|------|--------|------|
| 时间 | $O(n + m)$ | Tarjan算法线性复杂度 |
| 空间 | $O(n + m)$ | 邻接表+数组 |

## ML/DL关联

### 网络鲁棒性分析
- **关键节点识别**：割点对应网络中的关键节点
- **网络攻击预测**：预测攻击哪些节点会导致网络瘫痪
- **强化学习**：学习保护关键节点的策略

## 笔试面试高频问题

**Q: 为什么根节点需要至少两个子树才是割点？**
> 如果根节点只有一个子树，删除根节点后，子树内部仍然连通（因为子树是连通的）。只有当根节点有两个或以上子树时，删除根节点才会使子树之间不连通。

**Q: 为什么非根节点用low[v] >= dfn[u]判断？**
> low[v] >= dfn[u]意味着v及其子树无法绕过u到达u的祖先。删除u后，v的子树就与图的其余部分断开了。

**Q: 如何求割边（桥）？**
> 类似割点，条件改为low[v] > dfn[u]（严格大于）。
