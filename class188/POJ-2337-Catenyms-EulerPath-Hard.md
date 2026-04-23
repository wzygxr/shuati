# 【北大POJ】-2337-Catenyms-欧拉路径-困难

## 题目原始链接
http://poj.org/problem?id=2337

## 题目完整描述
给出 N 个由小写字母组成的单词，你的任务是将它们排成一行，使得相邻单词首尾字母相同（即第 i 个单词的末尾字母与第 i+1 个单词的首字母相同）。输出字典序最小的方案。

例如，单词 "dog", "visit", "pig", "tickle" 可以排成 "visit-tickle-dog-pig"。

### 输入输出格式
- 输入：第一行包含测试用例数量T，每个测试用例第一行包含整数N，接下来N行每行一个单词
- 输出：如果无法排列，输出 "***"，否则输出字典序最小的排列

### 数据范围
- 1 <= N <= 1000
- 每个单词长度不超过20

### 样例输入输出
```
输入：
2
3
auc
bud
ccob
4
ccof
dal
aabb
be

输出：
ccob_bud_auc
aabb_be_ccof_dal
```

## 笔试/面试考察点分析
- **考察点1**：欧拉路径判定（有向图中最多一个起点出度比入度多1，最多一个终点入度比出度多1）
- **考察点2**：Hierholzer算法实现（迭代版本，处理字典序）
- **考察点3**：字符串处理和图的建模（将单词转换为边）
- **考察点4**：连通性检查（弱连通性检查）
- **考察点5**：复杂度分析（时间O(E log E)，空间O(V+E)）

## 解题思路
这是一个经典的单词接龙问题，本质是有向图欧拉路径问题：

1. 将每个单词看作一条有向边，从单词首字母指向末字母
2. 统计每个字母节点的入度和出度
3. 检查欧拉路径存在条件
4. 确定起点（如果有度数不平衡的节点，从出度比入度多1的节点开始）
5. 使用Hierholzer算法找欧拉路径，注意要按字典序处理
6. 连通性检查确保所有边都在同一连通分量中

## 完整代码实现

```cpp
#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <stack>
#include <queue>
#include <set>
using namespace std;

struct Word {
    string s;
    char start, end;
    int id;
    
    bool operator<(const Word& other) const {  // 按字典序排序，面试需说明比较函数的重要性
        return s < other.s;
    }
};

int n;
vector<Word> words;
vector<vector<int>> adj;  // 邻接表存储边的索引
vector<int> in_deg(26, 0), out_deg(26, 0);  // 入度和出度
vector<bool> vis(26, false);  // 访问标记，用于连通性检查

// DFS检查连通性
void dfs(int u) {
    vis[u] = true;  // 标记当前节点已访问
    for (int v = 0; v < 26; v++) {
        if (adj[u][v] > 0 || adj[v][u] > 0) {  // 如果存在边
            if (!vis[v]) {
                dfs(v);  // 递归访问
            }
        }
    }
}

int main() {
    int T;
    cin >> T;
    
    while (T--) {
        cin >> n;
        words.resize(n);
        
        // 重置度数数组
        fill(in_deg.begin(), in_deg.end(), 0);
        fill(out_deg.begin(), in_deg.end(), 0);
        fill(vis.begin(), vis.end(), false);
        
        for (int i = 0; i < n; i++) {
            cin >> words[i].s;
            words[i].start = words[i].s[0] - 'a';  // 转换为索引
            words[i].end = words[i].s.back() - 'a';  // 单词末字母索引
            words[i].id = i;
            out_deg[words[i].start]++;  // 更新出度
            in_deg[words[i].end]++;     // 更新入度
        }
        
        // 检查欧拉路径存在条件 - 有向图欧拉路径判定定理，面试必考
        int start_nodes = 0, end_nodes = 0;
        int start_node = -1;
        
        for (int i = 0; i < 26; i++) {
            int diff = out_deg[i] - in_deg[i];
            if (diff == 1) {  // 出度比入度多1，可能是起点
                start_nodes++;
                start_node = i;
            } else if (diff == -1) {  // 入度比出度多1，可能是终点
                end_nodes++;
            } else if (diff != 0) {  // 度数差不是-1, 0, 1，不满足条件
                start_nodes = end_nodes = 2;  // 强制使条件不成立
                break;
            }
        }
        
        // 检查度数条件
        if (!((start_nodes == 0 && end_nodes == 0) || (start_nodes == 1 && end_nodes == 1))) {
            cout << "***" << endl;
            continue;
        }
        
        // 构建邻接表，按字典序存储边
        adj.assign(26, vector<int>(26, 0));
        sort(words.begin(), words.end());  // 按字典序排序单词
        
        for (int i = 0; i < n; i++) {
            adj[words[i].start][words[i].end]++;  // 添加边
        }
        
        // 检查连通性
        int start_char = -1;
        for (int i = 0; i < 26; i++) {
            if (out_deg[i] > 0 || in_deg[i] > 0) {  // 如果有度数
                start_char = i;
                break;
            }
        }
        
        if (start_char != -1) {
            dfs(start_char);  // 从任意有度数的节点开始DFS
        }
        
        bool connected = true;
        for (int i = 0; i < 26; i++) {
            if ((in_deg[i] > 0 || out_deg[i] > 0) && !vis[i]) {  // 有度数但未访问
                connected = false;
                break;
            }
        }
        
        if (!connected) {
            cout << "***" << endl;
            continue;
        }
        
        // 确定起点
        int start = start_node;
        if (start == -1) {
            for (int i = 0; i < 26; i++) {
                if (out_deg[i] > 0) {  // 从任意有出度的节点开始（欧拉回路）
                    start = i;
                    break;
                }
            }
        }
        
        // 使用Hierholzer算法构造路径
        stack<int> path_st;
        vector<int> path;
        path_st.push(start);
        
        while (!path_st.empty()) {
            int u = path_st.top();
            bool has_next = false;
            
            for (int v = 0; v < 26; v++) {
                if (adj[u][v] > 0) {  // 如果有边
                    adj[u][v]--;
                    path_st.push(v);
                    has_next = true;
                    break;
                }
            }
            
            if (!has_next) {  // 没有未访问的边
                path.push_back(path_st.top());
                path_st.pop();
            }
        }
        
        reverse(path.begin(), path.end());  // 反转路径
        
        // 构造结果字符串
        string result = "";
        for (int i = 0; i < path.size() - 1; i++) {
            // 找到对应的单词
            char from = path[i] + 'a';
            char to = path[i+1] + 'a';
            
            // 在剩余单词中找到匹配的单词
            for (int j = 0; j < n; j++) {
                if (words[j].s != "" && words[j].start == from && words[j].end == to) {
                    if (result.empty()) {
                        result += words[j].s;
                    } else {
                        result += "_" + words[j].s;  // 用下划线连接
                    }
                    words[j].s = "";  // 标记为已使用
                    break;
                }
            }
        }
        
        cout << result << endl;
    }
    
    return 0;
}
```

## 代码逐行注释
- `struct Word { string s; char start, end; int id; }` - 定义单词结构体，包含字符串、首尾字母和ID，便于处理
- `fill(in_deg.begin(), in_deg.end(), 0)` - 初始化入度数组，笔试需注意初始化细节
- `int diff = out_deg[i] - in_deg[i]` - 计算度数差，欧拉路径判定的核心操作
- `if (!((start_nodes == 0 && end_nodes == 0) || (start_nodes == 1 && end_nodes == 1)))` - 检查欧拉路径存在条件，面试必考知识点
- `sort(words.begin(), words.end())` - 按字典序排序，确保结果字典序最小，面试需说明排序的重要性
- **ML/DL关联**：该算法将字符串序列转化为图结构，可用于自然语言处理中的序列建模

## 时间/空间复杂度分析
- **时间复杂度**：O(E log E)，其中E为单词数量。排序时间为O(E log E)，Hierholzer算法为O(E)
- **空间复杂度**：O(V + E)，V为字母表大小(26)，E为单词数量，用于存储图结构
- **面试高频提问**：为什么时间复杂度是O(E log E)？因为需要按字典序处理边，必须先排序

## 同类题目拓展
- **类似题目1**：LeetCode 332 - Reconstruct Itinerary - 机场路径问题
- **类似题目2**：洛谷 P1341 - 无序字母对 - 无向图欧拉路径问题
- **变种方向1**：如果要求输出字典序最大的路径，如何修改算法？
- **变种方向2**：如果单词可以重复使用，问题如何变化？

## ML/DL关联思考
- 该算法将字符串序列转化为图结构，可用于自然语言处理中的序列建模
- 在图神经网络中，单词接龙问题的建模方式可应用于文本关系抽取
- 欧拉路径的全局遍历特性有助于捕获序列的全局依赖关系，改进RNN/Transformer模型
- 可以将单词接龙的求解过程视为一种序列到序列的转换，应用于机器翻译等任务