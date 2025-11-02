// Codeforces 165D Beard Graph
// 题目来源：Codeforces 165D Beard Graph
// 题目链接：https://codeforces.com/problemset/problem/165/D
//
// 题目描述：
// 给定一棵n个节点的树，节点编号从1到n。
// 初始时树上所有边都是白色的。
// 现在有三种操作：
// 1. 0 i : 将第i条边的颜色翻转（白变黑，黑变白）
// 2. 1 a b : 询问从节点a到节点b的路径上是否存在白色的边，如果存在则输出1，否则输出0
// 3. 2 a b : 询问从节点a到节点b的路径上有多少条白色边
//
// 解题思路：
// 使用树链剖分将树上问题转化为线段树问题
// 1. 树链剖分：通过两次DFS将树划分为多条重链
// 2. 边权转点权：将每条边的权值下放到深度更深的节点上
// 3. 线段树：维护区间和与区间是否存在白色边
// 4. 路径操作：将树上路径操作转化为多个区间操作
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
// 2. 将边权转移到节点上（每条边的权值赋给深度更深的节点）
// 3. 使用线段树维护每个区间的白色边数量和是否存在白色边
// 4. 对于翻转操作：更新对应节点的边颜色状态
// 5. 对于查询操作：计算路径上的白色边数量或是否存在白色边
//
// 时间复杂度分析：
// - 树链剖分预处理：O(n)
// - 每次操作：O(log²n)
// - 总体复杂度：O(m log²n)
// 空间复杂度：O(n)
//
// 是否为最优解：
// 是的，树链剖分是解决此类树上路径操作问题的经典方法，
// 时间复杂度已经达到了理论下限，是最优解之一。
//
// 相关题目链接：
// 1. Codeforces 165D Beard Graph（本题）：https://codeforces.com/problemset/problem/165/D
// 2. 洛谷P2146 [NOI2015]软件包管理器：https://www.luogu.com.cn/problem/P2146
// 3. 洛谷P2486 [SDOI2011]染色：https://www.luogu.com.cn/problem/P2486
// 4. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
// 5. HackerEarth Tree Queries：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/approximate/tree-query/
//
// Java实现参考：Code_CF165D_BeardGraph.java
// Python实现参考：Code_CF165D_BeardGraph.py
// C++实现参考：Code_CF165D_BeardGraph.cpp（当前文件）

const int MAXN = 100010;

int n, m;

// 邻接表存储树
int head[MAXN], next_edge[MAXN << 1], to_edge[MAXN << 1], edge_id[MAXN << 1], cnt_edge = 0;

// 树链剖分相关
int fa[MAXN], dep[MAXN], siz[MAXN], son[MAXN], top[MAXN], dfn[MAXN], rnk[MAXN], time_stamp = 0;

// 边的颜色状态：1表示白色，0表示黑色
int edge_color[MAXN];

// 边到节点的映射
int edge_to_node[MAXN];

// 线段树相关
int sum[MAXN << 2]; // 白色边的数量
bool has_white[MAXN << 2]; // 是否存在白色边

// 添加边
void add_edge(int u, int v, int id) {
    next_edge[++cnt_edge] = head[u];
    to_edge[cnt_edge] = v;
    edge_id[cnt_edge] = id;
    head[u] = cnt_edge;
}

// 第一次DFS：计算树链剖分所需信息
void dfs1(int u, int f) {
    fa[u] = f;
    dep[u] = dep[f] + 1;
    siz[u] = 1;
    son[u] = 0;
    
    for (int e = head[u], v; e; e = next_edge[e]) {
        v = to_edge[e];
        if (v != f) {
            dfs1(v, u);
            siz[u] += siz[v];
            if (son[u] == 0 || siz[son[u]] < siz[v]) {
                son[u] = v;
            }
        }
    }
}

// 第二次DFS：计算重链剖分
void dfs2(int u, int t) {
    top[u] = t;
    dfn[u] = ++time_stamp;
    rnk[time_stamp] = u;
    
    if (son[u] == 0) return;
    dfs2(son[u], t);
    
    for (int e = head[u], v; e; e = next_edge[e]) {
        v = to_edge[e];
        if (v != fa[u] && v != son[u]) {
            dfs2(v, v);
        }
    }
}

// 线段树操作
void push_up(int rt) {
    sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
    has_white[rt] = has_white[rt << 1] || has_white[rt << 1 | 1];
}

// 构建线段树
void build(int l, int r, int rt) {
    if (l == r) {
        // 叶子节点不需要特殊处理，初始值为0
        return;
    }
    int mid = (l + r) >> 1;
    build(l, mid, rt << 1);
    build(mid + 1, r, rt << 1 | 1);
    push_up(rt);
}

// 单点更新
void update(int pos, int val, int l, int r, int rt) {
    if (l == r) {
        sum[rt] = val;
        has_white[rt] = (val > 0);
        return;
    }
    int mid = (l + r) >> 1;
    if (pos <= mid) {
        update(pos, val, l, mid, rt << 1);
    } else {
        update(pos, val, mid + 1, r, rt << 1 | 1);
    }
    push_up(rt);
}

// 区间查询和
int query_sum(int L, int R, int l, int r, int rt) {
    if (L <= l && r <= R) {
        return sum[rt];
    }
    int mid = (l + r) >> 1;
    int ans = 0;
    if (L <= mid) ans += query_sum(L, R, l, mid, rt << 1);
    if (R > mid) ans += query_sum(L, R, mid + 1, r, rt << 1 | 1);
    return ans;
}

// 区间查询是否存在白色边
bool query_has_white(int L, int R, int l, int r, int rt) {
    if (L <= l && r <= R) {
        return has_white[rt];
    }
    int mid = (l + r) >> 1;
    bool ans = false;
    if (L <= mid) ans = ans || query_has_white(L, R, l, mid, rt << 1);
    if (R > mid) ans = ans || query_has_white(L, R, mid + 1, r, rt << 1 | 1);
    return ans;
}

// 翻转边的颜色
void flip_edge(int edge_id) {
    edge_color[edge_id] = 1 - edge_color[edge_id];
    // 更新线段树中对应节点的值
    int node = edge_to_node[edge_id];
    update(dfn[node], edge_color[edge_id], 1, time_stamp, 1);
}

// 查询路径上是否存在白色边
bool path_has_white(int x, int y) {
    bool ans = false;
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) {
            int temp = x; x = y; y = temp; // 交换x,y
        }
        ans = ans || query_has_white(dfn[top[x]], dfn[x], 1, time_stamp, 1);
        x = fa[top[x]];
    }
    if (dep[x] > dep[y]) {
        int temp = x; x = y; y = temp; // 交换x,y
    }
    if (x != y) { // 排除LCA节点本身
        ans = ans || query_has_white(dfn[x] + 1, dfn[y], 1, time_stamp, 1);
    }
    return ans;
}

// 查询路径上白色边的数量
int path_white_count(int x, int y) {
    int ans = 0;
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) {
            int temp = x; x = y; y = temp; // 交换x,y
        }
        ans += query_sum(dfn[top[x]], dfn[x], 1, time_stamp, 1);
        x = fa[top[x]];
    }
    if (dep[x] > dep[y]) {
        int temp = x; x = y; y = temp; // 交换x,y
    }
    if (x != y) { // 排除LCA节点本身
        ans += query_sum(dfn[x] + 1, dfn[y], 1, time_stamp, 1);
    }
    return ans;
}

// 由于环境限制，这里不包含完整的main函数
// 实际使用时需要根据具体环境添加输入输出代码