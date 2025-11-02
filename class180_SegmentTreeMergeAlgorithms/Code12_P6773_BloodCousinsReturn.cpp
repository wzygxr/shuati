#include <iostream>
#include <vector>
#include <cstring>
#include <algorithm>
using namespace std;

/**
 * 题目：P6773 [NOI2020] 命运
 * 测试链接：https://www.luogu.com.cn/problem/P6773
 * 
 * 题目描述：
 * 给定一棵n个节点的树，树上有m条限制条件，每条限制条件(u,v)表示u和v的公共祖先中深度最大的那个节点必须在路径u-v上。
 * 求有多少种给树边染色的方案（每条边染成黑色或白色），使得所有限制条件都满足。
 * 
 * 解题思路：
 * 1. 树形DP + 线段树合并
 * 2. 设dp[u][d]表示以u为根的子树中，从根到u的路径上深度为d的边必须被染成黑色的方案数
 * 3. 使用线段树合并优化DP状态转移
 * 4. 时间复杂度：O(n log n)
 * 
 * 核心思想：
 * - 对于每个限制条件(u,v)，设w=lca(u,v)，那么从w到u和w到v的路径上至少有一条边是黑色
 * - 转化为：对于每个节点u，记录其子树中需要满足的最深的限制条件
 * - 使用线段树维护DP状态，通过合并操作实现高效状态转移
 */

const int N = 500010;
const int MOD = 998244353;
const int LOG = 20;

int n, m;
int h[N], e[N * 2], ne[N * 2], idx;
int fa[N][LOG], depth[N];
vector<int> constraints[N];

// 线段树相关
struct Node {
    int l, r;
    long long sum, mul;
    Node() : l(0), r(0), sum(0), mul(1) {}
} tr[N * 40];

int root[N], cnt;

void add(int a, int b) {
    e[idx] = b;
    ne[idx] = h[a];
    h[a] = idx++;
}

void dfs(int u, int father) {
    depth[u] = depth[father] + 1;
    fa[u][0] = father;
    for (int i = 1; i < LOG; i++) {
        fa[u][i] = fa[fa[u][i - 1]][i - 1];
    }
    for (int i = h[u]; i != -1; i = ne[i]) {
        int j = e[i];
        if (j == father) continue;
        dfs(j, u);
    }
}

int lca(int a, int b) {
    if (depth[a] < depth[b]) swap(a, b);
    for (int i = LOG - 1; i >= 0; i--) {
        if (depth[fa[a][i]] >= depth[b]) {
            a = fa[a][i];
        }
    }
    if (a == b) return a;
    for (int i = LOG - 1; i >= 0; i--) {
        if (fa[a][i] != fa[b][i]) {
            a = fa[a][i];
            b = fa[b][i];
        }
    }
    return fa[a][0];
}

void pushup(int u) {
    tr[u].sum = (tr[tr[u].l].sum + tr[tr[u].r].sum) % MOD;
}

void pushdown(int u, int l, int r) {
    if (tr[u].mul != 1) {
        int mid = (l + r) >> 1;
        if (tr[u].l) {
            tr[tr[u].l].sum = tr[tr[u].l].sum * tr[u].mul % MOD;
            tr[tr[u].l].mul = tr[tr[u].l].mul * tr[u].mul % MOD;
        }
        if (tr[u].r) {
            tr[tr[u].r].sum = tr[tr[u].r].sum * tr[u].mul % MOD;
            tr[tr[u].r].mul = tr[tr[u].r].mul * tr[u].mul % MOD;
        }
        tr[u].mul = 1;
    }
}

void update(int u, int l, int r, int pos, long long val) {
    if (l == r) {
        tr[u].sum = (tr[u].sum + val) % MOD;
        return;
    }
    pushdown(u, l, r);
    int mid = (l + r) >> 1;
    if (pos <= mid) {
        if (!tr[u].l) tr[u].l = ++cnt;
        update(tr[u].l, l, mid, pos, val);
    } else {
        if (!tr[u].r) tr[u].r = ++cnt;
        update(tr[u].r, mid + 1, r, pos, val);
    }
    pushup(u);
}

long long query(int u, int l, int r, int ql, int qr) {
    if (!u || ql > r || qr < l) return 0;
    if (ql <= l && r <= qr) return tr[u].sum;
    pushdown(u, l, r);
    int mid = (l + r) >> 1;
    long long res = 0;
    if (ql <= mid) res = (res + query(tr[u].l, l, mid, ql, qr)) % MOD;
    if (qr > mid) res = (res + query(tr[u].r, mid + 1, r, ql, qr)) % MOD;
    return res;
}

int merge(int u, int v, int l, int r, long long mulu, long long mulv) {
    if (!u && !v) return 0;
    if (!u) {
        tr[v].sum = tr[v].sum * mulv % MOD;
        tr[v].mul = tr[v].mul * mulv % MOD;
        return v;
    }
    if (!v) {
        tr[u].sum = tr[u].sum * mulu % MOD;
        tr[u].mul = tr[u].mul * mulu % MOD;
        return u;
    }
    if (l == r) {
        tr[u].sum = (tr[u].sum * mulu + tr[v].sum * mulv) % MOD;
        return u;
    }
    pushdown(u, l, r);
    pushdown(v, l, r);
    int mid = (l + r) >> 1;
    tr[u].l = merge(tr[u].l, tr[v].l, l, mid, mulu, mulv);
    tr[u].r = merge(tr[u].r, tr[v].r, mid + 1, r, mulu, mulv);
    pushup(u);
    return u;
}

void dfs_dp(int u, int father) {
    // 初始化当前节点的线段树
    root[u] = ++cnt;
    update(root[u], 0, n, 0, 1);
    
    // 处理所有限制条件，找到最深的要求
    int maxd = 0;
    for (int d : constraints[u]) {
        maxd = max(maxd, d);
    }
    
    for (int i = h[u]; i != -1; i = ne[i]) {
        int j = e[i];
        if (j == father) continue;
        
        dfs_dp(j, u);
        
        // 计算子树的贡献
        long long sum = query(root[j], 0, n, 0, depth[u]);
        
        // 合并线段树
        root[u] = merge(root[u], root[j], 0, n, sum, (MOD + 1 - sum) % MOD);
    }
    
    // 处理当前节点的限制条件
    if (maxd > 0) {
        update(root[u], 0, n, maxd, 0);
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    cin >> n;
    
    // 初始化邻接表
    memset(h, -1, sizeof h);
    idx = 0;
    
    // 建树
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        add(u, v);
        add(v, u);
    }
    
    // DFS预处理深度和父节点
    dfs(1, 0);
    
    // 处理限制条件
    cin >> m;
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        int w = lca(u, v);
        constraints[u].push_back(depth[w]);
        constraints[v].push_back(depth[w]);
    }
    
    // 初始化线段树
    cnt = 0;
    
    // 树形DP
    dfs_dp(1, 0);
    
    // 输出结果
    long long ans = query(root[1], 0, n, 0, depth[1]);
    cout << ans << endl;
    
    return 0;
}

/**
 * 类似题目推荐：
 * 1. P5494 【模板】线段树合并 - 线段树合并模板题
 * 2. CF911G Mass Change Queries - 区间赋值问题
 * 3. P4556 [Vani有约会]雨天的尾巴 - 树上差分+线段树合并
 * 4. P3224 [HNOI2012]永无乡 - 并查集+线段树合并
 * 5. P5298 [PKUWC2018]Minimax - 概率DP+线段树合并
 * 
 * 解题技巧总结：
 * 1. 线段树合并常用于优化树形DP，特别是需要维护子树信息的情况
 * 2. 注意线段树合并的时间复杂度是O(n log n)，但需要合理分配内存
 * 3. 对于限制条件，通常转化为对深度的要求
 * 4. 使用懒标记优化区间乘操作
 * 5. C++中注意数组大小和内存管理
 */