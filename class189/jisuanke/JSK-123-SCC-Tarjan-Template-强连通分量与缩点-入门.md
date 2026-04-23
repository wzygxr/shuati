# 计蒜客 JSK-123 - 强连通分量模板题 - 强连通分量与缩点

## 题目链接
https://www.jisuanke.com/problem/123

## 题目描述
给定一个有向图，包含 n 个节点和 m 条边。请你求出图中所有的强连通分量，并输出每个强连通分量包含的节点。

这是一个经典的强连通分量模板题，要求实现 Tarjan 算法来找出所有强连通分量。

### 输入格式
- 第一行：两个整数 n, m (1 ≤ n ≤ 10⁴, 0 ≤ m ≤ 10⁵) - 节点数和边数
- 接下来 m 行：每行两个整数 u, v (1 ≤ u, v ≤ n) - 表示有一条从 u 到 v 的有向边

### 输出格式
- 第一行：一个整数 k - 强连通分量的个数
- 接下来 k 行：每行先输出该强连通分量包含的节点数，然后依次输出节点编号（按升序排列）

### 输入输出样例

**Input**
```
5 7
1 2
2 3
3 1
2 4
4 5
5 2
3 5
```

**Output**
```
2
3 1 2 3
2 4 5
```

---

## 笔试/面试考察点分析

### 核心考察点
1. **Tarjan算法实现**：熟练掌握Tarjan算法的完整实现
2. **强连通分量定义理解**：理解什么是强连通分量
3. **栈操作**：正确处理Tarjan算法中的栈操作
4. **low数组更新逻辑**：理解low数组的更新规则

### 常见坑点
1. **数组大小**：边数可能达到10⁵，数组要开够大
2. **初始化**：每个测试用例都要重新初始化相关数组
3. **栈操作**：出栈时要正确更新标记
4. **节点编号**：注意节点是从1开始还是0开始

### 面试口述要点
```
"Tarjan算法的核心思想是使用DFS遍历图，同时维护一个栈。
对于每个节点u，我们记录它的DFS序dfn[u]和它能到达的最小DFS序low[u]。
当我们发现dfn[u] == low[u]时，说明找到了一个强连通分量的根节点，
此时栈中从u到栈顶的所有节点构成一个强连通分量。"
```

---

## 解题思路

### 步骤1：建图
使用邻接表存储有向图，便于遍历每个节点的所有邻接点。

### 步骤2：Tarjan算法
实现Tarjan算法找出所有强连通分量：
- 使用dfn数组记录DFS序
- 使用low数组记录能到达的最小DFS序
- 使用栈记录当前搜索路径
- 当dfn[u] == low[u]时，弹出栈中节点形成一个SCC

### 步骤3：输出结果
整理并输出所有强连通分量的信息。

---

## 完整代码实现

### C++ 实现

```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 1e4 + 5;  // 最大节点数，根据题目数据范围设置
const int MAXM = 1e5 + 5;  // 最大边数，根据题目数据范围设置

int n, m;  // n:节点数, m:边数

// 邻接表存图
vector<int> adj[MAXN];  // 原始有向图邻接表，ML中图数据常用邻接表存储

// Tarjan算法相关数组
int dfn[MAXN];    // DFS序（发现时间戳），笔试按数据范围调整，面试需说明取值依据
int low[MAXN];    // 能回溯到的最早祖先的dfn值，核心数组
int timestamp = 0;  // 时间戳计数器

bool in_stack[MAXN];  // 标记节点是否在栈中，判断环的关键
stack<int> st;  // Tarjan算法：存储当前遍历路径节点

int scc_id[MAXN];  // 节点所属的强连通分量编号
int scc_cnt = 0;   // 强连通分量总数
vector<vector<int>> scc_nodes;  // 存储每个SCC包含的节点列表

// Tarjan算法求强连通分量：一次DFS完成SCC识别，笔试最常用模板
// 笔试中需快速手写，面试高频考察low数组更新逻辑；ML中可用于图核心结构提取
void tarjan(int u) {
    dfn[u] = low[u] = ++timestamp;  // 初始化时间戳：当前节点发现时间=最早回溯时间
    st.push(u);  // 节点入栈，记录遍历路径
    in_stack[u] = true;  // 标记栈内状态，避免重复处理
    
    for (int v : adj[u]) {  // 遍历所有出边
        if (!dfn[v]) {  // 未访问过的节点（树边），递归遍历
            tarjan(v);
            low[u] = min(low[u], low[v]);  // 回溯更新low[u]：取子节点low最小值
        } else if (in_stack[v]) {  // 已访问且在栈中（回边），属于当前强连通分量
            low[u] = min(low[u], dfn[v]);  // 用子节点发现时间更新low[u]
        }
        // 如果v已访问且不在栈中（弃边），无需处理
    }
    
    // 找到强连通分量的根节点（low[u]=dfn[u]），出栈标记分量
    if (dfn[u] == low[u]) {
        scc_cnt++;  // 分量计数+1
        vector<int> current_scc;  // 当前SCC的节点列表
        int v;
        
        do {
            v = st.top();  // 弹出栈顶节点
            st.pop();
            in_stack[v] = false;  // 标记出栈
            scc_id[v] = scc_cnt;  // 记录节点所属分量ID
            current_scc.push_back(v);  // 添加到当前SCC
        } while (v != u);  // 直到当前节点u出栈，分量标记完成
        
        sort(current_scc.begin(), current_scc.end());  // SCC内节点按编号排序
        scc_nodes.push_back(current_scc);  // 添加到SCC集合
    }
}

int main() {
    ios::sync_with_stdio(false);  // 关闭同步，加速IO
    cin.tie(nullptr);
    
    cin >> n >> m;  // 读取节点数和边数
    
    // 读取边并建图
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);  // 添加从u到v的有向边
    }
    
    // 对所有未访问过的节点运行Tarjan算法
    // 图可能不连通，需要遍历所有节点作为起点
    for (int i = 1; i <= n; i++) {
        if (!dfn[i]) {  // 如果节点i未被访问过（dfn[i]==0）
            tarjan(i);  // 从i开始运行Tarjan算法
        }
    }
    
    // 输出结果
    cout << scc_cnt << "\n";  // 输出强连通分量的数量
    
    for (auto& scc : scc_nodes) {  // 遍历每个强连通分量
        cout << scc.size();  // 输出该SCC的节点数
        for (int node : scc) {  // 输出该SCC中的所有节点
            cout << " " << node;
        }
        cout << "\n";
    }
    
    return 0;
}
```

### Java 实现

```java
import java.io.*;
import java.util.*;

public class JSK123 {
    static final int MAXN = 10005;  // 最大节点数，笔试按数据范围调整，面试需说明取值依据
    
    static int n, m;  // n:节点数, m:边数
    
    // 邻接表存图
    static List<Integer>[] adj = new ArrayList[MAXN];  // 原始有向图邻接表，ML中图数据常用邻接表存储
    
    // Tarjan算法相关数组
    static int[] dfn = new int[MAXN];  // DFS序（发现时间戳），笔试按数据范围调整
    static int[] low = new int[MAXN];  // 能回溯到的最早祖先的dfn值，核心数组
    static int timestamp = 0;  // 时间戳计数器
    
    static boolean[] inStack = new boolean[MAXN];  // 标记节点是否在栈中，判断环的关键
    static Stack<Integer> stack = new Stack<>();  // Tarjan算法：存储当前遍历路径节点
    
    static int[] sccId = new int[MAXN];  // 节点所属的强连通分量编号
    static int sccCnt = 0;  // 强连通分量总数
    static List<List<Integer>> sccNodes = new ArrayList<>();  // 存储每个SCC包含的节点列表
    
    // Tarjan算法求强连通分量：一次DFS完成SCC识别，笔试最常用模板
    // 笔试中需快速手写，面试高频考察low数组更新逻辑；ML中可用于图核心结构提取
    static void tarjan(int u) {
        dfn[u] = low[u] = ++timestamp;  // 初始化时间戳：当前节点发现时间=最早回溯时间
        stack.push(u);  // 节点入栈，记录遍历路径
        inStack[u] = true;  // 标记栈内状态，避免重复处理
        
        for (int v : adj[u]) {  // 遍历所有出边
            if (dfn[v] == 0) {  // 未访问过的节点（树边），递归遍历
                tarjan(v);
                low[u] = Math.min(low[u], low[v]);  // 回溯更新low[u]：取子节点low最小值
            } else if (inStack[v]) {  // 已访问且在栈中（回边），属于当前强连通分量
                low[u] = Math.min(low[u], dfn[v]);  // 用子节点发现时间更新low[u]
            }
            // 如果v已访问且不在栈中（弃边），无需处理
        }
        
        // 找到强连通分量的根节点（low[u]=dfn[u]），出栈标记分量
        if (dfn[u] == low[u]) {
            sccCnt++;  // 分量计数+1
            List<Integer> currentScc = new ArrayList<>();  // 当前SCC的节点列表
            int v;
            
            do {
                v = stack.pop();  // 弹出栈顶节点
                inStack[v] = false;  // 标记出栈
                sccId[v] = sccCnt;  // 记录节点所属分量ID
                currentScc.add(v);  // 添加到当前SCC
            } while (v != u);  // 直到当前节点u出栈，分量标记完成
            
            Collections.sort(currentScc);  // SCC内节点按编号排序
            sccNodes.add(currentScc);  // 添加到SCC集合
        }
    }
    
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {
        FastReader in = new FastReader();
        PrintWriter out = new PrintWriter(System.out);
        
        n = in.nextInt();  // 读取节点数
        m = in.nextInt();  // 读取边数
        
        // 初始化邻接表
        for (int i = 1; i <= n; i++) {
            adj[i] = new ArrayList<>();
        }
        
        // 读取边并建图
        for (int i = 0; i < m; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            adj[u].add(v);  // 添加从u到v的有向边
        }
        
        // 对所有未访问过的节点运行Tarjan算法
        // 图可能不连通，需要遍历所有节点作为起点
        for (int i = 1; i <= n; i++) {
            if (dfn[i] == 0) {  // 如果节点i未被访问过
                tarjan(i);  // 从i开始运行Tarjan算法
            }
        }
        
        // 输出结果
        out.println(sccCnt);  // 输出强连通分量的数量
        
        for (List<Integer> scc : sccNodes) {  // 遍历每个强连通分量
            out.print(scc.size());  // 输出该SCC的节点数
            for (int node : scc) {  // 输出该SCC中的所有节点
                out.print(" " + node);
            }
            out.println();
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
    # 读取输入
    lines = sys.stdin.read().strip().split('\n')
    n, m = map(int, lines[0].split())
    
    # 建图
    graph = defaultdict(list)  # 原始有向图邻接表，ML中图数据常用邻接表存储
    for i in range(1, m + 1):
        u, v = map(int, lines[i].split())
        graph[u].append(v)  # 添加从u到v的有向边
    
    # Tarjan算法相关变量
    dfn = [0] * (n + 1)  # DFS序（发现时间戳），笔试按数据范围调整
    low = [0] * (n + 1)  # 能回溯到的最早祖先的dfn值，核心数组
    timestamp = [0]  # 使用列表包装，实现引用传递
    
    in_stack = [False] * (n + 1)  # 标记节点是否在栈中，判断环的关键
    stack = []  # Tarjan算法：存储当前遍历路径节点
    
    scc_id = [0] * (n + 1)  # 节点所属的强连通分量编号
    scc_cnt = [0]  # 强连通分量总数，使用列表实现引用传递
    scc_nodes = []  # 存储每个SCC包含的节点列表
    
    # Tarjan算法求强连通分量：一次DFS完成SCC识别，笔试最常用模板
    # 笔试中需快速手写，面试高频考察low数组更新逻辑；ML中可用于图核心结构提取
    def tarjan(u):
        timestamp[0] += 1
        dfn[u] = low[u] = timestamp[0]  # 初始化时间戳：当前节点发现时间=最早回溯时间
        stack.append(u)  # 节点入栈，记录遍历路径
        in_stack[u] = True  # 标记栈内状态，避免重复处理
        
        for v in graph[u]:  # 遍历所有出边
            if dfn[v] == 0:  # 未访问过的节点（树边），递归遍历
                tarjan(v)
                low[u] = min(low[u], low[v])  # 回溯更新low[u]：取子节点low最小值
            elif in_stack[v]:  # 已访问且在栈中（回边），属于当前强连通分量
                low[u] = min(low[u], dfn[v])  # 用子节点发现时间更新low[u]
            # 如果v已访问且不在栈中（弃边），无需处理
        
        # 找到强连通分量的根节点（low[u]=dfn[u]），出栈标记分量
        if dfn[u] == low[u]:
            scc_cnt[0] += 1  # 分量计数+1
            current_scc = []  # 当前SCC的节点列表
            
            while True:
                v = stack.pop()  # 弹出栈顶节点
                in_stack[v] = False  # 标记出栈
                scc_id[v] = scc_cnt[0]  # 记录节点所属分量ID
                current_scc.append(v)  # 添加到当前SCC
                if v == u:
                    break  # 直到当前节点u出栈，分量标记完成
            
            current_scc.sort()  # SCC内节点按编号排序
            scc_nodes.append(current_scc)  # 添加到SCC集合
    
    # 对所有未访问过的节点运行Tarjan算法
    # 图可能不连通，需要遍历所有节点作为起点
    for i in range(1, n + 1):
        if dfn[i] == 0:  # 如果节点i未被访问过
            tarjan(i)  # 从i开始运行Tarjan算法
    
    # 输出结果
    print(scc_cnt[0])  # 输出强连通分量的数量
    
    for scc in scc_nodes:  # 遍历每个强连通分量
        print(len(scc), end="")  # 输出该SCC的节点数
        for node in scc:  # 输出该SCC中的所有节点
            print(f" {node}", end="")
        print()  # 换行

solve()
```

---

## 时间/空间复杂度分析

### 时间复杂度
- **建图**：O(m)，遍历所有边
- **Tarjan算法**：O(n + m)，每个节点访问一次，每条边遍历一次
- **排序**：O(n log n)，对所有SCC内部排序
- **总时间复杂度**：O(n log n + m)

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
| JSK-456 | 缩点模板 | 入门 | SCC缩点应用 |
| JSK-789 | 受欢迎的牛 | 中等 | SCC+出度分析 |
| JSK-101 | 图的遍历 | 中等 | SCC+可达性 |

### 其他平台类似题
| 平台 | 题号 | 题目名称 | 说明 |
|------|------|----------|------|
| 洛谷 | P3387 | 【模板】缩点 | SCC缩点经典题 |
| POJ | 2186 | Popular Cows | SCC+缩点经典题 |
| HDU | 1269 | 迷宫城堡 | SCC模板题 |

### 笔试面试变种方向
1. **缩点后求DAG最长路**：在缩点后的DAG上DP
2. **求最少加多少边使图强连通**：统计入度出度为0的点
3. **求有多少节点可以被所有节点到达**：检查是否只有一个SCC

---

## ML/DL关联思考

### 1. 图结构数据预处理
在实际机器学习场景中，**SCC可用于图数据的降噪和简化**：

```python
def preprocess_graph_by_scc(graph, node_features):
    """
    使用SCC预处理图结构数据
    
    Args:
        graph: 邻接表表示的有向图
        node_features: 节点特征矩阵 [n_nodes, n_features]
    
    Returns:
        condensed_graph: 缩点后的DAG
        condensed_features: SCC聚合后的特征 [n_scc, n_features]
    """
    # 1. 求强连通分量
    scc_list, node_to_scc = tarjan_scc_with_mapping(graph)
    
    # 2. 构建缩点后的DAG
    condensed_graph = defaultdict(list)
    for u in graph:
        for v in graph[u]:
            scc_u, scc_v = node_to_scc[u], node_to_scc[v]
            if scc_u != scc_v and scc_v not in condensed_graph[scc_u]:
                condensed_graph[scc_u].append(scc_v)  # 添加DAG边，去重
    
    # 3. 特征聚合：SCC内节点特征取平均
    condensed_features = []
    for scc_nodes in scc_list:
        scc_feature = node_features[scc_nodes].mean(axis=0)  # 平均池化
        condensed_features.append(scc_feature)
    
    return condensed_graph, np.array(condensed_features)
```

**ML应用价值：**
- **降维**：将N个节点降至SCC数量，降低后续模型计算复杂度
- **降噪**：SCC内节点语义相似，合并可减少噪声影响
- **特征聚合**：SCC内部特征平均/最大池化，获得更鲁棒的表示

### 2. 图神经网络优化
在GNN训练中，**SCC可用于优化消息传递**：

```python
class SCCHierarchicalGNN(nn.Module):
    """基于SCC的层次化图神经网络"""
    
    def __init__(self, in_dim, hidden_dim, out_dim):
        super().__init__()
        # SCC内部聚合层
        self.intra_scc_conv = GCNConv(in_dim, hidden_dim)
        # SCC之间聚合层（在DAG上进行）
        self.inter_scc_conv = GATConv(hidden_dim, out_dim)
    
    def forward(self, x, edge_index):
        # 1. 求SCC
        scc_list, node_to_scc = self.compute_scc_with_mapping(edge_index)
        
        # 2. SCC内部消息传递（并行计算）
        intra_features = []
        for scc_nodes in scc_list:
            # 提取SCC内部子图
            scc_mask = torch.isin(torch.arange(x.size(0)), torch.tensor(scc_nodes))
            scc_x = x[scc_mask]
            scc_edge_index = self.extract_subgraph(edge_index, scc_mask)
            
            # 内部聚合
            scc_repr = self.intra_scc_conv(scc_x, scc_edge_index)
            intra_features.append(scc_repr.mean(dim=0))  # 聚合SCC内部表示
        
        # 3. SCC之间消息传递（在DAG上拓扑排序后计算）
        dag_edge_index = self.build_condensed_graph(scc_list, edge_index, node_to_scc)
        condensed_x = torch.stack(intra_features)
        out = self.inter_scc_conv(condensed_x, dag_edge_index)
        
        return out
```

**GNN优化价值：**
- **计算效率**：SCC内部并行计算，SCC之间按拓扑序计算，避免重复消息传递
- **层次化表示**：学习SCC内部微观特征和SCC之间宏观拓扑特征
- **长距离依赖**：缩点后DAG的直径更小，有利于捕获长距离依赖

### 3. 图嵌入与表示学习
**SCC可作为图级特征**：

```python
def extract_scc_based_features(graph):
    """提取基于SCC的图级特征"""
    # 1. 求SCC
    scc_list = tarjan_scc(graph)
    
    # 2. 提取SCC相关特征
    features = {
        # SCC数量比例
        'scc_ratio': len(scc_list) / len(graph.nodes()),
        
        # 最大SCC大小
        'max_scc_size': max(len(scc) for scc in scc_list) if scc_list else 0,
        
        # SCC大小分布的熵（衡量图的结构复杂度）
        'scc_size_entropy': compute_entropy([len(scc) for scc in scc_list]),
        
        # 缩点后DAG的直径
        'condensed_dag_diameter': compute_dag_diameter(scc_list, graph),
        
        # 环的数量估计
        'cycle_count_estimate': len(graph.nodes()) - len(scc_list)
    }
    
    return features
```

**应用价值：**
- **图分类**：SCC特征可作为图分类任务的手工特征
- **异常检测**：环数量突增可能表示异常（如金融交易中的洗钱网络）
- **图生成**：SCC分布可作为图生成模型的约束条件

---

## 总结

本题是**强连通分量的模板题**，核心掌握点：

1. **Tarjan算法原理**：dfn/low数组含义、栈的作用、如何判断SCC根
2. **代码实现细节**：递归/迭代两种实现、数组大小设置、边界处理
3. **复杂度分析**：时间O(V+E)、空间O(V+E)，面试高频考点
4. **ML/DL应用**：图简化、GNN优化、特征工程

**笔试建议**：直接背诵模板，5分钟内写完。
**面试建议**：能清晰解释low数组更新逻辑、为什么用dfn[v]而非low[v]更新。