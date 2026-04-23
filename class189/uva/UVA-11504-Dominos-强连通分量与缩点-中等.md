# UVA 11504 - Dominos - 强连通分量与缩点

## 题目链接
https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2499

## 题目描述
Dominos are placed vertically along a line. Some dominos are pushed to the right, causing other dominos to fall. When a domino falls, it causes dominos in its range to fall as well. We need to determine the minimum number of dominos to push initially to make all dominos fall.

In graph terms: Given a directed graph, find the minimum number of vertices to start from such that all vertices are reachable. This is equivalent to finding the number of vertices with in-degree 0 in the condensed graph (DAG formed by SCC).

### 输入格式
- First line: T (number of test cases)
- For each test case:
  - First line: n (number of dominos), m (number of relations)
  - Next m lines: x, y - if x falls, it causes y to fall

### 输出格式
For each test case, output the minimum number of dominos to push.

### 输入输出样例

**Input**
```
2
3 2
1 2
2 3
4 4
1 2
2 1
1 3
3 4
```

**Output**
```
1
2
```

---

## 笔试/面试考察点分析

### 核心考察点
1. **强连通分量识别**：在有向图中找到所有SCC
2. **缩点后DAG处理**：将每个SCC缩为单个节点，形成DAG
3. **入度分析**：统计缩点后DAG中入度为0的节点数
4. **图论转化**：将原问题转化为图论问题

### 常见坑点
1. **缩点实现**：正确构建缩点后的DAG
2. **重边处理**：缩点后可能存在重边，需要去重
3. **单节点SCC**：单独节点也是有效的SCC

### 面试口述要点
```
"这个问题可以转化为图论问题：在有向图中，找到最少的起始点，
使得从这些点出发可以到达所有节点。
解决步骤：
1. 使用Tarjan算法找出所有强连通分量；
2. 将每个SCC缩为单个节点，构建DAG；
3. 统计DAG中入度为0的节点数，这就是答案。"
```

---

## 解题思路

### 步骤1：建图并求SCC
使用Tarjan算法求出图中所有强连通分量，时间复杂度O(n+m)。

### 步骤2：缩点构建DAG
将每个SCC缩为单个节点，构建缩点后的DAG。

### 步骤3：统计入度为0的节点数
在缩点后的DAG中，统计入度为0的节点数量。

### 步骤4：处理重边
缩点后可能产生重边，需要去重避免错误计算入度。

---

## 完整代码实现

### C++ 实现

```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 1e5 + 5;

int n, m;
vector<int> adj[MAXN];  // 原始有向图邻接表，ML中图数据常用邻接表存储

// Tarjan算法相关变量
int dfn[MAXN], low[MAXN], timestamp = 0;  // dfn时间戳/low最早回溯时间戳，核心数组
bool in_stack[MAXN];  // Tarjan算法：标记节点是否在栈中，判断环的关键
stack<int> st;  // Tarjan算法：存储当前遍历路径节点
int scc_id[MAXN], scc_cnt = 0;  // scc_id[i]节点i所属分量ID/scc_cnt分量总数，缩点核心标识

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
        int v;
        do {
            v = st.top();
            st.pop();
            in_stack[v] = false;  // 标记出栈
            scc_id[v] = scc_cnt;  // 记录节点所属分量ID
        } while (v != u);  // 直到当前节点出栈，分量标记完成
    }
}

int main() {
    ios::sync_with_stdio(false);  // 关闭同步，加速IO
    cin.tie(nullptr);
    
    int t;
    cin >> t;
    while (t--) {
        cin >> n >> m;
        
        // 初始化
        for (int i = 1; i <= n; i++) {
            adj[i].clear();  // 清空邻接表
            dfn[i] = 0;      // 重置dfn数组
        }
        scc_cnt = 0;         // 重置SCC计数
        timestamp = 0;       // 重置时间戳
        
        // 读取边
        for (int i = 0; i < m; i++) {
            int x, y;
            cin >> x >> y;
            adj[x].push_back(y);  // 添加有向边x->y
        }
        
        // 求强连通分量
        for (int i = 1; i <= n; i++) {
            if (!dfn[i]) {
                tarjan(i);  // 未访问节点启动Tarjan
            }
        }
        
        // 构建缩点后的DAG并统计入度
        vector<bool> has_incoming(scc_cnt + 1, false);  // 标记SCC是否有入边
        set<pair<int, int>> edge_set;  // 用集合去重，避免重边影响入度计算
        
        for (int u = 1; u <= n; u++) {
            for (int v : adj[u]) {
                int scc_u = scc_id[u];  // u所属的SCC
                int scc_v = scc_id[v];  // v所属的SCC
                
                if (scc_u != scc_v) {  // 不同SCC之间才有边
                    edge_set.insert({scc_u, scc_v});  // 添加缩点后的边
                }
            }
        }
        
        // 标记有入边的SCC
        for (auto& edge : edge_set) {
            has_incoming[edge.second] = true;  // 目标SCC有入边
        }
        
        // 统计入度为0的SCC数量
        int ans = 0;
        for (int i = 1; i <= scc_cnt; i++) {
            if (!has_incoming[i]) {
                ans++;  // 入度为0的SCC需要手动触发
            }
        }
        
        cout << ans << "\n";  // 输出答案
    }
    
    return 0;
}
```

### Java 实现

```java
import java.io.*;
import java.util.*;

public class UVA11504 {
    static final int MAXN = 100005;
    
    static int n, m;
    static List<Integer>[] adj = new ArrayList[MAXN];  // 原始有向图邻接表，ML中图数据常用邻接表存储
    
    // Tarjan算法相关变量
    static int[] dfn = new int[MAXN];  // dfn时间戳，节点发现时间
    static int[] low = new int[MAXN];  // low值，能回溯到的最早祖先
    static int timestamp = 0;  // 时间戳计数器
    static boolean[] inStack = new boolean[MAXN];  // 标记节点是否在栈中
    static Stack<Integer> stack = new Stack<>();  // Tarjan算法栈
    static int[] sccId = new int[MAXN];  // 节点所属SCC编号
    static int sccCnt = 0;  // SCC总数
    
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
            int v;
            do {
                v = stack.pop();
                inStack[v] = false;  // 标记出栈
                sccId[v] = sccCnt;  // 记录节点所属分量ID
            } while (v != u);  // 直到当前节点出栈，分量标记完成
        }
    }
    
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {
        FastReader in = new FastReader();
        PrintWriter out = new PrintWriter(System.out);
        
        int t = in.nextInt();
        while (t-- > 0) {
            n = in.nextInt();
            m = in.nextInt();
            
            // 初始化
            for (int i = 1; i <= n; i++) {
                if (adj[i] == null) adj[i] = new ArrayList<>();
                adj[i].clear();  // 清空邻接表
                dfn[i] = 0;      // 重置dfn数组
            }
            sccCnt = 0;          // 重置SCC计数
            timestamp = 0;       // 重置时间戳
            
            // 读取边
            for (int i = 0; i < m; i++) {
                int x = in.nextInt();
                int y = in.nextInt();
                adj[x].add(y);  // 添加有向边x->y
            }
            
            // 求强连通分量
            for (int i = 1; i <= n; i++) {
                if (dfn[i] == 0) {
                    tarjan(i);  // 未访问节点启动Tarjan
                }
            }
            
            // 构建缩点后的DAG并统计入度
            boolean[] hasIncoming = new boolean[sccCnt + 1];  // 标记SCC是否有入边
            Set<String> edgeSet = new HashSet<>();  // 用集合去重，避免重边影响入度计算
            
            for (int u = 1; u <= n; u++) {
                for (int v : adj[u]) {
                    int sccU = sccId[u];  // u所属的SCC
                    int sccV = sccId[v];  // v所属的SCC
                    
                    if (sccU != sccV) {  // 不同SCC之间才有边
                        edgeSet.add(sccU + "," + sccV);  // 添加缩点后的边
                    }
                }
            }
            
            // 标记有入边的SCC
            for (String edge : edgeSet) {
                String[] parts = edge.split(",");
                int targetScc = Integer.parseInt(parts[1]);
                hasIncoming[targetScc] = true;  // 目标SCC有入边
            }
            
            // 统计入度为0的SCC数量
            int ans = 0;
            for (int i = 1; i <= sccCnt; i++) {
                if (!hasIncoming[i]) {
                    ans++;  // 入度为0的SCC需要手动触发
                }
            }
            
            out.println(ans);  // 输出答案
        }
        
        out.flush();
        out.close();
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
    }
}
```

### Python 实现

```python
import sys
from collections import defaultdict

def solve():
    input_lines = sys.stdin.read().strip().split('\n')
    idx = 0
    
    t = int(input_lines[idx])
    idx += 1
    
    results = []
    
    for _ in range(t):
        n, m = map(int, input_lines[idx].split())
        idx += 1
        
        # 建图
        graph = defaultdict(list)  # 原始有向图邻接表，ML中图数据常用邻接表存储
        for _ in range(m):
            x, y = map(int, input_lines[idx].split())
            idx += 1
            graph[x].append(y)  # 添加有向边x->y
        
        # Tarjan算法相关变量
        dfn = [0] * (n + 1)  # dfn时间戳，节点发现时间
        low = [0] * (n + 1)  # low值，能回溯到的最早祖先
        timestamp = [0]  # 使用列表包装，实现引用传递
        stack = []  # Tarjan算法栈
        in_stack = [False] * (n + 1)  # 标记节点是否在栈中
        scc_id = [0] * (n + 1)  # 节点所属SCC编号
        scc_cnt = [0]  # SCC总数，使用列表实现引用传递
        
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
                while True:
                    v = stack.pop()
                    in_stack[v] = False  # 标记出栈
                    scc_id[v] = scc_cnt[0]  # 记录节点所属分量ID
                    if v == u:
                        break  # 直到当前节点出栈，分量标记完成
        
        # 求强连通分量
        for i in range(1, n + 1):
            if dfn[i] == 0:
                tarjan(i)  # 未访问节点启动Tarjan
        
        # 构建缩点后的DAG并统计入度
        has_incoming = [False] * (scc_cnt[0] + 1)  # 标记SCC是否有入边
        edge_set = set()  # 用集合去重，避免重边影响入度计算
        
        for u in range(1, n + 1):
            for v in graph[u]:
                scc_u = scc_id[u]  # u所属的SCC
                scc_v = scc_id[v]  # v所属的SCC
                
                if scc_u != scc_v:  # 不同SCC之间才有边
                    edge_set.add((scc_u, scc_v))  # 添加缩点后的边
        
        # 标记有入边的SCC
        for scc_u, scc_v in edge_set:
            has_incoming[scc_v] = True  # 目标SCC有入边
        
        # 统计入度为0的SCC数量
        ans = 0
        for i in range(1, scc_cnt[0] + 1):
            if not has_incoming[i]:
                ans += 1  # 入度为0的SCC需要手动触发
        
        results.append(str(ans))  # 添加结果
    
    print('\n'.join(results))

solve()
```

---

## 时间/空间复杂度分析

### 时间复杂度
- **建图**：O(m)，遍历所有边
- **Tarjan算法**：O(n + m)，每个节点访问一次，每条边遍历一次
- **缩点处理**：O(n + m)，遍历所有边构建缩点后DAG
- **统计入度**：O(SCC数量)，最多O(n)
- **总时间复杂度**：O(n + m) 每个测试用例

### 空间复杂度
- **图存储**：O(n + m)，邻接表
- **Tarjan数组**：O(n)，dfn、low、inStack等
- **DAG存储**：O(SCC数量^2) 最坏情况，但实际上远小于此
- **总空间复杂度**：O(n + m)

### 面试常考复杂度问题
**Q: 为什么缩点后得到的一定是有向无环图(DAG)？**

**A:** 
- 如果缩点后存在环，那么这个环上的所有SCC实际上可以合并成一个更大的SCC
- 这与原SCC的"极大性"矛盾（即不能加入更多节点保持强连通）
- 因此缩点后的图必定是DAG

---

## 同类题目拓展

### 同平台类似题
| 题号 | 题目名称 | 难度 | 说明 |
|------|----------|------|------|
| UVA 2 strongly_connected | Components | 中等 | 基础SCC题 |
| UVA 11770 | Lighting Away | 中等 | 类似dominos问题 |
| UVA 1229 - Sub-dictionary | 中等 | SCC+拓扑排序 |

### 其他平台类似题
| 平台 | 题号 | 题目名称 | 说明 |
|------|------|----------|------|
| POJ | 2186 | Popular Cows | SCC+缩点经典题 |
| HDU | 1269 | 迷宫城堡 | SCC模板题 |
| Codeforces | 427C | Checkposts | SCC最小成本 |

### 笔试面试变种方向
1. **最小加边数**：最少加几条边使图变为强连通图
2. **最大可达点数**：从一个点出发最多能到达多少点
3. **关键节点**：删除哪个点会使答案变化

---

## ML/DL关联思考

### 1. 信息传播模型
在实际机器学习场景中，**Domino问题类似于信息在网络中的传播**：

```python
def simulate_information_spread(graph, initial_nodes):
    """
    模拟信息在有向图中的传播过程
    
    Args:
        graph: 邻接表表示的有向图
        initial_nodes: 初始激活节点集合
    
    Returns:
        activated_nodes: 最终激活的节点集合
    """
    # 1. 求SCC
    scc_list, node_to_scc = tarjan_scc_with_mapping(graph)
    
    # 2. 构建缩点后的DAG
    condensed_graph = build_condensed_graph(graph, node_to_scc)
    
    # 3. 在DAG上模拟传播
    activated_scc = set()
    for node in initial_nodes:
        activated_scc.add(node_to_scc[node])
    
    # 4. 通过拓扑排序传播
    topo_order = topological_sort(condensed_graph)
    for scc in topo_order:
        if scc in activated_scc:
            # 该SCC被激活，激活所有邻居SCC
            for neighbor_scc in condensed_graph.get(scc, []):
                activated_scc.add(neighbor_scc)
    
    # 5. 转换回原节点
    activated_nodes = set()
    for scc_id, nodes in enumerate(scc_list):
        if scc_id in activated_scc:
            activated_nodes.update(nodes)
    
    return activated_nodes
```

**ML应用价值：**
- **病毒传播建模**：预测疾病或信息的传播范围
- **营销策略**：选择最有影响力的初始用户
- **网络鲁棒性**：评估网络对外部冲击的响应

### 2. 图神经网络中的影响传播
在GNN中，**SCC可用于优化消息传播机制**：

```python
class SCCBasedPropagation(nn.Module):
    """基于SCC的图消息传播机制"""
    
    def __init__(self, node_dim, scc_dim):
        super().__init__()
        # SCC内部传播层
        self.intra_propagation = nn.Linear(node_dim, node_dim)
        # SCC间传播层
        self.inter_propagation = nn.Linear(node_dim, scc_dim)
        
    def forward(self, x, edge_index, scc_mapping):
        """
        Args:
            x: 节点特征 [num_nodes, node_dim]
            edge_index: 边索引 [2, num_edges]
            scc_mapping: 节点到SCC的映射 [num_nodes]
        """
        # 1. SCC内部消息传播
        # 对每个SCC内部进行密集连接的消息传递
        intra_updated = x.clone()
        
        # 2. 构建SCC级别的图进行跨SCC传播
        scc_features = self.aggregate_by_scc(intra_updated, scc_mapping)
        condensed_edge_index = self.build_condensed_edges(edge_index, scc_mapping)
        
        # 3. 在SCC间传播消息
        final_scc_repr = self.inter_propagation(scc_features)
        
        # 4. 将SCC表示广播回节点级别
        node_repr = self.broadcast_to_nodes(final_scc_repr, scc_mapping)
        
        return node_repr
    
    def aggregate_by_scc(self, node_features, scc_mapping):
        """按SCC聚合节点特征"""
        unique_sccs = torch.unique(scc_mapping)
        scc_features = []
        
        for scc_id in unique_sccs:
            mask = scc_mapping == scc_id
            scc_feat = torch.mean(node_features[mask], dim=0, keepdim=True)
            scc_features.append(scc_feat)
        
        return torch.cat(scc_features, dim=0)
```

**GNN优化价值：**
- **计算效率**：减少不必要的消息传递
- **可解释性**：SCC提供自然的社区结构
- **稳定性**：避免在强连通区域内的过度平滑

### 3. 网络可控性分析
**SCC在控制系统理论中的应用**：

```python
def analyze_network_controllability(graph):
    """分析网络的可控性"""
    # 1. 检测SCC
    scc_list = tarjan_scc(graph)
    
    # 2. 构建缩点后的DAG
    scc_graph = build_scc_dag(graph, scc_list)
    
    # 3. 计算最小控制输入数量
    # 等于DAG中入度为0的节点数
    zero_indegree_sccs = 0
    for scc_id in range(len(scc_list)):
        in_degree = sum(1 for neighbors in scc_graph.values() if scc_id in neighbors)
        if in_degree == 0:
            zero_indegree_sccs += 1
    
    controllability_metrics = {
        # 最小控制节点数
        'min_control_nodes': zero_indegree_sccs,
        
        # 网络层次性（DAG深度）
        'network_hierarchy': calculate_dag_depth(scc_graph),
        
        # 控制路径长度
        'avg_control_distance': calculate_avg_distance(scc_graph),
        
        # 鲁棒性指标
        'robustness_score': len(scc_list) / len(graph.nodes())  # SCC越分散越鲁棒
    }
    
    return controllability_metrics
```

**应用价值：**
- **系统控制**：确定最小控制输入集合
- **网络设计**：优化网络结构以提高可控性
- **故障诊断**：识别网络中的关键控制点

---

## 总结

本题是**强连通分量与缩点的经典应用题**，核心掌握点：

1. **问题转化**：将原问题转化为图论中的最小起始点问题
2. **SCC识别**：使用Tarjan算法找到所有强连通分量
3. **缩点思想**：将SCC缩为单个节点，形成DAG
4. **入度分析**：统计DAG中入度为0的节点数
5. **复杂度分析**：时间O(V+E)、空间O(V+E)，面试高频考点
6. **ML/DL应用**：信息传播、网络控制、GNN优化

**笔试建议**：熟练掌握缩点后DAG的构建过程，注意重边处理。
**面试建议**：能清晰解释为什么答案等于缩点后入度为0的节点数。