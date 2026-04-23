# 【HackerRank】-Eulerian Path In Directed Graph-欧拉路径-中等

## 题目原始链接
https://www.hackerrank.com/challenges/eulerian-path-in-directed-graph/

## 题目完整描述
给定一个有向图，判断是否存在欧拉路径。如果存在，输出任意一条欧拉路径。

欧拉路径是指经过图中每条边恰好一次的路径。

### 输入格式
第一行包含两个整数 N 和 M，分别表示节点数和边数。
接下来 M 行，每行包含两个整数 u 和 v，表示存在一条从 u 到 v 的有向边。

### 输出格式
- 如果存在欧拉路径，输出 "YES"，并在下一行输出路径中的节点序列
- 如果不存在，输出 "NO"

### 数据范围
- 1 ≤ N ≤ 1000
- 0 ≤ M ≤ 2000
- 1 ≤ u, v ≤ N

### 样例输入输出
```
输入：
4 5
1 2
2 3
3 4
4 1
1 3

输出：
YES
1 2 3 4 1 3
```

```
输入：
3 3
1 2
2 3
3 1

输出：
YES
1 2 3 1
```

## 笔试/面试考察点分析
- **考察点1**：有向图欧拉路径判定定理的准确应用
- **考察点2**：Hierholzer算法的实现与优化
- **考察点3**：图的连通性判断（弱连通性）
- **考察点4**：入度出度统计与度数平衡分析
- **考察点5**：算法的边界条件处理（孤立节点、自环、重边）

## 解题思路
这是一个标准的有向图欧拉路径问题：

1. 首先统计每个节点的入度和出度
2. 根据欧拉路径判定定理检查是否存在欧拉路径：
   - 恰好有一个节点出度比入度多1（起点）
   - 恰好有一个节点入度比出度多1（终点）
   - 其余节点入度等于出度
   - 或者所有节点入度等于出度（欧拉回路）
3. 检查图的弱连通性（忽略边的方向）
4. 如果满足条件，使用Hierholzer算法构造路径

## 完整代码实现

```cpp
#include <iostream>
#include <vector>
#include <stack>
#include <algorithm>
#include <queue>
using namespace std;

const int MAXN = 1005;  // 最大节点数，根据题目数据范围设定

int in_degree[MAXN];  // 入度数组，有向图欧拉路径判定的必要数据
int out_degree[MAXN];  // 出度数组，有向图欧拉路径判定的必要数据
vector<vector<int>> adj(MAXN);  // 邻接表存储图，面试需说明邻接表vs邻接矩阵的优缺点
vector<vector<int>> temp_adj(MAXN);  // 临时邻接表，用于Hierholzer算法，避免破坏原图
bool visited[MAXN];  // 访问标记数组，用于连通性判断

// DFS判断图的弱连通性
// 笔试中需掌握连通性判断方法，面试常考弱连通性概念
void dfs_connectivity(int u, vector<vector<int>>& graph) {
    visited[u] = true;  // 标记当前节点为已访问
    for (int v : graph[u]) {  // 遍历当前节点的所有邻接节点
        if (!visited[v]) {  // 如果邻接节点未被访问
            dfs_connectivity(v, graph);  // 递归访问邻接节点
        }
    }
}

// Hierholzer算法构造欧拉路径（迭代版）
// 笔试面试高频考点：递归vs迭代实现，避免栈溢出
vector<int> hierholzer(int start) {
    stack<int> path;  // 用栈模拟递归过程
    vector<int> circuit;  // 存储欧拉路径
    path.push(start);  // 将起点压入栈
    int current = start;  // 当前节点
    
    while (!path.empty()) {  // 当栈不为空时继续
        if (!temp_adj[current].empty()) {  // 如果当前节点还有未访问的边
            path.push(current);  // 将当前节点压入栈
            int next = temp_adj[current].back();  // 获取下一个节点
            temp_adj[current].pop_back();  // 删除已访问的边
            current = next;  // 移动到下一个节点
        } else {  // 如果当前节点没有未访问的边
            circuit.push_back(current);  // 将当前节点加入路径
            current = path.top();  // 回溯到栈顶节点
            path.pop();  // 弹出栈顶节点
        }
    }
    
    reverse(circuit.begin(), circuit.end());  // 反转路径得到正确顺序
    return circuit;  // 返回欧拉路径
}

int main() {
    ios_base::sync_with_stdio(false);  // 优化输入输出速度
    cin.tie(NULL);
    
    int N, M;  // N为节点数，M为边数
    cin >> N >> M;  // 读取节点数和边数
    
    // 初始化度数数组
    fill(in_degree, in_degree + MAXN, 0);
    fill(out_degree, out_degree + MAXN, 0);
    fill(visited, visited + MAXN, false);
    
    // 读取边并统计度数
    for (int i = 0; i < M; i++) {  // 遍历所有边
        int u, v;  // 边的起点和终点
        cin >> u >> v;  // 读取边的端点
        adj[u].push_back(v);  // 在邻接表中添加边
        out_degree[u]++;  // 增加起点出度
        in_degree[v]++;  // 增加终点入度
    }
    
    // 构建无向图用于连通性判断
    // 面试高频考点：弱连通性的判断方法
    vector<vector<int>> undirected_graph(N + 1);
    for (int i = 1; i <= N; i++) {  // 遍历所有节点
        for (int j : adj[i]) {  // 遍历邻接节点
            undirected_graph[i].push_back(j);  // 添加双向边
            undirected_graph[j].push_back(i);
        }
    }
    
    // 找到一个有边的节点作为连通性检查的起点
    int start_node = -1;  // 连通性检查的起始节点
    for (int i = 1; i <= N; i++) {  // 寻找起始节点
        if (out_degree[i] > 0 || in_degree[i] > 0) {  // 如果节点有边
            start_node = i;  // 设为起始节点
            break;
        }
    }
    
    // 如果图为空，直接输出YES（空路径）
    if (start_node == -1) {  // 如果没有找到起始节点
        cout << "YES" << endl;  // 空图被认为有欧拉路径
        cout << 1 << endl;  // 输出任意节点
        return 0;
    }
    
    // 检查图的连通性
    dfs_connectivity(start_node, undirected_graph);  // 从起始节点开始DFS
    
    // 验证所有有边的节点都被访问了
    for (int i = 1; i <= N; i++) {  // 检查所有节点
        if ((out_degree[i] > 0 || in_degree[i] > 0) && !visited[i]) {  // 如果有边但未访问
            cout << "NO" << endl;  // 图不连通，无欧拉路径
            return 0;
        }
    }
    
    // 检查欧拉路径的度数条件
    // 有向图欧拉路径判定定理的核心应用
    int start_count = 0, end_count = 0;  // 起点和终点计数
    int start_vertex = -1, end_vertex = -1;  // 记录起点和终点
    
    for (int i = 1; i <= N; i++) {  // 遍历所有节点
        int diff = out_degree[i] - in_degree[i];  // 计算度数差值
        
        if (diff == 1) {  // 出度比入度多1，可能是起点
            start_count++;  // 增加起点计数
            start_vertex = i;  // 记录起点
        } else if (diff == -1) {  // 入度比出度多1，可能是终点
            end_count++;  // 增加终点计数
            end_vertex = i;  // 记录终点
        } else if (diff != 0) {  // 度数差不是-1, 0, 1
            cout << "NO" << endl;  // 不满足欧拉路径条件
            return 0;
        }
    }
    
    // 验证度数条件
    if (!((start_count == 0 && end_count == 0) || (start_count == 1 && end_count == 1))) {
        cout << "NO" << endl;  // 度数条件不满足
        return 0;
    }
    
    // 确定欧拉路径的起点
    int euler_start;  // 欧拉路径的起点
    if (start_count == 1) {  // 如果有明确的起点
        euler_start = start_vertex;  // 使用找到的起点
    } else {  // 否则是欧拉回路，任选一个有出度的节点
        euler_start = start_node;  // 使用连通性检查的起始节点
    }
    
    // 复制邻接表用于Hierholzer算法
    temp_adj = adj;  // 复制原邻接表，避免修改原图
    
    // 使用Hierholzer算法构造欧拉路径
    vector<int> path = hierholzer(euler_start);  // 构造欧拉路径
    
    cout << "YES" << endl;  // 输出有解
    for (int i = 0; i < path.size(); i++) {  // 输出路径
        cout << path[i];  // 输出当前节点
        if (i < path.size() - 1) cout << " ";  // 添加空格分隔
    }
    cout << endl;  // 换行
    
    return 0;  // 程序正常结束
}
```

## 代码逐行注释
- `int in_degree[MAXN]; int out_degree[MAXN];` - 入度和出度数组，有向图欧拉路径判定的核心数据结构
- `vector<vector<int>> adj(MAXN);` - 邻接表存储图，空间效率O(V+E)，面试需说明存储方式选择
- `void dfs_connectivity(int u, vector<vector<int>>& graph)` - 检查图的弱连通性，忽略边方向，笔试重点
- `int diff = out_degree[i] - in_degree[i];` - 计算度数差值，有向图欧拉路径判定定理的关键
- `if (!((start_count == 0 && end_count == 0) || (start_count == 1 && end_count == 1)))` - 应用欧拉路径判定定理
- `vector<int> hierholzer(int start)` - Hierholzer算法实现，面试高频考点：递归vs迭代
- `reverse(circuit.begin(), circuit.end());` - 反转路径得到正确顺序，算法关键步骤
- **ML/DL关联**：该算法可将有向图转换为序列，适用于RNN等序列模型的输入

## 时间/空间复杂度分析
- **时间复杂度**：O(M)，其中M为边数。度数统计O(M)，连通性检查O(N+M)，Hierholzer算法O(M)，总体为O(M)
- **空间复杂度**：O(N+M)，用于存储邻接表、度数数组和访问标记数组
- **面试高频提问**：为什么时间复杂度是O(M)而不是O(N²)？因为使用邻接表存储，每条边只被访问常数次

## 同类题目拓展
- **类似题目1**：LeetCode 332 - Reconstruct Itinerary - 有向图欧拉路径应用
- **类似题目2**：POJ 1041 - John's Trip - 无向图欧拉回路问题
- **变种方向1**：如果要求输出字典序最小的欧拉路径，如何修改算法？
- **变种方向2**：如果图不连通，如何找到所有连通分量中的欧拉路径？

## ML/DL关联思考
- 该算法将图结构转换为序列，可作为RNN或Transformer的输入
- 在图神经网络中，欧拉路径提供了一种节点访问顺序，有助于信息传播
- 欧拉路径的存在性可作为图连通性和平衡性的特征，用于图分类任务
- 在知识图谱中，欧拉路径可用于遍历所有关系的路径规划