# 牛客网 NC567 - 公司内推关系 - 强连通分量与缩点

## 题目链接
https://www.nowcoder.com/practice/...

## 题目描述
在一家公司中，员工之间存在内推关系，可以形成一个有向图。每个员工只能内推一个人，但可以被多人内推。现在想知道，最少需要联系多少个员工，才能通过内推关系触达到所有员工？

这个问题本质上是在求有向图中，最少需要选择多少个起始点，才能从这些点出发遍历到图中的所有节点。这可以通过求强连通分量并缩点后，统计入度为0的节点数来解决。

### 输入格式
- 第一行：两个整数 n, m (1 ≤ n ≤ 10⁴, 0 ≤ m ≤ 10⁵) - 员工数和内推关系数
- 接下来 m 行：每行两个整数 u, v (1 ≤ u, v ≤ n) - 表示员工 u 内推了员工 v

### 输出格式
- 一行：一个整数 - 最少需要联系的员工数

### 输入输出样例

**Input**
```
6 7
1 2
2 3
3 1
3 4
4 5
5 6
6 4
```

**Output**
```
1
```

---

## 笔试/面试考察点分析

### 核心考察点
1. **问题转化**：将实际问题转化为图论中的最小起始点问题
2. **强连通分量识别**：在有向图中找出所有SCC
3. **缩点后DAG处理**：将SCC缩为单点，统计入度为0的点数
4. **图论应用**：理解缩点后DAG的性质

### 常见坑点
1. **重边处理**：缩点后可能产生重边，需要去重
2. **自环处理**：自环也是一个SCC
3. **连通性**：图可能不连通，需要考虑所有连通分量

### 面试口述要点
```
"这个问题的本质是：在有向图中找到最少的起始点，
使得从这些点出发可以到达所有节点。
解法是：
1. 使用Tarjan算法求出所有强连通分量；
2. 将每个SCC缩为单个节点，形成DAG；
3. 统计DAG中入度为0的节点数，这就是答案。
因为DAG中入度为0的节点无法从其他节点到达，
所以必须作为起始点。"
```

---

## 解题思路

### 步骤1：问题转化
将"最少联系员工数"问题转化为图论问题：在有向图中找最少的起始点使所有节点可达。

### 步骤2：求强连通分量
使用Tarjan算法找出图中所有强连通分量。

### 步骤3：缩点构建DAG
将每个SCC缩为单个节点，构建缩点后的DAG。

### 步骤4：统计入度为0的节点数
在缩点后的DAG中，统计入度为0的节点数量。

---

## 完整代码实现

### C++ 实现

```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 1e4 + 5;  // 最大节点数，笔试按数据范围调整，面试需说明取值依据
const int MAXM = 1e5 + 5;  // 最大边数，笔试按数据范围调整

int n, m;  // n:员工数, m:内推关系数

// 邻接表存图
vector<int> adj[MAXN];  // 原始有向图邻接表，ML中图数据常用邻接表存储

// Tarjan算法相关数组
int dfn[MAXN];    // DFS序（发现时间戳），笔试按数据范围调整
int low[MAXN];    // 能回溯到的最早祖先的dfn值，核心数组
int timestamp = 0;  // 时间戳计数器

bool in_stack[MAXN];  // 标记节点是否在栈中，判断环的关键
stack<int> st;  // Tarjan算法：存储当前遍历路径节点

int scc_id[MAXN];  // 节点所属的强连通分量编号
int scc_cnt = 0;   // 强连通分量总数

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
    }
    
    // 找到强连通分量的根节点（low[u]=dfn[u]），出栈标记分量
    if (dfn[u] == low[u]) {
        scc_cnt++;  // 分量计数+1
        int v;
        
        do {
            v = st.top();  // 弹出栈顶节点
            st.pop();
            in_stack[v] = false;  // 标记出栈
            scc_id[v] = scc_cnt;  // 记录节点所属分量ID
        } while (v != u);  // 直到当前节点u出栈，分量标记完成
    }
}

int main() {
    ios::sync_with_stdio(false);  // 关闭同步，加速IO
    cin.tie(nullptr);
    
    cin >> n >> m;  // 读取员工数和内推关系数
    
    // 读取内推关系并建图
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);  // 添加从u到v的内推关系边
    }
    
    // 对所有未访问过的节点运行Tarjan算法
    for (int i = 1; i <= n; i++) {
        if (!dfn[i]) {  // 如果节点i未被访问过
            tarjan(i);  // 从i开始运行Tarjan算法
        }
    }
    
    // 构建缩点后的DAG并统计入度
    vector<bool> has_incoming(scc_cnt + 1, false);  // 标记SCC是否有入边
    set<pair<int, int>> edge_set;  // 用集合去重，避免重边影响入度计算
    
    // 遍历原图的所有边，构建缩点后的DAG
    for (int u = 1; u <= n; u++) {
        for (int v : adj[u]) {
            int scc_u = scc_id[u];  // u所属的SCC
            int scc_v = scc_id[v];  // v所属的SCC
            
            if (scc_u != scc_v) {  // 不同SCC之间才有边（避免自环）
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
    return 0;
}
```

### Java 实现

```java
import java.io.*;
import java.util.*;

public class NC567 {
    static final int MAXN = 10005;  // 最大节点数，笔试按数据范围调整，面试需说明取值依据
    
    static int n, m;  // n:员工数, m:内推关系数
    
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
        }
        
        // 找到强连通分量的根节点（low[u]=dfn[u]），出栈标记分量
        if (dfn[u] == low[u]) {
            sccCnt++;  // 分量计数+1
            int v;
            
            do {
                v = stack.pop();  // 弹出栈顶节点
                inStack[v] = false;  // 标记出栈
                sccId[v] = sccCnt;  // 记录节点所属分量ID
            } while (v != u);  // 直到当前节点u出栈，分量标记完成
        }
    }
    
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {
        FastReader in = new FastReader();
        PrintWriter out = new PrintWriter(System.out);
        
        n = in.nextInt();  // 读取员工数
        m = in.nextInt();  // 读取内推关系数
        
        // 初始化邻接表
        for (int i = 1; i <= n; i++) {
            adj[i] = new ArrayList<>();
        }
        
        // 读取内推关系并建图
        for (int i = 0; i < m; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            adj[u].add(v);  // 添加从u到v的内推关系边
        }
        
        // 对所有未访问过的节点运行Tarjan算法
        for (int i = 1; i <= n; i++) {
            if (dfn[i] == 0) {  // 如果节点i未被访问过
                tarjan(i);  // 从i开始运行Tarjan算法
            }
        }
        
        // 构建缩点后的DAG并统计入度
        boolean[] hasIncoming = new boolean[sccCnt + 1];  // 标记SCC是否有入边
        Set<String> edgeSet = new HashSet<>();  // 用集合去重，避免重边影响入度计算
        
        // 遍历原图的所有边，构建缩点后的DAG
        for (int u = 1; u <= n; u++) {
            for (int v : adj[u]) {
                int sccU = sccId[u];  // u所属的SCC
                int sccV = sccId[v];  // v所属的SCC
                
                if (sccU != sccV) {  // 不同SCC之间才有边（避免自环）
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
        graph[u].append(v)  # 添加从u到v的内推关系边
    
    # Tarjan算法相关变量
    dfn = [0] * (n + 1)  # DFS序（发现时间戳），笔试按数据范围调整
    low = [0] * (n + 1)  # 能回溯到的最早祖先的dfn值，核心数组
    timestamp = [0]  # 使用列表包装，实现引用传递
    
    in_stack = [False] * (n + 1)  # 标记节点是否在栈中，判断环的关键
    stack = []  # Tarjan算法：存储当前遍历路径节点
    
    scc_id = [0] * (n + 1)  # 节点所属的强连通分量编号
    scc_cnt = [0]  # 强连通分量总数，使用列表实现引用传递
    
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
        
        # 找到强连通分量的根节点（low[u]=dfn[u]），出栈标记分量
        if dfn[u] == low[u]:
            scc_cnt[0] += 1  # 分量计数+1
            
            while True:
                v = stack.pop()  # 弹出栈顶节点
                in_stack[v] = False  # 标记出栈
                scc_id[v] = scc_cnt[0]  # 记录节点所属分量ID
                if v == u:
                    break  # 直到当前节点u出栈，分量标记完成
    
    # 对所有未访问过的节点运行Tarjan算法
    for i in range(1, n + 1):
        if dfn[i] == 0:  # 如果节点i未被访问过
            tarjan(i)  # 从i开始运行Tarjan算法
    
    # 构建缩点后的DAG并统计入度
    has_incoming = [False] * (scc_cnt[0] + 1)  # 标记SCC是否有入边
    edge_set = set()  # 用集合去重，避免重边影响入度计算
    
    # 遍历原图的所有边，构建缩点后的DAG
    for u in range(1, n + 1):
        for v in graph[u]:
            scc_u = scc_id[u]  # u所属的SCC
            scc_v = scc_id[v]  # v所属的SCC
            
            if scc_u != scc_v:  # 不同SCC之间才有边（避免自环）
                edge_set.add((scc_u, scc_v))  # 添加缩点后的边
    
    # 标记有入边的SCC
    for scc_u, scc_v in edge_set:
        has_incoming[scc_v] = True  # 目标SCC有入边
    
    # 统计入度为0的SCC数量
    ans = 0
    for i in range(1, scc_cnt[0] + 1):
        if not has_incoming[i]:
            ans += 1  # 入度为0的SCC需要手动触发
    
    print(ans)  # 输出答案

solve()
```

---

## 时间/空间复杂度分析

### 时间复杂度
- **建图**：O(m)，遍历所有边
- **Tarjan算法**：O(n + m)，每个节点访问一次，每条边遍历一次
- **缩点处理**：O(n + m)，遍历所有边构建缩点后DAG
- **统计入度**：O(SCC数量)，最多O(n)
- **总时间复杂度**：O(n + m)

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
| NC123 | 朋友圈 | 中等 | 并查集/SCC |
| NC456 | 社交网络 | 中等 | SCC应用 |
| NC789 | 传播感染 | 中等 | SCC+拓扑排序 |

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

### 1. 社交网络分析
在实际机器学习场景中，**公司内推关系类似于社交网络中的影响传播**：

```python
def analyze_company_referral_network(graph, employee_attributes):
    """
    分析公司内推网络的结构特征
    
    Args:
        graph: 邻接表表示的内推关系图
        employee_attributes: 员工属性字典
    
    Returns:
        network_insights: 网络洞察报告
    """
    # 1. 检测强连通分量（紧密工作小组）
    scc_list = tarjan_scc(graph)
    
    # 2. 构建缩点后的DAG（组织层级结构）
    condensed_graph, node_to_scc = build_condensed_dag(graph, scc_list)
    
    # 3. 分析网络特征
    insights = {
        # 内推网络的凝聚性（SCC数量与节点数的比例）
        'network_cohesion': len(scc_list) / len(graph.nodes()),
        
        # 信息传播效率（DAG深度）
        'information_flow_efficiency': calculate_dag_depth(condensed_graph),
        
        # 关键传播节点（入度为0的SCC）
        'key_referral_groups': count_zero_indegree_scc(condensed_graph),
        
        # 影响力集中度（最大SCC占比）
        'influence_concentration': max(len(scc) for scc in scc_list) / len(graph.nodes()) if scc_list else 0
    }
    
    return insights
```

**ML应用价值：**
- **组织分析**：识别公司内部的紧密合作小组
- **人才招聘**：发现高效的内推渠道
- **知识管理**：优化信息在组织内的传播路径

### 2. 推荐系统优化
在推荐系统中，**SCC可用于改进用户-物品图的推荐效果**：

```python
class SCCBasedRecommender:
    """基于SCC的推荐系统"""
    
    def __init__(self, user_item_graph):
        self.graph = user_item_graph
        # 1. 检测用户群组（基于共同兴趣的SCC）
        self.user_sccs = self.detect_user_communities()
        
        # 2. 构建用户群组-物品图
        self.group_item_graph = self.build_group_item_graph()
    
    def detect_user_communities(self):
        """检测用户社区（基于行为相似性）"""
        # 使用修改版的Tarjan算法检测用户交互图中的SCC
        scc_list = tarjan_scc(self.graph)
        return scc_list
    
    def build_group_item_graph(self):
        """构建群组-物品交互图"""
        group_item_graph = defaultdict(set)
        
        for scc_id, users in enumerate(self.user_sccs):
            for user in users:
                # 获取用户交互的物品
                for item in self.graph[user]:
                    group_item_graph[scc_id].add(item)
        
        return group_item_graph
    
    def recommend_for_user(self, user_id):
        """为特定用户生成推荐"""
        # 找到用户所属的SCC
        user_scc = None
        for scc_id, users in enumerate(self.user_sccs):
            if user_id in users:
                user_scc = scc_id
                break
        
        if user_scc is not None:
            # 获取该SCC交互过的物品
            scc_items = self.group_item_graph[user_scc]
            # 过滤掉用户已交互的物品
            recommendations = scc_items - set(self.graph[user_id])
            return list(recommendations)
        
        return []
```

**推荐系统优化价值：**
- **冷启动问题**：新用户可基于其所属SCC进行推荐
- **多样性提升**：利用SCC内的多样化兴趣
- **计算效率**：群组级别的推荐比个体推荐更高效

### 3. 企业知识图谱
**SCC在构建企业知识图谱中的应用**：

```python
def construct_knowledge_graph_with_scc(entities, relationships):
    """基于SCC构建企业知识图谱"""
    # 1. 构建实体关系图
    graph = defaultdict(list)
    for rel in relationships:
        graph[rel['source']].append(rel['target'])
    
    # 2. 检测紧密相关的实体群组（SCC）
    entity_sccs = tarjan_scc(graph)
    
    # 3. 构建分层知识图谱
    knowledge_graph = {
        'entities': entities,
        'relationships': relationships,
        'entity_groups': []  # 实体群组信息
    }
    
    for scc in entity_sccs:
        group_info = {
            'members': scc,
            'semantic_type': infer_semantic_type(scc),  # 推断语义类型
            'centrality': calculate_group_centrality(scc, graph),  # 中心性
            'cohesion': calculate_group_cohesion(scc, graph)  # 凝聚性
        }
        knowledge_graph['entity_groups'].append(group_info)
    
    return knowledge_graph
```

**应用价值：**
- **知识组织**：将相关实体聚类，便于管理和检索
- **智能问答**：基于实体群组提供更准确的答案
- **决策支持**：识别关键实体群组及其相互关系

---

## 总结

本题是**强连通分量与缩点的实际应用题**，核心掌握点：

1. **问题转化**：将实际场景转化为图论问题
2. **SCC识别**：使用Tarjan算法找到所有强连通分量
3. **缩点思想**：将SCC缩为单个节点，形成DAG
4. **入度分析**：统计DAG中入度为0的节点数
5. **复杂度分析**：时间O(V+E)、空间O(V+E)，面试高频考点
6. **ML/DL应用**：社交网络分析、推荐系统、知识图谱

**笔试建议**：熟练掌握缩点后DAG的构建过程，注意重边处理。
**面试建议**：能清晰解释为什么答案等于缩点后入度为0的节点数。