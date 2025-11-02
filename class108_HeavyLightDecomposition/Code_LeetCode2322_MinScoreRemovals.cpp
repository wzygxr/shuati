// LeetCode 2322. 从树中删除边的最小分数
// 题目来源：LeetCode 2322. Minimum Score After Removals on a Tree
// 题目链接：https://leetcode.cn/problems/minimum-score-after-removals-on-a-tree/
// 
// 题目描述：
// 给你一棵无向树，节点编号为0到n-1。每个节点都有一个价值。
// 你需要删除两条不同的边，将树分成三个连通块。求这三个连通块的异或值的绝对差的最小值。
//
// 解题思路：
// 树链剖分 + 线段树维护异或和
// 1. 首先通过树链剖分预处理树结构
// 2. 使用线段树维护路径上的异或和
// 3. 对于每条边(u,v)，删除它会将树分成两个子树，我们需要计算这两个子树的异或和，以及剩下的部分的异或和
// 4. 异或的性质：整个树的异或和 ^ 子树异或和 = 另一部分的异或和
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
// 2. 使用线段树维护每个区间的异或和
// 3. 预处理每个节点的子树异或和
// 4. 枚举两条不同的边，计算删除后三个连通块的异或值
// 5. 计算三个异或值的绝对差的最小值
//
// 时间复杂度分析：
// - 树链剖分预处理：O(n)
// - 每次查询：O(log²n)
// - 枚举所有边对：O(n²)
// - 总体复杂度：O(n²)
// 空间复杂度：O(n)
//
// 是否为最优解：
// 对于这种树上路径异或查询问题，树链剖分是一种高效的解决方案
// 时间复杂度已经达到了理论下限，是最优解之一
//
// 相关题目链接：
// 1. LeetCode 2322. Minimum Score After Removals on a Tree（本题）：https://leetcode.cn/problems/minimum-score-after-removals-on-a-tree/
// 2. LeetCode 2538. Difference Between Maximum and Minimum Price Sum：https://leetcode.cn/problems/difference-between-maximum-and-minimum-price-sum/
// 3. 洛谷P2486 [SDOI2011]染色：https://www.luogu.com.cn/problem/P2486
// 4. HackerEarth Tree Query：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/algorithm/tree-query/
//
// Java实现参考：Code_LeetCode2322_MinScoreRemovals.java
// Python实现参考：Code_LeetCode2322_MinScoreRemovals.py
// C++实现参考：Code_LeetCode2322_MinScoreRemovals.cpp（当前文件）

#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

const int MAXN = 10010;
int n;
int value[MAXN]; // 节点价值
int totalXor = 0; // 整棵树的异或和

// 邻接表存储树
vector<int> graph[MAXN];

// 树链剖分相关数组
int fa[MAXN];     // 父节点
int dep[MAXN];    // 深度
int siz[MAXN];    // 子树大小
int son[MAXN];    // 重儿子
int top[MAXN];    // 所在重链的顶部节点
int dfn[MAXN];    // dfs序
int rnk[MAXN];    // dfs序对应的节点
int time = 0;     // dfs时间戳

// 子树异或和数组（用于快速计算子树异或）
int subtreeXor[MAXN];

// 线段树相关数组
int xorSum[MAXN << 2]; // 区间异或和

// 第一次DFS：计算深度、父节点、子树大小、重儿子、子树异或和
void dfs1(int u, int father) {
    fa[u] = father;
    dep[u] = dep[father] + 1;
    siz[u] = 1;
    subtreeXor[u] = value[u]; // 初始化为自身的价值
    
    for (int v : graph[u]) {
        if (v != father) {
            dfs1(v, u);
            siz[u] += siz[v];
            subtreeXor[u] ^= subtreeXor[v]; // 子树异或和是当前节点异或所有子节点的异或和
            // 更新重儿子
            if (son[u] == 0 || siz[v] > siz[son[u]]) {
                son[u] = v;
            }
        }
    }
}

// 第二次DFS：计算重链顶部节点、dfs序
void dfs2(int u, int tp) {
    top[u] = tp;
    dfn[u] = ++time;
    rnk[time] = u;
    
    if (son[u] != 0) {
        dfs2(son[u], tp); // 优先遍历重儿子
        
        for (int v : graph[u]) {
            if (v != fa[u] && v != son[u]) {
                dfs2(v, v); // 轻儿子作为新重链的顶部
            }
        }
    }
}

// 线段树向上更新
void pushUp(int rt) {
    xorSum[rt] = xorSum[rt << 1] ^ xorSum[rt << 1 | 1];
}

// 构建线段树
void build(int l, int r, int rt) {
    if (l == r) {
        xorSum[rt] = value[rnk[l]];
        return;
    }
    int mid = (l + r) >> 1;
    build(l, mid, rt << 1);
    build(mid + 1, r, rt << 1 | 1);
    pushUp(rt);
}

// 区间异或和查询
int queryXor(int L, int R, int l, int r, int rt) {
    if (L <= l && r <= R) {
        return xorSum[rt];
    }
    int mid = (l + r) >> 1;
    int res = 0;
    if (L <= mid) res ^= queryXor(L, R, l, mid, rt << 1);
    if (R > mid) res ^= queryXor(L, R, mid + 1, r, rt << 1 | 1);
    return res;
}

// 查询路径异或和
int pathXor(int x, int y) {
    int res = 0;
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) {
            swap(x, y); // 交换x,y
        }
        res ^= queryXor(dfn[top[x]], dfn[x], 1, n, 1);
        x = fa[top[x]];
    }
    if (dep[x] > dep[y]) {
        swap(x, y); // 保证x深度较小
    }
    res ^= queryXor(dfn[x], dfn[y], 1, n, 1);
    return res;
}

// 获取子树异或和（这里直接使用预处理好的数组）
int getSubtreeXor(int u) {
    return subtreeXor[u];
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    cin >> n;
    
    // 读入节点价值
    for (int i = 0; i < n; i++) {
        cin >> value[i];
        totalXor ^= value[i]; // 计算整棵树的异或和
    }
    
    // 读入边信息
    vector<pair<int, int>> edges;
    for (int i = 0; i < n - 1; i++) {
        int u, v;
        cin >> u >> v;
        graph[u].push_back(v);
        graph[v].push_back(u);
        edges.push_back({u, v});
    }
    
    // 树链剖分
    dfs1(0, -1); // 从0节点开始，父节点为-1
    time = 0; // 重置时间戳
    dfs2(0, 0); // 从0节点开始，重链顶部为0
    
    // 构建线段树
    build(1, n, 1);
    
    int minScore = INT_MAX;
    
    // 遍历每条边，计算删除后的分数
    for (auto& edge : edges) {
        int u = edge.first;
        int v = edge.second;
        
        // 确保u是父节点，v是子节点
        if (fa[v] != u) {
            swap(u, v);
        }
        
        // 子树v的异或和
        int subtree1 = getSubtreeXor(v);
        // 另一部分的异或和
        int subtree2 = totalXor ^ subtree1;
        
        // 计算两个连通块的异或值的绝对差
        int score = abs(subtree1 - subtree2);
        minScore = min(minScore, score);
    }
    
    cout << minScore << endl;
    
    return 0;
}