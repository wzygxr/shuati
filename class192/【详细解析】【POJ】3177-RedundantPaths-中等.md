# 【POJ】3177 - Redundant Paths（冗余路径）

## 一、题目信息

### 1.1 原始链接
- POJ：https://poj.org/problem?id=3177
- 洛谷对应：P2860

### 1.2 题目描述

给定一张连通的无向图，最少需要添加多少条边，使得任意两点之间都有两条边不重复的路径。

**输入格式**：
- n, m
- m 行：u, v

**输出格式**：
- 最少需要添加的边数

---

## 二、考察点分析

| 考点 | 说明 |
|------|------|
| 边双连通分量 | Tarjan求e-DCC |
| 缩点 | 将图转化为树 |
| 叶子统计 | 度为1的节点数 |
| 公式 | (leaf + 1) / 2 |

---

## 三、解题思路

### 算法流程

```
1. Tarjan求割边
2. 边双缩点，得到一棵树
3. 统计叶子节点数 leaf答案 = (leaf + 1)
4.  / 2
```

### 核心代码

```cpp
// Tarjan求割边
void tarjan(int u, int parentEdge) {
    dfn[u] = low[u] = ++timestamp;
    for (int e = head[u]; e != -1; e = edges[e].next) {
        int v = edges[e].to;
        int id = edges[e].id;
        if (id == parentEdge) continue;
        if (!dfn[v]) {
            tarjan(v, id);
            low[u] = min(low[u], low[v]);
            if (low[v] > dfn[u]) {
                is_bridge[id] = true;
            }
        } else {
            low[u] = min(low[u], dfn[v]);
        }
    }
}

// 边双缩点
void dfs_belong(int u) {
    belong[u] = ebccCnt;
    for (int e = head[u]; e != -1; e = edges[e].next) {
        int v = edges[e].to;
        int id = edges[e].id;
        if (!belong[v] && !is_bridge[id]) {
            dfs_belong(v);
        }
    }
}

// 统计叶子
int leaf = 0;
for (int i = 1; i <= ebccCnt; i++) {
    if (degree[i] == 1) leaf++;
}

int ans = (leaf + 1) / 2;
```

---

## 四、复杂度分析

| 复杂度 | 值 |
|--------|-----|
| 时间 | O(n+m) |
| 空间 | O(n+m) |

---

## 五、面试要点

### 核心公式推导

```
每次加一条边，可以将两个叶子节点连接
连接后，这两个叶子及路径上的节点都变成非叶子
所以需要 (leaf + 1) / 2 条边
```

### 面试话术

```
"1. 用Tarjan求割边
2. 将边双连通分量缩点，得到一棵树
3. 统计度为1的叶子节点数leaf
4. 答案就是 (leaf + 1) / 2"
```
