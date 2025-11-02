#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <unordered_map>
#include <set>
#include <stack>
using namespace std;

/**
 * 树上莫队算法实现 - 树上路径查询问题
 * 
 * 题目描述：
 * 给定一棵树，每个节点有一个权值。多次查询两个节点之间的路径上有多少种不同的权值。
 * 
 * 解题思路：
 * 1. 树上莫队通过欧拉序或DFS序将树结构转换为线性结构
 * 2. 使用时间戳标记每个节点的进入和离开时间
 * 3. 将树上的路径查询转换为线性数组的区间查询
 * 4. 应用莫队算法处理这些区间查询
 * 
 * 时间复杂度分析：
 * - 树上莫队的时间复杂度为 O(n * sqrt(n))，其中 n 是树的节点数
 * 
 * 空间复杂度分析：
 * - 存储树的邻接表、欧拉序等需要 O(n) 的空间
 * - 其他辅助数组需要 O(n) 的空间
 * - 总体空间复杂度为 O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：处理树为空或查询无效的情况
 * 2. 性能优化：合理选择块的大小，使用奇偶排序优化
 * 3. 代码可读性：清晰的变量命名和详细的注释
 */

// 用于存储查询的结构
struct Query {
    int l;      // 查询的左边界（欧拉序中的位置）
    int r;      // 查询的右边界（欧拉序中的位置）
    int lca;    // 两个节点的最近公共祖先
    int idx;    // 查询的索引，用于输出答案时保持顺序
    int block;  // 查询所属的块
    
    Query(int l, int r, int lca, int idx, int blockSize) 
        : l(l), r(r), lca(lca), idx(idx), block(l / blockSize) {}
};

// 全局变量
vector<vector<int>> tree;      // 树的邻接表
vector<int> values;            // 原始权值数组
vector<int> discreteValues;    // 离散化后的权值数组
int valueRange;                // 离散化后的值域范围
vector<int> euler;             // 欧拉序数组
vector<int> inTime;            // 节点的进入时间戳
vector<int> outTime;           // 节点的离开时间戳
vector<int> parent;            // 节点的父节点
vector<int> depth;             // 节点的深度
vector<vector<int>> up;        // 用于LCA的倍增表
int timeStamp;                 // 时间戳
int blockSize;                 // 块的大小
vector<int> count;             // 每个权值出现的次数
int currentResult;             // 当前不同权值的数量
vector<bool> inCurrent;        // 记录节点是否在当前区间中
vector<int> answers;           // 答案数组

/**
 * 离散化函数
 * @param arr 原始权值数组
 * @return 离散化后的值域范围
 */
int discretize(const vector<int>& arr) {
    set<int> valueSet(arr.begin(), arr.end());
    vector<int> valueList(valueSet.begin(), valueSet.end());
    
    unordered_map<int, int> valueToId;
    for (int i = 0; i < valueList.size(); i++) {
        valueToId[valueList[i]] = i + 1;  // 从1开始编号
    }
    
    discreteValues.resize(arr.size());
    for (int i = 0; i < arr.size(); i++) {
        discreteValues[i] = valueToId[arr[i]];
    }
    
    return valueList.size();
}

/**
 * 使用非递归DFS预处理LCA所需的父节点和深度信息
 * @param n 节点数
 * @param startNode 起始节点
 */
void dfsLCA(int n, int startNode) {
    timeStamp = 0;
    inTime.assign(n + 1, 0);
    outTime.assign(n + 1, 0);
    parent.assign(n + 1, 0);
    depth.assign(n + 1, 0);
    euler.resize(2 * n + 2);
    
    stack<pair<int, bool>> stk;
    stk.push({startNode, false});
    
    while (!stk.empty()) {
        auto [node, visited] = stk.top();
        stk.pop();
        
        if (visited) {
            outTime[node] = timeStamp;
            continue;
        }
        
        inTime[node] = timeStamp;
        euler[timeStamp] = node;
        timeStamp++;
        
        // 重新压入当前节点（标记为已访问）
        stk.push({node, true});
        
        // 压入子节点（逆序以保持顺序）
        for (auto it = tree[node].rbegin(); it != tree[node].rend(); ++it) {
            int neighbor = *it;
            if (neighbor != parent[node]) {
                parent[neighbor] = node;
                depth[neighbor] = depth[node] + 1;
                stk.push({neighbor, false});
            }
        }
    }
}

/**
 * 预处理倍增表
 * @param n 节点数
 * @return 倍增表
 */
vector<vector<int>> preprocessLCA(int n) {
    int logMax = static_cast<int>(log2(n)) + 2;
    vector<vector<int>> upTable(logMax, vector<int>(n + 1));
    
    // 初始化up[0]层
    for (int i = 1; i <= n; i++) {
        upTable[0][i] = parent[i];
    }
    
    // 填充倍增表
    for (int k = 1; k < logMax; k++) {
        for (int i = 1; i <= n; i++) {
            upTable[k][i] = upTable[k-1][upTable[k-1][i]];
        }
    }
    
    return upTable;
}

/**
 * 查找两个节点的最近公共祖先
 * @param u 节点u
 * @param v 节点v
 * @param upTable 倍增表
 * @return 最近公共祖先
 */
int findLCA(int u, int v, const vector<vector<int>>& upTable) {
    if (depth[u] < depth[v]) {
        swap(u, v);
    }
    
    // 将u提升到与v同一深度
    int logMax = upTable.size();
    for (int k = logMax - 1; k >= 0; k--) {
        if (depth[u] - (1 << k) >= depth[v]) {
            u = upTable[k][u];
        }
    }
    
    if (u == v) {
        return u;
    }
    
    // 同时提升u和v
    for (int k = logMax - 1; k >= 0; k--) {
        if (upTable[k][u] != upTable[k][v]) {
            u = upTable[k][u];
            v = upTable[k][v];
        }
    }
    
    return upTable[0][u];
}

/**
 * 比较两个查询的顺序，用于莫队算法的排序
 * 奇偶排序优化：偶数块按r升序，奇数块按r降序
 */
bool compareQueries(const Query& q1, const Query& q2) {
    if (q1.block != q2.block) {
        return q1.block < q2.block;
    }
    // 奇偶排序优化
    return q1.block % 2 == 0 ? q1.r < q2.r : q1.r > q2.r;
}

/**
 * 切换节点的状态（加入或移除）
 * @param node 节点编号
 */
void toggle(int node) {
    int val = discreteValues[node];
    if (inCurrent[node]) {
        // 移除节点
        count[val]--;
        if (count[val] == 0) {
            currentResult--;
        }
    } else {
        // 添加节点
        if (count[val] == 0) {
            currentResult++;
        }
        count[val]++;
    }
    inCurrent[node] = !inCurrent[node];
}

/**
 * 主解题函数
 * @param n 节点数
 * @param m 查询数
 * @param val 节点权值数组
 * @param edges 边的列表
 * @param queriesInput 查询列表
 * @return 每个查询的结果
 */
vector<int> solveTreeMo(int n, int m, const vector<int>& val, const vector<vector<int>>& edges, 
                        const vector<vector<int>>& queriesInput) {
    // 异常处理
    if (n == 0 || m == 0) {
        return {};
    }
    
    // 构建邻接表
    tree.resize(n + 1);
    for (const auto& edge : edges) {
        int u = edge[0];
        int v = edge[1];
        tree[u].push_back(v);
        tree[v].push_back(u);
    }
    
    // 初始化原始权值数组
    values.resize(n + 1);
    for (int i = 1; i <= n; i++) {
        values[i] = val[i];
    }
    
    // 离散化
    valueRange = discretize(values);
    
    // DFS预处理LCA相关信息
    dfsLCA(n, 1);  // 假设根节点为1
    
    // 预处理倍增表
    vector<vector<int>> upTable = preprocessLCA(n);
    
    // 转换查询
    blockSize = static_cast<int>(sqrt(n)) + 1;
    vector<Query> queries;
    for (int i = 0; i < m; i++) {
        int u = queriesInput[i][0];
        int v = queriesInput[i][1];
        
        // 确保u的进入时间小于v的进入时间
        if (inTime[u] > inTime[v]) {
            swap(u, v);
        }
        
        int ancestor = findLCA(u, v, upTable);
        int l, r;
        
        // 处理两种情况：u是v的祖先，或者不是
        if (ancestor == u) {
            l = inTime[u];
            r = inTime[v];
        } else {
            l = outTime[u];
            r = inTime[v];
        }
        
        queries.emplace_back(l, r, ancestor, i, blockSize);
    }
    
    // 对查询进行排序
    sort(queries.begin(), queries.end(), compareQueries);
    
    // 初始化莫队算法相关数组
    count.assign(valueRange + 2, 0);
    currentResult = 0;
    inCurrent.assign(n + 1, false);
    answers.assign(m, 0);
    
    // 初始化当前区间的左右指针
    int curL = 1;
    int curR = 0;
    
    // 处理每个查询
    for (const Query& q : queries) {
        int l = q.l;
        int r = q.r;
        int ancestor = q.lca;
        int idx = q.idx;
        
        // 调整左右指针到目标位置
        while (curL > l) toggle(euler[--curL]);
        while (curR < r) toggle(euler[++curR]);
        while (curL < l) toggle(euler[curL++]);
        while (curR > r) toggle(euler[curR--]);
        
        // 处理LCA节点
        bool lcaAdded = false;
        if (ancestor != euler[l]) {
            int valAncestor = discreteValues[ancestor];
            if (!inCurrent[ancestor]) {
                if (count[valAncestor] == 0) {
                    currentResult++;
                }
                count[valAncestor]++;
                lcaAdded = true;
            }
        }
        
        // 保存当前查询的结果
        answers[idx] = currentResult;
        
        // 恢复LCA节点的状态
        if (lcaAdded) {
            int valAncestor = discreteValues[ancestor];
            count[valAncestor]--;
            if (count[valAncestor] == 0) {
                currentResult--;
            }
        }
    }
    
    return answers;
}

/**
 * 主函数，用于测试
 */
int main() {
    // 测试用例
    int n = 5;
    int m = 2;
    vector<int> val = {0, 1, 2, 1, 3, 2};  // 节点编号从1开始，索引0不使用
    vector<vector<int>> edges = {
        {1, 2},
        {1, 3},
        {2, 4},
        {2, 5}
    };
    vector<vector<int>> queries = {
        {3, 4},  // 查询节点3和4之间路径上的不同权值数量
        {2, 5}   // 查询节点2和5之间路径上的不同权值数量
    };
    
    vector<int> results = solveTreeMo(n, m, val, edges, queries);
    
    // 输出结果
    cout << "Query Results:" << endl;
    for (int result : results) {
        cout << result << endl;
    }
    
    return 0;
}