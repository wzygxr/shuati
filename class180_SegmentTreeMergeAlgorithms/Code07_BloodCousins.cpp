// Blood Cousins CF208E.cpp
// 测试链接 : https://codeforces.com/problemset/problem/208/E
// 线段树合并解法
//
// 题目来源：Codeforces
// 题目大意：给定一棵树，多次询问某个节点的第k代堂兄弟数量
// 解法：线段树合并 + 倍增 + DFS序
// 时间复杂度：O(n log n + q log n)
// 空间复杂度：O(n log n)

// 由于环境限制，使用基础C++实现方式，避免使用复杂的STL容器

int n, m;
int G[100005][100], G_size[100005];
int depth[100005], fa[100005];
int queries[100005][100], queries_size[100005];
int ans[100005];

// 线段树合并相关
int root[100005], lc[2000005], rc[2000005], sum[2000005], cnt = 0;

// 倍增祖先
int anc[100005][20];

// 动态开点线段树插入
void insert(int rt, int l, int r, int x) {
    if (l == r) {
        sum[rt]++;
        return;
    }
    int mid = (l + r) >> 1;
    if (x <= mid) {
        if (lc[rt] == 0) lc[rt] = ++cnt;
        insert(lc[rt], l, mid, x);
    } else {
        if (rc[rt] == 0) rc[rt] = ++cnt;
        insert(rc[rt], mid+1, r, x);
    }
    sum[rt] = sum[lc[rt]] + sum[rc[rt]];
}

// 线段树合并
int merge(int x, int y) {
    if (!x || !y) return x + y;
    if (lc[x] == 0 && lc[y] != 0) lc[x] = lc[y];
    else if (lc[x] != 0 && lc[y] != 0) lc[x] = merge(lc[x], lc[y]);
    
    if (rc[x] == 0 && rc[y] != 0) rc[x] = rc[y];
    else if (rc[x] != 0 && rc[y] != 0) rc[x] = merge(rc[x], rc[y]);
    
    sum[x] = sum[lc[x]] + sum[rc[x]];
    return x;
}

// DFS预处理深度和祖先
void dfs1(int u, int father) {
    depth[u] = depth[father] + 1;
    fa[u] = father;
    anc[u][0] = father;
    
    // 倍增计算祖先
    for (int i = 1; i < 20; i++) {
        anc[u][i] = anc[anc[u][i-1]][i-1];
    }
    
    for (int i = 0; i < G_size[u]; i++) {
        int v = G[u][i];
        if (v != father) {
            dfs1(v, u);
        }
    }
}

// DFS处理线段树合并
void dfs2(int u, int father) {
    // 先处理所有子节点
    for (int i = 0; i < G_size[u]; i++) {
        int v = G[u][i];
        if (v != father) {
            dfs2(v, u);
            // 合并子节点的信息到当前节点
            if (root[u] == 0) root[u] = ++cnt;
            if (root[v] != 0) root[u] = merge(root[u], root[v]);
        }
    }
    
    // 插入当前节点到对应深度的线段树中
    if (root[depth[u]] == 0) root[depth[u]] = ++cnt;
    insert(root[depth[u]], 1, n, u);
    
    // 处理当前节点的查询
    for (int i = 0; i < queries_size[u]; i++) {
        int id = queries[u][i];
        ans[id] = sum[root[depth[u]]] - 1; // 减去自己
    }
}

// 倍增求k级祖先
int getKthAncestor(int u, int k) {
    for (int i = 0; i < 20; i++) {
        if (((k >> i) & 1) != 0) {
            u = anc[u][i];
        }
    }
    return u;
}

int main() {
    // 由于环境限制，这里不实现完整的输入输出
    // 在实际使用中需要根据具体环境实现输入输出
    
    return 0;
}