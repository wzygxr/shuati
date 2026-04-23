# Codeforces 427C - Checkposts - 强连通分量与缩点

## 题目链接
https://codeforces.com/problemset/problem/427/C

## 题目描述
There are `n` posts denoted by integers from `1` to `n` and `m` one-way roads connecting them. There may be multiple roads connecting the same pair of posts, and there may be roads connecting a post to itself. Each post has a cost to build a checkpost. We need to find the minimal total cost to build checkposts such that every post is reachable from at least one checkpost.

### 输入格式
- First line contains integer `n` (1 ≤ n ≤ 10^5) - number of posts
- Second line contains `n` integers `c_i` (1 ≤ c_i ≤ 10^9) - cost to build checkpost at post `i`
- Third line contains integer `m` (0 ≤ m ≤ 3·10^5) - number of roads
- Next `m` lines contain pairs `u_i`, `v_i` (1 ≤ u_i, v_i ≤ n) - roads from `u_i` to `v_i`

### 输出格式
Output two space-separated integers: minimal total cost and number of ways to achieve this cost modulo 10^9 + 7.

### 输入输出样例

**Input**
```
4
1 2 3 4
4
1 2
2 3
3 4
4 1
```

**Output**
```
1 1
```

---

## 笔试/面试考察点分析

### 核心考察点
1. **强连通分量识别**：在有向图中找到所有SCC
2. **缩点后DAG处理**：SCC缩点后变成DAG，每个SCC只需要建一个checkpost
3. **最小值计数**：在每个SCC中选择cost最小的点，同时计算方案数

### 常见坑点
1. **数据类型**：cost可能达到10^9，需要用long long
2. **方案数计算**：同一SCC中最小值可能有多个，需要乘法原理
3. **缩点细节**：自环也是合法的SCC

### 面试口述要点
```
"这个问题的核心是：在有向图中选择最少的点，使得从这些点出发可以到达所有节点。
第一步：用Tarjan算法求出所有强连通分量；
第二步：每个SCC内部只需要选择一个点建立checkpost；
第三步：对于每个SCC，选择成本最低的点，统计方案数；
第四步：所有SCC的选择相乘得到总方案数。"
```

---

## 解题思路

### 步骤1：建图并求SCC
使用Tarjan算法求出图中所有强连通分量，时间复杂度O(n+m)。

### 步骤2：统计每个SCC的最小成本
对每个SCC，找出其中cost最小的点，以及最小值出现次数。

### 步骤3：计算总成本和方案数
将所有SCC的最小成本相加，方案数相乘。

---

## 完整代码实现

### C++ 实现

```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 1e5 + 5;
const long long MOD = 1e9 + 7;

int n, m;
long long cost[MAXN];  // 每个节点的建设成本，ML中可用于节点重要性评估

// 邻接表存储原始图
vector<int> adj[MAXN];  // 原始有向图邻接表，ML中图数据常用邻接表存储

// Tarjan算法相关变量
int dfn[MAXN], low[MAXN], timestamp = 0;  // dfn时间戳/low最早回溯时间戳，核心数组
bool in_stack[MAXN];  // Tarjan算法：标记节点是否在栈中，判断环的关键
stack<int> st;  // Tarjan算法：存储当前遍历路径节点
int scc_id[MAXN], scc_cnt = 0;  // scc_id[i]节点i所属分量ID/scc_cnt分量总数，缩点核心标识
vector<vector<int>> scc_nodes;  // 存储每个SCC包含的节点，笔试高频统计需求

// Tarjan算法求强连通分量：一次DFS完成SCC识别，笔试最常用模板
// 笔试中需快速手写，面试高频考察low数组更新逻辑；ML中可用于图核心结构提取
void tarjan(int u) {
    dfn[u] = low[u] = ++timestamp;  // 初始化时间戳：当前节点发现时间=最早回溯时间
    st.push(u);  // 节点入栈，记录遍历路径
    in_stack[u] = true;  // 标记栈内状态，避免重复处理
    for (int v : adj[u]) {  // 遍历所有出边
        if (!dfn[v]) {  // 未访问过的节点，递归遍历
            tarjan(v);
            low[u] = min(low[u], low[v]);  // 回溯更新low[u]：取子节点low最小值
        } else if (in_stack[v]) {  // 已访问且在栈中（属于当前强连通分量）
            low[u] = min(low[u], dfn[v]);  // 用子节点发现时间更新low[u]
        }
    }
    // 找到强连通分量的根节点（low[u]=dfn[u]），出栈标记分量
    if (dfn[u] == low[u]) {
        scc_cnt++;  // 分量计数+1
        vector<int> current_scc;  // 当前SCC的节点列表
        int v;
        do {
            v = st.top();
            st.pop();
            in_stack[v] = false;  // 标记出栈
            scc_id[v] = scc_cnt;  // 记录节点所属分量ID
            current_scc.push_back(v);  // 添加到当前SCC
        } while (v != u);  // 直到当前节点出栈，分量标记完成
        scc_nodes.push_back(current_scc);  // 添加到SCC集合
    }
}

int main() {
    ios::sync_with_stdio(false);  // 关闭同步，加速IO
    cin.tie(nullptr);

    cin >> n;
    for (int i = 1; i <= n; i++) {
        cin >> cost[i];  // 输入每个节点的建设成本
    }

    cin >> m;
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);  // 添加有向边u->v
    }

    // 对所有未访问节点运行Tarjan算法
    for (int i = 1; i <= n; i++) {
        if (!dfn[i]) {
            tarjan(i);
        }
    }

    // 计算最小成本和方案数
    long long total_cost = 0;  // 总建设成本
    long long total_ways = 1;  // 总方案数
    for (auto& scc : scc_nodes) {  // 遍历每个强连通分量
        long long min_cost = LLONG_MAX;  // 当前SCC的最小成本
        int count_min = 0;  // 最小成本出现次数
        
        // 找出当前SCC中的最小成本
        for (int node : scc) {
            if (cost[node] < min_cost) {
                min_cost = cost[node];  // 更新最小成本
                count_min = 1;  // 重置计数
            } else if (cost[node] == min_cost) {
                count_min++;  // 增加相同最小值的计数
            }
        }
        
        total_cost += min_cost;  // 累加最小成本
        total_ways = (total_ways * count_min) % MOD;  // 乘法原理计算方案数
    }

    cout << total_cost << " " << total_ways << "\n";
    return 0;
}
```

### Java 实现

```java
import java.io.*;
import java.util.*;

public class CF427CCheckposts {
    static final int MAXN = 100005;
    static final long MOD = 1000000007L;
    
    static int n, m;
    static long[] cost = new long[MAXN];  // 每个节点的建设成本，ML中可用于节点重要性评估
    
    // 邻接表存储原始图
    static List<Integer>[] adj = new ArrayList[MAXN];  // 原始有向图邻接表，ML中图数据常用邻接表存储
    
    // Tarjan算法相关变量
    static int[] dfn = new int[MAXN];  // dfn时间戳，节点发现时间
    static int[] low = new int[MAXN];  // low值，能回溯到的最早祖先
    static int timestamp = 0;  // 时间戳计数器
    static boolean[] inStack = new boolean[MAXN];  // 标记节点是否在栈中
    static Stack<Integer> stack = new Stack<>();  // Tarjan算法栈
    static int[] sccId = new int[MAXN];  // 节点所属SCC编号
    static int sccCnt = 0;  // SCC总数
    static List<List<Integer>> sccNodes = new ArrayList<>();  // 存储每个SCC的节点
    
    // Tarjan算法求强连通分量：一次DFS完成SCC识别，笔试最常用模板
    // 笔试中需快速手写，面试高频考察low数组更新逻辑；ML中可用于图核心结构提取
    static void tarjan(int u) {
        dfn[u] = low[u] = ++timestamp;  // 初始化时间戳：当前节点发现时间=最早回溯时间
        stack.push(u);  // 节点入栈，记录遍历路径
        inStack[u] = true;  // 标记栈内状态，避免重复处理
        
        for (int v : adj[u]) {  // 遍历所有出边
            if (dfn[v] == 0) {  // 未访问过的节点，递归遍历
                tarjan(v);
                low[u] = Math.min(low[u], low[v]);  // 回溯更新low[u]：取子节点low最小值
            } else if (inStack[v]) {  // 已访问且在栈中（属于当前强连通分量）
                low[u] = Math.min(low[u], dfn[v]);  // 用子节点发现时间更新low[u]
            }
        }
        
        // 找到强连通分量的根节点（low[u]=dfn[u]），出栈标记分量
        if (dfn[u] == low[u]) {
            sccCnt++;  // 分量计数+1
            List<Integer> currentScc = new ArrayList<>();  // 当前SCC的节点列表
            int v;
            do {
                v = stack.pop();
                inStack[v] = false;  // 标记出栈
                sccId[v] = sccCnt;  // 记录节点所属分量ID
                currentScc.add(v);  // 添加到当前SCC
            } while (v != u);  // 直到当前节点出栈，分量标记完成
            sccNodes.add(currentScc);  // 添加到SCC集合
        }
    }
    
    public static void main(String[] args) throws IOException {
        FastReader in = new FastReader();
        
        n = in.nextInt();
        for (int i = 1; i <= n; i++) {
            cost[i] = in.nextLong();  // 输入每个节点的建设成本
        }
        
        m = in.nextInt();
        for (int i = 1; i <= n; i++) {
            adj[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            adj[u].add(v);  // 添加有向边u->v
        }
        
        // 对所有未访问节点运行Tarjan算法
        for (int i = 1; i <= n; i++) {
            if (dfn[i] == 0) {
                tarjan(i);
            }
        }
        
        // 计算最小成本和方案数
        long totalCost = 0;  // 总建设成本
        long totalWays = 1;  // 总方案数
        for (List<Integer> scc : sccNodes) {  // 遍历每个强连通分量
            long minCost = Long.MAX_VALUE;  // 当前SCC的最小成本
            int countMin = 0;  // 最小成本出现次数
            
            // 找出当前SCC中的最小成本
            for (int node : scc) {
                if (cost[node] < minCost) {
                    minCost = cost[node];  // 更新最小成本
                    countMin = 1;  // 重置计数
                } else if (cost[node] == minCost) {
                    countMin++;  // 增加相同最小值的计数
                }
            }
            
            totalCost += minCost;  // 累加最小成本
            totalWays = (totalWays * countMin) % MOD;  // 乘法原理计算方案数
        }
        
        System.out.println(totalCost + " " + totalWays);
    }
    
    // 快速读取类，提高IO效率
    static class FastReader {
        BufferedReader br;
        StringTokenizer st;
        
        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }
        
        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }
        
        int nextInt() {
            return Integer.parseInt(next());
        }
        
        long nextLong() {
            return Long.parseLong(next());
        }
    }
}
```

### Python 实现

```python
import sys
from collections import defaultdict

def solve():
    # 读取输入
    n = int(input())
    costs = [0] + list(map(int, input().split()))  # 成本数组，1-indexed
    m = int(input())
    
    # 建图
    graph = defaultdict(list)  # 原始有向图邻接表，ML中图数据常用邻接表存储
    for _ in range(m):
        u, v = map(int, input().split())
        graph[u].append(v)  # 添加有向边u->v
    
    # Tarjan算法相关变量
    dfn = [0] * (n + 1)  # dfn时间戳，节点发现时间
    low = [0] * (n + 1)  # low值，能回溯到的最早祖先
    timestamp = [0]  # 使用列表包装，实现引用传递
    stack = []  # Tarjan算法栈
    in_stack = [False] * (n + 1)  # 标记节点是否在栈中
    scc_id = [0] * (n + 1)  # 节点所属SCC编号
    scc_cnt = [0]  # SCC总数，使用列表实现引用传递
    scc_nodes = []  # 存储每个SCC的节点
    
    # Tarjan算法求强连通分量：一次DFS完成SCC识别，笔试最常用模板
    # 笔试中需快速手写，面试高频考察low数组更新逻辑；ML中可用于图核心结构提取
    def tarjan(u):
        timestamp[0] += 1
        dfn[u] = low[u] = timestamp[0]  # 初始化时间戳：当前节点发现时间=最早回溯时间
        stack.append(u)  # 节点入栈，记录遍历路径
        in_stack[u] = True  # 标记栈内状态，避免重复处理
        
        for v in graph[u]:  # 遍历所有出边
            if dfn[v] == 0:  # 未访问过的节点，递归遍历
                tarjan(v)
                low[u] = min(low[u], low[v])  # 回溯更新low[u]：取子节点low最小值
            elif in_stack[v]:  # 已访问且在栈中（属于当前强连通分量）
                low[u] = min(low[u], dfn[v])  # 用子节点发现时间更新low[u]
        
        # 找到强连通分量的根节点（low[u]=dfn[u]），出栈标记分量
        if dfn[u] == low[u]:
            scc_cnt[0] += 1  # 分量计数+1
            current_scc = []  # 当前SCC的节点列表
            while True:
                v = stack.pop()
                in_stack[v] = False  # 标记出栈
                scc_id[v] = scc_cnt[0]  # 记录节点所属分量ID
                current_scc.append(v)  # 添加到当前SCC
                if v == u:
                    break  # 直到当前节点出栈，分量标记完成
            scc_nodes.append(current_scc)  # 添加到SCC集合
    
    # 对所有未访问节点运行Tarjan算法
    for i in range(1, n + 1):
        if dfn[i] == 0:
            tarjan(i)
    
    # 计算最小成本和方案数
    total_cost = 0  # 总建设成本
    total_ways = 1  # 总方案数
    MOD = 1000000007
    
    for scc in scc_nodes:  # 遍历每个强连通分量
        min_cost = float('inf')  # 当前SCC的最小成本
        count_min = 0  # 最小成本出现次数
        
        # 找出当前SCC中的最小成本
        for node in scc:
            if costs[node] < min_cost:
                min_cost = costs[node]  # 更新最小成本
                count_min = 1  # 重置计数
            elif costs[node] == min_cost:
                count_min += 1  # 增加相同最小值的计数
        
        total_cost += min_cost  # 累加最小成本
        total_ways = (total_ways * count_min) % MOD  # 乘法原理计算方案数
    
    print(total_cost, total_ways)

solve()
```

---

## 时间/空间复杂度分析

### 时间复杂度
- **建图**：O(m)，遍历所有边
- **Tarjan算法**：O(n + m)，每个节点访问一次，每条边遍历一次
- **SCC处理**：O(n)，遍历所有节点统计最小成本
- **总时间复杂度**：O(n + m)

### 空间复杂度
- **图存储**：O(n + m)，邻接表
- **Tarjan数组**：O(n)，dfn、low、inStack等
- **SCC存储**：O(n)，sccId、栈等
- **总空间复杂度**：O(n + m)

### 面试常考复杂度问题
**Q: 为什么Tarjan算法时间复杂度是O(V+E)？**

**A:** 
- DFS过程中每个节点只被访问一次，时间O(V)
- 每条边只被遍历一次（从起点出发时），时间O(E)
- 栈操作（入栈出栈）每个节点各一次，时间O(V)
- 因此总时间复杂度为O(V + E)

---

## 同类题目拓展

### 同平台类似题
| 题号 | 题目名称 | 难度 | 说明 |
|------|----------|------|------|
| CF 228E | The Road to Berland is Paved With Good Intentions | 中等 | 2-SAT + SCC |
| CF 455C | Civilization | 中等 | 并查集 + SCC |
| CF 292D | Connected Components | 中等 | 离线处理 + 并查集 |

### 其他平台类似题
| 平台 | 题号 | 题目名称 | 说明 |
|------|------|----------|------|
| POJ | 2186 | Popular Cows | SCC+缩点经典题 |
| HDU | 1269 | 迷宫城堡 | SCC模板题 |
| LeetCode | 1192 | Critical Connections | 桥边检测 |

### 笔试面试变种方向
1. **加权SCC**：每个SCC内节点有权值，选择最优策略
2. **动态SCC**：边的增加/删除对SCC的影响
3. **概率SCC**：边存在的概率不确定，期望SCC数量

---

## ML/DL关联思考

### 1. 图结构数据预处理
在实际机器学习场景中，**SCC可用于图数据的降噪和简化**：

```python
def optimize_graph_by_scc(graph, node_costs):
    """
    使用SCC优化图上的资源分配问题
    
    Args:
        graph: 邻接表表示的有向图
        node_costs: 节点建设成本数组
    
    Returns:
        optimal_cost: 最优总成本
        solution_count: 方案数量
    """
    # 1. 求强连通分量
    scc_list = tarjan_scc(graph)
    
    # 2. 在每个SCC中选择最小成本节点
    total_cost = 0
    total_ways = 1
    MOD = 10**9 + 7
    
    for scc in scc_list:
        min_cost = min(node_costs[node] for node in scc)
        min_count = sum(1 for node in scc if node_costs[node] == min_cost)
        
        total_cost += min_cost
        total_ways = (total_ways * min_count) % MOD
    
    return total_cost, total_ways
```

**ML应用价值：**
- **资源优化**：在社交网络中选择关键节点投放广告
- **影响力最大化**：在传播模型中选择最佳种子节点
- **网络监控**：在通信网络中选择最少监控点覆盖全部流量

### 2. 图神经网络优化
在GNN训练中，**SCC可用于优化注意力机制**：

```python
class SCCHierarchicalGNN(nn.Module):
    """基于SCC的层次化图神经网络"""
    
    def __init__(self, in_dim, hidden_dim, out_dim):
        super().__init__()
        # SCC内部聚合层
        self.intra_scc_layer = nn.Linear(in_dim, hidden_dim)
        # SCC之间聚合层（在DAG上进行）
        self.inter_scc_layer = nn.Linear(hidden_dim, out_dim)
    
    def forward(self, x, edge_index):
        # 1. 求SCC
        scc_list = self.compute_scc(edge_index)
        
        # 2. SCC内部特征聚合
        scc_reprs = []
        for scc in scc_list:
            scc_features = x[scc]  # SCC内节点特征
            # 内部聚合（如平均池化）
            intra_repr = scc_features.mean(dim=0)
            scc_reprs.append(self.intra_scc_layer(intra_repr))
        
        scc_reprs = torch.stack(scc_reprs)
        
        # 3. SCC之间交互（在缩点后的DAG上传播）
        final_repr = self.inter_scc_layer(scc_reprs)
        
        return final_repr
```

**GNN优化价值：**
- **计算效率**：减少消息传递轮次，加速训练
- **层次化表示**：学习局部和全局特征
- **可解释性**：SCC提供语义模块化

### 3. 社交网络分析
**SCC在社区发现中的应用**：

```python
def analyze_social_network_communities(adj_matrix):
    """分析社交网络中的紧密社区"""
    # 转换为邻接表
    graph = convert_to_adj_list(adj_matrix)
    
    # 求SCC（紧密连接的社区）
    scc_list = tarjan_scc(graph)
    
    community_stats = {
        # 社区数量
        'num_communities': len(scc_list),
        
        # 社区大小分布
        'size_distribution': [len(scc) for scc in scc_list],
        
        # 平均社区密度
        'avg_density': calculate_avg_density(scc_list, graph),
        
        # 最大社区占比
        'largest_community_ratio': max(len(scc) for scc in scc_list) / len(graph.nodes())
    }
    
    return community_stats
```

**应用价值：**
- **社区发现**：识别用户群体和兴趣社区
- **影响力分析**：SCC内部影响力传播特性
- **推荐系统**：基于社区结构的个性化推荐

---

## 总结

本题是**强连通分量的经典应用题**，核心掌握点：

1. **SCC识别**：使用Tarjan算法找到所有强连通分量
2. **缩点思想**：每个SCC只需选择一个节点，转化为DAG问题
3. **组合数学**：最小值计数，乘法原理计算方案数
4. **复杂度分析**：时间O(V+E)、空间O(V+E)，面试高频考点
5. **ML/DL应用**：图优化、社区发现、资源分配

**笔试建议**：熟练掌握Tarjan模板，理解缩点后DAG的性质。
**面试建议**：能清晰解释为什么每个SCC只需选一个点，方案数如何计算。