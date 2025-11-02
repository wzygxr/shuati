// U92652 【模板】kruskal重构树 - C++实现
// 题目描述：
// 给出一个有 n 个结点， m 条边的无向图，每条边有一个边权。
// 求结点 x,y 之间所有路径的中，最长的边最小值是多少，若这两个点之间没有任何路径，输出 -1 。
// 共有 Q 组询问。
//
// 输入格式：
// 第一行三个整数 n,m,Q 。
// 接下来 m 行每行三个整数 x,y,z(1 ≤ x,y ≤ n,1 ≤ z ≤ 1000000) ，表示有一条连接 x 和 y 长度为 z 的边。
// 接下来 Q 行每行两个整数 x,y(x ≠ y) ，表示一组询问。
//
// 输出格式：
// Q 行，每行一个整数，表示一组询问的答案。
//
// 解题思路：
// 这是一道Kruskal重构树的模板题。
// 要求两点间所有路径中最大边权的最小值，可以转化为在最小生成树上求两点间路径上的最大边权。
// 使用Kruskal重构树的方法：
// 1. 按边权从小到大排序，构建最小生成树的Kruskal重构树
// 2. 重构树中，每个原始节点是叶子节点，内部节点代表边
// 3. 重构树满足大根堆性质（因为我们按从小到大排序构建）
// 4. 两点间路径的最大边权最小值等于它们在重构树上的LCA节点权值
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

#include <bits/stdc++.h>
using namespace std;

const int MAXN = 300001;
const int MAXM = 300001;
const int MAXH = 20;

struct Edge {
    int u, v, w;
};

bool cmp(Edge a, Edge b) {
    return a.w < b.w;  // 按边权从小到大排序
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
// 按边权从小到大排序，构建最小生成树的Kruskal重构树
void kruskalRebuild() {
    for (int i = 1; i <= n; i++) {
        father[i] = i;
    }
    
    // 按边权从小到大排序
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
    
    cin >> n >> m >> q;
    
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
    
    for (int i = 1, x, y; i <= q; i++) {
        cin >> x >> y;
        
        // 如果两点不连通，输出-1
        // 在Kruskal重构树中，如果两个点不连通，说明在原图中也不连通
        if (find(x) != find(y)) {
            cout << "-1\n";
        } else {
            // 否则输出LCA节点的权值，即路径上最大边权的最小值
            // 这是Kruskal重构树的重要性质：两点间路径的最大边权最小值等于它们LCA的点权
            cout << nodeKey[lca(x, y)] << "\n";
        }
    }
    
    return 0;
}