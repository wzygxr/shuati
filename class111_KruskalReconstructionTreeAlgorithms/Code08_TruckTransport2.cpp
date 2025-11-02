#include <iostream>
#include <algorithm>
#include <cstdio>

// P1967 [NOIP2013 提高组] 货车运输 - C++实现
// 题目描述：
// A 国有 n 座城市，编号从 1 到 n，城市之间有 m 条双向道路。每一条道路对车辆都有重量限制，简称限重。
// 现在有 q 辆货车在运输货物，司机们想知道每辆车在不超过车辆限重的情况下，最多能运多重的货物。
//
// 输入格式：
// 第一行有两个用一个空格隔开的整数 n,m，表示 A 国有 n 座城市和 m 条道路。
// 接下来 m 行每行三个整数 x, y, z，每两个整数之间用一个空格隔开，表示从 x 号城市到 y 号城市有一条限重为 z 的道路。
// 注意：x ≠ y，两座城市之间可能有多条道路。
// 接下来一行有一个整数 q，表示有 q 辆货车需要运货。
// 接下来 q 行，每行两个整数 x,y，之间用一个空格隔开，表示一辆货车需要从 x 城市运输货物到 y 城市，保证 x ≠ y。
//
// 输出格式：
// 共有 q 行，每行一个整数，表示对于每一辆货车，它的最大载重是多少。
// 如果货车不能到达目的地，输出 -1。
//
// 解题思路：
// 这是一个经典的Kruskal重构树应用问题。
// 要求路径上最小边权的最大值，可以转化为在最大生成树上求两点间路径上的最小边权。
// 使用Kruskal重构树的方法：
// 1. 按边权从大到小排序，构建最大生成树的Kruskal重构树
// 2. 重构树中，每个原始节点是叶子节点，内部节点代表边
// 3. 重构树满足小根堆性质（因为我们按从大到小排序构建）
// 4. 两点间路径的最小边权最大值等于它们在重构树上的LCA节点权值
//
// 时间复杂度分析：
// 1. 构建Kruskal重构树：O(m log m) - 主要是排序的复杂度
// 2. DFS预处理：O(n) - 每个节点访问一次
// 3. 每次查询：O(log n) - 倍增LCA的复杂度
// 总复杂度：O(m log m + q log n)
//
// 空间复杂度分析：
// 1. 存储边：O(m)
// 2. 存储图和重构树：O(n)
// 3. 倍增表：O(n log n)
// 总空间复杂度：O(n log n + m)

using namespace std;

const int MAXN = 10001;
const int MAXM = 50001;
const int MAXH = 16;

struct Edge {
    int u, v, w;
};

bool cmp(Edge a, Edge b) {
    return a.w > b.w;  // 按边权从大到小排序
}

int n, m, q;
Edge edge[MAXM];

int father[MAXN * 2];
int head[MAXN * 2];
int nxt[MAXN * 2];
int to[MAXN * 2];
int cntg;
int nodeKey[MAXN * 2];
int cntu;

int dep[MAXN * 2];
int stjump[MAXN * 2][MAXH];

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
// 由于要求最小边权的最大值，我们按边权从大到小排序构建最大生成树
void kruskalRebuild() {
    for (int i = 1; i <= n; i++) {
        father[i] = i;
    }
    
    // 按边权从大到小排序
    sort(edge + 1, edge + m + 1, cmp);
    
    cntu = n;
    for (int i = 1; i <= m; i++) {
        int fx = find(edge[i].u);
        int fy = find(edge[i].v);
        if (fx != fy) {
            // 合并两个连通分量
            father[fx] = father[fy] = ++cntu;
            father[cntu] = cntu;
            // 新节点的权值为边权
            nodeKey[cntu] = edge[i].w;
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
    
    // 构建倍增表
    for (int p = 1; p < MAXH; p++) {
        stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
    }
    
    // 递归处理子节点
    for (int e = head[u]; e > 0; e = nxt[e]) {
        dfs(to[e], u);
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

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    cin >> n >> m;
    
    for (int i = 1; i <= m; i++) {
        cin >> edge[i].u >> edge[i].v >> edge[i].w;
    }
    
    // 构建Kruskal重构树
    kruskalRebuild();
    
    // 对每个连通分量进行DFS预处理
    for (int i = 1; i <= cntu; i++) {
        if (i == father[i]) {
            dfs(i, 0);
        }
    }
    
    cin >> q;
    for (int i = 1, x, y; i <= q; i++) {
        cin >> x >> y;
        
        // 如果两点不连通，输出-1
        if (find(x) != find(y)) {
            cout << "-1\n";
        } else {
            // 否则输出LCA节点的权值，即路径上最小边权的最大值
            cout << nodeKey[lca(x, y)] << "\n";
        }
    }
    
    return 0;
}