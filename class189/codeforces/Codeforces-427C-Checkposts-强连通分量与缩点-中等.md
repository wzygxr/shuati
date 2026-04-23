# Codeforces 427C - Checkposts

## 题目链接
https://codeforces.com/problemset/problem/427/C

## 题目描述

Bersland有 $n$ 个城市，城市之间有 $m$ 条单向道路。每个城市需要建立一个检查站，建站成本为 $cost_i$。为了保证安全，需要满足：从任意城市出发，都能到达某个有检查站的城市。

求满足条件的最小总成本，以及达到最小成本的建站方案数（模 $10^9+7$）。

### 输入格式

第一行一个整数 $n$，表示城市数量。

第二行 $n$ 个整数 $cost_1, cost_2, ..., cost_n$，表示每个城市的建站成本。

第三行一个整数 $m$，表示道路数量。

接下来 $m$ 行，每行两个整数 $u, v$，表示从 $u$ 到 $v$ 有一条单向道路。

### 输出格式

输出两个整数：最小总成本和方案数（模 $10^9+7$）。

### 输入输出样例

**输入**
```
3
1 2 3
3
1 2
2 3
3 2
```

**输出**
```
3 1
```

**解释**：
- 城市2和城市3构成SCC，最小成本是2（选城市2）
- 城市1可以到达城市2，所以只需要在城市2建站
- 最小成本 = 2，方案数 = 1

### 数据范围
- $1 \le n \le 10^5$
- $0 \le m \le 10^5$
- $1 \le cost_i \le 10^9$

---

## 笔试/面试考察点分析

### 核心考察点
1. **强连通分量缩点**：SCC内的城市可以互相到达，只需选一个建站
2. **DAG入度分析**：入度为0的SCC必须从内部选城市建站
3. **组合计数**：每个必选SCC内部选择成本最小的城市，方案数相乘

### 解题关键 insight
```
【核心观察】
1. 缩点后形成DAG
2. 入度为0的SCC：无法从其他SCC到达，必须在内部建站
3. 入度>0的SCC：可以从其他SCC到达，不需要自己建站
4. 在每个入度为0的SCC中，选择成本最小的城市建站
5. 方案数 = 每个入度为0的SCC中，成本最小城市数量的乘积
```

### 面试口述要点
```
"这道题需要找最小成本的检查站布置方案。首先缩点，
 入度为0的强连通分量必须从内部选城市建站。
 对每个入度为0的SCC，选择成本最小的城市。
 最小成本就是这些最小成本之和，
 方案数是每个SCC中最小成本城市数量的乘积。"
```

---

## 解题思路

### 步骤1：求强连通分量
使用Tarjan算法求出所有SCC。

### 步骤2：计算每个SCC的最小成本及计数
对每个SCC：
- 找到最小成本 `minCost`
- 统计最小成本城市的数量 `count`

### 步骤3：缩点统计入度
遍历所有边，若两端属于不同SCC，统计入度。

### 步骤4：计算答案
- 入度为0的SCC必须建站
- 答案成本 = 这些SCC的 `minCost` 之和
- 答案方案数 = 这些SCC的 `count` 之积（模 $10^9+7$）

---

## 完整代码实现

### C++实现

```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 100001;
const int MAXM = 100001;
const int MOD = 1e9 + 7;

typedef long long ll;

int n, m;
ll cost[MAXN];
int U[MAXM], V[MAXM];

// 链式前向星
int head[MAXN], nxt[MAXM], to[MAXM], cntg;

// Tarjan
int dfn[MAXN], low[MAXN], cntd;
int sta[MAXN], top;
int belong[MAXN], sccCnt;

// SCC信息
ll sccMinCost[MAXN];  // 每个SCC的最小成本
int sccMinCount[MAXN]; // 每个SCC最小成本的数量
int inDegree[MAXN];    // 缩点后入度

void addEdge(int u, int v) {
    nxt[++cntg] = head[u];
    to[cntg] = v;
    head[u] = cntg;
}

void tarjan(int u) {
    dfn[u] = low[u] = ++cntd;
    sta[++top] = u;
    
    for (int e = head[u]; e; e = nxt[e]) {
        int v = to[e];
        if (!dfn[v]) {
            tarjan(v);
            low[u] = min(low[u], low[v]);
        } else if (!belong[v]) {
            low[u] = min(low[u], dfn[v]);
        }
    }
    
    if (dfn[u] == low[u]) {
        sccCnt++;
        int pop;
        do {
            pop = sta[top--];
            belong[pop] = sccCnt;
            
            // 更新SCC最小成本
            if (cost[pop] < sccMinCost[sccCnt]) {
                sccMinCost[sccCnt] = cost[pop];
                sccMinCount[sccCnt] = 1;
            } else if (cost[pop] == sccMinCost[sccCnt]) {
                sccMinCount[sccCnt]++;
            }
        } while (pop != u);
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    // 初始化SCC最小成本为无穷大
    memset(sccMinCost, 0x3f, sizeof(sccMinCost));
    
    cin >> n;
    for (int i = 1; i <= n; i++) {
        cin >> cost[i];
    }
    
    cin >> m;
    for (int i = 1; i <= m; i++) {
        cin >> U[i] >> V[i];
        addEdge(U[i], V[i]);
    }
    
    // Tarjan求SCC
    for (int i = 1; i <= n; i++) {
        if (!dfn[i]) tarjan(i);
    }
    
    // 缩点统计入度
    for (int i = 1; i <= m; i++) {
        int u = belong[U[i]];
        int v = belong[V[i]];
        if (u != v) {
            inDegree[v]++;
        }
    }
    
    // 计算答案
    ll totalCost = 0;
    ll ways = 1;
    
    for (int i = 1; i <= sccCnt; i++) {
        if (inDegree[i] == 0) {  // 入度为0的SCC必须建站
            totalCost += sccMinCost[i];
            ways = (ways * sccMinCount[i]) % MOD;
        }
    }
    
    cout << totalCost << " " << ways << "\n";
    return 0;
}
```

### Java实现

```java
import java.io.*;
import java.util.*;

public class Main {
    static int MAXN = 100001;
    static int MAXM = 100001;
    static int MOD = 1000000007;
    
    static int n, m;
    static long[] cost = new long[MAXN];
    static int[] U = new int[MAXM];
    static int[] V = new int[MAXM];
    
    // 链式前向星
    static int[] head = new int[MAXN];
    static int[] nxt = new int[MAXM];
    static int[] to = new int[MAXM];
    static int cntg;
    
    // Tarjan
    static int[] dfn = new int[MAXN];
    static int[] low = new int[MAXN];
    static int cntd;
    static int[] sta = new int[MAXN];
    static int top;
    static int[] belong = new int[MAXN];
    static int sccCnt;
    
    // SCC信息
    static long[] sccMinCost = new long[MAXN];
    static int[] sccMinCount = new int[MAXN];
    static int[] inDegree = new int[MAXN];
    
    static void addEdge(int u, int v) {
        nxt[++cntg] = head[u];
        to[cntg] = v;
        head[u] = cntg;
    }
    
    static void tarjan(int u) {
        dfn[u] = low[u] = ++cntd;
        sta[++top] = u;
        
        for (int e = head[u]; e > 0; e = nxt[e]) {
            int v = to[e];
            if (dfn[v] == 0) {
                tarjan(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (belong[v] == 0) {
                low[u] = Math.min(low[u], dfn[v]);
            }
        }
        
        if (dfn[u] == low[u]) {
            sccCnt++;
            int pop;
            do {
                pop = sta[top--];
                belong[pop] = sccCnt;
                
                // 更新SCC最小成本
                if (cost[pop] < sccMinCost[sccCnt]) {
                    sccMinCost[sccCnt] = cost[pop];
                    sccMinCount[sccCnt] = 1;
                } else if (cost[pop] == sccMinCost[sccCnt]) {
                    sccMinCount[sccCnt]++;
                }
            } while (pop != u);
        }
    }
    
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        
        // 初始化SCC最小成本为无穷大
        Arrays.fill(sccMinCost, Long.MAX_VALUE / 4);
        
        n = Integer.parseInt(br.readLine().trim());
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            cost[i] = Long.parseLong(st.nextToken());
        }
        
        m = Integer.parseInt(br.readLine().trim());
        for (int i = 1; i <= m; i++) {
            st = new StringTokenizer(br.readLine());
            U[i] = Integer.parseInt(st.nextToken());
            V[i] = Integer.parseInt(st.nextToken());
            addEdge(U[i], V[i]);
        }
        
        // Tarjan
        for (int i = 1; i <= n; i++) {
            if (dfn[i] == 0) tarjan(i);
        }
        
        // 缩点统计入度
        for (int i = 1; i <= m; i++) {
            int u = belong[U[i]];
            int v = belong[V[i]];
            if (u != v) {
                inDegree[v]++;
            }
        }
        
        // 计算答案
        long totalCost = 0;
        long ways = 1;
        
        for (int i = 1; i <= sccCnt; i++) {
            if (inDegree[i] == 0) {
                totalCost += sccMinCost[i];
                ways = (ways * sccMinCount[i]) % MOD;
            }
        }
        
        System.out.println(totalCost + " " + ways);
    }
}
```

---

## 时间/空间复杂度分析

### 时间复杂度
- **Tarjan算法**：$O(n + m)$
- **缩点统计入度**：$O(m)$
- **总时间复杂度**：$O(n + m)$

### 空间复杂度
- **图存储**：$O(n + m)$
- **Tarjan数组**：$O(n)$
- **总空间复杂度**：$O(n + m)$

---

## 同类题目拓展

### SCC+入度分析+计数
| 题号 | 题目名称 | 平台 | 说明 |
|------|----------|------|------|
| P2341 | 受欢迎的牛 | 洛谷 | 出度分析 |
| 427C | Checkposts | Codeforces | 入度分析+计数 |

---

## ML/DL关联思考

### 1. 最小覆盖集问题
本题是**最小覆盖集问题**的图论版本：

```python
def min_coverage_with_scc(graph, node_costs):
    """
    在带权图中寻找最小成本覆盖集
    
    应用场景：
    - 传感器网络部署：选择最少数量的传感器覆盖整个网络
    - 广告投放：选择关键用户进行推广，覆盖最大受众
    """
    # 1. 求SCC
    sccs = tarjan_scc(graph)
    condensed_graph, node_to_scc = condense(graph, sccs)
    
    # 2. 计算每个SCC的最小成本
    scc_min_costs = []
    scc_min_counts = []
    for scc in sccs:
        costs = [node_costs[node] for node in scc]
        min_cost = min(costs)
        min_count = costs.count(min_cost)
        scc_min_costs.append(min_cost)
        scc_min_counts.append(min_count)
    
    # 3. 找到入度为0的SCC（必须覆盖）
    in_degrees = [condensed_graph.in_degree(i) for i in range(len(sccs))]
    
    total_cost = sum(scc_min_costs[i] for i in range(len(sccs)) if in_degrees[i] == 0)
    total_ways = reduce(lambda x, y: x * y, 
                       [scc_min_counts[i] for i in range(len(sccs)) if in_degrees[i] == 0], 1)
    
    return total_cost, total_ways
```

**应用场景：**
- **传感器网络**：最小成本部署传感器覆盖所有区域
- **监控摄像头布局**：选择最佳位置覆盖所有通道
- **关键基础设施保护**：选择关键节点进行加固

---

## 总结

本题是**SCC缩点+入度分析+组合计数**的经典题，核心掌握点：

1. **缩点思想**：SCC内部互相可达，可作为一个整体
2. **入度分析**：入度为0的SCC必须从内部选
3. **组合计数**：方案数是各SCC最小成本城市数量的乘积
4. **取模运算**：方案数可能很大，注意及时取模

**笔试建议**：注意初始化SCC最小成本为无穷大，方案数取模。
**面试建议**：解释为什么要找入度为0的SCC，以及如何计算方案数。
