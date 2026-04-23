# PTA 甲级 1013 Battle Over Cities - 强连通分量与缩点

## 题目信息
- **平台**: PTA (浙江大学程序设计自动评测系统)
- **题号**: 甲级 1013
- **标题**: Battle Over Cities
- **难度**: 中等
- **时间限制**: 400ms
- **内存限制**: 64MB

## 题目链接
- https://pintia.cn/problem-sets/994805342720868352/problems/994805500414115840

## 题目描述
一场战争正在N个城市之间进行。每个城市由一条道路与其他一些城市相连。为了使两个城市之间没有路径，占领军决定摧毁一些道路。然而，他们需要确保被摧毁的道路总数不超过K条，并且他们希望最大化被分成至少两个独立组件的城市数量。

对于每个城市，计算如果该城市被占领（即该城市及其所有相连道路被移除），图会变成多少个连通分量。

## 输入格式
每个输入文件包含一个测试用例。

第一行给出四个整数：N（城市数量，2 ≤ N ≤ 1000）、M（道路数量，≤ 10000）、K（需要查询的城市数量，≤ N）。

接下来M行，每行给出一个道路连接的两个城市编号（城市编号从1到N）。

最后一行给出K个需要查询的城市编号。

## 输出格式
对于每个查询的城市，输出一行表示该城市被占领后，图会变成多少个连通分量。

## 样例输入
```
3 2 3
1 2
1 3
1 2 3
```

## 样例输出
```
1
1
0
```

## 笔试/面试考察点分析

### 核心考察点
1. **无向图连通分量计算**：本题看似关于强连通分量，但实际考察无向图的连通性，可用于对比学习
2. **图遍历算法应用**：DFS/BFS计算连通分量数量
3. **节点删除对连通性的影响**：分析移除一个节点后图的连通性变化
4. **时间复杂度优化**：对于K个查询，需要高效处理

### 面试高频提问
1. **本题与强连通分量的区别**？
   - 本题是无向图连通分量，强连通分量是有向图特有概念
   - 无向图连通只需存在路径，有向图强连通需要双向路径
   
2. **如何优化多个查询的效率**？
   - 朴素做法：每次删除节点后DFS，O(K × (N+M))
   - 优化思路：预处理割点（使用Tarjan算法），本题中删除割点会增加连通分量数

3. **割点与连通分量的关系**？
   - 割点（Articulation Point）是删除后会增加连通分量数量的节点
   - 可用Tarjan算法的dfn/low数组判断割点

## 解题思路

### 方法一：朴素DFS/BFS（易于理解）
1. 对于每个查询城市，标记该城市为"已删除"
2. 对剩余图进行DFS/BFS，统计连通分量数量
3. 时间复杂度：O(K × (N+M))

### 方法二：割点优化（面试加分项）
1. 使用Tarjan算法预处理所有割点
2. 对于非割点，删除后连通分量数不变（或减1，取决于具体定义）
3. 对于割点，删除后连通分量数 = 原分量数 + 该割点分割的子树数量
4. 时间复杂度：O(N+M+K)

### 笔试面试答题逻辑
**笔试场景**：写朴素DFS版本，确保正确性优先
**面试口述**：先讲朴素做法，再讲割点优化，展示深度思考

## 完整代码实现

```cpp
#include <iostream>
#include <vector>
#include <cstring>
using namespace std;

const int MAXN = 1005; // 最大节点数，根据题目N ≤ 1000设定，笔试面试需根据数据范围调整

vector<int> adj[MAXN]; // 邻接表存储无向图，ML中图数据常用存储结构
bool visited[MAXN];    // DFS标记数组，记录节点是否被访问过
bool deleted[MAXN];    // 标记被删除的节点（查询城市）

/**
 * DFS遍历计算连通分量
 * @param u 当前遍历的节点编号
 * 笔试面试要点：DFS是图遍历基础，必须熟练手写
 * ML关联：DFS遍历顺序可作为图神经网络的节点采样策略
 */
void dfs(int u) {
    visited[u] = true; // 标记当前节点已访问，避免重复遍历
    for (int v : adj[u]) { // 遍历当前节点的所有邻居节点
        if (!visited[v] && !deleted[v]) { // 邻居未被访问且未被删除
            dfs(v); // 递归访问邻居节点，深入遍历连通块
        }
    }
}

/**
 * 计算图中连通分量数量（排除被删除节点）
 * @return 连通分量数量
 * 笔试常考：连通分量计数是图论基础问题
 * ML关联：连通分量数量是图的重要拓扑特征，可作为图分类的特征输入
 */
int countComponents(int n) {
    memset(visited, false, sizeof(visited)); // 重置访问标记数组，每次查询前清空
    int components = 0; // 连通分量计数器，初始为0
    
    for (int i = 1; i <= n; i++) { // 遍历所有节点，从1开始编号（题目规定）
        // 条件判断：节点未被访问、未被删除（不是当前查询城市）
        if (!visited[i] && !deleted[i]) {
            components++; // 发现新的连通分量，计数+1
            dfs(i); // 从该节点开始DFS，标记整个连通块
        }
    }
    return components; // 返回连通分量总数
}

int main() {
    int n, m, k; // n:城市数 m:道路数 k:查询数
    cin >> n >> m >> k; // 读取三个整数
    
    // 读取M条道路，构建无向图邻接表
    for (int i = 0; i < m; i++) {
        int u, v; // 道路连接的两个城市编号
        cin >> u >> v; // 读取两个城市编号
        adj[u].push_back(v); // 无向图：u到v添加边
        adj[v].push_back(u); // 无向图：v到u添加边（双向）
    }
    
    // 处理K个查询
    for (int i = 0; i < k; i++) {
        int city; // 当前查询的城市编号
        cin >> city; // 读取查询城市
        
        memset(deleted, false, sizeof(deleted)); // 重置删除标记数组
        deleted[city] = true; // 标记当前城市为已删除（被占领）
        
        // 计算并输出删除该城市后的连通分量数
        cout << countComponents(n) - 1; // 减1是因为不计入被删除的城市本身
        
        if (i < k - 1) cout << endl; // 除最后一行外，每行输出后换行
    }
    
    return 0; // 程序正常结束
}
```

## Python实现

```python
import sys
from collections import defaultdict

# 设置递归深度，防止大规模图遍历栈溢出，笔试面试需特别注意
sys.setrecursionlimit(10000)

def dfs(u, adj, visited, deleted):
    """
    DFS遍历连通块
    :param u: 当前节点
    :param adj: 邻接表
    :param visited: 访问标记数组
    :param deleted: 删除标记数组
    ML关联：DFS遍历树可作为图神经网络的计算图结构
    """
    visited[u] = True  # 标记当前节点已访问
    for v in adj[u]:  # 遍历所有邻居
        # 邻居未访问且未被删除，递归访问
        if not visited[v] and not deleted[v]:
            dfs(v, adj, visited, deleted)

def count_components(n, adj, deleted):
    """
    统计连通分量数量
    :return: 连通分量数
    笔试要点：这是标准的连通分量计数模板
    """
    visited = [False] * (n + 1)  # 初始化访问标记数组，索引从1开始
    components = 0  # 连通分量计数器
    
    for i in range(1, n + 1):  # 遍历所有节点
        if not visited[i] and not deleted[i]:  # 未访问且未删除
            components += 1  # 发现新连通分量
            dfs(i, adj, visited, deleted)  # DFS遍历整个连通块
    
    return components

def main():
    # 读取输入
    n, m, k = map(int, input().split())  # 城市数、道路数、查询数
    
    # 构建邻接表，使用defaultdict方便动态添加
    adj = defaultdict(list)
    
    # 读取M条边，构建无向图
    for _ in range(m):
        u, v = map(int, input().split())
        adj[u].append(v)  # u到v的边
        adj[v].append(u)  # v到u的边（无向图双向）
    
    # 读取K个查询城市
    queries = list(map(int, input().split()))
    
    # 处理每个查询
    results = []
    for city in queries:
        deleted = [False] * (n + 1)  # 重置删除标记
        deleted[city] = True  # 标记当前城市被删除
        
        # 计算连通分量数并记录结果
        comp = count_components(n, adj, deleted)
        results.append(comp - 1)  # 减1去除被删除城市本身
    
    # 输出所有结果
    print('\n'.join(map(str, results)))

if __name__ == "__main__":
    main()
```

## 时间/空间复杂度分析

### 时间复杂度
- **朴素DFS做法**：O(K × (N+M))
  - 每次查询需要遍历全图：O(N+M)
  - K次查询：O(K × (N+M))
  - 本题数据范围N≤1000, M≤10000, K≤N，可以接受

- **割点优化做法**：O(N+M+K)
  - Tarjan预处理割点：O(N+M)
  - 每次查询O(1)回答
  - 适合M、N更大的场景

### 空间复杂度
- **邻接表存储**：O(N+M)
- **访问标记数组**：O(N)
- **总空间复杂度**：O(N+M)

## 同类题目拓展

### PTA同平台
- **甲级 1013 Battle Over Cities** - 本题，无向图连通分量
- **甲级 1003 Emergency** - 带权图最短路+连通性
- **乙级 1013 数素数** - 与图论无关，数值处理

### 其他平台同类题
- **洛谷 P2860 [USACO06JAN]冗余路径Redundant Paths** - 边双连通分量
- **洛谷 P3388 【模板】割点（割顶）** - 割点模板题
- **LeetCode 1192 查找集群中的关键连接** - 桥（关键边）
- **POJ 1523 SPF** - 割点统计

## 面试变种方向
1. **删除一条边而不是一个点**：求桥（Bridge）
2. **有向图版本**：删除一个点后求强连通分量数
3. **带权图版本**：删除点后的最大连通块大小

## ML/DL关联思考

### 1. 连通分量作为图特征
- **应用场景**：图分类任务中，连通分量数量是重要的拓扑特征
- **实现方式**：将图的连通分量数、最大连通分量大小作为节点/图级别的特征输入GNN
- **价值**：反映图的稀疏性和模块化程度

### 2. 节点重要性评估
- **核心思想**：删除后增加连通分量数多的节点更重要（类似介数中心性）
- **ML应用**：节点重要性预测、关键节点识别
- **DL结合**：可用GNN学习节点删除对图连通性的影响

### 3. 图简化与采样
- **方法**：基于连通分量的图粗化（Graph Coarsening）
- **目的**：降低大规模图神经网络的计算复杂度
- **实现**：将每个连通分量收缩为一个超节点，构建层次化图结构

### 4. 对比学习视角
- **强连通分量 vs 连通分量**：有向图与无向图的核心区别
- **面试考点**：理解两者的定义差异和应用场景
- **ML价值**：不同类型的连通性模式对应不同的语义含义
