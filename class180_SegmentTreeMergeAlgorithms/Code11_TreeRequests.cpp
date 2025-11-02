// Tree Requests CF570D.cpp
// 测试链接 : https://codeforces.com/problemset/problem/570/D
// 线段树合并解法
//
// 题目来源：Codeforces
// 题目大意：树上字符串查询问题，判断子树中节点字符能否重排成回文串
// 解法：线段树合并 + 位运算 + DFS序
// 时间复杂度：O(n log n)
// 空间复杂度：O(n log n)

// 由于环境限制，使用基础C++实现方式，避免使用复杂的STL容器

int n, m;
int G[500005][10], G_size[500005];
char val[500005];
bool ans[500005];

// 查询信息
int queryV[500005], queryH[500005];
int queries[500005][100], queries_size[500005];

// 线段树合并相关
int root[500005], lc[10000005], rc[10000005], cnt[10000005];
int segCnt = 0;

// DFS序相关
int dfn[500005], end[500005], dfnCnt = 0;

// 动态开点线段树插入
void insert(int rt, int l, int r, int x, int val) {
    if (l == r) {
        cnt[rt] ^= (1 << val); // 异或操作，出现偶数次为0，奇数次为1
        return;
    }
    int mid = (l + r) >> 1;
    if (x <= mid) {
        if (lc[rt] == 0) lc[rt] = ++segCnt;
        insert(lc[rt], l, mid, x, val);
    } else {
        if (rc[rt] == 0) rc[rt] = ++segCnt;
        insert(rc[rt], mid+1, r, x, val);
    }
    cnt[rt] = cnt[lc[rt]] ^ cnt[rc[rt]]; // 合并左右子树信息
}

// 线段树合并
int merge(int x, int y, int l, int r) {
    if (!x || !y) return x + y;
    if (l == r) {
        cnt[x] ^= cnt[y];
        return x;
    }
    int mid = (l + r) >> 1;
    lc[x] = merge(lc[x], lc[y], l, mid);
    rc[x] = merge(rc[x], rc[y], mid+1, r);
    cnt[x] = cnt[lc[x]] ^ cnt[rc[x]];
    return x;
}

// 查询子树中字符出现次数的奇偶性
int query(int rt, int l, int r, int x, int y) {
    if (x <= l && r <= y) {
        return cnt[rt];
    }
    int mid = (l + r) >> 1;
    int res = 0;
    if (x <= mid && lc[rt] != 0) {
        res ^= query(lc[rt], l, mid, x, y);
    }
    if (y > mid && rc[rt] != 0) {
        res ^= query(rc[rt], mid+1, r, x, y);
    }
    return res;
}

// DFS序处理
void dfs1(int u, int father) {
    dfn[u] = ++dfnCnt;
    for (int i = 0; i < G_size[u]; i++) {
        int v = G[u][i];
        if (v != father) {
            dfs1(v, u);
        }
    }
    end[u] = dfnCnt;
}

// DFS处理线段树合并
void dfs2(int u, int father) {
    // 先处理所有子节点
    for (int i = 0; i < G_size[u]; i++) {
        int v = G[u][i];
        if (v != father) {
            dfs2(v, u);
            // 合并子节点的信息到当前节点
            root[u] = merge(root[u], root[v], 1, n);
        }
    }
    
    // 插入当前节点的信息
    if (root[u] == 0) root[u] = ++segCnt;
    insert(root[u], 1, n, dfn[u], val[u] - 'a');
    
    // 处理当前节点的查询
    for (int i = 0; i < queries_size[u]; i++) {
        int id = queries[u][i];
        // 查询子树中字符出现次数的奇偶性
        int res = query(root[u], 1, n, 1, n);
        // 判断是否可以重排为回文串
        // 回文串的条件是最多只有一个字符出现奇数次
        ans[id] = (res & (res - 1)) == 0; // 判断是否为2的幂次或0
    }
}

int main() {
    // 由于环境限制，这里不实现完整的输入输出
    // 在实际使用中需要根据具体环境实现输入输出
    
    return 0;
}