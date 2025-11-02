#include <bits/stdc++.h>
using namespace std;

// 重链剖分解决LCA查询，C++版
// 题目来源：洛谷P3379 【模板】最近公共祖先（LCA）
// 题目链接：https://www.luogu.com.cn/problem/P3379
//
// 题目描述：
// 如题，给定一棵有根多叉树，请求出指定两个点直接最近的公共祖先。
// 一共有n个节点，给定n-1条边，节点连成一棵树，给定头节点编号root
// 一共有m条查询，每条查询给定a和b，打印a和b的最低公共祖先
// 请用树链剖分的方式实现
// 1 <= n、m <= 5 * 10^5
//
// 解题思路：
// 使用树链剖分解决LCA问题
// 1. 树链剖分：通过两次DFS将树划分为多条重链
// 2. LCA查询：利用树链剖分的性质，当两个节点不在同一重链上时，
//    将深度较大的节点跳到其重链顶端的父节点，直到两个节点在同一重链上
// 3. 递归实现：C++版本使用递归实现DFS
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算重链顶端）
// 2. 对于LCA查询：
//    - 当两个节点不在同一重链上时，将深度较大的节点跳到其重链顶端的父节点
//    - 重复此过程直到两个节点在同一重链上
//    - 此时深度较小的节点即为LCA
//
// 时间复杂度分析：
// - 树链剖分预处理：O(n)
// - 每次LCA查询：O(log n)
// - 总体复杂度：O(n + m log n)
// 空间复杂度：O(n)
//
// 是否为最优解：
// 树链剖分解决LCA问题是一种高效的解决方案，
// 时间复杂度已经达到了理论下限，是最优解之一。
//
// 相关题目链接：
// 1. 洛谷P3379 【模板】最近公共祖先（LCA）（本题）：https://www.luogu.com.cn/problem/P3379
// 2. 洛谷P3384 【模板】重链剖分/树链剖分：https://www.luogu.com.cn/problem/P3384
// 3. LeetCode 1483. 树节点的第 K 个祖先：https://leetcode.cn/problems/kth-ancestor-of-a-tree-node/
// 4. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
//
// Java实现参考：Code02_LCA1.java
// Python实现参考：Code02_LCA1.py
// C++实现参考：Code02_LCA1.cpp（当前文件）

const int MAXN = 500001;
int n, m, root;

// 链式前向星存图
int head[MAXN], nxt[MAXN << 1], to[MAXN << 1], cnt = 0;

// 树链剖分相关数组
int fa[MAXN];     // 父节点
int dep[MAXN];    // 深度
int siz[MAXN];    // 子树大小
int son[MAXN];    // 重儿子
int top[MAXN];    // 所在重链的顶部节点

void addEdge(int u, int v) {
    nxt[++cnt] = head[u];
    to[cnt] = v;
    head[u] = cnt;
}

// 第一次DFS，计算父节点、深度、子树大小和重儿子
void dfs1(int u, int f) {
    fa[u] = f;
    dep[u] = dep[f] + 1;
    siz[u] = 1;
    
    for (int e = head[u]; e; e = nxt[e]) {
        int v = to[e];
        if (v != f) {
            dfs1(v, u);
            siz[u] += siz[v];
            if (son[u] == 0 || siz[son[u]] < siz[v]) {
                son[u] = v;
            }
        }
    }
}

// 第二次DFS，计算重链顶端
void dfs2(int u, int t) {
    top[u] = t;
    if (son[u] == 0) return;
    dfs2(son[u], t);  // 先处理重儿子
    for (int e = head[u]; e; e = nxt[e]) {
        int v = to[e];
        if (v != fa[u] && v != son[u]) {
            dfs2(v, v);  // 轻儿子作为新重链的顶端
        }
    }
}

// 使用树链剖分求两个节点的最近公共祖先
int lca(int a, int b) {
    // 当两个节点不在同一条重链上时
    while (top[a] != top[b]) {
        // 将深度较大的节点跳到其重链顶端的父节点
        if (dep[top[a]] < dep[top[b]]) swap(a, b);
        a = fa[top[a]];
    }
    // 当两个节点在同一条重链上时，深度较小的节点即为LCA
    return dep[a] <= dep[b] ? a : b;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    cin >> n >> m >> root;
    
    // 读取边信息
    for (int i = 1, u, v; i < n; i++) {
        cin >> u >> v;
        addEdge(u, v);
        addEdge(v, u);
    }
    
    // 树链剖分
    dfs1(root, 0);
    dfs2(root, root);
    
    // 处理查询
    for (int i = 1, a, b; i <= m; i++) {
        cin >> a >> b;
        cout << lca(a, b) << "\n";
    }
    
    return 0;
}