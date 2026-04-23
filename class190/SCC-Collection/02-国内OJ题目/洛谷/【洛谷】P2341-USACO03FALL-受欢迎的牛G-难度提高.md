# 洛谷 P2341 - 受欢迎的牛 G

## 题目链接
https://www.luogu.com.cn/problem/P2341

## 题目描述

每头奶牛都想成为全场最受欢迎的奶牛，所以它们经常互相拜访。

奶牛世界的社交关系可以用一个有向图表示，如果奶牛 A 喜欢奶牛 B，就会有一条从 A 指向 B 的有向边。

受欢迎的奶牛是指：被所有其他奶牛喜欢的奶牛，或者说，从任何其他奶牛出发，都能通过若干条有向边到达的奶牛。

现在给定 N 头奶牛和 M 个喜欢关系，请计算有多少头奶牛是受欢迎的。

## 输入格式

第一行包含两个整数 N 和 M，表示奶牛数量和喜欢关系数量。

接下来 M 行，每行两个整数 A 和 B，表示奶牛 A 喜欢奶牛 B（即存在边 A → B）。

## 输出格式

输出一个整数，表示受欢迎奶牛的数量。

## 数据范围

- 1 ≤ N ≤ 10,000
- 1 ≤ M ≤ 50,000
- 1 ≤ A, B ≤ N

## 样例输入
```
3 3
1 2
2 1
2 3
```

## 样例输出
```
1
```

## 样例解释

- 奶牛 1 和 2 互相喜欢，形成一个强连通分量
- 奶牛 2 喜欢奶牛 3
- 从奶牛 1 或 2 可以到达奶牛 3
- 但只有奶牛 3 能被所有奶牛到达（从1→2→3，从2→3）
- 所以只有 1 头奶牛受欢迎

## 笔试/面试考察点分析

### 核心考察点

1. **强连通分量缩点的经典应用**
   - 将互相喜欢的奶牛（强连通分量）缩成一个点
   - 缩点后形成 DAG，简化问题

2. **出度为零的强连通分量**
   - 在缩点后的 DAG 中，只有出度为 0 的 SCC 才能被所有点到达
   - 如果有多个出度为 0 的 SCC，则不存在受欢迎的奶牛

3. **Tarjan 算法的实际应用**
   - 求 SCC + 统计每个 SCC 的节点数

### 面试口述思路

```
"这道题的关键是观察性质：如果一头奶牛受欢迎，那么从任何奶牛出发都能到达它。

首先，我们考虑互相喜欢的奶牛群体——这就是一个强连通分量。
将每个强连通分量缩成一个点后，图变成了 DAG。

在 DAG 中，什么样的点能被所有点到达呢？
一定是出度为 0 的点（汇点）。
如果有多个出度为 0 的点，它们之间无法互相到达，所以不存在受欢迎的奶牛。

因此算法流程是：
1. Tarjan 求 SCC
2. 缩点统计每个 SCC 的出度
3. 如果出度为 0 的 SCC 只有一个，答案就是它的节点数
   否则答案为 0"
```

## 解题思路

### 算法流程

```
步骤1: 用 Tarjan 算法求所有强连通分量
        - 记录每个节点所属 SCC 的编号
        - 记录每个 SCC 包含的节点数

步骤2: 缩点，统计每个 SCC 的出度
        - 遍历原图的所有边 (u, v)
        - 如果 scc[u] ≠ scc[v]，则 scc[u] 的出度 +1

步骤3: 找出所有出度为 0 的 SCC
        - 如果数量为 1，输出该 SCC 的节点数
        - 如果数量 > 1，输出 0（不存在受欢迎的奶牛）
```

### 正确性证明

**引理1**：缩点后，只有出度为 0 的 SCC 可能被所有节点到达。

**证明**：若 SCC A 的出度 > 0，存在边 A→B，则从 B 无法到达 A（DAG 性质），所以 A 不能被所有节点到达。

**引理2**：如果缩点后只有一个出度为 0 的 SCC，则它能被所有节点到达。

**证明**：在有限 DAG 中，从任意节点出发沿着边走，由于无环，最终一定会到达一个出度为 0 的节点。如果只有一个这样的节点，则所有路径都终止于它，所以它被所有节点到达。

**定理**：算法正确计算受欢迎奶牛数量。

**证明**：
- 情况1：多个出度为 0 的 SCC。由引理1，每个都不能被所有节点到达，所以不存在受欢迎的奶牛，答案为 0。
- 情况2：一个出度为 0 的 SCC。由引理2，它能被所有节点到达，而 SCC 内部互相可达，所以该 SCC 中所有节点都受欢迎，答案为该 SCC 的节点数。

## 完整代码实现

### C++ 实现

```cpp
#include <iostream>
#include <vector>
#include <stack>
#include <cstring>
using namespace std;

// maxn: 最大节点数，根据题目 N ≤ 10000 设置，面试时需根据数据范围调整
const int MAXN = 10005;

// 邻接表存图：vector 动态数组存储每个节点的出边
// 使用 vector 方便处理不定数量的边，笔试时也可使用数组模拟邻接表优化常数
vector<int> adj[MAXN];

// Tarjan 算法核心数组
// dfn[i]: 节点 i 的 DFS 访问序号（时间戳），0 表示未访问
// low[i]: 节点 i 能回溯到的最小 dfn 值，用于判断强连通分量的根
int dfn[MAXN], low[MAXN];

// timestamp: 全局时间戳计数器，每次发现新节点时递增
// 用于给 dfn 数组赋值，区分不同节点的访问顺序
int timestamp = 0;

// 节点栈：存储当前 DFS 路径上的节点，用于提取强连通分量
// 栈中节点构成一条从根到当前节点的路径，当发现 SCC 根时出栈
stack<int> st;

// in_stack[i]: 标记节点 i 是否在栈中
// 用于判断节点是否在当前 DFS 路径上，区分横叉边和回边
bool in_stack[MAXN];

// scc_id[i]: 节点 i 所属的强连通分量编号，范围 [1, scc_cnt]
// 相同 scc_id 的节点属于同一个强连通分量，可以互相到达
int scc_id[MAXN];

// scc_cnt: 强连通分量的总数
// 每发现一个新的 SCC，该值递增
int scc_cnt = 0;

// scc_size[i]: 第 i 个强连通分量包含的节点数量
// 本题最终答案就是目标 SCC 的 scc_size
int scc_size[MAXN];

// scc_outdeg[i]: 第 i 个强连通分量的出度（缩点后的出度）
// 用于判断该 SCC 是否为汇点（出度为 0）
int scc_outdeg[MAXN];

/**
 * Tarjan 算法：求强连通分量
 * 核心思想：通过一次 DFS，利用 dfn 和 low 数组识别强连通分量
 * 
 * 时间复杂度：O(N + M)，每个节点和边只访问一次
 * 空间复杂度：O(N)，栈和数组的空间
 * 
 * @param u: 当前访问的节点
 */
void tarjan(int u) {
    // 步骤1：初始化当前节点的 dfn 和 low 值
    // 发现新节点时，dfn 和 low 都等于当前时间戳
    // 表示当前节点只能到达自己
    dfn[u] = low[u] = ++timestamp;
    
    // 步骤2：将当前节点压入栈
    // 栈中保存从根到当前节点的路径，用于后续提取 SCC
    st.push(u);
    in_stack[u] = true;  // 标记节点在栈中
    
    // 步骤3：遍历 u 的所有出边
    // 递归访问邻接节点，更新 low 值
    for (int v : adj[u]) {
        if (dfn[v] == 0) {
            // 情况1：v 未访问过，递归访问
            // 递归完成后，用 v 的 low 值更新 u 的 low 值
            // 因为 u 能通过 v 到达的节点，u 也能到达
            tarjan(v);
            low[u] = min(low[u], low[v]);
        } else if (in_stack[v]) {
            // 情况2：v 已访问且在栈中，说明 v 在当前 DFS 路径上
            // 这是回边，构成环，用 v 的 dfn 更新 u 的 low
            // 注意：这里用 dfn[v] 而不是 low[v]，因为 v 在栈中，dfn[v] 就是它能到达的最小值
            low[u] = min(low[u], dfn[v]);
        }
        // 情况3：v 已访问但不在栈中，说明 v 属于其他 SCC
        // 这是横叉边，忽略，因为 v 所在 SCC 已经处理完毕
    }
    
    // 步骤4：判断 u 是否为强连通分量的根
    // 当 dfn[u] == low[u] 时，u 是 SCC 的根
    // 这意味着 u 无法通过任何路径回到之前的节点
    if (dfn[u] == low[u]) {
        scc_cnt++;  // 发现新的 SCC，计数器加1
        int v;
        // 从栈中弹出节点，直到 u 被弹出
        // 这些节点构成一个强连通分量
        do {
            v = st.top();      // 获取栈顶元素
            st.pop();          // 弹出栈顶
            in_stack[v] = false;  // 标记不在栈中
            scc_id[v] = scc_cnt;  // 记录所属 SCC 编号
            scc_size[scc_cnt]++;  // 该 SCC 节点数加1
        } while (v != u);  // 直到 u 被弹出，SCC 提取完成
    }
}

int main() {
    // 步骤1：读入数据
    int n, m;  // n: 奶牛数量，m: 喜欢关系数量
    cin >> n >> m;
    
    // 步骤2：建图
    // 读入 m 条有向边，构建邻接表
    for (int i = 0; i < m; i++) {
        int a, b;  // a 喜欢 b，即边 a → b
        cin >> a >> b;
        adj[a].push_back(b);  // 添加有向边
    }
    
    // 步骤3：初始化数组
    // 将数组清零，准备 Tarjan 算法
    memset(dfn, 0, sizeof(dfn));
    memset(low, 0, sizeof(low));
    memset(in_stack, false, sizeof(in_stack));
    memset(scc_id, 0, sizeof(scc_id));
    memset(scc_size, 0, sizeof(scc_size));
    memset(scc_outdeg, 0, sizeof(scc_outdeg));
    
    // 步骤4：对所有未访问节点运行 Tarjan 算法
    // 图可能不连通，需要遍历所有节点
    for (int i = 1; i <= n; i++) {
        if (dfn[i] == 0) {  // 节点 i 未访问
            tarjan(i);  // 从 i 开始 DFS
        }
    }
    
    // 步骤5：缩点，统计每个 SCC 的出度
    // 遍历原图的所有边，如果边的两端属于不同 SCC，则起点的 SCC 出度加1
    for (int u = 1; u <= n; u++) {
        for (int v : adj[u]) {
            if (scc_id[u] != scc_id[v]) {
                // 边 (u,v) 是 SCC 之间的边，增加 scc[u] 的出度
                scc_outdeg[scc_id[u]]++;
            }
        }
    }
    
    // 步骤6：统计出度为 0 的 SCC 数量和答案
    int ans = 0;        // 最终答案：受欢迎奶牛数量
    int zero_out = 0;   // 出度为 0 的 SCC 数量
    
    for (int i = 1; i <= scc_cnt; i++) {
        if (scc_outdeg[i] == 0) {  // 找到出度为 0 的 SCC
            zero_out++;  // 计数
            ans = scc_size[i];  // 记录该 SCC 的节点数
        }
    }
    
    // 步骤7：输出结果
    // 如果有多个出度为 0 的 SCC，说明不存在被所有奶牛喜欢的奶牛
    if (zero_out > 1) {
        cout << 0 << endl;
    } else {
        cout << ans << endl;
    }
    
    return 0;
}
```

### Python 实现

```python
import sys
from collections import defaultdict

# 设置递归深度，防止 DFS 过深导致栈溢出
# 对于 N ≤ 10000 的数据，默认递归深度可能不够
sys.setrecursionlimit(20000)

def solve():
    # 读入数据
    data = sys.stdin.read().split()
    idx = 0
    n = int(data[idx]); idx += 1  # 节点数
    m = int(data[idx]); idx += 1  # 边数
    
    # 构建邻接表
    # adj[u] 存储从 u 出发的所有邻接节点
    adj = [[] for _ in range(n + 1)]
    for _ in range(m):
        a = int(data[idx]); idx += 1
        b = int(data[idx]); idx += 1
        adj[a].append(b)  # 添加有向边 a → b
    
    # Tarjan 算法相关数据结构
    dfn = [0] * (n + 1)      # 访问时间戳，0 表示未访问
    low = [0] * (n + 1)      # 能回溯到的最小时间戳
    timestamp = [0]          # 使用列表实现引用传递，模拟全局变量
    stack = []               # 节点栈
    in_stack = [False] * (n + 1)  # 标记节点是否在栈中
    scc_id = [0] * (n + 1)   # 每个节点所属 SCC 的编号
    scc_cnt = [0]            # SCC 总数
    scc_size = defaultdict(int)  # 每个 SCC 的节点数量
    
    def tarjan(u):
        """
        Tarjan 算法求强连通分量
        
        参数:
            u: 当前访问的节点
        
        时间复杂度：O(N + M)
        空间复杂度：O(N)
        """
        # 步骤1：初始化时间戳
        timestamp[0] += 1
        dfn[u] = low[u] = timestamp[0]
        
        # 步骤2：节点入栈
        stack.append(u)
        in_stack[u] = True
        
        # 步骤3：遍历所有出边
        for v in adj[u]:
            if dfn[v] == 0:
                # 未访问，递归
                tarjan(v)
                # 回溯更新 low 值
                low[u] = min(low[u], low[v])
            elif in_stack[v]:
                # 在栈中，更新 low
                low[u] = min(low[u], dfn[v])
        
        # 步骤4：判断是否为 SCC 根
        if dfn[u] == low[u]:
            # 发现新的 SCC
            scc_cnt[0] += 1
            while True:
                v = stack.pop()
                in_stack[v] = False
                scc_id[v] = scc_cnt[0]
                scc_size[scc_cnt[0]] += 1
                if v == u:
                    break
    
    # 对所有未访问节点运行 Tarjan
    for i in range(1, n + 1):
        if dfn[i] == 0:
            tarjan(i)
    
    # 缩点统计出度
    scc_outdeg = defaultdict(int)
    for u in range(1, n + 1):
        for v in adj[u]:
            if scc_id[u] != scc_id[v]:
                scc_outdeg[scc_id[u]] += 1
    
    # 统计出度为 0 的 SCC
    ans = 0
    zero_out = 0
    for i in range(1, scc_cnt[0] + 1):
        if scc_outdeg[i] == 0:
            zero_out += 1
            ans = scc_size[i]
    
    # 输出结果
    if zero_out > 1:
        print(0)
    else:
        print(ans)

if __name__ == "__main__":
    solve()
```

## 时间/空间复杂度分析

### Tarjan 算法复杂度

- **时间复杂度**：O(N + M)
  - 每个节点访问一次，每条边访问一次
  - N 是节点数，M 是边数

- **空间复杂度**：O(N)
  - dfn、low、in_stack、scc_id、scc_size 数组：O(N)
  - 递归栈空间：O(N)
  - 节点栈空间：O(N)

### 整体复杂度

- **时间复杂度**：O(N + M)
  - Tarjan 算法 O(N + M)
  - 缩点统计出度 O(M)
  - 统计答案 O(N)

- **空间复杂度**：O(N + M)
  - 邻接表存储图：O(N + M)
  - 各种数组：O(N)

## 同类题目拓展

### 洛谷平台

| 题号 | 题目名称 | 难度 | 核心考点 |
|------|---------|------|---------|
| P3387 | 【模板】缩点 | 提高 | SCC + 缩点 + DAG DP |
| P2863 | The Cow Prom S | 普及/提高- | 统计 SCC 数量 |
| P1407 | 国家集训队 - 稳定婚姻 | 提高+/省选- | SCC + 建图技巧 |

### 其他平台

| 平台 | 题号 | 题目名称 | 难度 |
|------|------|---------|------|
| POJ | 2186 | Popular Cows | 中等 |
| HDU | 1269 | 迷宫城堡 | 入门 |
| 牛客网 | - | 受欢迎的牛 | 中等 |

## ML/DL 关联思考

### 图结构简化

这道题展示了强连通分量在**图结构简化**中的核心作用：

1. **冗余信息合并**：互相喜欢的奶牛群体（SCC）可以视为一个整体，这在社交网络分析中非常有用。在 GNN（图神经网络）中，可以将强连通分量内的节点合并，降低图规模。

2. **特征传播效率**：在缩点后的 DAG 上，特征传播（Feature Propagation）可以按拓扑序进行，避免循环依赖，提高训练效率。

### 实际应用场景

**社交网络影响力分析**：
- 将用户视为节点，关注关系视为有向边
- 强连通分量代表相互关注的用户群体（如粉丝圈）
- 出度为 0 的 SCC 代表"终极关注目标"
- 可以用 GNN 在此结构上学习用户影响力特征

**代码仓库依赖分析**：
- 模块间的依赖关系构成有向图
- 强连通分量代表循环依赖的模块组
- 识别出度为 0 的 SCC 有助于找到"基础模块"
