# 【洛谷】P2863 - [USACO06JAN] The Cow Prom

## 题目信息

- **题目链接**: https://www.luogu.com.cn/problem/P2863
- **难度**: 普及/提高-
- **算法标签**: 强连通分量、Tarjan算法

## 题目描述

奶牛们要去参加舞会。舞会中有N头奶牛，M对互相喜欢的关系。如果A喜欢B，那么A会邀请B参加舞会。如果一头奶牛被至少一头奶牛邀请，它就会参加舞会。

问题是：有多少个"受欢迎的牛群"，其中受欢迎的牛群定义为一群奶牛，牛群中的每头奶牛都被牛群内的其他奶牛邀请，且牛群外的奶牛都不邀请牛群内的奶牛。

## 输入输出格式

### 输入格式
第一行两个整数N,M
接下来M行，每行两个整数A,B，表示A喜欢B（A邀请B）

### 输出格式
输出受欢迎的牛群数量

### 数据范围
- $1 \leq N \leq 10000$
- $1 \leq M \leq 50000$

## 笔试/面试考察点分析

### 核心考察点
1. **SCC的直观理解**：题目描述的"牛群"就是强连通分量
2. **出度为0的SCC特性**：缩点后出度为0的SCC无法到达其他SCC
3. **唯一性判断**：需要判断是否存在唯一的出度为0的SCC

### 面试口述要点
- 首先将问题抽象为图论模型：奶牛为节点，喜欢关系为有向边
- "受欢迎的牛群" = 强连通分量（内部互相可达）
- 缩点后，出度为0的SCC是关键（无法邀请外部奶牛）
- 如果有多个出度为0的SCC，则没有满足条件的牛群

## 解题思路

### 步骤1：建图并求SCC
使用Tarjan算法求出所有强连通分量。

### 步骤2：缩点并统计出度
- 构建缩点后的DAG
- 统计每个SCC的出度

### 步骤3：判断结果
- 如果出度为0的SCC数量为1，则该SCC的大小为答案
- 否则答案为0

## 完整代码实现

```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 10005;      // 最大节点数
const int MAXM = 50005;      // 最大边数

// 原图邻接表
vector<int> adj[MAXN];

// Tarjan算法相关
int dfn[MAXN];               // 访问时间戳
int low[MAXN];               // 能回溯到的最早时间戳
int timestamp = 0;           // 时间戳计数器
stack<int> st;               // DFS栈
bool in_stack[MAXN];         // 是否在栈中标记

// SCC相关
int scc_id[MAXN];            // 节点所属SCC编号
int scc_size[MAXN];          // 每个SCC的大小
int scc_cnt = 0;             // SCC总数

// 缩点相关
vector<int> dag[MAXN];       // 缩点后的DAG
int out_deg[MAXN];           // 每个SCC的出度

/**
 * Tarjan算法DFS
 * 
 * 面试要点解释：
 * 1. dfn记录访问顺序，low记录能回溯到的最早节点
 * 2. 栈用于记录当前DFS路径上的节点
 * 3. 当dfn[u]==low[u]时，u是当前SCC的根
 */
void tarjan(int u) {
    // 初始化时间戳
    dfn[u] = low[u] = ++timestamp;
    
    // 入栈
    st.push(u);
    in_stack[u] = true;
    
    // 遍历所有邻接节点
    for (int v : adj[u]) {
        if (!dfn[v]) {
            // 未访问，递归DFS
            tarjan(v);
            // 回溯更新low值
            low[u] = min(low[u], low[v]);
        } else if (in_stack[v]) {
            // 已访问且在栈中，形成环
            low[u] = min(low[u], dfn[v]);
        }
    }
    
    // 找到SCC根节点
    if (dfn[u] == low[u]) {
        scc_cnt++;
        int v;
        do {
            v = st.top();
            st.pop();
            in_stack[v] = false;
            scc_id[v] = scc_cnt;
            scc_size[scc_cnt]++;
        } while (v != u);
    }
}

/**
 * 缩点并统计出度
 */
void shrink(int n) {
    for (int u = 1; u <= n; u++) {
        for (int v : adj[u]) {
            if (scc_id[u] != scc_id[v]) {
                dag[scc_id[u]].push_back(scc_id[v]);
                out_deg[scc_id[u]]++;   // 统计出度
            }
        }
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, m;
    cin >> n >> m;
    
    // 建图
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);
    }
    
    // 初始化
    memset(dfn, 0, sizeof(dfn));
    memset(low, 0, sizeof(low));
    memset(in_stack, false, sizeof(in_stack));
    memset(scc_size, 0, sizeof(scc_size));
    memset(out_deg, 0, sizeof(out_deg));
    
    // Tarjan求SCC
    for (int i = 1; i <= n; i++) {
        if (!dfn[i]) tarjan(i);
    }
    
    // 缩点
    shrink(n);
    
    // 统计出度为0的SCC
    int zero_out = 0;
    int ans_scc = 0;
    
    for (int i = 1; i <= scc_cnt; i++) {
        if (out_deg[i] == 0) {
            zero_out++;
            ans_scc = i;
        }
    }
    
    // 判断结果
    if (zero_out == 1) {
        cout << scc_size[ans_scc] << endl;
    } else {
        cout << 0 << endl;
    }
    
    return 0;
}
```

## 复杂度分析

| 指标 | 复杂度 | 说明 |
|------|--------|------|
| 时间 | $O(N + M)$ | Tarjan+缩点线性扫描 |
| 空间 | $O(N + M)$ | 邻接表+数组 |

## ML/DL关联

### 图聚类应用
- **SCC作为初始聚类**：在图聚类任务中，SCC可以作为强连接社区的初始划分
- **社区发现**：将SCC作为种子社区，进一步扩展发现更大的社区结构

### 社交网络分析
- **紧密群体识别**：类似"受欢迎牛群"的概念，在社交网络中寻找高度互动的群体
- **意见领袖发现**：出度为0的SCC代表无法被外部影响的封闭群体

## 笔试面试问题

**Q: 本题和P2341的区别是什么？**
> 本题关注的是"被邀请"关系，所以看的是出度为0的SCC；P2341关注的是"能到达所有点"，看的是入度为0的SCC。一个是"被谁喜欢"，一个是"能到达谁"。

**Q: 如果题目问"有多少头奶牛能被所有奶牛邀请"，怎么求？**
> 同样找缩点后入度为0的SCC（因为入度为0意味着不能被其他SCC到达），判断唯一性后输出大小。
