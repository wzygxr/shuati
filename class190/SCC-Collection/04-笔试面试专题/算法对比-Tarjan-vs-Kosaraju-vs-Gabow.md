# 三种强连通分量算法对比详解

## 概述

求有向图强连通分量(SCC)有三种经典算法：
1. **Tarjan算法**（1972）
2. **Kosaraju算法**（1978）
3. **Gabow算法**（2000）

本文详细对比三种算法的核心思想、复杂度、优缺点和适用场景。

---

## 一、Tarjan算法

### 核心思想
- 一次DFS遍历完成SCC识别
- 利用`dfn`（时间戳）和`low`（最早回溯时间）数组
- 用栈维护当前DFS路径

### 算法流程
```
1. 初始化dfn[u] = low[u] = ++timestamp
2. u入栈，标记in_stack[u] = true
3. 遍历u的所有邻接点v：
   - 如果v未访问：递归访问v，回溯后low[u] = min(low[u], low[v])
   - 如果v在栈中：low[u] = min(low[u], dfn[v])
4. 如果dfn[u] == low[u]，u是SCC根，出栈标记整个SCC
```

### 代码实现
```cpp
void tarjan(int u) {
    dfn[u] = low[u] = ++timestamp;
    st.push(u);
    in_stack[u] = true;
    
    for (int v : adj[u]) {
        if (!dfn[v]) {
            tarjan(v);
            low[u] = min(low[u], low[v]);
        } else if (in_stack[v]) {
            low[u] = min(low[u], dfn[v]);
        }
    }
    
    if (dfn[u] == low[u]) {
        scc_cnt++;
        int v;
        do {
            v = st.top(); st.pop();
            in_stack[v] = false;
            scc_id[v] = scc_cnt;
        } while (v != u);
    }
}
```

### 复杂度
- **时间**：$O(V + E)$
- **空间**：$O(V)$

### 优缺点
| 优点 | 缺点 |
|------|------|
| 只需一次DFS | 递归可能栈溢出 |
| 代码简洁 | 需要维护栈 |
| 常数较小 | 理解难度稍大 |

---

## 二、Kosaraju算法

### 核心思想
- **两次DFS**：第一次在原始图，第二次在反向图
- 第一次DFS记录完成时间顺序
- 第二次DFS按完成时间逆序在反向图上遍历

### 算法流程
```
1. 在原始图上DFS，记录节点完成顺序（后序）
2. 构建反向图
3. 按完成时间逆序在反向图上DFS
4. 每次DFS访问到的节点构成一个SCC
```

### 为什么正确？
- 在反向图中，SCC仍然强连通
- 按完成时间逆序 ensures 先访问"汇点"SCC
- 反向图中从"汇点"SCC无法到达其他SCC

### 代码实现
```cpp
// 第一次DFS：记录完成顺序
void dfs1(int u) {
    vis[u] = true;
    for (int v : adj[u]) {
        if (!vis[v]) dfs1(v);
    }
    order.push_back(u);  // 后序加入
}

// 第二次DFS：在反向图上找SCC
void dfs2(int u) {
    scc_id[u] = scc_cnt;
    for (int v : radj[u]) {  // 反向图
        if (!scc_id[v]) dfs2(v);
    }
}

void kosaraju(int n) {
    // 第一次DFS
    for (int i = 1; i <= n; i++) {
        if (!vis[i]) dfs1(i);
    }
    
    // 第二次DFS
    reverse(order.begin(), order.end());
    for (int u : order) {
        if (!scc_id[u]) {
            scc_cnt++;
            dfs2(u);
        }
    }
}
```

### 复杂度
- **时间**：$O(V + E)$
- **空间**：$O(V + E)$（需要反向图）

### 优缺点
| 优点 | 缺点 |
|------|------|
| 逻辑清晰易理解 | 需要建反向图 |
| 无需维护栈 | 需要两次遍历 |
| 容易改成非递归 | 空间开销稍大 |

---

## 三、Gabow算法

### 核心思想
- 类似Tarjan，但用**两个栈**代替dfn/low数组
- `stack1`：当前DFS路径
- `stack2`：可能的SCC根节点

### 算法流程
```
1. u入stack1和stack2
2. 遍历u的邻接点v：
   - 如果v未访问：递归访问v
   - 如果v在stack1中：从stack2弹出直到v的dfn <= stack2顶的dfn
3. 如果stack2顶 == u，弹出stack1直到u，构成SCC
```

### 代码实现
```cpp
void gabow(int u) {
    dfn[u] = ++timestamp;
    st1.push(u);
    st2.push(u);
    
    for (int v : adj[u]) {
        if (!dfn[v]) {
            gabow(v);
        } else if (!scc_id[v]) {
            // v在stack1中但未分配SCC
            while (dfn[st2.top()] > dfn[v]) {
                st2.pop();
            }
        }
    }
    
    if (st2.top() == u) {
        st2.pop();
        scc_cnt++;
        int v;
        do {
            v = st1.top(); st1.pop();
            scc_id[v] = scc_cnt;
        } while (v != u);
    }
}
```

### 复杂度
- **时间**：$O(V + E)$
- **空间**：$O(V)$

### 优缺点
| 优点 | 缺点 |
|------|------|
| 无需low数组 | 使用两个栈 |
| 理论效率高 | 代码稍复杂 |
| 容易并行化 | 实际不常用 |

---

## 四、算法对比总结

| 特性 | Tarjan | Kosaraju | Gabow |
|------|--------|----------|-------|
| **遍历次数** | 1次 | 2次 | 1次 |
| **额外空间** | O(V)栈 | O(V+E)反向图 | O(V)两个栈 |
| **代码难度** | 中等 | 简单 | 较难 |
| **常数因子** | 小 | 中等 | 小 |
| **适用场景** | 竞赛/面试 | 教学/理解 | 理论研究 |
| **推荐度** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐ |

---

## 五、面试选择建议

### 笔试/竞赛
- **首选Tarjan**：代码短，常数小，一次DFS完成

### 面试口述
- **推荐Kosaraju**：逻辑清晰，容易解释，两次DFS的概念直观

### 特殊场景
- **内存受限**：Tarjan或Gabow（空间O(V)）
- **需要反向图的其他操作**：Kosaraju（顺便建了反向图）

---

## 六、常见面试问题

**Q1: 三种算法的核心区别是什么？**
> Tarjan用dfn/low数组+栈一次完成；Kosaraju两次DFS+反向图；Gabow用两个栈避免low数组计算。

**Q2: 为什么Kosaraju第二次DFS要在反向图上？**
> 反向图中SCC结构不变，但边的方向相反。按完成时间逆序 ensures 先访问汇点SCC，这样每次DFS不会跨SCC。

**Q3: Tarjan的low数组更新为什么有时用dfn有时用low？**
> 对于未访问的子节点用low[v]（子树信息），对于已访问且在栈中的节点用dfn[v]（直接信息）。

**Q4: 如果图有10^6个节点，选哪个算法？**
> 选Tarjan的非递归版本或Gabow，避免递归栈溢出。同时考虑用链式前向星存图节省空间。
