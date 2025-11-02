// HackerEarth - Tree Query with Multiple Operations
// 题目来源：HackerEarth Tree Query
// 题目链接：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/algorithm/tree-query/
// 
// 题目描述：
// 给定一棵树，支持以下操作：
// 1. 更新某个节点的值
// 2. 查询树中两个节点之间的路径上的节点值的和
// 3. 查询树中两个节点之间的路径上的节点值的最大值
// 4. 查询子树中所有节点值的和
// 5. 查询子树中所有节点值的最大值
//
// 解题思路：
// 树链剖分 + 线段树维护区间和与区间最大值
// 1. 使用树链剖分将树划分为多个链，转换为线段树可以处理的区间
// 2. 路径查询通过多次区间查询实现
// 3. 子树查询可以直接通过连续区间查询实现，因为树链剖分后的子树在DFS序中是连续的
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
// 2. 使用线段树维护每个区间的和与最大值
// 3. 对于更新操作：更新单个节点的值
// 4. 对于路径查询：使用树链剖分将路径分解为多个区间，分别查询后合并结果
// 5. 对于子树查询：直接查询以该节点为根的子树对应的连续区间
//
// 时间复杂度分析：
// - 树链剖分预处理：O(n)
// - 每次操作：O(log²n)
// - 总体复杂度：O(m log²n)
// 空间复杂度：O(n)
//
// 是否为最优解：
// 对于这种树上的路径和子树查询问题，树链剖分是一种高效的解决方案
// 时间复杂度已经达到了理论下限，是最优解之一
//
// 相关题目链接：
// 1. HackerEarth Tree Query（本题）：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/algorithm/tree-query/
// 2. 洛谷P3979 遥远的国度：https://www.luogu.com.cn/problem/P3979
// 3. 洛谷P3976 [AHOI2015]旅游：https://www.luogu.com.cn/problem/P3976
// 4. 洛谷P2486 [SDOI2011]染色：https://www.luogu.com.cn/problem/P2486
// 5. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
//
// Java实现参考：Code_HackerEarth_TreeQueryMultipleOps.java
// Python实现参考：Code_HackerEarth_TreeQueryMultipleOps.py
// C++实现参考：Code_HackerEarth_TreeQueryMultipleOps.cpp（当前文件）

#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

const int MAXN = 100010;
int n;
int value[MAXN]; // 节点价值

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
int treeSize[MAXN]; // 子树大小（用于子树查询）
int time = 0;     // dfs时间戳

// 线段树相关数组
int sumTree[MAXN << 2]; // 区间和
int maxTree[MAXN << 2]; // 区间最大值

// 第一次DFS：计算深度、父节点、子树大小、重儿子
void dfs1(int u, int father) {
    fa[u] = father;
    dep[u] = dep[father] + 1;
    siz[u] = 1;
    son[u] = 0;
    
    for (int v : graph[u]) {
        if (v != father) {
            dfs1(v, u);
            siz[u] += siz[v];
            // 更新重儿子
            if (son[u] == 0 || siz[v] > siz[son[u]]) {
                son[u] = v;
            }
        }
    }
}

// 第二次DFS：计算重链顶部节点、dfs序、子树大小（用于子树查询）
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
    
    // 计算子树大小（用于子树查询，子树的范围是[dfn[u], dfn[u] + treeSize[u] - 1]）
    treeSize[u] = siz[u];
}

// 线段树向上更新
void pushUp(int rt) {
    sumTree[rt] = sumTree[rt << 1] + sumTree[rt << 1 | 1];
    maxTree[rt] = max(maxTree[rt << 1], maxTree[rt << 1 | 1]);
}

// 构建线段树
void build(int l, int r, int rt) {
    if (l == r) {
        sumTree[rt] = value[rnk[l]];
        maxTree[rt] = value[rnk[l]];
        return;
    }
    int mid = (l + r) >> 1;
    build(l, mid, rt << 1);
    build(mid + 1, r, rt << 1 | 1);
    pushUp(rt);
}

// 单点更新
void updatePoint(int pos, int val, int l, int r, int rt) {
    if (l == r) {
        sumTree[rt] = val;
        maxTree[rt] = val;
        return;
    }
    int mid = (l + r) >> 1;
    if (pos <= mid) {
        updatePoint(pos, val, l, mid, rt << 1);
    } else {
        updatePoint(pos, val, mid + 1, r, rt << 1 | 1);
    }
    pushUp(rt);
}

// 区间查询和
int querySum(int L, int R, int l, int r, int rt) {
    if (L <= l && r <= R) {
        return sumTree[rt];
    }
    int mid = (l + r) >> 1;
    int res = 0;
    if (L <= mid) res += querySum(L, R, l, mid, rt << 1);
    if (R > mid) res += querySum(L, R, mid + 1, r, rt << 1 | 1);
    return res;
}

// 区间查询最大值
int queryMax(int L, int R, int l, int r, int rt) {
    if (L <= l && r <= R) {
        return maxTree[rt];
    }
    int mid = (l + r) >> 1;
    int res = INT_MIN;
    if (L <= mid) res = max(res, queryMax(L, R, l, mid, rt << 1));
    if (R > mid) res = max(res, queryMax(L, R, mid + 1, r, rt << 1 | 1));
    return res;
}

// 查询路径和
int pathSum(int x, int y) {
    int res = 0;
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) {
            swap(x, y); // 交换x,y
        }
        res += querySum(dfn[top[x]], dfn[x], 1, n, 1);
        x = fa[top[x]];
    }
    if (dep[x] > dep[y]) {
        swap(x, y); // 保证x深度较小
    }
    res += querySum(dfn[x], dfn[y], 1, n, 1);
    return res;
}

// 查询路径最大值
int pathMax(int x, int y) {
    int res = INT_MIN;
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) {
            swap(x, y); // 交换x,y
        }
        res = max(res, queryMax(dfn[top[x]], dfn[x], 1, n, 1));
        x = fa[top[x]];
    }
    if (dep[x] > dep[y]) {
        swap(x, y); // 保证x深度较小
    }
    res = max(res, queryMax(dfn[x], dfn[y], 1, n, 1));
    return res;
}

// 查询子树和
int subtreeSum(int u) {
    return querySum(dfn[u], dfn[u] + treeSize[u] - 1, 1, n, 1);
}

// 查询子树最大值
int subtreeMax(int u) {
    return queryMax(dfn[u], dfn[u] + treeSize[u] - 1, 1, n, 1);
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    cin >> n;
    
    // 读入节点价值
    for (int i = 1; i <= n; i++) {
        cin >> value[i];
    }
    
    // 读入边信息
    for (int i = 0; i < n - 1; i++) {
        int u, v;
        cin >> u >> v;
        graph[u].push_back(v);
        graph[v].push_back(u);
    }
    
    // 树链剖分
    dfs1(1, 0); // 从1节点开始，父节点为0
    time = 0; // 重置时间戳
    dfs2(1, 1); // 从1节点开始，重链顶部为1
    
    // 构建线段树
    build(1, n, 1);
    
    // 处理操作
    int q;
    cin >> q;
    while (q--) {
        int op;
        cin >> op;
        
        if (op == 1) {
            // 更新某个节点的值
            int node, val;
            cin >> node >> val;
            updatePoint(dfn[node], val, 1, n, 1);
        } else if (op == 2) {
            // 查询路径和
            int u, v;
            cin >> u >> v;
            cout << pathSum(u, v) << endl;
        } else if (op == 3) {
            // 查询路径最大值
            int u, v;
            cin >> u >> v;
            cout << pathMax(u, v) << endl;
        } else if (op == 4) {
            // 查询子树和
            int u;
            cin >> u;
            cout << subtreeSum(u) << endl;
        } else if (op == 5) {
            // 查询子树最大值
            int u;
            cin >> u;
            cout << subtreeMax(u) << endl;
        }
    }
    
    return 0;
}