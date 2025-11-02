// Tree Path Queries - 树上莫队算法实现 (C++版本)
// 题目来源: 模板题 - 树上路径不同元素查询
// 题目链接: https://www.luogu.com.cn/problem/P4396
// 题目大意: 给定一棵树，每个节点有一个权值，每次查询路径u-v上有多少不同的权值
// 时间复杂度: O(n*sqrt(n))，空间复杂度: O(n)

#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <cstdio>
#include <cstring>

using namespace std;

const int MAXN = 100010;
const int MAXV = 100010;
const int LOG = 20;

struct Query {
    int l, r, lca, id;
};

int n, m, idx;
int arr[MAXN];
int bi[MAXN * 2]; // 分块数组
int cnt[MAXV];    // 记录每种权值的出现次数
int ans[MAXN];    // 存储答案
int diff = 0;     // 当前路径不同元素的数量

// 树的邻接表
vector<int> tree[MAXN];

// 欧拉序相关
int in[MAXN];   // 进入时间戳
int out[MAXN];  // 离开时间戳
int seq[MAXN * 2]; // 欧拉序序列
int fa[MAXN];   // 父节点
int dep[MAXN];  // 深度
int up[MAXN][LOG]; // 倍增数组，用于LCA查询
Query queries[MAXN];

// 查询排序比较器
bool QueryCmp(Query a, Query b) {
    if (bi[a.l] != bi[b.l]) {
        return bi[a.l] < bi[b.l];
    }
    // 奇偶优化
    if (bi[a.l] & 1) {
        return a.r < b.r;
    } else {
        return a.r > b.r;
    }
}

// 添加/删除节点到路径
void toggle(int node) {
    int value = arr[node];
    if (cnt[value] > 0) {
        cnt[value]--;
        if (cnt[value] == 0) {
            diff--;
        }
    } else {
        cnt[value]++;
        diff++;
    }
}

// 预处理LCA的倍增数组
void dfs(int u, int parent) {
    in[u] = ++idx;
    seq[idx] = u;
    fa[u] = parent;
    dep[u] = dep[parent] + 1;
    up[u][0] = parent;
    for (int i = 1; i < LOG; i++) {
        up[u][i] = up[up[u][i-1]][i-1];
    }
    for (int v : tree[u]) {
        if (v != parent) {
            dfs(v, u);
        }
    }
    out[u] = ++idx;
    seq[idx] = u;
}

// 查询LCA
int lca(int u, int v) {
    if (dep[u] < dep[v]) {
        swap(u, v);
    }
    // 提升u到v的深度
    for (int i = LOG - 1; i >= 0; i--) {
        if (dep[up[u][i]] >= dep[v]) {
            u = up[u][i];
        }
    }
    if (u == v) return u;
    // 同时提升u和v
    for (int i = LOG - 1; i >= 0; i--) {
        if (up[u][i] != up[v][i]) {
            u = up[u][i];
            v = up[v][i];
        }
    }
    return up[u][0];
}

// 将树上路径转换为欧拉序区间
void buildQuery(int u, int v, int id) {
    int ancestor = lca(u, v);
    if (ancestor == u) {
        // 路径u-v在同一条链上，u是祖先
        queries[id].l = in[u];
        queries[id].r = in[v];
        queries[id].lca = 0;
        queries[id].id = id;
    } else if (ancestor == v) {
        // 路径u-v在同一条链上，v是祖先
        queries[id].l = in[v];
        queries[id].r = in[u];
        queries[id].lca = 0;
        queries[id].id = id;
    } else {
        // 路径u-v需要经过LCA
        if (in[u] > in[v]) {
            swap(u, v);
        }
        queries[id].l = out[u];
        queries[id].r = in[v];
        queries[id].lca = ancestor;
        queries[id].id = id;
    }
}

int main() {
    // 读取节点数和查询次数
    scanf("%d%d", &n, &m);
    
    // 读取节点权值
    for (int i = 1; i <= n; i++) {
        scanf("%d", &arr[i]);
    }

    // 读取树的边
    for (int i = 0; i < n - 1; i++) {
        int u, v;
        scanf("%d%d", &u, &v);
        tree[u].push_back(v);
        tree[v].push_back(u);
    }

    // 预处理欧拉序和LCA
    idx = 0;
    dfs(1, 0);

    // 分块 - 块大小为sqrt(2n)
    int blockSize = sqrt(2 * n) + 1;
    for (int i = 1; i <= 2 * n; i++) {
        bi[i] = (i - 1) / blockSize;
    }

    // 读取查询并构建欧拉序查询
    for (int i = 0; i < m; i++) {
        int u, v;
        scanf("%d%d", &u, &v);
        buildQuery(u, v, i);
    }

    // 排序查询
    sort(queries, queries + m, QueryCmp);

    // 初始化莫队指针
    int winL = 1, winR = 0;
    memset(cnt, 0, sizeof(cnt));
    diff = 0;

    // 处理每个查询
    for (int i = 0; i < m; i++) {
        Query q = queries[i];
        int l = q.l;
        int r = q.r;
        int ancestor = q.lca;
        int id = q.id;

        // 移动指针
        while (winR < r) toggle(seq[++winR]);
        while (winL > l) toggle(seq[--winL]);
        while (winR > r) toggle(seq[winR--]);
        while (winL < l) toggle(seq[winL++]);

        // 如果有LCA，需要额外处理
        if (ancestor != 0) {
            toggle(ancestor); // 临时加入LCA
            ans[id] = diff;
            toggle(ancestor); // 记得撤销
        } else {
            ans[id] = diff;
        }
    }

    // 输出答案
    for (int i = 0; i < m; i++) {
        printf("%d\n", ans[i]);
    }

    return 0;
}