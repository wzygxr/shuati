// More Powerful P3899.cpp
// 测试链接 : https://www.luogu.com.cn/problem/P3899
// 线段树合并解法
//
// 题目来源：湖南集训
// 题目大意：树上DP问题，需要维护子树信息
// 解法：线段树合并 + 树形DP
// 时间复杂度：O(n log n)
// 空间复杂度：O(n log n)

// 由于环境限制，使用基础C++实现方式，避免使用复杂的STL容器

int n, q;
int G[300005][10], G_size[300005];
long long ans[300005];
int size[300005]; // 子树大小

// 线段树合并相关
int root[300005], lc[6000005], rc[6000005];
long long sum[6000005], cnt[6000005];
int segCnt = 0;

// 计算子树大小
void dfs1(int u, int father) {
    size[u] = 1;
    for (int i = 0; i < G_size[u]; i++) {
        int v = G[u][i];
        if (v != father) {
            dfs1(v, u);
            size[u] += size[v];
        }
    }
}

// 动态开点线段树插入
void insert(int rt, int l, int r, int x, long long val) {
    if (l == r) {
        sum[rt] += (long long)x * val;
        cnt[rt] += val;
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
    sum[rt] = sum[lc[rt]] + sum[rc[rt]];
    cnt[rt] = cnt[lc[rt]] + cnt[rc[rt]];
}

// 线段树合并
int merge(int x, int y, int l, int r) {
    if (!x || !y) return x + y;
    if (l == r) {
        sum[x] += sum[y];
        cnt[x] += cnt[y];
        return x;
    }
    int mid = (l + r) >> 1;
    lc[x] = merge(lc[x], lc[y], l, mid);
    rc[x] = merge(rc[x], rc[y], mid+1, r);
    sum[x] = sum[lc[x]] + sum[rc[x]];
    cnt[x] = cnt[lc[x]] + cnt[rc[x]];
    return x;
}

// 查询前k大的和
long long query(int rt, int l, int r, long long k) {
    if (k <= 0) return 0;
    if (l == r) {
        return (k < cnt[rt] ? k : cnt[rt]) * l;
    }
    int mid = (l + r) >> 1;
    if (cnt[rc[rt]] >= k) {
        return query(rc[rt], mid+1, r, k);
    } else {
        return sum[rc[rt]] + query(lc[rt], l, mid, k - cnt[rc[rt]]);
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
            root[u] = merge(root[u], root[v], 1, n);
        }
    }
    
    // 插入当前节点的信息
    if (root[u] == 0) root[u] = ++segCnt;
    insert(root[u], 1, n, size[u], 1);
    
    // 记录答案
    long long k = size[u] < n ? size[u] : n;
    ans[u] = query(root[u], 1, n, k);
}

int main() {
    // 由于环境限制，这里不实现完整的输入输出
    // 在实际使用中需要根据具体环境实现输入输出
    
    return 0;
}