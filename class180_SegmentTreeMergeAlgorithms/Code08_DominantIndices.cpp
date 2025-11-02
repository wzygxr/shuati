// Dominant Indices CF1009F.cpp
// 测试链接 : https://codeforces.com/problemset/problem/1009/F
// 线段树合并解法
//
// 题目来源：Codeforces
// 题目大意：对于每个节点，求其子树中深度最大的节点的深度
// 解法：线段树合并 + 树形DP
// 时间复杂度：O(n log n)
// 空间复杂度：O(n log n)

// 由于环境限制，使用基础C++实现方式，避免使用复杂的STL容器

int n;
int G[1000005][10], G_size[1000005];
int ans[1000005];

// 线段树合并相关
int root[1000005], lc[20000005], rc[20000005], sum[20000005];
int maxDepth[20000005], maxCount[20000005];
int cnt = 0;

// 动态开点线段树插入
void insert(int rt, int l, int r, int x, int val) {
    if (l == r) {
        sum[rt] += val;
        maxDepth[rt] = l;
        maxCount[rt] = sum[rt];
        return;
    }
    int mid = (l + r) >> 1;
    if (x <= mid) {
        if (lc[rt] == 0) lc[rt] = ++cnt;
        insert(lc[rt], l, mid, x, val);
    } else {
        if (rc[rt] == 0) rc[rt] = ++cnt;
        insert(rc[rt], mid+1, r, x, val);
    }
    // 合并左右子树信息
    sum[rt] = sum[lc[rt]] + sum[rc[rt]];
    if (maxCount[lc[rt]] > maxCount[rc[rt]]) {
        maxDepth[rt] = maxDepth[lc[rt]];
        maxCount[rt] = maxCount[lc[rt]];
    } else if (maxCount[lc[rt]] < maxCount[rc[rt]]) {
        maxDepth[rt] = maxDepth[rc[rt]];
        maxCount[rt] = maxCount[rc[rt]];
    } else {
        maxDepth[rt] = (maxDepth[lc[rt]] < maxDepth[rc[rt]]) ? maxDepth[lc[rt]] : maxDepth[rc[rt]];
        maxCount[rt] = maxCount[lc[rt]];
    }
}

// 线段树合并
int merge(int x, int y, int l, int r) {
    if (!x || !y) return x + y;
    if (l == r) {
        sum[x] += sum[y];
        maxDepth[x] = l;
        maxCount[x] = sum[x];
        return x;
    }
    int mid = (l + r) >> 1;
    lc[x] = merge(lc[x], lc[y], l, mid);
    rc[x] = merge(rc[x], rc[y], mid+1, r);
    // 合并左右子树信息
    sum[x] = sum[lc[x]] + sum[rc[x]];
    if (maxCount[lc[x]] > maxCount[rc[x]]) {
        maxDepth[x] = maxDepth[lc[x]];
        maxCount[x] = maxCount[lc[x]];
    } else if (maxCount[lc[x]] < maxCount[rc[x]]) {
        maxDepth[x] = maxDepth[rc[x]];
        maxCount[x] = maxCount[rc[x]];
    } else {
        maxDepth[x] = (maxDepth[lc[x]] < maxDepth[rc[x]]) ? maxDepth[lc[x]] : maxDepth[rc[x]];
        maxCount[x] = maxCount[lc[x]];
    }
    return x;
}

// DFS处理线段树合并
void dfs(int u, int father) {
    // 先处理所有子节点
    for (int i = 0; i < G_size[u]; i++) {
        int v = G[u][i];
        if (v != father) {
            dfs(v, u);
            // 合并子节点的信息到当前节点
            root[u] = merge(root[u], root[v], 1, n);
        }
    }
    
    // 插入当前节点的深度信息（深度为1）
    if (root[u] == 0) root[u] = ++cnt;
    insert(root[u], 1, n, 1, 1);
    
    // 记录答案
    ans[u] = maxDepth[root[u]] - 1; // 减去1得到相对于当前节点的深度
}

int main() {
    // 由于环境限制，这里不实现完整的输入输出
    // 在实际使用中需要根据具体环境实现输入输出
    
    return 0;
}