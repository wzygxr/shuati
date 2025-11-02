// Codeforces 1706E Qpwoeirut and Vertices - C++实现
// 题目描述：
// 给定一个包含n个节点和m条边的无向图，以及q个查询。
// 每个查询给出一个区间[l,r]，要求找出使得区间[l,r]内所有节点都连通的最少边数。
// 注意：这些边必须是原图中编号从1到某个值的连续边。
//
// 输入格式：
// 第一行包含一个整数t，表示测试用例数量。
// 每个测试用例的第一行包含三个整数n, m, q (2≤n≤10^5, 1≤m,q≤2⋅10^5)。
// 接下来m行，每行包含两个整数u, v (1≤u,v≤n, u≠v)，表示一条边。
// 接下来q行，每行包含两个整数l, r (1≤l≤r≤n)，表示一个查询。
//
// 输出格式：
// 对于每个查询，输出一个整数表示答案。
//
// 解题思路：
// 这是一道典型的Kruskal重构树应用题。
// 由于要求的是使得区间[l,r]内所有节点都连通的最少边数，我们可以将边按照编号排序，
// 然后构建Kruskal重构树。对于每个查询，我们需要找到包含[l,r]区间内所有节点的最小连通子树。
// 在Kruskal重构树中，这个子树的根节点对应的边编号就是答案。
//
// 算法步骤：
// 1. 构建Kruskal重构树，将边按照编号排序
// 2. 对于每个节点，记录它在重构树中的叶子节点
// 3. 对于每个查询[l,r]，找到包含这些节点的最小连通子树
// 4. 这可以通过找到这些节点在重构树中的LCA来实现
//
// 时间复杂度分析：
// 1. 构建Kruskal重构树：O(m log m)
// 2. DFS预处理：O(n)
// 3. 每次查询：O(log n)
// 总复杂度：O(m log m + q log n)
//
// 空间复杂度分析：
// 1. 存储边：O(m)
// 2. 存储图和重构树：O(n)
// 3. 倍增表：O(n log n)
// 总空间复杂度：O(n log n + m)

#include <iostream>
#include <algorithm>
#include <cstring>
using namespace std;

const int MAXN = 200001;
const int MAXM = 200001;
const int MAXH = 20;

int n, m, q;

// 每条边有三个信息，节点u、节点v、边编号i
int edge[MAXM][3];

// 并查集
int father[MAXN * 2];

// Kruskal重构树的建图
int head[MAXN * 2], nxt[MAXN * 2], to[MAXN * 2], cntg;

// Kruskal重构树上，节点的权值（边编号）
int nodeKey[MAXN * 2];
// Kruskal重构树上，点的数量
int cntu;

// 每个原始节点在重构树中对应的叶子节点
int leaf[MAXN];

// Kruskal重构树上，dfs过程建立的信息
int dep[MAXN * 2];
int stjump[MAXN * 2][MAXH];
int dfn[MAXN * 2];
int size[MAXN * 2];
int dfntime;

int find(int i) {
    if (i != father[i]) {
        father[i] = find(father[i]);
    }
    return father[i];
}

void addEdge(int u, int v) {
    nxt[++cntg] = head[u];
    to[cntg] = v;
    head[u] = cntg;
}

// 构建Kruskal重构树
// 按边编号从小到大排序
void kruskalRebuild() {
    for (int i = 1; i <= n; i++) {
        father[i] = i;
    }
    
    // 按边编号从小到大排序
    sort(edge + 1, edge + m + 1, [](int* a, int* b) {
        return a[2] < b[2];
    });
    
    cntu = n;
    for (int i = 1, fx, fy; i <= m; i++) {
        fx = find(edge[i][0]);
        fy = find(edge[i][1]);
        if (fx != fy) {
            // 合并两个连通分量
            father[fx] = father[fy] = ++cntu;
            father[cntu] = cntu;
            // 新节点的权值为边编号
            nodeKey[cntu] = edge[i][2];
            // 建立父子关系
            addEdge(cntu, fx);
            addEdge(cntu, fy);
        }
    }
}

// DFS预处理，构建倍增表
void dfs(int u, int fa) {
    dep[u] = dep[fa] + 1;
    stjump[u][0] = fa;
    dfn[u] = ++dfntime;
    
    // 构建倍增表
    for (int p = 1; p < MAXH; p++) {
        stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
    }
    
    size[u] = (u <= n) ? 1 : 0; // 叶子节点size为1
    // 递归处理子节点
    for (int e = head[u]; e; e = nxt[e]) {
        dfs(to[e], u);
        size[u] += size[to[e]];
    }
}

// 计算两点的最近公共祖先(LCA)
int lca(int a, int b) {
    // 保证a在更深的位置
    if (dep[a] < dep[b]) {
        swap(a, b);
    }
    
    // 将a提升到和b同一深度
    for (int p = MAXH - 1; p >= 0; p--) {
        if (dep[stjump[a][p]] >= dep[b]) {
            a = stjump[a][p];
        }
    }
    
    // 如果已经相遇，直接返回
    if (a == b) {
        return a;
    }
    
    // 同时向上提升，直到相遇
    for (int p = MAXH - 1; p >= 0; p--) {
        if (stjump[a][p] != stjump[b][p]) {
            a = stjump[a][p];
            b = stjump[b][p];
        }
    }
    
    // 返回LCA
    return stjump[a][0];
}

// 找到包含[l,r]区间内所有节点的最小连通子树的根节点
int findSubtreeRoot(int l, int r) {
    // 特殊情况：只有一个节点
    if (l == r) {
        return leaf[l];
    }
    
    // 找到[l,r]区间内节点的LCA
    int root = leaf[l];
    for (int i = l + 1; i <= r; i++) {
        root = lca(root, leaf[i]);
    }
    
    return root;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    cout.tie(0);
    
    int t;
    cin >> t;
    
    for (int cases = 0; cases < t; cases++) {
        cin >> n >> m >> q;
        
        // 初始化
        cntg = 0;
        dfntime = 0;
        memset(head, 0, sizeof(head));
        
        for (int i = 1; i <= m; i++) {
            cin >> edge[i][0] >> edge[i][1];
            edge[i][2] = i; // 边编号就是i
        }
        
        // 构建Kruskal重构树
        kruskalRebuild();
        
        // 对每个连通分量进行DFS预处理
        for (int i = 1; i <= cntu; i++) {
            if (i == father[i]) {
                dfs(i, 0);
            }
        }
        
        // 记录每个原始节点在重构树中对应的叶子节点
        for (int i = 1; i <= n; i++) {
            leaf[i] = i;
        }
        
        for (int i = 1, l, r; i <= q; i++) {
            cin >> l >> r;
            
            // 找到包含[l,r]区间内所有节点的最小连通子树的根节点
            int root = findSubtreeRoot(l, r);
            
            // 输出该根节点对应的边编号
            cout << nodeKey[root] << "\n";
        }
    }
    
    return 0;
}