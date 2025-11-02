// class131/Code17_SegmentTreeMerge.cpp
// 洛谷 P4556 (Vani有约会) 雨天的尾巴 / 【模板】线段树合并
// 题目链接: https://www.luogu.com.cn/problem/P4556

#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
using namespace std;

const int MAXN = 100010;

// 线段树节点定义
struct Node {
    int left, right; // 左右子节点索引
    int val;        // 节点值（救济粮数量）
    int maxVal;     // 最大值
    int maxType;    // 最大值对应的救济粮类型
} tree[MAXN * 40]; // 动态开点线段树，空间要足够大

vector<int> graph[MAXN]; // 树的邻接表
int root[MAXN];          // 每个节点对应的线段树根节点
int ans[MAXN];           // 每个节点的答案
int cnt;                 // 动态开点计数器
int maxType;             // 最大救济粮类型

// LCA相关
int up[20][MAXN];        // 祖先表，2^k级祖先
int depth[MAXN];         // 深度数组
int LOG;                 // log2(n)向上取整

// 初始化线段树节点
void initNode(int node) {
    tree[node].left = tree[node].right = 0;
    tree[node].val = 0;
    tree[node].maxVal = 0;
    tree[node].maxType = 0;
}

// 线段树更新操作
void update(int &root, int l, int r, int pos, int val) {
    if (!root) {
        root = ++cnt;
        initNode(root);
    }
    
    if (l == r) {
        tree[root].val += val;
        tree[root].maxVal = tree[root].val;
        tree[root].maxType = l;
        return;
    }
    
    int mid = (l + r) >> 1;
    if (pos <= mid) {
        update(tree[root].left, l, mid, pos, val);
    } else {
        update(tree[root].right, mid + 1, r, pos, val);
    }
    
    // 更新当前节点的maxVal和maxType
    tree[root].maxVal = 0;
    tree[root].maxType = 0;
    
    // 比较左子树
    if (tree[root].left) {
        if (tree[tree[root].left].maxVal > tree[root].maxVal ||
            (tree[tree[root].left].maxVal == tree[root].maxVal && tree[tree[root].left].maxType < tree[root].maxType)) {
            tree[root].maxVal = tree[tree[root].left].maxVal;
            tree[root].maxType = tree[tree[root].left].maxType;
        }
    }
    
    // 比较右子树
    if (tree[root].right) {
        if (tree[tree[root].right].maxVal > tree[root].maxVal ||
            (tree[tree[root].right].maxVal == tree[root].maxVal && tree[tree[root].right].maxType < tree[root].maxType)) {
            tree[root].maxVal = tree[tree[root].right].maxVal;
            tree[root].maxType = tree[tree[root].right].maxType;
        }
    }
}

// 线段树合并操作
int merge(int a, int b, int l, int r) {
    if (!a) return b;
    if (!b) return a;
    
    if (l == r) {
        tree[a].val += tree[b].val;
        tree[a].maxVal = tree[a].val;
        tree[a].maxType = l;
        return a;
    }
    
    int mid = (l + r) >> 1;
    tree[a].left = merge(tree[a].left, tree[b].left, l, mid);
    tree[a].right = merge(tree[a].right, tree[b].right, mid + 1, r);
    
    // 更新当前节点的maxVal和maxType
    tree[a].maxVal = 0;
    tree[a].maxType = 0;
    
    // 比较左子树
    if (tree[a].left) {
        if (tree[tree[a].left].maxVal > tree[a].maxVal ||
            (tree[tree[a].left].maxVal == tree[a].maxVal && tree[tree[a].left].maxType < tree[a].maxType)) {
            tree[a].maxVal = tree[tree[a].left].maxVal;
            tree[a].maxType = tree[tree[a].left].maxType;
        }
    }
    
    // 比较右子树
    if (tree[a].right) {
        if (tree[tree[a].right].maxVal > tree[a].maxVal ||
            (tree[tree[a].right].maxVal == tree[a].maxVal && tree[tree[a].right].maxType < tree[a].maxType)) {
            tree[a].maxVal = tree[tree[a].right].maxVal;
            tree[a].maxType = tree[tree[a].right].maxType;
        }
    }
    
    return a;
}

// LCA预处理 - DFS
void dfsLCA(int u, int parent) {
    depth[u] = depth[parent] + 1;
    up[0][u] = parent;
    
    for (int k = 1; k < LOG; k++) {
        up[k][u] = up[k-1][up[k-1][u]];
    }
    
    for (int v : graph[u]) {
        if (v != parent) {
            dfsLCA(v, u);
        }
    }
}

// 获取LCA
int getLCA(int u, int v) {
    if (depth[u] < depth[v]) {
        swap(u, v);
    }
    
    // 将u提升到v的深度
    for (int k = LOG - 1; k >= 0; k--) {
        if (depth[u] - (1 << k) >= depth[v]) {
            u = up[k][u];
        }
    }
    
    if (u == v) return u;
    
    for (int k = LOG - 1; k >= 0; k--) {
        if (up[k][u] != up[k][v]) {
            u = up[k][u];
            v = up[k][v];
        }
    }
    
    return up[0][u];
}

// DFS合并子树并统计答案
void dfsMerge(int u, int parent) {
    for (int v : graph[u]) {
        if (v != parent) {
            dfsMerge(v, u);
            root[u] = merge(root[u], root[v], 1, maxType);
        }
    }
    
    // 记录该节点的答案
    ans[u] = tree[root[u]].maxType;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    int n, m;
    cin >> n >> m;
    
    // 构建树
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        graph[u].push_back(v);
        graph[v].push_back(u);
    }
    
    // 预处理LCA
    LOG = 0;
    while ((1 << LOG) <= n) LOG++;
    memset(depth, -1, sizeof(depth));
    dfsLCA(1, 0);
    
    // 初始化线段树相关变量
    cnt = 0;
    memset(root, 0, sizeof(root));
    maxType = 0;
    
    // 处理操作
    while (m--) {
        int x, y, z;
        cin >> x >> y >> z;
        maxType = max(maxType, z);
        
        int lca = getLCA(x, y);
        int parentLCA = up[0][lca];
        
        // 树上差分
        update(root[x], 1, maxType, z, 1);
        update(root[y], 1, maxType, z, 1);
        update(root[lca], 1, maxType, z, -1);
        if (parentLCA != 0) {
            update(root[parentLCA], 1, maxType, z, -1);
        }
    }
    
    // DFS合并子树线段树
    dfsMerge(1, 0);
    
    // 输出答案
    for (int i = 1; i <= n; i++) {
        cout << ans[i] << "\n";
    }
    
    return 0;
}

/*
 * 复杂度分析:
 * - 时间复杂度: O(n log n + m log n)
 *   - LCA预处理: O(n log n)
 *   - 每次更新操作: O(log n)
 *   - 线段树合并: O(n log n)，因为每个节点最多被合并一次
 * - 空间复杂度: O(n log n)
 *   - 动态开点线段树的空间复杂度
 * 
 * 算法详解:
 * 本题使用线段树合并解决树上问题。核心思想是对树上的每个节点维护一个线段树，
 * 线段树中存储该节点各种救济粮的数量。通过树上差分和线段树合并来高效处理操作。
 *
 * 算法步骤:
 * 1. 树上差分: 对于每次在路径(u,v)上投放类型z的救济粮，我们在u和v处+1，
 *    在lca(u,v)和parent[lca(u,v)]处-1
 * 2. DFS合并: 从叶子节点向根节点合并线段树，统计每个节点的信息
 * 3. 线段树合并: 合并两个节点的线段树，同时维护最大值和对应的类型
 *
 * 关键优化:
 * 1. 使用倍增法预处理LCA，时间复杂度O(n log n)
 * 2. 动态开点线段树节省空间
 * 3. 线段树合并避免重复计算
 * 4. 树上差分将路径更新转化为点更新
 *
 * 优化点:
 * 1. 可以使用更紧凑的动态开点策略
 * 2. 对于救济粮类型较大的情况，可以进行离散化处理
 * 3. 标记永久化可以简化代码，但需要注意正确性
 */