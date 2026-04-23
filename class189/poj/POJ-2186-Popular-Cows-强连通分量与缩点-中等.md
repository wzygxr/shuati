# POJ 2186 - Popular Cows

## 题目链接
http://poj.org/problem?id=2186

## 题目描述

每头牛都有一个梦想：成为一头明星牛，被所有其他牛喜欢。给定 $N$ 头牛和 $M$ 个喜欢关系（$A \rightarrow B$ 表示 $A$ 喜欢 $B$），喜欢关系具有传递性。求被所有牛都喜欢的牛的数量。

### 输入格式

第一行两个整数 $N$ 和 $M$。

接下来 $M$ 行，每行两个整数 $A$ 和 $B$，表示 $A$ 喜欢 $B$。

### 输出格式

一行一个整数，表示被所有牛都喜欢的牛的数量。

### 输入输出样例

**输入**
```
3 3
1 2
2 1
2 3
```

**输出**
```
1
```

### 数据范围
- $1 \le N \le 10,000$
- $1 \le M \le 50,000$

---

## 笔试/面试考察点分析

### 核心考察点
1. **强连通分量（SCC）**：理解SCC的定义和性质
2. **缩点（Condensation）**：将SCC缩为单个节点
3. **出度分析**：明星牛所在SCC出度必须为0

### 与洛谷P2341的关系
```
本题是洛谷P2341【受欢迎的牛】的原题（英文版）
解题思路和代码完全相同
```

### 面试口述要点
```
"这道题需要找出被所有节点都能到达的节点。首先用Tarjan算法
 求出所有强连通分量，然后将每个SCC缩成一个点。缩点后形成DAG，
 只有出度为0的SCC才可能被所有节点到达。如果存在多个出度为0的SCC，
 它们互相不可达，就没有明星牛。"
```

---

## 解题思路

### 步骤1：求强连通分量
使用Tarjan算法求出所有SCC。

### 步骤2：缩点
遍历所有边，若两端属于不同SCC，则统计出度。

### 步骤3：统计出度为0的SCC
- 数量为1：该SCC的大小即为答案
- 数量>1：答案为0

---

## 完整代码实现

### C++实现（POJ推荐）

```cpp
#include <iostream>
#include <vector>
#include <cstring>
#include <algorithm>
using namespace std;

const int MAXN = 10001;
const int MAXM = 50001;

int N, M;
int A[MAXM], B[MAXM]; // 存储边

// 链式前向星
int head[MAXN], nxt[MAXM], to[MAXM], cntg;

// Tarjan
int dfn[MAXN], low[MAXN], cntd;
int sta[MAXN], top;
int belong[MAXN], sccSize[MAXN], sccCnt;

// 缩点
int outDegree[MAXN];

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
            sccSize[sccCnt]++;
        } while (pop != u);
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    cin >> N >> M;
    for (int i = 1; i <= M; i++) {
        cin >> A[i] >> B[i];
        addEdge(A[i], B[i]);
    }
    
    for (int i = 1; i <= N; i++) {
        if (!dfn[i]) tarjan(i);
    }
    
    // 缩点统计出度
    for (int i = 1; i <= M; i++) {
        int scc1 = belong[A[i]];
        int scc2 = belong[B[i]];
        if (scc1 != scc2) {
            outDegree[scc1]++;
        }
    }
    
    // 统计出度为0的SCC
    int cnt = 0, ans = 0;
    for (int i = 1; i <= sccCnt; i++) {
        if (outDegree[i] == 0) {
            cnt++;
            ans = sccSize[i];
        }
    }
    
    if (cnt > 1) ans = 0;
    
    cout << ans << endl;
    return 0;
}
```

### Java实现

```java
import java.util.*;
import java.io.*;

public class Main {
    static int MAXN = 10001;
    static int MAXM = 50001;
    
    static int N, M;
    static int[] A = new int[MAXM];
    static int[] B = new int[MAXM];
    
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
    static int[] sccSize = new int[MAXN];
    static int sccCnt;
    
    // 缩点
    static int[] outDegree = new int[MAXN];
    
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
                sccSize[sccCnt]++;
            } while (pop != u);
        }
    }
    
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        
        for (int i = 1; i <= M; i++) {
            st = new StringTokenizer(br.readLine());
            A[i] = Integer.parseInt(st.nextToken());
            B[i] = Integer.parseInt(st.nextToken());
            addEdge(A[i], B[i]);
        }
        
        for (int i = 1; i <= N; i++) {
            if (dfn[i] == 0) tarjan(i);
        }
        
        // 缩点
        for (int i = 1; i <= M; i++) {
            int scc1 = belong[A[i]];
            int scc2 = belong[B[i]];
            if (scc1 != scc2) {
                outDegree[scc1]++;
            }
        }
        
        int cnt = 0, ans = 0;
        for (int i = 1; i <= sccCnt; i++) {
            if (outDegree[i] == 0) {
                cnt++;
                ans = sccSize[i];
            }
        }
        
        if (cnt > 1) ans = 0;
        
        System.out.println(ans);
    }
}
```

---

## 时间/空间复杂度分析

### 时间复杂度
- **Tarjan算法**：$O(N + M)$
- **缩点**：$O(M)$
- **总时间复杂度**：$O(N + M)$

### 空间复杂度
- **图存储**：$O(N + M)$
- **Tarjan数组**：$O(N)$
- **总空间复杂度**：$O(N + M)$

---

## 同类题目拓展

### 相同思路题目
| 题号 | 题目名称 | 平台 | 说明 |
|------|----------|------|------|
| P2341 | 受欢迎的牛 | 洛谷 | 本题中文版 |
| 1174 | 受欢迎的牛 | AcWing | 同P2341 |
| 2553 | The Bottom of a Graph | POJ | 类似思路 |

---

## ML/DL关联思考

### 1. 社交网络影响力分析
本题的经典场景是**社交网络中的影响力分析**：

```python
def find_influencers(social_network):
    """
    在社交网络中找出最有影响力的用户群体
    
    模型假设：
    - 边A->B表示A关注B（A喜欢B的内容）
    - 影响力可传递：A关注B，B关注C，则A也受C影响
    - "明星用户"是被所有用户关注（或间接关注）的用户
    """
    # 1. 求SCC（紧密的社交圈子）
    sccs = tarjan_scc(social_network)
    
    # 2. 缩点
    condensed_graph = condense(social_network, sccs)
    
    # 3. 找出度为0的SCC（被所有人关注的圈子）
    influencer_groups = []
    for scc_id, scc in enumerate(sccs):
        if condensed_graph.out_degree(scc_id) == 0:
            influencer_groups.append(scc)
    
    return influencer_groups
```

**实际应用：**
- **微博大V发现**：找出被最多人关注的用户群体
- **意见领袖识别**：识别能够影响最多人的关键群体
- **营销目标筛选**：优先向明星用户群体投放广告

---

## 总结

本题是**强连通分量缩点思想的经典入门题**，核心掌握点：

1. **SCC缩点**：将复杂图简化为DAG
2. **出度分析**：出度为0的SCC是候选明星
3. **唯一性判断**：多个出度为0的SCC意味着无解

**笔试建议**：直接背诵模板代码，注意数组大小。
**面试建议**：清晰解释缩点思想和出度分析逻辑。
