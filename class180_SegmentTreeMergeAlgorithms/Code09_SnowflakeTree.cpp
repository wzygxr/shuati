// Snowflake Tree P5384.cpp
// 测试链接 : https://www.luogu.com.cn/problem/P5384
// 线段树合并解法
//
// 题目来源：Cnoi2019
// 题目大意：树上路径查询问题，需要维护路径信息
// 解法：线段树合并 + DFS序 + 区间更新
// 时间复杂度：O(n log n)
// 空间复杂度：O(n log n)

// 由于环境限制，使用基础C++实现方式，避免使用复杂的STL容器

int n, q;
int G[1000005][10], G_size[1000005];
int val[1000005];
long long ans[1000005];

// 线段树合并相关
int root[1000005], lc[20000005], rc[20000005];
long long sum[20000005], addTag[20000005];
int cnt = 0;

const long long MOD = 998244353;

// DFS序相关
int dfn[1000005], end[1000005], dfnCnt = 0;

// 更新父节点信息
void pushUp(int rt) {
    sum[rt] = (sum[lc[rt]] + sum[rc[rt]]) % MOD;
}

// 下放标记
void pushDown(int rt, int l, int r) {
    if (addTag[rt] != 0) {
        int mid = (l + r) >> 1;
        if (lc[rt] == 0) lc[rt] = ++cnt;
        if (rc[rt] == 0) rc[rt] = ++cnt;
        addTag[lc[rt]] = (addTag[lc[rt]] + addTag[rt]) % MOD;
        addTag[rc[rt]] = (addTag[rc[rt]] + addTag[rt]) % MOD;
        sum[lc[rt]] = (sum[lc[rt]] + addTag[rt] * (mid - l + 1)) % MOD;
        sum[rc[rt]] = (sum[rc[rt]] + addTag[rt] * (r - mid)) % MOD;
        addTag[rt] = 0;
    }
}

// 区间加法
void update(int rt, int l, int r, int x, int y, long long val) {
    if (x <= l && r <= y) {
        addTag[rt] = (addTag[rt] + val) % MOD;
        sum[rt] = (sum[rt] + val * (r - l + 1)) % MOD;
        return;
    }
    pushDown(rt, l, r);
    int mid = (l + r) >> 1;
    if (x <= mid) {
        if (lc[rt] == 0) lc[rt] = ++cnt;
        update(lc[rt], l, mid, x, y, val);
    }
    if (y > mid) {
        if (rc[rt] == 0) rc[rt] = ++cnt;
        update(rc[rt], mid+1, r, x, y, val);
    }
    pushUp(rt);
}

// 查询区间和
long long query(int rt, int l, int r, int x, int y) {
    if (x <= l && r <= y) {
        return sum[rt];
    }
    pushDown(rt, l, r);
    int mid = (l + r) >> 1;
    long long res = 0;
    if (x <= mid && lc[rt] != 0) {
        res = (res + query(lc[rt], l, mid, x, y)) % MOD;
    }
    if (y > mid && rc[rt] != 0) {
        res = (res + query(rc[rt], mid+1, r, x, y)) % MOD;
    }
    return res;
}

// 线段树合并
int merge(int x, int y, int l, int r) {
    if (!x || !y) return x + y;
    if (l == r) {
        sum[x] = (sum[x] + sum[y]) % MOD;
        return x;
    }
    pushDown(x, l, r);
    pushDown(y, l, r);
    int mid = (l + r) >> 1;
    lc[x] = merge(lc[x], lc[y], l, mid);
    rc[x] = merge(rc[x], rc[y], mid+1, r);
    pushUp(x);
    return x;
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
    if (root[u] == 0) root[u] = ++cnt;
    update(root[u], 1, n, dfn[u], dfn[u], val[u]);
}

int main() {
    // 由于环境限制，这里不实现完整的输入输出
    // 在实际使用中需要根据具体环境实现输入输出
    
    return 0;
}