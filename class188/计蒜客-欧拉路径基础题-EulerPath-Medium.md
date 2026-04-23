# 【计蒜客】-欧拉路径基础题-欧拉路径-中等

## 题目原始链接
https://www.jisuanke.com/problem/...

## 题目完整描述
给定一个无向图，包含 n 个节点和 m 条边，判断是否存在欧拉路径或欧拉回路。如果存在，输出路径；如果不存在，输出 "No Solution"。

欧拉路径是指经过图中每条边恰好一次的路径，欧拉回路是起点和终点相同的欧拉路径。

### 输入输出格式
- 输入：第一行包含两个整数 n 和 m，接下来 m 行每行两个整数 u 和 v，表示节点 u 和 v 之间有一条无向边
- 输出：如果存在欧拉路径或回路，输出路径上的节点序列；否则输出 "No Solution"

### 数据范围
- 1 <= n <= 1000
- 0 <= m <= 2000

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
1 2 3 4 1 3

输入：
4 4
1 2
2 3
3 4
4 1

输出：
1 2 3 4 1
```

## 笔试/面试考察点分析
- **考察点1**：欧拉路径判定（无向图中奇度数节点数为0或2）
- **考察点2**：Hierholzer算法实现（递归与迭代两种方式）
- **考察点3**：图的表示方法（邻接表 vs 邻接矩阵）
- **考察点4**：连通性检查（确保图是连通的）
- **考察点5**：复杂度分析（时间O(E)，空间O(V+E)）

## 解题思路
这是一个基础的无向图欧拉路径问题，需要分步解决：

1. 构建图的邻接表表示
2. 统计每个节点的度数
3. 检查欧拉路径存在条件（奇度数节点数为0或2）
4. 确定起点（如果有奇度数节点，从奇度数节点开始；否则从任意非零度节点开始）
5. 使用Hierholzer算法构造欧拉路径

## 完整代码实现

```cpp
#include <iostream>
#include <vector>
#include <stack>
#include <algorithm>
#include <cstring>
using namespace std;

const int MAXN = 1005;  // 最大节点数，根据数据范围设置
vector<vector<int>> adj(MAXN);  // 邻接表存储图，ML中图数据常用邻接表存储
int deg[MAXN];  // 度数数组，无向欧拉路径判定核心
bool vis_edge[MAXN][MAXN];  // 标记边是否被访问（避免重复遍历），Hierholzer算法关键
vector<int> euler_path;  // 存储最终构造的欧拉路径/回路，ML中可作为图的序列特征输出
int n, m;  // 节点数和边数

// 无向图欧拉路径判定函数：返回是否存在欧拉路径/回路，及起点（-1表示不存在）
// 笔试中该判定是基础，需熟练掌握；ML中可用于图数据的预处理筛选（仅处理存在欧拉路径的图）
int judge_undirected() {
    int cnt_odd = 0;  // 统计奇数度节点数量，无向欧拉路径判定核心指标
    int start = 1;  // 默认起点为1号节点
    
    for (int i = 1; i <= n; i++) {
        if (deg[i] % 2 != 0) {  // 度数为奇数
            cnt_odd++;
            start = i;  // 奇数度节点作为起点（欧拉路径）
        }
    }
    
    // 无向图欧拉路径判定定理：奇数度节点数为0（回路）或2（路径），且图连通
    if (cnt_odd != 0 && cnt_odd != 2) {
        return -1;  // 不存在欧拉路径/回路
    }
    return start;  // 返回起点（存在的情况下）
}

// Hierholzer算法（无向图）：递归构造欧拉路径/回路
// 笔试中该算法是欧拉路径构造的核心，需快速手写；ML中可用于将图转化为有序序列特征
void hierholzer_undirected(int u) {
    // 遍历当前节点的所有邻接边
    for (int i = 0; i < adj[u].size(); i++) {
        int v = adj[u][i];
        if (!vis_edge[u][v]) {  // 该边未被访问
            vis_edge[u][v] = vis_edge[v][u] = true;  // 标记边为已访问（无向图需标记双向）
            hierholzer_undirected(v);  // 递归遍历邻接节点
            // 回溯时将当前节点加入路径（后序遍历，最终需反转路径），面试需说明回溯逻辑
            euler_path.push_back(u);
        }
    }
}

// 检查连通性，确保图是连通的
bool is_connected() {
    vector<bool> visited(n + 1, false);
    stack<int> st;
    
    // 从任意有度数的节点开始DFS
    int start_node = -1;
    for (int i = 1; i <= n; i++) {
        if (deg[i] > 0) {
            start_node = i;
            break;
        }
    }
    
    if (start_node == -1) return true;  // 没有边的图认为是连通的
    
    st.push(start_node);
    visited[start_node] = true;
    
    while (!st.empty()) {
        int u = st.top(); st.pop();
        for (int v : adj[u]) {
            if (!visited[v]) {
                visited[v] = true;
                st.push(v);
            }
        }
    }
    
    // 检查是否所有有度数的节点都被访问
    for (int i = 1; i <= n; i++) {
        if (deg[i] > 0 && !visited[i]) {
            return false;  // 存在未访问的节点，图不连通
        }
    }
    return true;
}

int main() {
    cin >> n >> m;  // 读入节点数和边数，笔试需注意输入格式正确性
    
    // 初始化边访问标记，笔试需注意初始化细节
    memset(vis_edge, false, sizeof(vis_edge));
    
    // 读入边并构建图
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);  // 添加无向边
        adj[v].push_back(u);
        deg[u]++;  // 更新度数
        deg[v]++;
    }
    
    // 检查连通性
    if (!is_connected()) {
        cout << "No Solution" << endl;
        return 0;
    }
    
    int start = judge_undirected();  // 判定无向图欧拉路径是否存在
    if (start == -1) {
        cout << "No Solution" << endl;
        return 0;
    }
    
    hierholzer_undirected(start);  // 构造欧拉路径
    euler_path.push_back(start);  // 添加起点，形成完整路径
    
    // 输出欧拉路径，笔试中需按题目要求格式输出，ML中可作为图的序列特征使用
    for (int i = euler_path.size() - 1; i >= 0; i--) {  // 从后往前输出，因为递归版是后序遍历
        cout << euler_path[i];
        if (i > 0) cout << " ";
    }
    cout << endl;
    
    return 0;
}
```

## 代码逐行注释
- `vector<vector<int>> adj(MAXN)` - 使用邻接表存储图结构，空间复杂度O(V+E)，面试需说明邻接表的优势
- `int cnt_odd = 0` - 统计奇度数节点数量，无向图欧拉路径判定核心指标
- `if (cnt_odd != 0 && cnt_odd != 2)` - 无向图欧拉路径判定定理的核心应用，面试必考
- `void hierholzer_undirected(int u)` - Hierholzer算法核心实现，面试重点算法
- `euler_path.push_back(u)` - 在回溯时将节点加入路径，这是Hierholzer算法的关键，面试需解释原因
- **ML/DL关联**：该算法将图结构转化为序列特征，可用于图神经网络的节点序列化输入

## 时间/空间复杂度分析
- **时间复杂度**：O(E)，其中E为边数。每个边只会被访问一次，Hierholzer算法的时间复杂度为O(E)
- **空间复杂度**：O(V + E)，V为节点数，E为边数，用于存储图结构和递归栈空间
- **面试高频提问**：为什么时间复杂度是O(E)而不是O(V+E)？因为每条边只访问一次，与节点数无关

## 同类题目拓展
- **类似题目1**：洛谷 P2731 - 骑马修栅栏 - 无向图欧拉路径模板题
- **类似题目2**：POJ 1041 - John's Trip - 带权无向图欧拉回路
- **变种方向1**：如果是有向图的欧拉路径，判定条件如何变化？
- **变种方向2**：如果要求输出字典序最小的欧拉路径，如何修改算法？

## ML/DL关联思考
- 该算法本质上是将图结构数据转化为序列特征，适配RNN/Transformer等序列模型输入
- 在图神经网络中，欧拉路径的遍历顺序可以作为一种节点聚合顺序，影响信息传播
- 欧拉路径的全局遍历特性有助于捕获图的全局特征，弥补GNN局部聚合的不足
- 可以将欧拉路径的构造过程视为图嵌入的一种方法，用于下游机器学习任务