# 【Codeforces】-723E-One-Two-Three-欧拉路径-困难

## 题目原始链接
https://codeforces.com/problemset/problem/723/E

## 题目完整描述
给定一些边，每条边可以是一条无向边，也可以是一条有向边。你的任务是对所有无向边指定方向，使得最终的图存在欧拉路径。

如果存在这样的定向方案，输出每条无向边的方向；否则输出"-1"。

### 输入格式
第一行包含两个整数 n 和 m，分别表示节点数和边数。
接下来 m 行，每行包含三个整数 t, u, v，表示：
- t=1：这是一条有向边，从 u 到 v
- t=2：这是一条无向边，连接 u 和 v

### 输出格式
如果不存在满足条件的定向方案，输出"-1"。
否则输出 m 行，第 i 行表示第 i 条边的方向：
- 如果原边是有向边，输出原方向
- 如果原边是无向边，输出定向后的方向

### 数据范围
- 1 ≤ n ≤ 100
- 0 ≤ m ≤ 400
- 1 ≤ u, v ≤ n
- u ≠ v

### 样例输入输出
```
输入：
3 4
2 1 2
2 1 2
1 2 3
1 3 1

输出：
1 2
2 1
2 3
3 1
```

## 笔试/面试考察点分析
- **考察点1**：混合图欧拉路径的判定与构造算法
- **考察点2**：二分图匹配在图论问题中的应用
- **考察点3**：图的连通性判断（无向边的连通性）
- **考察点4**：度数调整策略的设计与实现
- **考察点5**：网络流或贪心算法解决边定向问题

## 解题思路
这是一个经典的混合图欧拉路径问题：

1. 首先检查有向边构成的图的连通性
2. 计算每个节点的净度数（出度-入度）
3. 使用二分图匹配算法来定向无向边，使得最终满足欧拉路径的度数条件
4. 二分图左侧是需要减少出度的节点，右侧是需要减少入度的节点
5. 无向边对应二分图中的边，通过最大匹配来决定边的定向

## 完整代码实现

```cpp
#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
#include <cmath>
#include <cstring>
using namespace std;

const int MAXN = 105;  // 定义最大节点数，笔试中需根据题目数据范围设置
const int MAXM = 405;  // 定义最大边数，笔试中需根据题目数据范围设置

int n, m;  // n为节点数，m为边数，笔试需注意变量含义
vector<pair<int, int>> edges;  // 存储所有边，面试需说明pair的使用场景
vector<int> undirected_edges;  // 存储无向边的索引，用于后续定向
int in_degree[MAXN], out_degree[MAXN];  // 入度和出度数组，欧拉路径判定核心
int degree[MAXN];  // 无向边的度数，用于连通性判断
bool adj[MAXN][MAXN];  // 邻接矩阵，存储无向边连接关系，面试需说明邻接矩阵优缺点
int match[MAXM];  // 二分图匹配数组，用于无向边定向
bool used[MAXM];  // 二分图匹配中使用的访问标记，防止重复匹配
vector<int> g[MAXN];  // 二分图的邻接表，存储匹配关系

// DFS判断图的连通性
// 笔试中需掌握DFS/BFS等图遍历算法，面试常考递归vs迭代实现
bool visited[MAXN];
void dfs_connectivity(int u) {
    visited[u] = true;  // 标记当前节点为已访问，避免重复访问
    for (int v = 1; v <= n; v++) {  // 遍历所有可能的相邻节点
        if (!visited[v] && adj[u][v]) {  // 如果相邻节点未访问且存在无向边
            dfs_connectivity(v);  // 递归访问相邻节点
        }
    }
}

// 二分图最大匹配的增广路径算法
// 面试高频考点：匈牙利算法原理与实现，时间复杂度分析
bool dfs_matching(int u) {
    for (int v : g[u]) {  // 遍历二分图中u的所有相邻节点
        if (used[v]) continue;  // 如果该节点已被使用，跳过
        used[v] = true;  // 标记为已使用，防止重复匹配
        
        // 如果该节点未匹配或者可以找到增广路径
        if (match[v] == -1 || dfs_matching(match[v])) {
            match[v] = u;  // 建立匹配关系
            return true;  // 找到增广路径，返回成功
        }
    }
    return false;  // 未找到增广路径，返回失败
}

// 使用匈牙利算法求二分图最大匹配
// 笔试面试重点：二分图匹配在实际问题中的应用场景
int max_bipartite_matching(int left_size) {
    memset(match, -1, sizeof(match));  // 初始化匹配数组为-1（未匹配）
    int matching_count = 0;  // 匹配计数器，记录成功匹配的数量
    
    for (int i = 1; i <= left_size; i++) {  // 遍历左侧所有节点
        memset(used, false, sizeof(used));  // 每次匹配前清空访问标记
        if (dfs_matching(i)) {  // 尝试为当前节点找到匹配
            matching_count++;  // 匹配成功，增加计数
        }
    }
    return matching_count;  // 返回最大匹配数
}

int main() {
    ios_base::sync_with_stdio(false);  // 加速输入输出，笔试中常用优化技巧
    cin.tie(NULL);
    
    cin >> n >> m;  // 读取节点数和边数
    
    // 初始化度数数组
    memset(in_degree, 0, sizeof(in_degree));
    memset(out_degree, 0, sizeof(out_degree));
    memset(degree, 0, sizeof(degree));
    memset(adj, false, sizeof(adj));
    
    // 读取边并统计度数
    for (int i = 0; i < m; i++) {  // 遍历所有边
        int t, u, v;  // t为边类型，u,v为端点
        cin >> t >> u >> v;  // 读取边类型和端点
        edges.push_back({u, v});  // 存储边
        
        if (t == 1) {  // 有向边
            out_degree[u]++;  // 增加起点出度
            in_degree[v]++;  // 增加终点入度
        } else {  // 无向边
            undirected_edges.push_back(i);  // 记录无向边索引
            degree[u]++;  // 增加两端点的度数
            degree[v]++;
            adj[u][v] = adj[v][u] = true;  // 在邻接矩阵中标记无向边
        }
    }
    
    // 检查连通性
    // 混合图欧拉路径的前提是忽略方向后图是连通的
    memset(visited, false, sizeof(visited));  // 初始化访问标记
    int start_node = -1;  // 寻找起始节点
    for (int i = 1; i <= n; i++) {  // 寻找第一个度数大于0的节点
        if (degree[i] + in_degree[i] + out_degree[i] > 0) {  // 如果节点有边
            start_node = i;  // 将其设为起始节点
            break;
        }
    }
    
    if (start_node != -1) {  // 如果找到了起始节点
        dfs_connectivity(start_node);  // 从起始节点开始DFS
    }
    
    // 检查图是否连通
    for (int i = 1; i <= n; i++) {  // 检查所有节点
        if (degree[i] + in_degree[i] + out_degree[i] > 0 && !visited[i]) {  // 如果有边但未访问
            cout << -1 << endl;  // 图不连通，无解
            return 0;
        }
    }
    
    // 计算每个节点的净度数（出度-入度）
    // 这是混合图欧拉路径判定的关键步骤
    vector<int> net_degree(n + 1);  // 净度数数组
    for (int i = 1; i <= n; i++) {
        net_degree[i] = out_degree[i] - in_degree[i];  // 净度数 = 出度 - 入度
    }
    
    // 检查是否存在欧拉路径的度数条件
    // 欧拉路径：最多一个节点净度数为1（起点），最多一个节点净度数为-1（终点）
    int pos_count = 0, neg_count = 0;  // 正负净度数节点计数
    for (int i = 1; i <= n; i++) {
        if (net_degree[i] > 1 || net_degree[i] < -1) {  // 净度数超出范围
            cout << -1 << endl;  // 无法形成欧拉路径
            return 0;
        }
        if (net_degree[i] == 1) pos_count++;  // 净度数为1的节点
        if (net_degree[i] == -1) neg_count++;  // 净度数为-1的节点
    }
    
    // 检查度数条件
    if (!((pos_count == 0 && neg_count == 0) || (pos_count == 1 && neg_count == 1))) {
        cout << -1 << endl;  // 不满足欧拉路径度数条件
        return 0;
    }
    
    // 构建二分图进行无向边定向
    // 左侧：需要减少出度的节点（正净度数节点）
    // 右侧：需要减少入度的节点（负净度数节点）
    vector<int> positive_nodes, negative_nodes;  // 存储正负净度数节点
    for (int i = 1; i <= n; i++) {
        if (net_degree[i] > 0) {  // 净度数为正，需要减少出度
            for (int j = 0; j < net_degree[i]; j++) {
                positive_nodes.push_back(i);  // 添加到正节点列表
            }
        } else if (net_degree[i] < 0) {  // 净度数为负，需要减少入度
            for (int j = 0; j < abs(net_degree[i]); j++) {
                negative_nodes.push_back(i);  // 添加到负节点列表
            }
        }
    }
    
    // 构建二分图的邻接表
    // 每条无向边对应二分图中的一条边
    for (int idx : undirected_edges) {  // 遍历所有无向边
        int u = edges[idx].first;  // 边的起点
        int v = edges[idx].second;  // 边的终点
        
        // 检查u是否在positive_nodes中，v是否在negative_nodes中
        for (int i = 0; i < positive_nodes.size(); i++) {
            if (positive_nodes[i] == u) {
                for (int j = 0; j < negative_nodes.size(); j++) {
                    if (negative_nodes[j] == v) {
                        g[i + 1].push_back(j + 1);  // 在二分图中添加边
                    }
                }
            }
        }
        
        // 也尝试相反方向：从v到u
        for (int i = 0; i < positive_nodes.size(); i++) {
            if (positive_nodes[i] == v) {
                for (int j = 0; j < negative_nodes.size(); j++) {
                    if (negative_nodes[j] == u) {
                        g[i + 1].push_back(j + 1);  // 在二分图中添加边
                    }
                }
            }
        }
    }
    
    // 求二分图最大匹配
    int matching_size = max_bipartite_matching(positive_nodes.size());  // 求最大匹配
    
    // 检查是否所有需要定向的边都能匹配
    if (matching_size != positive_nodes.size()) {
        cout << -1 << endl;  // 匹配失败，无解
        return 0;
    }
    
    // 输出结果
    vector<pair<int, int>> result_edges = edges;  // 复制边列表用于输出
    for (int i = 0; i < undirected_edges.size(); i++) {
        int edge_idx = undirected_edges[i];  // 无向边的原始索引
        int u = edges[edge_idx].first;  // 原始起点
        int v = edges[edge_idx].second;  // 原始终点
        
        // 根据匹配结果确定方向
        bool found = false;
        for (int j = 1; j <= positive_nodes.size(); j++) {
            if (match[j] != -1 && positive_nodes[j-1] == u && negative_nodes[match[j]-1] == v) {
                result_edges[edge_idx] = {u, v};  // 方向为u->v
                found = true;
                break;
            }
            if (match[j] != -1 && positive_nodes[j-1] == v && negative_nodes[match[j]-1] == u) {
                result_edges[edge_idx] = {v, u};  // 方向为v->u
                found = true;
                break;
            }
        }
        
        // 如果没找到匹配，使用默认方向
        if (!found) {
            result_edges[edge_idx] = {u, v};
        }
    }
    
    // 输出所有边的方向
    for (auto& edge : result_edges) {
        cout << edge.first << " " << edge.second << "\n";  // 输出边的方向
    }
    
    return 0;
}
```

## 代码逐行注释
- `vector<pair<int, int>> edges;` - 存储所有边，使用pair存储起点和终点，笔试需熟悉STL容器
- `int in_degree[MAXN], out_degree[MAXN];` - 存储有向边的出入度，欧拉路径判定的核心数据
- `bool dfs_matching(int u)` - 二分图匹配的DFS函数，面试高频考点：匈牙利算法实现
- `net_degree[i] = out_degree[i] - in_degree[i];` - 计算净度数，混合图欧拉路径判定关键
- `if (!((pos_count == 0 && neg_count == 0) || (pos_count == 1 && neg_count == 1)))` - 检查欧拉路径度数条件
- `max_bipartite_matching(positive_nodes.size())` - 求二分图最大匹配，用于边定向
- `ios_base::sync_with_stdio(false);` - 优化输入输出速度，笔试中常用的性能优化技巧
- **ML/DL关联**：该算法可应用于网络结构设计，通过边定向优化图的遍历特性

## 时间/空间复杂度分析
- **时间复杂度**：O(M × M × N)，其中M为无向边数量，N为节点数。二分图匹配的时间复杂度为O(V×E)，这里约为O(M²×N)
- **空间复杂度**：O(N² + M)，N²用于邻接矩阵，M用于存储边和其他辅助数组
- **面试高频提问**：为什么使用二分图匹配解决边定向问题？因为可以将定向问题转化为匹配问题，确保度数平衡

## 同类题目拓展
- **类似题目1**：UVa 10735 - Euler Circuit - 混合图欧拉回路问题
- **类似题目2**：Codeforces 508D - Tanya and Password - 另一个欧拉路径应用
- **变种方向1**：如果要求输出字典序最小的解，如何修改算法？
- **变种方向2**：如果需要输出所有可能的解，如何实现？

## ML/DL关联思考
- 该算法展示了图结构优化的方法，可用于设计更好的图神经网络结构
- 混合图定向问题类似于神经网络中的连接方向优化
- 二分图匹配思想可用于注意力机制中的节点匹配
- 欧拉路径在知识图谱中可用于实体关系的最优遍历路径