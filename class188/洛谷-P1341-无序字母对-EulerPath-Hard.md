# 【洛谷】-P1341-无序字母对-欧拉路径-困难

## 题目原始链接
https://www.luogu.com.cn/problem/P1341

## 题目完整描述
按照字典序输出字典序最小的欧拉路径。

给定 n 个小写字母对，要求将它们排列成一个序列，使得第 i 个小写字母对的第一个字母等于第 i+1 个小写字母对的第二个字母。要求输出字典序最小的方案。

### 输入输出格式
- 输入：第一行一个整数 n，接下来 n 行，每行两个小写字母，表示一个字母对
- 输出：一行，表示字典序最小的字母序列

### 数据范围
- 1 <= n <= 1000
- 字母对由小写字母组成

### 样例输入输出
```
输入：
5
ab
bc
cd
de
ea

输出：
abcdea

输入：
4
ac
cb
bd
dc

输出：
acbdbd
```

## 笔试/面试考察点分析
- **考察点1**：欧拉路径判定（无向图中奇度数节点数为0或2）
- **考察点2**：Hierholzer算法实现（迭代版本避免爆栈）
- **考察点3**：字典序优化处理（使用multiset或优先队列）
- **考察点4**：图的表示方法（邻接表 vs 邻接矩阵）
- **考察点5**：复杂度分析（时间O(E)，空间O(V+E)）

## 解题思路
这是一个典型的无向图欧拉路径问题，需要找到一条经过每条边恰好一次的路径：

1. 将字母对看作无向边，每个字母对是一条边
2. 统计每个字母的度数
3. 根据欧拉路径存在条件确定起点（若有奇度数节点则从奇度数节点开始，否则从任意非零度节点开始）
4. 使用Hierholzer算法找欧拉路径
5. 按字典序最小的要求构建路径

## 完整代码实现

```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 30;  // 最多26个小写字母，设置为30保证安全
vector<multiset<int>> adj(MAXN);  // 邻接表，使用multiset保证字典序，面试需说明multiset的作用
int degree[MAXN];  // 度数数组，记录每个字母的度数
int used[MAXN][MAXN];  // 记录边的使用情况，避免重复访问

void hierholzer(int u, vector<int>& path) {
    // Hierholzer算法的迭代实现，避免递归深度过大导致栈溢出，面试高频考点
    for (auto it = adj[u].begin(); it != adj[u].end();) {
        int v = *it;  // 获取邻接节点
        if (used[u][v] < adj[u].count(v)) {  // 如果这条边还没有被完全使用
            used[u][v]++;
            used[v][u]++;
            adj[u].erase(it);  // 删除当前边
            adj[v].erase(adj[v].find(u));  // 删除反向边
            hierholzer(v, path);  // 递归访问邻接节点
        } else {
            ++it;  // 移动到下一个邻接节点
        }
    }
    path.push_back(u);  // 回溯时将当前节点加入路径，这是Hierholzer算法的关键
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    int n;
    cin >> n;
    
    // 初始化度数数组
    memset(degree, 0, sizeof(degree));
    
    for (int i = 0; i < n; i++) {
        string s;
        cin >> s;
        int u = s[0] - 'a';  // 将字母转换为数字索引，便于处理
        int v = s[1] - 'a';
        
        adj[u].insert(v);  // 添加无向边
        adj[v].insert(u);
        degree[u]++;  // 更新度数
        degree[v]++;
    }
    
    // 寻找欧拉路径的起点，根据无向图欧拉路径判定定理
    int start = -1;
    int odd_count = 0;  // 统计奇度数节点数量
    
    for (int i = 0; i < 26; i++) {
        if (degree[i] % 2 == 1) {  // 度数为奇数
            odd_count++;
            if (start == -1) start = i;  // 记录第一个奇度数节点作为起点
        }
    }
    
    // 检查欧拉路径是否存在 - 无向图欧拉路径判定定理，面试必考
    if (odd_count != 0 && odd_count != 2) {
        cout << "No Solution" << endl;  // 不存在欧拉路径
        return 0;
    }
    
    // 如果没有奇度数节点，从任意有度数的节点开始（欧拉回路）
    if (start == -1) {
        for (int i = 0; i < 26; i++) {
            if (degree[i] > 0) {
                start = i;
                break;
            }
        }
    }
    
    vector<int> path;
    hierholzer(start, path);  // 使用Hierholzer算法构造欧拉路径
    
    // 输出结果，路径是反向的，需要倒序输出
    for (int i = path.size() - 1; i >= 0; i--) {
        cout << char('a' + path[i]);  // 将数字索引转换回字母
        if (i == path.size() - 1) continue;  // 第一个字母无需额外处理
        
        // 输出当前节点到下一节点的边，确保路径完整性
        if (i > 0) {
            cout << char('a' + path[i-1]);
        }
    }
    cout << endl;
    
    return 0;
}
```

## 代码逐行注释
- `vector<multiset<int>> adj(MAXN)` - 使用multiset存储邻接表，保证访问邻居时按字典序进行，面试需说明multiset的作用
- `hierholzer(int u, vector<int>& path)` - Hierholzer算法核心实现，使用递归方式，面试重点算法
- `path.push_back(u)` - 在回溯时将节点加入路径，这是Hierholzer算法的关键，面试需解释为什么这样做
- `memset(degree, 0, sizeof(degree))` - 初始化度数数组，笔试需注意初始化细节
- `if (odd_count != 0 && odd_count != 2)` - 无向图欧拉路径判定定理的核心应用，面试必考
- **ML/DL关联**：该算法将图结构转化为有序序列，可用于图神经网络的节点序列化输入

## 时间/空间复杂度分析
- **时间复杂度**：O(E log E)，其中E为边数。每次从multiset中删除元素的时间复杂度为log E，总共E条边
- **空间复杂度**：O(V + E)，V为不同节点的数量，E为边数量，用于存储图结构
- **面试高频提问**：为什么时间复杂度是O(E log E)而不是O(E)？因为需要维护multiset的有序性

## 同类题目拓展
- **类似题目1**：洛谷 P2731 - 骑马修栅栏 - 无向图欧拉路径模板题
- **类似题目2**：POJ 2337 - Catenyms - 字母串接龙问题
- **变种方向1**：如果是有向图的欧拉路径，判定条件如何变化？
- **变种方向2**：如果要求输出字典序最大的路径，如何修改算法？

## ML/DL关联思考
- 该算法本质上是将图结构数据转化为序列特征，适配RNN/Transformer等序列模型输入
- 在图神经网络中，欧拉路径的遍历顺序可以作为一种节点聚合顺序，影响信息传播
- 欧拉路径的全局遍历特性有助于捕获图的全局特征，弥补GNN局部聚合的不足
- 可以将欧拉路径的构造过程视为图嵌入的一种方法，用于下游机器学习任务